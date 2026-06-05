<template>
  <div class="modal-overlay" v-if="isOpen" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">{{ isEditing ? 'Редагувати групу' : 'Додати групу' }}</h2>
        <button class="close-btn" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <div class="form-row">
          <div class="form-group">
            <label>Назва групи</label>
            <input type="text" v-model="form.name" placeholder="КН-21" />
          </div>
          <div class="form-group">
            <label>Курс</label>
            <select v-model="form.course">
              <option :value="1">1 курс</option>
              <option :value="2">2 курс</option>
              <option :value="3">3 курс</option>
              <option :value="4">4 курс</option>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label>Спеціальність</label>
          <input type="text" v-model="form.specialty" placeholder="Комп'ютерні науки" />
        </div>

        <div class="form-group">
          <label>Куратор</label>
          <input type="text" v-model="form.curator" placeholder="Іванов Іван Іванович" />
        </div>

        <div class="form-group">
          <label>Кількість студентів</label>
          <input type="number" v-model.number="form.studentsCount" placeholder="25" min="0" />
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn btn-secondary" @click="closeModal">Скасувати</button>
        <button class="btn btn-primary" @click="saveGroup">Зберегти</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';

const props = defineProps({
  isOpen: Boolean,
  groupToEdit: {
    type: Object as () => any | null,
    default: null
  }
});

const emit = defineEmits(['close', 'save']);

const isEditing = ref(false);

const form = ref({
  id: null as number | null,
  name: '',
  specialty: '',
  course: 1,
  curator: '',
  studentsCount: 0
});

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    if (props.groupToEdit) {
      isEditing.value = true;
      form.value = { ...props.groupToEdit };
    } else {
      isEditing.value = false;
      form.value = { id: null, name: '', specialty: '', course: 1, curator: '', studentsCount: 0 };
    }
  }
});

const closeModal = () => {
  emit('close');
};

const saveGroup = () => {
  if (!form.value.name.trim()) return;
  emit('save', { ...form.value });
};
</script>

<style src="../css/components/TeacherModal.css"></style>
