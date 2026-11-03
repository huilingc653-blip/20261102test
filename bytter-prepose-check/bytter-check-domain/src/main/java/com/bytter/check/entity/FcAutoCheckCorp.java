package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 自动对账配置关联单位
 * @date 2022-09-20
 */
@Entity
@Table(name = "FC_AUTO_CHECK_CORP", schema = "")
public class FcAutoCheckCorp extends IdEntity implements java.io.Serializable {
    /**
     * 自动对账策略代码
     */
    private String autoCheckCode;
    /**
     * 对账类型
     */
    private String checkType;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 核对标准
     */
    private String checkTheStandard;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    @Column(name = "AUTO_CHECK_CODE")
    public String getAutoCheckCode() {
        return autoCheckCode;
    }
    public void setAutoCheckCode(String autoCheckCode) {
        this.autoCheckCode = autoCheckCode;
    }
    @Column(name = "CHECK_TYPE")
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CHECK_THE_STANDARD")
    public String getCheckTheStandard() {
        return checkTheStandard;
    }
    public void setCheckTheStandard(String checkTheStandard) {
        this.checkTheStandard = checkTheStandard;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
