<template>
  <div class="teachers-page">
    <div class="toolbar">
      <div class="search-box">
        <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3C5.91 3 3 5.91 3 9.5C3 13.09 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5C5 7.01 7.01 5 9.5 5C11.99 5 14 7.01 14 9.5C14 11.99 11.99 14 9.5 14Z"/></svg>
        <input type="text" placeholder="Пошук викладачів..." class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters">
        <select class="filter-select" v-model="selectedPosition">
          <option value="Усі посади">Усі посади</option>
          <option value="Професор">Професор</option>
          <option value="Доцент">Доцент</option>
          <option value="Старший викладач">Старший викладач</option>
          <option value="Асистент">Асистент</option>
        </select>

        <select class="filter-select" v-model="sortBy">
          <option value="name_asc">Сортувати: А-Я</option>
          <option value="name_desc">Сортувати: Я-А</option>
        </select>

        <button v-if="canEdit" class="btn btn-primary add-btn" @click="handleAdd">+ Додати</button>
      </div>
    </div>

    <div class="results-count">Знайдено: {{ filteredTeachers.length }}</div>

    <div v-if="loading" class="state-message">Завантаження...</div>
    <div v-else-if="error" class="state-message error">{{ error }}</div>
    <div v-else class="teachers-list">
      <div class="teacher-card" v-for="teacher in filteredTeachers" :key="teacher.id">
        <div class="teacher-avatar">{{ teacher.initials }}</div>

        <div class="teacher-details">
          <div class="teacher-name">{{ teacher.name }}</div>
          <div class="teacher-subtitle">{{ teacher.position }}</div>

          <div class="teacher-contacts">
            <span class="contact-item">
              <svg class="contact-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 18H4V8L12 13L20 8V18ZM12 11L4 6H20L12 11Z"/></svg>
              {{ teacher.email }}
            </span>
          </div>
        </div>

        <div v-if="canEdit" class="teacher-actions">
          <button class="action-btn" @click="handleEdit(teacher)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z"/></svg>
          </button>
          <button class="action-btn delete-btn" @click="handleDelete(teacher.id)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z"/></svg>
          </button>
        </div>
      </div>

      <div v-if="filteredTeachers.length === 0" style="text-align: center; color: var(--text-muted); padding: 40px;">
        За вашим запитом нікого не знайдено
      </div>
    </div>

    <TeacherModal
      :is-open="isModalOpen"
      :teacher-to-edit="editingTeacher"
      @close="isModalOpen = false"
      @save="saveTeacher"
    />

    <div class="delete-confirm-overlay" v-if="deletingId !== null" @click.self="deletingId = null">
      <div class="delete-confirm">
        <p>Ви впевнені, що хочете видалити цього викладача?</p>
        <div class="delete-confirm-actions">
          <button class="btn btn-secondary" @click="deletingId = null">Скасувати</button>
          <button class="btn btn-danger" @click="confirmDelete">Видалити</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import TeacherModal from '../../components/TeacherModal.vue'
import { teachersApi, type TeacherResponse } from '../../api/teachersApi'
import { useAuthStore } from '../../stores/authStore'

const authStore = useAuthStore()
const canEdit = computed(() => authStore.isRole('ADMIN', 'MANAGER'))

const searchQuery = ref('')
const selectedPosition = ref('Усі посади')
const sortBy = ref('name_asc')

const isModalOpen = ref(false)
const editingTeacher = ref<any>(null)
const deletingId = ref<number | null>(null)
const loading = ref(false)
const error = ref('')

interface TeacherUI {
  id: number
  initials: string
  name: string
  position: string
  email: string
  firstName: string
  lastName: string
  userId: number | null
}

const teachers = ref<TeacherUI[]>([])

function mapTeacher(t: TeacherResponse): TeacherUI {
  const name = `${t.lastName} ${t.firstName}`.trim()
  const initials = `${(t.lastName[0] || '')}${(t.firstName[0] || '')}`.toUpperCase()
  return { id: t.id, initials, name, position: t.position || '', email: t.email, firstName: t.firstName, lastName: t.lastName, userId: t.userId ?? null }
}

async function fetchTeachers() {
  loading.value = true
  error.value = ''
  try {
    const data = await teachersApi.getAll()
    teachers.value = data.map(mapTeacher)
  } catch (e: any) {
    error.value = 'Не вдалося завантажити викладачів. Перевірте підключення до сервера.'
  } finally {
    loading.value = false
  }
}

onMounted(fetchTeachers)

const filteredTeachers = computed(() => {
  let result = teachers.value
  if (searchQuery.value) result = result.filter(t => t.name.toLowerCase().includes(searchQuery.value.toLowerCase()))
  if (selectedPosition.value !== 'Усі посади') result = result.filter(t => t.position === selectedPosition.value)
  result = [...result].sort((a, b) => sortBy.value === 'name_asc' ? a.name.localeCompare(b.name) : b.name.localeCompare(a.name))
  return result
})

const handleAdd = () => {
  editingTeacher.value = null
  isModalOpen.value = true
}

const handleEdit = (teacher: TeacherUI) => {
  editingTeacher.value = { ...teacher }
  isModalOpen.value = true
}

const handleDelete = (id: number) => {
  deletingId.value = id
}

const confirmDelete = async () => {
  if (deletingId.value === null) return
  try {
    await teachersApi.delete(deletingId.value)
    await fetchTeachers()
  } catch {
    error.value = 'Не вдалося видалити викладача.'
  } finally {
    deletingId.value = null
  }
}

const saveTeacher = async (formData: any) => {
  try {
    if (formData.id) {
      await teachersApi.update(formData.id, { firstName: formData.firstName || '', lastName: formData.lastName || '', email: formData.email || '', position: formData.position || '' })
    } else {
      await teachersApi.create({ firstName: formData.firstName || '', lastName: formData.lastName || '', email: formData.email || '', position: formData.position || '', userId: formData.userId || null })
    }
    isModalOpen.value = false
    await fetchTeachers()
  } catch {
    error.value = 'Не вдалося зберегти викладача.'
  }
}
</script>

<style src="../../css/views/admin/TeacherPage.css"></style>
