package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.io.Serializable;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_INTER_CONFIG", catalog = "")
public class ErpInterConfig extends IdEntity implements Serializable{
    private String type;
    private String code;
    private String name;
    private String value;
    private Boolean validSign;
    public ErpInterConfig(){
        super();
    }
    public ErpInterConfig(String type, String code, String value) {
        super();
        this.type = type;
        this.code = code;
        this.value = value;
        this.validSign = true;
    }
    @Basic
    @Column(name = "TYPE", nullable = true, length = 20)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Basic
    @Column(name = "CODE", nullable = true, length = 50)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Basic
    @Column(name = "NAME", nullable = true, length = 100)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Basic
    @Column(name = "VALUE", nullable = true, length = 20)
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Basic
    @Column(name = "VALID_SIGN", nullable = true, precision = 0)
    public Boolean getValidSign() {
        return validSign;
    }
    public void setValidSign(Boolean validSign) {
        this.validSign = validSign;
    }
}
