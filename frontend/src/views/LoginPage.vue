<template>
  <div class="login-page">
    <div class="login-card">
      <div class="card-header">
        <div class="header-icon">
        </div>
        <h1 class="header-title">Кафедра інформатики</h1>
        <p class="header-subtitle">Система управління навчальним процесом</p>
      </div>

      <div class="card-body">
        <div class="body-title">Оберіть роль для демонстрації:</div>

        <div class="role-list">
          <div class="role-item role-admin">
            <div class="role-avatar">А</div>
            <div class="role-info">
              <span class="role-name">Адміністратор Максим</span>
              <span class="role-desc">Адміністратор • admin@univ.ua</span>
            </div>
          </div>

          <div class="role-item role-student">
            <div class="role-avatar">Б</div>
            <div class="role-info">
              <span class="role-name">Бондаренко Олена Сергіївна</span>
              <span class="role-desc">Студент • bondarenko@student.ua</span>
            </div>
          </div>
        </div>

        <div class="actions-group">
          <button class="btn btn-primary" disabled>Увійти до системи</button>
          <button class="btn btn-secondary">Продовжити як гість</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style src="@/css/views/LoginPage.css"></style>
