import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import RegisterPage from '@/components/RegisterPage.vue'
import Home from '@/components/Home.vue'
import MoodGraph from '@/components/MoodGraph.vue'
import EntriesMenu from '@/components/EntriesMenu.vue'
import MarkdownEditor from '@/components/MarkdownEditor.vue'
import { useAuthStore } from '@/stores/auth'

const routes = [
  { path: '/', redirect: '/login', meta: { public: true } },
  { path: '/login', component: LoginPage, meta: { public: true } },
  { path: '/register', component: RegisterPage, meta: { public: true } },
  { path: '/home', component: Home },
  { path: '/graph', component: MoodGraph },
  { path: '/menu', component: EntriesMenu },
  { path: '/entry/:id', component: MarkdownEditor },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to, _from, next) => {
  const auth = useAuthStore()

  if (auth.token && !auth.user) {
    auth.fetchUser()
  }

  const isPublic = to.meta.public === true
  if (isPublic || auth.isLoggedIn) {
    return next()
  } else {
    return next('/login')
  }
})

export default router
