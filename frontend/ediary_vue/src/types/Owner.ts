export interface Owner {
    id: number;
    name: string;
    birthDate: string;
    login: string;
    password: string;
    createdDate: string;
}

export interface OwnerLoginDTO {
    login: string;
    password: string;
}

export interface OwnerInfoDTO {
    id: number;
    name: string;
    birthDate: string;
    login: string;
    createdDate: string;
}

export interface OwnerCreateDTO {
    name: string;
    birthDate: string;
    login: string;
    password: string;
}

export interface TokenResponse {
    token: string;
}

export interface TokenRequest {
    login: string;
    password: string;
}
