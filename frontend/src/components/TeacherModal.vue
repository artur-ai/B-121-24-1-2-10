<template>
  <div class="modal-overlay" v-if="isOpen" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">{{ isEditing ? 'Редагувати викладача' : 'Додати викладача' }}</h2>
        <button class="close-btn" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <div v-if="validationError" class="form-error">{{ validationError }}</div>

        <div class="form-row">
          <div class="form-group">
            <label>Прізвище</label>
            <input type="text" v-model="form.lastName" placeholder="Іванов" />
          </div>
          <div class="form-group">
            <label>Ім'я</label>
            <input type="text" v-model="form.firstName" placeholder="Іван" />
          </div>
        </div>

        <div class="form-group">
          <label>Посада</label>
          <select v-model="form.position">
            <option value="Професор">Професор</option>
            <option value="Доцент">Доцент</option>
            <option value="Старший викладач">Старший викладач</option>
            <option value="Асистент">Асистент</option>
          </select>
        </div>

        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="form.email" placeholder="email@univ.ua" />
        </div>

        <div class="form-group">
          <label>ID облікового запису (userId)</label>
          <input type="number" v-model.number="form.userId" placeholder="Необов'язково — для входу як викладач" min="1" />
          <span class="form-hint">Вкажіть ID користувача, якому дозволено входити як цей викладач</span>
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn btn-secondary" @click="closeModal">Скасувати</button>
        <button class="btn btn-primary" @click="saveTeacher">Зберегти</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps({
  isOpen: Boolean,
  teacherToEdit: {
    type: Object as () => any | null,
    default: null
  }
})

const emit = defineEmits(['close', 'save'])

const form = ref({
  id: null as number | null,
  lastName: '',
  firstName: '',
  position: 'Доцент',
  email: '',
  userId: null as number | null
})

const validationError = ref('')
const isEditing = ref(false)

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    validationError.value = ''
    if (props.teacherToEdit) {
      isEditing.value = true
      form.value = {
        id: props.teacherToEdit.id,
        lastName: props.teacherToEdit.lastName || '',
        firstName: props.teacherToEdit.firstName || '',
        position: props.teacherToEdit.position || 'Доцент',
        email: props.teacherToEdit.email || '',
        userId: props.teacherToEdit.userId ?? null
      }
    } else {
      isEditing.value = false
      form.value = { id: null, lastName: '', firstName: '', position: 'Доцент', email: '', userId: null }
    }
  }
})

const closeModal = () => {
  emit('close')
}

const saveTeacher = () => {
  if (!form.value.lastName.trim()) {
    validationError.value = 'Введіть прізвище'
    return
  }
  if (!form.value.firstName.trim()) {
    validationError.value = "Введіть ім'я"
    return
  }
  if (!form.value.email.trim()) {
    validationError.value = 'Введіть email'
    return
  }
  validationError.value = ''
  emit('save', { ...form.value })
}
</script>

<style src="../css/components/TeacherModal.css"></style>
