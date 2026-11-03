package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.billPool.dao.AbPoolIntoApplyDao;
import com.bytter.bill.domain.billPool.dao.AbPoolPledgeeDao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.entity.AbPoolPledgee;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyVO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: AbPoolIntoApplyService
 * @Description: 入池申请service
 * @date 2020/1/10 17:58:33
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POLL_INTOAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POLL_INTOAPPLY)
@Service("abPoolIntoApplyService")
@Transactional 
public class AbPoolIntoApplyService extends BaseService<AbPoolDtl,String> implements IWorkFlowListener{
    @Autowired
    private AbCorpService corpService; 
	@Autowired
	private AbWorkFlowService workFlowService;
    
    @Autowired
    private AbPoolIntoApplyDao abPoolIntoApplyDao;
    
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
	private AbBisBifInitService abBisBifInitService;
	@Autowired
	private AbPoolDiscountService abPoolDiscountService;
	/**
     * 查询入池信息方法
     * @param
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(AbPoolConstant.AB_POLL_LINTOAPPLY_RESOURCE_OP,
                input.getCurrentUserId());
        return abPoolIntoApplyDao.listAbPoolIntoApply(input,corpAuthList);
    }
    /**
     * 保存入池信息
     * @param user
     * @return
     */
    public AjaxJson saveIntoApple(AbPooIntoBeanVO abPooIntoBeanVO,ICurrentUser user){
    	//qt1 通用验证
        AjaxJson reJson = new AjaxJson();
		if(null==abPooIntoBeanVO | StringUtils.isBlank(abPooIntoBeanVO.getIds())){
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR);
		}
		AbPool  abPool = abPoolService.get(abPooIntoBeanVO.getBpId(),true);
		AbPoolif abPoolif = abPoolifService.get(abPool.getPifId(),true);
		AbPoolPledgee abPoolPledgee = abPoolPledgeeDao.findAbPoolPledgeeByBpId(abPooIntoBeanVO.getBpId());
		if(!StringUtils.isNotEmpty(abPool)
				&&!StringUtils.isNotEmpty(abPoolif)){
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR);
		}
		List<AbAccrecordsVO> executableList = abAccrecordsService.abPoolRecordList(abPooIntoBeanVO.getIds());
		if(!ListUtils.isNotNullOrEmpty(executableList)){
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR);
		}
		
		for(AbAccrecordsVO abRecordVo:executableList){
				if(!AccebillConstant.AB_BILL_VALID_SIGN_1.equals(abRecordVo.getValidSign())){
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}
				//电票内数据验证
				if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecordVo.getBillFlag()))){
	    			//不可转让验证
	    			if(!StringUtils.isBlank(abRecordVo.getBankendrsmtmk()) &&
	    					AccebillConstant.AB_BANEM_N.equals(abRecordVo.getBankendrsmtmk())){
	    				throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_TRANSFER_ERROR);
	    			}
	    			//电票质权人信息未配置
	    			if(null==abPoolPledgee){
	    				throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_PLEDGEE_ERROR);
	    			}
	    			
	    			//非空判断电票
	    			if(StringUtils.isBlank(abPooIntoBeanVO.getBankAcc())
	    					| StringUtils.isBlank(abPoolif.getPifCode())){
	    				throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_NOTNULL_ERROR);
	    			}
	    			//判断是否线上电票
	    			if(AccebillConstant.AB_BILL_PAYEMITSTTLMMK0.equals(abRecordVo.getCirculationMode())){
	    				throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_SENDSIGN_ERROR);
	    			}
	    			//持票人接口代码必须和池接口一致
	    			if(!StringUtils.equals(abRecordVo.getBifCode(), abPoolif.getBifCode())){
	        			throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_BIFCODE_ERROR);
	    			}
	    			//兴业银行电票只支持质押入池
	    			if(AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abRecordVo.getBifCode())
	    					&& !AbPoolConstant.AB_POOL_INOUT_TYPE1.equals(abPooIntoBeanVO.getIntoType())){
	    				throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_INTOTYPE_ERROR);
	    			}
					//中信银行电票只支持质押入池
					if(AbPoolConstant.BIF_CODE_CITIC_9001.equals(abRecordVo.getBifCode())
							&& !AbPoolConstant.AB_POOL_INOUT_TYPE1.equals(abPooIntoBeanVO.getIntoType())){
						throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_CITIC_INTOTYPE_ERROR);
					}
	    			//add: 浙商银行 //浙商银行只支持电票质押入池
	    			if(AbPoolConstant.AB_POOL_INTO_APPLE_CZB.equals(abRecordVo.getBifCode())
	    					&& !AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecordVo.getBillFlag()))
	    					&& !AbPoolConstant.AB_POOL_INOUT_TYPE1.equals(abPooIntoBeanVO.getIntoType())){
	    				throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_PLEDGEE_PMS_ERROR);
	    			}
	    			abPooIntoBeanVO.setAccName(abPoolPledgee.getAccName());
	    			abPooIntoBeanVO.setBankAcc(abPoolPledgee.getBankAcc());
	    			abPooIntoBeanVO.setBankName(abPoolPledgee.getBankName());
	    			abPooIntoBeanVO.setBankNo(abPoolPledgee.getBankNo());
	    		}
				
				
	    		//公共 验证：电票只支持同行入池和在线电票入池且票据是可转让票据
	            //入池申请的时候产生票据池交易AB_POOL_DTL
	            AbPoolDtl abPoolDtl = new AbPoolDtl();
	            abPoolDtl.setVvId(abRecordVo.getVvid());
	            abPoolDtl.setInoutType(abPooIntoBeanVO.getIntoType());//入池方式
	            abPoolDtl.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8018);
	            abPoolDtl.setAppNo(SequenceSingleton.getSequence(SequenceEnum.BILL_POOL_INTO_APPNO));
	            abPoolDtl.setOrigNo(abRecordVo.getOrigNo());
	            abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
	            abPoolDtl.setCreateTime(new Date());
	            abPoolDtl.setCreateId(user.getId());
	            abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
	            abPoolDtl.setBpId(abPooIntoBeanVO.getBpId());
	            abPoolIntoApplyDao.save(abPoolDtl);
	            
	            //生成一笔新的8018的入池申请业务流水
	            AbAccrecords pdAbAccrecords = getAbAccrecordsNew(abPooIntoBeanVO,abRecordVo,user);
	            pdAbAccrecords.setPdId(abPoolDtl.getId());
				//兴业银行 ||  浙商银行 ||
				if(AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolif.getBifCode())
						| AbPoolConstant.AB_POOL_INTO_APPLE_CZB.equals(abPoolif.getBifCode())){
					//电票质押入池
					if(AbPoolConstant.AB_POOL_INOUT_TYPE_1.equals(abPooIntoBeanVO.getIntoType())
						&& AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecordVo.getBillFlag()))){
						pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_018);
					}else{
						pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8018);
					}
				}
				//建设银行、中国银行、中信银行
				if(AbPoolConstant.AB_POOL_INTO_APPLE_9018.equals(abPoolif.getBifCode())
					| AbPoolConstant.BIF_CODE_BOC_9021.equals(abPoolif.getBifCode())
					| AbPoolConstant.BIF_CODE_CITIC_9001.equals(abPoolif.getBifCode())){
					pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8018);
				}
				//农行质押申请判断
				if(AbPoolConstant.AB_POOL_INTO_APPLE_ABC.equals(abPoolif.getBifCode())){
//					if(AbPoolConstant.AB_POOL_INOUT_TYPE_1.equals(abPooIntoBeanVO.getIntoType())){
//						pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_018);
//					}else if(AbPoolConstant.AB_POOL_INOUT_TYPE_2.equals(abPooIntoBeanVO.getIntoType())){
						pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8018);
