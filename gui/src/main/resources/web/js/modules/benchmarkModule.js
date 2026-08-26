import { api } from '../api.js';

let singleChartInstance = null;
let quadrantInstances = {};
let lastBenchmarkData = null;

export function initBenchmarkModule() {
  document.getElementById('btn-run-benchmark')?.addEventListener('click', onRunBenchmark);

  window.addEventListener('themeChanged', () => {
    if (lastBenchmarkData) {
      renderBenchmarkView(lastBenchmarkData);
    }
  });
}

async function onRunBenchmark() {
  const algoSelect = document.getElementById('benchmark-algo-select');
  const btn = document.getElementById('btn-run-benchmark');
  const loadingBanner = document.getElementById('benchmark-loading-banner');
  const loadingText = document.getElementById('benchmark-loading-text');

  const algo = algoSelect ? algoSelect.value : 'SearchComparison';

  if (btn) {
    btn.disabled = true;
    btn.innerHTML = `<span class="spinner" style="width:13px;height:13px;border-width:2px;vertical-align:middle;margin-right:6px;"></span> Running...`;
  }
  if (loadingBanner) {
    loadingBanner.style.display = 'flex';
    if (loadingText) loadingText.innerText = `Executing empirical benchmark for ${algo} across scales...`;
  }

  try {
    const data = await api.runBenchmark(algo);
    lastBenchmarkData = data;
    renderBenchmarkView(data);
    renderBenchmarkTable(data);
  } catch (err) {
    console.error('Error running benchmark:', err);
    alert('Benchmark execution failed: ' + err.message);
  } finally {
    if (btn) {
      btn.disabled = false;
      btn.innerText = 'Execute Benchmark Suite';
    }
    if (loadingBanner) {
      loadingBanner.style.display = 'none';
    }
  }
}

function renderBenchmarkView(data) {
  const singleContainer = document.getElementById('single-chart-container');
  const quadrantsContainer = document.getElementById('search-quadrants-container');

  if (data.mode === 'search_quadrants') {
    if (singleContainer) singleContainer.classList.add('hidden');
    if (quadrantsContainer) quadrantsContainer.classList.remove('hidden');
    renderSearchQuadrants(data.scales, data.quadrants);
  } else {
    if (quadrantsContainer) quadrantsContainer.classList.add('hidden');
    if (singleContainer) singleContainer.classList.remove('hidden');
    renderSingleChart(data.algorithm, data.scales, data.series);
  }
}

function renderSearchQuadrants(scales, quadrants) {
  const isLight = document.documentElement.getAttribute('data-theme') === 'light';
  const gridColor = isLight ? '#e2e8f0' : '#1e293b';
  const textColor = isLight ? '#475569' : '#94a3b8';
  const tickColor = isLight ? '#0f172a' : '#f8fafc';

  const chartIds = ['chartQ1', 'chartQ2', 'chartQ3', 'chartQ4'];
  const labels = scales.map(s => `N = ${s.toLocaleString()}`);

  quadrants.forEach((q, idx) => {
    const canvasId = chartIds[idx];
    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    if (quadrantInstances[canvasId]) {
      quadrantInstances[canvasId].destroy();
    }

    quadrantInstances[canvasId] = new Chart(canvas.getContext('2d'), {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'LinearSearch (O(N))',
            data: q.linear,
            borderColor: '#ef4444',
            backgroundColor: 'transparent',
            borderWidth: 2.5,
            tension: 0.2,
            pointBackgroundColor: '#ef4444',
            pointRadius: 4
          },
          {
            label: 'BinarySearch (O(log N))',
            data: q.binary,
            borderColor: '#10b981',
            backgroundColor: 'transparent',
            borderWidth: 2.5,
            tension: 0.2,
            pointBackgroundColor: '#10b981',
            pointRadius: 4
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            grid: { color: gridColor },
            ticks: { color: tickColor, font: { family: 'JetBrains Mono', size: 11 } }
          },
          y: {
            title: { display: true, text: `Time (${q.unit})`, color: textColor, font: { size: 11, weight: '600' } },
            grid: { color: gridColor },
            ticks: { color: tickColor, font: { family: 'JetBrains Mono', size: 11 } }
          }
        },
        plugins: {
          legend: {
            labels: { color: tickColor, font: { family: 'Plus Jakarta Sans', size: 11, weight: '700' } }
          },
          tooltip: {
            callbacks: {
              label: (ctx) => ` ${ctx.dataset.label}: ${ctx.parsed.y} ${q.unit}`
            }
          }
        }
      }
    });
  });
}

