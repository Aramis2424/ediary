import { http, HttpResponse } from 'msw'
import { ownersInfo } from './dataOwners'

export const handlers = [
  http.get('/api/owners/:id', ({ params }) => {
    const user = ownersInfo.find(v => v.id === Number(params.id))
    if (!user) {
      return HttpResponse.json({ message: 'User not found' }, { status: 404 })
    }
    return HttpResponse.json(user)
  })
]