import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { DiaryInfoDTO, DiaryCreateDTO } from '@/types/Diary.ts';

export const getDiaries = async (ownerId: number): Promise<AxiosResponse<DiaryInfoDTO[]>> => {
  const res: AxiosResponse<DiaryInfoDTO[]> = await api.get(`/owners/${ownerId}/diaries`);
  return res;
};

export const postDiary = async (newDiary: DiaryCreateDTO): Promise<AxiosResponse<DiaryInfoDTO>> => {
  const res = await api.post<DiaryInfoDTO, AxiosResponse<DiaryInfoDTO>, DiaryCreateDTO>(
                '/diaries', newDiary)
  return res;
};
