import { api } from '../api.js';
import { drawDijkstraRoute } from './mapModule.js';

export function initDispatchModule() {
  loadStatus();
  loadQueuesState();
  bindEvents();
  initTelemetryListeners();
  
  // Initial telemetry load
  setTimeout(updateActiveTelemetry, 300);

  window.addEventListener('dispatch-refresh', () => {
    loadStatus();
    loadQueuesState();
    updateActiveTelemetry();
  });
}

function initTelemetryListeners() {
  const originSelect = document.getElementById('select-origin');
  const destSelect = document.getElementById('select-dest');
  const slider = document.getElementById('capacity-slider');
  const sliderVal = document.getElementById('capacity-val');

  originSelect?.addEventListener('change', updateActiveTelemetry);
  destSelect?.addEventListener('change', updateActiveTelemetry);
  
  slider?.addEventListener('input', (e) => {
    if (sliderVal) sliderVal.innerText = `${parseFloat(e.target.value).toFixed(1)} kg`;
    updateActiveTelemetry();
  });
}

async function updateActiveTelemetry() {
  const originSelect = document.getElementById('select-origin');
  const destSelect = document.getElementById('select-dest');
  const slider = document.getElementById('capacity-slider');

  const originId = originSelect ? originSelect.value : '1';
  const destId = destSelect ? destSelect.value : '16';
  const capacity = slider ? parseFloat(slider.value) : 35.0;

  // 1. Live Dijkstra Route on Leaflet Map
  drawDijkstraRoute(originId, destId);

  // 2. Multi-Structure Index Lookup Telemetry
  try {
    const searchData = await api.searchFacility(originId);
    const latencyEl = document.getElementById('route-latency');
    if (latencyEl && searchData.timings) {
      latencyEl.innerText = `HashTable: ${searchData.timings.CustomHashTable} · BST: ${searchData.timings.BinarySearchTree} · RBTree: ${searchData.timings.RedBlackTree}`;
    }
  } catch (e) {}

  // 3. Knapsack DP Cargo Packaging
  try {
    const cargoData = await api.runKnapsack(capacity);
    const badge = document.getElementById('knapsack-value-badge');
    if (badge) badge.innerText = `Total Clinical Value: ${cargoData.totalValue.toFixed(1)} pts (${cargoData.totalWeight.toFixed(1)} kg)`;
    
    const timeEl = document.getElementById('knapsack-time');
    if (timeEl) timeEl.innerText = `${(cargoData.executionTimeNs / 1000).toFixed(2)} µs (${cargoData.executionTimeNs} ns)`;

    const grid = document.getElementById('cargo-grid');
    if (grid) {
      grid.innerHTML = '';
      cargoData.selectedItems.forEach(item => {
        const card = document.createElement('div');
        card.className = 'cargo-item';
        card.innerHTML = `
          <div style="font-weight: 700; color: var(--text-primary); margin-bottom: 2px;">${item.name}</div>
          <div style="display: flex; justify-content: space-between; color: var(--text-muted); font-size: 11.5px;">
            <span>Weight: <b>${item.weight.toFixed(1)} kg</b></span>
            <span>Utility: <b style="color: var(--accent-green);">${item.value.toFixed(0)} pts</b></span>
          </div>
        `;
        grid.appendChild(card);
      });
    }
  } catch (e) {}
}

async function loadStatus() {
  try {
    const data = await api.getDispatchStatus();
    updateMetrics(data);
  } catch (err) {
    console.error('Error fetching dispatch status:', err);
  }
}

async function loadQueuesState() {
  try {
    const data = await api.getDispatchQueues();
    renderQueueList('list-urgent-pq', data.urgentQueue, 'Urgent PQ');
    renderQueueList('list-normal-queue', data.normalQueue, 'Normal FIFO');
    renderStackList('list-undo-stack', data.undoStack);
  } catch (err) {
    console.error('Error fetching queue state:', err);
  }
}

function updateMetrics(data) {
  document.getElementById('stat-urgent').innerText = data.urgent;
  document.getElementById('stat-preempt').innerText = data.preemption;
  document.getElementById('stat-normal').innerText = data.normal;
  document.getElementById('stat-served').innerText = data.served;
  document.getElementById('undo-depth-badge').innerText = `Undo Stack Depth: ${data.undoDepth}`;
}

function bindEvents() {
  document.getElementById('btn-serve-single')?.addEventListener('click', onServeSingle);
  document.getElementById('btn-repopulate-shift')?.addEventListener('click', onRepopulateShift);
  document.getElementById('btn-undo')?.addEventListener('click', onUndo);
  document.getElementById('btn-admit-mission')?.addEventListener('click', onAdmitMission);
}

async function onAdmitMission() {
  const originSelect = document.getElementById('select-origin');
  const destSelect = document.getElementById('select-dest');
  const prioritySelect = document.getElementById('admit-priority');
  const resourceSelect = document.getElementById('admit-resource');

  const origin = originSelect ? parseInt(originSelect.value) : 1;
  const dest = destSelect ? parseInt(destSelect.value) : 16;
  const priority = prioritySelect ? parseInt(prioritySelect.value) : 2;
  const resType = resourceSelect ? parseInt(resourceSelect.value) : 0;

  try {
    const data = await api.admitDispatch(priority, resType, origin, dest);
    appendFeedCard(`<strong>MISSION ADMITTED: Ingested Patient Transfer Request #${data.requestId} into ${data.queueType}</strong>`, 'admit');
    loadStatus();
    loadQueuesState();
    updateActiveTelemetry();
  } catch (err) {
    console.error('Error admitting mission:', err);
  }
}

