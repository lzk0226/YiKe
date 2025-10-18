// 动态加载导航栏组件
function loadNavbarComponent() {
  fetch('./component/navbar.html')
    .then(response => response.text())
    .then(html => {
      // 提取导航栏 HTML部分
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, 'text/html');
      const navbar = doc.querySelector('#navbar-component');
      const styles = doc.querySelector('style');
      const script = doc.querySelector('script');

      // 插入样式
      if (styles) {
        document.head.appendChild(styles);
      }

      // 插入导航栏 HTML
      if (navbar) {
        document.getElementById('navbar-container').appendChild(navbar);
      }

      // 执行脚本
      if (script) {
        const newScript = document.createElement('script');
        newScript.textContent = script.textContent;
        document.body.appendChild(newScript);
      }

      // 初始化导航栏
      initializeNavbar();

      // 导航栏加载完成后,检查登录状态并控制FAB按钮
      setTimeout(() => {
        updateFabButtonVisibility();
      }, 200);
    })
    .catch(error => {
      console.error('加载导航栏组件失败:', error);
    });
}

// 初始化导航栏
function initializeNavbar() {
  if (window.NavbarComponent) {
    const navbar = new NavbarComponent({
      isLoggedIn: false,
      onNavigate: handleNavigation,
      onAuth: handleAuth,
      onUpload: handleUpload,
      onUserAction: handleUserAction
    });

    // 设置当前页面为笔记广场
    navbar.setActivePage('notes');
  }
}

// 检查登录状态并更新FAB按钮显示状态
function updateFabButtonVisibility() {
  const fabButton = document.querySelector('.fab');
  if (!fabButton) return;

  try {
    const accessToken = localStorage.getItem('accessToken');
    const userStr = localStorage.getItem('user');
    const isLoggedIn = localStorage.getItem('isLoggedIn');

    // 检查是否已登录
    const userLoggedIn = !!(accessToken && userStr && isLoggedIn === 'true');

    if (userLoggedIn) {
      // 已登录:显示FAB按钮
      fabButton.style.display = 'flex';
      fabButton.style.opacity = '1';
      fabButton.style.visibility = 'visible';
      console.log('用户已登录,显示发布按钮');
    } else {
      // 未登录:隐藏FAB按钮
      fabButton.style.display = 'none';
      fabButton.style.opacity = '0';
      fabButton.style.visibility = 'hidden';
      console.log('用户未登录,隐藏发布按钮');
    }
  } catch (error) {
    console.error('检查登录状态时出错:', error);
    // 出错时隐藏按钮
    fabButton.style.display = 'none';
  }
}

// 处理导航
function handleNavigation(section, href) {
  console.log('导航到:', section);

  if (section === 'home') {
    window.location.href = 'index.html';
  } else if (section === 'notes') {
    return;
  } else {
    alert(`导航到${section}页面`);
  }
}

// 处理认证
function handleAuth(action) {
  if (action === 'login') {
    alert('打开登录弹窗');
  } else if (action === 'register') {
    alert('打开注册弹窗');
  }
}

// 处理上传
function handleUpload() {
  alert('打开上传笔记页面');
}

// 处理用户操作
function handleUserAction(action) {
  console.log('用户操作:', action);

  switch (action) {
    case 'profile':
      alert('打开个人中心');
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
      if (confirm('确定要退出登录吗?')) {
        // 清除登录信息
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('expiresIn');

        alert('已退出登录');

        // 更新FAB按钮显示状态
        updateFabButtonVisibility();

        // 刷新导航栏状态
        if (window.navbarInstance) {
          window.navbarInstance.refreshLoginStatus();
        }
      }
      break;
  }
}

// 查看笔记详情
function viewNoteDetail(noteId) {
  window.location.href = `component/noteDetails.html?id=${noteId}`;
}

