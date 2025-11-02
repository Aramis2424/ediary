import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { MoodCreateDTO, MoodInfoDTO } from '@/types/Mood.ts';

export const postMood = async (newMood: MoodCreateDTO): Promise<AxiosResponse<MoodInfoDTO>> => {
  const res = await api.post<MoodInfoDTO, AxiosResponse<MoodInfoDTO>, MoodCreateDTO>(
                '/moods', newMood)
  return res;
};

export const getMoods = async (ownerId: number): Promise<AxiosResponse<MoodInfoDTO[]>> => {
  const res: AxiosResponse<MoodInfoDTO[]> = await api.get(`/owners/${ownerId}/moods`);
  return res;
};

export const getPermissionMood = async (ownerId: number, date: string): Promise<AxiosResponse<void>> => {
  const res: AxiosResponse<void> = await api.head(`/owners/${ownerId}/moods`, {
    params: {
      date: date
    }
  });
  return res;
}
