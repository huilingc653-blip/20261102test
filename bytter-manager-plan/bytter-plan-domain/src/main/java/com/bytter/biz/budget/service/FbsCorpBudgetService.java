package com.bytter.biz.budget.service;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.budget.dao.FbsCorpBudgetDao;
import com.bytter.biz.budget.entity.FbsCorpBudget;
import com.bytter.biz.budget.vo.CorpVO;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.biz.budget.vo.FbsCorpBudgetVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.OrgantypeModuleDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description:
 * @date 2017-06-09 15:24:05
 */
@Service("fbsCorpBudgetService")
@Transactional
public class FbsCorpBudgetService extends BaseService<FbsCorpBudget, String> implements ICorpDeleteVerifyService {
	@Autowired
	private FbsCorpBudgetDao fbsCorpBudgetDao;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@Autowired
	private FbsBudgetResolveYearService fbsBudgetResolveYearService;
	@Autowired
	private FbsBudgetResolveMonthService fbsBudgetResolveMonthService;
	@Autowired
	private FbsBudgetRollMonthService fbsBudgetRollMonthService;
	@Autowired
	private FbsBudgetRollMonthWeekService fbsBudgetRollMonthWeekService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Resource
    private IMultiLangService multiLangServiceI;
    @Resource
    private IOrgantypeService organtypeServiceImpl;
	@Override
	public BaseDao<FbsCorpBudget, String> getEntityDao() {
		return fbsCorpBudgetDao;
	}
	/**
	 * 保存资金计划控制
	 *
	 * @param reportCorpIds
	 * @param controlCorpIds
	 */
	public void saveCtlBudget(List<String> reportCorpIds, List<String> controlCorpIds) {
		fbsCorpBudgetDao.updateReportBudget(reportCorpIds);
		fbsCorpBudgetDao.updateControlBudget(controlCorpIds);
	}
	/**
	 * 保存资金计划汇总
	 *
	 * @param corpIds
	 */
	public void saveCollectFlag(List<String> corpIds) {
		fbsCorpBudgetDao.updateCollectFlag(corpIds);
	}
	/**
	 * 初始化零计划
	 *
	 * @param corpId
	 */
	public void initZeroBudget(String corpId, ICurrentUser user) {
		//是否有年计划及控制：0-没有 1-有并控制子周期计划 2-有但控制子周期计划
		String ycrl = configService.getConfig("fbs", "ycrl").getValue();
		String qcrl = configService.getConfig("fbs", "qcrl").getValue();
		String mcrl = configService.getConfig("fbs", "mcrl").getValue();
		String wcrl = configService.getConfig("fbs", "wcrl").getValue();
		String dcrl = configService.getConfig("fbs", "dcrl").getValue();
		if (!CommonConstant.NO.equals(ycrl)) {//年计划
			String timeType = BudgetConstant.TIME_TYPE_YEAR;
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), BudgetConstant.TIME_TYPE_YEAR);
			FbsBudgetVO budget = fbsBudgetService.initBudget(corpId, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), BudgetConstant.STATUE_CHECK, user);
			//年计划上报模式:0,年固定;1,年分解
			if (BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equals(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_YIMODE))) {
				fbsBudgetResolveYearService.initResolveYearBudget(budget, user);
			}
		}
		if (!CommonConstant.NO.equals(qcrl)) {//季计划
			String timeType = BudgetConstant.TIME_TYPE_QUARTER;
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			fbsBudgetService.initBudget(corpId, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), BudgetConstant.STATUE_CHECK, user);
		}
		if (!CommonConstant.NO.equals(mcrl)) {//月计划
			String timeType = BudgetConstant.TIME_TYPE_MONTH;
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			FbsBudgetVO budget = fbsBudgetService.initBudget(corpId, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), BudgetConstant.STATUE_CHECK, user);
			//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
			String mimode = fbsBudgetService.getInputMode("mimode");
			if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING.equals(mimode)) {
				fbsBudgetRollMonthService.initBudget(budget, user);
			} else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_ROLLING_WEEK.equals(mimode)) {
				fbsBudgetRollMonthWeekService.initBudget(budget, user);
			} else if (BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equals(mimode)) {
				fbsBudgetResolveMonthService.initResolveMonthBudget(budget, user);
			}
		}
		if (!CommonConstant.NO.equals(wcrl)) {//周计划
			String timeType = BudgetConstant.TIME_TYPE_WEEK;
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			fbsBudgetService.initBudget(corpId, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), BudgetConstant.STATUE_CHECK, user);
		}
		if (!CommonConstant.NO.equals(dcrl)) {//天计划
			String timeType = BudgetConstant.TIME_TYPE_DAY;
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(DateUtils.getDate(), timeType);
			fbsBudgetService.initBudget(corpId, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), BudgetConstant.STATUE_CHECK, user);
		}
	}
	/**
	 * 查询资金计划控制表
	 *
	 * @param seach 查询条件-单位编码或单位名称
	 * @return
	 */
	public List<CorpDTO> listCorpIdsBySeach(String seach) {
		List<String> corpIds = corpService.getCorpIds();
		// 所有需要计划汇总设置的单位列表
		List<FbsCorpBudget> allCollect = corpIds.stream().map(c -> {
			FbsCorpBudget fbsCorpBudget = new FbsCorpBudget();
			fbsCorpBudget.setCorpId(c);
			fbsCorpBudget.setCollectFlag(0);
			fbsCorpBudget.setControlBudget(0);
			fbsCorpBudget.setReportBudget(0);
			return fbsCorpBudget;
		}).collect(Collectors.toList());
		// 已初始化的汇总设置
		List<FbsCorpBudget> list = this.fbsCorpBudgetDao.listFbsCorpBudgetByNotCorp();
		if (ObjectUtil.isNotEmpty(list)) {
			// 过滤掉已初始化的数据
			allCollect = allCollect.stream().filter(
							a -> !list.stream().map(FbsCorpBudget::getCorpId).collect(Collectors.toList()).contains(a.getCorpId()))
					.collect(Collectors.toList());
		}
		if (ObjectUtil.isNotEmpty(allCollect)){
			logger.info("初始化单位计划汇总设置数据。。。。。。");
			this.batchSaveOrUpdate(allCollect);
		}
//		List<String> ctlCorpIds = this.fbsCorpBudgetDao.listCorpIdsBySeach(seach);
		List<String> ctlCorpIds = corpService.listCorpIdsBySeach(seach);
		List<CorpDTO> listCorp = this.corpService.listCorpDTOByCorpIds2(ctlCorpIds);
		if (EmptyUtils.isEmptyList(listCorp)) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		return corpService.listCorpTree(listCorp);
	}
	public Map<String, FbsCorpBudgetVO> listFbsCorpBudget(String seach) {
		List<CorpDTO> corpDTOList = corpService.listCorpBySeach(seach);
		List<String> corpIds = corpDTOList.stream().map(CorpDTO::getId).collect(Collectors.toList());
		List<FbsCorpBudgetVO> FbsCorpBudgetVOs = this.fbsCorpBudgetDao.listFbsCorpBudget(corpIds);
		if (EmptyUtils.isEmptyList(FbsCorpBudgetVOs)) {
			return null;
		}
		Map<String, CorpDTO> corpDTOMap = corpDTOList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
		Map<String, FbsCorpBudgetVO> map = new HashMap<>();
		for (FbsCorpBudgetVO vo : FbsCorpBudgetVOs) {
			CorpDTO dto = corpDTOMap.get(vo.getCorpId());
			vo.setCorpName(dto.getName());
			vo.setCorpCode(dto.getCode());
			vo.setCorpParentId(dto.getParentId());
			vo.setCorpNetId(dto.getNetId());
			vo.setCorpType(dto.getType());
			vo.setChildCount(dto.getChildCount());
			map.put(vo.getCorpId(), vo);
		}
		return map;
	}
	public Map<String, FbsCorpBudgetVO> listFbsCorpBudgets(String seach) {
		List<CorpDTO> corpDTOList = corpService.listCorpBySeach(seach);
		List<String> corpIds = corpDTOList.stream().map(CorpDTO::getId).collect(Collectors.toList());
		Map<String, CorpDTO> corpDTOMap = corpDTOList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
		List<FbsCorpBudgetVO> fbsCorpBudgetVOS = this.listByCorpIds(corpIds);
		List<FbsCorpBudgetVO> list = fbsCorpBudgetVOS.stream().map(a -> {
			FbsCorpBudgetVO fbsCorpBudgetVO = new FbsCorpBudgetVO();
			CorpDTO dto = corpDTOMap.get(a.getCorpId());
			fbsCorpBudgetVO.setCorpId(dto.getId());
			fbsCorpBudgetVO.setCorpCode(dto.getCode());
			fbsCorpBudgetVO.setCorpName(dto.getName());
			fbsCorpBudgetVO.setCorpType(dto.getType());
			fbsCorpBudgetVO.setCorpParentId(dto.getParentId());
			fbsCorpBudgetVO.setCorpNetId(dto.getNetId());
			fbsCorpBudgetVO.setChildCount(dto.getChildCount());
			fbsCorpBudgetVO.setReportBudget(a.getReportBudget());
			fbsCorpBudgetVO.setControlBudget(a.getControlBudget());
			fbsCorpBudgetVO.setCollectFlag(a.getCollectFlag());
			return fbsCorpBudgetVO;
		}).collect(Collectors.toList());
//		List<FbsCorpBudgetVO> list = this.fbsCorpBudgetDao.listFbsCorpBudgets(seach);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		Map<String, FbsCorpBudgetVO> map = new HashMap<>();
		for (FbsCorpBudgetVO vo : list) {
			map.put(vo.getCorpId(), vo);
		}
		return map;
	}
	
	public Map<String,CorpVO> mapCorp(String netId){
		List<CorpVO> list=this.fbsCorpBudgetDao.listCorp(netId);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		Map<String, CorpVO> map = new HashMap<>();
		for (CorpVO vo : list) {
			map.put(vo.getCorpId(), vo);
		}
		return map;
	}

	public List<CorpVO> listCorp(String netId){
		List<CorpVO> list=this.fbsCorpBudgetDao.listCorp(netId);
		return list;
	}
	
	
	/**
	 * 根据单位ID获取单位资金计划设置表
	 *
	 * @param corpId
	 * @return
	 */
	public FbsCorpBudget getByCorpId(String corpId) {
		return this.fbsCorpBudgetDao.getUniqueBy("corpId", corpId);
	}
	/**
	 * 根据单位ID获取单位资金计划设置表
	 *
	 * @param corpId
	 * @return
	 */
	public FbsCorpBudgetVO getCorpBudgetVOByCorpId(String corpId) {
		CorpDTO corpDTO = corpService.get(corpId);
		if (Objects.isNull(corpDTO)) return null;
		FbsCorpBudgetVO corpBudgetVO = this.fbsCorpBudgetDao.getCorpBudgetVOByCorpId(corpId);
		if (EmptyUtils.isEmpty(corpBudgetVO)) {
			return null;
		}
		corpBudgetVO.setCorpName(corpDTO.getName());
		corpBudgetVO.setCorpCode(corpDTO.getCode());
		corpBudgetVO.setCorpParentId(corpDTO.getParentId());
		corpBudgetVO.setCorpType(corpDTO.getType());
		corpBudgetVO.setCorpNetId(corpDTO.getNetId());
		return corpBudgetVO;
	}
	/**
	 * 单位是否上报
	 * true/false
	 *
	 * @param corpId
	 * @return
	 */
	public boolean reportBudgetByCorpId(String corpId) {
		FbsCorpBudget entity = this.getByCorpId(corpId);
		if (EmptyUtils.isEmpty(entity) || EmptyUtils.isEmpty(entity.getReportBudget())) {
			return false;
		}
		return BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES.intValue() == entity.getReportBudget().intValue();
	}
	/**
	 * 单位是否控制
	 * true/false
	 *
	 * @param corpId
	 * @return
	 */
	public boolean controlBudgetByCorpId(String corpId) {
		FbsCorpBudget entity = this.getByCorpId(corpId);
		if (EmptyUtils.isEmpty(entity) || EmptyUtils.isEmpty(entity.getControlBudget())) {
			return false;
		}
		return BudgetConstant.CORP_BUDGET_CONTROL_BUDGET_YES.intValue() == entity.getControlBudget().intValue();
	}
	/**
	 * 单位是否汇总
	 * true/false
	 *
	 * @param corpId
	 * @return
	 */
	public boolean collectFlagByCorpId(String corpId) {
		FbsCorpBudget entity = this.getByCorpId(corpId);
		if (EmptyUtils.isEmpty(entity) || EmptyUtils.isEmpty(entity.getCollectFlag())) {
			return false;
		}
		return BudgetConstant.CORP_BUDGET_COLLECT_FLAG_YES.intValue() == entity.getCollectFlag().intValue();
	}
	/**
	 * 获取可上报的单位
	 *
	 * @return
	 */
	public List<FbsCorpBudgetVO> listFbsCorpBudgetVOByReportBudget() {
		List<FbsCorpBudgetVO> fbsCorpBudgetVOS = this.fbsCorpBudgetDao.listFbsCorpBudgetVO(BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES, null, null);
		List<String> corpIds = fbsCorpBudgetVOS.stream().map(FbsCorpBudgetVO::getCorpId).collect(Collectors.toList());
		List<CorpDTO> corpDTOList = corpService.getCorpByIdsHadChildCount(corpIds);
		Map<String, CorpDTO> corpDTOMap = corpDTOList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
		fbsCorpBudgetVOS.forEach(a -> {
			CorpDTO corpDTO = corpDTOMap.get(a.getCorpId());
			a.setCorpName(corpDTO.getName());
			a.setCorpCode(corpDTO.getCode());
			a.setCorpType(corpDTO.getType());
			a.setCorpNetId(corpDTO.getNetId());
			a.setChildCount(corpDTO.getChildCount());
			a.setCorpParentId(corpDTO.getParentId());
		});
		return fbsCorpBudgetVOS;
	}
	/**
	 * 获取受控的单位
	 *
	 * @return
	 */
	public List<FbsCorpBudgetVO> listFbsCorpBudgetDTOByControlBudget() {
		List<FbsCorpBudgetVO> fbsCorpBudgetVOS = this.fbsCorpBudgetDao.listFbsCorpBudgetVO(BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES, BudgetConstant.CORP_BUDGET_CONTROL_BUDGET_YES, null);
		List<String> corpIds = fbsCorpBudgetVOS.stream().map(FbsCorpBudgetVO::getCorpId).collect(Collectors.toList());
		List<CorpDTO> corpDTOList = corpService.getCorpByIdsHadChildCount(corpIds);
		Map<String, CorpDTO> corpDTOMap = corpDTOList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
		fbsCorpBudgetVOS.forEach(a -> {
			CorpDTO corpDTO = corpDTOMap.get(a.getCorpId());
			a.setCorpName(corpDTO.getName());
			a.setCorpCode(corpDTO.getCode());
			a.setCorpType(corpDTO.getType());
			a.setCorpNetId(corpDTO.getNetId());
			a.setChildCount(corpDTO.getChildCount());
			a.setCorpParentId(corpDTO.getParentId());
		});
		return fbsCorpBudgetVOS;
	}
	/**
	 * 获取可上报的下属单位
	 *
	 * @param parentCorpId
	 * @return
	 */
	public List<FbsCorpBudgetVO> listReportBudgetByParentCorpId(String parentCorpId) {
		// List<CorpDTO> corpDTOList = corpService.getCorpByParentId(parentCorpId);
        // ArrayList<FbsCorpBudgetVO> fbsCorpBudgetVOS = new ArrayList<>();
		// fbsCorpBudgetVOList.forEach(a -> {
		// 	corpDTOList.forEach(b ->{
		// 		if (a.getCorpId().equals(b.getId())){
		// 			FbsCorpBudgetVO fbsCorpBudgetVO = new FbsCorpBudgetVO();
		// 			fbsCorpBudgetVO.setCorpId(b.getId());
		// 			fbsCorpBudgetVO.setCorpCode(b.getCode());
		// 			fbsCorpBudgetVO.setCorpName(b.getName());
		// 			fbsCorpBudgetVO.setCorpType(b.getType());
		// 			fbsCorpBudgetVO.setCorpNetId(b.getNetId());
		// 			fbsCorpBudgetVO.setCorpParentId(b.getParentId());
		// 			fbsCorpBudgetVO.setChildCount(b.getChildCount());
		// 			fbsCorpBudgetVO.setCollectFlag(a.getCollectFlag());
		// 			fbsCorpBudgetVO.setControlBudget(a.getControlBudget());
		// 			fbsCorpBudgetVO.setReportBudget(a.getReportBudget());
		// 			fbsCorpBudgetVOS.add(fbsCorpBudgetVO);
		// 		}
		// 	});
		// });
		return this.fbsCorpBudgetDao.listReportBudgetByParentCorpId(parentCorpId);
	}
	/**
	 * 获取可汇总的下属单位
	 * 必须可上报才能可汇总
	 *
	 * @param parentCorpId
	 * @return
	 */
	public List<FbsCorpBudgetVO> listReportAndCollectByParentCorpId(String parentCorpId) {
		// List<CorpDTO> corpDTOList = corpService.getCorpByParentId(parentCorpId);
        // ArrayList<FbsCorpBudgetVO> fbsCorpBudgetVOS = new ArrayList<>();
		// fbsCorpBudgetVOList.forEach(a -> {
		// 	corpDTOList.forEach(b ->{
		// 		if (a.getCorpId().equals(b.getId())){
		// 			FbsCorpBudgetVO fbsCorpBudgetVO = new FbsCorpBudgetVO();
		// 			fbsCorpBudgetVO.setCorpId(b.getId());
		// 			fbsCorpBudgetVO.setCorpCode(b.getCode());
		// 			fbsCorpBudgetVO.setCorpName(b.getName());
		// 			fbsCorpBudgetVO.setCorpType(b.getType());
		// 			fbsCorpBudgetVO.setCorpNetId(b.getNetId());
		// 			fbsCorpBudgetVO.setCorpParentId(b.getParentId());
		// 			fbsCorpBudgetVO.setChildCount(b.getChildCount());
		// 			fbsCorpBudgetVO.setCollectFlag(a.getCollectFlag());
		// 			fbsCorpBudgetVO.setControlBudget(a.getControlBudget());
		// 			fbsCorpBudgetVO.setReportBudget(a.getReportBudget());
		// 			fbsCorpBudgetVOS.add(fbsCorpBudgetVO);
		// 		}
		// 	});
		// });
		return this.fbsCorpBudgetDao.listReportAndCollectByParentCorpId(parentCorpId);
	}
	/**
	 * 获取汇总单位集合
	 *
	 * @param parentCorpIds
	 * @return
	 */
	public Map<String, String> listReportAndCollectByParentCorpId(List<String> parentCorpIds) {
		Map<String, String> collectMap = new HashMap<>();
		//获取管理的单位,vo.getCorpId() --> sys_corp 的 parentId
		List<FbsCorpBudgetVO> list = this.fbsCorpBudgetDao.listReportAndCollectByParentCorpId(parentCorpIds);
		/*if (!EmptyUtils.isEmptyList(list)) {
			for (FbsCorpBudgetVO vo : list) {
				collectMap.put(vo.getCorpId(), CommonConstant.YES);
			}
		}*/
		//取交集 select distinct c.parent_id as corpId from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id
		//根据 parentCorpIds,c.status = 1 查询 sys_corp表，返回 corp_id,parent_id；拿sys_corp 返回的数据
		List<CorpDTO> corpDTOS = corpService.getCorpByParentIds(parentCorpIds);
		List<CorpDTO> collect1 = corpDTOS.stream().filter(a -> list.stream().map(FbsCorpBudgetVO::getCorpId).anyMatch(corpId -> a.getId().equals(corpId))).collect(Collectors.toList());
		if (!EmptyUtils.isEmptyList(collect1)) {
			for (CorpDTO dto : collect1) {
				collectMap.put(dto.getParentId(), CommonConstant.YES);
			}
		}
		return collectMap;
	}
	/**
	 * 判断单位是否要汇总下级单位
	 *
	 * @param corpId
	 * @return
	 */
	public boolean getChildCollectFlag(String corpId) {
		boolean result = false;
		//获取管理的单位
		List<FbsCorpBudgetVO> list = this.listReportAndCollectByParentCorpId(corpId);
		if (!EmptyUtils.isEmptyList(list)) {
			for (FbsCorpBudgetVO vo : list) {
				if (BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES.intValue() == vo.getReportBudget().intValue()
						&& BudgetConstant.CORP_BUDGET_COLLECT_FLAG_YES.intValue() == vo.getCollectFlag().intValue()) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
	/**
	 * 判断单位本身是否要汇总
	 *
	 * @param corpId
	 * @return
	 */
	public boolean getCollectFlag(String corpId) {
		FbsCorpBudgetVO vo = this.getCorpBudgetVOByCorpId(corpId);
		boolean result = false;
		if (!EmptyUtils.isEmpty(vo) && vo.getCollectFlag() == 1 && StringUtils.isNotEmpty(vo.getCorpParentId())) {
			result = true;
		}
		return result;
	}
	/**
	 * 根据单位集合获取单位资金计划设置表
	 *
	 * @return
	 */
	public List<FbsCorpBudgetVO> listByCorpIds(List<String> corpIds) {
		//改为接口调用，填充数据
//		List<CorpDTO> corpDTOList = iSystemForeignService.getCorpByIds(corpIds);
		List<CorpDTO> corpDTOList = corpService.listCorpDTOByCorpIds2(corpIds);
		List<FbsCorpBudgetVO> fbsCorpBudgetVOList = this.fbsCorpBudgetDao.listByCorpIds(corpIds);
		fbsCorpBudgetVOList.forEach(a ->{
			corpDTOList.forEach(b ->{
				if (Objects.equals(a.getCorpId(),b.getId())){
					a.setCorpName(b.getName());
					a.setCorpCode(b.getCode());
					a.setCorpParentId(b.getParentId());
					a.setCorpType(b.getType());
					a.setCorpNetId(b.getNetId());
				}
			});
		});
		return fbsCorpBudgetVOList;
	}
	/**
	 * 获取具有上报权限的单位集合
	 *
	 * @return
	 */
	public List<String> listCorpIdsByReportBudget(List<String> corpIds) {
//		List<CorpDTO> corpDTOS = iSystemForeignService.getCorpByIds(corpIds);
		List<CorpDTO> corpDTOS = corpService.listCorpDTOByCorpIds2(corpIds);
		List<String> list = this.fbsCorpBudgetDao.listCorpIdsByReportBudget(corpIds);
		if (CollectionUtils.isEmpty(corpDTOS)){
			return list;
		}
		List<String> collect = list.stream().filter(a -> corpDTOS.stream().map(CorpDTO::getId).anyMatch(id -> Objects.equals(id, a))).collect(Collectors.toList());
//		return this.fbsCorpBudgetDao.listCorpIdsByReportBudget(corpIds);
		return collect;
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位设置为资金计划汇总单位，不允许删除！
			int num = this.fbsCorpBudgetDao.getCorpBudgetCountByCorpId(corpId, CommonConstant.NO);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.FBS,
						SysConfigConstant.BusType.FBS_COLLECTFLAG,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_DEL_COLLECTFLAG_ERROR)));
			}
			//单位设置为资金计划上报或控制单位，不允许删除!
			num = this.fbsCorpBudgetDao.getCorpBudgetCountByCorpId(corpId, CommonConstant.YES);
			if(num>0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.FBS,
						SysConfigConstant.BusType.FBS_CONTROLBUDGET,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_DEL_CONTROLBUDGET_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}


    /**
     * 判断单位是否要汇总下级单位
     *
     * @param corpIds
     * @return
     */
    public boolean getChildCollectFlagMap(List<String> corpIds) {
        boolean result = false;
        //获取管理的单位
        List<FbsCorpBudgetVO> list = fbsCorpBudgetDao.listReportAndCollectByParentCorpIds(corpIds);
        if (!EmptyUtils.isEmptyList(list)) {
            for (FbsCorpBudgetVO vo : list) {
                if (BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES.intValue() == vo.getReportBudget().intValue()
                        && BudgetConstant.CORP_BUDGET_COLLECT_FLAG_YES.intValue() == vo.getCollectFlag().intValue()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取可上报的下属单位
     *
     * @param parentCorpIds
     * @return
     */
    public List<FbsCorpBudgetVO> listReportBudgetByParentCorpId(List<String> parentCorpIds) {
        return this.fbsCorpBudgetDao.listReportBudgetByParentCorpId(parentCorpIds);
    }

    /**
     * 判断单位本身是否要汇总
     *
     * @param corpIds
     * @return
     */
    public Map<String,Boolean> queryCollectFlagMap(List<String> corpIds) {
        List<FbsCorpBudgetVO> voList = this.queryCorpBudgetVOByCorpId(corpIds);
        Map<String,Boolean> flagMap=new HashMap<>();
        for (FbsCorpBudgetVO vo:voList){
            boolean result = false;
            if (!EmptyUtils.isEmpty(vo) && vo.getCollectFlag() == 1 && StringUtils.isNotEmpty(vo.getCorpParentId())) {
                result = true;
            }
            flagMap.put(vo.getCorpId(),result);
        }
        return flagMap;
    }



    /**
     * 根据单位ID获取单位资金计划设置表
     *
     * @param corpIds
     * @return
     */
    public List<FbsCorpBudgetVO> queryCorpBudgetVOByCorpId(List<String> corpIds) {
        //是否启动资金计划组织 1为已启动，2为未启动
        OrgantypeModuleDTO organtypeModuleDTO = organtypeServiceImpl.getOrgantypeModule(SysConfigConstant.Module.FBS);
        Integer isValid = organtypeModuleDTO.getIsValid();
        List<FbsCorpBudgetVO> voList = null;
        if(isValid == 1){
            //获取多组织版本号id
            String releaseId =  corpService.getEffectiveOrgIdByModuld(ConfigConstant.FBS);
            voList = this.fbsCorpBudgetDao.queryCorpBudgetVOByCorpId(corpIds,releaseId);
        }else{
            voList = this.fbsCorpBudgetDao.queryCorpBudgetVOByCorpId(corpIds);
        }
        return voList;
    }


}
