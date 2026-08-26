/**
 * Ghana Smart Hospital Operations Optimizer (DCIT 204 / 308)
 * Main Frontend Coordinator & Router
 */

import { initMapModule, refreshMapSize } from './modules/mapModule.js';
import { initDispatchModule } from './modules/dispatchModule.js';
import { initSearchModule } from './modules/searchModule.js';
import { initCargoModule } from './modules/cargoModule.js';
import { initBenchmarkModule } from './modules/benchmarkModule.js';
import { initStudentModule } from './modules/studentModule.js';

document.addEventListener('DOMContentLoaded', () => {
  initThemeManager();
  initTabRouter();
  initMapModule();
  initDispatchModule();
  initSearchModule();
  initCargoModule();
  initBenchmarkModule();
  initStudentModule();
});

function initThemeManager() {
  const btn = document.getElementById('btn-theme-toggle');
  const logo = document.querySelector('.brand-logo');

  btn?.addEventListener('click', () => {
    const current = document.documentElement.getAttribute('data-theme') || 'dark';
    const next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);

    if (logo) {
      logo.src = next === 'light' ? '/assets/logo-compact-light.png' : '/assets/logo-compact-dark.png';
    }

    window.dispatchEvent(new CustomEvent('themeChanged', { detail: { theme: next } }));
  });
}

function initTabRouter() {
  const tabs = document.querySelectorAll('.nav-tab');
  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      tabs.forEach(t => t.classList.remove('active'));
      document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));

      tab.classList.add('active');
      const paneId = tab.getAttribute('data-tab');
      const targetPane = document.getElementById(paneId);
      if (targetPane) {
        targetPane.classList.add('active');
      }

      if (paneId === 'tab-map') {
        refreshMapSize();
      } else if (paneId === 'tab-dispatch') {
        window.dispatchEvent(new CustomEvent('dispatch-refresh'));
      }
    });
  });
}
