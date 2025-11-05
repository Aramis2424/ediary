import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createTestingPinia } from '@pinia/testing'
import { createRouter, createWebHistory } from 'vue-router'
import LoginForm from '../../src/views/LoginPage.vue'
import { useAuthStore } from '../../src/stores/auth'
import { routes } from '../../src/router/index.ts'

// создаём мок функции toast вне describe()
const toastError = vi.fn()

vi.mock('vue-toastification', () => ({
  useToast: vi.fn(() => ({
    error: toastError,
  })),
}))

describe('LoginForm.vue', () => {
  let wrapper: ReturnType<typeof mount>
  let authStore: ReturnType<typeof useAuthStore>
  let router: ReturnType<typeof createRouter>

  beforeEach(async () => {
    router = createRouter({
      history: createWebHistory(),
      routes,
    })

    const pinia = createTestingPinia({
      createSpy: vi.fn,
    })

    wrapper = mount(LoginForm, {
      global: {
        plugins: [pinia, router],
      },
    })

    authStore = useAuthStore()
    toastError.mockReset()
  })

  describe('successful login', () => {
    it('redirects to /home after successful login', async () => {
      authStore.login = vi.fn().mockResolvedValueOnce({ token: 'fakeToken' })
      const pushSpy = vi.spyOn(router, 'push').mockResolvedValue()

      await wrapper.get('input[type="text"]').setValue('user')
      await wrapper.get('input[type="password"]').setValue('pass')
      await wrapper.find('form').trigger('submit.prevent')

      expect(authStore.login).toHaveBeenCalledWith({
        login: 'user',
        password: 'pass',
      })
      expect(pushSpy).toHaveBeenCalledWith('/home')
    })
  })
  
  describe('failed login', () => {
    it('shows toast error when login fails', async () => {
      authStore.login = vi.fn().mockRejectedValueOnce(new Error('Login failed'))

      await wrapper.get('input[type="text"]').setValue('baduser')
      await wrapper.get('input[type="password"]').setValue('wrongpass')
      await wrapper.find('form').trigger('submit.prevent')

      expect(authStore.login).toHaveBeenCalled()
      expect(toastError).toHaveBeenCalledWith('Неверные данные')
    })
  })
})