import { api } from './axios.ts'
import type { AxiosResponse } from 'axios';
import type { OwnerCreateDTO, OwnerInfoDTO, TokenRequest, TokenResponse } from '@/types/Owner.ts';

export const postOwnerRegister = async (newOnwer: OwnerCreateDTO): Promise<AxiosResponse<OwnerInfoDTO>> => {
    const res = await api.post<OwnerInfoDTO, AxiosResponse<OwnerInfoDTO>, OwnerCreateDTO>('/owners', newOnwer);
    return res;
};

export const postOwnerLogin = async (loginData: TokenRequest): Promise<AxiosResponse<TokenResponse>> => {
    const res = await api.post<TokenResponse, AxiosResponse<TokenResponse>, TokenRequest>('/token/create', loginData);
    return res;
};

export const getOwner = async (): Promise<AxiosResponse<OwnerInfoDTO>> => {
    const res = await api.get<OwnerInfoDTO, AxiosResponse<OwnerInfoDTO>, OwnerCreateDTO>('/owners/me');
    return res;
};
