import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getDiaries, postDiary } from '../../src/api/diaryApi'
import { api } from '../../src/api/axios'
import type { DiaryInfoDTO, DiaryCreateDTO } from '../../src/types/Diary'

vi.mock('../../src/api/axios', () => ({
  api: {
    get: vi.fn(),
    post: vi.fn(),
  },
}))

describe('diaryApi', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('getDiaries should call GET /owners/:ownerId/diaries and return AxiosResponse<DiaryInfoDTO[]>', async () => {
    const mockData: DiaryInfoDTO[] = [
      { id: 1, title: 'Diary 1', description: 'Test 1', cntEntries: 10, createdDate: '2020-01-01' },
      { id: 2, title: 'Diary 2', description: 'Test 2', cntEntries: 9, createdDate: '2020-02-02' },
    ]
    const mockResponse = {
      data: mockData,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {},
    };

    (api.get as any).mockResolvedValue(mockResponse)

    const ownerId = 123
    const result = await getDiaries(ownerId)

    expect(api.get).toHaveBeenCalledTimes(1)
    expect(api.get).toHaveBeenCalledWith(`/owners/${ownerId}/diaries`)
    expect(result).toEqual(mockResponse)
  })

  it('postDiary should call POST /diaries with newDiary and return AxiosResponse<DiaryInfoDTO>', async () => {
    const newDiary: DiaryCreateDTO = { title: 'New Diary', description: 'Test 1', ownerId: 1 }
    const mockData: DiaryInfoDTO = { id: 1, title: 'New Diary', description: 'Test 1', cntEntries: 10, createdDate: '2020-01-01' }
    const mockResponse = {
      data: mockData,
      status: 201,
      statusText: 'Created',
      headers: {},
      config: {},
    }

    ;(api.post as any).mockResolvedValue(mockResponse)

    const result = await postDiary(newDiary)

    expect(api.post).toHaveBeenCalledTimes(1)
    expect(api.post).toHaveBeenCalledWith('/diaries', newDiary)
    expect(result).toEqual(mockResponse)
  })
})
