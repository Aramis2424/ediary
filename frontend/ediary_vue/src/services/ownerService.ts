import { postOwnerLogin, postOwnerRegister, getOwner } from '../api/ownerApi';
import type { OwnerInfoDTO, OwnerCreateDTO, TokenRequest, TokenResponse } from '@/types/Owner';

export const fetchOwner = async (): Promise<OwnerInfoDTO> => {
    try {
        const res = await getOwner();
        if (!res.data || res.status !== 200) {
            throw new Error('Cannot fetch owner');
        }
        return res.data;
    } catch (error: any) {
        if (error.response?.status === 404) {
            throw new Error("Entry not found");
        } else if (error.response?.status === 401) {
            throw new Error("Unauthorized");
        } else {
            throw error;
        }
    }
};

export const getToken = async (tokenReq: TokenRequest): Promise<TokenResponse> => {
    try {
        const res = await postOwnerLogin(tokenReq);
        if (!res.data || res.status !== 200) {
            throw new Error('Cannot login owner');
        }
        return res.data;
    } catch (error: any) {
        if (error.response?.status === 401) {
            throw new Error("Unauthorized");
        } else {
            throw error;
        }
    }
};

export const createOwner = async (newOwner: OwnerCreateDTO): Promise<OwnerInfoDTO> => {
    try {
        const res = await postOwnerRegister(newOwner);
        if (!res.data || res.status !== 201) {
            console.log(res.status);
            throw new Error('Cannot register owner');
        }
        return res.data;
    } catch (error: any) {
        if (error.response?.status === 401) {
            throw new Error("Unauthorized");
        } else if (error.response?.status === 409) {
            throw new Error("User already exists");
        } else {
            throw error;
        }
    }
};
