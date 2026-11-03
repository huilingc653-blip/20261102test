package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 虚拟账号操作指令表
 * @date 2022-08-01 17:50:16
 */
@Entity
@Table(name = "BIS_VIRTUAL_ACC_EXC", schema = "")
public class BisVirtualAccExc extends IdEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1291859506997225441L;
    /**银行账号*/
    private String bankAcc;
    /**开户行号*/
    private String bankCode;
    /**虚拟账号*/
    private String virtualNo;
    /**虚拟账号名称*/
    private String virtualName;
    /**额度控制标志 Y：允许透支 N：不允许透支*/
    private String overCtrl;
    /**退票处理方式 Y:退回原记账子单元 N：退回结算户*/
    private String backType;
    /**余额非零时是否可关闭 Y：可关闭，N：不可关闭*/
    private String closeType;
    /**申请日期*/
    private Date reqDate;
    /**指令状态 3-待发送 2-失败 1-待查结果 0-成功*/
    private String voucherStat;
    /**接口代码*/
    private String bifCode;
    /**返回信息*/
    private String returnMsg;
    /**返回时间*/
    private Date returnTime;
    /**操作类型 1-新增 2-修改*/
    private String busType;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    @Column(name = "BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
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
    @Column(name = "REQ_DATE")
    public Date getReqDate() {
        return reqDate;
    }
    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }
    @Column(name = "VOUCHER_STAT")
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    @Column(name = "BIF_CODE")
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    @Column(name = "RETURN_MSG")
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    @Column(name = "RETURN_TIME")
    public Date getReturnTime() {
        return returnTime;
    }
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
    @Column(name = "BUS_TYPE")
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
