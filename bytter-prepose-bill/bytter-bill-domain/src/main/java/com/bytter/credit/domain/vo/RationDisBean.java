package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import com.bytter.core.utils.NumberUtil;
public class RationDisBean implements Serializable {
    /**
     * 授信品种
     */
    private String itemCode;
    /**
     * 授信额度
     */
    private BigDecimal money;
    /**
     * 敞口额度
     */
    private BigDecimal openMoney;
    /**
     * 最高额度
     */
    private BigDecimal maxMoney;
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public BigDecimal getMoney() {
        return NumberUtil.formatNumericNull(this.money);
    }
    public void setMoney(BigDecimal money) {
        this.money = NumberUtil.formatNumericNull(money);
    }
    public BigDecimal getOpenMoney() {
        return NumberUtil.formatNumericNull(this.openMoney);
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = NumberUtil.formatNumericNull(openMoney);
    }
    public BigDecimal getMaxMoney() {
        return NumberUtil.formatNumericNull(maxMoney);
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    /**
     * 功能介绍 : 金额相减。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public RationDisBean subBean(RationDisBean bean) {
        /*
         * if (StringUtils.isEmpty(this.itemCode)) { return; } if (null == bean) { return; } if
         * (StringUtils.isEmpty(bean.itemCode)) { return; } if (!this.itemCode.equals(bean.getItemCode())) { return; }
         */
        this.money = NumberUtil.formatNumericNull(this.money);
        this.openMoney = NumberUtil.formatNumericNull(this.openMoney);
        this.maxMoney = NumberUtil.formatNumericNull(this.maxMoney);
        this.money = this.money.subtract(bean.getMoney());
        this.openMoney = this.openMoney.subtract(bean.getOpenMoney());
        this.maxMoney = this.maxMoney.subtract(bean.getMaxMoney());
        return this;
    }
    /**
     * 功能介绍 : 金额相加。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public RationDisBean addBean(RationDisBean bean) {
        /*
         * if (StringUtils.isEmpty(this.itemCode)) { return; } if (null == bean) { return; } if
         * (StringUtils.isEmpty(bean.itemCode)) { return; } if (!this.itemCode.equals(bean.getItemCode())) { return; }
         */
        this.money = NumberUtil.formatNumericNull(this.money);
        this.openMoney = NumberUtil.formatNumericNull(this.openMoney);
        this.maxMoney = NumberUtil.formatNumericNull(this.maxMoney);
        this.money = this.money.add(bean.getMoney());
        this.openMoney = this.openMoney.add(bean.getOpenMoney());
        this.maxMoney = this.maxMoney.add(bean.getMaxMoney());
        return this;
    }
}
