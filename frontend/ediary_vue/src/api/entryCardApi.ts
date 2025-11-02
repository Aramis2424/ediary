import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { EntryCard } from '@/types/EntryCard.ts';
import type { EntryCardFilter } from '@/types/EntryCard'

export const getEntryCards = async (diaryId: number, filters: EntryCardFilter): Promise<AxiosResponse<EntryCard[]>> => {
  const params: EntryCardFilter = {
    title: '',
    date_from: '',
    date_to: ''
  }

  if (filters.title) params.title = filters.title
  if (filters.date_from) params.date_from = filters.date_from
  if (filters.date_to) params.date_to = filters.date_to

  const res: AxiosResponse<EntryCard[]> = await api.get(`/diaries/${diaryId}/entry-cards`, { params });
  return res;
};
