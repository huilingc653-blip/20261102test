package com.bytter.system.api.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.util.Date;
public class TbBaseRateDTO implements Serializable {
    private String id;
    /** 编码 */
    private String code;
    /** 基准类型名称 */
    private String baseTypeName;
    /** 生效日期 */
    private java.util.Date timeOfTakeEffect;
    /** 央行基准利率 */
    private String isBankBaseRate;
    /** 备注 */
    private String rmk;
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
    public String getBaseTypeName() {
        return baseTypeName;
    }
    public void setBaseTypeName(String baseTypeName) {
        this.baseTypeName = baseTypeName;
    }
    public Date getTimeOfTakeEffect() {
        return timeOfTakeEffect;
    }
    public void setTimeOfTakeEffect(Date timeOfTakeEffect) {
        this.timeOfTakeEffect = timeOfTakeEffect;
    }
    public String getIsBankBaseRate() {
        return isBankBaseRate;
    }
    public void setIsBankBaseRate(String isBankBaseRate) {
        this.isBankBaseRate = isBankBaseRate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
