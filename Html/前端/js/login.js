// API配置
const API_BASE_URL = 'http://localhost:8080/user';

// DOM元素
const cont = document.querySelector('.cont');
const imgBtn = document.querySelector('.img__btn');

// 登录表单元素
const loginEmail = document.getElementById('login-email');
const loginPassword = document.getElementById('login-password');
const loginBtn = document.getElementById('login-btn');

// 注册表单元素
const regUsername = document.getElementById('reg-username');
const regEmail = document.getElementById('reg-email');
const regPassword = document.getElementById('reg-password');
const confirmPassword = document.getElementById('confirm-password');
const registerBtn = document.getElementById('register-btn');

// 工具函数：显示消息
const showMessage = (message, type = 'info') => {
  const messageDiv = document.createElement('div');
  messageDiv.className = `message message-${type}`;
  messageDiv.textContent = message;
  document.body.appendChild(messageDiv);

  setTimeout(() => messageDiv.style.opacity = '1', 100);

  setTimeout(() => {
    messageDiv.style.opacity = '0';
    setTimeout(() => document.body.removeChild(messageDiv), 300);
  }, 3000);
};

// 设置按钮加载状态
const setButtonLoading = (button, isLoading, originalText) => {
  if (isLoading) {
    button.disabled = true;
    button.textContent = '加载中...';
  } else {
    button.disabled = false;
    button.textContent = originalText;
  }
};

// 邮箱验证
const isValidEmail = email => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

// API调用：登录
const loginUser = async (email, password) => {
  try {
    const url = `${API_BASE_URL}/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`;
    const res = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' }
    });
    return await res.json();
  } catch (e) {
    console.error(e);
    return { code: 500, message: '网络错误' };
  }
};

// API调用：注册
const registerUser = async (email, password, username) => {
  try {
    const userData = {
      email,
      password,
      username,
      nickname: username,
      avatar: '',
      school: '',
      major: ''
    };
    const res = await fetch(`${API_BASE_URL}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userData)
    });
    return await res.json();
  } catch (e) {
    console.error(e);
    return { code: 500, message: '网络错误' };
  }
};

// 登录处理
const handleLogin = async () => {
  const email = loginEmail.value.trim();
  const password = loginPassword.value.trim();

  if (!email || !password) {
    showMessage('请填写邮箱和密码', 'error');
    return;
  }

  if (!isValidEmail(email)) {
    showMessage('请输入有效邮箱', 'error');
    return;
  }

  setButtonLoading(loginBtn, true, '登录');

  try {
    const result = await loginUser(email, password);
    if (result.code === 200 && result.data) {
      // 保存 token 和用户信息
      localStorage.setItem('accessToken', result.data.accessToken);
      localStorage.setItem('refreshToken', result.data.refreshToken);
      localStorage.setItem('expiresIn', result.data.expiresIn);
      localStorage.setItem('user', JSON.stringify(result.data.user));
      localStorage.setItem('isLoggedIn', 'true');

      showMessage('登录成功！', 'success');

      setTimeout(() => {
        window.location.href = 'noteSquare.html';
      }, 500);
    } else {
      showMessage(result.message || '登录失败', 'error');
    }
  } finally {
    setButtonLoading(loginBtn, false, '登录');
  }
};

// 注册处理
const handleRegister = async () => {
  const email = regEmail.value.trim();
  const password = regPassword.value.trim();
  const username = regUsername.value.trim();
  const confirmPass = confirmPassword.value.trim();

  if (!email || !password || !confirmPass) {
    showMessage('请完整填写注册信息', 'error');
    return;
  }

  if (!isValidEmail(email)) {
    showMessage('请输入有效邮箱', 'error');
    return;
  }

  if (password.length < 6) {
    showMessage('密码至少6位', 'error');
    return;
  }

  if (password !== confirmPass) {
    showMessage('两次密码不一致', 'error');
    return;
  }

  setButtonLoading(registerBtn, true, '注册');

  try {
    const result = await registerUser(email, password, username || email.split('@')[0]);
    if (result.code === 200) {
      showMessage('注册成功，请登录', 'success');

      // 清空注册表单
      regEmail.value = '';
      regPassword.value = '';
      regUsername.value = '';
      confirmPassword.value = '';

      setTimeout(() => {
        // 切换到登录表单
        cont.classList.remove('s--signup');
        // 预填邮箱
        loginEmail.value = email;
        loginPassword.focus();
      }, 1000);
    } else {
      showMessage(result.message || '注册失败', 'error');
    }
  } finally {
    setButtonLoading(registerBtn, false, '注册');
  }
};

// 表单切换动画
imgBtn.addEventListener('click', function () {
  cont.classList.toggle('s--signup');
});

// 按钮点击事件
loginBtn.addEventListener('click', handleLogin);
registerBtn.addEventListener('click', handleRegister);

// 回车提交
loginEmail.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') handleLogin();
});
loginPassword.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') handleLogin();
});

regEmail.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') handleRegister();
});
regPassword.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') handleRegister();
});
confirmPassword.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') handleRegister();
});

// 页面加载时检查登录状态
document.addEventListener('DOMContentLoaded', () => {
  const accessToken = localStorage.getItem('accessToken');
  const user = localStorage.getItem('user');
  if (accessToken && user) {
    console.log('已登录用户:', JSON.parse(user));
    window.location.href = 'noteSquare.html';
  }
});