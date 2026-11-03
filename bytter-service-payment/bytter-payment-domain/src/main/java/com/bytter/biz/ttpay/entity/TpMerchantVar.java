package com.bytter.biz.ttpay.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 商户参数信息对应实体
 * @author 郑刚
 * @date 2019-09-26 15:43:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "TP_MERCHANT_VAR", schema = "")
public class TpMerchantVar extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/**商户表ID	MCH_ID	VARCHAR2(32)**/
	private String mchId;
	
	/**参数代码:	VAR_CODE	VARCHAR2(20)**/
	private String varCode;
	
	/**参数名称:	VAR_NAME	VARCHAR2(50)**/
	private String varName;
	
	/**参数值	VAR_VALUE	VARCHAR2(2000)**/
	private String varValue;
    /**参数值	VAR_VALUE_STR	VARCHAR2(2000)**/
    private String varValueStr;
	
	/**附件ID**/
	private String fileId;
	
	/**
	 * 排序字段
	 */
	private Double sort;
	@Column(name = "mch_Id")
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	@Column(name = "var_Code")
	public String getVarCode() {
		return varCode;
	}
	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}
	@Column(name = "var_Name")
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	@Column(name = "var_Value")
	public String getVarValue() {
		return varValue;
	}
	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}
    @Column(name = "VAR_VALUE_STR")
    public String getVarValueStr() {
        return varValueStr;
    }
    public void setVarValueStr(String varValueStr) {
        this.varValueStr = varValueStr;
    }
	@Column(name = "sort")
	public Double getSort() {
		return sort;
	}
	public void setSort(Double sort) {
		this.sort = sort;
	}
	@Column(name = "FILE_ID")
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	@Override
	public String toString() {
		return "TpMerchantVar [mchId=" + mchId + ", varCode=" + varCode + ", varName=" + varName + ", varValue="
				+ varValue + ", sort=" + sort + "]";
	}
	
}
