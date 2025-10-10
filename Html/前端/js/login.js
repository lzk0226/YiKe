const body = document.querySelector("body");
const modal = document.querySelector(".modal");
const modalButton = document.querySelector(".modal-button");
const closeButton = document.querySelector(".close-button");
const loginForm = document.querySelector(".login-form");
const registerForm = document.querySelector(".register-form");
const toRegister = document.getElementById("toRegister");
const toLogin = document.getElementById("toLogin");

// 登录表单元素
const loginEmailInput = document.getElementById("email");
const loginPasswordInput = document.getElementById("password");
const loginButton = document.querySelector(".login-form .input-button");

// 注册表单元素
const regEmailInput = document.getElementById("reg-email");
const regPasswordInput = document.getElementById("reg-password");
const regUsernameInput = document.getElementById("reg-username");
const confirmPasswordInput = document.getElementById("confirm-password");
const registerButton = document.querySelector(".register-form .input-button");

let autoOpened = false; // 避免重复触发

const API_BASE_URL = 'http://localhost:8080/user';

// 工具函数：显示消息
const showMessage = (message, type = 'info') => {
  const messageDiv = document.createElement('div');
  messageDiv.className = `message message-${type}`;
  messageDiv.textContent = message;
  messageDiv.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 12px 20px;
    border-radius: 4px;
    color: white;
    font-size: 14px;
    z-index: 10000;
    opacity: 0;
    transition: opacity 0.3s ease;
    ${type === 'success' ? 'background: #4CAF50;' : ''}
    ${type === 'error' ? 'background: #f44336;' : ''}
    ${type === 'info' ? 'background: #2196F3;' : ''}
  `;
  document.body.appendChild(messageDiv);
  setTimeout(() => messageDiv.style.opacity = '1', 100);
  setTimeout(() => {
    messageDiv.style.opacity = '0';
    setTimeout(() => document.body.removeChild(messageDiv), 300);
  }, 3000);
};

const setButtonLoading = (button, isLoading) => {
  if (isLoading) {
    button.disabled = true;
    button.textContent = '加载中...';
    button.style.opacity = '0.7';
  } else {
    button.disabled = false;
    button.textContent = button === loginButton ? '登录' : '注册';
    button.style.opacity = '1';
  }
};


const isValidEmail = email => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

// API调用：登录
const loginUser = async (email, password) => {
  try {
    const url = `${API_BASE_URL}/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`;
    const res = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' } });
    return await res.json();
  } catch (e) {
    console.error(e);
    return { code: 500, message: '网络错误' };
  }
};

// API调用：注册
const registerUser = async (email, password, username) => {
  try {
    const userData = { email, password, username, nickname: username, avatar: '', school: '', major: '' };
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
  const email = loginEmailInput.value.trim();
  const password = loginPasswordInput.value.trim();
  if (!email || !password) return showMessage('请填写邮箱和密码', 'error');
  if (!isValidEmail(email)) return showMessage('请输入有效邮箱', 'error');

  setButtonLoading(loginButton, true);

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
        closeModal();
        window.location.href = 'noteSquare.html';
      }, 500); // 0.5秒即可
    }
    else {
      showMessage(result.message || '登录失败', 'error');
    }
  } finally {
    setButtonLoading(loginButton, false);
  }
};

// 注册处理
const handleRegister = async () => {
  const email = regEmailInput.value.trim();
  const password = regPasswordInput.value.trim();
  const username = regUsernameInput.value.trim();
  const confirmPassword = confirmPasswordInput.value.trim();
  if (!email || !password || !confirmPassword) return showMessage('请完整填写注册信息', 'error');
  if (!isValidEmail(email)) return showMessage('请输入有效邮箱', 'error');
  if (password.length < 6) return showMessage('密码至少6位', 'error');
  if (password !== confirmPassword) return showMessage('两次密码不一致', 'error');

  setButtonLoading(registerButton, true);

  try {
    const result = await registerUser(email, password, username || email.split('@')[0]);
    if (result.code === 200) {
      showMessage('注册成功，请登录', 'success');
      regEmailInput.value = regPasswordInput.value = regUsernameInput.value = confirmPasswordInput.value = '';
      setTimeout(() => {
        registerForm.style.display = 'none';
        loginForm.style.display = 'block';
        loginEmailInput.value = email;
        loginPasswordInput.focus();
      }, 1000);
    } else {
      showMessage(result.message || '注册失败', 'error');
    }
  } finally {
    setButtonLoading(registerButton, false);
  }
};

// 模态框操作
const openModal = () => { modal.classList.add("is-open"); body.style.overflow = "hidden"; };
const closeModal = () => { modal.classList.remove("is-open"); body.style.overflow = "initial"; };

// 事件监听
modalButton.addEventListener("click", openModal);
closeButton.addEventListener("click", closeModal);
loginButton.addEventListener("click", handleLogin);
registerButton.addEventListener("click", handleRegister);
toRegister.addEventListener("click", e => { e.preventDefault(); loginForm.style.display = "none"; registerForm.style.display = "block"; });
toLogin.addEventListener("click", e => { e.preventDefault(); registerForm.style.display = "none"; loginForm.style.display = "block"; });

// Esc关闭
document.onkeydown = evt => { if ((evt || window.event).keyCode === 27) closeModal(); };
// 回车提交
loginForm.addEventListener("keypress", e => { if (e.key === 'Enter') handleLogin(); });
registerForm.addEventListener("keypress", e => { if (e.key === 'Enter') handleRegister(); });

// 页面滚动到底部自动展开登录
window.addEventListener("scroll", () => {
  const scrollTop = window.scrollY || document.documentElement.scrollTop;
  const windowHeight = window.innerHeight;
  const docHeight = document.documentElement.scrollHeight;
  if (!autoOpened && scrollTop + windowHeight >= docHeight - 10) {
    openModal();
    autoOpened = true;
  }
});

// 页面加载时检查 token 并自动跳转
document.addEventListener('DOMContentLoaded', () => {
  const accessToken = localStorage.getItem('accessToken');
  const user = localStorage.getItem('user');
  if (accessToken && user) {
    console.log('已登录用户:', JSON.parse(user));
    window.location.href = 'noteSquare.html';
  }
});
