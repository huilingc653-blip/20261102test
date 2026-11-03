package com.bytter.report.domain.cms.vo;
import com.bytter.core.constant.RationConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 授信录入VO
 *
 * @author liyh
 */
public class RatBkrationVO extends ExcelModel implements Serializable, IBusinessKey {
    @BtCompositeSearch(fieldName = "rb.id")
    private String id;
    private String disCode;
    /**
     * 授信编号
     */
    @BtCompositeSearch(fieldName = "rb.rat_code")
    private String ratCode;
    /**
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "rb.corp_id")
    private String corpId;
    /**
     * 单位代码
     */
    @Excel(name = "*授信单位")
    private String corpName;
    /**
     * 授信种类
     */
    @BtCompositeSearch(fieldName = "rb.rat_type")
    private String ratType;
    /**
     * 银行类型
     */
    @BtCompositeSearch(fieldName = "rb.bank_type")
    private String bankType;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 银行编号
     */
    @Excel(name = "*银行开户行行号")
    private String bankCode;
    /**
     * 银行名称
     */
    @BtCompositeSearch(fieldName = "rb.bank_name")
    private String bankName;
    /**
     * 敞口授信
     */
    private String openSign;
    /**
     * 授信模式
     */
    @BtCompositeSearch(fieldName = "rb.rat_mode")
    private String ratMode;
    @Excel(name = "*授信模式")
    private String ratModeName;
    /**
     * 授信币种
     */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "rb.cur_code")
    private String curCode;
    /**
     * 授信币种
     */
    @Excel(name = "*授信币种")
    private String curName;
    /**
     * 授信开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "rb.beginDate")
    @Excel(name = "*授信开始时间", format = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 授信结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "rb.endDate")
    @Excel(name = "*授信结束时间", format = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 授信金额
     */
    @BtCompositeSearch(fieldName = "rb.money")
    private BigDecimal money;
    /**
     * 敞口额度
     */
    private BigDecimal openMoney;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "rb.status")
    private Integer status;
    /**
     * 授信合同号
     */
    @BtCompositeSearch(fieldName = "rb.contract_no")
    @Excel(name = "授信合同号")
    private String contractNo;
    // 创建日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 单品种条数
     */
    private Integer singItemSize;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    /**
     * 总品种数
     */
    private Integer itemSize;
    /**
     * 授信录入明细数据
     */
    private List<RatBkRationDtlVm> rationDtls;
    /**
     * 延期ID
     */
    private String delayCode;
    /**
     * 延期状态
     */
    private Integer delayStatus;
    /**
     * 延期到期日期
     */
    private Date delayDate;
    /**
     * 授信日期
     */
    private Date ratDate;
    @BtCompositeSearch(fieldName = "rb.begindate")
    private Date sDate;
    @BtCompositeSearch(fieldName = "rb.enddate")
    private Date eDate;
    /**
     * 额度使用单位代码
     */
    @BtCompositeSearch(fieldName = "rd.corp_Id")
    private String disCorp;
    /**
     * 额度使用单位
     */
    private String disCorpName;
    /**
     * 授信金额
     */
    private BigDecimal gMoney;
    /**
     * 授信金额
     */
    private BigDecimal lMoney;
    /**
     * 授信开始日期
     */
    private Date gBeginDate;
    /**
     * 授信开始日期
     */
    private Date lBeginDate;
    /**
     * 授信到期日期
     */
    private Date gEndDate;
    /**
     * 授信到期日期
     */
    private Date lEndDate;
    /**
     * 过期标识
     */
    private String overdueSign = RationConstant.RAT_COMMON_N;
    /**
     * 是否挂接担保
     */
    @BtCompositeSearch(fieldName = "rb.is_hang_guarantee")
    private String isHangGuarantee;
    /**
     * 担保合同
     */
    private String guaContract;
    /**
     * 附件路径
     */
    private String attachfilepath;
    /**
     * 创建人
     */
    @BtCompositeSearch(fieldName = "rb.user_name")
    private String userName;
    /**
     * 授信类型
     */
    private String itemFlag;
    /**
     * 授信品种
     */
    private String itemCode;
    /**
     * 结转授信编号(存储为id)
     **/
    private String oldRatCode;
    @Excel(name = "结转授信编号")
    private String oldRatCodeCode;
    /**
     * 能否继续进行编辑
     **/
    private boolean toUpdate;
    @Override
    public String getBeanName() {
        return "ratBkrationService";
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
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
    public List<RatBkRationDtlVm> getRationDtls() {
        return rationDtls;
    }
    public void setRationDtls(List<RatBkRationDtlVm> rationDtls) {
        this.rationDtls = rationDtls;
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
    public Integer getItemSize() {
        return itemSize;
    }
    public void setItemSize(Integer itemSize) {
        this.itemSize = itemSize;
    }
    public Date getSDate() {
        return sDate;
    }
    public void setSDate(Date sDate) {
        this.sDate = sDate;
    }
    public Date getEDate() {
        return eDate;
    }
    public void setEDate(Date eDate) {
        this.eDate = eDate;
    }
    public String getDelayCode() {
        return delayCode;
    }
    public void setDelayCode(String delayCode) {
        this.delayCode = delayCode;
    }
    public Integer getDelayStatus() {
        return delayStatus;
    }
    public void setDelayStatus(Integer delayStatus) {
        this.delayStatus = delayStatus;
    }
    public Date getDelayDate() {
        return delayDate;
    }
    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getDisCorp() {
        return disCorp;
    }
    public void setDisCorp(String disCorp) {
        this.disCorp = disCorp;
    }
    public String getDisCorpName() {
        return disCorpName;
    }
    public void setDisCorpName(String disCorpName) {
        this.disCorpName = disCorpName;
    }
    public String getOverdueSign() {
        return overdueSign;
    }
    public void setOverdueSign(String overdueSign) {
        this.overdueSign = overdueSign;
    }
    @Override
    public String getBusinessKey() {
        return getId();
    }
    public Date getRatDate() {
        return ratDate;
    }
    public void setRatDate(Date ratDate) {
        this.ratDate = ratDate;
    }
    public BigDecimal getgMoney() {
        return gMoney;
    }
    public void setgMoney(BigDecimal gMoney) {
        this.gMoney = gMoney;
    }
    public BigDecimal getlMoney() {
        return lMoney;
    }
    public void setlMoney(BigDecimal lMoney) {
        this.lMoney = lMoney;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getAttachfilepath() {
        return attachfilepath;
    }
    public void setAttachfilepath(String attachfilepath) {
        this.attachfilepath = attachfilepath;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
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
    public Date getgBeginDate() {
        return gBeginDate;
    }
    public void setgBeginDate(Date gBeginDate) {
        this.gBeginDate = gBeginDate;
    }
    public Date getlBeginDate() {
        return lBeginDate;
    }
    public void setlBeginDate(Date lBeginDate) {
        this.lBeginDate = lBeginDate;
    }
    public Date getgEndDate() {
        return gEndDate;
    }
    public void setgEndDate(Date gEndDate) {
        this.gEndDate = gEndDate;
    }
    public Date getlEndDate() {
        return lEndDate;
    }
    public void setlEndDate(Date lEndDate) {
        this.lEndDate = lEndDate;
    }
    public String getOldRatCode() {
        return oldRatCode;
    }
    public void setOldRatCode(String oldRatCode) {
        this.oldRatCode = oldRatCode;
    }
    public String getRatModeName() {
        return ratModeName;
    }
    public void setRatModeName(String ratModeName) {
        this.ratModeName = ratModeName;
    }
    public String getOldRatCodeCode() {
        return oldRatCodeCode;
    }
    public void setOldRatCodeCode(String oldRatCodeCode) {
        this.oldRatCodeCode = oldRatCodeCode;
    }
    public boolean isToUpdate() {
        return toUpdate;
    }
    public void setToUpdate(boolean toUpdate) {
        this.toUpdate = toUpdate;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public Date getsDate() {
        return sDate;
    }
    public void setsDate(Date sDate) {
        this.sDate = sDate;
    }
    public Date geteDate() {
        return eDate;
    }
    public void seteDate(Date eDate) {
        this.eDate = eDate;
    }
}
