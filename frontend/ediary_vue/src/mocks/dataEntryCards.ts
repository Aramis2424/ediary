import type { EntryCard } from "@/types/EntryCard";
import { entries } from "./dataEntries";

export function getCards(): EntryCard[] {
    let cards: EntryCard[] = []
    for (const entry of entries) {
        cards.push(
            { diaryId: entry.diaryId, entryId: entry.id, title: entry.title, 
                scoreMood: Math.floor(Math.random() * 10) + 1, 
                scoreProductivity: Math.floor(Math.random() * 10) + 1, 
                createdDate: entry.createdDate }
        )
    }
    return cards
}