//					}
				}

				if(pdAbAccrecords.getBriefId() == null){
					pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8018);
				}
				pdAbAccrecords.setStatus(AbPoolConstant.AB_POOL_STATUS11);
				pdAbAccrecords.setBifCode(abPoolif.getBifCode());
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
		    abVoucherinfoViceService.updateBackBriefIdIsNull(abRecordVo.getVvid());
		    //上一笔流水为不可用
	        Map<String,Object> lastRefMap = new HashMap<String,Object>();
	        lastRefMap.put("sid", abRecordVo.getId());
	        lastRefMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
	        abAccrecordsService.updateRecordByPdId(lastRefMap, null,user);
	           
	        //修改从表池状态poolStatus为池外状态
	        abVoucherinfoViceService.updatePoolSatatus(abRecordVo.getVvid(), AbPoolConstant.AB_BILL_POOL_STATUS_0, user);
	        
	        // 启动工作流
	    	 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POLL_INTOAPPLY, abPoolDtl.getId(), null,
	    				abRecordVo.getCorpId(),user.getId());
	    	abLogService.saveAbLog(abRecordVo.getVvid(), abRecordVo.getVvid(), AbPoolConstant.AB_POOL_BRIEF_8018, 
	    				AccebillConstant.ABLOG_LOGTYPE_ADD,
	    				AbPoolConstant.BILL_LOG_BILLPOOL_INTO, user);
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
    public AjaxJson updateOptStatus(String id,Integer opt,ICurrentUser user){
    	boolean flag =AbPoolConstant.OPT_INI_Y.equals(opt == null ? null : opt.toString())?true:false;
    	AjaxJson reJson = new AjaxJson();
		AbPoolDtl abPoolDtl =abPoolIntoApplyDao.get(id,true);
		AbVoucherinfoVice abVoucherinfoVice =abVoucherinfoViceService.get(abPoolDtl.getVvId(),true);
		Map<String,Object> refMap = new HashMap<String,Object>();
		//继续
		if(flag){
			//设置票据池明细表
			refMap.put("rid", abPoolDtl.getId());
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS95);
			refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abPoolIntoApplyDao.updateRecordById(refMap, user);
			
			//当前流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), abPoolDtl.getId(), true);
			refMap.put("sid", recordVO.getId());
			refMap.put("returnCode", AccebillConstant.AB_BILL_RETURN_CODE_3);
//			refMap.put("returnMsg", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abAccrecordsService.updateRecordByPdId(refMap,null,user);
	    //指令终止
		}else{
			//设置票据池明细表状态为【已删除】
			refMap.put("rid", id);
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS_2);
			refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS_3);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
			abPoolIntoApplyDao.updateRecordById(refMap, user);
			
			//当前流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), abPoolDtl.getId(), true);
			refMap.put("sid", recordVO.getId());
			abAccrecordsService.updateRecordByPdId(refMap,null,user);
			
			//上一笔流水
			AbAccrecords lastVO = abAccrecordsDao.get(recordVO.getLastid(),true);
			lastVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		    abAccrecordsService.update(lastVO, user);
			
			//修改从表池状态poolStatus为池外状态
            abVoucherinfoViceService.updatePoolSatatus(recordVO.getVvid(), AbPoolConstant.AB_BILL_POOL_STATUS_0, user);
		}
        abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfoVice.getId(), AbPoolConstant.AB_POOL_BRIEF_8018, 
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
                .getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POLL_INTOAPPLY, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        //调用公共查询当前审批人审批
        DataTablesOutput dp = abPoolIntoApplyDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    
    
    /**
	 * 根据票据主表和流水表生成流水入库数据
	 */
	protected AbAccrecords getAbAccrecordsNew(AbPooIntoBeanVO abPooIntoBeanVO,
			AbAccrecordsVO abAccrecords,ICurrentUser user) {
		AbAccrecords abAccrecordsNew = new AbAccrecords();
		abAccrecordsNew.setBusinessDate(new Date());// 业务归属日期
		abAccrecordsNew.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());// 不得转让标记：EM00
		abAccrecordsNew.setCnAccrecords(abAccrecords.getCnAccrecords());// 前手
		abAccrecordsNew.setAbs(abAccrecords.getAbs());
		abAccrecordsNew.setRmk(abAccrecords.getRmk());
		abAccrecordsNew.setBillFlag(abAccrecords.getBillFlag());// 流通介质
		abAccrecordsNew.setInsideSing(abAccrecords.getInsideSing());
		abAccrecordsNew.setEnterStockOld(abAccrecords.getEnterStockOld());
		abAccrecordsNew.setApplyDate(new Date());
		abAccrecordsNew.setBillId(abAccrecords.getBillId());
		abAccrecordsNew.setReqNm(abAccrecords.getBankAcc());// 业务申请人账号
		abAccrecordsNew.setReqAcctId(abAccrecords.getBankAcc());// 业务申请人账号
		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getBankCode());
		
		abAccrecordsNew.setRecNm(abPooIntoBeanVO.getAccName());// 业务接收人名称
		abAccrecordsNew.setRecAcctId(abPooIntoBeanVO.getBankAcc());// 业务接收人账号
		abAccrecordsNew.setRecAcctsvcr(abPooIntoBeanVO.getBankNo());// 业务接收人开户行号
		
		abAccrecordsNew.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);// 设置流水创建人为空
		
		abAccrecordsNew.setVid(abAccrecords.getVid());
		abAccrecordsNew.setVvid(abAccrecords.getVvid());
		abAccrecordsNew.setLastid(abAccrecords.getId());
		abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
		abAccrecordsNew.setCreateDate(new Date());
		abAccrecordsNew.setCreateBy(user.getUsername());
		abAccrecordsNew.setCreateName(user.getLoginName());
		return abAccrecordsNew;
	}
	
    private void wrapBriefId(AbAccrecords pdAbAccrecords){
    	
    }
    
    @Override
    public BaseDao<AbPoolDtl, String> getEntityDao() {
        return null;
    }
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
	
	/**
	 * 查
	 * @param bpid
	 * @return
	 */
	public AbPoolIntoApplyVO queryPoolIntoVObyId(String bpid){
		return abPoolIntoApplyDao.queryAbPoolIntoApplyVOByBpid(bpid);
	}
	/**
	 * 根据id查询详细
	 * @param id
	 * @return 明细
	 */
	public AbPoolDtl getAbPoolDtlById(String id){
		return abPoolIntoApplyDao.get(id,true);
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		AbPoolDtl abPoolDtl =abPoolIntoApplyDao.get(workFlowResult.getBusinessKey(),true);
		//获取从表信息
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abPoolDtl.getVvId());
		int num = 0;
		if (workFlowResult.isPass()) {
			//处理票据池明细交易
			Map<String,Object> refMap = new HashMap<String,Object>();
			refMap.put("rid", abPoolDtl.getId());
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS95);
			refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abPoolIntoApplyDao.updateRecordById(refMap, workFlowResult.getUser());
     
			//当前流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), abPoolDtl.getId(), true);
			
			//上一笔流水
			AbAccrecords lastVO = abAccrecordsDao.get(recordVO.getLastid(),true);
			abAccrecordsService.updateValidSign(lastVO.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, workFlowResult.getUser());
		    
		    //当前流水修改
			refMap.put("sid", recordVO.getId());
			num = abAccrecordsService.updateRecordByPdId(refMap,null,workFlowResult.getUser());
		} else {
			//处理票据池明细交易
			Map<String,Object> refMap = new HashMap<String,Object>();
			refMap.put("rid", abPoolDtl.getId());
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS_2);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
			abPoolIntoApplyDao.updateRecordById(refMap, workFlowResult.getUser());
			//当前流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), abPoolDtl.getId(), true);
			//上一笔流水
			AbAccrecords lastVO = abAccrecordsDao.get(recordVO.getLastid(),true);
		    abAccrecordsService.updateValidSign(lastVO.getId(), AccebillConstant.AB_BILL_VALID_SIGN_1, workFlowResult.getUser());
		    //设置打回信息
		    abVoucherinfoVice.setBackAcid(recordVO.getId());
		    abVoucherinfoVice.setBackBriefId(AbPoolConstant.AB_POOL_BRIEF_8018);
		    abVoucherinfoViceService.update(abVoucherinfoVice);
		    
		    //当前流水修改
			refMap.put("sid", recordVO.getId());
			num = abAccrecordsService.updateRecordByPdId(refMap,null,workFlowResult.getUser());
		}
		if (num != 1) {
			throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
       abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfoVice.getId(), AbPoolConstant.AB_POOL_BRIEF_8018, 
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
	@Override
	public void flowRevoke(IWorkFlowResult workFlowResult) {
	}
}
