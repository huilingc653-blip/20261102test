package com.bytter.biz.transfer.swap.vo;
import java.io.Serializable;
import java.util.Date;
public class ErpHisDtlComFieldVO implements Serializable {
    private String id;
    private String templateId; //映射erp_his_dtl_template中的template_id
    private String type;//类型类型:payer付款；payee收款;
    private String fieldCode;//字段代码
    private String showName;
    private String textValue;//文本值
    private Date dateValue;//日期值
    private Long intValue;//数字值
    private String reverse1;//备用1
    private String reverse2;//备用2
    private Long isNotNull; //是否必填：0否1是
    private Long validSign; //有效标记:0无效1有效
    private Long orderNum; //排序字段
    private Long isInput;// 是否手动输入（0否,1是）
    private String dataSource; //数据来源
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getFieldCode() {
        return fieldCode;
    }
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
    public String getShowName() {
        return showName;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    public String getTextValue() {
        return textValue;
    }
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }
    public Date getDateValue() {
        return dateValue;
    }
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }
    public Long getIntValue() {
        return intValue;
    }
    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }
    public String getReverse1() {
        return reverse1;
    }
    public void setReverse1(String reverse1) {
        this.reverse1 = reverse1;
    }
    public String getReverse2() {
        return reverse2;
    }
    public void setReverse2(String reverse2) {
        this.reverse2 = reverse2;
    }
    public Long getIsNotNull() {
        return isNotNull;
    }
    public void setIsNotNull(Long isNotNull) {
        this.isNotNull = isNotNull;
    }
    public Long getValidSign() {
        return validSign;
    }
    public void setValidSign(Long validSign) {
        this.validSign = validSign;
    }
    public Long getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }
    public Long getIsInput() {
        return isInput;
    }
    public void setIsInput(Long isInput) {
        this.isInput = isInput;
    }
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
