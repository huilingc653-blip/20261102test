package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.dao.AbPoolIntoApplyDao;
import com.bytter.bill.domain.billPool.dao.AbPoolPdTstDao;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolPdTstVO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.system.api.dto.BisBifInitDTO;
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
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
　 * @Title: 质押托管转换Services
　 * @author jy.cao
  * @Description:AbPoolPdTstService.java
　 * @date 2020年3月5日
　 * @version 1.0
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POLL_PDSTS)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POLL_PDSTS)
@Service("abPoolPdTstService")
@Transactional
public class AbPoolPdTstService extends BaseService<AbPoolPdTstVO,String> implements IWorkFlowListener{
	
	@Autowired
	private AbCorpService corpService;
	
	@Autowired
	private AbLogService abLogService;
	
	@Autowired(required = false)
	private AbWorkFlowService workFlowService;
	
	@Autowired
	private AbPoolPdTstDao abPoolPdTstDao;
	
	
	@Autowired
	private AbPoolService abPoolService;
	@Autowired
	private AbPoolifService abPoolifService;
	
	
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    
    @Autowired
    private AbPoolIntoApplyDao abPoolIntoApplyDao;
    
    @Autowired
    private AbAccrecordsDao abAccrecordsDao;
    
    @Autowired
	private AbBisBifInitService abBisBifInitService;
	
