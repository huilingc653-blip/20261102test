package com.bytter.system.api.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 *
 *
 */
@ApiModel("账户日志信息")
public class FcAccLogVO implements Serializable {
	@ApiModelProperty(value = "主键ID")
	private String id;
	/**账号*/
	@ApiModelProperty(value = "账号")
	private String account;
	/**业务表单据Id*/
	@ApiModelProperty(value = "业务表单据Id")
	private String bizId;
	/**业务类型[10开户，11改户，12初始化，13销户，14冻结，15解冻]*/
	@Dict(dictGroup = "fcAcc.bizType", inject = "bizTypeName")
	@ApiModelProperty(value = "业务类型[10开户，11改户，12初始化，13销户，14冻结，15解冻]")
	private String bizType;
	@ApiModelProperty(value = "业务类型名称")
	private String bizTypeName;
	/**业务信息*/
	@ApiModelProperty(value = "业务信息")
	private String bizMsg;
	/**操作人*/
	@Dict(tableName="sys_user",valueFieldName="login_name",showFiledName="username", inject = "opName")
	@ApiModelProperty(value = "操作人")
	private String opCode;
	@ApiModelProperty(value = "操作人姓名")
	private String opName;
	/**操作时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@ApiModelProperty(value = "操作时间")
	private Timestamp opTime;
	/**操作类型[T暂存，D删除，A提交，M修改，C复核,I导入]*/
	@Dict(dictGroup = "fcAcc.opType", inject = "opTypeName")
	@ApiModelProperty(value = "操作类型[T暂存，D删除，A提交，M修改，C复核,I导入]")
	private String opType;
	@ApiModelProperty(value = "操作类型名称")
	private String opTypeName;
	/**账户类别*/
	@ApiModelProperty(value = "账户类别")
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