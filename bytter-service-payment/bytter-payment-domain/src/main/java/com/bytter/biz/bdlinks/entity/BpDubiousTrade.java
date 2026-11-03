package com.bytter.biz.bdlinks.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: BP_DUBIOUS_TRADE 可疑交易
 * @author huangx
 * @date 2021-3-5 10:09:31
 * @version 1.0
 *
 */
@Entity
@Table(name = "BP_DUBIOUS_TRADE", schema = "")
@SuppressWarnings("serial")
public class BpDubiousTrade extends IdEntity implements java.io.Serializable {
    /**状态 0-可发送 1-不可发送 2-指令无效*/
    private String status;
    /**创建日期*/
    private Date createTime;
    /**付款指令凭证号*/
    private String businessId;
    /**外汇标志 0-非外汇（即bis_exc表中数据） 1-外汇（指令）(即bis_foreign_exc表中数据*/
    private String foreignFlag;
    @Column(name ="STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name ="CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name ="BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    @Column(name ="FOREIGN_FLAG")
    public String getForeignFlag() {
        return foreignFlag;
    }
    public void setForeignFlag(String foreignFlag) {
        this.foreignFlag = foreignFlag;
    }
}