// 全局状态管理
const appState = {
  currentPage: 1,
  pageSize: 6,
  sortType: 'latest',
  selectedSubjectId: null,
  selectedNoteTypeId: null // 改为单选,只存储一个ID
};

// 获取学科分类数据
async function fetchSubjects() {
  try {
    const response = await fetch('http://localhost:8080/user/subjects/list');
    const result = await response.json();

    if (result.code === 200 && result.rows) {
      const subjects = result.rows;
      const container = document.getElementById('subjectTagsContainer');

      // 清除原有除了"全部"的标签
      container.querySelectorAll('.tag:not(.active)').forEach(tag => tag.remove());

      subjects.forEach(subject => {
        const div = document.createElement('div');
        div.className = 'tag';
        div.textContent = subject.name;
        div.dataset.id = subject.id;
        container.appendChild(div);
      });

      // 重新绑定点击事件
      bindSubjectTagEvents();
    } else {
      console.error('获取学科分类失败:', result.msg);
    }
  } catch (error) {
    console.error('请求学科分类接口失败:', error);
  }
}

// 绑定学科标签点击事件
function bindSubjectTagEvents() {
  const subjectTags = document.querySelectorAll('.subject-tags .tag');
  subjectTags.forEach(tag => {
    tag.addEventListener('click', function () {
      subjectTags.forEach(t => t.classList.remove('active'));
      this.classList.add('active');

      // 更新全局状态
      appState.selectedSubjectId = this.dataset.id ? parseInt(this.dataset.id) : null;
      appState.currentPage = 1;

      // 重新加载数据
      renderNotes();
    });
  });
}

// 获取笔记类型数据
async function fetchNoteTypes() {
  try {
    const response = await fetch('http://localhost:8080/user/types/list');
    const result = await response.json();

    if (result.code === 200 && result.rows) {
      const types = result.rows;
      const container = document.getElementById('noteTypeContainer');

      container.innerHTML = '';

      types.forEach(type => {
        const label = document.createElement('label');
        label.className = 'filter-option';

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.checked = false; // 默认不选中
        checkbox.dataset.id = type.id;

        const span = document.createElement('span');
        span.textContent = type.name;

        label.appendChild(checkbox);
        label.appendChild(span);
        container.appendChild(label);
      });

      // 初始化选中状态为null(不选择任何类型)
      appState.selectedNoteTypeId = null;
      bindNoteTypeEvents();
    } else {
      console.error('获取笔记类型失败:', result.msg);
    }
  } catch (error) {
    console.error('请求笔记类型接口失败:', error);
  }
}

// 绑定笔记类型事件(改为单选模式)
function bindNoteTypeEvents() {
  const typeCheckboxes = document.querySelectorAll('#noteTypeContainer input[type="checkbox"]');

  typeCheckboxes.forEach(cb => {
    cb.addEventListener('change', function () {
      const currentId = parseInt(this.dataset.id);

      if (this.checked) {
        // 如果当前被勾选,取消其他所有复选框
        typeCheckboxes.forEach(otherCb => {
          if (otherCb !== this) {
            otherCb.checked = false;
          }
        });

        // 更新全局状态为当前选中的ID
        appState.selectedNoteTypeId = currentId;
      } else {
        // 如果当前被取消勾选,设置为null
        appState.selectedNoteTypeId = null;
      }

      appState.currentPage = 1; // 重置到第一页

      // 重新加载数据
      renderNotes();
    });
  });
}

// 加载卡片模板
async function loadNoteCardTemplate() {
  const response = await fetch('./component/noteCard.html');
  return await response.text();
}

