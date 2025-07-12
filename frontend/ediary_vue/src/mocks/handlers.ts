import { http, HttpResponse } from 'msw'

import { owners } from './dataOwners'
import type { OwnerCreateDTO, TokenRequest } from '@/types/Owner'

import { entries, toInfoDto } from './dataEntries'
import type { Entry, EntryCreateDTO, EntryPermissionRes } from '@/types/Entry'

import { getCards } from './dataEntryCards'
import type { EntryCard } from '@/types/EntryCard'

import { diaries, toInfoDto as toDiaryInfoDto } from './dataDiaries'
import type { Diary, DiaryCreateDTO } from '@/types/Diary'

import { moods, toInfoDto as toMoodInfoDto } from './dataMoods'
import type { Mood, MoodCreateDTO, MoodPermissionRes } from '@/types/Mood'

export const handlers = [
  http.get('/api/v1/owners/me', async ({ request }) => {
    const authHeader = request.headers.get('Authorization')
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return HttpResponse.json({ message: 'Unauthorized' }, { status: 401 })
    }
    const token = authHeader.split(' ')[1]
    const login = token
    const user = owners.find(v => v.login === login)
    if (!user) {
      return HttpResponse.json({ message: 'User not found' }, { status: 404 })
    }
    return HttpResponse.json(user, { status: 200 })
  }),
  http.post('/api/v1/owners', async ({ request }) => {
    const newOwner = await request.json() as OwnerCreateDTO
    const user = owners.find(v => v.login === newOwner.login)
    if (user) {
      return HttpResponse.json({ message: 'User already exists' }, { status: 409 })
    }
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    const nextId: number = owners.reduce((prevId, curEntry) => {
        return curEntry.id > prevId ? curEntry.id : prevId
      }, 0) + 1
    const createdOwner = {id: nextId, createdDate: formattedDate, ...newOwner};
    owners.push(createdOwner)
    return HttpResponse.json(createdOwner, { status: 201 })
  }),
  http.post('/api/v1/token/create', async ({ request }) => {
    const { login, password } = await request.json() as TokenRequest
    const user = owners.find(v => v.login === login && v.password === password)
    if (!user) {
      return HttpResponse.json({ message: 'Invalid credentials' }, { status: 401 })
    }
    return HttpResponse.json({token: user.login}, { status: 200 })
  }),

  http.get('/api/v1/entries/:id', ({ params }) => {
    const entry: Entry | undefined = entries.find(v => v.id === Number(params.id))
    if (!entry) {
      return HttpResponse.json({ message: 'Entry not found' }, { status: 404 })
    }
    return HttpResponse.json(toInfoDto(entry))
  }),
  http.post('/api/v1/entries', async ({ request }) => {
    const body = await request.json() as EntryCreateDTO
    if (!body)
      return HttpResponse.json({ message: 'Entry was not created' }, { status: 402 })
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    const nextId: number = entries.reduce((prevId, curEntry) => {
      return curEntry.id > prevId ? curEntry.id : prevId
    }, 0) + 1    
    const newEntry: Entry = { id: nextId, diaryID: Number(body.diaryID), 
      title: body.title, content: body.content, createdDate: formattedDate}
    entries.push(newEntry)
    return HttpResponse.json(newEntry, { status: 201 })
  }),
  http.put('/api/v1/entries/:id', async ({ params, request  }) => {
    const entry: Entry | undefined = entries.find(v => v.id === Number(params.id))
    if (!entry) {
      return HttpResponse.json({ message: 'Entry not found' }, { status: 404 })
    }
    const patch = await request.json()
    Object.assign(entry, patch)
    return HttpResponse.json(entry)
  }),
  http.delete('/api/v1/entries/:id', ({ params }) => {
    const filtered = entries.filter(v => v.id !== Number(params.id))

    entries.length = 0;
    entries.push(...filtered);

    return HttpResponse.json({ success: true }, { status: 204 })
  }),
  http.get('/api/v1/diaries/:diaryId/can-create-entry', ({  }) => {
    const permission: EntryPermissionRes =  {allowed: Math.random() < 0.7};
    return HttpResponse.json(permission)
  }),

  http.get('/api/v1/diaries/:diaryId/entry-cards', ({ params }) => {
    const cards: EntryCard[] | undefined = getCards().filter(v => v.diaryId === Number(params.diaryId))
    if (!cards) {
      return HttpResponse.json({ message: 'Cards not found' }, { status: 404 })
    }
    return HttpResponse.json(cards)
  }),

  http.get('/api/v1/owners/:ownerId/diaries', ({ params }) => {
    const requiredDiaries: Diary[] | undefined = diaries.filter(v => v.ownerID === Number(params.ownerId))
    if (!Array.isArray(requiredDiaries) || requiredDiaries.length === 0) {
      return HttpResponse.json({ message: 'Diaries not found' }, { status: 404 })
    }
    const requiredDtoDiaries = requiredDiaries.map(it => toDiaryInfoDto(it))
    return HttpResponse.json(requiredDtoDiaries)
  }),
  http.post('/api/v1/diaries', async ({ request }) => {
    const body = await request.json() as DiaryCreateDTO
    if (!body)
      return HttpResponse.json({ message: 'Diary was not created' }, { status: 402 })
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    const nextId: number = diaries.reduce((prevId, curEntry) => {
      return curEntry.id > prevId ? curEntry.id : prevId
    }, 0) + 1    
    const newDiary: Diary = { id: nextId, ownerID: Number(body.ownerID), 
      title: body.title, description: body.description, cntEntries: 0, createdDate: formattedDate}
    diaries.push(newDiary)
    return HttpResponse.json(newDiary, { status: 201 })
  }),

  http.post('/api/v1/moods', async ({ request }) => {
    const body = await request.json() as MoodCreateDTO
    if (!body)
      return HttpResponse.json({ message: 'Mood was not created' }, { status: 402 })
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    const nextId: number = moods.reduce((prevId, curEntry) => {
      return curEntry.id > prevId ? curEntry.id : prevId
    }, 0) + 1    
    const newMood: Mood = { id: nextId, ownerID: Number(body.ownerID), 
      scoreMood: body.scoreMood, scoreProductivity: body.scoreProductivity, bedtime: body.bedtime, 
      wakeUpTime: body.wakeUpTime, createdDate: formattedDate}
    moods.push(newMood)
    return HttpResponse.json(newMood, { status: 201 })
  }),
  http.get('/api/v1/owners/:ownerId/moods', ({ params }) => {
    const requiredMoods: Mood[] | undefined = moods.filter(v => v.ownerID === Number(params.ownerId))
    if (!Array.isArray(requiredMoods) || requiredMoods.length === 0) {
      return HttpResponse.json({ message: 'Moods not found' }, { status: 404 })
    }
    const requiredDtoMoods = requiredMoods.map(it => toMoodInfoDto(it))
    return HttpResponse.json(requiredDtoMoods)
  }),
  http.get('/api/v1/owners/:ownerId/can-create-mood', ({  }) => {
    const permission: MoodPermissionRes =  {allowed: Math.random() < 0.7};
    return HttpResponse.json(permission)
  }),
]
