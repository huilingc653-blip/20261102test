package com.bytter.bill.domain.bill.bean;


import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * Title: DSLPaymentResponseBean
 * Description:  付款单同步返回响应信息
 * @author  wangh
 * @date 2020-4-21
 */
public class DSLAbEmitBillPayResponseBean {

	@JSONField(name = "RECEIPT_NUM")
	private String receiptNum; //票据号

	@JSONField(name = "STATUS")
	private String status;// 状态(2成功,1失败)

	@JSONField(name = "MESSAGE")
	private String message;// 接受失败原因

	/**orgId*/
	@JSONField(name = "ORG_ID")
	private String orgId;

	/**单位代码*/
	@JSONField(name = "COMPANY_CODE")
	private String companyCode;

	public DSLAbEmitBillPayResponseBean() {
		super();
	}

	public DSLAbEmitBillPayResponseBean(String receiptNum, String status, String message) {
		super();
		this.receiptNum = receiptNum;
		this.status = status;
		this.message = message;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
