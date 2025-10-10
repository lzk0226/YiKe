// 全局变量
let currentNote = null;
let comments = [];
let isLoggedIn = false;
let currentUser = null;

// 页面加载
window.onload = function () {
  // 检查登录状态
  checkLoginStatus();

  const params = new URLSearchParams(window.location.search);
  const noteId = params.get("id");

  if (!noteId) {
    // 如果没有笔记ID，显示错误页面
    showError("未找到笔记ID");
    return;
  } else {
    fetchNoteDetail(noteId);
    // 加载该笔记的评论
    loadComments(noteId);
  }
};

// 检查用户登录状态
function checkLoginStatus() {
  try {
    const accessToken = localStorage.getItem('accessToken');
    const userStr = localStorage.getItem('user');
    const loginStatus = localStorage.getItem('isLoggedIn');

    if (accessToken && userStr && loginStatus === 'true') {
      try {
        const userData = JSON.parse(userStr);
        if (userData && (userData.username || userData.nickname)) {
          const oldLoginStatus = isLoggedIn;
          isLoggedIn = true;
          currentUser = {
            id: userData.id || 1,
            username: userData.username || userData.nickname,
            avatar: userData.avatar || '/assets/img/2.jpg'
          };
          console.log('用户已登录:', currentUser);
          updateCommentFormUI();

          // 如果当前有笔记数据且用户刚登录，获取用户状态
          if (currentNote && currentNote.noteId && oldLoginStatus !== isLoggedIn) {
            fetchNoteUserStatus(currentNote.noteId);
          }
          return;
        }
      } catch (parseError) {
        console.error('解析用户数据失败:', parseError);
      }
    }

    // 未登录状态
    const oldLoginStatus = isLoggedIn;
    isLoggedIn = false;
    currentUser = null;
    console.log('用户未登录');
    updateCommentFormUI();

    // 清除用户状态
    if (currentNote && oldLoginStatus !== isLoggedIn) {
      currentNote.hasLiked = false;
      currentNote.hasFavorited = false;
      updateLikeButton();
      updateFavoriteButton();
    }
  } catch (error) {
    console.error('检查登录状态失败:', error);
    isLoggedIn = false;
    currentUser = null;
    updateCommentFormUI();
  }
}

// 更新评论表单UI
function updateCommentFormUI() {
  const commentForm = document.querySelector('.comment-form');

  if (!commentForm) return;

  if (isLoggedIn) {
    // 已登录 - 显示正常的评论表单
    commentForm.innerHTML = `
      <textarea id="commentInput" class="comment-input" placeholder="写下你的评论..." maxlength="1000"
        oninput="updateCharCount()"></textarea>
      <div class="comment-actions">
        <span class="char-count" id="charCount">0/1000</span>
        <button class="btn btn-primary" onclick="submitComment()">发表评论</button>
      </div>
    `;
  } else {
    // 未登录 - 显示登录提示
    commentForm.innerHTML = `
      <div class="login-prompt">
        <div class="login-prompt-content">
          <div class="login-icon">🔒</div>
          <h3>登录后即可发表评论</h3>
          <p>加入我们的学习社区，与其他同学交流讨论</p>
          <div class="login-actions">
            <button class="btn btn-primary" onclick="redirectToLogin()">立即登录</button>
            <button class="btn btn-secondary" onclick="redirectToRegister()">免费注册</button>
          </div>
        </div>
      </div>
    `;
  }
}

// 跳转到登录页面
function redirectToLogin() {
  // 保存当前页面URL，登录后可以返回
  sessionStorage.setItem('returnUrl', window.location.href);
  window.location.href = '/login.html';
}

// 跳转到注册页面
function redirectToRegister() {
  sessionStorage.setItem('returnUrl', window.location.href);
  window.location.href = 'login.html#register';
}

// Token过期处理
function handleTokenExpired() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('user');
  localStorage.setItem('isLoggedIn', 'false');

  isLoggedIn = false;
  currentUser = null;

  // 重置笔记状态
  if (currentNote) {
    currentNote.hasLiked = false;
    currentNote.hasFavorited = false;
    updateLikeButton();
    updateFavoriteButton();
  }

  updateCommentFormUI();
  showToast('登录已过期，请重新登录', 'error');
}

