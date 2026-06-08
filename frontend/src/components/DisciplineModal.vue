<template>
  <div class="modal-overlay" v-if="isOpen" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">{{ isEditing ? 'Редагувати дисципліну' : 'Додати дисципліну' }}</h2>
        <button class="close-btn" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <div v-if="validationError" class="form-error">{{ validationError }}</div>

        <div class="form-group">
          <label>Назва дисципліни</label>
          <input type="text" v-model="form.name" placeholder="Введіть назву" />
        </div>

        <div class="form-group">
          <label>Опис</label>
          <input type="text" v-model="form.description" placeholder="Короткий опис дисципліни" />
        </div>

        <div class="form-group" v-if="teachers && teachers.length">
          <label>Викладачі</label>
          <div class="teachers-check-list">
            <label class="teacher-check-item" v-for="t in teachers" :key="t.id">
              <input
                type="checkbox"
                :checked="form.teacherIds.includes(t.id)"
                @change="toggleTeacher(t.id)"
              />
              <span>{{ t.lastName }} {{ t.firstName }}</span>
              <span class="teacher-check-pos" v-if="t.position">{{ t.position }}</span>
            </label>
          </div>
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn btn-secondary" @click="closeModal">Скасувати</button>
        <button class="btn btn-primary" @click="saveDiscipline">Зберегти</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import type { TeacherResponse } from '../api/teachersApi'

const props = defineProps<{
  isOpen: boolean
  disciplineToEdit?: any | null
  teachers?: TeacherResponse[]
}>()

const emit = defineEmits(['close', 'save'])

const form = reactive({
  id: null as number | null,
  name: '',
  description: '',
  teacherIds: [] as number[]
})

const validationError = ref('')
const isEditing = ref(false)

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    validationError.value = ''
    if (props.disciplineToEdit) {
      isEditing.value = true
      form.id = props.disciplineToEdit.id
      form.name = props.disciplineToEdit.name || ''
      form.description = props.disciplineToEdit.description || ''
      form.teacherIds = [...(props.disciplineToEdit.teacherIds ?? [])]
    } else {
      isEditing.value = false
      form.id = null
      form.name = ''
      form.description = ''
      form.teacherIds = []
    }
  }
})

function toggleTeacher(id: number) {
  const idx = form.teacherIds.indexOf(id)
  if (idx === -1) {
    form.teacherIds.push(id)
  } else {
    form.teacherIds.splice(idx, 1)
  }
}

const closeModal = () => emit('close')

const saveDiscipline = () => {
  if (!form.name.trim()) {
    validationError.value = 'Введіть назву дисципліни'
    return
  }
  validationError.value = ''
  emit('save', {
    id: form.id,
    name: form.name,
    description: form.description,
    teacherIds: [...form.teacherIds]
  })
}
</script>

<style src="../css/components/DisciplineModal.css"></style>
