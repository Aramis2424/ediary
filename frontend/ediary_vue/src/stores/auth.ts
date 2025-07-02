import { defineStore } from 'pinia'
import type { OwnerInfoDTO, OwnerCreateDTO, TokenRequest, TokenResponse } from '@/types/Owner'
import { api } from '@/api/axios'
import type { AxiosResponse } from 'axios'

interface AuthState {
  user: OwnerInfoDTO | null
  token: string | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: null,
    token: localStorage.getItem('token'),
  }),

  getters: {
    isLoggedIn: (state): boolean => !!state.token,
  },

  actions: {
    async login(reqToken: TokenRequest) {
      try {
        const response = await api.post<TokenRequest, AxiosResponse<TokenResponse>>('/token/create', reqToken)
        this.token = response.data.token
        
        localStorage.setItem('token', this.token)

        await this.fetchUser()
      } catch {
        throw new Error('Login failed')
      }
    },

    async register(reqRegister: OwnerCreateDTO) {
      await api.post('/owners', reqRegister)
      await this.login({
        login: reqRegister.login, 
        password: reqRegister.password})
    },

    async fetchUser() {
      if (!this.token) 
        return
      try {
        const response = await api.get<null, AxiosResponse<OwnerInfoDTO>>('/owners/me')
        this.user = response.data
      } catch {
        this.logout()
      }
    },

    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
    },
  },
  
})
