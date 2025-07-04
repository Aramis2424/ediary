import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getEntry, postEntry, putEntry, deleteEntry } from '../../src/api/entryApi'
import { api } from '../../src/api/axios'
import type { EntryInfoDTO, EntryCreateDTO, EntryUpdateDTO } from '../../src/types/Entry'

vi.mock('../../src/api/axios', () => ({
  api: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
  },
}))

describe('entryApi', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('getEntry should return AxiosResponse<EntryInfoDTO>', async () => {
    const mockData: EntryInfoDTO = { id: 1, title: 'Test Entry', 
      content: 'Testing', createdDate: '2020-01-01' }
    const mockResponse = {
      data: mockData,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {},
    };

    (api.get as any).mockResolvedValue(mockResponse)

    const result = await getEntry(1)
    expect(api.get).toHaveBeenCalledWith('/entries/1')
    expect(result).toEqual(mockResponse)
  })

  it('postEntry should post new entry and return AxiosResponse<EntryInfoDTO>', async () => {
    const newEntry: EntryCreateDTO = { title: 'New', content: 'Hello', diaryId: "1" }
    const mockData: EntryInfoDTO = { id: 1, title: 'New', 
      content: 'Hello', createdDate: '2020-01-01' }
    const mockResponse = {
      data: mockData,
      status: 201,
      statusText: 'Created',
      headers: {},
      config: {},
    };

    (api.post as any).mockResolvedValue(mockResponse)

    const result = await postEntry(newEntry)
    expect(api.post).toHaveBeenCalledWith('/entries/', newEntry)
    expect(result).toEqual(mockResponse)
  })

  it('putEntry should update entry and return AxiosResponse<EntryInfoDTO>', async () => {
    const updatedEntry: EntryUpdateDTO = { title: 'Updated', content: 'Changed' }
    const mockData: EntryInfoDTO = { id: 1, title: 'Updated', 
      content: 'Changed', createdDate: '2020-01-01' }
    const mockResponse = {
      data: mockData,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {},
    };

    (api.put as any).mockResolvedValue(mockResponse)

    const result = await putEntry(3, updatedEntry)
    expect(api.put).toHaveBeenCalledWith('/entries/3', updatedEntry)
    expect(result).toEqual(mockResponse)
  })

  it('deleteEntry should delete entry and return AxiosResponse<void>', async () => {
    const mockResponse = {
      data: undefined,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {},
    };

    (api.delete as any).mockResolvedValue(mockResponse)

    const result = await deleteEntry(5)
    expect(api.delete).toHaveBeenCalledWith('/entries/5')
    expect(result).toEqual(mockResponse)
  })
})
