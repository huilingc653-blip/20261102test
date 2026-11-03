package com.bytter.cashVoucher.vo;
/**
 * 
 * @author caojy
 * 
 */
public enum BvPayeeChequeEnum {
	//收款支票状态 状态：-2已删除，-1已打回，0已登记，1复核中，2已入库，3已退票，4已作废
	PAYEECHEQUE_STATUS_DELETE(-2, "delete", "03","bill.log.delete.succ"),
	PAYEECHEQUE_STATUS_BACK(-1, "back", "30","bill.log.back.succ"),
	PAYEECHEQUE_STATUS_SAVE(0, "save", "01","bill.log.save.succ"),
	PAYEECHEQUE_STATUS_CHECK(1, "check", "27","bill.blankvou.bvPayeeChequ.save"),
	PAYEECHEQUE_STATUS_INPUT(2, "input", "06","bill.log.check.succ"),
	PAYEECHEQUE_STATUS_BACKBILL(3, "backbill", "04","bill.blankvou.bvPayeeCheque.returnTicket"),
	PAYEECHEQUE_STATUS_CANCELLATION(4, "cancellation", "23","bill.blankvou.bvPayeeCheque.cancel"),
	
	
	PAYEECHEQUE_BASE_SAVE(0, "baseSave", "02","common.update.data"),
	PAYEECHEQUE_BASE_CHECK(1, "baseCheck", "27","bill.log.submit.succ");
	
	private Integer status;
	private String operate;
	private String logtype;
	private String msg;
	BvPayeeChequeEnum(Integer status, String operate, String logtype, String msg) {
		this.status = status;
		this.operate = operate;
		this.logtype = logtype;
		this.msg = msg;
	}
	public static BvPayeeChequeEnum getResource(String operate) {
		for (BvPayeeChequeEnum c : BvPayeeChequeEnum.values()) {
			if (c.getOperate().equals(operate)) {
				return c;
			}
		}
		return null;
	}
	public static String getResourceLogtype(String operate) {
		for (BvPayeeChequeEnum c : BvPayeeChequeEnum.values()) {
			if (c.getOperate().equals(operate)) {
				return c.getLogtype();
			}
		}
		return null;
	}
	public static String getResourceMsg(String operate) {
		for (BvPayeeChequeEnum c : BvPayeeChequeEnum.values()) {
			if (c.getOperate().equals(operate)) {
				return c.msg;
			}
		}
		return null;
	}
	public Integer getStatus() {
		return status;
	}
	public String getOperate() {
		return operate;
	}
	public String getLogtype() {
		return logtype;
	}
	public String getMsg() {
		return msg;
	}
}
