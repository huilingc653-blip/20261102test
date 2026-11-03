package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.io.Serializable;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_INTER_CONFIG_SET", catalog = "")
public class ErpInterConfigSet extends IdEntity implements Serializable{
    private String type;
    private Boolean isLineSign;
    private String objectCode;
    private String rangeCode;
    private String matchtempId;
    private String matchFieldId;
    private Long ordernum;
    @Basic
    @Column(name = "TYPE", nullable = true, length = 20)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Basic
    @Column(name = "IS_LINE_SIGN", nullable = true, precision = 0)
    public Boolean getLineSign() {
        return isLineSign;
    }
    public void setLineSign(Boolean lineSign) {
        isLineSign = lineSign;
    }
    @Basic
    @Column(name = "OBJECT_CODE", nullable = true, length = 20)
    public String getObjectCode() {
        return objectCode;
    }
    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }
    @Basic
    @Column(name = "RANGE_CODE", nullable = true, length = 50)
    public String getRangeCode() {
        return rangeCode;
    }
    public void setRangeCode(String rangeCode) {
        this.rangeCode = rangeCode;
    }
    @Basic
    @Column(name = "MATCHTEMP_ID", nullable = true, length = 32)
    public String getMatchtempId() {
        return matchtempId;
    }
    public void setMatchtempId(String matchtempId) {
        this.matchtempId = matchtempId;
    }
    @Basic
    @Column(name = "MATCH_FIELD_ID", nullable = true, length = 32)
    public String getMatchFieldId() {
        return matchFieldId;
    }
    public void setMatchFieldId(String matchFieldId) {
        this.matchFieldId = matchFieldId;
    }
    @Basic
    @Column(name = "ORDERNUM", nullable = true, precision = 0)
    public Long getOrdernum() {
        return ordernum;
    }
    public void setOrdernum(Long ordernum) {
        this.ordernum = ordernum;
    }
}
