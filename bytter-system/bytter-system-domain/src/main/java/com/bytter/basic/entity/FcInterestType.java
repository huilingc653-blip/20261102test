package com.bytter.basic.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author ZHANGXB
 * @version 1.0
 * @Title: Entity
 * @Description: 利率种类实体类
 * @date 2017-06-05 10:52:28
 */
@Entity
@Table(name = "fc_interest_type", schema = "")
public class FcInterestType implements java.io.Serializable {
    private String interestCode;
    private String interestName;
    private String rmk;
    @Id
    @Column(name = "INTEREST_CODE")
    public String getInterestCode() {
        return interestCode;
    }
    public void setInterestCode(String interestCode) {
        this.interestCode = interestCode;
    }
    @Column(name = "INTEREST_NAME")
    public String getInterestName() {
        return interestName;
    }
    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
