package com.ruoyi.yike.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.yike.domain.Note;
import com.ruoyi.yike.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/notes")
public class NoteController extends BaseController {

    @Autowired
    private NoteService noteService;

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
}