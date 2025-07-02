import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { EntryInfoDTO, EntryCreateDTO } from '@/types/Entry.ts';

export const getEntry = async (entryId: number): Promise<EntryInfoDTO> => {
  const res = await api.get(`/api/v1/entries/${entryId}`);
  return res.data;
};

export const createEntry = async (newEntry: EntryCreateDTO): Promise<EntryInfoDTO> => {
  const res: AxiosResponse<EntryInfoDTO> = await api.post<
                    EntryInfoDTO, AxiosResponse<EntryInfoDTO>, EntryCreateDTO>(
  '/api/v1/entries/',
  newEntry
)
  return res.data;
};
