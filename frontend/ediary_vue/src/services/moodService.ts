import { postMood } from '@/api/moodApi';
import type { MoodCreateDTO, MoodInfoDTO } from '@/types/Mood';
import dayjs from 'dayjs'

export const createMood = async (ownerID: number, sP: number, sM: number, 
    bedtime: string, wakeUpTime: string): Promise<MoodInfoDTO> => {

    const newMood: MoodCreateDTO = await buildMood(
        ownerID, sP, sM, bedtime, wakeUpTime
    )
    try {
        const res = await postMood(newMood);
        if (!res.data || res.status !== 201) {
            throw new Error('Cannot create mood');
        }
        return res.data;
    } catch (error: any) {
        throw error;
    }
};

async function buildMood(ownerID:number, sP: number, sM: number, 
    bedtime: string, wakeUpTime: string): Promise<MoodCreateDTO> {

  const [bedDateTime, wakeUpDateTime] = await Promise.all([
    resolveDateTime(bedtime),
    resolveDateTime(wakeUpTime)
    ])

  const builtMood: MoodCreateDTO = {
    ownerID: ownerID,
    scoreMood: sM,
    scoreProductivity: sP,
    bedtime: bedDateTime,
    wakeUpTime: wakeUpDateTime,
  }
  return builtMood
}

async function resolveDateTime(timeStr: string): Promise<string> {
  const [hours, minutes] = timeStr.split(':').map(Number)

  const now = dayjs()
  const twenty = dayjs().hour(20).minute(0).second(0)

  const inputTime = dayjs().hour(hours).minute(minutes).second(0)

  const date = inputTime.isBefore(twenty) ? now : now.subtract(1, 'day')

  const result = date.format('YYYY-MM-DD') + `T${timeStr}:00`
  return result
}
