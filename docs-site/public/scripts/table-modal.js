// Septagon Global Fullscreen Table Modal & Dynamic Popular Topics Script
document.addEventListener('DOMContentLoaded', () => {
  initTableModals();
  initPopularTopics();
  initScrollFloatingHeader();
  initMobileLogoSwitch();
  initSidebarScrollWatcher();
  initMermaidDiagrams();
});

// Re-run on client-side page transitions (Astro view transitions)
document.addEventListener('astro:page-load', () => {
  initTableModals();
  initPopularTopics();
  initScrollFloatingHeader();
  initMobileLogoSwitch();
  initSidebarScrollWatcher();
  initMermaidDiagrams();
});

// ---------------------------------------------------- //
// DETACHED & IDLE SIDEBAR SCROLLBAR AUTO-HIDE SYSTEM  //
// ---------------------------------------------------- //
function initSidebarScrollWatcher() {
  const sidebars = document.querySelectorAll('.sidebar-pane, .right-sidebar, .sidebar-content, .right-sidebar-panel');
  sidebars.forEach((sidebar) => {
    if (sidebar.__scrollWatcherAttached) return;
    sidebar.__scrollWatcherAttached = true;

    let scrollTimeout;
    sidebar.addEventListener('scroll', () => {
      sidebar.classList.add('is-scrolling');
      clearTimeout(scrollTimeout);
      scrollTimeout = setTimeout(() => {
        sidebar.classList.remove('is-scrolling');
      }, 800);
    }, { passive: true });
  });
}

// ---------------------------------------------------- //
// MOBILE COMPACT LOGO SWITCH SYSTEM                    //
// ---------------------------------------------------- //
function initMobileLogoSwitch() {
  const siteTitles = document.querySelectorAll('.site-title');
  siteTitles.forEach((siteTitle) => {
    siteTitle.querySelectorAll('img').forEach((img) => {
      img.setAttribute('data-image-component', 'true');
      img.setAttribute('loading', 'eager');
    });

    if (siteTitle.querySelector('.logo-compact-dark')) return;

    const compactDark = document.createElement('img');
    compactDark.className = 'logo-compact logo-compact-dark light:sl-hidden';
    compactDark.src = '/logo-compact-dark.png';
    compactDark.alt = 'Septagon';
    compactDark.setAttribute('data-image-component', 'true');
    compactDark.setAttribute('loading', 'eager');

    const compactLight = document.createElement('img');
    compactLight.className = 'logo-compact logo-compact-light dark:sl-hidden';
    compactLight.src = '/logo-compact-light.png';
    compactLight.alt = 'Septagon';
    compactLight.setAttribute('data-image-component', 'true');
    compactLight.setAttribute('loading', 'eager');

    siteTitle.insertBefore(compactDark, siteTitle.firstChild);
    siteTitle.insertBefore(compactLight, siteTitle.firstChild);
  });
}

// ---------------------------------------------------- //
// FLOATING GLASS CAPSULE HEADER — SCROLL LISTENER      //
// ---------------------------------------------------- //
function initScrollFloatingHeader() {
  // Prevent duplicate listeners across page transitions
  if (window.__scrollFloatingHeaderActive) return;
  window.__scrollFloatingHeaderActive = true;

  const SCROLL_THRESHOLD = 20;   // px to scroll before detaching
  const SCROLL_RELEASE = 5;      // px hysteresis to re-attach
  let isScrolled = false;
  let ticking = false;

  const onScroll = () => {
    if (ticking) return;
    ticking = true;

    requestAnimationFrame(() => {
      const y = window.scrollY;

      if (!isScrolled && y > SCROLL_THRESHOLD) {
        isScrolled = true;
        document.body.classList.add('is-scrolled');
      } else if (isScrolled && y < SCROLL_RELEASE) {
        isScrolled = false;
        document.body.classList.remove('is-scrolled');
      }

      ticking = false;
    });
  };

  window.addEventListener('scroll', onScroll, { passive: true });

  // Set initial state in case page loads already scrolled
  if (window.scrollY > SCROLL_THRESHOLD) {
    isScrolled = true;
    document.body.classList.add('is-scrolled');
  }
}

