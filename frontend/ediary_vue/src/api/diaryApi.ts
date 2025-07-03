import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { DiaryInfoDTO, DiaryCreateDTO } from '@/types/Diary.ts';

export const getDiaries = async (ownerId: number): Promise<DiaryInfoDTO[]> => {
  const res: AxiosResponse<DiaryInfoDTO[]> = await api.get(`/owners/${ownerId}/diaries`);
  return res.data;
};

export const createDiary = async (newDiary: DiaryCreateDTO): Promise<DiaryInfoDTO> => {
  const res = await api.post<DiaryInfoDTO, AxiosResponse<DiaryInfoDTO>, DiaryCreateDTO>(
                '/diaries', newDiary)
  return res.data;
};
