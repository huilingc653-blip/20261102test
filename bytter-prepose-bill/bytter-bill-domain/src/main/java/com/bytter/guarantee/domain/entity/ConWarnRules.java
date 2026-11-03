package com.bytter.guarantee.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: ConWarnRules
 * @Description: 合同预警规则Entity
 * @author wangzy
 * @date 2019/11/4 15:49
 *
 */
@Entity
@Table(name = "CON_WARN_RULES", schema = "")
public class ConWarnRules extends IdEntity implements java.io.Serializable {
    /** 合同类型Id */
    private String conTypeId;
    /** 预警提前天数 */
    private Integer beforDay;
    /** 预警间隔天数 */
    private Integer intervalDay;
    /** 是否启用 */
    private String validSign;
    /** 操作人ID */
    private String opId;
    /** 操作时间 */
    private Date opDate;
    @Column(name = "CON_TYPE_ID")
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    @Column(name = "BEFOR_DAY")
    public Integer getBeforDay() {
        return beforDay;
    }
    public void setBeforDay(Integer beforDay) {
        this.beforDay = beforDay;
    }
    @Column(name = "INTERVAL_DAY")
    public Integer getIntervalDay() {
        return intervalDay;
    }
    public void setIntervalDay(Integer intervalDay) {
        this.intervalDay = intervalDay;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "OP_ID")
    public String getOpId() {
        return opId;
    }
    public void setOpId(String opId) {
        this.opId = opId;
    }
    @Column(name = "OP_DATE")
    public Date getOpDate() {
        return opDate;
    }
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }
}
