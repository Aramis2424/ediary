import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '../components/LoginPage.vue'
import RegisterPage from '../components/RegisterPage.vue'
import Home from '../components/Home.vue'
import MoodGraph from '../components/MoodGraph.vue'
import EntriesMenu from '../components/EntriesMenu.vue'
import MarkdownEditor from '../components/MarkdownEditor.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  { path: '/home', component: Home },
  { path: '/graph', component: MoodGraph },
  { path: '/menu', component: EntriesMenu },
  { path: '/entry/:id', component: MarkdownEditor },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
