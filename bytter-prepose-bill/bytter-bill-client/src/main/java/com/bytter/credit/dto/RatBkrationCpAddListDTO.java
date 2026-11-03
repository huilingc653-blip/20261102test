package com.bytter.credit.dto;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * 集团授信追加对象
 *
 * @author liyh
 */
public class RatBkrationCpAddListDTO implements Serializable {
    private String id;
    /**
     * 授信分配编号
     */
    private String disCode;
    /**
     * 授信编号
     */
    private String ratCode;
    /**
     * 授信录入单位
     */
    private String ratCorpId;
    /**
     * 授信录入单位
     */
    private String ratCorpName;
    /**
     * 分配方式
     */
    private String disWay;
    /**
     * 二次分配单位
     */
    private String disCorpId;

    /**
     * 二次分配单位名称
     */
    private String disCorpName;
    /**
     * 公司代码
     */
    private String corpId;
    /**
     * 单位代码
     */
    private String corpName;
    /**
     * 授信种类
     */
    private String ratType;
    /**
     * 银行类型
     */
    private String bankType;
    /**
     * 银行编号
     */
    private String bankCode;
    /**
     * 银行编号
     */
    private String bankName;
    /**
     * 授信币种
     */
    @Currency(inject = "curName")
    private String curCode;
    /**
     * 授信币种
     */
    private String curName;
    /**
     * 授信合同号
     */
    private String contractNo;
    /**
     * 银行类型名称
     */
    private String typeName;
    /**
     * 银行类别名称
     */
    private String bankTypeName;
    /**
     * 授信开始时间
     */
    private java.util.Date beginDate;
    /**
     * 授信结束时间
     */
    private java.util.Date endDate;
    /**
     * 授信金额
     */
    private BigDecimal money;
    /**
     * 敞口额度
     */
    private BigDecimal openMoney;
    /**
     * 授信金额 合计
     */
    private BigDecimal moneysum;
    /**
     * 敞口金额 合计
     */
    private BigDecimal openMoneysum;
    /**
     * 是否通过
     */
    private String ispassed;
    /**
     * 分配方式
     */
    private String distributeWay;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 额度类型
     */
    private String type;
    /**
     * 额度类型名称
     */
    private String creditTypeName;
    /**
     * 授信分配明细数据
     */
    private List<RatBkrationCpAddDtlDTO> singRationDtls;
    private List<RatBkrationCpAddDtlDTO> compRationDtls;
    /**
     * 单品种条数
     */
    private Integer singItemSize;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    /**
     * 综合品种行
     */
    private Integer compItemIndex;
    /**
     * 总品种数
     */
    private Integer itemSize;
    /**
     * 更新授信标识
     */
    private boolean updateSign = false;
    /**
     * 可追加额度
     */
    private BigDecimal allAddMoney;
    /**
     * 可追加敞口额度
     */
    private BigDecimal allAddOpenMoney;
    /**
     * 授信模式
     */
    private String ratMode;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 附件路径
     */
    private String attachfilepath;
    private boolean viewFlag = false;
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        if (null == money) {
            this.money = this.moneysum;
        }
        this.money = money;
    }
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        if (null == openMoney) {
            this.openMoney = this.openMoneysum;
        }
        this.openMoney = openMoney;
    }
    public BigDecimal getMoneysum() {
        return moneysum;
    }
    public void setMoneysum(BigDecimal moneysum) {
        this.moneysum = moneysum;
    }
    public BigDecimal getOpenMoneysum() {
        return openMoneysum;
    }
    public void setOpenMoneysum(BigDecimal openMoneysum) {
        this.openMoneysum = openMoneysum;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getRatCorpId() {
        return ratCorpId;
    }
    public void setRatCorpId(String ratCorpId) {
        this.ratCorpId = ratCorpId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getIspassed() {
        return ispassed;
    }
    public void setIspassed(String ispassed) {
        this.ispassed = ispassed;
    }
    public String getDistributeWay() {
        return distributeWay;
    }
    public void setDistributeWay(String distributeWay) {
        this.distributeWay = distributeWay;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public List<RatBkrationCpAddDtlDTO> getSingRationDtls() {
        return singRationDtls;
    }
    public void setSingRationDtls(List<RatBkrationCpAddDtlDTO> singRationDtls) {
        this.singRationDtls = singRationDtls;
    }
    public List<RatBkrationCpAddDtlDTO> getCompRationDtls() {
        return compRationDtls;
    }
    public void setCompRationDtls(List<RatBkrationCpAddDtlDTO> compRationDtls) {
        this.compRationDtls = compRationDtls;
    }
    public Integer getSingItemSize() {
        return singItemSize;
    }
    public void setSingItemSize(Integer singItemSize) {
        this.singItemSize = singItemSize;
    }
    public Integer getCompItemSize() {
        return compItemSize;
    }
    public void setCompItemSize(Integer compItemSize) {
        this.compItemSize = compItemSize;
    }
    public Integer getCompItemIndex() {
        if (null == this.compItemIndex) {
            this.compItemIndex = Integer.valueOf(0);
        }
        return compItemIndex;
    }
    public void setCompItemIndex(Integer compItemIndex) {
        this.compItemIndex = compItemIndex;
    }
    public Integer getItemSize() {
        return itemSize;
    }
    public void setItemSize(Integer itemSize) {
        this.itemSize = itemSize;
    }
    public boolean isUpdateSign() {
        return updateSign;
    }
    public void setUpdateSign(boolean updateSign) {
        this.updateSign = updateSign;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public BigDecimal getAllAddMoney() {
        return allAddMoney;
    }
    public void setAllAddMoney(BigDecimal allAddMoney) {
        this.allAddMoney = allAddMoney;
    }
    public BigDecimal getAllAddOpenMoney() {
        return allAddOpenMoney;
    }
    public void setAllAddOpenMoney(BigDecimal allAddOpenMoney) {
        this.allAddOpenMoney = allAddOpenMoney;
    }
    public String getRatCorpName() {
        return ratCorpName;
    }
    public void setRatCorpName(String ratCorpName) {
        this.ratCorpName = ratCorpName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public java.util.Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getDisCorpId() {
        return disCorpId;
    }

    public void setDisCorpId(String disCorpId) {
        this.disCorpId = disCorpId;
    }

    public String getDisCorpName() {
        return disCorpName;
    }

    public void setDisCorpName(String disCorpName) {
        this.disCorpName = disCorpName;
    }

    public String getCreditTypeName() {
        return creditTypeName;
    }

    public void setCreditTypeName(String creditTypeName) {
        this.creditTypeName = creditTypeName;
    }

    public String getDisWay() {
        return disWay;
    }

    public void setDisWay(String disWay) {
        this.disWay = disWay;
    }

    public boolean isViewFlag() {
        return viewFlag;
    }

    public void setViewFlag(boolean viewFlag) {
        this.viewFlag = viewFlag;
    }

    public String getAttachfilepath() {
        return attachfilepath;
    }

    public void setAttachfilepath(String attachfilepath) {
        this.attachfilepath = attachfilepath;
    }
}
