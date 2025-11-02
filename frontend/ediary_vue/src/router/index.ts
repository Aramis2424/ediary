import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/views/LoginPage.vue'
import RegisterPage from '@/views/RegisterPage.vue'
import Home from '@/views/Home.vue'
import MoodMenu from '@/views/MoodMenu.vue'
import EntriesMenu from '@/views/EntriesMenu.vue'
import MarkdownEditor from '@/views/MarkdownEditor.vue'
import SidebarHome from '@/views/sidebars/SidebarHome.vue'
import SidebarEntries from '@/views/sidebars/SidebarEntries.vue'
import SidebarEntry from '@/views/sidebars/SidebarEntry.vue'
import SidebarMood from '@/views/sidebars/SidebarMood.vue'
import { useAuthStore } from '@/stores/auth'

const routes = [
  { path: '/', redirect: '/login', meta: { public: true } },
  { path: '/login', component: LoginPage, meta: { public: true } },
  { path: '/register', component: RegisterPage, meta: { public: true } },
  {
    path: '/home',
    components: { default: Home, sidebar: SidebarHome }
  },
  {
    path: '/mood',
    components: { default: MoodMenu, sidebar: SidebarMood }
  },
  {
    path: '/menu',
    components: { default: EntriesMenu, sidebar: SidebarEntries }
  },
  {
    path: '/entry/:id',
    components: { default: MarkdownEditor, sidebar: SidebarEntry }
  },
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
