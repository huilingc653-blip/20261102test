package com.bytter.bill.domain.billPool.vo;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhenghj
 * @Title: 额度分配核准VO
 * @version 1.0
 * @Description: 额度分配核准Vo
 * @date 2020-2-24
 */
@SuppressWarnings("serial")
public class AbPoolLimitAllotCheckVO implements java.io.Serializable,  IBusinessKey {
    /**
     * 额度分配核准Id
     */
    private String id;
    private String ids;
    /**单位ID**/
    @BtCompositeSearch(fieldName = "APLAC.corp_id")
    private String corpId;
    private String corpCode;
    private String corpName;
    /**父级单位ID**/
    private String parentId;
    private String parentName;
    /**
     * 票据池id
     */
    @BtCompositeSearch(fieldName = "APLAC.BP_Id")
    private String bpId;
    /**
     * 票据池名称
     */
    @BtCompositeSearch(fieldName = "AP.BP_NAME")
    private String bpName;
    /**
     * 业务类型ID
     */
    private String busTypeId;
    /**
     * 业务类型名称
     */
    private String busTypeName;
    /**
     * 额度种类:1：低风险， 2：非低风险
     */
    @Dict(dictGroup = "AB_POOL_LIMT_TYPE", inject = "limtTypeName")
    @BtCompositeSearch(fieldName = "APLAC.LIMT_TYPE")
    private  String limtType;
    private  String limtTypeName;
    /**
     * 总额度
     */
    @BtCompositeSearch(fieldName = "APLAC.POOL_MONEY")
    private BigDecimal poolMoney;
    /**
     * 可用额度
     */
    @BtCompositeSearch(fieldName = "APLAC.VALID_MONEY")
    private BigDecimal validMoney;
    /**
     * 已用额度
     */
    @BtCompositeSearch(fieldName = "APLAC.LOCK_MONEY")
    private BigDecimal lockMoney;
    /**
     * 申请额度
     */
    @BtCompositeSearch(fieldName = "APLAC.APPLY_MONEY")
    private BigDecimal applyMoney;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "APLAC.END_DATE")
    private Date endDate;
    /**
     * 核准类型：1通过0不通过
     */
    @Dict(dictGroup = "AB_POOL_CHECK_TYPE", inject = "checkTypeName")
    private String checkType;
    @BtCompositeSearch(fieldName = "APLAC.check_Type")
    private String checkTypeName;
    /**
     * 核准信息
     */
    @BtCompositeSearch(fieldName = "APLAC.check_Msg")
    private String checkMsg;
    /**
     * 银行返回代码
     */
    private String returnCode;
    /**
     * 银行返回消息
     */
    @BtCompositeSearch(fieldName = "APBE.RE_MSG")
    private String returnMsg;
    /**
     * 指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3
     */
    @Dict(dictGroup = "AB_POOL_OPT_STATUS", inject = "optStatusName")
    @BtCompositeSearch(fieldName = "APLAC.OPT_STATUS")
    private String optStatus;
    private String optStatusName;
    /**
     * 审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过
     */
    @Dict(dictGroup = "AB_POOL_STATUS", inject = "statusName")
    @BtCompositeSearch(fieldName = "APLAC.STATUS")
    private Integer status;
    private String statusName;
    /**
     * 单位层级
     */
    private Integer bustypeLevel;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "APLAC.create_Time")
    private Date createTime;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    private String businessKey;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
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
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getBpId() {
        return bpId;
    }
    public void setBpId(String bpId) {
        this.bpId = bpId;
    }
    public String getBpName() {
        return bpName;
    }
    public void setBpName(String bpName) {
        this.bpName = bpName;
    }
    public String getBusTypeId() {
        return busTypeId;
    }
    public void setBusTypeId(String busTypeId) {
        this.busTypeId = busTypeId;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }
    public String getLimtType() {
        return limtType;
    }
    public void setLimtType(String limtType) {
        this.limtType = limtType;
    }
    public String getLimtTypeName() {
        return limtTypeName;
    }
    public void setLimtTypeName(String limtTypeName) {
        this.limtTypeName = limtTypeName;
    }
    public BigDecimal getPoolMoney() {
        return poolMoney;
    }
    public void setPoolMoney(BigDecimal poolMoney) {
        this.poolMoney = poolMoney;
    }
    public BigDecimal getValidMoney() {
        return validMoney;
    }
    public void setValidMoney(BigDecimal validMoney) {
        this.validMoney = validMoney;
    }
    public BigDecimal getLockMoney() {
        return lockMoney;
    }
    public void setLockMoney(BigDecimal lockMoney) {
        this.lockMoney = lockMoney;
    }
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public String getCheckTypeName() {
        return checkTypeName;
    }
    public void setCheckTypeName(String checkTypeName) {
        this.checkTypeName = checkTypeName;
    }
    public String getCheckMsg() {
        return checkMsg;
    }
    public void setCheckMsg(String checkMsg) {
        this.checkMsg = checkMsg;
    }
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    public String getOptStatus() {
        return optStatus;
    }
    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }
    public String getOptStatusName() {
        return optStatusName;
    }
    public void setOptStatusName(String optStatusName) {
        this.optStatusName = optStatusName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public Integer getBustypeLevel() {
        return bustypeLevel;
    }
    public void setBustypeLevel(Integer bustypeLevel) {
        this.bustypeLevel = bustypeLevel;
    }
    public String getCheckboxHide() {
        if(StringUtils.isNotEmpty(this.getBusinessKey())){
            //审批列表查询
            return "0";
        }
        if(AbPoolConstant.AB_POOL_STATUS_1.equals(this.getStatus()) || StringUtils.isBlank(checkType)){
            return "0";
        }else{
            return "1";
        }
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getBusinessKey() {
        return businessKey;
    }
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    @Override
    public String toString() {
        return "AbPoolLimitAllotCheckVO{" +
                "id='" + id + '\'' +
                ", ids='" + ids + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", corpName='" + corpName + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentName='" + parentName + '\'' +
                ", bpId='" + bpId + '\'' +
                ", bpName='" + bpName + '\'' +
                ", busTypeId='" + busTypeId + '\'' +
                ", busTypeName='" + busTypeName + '\'' +
                ", limtType='" + limtType + '\'' +
                ", limtTypeName='" + limtTypeName + '\'' +
                ", poolMoney=" + poolMoney +
                ", validMoney=" + validMoney +
                ", lockMoney=" + lockMoney +
                ", applyMoney=" + applyMoney +
                ", endDate=" + endDate +
                ", checkType='" + checkType + '\'' +
                ", checkTypeName='" + checkTypeName + '\'' +
                ", checkMsg='" + checkMsg + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", optStatusName='" + optStatusName + '\'' +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", bustypeLevel=" + bustypeLevel +
                ", rmk='" + rmk + '\'' +
                ", createTime=" + createTime +
                ", checkboxHide='" + checkboxHide + '\'' +
                ", businessKey='" + businessKey + '\'' +
                '}';
    }
}