// 获取笔记数据(支持筛选条件)
async function fetchNotes(page = 1, pageSize = 6, sortType = 'latest', subjectId = null, noteTypeId = null) {
  let url = '';

  // 根据排序类型选择对应的后端接口
  switch (sortType) {
    case 'latest':
      url = `http://localhost:8080/user/notes/cards/latest`;
      break;
    case 'most-read':
      url = `http://localhost:8080/user/notes/cards/most-read`;
      break;
    case 'top-rated':
      url = `http://localhost:8080/user/notes/cards/top-rated`;
      break;
    case 'most-favorited':
      url = `http://localhost:8080/user/notes/cards/most-favorited`;
      break;
    default:
      url = `http://localhost:8080/user/notes/cards/latest`;
  }

  // 构建查询参数
  const params = new URLSearchParams({
    page: page.toString(),
    pageSize: pageSize.toString()
  });

  if (subjectId) {
    params.append('subjectId', subjectId.toString());
  }

  if (noteTypeId) {
    params.append('noteTypeId', noteTypeId.toString());
  }

  const fullUrl = `${url}?${params.toString()}`;

  try {
    const response = await fetch(fullUrl);
    const result = await response.json();

    if (result.code === 200) {
      const notes = result.data.list || result.data;
      return {
        notes: notes.map(note => ({
          id: note.id,
          title: note.title,
          author: note.author?.nickname || 'Anonymous',
          authorInitial: note.author?.nickname ? note.author.nickname.charAt(0) : 'A',
          subject: note.subject?.name || '未知',
          description: note.description || '',
          likes: note.likes || 0,
          views: note.views || 0,
          rating: note.rating || 0,
          icon: '📝',
          noteTypeId: note.noteTypeId,
          noteTypeName: note.noteType?.name || ''
        })),
        total: result.data.total || 0,
        pageNum: result.data.pageNum || page,
        pageSize: result.data.pageSize || pageSize
      };
    } else {
      console.error('获取笔记失败:', result.msg);
      return { notes: [], total: 0 };
    }
  } catch (error) {
    console.error('请求笔记卡片接口失败:', error);
    return { notes: [], total: 0 };
  }
}

// 渲染笔记卡片
async function renderNotes() {
  const notesGrid = document.getElementById('notesGrid');
  const template = await loadNoteCardTemplate();

  // 使用单个笔记类型ID
  const noteTypeId = appState.selectedNoteTypeId;

  const { notes, total, pageSize } = await fetchNotes(
    appState.currentPage,
    appState.pageSize,
    appState.sortType,
    appState.selectedSubjectId,
    noteTypeId
  );

  notesGrid.innerHTML = notes.map(note => {
    let card = template;
    card = card.replaceAll('{{id}}', note.id)
      .replaceAll('{{title}}', note.title)
      .replaceAll('{{author}}', note.author)
      .replaceAll('{{authorInitial}}', note.authorInitial)
      .replaceAll('{{subject}}', note.subject)
      .replaceAll('{{description}}', note.description)
      .replaceAll('{{likes}}', note.likes)
      .replaceAll('{{views}}', note.views)
      .replaceAll('{{rating}}', note.rating)
      .replaceAll('{{icon}}', note.icon)
      .replaceAll('{{noteTypeId}}', note.noteTypeId)
      .replaceAll('{{noteTypeName}}', note.noteTypeName);
    return card;
  }).join('');

  // 渲染分页
  renderPagination(total, pageSize);
}

