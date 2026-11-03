package com.bytter.biz.ttpay.vo;
import com.bytter.system.api.dto.AttachmentDTO;
/**
 * @Title: VO
 * @Description: 商户信息参数VO
 * @author 郑刚
 * @date 2019-09-26 15:43:05
 * @version 1.0
 */
public class TpMerchantVarVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public TpMerchantVarVO(){
		
	}
	public TpMerchantVarVO(String varCode,String varName,String varValue,String fileId){
		this.varCode=varCode;
		this.varName=varName;
		this.varValue=varValue;
		this.fileId=fileId;
	}
	public TpMerchantVarVO(String varCode,String varName,String varValue){
		this.varCode=varCode;
		this.varName=varName;
		this.varValue=varValue;
	}
	private String id;
	
	/**商户表ID	MCH_ID	VARCHAR2(32)**/
	private String mchId;
	
	/**参数代码:	VAR_CODE	VARCHAR2(20)**/
	private String varCode;
	
	/**参数名称:	VAR_NAME	VARCHAR2(50)**/
	private String varName;
	
	/**参数值	VAR_VALUE	VARCHAR2(2000)**/
	private String varValue;
	/**文件ID	**/
	private String fileId;
	
	private AttachmentDTO attachDTO;
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getVarCode() {
		return varCode;
	}
	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getVarValue() {
		return varValue;
	}
	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public AttachmentDTO getAttachDTO() {
		return attachDTO;
	}
	public void setAttachDTO(AttachmentDTO attachDTO) {
		this.attachDTO = attachDTO;
	}
	@Override
	public String toString() {
		return "TpMerchantVarVO [id=" + id + ", mchId=" + mchId + ", varCode=" + varCode + ", varName=" + varName
				+ ", varValue=" + varValue + "]";
	}
}
