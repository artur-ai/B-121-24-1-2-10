<template>
  <div class="students-page">
    <div class="toolbar">
      <div class="search-box">
        <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3C5.91 3 3 5.91 3 9.5C3 13.09 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5C5 7.01 7.01 5 9.5 5C11.99 5 14 7.01 14 9.5C14 11.99 11.99 14 9.5 14Z"/></svg>
        <input type="text" placeholder="Пошук студентів..." class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters">
        <select class="filter-select" v-model="selectedGroup">
          <option value="">Усі групи</option>
          <option v-for="g in availableGroups" :key="g.id" :value="g.name">{{ g.name }}</option>
        </select>

        <select class="filter-select" v-model="sortBy">
          <option value="name_asc">Сортувати: А-Я</option>
          <option value="name_desc">Сортувати: Я-А</option>
        </select>

        <button v-if="canEdit" class="btn btn-primary add-btn" @click="handleAdd">+ Додати</button>
      </div>
    </div>

    <div class="results-count">Знайдено: {{ filteredStudents.length }}</div>

    <div v-if="loading" class="state-message">Завантаження...</div>
    <div v-else-if="error" class="state-message error">{{ error }}</div>
    <div v-else class="students-list">
      <div class="student-card" v-for="student in filteredStudents" :key="student.id">
        <div class="student-avatar">{{ student.initials }}</div>

        <div class="student-details">
          <div class="student-name">{{ student.name }}</div>
          <div class="student-subtitle">{{ student.groupName || 'Без групи' }}</div>

          <div class="student-contacts">
            <span class="contact-item">
              <svg class="contact-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 18H4V8L12 13L20 8V18ZM12 11L4 6H20L12 11Z"/></svg>
              {{ student.email }}
            </span>
          </div>
        </div>

        <div v-if="canEdit" class="student-actions">
          <button class="action-btn" @click="handleEdit(student)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z"/></svg>
          </button>
          <button class="action-btn delete-btn" @click="handleDelete(student.id)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z"/></svg>
          </button>
        </div>
      </div>

      <div v-if="filteredStudents.length === 0" style="text-align: center; color: var(--text-muted); padding: 40px;">
        За вашим запитом нікого не знайдено
      </div>
    </div>

    <StudentModal
      :is-open="isModalOpen"
      :student-to-edit="editingStudent"
      :groups="availableGroups"
      @close="isModalOpen = false"
      @save="saveStudent"
    />

    <div class="delete-confirm-overlay" v-if="deletingId !== null" @click.self="deletingId = null">
      <div class="delete-confirm">
        <p>Ви впевнені, що хочете видалити цього студента?</p>
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
import StudentModal from '../../components/StudentModal.vue'
import { studentsApi, type StudentResponse } from '../../api/studentsApi'
import { groupsApi, type GroupResponse } from '../../api/groupsApi'
import { useAuthStore } from '../../stores/authStore'

const authStore = useAuthStore()
const canEdit = computed(() => authStore.isRole('ADMIN', 'MANAGER'))

const searchQuery = ref('')
const selectedGroup = ref('')
const sortBy = ref('name_asc')

const isModalOpen = ref(false)
const editingStudent = ref<any>(null)
const deletingId = ref<number | null>(null)
const loading = ref(false)
const error = ref('')

const availableGroups = ref<GroupResponse[]>([])

interface StudentUI {
  id: number
  initials: string
  name: string
  email: string
  groupId: number | null
  groupName: string
  firstName: string
  lastName: string
}

const students = ref<StudentUI[]>([])

function mapStudent(s: StudentResponse): StudentUI {
  const name = `${s.lastName} ${s.firstName}`.trim()
  const initials = `${s.lastName?.[0] ?? ''}${s.firstName?.[0] ?? ''}`.toUpperCase()
  return {
    id: s.id,
    initials,
    name,
    email: s.email,
    groupId: s.groupId ?? null,
    groupName: s.groupName ?? '',
    firstName: s.firstName,
    lastName: s.lastName
  }
}

async function fetchAll() {
  loading.value = true
  error.value = ''
  try {
    const [studentsData, groupsData] = await Promise.all([
      studentsApi.getAll(),
      groupsApi.getAll()
    ])
    students.value = studentsData.map(mapStudent)
    availableGroups.value = groupsData
  } catch {
    error.value = 'Не вдалося завантажити дані. Перевірте підключення до сервера.'
  } finally {
    loading.value = false
  }
}

onMounted(fetchAll)

const filteredStudents = computed(() => {
  let result = students.value
  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase()
    result = result.filter(s => s.name.toLowerCase().includes(q) || s.email.toLowerCase().includes(q))
  }
  if (selectedGroup.value) {
    result = result.filter(s => s.groupName === selectedGroup.value)
  }
  return [...result].sort((a, b) =>
    sortBy.value === 'name_asc' ? a.name.localeCompare(b.name) : b.name.localeCompare(a.name)
  )
})

const handleAdd = () => {
  editingStudent.value = null
  isModalOpen.value = true
}

const handleEdit = (student: StudentUI) => {
  editingStudent.value = student
  isModalOpen.value = true
}

const handleDelete = (id: number) => {
  deletingId.value = id
}

const confirmDelete = async () => {
  if (deletingId.value === null) return
  try {
    await studentsApi.delete(deletingId.value)
    await fetchAll()
  } catch {
    error.value = 'Не вдалося видалити студента.'
  } finally {
    deletingId.value = null
  }
}

const saveStudent = async (formData: any) => {
  try {
    if (formData.id) {
      await studentsApi.update(formData.id, {
        firstName: formData.firstName || '',
        lastName: formData.lastName || '',
        email: formData.email || '',
        groupId: formData.groupId ?? null
      })
    } else {
      await studentsApi.create({
        firstName: formData.firstName || '',
        lastName: formData.lastName || '',
        email: formData.email || '',
        groupId: formData.groupId ?? null,
        userId: formData.userId || null
      })
    }
    isModalOpen.value = false
    await fetchAll()
  } catch {
    error.value = 'Не вдалося зберегти студента.'
  }
}
</script>

<style src="../../css/views/admin/StudentsPage.css"></style>
