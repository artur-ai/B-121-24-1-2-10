<template>
  <div class="grades-page">
    <div v-if="!studentId" class="link-card">
      <h3>Введіть ваш ID студента</h3>
      <p>ID надається адміністратором при створенні профілю студента.</p>
      <div class="link-form">
        <input type="number" v-model.number="inputId" placeholder="Наприклад: 3" min="1" />
        <button class="btn btn-primary" @click="saveStudentId" :disabled="!inputId">Підтвердити</button>
      </div>
    </div>

    <template v-else>
      <div class="grades-header">
        <div class="grades-title">Мої оцінки</div>
        <button class="btn-reset" @click="resetStudentId">Змінити ID</button>
      </div>

      <div v-if="loading" class="state-message">Завантаження...</div>
      <div v-else-if="error" class="state-message error">{{ error }}</div>
      <div v-else-if="grades.length === 0" class="empty-state">Оцінок ще немає</div>
      <div v-else class="grades-table-card">
        <div class="table-header">
          <div class="col-disc">Дисципліна</div>
          <div class="col-val">Оцінка</div>
          <div class="col-letter">Літера</div>
          <div class="col-date">Дата</div>
          <div class="col-comment">Коментар</div>
        </div>
        <div class="table-row" v-for="g in grades" :key="g.id">
          <div class="col-disc">{{ g.disciplineName }}</div>
          <div class="col-val">
            <span class="grade-badge" :class="gradeClass(g.value)">{{ g.value }}</span>
          </div>
          <div class="col-letter">
            <span class="letter-badge" :class="letterClass(g.value)">{{ letterLabel(g.value) }}</span>
          </div>
          <div class="col-date">{{ formatDate(g.gradedAt) }}</div>
          <div class="col-comment">{{ g.comment || '—' }}</div>
        </div>
      </div>

      <div class="grades-summary" v-if="grades.length > 0">
        <div class="summary-item">
          <div class="summary-num">{{ grades.length }}</div>
          <div class="summary-label">Оцінок</div>
        </div>
        <div class="summary-item">
          <div class="summary-num">{{ avgGrade }}</div>
          <div class="summary-label">Середній бал</div>
        </div>
        <div class="summary-item">
          <div class="summary-num">{{ maxGrade }}</div>
          <div class="summary-label">Найвища оцінка</div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { gradesApi, type GradeResponse } from '../../api/gradesApi'

const STORAGE_KEY = 'student_id'

const studentId = ref<number | null>(Number(localStorage.getItem(STORAGE_KEY)) || null)
const inputId = ref<number | null>(null)
const grades = ref<GradeResponse[]>([])
const loading = ref(false)
const error = ref('')

const avgGrade = computed(() => {
  if (!grades.value.length) return 0
  return (grades.value.reduce((s, g) => s + g.value, 0) / grades.value.length).toFixed(1)
})
const maxGrade = computed(() => grades.value.length ? Math.max(...grades.value.map(g => g.value)) : 0)

function gradeClass(v: number) {
  if (v >= 90) return 'grade-excellent'
  if (v >= 75) return 'grade-good'
  if (v >= 60) return 'grade-satisf'
  return 'grade-fail'
}

function letterOf(v: number): string {
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
  if (v >= 90) return 'letter-a'
  if (v >= 75) return 'letter-bc'
  if (v >= 60) return 'letter-de'
  return 'letter-f'
}

function formatDate(dt: string) {
  return new Date(dt).toLocaleDateString('uk-UA', { day: '2-digit', month: '2-digit', year: 'numeric' })
}

function saveStudentId() {
  if (!inputId.value) return
  localStorage.setItem(STORAGE_KEY, String(inputId.value))
  studentId.value = inputId.value
  loadGrades()
}

function resetStudentId() {
  localStorage.removeItem(STORAGE_KEY)
  studentId.value = null
  grades.value = []
}

async function loadGrades() {
  if (!studentId.value) return
  loading.value = true
  error.value = ''
  try {
    grades.value = await gradesApi.getByStudent(studentId.value)
  } catch {
    error.value = 'Не вдалося завантажити оцінки.'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  if (studentId.value) loadGrades()
})
</script>

<style src="@/css/views/student/GradesPage.css"></style>
