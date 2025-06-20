import { http, HttpResponse } from 'msw'
import { ownersInfo } from './dataOwners'
import { entriesInfo, entriesUpdate } from './dataEntries'
import type { EntryInfoDTO, EntryUpdateDTO } from '@/types/Entry'

export const handlers = [
  http.get('/api/owners/:id', ({ params }) => {
    const user = ownersInfo.find(v => v.id === Number(params.id))
    if (!user) {
      return HttpResponse.json({ message: 'User not found' }, { status: 404 })
    }
    return HttpResponse.json(user)
  }),

  http.get('/api/entries/:id', ({ params }) => {
    const entry: EntryInfoDTO | undefined = entriesInfo.find(v => v.id === Number(params.id))
    if (!entry) {
      return HttpResponse.json({ message: 'Entry not found' }, { status: 404 })
    }
    return HttpResponse.json(entry)
  }),
]