// Complete Topic Catalog Pool for Random Discovery Selection
const TOPIC_CATALOG = [
  { title: 'Dijkstra Shortest Path', url: '/algorithms/graph/dijkstra/', icon: 'path' },
  { title: 'Red-Black Tree Balancing', url: '/data-structures/trees/red-black-tree/', icon: 'circle' },
  { title: 'Min-Heap Priority Queue', url: '/data-structures/trees/min-heap-dispatch/', icon: 'triangle' },
  { title: 'Dynamic Array List', url: '/data-structures/linear/dynamic-array/', icon: 'rect' },
  { title: 'Dynamic Programming Routing', url: '/algorithms/optimization/dynamic-programming/', icon: 'flag' },
  { title: 'Academic References', url: '/overview/references/', icon: 'book' },
  { title: 'Weekly Development Log', url: '/overview/dev-log/', icon: 'book' },
  { title: 'Array Stack (Undo Log)', url: '/data-structures/linear/array-stack/', icon: 'rect' },
  { title: 'Array Deque (Urgent Queue)', url: '/data-structures/linear/array-deque/', icon: 'rect' },
  { title: 'Circular Queue', url: '/data-structures/linear/circular-queue/', icon: 'circle' },
  { title: 'Linked List Invariants', url: '/data-structures/linear/linked-list-proof/', icon: 'path' },
  { title: 'Binary Search Tree (BST)', url: '/data-structures/trees/binary-search-tree/', icon: 'triangle' },
  { title: 'Custom Hash Table', url: '/data-structures/hashing/custom-hash-table/', icon: 'rect' },
  { title: 'Binary & Linear Search', url: '/algorithms/searching/binary-search/', icon: 'path' },
  { title: 'Unsorted Precondition Case', url: '/algorithms/searching/unsorted-precondition/', icon: 'path' },
  { title: 'Insertion Sort', url: '/algorithms/sorting/insertion-sort/', icon: 'flag' },
  { title: 'Selection Sort', url: '/algorithms/sorting/selection-sort/', icon: 'flag' },
  { title: 'Merge Sort & Quick Sort', url: '/algorithms/sorting/merge-quick-sort/', icon: 'flag' },
  { title: 'Kruskal & Prim MST', url: '/algorithms/graph/kruskal-prim/', icon: 'path' },
  { title: 'Greedy Failure Counterexample', url: '/algorithms/optimization/greedy-failure/', icon: 'flag' },
  { title: 'Greedy & DP Correctness Proof', url: '/algorithms/optimization/greedy-dp-correctness/', icon: 'book' },
  { title: 'Recursion & Induction Proof', url: '/algorithms/optimization/recursion-induction/', icon: 'book' },
];

function getIconSvg(type) {
  switch (type) {
    case 'circle':
      return `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/></svg>`;
    case 'triangle':
      return `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polygon points="12 2 22 22 2 22"/></svg>`;
    case 'rect':
      return `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2"/></svg>`;
    case 'flag':
      return `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1z"/></svg>`;
    case 'book':
      return `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>`;
    default:
      return `<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>`;
  }
}

// Randomly Select 6 Distinct Topics from Full Catalog
function getRandomTopics(count = 6) {
  // Harvest active sidebar links if available
  const sidebarLinks = Array.from(document.querySelectorAll('nav.sidebar a, .sidebar a'))
    .map((a) => ({
      title: a.innerText.trim(),
      url: a.getAttribute('href'),
      icon: 'path',
    }))
    .filter((item) => item.title && item.url && !item.url.startsWith('#'));

  const pool = sidebarLinks.length >= count ? sidebarLinks : TOPIC_CATALOG;

  // Shuffle pool (Fisher-Yates)
  const shuffled = [...pool];
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }

  return shuffled.slice(0, count);
}

