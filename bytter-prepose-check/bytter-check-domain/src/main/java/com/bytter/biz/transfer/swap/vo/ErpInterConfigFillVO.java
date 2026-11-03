package com.bytter.biz.transfer.swap.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
public class ErpInterConfigFillVO implements Serializable {
    private String id;
    private String type;
    @Dict(inject="codeName",tableName="ERP_DATATYPE_FIELD",valueFieldName="FIELD_CODE",showFiledName="field_name")
    private String fieldCode;
    private String showName;
    private Long isMatching;
    private Long isMakeup;
    private Long isNotNull;
    /**有效标志*/
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    private Long validSign;
    private Long orderNum;
    private Long inputType;
    private Long isInput;
    private String dataSource;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public Long getIsMatching() {
        return isMatching;
    }
    public void setIsMatching(Long isMatching) {
        this.isMatching = isMatching;
    }
    public Long getIsMakeup() {
        return isMakeup;
    }
    public void setIsMakeup(Long isMakeup) {
        this.isMakeup = isMakeup;
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
    public Long getInputType() {
        return inputType;
    }
    public void setInputType(Long inputType) {
        this.inputType = inputType;
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
