<template>
  <div class="modal-overlay" v-if="isOpen" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">{{ isEditing ? 'Редагувати студента' : 'Додати студента' }}</h2>
        <button class="close-btn" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <div class="form-group">
          <label>ПІБ студента</label>
          <input type="text" v-model="form.name" placeholder="Прізвище Ім'я По батькові" />
        </div>

        <div class="form-group">
          <label>Email</label>
          <input type="email" v-model="form.email" placeholder="email@student.ua" />
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Група</label>
            <select v-model="form.group">
              <option value="КН-21">КН-21</option>
              <option value="КН-31">КН-31</option>
              <option value="ПЗ-11">ПЗ-11</option>
              <option value="ПЗ-21">ПЗ-21</option>
            </select>
          </div>
          <div class="form-group">
            <label>Рік вступу</label>
            <input type="number" v-model.number="form.year" min="2000" max="2030" />
          </div>
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
import { ref, watch } from 'vue';

const props = defineProps({
  isOpen: Boolean,
  studentToEdit: {
    type: Object as () => any | null,
    default: null
  }
});

const emit = defineEmits(['close', 'save']);

const form = ref({
  id: null as number | null,
  name: '',
  email: '',
  group: 'КН-21',
  year: new Date().getFullYear()
});

const isEditing = ref(false);

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    if (props.studentToEdit) {
      isEditing.value = true;
      form.value = { ...props.studentToEdit };
    } else {
      isEditing.value = false;
      form.value = {
        id: null,
        name: '',
        email: '',
        group: 'КН-21',
        year: new Date().getFullYear()
      };
    }
  }
});

const closeModal = () => emit('close');

const saveStudent = () => {
  if (!form.value.name || !form.value.email) return alert('Заповніть обов\'язкові поля');
  emit('save', { ...form.value });
};
</script>

<style src="../css/components/StudentModal.css"></style>
