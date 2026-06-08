<template>
  <div class="login-page">
    <div class="login-card">
      <div class="card-header">
        <div class="header-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 3L1 9L4 10.63V17.5C4 18.88 7.58 20 12 20C16.42 20 20 18.88 20 17.5V10.63L23 9L12 3ZM12 18.25C8.6 18.25 5.5 17.38 5.5 16V11.45L12 15L18.5 11.45V16C18.5 17.38 15.4 18.25 12 18.25ZM12 13.1L3.25 8.32L12 3.52L20.75 8.32L12 13.1Z"/></svg>
        </div>
        <h1 class="header-title">Кафедра інформатики</h1>
        <p class="header-subtitle">Система управління навчальним процесом</p>
      </div>

      <div class="card-body">
        <div class="auth-tabs">
          <button class="auth-tab" :class="{ active: mode === 'login' }" @click="switchMode('login')">Вхід</button>
          <button class="auth-tab" :class="{ active: mode === 'register' }" @click="switchMode('register')">Реєстрація</button>
        </div>

        <div v-if="errorMsg" class="login-error">{{ errorMsg }}</div>
        <div v-if="successMsg" class="login-success">{{ successMsg }}</div>

        <div class="login-form">
          <div class="login-field">
            <label>Логін</label>
            <input type="text" v-model="username" placeholder="Введіть логін" @keyup.enter="handleSubmit" />
          </div>
          <div class="login-field">
            <label>Пароль</label>
            <input type="password" v-model="password" placeholder="Введіть пароль (мін. 6 символів)" @keyup.enter="handleSubmit" />
          </div>
          <div v-if="mode === 'register'" class="login-field">
            <label>Роль</label>
            <select v-model="role" class="login-select">
              <option value="ADMIN">Адміністратор</option>
              <option value="MANAGER">Менеджер</option>
              <option value="TEACHER">Викладач</option>
              <option value="STUDENT">Студент</option>
            </select>
          </div>
        </div>

        <div class="actions-group">
          <button
            class="btn btn-primary"
            :disabled="loading || !username || !password || (mode === 'register' && !role)"
            @click="handleSubmit"
          >
            {{ loading ? '...' : (mode === 'login' ? 'Увійти до системи' : 'Зареєструватись') }}
          </button>
          <router-link to="/admin" class="btn btn-secondary guest-btn">Переглянути як гість</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../../api/authApi'
import { useAuthStore } from '../../stores/authStore'

const router = useRouter()
const authStore = useAuthStore()
authStore.clearAuth()

const mode = ref<'login' | 'register'>('login')
const username = ref('')
const password = ref('')
const role = ref('STUDENT')
const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')

const switchMode = (m: 'login' | 'register') => {
  mode.value = m
  errorMsg.value = ''
  successMsg.value = ''
}

const redirectAfterLogin = (userRole: string) => {
  if (userRole === 'STUDENT') return router.push('/student')
  if (userRole === 'TEACHER') return router.push('/teacher')
  router.push('/admin')
}

const handleSubmit = async () => {
  if (!username.value || !password.value) return
  loading.value = true
  errorMsg.value = ''
  successMsg.value = ''
  try {
    if (mode.value === 'login') {
      const res = await authApi.login({ username: username.value, password: password.value })
      authStore.setAuth(res.token, res.username, res.role)
      redirectAfterLogin(res.role)
    } else {
      const res = await authApi.register({ username: username.value, password: password.value, role: role.value })
      authStore.setAuth(res.token, res.username, res.role)
      redirectAfterLogin(res.role)
    }
  } catch (err: any) {
    if (mode.value === 'login') {
      errorMsg.value = 'Невірний логін або пароль'
    } else {
      const msg = err?.message || ''
      if (msg.includes('409')) errorMsg.value = 'Такий логін вже існує'
      else if (msg.includes('400')) errorMsg.value = 'Помилка даних. Пароль мінімум 6 символів'
      else if (msg.includes('Failed') || msg.includes('fetch')) errorMsg.value = 'Сервер недоступний. Перевірте що backend запущений'
      else errorMsg.value = `Помилка реєстрації (${msg})`
    }
  } finally {
    loading.value = false
  }
}
</script>

<style src="@/css/views/LoginPage.css"></style>
