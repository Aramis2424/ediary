export interface Diary {
    id: number;
    ownerID: number;
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
    ownerID: number;
    title: string;
    description: string;
}
