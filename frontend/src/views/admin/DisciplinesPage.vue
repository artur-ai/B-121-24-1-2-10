<template>
  <div class="disciplines-page">
    <div class="toolbar">
      <div class="search-box">
        <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M15.5 14H14.71L14.43 13.73C15.41 12.59 16 11.11 16 9.5C16 5.91 13.09 3 9.5 3C5.91 3 3 5.91 3 9.5C3 13.09 5.91 16 9.5 16C11.11 16 12.59 15.41 13.73 14.43L14 14.71V15.5L19 20.49L20.49 19L15.5 14ZM9.5 14C7.01 14 5 11.99 5 9.5C5 7.01 7.01 5 9.5 5C11.99 5 14 7.01 14 9.5C14 11.99 11.99 14 9.5 14Z"/></svg>
        <input type="text" placeholder="Пошук дисциплін..." class="search-input" v-model="searchQuery" />
      </div>

      <div class="filters">
        <select class="filter-select" v-model="selectedTeacher">
          <option value="">Усі викладачі</option>
          <option value="Шевченко М.П.">Шевченко М.П.</option>
          <option value="Коваленко А.І.">Коваленко А.І.</option>
          <option value="Петренко Л.В.">Петренко Л.В.</option>
        </select>

        <select class="filter-select" v-model="sortBy">
          <option value="name">Назва</option>
          <option value="credits">Кредити</option>
        </select>

        <button class="btn btn-primary add-btn" @click="handleAdd">+ Додати</button>
      </div>
    </div>

    <div class="results-count">Знайдено: {{ filteredDisciplines.length }}</div>

    <div class="disciplines-list">
      <div class="discipline-card" v-for="disc in filteredDisciplines" :key="disc.id">

        <div class="discipline-content">
          <div class="discipline-name">{{ disc.name }}</div>
          <div class="discipline-meta">
            Викладач: {{ disc.teacher }} · {{ disc.credits }} кредити · {{ disc.semester }} семестр
          </div>

          <div class="discipline-groups">
            <span class="group-tag" v-for="group in disc.groups" :key="group">{{ group }}</span>
          </div>
        </div>

        <div class="discipline-right">
          <div class="credit-badge">{{ disc.credits }} кр.</div>
          <div class="discipline-actions">
            <button class="action-btn" @click="handleEdit(disc)">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21H6.75L17.81 9.94L14.06 6.19L3 17.25ZM20.71 7.04C21.1 6.65 21.1 6.02 20.71 5.63L18.37 3.29C17.98 2.9 17.35 2.9 16.96 3.29L15.13 5.12L18.88 8.87L20.71 7.04Z"/></svg>
            </button>
            <button class="action-btn delete-btn" @click="handleDelete(disc.id)">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z"/></svg>
            </button>
          </div>
        </div>
      </div>

      <div v-if="filteredDisciplines.length === 0" class="empty-state">
        За вашим запитом нічого не знайдено
      </div>
    </div>

    <DisciplineModal
      :is-open="isModalOpen"
      :discipline-to-edit="editingDiscipline"
      @close="isModalOpen = false"
      @save="saveDiscipline"
    />

    <div class="delete-confirm-overlay" v-if="deletingId !== null" @click.self="deletingId = null">
      <div class="delete-confirm">
        <p>Ви впевнені, що хочете видалити цю дисципліну?</p>
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
import DisciplineModal from '../../components/DisciplineModal.vue';

const searchQuery = ref('');
const selectedTeacher = ref('');
const sortBy = ref('name');

const isModalOpen = ref(false);
const editingDiscipline = ref<any>(null);
const deletingId = ref<number | null>(null);

const disciplines = ref([
  { id: 1, name: 'Алгоритми та структури даних', teacher: 'Шевченко М.П.', credits: 5, semester: 3, groups: ['КН-21', 'КН-31'] },
  { id: 2, name: 'Вища математика', teacher: 'Коваленко А.І.', credits: 6, semester: 1, groups: ['КН-21', 'ПЗ-11', 'ПЗ-21'] },
  { id: 3, name: 'Об\'єктно-орієнтоване програмування', teacher: 'Петренко Л.В.', credits: 5, semester: 2, groups: ['КН-21', 'ПЗ-11'] },
  { id: 4, name: 'Системне програмування', teacher: 'Шевченко М.П.', credits: 4, semester: 4, groups: ['КН-31'] },
]);

const filteredDisciplines = computed(() => {
  let result = disciplines.value;
  if (searchQuery.value) {
    const q = searchQuery.value.toLowerCase();
    result = result.filter(d => d.name.toLowerCase().includes(q));
  }
  if (selectedTeacher.value) {
    result = result.filter(d => d.teacher === selectedTeacher.value);
  }
  result = [...result].sort((a, b) => {
    if (sortBy.value === 'credits') return b.credits - a.credits;
    return a.name.localeCompare(b.name);
  });
  return result;
});

const handleAdd = () => {
  editingDiscipline.value = null;
  isModalOpen.value = true;
};

const handleEdit = (disc: any) => {
  editingDiscipline.value = disc;
  isModalOpen.value = true;
};

const handleDelete = (id: number) => {
  deletingId.value = id;
};

const confirmDelete = () => {
  disciplines.value = disciplines.value.filter(d => d.id !== deletingId.value);
  deletingId.value = null;
};

const saveDiscipline = (data: any) => {
  if (data.id) {
    const index = disciplines.value.findIndex(d => d.id === data.id);
    if (index !== -1) disciplines.value[index] = data;
  } else {
    const newId = disciplines.value.length > 0 ? Math.max(...disciplines.value.map(d => d.id)) + 1 : 1;
    disciplines.value.push({ ...data, id: newId });
  }
  isModalOpen.value = false;
};
</script>

<style src="../../css/views/admin/DisciplinesPage.css"></style>
