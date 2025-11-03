import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import './assets/main.css'
import './assets/fonts.css'
import './assets/backgrounds.css';
import router from './router'
import App from './App.vue'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'

const options = {
  position: "top-right",
  timeout: 3000,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
  transition: "Vue-Toastification__fade",
}

if (import.meta.env.DEV) {
  const { worker } = await import('@/mocks/browser')
  await worker.start({
      onUnhandledRequest: 'bypass'
    })
    console.log('[MSW] Mock-server is running')
}

const app = createApp(App)
    .use(router)
    .use(createPinia()
        .use(piniaPluginPersistedstate))

app.use(Toast, options)
app.mount('#app')
