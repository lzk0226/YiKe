package com.ruoyi.yike.domain;

import java.util.Date;


public class SystemConfig {
    /** 配置ID */
    private Integer id;

    /** 配置键 */
    private String configKey;

    /** 配置值 */
    private String configValue;

    /** 配置描述 */
    private String description;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    // 构造函数
    public SystemConfig() {}

    // Getter 和 Setter 方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}