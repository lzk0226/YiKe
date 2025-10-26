const API_BASE = 'http://localhost:8080';
let currentUser = null;
let myNotesData = [];
let favoritesData = [];
let selectedAvatarBase64 = null;

function getAuthHeaders() {
  const token = localStorage.getItem('accessToken');
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : ''
  };
}

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
        await loadFollowStats();
      }
    }
  } catch (error) {
    console.error('加载用户信息失败:', error);
  }
}

function updateProfileForm(user) {
  document.getElementById('nickname').value = user.nickname || user.username || '';
  document.getElementById('email').value = user.email || '';
  document.getElementById('school').value = user.school || '';
  document.getElementById('major').value = user.major || '';
  const avatarPreview = document.getElementById('avatarPreview');
  avatarPreview.src = user.avatar || '/assets/img/2.jpg';
  selectedAvatarBase64 = null;
}

// 加载关注统计
async function loadFollowStats() {
  try {
    const userId = getCurrentUserId();

    const [followingRes, followerRes] = await Promise.all([
      fetch(`${API_BASE}/user/follow/followingCount?userId=${userId}`, {
        headers: getAuthHeaders()
      }),
      fetch(`${API_BASE}/user/follow/followerCount?userId=${userId}`, {
        headers: getAuthHeaders()
      })
    ]);

    if (followingRes.ok && followerRes.ok) {
      const followingResult = await followingRes.json();
      const followerResult = await followerRes.json();

      if (followingResult.code === 200) {
        document.getElementById('followingCount').textContent = followingResult.data || 0;
      }
      if (followerResult.code === 200) {
        document.getElementById('followerCount').textContent = followerResult.data || 0;
      }
    }
  } catch (error) {
    console.error('加载关注统计失败:', error);
  }
}

// 显示关注列表
async function showFollowingList() {
  const tab = document.querySelector('[data-tab="my-following"]');
  tab.click();
  await loadFollowingList();
}

// 显示粉丝列表
async function showFollowerList() {
  const tab = document.querySelector('[data-tab="my-followers"]');
  tab.click();
  await loadFollowerList();
}

// 获取用户详细信息
async function getUserInfo(userId) {
  try {
    const response = await fetch(`${API_BASE}/user/${userId}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        return result.data;
      }
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
  return null;
}

// 加载关注列表
async function loadFollowingList() {
  try {
    const userId = getCurrentUserId();
    const response = await fetch(`${API_BASE}/user/follow/list?followerId=${userId}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        const followList = result.rows || [];

        // 获取所有被关注用户的详细信息
        const usersWithInfo = await Promise.all(
          followList.map(async (follow) => {
            const userInfo = await getUserInfo(follow.followingId);
            return {
              ...follow,
              userInfo: userInfo
            };
          })
        );

        renderUserList(usersWithInfo, 'followingList', true);
      }
    }
  } catch (error) {
    console.error('加载关注列表失败:', error);
    renderUserList([], 'followingList', true);
  }
}

// 加载粉丝列表
async function loadFollowerList() {
  try {
    const userId = getCurrentUserId();
    const response = await fetch(`${API_BASE}/user/follow/list?followingId=${userId}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        const followerList = result.rows || [];

        // 获取所有粉丝的详细信息
        const usersWithInfo = await Promise.all(
          followerList.map(async (follow) => {
            const userInfo = await getUserInfo(follow.followerId);
            return {
              ...follow,
              userInfo: userInfo
            };
          })
        );

        renderUserList(usersWithInfo, 'followerList', false);
      }
    }
  } catch (error) {
    console.error('加载粉丝列表失败:', error);
    renderUserList([], 'followerList', false);
  }
}

// 渲染用户列表
function renderUserList(users, containerId, isFollowing) {
  const container = document.getElementById(containerId);
  if (!container) return;

  if (users.length === 0) {
    container.innerHTML = '<div style="text-align: center; padding: 2rem; color: #9ca3af;">暂无数据</div>';
    return;
  }

  container.innerHTML = users.map(item => {
    const user = item.userInfo;
    const userId = isFollowing ? item.followingId : item.followerId;

    if (!user) {
      return '';
    }

    return `
          <div class="user-item">
            <img src="${user.avatar || '/assets/img/2.jpg'}" 
                 alt="头像" 
                 class="user-avatar"
                 onclick="goToUserProfile(${userId})">
            <div class="user-info" onclick="goToUserProfile(${userId})">
              <div class="user-name">${user.nickname || user.username || '未知用户'}</div>
              <div class="user-meta">${user.school || '暂无学校信息'} ${user.major ? '· ' + user.major : ''}</div>
            </div>
            <div class="user-actions">
              ${isFollowing
        ? `<button class="action-btn secondary" onclick="unfollowUser(${userId})">取消关注</button>`
        : `<button class="action-btn" onclick="followUser(${userId})">关注</button>`
      }
            </div>
          </div>
        `;
  }).filter(html => html !== '').join('');
}

// 跳转到用户主页
function goToUserProfile(userId) {
  const currentUserId = getCurrentUserId();

  // 如果是查看自己，跳转到个人中心
  if (currentUserId && currentUserId.toString() === userId.toString()) {
    window.location.href = 'Profile-container.html';
  } else {
    // 否则跳转到用户主页
    window.location.href = `User-Profile.html?userId=${userId}`;
  }
}

// 关注用户
async function followUser(followingId) {
  try {
    const followerId = getCurrentUserId();
    const response = await fetch(`${API_BASE}/user/follow/follow?followerId=${followerId}&followingId=${followingId}`, {
      method: 'POST',
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        alert('关注成功');
        await loadFollowStats();
        await loadFollowerList();
      } else {
        alert(result.msg || '关注失败');
      }
    }
  } catch (error) {
    console.error('关注失败:', error);
    alert('关注失败: ' + error.message);
  }
}

// 取消关注
async function unfollowUser(followingId) {
  if (!confirm('确定要取消关注吗？')) return;

  try {
    const followerId = getCurrentUserId();
    const response = await fetch(`${API_BASE}/user/follow/unfollow?followerId=${followerId}&followingId=${followingId}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        alert('取消关注成功');
        await loadFollowStats();
        await loadFollowingList();
      } else {
        alert(result.msg || '取消关注失败');
      }
    }
  } catch (error) {
    console.error('取消关注失败:', error);
    alert('取消关注失败: ' + error.message);
  }
}

