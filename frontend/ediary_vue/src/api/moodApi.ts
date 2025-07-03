import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { MoodCreateDTO, MoodInfoDTO } from '@/types/Mood.ts';

export const postMood = async (newMood: MoodCreateDTO): Promise<AxiosResponse<MoodInfoDTO>> => {
  const res = await api.post<MoodInfoDTO, AxiosResponse<MoodInfoDTO>, MoodCreateDTO>(
                '/moods/', newMood)
  return res;
};
