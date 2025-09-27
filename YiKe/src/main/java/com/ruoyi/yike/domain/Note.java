package com.ruoyi.yike.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户端笔记信息
 */
public class Note {
    /** 笔记ID */
    private Long id;

    /** 笔记标题 */
    private String title;

    /** 笔记内容(富文本) */
    private String content;

    /** 笔记简介 */
    private String description;

    /** 学科分类ID */
    private Integer subjectId;

    /** 笔记类型ID */
    private Integer noteTypeId;

    /** 作者ID */
    private Long userId;

    /** 浏览量 */
    private Integer views;

    /** 点赞数 */
    private Integer likes;

    /** 收藏数 */
    private Integer favorites;

    /** 评分(0-5分) */
    private BigDecimal rating;

    /** 评分人数 */
    private Integer ratingCount;

    // 关联对象（用于前端显示）
    private Subject subject;
    private NoteType noteType;
    private User author;
    private List<NoteImage> images;

    // 构造函数
    public Note() {}

    public Note(Long id, String title, String content, String description,
                Integer subjectId, Integer noteTypeId, Long userId,
                Integer views, Integer likes, Integer favorites,
                BigDecimal rating, Integer ratingCount,
                Subject subject, NoteType noteType, User author, List<NoteImage> images) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.subjectId = subjectId;
        this.noteTypeId = noteTypeId;
        this.userId = userId;
        this.views = views;
        this.likes = likes;
        this.favorites = favorites;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.subject = subject;
        this.noteType = noteType;
        this.author = author;
        this.images = images;
    }

    // Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getSubjectId() { return subjectId; }
    public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }

    public Integer getNoteTypeId() { return noteTypeId; }
    public void setNoteTypeId(Integer noteTypeId) { this.noteTypeId = noteTypeId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getViews() { return views != null ? views : 0; }
    public void setViews(Integer views) { this.views = views; }

    public Integer getLikes() { return likes != null ? likes : 0; }
    public void setLikes(Integer likes) { this.likes = likes; }

    public Integer getFavorites() { return favorites != null ? favorites : 0; }
    public void setFavorites(Integer favorites) { this.favorites = favorites; }

    public BigDecimal getRating() { return rating != null ? rating : BigDecimal.ZERO; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public Integer getRatingCount() { return ratingCount != null ? ratingCount : 0; }
    public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public NoteType getNoteType() { return noteType; }
    public void setNoteType(NoteType noteType) { this.noteType = noteType; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public List<NoteImage> getImages() { return images; }
    public void setImages(List<NoteImage> images) { this.images = images; }

    // 前端卡片需要的字段
    public String getAuthorInitial() {
        if (author != null && author.getNickname() != null && !author.getNickname().isEmpty()) {
            return author.getNickname().substring(0, 1);
        }
        return "";
    }

    public String getSubjectName() {
        return subject != null ? subject.getName() : "";
    }

    public String getNoteTypeName() {
        return noteType != null ? noteType.getName() : "";
    }
}
