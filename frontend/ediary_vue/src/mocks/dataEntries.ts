import type { Entry, EntryInfoDTO, EntryCreateDTO, EntryUpdateDTO } from "@/types/Entry";

export const entries: Entry[] = [
    { "id":1, "diaryId":3, "title":"Break.", "content":"Well never politics expect reason rock professional. Somebody close food anything. Deep card much research.", "createdDate":"2022-10-10" },
    { "id":2, "diaryId":3, "title":"Ten everything.", "content":"Indeed window daughter nothing. Type beat base state. By result clear we.", "createdDate":"2022-12-07" },
    { "id":3, "diaryId":3, "title":"Newspaper member.", "content":"Inside report me water image TV. Us read she fund begin speech fly size. Water soldier he action local major picture.", "createdDate":"2022-10-29" },
    { "id":4, "diaryId":4, "title":"Might born.", "content":"Respond happen wrong our reveal before provide suggest. Which low half air nation above relate. Growth pretty nation first past ten available program.", "createdDate":"2023-07-17" },
    { "id":5, "diaryId":4, "title":"Baby.", "content":"Open myself line bring put budget soldier. Mission east bring smile. Force ahead west military.", "createdDate":"2023-11-27" },
    { "id":6, "diaryId":6, "title":"Republican statement.", "content":"Throughout discussion article player create attention send. Clear north trade nice. Section professor know.", "createdDate":"2020-01-27" },
    { "id":7, "diaryId":6, "title":"Sit.", "content":"Happy rise shoulder probably happen draw. Concern education wrong interest sense. Close central specific main those hear.", "createdDate":"2023-02-09" },
    { "id":8, "diaryId":6, "title":"Relate.", "content":"Own interesting hotel there animal moment politics. Will charge today condition meeting use even relationship. Practice little model country choice some.", "createdDate":"2022-09-20" },
    { "id":9, "diaryId":6, "title":"Trade.", "content":"Lose more number analysis state western. Might ahead word throw front statement summer. Development star crime home weight.", "createdDate":"2020-10-08" },
    { "id":10, "diaryId":8, "title":"Rather.", "content":"Rest care behavior line quickly case. Together number subject some. Finish produce hospital operation.", "createdDate":"2021-06-24" },
    { "id":11, "diaryId":9, "title":"Stop century.", "content":"Open quality short character hospital strong begin. Nothing especially may ok person. Common choose relate action point bank.", "createdDate":"2023-04-28" },
    { "id":12, "diaryId":9, "title":"Will.", "content":"There scene soldier quality staff character. Realize red alone enjoy officer performance size everyone. Material once my measure offer.", "createdDate":"2021-11-25" },
    { "id":13, "diaryId":11, "title":"Southern.", "content":"Small surface light important painting why. Child happy thing score open. Collection inside instead available bad environmental.", "createdDate":"2022-10-23" },
    { "id":14, "diaryId":11, "title":"Defense.", "content":"My once parent near. Fire loss hard ok billion. Seek always degree.", "createdDate":"2022-07-27" },
    { "id":15, "diaryId":11, "title":"Leave skin.", "content":"Threat enjoy third expert benefit. So news pull age deep time peace. South maybe effort past challenge how.", "createdDate":"2022-05-23" },
    { "id":16, "diaryId":11, "title":"Issue.", "content":"Practice as everything shoulder both enter. Suggest rich cup large federal onto. Effort wind grow why board vote.", "createdDate":"2020-02-23" },
    { "id":17, "diaryId":12, "title":"Particular husband.", "content":"Baby ten down course right. He born spend state affect subject more others. Low our chair middle evening allow card major.", "createdDate":"2021-02-21" },
    { "id":18, "diaryId":12, "title":"Current.", "content":"Around eat dinner itself whose power firm. Tax cell himself hundred remember reason. Structure less raise recognize win live.", "createdDate":"2023-03-09" },
    { "id":19, "diaryId":12, "title":"Home side.", "content":"Direction but door lay. Century care PM leg thought. Over his attention seven.", "createdDate":"2021-05-09" },
    { "id":20, "diaryId":12, "title":"Food.", "content":"Own candidate pass painting Congress. Everyone strong wide skill visit physical finally. Air event wife voice glass somebody.", "createdDate":"2023-03-24" },
    { "id":21, "diaryId":34, "title":"For.", "content":"Serious radio kitchen down weight improve art risk. View him prepare answer. Bit some artist behind.", "createdDate":"2022-01-10" },
    { "id":61, "diaryId":34, "title":"Continue skin.", "content":"Far throw popular article reason list particularly. Realize under rate point time executive budget. Stuff moment example similar.", "createdDate":"2020-03-02" },
    { "id":62, "diaryId":34, "title":"Pm.", "content":"Argue possible maybe bed. Court future reality. South be affect arrive clearly.", "createdDate":"2020-03-08" },
    { "id":63, "diaryId":34, "title":"Wife term.", "content":"Wife meeting fight break deal skill. Against interest ball executive card west. Could home population choose.", "createdDate":"2020-03-10" }
]

export function toInfoDto(obj: Entry): EntryInfoDTO {
    return { id: obj.id, title: obj.title, content: obj.content, createdDate: obj.createdDate }
}

export function toCreateDto(obj: Entry): EntryCreateDTO {
    return { diaryId: obj.diaryId, title: obj.title, content: obj.content }
}

export function toUpdateDto(obj: Entry): EntryUpdateDTO {
    return { title: obj.title, content: obj.content }
}
