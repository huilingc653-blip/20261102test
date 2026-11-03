package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefineDTO;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefinePoolDTO;
import java.util.Map;
/**
 * 柜台账务对外接口 
 * @author lixi
 * @since 2017/8/16
 */
public interface IFcBusinessRuleDefineService {
	/**
	 * 查询记账规则
	 * @param map 网点id 科目code 币别 
	 * @return FcBusinessRuleDefineDTO 记账规则
	 */
	FcBusinessRuleDefineDTO getBusinessRuleExp(Map<String, Object> map);
	/**
	 * 查询资金池记账规则
	 * @param poolId 资金池Id
	 * @param curId 币别id
	 * @param sysRaceCode 系统业务种类
	 * @return FcBusinessRuleDefineDTO 记账规则
	 */
    FcBusinessRuleDefinePoolDTO getBusinessRulePoolExp(String poolId, String curId, String sysRaceCode);
}
