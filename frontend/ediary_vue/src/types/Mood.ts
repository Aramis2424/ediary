export interface MoodCreateDTO {
    ownerId: number;
    scoreMood: number;
    scoreProductivity: number;
    bedtime: Date;
    wakeUpTime: Date;
}

export interface MoodUpdateDTO {
    scoreMood: number;
    scoreProductivity: number;
    bedtime: Date;
    wakeUpTime: Date;
}

export interface MoodInfoDTO {
    id: number;
    scoreMood: number;
    scoreProductivity: number;
    bedtime: Date;
    wakeUpTime: Date;
    createdAt: Date;
}
