import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/views/LoginPage.vue'
import RegisterPage from '@/views/RegisterPage.vue'
import Home from '@/views/Home.vue'
import MoodMenu from '@/views/MoodMenu.vue'
import EntriesMenu from '@/views/EntriesMenu.vue'
import MarkdownEditor from '@/views/MarkdownEditor.vue'
import { useAuthStore } from '@/stores/auth'

const routes = [
  { path: '/', redirect: '/login', meta: { public: true } },
  { path: '/login', component: LoginPage, meta: { public: true } },
  { path: '/register', component: RegisterPage, meta: { public: true } },
  { path: '/home', component: Home },
  { path: '/mood', component: MoodMenu },
  { path: '/menu', component: EntriesMenu },
  { path: '/entry/:id', component: MarkdownEditor },
]

const router = createRouter({
  history: createWebHistory(),
  //history: createWebHistory('/legacy/'),
  routes,
})

router.beforeEach(async (to, _from, next) => {
  const auth = useAuthStore()

  if (auth.token && !auth.user) {
    try {
    await auth.fetchUser()    
    } catch {
      return next('/login')
    }
  }

  const isPublic = to.meta.public === true
  if (isPublic || auth.isLoggedIn) {
    return next()
  } else {
    return next('/login')
  }
})

export default router
