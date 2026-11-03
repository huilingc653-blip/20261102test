package com.bytter.biz.ttpay.vo;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
/**
 * @Title: VO
 * @Description: 商户信息VO
 * @author 郑刚
 * @date 2019-09-26 15:43:05
 * @version 1.0
 */
@ExcelTarget("TpMerchantVO")
public class TpMerchantVO extends ExcelModel implements java.io.Serializable,IBusinessKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TpMerchantVO(){
		
	}
	public TpMerchantVO(String merchantNo,String rmk){
		this.merchantNo=merchantNo;
		this.rmk=rmk;
	}
	private String businessKey;
	
	private String id;
	
	/**	上级ID	PARENT_ID	VARCHAR2(32)**/
	private String parentId;
	
	/**单位ID	CORP_ID	VARCHAR2(32)**/
	@BtCompositeSearch(fieldName = "m.corp_id")
	private String corpId;
	
	/**单位名称**/
	@Excel(name = "所属单位代码")
	private String corpCode;
	
	/**单位名称**/
	@BtCompositeSearch(fieldName = "m.corp_id")
	@Excel(name = "所属单位名称")
	private String corpName;
	
	/**商户号	MERCHANT_NO	VARCHAR2(255)**/
	@Excel(name = "*商户号",isImportField="true")
	@NotNull(message = TtPayConstant.TTPAY_EXCEL_ERRORMSG_MERCHANTNO_NULL)
	@BtCompositeSearch(fieldName = "m.merchant_No")
	private String merchantNo;
	@Excel(name = "主商户号")
	private String merchantNoParent;
	
	/**第三方支付方式代码	PAY_TYPE	VARCHAR2(20)**/
    @Dict(dictGroup = TtPayConstant.TTPAY_PAYTYPE_DICT, inject = "payTypeName")
	private String payType;
	
	/**第三方支付方式名称**/
	@BtCompositeSearch(fieldName = "m.pay_Type")
	@Excel(name = "*第三方支付方式",isImportField="true")
	@NotNull(message = TtPayConstant.TTPAY_EXCEL_ERRORMSG_PAYTYPE_NULL)
	private String payTypeName;
	
	/**备注	RMK	VARCHAR2(200)**/	
	@Excel(name = "备注")
	@BtCompositeSearch(fieldName = "m.rmk")
	private String rmk;
	
	/**是否上线	IS_ONLINE	VARCHAR2(1)**/	
    @Dict(dictGroup = "sf_yn", inject = "isOnlineName")
	private String isOnline;
	
	/**是否上线	IS_ONLINE	VARCHAR2(1)**/	
	@Excel(name = "是否具备直连接口")
	private String isOnlineName;
	
	/**
	 * 对应子商行号
	 */
	private List<TpMerchantVO> sonMerchantList;
	
	/**
	 * 对应商行参数信息
	 */
	private List<TpMerchantVarVO> merchantVarList;
	
	private String[] sonMerchantNo;
	
	private String[] sonRmk;
	
	private String[] varCode;
	
	private String[] varName;
	
	private String[] varValue;
	
	private String[] fileId;
	/**
	 * 排序字段
	 */
	private Double sort;
	
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
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<TpMerchantVO> getSonMerchantList() {
		return sonMerchantList;
	}
	public void setSonMerchantList(List<TpMerchantVO> sonMerchantList) {
		this.sonMerchantList = sonMerchantList;
	}
	public List<TpMerchantVarVO> getMerchantVarList() {
		return merchantVarList;
	}
	public void setMerchantVarList(List<TpMerchantVarVO> merchantVarList) {
		this.merchantVarList = merchantVarList;
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
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getIsOnlineName() {
		return isOnlineName;
	}
	public void setIsOnlineName(String isOnlineName) {
		this.isOnlineName = isOnlineName;
	}
	public Double getSort() {
		return sort;
	}
	public void setSort(Double sort) {
		this.sort = sort;
	}
	public String[] getSonMerchantNo() {
		return sonMerchantNo;
	}
	public void setSonMerchantNo(String[] sonMerchantNo) {
		this.sonMerchantNo = sonMerchantNo;
	}
	public String[] getSonRmk() {
		return sonRmk;
	}
	public void setSonRmk(String[] sonRmk) {
		this.sonRmk = sonRmk;
	}
	public String[] getVarCode() {
		return varCode;
	}
	public void setVarCode(String[] varCode) {
		this.varCode = varCode;
	}
	public String[] getVarName() {
		return varName;
	}
	public void setVarName(String[] varName) {
		this.varName = varName;
	}
	public String[] getVarValue() {
		return varValue;
	}
	public void setVarValue(String[] varValue) {
		this.varValue = varValue;
	}
	public String getMerchantNoParent() {
		return merchantNoParent;
	}
	public void setMerchantNoParent(String merchantNoParent) {
		this.merchantNoParent = merchantNoParent;
	}
	@Override
	public String toString() {
		return "TpMerchantVO [id=" + id + ", parentId=" + parentId + ", corpId=" + corpId + ", corpCode=" + corpCode
				+ ", corpName=" + corpName + ", merchantNo=" + merchantNo + ", payType=" + payType + ", payTypeName="
				+ payTypeName + ", rmk=" + rmk + ", isOnline=" + isOnline + ", isOnlineName=" + isOnlineName
				+ ", sonMerchantList=" + sonMerchantList + ", merchantVarList=" + merchantVarList + ", sonMerchantNo="
				+ Arrays.toString(sonMerchantNo) + ", sonRmk=" + Arrays.toString(sonRmk) + ", varCode="
				+ Arrays.toString(varCode) + ", varName=" + Arrays.toString(varName) + ", varValue="
				+ Arrays.toString(varValue) + ", sort=" + sort + "]";
	}
	@Override
	public String getBusinessKey() {
		return this.getId();
	}
	
	@Override
	public String getBeanName() {
		return "tpMerchantService";
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String[] getFileId() {
		return fileId;
	}
	public void setFileId(String[] fileId) {
		this.fileId = fileId;
	}
	
}
