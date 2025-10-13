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
    showError("未找到笔记ID");
    return;
  } else {
    fetchNoteDetail(noteId);
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

          if (currentNote && currentNote.noteId && oldLoginStatus !== isLoggedIn) {
            fetchNoteUserStatus(currentNote.noteId);
          }
          return;
        }
      } catch (parseError) {
        console.error('解析用户数据失败:', parseError);
      }
    }

    const oldLoginStatus = isLoggedIn;
    isLoggedIn = false;
    currentUser = null;
    console.log('用户未登录');
    updateCommentFormUI();

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
    commentForm.innerHTML = `
      <textarea id="commentInput" class="comment-input" placeholder="写下你的评论..." maxlength="1000"
        oninput="updateCharCount()"></textarea>
      <div class="comment-actions">
        <span class="char-count" id="charCount">0/1000</span>
        <button class="btn btn-primary" onclick="submitComment()">发表评论</button>
      </div>
    `;
  } else {
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
          authorInitial: getAuthorInitial(d.author),
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
          hasLiked: false,
          hasFavorited: false
        };

        loadNote();

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

// 获取作者首字母或头像
function getAuthorInitial(author) {
  if (!author) return "?";

  // 如果有昵称，获取首字母
  if (author.nickname) {
    return author.nickname.charAt(0).toUpperCase();
  }

  // 如果有用户名，获取首字母
  if (author.username) {
    return author.username.charAt(0).toUpperCase();
  }

  return "?";
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
        currentNote.hasLiked = data.data.isLiked || false;
        currentNote.hasFavorited = data.data.isFavorited || false;

        updateLikeButton();
        updateFavoriteButton();
      }
    })
    .catch(err => {
      console.error('获取用户状态失败:', err);
    });
}

// 渲染笔记内容
function loadNote() {
  document.getElementById('noteLoading').style.display = 'none';
  document.getElementById('noteContent').style.display = 'block';

  // 设置标题
  document.getElementById('noteTitle').textContent = currentNote.title;

  // 直接处理并设置富文本内容
  const processedContent = processRichContent(currentNote.content);
  document.getElementById('noteBody').innerHTML = processedContent;

  // 设置简介
  if (currentNote.description && currentNote.description.trim()) {
    document.getElementById('noteDescription').style.display = 'block';
    document.getElementById('descriptionText').textContent = currentNote.description;
  }

  // 设置作者信息
  document.getElementById('authorName').textContent = currentNote.author;

  // 设置作者头像
  const authorAvatar = document.getElementById('authorAvatar');
  if (currentNote.authorAvatar) {
    // 检查是否是base64头像或URL头像
    if (currentNote.authorAvatar.startsWith('data:image/') ||
      currentNote.authorAvatar.startsWith('http://') ||
      currentNote.authorAvatar.startsWith('https://') ||
      currentNote.authorAvatar.startsWith('/')) {
      // 显示图片头像
      authorAvatar.innerHTML = `<img src="${currentNote.authorAvatar}" alt="avatar" class="avatar-img" onerror="this.style.display='none'; this.parentNode.textContent='${currentNote.authorInitial}';" />`;
      authorAvatar.style.background = 'transparent';
    } else {
      // 如果不是有效的图片URL，显示首字母
      authorAvatar.textContent = currentNote.authorInitial;
    }
  } else {
    // 没有头像，显示首字母
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

// 优化后的富文本内容处理
function processRichContent(htmlContent) {
  if (!htmlContent) return '';

  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = htmlContent;

  // 处理所有图片
  const images = tempDiv.querySelectorAll('img');
  images.forEach((img, index) => {
    const src = img.getAttribute('src');

    if (src && src.startsWith('data:image/')) {
      processBase64ImageFast(img);
    } else if (src) {
      processNetworkImage(img);
    }
  });

  return tempDiv.innerHTML;
}

// 快速处理Base64图片
function processBase64ImageFast(img) {
  img.classList.add('base64-image');
  const originalSrc = img.src;

  img.style.cursor = 'pointer';
  img.onclick = function (e) {
    e.preventDefault();
    previewImage(originalSrc);
  };

  img.onerror = function () {
    replaceWithErrorPlaceholder(this, 'Base64图片加载失败');
  };
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

// 切换笔记点赞
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
        const wasLiked = currentNote.hasLiked;
        currentNote.hasLiked = data.data.isLiked;

        if (currentNote.hasLiked && !wasLiked) {
          currentNote.likes += 1;
        } else if (!currentNote.hasLiked && wasLiked) {
          currentNote.likes -= 1;
        }

        document.getElementById('likeCount').textContent = currentNote.likes;
        updateLikeButton();

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
      likeBtn.style.pointerEvents = 'auto';
    });
}

// 切换收藏
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
        const wasFavorited = currentNote.hasFavorited;
        currentNote.hasFavorited = data.data.isFavorited;

        if (currentNote.hasFavorited && !wasFavorited) {
          currentNote.favorites += 1;
        } else if (!currentNote.hasFavorited && wasFavorited) {
          currentNote.favorites -= 1;
        }

        document.getElementById('favoriteCount').textContent = currentNote.favorites;
        updateFavoriteButton();

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

  if (!isLoggedIn) {
    favoriteBtn.style.opacity = '0.6';
  } else {
    favoriteBtn.style.opacity = '1';
  }
}

