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
 * @Description: 票据池接口
 * @date 2019-12-30
 */
@Entity
@Table(name = "AB_POOLIF", schema = "")
@SuppressWarnings("serial")
public class AbPoolif extends IdEntity implements java.io.Serializable {
    /**
     * 池接口代码
     */
    private String pifCode;
    /**
     * 池接口名称
     */
    private String pifName;
    /**
     * 银行接口代码
     */
    private String bifCode;
    /**
     * 银行接口代码ID
     */
    private String bifId;
    /**
     * 备注
     */
    private String rmk;
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

    @Column(name = "BIF_ID", length = 32)
    public String getBifId() {
        return bifId;
    }

    public void setBifId(String bifId) {
        this.bifId = bifId;
    }

    @Column(name = "PIF_CODE", nullable = true, length = 12)
    public String getPifCode() {
        return pifCode;
    }
    public void setPifCode(String pifCode) {
        this.pifCode = pifCode;
    }
    @Column(name = "PIF_NAME", nullable = true, length = 100)
    public String getPifName() {
        return pifName;
    }
    public void setPifName(String pifName) {
        this.pifName = pifName;
    }
    @Column(name = "BIF_CODE", nullable = true, length = 10)
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
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
        return "AbPoolif{" +
                "pifCode='" + pifCode + '\'' +
                ", pifName='" + pifName + '\'' +
                ", bifCode='" + bifCode + '\'' +
                ", rmk='" + rmk + '\'' +
                ", validSign='" + validSign + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", updateId='" + updateId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
