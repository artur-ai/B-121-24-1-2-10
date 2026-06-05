<template>
  <div class="teachers-page">
    <div class="toolbar">
      <div class="search-box">
        <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3C5.91 3 3 5.91 3 9.5C3 13.09 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5C5 7.01 7.01 5 9.5 5C11.99 5 14 7.01 14 9.5C14 11.99 11.99 14 9.5 14Z"/></svg>
        <input type="text" placeholder="Пошук викладачів..." class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters">
        <select class="filter-select" v-model="selectedPosition">
          <option value="Усі посади">Усі посади</option>
          <option value="Професор">Професор</option>
          <option value="Доцент">Доцент</option>
          <option value="Старший викладач">Старший викладач</option>
          <option value="Асистент">Асистент</option>
        </select>

        <select class="filter-select" v-model="sortBy">
          <option value="name_asc">Сортувати: А-Я</option>
          <option value="name_desc">Сортувати: Я-А</option>
        </select>

        <button class="btn btn-primary add-btn" @click="handleAdd">+ Додати</button>
      </div>
    </div>

    <div class="results-count">Знайдено: {{ filteredTeachers.length }}</div>

    <div class="teachers-list">
      <div class="teacher-card" v-for="teacher in filteredTeachers" :key="teacher.id">
        <div class="teacher-avatar">{{ teacher.initials }}</div>

        <div class="teacher-details">
          <div class="teacher-name">{{ teacher.name }}</div>
          <div class="teacher-subtitle">{{ teacher.position }} · {{ teacher.degree }}</div>

          <div class="teacher-contacts">
            <span class="contact-item">
              <svg class="contact-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 18H4V8L12 13L20 8V18ZM12 11L4 6H20L12 11Z"/></svg>
              {{ teacher.email }}
            </span>
            <span class="contact-item">
              <svg class="contact-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M20.01 15.38C18.78 15.38 17.59 15.18 16.5 14.82C16.15 14.7 15.77 14.79 15.5 15.06L13.27 17.29C10.38 15.82 8.18 13.62 6.71 10.73L8.94 8.5C9.21 8.23 9.3 7.85 9.18 7.5C8.82 6.41 8.62 5.22 8.62 3.99C8.62 3.45 8.18 3 7.63 3H4.06C3.51 3 3 3.48 3 4.03C3 13.43 10.57 21 19.97 21C20.52 21 20.49 21 19.94V16.37C21 15.82 20.56 15.38 20.01 15.38Z"/></svg>
              {{ teacher.phone }}
            </span>
          </div>

          <div class="teacher-subjects" v-if="teacher.subjects.length > 0">
            <span class="subject-tag" v-for="subject in teacher.subjects" :key="subject">
              {{ subject }}
            </span>
          </div>
        </div>

        <div class="teacher-actions">
          <button class="action-btn" @click="handleEdit(teacher)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z"/></svg>
          </button>
          <button class="action-btn" @click="handleDelete(teacher.id)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z"/></svg>
          </button>
        </div>
      </div>

      <div v-if="filteredTeachers.length === 0" style="text-align: center; color: var(--text-muted); padding: 40px;">
        За вашим запитом нікого не знайдено
      </div>
    </div>

    <TeacherModal
      :is-open="isModalOpen"
      :teacher-to-edit="editingTeacher"
      @close="isModalOpen = false"
      @save="saveTeacher"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import TeacherModal from '../../components/TeacherModal.vue';

const searchQuery = ref('');
const selectedPosition = ref('Усі посади');
const sortBy = ref('name_asc');

const isModalOpen = ref(false);
const editingTeacher = ref(null);

const teachers = ref([
  { id: 1, initials: 'АК', name: 'Коваленко Андрій Іванович', position: 'Доцент', degree: 'Кандидат фізико-математичних наук', email: 'kovalenko@univ.ua', phone: '+380679876543', subjects: ['Вища математика'] },
  { id: 2, initials: 'ЛП', name: 'Петренко Людмила Василівна', position: 'Старший викладач', degree: 'Кандидат технічних наук', email: 'petrenko@univ.ua', phone: '+380501112233', subjects: ['Об\'єктно-орієнтоване програмування'] },
  { id: 3, initials: 'ІС', name: 'Савченко Ігор Олегович', position: 'Асистент', degree: 'Асистент', email: 'savchenko@univ.ua', phone: '+380634445566', subjects: [] },
  { id: 4, initials: 'МШ', name: 'Шевченко Микола Петрович', position: 'Професор', degree: 'Доктор технічних наук', email: 'shevchenko@univ.ua', phone: '+380671234567', subjects: ['Алгоритми та структури даних', 'Системне програмування'] }
]);

const filteredTeachers = computed(() => {
  let result = teachers.value;
  if (searchQuery.value) result = result.filter(t => t.name.toLowerCase().includes(searchQuery.value.toLowerCase()));
  if (selectedPosition.value !== 'Усі посади') result = result.filter(t => t.position === selectedPosition.value);
  result = result.sort((a, b) => sortBy.value === 'name_asc' ? a.name.localeCompare(b.name) : b.name.localeCompare(a.name));
  return result;
});

const handleAdd = () => {
  editingTeacher.value = null;
  isModalOpen.value = true;
};

const handleEdit = (teacher: any) => {
  editingTeacher.value = teacher;
  isModalOpen.value = true;
};

const handleDelete = (id: number) => {
  if (confirm('Ви впевнені, що хочете видалити цього викладача?')) {
    teachers.value = teachers.value.filter(t => t.id !== id);
  }
};

const saveTeacher = (teacherData: any) => {
  if (teacherData.id) {
    const index = teachers.value.findIndex(t => t.id === teacherData.id);
    if (index !== -1) teachers.value[index] = teacherData;
  } else {
    const newId = teachers.value.length > 0 ? Math.max(...teachers.value.map(t => t.id)) + 1 : 1;
    teachers.value.push({ ...teacherData, id: newId });
  }
  isModalOpen.value = false;
};
</script>

<style src="../../css/views/admin/TeacherPage.css"></style>
