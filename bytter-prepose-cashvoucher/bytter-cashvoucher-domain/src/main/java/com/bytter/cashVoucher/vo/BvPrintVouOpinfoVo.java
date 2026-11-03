package com.bytter.cashVoucher.vo;
import java.util.List;
/**   
 * @Title: vo
 * @Description: 凭证打印操作vo
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BvPrintVouOpinfoVo implements java.io.Serializable {
	
	/**凭证打印格式id*/
	private String printId;
	/**凭证类型ID*/
	private String vouTid;
	/**数据ID*/
	private String dataIds;
	/**
	 * 是否入库标志（0不占用凭证、1占用凭证）
	 */
	private String isInbox;
	/**
	 * 打印凭证号
	 */
	private String printNo;
	/**
	 * 打印场景ID
	 */
	private String sceneId;
	private List<BvPrintFormatVo> printData;
	public String getPrintId() {
		return printId;
	}
	public void setPrintId(String printId) {
		this.printId = printId;
	}
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	public String getDataIds() {
		return dataIds;
	}
	public void setDataIds(String dataIds) {
		this.dataIds = dataIds;
	}
	public String getIsInbox() {
		return isInbox;
	}
	public void setIsInbox(String isInbox) {
		this.isInbox = isInbox;
	}
	public String getPrintNo() {
		return printNo;
	}
	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}
	public List<BvPrintFormatVo> getPrintData() {
		return printData;
	}
	public void setPrintData(List<BvPrintFormatVo> printData) {
		this.printData = printData;
	}
	public String getSceneId() {
		return sceneId;
	}
	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
	@Override
	public String toString() {
		return "BvPrintVouOpinfoVo [printId=" + printId + ", vouTid=" + vouTid + ", dataIds=" + dataIds + ", isInbox="
				+ isInbox + ", printNo=" + printNo + ", sceneId=" + sceneId + ", printData=" + printData + "]";
	}
}