// 显示错误页面
function showError(message) {
  document.getElementById('noteLoading').style.display = 'none';
  document.getElementById('noteContent').innerHTML = `
    <div class="error-state" style="text-align: center; padding: 60px 20px; color: #999;">
      <div style="font-size: 48px; margin-bottom: 16px;">😕</div>
      <h3 style="margin-bottom: 8px; color: #666;">出错了</h3>
      <p>${message}</p>
      <button onclick="window.history.back()" style="margin-top: 16px; padding: 8px 16px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">返回上页</button>
    </div>
  `;
}

// 获取笔记详情（实际API调用）
function fetchNoteDetail(noteId) {
  fetch(`http://localhost:8080/user/notes/detail/${noteId}`)
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP ${res.status}: ${res.statusText}`);
      }
      return res.json();
    })
    .then(res => {
      if (res.code === 200 && res.data) {
        const d = res.data;

        currentNote = {
          noteId: d.id,
          title: d.title,
          content: d.content,
          description: d.description,
          author: d.author?.nickname || "未知作者",
          authorInitial: d.authorInitial || d.author?.nickname?.charAt(0) || "?",
          authorAvatar: d.author?.avatar || "",
          subjectName: d.subjectName || d.subject?.name || "",
          noteTypeName: d.noteTypeName || d.noteType?.name || "",
          time: formatTime(d.createTime) || "刚刚",
          views: d.views || 0,
          likes: d.likes || 0,
          favorites: d.favorites || 0,
          rating: d.rating || 0,
          ratingCount: d.ratingCount || 0,
          commentCount: d.commentCount || 0,
          hasLiked: false, // 初始设为false，后续通过API获取
          hasFavorited: false // 初始设为false，后续通过API获取
        };

        loadNote();

        // 如果用户已登录，获取用户对该笔记的状态
        if (isLoggedIn) {
          fetchNoteUserStatus(noteId);
        }
      } else {
        showError(res.msg || "笔记不存在或已删除");
      }
    })
    .catch(err => {
      console.error("获取笔记详情失败:", err);
      showError("网络错误，请刷新页面重试");
    });
}

// 获取用户对笔记的点赞和收藏状态
function fetchNoteUserStatus(noteId) {
  const token = localStorage.getItem('accessToken');
  if (!token) return;

  fetch(`http://localhost:8080/user/notes/status/${noteId}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  })
    .then(res => {
      if (!res.ok) {
        if (res.status === 401) {
          // Token 无效，清除登录状态
          handleTokenExpired();
          return;
        }
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log('用户状态数据:', data);

      if (data.code === 200 && data.data) {
        // 更新本地状态
        currentNote.hasLiked = data.data.isLiked || false;
        currentNote.hasFavorited = data.data.isFavorited || false;

        // 更新UI显示
        updateLikeButton();
        updateFavoriteButton();
      }
    })
    .catch(err => {
      console.error('获取用户状态失败:', err);
      // 不影响主要功能，静默处理错误
    });
}

// 渲染笔记内容
function loadNote() {
  document.getElementById('noteLoading').style.display = 'none';
  document.getElementById('noteContent').style.display = 'block';

  // 设置标题
  document.getElementById('noteTitle').textContent = currentNote.title;

  // 优化：直接处理并设置富文本内容，无延迟
  const processedContent = processRichContent(currentNote.content);
  document.getElementById('noteBody').innerHTML = processedContent;

  // 设置简介（如果有的话）
  if (currentNote.description && currentNote.description.trim()) {
    document.getElementById('noteDescription').style.display = 'block';
    document.getElementById('descriptionText').textContent = currentNote.description;
  }

  // 设置作者信息
  document.getElementById('authorName').textContent = currentNote.author;

  const authorAvatar = document.getElementById('authorAvatar');
  if (currentNote.authorAvatar && currentNote.authorAvatar.startsWith('http')) {
    authorAvatar.innerHTML = `<img src="${currentNote.authorAvatar}" alt="avatar" class="avatar-img" onerror="this.style.display='none'; this.parentNode.textContent='${currentNote.authorInitial}';" />`;
  } else {
    authorAvatar.textContent = currentNote.authorInitial;
  }

  // 设置学科和笔记类型
  document.getElementById('subjectName').textContent = currentNote.subjectName;
  document.getElementById('noteTypeName').textContent = currentNote.noteTypeName;

  // 设置时间和统计信息
  document.getElementById('noteTime').textContent = currentNote.time;
  document.getElementById('viewCount').textContent = currentNote.views;
  document.getElementById('ratingValue').textContent = currentNote.rating.toFixed(1);
  document.getElementById('likeCount').textContent = currentNote.likes;
  document.getElementById('favoriteCount').textContent = currentNote.favorites;
  document.getElementById('commentCount').textContent = currentNote.commentCount;
  document.getElementById('commentCountHeader').textContent = currentNote.commentCount;

  // 初始化按钮状态
  updateLikeButton();
  updateFavoriteButton();
}

// 优化后的富文本内容处理 - 移除延迟和复杂验证
function processRichContent(htmlContent) {
  if (!htmlContent) return '';

  // 创建临时DOM来处理HTML内容
  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = htmlContent;

  // 处理所有图片 - 简化逻辑，直接处理
  const images = tempDiv.querySelectorAll('img');
  images.forEach((img, index) => {
    const src = img.getAttribute('src');

    if (src && src.startsWith('data:image/')) {
      // Base64图片直接处理，无延迟
      processBase64ImageFast(img);
    } else if (src) {
      // 普通网络图片处理
      processNetworkImage(img);
    }
  });

  return tempDiv.innerHTML;
}

// 快速处理Base64图片 - 移除所有延迟和复杂验证
function processBase64ImageFast(img) {
  img.classList.add('base64-image');

  const originalSrc = img.src;

  // 直接设置点击预览功能，无需额外验证
  img.style.cursor = 'pointer';
  img.onclick = function (e) {
    e.preventDefault();
    previewImage(originalSrc);
  };

  // 简单的错误处理
  img.onerror = function () {
    replaceWithErrorPlaceholder(this, 'Base64图片加载失败');
  };

  // 图片已经有src，浏览器会自动加载，无需额外处理
}

// 处理网络图片
function processNetworkImage(img) {
  img.classList.add('network-image');
  img.style.cursor = 'pointer';

  const originalSrc = img.src;

  img.onerror = function () {
    replaceWithErrorPlaceholder(this, '图片加载失败');
  };

  img.onclick = function (e) {
    e.preventDefault();
    previewImage(originalSrc);
  };
}

// 替换为错误占位符
function replaceWithErrorPlaceholder(img, errorMsg) {
  const errorDiv = document.createElement('div');
  errorDiv.className = 'image-error';
  errorDiv.innerHTML = `
    <div class="error-content">
      <span class="error-icon">🖼️</span>
      <p>${errorMsg}</p>
      <small>无法显示此图片</small>
    </div>
  `;

  img.parentNode.replaceChild(errorDiv, img);
}

// 切换笔记点赞 - 接入后端接口
function toggleLike() {
  if (!isLoggedIn) {
    showToast('请先登录后再点赞', 'error');
    return;
  }

  if (!currentNote || !currentNote.noteId) {
    showToast('笔记信息错误', 'error');
    return;
  }

  const token = localStorage.getItem('accessToken');
  if (!token) {
    showToast('请重新登录', 'error');
    return;
  }

  // 禁用按钮，防止重复点击
  const likeBtn = document.getElementById('likeBtn');
  likeBtn.style.pointerEvents = 'none';

  fetch(`http://localhost:8080/user/notes/like/${currentNote.noteId}`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  })
    .then(res => {
      if (!res.ok) {
        if (res.status === 401) {
          throw new Error('登录已过期，请重新登录');
        }
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log('点赞结果:', data);

      if (data.code === 200) {
        // 根据后端返回的状态更新本地数据
        const wasLiked = currentNote.hasLiked;
        currentNote.hasLiked = data.data.isLiked;

        // 更新点赞数
        if (currentNote.hasLiked && !wasLiked) {
          currentNote.likes += 1;
        } else if (!currentNote.hasLiked && wasLiked) {
          currentNote.likes -= 1;
        }

        // 更新UI
        document.getElementById('likeCount').textContent = currentNote.likes;
        updateLikeButton();

        // 显示成功消息
        showToast(data.data.message || (currentNote.hasLiked ? '点赞成功' : '取消点赞成功'), 'success');
      } else {
        showToast(data.msg || '点赞失败', 'error');
      }
    })
    .catch(err => {
      console.error('点赞请求失败:', err);
      if (err.message.includes('登录已过期') || err.message.includes('401')) {
        handleTokenExpired();
      } else {
        showToast('网络错误，请稍后重试', 'error');
      }
    })
    .finally(() => {
      // 恢复按钮状态
      likeBtn.style.pointerEvents = 'auto';
    });
}

// 切换收藏 - 接入后端接口
function toggleFavorite() {
  if (!isLoggedIn) {
    showToast('请先登录后再收藏', 'error');
    return;
  }

  if (!currentNote || !currentNote.noteId) {
    showToast('笔记信息错误', 'error');
    return;
  }

  const token = localStorage.getItem('accessToken');
  if (!token) {
    showToast('请重新登录', 'error');
    return;
  }

  // 禁用按钮，防止重复点击
  const favoriteBtn = document.getElementById('favoriteBtn');
  favoriteBtn.style.pointerEvents = 'none';

  fetch(`http://localhost:8080/user/notes/favorite/${currentNote.noteId}`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  })
    .then(res => {
      if (!res.ok) {
        if (res.status === 401) {
          throw new Error('登录已过期，请重新登录');
        }
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log('收藏结果:', data);

      if (data.code === 200) {
        // 根据后端返回的状态更新本地数据
        const wasFavorited = currentNote.hasFavorited;
        currentNote.hasFavorited = data.data.isFavorited;

        // 更新收藏数
        if (currentNote.hasFavorited && !wasFavorited) {
          currentNote.favorites += 1;
        } else if (!currentNote.hasFavorited && wasFavorited) {
          currentNote.favorites -= 1;
        }

        // 更新UI
        document.getElementById('favoriteCount').textContent = currentNote.favorites;
        updateFavoriteButton();

        // 显示成功消息
        showToast(data.data.message || (currentNote.hasFavorited ? '收藏成功' : '取消收藏成功'), 'success');
      } else {
        showToast(data.msg || '收藏失败', 'error');
      }
    })
    .catch(err => {
      console.error('收藏请求失败:', err);
      if (err.message.includes('登录已过期') || err.message.includes('401')) {
        handleTokenExpired();
      } else {
        showToast('网络错误，请稍后重试', 'error');
      }
    })
    .finally(() => {
      // 恢复按钮状态
      favoriteBtn.style.pointerEvents = 'auto';
    });
}

// 更新点赞按钮状态
function updateLikeButton() {
  const likeBtn = document.getElementById('likeBtn');
  if (!likeBtn) return;

  if (isLoggedIn && currentNote.hasLiked) {
    likeBtn.classList.add('liked');
    likeBtn.title = '取消点赞';
  } else {
    likeBtn.classList.remove('liked');
    likeBtn.title = isLoggedIn ? '点赞' : '登录后可点赞';
  }

  // 如果未登录，设置点击提示
  if (!isLoggedIn) {
    likeBtn.style.opacity = '0.6';
  } else {
    likeBtn.style.opacity = '1';
  }
}

// 更新收藏按钮状态
function updateFavoriteButton() {
  const favoriteBtn = document.getElementById('favoriteBtn');
  if (!favoriteBtn) return;

  if (isLoggedIn && currentNote.hasFavorited) {
    favoriteBtn.classList.add('favorited');
    favoriteBtn.title = '取消收藏';
  } else {
    favoriteBtn.classList.remove('favorited');
    favoriteBtn.title = isLoggedIn ? '收藏' : '登录后可收藏';
  }

  // 如果未登录，设置点击提示
  if (!isLoggedIn) {
    favoriteBtn.style.opacity = '0.6';
  } else {
    favoriteBtn.style.opacity = '1';
  }
}

// 从后端加载评论
function loadComments(noteId) {
  // 安全地获取DOM元素
  const commentsLoading = document.getElementById('commentsLoading');
  const commentsList = document.getElementById('commentsList');

  // 检查DOM元素是否存在
  if (!commentsLoading || !commentsList) {
    console.error('评论相关DOM元素未找到');
    return;
  }

  // 显示加载状态
  commentsLoading.style.display = 'block';
  commentsList.innerHTML = ''; // 清空现有评论

  if (!noteId) {
    // 如果没有noteId，显示空状态
    setTimeout(() => {
      if (commentsLoading) commentsLoading.style.display = 'none';
      renderComments();
    }, 500);
    return;
  }

  // 调用后端API获取评论
  fetch(`http://localhost:8080/user/comment/note/${noteId}`)
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log('评论数据:', data);

      // 处理后端返回的评论数据
      if (Array.isArray(data)) {
        comments = data.map(comment => ({
          id: comment.id,
          noteId: comment.noteId,
          userId: comment.userId,
          parentId: comment.parentId,
          author: comment.author?.nickname || `用户${comment.userId}`,
          content: comment.content,
          time: formatTime(comment.createTime) || "刚刚",
          likeCount: comment.likes || 0,
          hasLiked: comment.hasLiked || false
        }));
      } else {
        comments = [];
        console.warn('后端返回的评论数据格式不正确:', data);
      }

      // 安全地隐藏加载状态并渲染评论
      if (commentsLoading) commentsLoading.style.display = 'none';
      renderComments();

      // 更新评论数量
      updateCommentCount();

      // 默认保持在顶部，不自动滚动到底部
      const commentsSection = document.querySelector('.comments-list');
      if (commentsSection) {
        commentsSection.scrollTop = 0; // 始终从顶部开始
      }
    })
    .catch(err => {
      console.error('加载评论失败:', err);
      // 出错时显示空状态
      comments = [];
      if (commentsLoading) commentsLoading.style.display = 'none';
      renderComments();
      showToast('评论加载失败', 'error');
    });
}

