package com.bytter.subject.vo;
import com.bytter.core.annotation.Dict;
/**   
 * @Title: Entity
 * @Description: 系统科目
 * @author onlineGenerator
 * @date 2017-05-23 15:11:52
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class FcSubjectSortVo implements java.io.Serializable {
	@Dict(tableName="FC_SUBJECT_SORT",valueFieldName="id",showFiledName="SYS_SUBJECT_NAME",inject="sysSubjectName")
	private String id;
	/**
	 * 系统科目代码
	 */
	private String sysSubjectCode;
	/**
	 * 系统科目名称
	 */
	private String sysSubjectName;
	/**
	 * 是否唯一
	 */
	private String ifUnique;
	/**
	 * 备注
	 */
	private String rmk;
	public String getSysSubjectCode() {
		return sysSubjectCode;
	}
	public void setSysSubjectCode(String sysSubjectCode) {
		this.sysSubjectCode = sysSubjectCode;
	}
	public String getSysSubjectName() {
		return sysSubjectName;
	}
	public void setSysSubjectName(String sysSubjectName) {
		this.sysSubjectName = sysSubjectName;
	}
	public String getIfUnique() {
		return ifUnique;
	}
	public void setIfUnique(String ifUnique) {
		this.ifUnique = ifUnique;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
