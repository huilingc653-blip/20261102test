package com.bytter.system.vo;
import com.bytter.system.api.vo.BtBankAccVO;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: TreeVO
 * @date 2017-05-23 17:37:39
 */
@SuppressWarnings("serial")
public class TreeVO implements java.io.Serializable{
	/**ID,资金池时存资金池ID，非资金池存单位ID*/
	private String id;
	/**父账号id*/
	private String parentId;
	/**编码*/
	private String code;
	/**名称*/
	private String name;
	/**资金池时存单位ID*/
	private String corpId;
	/**主体单位标识 0-非资金池单位 1-资金池*/
	private String poolSign;
	
	private List<BtBankAccVO> accList;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BtBankAccVO> getAccList() {
		return accList;
	}
	public void setAccList(List<BtBankAccVO> accList) {
		this.accList = accList;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getPoolSign() {
		return poolSign;
	}
	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}
}
