import { describe, it, expect, vi, beforeEach } from 'vitest'
import { postOwnerRegister, postOwnerLogin, getOwner } from '../../src/api/ownerApi'
import { api } from '../../src/api/axios'
import type { OwnerCreateDTO, OwnerInfoDTO, TokenRequest, TokenResponse } from '../../src/types/Owner'

vi.mock('../../src/api/axios', () => ({
  api: {
    post: vi.fn(),
    get: vi.fn(),
  },
}))

describe('ownerApi', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('postOwnerRegister should POST /owners and return AxiosResponse<OwnerInfoDTO>', async () => {
    const newOwner: OwnerCreateDTO = { name:"Sandra", birthDate:"2005-05-26", login:"sandra977", password:"secret1" }
    const mockData: OwnerInfoDTO = { id: 1, name:"Sandra", birthDate:"2005-05-26", login:"sandra977", "createdDate":"2014-06-28" }
    const mockResponse = {
      data: mockData,
      status: 201,
      statusText: 'Created',
      headers: {},
      config: {},
    };

    (api.post as any).mockResolvedValue(mockResponse)

    const result = await postOwnerRegister(newOwner)

    expect(api.post).toHaveBeenCalledTimes(1)
    expect(api.post).toHaveBeenCalledWith('/owners', newOwner)
    expect(result).toEqual(mockResponse)
  })

  it('postOwnerLogin should POST /token/create and return AxiosResponse<TokenResponse>', async () => {
    const loginData: TokenRequest = { login: 'testuser', password: 'pass123' }
    const mockData: TokenResponse = { token: 'token123' }
    const mockResponse = {
      data: mockData,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {},
    };

    (api.post as any).mockResolvedValue(mockResponse)

    const result = await postOwnerLogin(loginData)

    expect(api.post).toHaveBeenCalledTimes(1)
    expect(api.post).toHaveBeenCalledWith('/token/create', loginData)
    expect(result).toEqual(mockResponse)
  })

  it('getOwner should GET /owners/me and return AxiosResponse<OwnerInfoDTO>', async () => {
    const mockData: OwnerInfoDTO = { id: 1, name:"Sandra", birthDate:"2005-05-26", login:"sandra977", "createdDate":"2014-06-28" }
    const mockResponse = {
      data: mockData,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {},
    };

    (api.get as any).mockResolvedValue(mockResponse)

    const result = await getOwner()

    expect(api.get).toHaveBeenCalledTimes(1)
    expect(api.get).toHaveBeenCalledWith('/owners/me')
    expect(result).toEqual(mockResponse)
  })
})
