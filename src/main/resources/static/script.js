const API = '/api';
let currentUser = null;
let editingTaskId = null;

function switchTab(tab, btn) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById('loginForm').classList.toggle('hidden', tab !== 'login');
    document.getElementById('registerForm').classList.toggle('hidden', tab !== 'register');
}

async function login() {
    const username = document.getElementById('loginUsername').value.trim();
    const password = document.getElementById('loginPassword').value;
    document.getElementById('loginError').textContent = '';
    if (!username || !password) {
        document.getElementById('loginError').textContent = 'Remplissez tous les champs.';
        return;
    }
    try {
        const res = await fetch(`${API}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Erreur');
        currentUser = { userId: data.userId, username: data.username };
        showApp();
    } catch (err) {
        document.getElementById('loginError').textContent = err.message;
    }
}

async function register() {
    const username = document.getElementById('regUsername').value.trim();
    const email    = document.getElementById('regEmail').value.trim();
    const password = document.getElementById('regPassword').value;
    document.getElementById('registerError').textContent = '';
    if (!username || !email || !password) {
        document.getElementById('registerError').textContent = 'Remplissez tous les champs.';
        return;
    }
    try {
        const res = await fetch(`${API}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.message || 'Erreur');
        currentUser = { userId: data.userId, username: data.username };
        showApp();
    } catch (err) {
        document.getElementById('registerError').textContent = err.message;
    }
}

function logout() {
    currentUser = null;
    document.getElementById('appPage').classList.add('hidden');
    document.getElementById('authPage').classList.remove('hidden');
}

function showApp() {
    document.getElementById('authPage').classList.add('hidden');
    document.getElementById('appPage').classList.remove('hidden');
    document.getElementById('welcomeUser').textContent = '👋 ' + currentUser.username;
    loadTasks();
    checkUrgentTasks();
}

async function loadTasks() {
    try {
        const res = await fetch(`${API}/tasks?userId=${currentUser.userId}`);
        const tasks = await res.json();
        renderTasks(tasks);
        updateStats(tasks);
    } catch (err) { console.error(err); }
}

async function applyFilter() {
    const val = document.getElementById('filterBy').value;
    if (!val) { loadTasks(); return; }
    const [by, value] = val.split('_');
    try {
        const res = await fetch(`${API}/tasks/filter?userId=${currentUser.userId}&by=${by}&value=${value}`);
        renderTasks(await res.json());
    } catch (err) { console.error(err); }
}

async function searchTasks() {
    const keyword = document.getElementById('searchInput').value.trim();
    if (!keyword) { loadTasks(); return; }
    try {
        const res = await fetch(`${API}/tasks/search?userId=${currentUser.userId}&keyword=${encodeURIComponent(keyword)}`);
        renderTasks(await res.json());
    } catch (err) { console.error(err); }
}

function renderTasks(tasks) {
    const container = document.getElementById('taskList');
    if (!tasks || tasks.length === 0) {
        container.innerHTML = '<div class="empty-state">Aucune tâche trouvée.</div>';
        return;
    }
    container.innerHTML = tasks.map(t => `
        <div class="task-card ${t.status === 'DONE' ? 'done-card' : ''}">
            <div class="priority-dot ${t.priority}"></div>
            <div class="task-body">
                <div class="task-title">${esc(t.title)}</div>
                ${t.description ? `<div class="task-desc">${esc(t.description)}</div>` : ''}
                <div class="task-badges">
                    <span class="badge badge-status-${t.status}">${statusLabel(t.status)}</span>
                    <span class="badge badge-priority-${t.priority}">${priorityLabel(t.priority)}</span>
                </div>
            </div>
            <div class="task-actions">
                <button class="btn-icon" onclick="openEditModal(${t.id})">✏️</button>
                <button class="btn-icon delete" onclick="deleteTask(${t.id})">🗑️</button>
            </div>
        </div>`).join('');
}

function updateStats(tasks) {
    document.getElementById('statTotal').textContent    = tasks.length;
    document.getElementById('statTodo').textContent     = tasks.filter(t => t.status === 'TODO').length;
    document.getElementById('statProgress').textContent = tasks.filter(t => t.status === 'IN_PROGRESS').length;
    document.getElementById('statDone').textContent     = tasks.filter(t => t.status === 'DONE').length;
}

