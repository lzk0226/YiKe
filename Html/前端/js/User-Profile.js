// API基础URL
const API_BASE = 'http://localhost:8080';

// 当前查看的用户ID
let viewingUserId = null;

// 当前登录用户ID
let currentUserId = null;

// 笔记数据
let userNotesData = [];

// 获取认证头信息
function getAuthHeaders() {
  const token = localStorage.getItem('accessToken');
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : ''
  };
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

// 从URL获取用户ID
function getUserIdFromURL() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get('userId');
}

// 加载用户信息
async function loadUserProfile() {
  try {
    const response = await fetch(`${API_BASE}/user/${viewingUserId}`, {
      headers: getAuthHeaders()
    });

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        const user = result.data;
        updateUserProfile(user);
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

// 更新用户信息显示
function updateUserProfile(user) {
  document.getElementById('userName').textContent = user.nickname || user.username || '未知用户';
  document.getElementById('userEmail').textContent = user.email || '-';
  document.getElementById('userSchool').textContent = user.school || '暂无信息';
  document.getElementById('userMajor').textContent = user.major || '暂无信息';

  const avatarElement = document.getElementById('userAvatar');

  if (user.avatar) {
    // 先强制设置尺寸
    avatarElement.style.width = '120px';
    avatarElement.style.height = '120px';
    avatarElement.style.objectFit = 'cover';

    // 然后设置图片源
    avatarElement.src = user.avatar;

    // 图片加载完成后再次确保尺寸
    avatarElement.onload = function () {
      this.style.width = '120px';
      this.style.height = '120px';
      this.style.objectFit = 'cover';
    };
  } else {
    avatarElement.src = '/assets/img/2.jpg';
  }
}

// 加载关注和粉丝数量
async function loadFollowStats() {
  try {
    // 获取关注数
    const followingResponse = await fetch(
      `${API_BASE}/user/follow/followingCount?userId=${viewingUserId}`,
      { headers: getAuthHeaders() }
    );

    if (followingResponse.ok) {
      const followingResult = await followingResponse.json();
      if (followingResult.code === 200) {
        document.getElementById('followingCount').textContent = followingResult.data || 0;
      }
    }

    // 获取粉丝数
    const followerResponse = await fetch(
      `${API_BASE}/user/follow/followerCount?userId=${viewingUserId}`,
      { headers: getAuthHeaders() }
    );

    if (followerResponse.ok) {
      const followerResult = await followerResponse.json();
      if (followerResult.code === 200) {
        document.getElementById('followerCount').textContent = followerResult.data || 0;
      }
    }
  } catch (error) {
    console.error('加载关注统计失败:', error);
  }
}

// 加载用户笔记
async function loadUserNotes() {
  try {
    const response = await fetch(
      `${API_BASE}/user/notes/my/cards?userId=${viewingUserId}&page=1&pageSize=50`,
      { headers: getAuthHeaders() }
    );

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        const notes = result.data.list || [];

        // 更新笔记数量
        document.getElementById('notesCount').textContent = notes.length;

        userNotesData = notes.map(note => ({
          id: note.id,
          title: note.title,
          subject: note.subjectName || '未分类',
          description: note.description || note.content?.substring(0, 100) || '暂无描述',
          views: note.views || 0,
          likes: note.likes || 0,
          rating: note.rating || 0,
          createdAt: note.createdAt || note.createTime,
          icon: getSubjectIcon(note.subjectName)
        }));

        renderNotes(userNotesData);
      } else {
        throw new Error(result.msg || '获取笔记失败');
      }
    } else {
      throw new Error('网络请求失败');
    }
  } catch (error) {
    console.error('加载用户笔记失败:', error);
    userNotesData = [];
    renderNotes(userNotesData);
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
  return iconMap[subjectName] || '📚';
}

// 渲染笔记列表
function renderNotes(notes) {
  const container = document.getElementById('userNotesGrid');
  const emptyState = document.getElementById('emptyState');

  if (notes.length === 0) {
    container.style.display = 'none';
    emptyState.style.display = 'block';
    return;
  }

  container.style.display = 'grid';
  emptyState.style.display = 'none';

  container.innerHTML = notes.map(note => `
  <div class="note-card" onclick="viewNote(${note.id})">
    <div class="note-content">
      <div class="note-title">${note.title}</div>
      <div class="note-meta">
        <span class="note-date">${formatDate(note.createdAt)}</span>
        <span class="note-subject">${note.subject}</span>
      </div>
      <div class="note-description">${note.description}</div>
      <div class="note-stats">
        <div class="note-stats-left">
          <span>👍 ${note.likes}</span>
          <span>👁️ ${note.views}</span>
          <span>⭐ ${note.rating}</span>
        </div>
      </div>
    </div>
  </div>
`).join('');

}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return '-';

  const date = new Date(dateStr);
  const now = new Date();
  const diff = now - date;

  const minute = 60 * 1000;
  const hour = 60 * minute;
  const day = 24 * hour;
  const month = 30 * day;

  if (diff < minute) {
    return '刚刚';
  } else if (diff < hour) {
    return Math.floor(diff / minute) + '分钟前';
  } else if (diff < day) {
    return Math.floor(diff / hour) + '小时前';
  } else if (diff < month) {
    return Math.floor(diff / day) + '天前';
  } else {
    return date.toLocaleDateString('zh-CN');
  }
}

