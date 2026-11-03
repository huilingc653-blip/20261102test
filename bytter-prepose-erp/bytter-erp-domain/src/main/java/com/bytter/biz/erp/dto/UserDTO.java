package com.bytter.biz.erp.dto;

import com.bytter.biz.erp.base.BaseDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * @author zhoul
 */
public class UserDTO  implements Serializable {

    @NotNull
    @XmlElement(name = "CODE")
    private String code;

    @NotNull
    @XmlElement(name = "customerCode")
    private String customerCode;

    @XmlElement(name = "CORP_CODE")
    @NotNull(groups = {BaseDTO.SaveOrUpdate.class})
    private String corpCode;

    @XmlElement(name = "USER_NAME")
    @NotNull(groups = {BaseDTO.SaveOrUpdate.class})
    private String userName;

    @XmlElement(name = "STATUS")
    @Max(value = 1, groups = {BaseDTO.SaveOrUpdate.class})
    private Integer status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "code='" + code + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", userName='" + userName + '\'' +
                ", status=" + status +
                '}';
    }
}
