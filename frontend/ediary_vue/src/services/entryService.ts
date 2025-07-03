import { postEntry, getEntry } from '@/api/entryApi';
import type { EntryInfoDTO, EntryCreateDTO } from '@/types/Entry';

export const fetchEntry = async (entryId: number): Promise<EntryInfoDTO> => {
    try {
        const res = await getEntry(entryId);
        if (!res.data || res.status !== 200) {
            throw new Error('Cannot fetch entry');
        }
        return res.data;
    } catch (error: any) {
        if (error.response?.status === 404) {
            throw new Error("Entry not found");
        } else if (error.response?.status === 403) {
            throw new Error("Access denied");
        } else {
        throw error;
        }
    }
};

export const createEntry = async (diaryId: number): Promise<EntryInfoDTO> => {
    const newEntry: EntryCreateDTO = {
        diaryId: String(diaryId),
        title: "Новый день",
        content: ""
    }
    try {
        const res = await postEntry(newEntry);
        if (!res.data || res.status !== 201) {
            throw new Error('Cannot create entry');
        }
        return res.data;
    } catch (error: any) {
        throw error;
    }
};
