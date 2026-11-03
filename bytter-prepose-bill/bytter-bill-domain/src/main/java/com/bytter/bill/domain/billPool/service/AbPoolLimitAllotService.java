package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbWorkFlowService;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.dao.AbPoolLimitAllotDao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitAllot;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotQueryVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotVO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Title: 额度分配Service
 * @Description: 额度分配业务处理类
 * @author zhenghj
 * @date 2020-01-13
 * @version 1.0
 *
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT)
@Service("abPoolLimitAllotService")
@Transactional
public class AbPoolLimitAllotService extends BaseService<AbPoolLimitAllot, String> implements IWorkFlowListener {
	@Autowired
	private AbPoolLimitAllotDao abPoolLimitAllotDao;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IDictService dictService;

	/**
	 * 加载数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_LIMITALLOT_RESOURCE_OP,dataTablesInput.getCurrentUserId());
		DataTablesOutput<AbPoolLimitAllotQueryVO> dataTablesUnion = abPoolLimitAllotDao.getQueryDataTablesUnion(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMIT_OP_TYPE","AB_POOL_LIMT_TYPE","AB_POOL_OPT_STATUS"
					,"AB_POOL_STATUS","AB_POOL_CHECK_TYPE","pool_limit_adjust_type");
			listData.forEach(item -> redundantValues(listDictMap, item));
		});
		return dataTablesUnion;
	}

	private static void redundantValues(Map<String, Map<String, DictDTO>> listDictMap, AbPoolLimitAllotQueryVO item) {
		Optional.ofNullable(listDictMap).ifPresent(listMap -> {
			Optional.ofNullable(item.getType()).flatMap(type -> Optional.ofNullable(listMap.get("AB_POOL_LIMIT_OP_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(type))))
					.ifPresent(dict -> item.setTypeName(dict.getName()));
			Optional.ofNullable(item.getLimtType()).flatMap(limtType -> Optional.ofNullable(listMap.get("AB_POOL_LIMT_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(limtType))))
					.ifPresent(dict -> item.setLimtTypeName(dict.getName()));
			Optional.ofNullable(item.getOptStatus()).flatMap(optStatus -> Optional.ofNullable(listMap.get("AB_POOL_OPT_STATUS"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus))))
					.ifPresent(dict -> item.setOptStatusName(dict.getName()));
			Optional.ofNullable(item.getStatus()).flatMap(status -> Optional.ofNullable(listMap.get("AB_POOL_STATUS"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(String.valueOf(status)))))
					.ifPresent(dict -> item.setStatusName(dict.getName()));
			Optional.ofNullable(item.getCheckType()).flatMap(checkType -> Optional.ofNullable(listMap.get("AB_POOL_CHECK_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(checkType))))
					.ifPresent(dict -> item.setCheckTypeName(dict.getName()));
			Optional.ofNullable(item.getAdjustType()).flatMap(adjustType -> Optional.ofNullable(listMap.get("pool_limit_adjust_type"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(adjustType))))
					.ifPresent(dict -> item.setAdjustTypeName(dict.getName()));
		});
	}

	/**
	 * 加载额度调剂数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getQueryDataTablesUnion(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=iCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_LIMITALLOT_RESOURCE_QUERYOP,dataTablesInput.getCurrentUserId());
		dataTablesInput.getFormdata().put("isQuery","true");
		return abPoolLimitAllotDao.getQueryDataTablesUnion(dataTablesInput, corpAuthList);
	}
	/**
	 * 功能介绍 : 保存额度分配信息<br>
	 *
	 * @param abPoolLimitAllotVO 额度分配信息
	 * @param userVo 操作用户
	 * @version 1.0
	 * @author zhenghj
	 */
	public boolean saveLimitAllotInfo(AbPoolLimitAllotVO abPoolLimitAllotVO, ICurrentUser userVo) {
		if (abPoolLimitAllotVO != null) {
			AbPoolLimitAllot limitAllot = new AbPoolLimitAllot();
			BeanUtils.copyNotNullProperties(abPoolLimitAllotVO, limitAllot);
			limitAllot.setStatus(AbPoolConstant.AB_POOL_STATUS11);
			limitAllot.setCreateId(userVo.getId());
			limitAllot.setCreateTime(new Date());
			abPoolLimitAllotDao.save(limitAllot);
			// 启动工作流
			 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT, limitAllot.getId(), abPoolLimitAllotVO,
					abPoolLimitAllotVO.getCorpId(), userVo.getId());
			abLogService.saveAbLog(null, limitAllot.getId(),null, AccebillConstant.ABLOG_LOGTYPE_ADD,
					AccebillConstant.LANG_LOG_SAVE_SUCC, userVo);
			return true;
		}
		return false;
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		//调用公共查询当前审批人审批
		DataTablesOutput dp = abPoolLimitAllotDao.dataTableApprove(dataTablesInput, businessList);
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 根据分配Id查询额度分配信息
	 *
	 * @param aid 额度分配id
	 * @return
	 */
	public AbPoolLimitAllotVO getLimitAllotVoById(String aid) {
		if (StringUtils.isNotEmpty(aid)) {
			List<AbPoolLimitAllotVO> list = abPoolLimitAllotDao.getLimitAllotVoById(aid);
			if (list != null && !list.isEmpty()) {
				Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMIT_OP_TYPE", "AB_POOL_LIMT_TYPE", "AB_POOL_OPT_STATUS"
						, "AB_POOL_STATUS", "AB_POOL_CHECK_TYPE");
				AbPoolLimitAllotVO allotVo = list.get(0);
				redundantValues(listDictMap, allotVo);
				return allotVo;
			}
		}
		return null;
	}

	private static void redundantValues(Map<String, Map<String, DictDTO>> listDictMap, AbPoolLimitAllotVO item) {
		Optional.ofNullable(listDictMap).ifPresent(listMap -> {
			Optional.ofNullable(item.getType()).flatMap(type -> Optional.ofNullable(listMap.get("AB_POOL_LIMIT_OP_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(type))))
					.ifPresent(dict -> item.setTypeName(dict.getName()));
			Optional.ofNullable(item.getLimtType()).flatMap(limtType -> Optional.ofNullable(listMap.get("AB_POOL_LIMT_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(limtType))))
					.ifPresent(dict -> item.setLimtTypeName(dict.getName()));
			Optional.ofNullable(item.getOptStatus()).flatMap(optStatus -> Optional.ofNullable(listMap.get("AB_POOL_OPT_STATUS"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus))))
					.ifPresent(dict -> item.setOptStatusName(dict.getName()));
			Optional.ofNullable(item.getStatus()).flatMap(status -> Optional.ofNullable(listMap.get("AB_POOL_STATUS"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(String.valueOf(status)))))
					.ifPresent(dict -> item.setStatusName(dict.getName()));
			Optional.ofNullable(item.getCheckType()).flatMap(checkType -> Optional.ofNullable(listMap.get("AB_POOL_CHECK_TYPE"))
							.flatMap(dictMap -> Optional.ofNullable(dictMap.get(checkType))))
					.ifPresent(dict -> item.setCheckTypeName(dict.getName()));
		});
	}

	@Override
	public BaseDao<AbPoolLimitAllot, String> getEntityDao() {
		return abPoolLimitAllotDao;
	}
	/**
	 * 审批回调方法
	 */
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		AbPoolLimitAllot entity = abPoolLimitAllotDao.get(workFlowResult.getBusinessKey());
		Integer status = workFlowResult.isPass() ? AbPoolConstant.AB_POOL_STATUS95 : AbPoolConstant.AB_POOL_STATUS_1;
		entity.setStatus(status);
		String logType = "";
		String msg = "";
		if (AbPoolConstant.AB_POOL_STATUS95.equals(status)) {
			// 审批通过
			// 指令状态为待发送
			entity.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
			logType = AccebillConstant.ABLOG_LOGTYPE_APP;
			msg = AccebillConstant.LANG_LOG_APP_SUCC;
		} else {
			// 审批打回
			logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
			msg = AccebillConstant.LANG_LOG_APP_BACK;
		}
		entity.setUpdateId(user.getId());
		entity.setUpdateTime(new Date());
		abPoolLimitAllotDao.update(entity);
		// 生成日志
		abLogService.saveAbLog(null, entity.getId(), null, logType, msg, user);
	}
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
}
