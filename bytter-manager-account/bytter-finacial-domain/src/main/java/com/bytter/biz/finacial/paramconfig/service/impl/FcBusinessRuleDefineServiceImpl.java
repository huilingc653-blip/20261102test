package com.bytter.biz.finacial.paramconfig.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefineDTO;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefinePoolDTO;
import com.bytter.biz.finacial.api.dto.FcVoucherEntryDefineDTO;
import com.bytter.biz.finacial.api.dto.FcVoucherEntryDefinePoolDTO;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.paramconfig.service.FcBusinessRuleDefinePoolService;
import com.bytter.biz.finacial.paramconfig.service.FcBusinessRuleDefineService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusinessRuleDefinePoolVo;
import com.bytter.biz.finacial.paramconfig.vo.FcBusinessRuleDefineVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
/**
* @Title: Service
* @Description: FC_BUSINESS_RULE_DEFINE
* @author onlineGenerator
* @date 2017-05-22 19:23:12
* @version 1.0
*
*/
@Service("fcBusinessRuleDefineServiceImpl")
@DubboService
public class FcBusinessRuleDefineServiceImpl implements IFcBusinessRuleDefineService {
	@Autowired
	private FcBusinessRuleDefineService fcBusinessRuleDefineService;
	@Autowired
	private FcBusinessRuleDefinePoolService fcBusinessRuleDefinePoolService;
	
	
	
 	/**
     * <b>功能：</b>对外查询业务规则<br>
     * <b>作者：</b>lixi<br>
     * <b>日期：</b>2017-8-16<br>
     */
 	@Override
	public FcBusinessRuleDefineDTO getBusinessRuleExp(Map<String, Object> map){
 		List<FcBusinessRuleDefineVo> list = fcBusinessRuleDefineService.getBusinessRuleExp(map);//查询记账规则
 		if(EmptyUtils.isEmptyList(list)){
 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_NOTDEFINE);
 		}
 		List<FcBusinessRuleDefineVo> temp = new ArrayList<>();
 		String netId= (String) map.get("netId");
 		String subjCode= (String) map.get("subjCode");
 		String curCode= (String) map.get("curCode");
 		for (FcBusinessRuleDefineVo fr : list) {
			if (EmptyUtils.isNotEmpty(netId)) {
				if(!netId.equals(fr.getNetId())){
					continue;
				}
			}
			if (EmptyUtils.isNotEmpty(subjCode)) {
				if(!subjCode.equals(fr.getSubjCode())){
					continue;
				}
			}
			if (EmptyUtils.isNotEmpty(curCode)) {
				if(!curCode.equals(fr.getCurCode())){
					continue;
				}
			}
 	        temp.add(fr);
		}
 		FcBusinessRuleDefineVo vo = EmptyUtils.isEmptyList(temp) ? list.get(0) : temp.get(0);
 		FcBusinessRuleDefineDTO dto = new FcBusinessRuleDefineDTO();
 		BeanUtils.copyNotNullProperties(vo, dto);
 		List<FcVoucherEntryDefineVo> define = fcBusinessRuleDefineService.getVoucher(vo.getId());
 		if(EmptyUtils.isEmptyList(define)){
 			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
 		}
 		List<FcVoucherEntryDefineDTO> tempEntry = BeanUtils.copyListProperties(define, FcVoucherEntryDefineDTO.class);
 		dto.setVouchers(tempEntry);
 		return dto;
 	}
 	
	/**
	 *资金池记账规则
	 */
	@Override
	public FcBusinessRuleDefinePoolDTO getBusinessRulePoolExp(String poolId, String curId, String sysRaceCode) {
		List<FcBusinessRuleDefinePoolVo> list = fcBusinessRuleDefinePoolService.getBusinessRuleExp(poolId, curId, sysRaceCode);//查询记账规则
		if(EmptyUtils.isEmptyList(list)){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_NOTDEFINE);
		}
		FcBusinessRuleDefinePoolVo vo = list.get(0);
		FcBusinessRuleDefinePoolDTO dto = new FcBusinessRuleDefinePoolDTO();
		BeanUtils.copyNotNullProperties(vo, dto);
		List<FcVoucherEntryDefineVo> define = fcBusinessRuleDefinePoolService.getVoucher(vo.getId());
		if(EmptyUtils.isEmptyList(define)){
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
		}
		List<FcVoucherEntryDefinePoolDTO> tempEntry = BeanUtils.copyListProperties(define, FcVoucherEntryDefinePoolDTO.class);
		dto.setVouchers(tempEntry);
		return dto;
	}
 	
}
