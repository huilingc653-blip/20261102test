package com.bytter.bill.domain.bill.vo;
/**   
 * @Title: Entity
 * @Description: 票据附件操作Vo
 * @author onlineGenerator
 * @date 2017-10-24 14:33:51
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class AbBillAttachmentVO extends AbAccrecordsVO implements java.io.Serializable {
	
	/**true 是否支持上传(一般用于只是查看附件)**/
	private String hasUpload;
	
	/**true 是否支持删除**/
	private String hasDelete;
	
	/**false 是否展开附件列表**/
	private String isExpand;
	public String getHasUpload() {
		return hasUpload;
	}
	public void setHasUpload(String hasUpload) {
		this.hasUpload = hasUpload;
	}
	public String getHasDelete() {
		return hasDelete;
	}
	public void setHasDelete(String hasDelete) {
		this.hasDelete = hasDelete;
	}
	public String getIsExpand() {
		return isExpand;
	}
	public void setIsExpand(String isExpand) {
		this.isExpand = isExpand;
	}
	
	
	
}
