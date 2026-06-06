import { defineStore } from 'pinia'
import { ref } from 'vue'

function decodeJwt(token: string): any {
  try {
    const payload = token.split('.')[1]
    return JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')))
  } catch {
    return {}
  }
}

function getUserIdFromToken(token: string | null): number | null {
  if (!token) return null
  const decoded = decodeJwt(token)
  return decoded.userId ?? null
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const username = ref<string | null>(localStorage.getItem('username'))
  const role = ref<string | null>(localStorage.getItem('role'))
  const userId = ref<number | null>(getUserIdFromToken(localStorage.getItem('token')))

  function setAuth(t: string, u: string, r: string) {
    token.value = t
    username.value = u
    role.value = r
    userId.value = getUserIdFromToken(t)
    localStorage.setItem('token', t)
    localStorage.setItem('username', u)
    localStorage.setItem('role', r)
  }

  function clearAuth() {
    token.value = null
    username.value = null
    role.value = null
    userId.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
  }

  function isRole(...roles: string[]): boolean {
    return roles.includes(role.value ?? '')
  }

  return { token, username, role, userId, setAuth, clearAuth, isRole }
})
