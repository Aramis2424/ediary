import { defineStore } from 'pinia'
import type { DiaryInfoDTO } from '@/types/Diary';

export const useOwnerStore = defineStore('diary', {
  state: (): DiaryInfoDTO => ({
    id: 0,
    title: '',
    description: '',
    cntEntries: 0,
    createdDate: '',
  }),
  actions: {
    logIn(diary: DiaryInfoDTO) {
      this.id = 0
      this.title = diary.title
      this. description = diary.description
      this.cntEntries = 0
      this.createdDate = ''
    },
    logOut() {
        this.id = 0,
        this.title = ''
        this.description = ''
        this.cntEntries = 0,
        this.createdDate = ''
    },
  },
  persist: true,
})
