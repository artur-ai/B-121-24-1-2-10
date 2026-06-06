<template>
  <div class="modal-overlay" v-if="isOpen" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">{{ isEditing ? 'Редагувати дисципліну' : 'Додати дисципліну' }}</h2>
        <button class="close-btn" @click="closeModal">&times;</button>
      </div>

      <div class="modal-body">
        <div class="form-group">
          <label>Назва дисципліни</label>
          <input type="text" v-model="form.name" placeholder="Введіть назву" />
        </div>

        <div class="form-group">
          <label>Викладач</label>
          <select v-model="form.teacher">
            <option value="Шевченко М.П.">Шевченко М.П.</option>
            <option value="Коваленко А.І.">Коваленко А.І.</option>
            <option value="Петренко Л.В.">Петренко Л.В.</option>
          </select>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Кредити</label>
            <input type="number" v-model.number="form.credits" min="1" max="10" />
          </div>
          <div class="form-group">
            <label>Семестр</label>
            <input type="number" v-model.number="form.semester" min="1" max="8" />
          </div>
        </div>

        <div class="form-group">
          <label>Групи (через кому)</label>
          <input type="text" v-model="groupsInput" placeholder="КН-21, ПЗ-11" />
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
import { ref, watch } from 'vue';

const props = defineProps({
  isOpen: Boolean,
  disciplineToEdit: {
    type: Object as () => any | null,
    default: null
  }
});

const emit = defineEmits(['close', 'save']);

const form = ref({
  id: null as number | null,
  name: '',
  teacher: 'Шевченко М.П.',
  credits: 4,
  semester: 1
});

const groupsInput = ref('');
const isEditing = ref(false);

watch(() => props.isOpen, (newVal) => {
  if (newVal) {
    if (props.disciplineToEdit) {
      isEditing.value = true;
      form.value = { ...props.disciplineToEdit };
      groupsInput.value = props.disciplineToEdit.groups.join(', ');
    } else {
      isEditing.value = false;
      form.value = { id: null, name: '', teacher: 'Шевченко М.П.', credits: 4, semester: 1 };
      groupsInput.value = '';
    }
  }
});

const closeModal = () => emit('close');

const saveDiscipline = () => {
  if (!form.value.name) return alert('Введіть назву');
  const groups = groupsInput.value.split(',').map(g => g.trim()).filter(g => g);
  emit('save', { ...form.value, groups });
};
</script>

<style src="../css/components/DisciplineModal.css"></style>