// 从后端加载评论
function loadComments(noteId) {
  const commentsLoading = document.getElementById('commentsLoading');
  const commentsList = document.getElementById('commentsList');

  if (!commentsLoading || !commentsList) {
    console.error('评论相关DOM元素未找到');
    return;
  }

  commentsLoading.style.display = 'block';
  commentsList.innerHTML = '';

  if (!noteId) {
    setTimeout(() => {
      if (commentsLoading) commentsLoading.style.display = 'none';
      renderComments();
    }, 500);
    return;
  }

  fetch(`http://localhost:8080/user/comment/note/${noteId}`)
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log('评论数据:', data);

      if (Array.isArray(data)) {
        comments = data.map(comment => ({
          id: comment.id,
          noteId: comment.noteId,
          userId: comment.userId,
          parentId: comment.parentId,
          author: comment.author?.nickname || `用户${comment.userId}`,
          authorAvatar: comment.author?.avatar || '',
          content: comment.content,
          time: formatTime(comment.createTime) || "刚刚",
          likeCount: comment.likes || 0,
          hasLiked: comment.hasLiked || false
        }));
      } else {
        comments = [];
        console.warn('后端返回的评论数据格式不正确:', data);
      }

      if (commentsLoading) commentsLoading.style.display = 'none';
      renderComments();
      updateCommentCount();

      const commentsSection = document.querySelector('.comments-list');
      if (commentsSection) {
        commentsSection.scrollTop = 0;
      }
    })
    .catch(err => {
      console.error('加载评论失败:', err);
      comments = [];
      if (commentsLoading) commentsLoading.style.display = 'none';
      renderComments();
      showToast('评论加载失败', 'error');
    });
}

// 渲染评论列表
function renderComments() {
  const commentsList = document.getElementById('commentsList');

  if (!commentsList) {
    console.error('评论列表DOM元素未找到');
    return;
  }

  if (comments.length === 0) {
    commentsList.innerHTML = '<div class="empty-state">暂无评论，快来发表第一条评论吧~</div>';
    return;
  }

  commentsList.innerHTML = comments.map(comment => {
    // 获取评论者头像或首字母
    let avatarHTML;
    if (comment.authorAvatar && (comment.authorAvatar.startsWith('data:image/') ||
      comment.authorAvatar.startsWith('http://') ||
      comment.authorAvatar.startsWith('https://') ||
      comment.authorAvatar.startsWith('/'))) {
      // 显示图片头像
      const initial = comment.author.charAt(0).toUpperCase();
      avatarHTML = `<img src="${comment.authorAvatar}" alt="avatar" class="avatar-img" 
                    onerror="this.style.display='none'; this.parentNode.textContent='${initial}';" />`;
    } else {
      // 显示首字母
      avatarHTML = comment.author.charAt(0).toUpperCase();
    }

    return `
      <div class="comment" data-comment-id="${comment.id}">
        <div class="avatar comment-avatar">${avatarHTML}</div>
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
    `;
  }).join('');
}

// 切换评论点赞
function toggleCommentLike(commentId) {
  if (!isLoggedIn) {
    showToast('请先登录后再点赞', 'error');
    return;
  }

  const comment = comments.find(c => c.id === commentId);
  if (!comment) return;

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
        comment.hasLiked = !comment.hasLiked;
        comment.likeCount += comment.hasLiked ? 1 : -1;

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

  const commentsSection = document.querySelector('.comments-list');
  const currentScrollTop = commentsSection ? commentsSection.scrollTop : 0;

  const submitBtn = document.querySelector('.comment-actions .btn-primary');
  const originalText = submitBtn.textContent;
  submitBtn.disabled = true;
  submitBtn.textContent = '发表中...';

  const commentData = {
    noteId: currentNote.noteId,
    userId: currentUser.id,
    parentId: null,
    content: content,
    likes: 0
  };

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
        input.value = '';
        updateCharCount();

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

        loadComments(currentNote.noteId);
        showToast('评论发表成功！', 'success');

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
      submitBtn.disabled = false;
      submitBtn.textContent = originalText;
    });
}

// 更新评论数量显示
function updateCommentCount() {
  const commentCount = comments.length;
  const commentCountHeader = document.getElementById('commentCountHeader');
  const commentCountElement = document.getElementById('commentCount');

  if (commentCountHeader) {
    commentCountHeader.textContent = commentCount;
  }

  if (currentNote) {
    currentNote.commentCount = commentCount;
    if (commentCountElement) {
      commentCountElement.textContent = commentCount;
    }
  }
}

// 添加消息提示功能
function showToast(message, type = 'info') {
  const existingToast = document.querySelector('.toast');
  if (existingToast) {
    existingToast.remove();
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.textContent = message;

  document.body.appendChild(toast);

  setTimeout(() => {
    toast.classList.add('show');
  }, 100);

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

// 键盘事件处理
document.addEventListener('keydown', function (e) {
  if (e.ctrlKey && e.key === 'Enter') {
    e.preventDefault();
    const commentInput = document.getElementById('commentInput');
    if (commentInput && document.activeElement === commentInput && isLoggedIn) {
      submitComment();
    }
  }

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

    if (oldLoginStatus !== isLoggedIn && currentNote && currentNote.noteId) {
      if (isLoggedIn) {
        fetchNoteUserStatus(currentNote.noteId);
      } else {
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
    setTimeout(() => {
      fetchNoteUserStatus(currentNote.noteId);
    }, 500);
  }
});