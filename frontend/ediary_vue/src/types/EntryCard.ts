export interface EntryCard {
    diaryId: number;
    entryId: number;
    title: string;
    scoreMood: number;
    scoreProductivity: number;
    createdDate: string;
}

export interface EntryCardFilter {
    title: string | null;
    dateFrom: string | null;
    dateTo: string | null;
}
