package com.bytter.biz.inneraccount.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author huangx
 * @version 1.0
 * @Title: 离线流水智能导入配置表Entity
 * @Description: BIS_ACC_DTL_IMPORT_CONFIGDTL
 * @date 2020-07-31 15:02:30
 */
@Entity
@Table(name = "BIS_ACC_DTL_IMPORT_CONFIGDTL")
public class BisAccDtlImportConfigdtl extends IdEntity implements java.io.Serializable {
    /** 导入格式主表ID */
    private String icId;
    /**
     * 对应字段
     * 本方账号bankAcc  对方账号oppAccNo  对方账户名oppAccName  对方开户行oppAccBank
     * 支出金额payMoney 收入金额incomeMoney  币别curId 交易日期transDate 交易时间transTime
     * 余额bal 摘要remark  用途uses 项目信息projectRemark
     */
    private String fieldKey;
    /** 对应起始值 */
    private String startVal;
    /** 对应结束值 */
    private String endVal;
    @Column(name ="ic_Id",nullable=false,length=32)
    public String getIcId() {
        return icId;
    }
    public void setIcId(String icId) {
        this.icId = icId;
    }
    @Column(name ="FIELD_KEY",nullable=true,length=20)
    public String getFieldKey() {
        return fieldKey;
    }
    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }
    @Column(name ="START_VAL",nullable=true,length=20)
    public String getStartVal() {
        return startVal;
    }
    public void setStartVal(String startVal) {
        this.startVal = startVal;
    }
    @Column(name ="END_VAL",nullable=true,length=20)
    public String getEndVal() {
        return endVal;
    }
    public void setEndVal(String endVal) {
        this.endVal = endVal;
    }
}
