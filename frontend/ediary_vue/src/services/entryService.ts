import { createEntry, getEntry } from '@/api/entryApi';
import type { EntryInfoDTO } from '@/types/Entry';

export const fetcEntry = async (entryId: number): Promise<EntryInfoDTO> => {
    return await getEntry(entryId);
};

export const saveEntry = async (diaryId: number): Promise<EntryInfoDTO> => {
    const newEntry = {
        diaryId: String(diaryId),
        title: "Новый день",
        content: ""
    }
    return await createEntry(newEntry);
};
