import { http, HttpResponse } from 'msw'
import { owners } from './data'

export const handlers = [
  http.get('/api/userы/:id', ({ params }) => {
    const user = owners.find(v => v.id === Number(params.id))
    if (!user) {
      return HttpResponse.json({ message: 'User not found' }, { status: 404 })
    }
    return HttpResponse.json(user)
  })
]