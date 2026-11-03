package com.bytter.finance.domain.directFinance.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:29
 * @Version 1.0
 */
@Entity
@Table(name = "DF_COST_TYPE", schema = "")
@SuppressWarnings("serial")
public class DfCostType extends IdEntity implements Serializable {
    /**
     * 费用编码
     */
    private String costCode;
    /**
     * 费用名称
     */
    private String costName;
    /**
     * 是否有效
     */
    private String isValid;
    /**
     * 是否初始化数据
     */
    private String isInit;
    @Column(name = "COST_CODE")
    public String getCostCode() {
        return costCode;
    }
    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }
    @Column(name = "COST_NAME")
    public String getCostName() {
        return costName;
    }
    public void setCostName(String costName) {
        this.costName = costName;
    }
    @Column(name = "IS_VALID")
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    @Column(name = "IS_INIT")
    public String getIsInit() {
        return isInit;
    }
    public void setIsInit(String isInit) {
        this.isInit = isInit;
    }
}
