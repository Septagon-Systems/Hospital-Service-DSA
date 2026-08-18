import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import remarkMath from 'remark-math';
import rehypeKatex from 'rehype-katex';

export default defineConfig({
  markdown: {
    remarkPlugins: [remarkMath],
    rehypePlugins: [rehypeKatex],
  },
  integrations: [
    starlight({
      title: 'Ghana Hospital Service DSA',
      description: 'Smart Hospital Operations Optimizer - Data Structures & Algorithms Documentation',
      favicon: '/favicon.svg',
      logo: {
        dark: './src/assets/logo-dark.png',
        light: './src/assets/logo-light.png',
        replacesTitle: false,
      },
      social: {
        github: 'https://github.com/Septagon-Systems/Hospital-Service-DSA',
      },
      head: [
        {
          tag: 'link',
          attrs: { rel: 'icon', type: 'image/svg+xml', href: '/favicon.svg' },
        },
        {
          tag: 'link',
          attrs: { rel: 'icon', type: 'image/png', href: '/favicon.png' },
        },
        {
          tag: 'link',
          attrs: { rel: 'shortcut icon', href: '/favicon.ico' },
        },
        {
          tag: 'script',
          attrs: { src: '/scripts/table-modal.js', defer: true },
        },
      ],
      customCss: [
        'katex/dist/katex.min.css',
        './src/styles/custom.css',
      ],
      sidebar: [
        {
          label: 'Project Overview',
          items: [
            { label: 'Dataset & Local Context', slug: 'overview/data-acquisition' },
            { label: 'Weekly Development Log', slug: 'overview/dev-log' },
            { label: 'Academic References', slug: 'overview/references' },
          ],
        },
        {
          label: 'Service Operations',
          items: [
            { label: 'Flowcharts & Formal Pseudocode', slug: 'service-operations/flowcharts-pseudocode' },
          ],
        },
        {
          label: 'Custom Data Structures',
          items: [
            {
              label: 'Linear Collections',
              items: [
                { label: 'Dynamic Array List', slug: 'data-structures/linear/dynamic-array' },
                { label: 'Dynamic Array Resize Trace', slug: 'data-structures/linear/dynamic-array-resize' },
                { label: 'Array Stack (Undo Log)', slug: 'data-structures/linear/array-stack' },
                { label: 'Array Deque (Urgent Queue)', slug: 'data-structures/linear/array-deque' },
                { label: 'Circular Queue', slug: 'data-structures/linear/circular-queue' },
                { label: 'Linked List Invariants', slug: 'data-structures/linear/linked-list-proof' },
              ],
            },
            {
              label: 'Trees & Priority Structures',
              items: [
                { label: 'Binary Search Tree (BST)', slug: 'data-structures/trees/binary-search-tree' },
                { label: 'Red-Black Tree', slug: 'data-structures/trees/red-black-tree' },
                { label: 'B-Tree Multi-Way Search Tree', slug: 'data-structures/trees/b-tree' },
                { label: 'Min-Heap Priority Queue', slug: 'data-structures/trees/min-heap-dispatch' },
              ],
            },
            {
              label: 'Hashing & Key-Value Stores',
              items: [
                { label: 'Custom Hash Table', slug: 'data-structures/hashing/custom-hash-table' },
                { label: 'Custom Map & Set Wrappers', slug: 'data-structures/hashing/custom-map-set' },
              ],
            },
          ],
        },
        {
          label: 'Searching & Sorting Algorithms',
          items: [
            {
              label: 'Search Algorithms',
              items: [
                { label: 'Binary & Linear Search', slug: 'algorithms/searching/binary-search' },
                { label: 'Unsorted Precondition Case', slug: 'algorithms/searching/unsorted-precondition' },
              ],
            },
            {
              label: 'Sorting Engines',
              items: [
                { label: 'Insertion Sort', slug: 'algorithms/sorting/insertion-sort' },
                { label: 'Selection Sort', slug: 'algorithms/sorting/selection-sort' },
                { label: 'Merge Sort & Quick Sort', slug: 'algorithms/sorting/merge-quick-sort' },
                { label: 'Search & Sort Loop Invariants', slug: 'algorithms/sorting/search-sort-loop-invariants' },
              ],
            },
          ],
        },
        {
          label: 'Graph & Optimization Engines',
          items: [
            {
              label: 'Graph Routing & MST',
              items: [
                { label: 'BFS & DFS Graph Traversal', slug: 'algorithms/graph/bfs-dfs' },
                { label: 'Dijkstra Shortest Path', slug: 'algorithms/graph/dijkstra' },
                { label: 'Kruskal & Prim MST', slug: 'algorithms/graph/kruskal-prim' },
              ],
            },
            {
              label: 'Optimization & Dynamic Programming',
              items: [
                { label: 'Dynamic Programming Routing', slug: 'algorithms/optimization/dynamic-programming' },
                { label: 'Greedy Failure Counterexample', slug: 'algorithms/optimization/greedy-failure' },
                { label: 'Greedy & DP Correctness Proof', slug: 'algorithms/optimization/greedy-dp-correctness' },
                { label: 'Recursion & Induction Proof', slug: 'algorithms/optimization/recursion-induction' },
              ],
            },
          ],
        },
        {
          label: 'Empirical Performance Lab',
          items: [
            { label: 'Empirical Benchmarking & Analysis', slug: 'performance/empirical-benchmarking' },
          ],
        },
      ],
    }),
  ],
});

