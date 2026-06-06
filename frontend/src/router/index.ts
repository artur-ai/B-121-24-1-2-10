import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import LoginPage from '../views/auth/LoginPage.vue'
import Dashboard from '../views/admin/DashBoard.vue'
import TeachersPage from '../views/admin/TeacherPage.vue'
import GroupsPage from '../views/admin/GroupsPage.vue'
import StudentsPage from '../views/admin/StudentPage.vue'
import DisciplinesPage from '../views/admin/DisciplinesPage.vue'
import GradesPage from '../views/student/GradesPage.vue'
import TeacherGradesPage from '../views/teacher/TeacherGradesPage.vue'
import PublicPage from '../views/public/PublicPage.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginPage },
  { path: '/public', component: PublicPage },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'admin', component: Dashboard },
      { path: 'admin/teachers', component: TeachersPage },
      { path: 'admin/groups', component: GroupsPage, meta: { roles: ['ADMIN', 'MANAGER'] } },
      { path: 'admin/students', component: StudentsPage, meta: { roles: ['ADMIN', 'MANAGER'] } },
      { path: 'admin/disciplines', component: DisciplinesPage },
      { path: 'student', component: GradesPage, meta: { roles: ['STUDENT'] } },
      { path: 'teacher', component: TeacherGradesPage, meta: { roles: ['TEACHER', 'ADMIN', 'MANAGER'] } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  const allowedRoles = to.meta.roles as string[] | undefined
  if (allowedRoles) {
    if (!token) return '/login'
    if (role && !allowedRoles.includes(role)) {
      if (role === 'STUDENT') return '/student'
      if (role === 'TEACHER') return '/teacher'
      return '/admin'
    }
  }
})

export default router
