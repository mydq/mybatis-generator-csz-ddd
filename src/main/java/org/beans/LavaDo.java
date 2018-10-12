package org.beans;

/**
 * @Author: csz
 * @Date: 2018/10/12 14:17
 */
import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import org.beans.util.SpringUtil;

public abstract class LavaDo {
    private Long id;
    private Date gmtCreate;
    private String creator;
    private Date gmtModified;
    private String modifier;
    private String isDeleted;

    public LavaDo() {
    }

    @JSONField(
            serialize = false
    )
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getGmtModified() {
        return this.gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }

    public String getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted == null ? null : isDeleted.trim();
    }


    @Override
    public String toString() {
        StringBuilder sb = (new StringBuilder("[")).append(this.getClass().getName()).append("]").append("ID:").append(this.getId() == null ? "Null" : this.getId());
        return sb.toString();
    }

    @JSONField(
            serialize = false
    )
    public abstract String getBoQualifiedIntfName();

    @JSONField(
            serialize = false
    )
    public LavaBo getLavaBo() throws ClassNotFoundException {
        return (LavaBo) SpringUtil.getBeansByType(Class.forName(this.getBoQualifiedIntfName()));
    }

    protected String getDoClassName() {
        return this.getClass().getCanonicalName();
    }
}
