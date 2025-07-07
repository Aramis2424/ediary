import { getEntryCards } from '@/api/entryCardApi';
import type { EntryCard } from '@/types/EntryCard';

export const fetchEntryCards = async (diaryId: number): Promise<EntryCard[]> => {
    try {
        const res = await getEntryCards(diaryId);
        if (!res.data || res.status !== 200) {
            throw new Error('Cannot fetch entry');
        }
        return res.data;
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
