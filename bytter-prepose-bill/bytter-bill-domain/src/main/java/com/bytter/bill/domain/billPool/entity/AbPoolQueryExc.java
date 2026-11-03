package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Entity
 * @Description: 票据池指令表
 * @date 2020-11-12
 */
@Entity
@Table(name = "AB_POOL_QUERY_EXC", schema = "")
@SuppressWarnings("serial")
public class AbPoolQueryExc extends IdEntity implements java.io.Serializable {
    /**
     * 池接口代码
     */
    private String corpId;
    /**
     * 票据池ID
     */
    private String bpId;
    /**
     * 查询类型（票据池额度01\出入池登记簿02\在池登记簿03）
     */
    private String excType;
    /**
     * 操作开始日期
     */
    private Date operDateStart;
    /**
     * 操作结束日期
     */
    private Date operDateEnd;
    /**
     * 指令状态
     * -3 银行交易失败
     * -1 发送失败
     * -2 交易打回
     * 16 待发送
     * 18 银行处理中
     * 19 银行交易成功
     */
    private String status;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新日期
     */
    private Date updateTime;
    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "BP_ID", nullable = true, length = 32)
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    @Column(name = "EXC_TYPE", nullable = true, length = 4)
    public String getExcType() {
        return excType;
    }
    public void setExcType(String excType) {
        this.excType = excType;
    }
    @Column(name = "OPER_DATE_START")
    public Date getOperDateStart() {
        return operDateStart;
    }
    public void setOperDateStart(Date operDateStart) {
        this.operDateStart = operDateStart;
    }
    @Column(name = "OPER_DATE_END")
    public Date getOperDateEnd() {
        return operDateEnd;
    }
    public void setOperDateEnd(Date operDateEnd) {
        this.operDateEnd = operDateEnd;
    }
    @Column(name = "STATUS", nullable = true, length = 2)
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public String toString() {
        return "AbPoolQueryExc{" +
                "corpId='" + corpId + '\'' +
                ", bpId='" + bpId + '\'' +
                ", excType='" + excType + '\'' +
                ", operDateStart=" + operDateStart +
                ", operDateEnd=" + operDateEnd +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
