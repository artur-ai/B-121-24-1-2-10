import { api } from './http'

export interface StudentResponse {
  id: number
  firstName: string
  lastName: string
  email: string
  groupId: number | null
  groupName: string | null
  userId: number | null
}

export interface CreateStudentRequest {
  firstName: string
  lastName: string
  email: string
  groupId: number | null
  userId?: number | null
}

export interface UpdateStudentRequest {
  firstName: string
  lastName: string
  email: string
  groupId: number | null
}

export const studentsApi = {
  getAll: () => api.get<StudentResponse[]>('/students'),
  getByGroup: (groupId: number) => api.get<StudentResponse[]>(`/students/group/${groupId}`),
  create: (data: CreateStudentRequest) => api.post<StudentResponse>('/students', data),
  update: (id: number, data: UpdateStudentRequest) => api.put<StudentResponse>(`/students/${id}`, data),
  delete: (id: number) => api.delete(`/students/${id}`)
}
