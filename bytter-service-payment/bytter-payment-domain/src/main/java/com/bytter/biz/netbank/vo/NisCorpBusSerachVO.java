package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
/**
 * @author wangzx
 * @Description 业务类别单位设置
 * @date 2017.12.14
 */
public class NisCorpBusSerachVO implements Serializable{
	private String id;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "n.corp_id")
	private String corpId;
	/**业务类别名称*/
	@BtCompositeSearch(fieldName = "t.bus_type_name")
	private String busTypeName;
	
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
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}
}
