package com.bytter.system.inneraccount.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
/**
 *
 *
 */
@Entity
@Table(name = "FC_ACC_LOG", schema = "")
public class FcAccLog extends IdEntity implements java.io.Serializable {
	/**账号*/
	private String account;
	/**业务表单据Id*/
	private String bizId;
	/**业务类型[10开户，11改户，12初始化，13销户，14冻结，15解冻]*/
	private String bizType;
	/**业务信息*/
	private String bizMsg;
	/**操作人*/
	private String opCode;
	/**操作时间*/
	private Timestamp opTime;
	/**操作类型[T暂存，D删除,A提交，M修改，C复核]*/
	private String opType;
	/**账户类别*/
	private String accType;
	@Column(name ="ACCOUNT",nullable=false,length=50)
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@Column(name ="BIZ_ID",nullable=false)
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	@Column(name ="BIZ_TYPE",nullable=false)
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	@Column(name ="BIZ_MSG",nullable=false)
	public String getBizMsg() {
		return bizMsg;
	}
	public void setBizMsg(String bizMsg) {
		this.bizMsg = bizMsg;
	}
	@Column(name ="OP_CODE",nullable=false)
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	@Column(name ="OP_TIME",nullable=false)
	public Timestamp getOpTime() {
		return opTime;
	}
	public void setOpTime(Timestamp opTime) {
		this.opTime = opTime;
	}
	@Column(name ="OP_TYPE",nullable=false)
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	@Column(name ="ACC_Type",nullable=true)
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
}