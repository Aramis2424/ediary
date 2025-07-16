import { defineStore } from 'pinia'
import type { DiaryInfoDTO } from '@/types/Diary';

export const useDiaryStore = defineStore('diary', {
  state: (): DiaryInfoDTO => ({
    id: 0,
    title: '',
    description: '',
    cntEntries: 0,
    createdDate: '',
  }),
  actions: {
    save(diary: DiaryInfoDTO) {
      this.id = diary.id
      this.title = diary.title
      this. description = diary.description
      this.cntEntries = diary.cntEntries
      this.createdDate = ''
    },
    remove() {
        this.id = 0,
        this.title = ''
        this.description = ''
        this.cntEntries = 0,
        this.createdDate = ''
    },
  },
  persist: true,
})
