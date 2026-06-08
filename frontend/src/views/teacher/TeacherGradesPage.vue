<template>
  <div class="tg-page">
    <div class="tg-page-header">
      <h1 class="tg-title">Виставлення оцінок</h1>
    </div>

    <div v-if="loadingTeacher" class="tg-state">Завантаження профілю...</div>

    <div v-else-if="!teacher" class="tg-no-profile">
      <p>Ваш обліковий запис не пов'язаний з жодним викладачем. Зверніться до адміністратора.</p>
    </div>

    <template v-else>
      <div class="tg-controls">
        <div class="tg-control-group">
          <label class="tg-label">Дисципліна</label>
          <select class="tg-select" v-model="selectedDisciplineId" @change="onDisciplineChange">
            <option :value="null" disabled>Оберіть дисципліну</option>
            <option v-for="d in myDisciplines" :key="d.id" :value="d.id">{{ d.name }}</option>
          </select>
        </div>

        <div class="tg-control-group">
          <label class="tg-label">Група</label>
          <select class="tg-select" v-model="selectedGroupId" @change="onGroupChange" :disabled="!selectedDisciplineId">
            <option :value="null" disabled>Оберіть групу</option>
            <option v-for="g in allGroups" :key="g.id" :value="g.id">{{ g.name }}</option>
          </select>
        </div>

        <div class="tg-control-group tg-search-group">
          <label class="tg-label">Пошук студента</label>
          <div class="tg-search-wrap">
            <svg class="tg-search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3C5.91 3 3 5.91 3 9.5C3 13.09 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5C5 7.01 7.01 5 9.5 5C11.99 5 14 7.01 14 9.5C14 11.99 11.99 14 9.5 14Z"/></svg>
            <input class="tg-search-input" type="text" v-model="searchQuery" placeholder="Прізвище або ім'я..." :disabled="!selectedGroupId" />
          </div>
        </div>
      </div>

      <div v-if="!selectedDisciplineId || !selectedGroupId" class="tg-hint">
        Оберіть дисципліну та групу щоб побачити студентів
      </div>

      <template v-else>
        <div v-if="loading" class="tg-state">Завантаження...</div>
        <div v-else-if="error" class="tg-state tg-error">{{ error }}</div>
        <div v-else-if="filteredStudents.length === 0" class="tg-state">Студентів не знайдено</div>
        <div v-else class="tg-table-card">
          <div class="tg-table-head">
            <div class="tg-col-student">Студент</div>
            <div class="tg-col-group">Група</div>
            <div class="tg-col-grade">Оцінка</div>
            <div class="tg-col-letter">Літера</div>
            <div class="tg-col-note">Примітка</div>
            <div class="tg-col-actions">Дії</div>
          </div>

          <div
            v-for="s in filteredStudents"
            :key="s.id"
            class="tg-table-row"
            :class="{ 'tg-row-editing': editingStudentId === s.id }"
          >
            <div class="tg-col-student">
              <span class="tg-student-name">{{ s.lastName }} {{ s.firstName }}</span>
            </div>
            <div class="tg-col-group">
              <span class="tg-group-badge">{{ s.groupName ?? '—' }}</span>
            </div>

            <template v-if="editingStudentId === s.id">
              <div class="tg-col-grade">
                <input
                  class="tg-edit-input tg-grade-input"
                  type="number"
                  v-model.number="editForm.value"
                  min="1"
                  max="100"
                  placeholder="1–100"
                />
              </div>
              <div class="tg-col-letter">
                <span class="tg-letter-preview" :class="letterClass(editForm.value)">
                  {{ letterOf(editForm.value) }}
                </span>
              </div>
              <div class="tg-col-note">
                <input
                  class="tg-edit-input tg-note-input"
                  type="text"
                  v-model="editForm.comment"
                  placeholder="Примітка (необов'язково)"
                />
              </div>
              <div class="tg-col-actions tg-edit-actions">
                <button class="btn btn-primary btn-sm" @click="saveGrade(s.id)" :disabled="saving">Зберегти</button>
                <button class="btn btn-secondary btn-sm" @click="cancelEdit">Скасувати</button>
              </div>
            </template>

            <template v-else>
              <div class="tg-col-grade">
                <span v-if="gradeOf(s.id)" class="tg-grade-badge" :class="gradeClass(gradeOf(s.id)!.value)">
                  {{ gradeOf(s.id)!.value }}
                </span>
                <span v-else class="tg-no-grade">—</span>
              </div>
              <div class="tg-col-letter">
                <span v-if="gradeOf(s.id)" class="tg-letter-badge" :class="letterClass(gradeOf(s.id)!.value)">
                  {{ letterLabel(gradeOf(s.id)!.value) }}
                </span>
                <span v-else class="tg-no-grade">—</span>
              </div>
              <div class="tg-col-note">
                <span class="tg-note-text">{{ gradeOf(s.id)?.comment || '—' }}</span>
              </div>
              <div class="tg-col-actions">
                <button
                  v-if="gradeOf(s.id)"
                  class="btn btn-secondary btn-sm"
                  @click="startEdit(s.id, gradeOf(s.id)!)"
                >Змінити</button>
                <button
                  v-else
                  class="btn btn-primary btn-sm"
                  @click="startAdd(s.id)"
                >Виставити</button>
              </div>
            </template>
          </div>
        </div>
      </template>
    </template>

    <div class="tg-error-toast" v-if="saveError">{{ saveError }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '../../stores/authStore'
import { teachersApi, type TeacherResponse } from '../../api/teachersApi'
import { disciplinesApi, type DisciplineResponse } from '../../api/disciplinesApi'
import { studentsApi, type StudentResponse } from '../../api/studentsApi'
import { groupsApi, type GroupResponse } from '../../api/groupsApi'
import { gradesApi, type GradeResponse } from '../../api/gradesApi'

const authStore = useAuthStore()

const teacher = ref<TeacherResponse | null>(null)
const loadingTeacher = ref(true)
const allDisciplines = ref<DisciplineResponse[]>([])
const allGroups = ref<GroupResponse[]>([])
const students = ref<StudentResponse[]>([])
const grades = ref<GradeResponse[]>([])

const selectedDisciplineId = ref<number | null>(null)
const selectedGroupId = ref<number | null>(null)
const searchQuery = ref('')
const loading = ref(false)
const error = ref('')
const saving = ref(false)
const saveError = ref('')

const editingStudentId = ref<number | null>(null)
const editingGradeId = ref<number | null>(null)
const editForm = ref({ value: 60, comment: '' })

const myDisciplines = computed(() =>
  allDisciplines.value.filter(d => teacher.value?.disciplineIds.includes(d.id))
)

const filteredStudents = computed(() => {
  const q = searchQuery.value.toLowerCase()
  if (!q) return students.value
  return students.value.filter(s =>
    s.lastName.toLowerCase().includes(q) || s.firstName.toLowerCase().includes(q)
  )
})

function gradeOf(studentId: number): GradeResponse | undefined {
  return grades.value.find(g => g.studentId === studentId && g.disciplineId === selectedDisciplineId.value)
}

function letterOf(v: number): string {
  if (!v || v < 1) return '—'
  if (v >= 90) return 'A'
  if (v >= 82) return 'B'
  if (v >= 75) return 'C'
  if (v >= 67) return 'D'
  if (v >= 60) return 'E'
  return 'F'
}

function letterLabel(v: number): string {
  if (v >= 90) return 'A (відмінно)'
  if (v >= 82) return 'B (добре)'
  if (v >= 75) return 'C (добре)'
  if (v >= 67) return 'D (задовільно)'
  if (v >= 60) return 'E (достатньо)'
  return 'F (незадовільно)'
}

function letterClass(v: number): string {
  if (!v || v < 1) return ''
  if (v >= 90) return 'letter-a'
  if (v >= 75) return 'letter-bc'
  if (v >= 60) return 'letter-de'
  return 'letter-f'
}

function gradeClass(v: number): string {
  if (v >= 90) return 'grade-excellent'
  if (v >= 75) return 'grade-good'
  if (v >= 60) return 'grade-satisf'
  return 'grade-fail'
}

async function onDisciplineChange() {
  selectedGroupId.value = null
  students.value = []
  grades.value = []
  editingStudentId.value = null
}

async function onGroupChange() {
  if (!selectedGroupId.value || !selectedDisciplineId.value) return
  loading.value = true
  error.value = ''
  editingStudentId.value = null
  try {
    const [studentData, gradeData] = await Promise.all([
      studentsApi.getByGroup(selectedGroupId.value),
      gradesApi.getByDiscipline(selectedDisciplineId.value)
    ])
    students.value = studentData
    grades.value = gradeData
  } catch {
    error.value = 'Не вдалося завантажити дані.'
  } finally {
    loading.value = false
  }
}

function startAdd(studentId: number) {
  editingStudentId.value = studentId
  editingGradeId.value = null
  editForm.value = { value: 60, comment: '' }
  saveError.value = ''
}

function startEdit(studentId: number, grade: GradeResponse) {
  editingStudentId.value = studentId
  editingGradeId.value = grade.id
  editForm.value = { value: grade.value, comment: grade.comment ?? '' }
  saveError.value = ''
}

function cancelEdit() {
  editingStudentId.value = null
  editingGradeId.value = null
  saveError.value = ''
}

async function saveGrade(studentId: number) {
  if (!editForm.value.value || editForm.value.value < 1 || editForm.value.value > 100) {
    saveError.value = 'Оцінка має бути від 1 до 100'
    return
  }
  saving.value = true
  saveError.value = ''
  try {
    if (editingGradeId.value !== null) {
      const updated = await gradesApi.update(editingGradeId.value, {
        value: editForm.value.value,
        comment: editForm.value.comment || undefined
      })
      const idx = grades.value.findIndex(g => g.id === editingGradeId.value)
      if (idx !== -1) grades.value[idx] = updated
    } else {
      const created = await gradesApi.create({
        studentId,
        disciplineId: selectedDisciplineId.value!,
        value: editForm.value.value,
        comment: editForm.value.comment || undefined
      })
      grades.value.push(created)
    }
    cancelEdit()
  } catch {
    saveError.value = 'Не вдалося зберегти оцінку.'
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  loadingTeacher.value = true
  try {
    const [teachers, disciplines, groups] = await Promise.all([
      teachersApi.getAll(),
      disciplinesApi.getAll(),
      groupsApi.getAll()
    ])
    allDisciplines.value = disciplines
    allGroups.value = groups
    teacher.value = teachers.find(t => t.userId === authStore.userId) ?? null
    if (teacher.value?.disciplineIds.length) {
      selectedDisciplineId.value = teacher.value.disciplineIds[0]
    }
  } finally {
    loadingTeacher.value = false
  }
})
</script>

<style src="@/css/views/teacher/TeacherGradesPage.css"></style>
