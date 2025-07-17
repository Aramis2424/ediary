import { describe, it, expect, vi, beforeEach } from 'vitest'
import { fetchOwner, getToken, createOwner } from '../../src/services/ownerService'
import * as ownerApi from '../../src/api/ownerApi'
import type { OwnerInfoDTO, OwnerCreateDTO, TokenRequest, TokenResponse } from '../../src/types/Owner'

vi.mock('../../src/api/ownerApi', () => ({
  getOwner: vi.fn(),
  postOwnerLogin: vi.fn(),
  postOwnerRegister: vi.fn(),
}))

describe('ownerService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('fetchOwner', () => {
    it('should return owner on success', async () => {
      const mockOwner: OwnerInfoDTO = { id: 1, name:"Sandra", birthDate:"2005-05-26", login:"sandra977", "createdDate":"2014-06-28" }
      const mockResponse = {
        data: mockOwner,
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
      };
      (ownerApi.getOwner as any).mockResolvedValue(mockResponse)

      const result = await fetchOwner()
      expect(result).toEqual(mockOwner)
      expect(ownerApi.getOwner).toHaveBeenCalled()
    })

    it('should throw "Entry not found" on 404', async () => {
      const error = { response: { status: 404 } };
      (ownerApi.getOwner as any).mockRejectedValue(error)

      await expect(fetchOwner()).rejects.toThrow('Entry not found')
    })

    it('should throw "Unauthorized" on 401', async () => {
      const error = { response: { status: 401 } };
      (ownerApi.getOwner as any).mockRejectedValue(error)

      await expect(fetchOwner()).rejects.toThrow('Unauthorized')
    })

    it('should throw unknown errors', async () => {
      const error = new Error('Some error');
      (ownerApi.getOwner as any).mockRejectedValue(error)

      await expect(fetchOwner()).rejects.toThrow('Some error')
    })
  })

  describe('getToken', () => {
    it('should return token on success', async () => {
      const mockToken: TokenResponse = { token: 'token123' }

      const mockResponse = {
        data: mockToken,
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
      };
      (ownerApi.postOwnerLogin as any).mockResolvedValue(mockResponse)

      const request: TokenRequest = { login: 'testuser', password: 'pass123' }
      const result = await getToken(request)

      expect(result).toEqual(mockToken)
      expect(ownerApi.postOwnerLogin).toHaveBeenCalledWith(request)
    })

    it('should throw "Unauthorized" on 401', async () => {
      const error = { response: { status: 401 } };
      (ownerApi.postOwnerLogin as any).mockRejectedValue(error)

      await expect(getToken({ username: '', password: '' })).rejects.toThrow('Unauthorized')
    })

    it('should throw for unexpected error', async () => {
      const error = new Error('Unexpected');
      (ownerApi.postOwnerLogin as any).mockRejectedValue(error)

      await expect(getToken({ username: '', password: '' })).rejects.toThrow('Unexpected')
    })
  })

  describe('createOwner', () => {
    it('should return owner on successful registration', async () => {
      const mockOwner: OwnerInfoDTO = { id: 1, name:"Sandra", birthDate:"2005-05-26", login:"sandra977", "createdDate":"2014-06-28" }

      const mockResponse = {
        data: mockOwner,
        status: 201,
        statusText: 'Created',
        headers: {},
        config: {},
      };
      (ownerApi.postOwnerRegister as any).mockResolvedValue(mockResponse)

      const request: OwnerCreateDTO = { name:"Sandra", birthDate:"2005-05-26", login:"sandra977", password:"secret1" }

      const result = await createOwner(request)
      expect(result).toEqual(mockOwner)
      expect(ownerApi.postOwnerRegister).toHaveBeenCalledWith(request)
    })

    it('should throw "User already exists" on 409', async () => {
      const error = { response: { status: 409 } };
      (ownerApi.postOwnerRegister as any).mockRejectedValue(error)

      await expect(createOwner({ username: '', email: '', password: '' })).rejects.toThrow('User already exists')
    })

    it('should throw "Unauthorized" on 401', async () => {
      const error = { response: { status: 401 } };
      (ownerApi.postOwnerRegister as any).mockRejectedValue(error)

      await expect(createOwner({ username: '', email: '', password: '' })).rejects.toThrow('Unauthorized')
    })

    it('should throw for unexpected error', async () => {
      const error = new Error('Something broke');
      (ownerApi.postOwnerRegister as any).mockRejectedValue(error)

      await expect(createOwner({ username: '', email: '', password: '' })).rejects.toThrow('Something broke')
    })
  })
})
