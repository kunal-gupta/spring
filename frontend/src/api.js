const API_BASE = "";
let memoryToken = "";

function readErrorMessage(payload, fallback) {
  if (!payload) return fallback;
  if (typeof payload === "string") return payload;
  if (payload.error) return payload.error;
  const firstKey = Object.keys(payload)[0];
  return firstKey ? `${firstKey}: ${payload[firstKey]}` : fallback;
}

function readStoredToken() {
  try {
    return localStorage.getItem("auth_token") || memoryToken || "";
  } catch {
    return memoryToken || "";
  }
}

function writeStoredToken(token) {
  memoryToken = token || "";
  try {
    if (token) {
      localStorage.setItem("auth_token", token);
    } else {
      localStorage.removeItem("auth_token");
    }
  } catch {
    // localStorage can fail in locked-down browser contexts.
  }
}

export async function apiRequest(path, options = {}) {
  const token = readStoredToken();
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers
  });

  const text = await response.text();
  let payload = null;
  if (text) {
    try {
      payload = JSON.parse(text);
    } catch {
      payload = text;
    }
  }

  if (!response.ok) {
    throw new Error(readErrorMessage(payload, `Request failed (${response.status})`));
  }

  return payload;
}

export function setToken(token) {
  writeStoredToken(token);
}

export function clearToken() {
  writeStoredToken("");
}

export function hasToken() {
  return Boolean(readStoredToken());
}