function openModal() {
    editingTaskId = null;
    document.getElementById('modalTitle').textContent = 'Nouvelle tâche';
    document.getElementById('taskTitle').value = '';
    document.getElementById('taskDesc').value  = '';
    document.getElementById('taskStatus').value   = 'TODO';
    document.getElementById('taskPriority').value = 'MEDIUM';
    document.getElementById('modalError').textContent = '';
    document.getElementById('taskModal').classList.remove('hidden');
}

async function openEditModal(id) {
    try {
        const res  = await fetch(`${API}/tasks/${id}`);
        const task = await res.json();
        editingTaskId = id;
        document.getElementById('modalTitle').textContent = 'Modifier la tâche';
        document.getElementById('taskTitle').value    = task.title;
        document.getElementById('taskDesc').value     = task.description || '';
        document.getElementById('taskStatus').value   = task.status;
        document.getElementById('taskPriority').value = task.priority;
        document.getElementById('modalError').textContent = '';
        document.getElementById('taskModal').classList.remove('hidden');
    } catch (err) { console.error(err); }
}

function closeModal() {
    document.getElementById('taskModal').classList.add('hidden');
}

async function saveTask() {
    const title    = document.getElementById('taskTitle').value.trim();
    const desc     = document.getElementById('taskDesc').value.trim();
    const status   = document.getElementById('taskStatus').value;
    const priority = document.getElementById('taskPriority').value;
    document.getElementById('modalError').textContent = '';
    if (!title) { document.getElementById('modalError').textContent = 'Le titre est obligatoire.'; return; }
    const body = { title, description: desc, status, priority };
    try {
        const url = editingTaskId
            ? `${API}/tasks/${editingTaskId}`
            : `${API}/tasks?userId=${currentUser.userId}`;
        const method = editingTaskId ? 'PUT' : 'POST';
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        if (!res.ok) throw new Error('Erreur sauvegarde');
        closeModal();
        loadTasks();
    } catch (err) { document.getElementById('modalError').textContent = err.message; }
}

async function deleteTask(id) {
    if (!confirm('Supprimer cette tâche ?')) return;
    try {
        await fetch(`${API}/tasks/${id}`, { method: 'DELETE' });
        loadTasks();
    } catch (err) { console.error(err); }
}

function statusLabel(s)   { return { TODO: 'À faire', IN_PROGRESS: 'En cours', DONE: 'Terminé' }[s] || s; }
function priorityLabel(p) { return { HIGH: 'Haute', MEDIUM: 'Moyenne', LOW: 'Basse' }[p] || p; }
function esc(str) { return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;'); }

document.addEventListener('keydown', e => {
    if (e.key === 'Escape') closeModal();
});


async function checkUrgentTasks() {
    try {
        const res = await fetch(`/api/tasks/urgent?userId=${currentUser.userId}`);
        const urgent = await res.json();
        if (urgent.length > 0) {
            showNotificationBanner(urgent);
        }
    } catch (err) { console.error(err); }
}

function showNotificationBanner(urgentTasks) {
    // Supprimer ancienne bannière si elle existe
    const old = document.getElementById('notifBanner');
    if (old) old.remove();

    const banner = document.createElement('div');
    banner.id = 'notifBanner';
    banner.style.cssText = `
        position: fixed;
        top: 70px;
        right: 20px;
        background: #1a1d27;
        border: 1px solid #f87171;
        border-radius: 12px;
        padding: 16px 20px;
        max-width: 320px;
        z-index: 999;
        box-shadow: 0 4px 24px rgba(248,113,113,0.3);
        animation: slideIn .3s ease;
    `;

    banner.innerHTML = `
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:10px;">
            <strong style="color:#f87171;">🚨 ${urgentTasks.length} tâche(s) urgente(s)</strong>
            <button onclick="document.getElementById('notifBanner').remove()"
                style="background:transparent; border:none; color:#8b92b8; cursor:pointer; font-size:16px;">✕</button>
        </div>
        ${urgentTasks.slice(0, 3).map(t => `
            <div style="font-size:13px; color:#e8eaf6; padding:6px 0; border-top:1px solid #2e3350;">
                ${t.notification}
            </div>
        `).join('')}
    `;

    document.body.appendChild(banner);

    // Disparaît automatiquement après 6 secondes
    setTimeout(() => {
        if (document.getElementById('notifBanner')) {
            document.getElementById('notifBanner').remove();
        }
    }, 6000);
}