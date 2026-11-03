package com.bytter.biz.finacial.paramconfig.service;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.BtSummaryDTO;
import com.bytter.biz.finacial.api.service.IFcFinacialParamConfigService;
import com.bytter.biz.finacial.paramconfig.dao.FcChargeRuleDao;
import com.bytter.biz.finacial.paramconfig.entity.ChargeTypeChargeRuleRel;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeRule;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.vo.FcChargeRuleVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("fcChargeRuleService")
@Transactional
public class FcChargeRuleService extends BaseService<FcChargeRule,String> {
	@Autowired
	private FcChargeRuleDao fcChargeRuleDao;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IFcFinacialParamConfigService fcpParamService;
	@DubboReference(check = false)
	private IFcVoucherTypeService iFcVoucherTypeService;
	@Resource
	private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IFcSubjectService iFcSubjectService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;

	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcChargeRule entity){
		super.deleteEntity(entity);
		deleteChargeTypeChargeRuleRel(entity.getId());
		deleteFcVoucherEntryDefine(entity.getId());
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcChargeRule entity,List<String> chargeTypeIds, String userId){
		super.saveEntity(entity);
		checkSummary(entity.getSummary(), userId);
		saveChargeTypeChargeRuleRel(entity.getId(),chargeTypeIds);//保存关联费用类型
		saveFcVoucherEntryDefine(entity.getId(),entity.getFcVoucherEntryDefines());//保存分录
// 		List<FcVoucherEntryDefine> defines = entity.getFcVoucherEntryDefines();
// 		for (FcVoucherEntryDefine define : defines) {
// 			checkSummary(define.getSummary(), userId);
//		}
	}
	/**
	 * <b>功能介绍：</b>验证摘要<br>
	 * <b>日期：</b>2017年9月21日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param summaryContent
	 */
	public void checkSummary(String summaryContent, String userId){
		if(EmptyUtils.isNotEmpty(summaryContent)&&EmptyUtils.isEmptyList(fcpParamService.getBtSummaryDTOByContent(summaryContent))){
			BtSummaryDTO summary = new BtSummaryDTO();
			summary.setSummaryContent(summaryContent);
			summary.setValidSign(FinacialConstant.VALID_TRUE);
			String handLing = multiLangServiceI.getLang("table.Management.of.handling.charge");//手续费计提管理
			String management = multiLangServiceI.getLang("table.Automatic.generation.of.commission.management");//手续费计提管理自动生成
			summary.setUsingtype(handLing);
			summary.setRmk(management);
			fcpParamService.addBtSummary(summary, userId);
		}
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcChargeRule entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcChargeRule entity,List<String> chargeTypeIds, String userId){
		super.saveOrUpdate(entity);
		checkSummary(entity.getSummary(), userId);
		deleteChargeTypeChargeRuleRel(entity.getId());
		deleteFcVoucherEntryDefine(entity.getId());
		saveChargeTypeChargeRuleRel(entity.getId(),chargeTypeIds);//保存关联费用类型
		saveFcVoucherEntryDefine(entity.getId(),entity.getFcVoucherEntryDefines());//保存分录
// 		List<FcVoucherEntryDefine> defines = entity.getFcVoucherEntryDefines();
// 		for (FcVoucherEntryDefine define : defines) {
// 			checkSummary(define.getSummary(), userId);
//		}
	}
	public BaseDao<FcChargeRule, String> getEntityDao() {
		return fcChargeRuleDao;
	}
	/**
	 * <b>功能介绍：</b>列表数据<br>
	 * <b>日期：</b>2017年9月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listFcChargeRule(DataTablesInput input){
		DataTablesOutput dataTablesOutput = fcChargeRuleDao.listFcChargeRule(input);
		List data = dataTablesOutput.getData();
		ArrayList<FcChargeRuleVo> fcChargeRuleVoArrayList = new ArrayList<FcChargeRuleVo>();
		ArrayList<String> debitSubjectIds = new ArrayList<>();
		ArrayList<String> creditSubjectIds = new ArrayList<>();
		ArrayList<String> curIds = new ArrayList<>();
		ArrayList<String> corpIds = new ArrayList<>();
		ArrayList<String> voucherIds = new ArrayList<>();
		for (Object datum : data) {
			FcChargeRuleVo vo = (FcChargeRuleVo)datum;
			voucherIds.add(vo.getVoucherId());
			debitSubjectIds.add(vo.getFsSubjId());
			creditSubjectIds.add(vo.getFssSubjId());
			curIds.add(vo.getCurCode());
			corpIds.add(vo.getNetId());
			fcChargeRuleVoArrayList.add(vo);
		}
		List<FcSubjectDTO> debitSubjectDTOS = iFcSubjectService.getSubjectByIds(debitSubjectIds);
		Map<String, FcSubjectDTO> subjectDTOMap = debitSubjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity()));
		List<FcSubjectDTO> creditSubjectDTOS = iFcSubjectService.getSubjectByIds(creditSubjectIds);
		Map<String, FcSubjectDTO> subjectDTOMap2 = creditSubjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity()));
		Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrencyByIds(curIds);
		Map<String, String> corpMap = iCorpService.getNameByIds(corpIds);
		List<FcVoucherTypeDTO> fcVoucherTypeDTOS = iFcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
		Map<String, String> voucherMap = fcVoucherTypeDTOS.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));

		fcChargeRuleVoArrayList.forEach( a -> {
			FcSubjectDTO fcSubjectDTO01 = subjectDTOMap.get(a.getFsSubjId());
			FcSubjectDTO fcSubjectDTO02 = subjectDTOMap2.get(a.getFssSubjId());
			if (StringUtils.isNotBlank(a.getCurCode())){
				BtCurrencyDTO btCurrencyDTO = curMap.get(a.getCurCode());
				a.setCurName(btCurrencyDTO.getCurName());
				a.setCurId(btCurrencyDTO.getId());
			}
			a.setDebitSubjCode(fcSubjectDTO01.getSubjectName());
			a.setFsSubjId(fcSubjectDTO01.getId());
			a.setCreditSubjCode(fcSubjectDTO02.getSubjectName());
			a.setFssSubjId(fcSubjectDTO02.getId());

			a.setNetId(corpMap.get(a.getNetId()));
			a.setVoucherType(voucherMap.get(a.getVoucherId()));
		});
		dataTablesOutput.setData(fcChargeRuleVoArrayList);
		return dataTablesOutput;
	}
	/**
	 * <b>功能介绍：</b>费用计提凭证分录list<br>
	 * <b>日期：</b>2017年9月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param id
	 * @return
	 */
	public List<FcVoucherEntryDefineVo> listFcVoucherEntryDefine(String id){
		List<FcVoucherEntryDefineVo> fcVoucherEntryDefineVos = fcChargeRuleDao.listFcVoucherEntryDefine(id);
		List<String> subjectIds = fcVoucherEntryDefineVos.stream().map(FcVoucherEntryDefineVo::getSubjectId).collect(Collectors.toList());
		List<String> voucherIds = fcVoucherEntryDefineVos.stream().map(FcVoucherEntryDefineVo::getVoucherId).collect(Collectors.toList());
		List<FcVoucherTypeDTO> fcVoucherTypeDTOS = iFcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
		Map<String, String> voucherMap = fcVoucherTypeDTOS.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));

		List<FcSubjectDTO> subjectDTOS = iFcSubjectService.getSubjectByIds(subjectIds);
		if (CollectionUtils.isNotEmpty(subjectDTOS)){
			Map<String, FcSubjectDTO> subjectDTOMap = subjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity()));
			fcVoucherEntryDefineVos.forEach( a -> {
				FcSubjectDTO fcSubjectDTO = subjectDTOMap.get(a.getSubjectId());
				a.setSubjectName(fcSubjectDTO.getSubjectName());
				a.setVoucherName(voucherMap.get(a.getVoucherId()));
			});
		}

		return fcVoucherEntryDefineVos;
	}
	/**
	 * <b>功能介绍：</b>费用类型与费用计提list<br>
	 * <b>日期：</b>2017年9月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param id
	 * @return
	 */
	public List<ChargeTypeChargeRuleRel> listChargeTypeChargeRules(String id){
		return fcChargeRuleDao.listChargeTypeChargeRules(id);
	}
	/**
	 * 功能介绍:保存费用类型与费用计提关联数据<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void saveChargeTypeChargeRuleRel(String chargeRuleId,List<String> chargeTypeIds){
		for (String chargeTypeId : chargeTypeIds) {
			ChargeTypeChargeRuleRel chargeTypeChargeRuleRel=new ChargeTypeChargeRuleRel();
			chargeTypeChargeRuleRel.setChargeRuleId(chargeRuleId);
			chargeTypeChargeRuleRel.setChargeTypeId(chargeTypeId);
			fcChargeRuleDao.saveChargeTypeChargeRuleRel(chargeTypeChargeRuleRel);
		}
	}
	/**
	 * 功能介绍:保存费用计提分录<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void saveFcVoucherEntryDefine(String chargeRuleId,List<FcVoucherEntryDefine> fcVoucherEntryDefines){
		for (FcVoucherEntryDefine fcVoucherEntryDefine : fcVoucherEntryDefines) {
			fcVoucherEntryDefine.setBusinessRuleDefineId(chargeRuleId);
			fcVoucherEntryDefine.setBelongType(10);
			fcChargeRuleDao.saveFcVoucherEntryDefine(fcVoucherEntryDefine);
		}
	}
	/**
	 * 功能介绍:删除费用类型与费用计提关联数据<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void deleteChargeTypeChargeRuleRel(String chargeRuleId){
		fcChargeRuleDao.deleteChargeTypeChargeRuleRel(chargeRuleId);
	}
	/**
	 * 功能介绍:删除费用计提分录<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void deleteFcVoucherEntryDefine(String chargeRuleId){
		fcChargeRuleDao.deleteFcVoucherEntryDefine(chargeRuleId);
	}
	/**
	 *
	 * 功能介绍 : 根据业务规则获取手续费规则。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcChargeRule> queryByBusRaceAndVoucherType(Set<String> raceCodes, Set<String> voucherTypeCodes,
														   String netCode) {
		return fcChargeRuleDao.queryByBusRaceAndVoucherType(raceCodes, voucherTypeCodes, netCode);
	}
	/**
	 *
	 * 功能介绍 : 根据id则获取费用计提详情。<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public FcChargeRuleVo getFcChargeRuleById(String id){
		FcChargeRuleVo vo = fcChargeRuleDao.getFcChargeRuleById(id);
		String fsSubjId = vo.getFsSubjId();
		String fssSubjId = vo.getFssSubjId();
		String netId = vo.getNetId();
		String curId = vo.getCurId();
		String subjectName = iFcSubjectService.getSubjectNameById(fsSubjId);
		String subjectName2 = iFcSubjectService.getSubjectNameById(fssSubjId);
		String corpName = iCorpService.getCorpName(netId);
		BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(curId);
		FcVoucherTypeDTO fcVoucherTypeDTO = iFcVoucherTypeService.getFcVoucherTypeById(vo.getVoucherId());
		vo.setVoucherType(fcVoucherTypeDTO.getVoucherName()); //历史遗留
		vo.setDebitSubjCode(subjectName);
		vo.setCreditSubjCode(subjectName2);
		vo.setNetId(corpName);
		if(ObjectUtils.isNotEmpty(btCurrencyDTO)) vo.setCurName(btCurrencyDTO.getCurName());
		return vo;
	}
	/**
	 * <b>功能介绍：</b>手续费账号<br>
	 * <b>日期：</b>2017年9月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param subCode
	 * @param netId
	 * @return
	 */
	public List<FcAccDTO> getChargeAcc(String subCode, String netId){
		return iFcAccService.getFcAccByCorpSubjectCode(netId, null, subCode);
	}
	/**
	 * <b>功能介绍：</b>初始化网点<br>
	 * <b>日期：</b>2017年9月19日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	public String getInitNetId(){
		return iCorpService.listNet().get(0).getId();
	}
	/**
	 * <b>功能：</b>查询所有有效、记账的付款方式凭证种类<br>
	 * <b>日期：</b> 2017-10-25 <br>
	 */
	public  List<FcVoucherTypeDTO> getFcVoucherType(){
		return iFcVoucherTypeService.getValidFcVoucherType();
	}
}
