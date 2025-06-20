export interface Entry{
    id: number;
    diaryId: number;
    title: string;
    content: string;
    createdDate: string;
}

export interface EntryInfoDTO {
    id: number;
    title: string;
    content: string;
    createdDate: string;
}

export interface EntryCreateDTO {
    diaryId: number;
    title: string;
    content: string;
}

export interface EntryUpdateDTO {
    title: string;
    content: string;
}
