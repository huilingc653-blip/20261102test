package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 虚拟账号记账单元信息
 * @date 2022-08-01 17:50:16
 */
@Entity
@Table(name = "BT_BANK_ACC_VIRTUAL", schema = "")
public class BtBankAccVirtual extends IdEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1291859506997225441L;
    /**银行账号ID*/
    private String bankAccId;
    /**分行（开户行号）*/
    private String bankCode;
    /**记账子单元编号（虚拟账号）*/
    private String virtualNo;
    /**记账子单元名称（虚拟账号名称）*/
    private String virtualName;
    /**额度控制标志 Y：允许透支 N：不允许透支*/
    private String overCtrl;
    /**退票处理方式 Y:退回原记账子单元 N：退回结算户*/
    private String backType;
    /**余额非零时是否可关闭 Y：可关闭，N：不可关闭*/
    private String closeType;
    @Column(name = "BANK_ACC_ID")
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    @Column(name = "VIRTUAL_NO")
    public String getVirtualNo() {
        return virtualNo;
    }
    public void setVirtualNo(String virtualNo) {
        this.virtualNo = virtualNo;
    }
    @Column(name = "VIRTUAL_NAME")
    public String getVirtualName() {
        return virtualName;
    }
    public void setVirtualName(String virtualName) {
        this.virtualName = virtualName;
    }
    @Column(name = "OVER_CTRL")
    public String getOverCtrl() {
        return overCtrl;
    }
    public void setOverCtrl(String overCtrl) {
        this.overCtrl = overCtrl;
    }
    @Column(name = "BACK_TYPE")
    public String getBackType() {
        return backType;
    }
    public void setBackType(String backType) {
        this.backType = backType;
    }
    @Column(name = "CLOSE_TYPE")
    public String getCloseType() {
        return closeType;
    }
    public void setCloseType(String closeType) {
        this.closeType = closeType;
    }
}
