import { api } from './http'

export interface DisciplineResponse {
  id: number
  name: string
  description: string
  teacherIds: number[]
}

export interface CreateDisciplineRequest {
  name: string
  description: string
}

export const disciplinesApi = {
  getAll: () => api.get<DisciplineResponse[]>('/disciplines'),
  create: (data: CreateDisciplineRequest) => api.post<DisciplineResponse>('/disciplines', data),
  update: (id: number, data: CreateDisciplineRequest) =>
    api.put<DisciplineResponse>(`/disciplines/${id}`, data),
  delete: (id: number) => api.delete(`/disciplines/${id}`)
}