async function onRepopulateShift() {
  try {
    const data = await api.repopulateDispatch();
    appendFeedCard(`<strong>SHIFT INGESTION: Loaded fresh shift batch from database (${data.urgent} Urgent, ${data.normal} Normal, ${data.preempt} Preemptions).</strong>`, 'admit');
    loadStatus();
    loadQueuesState();
  } catch (err) {
    console.error('Error reloading shift batch:', err);
  }
}

async function onServeSingle() {
  try {
    const data = await api.serveDispatch();
    if (!data.success) {
      alert(data.message);
      return;
    }

    // Live update map routing to highlight the active dispatched route
    if (data.originId && data.targetId) {
      drawDijkstraRoute(data.originId, data.targetId);
    }

    appendFeedCard(`
      <div style="display: flex; justify-content: space-between; margin-bottom: 4px;">
        <strong style="color: var(--accent-primary); font-size: 13.5px;">${data.serviceType}</strong>
        <span style="font-weight: 700; color: var(--text-muted);">Request #${data.requestId}</span>
      </div>
      <div style="margin-bottom: 4px;">
        <span style="background: var(--bg-input); padding: 2px 6px; border-radius: 4px; font-size: 11.5px; border: 1px solid var(--border-subtle);">${data.triageName}</span>
        <span style="font-size: 11.5px; color: var(--text-secondary); margin-left: 6px;">Payload: ${data.weightKg.toFixed(1)} kg</span>
      </div>
      <div style="color: var(--accent-green); font-weight: 600; margin-bottom: 2px;">
        ✓ Allocated ${data.resource.typeName} #${data.resource.id} (Cost: GH¢ ${data.resource.costGhc.toFixed(2)})
      </div>
      <div style="color: var(--text-muted); font-size: 11.5px;">
        Transfer Corridor: ${data.fromName} ➔ ${data.toName}
      </div>
    `, 'serve');

    loadStatus();
    loadQueuesState();
  } catch (err) {
    console.error('Error serving request:', err);
  }
}

async function onUndo() {
  try {
    const data = await api.undoDispatch();
    if (!data.success) {
      alert(data.message);
      return;
    }

    appendFeedCard(`
      <strong>STACK ROLLBACK: Undone ${data.actionType} on Request #${data.requestId}</strong><br>
      <span style="color: var(--accent-red);">Popped from ArrayStack ➔ Resource #${data.resourceId} restored to pool</span>
    `, 'undo');

    loadStatus();
    loadQueuesState();
  } catch (err) {
    console.error('Error rolling back action:', err);
  }
}

function appendFeedCard(htmlContent, type = 'serve') {
  const feed = document.getElementById('dispatch-feed');
  if (!feed) return;
  if (feed.querySelector('.empty-state')) {
    feed.innerHTML = '';
  }

  const card = document.createElement('div');
  card.className = `stream-card ${type}`;
  card.innerHTML = htmlContent;
  feed.insertBefore(card, feed.firstChild);
}

function renderQueueList(containerId, items, queueName) {
  const container = document.getElementById(containerId);
  if (!container) return;

  if (!items || items.length === 0) {
    container.innerHTML = `<div class="empty-state-sm">${queueName} is currently empty</div>`;
    return;
  }

  container.innerHTML = '';
  items.forEach(item => {
    const card = document.createElement('div');
    card.className = 'ds-card';
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between;">
        <strong style="color: var(--text-primary);">${item.serviceType}</strong>
        <span style="color: var(--text-muted); font-size: 11.5px;">#${item.requestId}</span>
      </div>
      <div style="color: var(--accent-amber); font-size: 11.5px; margin: 2px 0;">${item.triageName} · ${item.weightKg.toFixed(1)} kg</div>
      <div style="color: var(--text-muted); font-size: 11px;">Corridor: ${item.from} ➔ ${item.to}</div>
    `;
    container.appendChild(card);
  });
}

function renderStackList(containerId, items) {
  const container = document.getElementById(containerId);
  if (!container) return;

  if (!items || items.length === 0) {
    container.innerHTML = `<div class="empty-state-sm">Undo Stack is empty</div>`;
    return;
  }

  container.innerHTML = '';
  items.forEach((item, idx) => {
    const card = document.createElement('div');
    card.className = 'ds-card';
    const topBadge = idx === 0 ? `<span style="color: var(--accent-primary); font-weight: 700;">[TOP] </span>` : '';
    card.innerHTML = `
      <div style="display: flex; justify-content: space-between;">
        <div>${topBadge}<strong style="color: var(--text-primary);">${item.serviceType}</strong></div>
        <span style="color: var(--text-muted); font-size: 11.5px;">Req #${item.requestId}</span>
      </div>
      <div style="color: var(--accent-green); font-size: 11.5px; margin: 2px 0;">✓ Allocated ${item.resourceName} #${item.resourceId} (GH¢ ${item.costGhc.toFixed(2)})</div>
      <div style="color: var(--text-muted); font-size: 11px;">Corridor: ${item.from} ➔ ${item.to}</div>
    `;
    container.appendChild(card);
  });
}
