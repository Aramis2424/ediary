import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { EntryCard } from '@/types/EntryCard.ts';

export const getEntryCards = async (diaryId: number): Promise<AxiosResponse<EntryCard[]>> => {
  const res: AxiosResponse<EntryCard[]> = await api.get(`/diaries/${diaryId}/entry-cards`);
  return res;
};