// Popular Topics Pills Inside Search Modal
function initPopularTopics() {
  const updatePopularTopics = () => {
    const dialogs = document.querySelectorAll('dialog');
    
    dialogs.forEach((dialog) => {
      const input = dialog.querySelector('.pagefind-ui__search-input, input[type="text"], input[type="search"]');

      if (!input) return;

      const form = dialog.querySelector('.pagefind-ui__form') || input.closest('form') || input.parentNode;

      // Create or locate initial view container (Popular Topics & Quick Pills)
      let initialView = dialog.querySelector('.search-modal-initial-view');
      if (!initialView) {
        initialView = document.createElement('div');
        initialView.className = 'search-modal-initial-view';
        
        // Randomly pick 6 topics for initial render
        const randomTopics = getRandomTopics(6);
        const pillsHtml = randomTopics
          .map(
            (t) => `
            <a href="${t.url}">
              ${getIconSvg(t.icon)}
              ${t.title}
            </a>
          `
          )
          .join('');

        initialView.innerHTML = `
          <div class="search-modal-section-title">Suggested Topics & Discovery</div>
          <div class="search-modal-quick-pills">
            ${pillsHtml}
          </div>
        `;

        if (form && form.parentNode) {
          form.parentNode.insertBefore(initialView, form.nextSibling);
        }
      }

      // Re-shuffle random topics whenever search modal opens
      const openBtn = document.querySelector('button[data-open-modal]');
      if (openBtn && !openBtn.dataset.randomTopicListener) {
        openBtn.dataset.randomTopicListener = 'true';
        openBtn.addEventListener('click', () => {
          if (initialView) {
            const randomTopics = getRandomTopics(6);
            const pillsContainer = initialView.querySelector('.search-modal-quick-pills');
            if (pillsContainer) {
              pillsContainer.innerHTML = randomTopics
                .map(
                  (t) => `
                  <a href="${t.url}">
                    ${getIconSvg(t.icon)}
                    ${t.title}
                  </a>
                `
                )
                .join('');
            }
          }
        });
      }

      const handleToggle = () => {
        const query = input.value.trim();
        const activeResults = dialog.querySelector('.pagefind-ui__results-area');

        if (query.length === 0) {
          initialView.style.display = 'block';
          if (activeResults) activeResults.style.display = 'none';
        } else {
          initialView.style.display = 'none';
          if (activeResults) activeResults.style.display = 'block';
        }
      };

      input.addEventListener('input', handleToggle);
      input.addEventListener('focus', handleToggle);
      handleToggle();
    });
  };

  updatePopularTopics();

  const observer = new MutationObserver(() => {
    updatePopularTopics();
  });

  observer.observe(document.body, { childList: true, subtree: true });
}

function initTableModals() {
  const tables = document.querySelectorAll('.sl-markdown-content table');

  tables.forEach((table) => {
    if (table.closest('.table-outer-container')) return;

    const outerContainer = document.createElement('div');
    outerContainer.className = 'table-outer-container';

    const toolbar = document.createElement('div');
    toolbar.className = 'table-toolbar';

    const expandBtn = document.createElement('button');
    expandBtn.className = 'table-expand-btn';
    expandBtn.type = 'button';
    expandBtn.setAttribute('aria-label', 'View Table Fullscreen');
    expandBtn.setAttribute('title', 'Fullscreen View');
    expandBtn.innerHTML = `
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="15 3 21 3 21 9"></polyline>
        <polyline points="9 21 3 21 3 15"></polyline>
        <line x1="21" y1="3" x2="14" y2="10"></line>
        <line x1="3" y1="21" x2="10" y2="14"></line>
      </svg>
    `;

    toolbar.appendChild(expandBtn);

    const cardWrapper = document.createElement('div');
    cardWrapper.className = 'table-card-wrapper';

    table.parentNode.insertBefore(outerContainer, table);
    cardWrapper.appendChild(table);

    outerContainer.appendChild(toolbar);
    outerContainer.appendChild(cardWrapper);

    expandBtn.addEventListener('click', () => {
      openTableModal(table);
    });
  });
}

