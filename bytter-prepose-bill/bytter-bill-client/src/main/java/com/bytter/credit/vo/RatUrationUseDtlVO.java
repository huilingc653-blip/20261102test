package com.bytter.credit.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * 银行授信使用明细VO
 * 
 * @author liyh
 *
 */
public class RatUrationUseDtlVO implements Serializable {
    private String id;
    /**
     * 授信品种类型
     */
    private String itemFlag;
    /**
     * 品种
     */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    private String itemCode;
    /**
     * 品种顺序
     */
    private Integer itemOrder;
    /**
     * 品种名称
     */
    private String itemName;
    /** 授信总额 **/
    private BigDecimal ratTotalMoney;
    /** 授信额度 **/
    private BigDecimal money;
    /** 占用额度 **/
    private BigDecimal useMoney;
    /** 在审占用额度 **/
    private BigDecimal approveMoney;
    /** 剩余额度 **/
    private BigDecimal leftMoney;
    /** 可用额度 **/
    private BigDecimal overPlusMoney;
    /** 最高额度 **/
    private BigDecimal maxMoney;
    // 虚拟占用的最高额度
    private BigDecimal virtualMaxMoney;
    // 虚拟在途占用的最高额度
    private BigDecimal virtualUrationMaxMoney;
    /**
     * 综合授信品种集合数
     */
    private Integer rationSize;
    /** 保证金比例 **/
    private BigDecimal assurepercent;
    /** 费率 **/
    private BigDecimal procedureFeeRate;
    /** 授信方式 */
    private String creditType;
    private String ratCode;
    private String disCode;
    private List<RatUrationUseDtlVO> rationDlts;
    /** 保证金（已执行） */
    private BigDecimal bondApply;
    /** 保证金（在途） */
    private BigDecimal bondAway;
    /** 预测释放额度 **/
    private BigDecimal calReleaseMoney;
    /** 预测剩余额度 **/
    private BigDecimal calLeftMoney;
    private String compSign;
    /***
     * 占用品种业务数
     */
    private Integer sysRationSize = 1;
    /**
     * 业务占用明细
     */
    private List<RatUrationSysBean> sysBeans;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getItemFlag() {
        return itemFlag;
    }
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public Integer getItemOrder() {
        return itemOrder;
    }
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public BigDecimal getMoney() {
        if (null == this.money) {
            this.money = BigDecimal.ZERO;
        }
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getUseMoney() {
        if (null == this.useMoney) {
            this.useMoney = BigDecimal.ZERO;
        }
        return useMoney;
    }
    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }
    public BigDecimal getApproveMoney() {
        if (null == this.approveMoney) {
            this.approveMoney = BigDecimal.ZERO;
        }
        return approveMoney;
    }
    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }
    public BigDecimal getLeftMoney() {
        if (null == this.leftMoney) {
            this.leftMoney = BigDecimal.ZERO;
        }
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public BigDecimal getOverPlusMoney() {
        if (null == this.overPlusMoney) {
            this.overPlusMoney = BigDecimal.ZERO;
        }
        return overPlusMoney;
    }
    public void setOverPlusMoney(BigDecimal overPlusMoney) {
        this.overPlusMoney = overPlusMoney;
    }
    public Integer getRationSize() {
        if (null == this.rationSize) {
            this.rationSize = Integer.valueOf(0);
        }
        return rationSize;
    }
    public void setRationSize(Integer rationSize) {
        this.rationSize = rationSize;
    }
    public List<RatUrationUseDtlVO> getRationDlts() {
        return rationDlts;
    }
    public void setRationDlts(List<RatUrationUseDtlVO> rationDlts) {
        this.rationDlts = rationDlts;
    }
    // 添加综合品种
    public void addRationDtl(RatUrationUseDtlVO rationDtl) {
        if (null == this.rationDlts) {
            this.rationDlts = new ArrayList<RatUrationUseDtlVO>();
        }
        this.rationDlts.add(rationDtl);
    }
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    public BigDecimal getProcedureFeeRate() {
        return procedureFeeRate;
    }
    public void setProcedureFeeRate(BigDecimal procedureFeeRate) {
        this.procedureFeeRate = procedureFeeRate;
    }
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getCreditType() {
        return creditType;
    }
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    public BigDecimal getBondApply() {
        return bondApply;
    }
    public void setBondApply(BigDecimal bondApply) {
        this.bondApply = bondApply;
    }
    public BigDecimal getBondAway() {
        return bondAway;
    }
    public void setBondAway(BigDecimal bondAway) {
        this.bondAway = bondAway;
    }
    public BigDecimal getCalReleaseMoney() {
        return calReleaseMoney;
    }
    public void setCalReleaseMoney(BigDecimal calReleaseMoney) {
        this.calReleaseMoney = calReleaseMoney;
    }
    public BigDecimal getCalLeftMoney() {
        return calLeftMoney;
    }
    public void setCalLeftMoney(BigDecimal calLeftMoney) {
        this.calLeftMoney = calLeftMoney;
    }
    public String getCompSign() {
        return compSign;
    }
    public void setCompSign(String compSign) {
        this.compSign = compSign;
    }
    public Integer getSysRationSize() {
        return sysRationSize;
    }
    public void setSysRationSize(Integer sysRationSize) {
        this.sysRationSize = sysRationSize;
    }
    public List<RatUrationSysBean> getSysBeans() {
        return sysBeans;
    }
    public void setSysBeans(List<RatUrationSysBean> sysBeans) {
        this.sysBeans = sysBeans;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    public BigDecimal getRatTotalMoney() {
        return ratTotalMoney;
    }
    public void setRatTotalMoney(BigDecimal ratTotalMoney) {
        this.ratTotalMoney = ratTotalMoney;
    }
    public BigDecimal getVirtualMaxMoney() {
        if (null == this.virtualMaxMoney) {
            this.virtualMaxMoney = BigDecimal.ZERO;
        }
        return virtualMaxMoney;
    }
    public void setVirtualMaxMoney(BigDecimal virtualMaxMoney) {
        this.virtualMaxMoney = virtualMaxMoney;
    }
    public BigDecimal getVirtualUrationMaxMoney() {
        if (null == this.virtualUrationMaxMoney) {
            this.virtualUrationMaxMoney = BigDecimal.ZERO;
        }
        return virtualUrationMaxMoney;
    }
    public void setVirtualUrationMaxMoney(BigDecimal virtualUrationMaxMoney) {
        this.virtualUrationMaxMoney = virtualUrationMaxMoney;
    }
}
