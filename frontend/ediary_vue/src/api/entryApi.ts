import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { EntryInfoDTO, EntryCreateDTO } from '@/types/Entry.ts';

export const getEntry = async (entryId: number): Promise<AxiosResponse<EntryInfoDTO>> => {
  const res: AxiosResponse<EntryInfoDTO> = await api.get(`/entries/${entryId}`);
  return res;
};

export const postEntry = async (newEntry: EntryCreateDTO): Promise<AxiosResponse<EntryInfoDTO>> => {
  const res = await api.post<EntryInfoDTO, AxiosResponse<EntryInfoDTO>, EntryCreateDTO>(
                '/entries/', newEntry)
  return res;
};
