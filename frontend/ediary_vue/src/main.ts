import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import './assets/main.css'
import './assets/fonts.css'
import './assets/backgrounds.css';
import router from './router'
import App from './App.vue'

if (import.meta.env.DEV) {
  const { worker } = await import('@/mocks/browser')
  await worker.start({
      onUnhandledRequest: 'bypass'
    })
    console.log('[MSW] Mock-server is running')
}

createApp(App)
    .use(router)
    .use(createPinia()
        .use(piniaPluginPersistedstate))
    .mount('#app')
