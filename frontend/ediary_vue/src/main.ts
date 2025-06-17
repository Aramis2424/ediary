import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './assets/main.css'
import './assets/fonts.css'
import './assets/backgrounds.css';
import router from './router'
import App from './App.vue'

createApp(App)
    .use(router)
    .use(createPinia())
    .mount('#app')
