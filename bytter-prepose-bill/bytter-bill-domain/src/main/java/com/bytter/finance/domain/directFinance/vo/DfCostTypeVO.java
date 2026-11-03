package com.bytter.finance.domain.directFinance.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:29
 * @Version 1.0
 */
public class DfCostTypeVO implements Serializable {
    /**
     * 业务主键
     */
    private String id;
    /**
     * 费用编码
     */
    @BtCompositeSearch(fieldName = "t.cost_code")
    private String costCode;
    /**
     * 费用名称
     */
    @BtCompositeSearch(fieldName = "t.cost_name")
    private String costName;
    /**
     * 是否有效
     */
    private String isValid;
    /**
     * 是否初始化数据
     */
    private String isInit;
    public String getCostCode() {
        return costCode;
    }
    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }
    public String getCostName() {
        return costName;
    }
    public void setCostName(String costName) {
        this.costName = costName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    public String getIsInit() {
        return isInit;
    }
    public void setIsInit(String isInit) {
        this.isInit = isInit;
    }
}
