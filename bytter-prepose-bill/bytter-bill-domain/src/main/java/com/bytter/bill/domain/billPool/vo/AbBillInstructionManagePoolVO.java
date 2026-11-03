package com.bytter.bill.domain.billPool.vo;
import java.io.Serializable;
/**
 * @Title: Vo
 * @Description: 指令处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class AbBillInstructionManagePoolVO implements Serializable{
	private String id;
	private String ids;
	/*** 数据类型*/
	private String type;
	/*** 业务类型类型：poolBail票据池保证金poolLimit票据池额度poolCon票据池融资合同 */
	private String busType;
	/*** 额度类型：apply	申请applyCheck	申请核准allot	分配 allotCheck	分配核准*/
	private String limitType;
	/*** 操作类型 */
	private String opType;
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
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getLimitType() {
		return limitType;
	}
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "AbBillInstructionManagePoolVO [id=" + id + ", ids=" + ids + ", busType=" + busType + ", limitType="
				+ limitType + ", opType=" + opType + "]";
	}
	
	
}
