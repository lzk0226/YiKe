// API 基础 URL
const API_BASE = 'http://localhost:8080';

// 当前页面状态
let currentTab = 'hot-notes';
let currentPage = 1;
const pageSize = 10;

// 获取登录状态
function checkLoginStatus() {
  const accessToken = localStorage.getItem('accessToken');
  const isLoggedIn = localStorage.getItem('isLoggedIn');
  return !!(accessToken && isLoggedIn === 'true');
}

// 获取当前登录用户ID
function getCurrentUserId() {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    try {
      const user = JSON.parse(userStr);
      return user.id;
    } catch (e) {
      console.error('解析用户信息失败:', e);
    }
  }
  return null;
}

// 获取访问令牌
function getAccessToken() {
  return localStorage.getItem('accessToken');
}

// 更新 FAB 按钮显示状态
function updateFabButtonVisibility() {
  const fabButton = document.querySelector('.fab');
  if (checkLoginStatus()) {
    fabButton.classList.add('show');
  } else {
    fabButton.classList.remove('show');
  }
}

// API 请求封装
async function apiRequest(url) {
  try {
    const headers = {
      'Content-Type': 'application/json'
    };

    const token = getAccessToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, { headers });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('API请求失败:', error);
    throw error;
  }
}

// 获取热门笔记
async function fetchHotNotes(page = 1) {
  showLoading('hotNotes');
  try {
    const data = await apiRequest(`${API_BASE}/user/ranking/hot-notes?page=${page}&size=${pageSize}`);
    if (data.success && data.data) {
      renderHotNotes(data.data);
      updatePagination(data.total, page);
    } else {
      showError('hotNotes', '暂无数据');
    }
  } catch (error) {
    showError('hotNotes', '加载失败，请稍后重试');
  }
}

// 获取优秀作者
async function fetchTopAuthors(page = 1) {
  showLoading('topAuthors');
  try {
    const data = await apiRequest(`${API_BASE}/user/ranking/top-authors?page=${page}&size=${pageSize}`);
    if (data.success && data.data) {
      renderTopAuthors(data.data);
      updatePagination(data.total, page);
    } else {
      showError('topAuthors', '暂无数据');
    }
  } catch (error) {
    showError('topAuthors', '加载失败，请稍后重试');
  }
}

// 获取本周趋势
async function fetchTrending(page = 1) {
  showLoading('trending');
  try {
    const data = await apiRequest(`${API_BASE}/user/ranking/trending?page=${page}&size=${pageSize}`);
    if (data.success && data.data) {
      renderTrending(data.data);
      updatePagination(data.total, page);
    } else {
      showError('trending', '暂无数据');
    }
  } catch (error) {
    showError('trending', '加载失败，请稍后重试');
  }
}

// 显示加载状态
function showLoading(type) {
  document.getElementById(`${type}Loading`).style.display = 'block';
  document.getElementById(`${type}List`).innerHTML = '';
  document.getElementById(`${type}Error`).style.display = 'none';
}

// 显示错误信息
function showError(type, message) {
  document.getElementById(`${type}Loading`).style.display = 'none';
  document.getElementById(`${type}List`).innerHTML = '';
  const errorEl = document.getElementById(`${type}Error`);
  errorEl.textContent = message;
  errorEl.style.display = 'block';
}

// 渲染热门笔记
function renderHotNotes(notes) {
  document.getElementById('hotNotesLoading').style.display = 'none';
  const container = document.getElementById('hotNotesList');

  if (!notes || notes.length === 0) {
    container.innerHTML = '<div class="error-message">暂无热门笔记</div>';
    return;
  }

  container.innerHTML = notes.map((note, index) => {
    // 处理作者头像
    const authorName = note.authorNickname || note.authorName || '用';
    const authorId = note.authorId || note.userId;
    let avatarHtml = '';
    if (note.authorAvatar && note.authorAvatar.startsWith('data:image')) {
      avatarHtml = `<img src="${note.authorAvatar}" alt="头像" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">`;
    } else if (note.authorAvatar && (note.authorAvatar.startsWith('http://') || note.authorAvatar.startsWith('https://'))) {
      avatarHtml = `<img src="${note.authorAvatar}" alt="头像" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">`;
    } else {
      avatarHtml = authorName.charAt(0);
    }

    return `
          <div class="ranking-item ${index < 3 ? `top-${index + 1}` : ''}">
            <div class="rank-number">${(currentPage - 1) * pageSize + index + 1}</div>
            <div class="item-avatar" onclick="viewAuthor(${authorId}); event.stopPropagation();" style="cursor: pointer;">${avatarHtml}</div>
            <div class="item-info" onclick="viewNote(${note.id})" style="cursor: pointer;">
              <div class="item-title">${note.title || '未命名笔记'}</div>
              <div class="item-meta">
                <span onclick="viewAuthor(${authorId}); event.stopPropagation();" style="cursor: pointer; color: #3b82f6;">作者: ${authorName}</span>
                <span class="meta-tag">${note.subjectName || '未分类'}</span>
              </div>
            </div>
            <div class="item-stats">
              <div class="stat-item">
                <div class="stat-value">${formatNumber(note.views || 0)}</div>
                <div class="stat-label">阅读</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">${formatNumber(note.likes || 0)}</div>
                <div class="stat-label">点赞</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">${note.rating ? parseFloat(note.rating).toFixed(1) : '0.0'}</div>
                <div class="stat-label">评分</div>
              </div>
            </div>
          </div>
        `;
  }).join('');
}

