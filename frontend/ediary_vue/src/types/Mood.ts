export interface Mood {
    id: number;
    ownerID: number;
    scoreMood: number;
    scoreProductivity: number;
    bedtime: string;
    wakeUpTime: string;
    createdDate: string;
}

export interface MoodCreateDTO {
    ownerID: number;
    scoreMood: number;
    scoreProductivity: number;
    bedtime: string;
    wakeUpTime: string;
}

export interface MoodUpdateDTO {
    scoreMood: number;
    scoreProductivity: number;
    bedtime: string;
    wakeUpTime: string;
}

export interface MoodInfoDTO {
    id: number;
    scoreMood: number;
    scoreProductivity: number;
    bedtime: string;
    wakeUpTime: string;
    createdDate: string;
}
