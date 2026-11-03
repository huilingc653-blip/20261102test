package com.bytter.guarantee.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同申请流水表Entity
 * @Description: CON_INFO_RECORD合同申请流水表
 * @date 2019-11-04
 */
@Entity
@Table(name = "CON_INFO_RECORD", schema = "")
@SuppressWarnings("serial")
public class ConInfoRecord extends IdEntity implements java.io.Serializable {
    /**
     * 主信息ID
     */
    private String mid;
    /**
     * 上一笔流水Id
     */
    private String lastId;
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 业务状态
     */
    private Integer status;
    /**
     * 变更\履约\终止原因
     */
    private String opMsg;
    /**
     * 变更\履约\终止时间
     */
    private Date opDate;
    /**
     * 履约金额
     */
    private BigDecimal opMoney;
    /**
     * 履约\终止申请编号
     */
    private String opNo;
    /**
     * 外部业务ID
     */
    private String outerId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 打回原因
     */
    private String backMsg;
    /**
     * 业务申请附件ID
     */
    private String attachmentId;
    /**
     * 创建人id
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人id
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 方法: 取得主信息ID
     *
     * @return: java.lang.String 主信息ID
     */
    @Column(name = "MID", nullable = true, length = 50)
    public String getMid() {
        return mid;
    }
    /**
     * 设置主信息ID
     */
    public void setMid(String mid) {
        this.mid = mid;
    }
    /**
     * 方法: 取得上一笔流水ID
     *
     * @return: 上一笔流水ID
     */
    @Column(name = "LASTID", nullable = true, length = 32)
    public String getLastId() {
        return lastId;
    }
    /**
     * 设置上一笔流水ID
     */
    public void setLastId(String lastId) {
        this.lastId = lastId;
    }
    /**
     * 方法: 取得业务类型
     *
     * @return: java.lang.String 业务类型:01起草、02签署、03登记、04变更、05履约、06终止
     */
    @Column(name = "BUS_TYPE", nullable = true, length = 10)
    public String getBusType() {
        return busType;
    }
    /**
     * 业务类型:01起草、02签署、03登记、04变更、05履约、06终止
     */
    public void setBusType(String busType) {
        this.busType = busType;
    }
    /**
     * 方法: 取得业务状态
     *
     * @return: java.lang.Integer 业务状态： 起草：0暂存、11待审批、-1审批打回、95审批通过、-2签署打回、-99已删除 签署：0待签署、11待审批、-1审批打回、95审批通过
     *          登记、变更、履约、终止：0暂存、11待审批、-1审批打回、95审批通过、-99已删除
     */
    @Column(name = "STATUS", nullable = true, length = 6)
    public Integer getStatus() {
        return status;
    }
    /**
     * 设置 业务状态： 起草：0暂存、11待审批、-1审批打回、95审批通过、-2签署打回、-99已删除 签署：0待签署、11待审批、-1审批打回、95审批通过
     * 登记、变更、履约、终止：0暂存、11待审批、-1审批打回、95审批通过、-99已删除
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得变更\履约\终止原因
     *
     * @return: java.lang.String 变更\履约\终止原因
     */
    @Column(name = "OP_MSG", nullable = true, length = 200)
    public String getOpMsg() {
        return opMsg;
    }
    /**
     * 设置 变更\履约\终止原因
     */
    public void setOpMsg(String opMsg) {
        this.opMsg = opMsg;
    }
    /**
     * 方法: 取得变更\履约\终止时间
     *
     * @return: java.util.Date 变更\履约\终止时间
     */
    @Column(name = "OP_DATE", nullable = true)
    public Date getOpDate() {
        return opDate;
    }
    /**
     * 设置 取得变更\履约\终止时间
     */
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }
    /**
     * 方法: 取得履约\终止申请编号
     *
     * @return: java.lang.String 变更\履约\终止原因
     */
    @Column(name = "OP_NO", nullable = true, length = 50)
    public String getOpNo() {
        return opNo;
    }
    /**
     * 设置履约\终止申请编号
     */
    public void setOpNo(String opNo) {
        this.opNo = opNo;
    }
    /**
     * 方法: 取得
     *
     * @return: 外部业务ID
     */
    @Column(name = "OUTER_ID", nullable = true, length = 10)
    public String getOuterId() {
        return outerId;
    }
    /**
     * 设置外部业务ID
     */
    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }
    /**
     * 方法: 取得 备注
     *
     * @return:
     */
    @Column(name = "RMK", nullable = true, length = 500)
    public String getRmk() {
        return rmk;
    }
    /**
     * 设置 备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得 打回原因
     *
     * @return: 打回原因
     */
    @Column(name = "BACK_MSG", nullable = true, length = 100)
    public String getBackMsg() {
        return backMsg;
    }
    /**
     * 设置 打回原因
     */
    public void setBackMsg(String backMsg) {
        this.backMsg = backMsg;
    }
    /**
     * 方法: 取得 附件ID
     *
     * @return: 附件ID
     */
    @Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
    public String getAttachmentId() {
        return attachmentId;
    }
    /**
     * 设置 附件ID
     */
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    /**
     * 方法: 取得 创建人名称
     *
     * @return: 创建人名称
     */
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return createId;
    }
    /**
     * 设置 创建人名称
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    /**
     * 方法: 取得 创建日期
     *
     * @return: 创建日期
     */
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * 方法: 取得 更新人名称
     *
     * @return: 更新人名称
     */
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return updateId;
    }
    /**
     * 设置 更新人名称
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    /**
     * 方法: 取得 更新日期
     *
     * @return:更新日期
     */
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置更新日期
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 方法: 取得有效标志
     *
     * @return: java.lang.String 有效标志：0业务中1业务完成2已作废
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return validSign;
    }
    /**
     * 设置 有效标志：0业务中1业务完成2已作废
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "OP_MONEY")
    public BigDecimal getOpMoney() {
        return opMoney;
    }
    public void setOpMoney(BigDecimal opMoney) {
        this.opMoney = opMoney;
    }
    @Override
    public String toString() {
        return "ConInfoRecord{" + "mid='" + mid + '\'' + ", lastId='" + lastId + '\'' + ", busType='" + busType + '\''
                + ", status=" + status + ", opMsg='" + opMsg + '\'' + ", opDate=" + opDate + ", opNo='" + opNo + '\''
                + ", opMoney='" + opMoney + '\'' + ", outerId='" + outerId + '\'' + ", rmk='" + rmk + '\''
                + ", backMsg='" + backMsg + '\'' + ", attachmentId='" + attachmentId + '\'' + ", createId='" + createId
                + '\'' + ", createTime=" + createTime + ", updateId='" + updateId + '\'' + ", updateTime=" + updateTime
                + ", validSign='" + validSign + '\'' + '}';
    }
}