// 渲染评论列表
function renderComments() {
  const commentsList = document.getElementById('commentsList');

  // 检查DOM元素是否存在
  if (!commentsList) {
    console.error('评论列表DOM元素未找到');
    return;
  }

  if (comments.length === 0) {
    commentsList.innerHTML = '<div class="empty-state">暂无评论，快来发表第一条评论吧~</div>';
    return;
  }

  commentsList.innerHTML = comments.map(comment => `
    <div class="comment" data-comment-id="${comment.id}">
      <div class="avatar comment-avatar">${comment.author.charAt(0)}</div>
      <div class="comment-body">
        <div class="comment-header">
          <span class="comment-author">${comment.author}</span>
          <span class="comment-time">${comment.time}</span>
        </div>
        <div class="comment-content">${comment.content}</div>
        <div class="comment-actions">
          ${isLoggedIn ?
      `<button class="comment-action ${comment.hasLiked ? 'liked' : ''}" 
                onclick="toggleCommentLike(${comment.id})">
            👍 <span class="like-count">${comment.likeCount}</span>
          </button>` :
      `<span class="comment-action disabled" title="登录后可点赞">
            👍 <span class="like-count">${comment.likeCount}</span>
          </span>`
    }
        </div>
      </div>
    </div>
  `).join('');
}

