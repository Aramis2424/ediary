import { describe, it, expect, vi, beforeEach } from 'vitest'
import { fetchEntryCards } from '../../src/services/entryCardService'
import * as entryApi from '../../src/api/entryCardApi'
import type { EntryCard } from '../../src/types/EntryCard'

vi.mock('../../src/api/entryCardApi', () => ({
  getEntryCards: vi.fn(),
}))

describe('entryService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('fetchEntryCards', () => {
    it('should return entry cards on success', async () => {
      const mockCards: EntryCard[] = [
      { title: 'Card 1', diaryId: 1, entryId: 1, scoreMood: 5, scoreProductivity: 7, createdDate: '2020-01-01' },
      { title: 'Card 2', diaryId: 1, entryId: 1, scoreMood: 5, scoreProductivity: 7, createdDate: '2020-01-01' },
    ]
      const mockResponse = {
        data: mockCards,
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
      };
      (entryApi.getEntryCards as any).mockResolvedValue(mockResponse)

      const result = await fetchEntryCards(1)
      expect(result).toEqual(mockCards)
      expect(entryApi.getEntryCards).toHaveBeenCalledWith(1)
    })

    it('should throw "Diary not found" on 404', async () => {
      const error = { response: { status: 404 } };
      (entryApi.getEntryCards as any).mockRejectedValue(error)

      await expect(fetchEntryCards(1)).rejects.toThrow('Diary not found')
    })

    it('should throw "Access denied" on 403', async () => {
      const error = { response: { status: 403 } };
      (entryApi.getEntryCards as any).mockRejectedValue(error)

      await expect(fetchEntryCards(1)).rejects.toThrow('Access denied')
    })

    it('should throw default error for unknown errors', async () => {
      const error = new Error('Something went wrong');
      (entryApi.getEntryCards as any).mockRejectedValue(error)

      await expect(fetchEntryCards(1)).rejects.toThrow('Something went wrong')
    })

    it('should throw if response is invalid', async () => {
      const mockResponse = {
        data: null,
        status: 500,
        statusText: 'Internal Server Error',
        headers: {},
        config: {},
      };
      (entryApi.getEntryCards as any).mockResolvedValue(mockResponse)

      await expect(fetchEntryCards(1)).rejects.toThrow('Cannot fetch entry')
    })
  })
})
