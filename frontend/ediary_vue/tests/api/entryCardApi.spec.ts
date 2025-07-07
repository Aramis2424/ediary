import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getEntryCards } from '../../src/api/entryCardApi'
import { api } from '../../src/api/axios'
import type { EntryCard } from '../../src/types/EntryCard'

vi.mock('../../src/api/axios', () => ({
  api: {
    get: vi.fn(),
  },
}))

describe('entryApi', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('getEntryCards should call GET /diaries/:diaryId/entry-cards and return AxiosResponse<EntryCard[]>', async () => {
    const mockData: EntryCard[] = [
      { title: 'Card 1', diaryId: 1, entryId: 1, scoreMood: 5, scoreProductivity: 7, createdDate: '2020-01-01' },
      { title: 'Card 2', diaryId: 1, entryId: 1, scoreMood: 5, scoreProductivity: 7, createdDate: '2020-01-01' },
    ]
    const mockResponse = {
      data: mockData,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {},
    };

    (api.get as any).mockResolvedValue(mockResponse)

    const diaryId = 42
    const result = await getEntryCards(diaryId)

    expect(api.get).toHaveBeenCalledTimes(1)
    expect(api.get).toHaveBeenCalledWith(`/diaries/${diaryId}/entry-cards`)
    expect(result).toEqual(mockResponse)
  })
})
