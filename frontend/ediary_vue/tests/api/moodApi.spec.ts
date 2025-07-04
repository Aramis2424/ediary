import { describe, it, expect, vi, beforeEach } from 'vitest'
import { postMood } from '../../src/api/moodApi'
import { api } from '../../src/api/axios'
import type { MoodCreateDTO, MoodInfoDTO } from '../../src/types/Mood'

vi.mock('../../src/api/axios', () => ({
  api: {
    post: vi.fn(),
  },
}))

describe('moodApi', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('postMood should call POST /moods/ with newMood and return AxiosResponse<MoodInfoDTO>', async () => {
    const newMood: MoodCreateDTO = { ownerId: 5, scoreMood: 6, scoreProductivity: 8, 
        "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26" }
    const mockData: MoodInfoDTO ={ id: 1, scoreMood: 6, scoreProductivity: 8, 
        "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26", "createdDate":"2021-01-18" }
    const mockResponse = {
      data: mockData,
      status: 201,
      statusText: 'Created',
      headers: {},
      config: {},
    };

    (api.post as any).mockResolvedValue(mockResponse)

    const result = await postMood(newMood)

    expect(api.post).toHaveBeenCalledTimes(1)
    expect(api.post).toHaveBeenCalledWith('/moods/', newMood)
    expect(result).toEqual(mockResponse)
  })
})
