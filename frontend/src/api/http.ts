const BASE_URL = '/api'

function getToken(): string | null {
  return localStorage.getItem('token')
}

async function http<T>(path: string, options: RequestInit = {}, skipContentType = false): Promise<T> {
  const token = getToken()
  const headers: Record<string, string> = {
    ...(skipContentType ? {} : { 'Content-Type': 'application/json' }),
    ...(options.headers as Record<string, string>)
  }
  if (token && !path.startsWith('/auth/')) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const res = await fetch(`${BASE_URL}${path}`, { ...options, headers })

  if (res.status === 401) {
    localStorage.removeItem('token')
    window.location.href = '/login'
    throw new Error('Unauthorized')
  }
  if (!res.ok) throw new Error(`${res.status} ${res.statusText}`)
  if (res.status === 204) return undefined as T
  return res.json()
}

export const api = {
  get: <T>(path: string) => http<T>(path),
  post: <T>(path: string, body: unknown) =>
    http<T>(path, { method: 'POST', body: JSON.stringify(body) }),
  postEmpty: <T>(path: string) =>
    http<T>(path, { method: 'POST' }, true),
  put: <T>(path: string, body: unknown) =>
    http<T>(path, { method: 'PUT', body: JSON.stringify(body) }),
  delete: (path: string) => http<void>(path, { method: 'DELETE' })
}