// 查看笔记详情
function viewNote(noteId) {
  window.location.href = `/component/noteDetails.html?id=${noteId}`;
}

// 检查是否已关注
async function checkFollowStatus() {
  if (!currentUserId || currentUserId === viewingUserId) {
    return;
  }

  try {
    const response = await fetch(
      `${API_BASE}/user/follow/isFollowing?followerId=${currentUserId}&followingId=${viewingUserId}`,
      { headers: getAuthHeaders() }
    );

    if (response.ok) {
      const result = await response.json();
      if (result.code === 200) {
        updateFollowButton(result.data);
      }
    }
  } catch (error) {
    console.error('检查关注状态失败:', error);
  }
}

// 更新关注按钮状态
function updateFollowButton(isFollowing) {
  const followBtn = document.getElementById('followBtn');
  const followBtnText = document.getElementById('followBtnText');

  if (isFollowing) {
    followBtn.classList.add('following');
    followBtnText.textContent = '已关注';
  } else {
    followBtn.classList.remove('following');
    followBtnText.textContent = '关注';
  }
}

// 关注/取消关注
async function toggleFollow() {
  if (!currentUserId) {
    alert('请先登录');
    window.location.href = 'login.html';
    return;
  }

  if (currentUserId === viewingUserId) {
    alert('不能关注自己');
    return;
  }

  const followBtn = document.getElementById('followBtn');
  const isFollowing = followBtn.classList.contains('following');

  try {
    followBtn.disabled = true;

    if (isFollowing) {
      // 取消关注
      const response = await fetch(
        `${API_BASE}/user/follow/unfollow?followerId=${currentUserId}&followingId=${viewingUserId}`,
        {
          method: 'DELETE',
          headers: getAuthHeaders()
        }
      );

      if (response.ok) {
        const result = await response.json();
        if (result.code === 200) {
          updateFollowButton(false);
          await loadFollowStats();
        } else {
          throw new Error(result.msg || '取消关注失败');
        }
      }
    } else {
      // 关注
      const response = await fetch(
        `${API_BASE}/user/follow/follow?followerId=${currentUserId}&followingId=${viewingUserId}`,
        {
          method: 'POST',
          headers: getAuthHeaders()
        }
      );

      if (response.ok) {
        const result = await response.json();
        if (result.code === 200) {
          updateFollowButton(true);
          await loadFollowStats();
        } else {
          throw new Error(result.msg || '关注失败');
        }
      }
    }
  } catch (error) {
    console.error('关注操作失败:', error);
    alert(error.message);
  } finally {
    followBtn.disabled = false;
  }
}

// 排序功能
function initSorting() {
  document.addEventListener('click', (e) => {
    if (e.target.classList.contains('sort-btn')) {
      const sortGroup = e.target.closest('.sort-options');
      sortGroup.querySelectorAll('.sort-btn').forEach(btn => btn.classList.remove('active'));
      e.target.classList.add('active');

      const sortType = e.target.getAttribute('data-sort');
      sortNotes(sortType);
    }
  });
}

// 排序笔记
function sortNotes(sortType) {
  let sortedData = [...userNotesData];

  switch (sortType) {
    case 'latest':
      sortedData.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
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

  renderNotes(sortedData);
}

// 检查登录状态 - 新增函数
function checkLoginStatus() {
  try {
    const accessToken = localStorage.getItem('accessToken');
    const userStr = localStorage.getItem('user');
    const isLoggedIn = localStorage.getItem('isLoggedIn');

    console.log('检查登录状态:', {
      hasToken: !!accessToken,
      hasUser: !!userStr,
      isLoggedIn: isLoggedIn
    });

    if (accessToken && userStr && isLoggedIn === 'true') {
      try {
        const userData = JSON.parse(userStr);
        console.log('用户数据:', userData);
        return {
          isLoggedIn: true,
          userData: userData
        };
      } catch (parseError) {
        console.error('解析用户信息失败:', parseError);
        // 清除无效的token
        localStorage.removeItem('accessToken');
        localStorage.removeItem('user');
        localStorage.removeItem('isLoggedIn');
      }
    }
  } catch (error) {
    console.error('检查登录状态失败:', error);
  }

  return {
    isLoggedIn: false,
    userData: null
  };
}

// 导航栏相关函数 - 修复版本
function loadNavbarComponent() {
  return new Promise((resolve, reject) => {
    // 检查是否已经加载过导航栏
    if (document.querySelector('.header')) {
      console.log('导航栏已存在，跳过加载');
      initializeNavbar();
      resolve();
      return;
    }

    fetch('./component/navbar.html')
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
      })
      .then(html => {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');

        // 获取导航栏元素
        const navbar = doc.querySelector('#navbar-component');
        const styles = doc.querySelector('style');
        const script = doc.querySelector('script');

        if (styles) {
          // 避免重复添加样式
          if (!document.querySelector('#navbar-styles')) {
            styles.id = 'navbar-styles';
            document.head.appendChild(styles);
          }
        }

        if (navbar) {
          const navbarContainer = document.getElementById('navbar-container');
          navbarContainer.innerHTML = '';
          navbarContainer.appendChild(navbar.cloneNode(true));
        }

        if (script) {
          const newScript = document.createElement('script');
          newScript.textContent = script.textContent;
          document.body.appendChild(newScript);
        }

        // 等待导航栏初始化完成
        const waitForNavbar = setInterval(() => {
          if (window.navbarInstance) {
            clearInterval(waitForNavbar);
            console.log('导航栏初始化完成');

            // 强制设置正确的登录状态
            forceNavbarLoginStatus();

            resolve();
          }
        }, 50);

        // 超时处理
        setTimeout(() => {
          clearInterval(waitForNavbar);
          console.log('导航栏加载超时，继续执行');
          resolve();
        }, 3000);
      })
      .catch(error => {
        console.error('加载导航栏组件失败:', error);
        reject(error);
      });
  });
}

