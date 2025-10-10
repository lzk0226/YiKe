// API基础URL
const API_BASE = 'http://localhost:8080';

// 获取当前用户信息
let currentUser = null;

// 初始化数据
let myNotesData = [];
let favoritesData = [];

// 获取认证头信息
function getAuthHeaders() {
  const token = localStorage.getItem('accessToken');
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : ''
  };
}

// 获取当前用户ID
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

// 加载用户信息
async function loadUserProfile() {
  try {
    const userId = getCurrentUserId();
    if (!userId) {
      alert('请先登录');
      window.location.href = 'login.html';
      return;
    }

    const response = await fetch(`${API_BASE}/user/${userId}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        currentUser = result.data;
        updateProfileForm(currentUser);
      } else {
        throw new Error(result.msg || '获取用户信息失败');
      }
    } else {
      throw new Error('网络请求失败');
    }
  } catch (error) {
    console.error('加载用户信息失败:', error);
    alert('加载用户信息失败: ' + error.message);
  }
}

// 更新个人资料表单
function updateProfileForm(user) {
  document.getElementById('nickname').value = user.nickname || user.username || '';
  document.getElementById('email').value = user.email || '';
  document.getElementById('school').value = user.school || '';
  document.getElementById('major').value = user.major || '';

  // 更新头像预览
  const avatarPreview = document.getElementById('avatarPreview');
  const defaultAvatar = '/assets/img/2.jpg'; // 默认头像路径，与导航栏一致
  avatarPreview.src = user.avatar ? user.avatar : defaultAvatar;
}


// 加载我的笔记
async function loadMyNotes() {
  try {
    const userId = getCurrentUserId();
    if (!userId) return;

    // 调用获取个人笔记的接口
    const response = await fetch(`${API_BASE}/user/notes/my/cards?userId=${userId}&page=1&pageSize=20`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        myNotesData = result.data.list.map(note => ({
          id: note.id,
          title: note.title,
          author: currentUser?.nickname || currentUser?.username || '我',
          subject: note.subjectName || '未分类',
          description: note.description || note.content?.substring(0, 100) || '暂无描述',
          views: note.views || 0,
          likes: note.likes || 0,
          rating: note.rating || 0,
          icon: getSubjectIcon(note.subjectName),
          isMine: true
        }));

        renderNotes(myNotesData, 'myNotesGrid', true);
      } else {
        throw new Error(result.msg || '获取笔记失败');
      }
    } else {
      throw new Error('网络请求失败');
    }
  } catch (error) {
    console.error('加载我的笔记失败:', error);
    myNotesData = [];
    renderNotes(myNotesData, 'myNotesGrid', true);
  }
}

// 加载我的收藏
async function loadMyFavorites() {
  try {
    // 调用新的获取收藏列表接口
    const response = await fetch(`${API_BASE}/user/notes/favorites?page=1&pageSize=20`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        const favorites = result.data.list || [];

        // 转换数据格式
        favoritesData = favorites.map(note => ({
          id: note.id,
          title: note.title,
          author: note.author?.nickname || '未知作者',
          subject: note.subject?.name || '未分类',
          description: note.description || note.content?.substring(0, 100) || '暂无描述',
          views: note.views || 0,
          likes: note.likes || 0,
          rating: note.rating || 0,
          icon: getSubjectIcon(note.subject?.name),
          isMine: false
        }));

        renderNotes(favoritesData, 'favoritesGrid', false);
      } else {
        throw new Error(result.msg || '获取收藏失败');
      }
    } else {
      throw new Error('网络请求失败');
    }
  } catch (error) {
    console.error('加载我的收藏失败:', error);
    favoritesData = [];
    renderNotes(favoritesData, 'favoritesGrid', false);
  }
}


// 根据学科获取图标
function getSubjectIcon(subjectName) {
  const iconMap = {
    '数学': '📘',
    '计算机': '💻',
    '英语': '🗣️',
    '物理': '⚡',
    '化学': '🧪',
    '生物': '🔬',
    '历史': '📜',
    '地理': '🌍',
    '语文': '📖'
  };
  return iconMap[subjectName] || '📝';
}

// 渲染笔记函数（保持不变，但需要调整操作函数）
function renderNotes(notes, containerId, showActions = true) {
  const container = document.getElementById(containerId);
  if (!container) return;

  container.innerHTML = notes.map(note => `
        <div class="note-card">
          <div class="note-content">
            <div class="note-title">${note.title}</div>
            <div class="note-meta">
              <div class="note-author">
                作者：${note.author}
              </div>
              <div class="note-subject">${note.subject}</div>
            </div>
            <div class="note-description">${note.description}</div>
            <div class="note-stats">
              <div>
                <span>👍 ${note.likes}</span>
                <span style="margin-left: 10px;">👁️ ${note.views}</span>
                <span style="margin-left: 10px;">⭐ ${note.rating}</span>
              </div>
              <div class="note-actions">
                ${showActions && note.isMine
      ? `<button class="action-btn" onclick="editNote(${note.id})">编辑</button>
                   <button class="action-btn secondary" onclick="deleteNote(${note.id})">删除</button>`
      : `<button class="action-btn" onclick="viewNote(${note.id})">查看</button>
                   ${!note.isMine ? `<button class="action-btn secondary" onclick="unfavoriteNote(${note.id})">取消收藏</button>` : ''}`
    }
              </div>
            </div>
          </div>
        </div>
      `).join('');
}

// 标签页切换（保持不变）
function initTabs() {
  const tabBtns = document.querySelectorAll('.tab-btn');
  const tabContents = document.querySelectorAll('.tab-content');

  tabBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      const targetTab = btn.getAttribute('data-tab');

      // 移除所有活跃状态
      tabBtns.forEach(b => b.classList.remove('active'));
      tabContents.forEach(c => c.classList.remove('active'));

      // 设置新的活跃状态
      btn.classList.add('active');
      document.getElementById(targetTab).classList.add('active');
    });
  });
}

// 排序功能（修改为重新加载数据）
function initSorting() {
  document.addEventListener('click', (e) => {
    if (e.target.classList.contains('sort-btn')) {
      // 移除同组内所有活跃状态
      const sortGroup = e.target.closest('.sort-options');
      sortGroup.querySelectorAll('.sort-btn').forEach(btn => btn.classList.remove('active'));
      e.target.classList.add('active');

      const sortType = e.target.getAttribute('data-sort');
      const activeTab = document.querySelector('.tab-content.active').id;

      // 根据排序类型重新加载数据
      if (activeTab === 'my-notes') {
        loadMyNotesWithSort(sortType);
      } else {
        loadMyFavoritesWithSort(sortType);
      }
    }
  });
}

// 按排序加载我的笔记
async function loadMyNotesWithSort(sortType) {
  // 这里可以添加排序参数到API调用
  await loadMyNotes();

  // 前端排序（如果后端不支持）
  let sortedData = [...myNotesData];
  switch (sortType) {
    case 'latest': sortedData.sort((a, b) => b.id - a.id); break;
    case 'views': sortedData.sort((a, b) => b.views - a.views); break;
    case 'rating': sortedData.sort((a, b) => b.rating - a.rating); break;
    case 'likes': sortedData.sort((a, b) => b.likes - a.likes); break;
  }

  renderNotes(sortedData, 'myNotesGrid', true);
}

// 按排序加载我的收藏（使用新接口）
async function loadMyFavoritesWithSort(sortType) {
  await loadMyFavorites();

  // 前端排序
  let sortedData = [...favoritesData];
  switch (sortType) {
    case 'latest':
      // 最新收藏已经是后端默认排序
      break;
    case 'views':
      sortedData.sort((a, b) => b.views - a.views);
      break;
    case 'rating':
      sortedData.sort((a, b) => b.rating - a.rating);
      break;
    case 'likes':
      sortedData.sort((a, b) => b.likes - a.likes);
      break;
  }

  renderNotes(sortedData, 'favoritesGrid', false);
}


// 笔记操作函数（对接后端）
async function editNote(noteId) {
  try {
    // 跳转到编辑页面或打开编辑模态框
    window.location.href = `../component/newNote.html?id=${noteId}`;
  } catch (error) {
    console.error('编辑笔记失败:', error);
    alert('编辑笔记失败: ' + error.message);
  }
}

async function deleteNote(noteId) {
  if (!confirm('确定要删除这篇笔记吗？')) return;

  try {
    const response = await fetch(`${API_BASE}/user/notes/${noteId}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        alert('笔记删除成功');
        // 重新加载我的笔记
        await loadMyNotes();
      } else {
        throw new Error(result.msg || '删除失败');
      }
    } else {
      throw new Error('网络请求失败');
    }
  } catch (error) {
    console.error('删除笔记失败:', error);
    alert('删除笔记失败: ' + error.message);
  }
}

