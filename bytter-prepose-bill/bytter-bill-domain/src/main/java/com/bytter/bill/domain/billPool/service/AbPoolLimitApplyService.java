package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbWorkFlowService;
import com.bytter.bill.domain.billPool.dao.*;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitApply;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyVO;
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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.system.api.ICorpDeleteVerifyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@WorkFlow(BillPoolWorkFlowConstant.AB_POOL_LIMIT_APPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POOL_LIMIT_APPLY)
@Service("abPoolLimitApplyService")
@Transactional
public class AbPoolLimitApplyService extends BaseService<AbPoolLimitApplyVO, String> 
implements IWorkFlowListener,ICorpDeleteVerifyService{
	@Autowired(required=false)
	private AbCorpService corpService;
	
	@Autowired
	private AbPoolLimitApplyDao abPoolLimitApplyDao;
	
	@Autowired
	private AbPoolLimitAllotDao abPoolLimitAllotDao;
	
	@Autowired
	private AbLogService abLogService;
	
	
	@Autowired(required=false)
	private AbWorkFlowService workFlowService;
	
	@Resource
    private IMultiLangService multiLangServiceI;
	
	@Autowired
	private AbPoolBailDtlDao abPoolBailDtlDao;
	
	@Autowired
	private AbPoolIntoApplyDao abPoolIntoApplyDao;
	
	@Autowired
	private AbPoolSignDao abPoolSignDao;
	@DubboReference(check = false)
	private IDictService dictService;
	
	
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		// 获取该用户有权限的单位id集
		List<String> corpAuthList = corpService.getUserCorpIds(AbPoolConstant.AB_POLL_LIMITAPPLY_RESOURCE_OP,
				input.getCurrentUserId());
		DataTablesOutput<AbPoolLimitApplyVO> dataTablesOutput = abPoolLimitApplyDao.listAbPoolLimitApply(input, corpAuthList, input.getCurrentUserId());
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(this::redundantValues);
		return dataTablesOutput;
	}

	private void redundantValues(List<AbPoolLimitApplyVO> listData) {
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_POOL_LIMT_TYPE","AB_POOL_STATUS","AB_POOL_OPT_STATUS");
		Optional.ofNullable(listDictMap).ifPresent(listMap -> listData.forEach(item -> {
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
		}));
	}


	public AjaxJson updateLimitApply(AbPoolLimitApplyVO abPoolLimitApplyVO,ICurrentUser user){
		AjaxJson reJson = new AjaxJson();
		AbPoolLimitApply abPoolLimitApply = new AbPoolLimitApply();
		BeanUtils.copyNotNullProperties(abPoolLimitApplyVO, abPoolLimitApply);
		abPoolLimitApply.setCreateTime(new Date());
		abPoolLimitApply.setCreateId(user.getId());
		abPoolLimitApply.setStatus(AbPoolConstant.AB_POOL_STATUS11);
		abPoolLimitApply.setOptStatus(null);
	    abPoolLimitApplyDao.save(abPoolLimitApply);
	     getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_LIMIT_APPLY, abPoolLimitApply.getId(), null,
	    		abPoolLimitApply.getCorpId(),user.getId());
		abLogService.saveAbLog(abPoolLimitApply.getId(), abPoolLimitApply.getId(), null, 
				AccebillConstant.ABLOG_LOGTYPE_ADD,AbPoolConstant.BILL_LOG_BILLPOOL_LIMIT_APPLY, user);
		reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
		reJson.setSuccess(true);
		return reJson;
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
                .getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POOL_LIMIT_APPLY, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        //调用公共查询当前审批人审批
        DataTablesOutput dp = abPoolLimitApplyDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
	
	@Override
	public BaseDao<AbPoolLimitApplyVO, String> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	  /**
     * 根据流水Id查询申领详细
     *
     * @param id 根据流水Id查询申领详细
     * @return
     */
    public AbPoolLimitApplyVO getAbPoolLimitApplyVoById(String id) {
        if (StringUtils.isNotEmpty(id)) {
            List<AbPoolLimitApplyVO> list = abPoolLimitApplyDao.getAbPoolLimitApplyVoById(id);
			Optional.ofNullable(list).ifPresent(this::redundantValues);
            if (ListUtils.isNotNullOrEmpty(list)) {
                return list.get(0);
            }
        }
        return null;
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
		 AbPoolLimitApply abPoolLimitApply = abPoolLimitApplyDao.get(workFlowResult.getBusinessKey());
         int num = 0;
         if (workFlowResult.isPass()) {
            /** 审批通过 */
            //修改流水表为审批通过,业务完成
            num = abPoolLimitApplyDao.updateabPoolLimitApplyById(abPoolLimitApply.getId(),
            		AbPoolConstant.AB_POOL_STATUS95,AbPoolConstant.AB_POOL_OPT_STATUS16,workFlowResult.getUser());
         } else {
            /** 打回 */
            num = abPoolLimitApplyDao.updateabPoolLimitApplyById(abPoolLimitApply.getId(),AbPoolConstant.AB_POOL_STATUS_1,null,workFlowResult.getUser());
         }
         if (num != 1) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        String logType = workFlowResult.isPass() ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
        abLogService.saveAbLog(abPoolLimitApply.getId(), abPoolLimitApply.getId(), null, logType, appMsg, workFlowResult.getUser());	
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
		   List<CorpVerifyErrorDTO> verifyErrorDTOS=new ArrayList<>();
		   //单位有额度申领申请单，不允许删除！
           int num=abPoolLimitApplyDao.getValidLimitNumByCorpId(corpId);
           if(num>0){
               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                       SysConfigConstant.Module.BILLPOOL,
                       SysConfigConstant.BusType.POOL_LIMIT,
                       multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_POOLLIMIT_ERROR)));
           }
           //单位有额度分配，不允许删除！
           num=abPoolLimitAllotDao.getValidLimitAllotNumByCorpId(corpId);
           if(num>0){
               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                       SysConfigConstant.Module.BILLPOOL,
                       SysConfigConstant.BusType.POOL_LIMIT_ALLO,
                       multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_POOLLIMITALLO_ERROR)));
           }
           //单位有保证金申请单，不允许删除！
           num=abPoolBailDtlDao.getValidBailNumByCorpId(corpId);
           if(num>0){
               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                       SysConfigConstant.Module.BILLPOOL,
                       SysConfigConstant.BusType.POOL_BAIL,
                       multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_POOLBAIL_ERROR)));
           }
           //单位有入池申请单，不允许删除！
           num=abPoolIntoApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_8018);
           if(num>0){
               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                       SysConfigConstant.Module.BILLPOOL,
                       SysConfigConstant.BusType.POOL_MANAGE,
                       multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_POOLMANAGE_ERROR)));
           }
           //单位有票据签收信息，不允许删除！待签收和已签收的都判断,贴现和背书不再判断，票据签收是前提
           num=abPoolSignDao.getValidSiginNumByCorpId(corpId);
           if(num>0){
               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                       SysConfigConstant.Module.BILLPOOL,
                       SysConfigConstant.BusType.POOL_ACCEBILL,
                       multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_POOLASIGN_ERROR)));
           }
           //单位有出票申请信息，不允许删除！！
           num=abPoolIntoApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_001);
           if(num>0){
               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                       SysConfigConstant.Module.BILLPOOL,
                       SysConfigConstant.BusType.POOL_ACCEBILL,
                       multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_POOLACCEBILL_ERROR)));
           }
           //单位有出票融资信息，不允许删除！
           num=abPoolIntoApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_8001);
           if(num>0){
               verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                       SysConfigConstant.Module.BILLPOOL,
                       SysConfigConstant.BusType.POOL_EMIT_FINA,
                       multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_POOLEMITFINA_ERROR)));
           }
		   if(ListUtils.isNotNullOrEmpty(verifyErrorDTOS)){
               return verifyErrorDTOS;
           }
		}
		return null;
	}
}
