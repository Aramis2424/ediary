import { postMood } from '@/api/moodApi';
import type { MoodCreateDTO, MoodInfoDTO } from '@/types/Mood';

export const createMood = async (newMood: MoodCreateDTO): Promise<MoodInfoDTO> => {
    try {
        const res = await postMood(newMood);
        if (!res.data || res.status !== 201) {
            throw new Error('Cannot create mood');
        }
        return res.data;
    } catch (error: any) {
        throw error;
    }
};
