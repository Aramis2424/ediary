import { describe, it, expect, vi, beforeEach } from 'vitest'
import { createMood } from '../../src/services/moodService'
import * as moodApi from '../../src/api/moodApi'
import type { MoodCreateDTO, MoodInfoDTO } from '../../src/types/Mood'

vi.mock('../../src/api/moodApi', () => ({
  postMood: vi.fn(),
}))

describe('moodService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('createMood', () => {
    it('should return mood on successful creation', async () => {
      const mockMood: MoodInfoDTO = { id: 1, scoreMood: 6, scoreProductivity: 8, 
        "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26", "createdDate":"2021-01-18" }

      const mockResponse = {
        data: mockMood,
        status: 201,
        statusText: 'Created',
        headers: {},
        config: {},
      };
      (moodApi.postMood as any).mockResolvedValue(mockResponse)

      const newMood: MoodCreateDTO = { ownerId: 5, scoreMood: 6, scoreProductivity: 8, 
        "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26" }

      const result = await createMood(newMood)
      expect(result).toEqual(mockMood)
      expect(moodApi.postMood).toHaveBeenCalledWith(newMood)
    })

    it('should throw if response is invalid', async () => {
      const mockResponse = {
        data: null,
        status: 400,
        statusText: 'Bad Request',
        headers: {},
        config: {},
      };
      (moodApi.postMood as any).mockResolvedValue(mockResponse)

      const newMood: MoodCreateDTO = { ownerId: 5, scoreMood: 6, scoreProductivity: 8, 
        "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26" }

      await expect(createMood(newMood)).rejects.toThrow('Cannot create mood')
    })

    it('should rethrow unexpected error', async () => {
      const error = new Error('Unexpected failure');
      (moodApi.postMood as any).mockRejectedValue(error)

      const newMood: MoodCreateDTO = { ownerId: 5, scoreMood: 6, scoreProductivity: 8, 
        "bedtime":"2022-01-23T19:26:26", "wakeUpTime":"2022-01-23T22:26:26" }

      await expect(createMood(newMood)).rejects.toThrow('Unexpected failure')
    })
  })
})
