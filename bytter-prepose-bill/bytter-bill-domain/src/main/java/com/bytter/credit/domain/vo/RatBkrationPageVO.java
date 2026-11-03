package com.bytter.credit.domain.vo;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 授信录入VO
 *
 * @author liyh
 */
public class RatBkrationPageVO implements Serializable {
    /**
     * 授信录入主键ID
     **/
    private String id;
    /**
     * 授信分配主键ID
     **/
    private String disCode;
    /**
     * 授信编号
     */
    private String ratCode;
    /**
     * 网点编号
     */
    private String netCode;
    /**
     * 单位代码
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
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 银行编号
     */
    private String bankCode;
    /**
     * 银行编号
     */
    private String bankName;
    /**
     * 敞口授信
     */
    private String openSign;
    /**
     * 授信模式
     */
    private String ratMode;
    /**
     * 授信币种
     */
//    @Currency(inject = "curName")
    private String curCode;
    /**
     * 授信币种
     */
    private String curName;
    /**
     * 授信开始时间
     */
    private Date beginDate;
    /**
     * 授信结束时间
     */
    private Date endDate;
    /**
     * 授信金额
     */
    private java.math.BigDecimal money;
    /**
     * 敞口额度
     */
    private java.math.BigDecimal openMoney;
    /**
     * 剩余授信额度
     */
    private java.math.BigDecimal unUserMoney;
    /**
     * 剩余敞口额度
     */
    private java.math.BigDecimal unUserOpenMoney;
    /**
     * 剩余授信额度
     */
    private java.math.BigDecimal disMoney;
    /**
     * 剩余敞口额度
     */
    private java.math.BigDecimal disOpenMoney;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 授信合同号
     */
    @BtCompositeSearch(fieldName = "rb.contract_no")
    private String contractNo;
    /**
     * 额度是否包含保证金
     */
    private String includeBailmoney;
    /**
     * 单品种条数
     */
    private Integer singItemSize;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    /**
     * 综合品种行数
     */
    private Integer compItemIndex;
    /**
     * 总品种数
     */
    private Integer itemSize;
    /**
     * 创建人
     */
    private String userName;
    /**
     * 授信录入综合品种明细数据
     */
    private List<RatBkRationDtlVm> compRationDtls;
    /**
     * 授信录入单品种明细数据
     */
    private List<RatBkRationDtlVm> singRationDtls;
    /**
     * 备注
     */
    private String remark;
    /**
     * 担保类型
     */
    private String guaType;
    /**
     * 担保单位
     */
    private String guaCorpId;
    /**
     * 担保合同
     */
    private String guaContract;
    /**
     * 担保备注
     **/
    private String guaranteeRmk;
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
     * 额度类型
     */
    private String type;
    /**
     * 额度类型名称
     */
    private String creditTypeName;
    private boolean viewFlag = false;
    /**
     * 是否挂接保证金
     */
    private String isHookBond;
    /**
     * 是否挂接担保
     */
    private String isHangGuarantee;
    /**
     * 附件路径
     */
    private String attachfilepath;
    /**
     * 合同附件
     */
    private String contractAttachfilepath;
    /**
     * 担保方式
     **/
    private String guaWay;
    /**
     * 挂接的合同ID
     */
    private String conId;
    /**
     * 结转授信编号(存储为id)
     **/
    private String oldRatCode;
    /**
     * 合同类型ID
     **/
    private String conTypeId;
    /**
     * 担保开始时间
     */
    private Date guaranteeBeginDate;
    /**
     * 担保结束时间
     */
    private Date guaranteeEndDate;

    public Date getGuaranteeBeginDate() {
        return guaranteeBeginDate;
    }

    public void setGuaranteeBeginDate(Date guaranteeBeginDate) {
        this.guaranteeBeginDate = guaranteeBeginDate;
    }

    public Date getGuaranteeEndDate() {
        return guaranteeEndDate;
    }

    public void setGuaranteeEndDate(Date guaranteeEndDate) {
        this.guaranteeEndDate = guaranteeEndDate;
    }

    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getDisCode() {
        return disCode;
    }
    public void setDisCode(String disCode) {
        this.disCode = disCode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
    public String getRatType() {
        return ratType;
    }
    public void setRatType(String ratType) {
        this.ratType = ratType;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getOpenSign() {
        return openSign;
    }
    public void setOpenSign(String openSign) {
        this.openSign = openSign;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    public java.math.BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(java.math.BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    public java.math.BigDecimal getUnUserMoney() {
        return unUserMoney;
    }
    public void setUnUserMoney(java.math.BigDecimal unUserMoney) {
        this.unUserMoney = unUserMoney;
    }
    public java.math.BigDecimal getUnUserOpenMoney() {
        return unUserOpenMoney;
    }
    public void setUnUserOpenMoney(java.math.BigDecimal unUserOpenMoney) {
        this.unUserOpenMoney = unUserOpenMoney;
    }
    public java.math.BigDecimal getDisMoney() {
        return disMoney;
    }
    public void setDisMoney(java.math.BigDecimal disMoney) {
        this.disMoney = disMoney;
    }
    public java.math.BigDecimal getDisOpenMoney() {
        return disOpenMoney;
    }
    public void setDisOpenMoney(java.math.BigDecimal disOpenMoney) {
        this.disOpenMoney = disOpenMoney;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public List<RatBkRationDtlVm> getCompRationDtls() {
        return compRationDtls;
    }
    public void setCompRationDtls(List<RatBkRationDtlVm> compRationDtls) {
        this.compRationDtls = compRationDtls;
    }
    public List<RatBkRationDtlVm> getSingRationDtls() {
        return singRationDtls;
    }
    public void setSingRationDtls(List<RatBkRationDtlVm> singRationDtls) {
        this.singRationDtls = singRationDtls;
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
    public String getIncludeBailmoney() {
        return includeBailmoney;
    }
    public void setIncludeBailmoney(String includeBailmoney) {
        this.includeBailmoney = includeBailmoney;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getGuaType() {
        return guaType;
    }
    public void setGuaType(String guaType) {
        this.guaType = guaType;
    }
    public String getGuaCorpId() {
        return guaCorpId;
    }
    public void setGuaCorpId(String guaCorpId) {
        this.guaCorpId = guaCorpId;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getDisWay() {
        return disWay;
    }
    public void setDisWay(String disWay) {
        this.disWay = disWay;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCreditTypeName() {
        return creditTypeName;
    }
    public void setCreditTypeName(String creditTypeName) {
        this.creditTypeName = creditTypeName;
    }
    public boolean isViewFlag() {
        return viewFlag;
    }
    public void setViewFlag(boolean viewFlag) {
        this.viewFlag = viewFlag;
    }
    public String getIsHookBond() {
        return isHookBond;
    }
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getAttachfilepath() {
        return attachfilepath;
    }
    public void setAttachfilepath(String attachfilepath) {
        this.attachfilepath = attachfilepath;
    }
    public String getContractAttachfilepath() {
        return contractAttachfilepath;
    }
    public void setContractAttachfilepath(String contractAttachfilepath) {
        this.contractAttachfilepath = contractAttachfilepath;
    }
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getOldRatCode() {
        return oldRatCode;
    }
    public void setOldRatCode(String oldRatCode) {
        this.oldRatCode = oldRatCode;
    }
}
