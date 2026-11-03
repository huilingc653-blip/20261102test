package com.bytter.guarantee.dto;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author fusb
 * @version 1.0 Title FcBondApplyDTO Description 保证金占用DTO
 * @since 2018年10月30日
 */
@SuppressWarnings("serial")
public class FcBondApplyDTO implements Serializable {
    /**占用id*/
    private String id;
    /** 业务单号 */
    private String billNo;
    /** 占用类型 "0"业务占用,"1"手工占用,
     在FcBondConstant有常量定义*/
    private String applyType;
    /** 项目类型"0"银行授信，"1"银行借款，"2"承兑汇票，"3"其他，"4"授信追加
     在FcBondConstant中有常量定义 */
    private String itemType;
    /** 项目单号 */
    private String itemNo;
    /**占用保证金的项目Id*/
    private String itemId;
    /** 单位ID */
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /** 银行联行号 */
    private String bankId;
    /** 银行名称,不必赋值,只是在查询占用信息的时候保存相应信息*/
    private String bankName;
    /** 银行账号,不必赋值,只是在查询占用信息的时候保存相应信息*/
    private String bankAcc;
    /** 账户名称,不必赋值,只是在查询占用信息的时候保存相应信息*/
    private String accName;
    /** 账户当前可用金额,不必赋值,只是在查询占用信息的时候保存相应信息 */
    private BigDecimal availMoney;
    /** 项目金额 */
    private BigDecimal itemMoney;
    /** 保证金比例 */
    private BigDecimal bondPercent;
    /** 项目开始日期 */
    private java.util.Date startDate;
    /** 项目结束日期 */
    private java.util.Date endDate;
    /** 项目期限,该属性可以不用赋值,在挂接保证金的接口实现类中会重新计算*/
    private Integer itemUnit;
    /** 保证金合同 */
    private String bondContract;
    /** 保证金ID */
    private String bondId;
    /** 占用金额 即为保证金金额*/
    private BigDecimal applyMoney;
    /** 备注 */
    private String remark;
    /** 状态: 0暂存,11待审批,-1打回,95占用,-2已删除,2已释放;在FcBondConstant中有定义*/
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /**币别 */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    private BigDecimal applyMoneyTotal;//保证金总额
    /**修改前挂接保证金Id*/
    private String bondIdPre;

    /**保证金利率*/
    private BigDecimal bondRate;

    /**
     * 保证金类型
     */
    @Dict(dictGroup = "AB_BILL_BAIL_TYPE", inject = "bailTypeName")
    private String bailType;

    private String bailTypeName;

    /**
     * 银行账号ID
     */
    private String bankAccId;

    public String getBailType() {
        return bailType;
    }

    public void setBailType(String bailType) {
        this.bailType = bailType;
    }

    public String getBailTypeName() {
        return bailTypeName;
    }

    public void setBailTypeName(String bailTypeName) {
        this.bailTypeName = bailTypeName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getApplyType() {
        return applyType;
    }
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
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

    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public BigDecimal getItemMoney() {
        return itemMoney;
    }
    public void setItemMoney(BigDecimal itemMoney) {
        this.itemMoney = itemMoney;
    }
    public BigDecimal getBondPercent() {
        return bondPercent;
    }
    public void setBondPercent(BigDecimal bondPercent) {
        this.bondPercent = bondPercent;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public Integer getItemUnit() {
        return itemUnit;
    }
    public void setItemUnit(Integer itemUnit) {
        this.itemUnit = itemUnit;
    }
    public String getBondContract() {
        return bondContract;
    }
    public void setBondContract(String bondContract) {
        this.bondContract = bondContract;
    }
    public String getBondId() {
        return bondId;
    }
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    public BigDecimal getApplyMoney() {
        return applyMoney;
    }
    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
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
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public BigDecimal getAvailMoney() {
        return availMoney;
    }
    public void setAvailMoney(BigDecimal availMoney) {
        this.availMoney = availMoney;
    }
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public BigDecimal getApplyMoneyTotal() {
        return applyMoneyTotal;
    }
    public void setApplyMoneyTotal(BigDecimal applyMoneyTotal) {
        this.applyMoneyTotal = applyMoneyTotal;
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

    public String getBondIdPre() {
        return bondIdPre;
    }

    public void setBondIdPre(String bondIdPre) {
        this.bondIdPre = bondIdPre;
    }

    public BigDecimal getBondRate() {
        return bondRate;
    }

    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }

    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
}