async function viewNote(noteId) {
  // 跳转到笔记详情页面
  window.location.href = `/component/noteDetails.html?id=${noteId}`;
}

// 取消收藏（使用新接口）
async function unfavoriteNote(noteId) {
  if (!confirm('确定要取消收藏这篇笔记吗？')) return;

  try {
    // 调用后端的点赞/收藏toggle接口
    const response = await fetch(`${API_BASE}/user/notes/favorite/${noteId}`, {
      method: 'POST',
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        alert('取消收藏成功');
        // 重新加载我的收藏
        await loadMyFavorites();
      } else {
        throw new Error(result.msg || '取消收藏失败');
      }
    } else {
      throw new Error('网络请求失败');
    }
  } catch (error) {
    console.error('取消收藏失败:', error);
    alert('取消收藏失败: ' + error.message);
  }
}

// 保存个人资料
async function saveUserProfile(userData) {
  try {
    const response = await fetch(`${API_BASE}/user/update`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify(userData)
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        // 更新本地存储的用户信息
        const updatedUser = { ...currentUser, ...userData };
        localStorage.setItem('user', JSON.stringify(updatedUser));

        // 更新导航栏显示
        if (window.navbarInstance) {
          window.navbarInstance.refreshLoginStatus();
        }

        return true;
      } else {
        throw new Error(result.msg || '更新失败');
      }
    } else {
      throw new Error('网络请求失败');
    }
  } catch (error) {
    console.error('保存个人资料失败:', error);
    throw error;
  }
}

