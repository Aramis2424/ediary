import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

export const api = axios.create({
  //baseURL: '/api/v1',
  baseURL: 'http://localhost:2323/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.token) {
    config.headers.Authorization = `Bearer ${auth.token}`
  }
  return config
})
