package com.bytter.system.entity;

import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * TTypegroup entity.
 */
@Entity
@Table(name = "SYS_DICT_GROUP")
@DynamicUpdate
public class DictGroup extends IdEntity implements Serializable {
    private String groupName;
    private String groupCode;
    private Date createDate;//创建时间
    private String createName;//创建用户
    @Column(name = "TYPE_GROUP_NAME", length = 50)
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    @Column(name = "TYPE_GROUP_CODE", length = 50)
    public String getGroupCode() {
        return this.groupCode;
    }
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "CREATE_NAME", length = 36)
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
}