// 导航栏相关函数
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
    })
    .catch(error => {
      console.error('加载导航栏组件失败:', error);
      showFallbackNavbar();
    });
}

function showFallbackNavbar() {
  // 导航栏组件加载失败，不显示默认导航栏
  console.error('导航栏组件加载失败');
}

function initializeNavbar() {
  if (window.NavbarComponent) {
    const navbar = new NavbarComponent({
      isLoggedIn: true, // 个人中心页面默认已登录
      onNavigate: (section, href) => {
        if (href.includes('.html')) {
          window.location.href = href;
        }
      },
      onAuth: (action) => {
        console.log('认证操作:', action);
      },
      onUpload: () => {
        alert('打开上传笔记页面');
      },
      onUserAction: (action) => {
        switch (action) {
          case 'profile':
            // 已在个人中心，无需跳转
            break;
          case 'my-notes':
            alert('打开我的笔记');
            break;
          case 'favorites':
            alert('打开我的收藏');
            break;
          case 'settings':
            alert('打开设置页面');
            break;
          case 'logout':
            if (confirm('确定要退出登录吗？')) {
              window.location.href = 'index.html';
            }
            break;
        }
      }
    });
  }
}


// 页面初始化
document.addEventListener('DOMContentLoaded', async function () {
  // 检查登录状态
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
  if (!isLoggedIn) {
    alert('请先登录');
    window.location.href = 'login.html';
    return;
  }

  // 加载导航栏
  loadNavbarComponent();

  // 初始化标签页
  initTabs();

  // 初始化排序功能
  initSorting();

  // 加载用户信息
  await loadUserProfile();

  // 加载我的笔记
  await loadMyNotes();

  // 加载我的收藏
  await loadMyFavorites();

  // 头像上传预览（保持不变）
  const avatarInput = document.getElementById('avatarInput');
  const avatarPreview = document.getElementById('avatarPreview');

  avatarInput.addEventListener('change', function (e) {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        avatarPreview.src = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  });

  // 保存资料按钮功能
  let isEditMode = false;
  const saveProfileBtn = document.getElementById('saveProfileBtn');
  const formInputs = document.querySelectorAll('.form-group input');

  saveProfileBtn.addEventListener('click', async () => {
    if (!isEditMode) {
      // 切换到编辑模式
      isEditMode = true;
      saveProfileBtn.textContent = '保存资料';
      saveProfileBtn.classList.add('edit-mode');

      // 启用所有输入框
      formInputs.forEach(input => {
        input.disabled = false;
      });
    } else {
      // 保存并切换回查看模式
      const nickname = document.getElementById('nickname').value;
      const email = document.getElementById('email').value;
      const school = document.getElementById('school').value;
      const major = document.getElementById('major').value;

      if (!nickname || !email) {
        alert('请填写必要信息');
        return;
      }

      try {
        const userData = {
          id: currentUser.id,
          nickname: nickname,
          email: email,
          school: school,
          major: major
        };

        await saveUserProfile(userData);

        isEditMode = false;
        saveProfileBtn.textContent = '修改资料';
        saveProfileBtn.classList.remove('edit-mode');

        // 禁用所有输入框
        formInputs.forEach(input => {
          input.disabled = true;
        });

        alert('资料已保存！');
      } catch (error) {
        alert('保存失败: ' + error.message);
      }
    }
  });
});