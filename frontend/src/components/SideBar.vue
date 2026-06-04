<template>
  <aside class="sidebar">
    <div class="sidebar-logo">
      <div class="logo-icon">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 3L1 9L4 10.63V17.5C4 18.88 7.58 20 12 20C16.42 20 20 18.88 20 17.5V10.63L23 9L12 3ZM12 18.25C8.6 18.25 5.5 17.38 5.5 16V11.45L12 15L18.5 11.45V16C18.5 17.38 15.4 18.25 12 18.25ZM12 13.1L3.25 8.32L12 3.52L20.75 8.32L12 13.1Z"/></svg>
      </div>
      <div class="logo-text">
        <span class="logo-title">Кафедра</span>
        <span class="logo-subtitle">інформатики</span>
      </div>
    </div>

    <nav class="sidebar-nav">
      <router-link to="/admin" class="nav-item" active-class="nav-item-active">
        <span class="nav-icon">🏠</span> Про кафедру
      </router-link>
      <router-link to="/admin/teachers" class="nav-item" active-class="nav-item-active">
        <span class="nav-icon">👨‍🏫</span> Викладачі
      </router-link>
      <router-link to="/admin/groups" class="nav-item" active-class="nav-item-active">
        <span class="nav-icon">👥</span> Групи
      </router-link>
      <router-link to="/admin/students" class="nav-item" active-class="nav-item-active">
        <span class="nav-icon">🎓</span> Студенти
      </router-link>
      <router-link to="/admin/disciplines" class="nav-item" active-class="nav-item-active">
        <span class="nav-icon">📚</span> Дисципліни
      </router-link>
    </nav>

    <div class="sidebar-footer">
      <div class="user-profile">
        <div class="user-avatar">А</div>
        <div class="user-info">
          <span class="user-name">Адміністратор Іван</span>
          <span class="user-role">Адміністратор</span>
        </div>
      </div>

      <button class="btn-logout" @click="handleLogout">
        <span class="logout-icon">🚪</span> Вийти
      </button>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const handleLogout = () => {
  router.push('/login');
};
</script>

<style src="@/css/components/SideBar.css"></style>
