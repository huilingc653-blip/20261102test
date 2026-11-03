package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 用途银行类别中间表
 * @date 2022-11-23 09:57:15
 */
@Entity
@Table(name = "BT_PURPOSE_BANK_TYPE", schema = "")
@SuppressWarnings("serial")
public class BtPurposeBankType extends IdEntity {
    private String purposeId;
    /**
     * 银行类别ID
     */
    private String bankTypeId;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  用途ID
     */
    @Column(name = "PURPOSE_ID", nullable = false, length = 32)
    public String getPurposeId() {
        return purposeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  用途ID
     */
    public void setPurposeId(String purposeId) {
        this.purposeId = purposeId;
    }
    @Column(name = "BANK_TYPE_ID", nullable = false, length = 32)
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
}