	@SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(AbPoolConstant.AB_POLL_PDTST_RESOURCE_OP,
                input.getCurrentUserId());
        return abPoolPdTstDao.listAbPoolTstApply(input,corpAuthList);
    }
	
	/**
     * 指令处理/重发继续
     * @param id
     * @param opt
     * @param user
     * @return
     */
    public AjaxJson updateOptStatus(String id,Integer opt,ICurrentUser user){
    	boolean flag =opt.toString().equals(AbPoolConstant.OPT_INI_Y)?true:false;
    	AjaxJson reJson = new AjaxJson();
		AbPoolDtl abPoolDtl = abPoolIntoApplyDao.get(id,true);
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
			//refMap.put("returnMsg", AccebillConstant.AB_BILL_VALID_SIGN_1);
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
			
			//设置上一笔票据表状态为可用
			Map<String,Object> baseMap = new HashMap<String,Object>();
			baseMap.put("rid", abPoolDtl.getLastid());
			baseMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abPoolIntoApplyDao.updateRecordById(baseMap, user);
			
			//上一笔流水
			Map<String,Object> lastMap = new HashMap<String,Object>();
			lastMap.put("rid", recordVO.getLastid());
			lastMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			lastMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
			abPoolIntoApplyDao.updateRecordById(lastMap, user);
		}
    	reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
        reJson.setSuccess(true);
        return reJson;
    }
	
    /**
     * @param 查询待审批的数据
     * @return
     */
	@SuppressWarnings("unchecked")
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowService
				.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POLL_PDSTS, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		// 调用公共查询当前审批人审批
		@SuppressWarnings("rawtypes")
		DataTablesOutput dp = abPoolPdTstDao.dataTableApprove(dataTablesInput, businessList);
		return workFlowBusiness.merge(dp);
	}
	
	
	/**
     * 执行质押托管转换申请
     * @param abPooIntoBeanVO
     * @return
     */
    public AjaxJson savePdTstApply(AbPooIntoBeanVO abPooIntoBeanVO,ICurrentUser user){
    	AjaxJson reJson = new AjaxJson();
		if (StringUtils.isNotEmpty(abPooIntoBeanVO.getIds())) {
	        String[] ridArray = abPooIntoBeanVO.getIds().split(",");
	        for(String rid:ridArray){
	        	if(!StringUtils.isBlank(rid)){
	        		AbPoolDtl ap = abPoolIntoApplyDao.get(rid,true);
					AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(ap.getVvId(),true);
	        		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abVoucherinfoVice.getVid(),true);
					//当前流水
					AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), ap.getId(), true);
//	        		AbPool  abPool = abPoolService.get(ap.getBpId());
//	        		AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
     	 			if(abVoucherinfo.getBillFlag().toString().equals(AccebillConstant.AB_BILL_FLAG_ELECT)){
	     	 			BtBankAccDTO bankAccInfo=abAccrecordsService.getBtBankAccDTO(abVoucherinfoVice.getBankAcc());
	     	 			String bifCode=	bankAccInfo.getBifCode();
	        			BisBifInitDTO bifInfo=abBisBifInitService.getObjectById(bifCode);
	        			if(null==bifInfo){
	        				throw new BusinessException(AbPoolMessageEnum.AB_POOL_INTO_MESSAGE_SENDSIGN_ERROR);
	        			}
	        			//兴业银行不可转让验证
	        			if(bifInfo.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_9015)){
	        				throw new BusinessException(AbPoolMessageEnum.AB_POOL_TST_MESSAGE_BIFCODE_ERROR);
	        			}
     	 			}
	        		
	                //修改票据池交易明细流水表状态为不可用
	        		Map<String,Object> refMap = new HashMap<String,Object>();
	        		refMap.put("rid", ap.getId());
	        		refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
	        		refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
	        		abPoolIntoApplyDao.updateRecordById(refMap, user);
	        		
	        		//新增转换流水
	                AbPoolDtl abPoolDtl = new AbPoolDtl();
	                abPoolDtl.setVvId(abVoucherinfoVice.getId());
	                if(ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE1)){
	                	abPoolDtl.setInoutType(AbPoolConstant.AB_POOL_INOUT_TYPE2);//质押转托管
		                abPoolDtl.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8118);
	                }else if(ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE2)){
	                	abPoolDtl.setInoutType(AbPoolConstant.AB_POOL_INOUT_TYPE1);//托管转质押
		                abPoolDtl.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8218);
		            }
	                abPoolDtl.setAppNo(abVoucherinfoVice.getAppNo());
	                abPoolDtl.setOrigNo(abVoucherinfo.getOrigNo());
	                abPoolDtl.setCreateTime(new Date());
	                abPoolDtl.setCreateId(user.getId());
	                abPoolDtl.setLastid(ap.getId());
	                abPoolDtl.setAppNo(SequenceSingleton.getSequence(SequenceEnum.BILL_POOL_INTO_APPNO));
	                abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
	                abPoolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
	                abPoolDtl.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
	                abPoolDtl.setBpId(ap.getBpId());
	                abPoolIntoApplyDao.save(abPoolDtl);
	                
	                //新流水
	                AbAccrecords pdAbAccrecords =getAbAccrecordsNew(recordVO);
	                pdAbAccrecords.setPdId(abPoolDtl.getId());
	                pdAbAccrecords.setLastid(recordVO.getId());
	                pdAbAccrecords.setStatus(AbPoolConstant.AB_POOL_STATUS11);
	                pdAbAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
	                pdAbAccrecords.setCreateDate(new Date());
					pdAbAccrecords.setCreateBy(user.getUsername());
					pdAbAccrecords.setCreateName(user.getLoginName());
					pdAbAccrecords.setBriefId(ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE1)
							?AbPoolConstant.AB_POOL_BRIEF_8118:AbPoolConstant.AB_POOL_BRIEF_8218);
					boolean flag =abAccrecordsService.saveAccrecords(pdAbAccrecords,user);
					if(flag){
						///处理票据池明细交易
						Map<String,Object> mstMap = new HashMap<String,Object>();
						mstMap.put("sid", pdAbAccrecords.getId());
						mstMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
						mstMap.put("sendSign", AbPoolConstant.OPT_INI_Y);
						abAccrecordsService.updateRecordByPdId(refMap, null,user);
					}	
	        		
	        		//修改前一笔流水流水表状态为不可用
	        		Map<String,Object> lastRefMap = new HashMap<String,Object>();
	                lastRefMap.put("sid", recordVO.getId());
	                lastRefMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
	                abAccrecordsService.updateRecordByPdId(lastRefMap, null,user);	   
	                
	                //设置打回状态
		            abVoucherinfoViceService.updateBackBriefIdIsNull(abVoucherinfoVice.getId());
		            
	        		// 启动工作流
	        		 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POLL_PDSTS, abPoolDtl.getId(), null,
	                		abVoucherinfoVice.getCorpId(),user.getId());
	        		abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfoVice.getId(), pdAbAccrecords.getBriefId(),
	        				AccebillConstant.ABLOG_LOGTYPE_ADD,
							AbPoolConstant.BILL_LOG_BILLPOOLTST_INTO, user);
	        	}
	        }
    	}
	    reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
        reJson.setSuccess(true);
		return reJson;
    	
    }
	
	
    /**
  	 * 根据票据主表和流水表生成流水入库数据
  	 */
  	protected AbAccrecords getAbAccrecordsNew(AbAccrecords abAccrecords) {
  		AbAccrecords abAccrecordsNew = new AbAccrecords();
  		abAccrecordsNew.setBusinessDate(new Date());// 业务归属日期
//  		abAccrecordsNew.setBankendrsmtmk(StringUtils.isBlank(abAccrecords.getBankendrsmtmk());// 不得转让标记：EM00
  		abAccrecordsNew.setCnAccrecords(abAccrecords.getCnAccrecords());// 前手
  		abAccrecordsNew.setAbs(abAccrecords.getAbs());
  		abAccrecordsNew.setRmk(abAccrecords.getRmk());
  		abAccrecordsNew.setBillFlag(abAccrecords.getBillFlag());// 流通介质
  		abAccrecordsNew.setLastid(abAccrecords.getId());
		abAccrecordsNew.setInsideSing(abAccrecords.getInsideSing());
		abAccrecordsNew.setEnterStockOld(abAccrecords.getEnterStockOld());
		abAccrecordsNew.setApplyDate(new Date());
  		abAccrecordsNew.setReqNm(abAccrecords.getReqNm());// 业务申请人账号
  		abAccrecordsNew.setReqAcctId(abAccrecords.getReqAcctId());// 业务申请人账号
  		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getReqAcctsvcr());
  		
  		abAccrecordsNew.setRecNm(abAccrecords.getRecNm());// 业务接收人名称
  		abAccrecordsNew.setRecAcctId(abAccrecords.getRecAcctId());// 业务接收人账号
  		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());// 业务接收人开户行号
  		
  		abAccrecordsNew.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);// 设置流水创建人为空
  		abAccrecordsNew.setVid(abAccrecords.getVid());
  		abAccrecordsNew.setVvid(abAccrecords.getVvid());
  		return abAccrecordsNew;
  	}
    
    
	
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
		
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
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS_1);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
			abPoolIntoApplyDao.updateRecordById(refMap, workFlowResult.getUser());
			
			//上一笔业务流水状态重置
			Map<String,Object> lstMap = new HashMap<String,Object>();
			lstMap.put("rid", abPoolDtl.getLastid());
			lstMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abPoolIntoApplyDao.updateRecordById(lstMap, workFlowResult.getUser());
			
			
			//当前票据业务流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), abPoolDtl.getId(), true);
			
			//上一笔票据业务流水
			AbAccrecords lastVO = abAccrecordsDao.get(recordVO.getLastid(),true);
		    abAccrecordsService.updateValidSign(lastVO.getId(), AccebillConstant.AB_BILL_VALID_SIGN_1, workFlowResult.getUser());
		    
		    //设置打回信息
		    abVoucherinfoVice.setBackAcid(recordVO.getId());
		    abVoucherinfoVice.setBackBriefId(AbPoolConstant.AB_POOL_BRIEF_8118);
		    abVoucherinfoViceService.update(abVoucherinfoVice);
		    
		    //当前流水修改
			refMap.put("sid", recordVO.getId());
			num = abAccrecordsService.updateRecordByPdId(refMap,null,workFlowResult.getUser());
		}
		if (num != 1) {
			throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfoVice.getId(), null, 
	    		   workFlowResult.isPass() ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE, 
	    				   workFlowResult.isPass() ? AbPoolConstant.BILL_LOG_APP_SUCC : AbPoolConstant.BILL_LOG_APP_BACK, workFlowResult.getUser());	
	}
	
	
	/**
	 * 查询转换流水
	 * @param bpid
	 * @return
	 */
	public List<AbPoolPdTstVO> queryTstInfoList(String vvid,String bpId){
		return abPoolPdTstDao.queryTstInfoList(vvid, bpId);
	}
	
	@Override
	public BaseDao<AbPoolPdTstVO, String> getEntityDao() {
		return abPoolPdTstDao;
	}
}
