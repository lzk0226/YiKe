package com.ruoyi.yike.domain;

public class NoteType {
    private Integer id;
    private String code;
    private String name;
    private String description;
    //private Integer status;  // 添加状态字段
//    private Integer sortOrder;
//    private Date createdAt;

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

   /* public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }*/

/*    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }*/
}