// 渲染分页组件
function renderPagination(total, pageSize) {
  const pagination = document.querySelector('.pagination');
  const totalPages = Math.ceil(total / pageSize);
  const currentPage = appState.currentPage;

  let html = '';

  // 上一页
  html += `<button class="page-btn" ${currentPage === 1 ? 'disabled' : ''} data-page="${currentPage - 1}">‹</button>`;

  // 显示页码按钮(简化版本)
  const maxVisiblePages = 5;
  let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
  let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

  // 调整起始页码
  if (endPage - startPage + 1 < maxVisiblePages) {
    startPage = Math.max(1, endPage - maxVisiblePages + 1);
  }

  // 第一页和省略号
  if (startPage > 1) {
    html += `<button class="page-btn" data-page="1">1</button>`;
    if (startPage > 2) {
      html += `<button class="page-btn disabled">...</button>`;
    }
  }

  // 页码按钮
  for (let i = startPage; i <= endPage; i++) {
    html += `<button class="page-btn ${i === currentPage ? 'active' : ''}" data-page="${i}">${i}</button>`;
  }

  // 最后一页和省略号
  if (endPage < totalPages) {
    if (endPage < totalPages - 1) {
      html += `<button class="page-btn disabled">...</button>`;
    }
    html += `<button class="page-btn" data-page="${totalPages}">${totalPages}</button>`;
  }

  // 下一页
  html += `<button class="page-btn" ${currentPage === totalPages ? 'disabled' : ''} data-page="${currentPage + 1}">›</button>`;

  pagination.innerHTML = html;

  // 绑定点击事件
  pagination.querySelectorAll('.page-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const page = parseInt(btn.dataset.page, 10);
      if (!isNaN(page)) {
        appState.currentPage = page;
        renderNotes();
      }
    });
  });
}

// 搜索功能(简化版本,实际应该调用后端搜索接口)
function handleSearch(searchTerm) {
  // 这里应该调用后端搜索接口
  console.log('搜索关键词:', searchTerm);
  // 暂时保持前端搜索,后续可以改为调用后端搜索接口
  const noteCards = document.querySelectorAll('.note-card');
  noteCards.forEach(card => {
    const title = card.querySelector('.note-title')?.textContent?.toLowerCase() || '';
    const description = card.querySelector('.note-description')?.textContent?.toLowerCase() || '';
    const subject = card.querySelector('.note-subject')?.textContent?.toLowerCase() || '';

    if (title.includes(searchTerm) || description.includes(searchTerm) || subject.includes(searchTerm)) {
      card.style.display = 'block';
    } else {
      card.style.display = 'none';
    }
  });
}

// 页面初始化
document.addEventListener('DOMContentLoaded', function () {
  // 加载基础数据
  fetchSubjects();
  fetchNoteTypes();
  loadNavbarComponent();

  // 初始化页面
  renderNotes();

  // 搜索功能
  const searchInput = document.querySelector('.search-input');
  searchInput.addEventListener('input', function (e) {
    const searchTerm = e.target.value.toLowerCase();
    handleSearch(searchTerm);
  });

  // 排序功能
  const sortBtns = document.querySelectorAll('.sort-btn');
  sortBtns.forEach(btn => {
    btn.addEventListener('click', function () {
      sortBtns.forEach(b => b.classList.remove('active'));
      this.classList.add('active');

      const sortTypeMap = {
        '最新发布': 'latest',
        '最多阅读': 'most-read',
        '评分最高': 'top-rated',
        '最多收藏': 'most-favorited'
      };

      const sortType = sortTypeMap[this.textContent];
      appState.sortType = sortType;
      appState.currentPage = 1;

      renderNotes();
    });
  });

  // FAB 点击事件 - 检查登录状态
  const fabButton = document.querySelector('.fab');
  if (fabButton) {
    fabButton.addEventListener('click', function () {
      // 再次检查登录状态以确保安全
      const accessToken = localStorage.getItem('accessToken');
      const isLoggedIn = localStorage.getItem('isLoggedIn');

      if (accessToken && isLoggedIn === 'true') {
        // 已登录,跳转到新建笔记页面
        window.location.href = '/component/newNote.html';
      } else {
        // 未登录,提示用户登录
        alert('请先登录后再发布笔记');
        // 可以跳转到登录页面
        // window.location.href = 'login.html';
      }
    });
  }

  // 监听localStorage变化,当登录状态改变时更新FAB按钮
  window.addEventListener('storage', function (e) {
    if (e.key === 'accessToken' || e.key === 'isLoggedIn' || e.key === 'user') {
      updateFabButtonVisibility();
    }
  });

  // 监听页面焦点重新获得(从其他标签页回来时)
  window.addEventListener('focus', function () {
    updateFabButtonVisibility();
  });
});