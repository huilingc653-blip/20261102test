package com.bytter.bill.domain.billPool.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: AbPoolDtl
 * @Description: 票据交易明细实体
 * @author jinyong.cao
 * @date 2020/1/10 17:34:09
 */
@Entity
@Table(name = "AB_POOL_DTL", schema = "")
public class AbPoolDtl extends IdEntity implements java.io.Serializable  {
	/**上一个池流水ID*/
	private String lastid;
    /**
     * 从表ID
     */
    private String vvId;
    /**
     * 票据池ID
     */
    private String bpId;
    /**
     * 业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款
     */
    private String briefId;
    /**
     * 申请单号
     */
    private String appNo;
    /**
     * 票据号码
     */
    private String origNo;
    /**
     * 出入池方式：1质押2托管
     */
    private String inoutType;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2
     */
    private String optStatus;
    /**
     * 状态：1启用、0在途、2作废
     */
    private String validSign;
    /**
     * 创建人名称
     */
    private String createId;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新人名称
     */
    private String updateId;
    /**
     * 更新日期
     */
    private Date updateTime;
    /**
     * 入池撤销id
     */
    private String repealId;
    @Column(name = "VV_ID", nullable = true, length = 32)
    public String getVvId() {
        return vvId;
    }
    @Column(name = "BP_ID", nullable = true, length = 32)
    public String getBpId() {
        return bpId;
    }
    @Column(name = "BRIEF_ID", nullable = true, length = 4)
    public String getBriefId() {
        return briefId;
    }
    @Column(name = "APP_NO", nullable = true, length = 50)
    public String getAppNo() {
        return appNo;
    }
    @Column(name = "ORIG_NO", nullable = true, length = 50)
    public String getOrigNo() {
        return origNo;
    }
    @Column(name = "INOUT_TYPE", nullable = true, length = 10)
    public String getInoutType() {
        return inoutType;
    }
    @Column(name = "STATUS", nullable = true, length = 2)
    public Integer getStatus() {
        return status;
    }
    @Column(name = "OPT_STATUS", nullable = true, length = 2)
    public String getOptStatus() {
        return optStatus;
    }
    public void setVvId(String vvId) {
        this.vvId = vvId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public void setInoutType(String inoutType) {
        this.inoutType = inoutType;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    @Column(name = "CREATE_ID", nullable = true, length = 32)
    public String getCreateId() {
        return createId;
    }
    public void setCreateId(String createId) {
        this.createId = createId;
    }
    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_ID", nullable = true, length = 32)
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name = "VALID_SIGN")
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Column(name ="LASTID")
	public String getLastid() {
		return lastid;
	}
	public void setLastid(String lastid) {
		this.lastid = lastid;
	}
    @Column(name ="REPEAL_ID")
    public String getRepealId() {
        return repealId;
    }
    public void setRepealId(String repealId) {
        this.repealId = repealId;
    }
}