// 强制设置导航栏登录状态 - 新增函数
function forceNavbarLoginStatus() {
  const loginStatus = checkLoginStatus();

  if (loginStatus.isLoggedIn && window.navbarInstance) {
    console.log('强制设置导航栏为登录状态');
    window.navbarInstance.login({
      username: loginStatus.userData.username || loginStatus.userData.nickname || '用户',
      avatar: loginStatus.userData.avatar || '/assets/img/2.jpg'
    });
  } else if (window.navbarInstance) {
    console.log('设置导航栏为未登录状态');
    window.navbarInstance.logout();
  }
}

function initializeNavbar() {
  // 等待全局navbarInstance可用
  const checkNavbar = setInterval(() => {
    if (window.navbarInstance) {
      clearInterval(checkNavbar);

      // 强制刷新导航栏状态
      forceNavbarLoginStatus();

      console.log('导航栏初始化完成');
    }
  }, 50);

  // 设置超时
  setTimeout(() => {
    clearInterval(checkNavbar);
  }, 3000);
}

// 调整页面布局以适应导航栏
function adjustLayoutForNavbar() {
  const navbar = document.querySelector('.header');
  if (navbar) {
    const navbarHeight = navbar.offsetHeight;
    document.body.style.paddingTop = navbarHeight + 'px';
    console.log('调整布局，导航栏高度:', navbarHeight);
  } else {
    console.warn('未找到导航栏元素');
  }
}

// 页面初始化
document.addEventListener('DOMContentLoaded', async function () {
  console.log('User-Profile 页面开始初始化...');

  // 获取要查看的用户ID
  viewingUserId = getUserIdFromURL();
  if (!viewingUserId) {
    alert('用户ID不存在');
    window.history.back();
    return;
  }

  // 获取当前登录用户ID
  currentUserId = getCurrentUserId();

  // 检查登录状态
  const loginStatus = checkLoginStatus();
  console.log('当前登录状态:', loginStatus);

  // 如果是查看自己的主页,跳转到个人中心
  if (currentUserId && currentUserId.toString() === viewingUserId) {
    window.location.href = 'Profile-container.html';
    return;
  }

  try {
    // 首先加载导航栏
    console.log('开始加载导航栏...');
    await loadNavbarComponent();

    // 强制调整布局
    setTimeout(() => {
      adjustLayoutForNavbar();
    }, 100);

    // 监听窗口大小变化
    window.addEventListener('resize', adjustLayoutForNavbar);

    // 初始化排序功能
    initSorting();

    // 并行加载用户信息、关注统计和笔记
    console.log('开始加载用户数据...');
    await Promise.all([
      loadUserProfile(),
      loadFollowStats(),
      loadUserNotes()
    ]);

    // 检查关注状态
    if (currentUserId) {
      await checkFollowStatus();
    } else {
      // 未登录时隐藏关注按钮
      const followBtn = document.getElementById('followBtn');
      followBtn.style.display = 'none';
    }

    // 关注按钮点击事件
    const followBtn = document.getElementById('followBtn');
    followBtn.addEventListener('click', toggleFollow);

    console.log('User-Profile 页面初始化完成');
  } catch (error) {
    console.error('页面初始化失败:', error);
  }
});

// 监听存储变化（当其他页面登录时）
window.addEventListener('storage', (e) => {
  if (e.key === 'accessToken' || e.key === 'isLoggedIn' || e.key === 'user') {
    console.log('检测到登录状态变化，刷新导航栏');
    if (window.navbarInstance) {
      setTimeout(() => {
        forceNavbarLoginStatus();
      }, 100);
    }
  }
});