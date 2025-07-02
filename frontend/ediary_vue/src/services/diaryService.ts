import { createDiary, getDiaries } from '@/api/diaryApi';
import type { DiaryInfoDTO, DiaryCreateDTO } from '@/types/Diary';

export const fetchDiary = async (ownerId: number): Promise<DiaryInfoDTO> => {
    const diaries: DiaryInfoDTO[] = await getDiaries(ownerId);
    if (!Array.isArray(diaries) || diaries.length === 0) {
        throw new Error("No diaries found for this owner");
    }
    return diaries[0];
};

export const saveDiary = async (ownerId: number): Promise<DiaryInfoDTO> => {
    const newDiary: DiaryCreateDTO = {
        ownerId: ownerId,
        title: "New diary",
        description: "Description for new diary"
      }
    return await createDiary(newDiary);
};
