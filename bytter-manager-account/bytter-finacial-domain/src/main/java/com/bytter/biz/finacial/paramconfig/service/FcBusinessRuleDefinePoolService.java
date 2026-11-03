package com.bytter.biz.finacial.paramconfig.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.api.service.IFcFinacialParamConfigService;
import com.bytter.biz.finacial.paramconfig.dao.FcBusinessRuleDefinePoolDao;
import com.bytter.biz.finacial.paramconfig.entity.FcBusinessRuleDefinePool;
import com.bytter.biz.finacial.paramconfig.entity.FcSysBusRacePool;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefinePool;
import com.bytter.biz.finacial.paramconfig.vo.*;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.IFcSubjectPoolService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.dto.FcSubjectPoolDTO;
import com.bytter.system.api.vo.FcCorpPoolVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: FC_BUSINESS_RULE_DEFINE_POOL
* @author onlineGenerator
* @date 2017-05-22 19:23:12 
* @version 1.0
*
*/
@Service("fcBusinessRuleDefinePoolService")
@Transactional
public class FcBusinessRuleDefinePoolService extends BaseService<FcBusinessRuleDefinePool,String>{
	@Autowired
	private FcBusinessRuleDefinePoolDao fcBusinessRuleDefinePoolDao;
	@Autowired
	private FcVoucherEntryDefinePoolService voucherEntryPoolService;
	@DubboReference(check = false)
	private IFcFinacialParamConfigService paramConfigService;
	@DubboReference(check = false)
	private IFcSubjectPoolService iFcSubjectPoolService;
	@DubboReference(check = false)
	private IFcCorpPoolService iFcCorpPoolService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IFcSubjectService iFcSubjectService;

