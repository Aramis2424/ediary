import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { EntryCard } from '@/types/EntryCard.ts';
import type { EntryCardFilter } from '@/types/EntryCard'

export const getEntryCards = async (diaryId: number, filters: EntryCardFilter): Promise<AxiosResponse<EntryCard[]>> => {
  const params: EntryCardFilter = {
    title: '',
    dateFrom: '',
    dateTo: ''
  }

  if (filters.title) params.title = filters.title
  if (filters.dateFrom) params.dateFrom = filters.dateFrom
  if (filters.dateTo) params.dateTo = filters.dateTo

  const res: AxiosResponse<EntryCard[]> = await api.get(`/diaries/${diaryId}/entry-cards`, { params });
  return res;
};
