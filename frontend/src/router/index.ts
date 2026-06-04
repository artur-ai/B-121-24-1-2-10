import { createRouter, createWebHistory } from 'vue-router';
import MainLayout from '../layouts/MainLayout.vue';
import LoginPage from '../views/auth/LoginPage.vue';
import Dashboard from '../views/admin/DashBoard.vue';
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
      { path: 'student', component: GradesPage },
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
