import { getMoods, getPermissionMood, postMood } from '@/api/moodApi';
import type { MoodCreateDTO, MoodInfoDTO, MoodPermissionRes } from '@/types/Mood';
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

export const fetchMoods = async (ownerId: number): Promise<MoodInfoDTO[]> => {
    try {
        const res = await getMoods(ownerId);
        if (!res.data || res.status !== 200) {
            throw new Error('Cannot fetch moods');
        }
        return res.data.sort(
          (a, b) => new Date(a.createdDate).getTime() - new Date(b.createdDate).getTime());
    } catch (error: any) {
        if (error.response?.status === 404) {
            throw new Error("Owner not found");
        } else if (error.response?.status === 403) {
            throw new Error("Access denied");
        } else {
            throw error;
        }
    }
};

export const fetchPermissionMood = async (ownerId: number): Promise<MoodPermissionRes> => {
    try {
        const res = await getPermissionMood(ownerId);
        if (!res.data || res.status !== 200) {
            throw new Error('Cannot get permission for creating mood');
        }
        return res.data;
    } catch (error: any) {
        if (error.response?.status === 404) {
            throw new Error("Owner not found");
        } else if (error.response?.status === 403) {
            throw new Error("Access denied");
        } else {
            throw error;
        }
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