function openTableModal(originalTable) {
  const overlay = document.createElement('div');
  overlay.className = 'table-modal-overlay';

  const modalCard = document.createElement('div');
  modalCard.className = 'table-modal-card';

  const modalHeader = document.createElement('div');
  modalHeader.className = 'table-modal-header';
  modalHeader.innerHTML = `
    <span class="table-modal-title">Table Detail View</span>
    <button type="button" class="table-modal-close-btn" aria-label="Close Modal">
      <span>Close (ESC)</span>
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
        <line x1="18" y1="6" x2="6" y2="18"></line>
        <line x1="6" y1="6" x2="18" y2="18"></line>
      </svg>
    </button>
  `;

  const modalBody = document.createElement('div');
  modalBody.className = 'table-modal-body';

  const clonedTable = originalTable.cloneNode(true);
  clonedTable.className = 'table-modal-content';
  modalBody.appendChild(clonedTable);

  modalCard.appendChild(modalHeader);
  modalCard.appendChild(modalBody);
  overlay.appendChild(modalCard);
  document.body.appendChild(overlay);

  requestAnimationFrame(() => {
    overlay.classList.add('active');
  });

  const closeBtn = modalHeader.querySelector('.table-modal-close-btn');
  
  const closeModal = () => {
    overlay.classList.remove('active');
    setTimeout(() => {
      if (document.body.contains(overlay)) {
        document.body.removeChild(overlay);
      }
    }, 250);
    document.removeEventListener('keydown', escHandler);
  };

  closeBtn.addEventListener('click', closeModal);
  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) closeModal();
  });

  const escHandler = (e) => {
    if (e.key === 'Escape') closeModal();
  };
  document.addEventListener('keydown', escHandler);
}

// ---------------------------------------------------- //
// DYNAMIC CLIENT-SIDE MERMAID SVG DIAGRAM RENDERER     //
// ---------------------------------------------------- //
async function initMermaidDiagrams() {
  const mermaidBlocks = document.querySelectorAll('pre[data-language="mermaid"]');
  if (mermaidBlocks.length === 0) return;

  // Function to get mermaid instance
  const getMermaid = async () => {
    if (window.mermaid) return window.mermaid;
    try {
      const module = await import('https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs');
      return module.default;
    } catch (e) {
      return null;
    }
  };

  const mermaid = await getMermaid();
  if (!mermaid) return;

  const isDark = document.documentElement.dataset.theme === 'dark' || document.documentElement.classList.contains('theme-dark') || window.matchMedia('(prefers-color-scheme: dark)').matches;

  try {
    mermaid.initialize({
      startOnLoad: false,
      theme: isDark ? 'dark' : 'default',
      securityLevel: 'loose',
      fontFamily: 'inherit',
    });
  } catch (e) {
    // Already initialized
  }

  for (let i = 0; i < mermaidBlocks.length; i++) {
    const pre = mermaidBlocks[i];
    if (pre.__mermaidRendered) continue;
    pre.__mermaidRendered = true;

    // Expressive Code splits code into div.ec-line spans; extract them accurately
    const lineElements = pre.querySelectorAll('.ec-line');
    let code = '';
    if (lineElements.length > 0) {
      code = Array.from(lineElements).map(el => el.textContent).join('\n');
    } else {
      code = pre.textContent || '';
    }

    if (!code.trim()) continue;

    const container = document.createElement('div');
    container.className = 'mermaid-diagram-container';
    container.style.display = 'flex';
    container.style.justifyContent = 'center';
    container.style.alignItems = 'center';
    container.style.padding = '1.5rem 1rem';
    container.style.margin = '1.5rem 0';
    container.style.background = isDark ? 'rgba(255, 255, 255, 0.03)' : 'rgba(0, 0, 0, 0.02)';
    container.style.borderRadius = '0.5rem';
    container.style.border = '1px solid var(--sl-color-hairline)';
    container.style.overflowX = 'auto';

    try {
      const id = `mermaid-svg-${Date.now()}-${i}-${Math.random().toString(36).substr(2, 5)}`;
      const { svg } = await mermaid.render(id, code.trim());
      container.innerHTML = svg;
      const figure = pre.closest('figure') || pre;
      figure.parentNode.replaceChild(container, figure);
    } catch (err) {
      console.warn('Mermaid rendering error for diagram:', err);
    }
  }
}