// 渲染优秀作者
function renderTopAuthors(authors) {
  document.getElementById('topAuthorsLoading').style.display = 'none';
  const container = document.getElementById('topAuthorsList');

  if (!authors || authors.length === 0) {
    container.innerHTML = '<div class="error-message">暂无优秀作者</div>';
    return;
  }

  container.innerHTML = authors.map((author, index) => {
    // 处理头像显示
    let avatarHtml = '';
    if (author.avatar && author.avatar.startsWith('data:image')) {
      avatarHtml = `<img src="${author.avatar}" alt="头像" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">`;
    } else if (author.avatar && (author.avatar.startsWith('http://') || author.avatar.startsWith('https://'))) {
      avatarHtml = `<img src="${author.avatar}" alt="头像" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">`;
    } else {
      avatarHtml = (author.nickname || author.username || '用').charAt(0);
    }

    return `
          <div class="ranking-item ${index < 3 ? `top-${index + 1}` : ''}" onclick="viewAuthor(${author.id})" style="cursor: pointer;">
            <div class="rank-number">${(currentPage - 1) * pageSize + index + 1}</div>
            <div class="item-avatar">${avatarHtml}</div>
            <div class="item-info">
              <div class="item-title">${author.nickname || author.username || '匿名用户'}</div>
              <div class="item-meta">
                <span>笔记数: ${author.noteCount || 0}</span>
                <span>${author.school || '未知学校'}</span>
              </div>
            </div>
            <div class="item-stats">
              <div class="stat-item">
                <div class="stat-value">${formatNumber(author.totalViews || 0)}</div>
                <div class="stat-label">总阅读</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">${formatNumber(author.totalLikes || 0)}</div>
                <div class="stat-label">总点赞</div>
              </div>
            </div>
          </div>
        `;
  }).join('');
}

// 渲染本周趋势
function renderTrending(notes) {
  document.getElementById('trendingLoading').style.display = 'none';
  const container = document.getElementById('trendingList');

  if (!notes || notes.length === 0) {
    container.innerHTML = '<div class="error-message">本周暂无趋势笔记</div>';
    return;
  }

  container.innerHTML = notes.map((note, index) => {
    // 处理作者头像
    const authorName = note.authorNickname || note.authorName || '用';
    const authorId = note.authorId || note.userId;
    let avatarHtml = '';
    if (note.authorAvatar && note.authorAvatar.startsWith('data:image')) {
      avatarHtml = `<img src="${note.authorAvatar}" alt="头像" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">`;
    } else if (note.authorAvatar && (note.authorAvatar.startsWith('http://') || note.authorAvatar.startsWith('https://'))) {
      avatarHtml = `<img src="${note.authorAvatar}" alt="头像" style="width: 100%; height: 100%; object-fit: cover; border-radius: 50%;">`;
    } else {
      avatarHtml = authorName.charAt(0);
    }

    return `
          <div class="ranking-item ${index < 3 ? `top-${index + 1}` : ''}">
            <div class="rank-number">${(currentPage - 1) * pageSize + index + 1}</div>
            <div class="item-avatar" onclick="viewAuthor(${authorId}); event.stopPropagation();" style="cursor: pointer;">${avatarHtml}</div>
            <div class="item-info" onclick="viewNote(${note.id})" style="cursor: pointer;">
              <div class="item-title">${note.title || '未命名笔记'}</div>
              <div class="item-meta">
                <span onclick="viewAuthor(${authorId}); event.stopPropagation();" style="cursor: pointer; color: #3b82f6;">作者: ${authorName}</span>
                <span class="meta-tag">${note.subjectName || '未分类'}</span>
                ${note.growthRate ? `<span style="color: #10b981; font-weight: 600;">${note.growthRate}</span>` : ''}
              </div>
            </div>
            <div class="item-stats">
              <div class="stat-item">
                <div class="stat-value">${formatNumber(note.views || 0)}</div>
                <div class="stat-label">阅读</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">${formatNumber(note.likes || 0)}</div>
                <div class="stat-label">点赞</div>
              </div>
            </div>
          </div>
        `;
  }).join('');
}

