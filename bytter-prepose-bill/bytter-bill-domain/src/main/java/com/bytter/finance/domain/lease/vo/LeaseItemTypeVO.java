package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author wanggl
 * @Date 2022-4-2
 * @Version 1.0
 */
public class LeaseItemTypeVO implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 类别编码
     */
    @BtCompositeSearch(fieldName = "c.code")
    private String code;
    /**
     * 类别名称
     */
    @BtCompositeSearch(fieldName = "c.name")
    private String name;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "c.rmk")
    private String rmk;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "c.status")
    private Integer status;
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
