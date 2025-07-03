import { postDiary, getDiaries } from '@/api/diaryApi';
import type { DiaryInfoDTO, DiaryCreateDTO } from '@/types/Diary';

export const fetchDiary = async (ownerId: number): Promise<DiaryInfoDTO> => {
    try {
        const res = await getDiaries(ownerId);
        if (!res.data || res.status !== 200) {
            throw new Error('Cannot fetch diary');
        }
        return res.data[0];
    } catch (error: any) {
        if (error.response?.status === 404) {
            throw new Error("Diary not found");
        } else if (error.response?.status === 403) {
            throw new Error("Access denied");
        } else {
        throw error;
        }
    }
};

export const createDiary = async (ownerId: number): Promise<DiaryInfoDTO> => {
    const newDiary: DiaryCreateDTO = {
        ownerId: ownerId,
        title: "New diary",
        description: "Description for new diary"
    }
    try {
        const res = await postDiary(newDiary);
        if (!res.data || res.status !== 201) {
            throw new Error('Cannot create diary');
        }
        return res.data;
    } catch (error: any) {
        throw error;
    }
};
