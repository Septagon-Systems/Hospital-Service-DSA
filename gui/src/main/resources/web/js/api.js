/**
 * REST API Client for Ghana Smart Hospital Operations Optimizer (DCIT 204 / 308)
 */
export const api = {
  async getHospitals() {
    const res = await fetch('/api/hospitals');
    return res.json();
  },

  async getRoutes() {
    const res = await fetch('/api/routes');
    return res.json();
  },

  async getSystemStatus() {
    const res = await fetch('/api/system/status');
    return res.json();
  },

  async runDijkstra(sourceId, destId) {
    const res = await fetch(`/api/route/dijkstra?source=${sourceId}&dest=${destId}`);
    return res.json();
  },

  async getMst() {
    const res = await fetch('/api/route/mst');
    return res.json();
  },

  async getDispatchStatus() {
    const res = await fetch('/api/dispatch/status');
    return res.json();
  },

  async getDispatchQueues() {
    const res = await fetch('/api/dispatch/queues');
    return res.json();
  },

  async serveDispatch() {
    const res = await fetch('/api/dispatch/serve', { method: 'POST' });
    return res.json();
  },

  async batchServeDispatch(count = 5) {
    const res = await fetch(`/api/dispatch/batch?count=${count}`, { method: 'POST' });
    return res.json();
  },

  async undoDispatch() {
    const res = await fetch('/api/dispatch/undo', { method: 'POST' });
    return res.json();
  },

  async admitDispatch(priority, resType, origin = 1, target = 16) {
    const res = await fetch(`/api/dispatch/admit?priority=${priority}&type=${resType}&origin=${origin}&target=${target}`, { method: 'POST' });
    return res.json();
  },

  async searchHospital(hospitalId) {
    const res = await fetch(`/api/search?id=${hospitalId}`);
    return res.json();
  },

  async optimizeKnapsack(capacityKg) {
    const res = await fetch(`/api/optimizer/knapsack?capacity=${capacityKg}`);
    return res.json();
  },

  async runBenchmark(algoName) {
    const res = await fetch(`/api/benchmark/run?algo=${algoName}`);
    return res.json();
  },

  async repopulateDispatch() {
    const res = await fetch('/api/dispatch/repopulate', { method: 'POST' });
    return res.json();
  },

  async calculateStudentParams(studentId, apply = false) {
    const res = await fetch(`/api/student/calc?id=${encodeURIComponent(studentId)}${apply ? '&apply=true' : ''}`);
    return res.json();
  }
};
