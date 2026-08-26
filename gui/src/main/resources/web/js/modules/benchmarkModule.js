import { api } from '../api.js';

let chartInstance = null;
let lastBenchmarkData = null;

export function initBenchmarkModule() {
  document.getElementById('btn-run-benchmark')?.addEventListener('click', onRunBenchmark);

  window.addEventListener('themeChanged', () => {
    if (lastBenchmarkData) {
      renderChart(lastBenchmarkData.algo, lastBenchmarkData.scales, lastBenchmarkData.avgTimes);
    }
  });
}

async function onRunBenchmark() {
  const algo = document.getElementById('benchmark-algo-select').value;
  try {
    const data = await api.runBenchmark(algo);

    const scales = [...new Set(data.runs.map(r => r.scale))];
    const avgTimes = scales.map(s => {
      const runsForScale = data.runs.filter(r => r.scale === s);
      const sum = runsForScale.reduce((acc, curr) => acc + curr.timeMs, 0);
      return (sum / runsForScale.length).toFixed(4);
    });

    lastBenchmarkData = { algo, scales, avgTimes, runs: data.runs };
    renderChart(algo, scales, avgTimes);
    renderBenchmarkTable(algo, scales, data.runs);
  } catch (err) {
    console.error('Error running benchmark:', err);
  }
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

function renderBenchmarkTable(algoName, scales, runs) {
  const tableCard = document.getElementById('benchmark-results-table-card');
  const tbody = document.getElementById('benchmark-table-body');
  if (!tableCard || !tbody) return;

  tableCard.classList.remove('hidden');
  tbody.innerHTML = '';

  const bigO = getBigOComplexity(algoName);

  scales.forEach(s => {
    const scaleRuns = runs.filter(r => r.scale === s);
    const r1 = scaleRuns[0] ? scaleRuns[0].timeMs.toFixed(4) : '-';
    const r2 = scaleRuns[1] ? scaleRuns[1].timeMs.toFixed(4) : '-';
    const r3 = scaleRuns[2] ? scaleRuns[2].timeMs.toFixed(4) : '-';
    const sum = scaleRuns.reduce((acc, curr) => acc + curr.timeMs, 0);
    const mean = (sum / (scaleRuns.length || 1)).toFixed(4);
    const maxMem = Math.max(...scaleRuns.map(r => r.memoryKb)).toFixed(1);

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td><strong>N = ${s.toLocaleString()}</strong></td>
      <td>${r1}</td>
      <td>${r2}</td>
      <td>${r3}</td>
      <td style="color: var(--accent-primary); font-weight: 700;">${mean}</td>
      <td>${maxMem}</td>
      <td><span class="complexity-badge">${bigO}</span></td>
    `;
    tbody.appendChild(tr);
  });
}

function renderChart(algoName, labels, dataPoints) {
  const ctx = document.getElementById('benchmarkChart').getContext('2d');
  if (chartInstance) {
    chartInstance.destroy();
  }

  const isLight = document.documentElement.getAttribute('data-theme') === 'light';
  const gridColor = isLight ? '#e2e8f0' : '#1e293b';
  const textColor = isLight ? '#475569' : '#94a3b8';
  const tickColor = isLight ? '#0f172a' : '#f8fafc';
  const primaryColor = isLight ? '#ea580c' : '#e07a5f';

  chartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels.map(l => `N = ${l.toLocaleString()}`),
      datasets: [{
        label: `${algoName} Mean Latency (${getBigOComplexity(algoName)})`,
        data: dataPoints,
        borderColor: primaryColor,
        backgroundColor: isLight ? 'rgba(234, 88, 12, 0.08)' : 'rgba(224, 122, 95, 0.12)',
        borderWidth: 3,
        tension: 0.3,
        fill: true,
        pointBackgroundColor: primaryColor,
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 7
      }]
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
          labels: { color: tickColor, font: { family: 'Plus Jakarta Sans', size: 13, weight: '700' } }
        },
        tooltip: {
          callbacks: {
            label: function(context) {
              return ` ${context.parsed.y} ms (${getBigOComplexity(algoName)})`;
            }
          }
        }
      }
    }
  });
}