// 切换评论点赞
function toggleCommentLike(commentId) {
  if (!isLoggedIn) {
    showToast('请先登录后再点赞', 'error');
    return;
  }

  const comment = comments.find(c => c.id === commentId);
  if (!comment) return;

  // 调用后端点赞API
  fetch(`http://localhost:8080/user/comment/like/${commentId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log('评论点赞结果:', data);

      if (data.code === 200) {
        // 成功点赞，更新本地状态
        comment.hasLiked = !comment.hasLiked;
        comment.likeCount += comment.hasLiked ? 1 : -1;

        // 更新页面显示
        const commentElement = document.querySelector(`[data-comment-id="${commentId}"]`);
        if (commentElement) {
          const likeButton = commentElement.querySelector('.comment-action');
          const likeCountSpan = commentElement.querySelector('.like-count');

          if (comment.hasLiked) {
            likeButton.classList.add('liked');
          } else {
            likeButton.classList.remove('liked');
          }

          likeCountSpan.textContent = comment.likeCount;
        }
      } else {
        console.error('点赞失败:', data);
        showToast(data.message || '点赞失败', 'error');
      }
    })
    .catch(err => {
      console.error('点赞请求失败:', err);
      showToast('网络错误，请稍后重试', 'error');
    });
}

// 更新字符计数
function updateCharCount() {
  const input = document.getElementById('commentInput');
  const count = document.getElementById('charCount');
  if (input && count) {
    count.textContent = `${input.value.length}/1000`;
  }
}

// 提交评论
function submitComment() {
  // 首先检查登录状态
  if (!isLoggedIn || !currentUser) {
    showToast('请先登录后再发表评论', 'error');
    redirectToLogin();
    return;
  }

  const input = document.getElementById('commentInput');

  if (!input) {
    console.error('评论输入框未找到');
    return;
  }

  const content = input.value.trim();

  if (!content) {
    showToast('请输入评论内容', 'error');
    return;
  }

  if (!currentNote || !currentNote.noteId) {
    showToast('无法获取笔记信息，请刷新页面重试', 'error');
    return;
  }

  // 记录当前滚动位置，提交新评论后保持位置
  const commentsSection = document.querySelector('.comments-list');
  const currentScrollTop = commentsSection ? commentsSection.scrollTop : 0;

  // 禁用提交按钮，防止重复提交
  const submitBtn = document.querySelector('.comment-actions .btn-primary');
  const originalText = submitBtn.textContent;
  submitBtn.disabled = true;
  submitBtn.textContent = '发表中...';

  // 构造评论数据
  const commentData = {
    noteId: currentNote.noteId,
    userId: currentUser.id,
    parentId: null, // 暂不支持回复评论，设为null
    content: content,
    likes: 0
  };

  // 调用后端API提交评论
  fetch('http://localhost:8080/user/comment/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(commentData)
  })
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log('评论提交结果:', data);

      if (data.code === 200) {
        // 评论成功，清空输入框
        input.value = '';
        updateCharCount();

        // 更新笔记的评论数量
        if (currentNote) {
          currentNote.commentCount++;
          const commentCountElement = document.getElementById('commentCount');
          const commentCountHeaderElement = document.getElementById('commentCountHeader');

          if (commentCountElement) {
            commentCountElement.textContent = currentNote.commentCount;
          }
          if (commentCountHeaderElement) {
            commentCountHeaderElement.textContent = currentNote.commentCount;
          }
        }

        // 刷新评论列表，新评论会出现在顶部（按时间倒序）
        loadComments(currentNote.noteId);

        // 显示成功提示
        showToast('评论发表成功！', 'success');

        // 新评论提交后，滚动到顶部查看新评论
        setTimeout(() => {
          if (commentsSection) {
            commentsSection.scrollTop = 0;
          }
        }, 200);
      } else {
        console.error('评论失败:', data);
        showToast(data.message || '评论发表失败', 'error');
      }
    })
    .catch(err => {
      console.error('评论请求失败:', err);
      showToast('网络错误，请稍后重试', 'error');
    })
    .finally(() => {
      // 恢复提交按钮状态
      submitBtn.disabled = false;
      submitBtn.textContent = originalText;
    });
}

// 新增：滚动到评论区顶部的函数
function scrollToCommentsTop() {
  const commentsSection = document.querySelector('.comments-list');
  if (commentsSection) {
    commentsSection.scrollTop = 0;
  }
}

// 新增：滚动到评论区底部的函数
function scrollToCommentsBottom() {
  const commentsSection = document.querySelector('.comments-list');
  if (commentsSection) {
    commentsSection.scrollTop = commentsSection.scrollHeight;
  }
}

// 更新评论数量显示
function updateCommentCount() {
  const commentCount = comments.length;
  const commentCountHeader = document.getElementById('commentCountHeader');
  const commentCountElement = document.getElementById('commentCount');

  // 安全地更新DOM元素
  if (commentCountHeader) {
    commentCountHeader.textContent = commentCount;
  }

  // 如果有笔记数据，也更新笔记的评论数
  if (currentNote) {
    currentNote.commentCount = commentCount;
    if (commentCountElement) {
      commentCountElement.textContent = commentCount;
    }
  }
}

// 添加消息提示功能
function showToast(message, type = 'info') {
  // 移除现有的toast
  const existingToast = document.querySelector('.toast');
  if (existingToast) {
    existingToast.remove();
  }

  // 创建新的toast
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.textContent = message;

  // 添加到页面
  document.body.appendChild(toast);

  // 显示动画
  setTimeout(() => {
    toast.classList.add('show');
  }, 100);

  // 自动隐藏
  setTimeout(() => {
    toast.classList.remove('show');
    setTimeout(() => {
      if (toast.parentNode) {
        toast.parentNode.removeChild(toast);
      }
    }, 300);
  }, 3000);
}

// 图片预览功能
function previewImage(src) {
  const previewImg = document.getElementById('previewImg');
  const previewModal = document.getElementById('imagePreview');

  // 显示模态框
  previewModal.style.display = 'flex';
  previewImg.style.opacity = '0';

  previewImg.onload = function () {
    this.style.opacity = '1';
  };

  previewImg.onerror = function () {
    showToast('图片预览失败', 'error');
    closeImagePreview();
  };

  previewImg.src = src;
}

// 关闭图片预览
function closeImagePreview() {
  document.getElementById('imagePreview').style.display = 'none';
  document.getElementById('previewImg').src = '';
}

// 添加评论区域滚动到底部的功能
function scrollCommentsToBottom() {
  const commentsSection = document.querySelector('.comments-list');
  if (commentsSection) {
    commentsSection.scrollTop = commentsSection.scrollHeight;
  }
}

// 添加评论区域滚动到顶部的功能
function scrollCommentsToTop() {
  const commentsSection = document.querySelector('.comments-list');
  if (commentsSection) {
    commentsSection.scrollTop = 0;
  }
}

// 键盘事件处理 - 优化版本
document.addEventListener('keydown', function (e) {
  // Ctrl+Enter 提交评论 - 仅在已登录且聚焦在输入框时
  if (e.ctrlKey && e.key === 'Enter') {
    e.preventDefault();
    const commentInput = document.getElementById('commentInput');
    if (commentInput && document.activeElement === commentInput && isLoggedIn) {
      submitComment();
    }
  }

  // ESC 关闭图片预览
  if (e.key === 'Escape') {
    closeImagePreview();
  }
});

// 工具函数：格式化时间
function formatTime(timeString) {
  if (!timeString) return '刚刚';

  try {
    const date = new Date(timeString);
    const now = new Date();
    const diff = now - date;

    if (diff < 60000) return '刚刚';
    if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`;
    if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`;
    if (diff < 2592000000) return `${Math.floor(diff / 86400000)}天前`;

    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  } catch (e) {
    return timeString;
  }
}

// 工具函数：防抖处理
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

// 优化字符计数更新
const debouncedUpdateCharCount = debounce(updateCharCount, 300);

// 页面初始化完成后绑定事件
document.addEventListener('DOMContentLoaded', function () {
  const commentInput = document.getElementById('commentInput');
  if (commentInput) {
    commentInput.oninput = debouncedUpdateCharCount;
  }
});

// 监听登录状态变化
window.addEventListener('storage', function (e) {
  if (e.key === 'accessToken' || e.key === 'isLoggedIn' || e.key === 'user') {
    const oldLoginStatus = isLoggedIn;
    checkLoginStatus();

    // 如果登录状态发生变化，重新获取用户状态
    if (oldLoginStatus !== isLoggedIn && currentNote && currentNote.noteId) {
      if (isLoggedIn) {
        fetchNoteUserStatus(currentNote.noteId);
      } else {
        // 登出时清除状态
        currentNote.hasLiked = false;
        currentNote.hasFavorited = false;
        updateLikeButton();
        updateFavoriteButton();
      }
    }
  }
});

// 页面焦点恢复时重新检查状态
window.addEventListener('focus', function () {
  checkLoginStatus();
});

// 页面可见性变化时重新检查状态
document.addEventListener('visibilitychange', function () {
  if (!document.hidden && isLoggedIn && currentNote && currentNote.noteId) {
    // 页面重新可见时，重新获取最新状态
    setTimeout(() => {
      fetchNoteUserStatus(currentNote.noteId);
    }, 500);
  }
});

// 错误处理：全局错误捕获
window.addEventListener('error', function (e) {
  console.error('全局错误:', e.error);
  // 可以在这里添加错误上报逻辑
});

// 图片右键菜单禁用（可选）
document.addEventListener('contextmenu', function (e) {
  if (e.target.tagName === 'IMG') {
    e.preventDefault();
  }
});

// 移动端触摸优化
let touchStartY = 0;
document.addEventListener('touchstart', function (e) {
  touchStartY = e.touches[0].clientY;
}, { passive: true });

document.addEventListener('touchmove', function (e) {
  const touchCurrentY = e.touches[0].clientY;
  const touchDiff = touchStartY - touchCurrentY;

  // 如果是在图片预览模式，允许缩放手势
  if (document.getElementById('imagePreview').style.display === 'flex') {
    e.stopPropagation();
  }
}, { passive: true });