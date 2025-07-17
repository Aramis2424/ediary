import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { MoodCreateDTO, MoodInfoDTO, MoodPermissionRes } from '@/types/Mood.ts';

export const postMood = async (newMood: MoodCreateDTO): Promise<AxiosResponse<MoodInfoDTO>> => {
  const res = await api.post<MoodInfoDTO, AxiosResponse<MoodInfoDTO>, MoodCreateDTO>(
                '/moods', newMood)
  return res;
};

export const getMoods = async (ownerId: number): Promise<AxiosResponse<MoodInfoDTO[]>> => {
  const res: AxiosResponse<MoodInfoDTO[]> = await api.get(`/owners/${ownerId}/moods`);
  return res;
};

export const getPermissionMood = async (ownerId: number, date: string): Promise<AxiosResponse<MoodPermissionRes>> => {
  const res: AxiosResponse<MoodPermissionRes> = await api.get(`/owners/${ownerId}/can-create-mood`, {
    params: {
      date: date
    }
  });
  return res;
}
