import { api } from '../api.js';

export function initCargoModule() {
  const slider = document.getElementById('capacity-slider');
  const valBadge = document.getElementById('capacity-val');

  slider?.addEventListener('input', (e) => {
    valBadge.innerText = `${parseFloat(e.target.value).toFixed(1)} kg`;
  });

  document.getElementById('btn-run-knapsack')?.addEventListener('click', onOptimizeCargo);
}

async function onOptimizeCargo() {
  const cap = document.getElementById('capacity-slider').value;
  try {
    const data = await api.optimizeKnapsack(cap);

    const resultBox = document.getElementById('knapsack-result-box');
    resultBox.classList.remove('hidden');

    document.getElementById('knapsack-value').innerText = `${data.totalValue.toFixed(1)} points`;
    document.getElementById('knapsack-weight').innerText = `${data.totalWeight.toFixed(1)} kg / ${data.capacity.toFixed(1)} kg`;
    document.getElementById('knapsack-time').innerText = `${(data.executionTimeNs / 1000000).toFixed(4)} ms (${data.executionTimeNs} ns)`;

    const cargoGrid = document.getElementById('cargo-grid');
    cargoGrid.innerHTML = '';

    data.selectedItems.forEach(item => {
      const card = document.createElement('div');
      card.className = 'cargo-item';
      card.innerHTML = `
        <strong>${item.name}</strong><br>
        <span style="color: #94a3b8;">Weight: ${item.weight} kg | Clinical Score: ${item.value} pts</span>
      `;
      cargoGrid.appendChild(card);
    });
  } catch (err) {
    console.error('Error running payload optimization:', err);
  }
}
