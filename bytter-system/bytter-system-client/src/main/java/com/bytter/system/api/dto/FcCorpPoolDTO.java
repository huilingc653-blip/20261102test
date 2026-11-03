package com.bytter.system.api.dto;


import java.io.Serializable;
/**
 * Title: Entity
 * Description: 资金池单位表
 * @author wangzx
 * @since 2019-04-30 16:44:38
 * @version 1.0
 *
 */

public class FcCorpPoolDTO implements Serializable{

	private String id;

	/**单位ID*/
	private String corpId;

	/**单位编码*/
	private String corpCode;

	/**单位名称*/
	private String corpName;

	/**资金池代码*/
	private String poolCode;

	/**资金池名称*/
	private String poolName;

	/**状态*/
	private String status;

	/**所属资金池*/
	private String poolId;

	/**主体单位标识*/
	private String poolSign;

	/**备注*/
	private String rmk;

	/**显示名称*/
	private String showName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
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
	public String getPoolCode() {
		return poolCode;
	}
	public void setPoolCode(String poolCode) {
		this.poolCode = poolCode;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	public String getPoolSign() {
		return poolSign;
	}
	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
}
