package com.bytter.tally.vo;
import com.bytter.biz.finacial.api.dto.FcManualFinacialColumnDTO;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.util.ArrayList;
import java.util.List;
public class FcManualFinacialTypeVO implements java.io.Serializable{
	private String id;
	/**系统业务种类Code(四位数)*/
	@BtCompositeSearch(fieldName = "a.sys_Race_Code")
	private String sysRaceCode;
	/**手工账名称*/
	@BtCompositeSearch(fieldName = "a.name")
	private String name;
	/**需要记手工帐数据的查询链接*/
	private String url;
	/**流程类型1记账、2改账、3复核*/
	@Dict(dictGroup="manual.finacial.flow.type", inject="flowTypeName")
	@BtCompositeSearch(fieldName = "a.flow_Type")
	private String flowType;
	private String flowTypeName;
	private List<FcManualFinacialColumnDTO> colList = new ArrayList<FcManualFinacialColumnDTO>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public String getFlowType() {
		return flowType;
	}
	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
	public String getFlowTypeName() {
		return flowTypeName;
	}
	public void setFlowTypeName(String flowTypeName) {
		this.flowTypeName = flowTypeName;
	}
	public List<FcManualFinacialColumnDTO> getColList() {
		return colList;
	}
	public void setColList(List<FcManualFinacialColumnDTO> colList) {
		this.colList = colList;
	}
}