	@DubboReference(check = false)
	private IFcVoucherTypeService iFcVoucherTypeService;
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcBusinessRuleDefinePool entity){
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id){
		super.delete(id);
	}
	/**
	 * 功能介绍 : 资金池业务记账规则列表数据查询。<br>-----
	 */
	public DataTablesOutput getBusninessRuleVoList(DataTablesInput input) {
		DataTablesOutput dataTablesOutput = fcBusinessRuleDefinePoolDao.getBusninessRuleVoList(input);
		List data = dataTablesOutput.getData();
		ArrayList<String> subjectIds = new ArrayList<String>();
		ArrayList<String> corpIds = new ArrayList<String>();
		ArrayList<String> curIds = new ArrayList<String>();
		ArrayList<FcBusinessRuleDefinePoolListVo> listVos = new ArrayList<>();
		for (Object datum : data) {
			FcBusinessRuleDefinePoolListVo vo = (FcBusinessRuleDefinePoolListVo)datum;
			if(StringUtils.isNotBlank(vo.getSubjId())) subjectIds.add(vo.getSubjId());
			if(StringUtils.isNotBlank(vo.getPoolId()))corpIds.add(vo.getPoolId());
			if(StringUtils.isNotBlank(vo.getCurId()))curIds.add(vo.getCurId());
			listVos.add(vo);
		}
		if (CollectionUtils.isNotEmpty(subjectIds)){
			List<FcSubjectPoolDTO> fcSubjectPoolDTOS = iFcSubjectPoolService.getFcSubjectPoolByIds(subjectIds);
			Map<String, FcSubjectPoolDTO> subjectMap = fcSubjectPoolDTOS.stream().collect(Collectors.toMap(FcSubjectPoolDTO::getId, Function.identity(),(a,b)->a));
			List<FcCorpPoolDTO> fcCorpPoolDTOS = iFcCorpPoolService.getFcCorpPoolByIds(corpIds);
			Map<String, FcCorpPoolDTO> corpPoolDTOMap = fcCorpPoolDTOS.stream().collect(Collectors.toMap(FcCorpPoolDTO::getId, Function.identity()));
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrencyByIds(curIds);
			listVos.forEach( a -> {
				FcSubjectPoolDTO fcSubjectPoolDTO = subjectMap.get(a.getSubjId());
				FcCorpPoolDTO fcCorpPoolDTO = corpPoolDTOMap.get(a.getPoolId());
				BtCurrencyDTO btCurrencyDTO = curMap.get(a.getCurId());
				if (fcSubjectPoolDTO != null ){
					a.setSubjCode(fcSubjectPoolDTO.getSubjectCode());
					a.setSubName(fcSubjectPoolDTO.getSubjectName());
				}
				if (fcCorpPoolDTO != null) a.setNetName(fcCorpPoolDTO.getPoolName());
				if (btCurrencyDTO != null){
					a.setCurCode(btCurrencyDTO.getCurCode());
					a.setCurName(btCurrencyDTO.getCurName());
				}

			});
			dataTablesOutput.setData(listVos);
		}
		return dataTablesOutput;
	}
	/**
	 * 功能介绍 : 初始化业务记账规则列表数据查询。<br>
	 */
	public DataTablesOutput getInitBusninessRuleVoList(DataTablesInput input) {
		DataTablesOutput dataTablesOutput = fcBusinessRuleDefinePoolDao.getInitBusninessRuleVoList(input);
		List data = dataTablesOutput.getData();
		ArrayList<FcBusinessRuleDefinePoolListVo> listVos = new ArrayList<>();
		ArrayList<String> subjIds = new ArrayList<>();
		for (Object datum : data) {
			FcBusinessRuleDefinePoolListVo vo = (FcBusinessRuleDefinePoolListVo)datum;
			subjIds.add(vo.getSubjId());
			listVos.add(vo);
		}
		List<FcSubjectDTO> fcSubjectDTOS = iFcSubjectService.getSubjectByIds(subjIds);
		Map<String, FcSubjectDTO> subjectMap = fcSubjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity()));
		listVos.forEach( a -> {
			FcSubjectDTO fcSubjectDTO = subjectMap.get(a.getSubjId());
			a.setSubName(fcSubjectDTO.getSubjectName());
			a.setSubjCode(fcSubjectDTO.getSubjectCode());
		});
		dataTablesOutput.setData(listVos);
		return dataTablesOutput;
	}
	/**
	 * 功能介绍 : 根据记账规则ID获取记账规则信息和分录明细。<br>
	 */
	public FcBusinessRuleDefinePoolVo getBusinessRuleDefinePoolVoById(String id) {
		FcBusinessRuleDefinePoolVo fcBusinessRuleDefinePool = fcBusinessRuleDefinePoolDao.queryById(id);
		if (null == fcBusinessRuleDefinePool) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
		}
		List<FcVoucherEntryDefineVo> entryDefineVos=voucherEntryPoolService.getVoucherEntryDefinesPoolByRuleId(id);
		for (FcVoucherEntryDefineVo fcVoucherEntryDefineVo : entryDefineVos) {
			if(fcVoucherEntryDefineVo.getSubjectName() == null){
				fcVoucherEntryDefineVo.setSubjectCode(null);
			}
		}
		fcBusinessRuleDefinePool.setVouchers(entryDefineVos);
		return fcBusinessRuleDefinePool;
	}
	/**
	 * 功能介绍 : 是否存在相同的记账规则。<br>
	 * <b>日期：</b>2017年11月23日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void checkSameRuleDefine(FcBusinessRuleDefinePoolVo ruleDefineVo) {
		List<FcBusinessRuleDefinePoolVo> ruleDefineVos = fcBusinessRuleDefinePoolDao.existBusinessRule(ruleDefineVo);
		if (!EmptyUtils.isEmptyList(ruleDefineVos)) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_SAME);
		}
	}
	/**
	 * 功能介绍：保存记账规则定义和分录数据<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void saveBusinessRule(FcBusinessRuleDefinePoolVo fcBusinessRuleDefinePoolVo, ICurrentUser user) {
		List<FcVoucherEntryDefineVo> vouchers = fcBusinessRuleDefinePoolVo.getVouchers();
		//分录不能为空
		for (FcVoucherEntryDefineVo voucher :vouchers) {
			if (EmptyUtils.isEmpty(voucher.getGroupNum()) || EmptyUtils.isEmpty(voucher.getSubjectCode())) {
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
			}
		}
		FcBusinessRuleDefinePool fcBusinessRuleDefine = new FcBusinessRuleDefinePool();
		//将网点，科目，币别等的空格过滤掉
		fcBusinessRuleDefinePoolVo.setCurId(StringUtils.formatNull(fcBusinessRuleDefinePoolVo.getCurId()).trim());
		fcBusinessRuleDefinePoolVo.setPoolId(StringUtils.formatNull(fcBusinessRuleDefinePoolVo.getPoolId()).trim());
		fcBusinessRuleDefinePoolVo.setSubjectId(StringUtils.formatNull(fcBusinessRuleDefinePoolVo.getSubjectId()).trim());
		fcBusinessRuleDefinePoolVo.setSysRaceCode(StringUtils.formatNull(fcBusinessRuleDefinePoolVo.getSysRaceCode()).trim());
		//拷贝数据对象
		BeanUtils.copyProperties(fcBusinessRuleDefinePoolVo, fcBusinessRuleDefine);
		//是否已存在相同的记账规则
		checkSameRuleDefine(fcBusinessRuleDefinePoolVo);
		fcBusinessRuleDefine.setCreateBy(user.getId());
		fcBusinessRuleDefine.setCreateTime(DateUtils.getDate());
		super.saveEntity(fcBusinessRuleDefine);
		Set<String> summaryContent = new HashSet<String>();
		//校验分录是否包含借贷标识
		boolean debitFlag = true, creditFlag = true;
		for (FcVoucherEntryDefineVo entryDefine : vouchers) {
			entryDefine.setBusinessRuleDefineId(fcBusinessRuleDefine.getId());
			if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_DEBIT)) {
				debitFlag = false;
			}
			if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_CREDIT)) {
				creditFlag = false;
			}
			entryDefine.setBelongType(20);
			if(!EmptyUtils.isEmpty(entryDefine.getSummary())){
				summaryContent.add(entryDefine.getSummary());
			}
		}
		//提示分录不包含借贷，必须借贷方都要定义
		if (creditFlag || debitFlag) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_BALANCE);
		}
		//批量保存分录
		List<FcVoucherEntryDefinePool> entryDefines = BeanUtils.copyListProperties(vouchers, FcVoucherEntryDefinePool.class);
		voucherEntryPoolService.batchSave(entryDefines);
	}
	/**
	 * 功能介绍：更新记账规则定义和分录数据<br>
	 */
	public void updateBusinessRule(FcBusinessRuleDefinePoolVo ruleDefinePoolVo, ICurrentUser user) {
		List<FcVoucherEntryDefinePool> vouchers = BeanUtils.copyListProperties(ruleDefinePoolVo.getVouchers(), FcVoucherEntryDefinePool.class);
		//分录不能为空
		for (FcVoucherEntryDefinePool voucher :vouchers) {
			if (EmptyUtils.isEmpty(voucher.getGroupNum()) || EmptyUtils.isEmpty(voucher.getSubjectCode())) {
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
			}
		}
		FcBusinessRuleDefinePool fcBusinessRuleDefine = this.get(ruleDefinePoolVo.getId());
		//将网点，科目，币别等的空格过滤掉
		verify(ruleDefinePoolVo);
		ruleDefinePoolVo.setCurId(ruleDefinePoolVo.getCurId().trim());
		ruleDefinePoolVo.setPoolId(ruleDefinePoolVo.getPoolId().trim());
		if(ruleDefinePoolVo.getSubjectId() == null){
			ruleDefinePoolVo.setSubjectId(ruleDefinePoolVo.getSubjectId());
		}else{
			ruleDefinePoolVo.setSubjectId(ruleDefinePoolVo.getSubjectId().trim());
		}
		ruleDefinePoolVo.setSysRaceCode(ruleDefinePoolVo.getSysRaceCode().trim());
		if (!EmptyUtils.isEmpty(fcBusinessRuleDefine)) {
			ruleDefinePoolVo.setId(fcBusinessRuleDefine.getId());
		}
		checkSameRuleDefine(ruleDefinePoolVo);
		//拷贝数据对象
		BeanUtils.copyNotNullProperties(ruleDefinePoolVo, fcBusinessRuleDefine);
		fcBusinessRuleDefine.setUpdateBy(user.getId());
		fcBusinessRuleDefine.setUpdateTime(DateUtils.getDate());
		super.updateEntity(fcBusinessRuleDefine);
		Set<String> summaryContent = new HashSet<String>();
		//校验分录是否包含借贷标识
		boolean debitFlag = true, creditFlag = true;
		for (FcVoucherEntryDefinePool entryDefine : vouchers) {
			entryDefine.setBusinessRuleDefineId(ruleDefinePoolVo.getId());
			if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_DEBIT)) {
				debitFlag = false;
			}
			if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_CREDIT)) {
				creditFlag = false;
			}
			entryDefine.setBelongType(20);
			if(!EmptyUtils.isEmpty(entryDefine.getSummary())){
				summaryContent.add(entryDefine.getSummary());
			}
		}
		//提示分录不包含借贷，必须借贷方都要定义
		if (creditFlag || debitFlag) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_BALANCE);
		}
		//先删除分录明细，再批量保存分录
		voucherEntryPoolService.deleteEntryDefinesByRuleId(ruleDefinePoolVo.getId());
		voucherEntryPoolService.batchSave(vouchers);
	}
	/**
     * <b>功能：</b>对外查询业务规则<br>
     */
	public List<FcBusinessRuleDefinePoolVo> getBusinessRuleExp(String poolId, String curId, String sysRaceCode){
		return fcBusinessRuleDefinePoolDao.getBusinessRuleExp(poolId, curId, sysRaceCode);
	}
	/***
     * 查询凭证分录定义
     */
    public List<FcVoucherEntryDefineVo> getVoucher(String defineId) {
		return fcBusinessRuleDefinePoolDao.getVoucher(defineId);
    }
	/**
	 * 功能介绍 : 根据记账规则ID删除分录和规则数据<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void deleteBusinessRule(String ruleId) {
		voucherEntryPoolService.deleteEntryDefinesByRuleId(ruleId);
		this.delete(ruleId);
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcBusinessRuleDefinePool entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcBusinessRuleDefinePool entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcBusinessRuleDefinePool entity){
		super.saveOrUpdate(entity);
	}
	public BaseDao<FcBusinessRuleDefinePool, String> getEntityDao() {
		return fcBusinessRuleDefinePoolDao;
	}
	/**
     * 获取有效的业务种类------
     * @return
     */
    public List<FcSysBusRacePool> getFcSysBusRacePool() {
        return fcBusinessRuleDefinePoolDao.getFcSysBusRacePool();
    }
    
    /**
	 * 功能介绍 : 根据记账规则ID查看记账规则信息和分录明细。<br>
	 * <b>日期：</b>2017年9月23日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public FcBusinessRuleDefinePoolVo getBusRuleDefinePoolVoById(String id) {
		FcBusinessRuleDefinePoolVo fcBusinessRuleDefine = this.fcBusinessRuleDefinePoolDao.getBusninessRulePoolVo(id);
		if (null == fcBusinessRuleDefine) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
		}
		FcCorpPoolVO fcCorpPoolVO = iFcCorpPoolService.getFcCorpPoolById(fcBusinessRuleDefine.getPoolId());
		if (fcCorpPoolVO != null) fcBusinessRuleDefine.setPoolId(fcCorpPoolVO.getPoolName());
		List<String> subjectIds = new ArrayList<>();
		subjectIds.add(fcBusinessRuleDefine.getSubjectId());
		List<FcSubjectPoolDTO> fcSubjectPoolDTOS = iFcSubjectPoolService.getFcSubjectPoolByIds(subjectIds);
		Map<String, FcSubjectPoolDTO> subjectMap = fcSubjectPoolDTOS.stream().collect(Collectors.toMap(FcSubjectPoolDTO::getId, Function.identity(), (a, b) -> a));
		FcSubjectPoolDTO fcSubjectPoolDTO = subjectMap.get(fcBusinessRuleDefine.getSubjectId());
		if (fcSubjectPoolDTO != null) {
			fcBusinessRuleDefine.setSubjectId(fcSubjectPoolDTO.getSubjectName());
		}

		List<FcVoucherEntryDefineVo> entryDefineVos=voucherEntryPoolService.getVoucherEntryDefinesPoolByRuleId(id);
		fcBusinessRuleDefine.setVouchers(entryDefineVos);
		return fcBusinessRuleDefine;
	}
	/**
	 * 功能介绍 : 根据记账规则ID查看记账规则信息和分录明细。<br>
	 * <b>日期：</b>2017年9月23日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public FcBusinessRuleDefineVo getInitBusRuleDefineVoById(String id) {
		FcBusinessRuleDefineVo fcBusinessRuleDefine = this.fcBusinessRuleDefinePoolDao.getInitBusninessRuleVo(id);
		if (null == fcBusinessRuleDefine) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
		}
		List<FcVoucherEntryDefineVo> entryDefineVos=voucherEntryPoolService.getInitVoucherEntryDefinesByRuleId(id);
		fcBusinessRuleDefine.setVouchers(entryDefineVos);
		String subjectId = fcBusinessRuleDefine.getSubjectId();
		String curId = fcBusinessRuleDefine.getCurId();
		String netId = fcBusinessRuleDefine.getNetId();
		fcBusinessRuleDefine.setSubjCode(iFcSubjectService.getSubjectNameById(subjectId));
		fcBusinessRuleDefine.setNetId(iFcCorpPoolService.getById(netId).getPoolName());
		fcBusinessRuleDefine.setCurCode(iBtCurrencyService.getObjectById(curId).getCurName());
		FcVoucherTypeDTO dto = iFcVoucherTypeService.getFcVoucherTypeById(fcBusinessRuleDefine.getVoucherId());
		fcBusinessRuleDefine.setVoucherType(dto.getVoucherName());
		return fcBusinessRuleDefine;
	}
	public void saveInitBusinessRuleDefine(String idKeys) {
	}
	/**
	 * 功能介绍 : 初始化所有的记账规则。<br>-----
	 */
	public void initBusinessRuleDefine() {
		fcBusinessRuleDefinePoolDao.importInitBusinessRule();
		//删除已存在的初始化的明细
		voucherEntryPoolService.deleteInitEntryRule();
		voucherEntryPoolService.importInitEntryRule();
	}
	/**
     * 获取有效的科目
     *
     * @return
     */
    public List<FcSubjectPoolVO> listValidSubject() {
//        return fcBusinessRuleDefinePoolDao.listValidSubject();
		List<FcSubjectPoolDTO> subjectPool = iFcSubjectPoolService.findSubjectPool();
		return BeanUtils.copyListProperties(subjectPool,FcSubjectPoolVO.class);
	}
    
    
    /***
     * 验证
     */
	public void verify(FcBusinessRuleDefinePoolVo ruleDefinePoolVo) {
		/** 网点 **/
		if (EmptyUtils.isEmpty(ruleDefinePoolVo.getPoolId())) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_POOLID_NOTNULL);
		}
		/** 科目 **/
//		if (EmptyUtils.isEmpty(ruleDefineVo.getSubjCode())) {
//			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_SUBJECTID_NOTNULL);
//		}
		/** 币别 **/
		if (EmptyUtils.isEmpty(ruleDefinePoolVo.getCurId())) {
			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_CURID_NOTNULL);
		}
	}
}
