import { defineStore } from 'pinia'
import type { OwnerInfoDTO, TokenResponse } from '@/types/Owner';

export const useOwnerStore = defineStore('owner', {
  state: (): OwnerInfoDTO => ({
    id: 0,
    name: '',
    birthDate: '',
    login: '',
    createdDate: '',
  }),
  getters: {
    isLoggedIn: (state) => !!state.id,
  },
  actions: {
    logIn(owner: OwnerInfoDTO) {
      this.id = owner.id
      this.name = owner.name
      this.birthDate = owner.birthDate
      this.login = owner.login
      this.createdDate = owner.createdDate
    },
    logOut() {
      this.id = 0
      this.name = ''
    },
  },
  persist: true,
})

export const useTokenStore = defineStore('token', {
  state: (): TokenResponse => ({
    token: '',
  }),
  actions: {
    logIn(response: TokenResponse) {
      this.token = response.token
    },
    logOut() {
      this.token = ''
    },
  },
  persist: true,
})
