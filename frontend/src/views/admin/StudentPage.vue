<template>
  <div class="students-page">
    <div class="toolbar">
      <div class="search-box">
        <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3C5.91 3 3 5.91 3 9.5C3 13.09 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5C5 7.01 7.01 5 9.5 5C11.99 5 14 7.01 14 9.5C14 11.99 11.99 14 9.5 14Z"/></svg>
        <input type="text" placeholder="Пошук студентів..." class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters">
        <select class="filter-select" v-model="selectedGroup">
          <option value="">Усі групи</option>
          <option value="КН-21">КН-21</option>
          <option value="КН-31">КН-31</option>
          <option value="ПЗ-11">ПЗ-11</option>
          <option value="ПЗ-21">ПЗ-21</option>
        </select>

        <select class="filter-select" v-model="sortBy">
          <option value="name">Прізвище</option>
          <option value="year">Рік вступу</option>
        </select>

        <button class="btn btn-primary add-btn" @click="handleAdd">+ Додати</button>
      </div>
    </div>

    <div class="results-count">Знайдено: {{ filteredStudents.length }}</div>

    <div class="students-table-card">
      <div class="table-header">
        <div class="col-name">Студент</div>
        <div class="col-group">Група</div>
        <div class="col-year">Рік вступу</div>
        <div class="col-actions"></div>
      </div>

      <div class="table-row" v-for="student in filteredStudents" :key="student.id">
        <div class="col-name">
          <div class="student-name">{{ student.name }}</div>
          <div class="student-email">{{ student.email }}</div>
        </div>

        <div class="col-group">
          <span class="group-badge">{{ student.group }}</span>
        </div>

        <div class="col-year">{{ student.year }}</div>

        <div class="col-actions">
          <button class="action-btn" @click="handleEdit(student)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z"/></svg>
          </button>
          <button class="action-btn delete-btn" @click="handleDelete(student.id)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z"/></svg>
          </button>
        </div>
      </div>

      <div v-if="filteredStudents.length === 0" class="empty-state">
        За вашим запитом нічого не знайдено
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

const searchQuery = ref('');
const selectedGroup = ref('');
const sortBy = ref('name');

const students = ref([
  { id: 1, name: 'Бондаренко Олена Сергіївна', email: 'bondarenko@student.ua', group: 'КН-21', year: 2023 },
  { id: 2, name: 'Гриценко Наталія Василівна', email: 'grytsenko@student.ua', group: 'КН-31', year: 2022 },
  { id: 3, name: 'Іваненко Марія Олегівна', email: 'ivanenko@student.ua', group: 'ПЗ-11', year: 2024 },
  { id: 4, name: 'Кравченко Дмитро Андрійович', email: 'kravchenko@student.ua', group: 'КН-21', year: 2023 },
  { id: 5, name: 'Лисенко Вікторія Ярославівна', email: 'lysenko@student.ua', group: 'ПЗ-21', year: 2023 },
  { id: 6, name: 'Мельник Тарас Олексійович', email: 'melnyk@student.ua', group: 'КН-21', year: 2023 },
  { id: 7, name: 'Рибаченко Артем Сергійович', email: 'rybachenko@student.ua', group: 'ПЗ-11', year: 2024 },
  { id: 8, name: 'Ткаченко Олексій Романович', email: 'tkachenko@student.ua', group: 'КН-31', year: 2022 },
]);

const filteredStudents = computed(() => {
  let result = students.value;

  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase();
    result = result.filter(s => s.name.toLowerCase().includes(q) || s.email.toLowerCase().includes(q));
  }

  if (selectedGroup.value) {
    result = result.filter(s => s.group === selectedGroup.value);
  }

  result = [...result].sort((a, b) => {
    if (sortBy.value === 'year') return b.year - a.year;
    return a.name.localeCompare(b.name);
  });

  return result;
});

const handleAdd = () => alert('Відкрити форму додавання студента');
const handleEdit = (student: any) => alert(`Редагувати: ${student.name}`);
const handleDelete = (id: number) => {
  if(confirm('Видалити студента?')) students.value = students.value.filter(s => s.id !== id);
};
</script>

<style src="../../css/views/admin/StudentsPage.css"></style>
