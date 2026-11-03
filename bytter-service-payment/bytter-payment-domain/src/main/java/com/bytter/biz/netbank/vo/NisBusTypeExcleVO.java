package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wangzx
 * @Description 业务类别单位设置
 * @date 2017.12.14
 */
@SuppressWarnings("serial")
@ExcelTarget("NisBusTypeExcleVO")
public class NisBusTypeExcleVO extends ExcelModel implements Serializable {
    //	@Dict(tableName="NIS_BUS_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
    private String id;
    /**
     * 业务类别编码
     */
    @Excel(name = "*业务种类编码")
    @NotNull(message = "nis.assign.message.bustype.null")
    @BtCompositeSearch(fieldName = "n.bus_type")
    private String busType;
    /**
     * 业务类别名称
     */
    @Excel(name = "*业务种类名称")
    @NotNull(message = "nis.assign.message.bustypename.null")
    @BtCompositeSearch(fieldName = "n.bus_type_name")
    private String busTypeName;
    private String busTypeNameShow;
    /**
     * 收付款标志
     */
    @Excel(name = "*收付款标志", type = 1, numFormat = "0")
    @NotNull(message = "nis.assign.message.cdsign.null")
    @BtCompositeSearch(fieldName = "n.cd_sign")
//	@Dict(dictGroup = "nisBusType.cdSign", inject = "cdSignStr")
    private String cdSign;
    private String cdSignStr;
    /**
     * 计划科目
     */
    @Excel(name = "计划科目")
    @BtCompositeSearch(fieldName = "n.item_code")
    private String itemCode;
    /**
     * 计划科目名称
     */
    @BtCompositeSearch(fieldName = "f.item_name")
    private String itemName;
    /**
     * 有效标志
     */
    @BtCompositeSearch(fieldName = "n.valid_sign")
    private String validSign;
    /**
     * 对应系统业务类别
     */
    private String sysBusType;
    /**
     * 对应系统业务类别名称
     */
    @Excel(name = "对应系统业务类别")
    private String sysBusTypeName;
    /**
     * 结算类型
     */
    private String settleType;
    /**
     * 结算类型名称
     */
    @Excel(name = "结算类型")
    private String settleTypeName;
    /**
     * 匹配规则  01-或者  02-并且
     */
    @Dict(dictGroup = "FUNDWARN_NUM_OPERATOR", inject = "matchRuleName")
    @Excel(name = "匹配规则", replace = {"或者_01", "并且_02"})
    private String matchRule;
    private String matchRuleName;
    /**
     * 匹配要素 remark-摘要、bankAcc-本方账户、accName-本方账户名、oppAccNo-对方账户、oppAccName-对方账户名、curId-币别、corpId-单位名称、uses-用途、postscript-附言
     */
    @Dict(dictGroup = "nisBusTypeRule.matchElements", inject = "matchElementsName")
    @Excel(name = "匹配要素")
    private String matchElements;
    private String matchElementsName;
    /**
     * 匹配关系 1-包含、2-不包含、3-等于、4-不等于
     */
    @Dict(dictGroup = "nisBusTypeRule.matchRelations", inject = "matchRelationsName")
    @Excel(name = "匹配关系")
    private String matchRelations;
    private String matchRelationsName;
    /**
     * 匹配内容
     */
    @Excel(name = "匹配内容")
    private String matchContent;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  业务类别编码
     */
    public String getBusType() {
        return this.busType;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  业务类别编码
     */
    public void setBusType(String busType) {
        this.busType = busType;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  业务类别名称
     */
    public String getBusTypeName() {
        return this.busTypeName;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  业务类别名称
     */
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public String getBusTypeNameShow() {
        return busTypeNameShow;
    }

    public void setBusTypeNameShow(String busTypeNameShow) {
        this.busTypeNameShow = busTypeNameShow;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  收付款标志
     */
    public String getCdSign() {
        return this.cdSign;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  收付款标志
     */
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }


    public String getCdSignStr() {
        return cdSignStr;
    }

    public void setCdSignStr(String cdSignStr) {
        this.cdSignStr = cdSignStr;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  计划科目
     */
    public String getItemCode() {
        return this.itemCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  计划科目
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Override
    public String getBeanName() {
        return "nisBusTypeService";
    }

    public String getSysBusType() {
        return sysBusType;
    }

    public void setSysBusType(String sysBusType) {
        this.sysBusType = sysBusType;
    }

    public String getSysBusTypeName() {
        return sysBusTypeName;
    }

    public void setSysBusTypeName(String sysBusTypeName) {
        this.sysBusTypeName = sysBusTypeName;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public String getSettleTypeName() {
        return settleTypeName;
    }

    public void setSettleTypeName(String settleTypeName) {
        this.settleTypeName = settleTypeName;
    }

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    public String getMatchRuleName() {
        return matchRuleName;
    }

    public void setMatchRuleName(String matchRuleName) {
        this.matchRuleName = matchRuleName;
    }

    public String getMatchElements() {
        return matchElements;
    }

    public void setMatchElements(String matchElements) {
        this.matchElements = matchElements;
    }

    public String getMatchElementsName() {
        return matchElementsName;
    }

    public void setMatchElementsName(String matchElementsName) {
        this.matchElementsName = matchElementsName;
    }

    public String getMatchRelations() {
        return matchRelations;
    }

    public void setMatchRelations(String matchRelations) {
        this.matchRelations = matchRelations;
    }

    public String getMatchRelationsName() {
        return matchRelationsName;
    }

    public void setMatchRelationsName(String matchRelationsName) {
        this.matchRelationsName = matchRelationsName;
    }

    public String getMatchContent() {
        return matchContent;
    }

    public void setMatchContent(String matchContent) {
        this.matchContent = matchContent;
    }
}
