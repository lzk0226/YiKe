const quill = new Quill('#editor', {
  theme: 'snow',
  placeholder: '请输入笔记内容...',
  modules: {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      [{ 'header': 1 }, { 'header': 2 }],
      [{ 'list': 'ordered' }, { 'list': 'bullet' }],
      ['blockquote', 'code-block'],
      [{ 'color': [] }, { 'background': [] }],
      ['link', 'image'],
      [{ 'align': [] }],
      ['clean']
    ]
  }
});

// 全局状态
let subjects = [];
let noteTypes = [];
let isEditMode = false; // 是否为编辑模式
let editNoteId = null; // 编辑的笔记ID

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', async function () {
  // 检查登录状态
  if (!checkLoginStatus()) {
    alert('请先登录后再发布笔记');
    window.location.href = '../login.html';
    return;
  }

  // 加载基础数据
  await loadSubjects();
  await loadNoteTypes();

  // 检查是否为编辑模式
  const urlParams = new URLSearchParams(window.location.search);
  const noteId = urlParams.get('id');

  if (noteId) {
    isEditMode = true;
    editNoteId = noteId;
    await loadNoteForEdit(noteId);
  }
});

// 检查登录状态
function checkLoginStatus() {
  const accessToken = localStorage.getItem('accessToken');
  const isLoggedIn = localStorage.getItem('isLoggedIn');
  return !!(accessToken && isLoggedIn === 'true');
}

// 获取JWT Token
function getAuthToken() {
  return localStorage.getItem('accessToken');
}

// 加载学科分类
async function loadSubjects() {
  try {
    const response = await fetch('http://localhost:8080/user/subjects/list');
    const result = await response.json();

    if (result.code === 200 && result.rows) {
      subjects = result.rows;
      const subjectSelect = document.getElementById('noteSubject');
      subjectSelect.innerHTML = '<option value="">请选择分类</option>';

      subjects.forEach(subject => {
        const option = document.createElement('option');
        option.value = subject.id;
        option.textContent = subject.name;
        subjectSelect.appendChild(option);
      });

      console.log('学科分类加载成功');
    } else {
      console.error('获取学科分类失败:', result.msg);
      showMessage('获取学科分类失败', 'error');
    }
  } catch (error) {
    console.error('请求学科分类接口失败:', error);
    showMessage('网络错误，无法加载学科分类', 'error');
  }
}

// 加载笔记类型
async function loadNoteTypes() {
  try {
    const response = await fetch('http://localhost:8080/user/types/list');
    const result = await response.json();

    if (result.code === 200 && result.rows) {
      noteTypes = result.rows;
      const typeSelect = document.getElementById('noteType');
      typeSelect.innerHTML = '<option value="">请选择类型</option>';

      noteTypes.forEach(type => {
        const option = document.createElement('option');
        option.value = type.id;
        option.textContent = type.name;
        typeSelect.appendChild(option);
      });

      console.log('笔记类型加载成功');
    } else {
      console.error('获取笔记类型失败:', result.msg);
      showMessage('获取笔记类型失败', 'error');
    }
  } catch (error) {
    console.error('请求笔记类型接口失败:', error);
    showMessage('网络错误，无法加载笔记类型', 'error');
  }
}

