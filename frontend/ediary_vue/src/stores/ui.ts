import { defineStore } from 'pinia'

export const useUiStore = defineStore('ui', {
  state: () => ({
    showSettings: false,
    showAboutYourself: false,
    showSearching: false,
  }),
  actions: {
    gotoHome(router: any) {
      router.push('/home')
    },
    gotoMenu(router: any) {
      router.push('/menu')
    },
  },
})