// 更新分页控件
function updatePagination(total, page) {
  const pagination = document.getElementById('pagination');
  const totalPages = Math.ceil(total / pageSize);

  if (totalPages <= 1) {
    pagination.style.display = 'none';
    return;
  }

  pagination.style.display = 'flex';
  document.getElementById('pageInfo').textContent = `第 ${page} 页 / 共 ${totalPages} 页`;
  document.getElementById('prevPage').disabled = page <= 1;
  document.getElementById('nextPage').disabled = page >= totalPages;
}

// 格式化数字
function formatNumber(num) {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w';
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k';
  }
  return num.toString();
}

// 查看笔记详情
function viewNote(noteId) {
  window.location.href = `/component/noteDetails.html?id=${noteId}`;
}

// 查看作者主页
function viewAuthor(authorId) {
  if (!authorId) {
    console.error('作者ID为空');
    return;
  }

  const currentUserId = getCurrentUserId();

  // 如果是查看自己，跳转到个人中心
  if (currentUserId && currentUserId.toString() === authorId.toString()) {
    window.location.href = 'Profile-container.html';
  } else {
    // 否则跳转到用户主页
    window.location.href = `User-Profile.html?userId=${authorId}`;
  }
}

// 加载当前标签页数据
function loadCurrentTabData(page = 1) {
  currentPage = page;
  switch (currentTab) {
    case 'hot-notes':
      fetchHotNotes(page);
      break;
    case 'top-authors':
      fetchTopAuthors(page);
      break;
    case 'trending':
      fetchTrending(page);
      break;
  }
}

// 标签页切换
function initTabs() {
  const tabBtns = document.querySelectorAll('.tab-btn');
  const tabContents = document.querySelectorAll('.tab-content');

  tabBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      const targetTab = btn.getAttribute('data-tab');

      tabBtns.forEach(b => b.classList.remove('active'));
      tabContents.forEach(c => c.classList.remove('active'));

      btn.classList.add('active');
      document.getElementById(targetTab).classList.add('active');

      currentTab = targetTab;
      currentPage = 1;
      loadCurrentTabData(1);
    });
  });
}

// 初始化分页按钮
function initPagination() {
  document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 1) {
      loadCurrentTabData(currentPage - 1);
    }
  });

  document.getElementById('nextPage').addEventListener('click', () => {
    loadCurrentTabData(currentPage + 1);
  });
}

// 加载导航栏
function loadNavbarComponent() {
  fetch('./component/navbar.html')
    .then(response => response.text())
    .then(html => {
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, 'text/html');
      const navbar = doc.querySelector('#navbar-component');
      const styles = doc.querySelector('style');
      const script = doc.querySelector('script');

      if (styles) {
        document.head.appendChild(styles);
      }

      if (navbar) {
        document.getElementById('navbar-container').appendChild(navbar);
      }

      if (script) {
        const newScript = document.createElement('script');
        newScript.textContent = script.textContent;
        document.body.appendChild(newScript);
      }

      initializeNavbar();
      updateFabButtonVisibility();
    })
    .catch(error => {
      console.error('加载导航栏组件失败:', error);
    });
}

// 初始化导航栏
function initializeNavbar() {
  if (window.NavbarComponent) {
    const navbar = new NavbarComponent({
      isLoggedIn: checkLoginStatus(),
      onNavigate: (section, href) => {
        if (href.includes('.html')) {
          window.location.href = href;
        }
      }
    });
  }
}

// 页面初始化
document.addEventListener('DOMContentLoaded', function () {
  // 加载导航栏
  loadNavbarComponent();

  // 初始化标签页
  initTabs();

  // 初始化分页
  initPagination();

  // 加载初始数据
  loadCurrentTabData(1);

  // FAB 按钮点击事件
  const fabButton = document.querySelector('.fab');
  fabButton.addEventListener('click', function () {
    if (checkLoginStatus()) {
      window.location.href = '/component/newNote.html';
    } else {
      alert('请先登录后再发布笔记');
    }
  });

  // 监听登录状态变化
  window.addEventListener('storage', function (e) {
    if (e.key === 'accessToken' || e.key === 'isLoggedIn') {
      updateFabButtonVisibility();
    }
  });
});