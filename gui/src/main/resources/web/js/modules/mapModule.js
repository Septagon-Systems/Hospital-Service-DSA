import { api } from '../api.js';

let map;
let hospitalMarkers = [];
let routeLines = [];
let activeDijkstraLine = null;
let mstLines = [];
let hospitalsData = [];
let darkTileLayer;
let osmTileLayer;
let isDarkTile = true;

export function initMapModule() {
  // Center of Ghana
  map = L.map('map-container').setView([7.9465, -1.0232], 7);

  // Esri Dark Gray Canvas — Free, fast, dark mode (zero watermark, zero API key)
  darkTileLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Dark_Gray_Base/MapServer/tile/{z}/{y}/{x}', {
    attribution: 'Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ',
    maxZoom: 16
  }).addTo(map);

  // Standard OpenStreetMap street layer (zero watermark, zero API key)
  osmTileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 19
  });

  loadMapData();
  bindMapEvents();
}

export function refreshMapSize() {
  if (map) {
    setTimeout(() => map.invalidateSize(), 200);
  }
}

async function loadMapData() {
  try {
    const [hospitals, routes] = await Promise.all([
      api.getHospitals(),
      api.getRoutes()
    ]);

    hospitalsData = hospitals;
    populateDropdowns(hospitals);
    renderHospitalNodes(hospitals);
  } catch (err) {
    console.error('Error loading GIS map dataset:', err);
  }
}

function populateDropdowns(hospitals) {
  const selectOrigin = document.getElementById('select-origin');
  const selectDest = document.getElementById('select-dest');
  const searchSelect = document.getElementById('search-hospital-select');

  hospitals.forEach(h => {
    const label = `[${h.id}] ${h.name} (${h.region})`;
    if (selectOrigin) selectOrigin.add(new Option(label, h.id));
    if (selectDest) selectDest.add(new Option(label, h.id));
    if (searchSelect) searchSelect.add(new Option(label, h.id));
  });

  if (selectOrigin) selectOrigin.value = "1";   // Korle Bu
  if (selectDest) selectDest.value = "16";      // Komfo Anokye
}

function renderHospitalNodes(hospitals) {
  hospitals.forEach(h => {
    const nodeIcon = L.divIcon({
      className: 'hosp-map-node',
      html: `<div style="background: #e07a5f; width: 9px; height: 9px; border-radius: 50%; border: 2px solid #fff; box-shadow: 0 0 6px #e07a5f;"></div>`,
      iconSize: [10, 10],
      iconAnchor: [5, 5]
    });

    const marker = L.marker([h.lat, h.lng], { icon: nodeIcon }).addTo(map);
    marker.bindPopup(`
      <div style="font-family: 'Plus Jakarta Sans', sans-serif; color: #0c0e14; padding: 4px;">
        <strong style="font-size: 13.5px; color: #e07a5f;">${h.name}</strong><br>
        <span style="color: #64748b; font-size: 12px;">${h.region} · ${h.town}</span>
        <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 6px 0;">
        <b>Facility Class:</b> ${h.type} (${h.ownership})<br>
        <b>ICU Bed Capacity:</b> ${h.icu} units<br>
        <b>Ambulance Fleet:</b> ${h.ambulances} vehicles
      </div>
    `);
    hospitalMarkers.push(marker);
  });
}

function bindMapEvents() {
  document.getElementById('btn-toggle-tiles')?.addEventListener('click', onToggleTiles);
  document.getElementById('btn-calculate-route')?.addEventListener('click', onCalculateRoute);
  document.getElementById('btn-toggle-mst')?.addEventListener('click', onToggleMst);
  document.getElementById('btn-reset-map')?.addEventListener('click', onResetMap);
}

function onToggleTiles() {
  const btn = document.getElementById('btn-toggle-tiles');
  if (isDarkTile) {
    map.removeLayer(darkTileLayer);
    map.addLayer(osmTileLayer);
    isDarkTile = false;
    if (btn) btn.innerText = 'Map Style: Dark / Street';
  } else {
    map.removeLayer(osmTileLayer);
    map.addLayer(darkTileLayer);
    isDarkTile = true;
    if (btn) btn.innerText = 'Map Style: Street / Dark';
  }
}

const roadGeometryCache = new Map();