function renderSingleChart(algoName, scales, seriesList) {
  const canvas = document.getElementById('benchmarkChart');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');

  if (singleChartInstance) {
    singleChartInstance.destroy();
  }

  const isLight = document.documentElement.getAttribute('data-theme') === 'light';
  const gridColor = isLight ? '#e2e8f0' : '#1e293b';
  const textColor = isLight ? '#475569' : '#94a3b8';
  const tickColor = isLight ? '#0f172a' : '#f8fafc';
  const primaryColor = isLight ? '#ea580c' : '#e07a5f';

  const datasets = (seriesList && seriesList.length > 0) ? seriesList.map(s => ({
    label: s.name,
    data: s.data,
    borderColor: s.color || primaryColor,
    backgroundColor: isLight ? 'rgba(234, 88, 12, 0.08)' : 'rgba(224, 122, 95, 0.12)',
    borderWidth: 3,
    tension: 0.3,
    fill: true,
    pointBackgroundColor: s.color || primaryColor,
    pointBorderColor: '#ffffff',
    pointBorderWidth: 2,
    pointRadius: 5,
    pointHoverRadius: 7
  })) : [];

  singleChartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: scales.map(l => `N = ${l.toLocaleString()}`),
      datasets: datasets
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        x: {
          title: { display: true, text: 'Input Scale (N)', color: textColor, font: { family: 'Plus Jakarta Sans', weight: '600' } },
          grid: { color: gridColor },
          ticks: { color: tickColor, font: { family: 'JetBrains Mono' } }
        },
        y: {
          title: { display: true, text: 'Mean Execution Time (ms)', color: textColor, font: { family: 'Plus Jakarta Sans', weight: '600' } },
          grid: { color: gridColor },
          ticks: { color: tickColor, font: { family: 'JetBrains Mono' } }
        }
      },
      plugins: {
        legend: {
          labels: { color: tickColor, font: { family: 'Plus Jakarta Sans', size: 12, weight: '700' } }
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return ` ${context.dataset.label}: ${context.parsed.y} ms`;
            }
          }
        }
      }
    }
  });
}

function getBigOComplexity(algoName) {
  if (algoName.includes('QuickSort') || algoName.includes('MergeSort')) return 'O(N log N)';
  if (algoName.includes('InsertionSort') || algoName.includes('SelectionSort')) return 'O(N²)';
  if (algoName.includes('BinarySearch') || algoName.includes('RedBlackTree') || algoName.includes('BTree')) return 'O(log N)';
  if (algoName.includes('CustomHashTable-Lookup')) return 'O(1)';
  if (algoName.includes('CustomHashTable-PutGet')) return 'O(N)';
  if (algoName.includes('LinearSearch') || algoName.includes('DynamicArray') || algoName.includes('LinkedList')) return 'O(N)';
  if (algoName.includes('Dijkstra')) return 'O(E + V log V)';
  if (algoName.includes('Kruskal')) return 'O(E log E)';
  return 'O(N)';
}

function renderBenchmarkTable(data) {
  const tableCard = document.getElementById('benchmark-results-table-card');
  const tbody = document.getElementById('benchmark-table-body');
  if (!tableCard || !tbody) return;

  tableCard.classList.remove('hidden');
  tbody.innerHTML = '';

  const bigO = getBigOComplexity(data.algorithm);

  if (data.mode === 'search_quadrants') {
    data.scales.forEach((s, sIdx) => {
      data.quadrants.forEach(q => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td><strong>N = ${s.toLocaleString()}</strong></td>
          <td style="font-weight: 600;">${q.title}</td>
          <td style="color: #ef4444;">Linear: ${q.linear[sIdx]} ${q.unit}</td>
          <td style="color: #10b981;">Binary: ${q.binary[sIdx]} ${q.unit}</td>
          <td style="color: var(--accent-primary); font-weight: 700;">${q.binary[sIdx]} ${q.unit}</td>
          <td>-</td>
          <td><span class="complexity-badge">O(log N) vs O(N)</span></td>
        `;
        tbody.appendChild(tr);
      });
    });
  } else {
    data.scales.forEach(s => {
      const scaleRuns = (data.runs || []).filter(r => r.scale === s);
      const r1 = scaleRuns[0] ? scaleRuns[0].timeMs.toFixed(4) : '-';
      const r2 = scaleRuns[1] ? scaleRuns[1].timeMs.toFixed(4) : '-';
      const r3 = scaleRuns[2] ? scaleRuns[2].timeMs.toFixed(4) : '-';
      const sum = scaleRuns.reduce((acc, curr) => acc + curr.timeMs, 0);
      const mean = (sum / (scaleRuns.length || 1)).toFixed(4);
      const maxMem = Math.max(...scaleRuns.map(r => r.memoryKb)).toFixed(1);

      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td><strong>N = ${s.toLocaleString()}</strong></td>
        <td>${r1} ms</td>
        <td>${r2} ms</td>
        <td>${r3} ms</td>
        <td style="color: var(--accent-primary); font-weight: 700;">${mean} ms</td>
        <td>${maxMem} KB</td>
        <td><span class="complexity-badge">${bigO}</span></td>
      `;
      tbody.appendChild(tr);
    });
  }
}
