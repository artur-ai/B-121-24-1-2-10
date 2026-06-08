import { api } from './http'

export interface TeacherResponse {
  id: number
  firstName: string
  lastName: string
  email: string
  position: string
  userId: number | null
  disciplineIds: number[]
}

export interface CreateTeacherRequest {
  firstName: string
  lastName: string
  email: string
  position: string
  userId?: number | null
}

export interface UpdateTeacherRequest {
  firstName: string
  lastName: string
  email: string
  position: string
  userId?: number | null
}

export const teachersApi = {
  getAll: () => api.get<TeacherResponse[]>('/teachers'),
  create: (data: CreateTeacherRequest) => api.post<TeacherResponse>('/teachers', data),
  update: (id: number, data: UpdateTeacherRequest) => api.put<TeacherResponse>(`/teachers/${id}`, data),
  delete: (id: number) => api.delete(`/teachers/${id}`),
  assignDiscipline: (teacherId: number, disciplineId: number) =>
    api.postEmpty<TeacherResponse>(`/teachers/${teacherId}/disciplines/${disciplineId}`),
  removeDiscipline: (teacherId: number, disciplineId: number) =>
    api.delete(`/teachers/${teacherId}/disciplines/${disciplineId}`)
}
