
// 动态加载导航栏组件
function loadNavbarComponent() {
  fetch('./component/navbar.html')
    .then(response => response.text())
    .then(html => {
      // 提取导航栏HTML部分
      const parser = new DOMParser();
      const doc = parser.parseFromString(html, 'text/html');
      const navbar = doc.querySelector('#navbar-component');
      const styles = doc.querySelector('style');
      const script = doc.querySelector('script');

      // 插入样式
      if (styles) {
        document.head.appendChild(styles);
      }

      // 插入导航栏HTML
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
    })
    .catch(error => {
      console.error('加载导航栏组件失败:', error);
      // 如果加载失败，显示简单的导航栏
      showFallbackNavbar();
    });
}

// 备用导航栏（组件加载失败时使用）
function showFallbackNavbar() {
  const fallbackNavbar = `
                <header style="background: rgba(255, 255, 255, 0.95); position: fixed; top: 0; left: 0; right: 0; z-index: 1000; padding: 1rem 2rem;">
                    <nav style="display: flex; justify-content: space-between; align-items: center;">
                        <a href="#" style="font-size: 1.8rem; font-weight: bold; color: #667eea; text-decoration: none;">📚 忆课</a>
                        <div>
                            <button onclick="showModal('loginModal')" style="margin-right: 1rem; padding: 0.5rem 1rem; border: 2px solid #667eea; background: transparent; color: #667eea; border-radius: 25px; cursor: pointer;">登录</button>
                            <button onclick="showModal('registerModal')" style="padding: 0.5rem 1rem; background: linear-gradient(45deg, #667eea, #764ba2); color: white; border: none; border-radius: 25px; cursor: pointer;">注册</button>
                        </div>
                    </nav>
                </header>
            `;
  document.getElementById('navbar-container').innerHTML = fallbackNavbar;
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

    // 设置当前页面为首页
    navbar.setActivePage('home');
  }
}

// 处理导航
function handleNavigation(section, href) {
  console.log('导航到:', section);

  // 平滑滚动到对应区域
  const targetSection = document.getElementById(section);
  if (targetSection) {
    targetSection.scrollIntoView({
      behavior: 'smooth',
      block: 'start'
    });
  }

  // 更新浏览器历史记录
  history.pushState(null, null, `#${section}`);
}

// 处理认证
function handleAuth(action) {
  if (action === 'login') {
    showModal('loginModal');
  } else if (action === 'register') {
    showModal('registerModal');
  }
}

// 处理上传
function handleUpload() {
  alert('打开上传笔记页面');
  // 这里可以跳转到上传页面或打开上传对话框
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
      if (confirm('确定要退出登录吗？')) {
        // 执行登出操作
        alert('已退出登录');
      }
      break;
  }
}

// 显示模态框
// function showModal(modalId) {
//     const modal = document.getElementById(modalId);
//     if (modal) {
//         modal.style.display = 'block';
//     }
// }

// // 隐藏模态框
// function hideModal(modalId) {
//     const modal = document.getElementById(modalId);
//     if (modal) {
//         modal.style.display = 'none';
//     }
// }

// Hero区域按钮处理
function handleStartSharing() {
  // 如果未登录，显示登录框
  showModal('registerModal');
}

function handleBrowseNotes() {
  // alert('跳转到笔记广场');
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function () {
  // 加载导航栏组件
  loadNavbarComponent();

  // 设置模态框关闭事件
  // document.querySelectorAll('.close').forEach(closeBtn => {
  //     closeBtn.addEventListener('click', function () {
  //         const modal = this.closest('.modal');
  //         if (modal) {
  //             modal.style.display = 'none';
  //         }
  //     });
  // });

  // // 点击模态框外部关闭
  // document.querySelectorAll('.modal').forEach(modal => {
  //     modal.addEventListener('click', function (e) {
  //         if (e.target === this) {
  //             this.style.display = 'none';
  //         }
  //     });
  // });

  // 滚动动画观察器
  const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
  };

  const observer = new IntersectionObserver(function (entries) {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.style.opacity = '1';
        entry.target.style.transform = 'translateY(0)';
      }
    });
  }, observerOptions);

  // 观察所有需要动画的元素
  document.querySelectorAll('.fade-in').forEach(el => {
    observer.observe(el);
  });
});

// 处理浏览器后退/前进按钮
window.addEventListener('popstate', function () {
  const hash = window.location.hash.substring(1);
  if (hash && window.NavbarComponent) {
    // 这里需要获取导航栏实例并设置活跃页面
    // 由于实例在其他作用域，这里简化处理
    const targetSection = document.getElementById(hash);
    if (targetSection) {
      targetSection.scrollIntoView({
        behavior: 'smooth',
        block: 'start'
      });
    }
  }
});
