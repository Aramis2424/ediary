import type { MoodInfoDTO, MoodCreateDTO, MoodUpdateDTO } from "@/types/Mood";

export const moodsInfo: MoodInfoDTO[] = [
    { "id":1, "scoreMood":1, "scoreProductivity":2, "bedtime":"2021-09-02T04:22:20", "wakeUpTime":"2021-09-02T07:22:20", "createdDate":"2023-05-17" },
    { "id":2, "scoreMood":9, "scoreProductivity":7, "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26", "createdDate":"2021-01-18" },
    { "id":3, "scoreMood":3, "scoreProductivity":2, "bedtime":"2023-10-29T22:38:54", "wakeUpTime":"2023-10-30T01:38:54", "createdDate":"2020-05-21" },
    { "id":4, "scoreMood":3, "scoreProductivity":2, "bedtime":"2021-02-08T17:44:52", "wakeUpTime":"2021-02-08T20:44:52", "createdDate":"2022-04-03" },
    { "id":5, "scoreMood":4, "scoreProductivity":9, "bedtime":"2022-01-25T09:58:24", "wakeUpTime":"2022-01-25T10:58:24", "createdDate":"2020-07-25" },
    { "id":6, "scoreMood":1, "scoreProductivity":6, "bedtime":"2020-02-13T01:19:20", "wakeUpTime":"2020-02-13T09:19:20", "createdDate":"2023-11-15" },
    { "id":7, "scoreMood":2, "scoreProductivity":8, "bedtime":"2021-02-11T12:38:01", "wakeUpTime":"2021-02-11T20:38:01", "createdDate":"2022-10-18" },
    { "id":8, "scoreMood":5, "scoreProductivity":9, "bedtime":"2021-10-21T22:01:00", "wakeUpTime":"2021-10-22T01:01:00", "createdDate":"2022-11-17" },
    { "id":9, "scoreMood":5, "scoreProductivity":3, "bedtime":"2020-06-02T05:00:01", "wakeUpTime":"2020-06-02T07:00:01", "createdDate":"2023-11-05" },
    { "id":10, "scoreMood":10, "scoreProductivity":6, "bedtime":"2023-04-14T17:33:09", "wakeUpTime":"2023-04-14T21:33:09", "createdDate":"2022-02-21" },
    { "id":11, "scoreMood":8, "scoreProductivity":5, "bedtime":"2022-06-19T12:26:06", "wakeUpTime":"2022-06-19T16:26:06", "createdDate":"2021-03-01" },
    { "id":12, "scoreMood":1, "scoreProductivity":5, "bedtime":"2020-02-13T00:06:36", "wakeUpTime":"2020-02-13T05:06:36", "createdDate":"2020-01-10" },
    { "id":45, "scoreMood":6, "scoreProductivity":9, "bedtime":"2020-02-04T00:00:00", "wakeUpTime":"2020-02-04T00:00:00", "createdDate":"2020-03-07" },
    { "id":46, "scoreMood":1, "scoreProductivity":4, "bedtime":"2020-02-03T00:00:00", "wakeUpTime":"2020-02-03T00:00:00", "createdDate":"2020-03-19" },
    { "id":47, "scoreMood":5, "scoreProductivity":2, "bedtime":"2020-02-09T00:00:00", "wakeUpTime":"2020-02-09T00:00:00", "createdDate":"2020-03-04" }
]

