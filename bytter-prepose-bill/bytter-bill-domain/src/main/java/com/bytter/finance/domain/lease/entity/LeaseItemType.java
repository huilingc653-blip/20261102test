package com.bytter.finance.domain.lease.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @Title 融资租赁基础数据
 * @Description
 * @Author wanggl
 * @Date 2022-4-2
 * @Version 1.0
 */
@Entity
@Table(name = "CMS_TENANCY_ITEMS_TYPES", schema = "")
@SuppressWarnings("serial")
public class LeaseItemType extends IdEntity implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 类别编码
     */
    private String code;
    /**
     * 类别名称
     */
    private String name;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "ID")
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
