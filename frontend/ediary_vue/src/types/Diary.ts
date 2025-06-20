export interface Diary {
    id: number;
    ownerId: number;
    title: string;
    description: string;
    cntEntries: number;
    createdDate: string;
}

export interface DiaryInfoDTO {
    id: number;
    title: string;
    description: string;
    cntEntries: number;
    createdDate: string;
}

export interface DiaryCreateDTO {
    ownerId: number;
    title: string;
    description: string;
}
