import { api } from './http'

export interface GradeResponse {
  id: number
  studentId: number
  studentFirstName: string
  studentLastName: string
  disciplineId: number
  disciplineName: string
  teacherId: number
  value: number
  gradedAt: string
  comment: string | null
}

export interface CreateGradeRequest {
  studentId: number
  disciplineId: number
  value: number
  comment?: string
}

export interface UpdateGradeRequest {
  value: number
  comment?: string
}

export const gradesApi = {
  getByStudent: (studentId: number) =>
    api.get<GradeResponse[]>(`/grades/student/${studentId}`),
  getByTeacher: (teacherId: number) =>
    api.get<GradeResponse[]>(`/grades/teacher/${teacherId}`),
  getByDiscipline: (disciplineId: number) =>
    api.get<GradeResponse[]>(`/grades/discipline/${disciplineId}`),
  getMyGrades: () =>
    api.get<GradeResponse[]>('/grades/my'),
  create: (data: CreateGradeRequest) =>
    api.post<GradeResponse>('/grades', data),
  update: (id: number, data: UpdateGradeRequest) =>
    api.put<GradeResponse>(`/grades/${id}`, data),
  delete: (id: number) =>
    api.delete(`/grades/${id}`)
}
