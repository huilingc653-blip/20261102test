package com.bytter.biz.bdlinks.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: SYS_CA_INFO
 * @date 2017-10-26 16:56:53
 * 指令表的中间账号表
 */
@Entity
@Table(name = "BIS_EXC_MID_ACC")
public class BisExcMidAcc  extends IdEntity implements java.io.Serializable {

    /**租户ID*/
    private String tenantId;

    /**指令编号*/
    private String bisExcId;
    /**付款账号*/
    private String midPayerAccNo;
    /**付款人*/
    private String midPayerAccName;
    /**付款开户行*/
    private String midPayerBankName;

    @Column(name ="TENANT_ID",nullable=true,length=32)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name ="BIS_EXC_ID",nullable=false,length=32)
    public String getBisExcId() {
        return bisExcId;
    }
    public void setBisExcId(String bisExcId) {
        this.bisExcId = bisExcId;
    }
    @Column(name ="MID_PAYER_ACC_NO",nullable=true,length=50)
    public String getMidPayerAccNo() {
        return midPayerAccNo;
    }
    public void setMidPayerAccNo(String midPayerAccNo) {
        this.midPayerAccNo = midPayerAccNo;
    }
    @Column(name ="MID_PAYER_ACC_NAME",nullable=true,length=140)
    public String getMidPayerAccName() {
        return midPayerAccName;
    }
    public void setMidPayerAccName(String midPayerAccName) {
        this.midPayerAccName = midPayerAccName;
    }
    @Column(name ="MID_PAYER_BANK_NAME",nullable=true,length=140)
    public String getMidPayerBankName() {
        return midPayerBankName;
    }
    public void setMidPayerBankName(String midPayerBankName) {
        this.midPayerBankName = midPayerBankName;
    }
}
