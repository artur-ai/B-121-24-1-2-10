<template>
  <div class="login-page">
    <div class="login-card">
      <div class="card-header">
        <div class="header-icon">
        </div>
        <h1 class="header-title">Кафедра інформатики</h1>
        <p class="header-subtitle">Система управління навчальним процесом</p>
      </div>

      <div class="card-body">
        <div class="body-title">Оберіть роль для демонстрації:</div>

        <div class="role-list">
          <div class="role-item role-admin"
               :class="{'is-selected': selectedRole === 'admin'}"
               @click="selectRole('admin')">
            <div class="role-avatar">А</div>
            <div class="role-info">
              <span class="role-name">Адміністратор Максим</span>
              <span class="role-desc">Адміністратор • admin@univ.ua</span>
            </div>
          </div>

          <div class="role-item role-student"
               :class="{'is-selected': selectedRole === 'student'}"
               @click="selectRole('student')">
            <div class="role-avatar">Б</div>
            <div class="role-info">
              <span class="role-name">Бондаренко Олена Сергіївна</span>
              <span class="role-desc">Студент • bondarenko@student.ua</span>
            </div>
          </div>
        </div>

        <div class="actions-group">
          <button class="btn btn-primary"
                  :disabled="!selectedRole"
                  @click="handleLogin">
            Увійти до системи
          </button>

          <button class="btn btn-secondary" @click="handleGuest">
            Продовжити як гість
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const selectedRole= ref <string | null > (null);

const selectRole = (role: string ) => {
  selectedRole.value = role;
};
const handleLogin = () => {
  if (!selectedRole.value) return;
if (selectedRole.value === 'admin') {
    router.push('/admin');
  } else if (selectedRole.value === 'student') {
    router.push('/student');
  }
};
const handleGuest =() => {
  router.push('/shared/guest');
}





</script>
<style src="@/css/views/LoginPage.css"></style>
