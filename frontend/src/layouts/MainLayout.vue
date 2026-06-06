<template>
  <div class="main-layout">
    <SideBar />

    <div class="layout-wrapper">
      <header class="top-header">
        <h1 class="page-title">{{ currentPageTitle }}</h1>
      </header>

      <main class="layout-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import SideBar from '../components/SideBar.vue';

const route = useRoute();

const currentPageTitle = computed(() => {
  switch (route.path) {
    case '/admin': return 'Про кафедру';
    case '/admin/teachers': return 'Викладачі';
    case '/admin/groups': return 'Групи';
    case '/admin/students': return 'Студенти';
    case '/admin/disciplines': return 'Дисципліни';
    case '/teacher': return 'Оцінки студентів';
    case '/student': return 'Мої оцінки';
    default: return 'Система управління';
  }
});
</script>

<style src="@/css/layouts/MainLayout.css"></style>
