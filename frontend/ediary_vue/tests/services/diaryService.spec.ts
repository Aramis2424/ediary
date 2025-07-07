import { describe, it, expect, vi, beforeEach } from 'vitest'
import { fetchDiary, createDiary } from '../../src/services/diaryService'
import * as diaryApi from '../../src/api/diaryApi'
import type { DiaryInfoDTO, DiaryCreateDTO } from '../../src/types/Diary'

vi.mock('../../src/api/diaryApi', () => ({
  getDiaries: vi.fn(),
  postDiary: vi.fn(),
}))

describe('diaryService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('fetchDiary', () => {
    it('should return the first diary on success', async () => {
      const mockDiary: DiaryInfoDTO = { id: 1, title: 'New Diary', description: 'Test 1', cntEntries: 10, createdDate: '2020-01-01' }
      const mockResponse = {
        data: [mockDiary],
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
      };
      (diaryApi.getDiaries as any).mockResolvedValue(mockResponse)

      const result = await fetchDiary(42)
      expect(result).toEqual(mockDiary)
      expect(diaryApi.getDiaries).toHaveBeenCalledWith(42)
    })

    it('should throw "Diary not found" on 404', async () => {
      const error = { response: { status: 404 } };
      (diaryApi.getDiaries as any).mockRejectedValue(error)

      await expect(fetchDiary(1)).rejects.toThrow('Diary not found')
    })

    it('should throw "Access denied" on 403', async () => {
      const error = { response: { status: 403 } };
      (diaryApi.getDiaries as any).mockRejectedValue(error)

      await expect(fetchDiary(1)).rejects.toThrow('Access denied')
    })

    it('should throw default error for other failures', async () => {
      const error = new Error('Unknown error');
      (diaryApi.getDiaries as any).mockRejectedValue(error)

      await expect(fetchDiary(1)).rejects.toThrow('Unknown error')
    })
  })

  describe('createDiary', () => {
    it('should return diary on successful creation', async () => {
      const mockDiary: DiaryInfoDTO = { id: 1, title: 'New Diary', description: 'Test 1', cntEntries: 10, createdDate: '2020-01-01' }
      const mockResponse = {
        data: mockDiary,
        status: 201,
        statusText: 'Created',
        headers: {},
        config: {},
      };
      (diaryApi.postDiary as any).mockResolvedValue(mockResponse)

      const result = await createDiary(1)
      expect(result).toEqual(mockDiary)

      const expectedPayload: DiaryCreateDTO = {
        ownerId: 1,
        title: 'New diary',
        description: 'Description for new diary',
      }
      expect(diaryApi.postDiary).toHaveBeenCalledWith(expectedPayload)
    })

    it('should throw error if response is invalid', async () => {
      const mockResponse = {
        data: null,
        status: 400,
        statusText: 'Bad Request',
        headers: {},
        config: {},
      };
      (diaryApi.postDiary as any).mockResolvedValue(mockResponse)

      await expect(createDiary(1)).rejects.toThrow('Cannot create diary')
    })

    it('should rethrow any unexpected error', async () => {
      const error = new Error('Unexpected');
      (diaryApi.postDiary as any).mockRejectedValue(error)

      await expect(createDiary(1)).rejects.toThrow('Unexpected')
    })
  })
})
