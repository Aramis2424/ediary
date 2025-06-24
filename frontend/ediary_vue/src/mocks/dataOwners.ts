import type { Owner, OwnerLoginDTO, OwnerInfoDTO, OwnerCreateDTO, TokenResponse, TokenRequest } from "@/types/Owner"

export const owners: Owner[] = [
    { "id":1, "name":"Sandra", "birthDate":"2005-05-26", "login":"sandra977", "password":"$2b$12$ovLPjI5l6XYJ.yz4kuDA4O9oOxIJklDaLR92Ry06RcCk30yU5SNgG", "createdDate":"2014-06-28" },
    { "id":2, "name":"Jacqueline", "birthDate":"1989-10-20", "login":"jacqueline489", "password":"$2b$12$Kp4GqADgaX52yRDRxsVn5uuzLUHgwz/TcKrg.weTtgxaD8UVL0.oK", "createdDate":"2012-05-21" },
    { "id":3, "name":"Curtis", "birthDate":"1977-11-01", "login":"curtis754", "password":"$2b$12$o8UHMoG9dPThvlkLSGYjYejFJu56NJrxVnmJ2odTzGbO9t/wtePqS", "createdDate":"2013-02-09" },
    { "id":4, "name":"Ashley", "birthDate":"2006-03-25", "login":"ashley216", "password":"$2b$12$1nodC0SPud2CPtfqUyZM6uPnGksqO.cmCM6vP64bqcdf2slRWAnWK", "createdDate":"2013-06-20" },
    { "id":5, "name":"Travis", "birthDate":"1996-04-09", "login":"travis141", "password":"$2b$12$wcupx373fUj4MmIEIS737uPiKnmsEpgBvu4aMglSHV1VaTee2wiEu", "createdDate":"2012-11-03" },
    { "id":6, "name":"Edward", "birthDate":"2006-02-06", "login":"edward758", "password":"$2b$12$9ucnLtzN1Cmt00kRw39KlefYBTkGW4nxTLWgI.c3aFSNplsJPD4G.", "createdDate":"2014-08-17" },
    { "id":7, "name":"Lisa", "birthDate":"2007-05-08", "login":"lisa608", "password":"$2b$12$SuZZ9c7bePtHc3hc9vr0T.NM.cQSE6msspJ9kKs1X8rc.nNzDoyxW", "createdDate":"2013-07-05" },
    { "id":8, "name":"Julia", "birthDate":"1970-02-05", "login":"julia273", "password":"$2b$12$D17FdJ8qmF6Jveb8VSfDBOtc3gfQYiMCMSxXHDdlkxhY9uVwQ6NAy", "createdDate":"2012-07-28" },
    { "id":9, "name":"Joshua", "birthDate":"1980-02-21", "login":"joshua516", "password":"$2b$12$ui5owXmkaclj6gq3NrnaoeJiEj4/SL2jqM9GllEtEmY0m8DM1EYMW", "createdDate":"2012-01-27" },
    { "id":10, "name":"George", "birthDate":"1970-04-03", "login":"george374", "password":"$2b$12$L80edk7FQorGAyHD/KaS0.wWwXn8bdooCIhx5mBVr93cF7NqwsjxO", "createdDate":"2013-04-16" },
    { "id":11, "name":"Ivan", "birthDate":"2000-01-01", "login":"ivan01", "password":"navi01", "createdDate":"2020-01-01" }
]

export function toLoginDto(obj: Owner): OwnerLoginDTO {
    return { login: obj.login, password: obj.password }
}

export function toInfoDto(obj: Owner): OwnerInfoDTO {
    return {id: obj.id, name: obj.name, birthDate: obj.birthDate, 
        login: obj.login, createdDate: obj.createdDate }
}

export function toCreateDto(obj: Owner): OwnerCreateDTO {
    return {name: obj.name, birthDate: obj.birthDate, 
        login: obj.login, password: obj.password }
}

export function toTokenRequest(obj: Owner): TokenRequest {
    return { login: obj.login, password: obj.password }
}

export const ownersRespones: TokenResponse[] = [
    { "token": "token01"},
    { "token": "token02"},
    { "token": "token03"},
    { "token": "token04"},
    { "token": "token05"},
    { "token": "token06"},
    { "token": "token07"},
    { "token": "token08"},
    { "token": "token09"},
    { "token": "token10"},
    { "token": "token11"},
]
