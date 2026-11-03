package com.bytter.biz.foreign.vo;

import com.bytter.biz.foreign.entity.FexCorpPoolLimit;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FexCorpPoolBankAccVO implements Serializable, IBaseDataInject {

    private String id;
    /**资金池ID*/
    private String poolId;
    /**资金池性质 1-跨境资金集中运营、2-境内外币资金池、3-境外外币资金池、4-跨境双向人民币资金池*/
    private String poolNature;
    /**资金池代码*/
    private String poolCode;
    /**资金池名称*/
    private String poolName;
    /**资金池显示名称*/
    private String showName;
    /**资金池标志 1-资金池主体 0-资金池主账户*/
    private String poolSign;
    /**是否限额管控 0-否、1-是*/
    private String isLimitControl;
    /**限额折算币种*/
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    private String englishCode;
    /**限额折算汇率类型*/
    private String rateType;
    /**限额折算汇率类型名称*/
    private String typeName;
    /**备注*/
    private String rmk;
    /**有效标志 1-有效 0-无效*/
    private String validSign;
    /**上级ID（主账户上级ID为资金池主体ID，成员账户上级ID为主账户ID）*/
    private String parentId;
    /**单位ID*/
    @Corp(inject = "corpName")
    private String corpId;
    /**单位编码*/
    private String corpCode;
    /**单位名称*/
    private String corpName;
    /**账号ID*/
    private String accId;
    /**账号*/
    private String bankAcc;
    /**账号名称*/
    private String accName;
    /**开户行名称*/
    private String bankName;
    /**账户资金性质 1-国内主账户、2-国外主账户、3-经常项目、4-资本项目、5-NRA账户*/
    private String accNature;
    /**是否主账户 0-否、1-是*/
    private String parentSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**限额设置 1-外债余额、2-对外放款、3-净流入额*/
    private String limitType;
    /**限额设置 1-外债余额*/
    private BigDecimal limitMoney1;
    /**限额设置 2-对外放款*/
    private BigDecimal limitMoney2;
    /**限额设置 3-净流入额*/
    private BigDecimal limitMoney3;
    /**存放主账号集合*/
    private List<FexCorpPoolBankAccVO> accList;
    /**存放成员账号集合*/
    private List<FexCorpPoolBankAccVO> childList;
    /**存放限制集合*/
    private List<FexCorpPoolLimit> fexCorpPoolLimits;

    /**多个id拼装字符串*/
    private String ids;
    /**多个账号拼装字符串*/
    private String accIds;
    /**多个单位拼装字符串*/
    private String corpIds;
    /**多个币别拼装字符串*/
    private String curIds;
    /**多个账户性质拼装字符串*/
    private String accNatures;
    /**多个限制设置拼装字符串*/
    private String limitTypes;

    /**余额日期*/
    private Date balDate;
    /**余额*/
    private BigDecimal bal;
    /**小计*/
    private BigDecimal subtotal;
    /**合计*/
    private BigDecimal total;
    /**小计集合*/
    List<Map<String, BigDecimal>> subCurList;
    /**合计map*/
    Map<String, BigDecimal> totalMap;
    /**折合汇率*/
    private String reducedCurId;

    public String getReducedCurId() {
        return reducedCurId;
    }

    public void setReducedCurId(String reducedCurId) {
        this.reducedCurId = reducedCurId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getPoolNature() {
        return poolNature;
    }

    public void setPoolNature(String poolNature) {
        this.poolNature = poolNature;
    }

    public String getPoolCode() {
        return poolCode;
    }

    public void setPoolCode(String poolCode) {
        this.poolCode = poolCode;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getPoolSign() {
        return poolSign;
    }

    public void setPoolSign(String poolSign) {
        this.poolSign = poolSign;
    }

    public String getIsLimitControl() {
        return isLimitControl;
    }

    public void setIsLimitControl(String isLimitControl) {
        this.isLimitControl = isLimitControl;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    public String getParentSign() {
        return parentSign;
    }

    public void setParentSign(String parentSign) {
        this.parentSign = parentSign;
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

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public BigDecimal getLimitMoney1() {
        return limitMoney1;
    }

    public void setLimitMoney1(BigDecimal limitMoney1) {
        this.limitMoney1 = limitMoney1;
    }

    public BigDecimal getLimitMoney2() {
        return limitMoney2;
    }

    public void setLimitMoney2(BigDecimal limitMoney2) {
        this.limitMoney2 = limitMoney2;
    }

    public BigDecimal getLimitMoney3() {
        return limitMoney3;
    }

    public void setLimitMoney3(BigDecimal limitMoney3) {
        this.limitMoney3 = limitMoney3;
    }

    public List<FexCorpPoolBankAccVO> getAccList() {
        return accList;
    }

    public void setAccList(List<FexCorpPoolBankAccVO> accList) {
        this.accList = accList;
    }

    public List<FexCorpPoolBankAccVO> getChildList() {
        return childList;
    }

    public void setChildList(List<FexCorpPoolBankAccVO> childList) {
        this.childList = childList;
    }

    public List<FexCorpPoolLimit> getFexCorpPoolLimits() {
        return fexCorpPoolLimits;
    }

    public void setFexCorpPoolLimits(List<FexCorpPoolLimit> fexCorpPoolLimits) {
        this.fexCorpPoolLimits = fexCorpPoolLimits;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getAccIds() {
        return accIds;
    }

    public void setAccIds(String accIds) {
        this.accIds = accIds;
    }

    public String getCorpIds() {
        return corpIds;
    }

    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }

    public String getCurIds() {
        return curIds;
    }

    public void setCurIds(String curIds) {
        this.curIds = curIds;
    }

    public String getAccNatures() {
        return accNatures;
    }

    public void setAccNatures(String accNatures) {
        this.accNatures = accNatures;
    }

    public String getLimitTypes() {
        return limitTypes;
    }

    public void setLimitTypes(String limitTypes) {
        this.limitTypes = limitTypes;
    }

    public Date getBalDate() {
        return balDate;
    }

    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<Map<String, BigDecimal>> getSubCurList() {
        return subCurList;
    }

    public void setSubCurList(List<Map<String, BigDecimal>> subCurList) {
        this.subCurList = subCurList;
    }

    public Map<String, BigDecimal> getTotalMap() {
        return totalMap;
    }

    public void setTotalMap(Map<String, BigDecimal> totalMap) {
        this.totalMap = totalMap;
    }
}