// 加载笔记用于编辑
async function loadNoteForEdit(noteId) {
  try {
    const token = getAuthToken();
    const response = await fetch(`http://localhost:8080/user/notes/my/detail/${noteId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    const result = await response.json();

    if (result.code === 200 && result.data) {
      const note = result.data;

      // 填充表单数据
      document.getElementById('noteTitle').value = note.title || '';
      document.getElementById('noteDescription').value = note.description || '';

      // 设置学科和类型（禁用选择）
      const subjectSelect = document.getElementById('noteSubject');
      const typeSelect = document.getElementById('noteType');

      subjectSelect.value = note.subjectId;
      typeSelect.value = note.noteTypeId;

      // 禁用学科和类型选择框
      subjectSelect.disabled = true;
      typeSelect.disabled = true;

      // 填充富文本内容
      if (note.content) {
        quill.root.innerHTML = note.content;
      }

      // 更新页面标题和按钮文本
      document.querySelector('.header h2').textContent = '编辑笔记';
      document.querySelector('.header p').textContent = '修改你的学习心得，完善笔记内容';
      document.getElementById('submitBtn').textContent = '确认修改';

      showMessage('笔记加载成功，可以开始编辑', 'success');
    } else {
      if (result.code === 401) {
        showMessage('登录已过期，请重新登录', 'error');
        setTimeout(() => {
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          localStorage.removeItem('user');
          localStorage.removeItem('isLoggedIn');
          window.location.href = '../login.html';
        }, 2000);
      } else if (result.code === 403) {
        showMessage('无权编辑此笔记', 'error');
        setTimeout(() => {
          window.location.href = '../noteSquare.html';
        }, 2000);
      } else {
        showMessage(result.msg || '加载笔记失败', 'error');
        setTimeout(() => {
          window.location.href = '../noteSquare.html';
        }, 2000);
      }
    }
  } catch (error) {
    console.error('加载笔记失败:', error);
    showMessage('网络错误，无法加载笔记', 'error');
    setTimeout(() => {
      window.location.href = '../noteSquare.html';
    }, 2000);
  }
}

// 显示消息提示
function showMessage(message, type = 'info') {
  const messageDiv = document.createElement('div');
  messageDiv.className = `message ${type}`;
  messageDiv.textContent = message;

  messageDiv.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 12px 20px;
    border-radius: 4px;
    color: white;
    font-size: 14px;
    z-index: 9999;
    min-width: 200px;
    text-align: center;
    transition: all 0.3s ease;
  `;

  switch (type) {
    case 'success':
      messageDiv.style.backgroundColor = '#67c23a';
      break;
    case 'error':
      messageDiv.style.backgroundColor = '#f56c6c';
      break;
    case 'warning':
      messageDiv.style.backgroundColor = '#e6a23c';
      break;
    default:
      messageDiv.style.backgroundColor = '#409eff';
  }

  document.body.appendChild(messageDiv);

  setTimeout(() => {
    if (messageDiv.parentNode) {
      messageDiv.parentNode.removeChild(messageDiv);
    }
  }, 3000);
}

// 验证表单数据
function validateForm() {
  const title = document.getElementById('noteTitle').value.trim();
  const subject = document.getElementById('noteSubject').value;
  const type = document.getElementById('noteType').value;
  const content = quill.root.innerHTML.trim();

  if (!title) {
    showMessage('请输入标题', 'error');
    return false;
  }

  if (title.length > 100) {
    showMessage('标题不能超过100个字符', 'error');
    return false;
  }

  if (!subject) {
    showMessage('请选择学科分类', 'error');
    return false;
  }

  if (!type) {
    showMessage('请选择笔记类型', 'error');
    return false;
  }

  if (!content || content === '<p><br></p>') {
    showMessage('请输入笔记内容', 'error');
    return false;
  }

  return true;
}

// 构建笔记数据对象
function buildNoteData() {
  const title = document.getElementById('noteTitle').value.trim();
  const subjectId = parseInt(document.getElementById('noteSubject').value);
  const noteTypeId = parseInt(document.getElementById('noteType').value);
  const content = quill.root.innerHTML.trim();
  const userDescription = document.getElementById('noteDescription').value.trim();

  let description;
  if (userDescription) {
    description = userDescription;
  } else {
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = content;
    description = tempDiv.textContent.trim().substring(0, 200);
  }

  return {
    title: title,
    content: content,
    description: description,
    subjectId: subjectId,
    noteTypeId: noteTypeId
  };
}

// 发布或更新笔记
async function submitNote() {
  if (!validateForm()) {
    return;
  }

  if (!checkLoginStatus()) {
    showMessage('登录状态已过期，请重新登录', 'error');
    setTimeout(() => {
      window.location.href = '../login.html';
    }, 2000);
    return;
  }

  const submitBtn = document.getElementById('submitBtn');
  const originalText = submitBtn.textContent;

  try {
    submitBtn.disabled = true;
    submitBtn.textContent = isEditMode ? '修改中...' : '发布中...';

    const noteData = buildNoteData();
    const token = getAuthToken();

    // 根据模式选择不同的API端点和方法
    const url = isEditMode
      ? `http://localhost:8080/user/notes/edit/${editNoteId}`
      : 'http://localhost:8080/user/notes/publish';

    const method = isEditMode ? 'PUT' : 'POST';

    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(noteData)
    });

    const result = await response.json();

    if (result.code === 200) {
      // 成功后直接返回上一页
      window.removeEventListener('beforeunload', beforeUnloadHandler);
      setTimeout(() => {
        history.back(); // 返回上一页
      }, 500); // 给点延迟，避免按钮状态还没恢复
    } else {
      if (result.code === 401) {
        showMessage('登录已过期，请重新登录', 'error');
        setTimeout(() => {
          localStorage.removeItem('accessToken');
          localStorage.removeItem('refreshToken');
          localStorage.removeItem('user');
          localStorage.removeItem('isLoggedIn');
          window.location.href = '../login.html';
        }, 2000);
      } else if (result.code === 400) {
        showMessage(result.msg || '请检查输入信息', 'error');
      } else if (result.code === 403) {
        showMessage('没有权限执行此操作', 'error');
      } else {
        showMessage(result.msg || (isEditMode ? '修改失败，请稍后重试' : '发布失败，请稍后重试'), 'error');
      }
    }


  } catch (error) {
    console.error('提交笔记失败:', error);
    if (error.name === 'TypeError' && error.message.includes('fetch')) {
      showMessage('网络连接异常，请检查网络后重试', 'error');
    } else {
      showMessage(isEditMode ? '修改失败，请稍后重试' : '发布失败，请稍后重试', 'error');
    }
  } finally {
    submitBtn.disabled = false;
    submitBtn.textContent = originalText;
  }
}

