package com.bytter.cashVoucher.vo;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.enums.CommonDictEnum;
import com.bytter.core.utils.StringUtils;
import com.bytter.cashVoucher.entity.BvPrintFormatDtl;
import com.bytter.cashVoucher.entity.BvPrintMatchRule;

import java.util.List;
/**   
 * @Title: vo
 * @Description: 凭证打印格式vo
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BvPrintFormatVo implements java.io.Serializable {
	
	/**凭证类型ID*/
	private String id;
	/**凭证类型ID*/
	// @Dict(tableName="BV_VOUCHER_TYPE",valueFieldName="id",showFiledName="name",inject="voucherType")
	private String vouTid;
	/**凭证类型*/
	@BtCompositeSearch(fieldName="VOU_TID")
	private String voucherType;
	/**打印场景ID*/
	// @Dict(tableName="BV_VOUTYPE_SCENE",valueFieldName="id",showFiledName="name",inject="sceneName")
	private String sceneId;
	/**打印场景名称*/
	@BtCompositeSearch(fieldName="SCENE_ID")
	private String sceneName;
	/**左边距*/
	@BtCompositeSearch(fieldName="left_Border")
	private Double leftBorder;
	/**打印格式编号*/
	@BtCompositeSearch(fieldName="fmt_No")
	private String fmtNo;
	/**打印格式说明*/
	@BtCompositeSearch(fieldName="fmt_Name")
	private String fmtName;
	/**纸张大小*/
	@BtCompositeSearch(fieldName="paper_Size")
	private String paperSize;
	/**下边距*/
	@BtCompositeSearch(fieldName="up_Border")
	private Double upBorder;
	/** 有效标志*/
	// @Dict(dictGroup = "sf_yn", inject = "validSignName")
	private String validSign;
	/** 背景图片ID*/
	private String backgroundImage;
	@BtCompositeSearch(fieldName="valid_Sign")
	private String validSignName;
	private List<BvPrintMatchRule> printMR;
	private List<BvPrintFormatDtl> printFmtDtl;
	/**备注*/
	private String rmk;
	/**
	 * 行数
	 */
	private Integer index;
	/**
	 * 匹配结果 1是0否2凭证号获取失败3页面填写凭证号不存在4获取凭证号重复
	 */
	private String isMatch;
	/**
	 * 打印占用凭证号
	 */
	private String vouNo;
	/**
	 * 打印金额
	 */
	private String money;
	/**
	 * 打印银行账户
	 */
	private String bankAcc;
	/**
	 * 打印银行大类
	 */
	private String bankType;
	/**
	 * 打印占用凭证号Id
	 */
	private String vouId;
	/**
	 * 操作信息表ID
	 */
	private String opInfoId;
	/**
	 * 单位ID
	 */
	private String corpId;
	/**
	 * 凭证套打完成回调servicebean
	 */
	private String servicebeanpop;
	public String getVouNo() {
		return vouNo;
	}
	public void setVouNo(String vouNo) {
		this.vouNo = vouNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public Double getLeftBorder() {
		return leftBorder;
	}
	public void setLeftBorder(Double leftBorder) {
		this.leftBorder = leftBorder;
	}
	public String getFmtNo() {
		return fmtNo;
	}
	public void setFmtNo(String fmtNo) {
		this.fmtNo = fmtNo;
	}
	public String getFmtName() {
		return fmtName;
	}
	public void setFmtName(String fmtName) {
		this.fmtName = fmtName;
	}
	public String getPaperSize() {
		return paperSize;
	}
	public void setPaperSize(String paperSize) {
		this.paperSize = paperSize;
	}
	public Double getUpBorder() {
		return upBorder;
	}
	public void setUpBorder(Double upBorder) {
		this.upBorder = upBorder;
	}
	public String getValidSign() {
//		if("1".equals(validSign)){
//			return "是";
//		}else if("0".equals(validSign)){
//			return "否";
//		}
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
	public String getValidSignName() {
		if (ObjectUtil.isNotEmpty(validSign)) {
			return CommonDictEnum.getDictValue("sf_yn", validSign);
		}
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public List<BvPrintMatchRule> getPrintMR() {
		return printMR;
	}
	public void setPrintMR(List<BvPrintMatchRule> printMR) {
		this.printMR = printMR;
	}
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	public List<BvPrintFormatDtl> getPrintFmtDtl() {
		return printFmtDtl;
	}
	public void setPrintFmtDtl(List<BvPrintFormatDtl> printFmtDtl) {
		this.printFmtDtl = printFmtDtl;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getIsMatch() {
		return isMatch;
	}
	public void setIsMatch(String isMatch) {
		this.isMatch = isMatch;
	}
	public String getVouId() {
		return vouId;
	}
	public void setVouId(String vouId) {
		this.vouId = vouId;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getOpInfoId() {
		return opInfoId;
	}
	public void setOpInfoId(String opInfoId) {
		this.opInfoId = opInfoId;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getServicebeanpop() {
		return servicebeanpop;
	}
	public void setServicebeanpop(String servicebeanpop) {
		this.servicebeanpop = servicebeanpop;
	}
	public String getBackgroundImage() {
		return backgroundImage;
	}
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	public String getSceneId() {
		return sceneId;
	}
	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
		if(StringUtils.isEmpty(sceneId)){
			this.sceneId = BlankvouConstant.BV_OTHERSCENE_UUID;
		}
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	@Override
	public String toString() {
		return "BvPrintFormatVo{" +
				"id='" + id + '\'' +
				", vouTid='" + vouTid + '\'' +
				", voucherType='" + voucherType + '\'' +
				", leftBorder=" + leftBorder +
				", fmtNo='" + fmtNo + '\'' +
				", fmtName='" + fmtName + '\'' +
				", paperSize='" + paperSize + '\'' +
				", upBorder=" + upBorder +
				", validSign='" + validSign + '\'' +
				", backgroundImage='" + backgroundImage + '\'' +
				", validSignName='" + validSignName + '\'' +
				", printMR=" + printMR +
				", printFmtDtl=" + printFmtDtl +
				", rmk='" + rmk + '\'' +
				", index=" + index +
				", isMatch='" + isMatch + '\'' +
				", vouNo='" + vouNo + '\'' +
				", money='" + money + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", bankType='" + bankType + '\'' +
				", vouId='" + vouId + '\'' +
				", opInfoId='" + opInfoId + '\'' +
				", corpId='" + corpId + '\'' +
				", servicebeanpop='" + servicebeanpop + '\'' +
				'}';
	}
}
