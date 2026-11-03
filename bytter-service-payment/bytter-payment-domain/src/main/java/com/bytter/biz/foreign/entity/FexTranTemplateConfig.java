package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 交易模板配置
 */


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fex_tran_template_config", schema = "")
public class FexTranTemplateConfig extends IdEntity implements java.io.Serializable {
    private static final long serialVersionUID = -7699394788668936226L;
    /**
     *
     */
    private String parentId;
    /**
     * 模板编号
     */
    private String templateNo;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 模板描述
     */
    private String templateDesc;
    /**
     * 银行类型ID
     */
    private String bankTypeId;

    /**
     *银行接口ID
     */
    private String bifId;

    /**
     * 国家ID
     */
    private String countryId;

    /**
     * 支付类型
     */
    private String payTypeId;
    /**
     *
     */

    private String remark;
    /**
     *
     */
    private String validSign;
    /**
     * 是否删除 0否 1是
     */

    private String isDelete;
    /**
     * 创建人ID
     */

    private String createBy;
    /**
     * 创建时间
     */

    private Date createDate;
    /**
     * 修改人ID
     */

    private String updateBy;
    /**
     * 修改时间
     */

    private Date updateDate;
    private Integer sort;


    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    @Column(name = "template_no")
    public String getTemplateNo() {
        return templateNo;
    }

    @Column(name = "template_name")
    public String getTemplateName() {
        return templateName;
    }

    @Column(name = "template_desc")
    public String getTemplateDesc() {
        return templateDesc;
    }

    @Column(name = "bank_type_id")
    public String getBankTypeId() {
        return bankTypeId;
    }

    @Column(name = "bif_id")
    public String getBifId() {
        return bifId;
    }

    @Column(name = "country_id")
    public String getCountryId() {
        return countryId;
    }

    @Column(name = "pay_type_id")
    public String getPayTypeId() {
        return payTypeId;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    @Column(name = "valid_sign")
    public String getValidSign() {
        return validSign;
    }

    @Column(name = "is_delete")
    public String getIsDelete() {
        return isDelete;
    }

    @Column(name = "create_by")
    public String getCreateBy() {
        return createBy;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    @Column(name = "update_by")
    public String getUpdateBy() {
        return updateBy;
    }

    @Column(name = "update_date")
    public Date getUpdateDate() {
        return updateDate;
    }

    @Column(name = "sort")
    public Integer getSort() {
        return sort;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public void setBifId(String bifId) {
        this.bifId = bifId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setPayTypeId(String payTypeId) {
        this.payTypeId = payTypeId;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }



    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
