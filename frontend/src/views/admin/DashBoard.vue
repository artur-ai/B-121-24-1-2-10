<template>
  <div class="dashboard-container">

    <section class="hero-card">
      <div class="hero-header">
        <div class="hero-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 3L1 9L4 10.63V17.5C4 18.88 7.58 20 12 20C16.42 20 20 18.88 20 17.5V10.63L23 9L12 3ZM12 18.25C8.6 18.25 5.5 17.38 5.5 16V11.45L12 15L18.5 11.45V16C18.5 17.38 15.4 18.25 12 18.25ZM12 13.1L3.25 8.32L12 3.52L20.75 8.32L12 13.1Z"/></svg>
        </div>
        <div class="hero-titles">
          <h2 class="hero-title">Кафедра інформатики</h2>
          <p class="hero-subtitle">Національний університет · Факультет комп'ютерних наук</p>
        </div>
      </div>

      <div class="hero-stats">
        <div class="stat-box">
          <div class="stat-number">{{ teachers.length || '—' }}</div>
          <div class="stat-label">Викладачів</div>
        </div>
        <div class="stat-box">
          <div class="stat-number">{{ disciplines.length || '—' }}</div>
          <div class="stat-label">Дисциплін</div>
        </div>
      </div>
    </section>

    <div class="tabs-container">
      <button class="tab-btn" :class="{ active: activeTab === 'about' }" @click="activeTab = 'about'">Про кафедру</button>
      <button class="tab-btn" :class="{ active: activeTab === 'teachers' }" @click="activeTab = 'teachers'">Викладачі</button>
      <button class="tab-btn" :class="{ active: activeTab === 'disciplines' }" @click="activeTab = 'disciplines'">Дисципліни</button>
    </div>

    <div class="info-section" v-if="activeTab === 'about'">
      <div class="content-card full-width">
        <h3 class="card-heading">Про кафедру</h3>
        <p class="card-text">
          Кафедра інформатики є одним із провідних підрозділів факультету комп'ютерних наук. Кафедра готує фахівців за напрямами «Комп'ютерні науки» та «Програмна інженерія». На кафедрі активно провадиться наукова діяльність, реалізуються дослідницькі проєкти у галузі штучного інтелекту, розподілених систем та кібербезпеки.
        </p>
      </div>

      <div class="cards-grid">
        <div class="content-card">
          <div class="card-label">ЗАВІДУВАЧ КАФЕДРИ</div>
          <h4 class="person-name">Шевченко Микола Петрович</h4>
          <p class="person-desc">Доктор технічних наук, Професор</p>
        </div>

        <div class="content-card">
          <div class="card-label">КОНТАКТИ</div>
          <ul class="contact-list">
            <li><span class="contact-icon">📧</span> kafedra@univ.ua</li>
            <li><span class="contact-icon">📞</span> +380 44 123-45-67</li>
            <li class="contact-muted">корп. 3, ауд. 301</li>
          </ul>
        </div>
      </div>
    </div>

    <div class="info-section" v-if="activeTab === 'teachers'">
      <div v-if="loading" class="state-message">Завантаження...</div>
      <div v-else-if="error" class="state-message error">{{ error }}</div>
      <div v-else-if="teachers.length === 0" class="content-card">
        <p class="card-text">Викладачів ще не додано.</p>
      </div>
      <div v-else class="teachers-grid">
        <div class="teacher-card" v-for="t in teachers" :key="t.id">
          <div class="teacher-avatar">{{ initials(t) }}</div>
          <div class="teacher-info">
            <div class="teacher-name">{{ t.lastName }} {{ t.firstName }}</div>
            <div class="teacher-position">{{ [t.position, t.degree].filter(Boolean).join(' · ') }}</div>
          </div>
          <div class="teacher-email">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 18H4V8L12 13L20 8V18ZM12 11L4 6H20L12 11Z"/></svg>
            {{ t.email }}
          </div>
        </div>
      </div>
    </div>

    <div class="info-section" v-if="activeTab === 'disciplines'">
      <div v-if="loading" class="state-message">Завантаження...</div>
      <div v-else-if="error" class="state-message error">{{ error }}</div>
      <div v-else-if="disciplines.length === 0" class="content-card">
        <p class="card-text">Дисциплін ще не додано.</p>
      </div>
      <div v-else class="disciplines-list-dash">
        <div class="discipline-item" v-for="d in disciplines" :key="d.id">
          <div class="discipline-avatar">{{ d.name?.[0]?.toUpperCase() }}</div>
          <div class="discipline-info">
            <div class="discipline-name">{{ d.name }}</div>
            <div class="discipline-desc" v-if="teacherName(d.teacherIds)">
              Викладач: {{ teacherName(d.teacherIds) }}
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { teachersApi, type TeacherResponse } from '../../api/teachersApi'
import { disciplinesApi, type DisciplineResponse } from '../../api/disciplinesApi'

const activeTab = ref('about')
const loading = ref(false)
const error = ref('')

const teachers = ref<TeacherResponse[]>([])
const disciplines = ref<DisciplineResponse[]>([])

const initials = (t: TeacherResponse) =>
  `${t.lastName?.[0] ?? ''}${t.firstName?.[0] ?? ''}`.toUpperCase()

const teacherName = (ids: number[]) => {
  if (!ids?.length) return ''
  return ids
    .map(id => teachers.value.find(t => t.id === id))
    .filter(Boolean)
    .map(t => `${t!.lastName} ${t!.firstName}`)
    .join(', ')
}

onMounted(async () => {
  loading.value = true
  error.value = ''
  try {
    const [t, d] = await Promise.all([teachersApi.getAll(), disciplinesApi.getAll()])
    teachers.value = t
    disciplines.value = d
  } catch {
    error.value = 'Не вдалося завантажити дані'
  } finally {
    loading.value = false
  }
})
</script>

<style src="@/css/views/Dashboard.css"></style>
