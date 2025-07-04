import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  fetchEntry,
  createEntry,
  updateEntry,
  removeEntry,
} from '../../src/services/entryService'
import * as entryApi from '../../src/api/entryApi'
import type { EntryInfoDTO, EntryCreateDTO, EntryUpdateDTO } from '../../src/types/Entry'

vi.mock('../../src/api/entryApi', () => ({
  getEntry: vi.fn(),
  postEntry: vi.fn(),
  putEntry: vi.fn(),
  deleteEntry: vi.fn(),
}))

describe('entryService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('fetchEntry', () => {
    it('should return entry on success', async () => {
      const mockEntry: EntryInfoDTO = {
        id: 1, title: 'Test', content: 'Content', createdDate: '2020-01-01'
      }
      const mockResponse = {
        data: mockEntry,
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
      };
      (entryApi.getEntry as any).mockResolvedValue(mockResponse)

      const result = await fetchEntry(1)
      expect(result).toEqual(mockEntry)
      expect(entryApi.getEntry).toHaveBeenCalledWith(1)
    })

    it('should throw "Entry not found" on 404', async () => {
      const error = { response: { status: 404 } };
      (entryApi.getEntry as any).mockRejectedValue(error)

      await expect(fetchEntry(1)).rejects.toThrow('Entry not found')
    })

    it('should throw "Access denied" on 403', async () => {
      const error = { response: { status: 403 } };
      (entryApi.getEntry as any).mockRejectedValue(error)

      await expect(fetchEntry(1)).rejects.toThrow('Access denied')
    })

    it('should throw for unexpected error', async () => {
      const error = new Error('Unexpected');
      (entryApi.getEntry as any).mockRejectedValue(error)

      await expect(fetchEntry(1)).rejects.toThrow('Unexpected')
    })
  })

  describe('createEntry', () => {
    it('should return created entry on success', async () => {
      const mockEntry: EntryInfoDTO = {
        id: 2, title: 'Новый день', content: '', createdDate: '2020-02-02'
      }
      const mockResponse = {
        data: mockEntry,
        status: 201,
        statusText: 'Created',
        headers: {},
        config: {},
      };
      (entryApi.postEntry as any).mockResolvedValue(mockResponse)

      const result = await createEntry(5)
      expect(result).toEqual(mockEntry)

      const expectedPayload: EntryCreateDTO = {
        diaryId: '5',
        title: 'Новый день',
        content: '',
      }
      expect(entryApi.postEntry).toHaveBeenCalledWith(expectedPayload)
    })

    it('should throw if creation fails with bad response', async () => {
      const mockResponse = {
        data: null,
        status: 400,
        statusText: 'Bad Request',
        headers: {},
        config: {},
      };
      (entryApi.postEntry as any).mockResolvedValue(mockResponse)

      await expect(createEntry(1)).rejects.toThrow('Cannot create entry')
    })

    it('should rethrow unexpected error', async () => {
      const error = new Error('Something went wrong');
      (entryApi.postEntry as any).mockRejectedValue(error)

      await expect(createEntry(1)).rejects.toThrow('Something went wrong')
    })
  })

  describe('updateEntry', () => {
    it('should return updated entry on success', async () => {
      const mockEntry: EntryInfoDTO = {
        id: 1, title: 'Updated', content: 'Changed', createdDate: '2020-03-01'
      }
      const mockResponse = {
        data: mockEntry,
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
      };
      (entryApi.putEntry as any).mockResolvedValue(mockResponse)

      const updatedData: EntryUpdateDTO = {
        title: 'Updated',
        content: 'Changed',
      }

      const result = await updateEntry(1, updatedData)
      expect(result).toEqual(mockEntry)
      expect(entryApi.putEntry).toHaveBeenCalledWith(1, updatedData)
    })

    it('should throw "Entry not found" on 404', async () => {
      const error = { response: { status: 404 } };
      (entryApi.putEntry as any).mockRejectedValue(error)

      await expect(updateEntry(1, { title: '', content: '' })).rejects.toThrow('Entry not found')
    })

    it('should throw "Access denied" on 403', async () => {
      const error = { response: { status: 403 } };
      (entryApi.putEntry as any).mockRejectedValue(error)

      await expect(updateEntry(1, { title: '', content: '' })).rejects.toThrow('Access denied')
    })

    it('should throw for unexpected error', async () => {
      const error = new Error('Unknown');
      (entryApi.putEntry as any).mockRejectedValue(error)

      await expect(updateEntry(1, { title: '', content: '' })).rejects.toThrow('Unknown')
    })
  })

  describe('removeEntry', () => {
    it('should succeed if status is 204', async () => {
      const mockResponse = {
        status: 204,
        statusText: 'No Content',
        headers: {},
        config: {},
        data: undefined,
      };
      (entryApi.deleteEntry as any).mockResolvedValue(mockResponse)

      await expect(removeEntry(1)).resolves.toBeUndefined()
      expect(entryApi.deleteEntry).toHaveBeenCalledWith(1)
    })

    it('should throw if status is not 204', async () => {
      const mockResponse = {
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
        data: {},
      };
      (entryApi.deleteEntry as any).mockResolvedValue(mockResponse)

      await expect(removeEntry(1)).rejects.toThrow('Cannot remove entry')
    })

    it('should rethrow unexpected error', async () => {
      const error = new Error('Unexpected failure');
      (entryApi.deleteEntry as any).mockRejectedValue(error)

      await expect(removeEntry(1)).rejects.toThrow('Unexpected failure')
    })
  })
})
