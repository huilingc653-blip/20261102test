package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: VO
 * @Description: 交易组
 * @author huangx
 * @date 2020-02-27 15:20:33
 * @version 1.0
 *
 */
public class FexTranGroupVO implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    private String id;
    /**流水号*/
    @BtCompositeSearch(fieldName = "t.bill_No")
    private String billNo;
    /**名称*/
    @BtCompositeSearch(fieldName = "t.group_Name")
    private String groupName;
    /**描述*/
    private String groupDesc;
    /**产品ID*/
    @BtCompositeSearch(fieldName = "t.pro_Id")
    private String proId;
    @BtCompositeSearch(fieldName = "f.pro_Name")
    private String proName;
    /**交易组规则ID*/
    private String ruleId;
    @BtCompositeSearch(fieldName = "r.rule_Name")
    private String ruleName;
    /**有效标志 1-是 0-否*/
    @BtCompositeSearch(fieldName = "t.valid_Sign")
    private String validSign;
    /**备注*/
    private String rmk;
    /**主交易参考信息*/
    private String mainInfo;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getMainInfo() {
        return mainInfo;
    }

    public void setMainInfo(String mainInfo) {
        this.mainInfo = mainInfo;
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
}
