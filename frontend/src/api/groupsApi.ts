import { api } from './http'

export interface GroupResponse {
  id: number
  name: string
  year: number
  studentCount: number
}

export interface GroupRequest {
  name: string
  year: number
}

export const groupsApi = {
  getAll: () => api.get<GroupResponse[]>('/groups'),
  create: (data: GroupRequest) => api.post<GroupResponse>('/groups', data),
  update: (id: number, data: GroupRequest) => api.put<GroupResponse>(`/groups/${id}`, data),
  delete: (id: number) => api.delete(`/groups/${id}`)
}
