package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * 收付补录信息表
 */
@Entity
@Table(name = "ERP_HIS_DTL_COMFIELD", schema = "")
public class ErpHisDtlComField extends IdEntity implements java.io.Serializable{
    private String templateId; //映射erp_his_dtl_template中的template_id
    private String type;//类型类型:payer付款；payee收款;
    private String fieldCode;//字段代码
    private String textValue;//文本值
    private Date dateValue;//日期值
    private Long intValue;//数字值
    private String reverse1;//备用1
    private String reverse2;//备用2
    @Column(name = "TEMPLATE_ID", nullable = false, length = 32)
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    @Column(name = "TYPE", nullable = false, length = 20)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "FIELD_CODE", nullable = false, length = 20)
    public String getFieldCode() {
        return fieldCode;
    }
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
    @Column(name = "TEXT_VALUE", nullable = true, length = 100)
    public String getTextValue() {
        return textValue;
    }
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
    @Column(name ="DATE_VALUE",nullable=true)
    public Date getDateValue() {
        return dateValue;
    }
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }
    @Column(name ="INT_VALUE",nullable=true,length=10)
    public Long getIntValue() {
        return intValue;
    }
    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }
    @Column(name = "REVERSE1", nullable = true, length = 100)
    public String getReverse1() {
        return reverse1;
    }
    public void setReverse1(String reverse1) {
        this.reverse1 = reverse1;
    }
    @Column(name = "REVERSE2", nullable = true, length = 100)
    public String getReverse2() {
        return reverse2;
    }
    public void setReverse2(String reverse2) {
        this.reverse2 = reverse2;
    }
}
