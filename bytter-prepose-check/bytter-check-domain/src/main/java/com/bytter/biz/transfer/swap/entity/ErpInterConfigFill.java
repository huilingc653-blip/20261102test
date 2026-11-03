package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.io.Serializable;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_INTER_CONFIG_FILL", catalog = "")
public class ErpInterConfigFill extends IdEntity implements Serializable{
    private String type;
    private String fieldCode;
    private String showName;
    private Long isMatching;
    private Long isMakeup;
    private Long isNotNull;
    private Long validSign;
    private Long orderNum;
    private Long inputType;
    private Long isInput;
    private String dataSource;
    @Column(name = "IS_MATCHING", nullable = true, precision = 0)
    public Long getIsMatching() {
        return isMatching;
    }
    public void setIsMatching(Long isMatching) {
        this.isMatching = isMatching;
    }
    @Column(name = "IS_MAKEUP", nullable = true, precision = 0)
    public Long getIsMakeup() {
        return isMakeup;
    }
    public void setIsMakeup(Long isMakeup) {
        this.isMakeup = isMakeup;
    }
    @Column(name = "IS_NOT_NULL", nullable = true, precision = 0)
    public Long getIsNotNull() {
        return isNotNull;
    }
    public void setIsNotNull(Long isNotNull) {
        this.isNotNull = isNotNull;
    }
    @Column(name = "VALID_SIGN", nullable = true, precision = 0)
    public Long getValidSign() {
        return validSign;
    }
    public void setValidSign(Long validSign) {
        this.validSign = validSign;
    }
    @Column(name = "INPUT_TYPE",  nullable = true, precision = 0)
    public Long getInputType() {
        return inputType;
    }
    public void setInputType(Long inputType) {
        this.inputType = inputType;
    }
    @Column(name = "IS_INPUT", nullable = true, precision = 0)
    public Long getIsInput() {
        return isInput;
    }
    public void setIsInput(Long isInput) {
        this.isInput = isInput;
    }
    @Column(name = "TYPE", nullable = true, length = 20)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "FIELD_CODE", nullable = true, length = 50)
    public String getFieldCode() {
        return fieldCode;
    }
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
    @Column(name = "SHOW_NAME", nullable = true, length = 100)
    public String getShowName() {
        return showName;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    @Column(name = "ORDERNUM", nullable = true, precision = 0)
    public Long getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }
    @Column(name = "DATA_SOURCE", nullable = true, length = 20)
    public String getDataSource() {
        return dataSource;
    }
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