async function loadMyNotes() {
  try {
    const userId = getCurrentUserId();
    if (!userId) return;

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
          isMine: true
        }));
        renderNotes(myNotesData, 'myNotesGrid', true);
      }
    }
  } catch (error) {
    console.error('加载我的笔记失败:', error);
    myNotesData = [];
    renderNotes(myNotesData, 'myNotesGrid', true);
  }
}

async function loadMyFavorites() {
  try {
    const response = await fetch(`${API_BASE}/user/notes/favorites?page=1&pageSize=20`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        const favorites = result.data.list || [];
        favoritesData = favorites.map(note => ({
          id: note.id,
          title: note.title,
          author: note.author?.nickname || '未知作者',
          subject: note.subject?.name || '未分类',
          description: note.description || note.content?.substring(0, 100) || '暂无描述',
          views: note.views || 0,
          likes: note.likes || 0,
          rating: note.rating || 0,
          isMine: false
        }));
        renderNotes(favoritesData, 'favoritesGrid', false);
      }
    }
  } catch (error) {
    console.error('加载我的收藏失败:', error);
    favoritesData = [];
    renderNotes(favoritesData, 'favoritesGrid', false);
  }
}

function renderNotes(notes, containerId, showActions = true) {
  const container = document.getElementById(containerId);
  if (!container) return;

  if (notes.length === 0) {
    container.innerHTML = '<div style="text-align: center; padding: 2rem; color: #9ca3af;">暂无笔记</div>';
    return;
  }

  container.innerHTML = notes.map(note => `
        <div class="note-card">
          <div class="note-content">
            <div class="note-title">${note.title}</div>
            <div class="note-meta">
              <div class="note-author">作者：${note.author}</div>
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

      // 切换到对应标签页时加载数据
      if (targetTab === 'my-following') {
        loadFollowingList();
      } else if (targetTab === 'my-followers') {
        loadFollowerList();
      }
    });
  });
}

function initSorting() {
  document.addEventListener('click', (e) => {
    if (e.target.classList.contains('sort-btn')) {
      const sortGroup = e.target.closest('.sort-options');
      sortGroup.querySelectorAll('.sort-btn').forEach(btn => btn.classList.remove('active'));
      e.target.classList.add('active');

      const sortType = e.target.getAttribute('data-sort');
      const activeTab = document.querySelector('.tab-content.active').id;

      if (activeTab === 'my-notes') {
        loadMyNotesWithSort(sortType);
      } else {
        loadMyFavoritesWithSort(sortType);
      }
    }
  });
}

async function loadMyNotesWithSort(sortType) {
  await loadMyNotes();
  let sortedData = [...myNotesData];
  switch (sortType) {
    case 'latest': sortedData.sort((a, b) => b.id - a.id); break;
    case 'views': sortedData.sort((a, b) => b.views - a.views); break;
    case 'rating': sortedData.sort((a, b) => b.rating - a.rating); break;
    case 'likes': sortedData.sort((a, b) => b.likes - a.likes); break;
  }
  renderNotes(sortedData, 'myNotesGrid', true);
}

async function loadMyFavoritesWithSort(sortType) {
  await loadMyFavorites();
  let sortedData = [...favoritesData];
  switch (sortType) {
    case 'latest': break;
    case 'views': sortedData.sort((a, b) => b.views - a.views); break;
    case 'rating': sortedData.sort((a, b) => b.rating - a.rating); break;
    case 'likes': sortedData.sort((a, b) => b.likes - a.likes); break;
  }
  renderNotes(sortedData, 'favoritesGrid', false);
}

async function editNote(noteId) {
  window.location.href = `../component/newNote.html?id=${noteId}`;
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
        await loadMyNotes();
      }
    }
  } catch (error) {
    console.error('删除笔记失败:', error);
    alert('删除笔记失败: ' + error.message);
  }
}

async function viewNote(noteId) {
  window.location.href = `/component/noteDetails.html?id=${noteId}`;
}

async function unfavoriteNote(noteId) {
  if (!confirm('确定要取消收藏这篇笔记吗？')) return;
  try {
    const response = await fetch(`${API_BASE}/user/notes/favorite/${noteId}`, {
      method: 'POST',
      headers: getAuthHeaders()
    });
    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        alert('取消收藏成功');
        await loadMyFavorites();
      }
    }
  } catch (error) {
    console.error('取消收藏失败:', error);
    alert('取消收藏失败: ' + error.message);
  }
}

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
        const updatedUser = { ...currentUser, ...userData };
        localStorage.setItem('user', JSON.stringify(updatedUser));
        currentUser = updatedUser;
        return true;
      }
    }
  } catch (error) {
    console.error('保存个人资料失败:', error);
    throw error;
  }
}

function compressImage(file, maxWidth = 800, quality = 0.8) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = function (e) {
      const img = new Image();
      img.onload = function () {
        const canvas = document.createElement('canvas');
        let width = img.width;
        let height = img.height;
        if (width > maxWidth) {
          height = (height * maxWidth) / width;
          width = maxWidth;
        }
        canvas.width = width;
        canvas.height = height;
        const ctx = canvas.getContext('2d');
        ctx.drawImage(img, 0, 0, width, height);
        const base64 = canvas.toDataURL('image/jpeg', quality);
        resolve(base64);
      };
      img.onerror = reject;
      img.src = e.target.result;
    };
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
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
  console.error('导航栏组件加载失败');
}

function initializeNavbar() {
  if (window.NavbarComponent) {
    const navbar = new NavbarComponent({
      isLoggedIn: true,
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

document.addEventListener('DOMContentLoaded', async function () {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
  if (!isLoggedIn) {
    alert('请先登录');
    window.location.href = 'login.html';
    return;
  }

  // 加载导航栏
  loadNavbarComponent();

  initTabs();
  initSorting();
  await loadUserProfile();
  await loadMyNotes();
  await loadMyFavorites();

  const avatarInput = document.getElementById('avatarInput');
  const avatarPreview = document.getElementById('avatarPreview');

  avatarInput.addEventListener('change', async function (e) {
    const file = e.target.files[0];
    if (file) {
      if (!file.type.startsWith('image/')) {
        alert('请选择图片文件!');
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        alert('图片大小不能超过5MB!');
        return;
      }
      try {
        const base64 = await compressImage(file, 400, 0.8);
        avatarPreview.src = base64;
        selectedAvatarBase64 = base64;
      } catch (error) {
        console.error('处理图片失败:', error);
        alert('处理图片失败,请重试');
      }
    }
  });

  let isEditMode = false;
  const saveProfileBtn = document.getElementById('saveProfileBtn');
  const formInputs = document.querySelectorAll('.form-group input');

  saveProfileBtn.addEventListener('click', async () => {
    if (!isEditMode) {
      isEditMode = true;
      saveProfileBtn.textContent = '保存资料';
      saveProfileBtn.classList.add('edit-mode');
      formInputs.forEach(input => {
        if (input.id !== 'email') {
          input.disabled = false;
        }
      });
    } else {
      const nickname = document.getElementById('nickname').value.trim();
      const email = document.getElementById('email').value.trim();
      const school = document.getElementById('school').value.trim();
      const major = document.getElementById('major').value.trim();

      if (!nickname) {
        alert('昵称不能为空');
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

        if (selectedAvatarBase64) {
          userData.avatar = selectedAvatarBase64;
        }

        await saveUserProfile(userData);

        isEditMode = false;
        saveProfileBtn.textContent = '修改资料';
        saveProfileBtn.classList.remove('edit-mode');

        formInputs.forEach(input => {
          input.disabled = true;
        });

        selectedAvatarBase64 = null;
        alert('资料已保存!');
      } catch (error) {
        alert('保存失败: ' + error.message);
      }
    }
  });

  const fabButton = document.querySelector('.fab');
  if (fabButton) {
    fabButton.style.display = 'flex';
    fabButton.style.opacity = '1';
    fabButton.style.visibility = 'visible';
    fabButton.addEventListener('click', function () {
      window.location.href = '/component/newNote.html';
    });
  }
});