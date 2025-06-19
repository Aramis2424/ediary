export interface DiaryInfoDTO {
    id: number;
    title: string;
    description: string;
    cntEntry: number;
    createdDate: string;
}

export interface DiaryCreateDTO {
    ownerId: number;
    title: string;
    description: string;
}
