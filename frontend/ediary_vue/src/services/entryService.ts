import { createEntry, getEntry } from '@/api/entryApi';
import type { EntryInfoDTO, EntryCreateDTO } from '@/types/Entry';

export const fetchEntry = async (entryId: number): Promise<EntryInfoDTO> => {
    return await getEntry(entryId);
};

export const saveEntry = async (diaryId: number): Promise<EntryInfoDTO> => {
    const newEntry: EntryCreateDTO = {
        diaryId: String(diaryId),
        title: "Новый день",
        content: ""
    }
    return await createEntry(newEntry);
};