export const moodsCreate: MoodCreateDTO[] = [
    { "ownerId":1, "scoreMood":1, "scoreProductivity":2, "bedtime":"2021-09-02T04:22:20", "wakeUpTime":"2021-09-02T07:22:20" },
    { "ownerId":2, "scoreMood":9, "scoreProductivity":7, "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26" },
    { "ownerId":2, "scoreMood":3, "scoreProductivity":2, "bedtime":"2023-10-29T22:38:54", "wakeUpTime":"2023-10-30T01:38:54" },
    { "ownerId":3, "scoreMood":3, "scoreProductivity":2, "bedtime":"2021-02-08T17:44:52", "wakeUpTime":"2021-02-08T20:44:52" },
    { "ownerId":3, "scoreMood":4, "scoreProductivity":9, "bedtime":"2022-01-25T09:58:24", "wakeUpTime":"2022-01-25T10:58:24" },
    { "ownerId":4, "scoreMood":1, "scoreProductivity":6, "bedtime":"2020-02-13T01:19:20", "wakeUpTime":"2020-02-13T09:19:20" },
    { "ownerId":5, "scoreMood":2, "scoreProductivity":8, "bedtime":"2021-02-11T12:38:01", "wakeUpTime":"2021-02-11T20:38:01" },
    { "ownerId":5, "scoreMood":5, "scoreProductivity":9, "bedtime":"2021-10-21T22:01:00", "wakeUpTime":"2021-10-22T01:01:00" },
    { "ownerId":6, "scoreMood":5, "scoreProductivity":3, "bedtime":"2020-06-02T05:00:01", "wakeUpTime":"2020-06-02T07:00:01" },
    { "ownerId":8, "scoreMood":10, "scoreProductivity":6, "bedtime":"2023-04-14T17:33:09", "wakeUpTime":"2023-04-14T21:33:09" },
    { "ownerId":8, "scoreMood":8, "scoreProductivity":5, "bedtime":"2022-06-19T12:26:06", "wakeUpTime":"2022-06-19T16:26:06" },
    { "ownerId":8, "scoreMood":1, "scoreProductivity":5, "bedtime":"2020-02-13T00:06:36", "wakeUpTime":"2020-02-13T05:06:36" },
    { "ownerId":11, "scoreMood":6, "scoreProductivity":9, "bedtime":"2020-02-04T00:00:00", "wakeUpTime":"2020-02-04T00:00:00" },
    { "ownerId":11, "scoreMood":1, "scoreProductivity":4, "bedtime":"2020-02-03T00:00:00", "wakeUpTime":"2020-02-03T00:00:00" },
    { "ownerId":11, "scoreMood":5, "scoreProductivity":2, "bedtime":"2020-02-09T00:00:00", "wakeUpTime":"2020-02-09T00:00:00" }
]

export const moodsUpdate: MoodUpdateDTO[] = [
    { "scoreMood":1, "scoreProductivity":2, "bedtime":"2021-09-02T04:22:20", "wakeUpTime":"2021-09-02T07:22:20" },
    { "scoreMood":9, "scoreProductivity":7, "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26" },
    { "scoreMood":3, "scoreProductivity":2, "bedtime":"2023-10-29T22:38:54", "wakeUpTime":"2023-10-30T01:38:54" },
    { "scoreMood":3, "scoreProductivity":2, "bedtime":"2021-02-08T17:44:52", "wakeUpTime":"2021-02-08T20:44:52" },
    { "scoreMood":4, "scoreProductivity":9, "bedtime":"2022-01-25T09:58:24", "wakeUpTime":"2022-01-25T10:58:24" },
    { "scoreMood":1, "scoreProductivity":6, "bedtime":"2020-02-13T01:19:20", "wakeUpTime":"2020-02-13T09:19:20" },
    { "scoreMood":2, "scoreProductivity":8, "bedtime":"2021-02-11T12:38:01", "wakeUpTime":"2021-02-11T20:38:01" },
    { "scoreMood":5, "scoreProductivity":9, "bedtime":"2021-10-21T22:01:00", "wakeUpTime":"2021-10-22T01:01:00" },
    { "scoreMood":5, "scoreProductivity":3, "bedtime":"2020-06-02T05:00:01", "wakeUpTime":"2020-06-02T07:00:01" },
    { "scoreMood":10, "scoreProductivity":6, "bedtime":"2023-04-14T17:33:09", "wakeUpTime":"2023-04-14T21:33:09" },
    { "scoreMood":8, "scoreProductivity":5, "bedtime":"2022-06-19T12:26:06", "wakeUpTime":"2022-06-19T16:26:06" },
    { "scoreMood":1, "scoreProductivity":5, "bedtime":"2020-02-13T00:06:36", "wakeUpTime":"2020-02-13T05:06:36" },
    { "scoreMood":6, "scoreProductivity":9, "bedtime":"2020-02-04T00:00:00", "wakeUpTime":"2020-02-04T00:00:00" },
    { "scoreMood":1, "scoreProductivity":4, "bedtime":"2020-02-03T00:00:00", "wakeUpTime":"2020-02-03T00:00:00" },
    { "scoreMood":5, "scoreProductivity":2, "bedtime":"2020-02-09T00:00:00", "wakeUpTime":"2020-02-09T00:00:00" }
]
