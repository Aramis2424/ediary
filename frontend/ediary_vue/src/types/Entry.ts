export interface Entry{
    id: number;
    diaryID: number;
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
    diaryID: string;
    title: string;
    content: string;
}

export interface EntryUpdateDTO {
    title: string;
    content: string;
}

export interface EntryPermissionRes {
    allowed: boolean
}
