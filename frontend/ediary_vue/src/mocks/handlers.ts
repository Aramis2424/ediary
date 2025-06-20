import { http, HttpResponse } from 'msw'
import { owners } from './dataOwners'

import { entries, toInfoDto } from './dataEntries'
import type { Entry } from '@/types/Entry'

export const handlers = [
  http.get('/api/owners/:id', ({ params }) => {
    const user = owners.find(v => v.id === Number(params.id))
    if (!user) {
      return HttpResponse.json({ message: 'User not found' }, { status: 404 })
    }
    return HttpResponse.json(user)
  }),

  http.get('/api/entries/:id', ({ params }) => {
    const entry: Entry | undefined = entries.find(v => v.id === Number(params.id))
    if (!entry) {
      return HttpResponse.json({ message: 'Entry not found' }, { status: 404 })
    }
    return HttpResponse.json(toInfoDto(entry))
  }),
]
