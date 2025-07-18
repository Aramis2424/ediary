/// <reference types="vitest" />
import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'node:url'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 2525,
  },
  test: {
    globals: true,
    environment: 'jsdom',
    include: ['src/**/*.spec.ts',
      'tests/**/*.spec.ts',
    ],
    coverage: {
      reporter: ['text', 'json', 'html'], 
    },
  },
})
