package com.bytter.bill.domain.billPool.vo;
import java.util.Date;
/**
 * @author zhenghj
 * @version 1.0
 * @Description: 票据池指令Vo
 * @date 2020-11-12
 */
@SuppressWarnings("serial")
public class AbPoolExcVO implements java.io.Serializable {
    /**
     * 票据池指令Id
     */
    private String id;
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
     * 开始日期
     */
    private Date operDateStart;
    /**
     * 结束日期
     */
    private Date operDateEnd;
    /**
     * 指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getExcType() {
        return excType;
    }
    public void setExcType(String excType) {
        this.excType = excType;
    }
    public Date getOperDateStart() {
        return operDateStart;
    }
    public void setOperDateStart(Date operDateStart) {
        this.operDateStart = operDateStart;
    }
    public Date getOperDateEnd() {
        return operDateEnd;
    }
    public void setOperDateEnd(Date operDateEnd) {
        this.operDateEnd = operDateEnd;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Override
    public String toString() {
        return "AbPoolExcVO{" +
                "id='" + id + '\'' +
                ", corpId='" + corpId + '\'' +
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
