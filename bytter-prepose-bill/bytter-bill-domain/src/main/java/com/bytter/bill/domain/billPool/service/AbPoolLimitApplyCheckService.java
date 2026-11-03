package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbWorkFlowService;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.dao.AbPoolLimitApplyCheckDao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitApplyCheck;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyCheckVO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Title: AbPoolLimitApplycheckService
 * @Description: 申请额度核准Service
 * @author jinyong.cao
 * @date 2020/1/21 00:07:38
 *
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POLL_IMITAPPLY_CHECK)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POLL_IMITAPPLY_CHECK)
@Service("abPoolLimitApplyCheckService")
@Transactional
public class AbPoolLimitApplyCheckService extends BaseService<AbPoolLimitApplyCheck,String> implements IWorkFlowListener{

	@Autowired
	private AbCorpService corpService;

	@Autowired
	private AbPoolLimitApplyCheckDao abPoolLimitApplyCheckDao;

	@Autowired(required = false)
	private AbWorkFlowService workFlowService;

	@Autowired
	private AbLogService abLogService;
	@DubboReference(check = false)
	private IDictService dictService;

	/**
     * 查询额度核领数据
     * @param
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(AbPoolConstant.AB_POLL_APPLYCHECK_RESOURCE_OP,
                input.getCurrentUserId());
		DataTablesOutput<AbPoolLimitApplyCheckVO> dataTablesOutput = abPoolLimitApplyCheckDao.listAbPoolIntoApplyCheck(input, corpAuthList, input.getCurrentUserId());
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMT_TYPE", "AB_POOL_STATUS", "AB_POOL_OPT_STATUS", "AB_POOL_CHECK_TYPE");
			listData.forEach(item -> redundantValues(listDictMap, item));
		});
		return dataTablesOutput;
    }

	private static void redundantValues(Map<String, Map<String, DictDTO>> listDictMap, AbPoolLimitApplyCheckVO item) {
		Optional.ofNullable(listDictMap).ifPresent(listMap -> {
			Optional.ofNullable(listDictMap.get("AB_POOL_LIMT_TYPE"))
					.flatMap(dictMap -> Optional.ofNullable(item.getLimtType())
							.flatMap(limitType -> Optional.ofNullable(dictMap.get(limitType))))
					.ifPresent(dict -> item.setLimtTypeName(dict.getName()));
			Optional.ofNullable(listDictMap.get("AB_POOL_STATUS"))
					.flatMap(dictMap -> Optional.ofNullable(item.getStatus())
							.flatMap(status -> Optional.ofNullable(dictMap.get(status))))
					.ifPresent(dict -> item.setStatusName(dict.getName()));
			Optional.ofNullable(listDictMap.get("AB_POOL_OPT_STATUS"))
					.flatMap(dictMap -> Optional.ofNullable(item.getOpStatus())
							.flatMap(opStatus -> Optional.ofNullable(dictMap.get(opStatus))))
					.ifPresent(dict -> item.setOpStatusName(dict.getName()));
			Optional.ofNullable(listDictMap.get("AB_POOL_CHECK_TYPE"))
					.flatMap(dictMap -> Optional.ofNullable(item.getCheckType())
							.flatMap(checkType -> Optional.ofNullable(dictMap.get(checkType))))
					.ifPresent(dict -> item.setCheckTypeName(dict.getName()));
		});
	}

	/**
     * 保存核d
     * @param user
     * @return
     */
    public AjaxJson saveLimitApplyCheck(AbPoolLimitApplyCheckVO abPoolLimitApplyCheckVO,ICurrentUser user){
        AjaxJson reJson = new AjaxJson();
		if (StringUtils.isNotEmpty(abPoolLimitApplyCheckVO.getIds())) {
			String[] ridArray = abPoolLimitApplyCheckVO.getIds().split(",");
			for (String rid : ridArray) {
				//修改
				this.updateabPoolLimitApplyCheckById(rid, AbPoolConstant.AB_POOL_STATUS11,AbPoolConstant.AB_POOL_OPT_STATUS16,
						abPoolLimitApplyCheckVO.getCheckType(), abPoolLimitApplyCheckVO.getCheckMsg(), user);
				AbPoolLimitApplyCheck vo = abPoolLimitApplyCheckDao.get(rid,true);
				 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POLL_IMITAPPLY_CHECK, rid, null,
						vo.getCorpId(),user.getId());
				abLogService.saveAbLog(rid, rid, null, 
	    				AccebillConstant.ABLOG_LOGTYPE_ADD,AbPoolConstant.BILL_LOG_BILLPOOL_LIMIT_APPLY, user);
			}
		}
        reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
        reJson.setSuccess(true);
		return reJson;
        
    }
	public int updateabPoolLimitApplyCheckById(String rid, Integer status, String optStatus, String checkType,
			String checkMsg, ICurrentUser userVo) {
		if (StringUtils.isEmpty(rid)) {
			throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params = new HashMap<>();
		String[] ids = { rid };
		params.put("ids", ids);
		if (status != null) {
			params.put("status", status);
		}
		if (optStatus != null) {
			params.put("optStatus", optStatus);
		}
		if (userVo != null) {
			params.put("updateId", userVo.getId());
		}
		if (checkType != null) {
			params.put("checkType", checkType);
		}
		if (checkMsg != null) {
			params.put("checkMsg", checkMsg);
		}
		params.put("updateTime", new Date());
		return abPoolLimitApplyCheckDao.updateRecordByParams(params);
	}
	
	 /**
     * 根据流水Id查询申领详细
     *
     * @return
     */
    public AbPoolLimitApplyCheckVO getAbPoolLimitApplyVoById(String id) {
		AbPoolLimitApplyCheckVO applyCheckVO = abPoolLimitApplyCheckDao.queryAbPoolLimitApplyCheckVoById(id);
		Optional.ofNullable(applyCheckVO).ifPresent(item -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMT_TYPE", "AB_POOL_STATUS", "AB_POOL_OPT_STATUS", "AB_POOL_CHECK_TYPE");
			redundantValues(listDictMap, item);
		});
		return applyCheckVO;
    }
    
    @Override
	public BaseDao<AbPoolLimitApplyCheck, String> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
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
        WorkFlowBusiness workFlowBusiness = this
                .getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POLL_IMITAPPLY_CHECK, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        //调用公共查询当前审批人审批
        DataTablesOutput dp = abPoolLimitApplyCheckDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    
    
	public WorkFlowBusiness getWorkFlowBusiness(String processDefinitionKey, IParameterInput pagePara){
		return workFlowService.getWorkFlowBusiness(processDefinitionKey, pagePara);
	}
	
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		 String appMsg = workFlowResult.isPass() ? AbPoolConstant.BILL_LOG_APP_SUCC : AbPoolConstant.BILL_LOG_APP_BACK;
		 AbPoolLimitApplyCheck abPoolLimitApplyCheck = abPoolLimitApplyCheckDao.get(workFlowResult.getBusinessKey());
         int num = 0;
         if (workFlowResult.isPass()) {
            /** 审批通过 */
            //修改流水表为审批通过,业务完成
            num = this.updateabPoolLimitApplyCheckById(abPoolLimitApplyCheck.getId(),
            		AbPoolConstant.AB_POOL_STATUS95,AbPoolConstant.AB_POOL_OPT_STATUS16, 
            		null,null,workFlowResult.getUser());
         } else {
            /** 打回 */
            num = this.updateabPoolLimitApplyCheckById(abPoolLimitApplyCheck.getId(),AbPoolConstant.AB_POOL_STATUS_1,null,
            		null,null,
            		workFlowResult.getUser());
         }
         if (num != 1) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        String logType = workFlowResult.isPass() ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
        abLogService.saveAbLog(abPoolLimitApplyCheck.getId(), abPoolLimitApplyCheck.getId(), null, logType, appMsg, workFlowResult.getUser());	
	}
}
