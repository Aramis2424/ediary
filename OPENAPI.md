# Swagger eDiary - OpenAPI 3.0

> Version 1.0.0

Внешнее публичное API в идеологии REST в формате OpenAPI 3.0 проекта eDiary — сервиса личных электронных дневников. Сервис позполяет пользователю создавать и редактировать записи и отмечать свое настроение. Авторизация по JWT-токену.

## Path Table

| Method | Path | Description |
| --- | --- | --- |
| POST | [/owners/](#postowners) | Create user. |
| POST | [/token/create](#posttokencreate) | Login user. |
| GET | [/owners/me](#getownersme) | Get current authenticated user |
| GET | [/owners/{ownerId}/diaries](#getownersowneriddiaries) | Get all owner`s diaries |
| GET | [/diaries/{diaryId}](#getdiariesdiaryid) | Get diary by id |
| PUT | [/diaries/{diaryId}](#putdiariesdiaryid) | Update diary by id |
| DELETE | [/diaries/{diaryId}](#deletediariesdiaryid) | Delete diary by id |
| POST | [/diaries](#postdiaries) | Create diary |
| GET | [/diaries/{diaryId}/entries](#getdiariesdiaryidentries) | Get all owner`s entries |
| GET | [/entries/{entryId}](#getentriesentryid) | Get entry by id |
| PATCH | [/entries/{entryId}](#patchentriesentryid) | Update entry by id |
| DELETE | [/entries/{entryId}](#deleteentriesentryid) | Delete entry by id |
| POST | [/entries](#postentries) | Create entry for diary |
| GET | [/diaries/{diaryId}/entry-cards](#getdiariesdiaryidentry-cards) | get entry cards representation |
| GET | [/diaries/{diaryId}/can-create-entry](#getdiariesdiaryidcan-create-entry) | get permission for creating entry |
| GET | [/owners/{ownerId}/moods](#getownersowneridmoods) | Get all owner`s moods |
| GET | [/moods/{moodId}](#getmoodsmoodid) | Get mood by id |
| PUT | [/moods/{moodId}](#putmoodsmoodid) | Update mood by id |
| DELETE | [/moods/{moodId}](#deletemoodsmoodid) | Delete mood by id |
| POST | [/moods](#postmoods) | Create mood for owner |
| GET | [/moods/{moodId}/can-create-mood](#getmoodsmoodidcan-create-mood) | get permission for creating entry |

## Reference Table

| Name | Path | Description |
| --- | --- | --- |
| OwnerCreateDTO | [#/components/schemas/OwnerCreateDTO](#componentsschemasownercreatedto) |  |
| OwnerInfoDTO | [#/components/schemas/OwnerInfoDTO](#componentsschemasownerinfodto) |  |
| DiaryCreateDTO | [#/components/schemas/DiaryCreateDTO](#componentsschemasdiarycreatedto) |  |
| DiaryUpdateDTO | [#/components/schemas/DiaryUpdateDTO](#componentsschemasdiaryupdatedto) |  |
| DiaryInfoDTO | [#/components/schemas/DiaryInfoDTO](#componentsschemasdiaryinfodto) |  |
| EntryCardDTO | [#/components/schemas/EntryCardDTO](#componentsschemasentrycarddto) |  |
| EntryPermissionRes | [#/components/schemas/EntryPermissionRes](#componentsschemasentrypermissionres) |  |
| EntryCreateDTO | [#/components/schemas/EntryCreateDTO](#componentsschemasentrycreatedto) |  |
| EntryUpdateDTO | [#/components/schemas/EntryUpdateDTO](#componentsschemasentryupdatedto) |  |
| EntryInfoDTO | [#/components/schemas/EntryInfoDTO](#componentsschemasentryinfodto) |  |
| MoodCreateDTO | [#/components/schemas/MoodCreateDTO](#componentsschemasmoodcreatedto) |  |
| MoodUpdateDTO | [#/components/schemas/MoodUpdateDTO](#componentsschemasmoodupdatedto) |  |
| MoodInfoDTO | [#/components/schemas/MoodInfoDTO](#componentsschemasmoodinfodto) |  |
| MoodPermissionRes | [#/components/schemas/MoodPermissionRes](#componentsschemasmoodpermissionres) |  |
| TokenRequest | [#/components/schemas/TokenRequest](#componentsschemastokenrequest) |  |
| TokenResponse | [#/components/schemas/TokenResponse](#componentsschemastokenresponse) |  |
| Error | [#/components/schemas/Error](#componentsschemaserror) |  |
| bearerAuth | [#/components/securitySchemes/bearerAuth](#componentssecurityschemesbearerauth) |  |

## Path Details

***

### [POST]/owners/

- Summary  
Create user.

#### RequestBody

- application/json

```ts
{
  name: string
  birthDate: string
  login: string
  password: string
}
```

#### Responses

- 201 User created successfully

`application/json`

```ts
{
  id?: integer
  name?: string
  birthDate?: string
  login?: string
  createdDate?: string
}
```

- 409 Such user already exists

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [POST]/token/create

- Summary  
Login user.

#### RequestBody

- application/json

```ts
{
  login: string
  password: string
}
```

#### Responses

- 200 JWT token returned

`application/json`

```ts
{
  token?: string
}
```

- 401 Invalid login/password

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/owners/me

- Summary  
Get current authenticated user

- Description  
Returns information about the currently authenticated owner using the provided JWT token.

- Security  
bearerAuth  

#### Responses

- 200 Successfully retrieved authenticated owner

`application/json`

```ts
{
  id?: integer
  name?: string
  birthDate?: string
  login?: string
  createdDate?: string
}
```

- 401 Unauthorized — invalid or expired token

- 403 Forbidden — access denied

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/owners/{ownerId}/diaries

- Summary  
Get all owner`s diaries

- Security  
bearerAuth  

#### Responses

- 200 List of diaries

`application/json`

```ts
{
  id?: integer
  title?: string
  description?: string
  // Количество записей в дневнике
  cntEntry?: integer
  createdDate?: string
}[]
```

- 403 Access denied

- 404 Owner not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/diaries/{diaryId}

- Summary  
Get diary by id

- Security  
bearerAuth  

#### Responses

- 200 Diary with the specified ID.

`application/json`

```ts
{
  id?: integer
  title?: string
  description?: string
  // Количество записей в дневнике
  cntEntry?: integer
  createdDate?: string
}
```

- 403 Access denied

- 404 Diary not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [PUT]/diaries/{diaryId}

- Summary  
Update diary by id

- Security  
bearerAuth  

#### RequestBody

- application/json

```ts
{
  title: string
  description?: string
}
```

#### Responses

- 200 Update diary

`application/json`

```ts
{
  id?: integer
  title?: string
  description?: string
  // Количество записей в дневнике
  cntEntry?: integer
  createdDate?: string
}
```

- 403 Access denied

- 404 Diary not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [DELETE]/diaries/{diaryId}

- Summary  
Delete diary by id

- Security  
bearerAuth  

#### Responses

- 204 Diary successfully deleted

- 403 Access denied

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [POST]/diaries

- Summary  
Create diary

- Security  
bearerAuth  

#### RequestBody

- application/json

```ts
{
  // ID владельца дневника
  ownerID: integer
  title: string
  description?: string
}
```

#### Responses

- 201 Created diary

`application/json`

```ts
{
  id?: integer
  title?: string
  description?: string
  // Количество записей в дневнике
  cntEntry?: integer
  createdDate?: string
}
```

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/diaries/{diaryId}/entries

- Summary  
Get all owner`s entries

- Security  
bearerAuth  

#### Responses

- 200 Returns all entries that belong to the specified diary.

`application/json`

```ts
{
  id?: integer
  title?: string
  content?: string
  createdDate?: string
}[]
```

- 403 Access denied

- 404 Diary not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/entries/{entryId}

- Summary  
Get entry by id

- Security  
bearerAuth  

#### Responses

- 200 Entry with the specified ID.

`application/json`

```ts
{
  id?: integer
  title?: string
  content?: string
  createdDate?: string
}
```

- 403 Access denied

- 404 Entry not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [PATCH]/entries/{entryId}

- Summary  
Update entry by id

- Security  
bearerAuth  

#### RequestBody

- application/json

```ts
{
  title: string
  content: string
}
```

#### Responses

- 200 Updated entry

`application/json`

```ts
{
  id?: integer
  title?: string
  content?: string
  createdDate?: string
}
```

- 403 Access denied

- 404 Diary not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [DELETE]/entries/{entryId}

- Summary  
Delete entry by id

- Security  
bearerAuth  

#### Responses

- 204 Entry successfully deleted

- 403 Access denied

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [POST]/entries

- Summary  
Create entry for diary

- Security  
bearerAuth  

#### RequestBody

- application/json

```ts
{
  // ID дневника, к которому принадлежит запись
  diaryID: integer
  title: string
  content: string
}
```

#### Responses

- 201 Creates a new entry and assigns it to a diary.

`application/json`

```ts
{
  id?: integer
  title?: string
  content?: string
  createdDate?: string
}
```

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/diaries/{diaryId}/entry-cards

- Summary  
get entry cards representation

- Security  
bearerAuth  

#### Responses

- 200 Returns all entries entry cards that belong to the specified diary.

`application/json`

```ts
{
  diaryId?: integer
  entryId?: integer
  title?: string
  scoreMood?: integer
  scoreProductivity?: integer
  createdDate?: string
}[]
```

- 403 Access denied

- 404 Diary not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/diaries/{diaryId}/can-create-entry

- Summary  
get permission for creating entry

- Security  
bearerAuth  

#### Parameters(Query)

```ts
date: string
```

#### Responses

- 200 Returns permission if user can create new entry

`application/json`

```ts
{
  allowed?: boolean
}[]
```

- 403 Access denied

- 404 Diary not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/owners/{ownerId}/moods

- Summary  
Get all owner`s moods

- Security  
bearerAuth  

#### Responses

- 200 Returns all moods that belong to the specified owner.

`application/json`

```ts
{
  id?: integer
  scoreMood?: integer
  scoreProductivity?: integer
  bedtime?: string
  wakeUpTime?: string
  createdAt?: string
}[]
```

- 403 Access denied

- 404 Owner not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/moods/{moodId}

- Summary  
Get mood by id

- Security  
bearerAuth  

#### Responses

- 200 Mood with the specified ID.

`application/json`

```ts
{
  id?: integer
  scoreMood?: integer
  scoreProductivity?: integer
  bedtime?: string
  wakeUpTime?: string
  createdAt?: string
}
```

- 403 Access denied

- 404 Mood not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [PUT]/moods/{moodId}

- Summary  
Update mood by id

- Security  
bearerAuth  

#### RequestBody

- application/json

```ts
{
  scoreMood: integer
  scoreProductivity: integer
  bedtime: string
  wakeUpTime: string
}
```

#### Responses

- 200 Returns updated mood

`application/json`

```ts
{
  id?: integer
  scoreMood?: integer
  scoreProductivity?: integer
  bedtime?: string
  wakeUpTime?: string
  createdAt?: string
}
```

- 403 Access denied

- 404 Mood not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [DELETE]/moods/{moodId}

- Summary  
Delete mood by id

- Security  
bearerAuth  

#### Responses

- 204 Mood successfully deleted

- 403 Access denied

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [POST]/moods

- Summary  
Create mood for owner

- Security  
bearerAuth  

#### RequestBody

- application/json

```ts
{
  ownerID: integer
  scoreMood: integer
  scoreProductivity: integer
  bedtime: string
  wakeUpTime: string
}
```

#### Responses

- 201 Creates a new mood and assigns it to a owner.

`application/json`

```ts
{
  id?: integer
  scoreMood?: integer
  scoreProductivity?: integer
  bedtime?: string
  wakeUpTime?: string
  createdAt?: string
}
```

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

***

### [GET]/moods/{moodId}/can-create-mood

- Summary  
get permission for creating entry

- Security  
bearerAuth  

#### Parameters(Query)

```ts
date: string
```

#### Responses

- 200 Returns permission if user can create new mood

`application/json`

```ts
{
  allowed?: boolean
}[]
```

- 403 Access denied

- 404 Owner not found

- default Unexpected error

`application/json`

```ts
{
  code: string
  message: string
}
```

## References

### #/components/schemas/OwnerCreateDTO

```ts
{
  name: string
  birthDate: string
  login: string
  password: string
}
```

### #/components/schemas/OwnerInfoDTO

```ts
{
  id?: integer
  name?: string
  birthDate?: string
  login?: string
  createdDate?: string
}
```

### #/components/schemas/DiaryCreateDTO

```ts
{
  // ID владельца дневника
  ownerID: integer
  title: string
  description?: string
}
```

### #/components/schemas/DiaryUpdateDTO

```ts
{
  title: string
  description?: string
}
```

### #/components/schemas/DiaryInfoDTO

```ts
{
  id?: integer
  title?: string
  description?: string
  // Количество записей в дневнике
  cntEntry?: integer
  createdDate?: string
}
```

### #/components/schemas/EntryCardDTO

```ts
{
  diaryId?: integer
  entryId?: integer
  title?: string
  scoreMood?: integer
  scoreProductivity?: integer
  createdDate?: string
}
```

### #/components/schemas/EntryPermissionRes

```ts
{
  allowed?: boolean
}
```

### #/components/schemas/EntryCreateDTO

```ts
{
  // ID дневника, к которому принадлежит запись
  diaryID: integer
  title: string
  content: string
}
```

### #/components/schemas/EntryUpdateDTO

```ts
{
  title: string
  content: string
}
```

### #/components/schemas/EntryInfoDTO

```ts
{
  id?: integer
  title?: string
  content?: string
  createdDate?: string
}
```

### #/components/schemas/MoodCreateDTO

```ts
{
  ownerID: integer
  scoreMood: integer
  scoreProductivity: integer
  bedtime: string
  wakeUpTime: string
}
```

### #/components/schemas/MoodUpdateDTO

```ts
{
  scoreMood: integer
  scoreProductivity: integer
  bedtime: string
  wakeUpTime: string
}
```

### #/components/schemas/MoodInfoDTO

```ts
{
  id?: integer
  scoreMood?: integer
  scoreProductivity?: integer
  bedtime?: string
  wakeUpTime?: string
  createdAt?: string
}
```

### #/components/schemas/MoodPermissionRes

```ts
{
  allowed?: boolean
}
```

### #/components/schemas/TokenRequest

```ts
{
  login: string
  password: string
}
```

### #/components/schemas/TokenResponse

```ts
{
  token?: string
}
```

### #/components/schemas/Error

```ts
{
  code: string
  message: string
}
```

### #/components/securitySchemes/bearerAuth

```ts
{
  "type": "http",
  "scheme": "bearer",
  "bearerFormat": "JWT"
}
```