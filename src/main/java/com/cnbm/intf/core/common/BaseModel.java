package com.cnbm.intf.core.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共模型
 *
 *
 * @create 2019022913:36
 */
@Data
public class BaseModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 实体编号（唯一标识）
     */
    protected String id;
    @TableField(exist = false)
    protected String createdBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date createdDate;
    @TableField(exist = false)
    protected String updatedBy;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date updatedDate;
    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    @TableField(exist = false)//表示该属性不为数据库表字段，但又是必须使用的。
    protected boolean isNewRecord = false;

    @JsonIgnore
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isBlank(getId());
    }

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     */
    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    public void preInsert() {
        this.createdDate = new Date();
        this.updatedDate = this.createdDate;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    public void preUpdate() {
        this.updatedDate = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseModel<?> that = (BaseModel<?>) obj;
        return null != this.getId() && this.getId().equals(that.getId());
    }
}
