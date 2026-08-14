import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

export default defineConfig({
  integrations: [
    starlight({
      title: 'Ghana Hospital Service DSA',
      description: 'Smart Hospital Operations Optimizer - Data Structures & Algorithms Documentation',
      social: {
        github: 'https://github.com/Septagon-Systems/Hospital-Service-DSA',
      },
      sidebar: [
        {
          label: '📌 Project Overview',
          autogenerate: { directory: 'overview' },
        },
        {
          label: '📦 Custom Data Structures',
          autogenerate: { directory: 'data-structures' },
        },
        {
          label: '⚡ Searching & Sorting Algorithms',
          autogenerate: { directory: 'algorithms' },
        },
      ],
    }),
  ],
});
