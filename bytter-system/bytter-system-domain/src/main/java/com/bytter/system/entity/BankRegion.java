package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/6/29.
 */
@Entity
@Table(name = "SYS_BANK_REGION")
public class BankRegion extends IdEntity implements Serializable {
    private String bankCity;
    private String bankInputCity;
    @Column(name = "BANK_CITY")
    public String getBankCity() {
        return bankCity;
    }
    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }
    @Column(name = "BANK_INPUT_CITY")
    public String getBankInputCity() {
        return bankInputCity;
    }
    public void setBankInputCity(String bankInputCity) {
        this.bankInputCity = bankInputCity;
    }
}
