package com.bytter.supply.domain.platform.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

import java.util.Date;

/**
 * @Title: ScfFunderInfoVO
 * @Description: 资金方信息
 * @version 1.0
 */
public class ScfFunderInfoVo {

    private String id;
    /** 资金方编码 */
    @BtCompositeSearch(fieldName = "CODE")
    private String code;
    /** 机构类型 01-保理公司 02-银行 03-其他*/
    @Dict(dictGroup = "scf.fund.institution.type", inject = "typeName")
    private String type;
    /** 机构类型名称 */
    private String typeName;
    /** 银行行号 */
    private String bankNo;
    /** 资金方名称 */
    @BtCompositeSearch(fieldName = "NAME")
    private String name;
    /** 资金方标识 */
    private String sign;
    /** 有效标识 1-有效 0-无效 */
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    @BtCompositeSearch(fieldName = "VALID")
    private String valid;
    /** 备注 */
    private String rmk;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
