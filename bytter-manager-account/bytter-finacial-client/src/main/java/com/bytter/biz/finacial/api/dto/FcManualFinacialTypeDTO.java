package com.bytter.biz.finacial.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
@Data
public class FcManualFinacialTypeDTO implements java.io.Serializable{
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
}
