import { createRouter, createWebHistory } from 'vue-router';
import MainLayout from '../layouts/MainLayout.vue';
import LoginPage from '../views/auth/LoginPage.vue';
import Dashboard from '../views/admin/DashBoard.vue';
import TeachersPage from '../views/admin/TeacherPage.vue';
import GroupsPage from '../views/admin/GroupsPage.vue';
import StudentsPage from '../views/admin/StudentPage.vue';
import DisciplinesPage from '../views/admin/DisciplinesPage.vue';
import GradesPage from '../views/student/GradesPage.vue';

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    component: LoginPage
  },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'admin', component: Dashboard },
      { path: 'admin/teachers', component: TeachersPage },
      { path: 'admin/groups', component: GroupsPage },
      { path: 'admin/students', component: StudentsPage },
      { path: 'admin/disciplines', component: DisciplinesPage },
      { path: 'student', component: GradesPage }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
