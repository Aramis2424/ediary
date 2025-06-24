import { http, HttpResponse } from 'msw'

import { owners } from './dataOwners'
import type { OwnerLoginDTO, OwnerCreateDTO, TokenRequest } from '@/types/Owner'

import { entries, toInfoDto } from './dataEntries'
import type { Entry, EntryCreateDTO } from '@/types/Entry'

import { getCards } from './dataEntryCards'
import type { EntryCard } from '@/types/EntryCard'

import { diaries, toInfoDto as toDiaryInfoDto } from './dataDiaries'
import type { Diary } from '@/types/Diary'

const token = "token123"

export const handlers = [
  http.post('/api/owner/login', async ({ request }) => {    
    const { login, password } = await request.json() as OwnerLoginDTO
    const user = owners.find(v => v.login === login && v.password === password)
    if (!user) {
      return HttpResponse.json({ message: 'User not found' }, { status: 404 })
    }
    return HttpResponse.json(user)
  }),
  http.post('/api/owner/register', async ({ request }) => {
    const newOwner = await request.json() as OwnerCreateDTO
    const user = owners.find(v => v.login === newOwner.login)
    if (user) {
      return HttpResponse.json({ message: 'User already exists' }, { status: 405 })
    }
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    const nextId: number = owners.reduce((prevId, curEntry) => {
      return curEntry.id > prevId ? curEntry.id : prevId
    }, 0) + 1 
    owners.push({id: nextId, createdDate: formattedDate, ...newOwner})
    return HttpResponse.json(user)
  }),
  http.post('/api/token/create', async ({ request }) => {
    const { login, password } = await request.json() as TokenRequest
    const user = owners.find(v => v.login === login && v.password === password)
    if (!user) {
      return HttpResponse.json({ message: 'Invalid credentials' }, { status: 401 })
    }
    return HttpResponse.json({token: token})
  }),

  http.get('/api/entries/:id', ({ params }) => {
    const entry: Entry | undefined = entries.find(v => v.id === Number(params.id))
    if (!entry) {
      return HttpResponse.json({ message: 'Entry not found' }, { status: 404 })
    }
    return HttpResponse.json(toInfoDto(entry))
  }),
  http.post('/api/entries', async ({ request }) => {
    const body = await request.json() as EntryCreateDTO
    if (!body)
      return HttpResponse.json({ message: 'Entry was not created' }, { status: 402 })
    const today = new Date();
    const formattedDate = today.toISOString().split('T')[0];
    const nextId: number = entries.reduce((prevId, curEntry) => {
      return curEntry.id > prevId ? curEntry.id : prevId
    }, 0) + 1    
    const newEntry: Entry = { id: nextId, diaryId: Number(body.diaryId), 
      title: body.title, content: body.content, createdDate: formattedDate}
    entries.push(newEntry)
    return HttpResponse.json(newEntry, { status: 201 })
  }),
  http.patch('/api/entries/:id', async ({ params, request  }) => {
    const entry: Entry | undefined = entries.find(v => v.id === Number(params.id))
    if (!entry) {
      return HttpResponse.json({ message: 'Entry not found' }, { status: 404 })
    }
    const patch = await request.json()
    Object.assign(entry, patch)
    return HttpResponse.json(entry)
  }),
  http.delete('/api/entries/:id', ({ params }) => {
    const filtered = entries.filter(v => v.id !== Number(params.id))

    entries.length = 0;
    entries.push(...filtered);

    return HttpResponse.json({ success: true })
  }),

  http.get('/api/entryCards/:id', ({ params }) => {
    const cards: EntryCard[] | undefined = getCards().filter(v => v.diaryId === Number(params.id))
    if (!cards) {
      return HttpResponse.json({ message: 'Cards not found' }, { status: 404 })
    }
    return HttpResponse.json(cards)
  }),

  http.get('/api/diaries/:ownerId', ({ params }) => {
    const diary: Diary | undefined = diaries.find(v => v.ownerId === Number(params.ownerId))
    if (!diary) {
      return HttpResponse.json({ message: 'Diary not found' }, { status: 404 })
    }
    return HttpResponse.json(toDiaryInfoDto(diary))
  }),
]
