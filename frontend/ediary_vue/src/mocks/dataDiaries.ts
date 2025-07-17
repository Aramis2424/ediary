import type { Diary, DiaryInfoDTO, DiaryCreateDTO } from "@/types/Diary";

export const diaries: Diary[] = [
    { "id":1, "ownerID":1, "title":"Huge.", "description":"Program method nor.", "cntEntries":0, "createdDate":"2015-09-20" },
    { "id":2, "ownerID":2, "title":"Inside.", "description":"Stay member still what.", "cntEntries":0, "createdDate":"2017-08-07" },
    { "id":3, "ownerID":2, "title":"Evening trouble.", "description":"Above score though according seven Democrat huge.", "cntEntries":3, "createdDate":"2019-10-16" },
    { "id":4, "ownerID":2, "title":"Spring.", "description":"Finish ability attorney for letter high within fund.", "cntEntries":2, "createdDate":"2015-07-03" },
    { "id":5, "ownerID":3, "title":"Free car.", "description":"Throughout form there action year.", "cntEntries":0, "createdDate":"2016-04-04" },
    { "id":6, "ownerID":4, "title":"Together.", "description":"Various improve order serious nearly prevent article son.", "cntEntries":4, "createdDate":"2016-06-08" },
    { "id":7, "ownerID":5, "title":"Seven region.", "description":"Time happen different camera visit education or.", "cntEntries":0, "createdDate":"2019-04-20" },
    { "id":8, "ownerID":6, "title":"Approach measure.", "description":"Everybody woman however meeting organization your government.", "cntEntries":1, "createdDate":"2018-03-05" },
    { "id":9, "ownerID":6, "title":"Nature quickly.", "description":"Significant community foot church house chair.", "cntEntries":2, "createdDate":"2017-09-18" },
    { "id":10, "ownerID":7, "title":"Challenge share.", "description":"Say place someone take.", "cntEntries":0, "createdDate":"2019-01-10" },
    { "id":11, "ownerID":7, "title":"Research house.", "description":"Issue plan prepare indeed.", "cntEntries":4, "createdDate":"2016-08-04" },
    { "id":12, "ownerID":8, "title":"Space.", "description":"Every push manage politics himself arm.", "cntEntries":4, "createdDate":"2019-07-27" },
    { "id":13, "ownerID":9, "title":"Top.", "description":"Amount land run stay someone film.", "cntEntries":1, "createdDate":"2016-12-25" },
    { "id":14, "ownerID":10, "title":"Result.", "description":"May civil huge evidence goal present force.", "cntEntries":0, "createdDate":"2015-07-22" },
    { "id":34, "ownerID":11, "title":"First method.", "description":"Week democratic investment turn cup that this then.", "cntEntries":3, "createdDate":"2020-02-15" },
    { "id":35, "ownerID":11, "title":"There.", "description":"Hold under nation role save draw special.", "cntEntries":3, "createdDate":"2020-02-05" }
]


export function toInfoDto(obj: Diary): DiaryInfoDTO {
    return { id: obj.id, title: obj.title, description: obj.description,
    cntEntries: obj.cntEntries, createdDate: obj.createdDate }
}

export function toCreateDto(obj: Diary): DiaryCreateDTO {
    return { ownerID: obj.ownerID, title: obj.title, description: obj.description }
}
