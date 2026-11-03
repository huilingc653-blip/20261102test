package com.bytter.biz.finacial.paramconfig.vo;
import com.bytter.core.annotation.Dict;
/**   
 * @Title: Entity
 * @Description: FC_SYS_BUS_RACE
 * @author onlineGenerator
 * @date 2017-05-22 16:10:01
 * @version 1.0
 *
 */
public class FcSysBusRaceVO implements java.io.Serializable  {
	private String id;
	/**系统业务品种代码*/
	private String sysRaceCode;
	/**系统业务品种名称*/
	private String sysRaceName;
	/**有效标识*/
	private String validSign;
	/**备注*/
	private String rmk;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public String getSysRaceName() {
		return sysRaceName;
	}
	public void setSysRaceName(String sysRaceName) {
		this.sysRaceName = sysRaceName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
}
