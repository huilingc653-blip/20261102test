package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.billPool.dao.AbPoolIntoRepealDao;
import com.bytter.bill.domain.billPool.dao.AbPoolPledgeeDao;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.entity.AbPoolIntoRepeal;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoRepealVO;
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
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: AbPoolIntoRepealService
 * @Description: 入池撤销service
 * @author zhenghj
 * @date 2021/11/08
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POLL_INTOREPEAL)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POLL_INTOREPEAL)
@Service("abPoolIntoRepealService")
@Transactional
public class AbPoolIntoRepealService extends BaseService<AbPoolIntoRepeal,String> implements IWorkFlowListener {
    @Autowired
    private AbCorpService corpService;
	@Autowired
	private AbWorkFlowService workFlowService;
    
    @Autowired
    private AbPoolIntoRepealDao abPoolIntoRepealDao;
    
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    
    @Autowired
    private AbAccrecordsDao abAccrecordsDao;
    
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbPoolService abPoolService;
	@Autowired
	private AbPoolifService abPoolifService;
	
	@Autowired
	private AbPoolPledgeeDao abPoolPledgeeDao;
	
	@Autowired
	private AbPoolIntoApplyService abPoolIntoApplyService;
	@Autowired
	private AbPoolDiscountService abPoolDiscountService;
	/**
     * 查询入池撤销信息方法
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(AbPoolConstant.AB_POLL_INTOREPEAL_RESOURCE_OP,
                input.getCurrentUserId());
        return abPoolIntoRepealDao.listAbPoolIntoRepeal(input,corpAuthList);
    }
    /**
     * 保存入池撤销信息
     * @param user
     * @return
     */
    public AjaxJson saveRepealDtl(AbPooIntoBeanVO abPooIntoBeanVO, ICurrentUser user){
    	//qt1 通用验证
        AjaxJson reJson = new AjaxJson();
		if(null==abPooIntoBeanVO | StringUtils.isBlank(abPooIntoBeanVO.getIds())){
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR);
		}
		List<AbPoolIntoRepealVO> intoRepealVOS = abPoolIntoRepealDao.queryAbPoolIntoRepealVoById(Arrays.asList(abPooIntoBeanVO.getIds().split(",")));
		if(ListUtils.isNotNullOrEmpty(intoRepealVOS)){
			for(AbPoolIntoRepealVO intoRepealVo : intoRepealVOS){
				//入池撤销的时候产生票据池交易AB_POOL_DTL
				AbPoolDtl abPoolDtl = new AbPoolDtl();
				abPoolDtl.setVvId(intoRepealVo.getViceId());
				//入池方式
				abPoolDtl.setInoutType(intoRepealVo.getInoutType());
				abPoolDtl.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8032);
				abPoolDtl.setAppNo(intoRepealVo.getAppNo());
				abPoolDtl.setOrigNo(intoRepealVo.getOrigNo());
				abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
				abPoolDtl.setCreateTime(new Date());
				abPoolDtl.setCreateId(user.getId());
				abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
				abPoolDtl.setBpId(intoRepealVo.getBpId());
				abPoolDtl.setRepealId(intoRepealVo.getId());
				abPoolIntoRepealDao.save(abPoolDtl);
				if(StringUtils.isNotEmpty(intoRepealVo.getAid())){
					//生成一笔新的8032的入池撤销业务流水
					AbAccrecords pdAbAccrecords = getAbAccrecordsNew(intoRepealVo,user);
					pdAbAccrecords.setPdId(abPoolDtl.getId());
					pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8032);
					pdAbAccrecords.setStatus(AbPoolConstant.AB_POOL_STATUS11);
					pdAbAccrecords.setBifCode(intoRepealVo.getBifCode());
					pdAbAccrecords.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
					pdAbAccrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
					if(abAccrecordsService.saveAccrecords(pdAbAccrecords,user)){
						///处理票据池明细交易
						Map<String,Object> refMap = new HashMap<String,Object>();
						refMap.put("sid", pdAbAccrecords.getId());
						refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
						abAccrecordsService.updateRecordByPdId(refMap, null,user);
					}
					//设置打回为空
					abVoucherinfoViceService.updateBackBriefIdIsNull(pdAbAccrecords.getVvid());
					//修改从表池状态poolStatus为入池撤销在途状态
					abVoucherinfoViceService.updatePoolSatatus(pdAbAccrecords.getVvid(), AbPoolConstant.AB_BILL_POOL_STATUS_12, user);
					// 启动工作流
					 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POLL_INTOREPEAL, abPoolDtl.getId(), null,
							intoRepealVo.getCorpId(),user.getId());
					abLogService.saveAbLog(pdAbAccrecords.getVvid(), pdAbAccrecords.getVvid(), AbPoolConstant.AB_POOL_BRIEF_8032,
							AccebillConstant.ABLOG_LOGTYPE_ADD,
							AbPoolConstant.BILL_LOG_BILLPOOL_INTO_REPEAL, user);
				}
			}
		}
        reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
        reJson.setSuccess(true);
        return reJson;
    }
    /**
     * 指令处理/重发继续
     * @param id
     * @param opt
     * @param user
     * @return
     */
    public AjaxJson updateOptStatus(String id, Integer opt, ICurrentUser user){
    	boolean flag =opt.toString().equals(AbPoolConstant.OPT_INI_Y)?true:false;
    	AjaxJson reJson = new AjaxJson();
		AbPoolDtl abPoolDtl =abPoolIntoApplyService.getAbPoolDtlById(id);
		Map<String,Object> refMap = new HashMap<String,Object>();
		//继续
		if(flag){
			//设置票据池明细表
			refMap.put("rid", abPoolDtl.getId());
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS95);
			refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abPoolIntoRepealDao.updateRecordById(refMap, user);
			
			//当前流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abPoolDtl.getVvId(), abPoolDtl.getId(), true);
			refMap.put("sid", recordVO.getId());
			refMap.put("returnCode", AccebillConstant.AB_BILL_RETURN_CODE_3);
			refMap.put("returnMsg", null);
			abAccrecordsService.updateRecordByPdId(refMap,null,user);
	    //指令终止
		}else{
			//设置票据池明细表状态为【已删除】
			refMap.put("rid", id);
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS_2);
			refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS_3);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
			abPoolIntoRepealDao.updateRecordById(refMap, user);
			
			//当前流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abPoolDtl.getVvId(), abPoolDtl.getId(), true);
			refMap.put("sid", recordVO.getId());
			abAccrecordsService.updateRecordByPdId(refMap,null,user);
			//修改从表池状态poolStatus为池外状态
            abVoucherinfoViceService.updatePoolSatatus(recordVO.getVvid(), AbPoolConstant.AB_BILL_POOL_STATUS_0, user);
		}
        abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getVvId(), AbPoolConstant.AB_POOL_BRIEF_8032,
    		   flag ? AccebillConstant.ABLOG_LOGTYPE_UPDATE : AccebillConstant.ABLOG_LOGTYPE_UPDATE,
    				   flag ? AbPoolConstant.LOG_INF_MSG_CONTINUE : AbPoolConstant.LOG_INF_MSG_STOP, user);
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
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POLL_INTOREPEAL, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        //调用公共查询当前审批人审批
        DataTablesOutput dp = abPoolIntoRepealDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
	/**
	 * 生成流水撤销数据
	 */
	protected AbAccrecords getAbAccrecordsNew(AbPoolIntoRepealVO intoRepealVo, ICurrentUser user) {
		AbAccrecords abAccrecords = abAccrecordsService.get(intoRepealVo.getAid());
		AbAccrecords abAccrecordsNew = new AbAccrecords();
		// 业务归属日期
		abAccrecordsNew.setBusinessDate(new Date());
		// 不得转让标记：EM00
		abAccrecordsNew.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
		// 前手
		abAccrecordsNew.setCnAccrecords(abAccrecords.getCnAccrecords());
		abAccrecordsNew.setAbs(abAccrecords.getAbs());
		abAccrecordsNew.setRmk(abAccrecords.getRmk());
		// 流通介质
		abAccrecordsNew.setBillFlag(abAccrecords.getBillFlag());
		abAccrecordsNew.setInsideSing(abAccrecords.getInsideSing());
		abAccrecordsNew.setEnterStockOld(abAccrecords.getEnterStockOld());
		abAccrecordsNew.setApplyDate(new Date());
		abAccrecordsNew.setBillId(abAccrecords.getBillId());
		// 业务申请人账号
		abAccrecordsNew.setReqNm(intoRepealVo.getBankAcc());
		// 业务申请人账号
		abAccrecordsNew.setReqAcctId(intoRepealVo.getBankAcc());
		abAccrecordsNew.setReqAcctsvcr(intoRepealVo.getBankCode());
		// 业务接收人名称
		abAccrecordsNew.setRecNm(abAccrecords.getRecNm());
		// 业务接收人账号
		abAccrecordsNew.setRecAcctId(abAccrecords.getRecAcctId());
		// 业务接收人开户行号
		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());
		// 设置流水创建人为空
		abAccrecordsNew.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);
		abAccrecordsNew.setVid(abAccrecords.getVid());
		abAccrecordsNew.setVvid(abAccrecords.getVvid());
		abAccrecordsNew.setLastid(abAccrecords.getId());
		abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
		abAccrecordsNew.setCreateDate(new Date());
		abAccrecordsNew.setCreateBy(user.getUsername());
		abAccrecordsNew.setCreateName(user.getLoginName());
		return abAccrecordsNew;
	}
    @Override
    public BaseDao<AbPoolIntoRepeal, String> getEntityDao() {
        return null;
    }
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
	
	/**
	 * 根据id查询详细
	 * @param rid 入池撤销id
	 * @return 明细
	 */
	public AbPoolIntoRepealVO queryAbPoolIntoRepealVoById(String rid){
		if(StringUtils.isNotEmpty(rid)){
			List<String> rids = new ArrayList<>();
			rids.add(rid);
			List<AbPoolIntoRepealVO> listInfo = abPoolIntoRepealDao.queryAbPoolIntoRepealVoById(rids);
			return ListUtils.isNotNullOrEmpty(listInfo)?listInfo.get(0):null;
		}
		return null;
	}
	/**
	 * 根据apdId查询详细
	 * @param apdId 入池撤销id
	 * @return 明细
	 */
	public AbPoolIntoRepealVO queryAbPoolIntoRepealVoByApdId(String apdId){
		if(StringUtils.isNotEmpty(apdId)){
			List<String> apdIds = new ArrayList<>();
			apdIds.add(apdId);
			List<AbPoolIntoRepealVO> listInfo = abPoolIntoRepealDao.queryAbPoolIntoRepealVoByApdId(apdIds);
			return ListUtils.isNotNullOrEmpty(listInfo)?listInfo.get(0):null;
		}
		return null;
	}
	
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		AbPoolIntoRepealVO abPoolIntoRepealVo = this.queryAbPoolIntoRepealVoByApdId(workFlowResult.getBusinessKey());
		//获取从表信息
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abPoolIntoRepealVo.getViceId());
		int num = 0;
		if (workFlowResult.isPass()) {
			//处理票据池明细交易
			Map<String,Object> refMap = new HashMap<String,Object>();
			refMap.put("rid", workFlowResult.getBusinessKey());
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS95);
			refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abPoolIntoRepealDao.updateRecordById(refMap, workFlowResult.getUser());
     
			//当前流水
			//AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), workFlowResult.getBusinessKey(), true);
		    //当前流水修改
			refMap.put("sid", abPoolIntoRepealVo.getAccId());
			num = abAccrecordsService.updateRecordByPdId(refMap,null,workFlowResult.getUser());
		} else {
			//处理票据池明细交易
			Map<String,Object> refMap = new HashMap<String,Object>();
			refMap.put("rid", workFlowResult.getBusinessKey());
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS_2);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
			abPoolIntoRepealDao.updateRecordById(refMap, workFlowResult.getUser());
			//当前流水
			//AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), abPoolIntoRepealVo.getId(), true);
		    //设置打回信息
		    abVoucherinfoVice.setBackAcid(abPoolIntoRepealVo.getAccId());
		    abVoucherinfoVice.setBackBriefId(AbPoolConstant.AB_POOL_BRIEF_8032);
		    abVoucherinfoViceService.update(abVoucherinfoVice);
		    
		    //当前流水修改
			refMap.put("sid", abPoolIntoRepealVo.getAccId());
			num = abAccrecordsService.updateRecordByPdId(refMap,null,workFlowResult.getUser());
		}
		if (num != 1) {
			throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
       abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfoVice.getId(), AbPoolConstant.AB_POOL_BRIEF_8032,
    		   workFlowResult.isPass() ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE,
    				   workFlowResult.isPass() ? AbPoolConstant.BILL_LOG_APP_SUCC : AbPoolConstant.BILL_LOG_APP_BACK, workFlowResult.getUser());
	}
	/**
	 * 根据pdId获取票据池交易明细数据最新生成的lasid
	 *
	 * @param pdId 票据池交易明细id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbPoolDtl getAbPoolDtlByLastPdId(String pdId) {
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isNotEmpty(pdId)) {
			params.put("lastId", pdId);
			return abPoolDiscountService.getAbPoolDtlByParams(params);
		}
		return null;
	}
}
