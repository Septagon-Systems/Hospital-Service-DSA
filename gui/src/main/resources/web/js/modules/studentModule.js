import { api } from '../api.js';

export function initStudentModule() {
  document.getElementById('btn-calc-student')?.addEventListener('click', () => onCalculate(false));
  document.getElementById('btn-apply-student')?.addEventListener('click', () => onCalculate(true));
}

async function onCalculate(apply = false) {
  const idStr = document.getElementById('input-student-id').value.trim();
  if (!idStr) return;

  try {
    const data = await api.calculateStudentParams(idStr, apply);

    const resultBox = document.getElementById('student-result-box');
    resultBox.classList.remove('hidden');

    document.getElementById('sp-id').innerText = data.indexNumber;
    document.getElementById('sp-priority').innerText = data.priorityWeight;
    document.getElementById('sp-hash').innerText = `${data.hashTableSize} (Prime)`;
    document.getElementById('sp-seed').innerText = data.randomSeed;
    document.getElementById('sp-penalty').innerText = `${data.routePenalty.toFixed(2)}x`;
    document.getElementById('sp-urgent').innerText = `${data.urgentCapacity} slots`;
    document.getElementById('sp-normal').innerText = `${data.normalCapacity} slots`;
    document.getElementById('sp-preempt').innerText = `${data.preemptionCapacity} slots`;

    if (apply) {
      window.dispatchEvent(new CustomEvent('dispatch-refresh'));
      alert(`Applied student-derived parameters for Index ${data.indexNumber}!\n• Urgent PQ: ${data.urgentCapacity} slots\n• Normal Queue: ${data.normalCapacity} slots\n• Preemption Deque: ${data.preemptionCapacity} slots\nShift queues re-initialized.`);
    }
  } catch (err) {
    console.error('Error calculating student parameters:', err);
  }
}