// 重置表单
function resetForm() {
  if (isEditMode) {
    // 编辑模式下不允许重置，而是返回上一页
    if (confirm('确定要放弃修改吗？')) {
      window.history.back();
    }
    return;
  }

  document.getElementById('noteTitle').value = '';
  document.getElementById('noteSubject').value = '';
  document.getElementById('noteType').value = '';
  document.getElementById('noteDescription').value = '';
  quill.setContents([]);
  showMessage('表单已重置', 'info');
}

// 绑定事件监听器
document.getElementById('resetBtn').addEventListener('click', resetForm);
document.getElementById('submitBtn').addEventListener('click', submitNote);

// 编辑模式下修改重置按钮文本
if (isEditMode) {
  document.getElementById('resetBtn').textContent = '取消编辑';
}

// 支持键盘快捷键
document.addEventListener('keydown', function (e) {
  // Ctrl+Enter 快速提交
  if (e.ctrlKey && e.key === 'Enter') {
    e.preventDefault();
    submitNote();
  }

  // Ctrl+R 重置表单或取消编辑
  if (e.ctrlKey && e.key === 'r') {
    e.preventDefault();
    if (isEditMode) {
      if (confirm('确定要放弃修改吗？')) {
        window.history.back();
      }
    } else {
      if (confirm('确定要重置表单吗？')) {
        resetForm();
      }
    }
  }
});

// 页面离开前提醒
function beforeUnloadHandler(e) {
  const title = document.getElementById('noteTitle').value.trim();
  const content = quill.root.innerHTML.trim();

  if (title || (content && content !== '<p><br></p>')) {
    e.preventDefault();
    e.returnValue = '';
    return '';
  }
}

window.addEventListener('beforeunload', beforeUnloadHandler);
