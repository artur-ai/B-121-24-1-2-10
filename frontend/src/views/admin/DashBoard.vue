<template>
  <div class="dashboard-container">

    <section class="hero-card">
      <div class="hero-header">
        <div class="hero-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M12 3L1 9L4 10.63V17.5C4 18.88 7.58 20 12 20C16.42 20 20 18.88 20 17.5V10.63L23 9L12 3ZM12 18.25C8.6 18.25 5.5 17.38 5.5 16V11.45L12 15L18.5 11.45V16C18.5 17.38 15.4 18.25 12 18.25ZM12 13.1L3.25 8.32L12 3.52L20.75 8.32L12 13.1Z"/></svg>
        </div>
        <div class="hero-titles">
          <h2 class="hero-title">Кафедра інформатики</h2>
          <p class="hero-subtitle">Національний університет · Факультет комп'ютерних наук</p>
        </div>
      </div>

      <div class="hero-stats">
        <div class="stat-box">
          <div class="stat-number">4</div>
          <div class="stat-label">Викладачів</div>
        </div>
        <div class="stat-box">
          <div class="stat-number">4</div>
          <div class="stat-label">Дисциплін</div>
        </div>
        <div class="stat-box">
          <div class="stat-number">2</div>
          <div class="stat-label">Спеціальностей</div>
        </div>
      </div>
    </section>

    <div class="tabs-container">
      <button
        class="tab-btn"
        :class="{ active: activeTab === 'about' }"
        @click="activeTab = 'about'"
      >
        Про кафедру
      </button>
      <button
        class="tab-btn"
        :class="{ active: activeTab === 'teachers' }"
        @click="activeTab = 'teachers'"
      >
        Викладачі
      </button>
      <button
        class="tab-btn"
        :class="{ active: activeTab === 'disciplines' }"
        @click="activeTab = 'disciplines'"
      >
        Дисципліни
      </button>
    </div>

    <div class="info-section" v-if="activeTab === 'about'">

      <div class="content-card full-width">
        <h3 class="card-heading">Про кафедру</h3>
        <p class="card-text">
          Кафедра інформатики є одним із провідних підрозділів факультету комп'ютерних наук. Кафедра готує фахівців за напрямами «Комп'ютерні науки» та «Програмна інженерія». На кафедрі активно провадиться наукова діяльність, реалізуються дослідницькі проєкти у галузі штучного інтелекту, розподілених систем та кібербезпеки.
        </p>
      </div>

      <div class="cards-grid">
        <div class="content-card">
          <div class="card-label">ЗАВІДУВАЧ КАФЕДРИ</div>
          <h4 class="person-name">Шевченко Микола Петрович</h4>
          <p class="person-desc">Доктор технічних наук, Професор</p>
        </div>

        <div class="content-card">
          <div class="card-label">КОНТАКТИ</div>
          <ul class="contact-list">
            <li><span class="contact-icon">📧</span> kafedra@univ.ua</li>
            <li><span class="contact-icon">📞</span> +380 44 123-45-67</li>
            <li class="contact-muted">корп. 3, ауд. 301</li>
          </ul>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const activeTab = ref('about');
</script>

<style src="@/css/views/Dashboard.css"></style>
