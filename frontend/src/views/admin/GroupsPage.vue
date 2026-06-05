<template>
  <div class="groups-page">
    <div class="toolbar">
      <div class="search-box">
        <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3C5.91 3 3 5.91 3 9.5C3 13.09 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5C5 7.01 7.01 5 9.5 5C11.99 5 14 7.01 14 9.5C14 11.99 11.99 14 9.5 14Z"/></svg>
        <input type="text" placeholder="Пошук груп..." class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters">
        <select class="filter-select" v-model="selectedCourse">
          <option value="">Усі курси</option>
          <option value="1">1 курс</option>
          <option value="2">2 курс</option>
          <option value="3">3 курс</option>
          <option value="4">4 курс</option>
        </select>

        <select class="filter-select" v-model="sortBy">
          <option value="name">Сортувати: назва</option>
          <option value="course">Сортувати: курс</option>
          <option value="students">Сортувати: студенти</option>
        </select>

        <button class="btn btn-primary add-btn" @click="handleAdd">+ Додати</button>
      </div>
    </div>

    <div class="results-count">Знайдено: {{ filteredGroups.length }}</div>

    <div class="groups-list">
      <div class="group-card" v-for="group in filteredGroups" :key="group.id">
        <div class="group-badge">{{ group.name }}</div>

        <div class="group-details">
          <div class="group-header">
            <span class="group-name">{{ group.name }}</span>
            <span class="group-course-tag">{{ group.course }} курс</span>
          </div>

          <div class="group-specialty">{{ group.specialty }}</div>

          <div class="group-meta">
            <span class="meta-item">
              <svg class="meta-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 12C14.21 12 16 10.21 16 8C16 5.79 14.21 4 12 4C9.79 4 8 5.79 8 8C8 10.21 9.79 12 12 12ZM12 14C9.33 14 4 15.34 4 18V20H20V18C20 15.34 14.67 14 12 14Z"/></svg>
              {{ group.curator }}
            </span>
            <span class="meta-item">
              <svg class="meta-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M16 11C17.66 11 18.99 9.66 18.99 8C18.99 6.34 17.66 5 16 5C14.34 5 13 6.34 13 8C13 9.66 14.34 11 16 11ZM8 11C9.66 11 10.99 9.66 10.99 8C10.99 6.34 9.66 5 8 5C6.34 5 5 6.34 5 8C5 9.66 6.34 11 8 11ZM8 13C5.67 13 1 14.17 1 16.5V18H15V16.5C15 14.17 10.33 13 8 13ZM16 13C15.71 13 15.38 13.02 15.03 13.05C16.19 13.89 17 15.02 17 16.5V18H23V16.5C23 14.17 18.33 13 16 13Z"/></svg>
              {{ group.studentsCount }} студентів
            </span>
          </div>
        </div>

        <div class="group-actions">
          <button class="action-btn" @click="handleEdit(group)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z"/></svg>
          </button>
          <button class="action-btn delete-btn" @click="handleDelete(group.id)">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z"/></svg>
          </button>
        </div>
      </div>

      <div v-if="filteredGroups.length === 0" class="empty-state">
        За вашим запитом нічого не знайдено
      </div>
    </div>

    <GroupModal
      :is-open="isModalOpen"
      :group-to-edit="editingGroup"
      @close="isModalOpen = false"
      @save="saveGroup"
    />

    <div class="delete-confirm-overlay" v-if="deletingId !== null" @click.self="deletingId = null">
      <div class="delete-confirm">
        <p>Ви впевнені, що хочете видалити цю групу?</p>
        <div class="delete-confirm-actions">
          <button class="btn btn-secondary" @click="deletingId = null">Скасувати</button>
          <button class="btn btn-danger" @click="confirmDelete">Видалити</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import GroupModal from '../../components/GroupModal.vue';

const searchQuery = ref('');
const selectedCourse = ref('');
const sortBy = ref('name');

const isModalOpen = ref(false);
const editingGroup = ref<any>(null);
const deletingId = ref<number | null>(null);

interface Group {
  id: number;
  name: string;
  specialty: string;
  course: number;
  curator: string;
  studentsCount: number;
}

const groups = ref<Group[]>([
  { id: 1, name: 'КН-21', specialty: "Комп'ютерні науки", course: 2, curator: 'Коваленко Андрій Іванович', studentsCount: 28 },
  { id: 2, name: 'КН-31', specialty: "Комп'ютерні науки", course: 3, curator: 'Петренко Людмила Василівна', studentsCount: 25 },
  { id: 3, name: 'ПЗ-11', specialty: 'Програмна інженерія', course: 1, curator: 'Савченко Ігор Олегович', studentsCount: 30 },
  { id: 4, name: 'ПЗ-21', specialty: 'Програмна інженерія', course: 2, curator: 'Шевченко Микола Петрович', studentsCount: 27 },
]);

const filteredGroups = computed(() => {
  let result = groups.value;

  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase();
    result = result.filter(g =>
      g.name.toLowerCase().includes(q) ||
      g.specialty.toLowerCase().includes(q) ||
      g.curator.toLowerCase().includes(q)
    );
  }

  if (selectedCourse.value) {
    result = result.filter(g => g.course === Number(selectedCourse.value));
  }

  result = [...result].sort((a, b) => {
    if (sortBy.value === 'course') return a.course - b.course;
    if (sortBy.value === 'students') return b.studentsCount - a.studentsCount;
    return a.name.localeCompare(b.name);
  });

  return result;
});

const handleAdd = () => {
  editingGroup.value = null;
  isModalOpen.value = true;
};

const handleEdit = (group: Group) => {
  editingGroup.value = group;
  isModalOpen.value = true;
};

const handleDelete = (id: number) => {
  deletingId.value = id;
};

const confirmDelete = () => {
  groups.value = groups.value.filter(g => g.id !== deletingId.value);
  deletingId.value = null;
};

const saveGroup = (data: any) => {
  if (data.id) {
    const index = groups.value.findIndex(g => g.id === data.id);
    if (index !== -1) groups.value[index] = data;
  } else {
    const newId = groups.value.length > 0 ? Math.max(...groups.value.map(g => g.id)) + 1 : 1;
    groups.value.push({ ...data, id: newId });
  }
  isModalOpen.value = false;
};
</script>

<style src="../../css/views/admin/GroupsPage.css"></style>
