import { defineStore } from 'pinia'

export const useOwnerStore = defineStore('owner', {
  state: () => ({
    id: 0,
    name: '',
    token: '',
  }),
  actions: {
    login(owner: { id: number; name: string; token: string; }) {
      this.id = owner.id
      this.name = owner.name
      this.token = owner.token
    },
    logout() {
      this.id = 0
      this.name = ''
      this.token = ''
    },
  },
})
