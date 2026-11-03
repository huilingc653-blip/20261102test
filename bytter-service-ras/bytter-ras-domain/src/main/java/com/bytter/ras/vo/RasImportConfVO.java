package com.bytter.ras.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bytter.core.annotation.Dict;
//import com.bytter.core.injections.IBaseDataInject;
import com.bytter.poi.handler.ExcelModel;

/**
 * 
* @Title:  营收稽核 导入配置表
* @Package com.bytter.ras.vo   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午2:47:37   
* @version V1.0
 */
public class RasImportConfVO extends ExcelModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -920840418281964830L;
	private String id;
	private String  type;
	/**缴款方式:002支付宝003微信004银联POS机005收钱吧006其它 排除001现金*/
    @Dict(dictGroup = "RAS_PAY_TYPE", inject = "payTypeName")
	private String  payType;
    private String payTypeName;
	/**文件类型xls xlsx csv txt 营收不存在txt*/
    @Dict(dictGroup = "RAS_IMPORT_FILE_DICT", inject = "fileTypeName")
	private String  fileType;  
    private String  fileTypeName;
    
    /**缴款方式:002支付宝003微信004银联POS机005收钱吧006其它 排除001现金*/
    @Dict(dictGroup = "RAS_AUDIT_DATASOURCE", inject = "dateSourceName")
	private String  dateSource;
	private String  dateSourceName;

	/**起始行*/
	private Integer startLine;
	/**是否有分隔符:0否1是*/
    @Dict(dictGroup = "RAS_RECEIVED_SEPARATOR", inject = "isFormatName")
	private String  isFormat;
	private String  isFormatName;
	/**txt分隔符*/
	private String  txtFormat;
	/**创建人名称*/
    private String  createName;  
    /**创建人登录名称*/
    private String  createBy;   
    /**创建日期*/
    private Date  createDate;   
    /**更新人名称*/
    private String  updateName;   
    /**更新人登录名称*/
    private String  updateBy;     
    /**更新日期*/
    private Date  updateDate;
    /**详细配置信息类**/
    private List<RasImportConfDtlVO> dtlList = new ArrayList<RasImportConfDtlVO>(); 
    /**
	 * 类型：对应字段: 营收 （serialNo交易流水号orderNO营收订单号shopName门店名称transDate交易日期
	 * money营收金额payType缴款方式custName客户名称custAcc客户账号rmk备注） 实收
	 * （serialNo交易流水号orderNO实收订单号shopName门店名称payeeAcc
	 * 收款账号payeeAccName收款户名payeeBankName收款账号开户行transDate交易日期payType缴款方式money
	 * 实收金额yhMoney优惠金额transMoney交易金额custName客户名称custAcc客户账号rmk备注）
	 */
	private String fieldKey;
	/** 对应起始值 */
	private String startVal;
	/** 对应结束值 */
	private String endVal;
	
	/** 详细表信息ID**/
	private String ricdId;
    
	/** 操作权限**/
	private Integer usePower;

    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Integer getStartLine() {
		return startLine;
	}
	public void setStartLine(Integer startLine) {
		this.startLine = startLine;
	}
	public String getIsFormat() {
		return isFormat;
	}
	public void setIsFormat(String isFormat) {
		this.isFormat = isFormat;
	}
	public String getTxtFormat() {
		return txtFormat;
	}
	public void setTxtFormat(String txtFormat) {
		this.txtFormat = txtFormat;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
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
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
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
	public String getFieldKey() {
		return fieldKey;
	}
	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}
	public String getStartVal() {
		return startVal;
	}
	public void setStartVal(String startVal) {
		this.startVal = startVal;
	}
	public String getEndVal() {
		return endVal;
	}
	public void setEndVal(String endVal) {
		this.endVal = endVal;
	}
	public Integer getUsePower() {
		return usePower;
	}
	public void setUsePower(Integer usePower) {
		this.usePower = usePower;
	}
    public RasImportConfVO(){
    	
    }
	public List<RasImportConfDtlVO> getDtlList() {
		return dtlList;
	}
	public void setDtlList(List<RasImportConfDtlVO> dtlList) {
		this.dtlList = dtlList;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getFileTypeName() {
		return fileTypeName;
	}
	public void setFileTypeName(String fileTypeName) {
		this.fileTypeName = fileTypeName;
	}
	public String getDateSource() {
		return dateSource;
	}
	public void setDateSource(String dateSource) {
		this.dateSource = dateSource;
	}
	public String getDateSourceName() {
		return dateSourceName;
	}
	public void setDateSourceName(String dateSourceName) {
		this.dateSourceName = dateSourceName;
	}
	public String getRicdId() {
		return ricdId;
	}
	public void setRicdId(String ricdId) {
		this.ricdId = ricdId;
	}
	public String getIsFormatName() {
		return isFormatName;
	}
	public void setIsFormatName(String isFormatName) {
		this.isFormatName = isFormatName;
	}
    
}
