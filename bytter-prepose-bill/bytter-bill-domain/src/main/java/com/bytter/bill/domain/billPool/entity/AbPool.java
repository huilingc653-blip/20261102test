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
 * @Description: 票据池
 * @date 2020-1-3
 */
@Entity
@Table(name = "AB_POOL", schema = "")
@SuppressWarnings("serial")
public class AbPool extends IdEntity implements java.io.Serializable {
    /**
     * 票据池代码
     */
    private String bpCode;
    /**
     * 票据池名称
     */
    private String bpName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 池接口Id
     */
    private String pifId;
    /**
     * 池类型:1：结算中心\ 2：财务公司\3：银行  默认为银行
     */
    private String bpType;
    /**
     * 池模式:1：集团票据池（统收统支）\ 2：集团票据池（自收自用）\3：单一票据池  默认为1
     */
    private String bpMode;
    /**
     * 池控制机构
     */
    private String bpOrg;
    /**
     * 状态：1启用、0禁用 默认禁用
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
    @Column(name = "BP_CODE", nullable = true, length = 21)
    public String getBpCode() {
        return bpCode;
    }
    public void setBpCode(String bpCode) {
        this.bpCode = bpCode;
    }
    @Column(name = "BP_NAME", nullable = true, length = 100)
    public String getBpName() {
        return bpName;
    }
    public void setBpName(String bpName) {
        this.bpName = bpName;
    }
    @Column(name = "PIF_ID", nullable = true, length = 32)
    public String getPifId() {
        return pifId;
    }
    public void setPifId(String pifId) {
        this.pifId = pifId;
    }
    @Column(name = "BP_TYPE", nullable = true, length = 2)
    public String getBpType() {
        return bpType;
    }
    public void setBpType(String bpType) {
        this.bpType = bpType;
    }
    @Column(name = "BP_MODE", nullable = true, length = 2)
    public String getBpMode() {
        return bpMode;
    }
    public void setBpMode(String bpMode) {
        this.bpMode = bpMode;
    }
    @Column(name = "BP_ORG", nullable = true, length = 100)
    public String getBpOrg() {
        return bpOrg;
    }
    public void setBpOrg(String bpOrg) {
        this.bpOrg = bpOrg;
    }
    @Column(name = "RMK", nullable = true, length = 200)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "VALID_SIGN", nullable = true, length = 2)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
    @Override
    public String toString() {
        return "AbPool{" +
                "bpCode='" + bpCode + '\'' +
                ", bpName='" + bpName + '\'' +
                ", rmk='" + rmk + '\'' +
                ", pifId='" + pifId + '\'' +
                ", bpType='" + bpType + '\'' +
                ", bpOrg='" + bpOrg + '\'' +
                ", validSign='" + validSign + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
