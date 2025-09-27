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
}