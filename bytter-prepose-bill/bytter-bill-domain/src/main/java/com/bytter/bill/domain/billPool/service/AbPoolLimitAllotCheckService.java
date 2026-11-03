package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbWorkFlowService;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.dao.AbPoolLimitAllotCheckDao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitAllotCheck;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotCheckVO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
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
 * @Title: 额度分配核准Service
 * @Description: 额度分配核准业务处理类
 * @author zhenghj
 * @date 2020-02-24
 * @version 1.0
 *
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT_CHECK)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT_CHECK)
@Service("abPoolLimitAllotCheckService")
@Transactional
public class AbPoolLimitAllotCheckService extends BaseService<AbPoolLimitAllotCheck, String>
		implements IWorkFlowListener {
	@Autowired
	private AbPoolLimitAllotCheckDao abPoolLimitAllotCheckDao;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
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
		List<String> corpAuthList=abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_LIMITALLOT_CHECK_RESOURCE_OP,dataTablesInput.getCurrentUserId());
		DataTablesOutput<AbPoolLimitAllotCheckVO> dataTablesUnion = abPoolLimitAllotCheckDao.getDataTablesUnion(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesUnion.getData()).ifPresent(listData->{
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMT_TYPE","AB_POOL_CHECK_TYPE","AB_POOL_OPT_STATUS","AB_POOL_STATUS");
			listData.forEach(item -> redundantValues(listDictMap, item));
		});
		return dataTablesUnion;
	}

	private static void redundantValues(Map<String, Map<String, DictDTO>> listDictMap, AbPoolLimitAllotCheckVO item) {
		Optional.ofNullable(listDictMap).ifPresent(listMap -> {
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

	/**
	 * 功能介绍 : 保存额度分配核准信息<br>
	 *
	 * @param abPoolLimitAllotCheckVO 额度分配核准信息
	 * @param userVo 操作用户
	 * @version 1.0
	 * @author zhenghj
	 */
	public boolean saveLimitAllotCheckInfo(AbPoolLimitAllotCheckVO abPoolLimitAllotCheckVO, ICurrentUser userVo) {
		if (abPoolLimitAllotCheckVO != null && StringUtils.isNotEmpty(abPoolLimitAllotCheckVO.getIds())) {
			String[] cid = abPoolLimitAllotCheckVO.getIds().split(",");
			for (int i = 0; i < cid.length; i++) {
				AbPoolLimitAllotCheck allotCheck = abPoolLimitAllotCheckDao.get(cid[i]);
				if(allotCheck == null) {
					return false;
				}
				allotCheck.setCheckType(abPoolLimitAllotCheckVO.getCheckType());
				allotCheck.setCheckMsg(abPoolLimitAllotCheckVO.getCheckMsg());
				allotCheck.setStatus(AbPoolConstant.AB_POOL_STATUS11);
				allotCheck.setUpdateTime(new Date());
				if(userVo!=null){
					allotCheck.setUpdateId(userVo.getId());
				}
				abPoolLimitAllotCheckDao.update(allotCheck);
				// 启动工作流
				 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT_CHECK, allotCheck.getId(), abPoolLimitAllotCheckVO,
						allotCheck.getCorpId(), userVo.getId());
				abLogService.saveAbLog(null, allotCheck.getId(), null, AccebillConstant.ABLOG_LOGTYPE_SUBMIT,
						AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
			}
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
				.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POOL_LIMITALLOT_CHECK, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		//调用公共查询当前审批人审批
		DataTablesOutput dp = abPoolLimitAllotCheckDao.dataTableApprove(dataTablesInput, businessList);
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 根据分配核准Id查询额度分配核准信息
	 *
	 * @param aid 额度分配核准id
	 * @return
	 */
	public AbPoolLimitAllotCheckVO getLimitAllotCheckVoByAid(String aid) {
		if (StringUtils.isNotEmpty(aid)) {
			List<AbPoolLimitAllotCheckVO> list = abPoolLimitAllotCheckDao.getLimitAllotCheckVoByAid(aid);
			if (list != null && !list.isEmpty()) {
				Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMT_TYPE", "AB_POOL_CHECK_TYPE", "AB_POOL_OPT_STATUS", "AB_POOL_STATUS");
				AbPoolLimitAllotCheckVO allotCheckVO = list.get(0);
				redundantValues(listDictMap, allotCheckVO);
				return allotCheckVO;
			}
		}
		return null;
	}
	@Override
	public BaseDao<AbPoolLimitAllotCheck, String> getEntityDao() {
		return abPoolLimitAllotCheckDao;
	}
	/**
	 * 审批回调方法
	 */
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		AbPoolLimitAllotCheck entity = abPoolLimitAllotCheckDao.get(workFlowResult.getBusinessKey());
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
		abPoolLimitAllotCheckDao.update(entity);
		// 生成日志
		abLogService.saveAbLog(null, entity.getId(), null, logType, msg, user);
	}
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
}
