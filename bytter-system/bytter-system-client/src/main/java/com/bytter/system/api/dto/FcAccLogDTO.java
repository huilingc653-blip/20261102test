package com.bytter.system.api.dto;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 *
 *
 */
public class FcAccLogDTO implements Serializable {
	private String id;
	/**账号*/
	private String account;
	/**业务表单据Id*/
	private String bizId;
	/**业务类型[10开户，11改户，12初始化，13销户，14冻结，15解冻]*/
	@Dict(dictGroup = "fcAcc.bizType", inject = "bizTypeName")
	private String bizType;
	private String bizTypeName;
	/**业务信息*/
	private String bizMsg;
	/**操作人*/
	@Dict(tableName="sys_user",valueFieldName="login_name",showFiledName="username", inject = "opName")
	private String opCode;
	private String opName;
	/**操作时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Timestamp opTime;
	/**操作类型[T暂存，D删除,A提交，M修改，C复核,I导入]*/
	@Dict(dictGroup = "fcAcc.opType", inject = "opTypeName")
	private String opType;
	private String opTypeName;
	/**账户类别*/
	private String accType;
	public String getBizTypeName() {
		return bizTypeName;
	}
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	public String getOpTypeName() {
		return opTypeName;
	}
	public void setOpTypeName(String opTypeName) {
		this.opTypeName = opTypeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getBizMsg() {
		return bizMsg;
	}
	public void setBizMsg(String bizMsg) {
		this.bizMsg = bizMsg;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public Timestamp getOpTime() {
		return opTime;
	}
	public void setOpTime(Timestamp opTime) {
		this.opTime = opTime;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
}