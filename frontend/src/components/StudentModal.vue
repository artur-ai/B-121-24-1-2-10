<template>
  <div class="modal-overlay" v-if="isOpen" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">{{ isEditing ? 'Редагувати студента' : 'Додати студента' }}</h2>
        <button class="close-btn" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <div v-if="validationError" class="form-error">{{ validationError }}</div>

        <div class="form-group">
          <label>Прізвище</label>
          <input type="text" v-model="form.lastName" placeholder="Прізвище" />
        </div>

        <div class="form-group">
          <label>Ім'я</label>
          <input type="text" v-model="form.firstName" placeholder="Ім'я" />
        </div>

        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="form.email" placeholder="email@student.ua" />
        </div>

        <div class="form-group">
          <label>Група</label>
          <select v-model="form.groupId">
            <option :value="null" disabled>Оберіть групу</option>
            <option v-for="g in groups" :key="g.id" :value="g.id">{{ g.name }}</option>
          </select>
        </div>

        <div class="form-group" v-if="!isEditing">
          <label>ID користувача (необов'язково)</label>
          <input type="number" v-model.number="form.userId" placeholder="Вкажіть ID для входу в систему" />
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn btn-secondary" @click="closeModal">Скасувати</button>
        <button class="btn btn-primary" @click="saveStudent">Зберегти</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import type { GroupResponse } from '../api/groupsApi'

const props = defineProps({
  isOpen: Boolean,
  studentToEdit: {
    type: Object as () => any | null,
    default: null
  },
  groups: {
    type: Array as () => GroupResponse[],
    default: () => []
  }
})

const emit = defineEmits(['close', 'save'])

const form = reactive({
  id: null as number | null,
  firstName: '',
  lastName: '',
  email: '',
  groupId: null as number | null,
  userId: null as number | null
})

const validationError = ref('')
const isEditing = ref(false)

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    validationError.value = ''
    if (props.studentToEdit) {
      isEditing.value = true
      form.id = props.studentToEdit.id
      form.firstName = props.studentToEdit.firstName || ''
      form.lastName = props.studentToEdit.lastName || ''
      form.email = props.studentToEdit.email || ''
      form.groupId = props.studentToEdit.groupId ?? null
      form.userId = null
    } else {
      isEditing.value = false
      form.id = null
      form.firstName = ''
      form.lastName = ''
      form.email = ''
      form.groupId = null
      form.userId = null
    }
  }
})

const closeModal = () => emit('close')

const saveStudent = () => {
  if (!form.lastName.trim()) {
    validationError.value = 'Введіть прізвище студента'
    return
  }
  if (!form.firstName.trim()) {
    validationError.value = "Введіть ім'я студента"
    return
  }
  if (!form.email.trim()) {
    validationError.value = 'Введіть email'
    return
  }
  validationError.value = ''
  emit('save', {
    id: form.id,
    firstName: form.firstName,
    lastName: form.lastName,
    email: form.email,
    groupId: form.groupId,
    userId: form.userId
  })
}
</script>

<style src="../css/components/StudentModal.css"></style>
