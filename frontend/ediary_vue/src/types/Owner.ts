export interface OwnerLoginDTO {
    login: string;
    password: string;
}

export interface OwnerInfoDTO {
    id: number;
    name: string;
    birthDate: Date;
    login: string;
    createdDate: Date;
}

export interface OwnerCreateDTO {
    name: string;
    birthDate: Date;
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
