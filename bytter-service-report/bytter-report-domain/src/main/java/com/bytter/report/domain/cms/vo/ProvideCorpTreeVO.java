package com.bytter.report.domain.cms.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 借款单位树 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2020/10/22 0022 <br>
 */
public class ProvideCorpTreeVO implements Serializable {
    private String corpId;//单位Id
    private String corpName;//单位Name
    private ProvideCorpTreeVO parentVO;
    private List<ProvideCorpTreeVO> childs;
    private BigDecimal leftMoney;//借款发放剩余余额
    private BigDecimal bailMoney;//保证金金额
    private BigDecimal converMoney;//借款余额
    private BigDecimal guaMoney;//担保金额
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public ProvideCorpTreeVO getParentVO() {
        return parentVO;
    }
    public void setParentVO(ProvideCorpTreeVO parentVO) {
        this.parentVO = parentVO;
    }
    public List<ProvideCorpTreeVO> getChilds() {
        if(null == childs){
            childs = new ArrayList<>();
        }
        return childs;
    }
    public void setChilds(List<ProvideCorpTreeVO> childs) {
        this.childs = childs;
    }
    public BigDecimal getLeftMoney() {
        if (null == leftMoney) {
            leftMoney = BigDecimal.ZERO;
        }
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public BigDecimal getBailMoney() {
        if (null == bailMoney) {
            bailMoney = BigDecimal.ZERO;
        }
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    public BigDecimal getConverMoney() {
        if (null == converMoney) {
            converMoney = BigDecimal.ZERO;
        }
        return converMoney;
    }
    public void setConverMoney(BigDecimal converMoney) {
        this.converMoney = converMoney;
    }
    public BigDecimal getGuaMoney() {
        if (null == guaMoney) {
            guaMoney = BigDecimal.ZERO;
        }
        return guaMoney;
    }
    public void setGuaMoney(BigDecimal guaMoney) {
        this.guaMoney = guaMoney;
    }
}
