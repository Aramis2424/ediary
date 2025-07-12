import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { EntryInfoDTO, EntryCreateDTO, EntryUpdateDTO, EntryPermissionRes } from '@/types/Entry.ts';

export const getEntry = async (entryId: number): Promise<AxiosResponse<EntryInfoDTO>> => {
  const res: AxiosResponse<EntryInfoDTO> = await api.get(`/entries/${entryId}`);
  return res;
};

export const postEntry = async (newEntry: EntryCreateDTO): Promise<AxiosResponse<EntryInfoDTO>> => {
  const res = await api.post<EntryInfoDTO, AxiosResponse<EntryInfoDTO>, EntryCreateDTO>(
                '/entries', newEntry)
  return res;
};

export const putEntry = async (entryId: number, updatedEntry: EntryUpdateDTO): Promise<AxiosResponse<EntryInfoDTO>> => {
  const res = await api.put<EntryInfoDTO, AxiosResponse<EntryInfoDTO>, EntryUpdateDTO>(
                `/entries/${entryId}`, updatedEntry)
  return res;
};

export const deleteEntry = async (entryId: number): Promise<AxiosResponse<void>> => {
  const res: AxiosResponse<void> = await api.delete(`/entries/${entryId}`);
  return res;
};

export const getPermissionEntry = async (diaryId: number): Promise<AxiosResponse<EntryPermissionRes>> => {
  const res: AxiosResponse<EntryPermissionRes> = await api.get(`/diaries/${diaryId}/can-create-entry`);
  return res;
}
