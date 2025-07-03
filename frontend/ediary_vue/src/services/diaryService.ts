import { createDiary, getDiaries } from '@/api/diaryApi';
import type { DiaryInfoDTO, DiaryCreateDTO } from '@/types/Diary';

export const fetchDiary = async (ownerId: number): Promise<DiaryInfoDTO> => {
    try {
        const diaries: DiaryInfoDTO[] = await getDiaries(ownerId);
        return diaries[0];
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

export const saveDiary = async (ownerId: number): Promise<DiaryInfoDTO> => {
    const newDiary: DiaryCreateDTO = {
        ownerId: ownerId,
        title: "New diary",
        description: "Description for new diary"
      }
    return await createDiary(newDiary);
};
