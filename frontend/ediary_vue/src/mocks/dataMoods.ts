import type { Mood, MoodInfoDTO, MoodCreateDTO, MoodUpdateDTO } from "@/types/Mood";

export const moods: Mood[] = [
    { "id":1, "ownerID":1, "scoreMood":1, "scoreProductivity":2, "bedtime":"2021-09-02T04:22:20", "wakeUpTime":"2021-09-02T07:22:20", "createdAt":"2023-05-17" },
    { "id":2, "ownerID":2, "scoreMood":9, "scoreProductivity":7, "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26", "createdAt":"2021-01-18" },
    { "id":3, "ownerID":2, "scoreMood":3, "scoreProductivity":2, "bedtime":"2023-10-29T22:38:54", "wakeUpTime":"2023-10-30T01:38:54", "createdAt":"2020-05-21" },
    { "id":4, "ownerID":3, "scoreMood":3, "scoreProductivity":2, "bedtime":"2021-02-08T17:44:52", "wakeUpTime":"2021-02-08T20:44:52", "createdAt":"2022-04-03" },
    { "id":5, "ownerID":3, "scoreMood":4, "scoreProductivity":9, "bedtime":"2022-01-25T09:58:24", "wakeUpTime":"2022-01-25T10:58:24", "createdAt":"2020-07-25" },
    { "id":6, "ownerID":4, "scoreMood":1, "scoreProductivity":6, "bedtime":"2020-02-13T01:19:20", "wakeUpTime":"2020-02-13T09:19:20", "createdAt":"2023-11-15" },
    { "id":7, "ownerID":5, "scoreMood":2, "scoreProductivity":8, "bedtime":"2021-02-11T12:38:01", "wakeUpTime":"2021-02-11T20:38:01", "createdAt":"2022-10-18" },
    { "id":8, "ownerID":5, "scoreMood":5, "scoreProductivity":9, "bedtime":"2021-10-21T22:01:00", "wakeUpTime":"2021-10-22T01:01:00", "createdAt":"2022-11-17" },
    { "id":9, "ownerID":6, "scoreMood":5, "scoreProductivity":3, "bedtime":"2020-06-02T05:00:01", "wakeUpTime":"2020-06-02T07:00:01", "createdAt":"2023-11-05" },
    { "id":10, "ownerID":8, "scoreMood":10, "scoreProductivity":6, "bedtime":"2023-04-14T17:33:09", "wakeUpTime":"2023-04-14T21:33:09", "createdAt":"2022-02-21" },
    { "id":11, "ownerID":8, "scoreMood":8, "scoreProductivity":5, "bedtime":"2022-06-19T12:26:06", "wakeUpTime":"2022-06-19T16:26:06", "createdAt":"2021-03-01" },
    { "id":12, "ownerID":8, "scoreMood":1, "scoreProductivity":5, "bedtime":"2020-02-13T00:06:36", "wakeUpTime":"2020-02-13T05:06:36", "createdAt":"2020-01-10" },
    { "id":45, "ownerID":11, "scoreMood":6, "scoreProductivity":9, "bedtime":"2020-02-04T00:30:00", "wakeUpTime":"2020-02-04T07:00:00", "createdAt":"2025-06-07" },
    { "id":46, "ownerID":11, "scoreMood":1, "scoreProductivity":4, "bedtime":"2020-02-03T01:00:00", "wakeUpTime":"2020-02-03T08:30:00", "createdAt":"2025-06-09" },
    { "id":47, "ownerID":11, "scoreMood":5, "scoreProductivity":2, "bedtime":"2020-02-09T00:10:00", "wakeUpTime":"2020-02-09T08:00:00", "createdAt":"2025-06-10" },

    { "id":48, "ownerID":11, "scoreMood":6, "scoreProductivity":9, "bedtime":"2020-02-10T00:30:00", "wakeUpTime":"2020-02-04T07:00:00", "createdAt":"2025-06-11" },
    { "id":49, "ownerID":11, "scoreMood":1, "scoreProductivity":4, "bedtime":"2020-02-11T01:00:00", "wakeUpTime":"2020-02-03T08:30:00", "createdAt":"2025-06-12" },
    { "id":50, "ownerID":11, "scoreMood":5, "scoreProductivity":2, "bedtime":"2020-02-12T00:10:00", "wakeUpTime":"2020-02-09T08:00:00", "createdAt":"2025-06-13" },
    { "id":51, "ownerID":11, "scoreMood":6, "scoreProductivity":9, "bedtime":"2020-02-13T00:30:00", "wakeUpTime":"2020-02-04T07:00:00", "createdAt":"2025-06-14" },
    { "id":52, "ownerID":11, "scoreMood":1, "scoreProductivity":4, "bedtime":"2020-02-14T01:00:00", "wakeUpTime":"2020-02-03T08:30:00", "createdAt":"2025-06-15" },
    { "id":53, "ownerID":11, "scoreMood":5, "scoreProductivity":2, "bedtime":"2020-02-15T00:10:00", "wakeUpTime":"2020-02-09T08:00:00", "createdAt":"2025-06-16" },
    { "id":54, "ownerID":11, "scoreMood":6, "scoreProductivity":9, "bedtime":"2020-02-16T00:30:00", "wakeUpTime":"2020-02-04T07:00:00", "createdAt":"2025-06-17" },
    { "id":55, "ownerID":11, "scoreMood":1, "scoreProductivity":4, "bedtime":"2020-02-17T01:00:00", "wakeUpTime":"2020-02-03T08:30:00", "createdAt":"2025-06-18" },
    { "id":56, "ownerID":11, "scoreMood":5, "scoreProductivity":2, "bedtime":"2020-02-18T00:10:00", "wakeUpTime":"2020-02-09T08:00:00", "createdAt":"2025-06-19" }
]

export function toInfoDto(obj: Mood): MoodInfoDTO {
    return { id: obj.id, scoreMood: obj.scoreMood, scoreProductivity: obj.scoreProductivity, 
        bedtime: obj.bedtime, wakeUpTime: obj.wakeUpTime, createdAt: obj.createdAt }
}

export function toCreateDto(obj: Mood): MoodCreateDTO {
    return { ownerID: obj.ownerID, scoreMood: obj.scoreMood, scoreProductivity: obj.scoreProductivity, 
        bedtime: obj.bedtime, wakeUpTime: obj.wakeUpTime }
}

export function toUpdateDto(obj: Mood): MoodUpdateDTO {
    return { scoreMood: obj.scoreMood, scoreProductivity: obj.scoreProductivity, 
        bedtime: obj.bedtime, wakeUpTime: obj.wakeUpTime }
}
