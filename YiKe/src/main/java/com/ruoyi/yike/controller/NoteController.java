package com.ruoyi.yike.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.yike.domain.Note;
import com.ruoyi.yike.service.NoteService;
import com.ruoyi.yike.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/notes")
public class NoteController extends BaseController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取所有笔记列表（原有方法）
     */
    @GetMapping("/list")
    public AjaxResult getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        return AjaxResult.success(notes);
    }

    /**
     * 根据ID获取笔记详情（原有方法）
     */
    @GetMapping("/{id}")
    public AjaxResult getNoteById(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return AjaxResult.error("笔记不存在");
        }
        return AjaxResult.success(note);
    }

    /**
     * 获取笔记卡片列表（简略版本）
     * 用于首页和笔记广场的卡片展示
     */
    @GetMapping("/cards/latest")
    public AjaxResult getLatestNotes(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "6") int pageSize,
                                     @RequestParam(required = false) Long subjectId,
                                     @RequestParam(required = false) Long noteTypeId) {
        List<Note> notes = noteService.getNoteCardsLatest(page, pageSize, subjectId, noteTypeId);
        int total = noteService.getNoteTotal(subjectId, noteTypeId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", notes);
        data.put("total", total);
        data.put("pageNum", page);
        data.put("pageSize", pageSize);
        return AjaxResult.success(data);
    }

    @GetMapping("/cards/most-read")
    public AjaxResult getMostReadNotes(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "6") int pageSize,
                                       @RequestParam(required = false) Long subjectId,
                                       @RequestParam(required = false) Long noteTypeId) {
        List<Note> notes = noteService.getNoteCardsMostRead(page, pageSize, subjectId, noteTypeId);
        int total = noteService.getNoteTotal(subjectId, noteTypeId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", notes);
        data.put("total", total);
        data.put("pageNum", page);
        data.put("pageSize", pageSize);
        return AjaxResult.success(data);
    }

    @GetMapping("/cards/top-rated")
    public AjaxResult getTopRatedNotes(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "6") int pageSize,
                                       @RequestParam(required = false) Long subjectId,
                                       @RequestParam(required = false) Long noteTypeId) {
        List<Note> notes = noteService.getNoteCardsTopRated(page, pageSize, subjectId, noteTypeId);
        int total = noteService.getNoteTotal(subjectId, noteTypeId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", notes);
        data.put("total", total);
        data.put("pageNum", page);
        data.put("pageSize", pageSize);
        return AjaxResult.success(data);
    }

    @GetMapping("/cards/most-favorited")
    public AjaxResult getMostFavoritedNotes(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "6") int pageSize,
                                            @RequestParam(required = false) Long subjectId,
                                            @RequestParam(required = false) Long noteTypeId) {
        List<Note> notes = noteService.getNoteCardsMostFavorited(page, pageSize, subjectId, noteTypeId);
        int total = noteService.getNoteTotal(subjectId, noteTypeId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", notes);
        data.put("total", total);
        data.put("pageNum", page);
        data.put("pageSize", pageSize);
        return AjaxResult.success(data);
    }

    @GetMapping("/search")
    public AjaxResult searchNotes(@RequestParam(required = false) String keyword,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "6") int pageSize,
                                  @RequestParam(required = false) Long subjectId,
                                  @RequestParam(required = false) Long noteTypeId) {
        try {
            // 参数验证
            if (keyword != null) {
                keyword = keyword.trim();
                if (keyword.isEmpty()) {
                    keyword = null;
                }
            }

            // 计算偏移量
            int offset = (page - 1) * pageSize;

            // 执行搜索
            List<Note> notes = noteService.getSearchNoteCards(keyword, subjectId, noteTypeId, offset, pageSize);
            int total = noteService.getSearchNoteTotal(keyword, subjectId, noteTypeId);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("list", notes);
            data.put("total", total);
            data.put("pageNum", page);
            data.put("pageSize", pageSize);
            data.put("keyword", keyword);

            return AjaxResult.success(data);
        } catch (Exception e) {
            return AjaxResult.error(500, "搜索笔记失败：" + e.getMessage());
        }
    }

    /**
     * 热门搜索建议 - 额外功能：获取热门搜索词
     * 基于笔记标题中的关键词统计
     */
    @GetMapping("/search/hot")
    public AjaxResult getHotSearchKeywords() {
        try {
            // 这里可以根据实际需求实现热门搜索词的统计逻辑
            // 暂时返回一些示例数据
            List<String> hotKeywords = Arrays.asList(
                    "数学", "物理", "化学", "英语", "编程",
                    "算法", "数据结构", "机器学习", "人工智能"
            );
            return AjaxResult.success(hotKeywords);
        } catch (Exception e) {
            return AjaxResult.error(500, "获取热门搜索词失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取笔记详细信息（完整版本）
     * 用于笔记详情页面，会自动增加浏览量
     */
    @GetMapping("/detail/{id}")
    public AjaxResult getNoteDetail(@PathVariable Long id) {
        Note note = noteService.getNoteDetailById(id);
        if (note == null) {
            return AjaxResult.error("笔记不存在");
        }
        return AjaxResult.success(note);
    }

    /**
     * 获取个人发布的笔记卡片列表
     * 用于个人主页展示
     */
    @GetMapping("/my/cards")
    public AjaxResult getMyNoteCards(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "6") int pageSize,
                                     @RequestParam(required = false) Long subjectId,
                                     @RequestParam(required = false) Long noteTypeId,
                                     @RequestParam Long userId) {
        List<Note> notes = noteService.getNoteCardsByUserId(userId, page, pageSize, subjectId, noteTypeId);
        int total = noteService.getNoteCountByUserId(userId, subjectId, noteTypeId);
        Map<String, Object> data = new HashMap<>();
        data.put("list", notes);
        data.put("total", total);
        data.put("pageNum", page);
        data.put("pageSize", pageSize);
        return AjaxResult.success(data);
    }

    /**
     * 获取个人笔记详情（用于编辑）
     * 需要验证token和用户身份
     */
    @GetMapping("/my/detail/{id}")
    public AjaxResult getMyNoteDetail(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return AjaxResult.error(401, "未提供有效的访问令牌");
            }

            // 去掉"Bearer "前缀
            token = token.substring(7);

            // 验证token并获取用户ID
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error(401, "访问令牌已过期或无效");
            }

            Long tokenUserId = jwtUtils.getUserIdFromToken(token);

            // 获取笔记详情
            Note note = noteService.getNoteDetailById(id);
            if (note == null) {
                return AjaxResult.error("笔记不存在");
            }

            // 验证笔记是否属于当前用户
            if (!note.getUserId().equals(tokenUserId)) {
                return AjaxResult.error(403, "无权限访问此笔记");
            }

            return AjaxResult.success(note);
        } catch (Exception e) {
            return AjaxResult.error(500, "获取笔记详情失败：" + e.getMessage());
        }
    }

    /**
     * 发布新笔记
     * 需要验证token和用户身份
     */
    @PostMapping("/publish")
    public AjaxResult publishNote(@RequestBody Note note, HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return AjaxResult.error(401, "未提供有效的访问令牌");
            }

            // 去掉"Bearer "前缀
            token = token.substring(7);

            // 验证token并获取用户ID
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error(401, "访问令牌已过期或无效");
            }

            Long tokenUserId = jwtUtils.getUserIdFromToken(token);
            if (tokenUserId == null) {
                return AjaxResult.error(401, "无效的用户令牌");
            }

            // 设置笔记的用户ID为token中的用户ID
            note.setUserId(tokenUserId);

            // 基本字段验证
            if (note.getTitle() == null || note.getTitle().trim().isEmpty()) {
                return AjaxResult.error(400, "笔记标题不能为空");
            }
            if (note.getContent() == null || note.getContent().trim().isEmpty()) {
                return AjaxResult.error(400, "笔记内容不能为空");
            }
            if (note.getSubjectId() == null) {
                return AjaxResult.error(400, "请选择学科分类");
            }
            if (note.getNoteTypeId() == null) {
                return AjaxResult.error(400, "请选择笔记类型");
            }

            // 设置默认值
            note.setViews(0);
            note.setLikes(0);
            note.setFavorites(0);
            note.setRating(BigDecimal.ZERO);
            note.setRatingCount(0);

            // 调用service保存笔记
            boolean success = noteService.publishNote(note);
            if (success) {
                return AjaxResult.success("笔记发布成功");
            } else {
                return AjaxResult.error("笔记发布失败");
            }
        } catch (Exception e) {
            return AjaxResult.error(500, "发布笔记失败：" + e.getMessage());
        }
    }

    /**
     * 编辑笔记
     * 需要验证token和用户身份，只能编辑自己的笔记
     */
    @PutMapping("/edit/{id}")
    public AjaxResult editNote(@PathVariable Long id, @RequestBody Note note, HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return AjaxResult.error(401, "未提供有效的访问令牌");
            }

            // 去掉"Bearer "前缀
            token = token.substring(7);

            // 验证token并获取用户ID
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error(401, "访问令牌已过期或无效");
            }

            Long tokenUserId = jwtUtils.getUserIdFromToken(token);
            if (tokenUserId == null) {
                return AjaxResult.error(401, "无效的用户令牌");
            }

            // 获取原笔记信息，验证是否存在和权限
            Note existingNote = noteService.getNoteById(id);
            if (existingNote == null) {
                return AjaxResult.error(404, "笔记不存在");
            }

            // 验证笔记是否属于当前用户
            if (!existingNote.getUserId().equals(tokenUserId)) {
                return AjaxResult.error(403, "无权限编辑此笔记");
            }

            // 基本字段验证
            if (note.getTitle() == null || note.getTitle().trim().isEmpty()) {
                return AjaxResult.error(400, "笔记标题不能为空");
            }
            if (note.getContent() == null || note.getContent().trim().isEmpty()) {
                return AjaxResult.error(400, "笔记内容不能为空");
            }
            if (note.getSubjectId() == null) {
                return AjaxResult.error(400, "请选择学科分类");
            }
            if (note.getNoteTypeId() == null) {
                return AjaxResult.error(400, "请选择笔记类型");
            }

            // 设置要更新的笔记ID和用户ID
            note.setId(id);
            note.setUserId(tokenUserId);

            // 保持原有的统计数据不变
            note.setViews(existingNote.getViews());
            note.setLikes(existingNote.getLikes());
            note.setFavorites(existingNote.getFavorites());
            note.setRating(existingNote.getRating());
            note.setRatingCount(existingNote.getRatingCount());

            // 调用service更新笔记
            boolean success = noteService.updateNote(note);
            if (success) {
                return AjaxResult.success("笔记更新成功");
            } else {
                return AjaxResult.error("笔记更新失败");
            }
        } catch (Exception e) {
            return AjaxResult.error(500, "编辑笔记失败：" + e.getMessage());
        }
    }

    /**
     * 点赞/取消点赞笔记
     * 需要验证token和用户身份
     */
    @PostMapping("/like/{id}")
    public AjaxResult toggleLikeNote(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return AjaxResult.error(401, "未提供有效的访问令牌");
            }

            // 去掉"Bearer "前缀
            token = token.substring(7);

            // 验证token并获取用户ID
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error(401, "访问令牌已过期或无效");
            }

            Long tokenUserId = jwtUtils.getUserIdFromToken(token);
            if (tokenUserId == null) {
                return AjaxResult.error(401, "无效的用户令牌");
            }

            // 检查笔记是否存在
            Note note = noteService.getNoteById(id);
            if (note == null) {
                return AjaxResult.error("笔记不存在");
            }

            // 调用service处理点赞逻辑
            boolean isLiked = noteService.toggleLikeNote(tokenUserId, id);

            Map<String, Object> data = new HashMap<>();
            data.put("isLiked", isLiked);
            data.put("message", isLiked ? "点赞成功" : "取消点赞成功");

            return AjaxResult.success(data);
        } catch (Exception e) {
            return AjaxResult.error(500, "点赞操作失败：" + e.getMessage());
        }
    }

    /**
     * 收藏/取消收藏笔记
     * 需要验证token和用户身份
     */
    @PostMapping("/favorite/{id}")
    public AjaxResult toggleFavoriteNote(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return AjaxResult.error(401, "未提供有效的访问令牌");
            }

            // 去掉"Bearer "前缀
            token = token.substring(7);

            // 验证token并获取用户ID
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error(401, "访问令牌已过期或无效");
            }

            Long tokenUserId = jwtUtils.getUserIdFromToken(token);
            if (tokenUserId == null) {
                return AjaxResult.error(401, "无效的用户令牌");
            }

            // 检查笔记是否存在
            Note note = noteService.getNoteById(id);
            if (note == null) {
                return AjaxResult.error("笔记不存在");
            }

            // 调用service处理收藏逻辑
            boolean isFavorited = noteService.toggleFavoriteNote(tokenUserId, id);

            Map<String, Object> data = new HashMap<>();
            data.put("isFavorited", isFavorited);
            data.put("message", isFavorited ? "收藏成功" : "取消收藏成功");

            return AjaxResult.success(data);
        } catch (Exception e) {
            return AjaxResult.error(500, "收藏操作失败：" + e.getMessage());
        }
    }

    /**
     * 检查用户对笔记的点赞和收藏状态
     * 需要验证token
     */
    @GetMapping("/status/{id}")
    public AjaxResult getNoteUserStatus(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                return AjaxResult.error(401, "未提供有效的访问令牌");
            }

            // 去掉"Bearer "前缀
            token = token.substring(7);

            // 验证token并获取用户ID
            if (!jwtUtils.validateToken(token)) {
                return AjaxResult.error(401, "访问令牌已过期或无效");
            }

            Long tokenUserId = jwtUtils.getUserIdFromToken(token);
            if (tokenUserId == null) {
                return AjaxResult.error(401, "无效的用户令牌");
            }

            // 检查笔记是否存在
            Note note = noteService.getNoteById(id);
            if (note == null) {
                return AjaxResult.error("笔记不存在");
            }

            // 获取用户状态
            boolean isLiked = noteService.isNoteLikedByUser(tokenUserId, id);
            boolean isFavorited = noteService.isNoteFavoritedByUser(tokenUserId, id);

            Map<String, Object> data = new HashMap<>();
            data.put("isLiked", isLiked);
            data.put("isFavorited", isFavorited);

            return AjaxResult.success(data);
        } catch (Exception e) {
            return AjaxResult.error(500, "获取状态失败：" + e.getMessage());
        }
    }
}