async function fetchLegGeometry(lng1, lat1, lng2, lat2) {
  const cacheKey = `${lng1.toFixed(4)},${lat1.toFixed(4)}-${lng2.toFixed(4)},${lat2.toFixed(4)}`;
  if (roadGeometryCache.has(cacheKey)) {
    return roadGeometryCache.get(cacheKey);
  }

  try {
    const url = `https://router.project-osrm.org/route/v1/driving/${lng1},${lat1};${lng2},${lat2}?overview=full&geometries=geojson`;
    const res = await fetch(url);
    if (res.ok) {
      const data = await res.json();
      if (data.routes && data.routes.length > 0) {
        const latLngs = data.routes[0].geometry.coordinates.map(c => [c[1], c[0]]);
        roadGeometryCache.set(cacheKey, latLngs);
        return latLngs;
      }
    }
  } catch (e) {
    // Graceful fallback to direct vector
  }
  const fallback = [[lat1, lng1], [lat2, lng2]];
  roadGeometryCache.set(cacheKey, fallback);
  return fallback;
}

async function buildFullRouteGeometry(waypoints) {
  let fullGeometry = [];
  for (let i = 0; i < waypoints.length - 1; i++) {
    const p1 = waypoints[i];
    const p2 = waypoints[i + 1];
    const legCoords = await fetchLegGeometry(p1.lng, p1.lat, p2.lng, p2.lat);
    if (i > 0) {
      fullGeometry = fullGeometry.concat(legCoords.slice(1));
    } else {
      fullGeometry = fullGeometry.concat(legCoords);
    }
  }
  return fullGeometry.length > 0 ? fullGeometry : waypoints.map(w => [w.lat, w.lng]);
}

export async function drawDijkstraRoute(source, dest) {
  try {
    const data = await api.runDijkstra(source, dest);

    if (!data.reachable) {
      alert('Destination facility is unreachable along the mapped highway network.');
      return null;
    }

    const resultBox = document.getElementById('route-result-box');
    if (resultBox) resultBox.classList.remove('hidden');
    const distEl = document.getElementById('route-distance');
    if (distEl) distEl.innerText = `${data.totalDistanceKm.toFixed(2)} km`;
    const timeEl = document.getElementById('route-time');
    if (timeEl) timeEl.innerText = `${data.travelTimeMins.toFixed(1)} mins (${(data.travelTimeMins / 60).toFixed(2)} hrs)`;
    const latEl = document.getElementById('route-latency');
    if (latEl) latEl.innerText = `${(data.executionTimeNs / 1000000).toFixed(4)} ms (${data.executionTimeNs} ns)`;

    const waypointsContainer = document.getElementById('route-waypoints');
    if (waypointsContainer) {
      waypointsContainer.innerHTML = '';
      data.waypoints.forEach((wp, idx) => {
        const row = document.createElement('div');
        row.className = 'waypoint-row';
        const label = idx === 0 ? 'START' : (idx === data.waypoints.length - 1 ? 'DEST' : `WAYPOINT ${idx}`);
        row.innerHTML = `
          <span><b>${label}:</b> [${wp.id}] ${wp.name}</span>
          <span style="color: var(--accent-primary); font-family: monospace;">${wp.legKm > 0 ? '+' + wp.legKm.toFixed(1) + ' km' : '0.0 km'}</span>
        `;
        waypointsContainer.appendChild(row);
      });
    }

    const routePolylineCoords = await buildFullRouteGeometry(data.waypoints);

    if (activeDijkstraLine) map.removeLayer(activeDijkstraLine);
    activeDijkstraLine = L.polyline(routePolylineCoords, {
      color: '#e07a5f',
      weight: 5,
      opacity: 0.95,
      lineJoin: 'round',
      lineCap: 'round'
    }).addTo(map);

    map.fitBounds(activeDijkstraLine.getBounds(), { padding: [40, 40] });
    return data;
  } catch (err) {
    console.error('Error calculating shortest route:', err);
    return null;
  }
}

async function onCalculateRoute() {
  const source = document.getElementById('select-origin').value;
  const dest = document.getElementById('select-dest').value;
  await drawDijkstraRoute(source, dest);
}

async function onToggleMst() {
  if (mstLines.length > 0) {
    mstLines.forEach(l => map.removeLayer(l));
    mstLines = [];
    return;
  }

  try {
    const data = await api.getMst();
    const hospitalMap = new Map(hospitalsData.map(h => [h.id, h]));

    data.kruskalEdges.forEach(e => {
      const h1 = hospitalMap.get(e.source);
      const h2 = hospitalMap.get(e.dest);
      if (h1 && h2) {
        const line = L.polyline([[h1.lat, h1.lng], [h2.lat, h2.lng]], {
          color: '#fbbf24',
          weight: 2.5,
          opacity: 0.85
        }).addTo(map);
        mstLines.push(line);
      }
    });
  } catch (err) {
    console.error('Error retrieving MST:', err);
  }
}

function onResetMap() {
  map.setView([7.9465, -1.0232], 7);
  if (activeDijkstraLine) map.removeLayer(activeDijkstraLine);
  mstLines.forEach(l => map.removeLayer(l));
  mstLines = [];
  document.getElementById('route-result-box')?.classList.add('hidden');
}
