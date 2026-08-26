import { api } from '../api.js';

export function initSearchModule() {
  document.getElementById('btn-run-search')?.addEventListener('click', onSearch);
}

async function onSearch() {
  const id = document.getElementById('search-hospital-select').value;
  try {
    const data = await api.searchHospital(id);

    const container = document.getElementById('search-result-container');
    container.classList.remove('hidden');

    const h = data.hospital;
    document.getElementById('facility-profile').innerHTML = `
      <h3 style="color: #e07a5f; margin-bottom: 4px; font-size: 16px;">[${h.id}] ${h.name}</h3>
      <p style="color: #94a3b8; font-size: 13.5px;">${h.region} · ${h.town} | <strong>Class:</strong> ${h.type} | <strong>ICU Beds:</strong> ${h.icu} | <strong>Ambulances:</strong> ${h.ambulances}</p>
    `;

    document.getElementById('time-hash').innerText = `${data.timings.hashNs} ns`;
    document.getElementById('time-rb').innerText = `${data.timings.rbNs} ns`;
    document.getElementById('time-bst').innerText = `${data.timings.bstNs} ns`;
    document.getElementById('time-btree').innerText = `${data.timings.bTreeNs} ns`;
    document.getElementById('time-binary').innerText = `${data.timings.binaryNs} ns`;
    document.getElementById('time-linear').innerText = `${data.timings.linearNs} ns`;
  } catch (err) {
    console.error('Error executing search benchmark:', err);
  }
}
