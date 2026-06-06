<template>
  <div class="public-page">
    <header class="public-header">
      <div class="public-header-inner">
        <div class="public-logo">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 3L1 9L4 10.63V17.5C4 18.88 7.58 20 12 20C16.42 20 20 18.88 20 17.5V10.63L23 9L12 3Z"/></svg>
          <span>Кафедра інформатики</span>
        </div>
        <router-link to="/login" class="btn btn-login">Увійти</router-link>
      </div>
    </header>

    <main class="public-main">
      <section class="public-hero">
        <h1>Кафедра інформатики</h1>
        <p>Національний університет · Факультет комп'ютерних наук</p>
        <div class="public-stats">
          <div class="pub-stat">
            <div class="pub-stat-num">{{ teachers.length }}</div>
            <div class="pub-stat-label">Викладачів</div>
          </div>
          <div class="pub-stat">
            <div class="pub-stat-num">{{ disciplines.length }}</div>
            <div class="pub-stat-label">Дисциплін</div>
          </div>
        </div>
      </section>

      <div class="public-tabs">
        <button class="pub-tab" :class="{ active: tab === 'teachers' }" @click="tab = 'teachers'">Викладачі</button>
        <button class="pub-tab" :class="{ active: tab === 'disciplines' }" @click="tab = 'disciplines'">Дисципліни</button>
        <button class="pub-tab" :class="{ active: tab === 'about' }" @click="tab = 'about'">Про кафедру</button>
      </div>

      <div v-if="loading" class="pub-state">Завантаження...</div>
      <div v-else-if="error" class="pub-state error">{{ error }}</div>

      <div v-else-if="tab === 'teachers'" class="pub-teachers-grid">
        <div class="pub-teacher-card" v-for="t in teachers" :key="t.id">
          <div class="pub-avatar">{{ initials(t) }}</div>
          <div class="pub-teacher-info">
            <div class="pub-teacher-name">{{ t.lastName }} {{ t.firstName }}</div>
            <div class="pub-teacher-pos">{{ t.position }}</div>
            <div class="pub-teacher-email">{{ t.email }}</div>
          </div>
        </div>
        <div v-if="teachers.length === 0" class="pub-empty">Викладачів ще не додано</div>
      </div>

      <div v-else-if="tab === 'disciplines'" class="pub-disciplines-list">
        <div class="pub-disc-card" v-for="d in disciplines" :key="d.id">
          <div class="pub-disc-name">{{ d.name }}</div>
          <div class="pub-disc-desc" v-if="d.description">{{ d.description }}</div>
          <div class="pub-disc-teacher" v-if="teacherName(d.teacherIds)">
            Викладач: {{ teacherName(d.teacherIds) }}
          </div>
        </div>
        <div v-if="disciplines.length === 0" class="pub-empty">Дисциплін ще не додано</div>
      </div>

      <div v-else-if="tab === 'about'" class="pub-about-card">
        <h2>Про кафедру</h2>
        <p>Кафедра інформатики є одним із провідних підрозділів факультету комп'ютерних наук. Кафедра готує фахівців за напрямами «Комп'ютерні науки» та «Програмна інженерія». На кафедрі активно провадиться наукова діяльність у галузі штучного інтелекту, розподілених систем та кібербезпеки.</p>
        <div class="pub-contacts">
          <div>📧 kafedra@univ.ua</div>
          <div>📞 +380 44 123-45-67</div>
          <div>📍 корп. 3, ауд. 301</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { teachersApi, type TeacherResponse } from '../../api/teachersApi'
import { disciplinesApi, type DisciplineResponse } from '../../api/disciplinesApi'

const tab = ref('teachers')
const teachers = ref<TeacherResponse[]>([])
const disciplines = ref<DisciplineResponse[]>([])
const loading = ref(false)
const error = ref('')

const initials = (t: TeacherResponse) =>
  `${t.lastName?.[0] ?? ''}${t.firstName?.[0] ?? ''}`.toUpperCase()

const teacherName = (ids: number[]) =>
  ids?.map(id => teachers.value.find(t => t.id === id))
    .filter(Boolean)
    .map(t => `${t!.lastName} ${t!.firstName}`)
    .join(', ') ?? ''

onMounted(async () => {
  loading.value = true
  error.value = ''
  try {
    const [t, d] = await Promise.all([teachersApi.getAll(), disciplinesApi.getAll()])
    teachers.value = t
    disciplines.value = d
  } catch {
    error.value = 'Не вдалося завантажити дані.'
  } finally {
    loading.value = false
  }
})
</script>

<style src="@/css/views/public/PublicPage.css"></style>
