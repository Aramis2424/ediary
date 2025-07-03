import { defineStore } from 'pinia'
import type { OwnerInfoDTO, OwnerCreateDTO, TokenRequest, TokenResponse } from '@/types/Owner'
import { createOwner, fetchOwner, getToken } from '@/api/ownerService'

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
        const response: TokenResponse = await getToken(reqToken)
        this.token = response.token

        localStorage.setItem('token', this.token)
        await this.fetchUser()
      } catch (error: any) {
        throw new Error(`Login failed: ${error}`)
      }
    },

    async register(reqRegister: OwnerCreateDTO) {
      try {
        await createOwner(reqRegister);
        await this.login({
          login: reqRegister.login, 
          password: reqRegister.password})
      } catch (error: any) {
        throw new Error(`Register failed: ${error}`)
      }
    },

    async fetchUser() {
      if (!this.token) 
        return
      try {
        const response = await fetchOwner()
        this.user = response
      } catch {
        this.logout()
      }
    },

    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
      localStorage.removeItem('diary')
    },
  },
  
})
