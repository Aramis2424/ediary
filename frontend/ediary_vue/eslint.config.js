import tseslint from '@typescript-eslint/eslint-plugin'
import tsparser from '@typescript-eslint/parser'
import vue from 'eslint-plugin-vue'
import vueParser from 'vue-eslint-parser'

export default [
  {
    ignores: ['node_modules', 'dist', '**/mockServiceWorker.js', 
              "src/shims-vue.d.ts", "vite.config.ts",
              "**/MoodGraph.vue", 'tests/**'],
  },
  {
    files: ['**/*.vue'],
    languageOptions: {
      parser: vueParser,
      parserOptions: {
        parser: tsparser, 
        ecmaVersion: 2020,
        sourceType: 'module',
        project: './tsconfig.app.json', 
        extraFileExtensions: ['.vue'], 
      },
    },
    plugins: {
      vue,
      '@typescript-eslint': tseslint,
    },
    rules: {
      'vue/multi-word-component-names': 'off',
      '@typescript-eslint/explicit-module-boundary-types': 'warn',
      '@typescript-eslint/no-unsafe-assignment': 'error',
      '@typescript-eslint/no-unsafe-call': 'error',
      '@typescript-eslint/no-unsafe-return': 'error',
      '@typescript-eslint/no-explicit-any': 'error',
    },
  },
  {
    files: ['**/*.ts', '**/*.tsx'],
    languageOptions: {
      parser: tsparser,
      parserOptions: {
        ecmaVersion: 2020,
        sourceType: 'module',
        project: './tsconfig.app.json',
      },
    },
    plugins: {
      '@typescript-eslint': tseslint,
    },
    rules: {
      '@typescript-eslint/explicit-function-return-type': 'warn',
      '@typescript-eslint/no-unused-vars': ['error', { argsIgnorePattern: '^_' }],
    },
  }
]
