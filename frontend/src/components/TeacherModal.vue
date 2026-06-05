<template>
  <div class="modal-overlay" v-if="isOpen" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">{{ isEditing ? 'Редагувати викладача' : 'Додати викладача' }}</h2>
        <button class="close-btn" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <div class="form-group">
          <label>ПІБ викладача</label>
          <input type="text" v-model="form.name" placeholder="Іванов Іван Іванович" />
        </div>

        <div class="form-row">
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
            <label>Науковий ступінь</label>
            <input type="text" v-model="form.degree" placeholder="Кандидат наук..." />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Email</label>
            <input type="email" v-model="form.email" placeholder="email@univ.ua" />
          </div>
          <div class="form-group">
            <label>Телефон</label>
            <input type="text" v-model="form.phone" placeholder="+380..." />
          </div>
        </div>

        <div class="form-group">
          <label>Дисципліни (через кому)</label>
          <input type="text" v-model="subjectsInput" placeholder="Вища математика, Фізика" />
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
import { ref, watch } from 'vue';

const props = defineProps({
  isOpen: Boolean,
  teacherToEdit: {
    type: Object as () => any | null,
    default: null
  }
});

const emit = defineEmits(['close', 'save']);

const form = ref({
  id: null as number | null,
  name: '',
  position: 'Доцент',
  degree: '',
  email: '',
  phone: ''
});

const subjectsInput = ref('');
const isEditing = ref(false);

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    if (props.teacherToEdit) {
      isEditing.value = true;
      form.value = { ...props.teacherToEdit } as any;
      subjectsInput.value = props.teacherToEdit.subjects.join(', ');
    } else {
      isEditing.value = false;
      form.value = { id: null, name: '', position: 'Доцент', degree: '', email: '', phone: '' };
      subjectsInput.value = '';
    }
  }
});

const closeModal = () => {
  emit('close');
};

const saveTeacher = () => {
  if (!form.value.name) return alert('Введіть ПІБ');

  const initials = form.value.name.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase();
  const subjects = subjectsInput.value.split(',').map(s => s.trim()).filter(s => s);

  emit('save', {
    ...form.value,
    initials,
    subjects
  });
};
</script>

<style src="../css/components/TeacherModal.css"></style>
