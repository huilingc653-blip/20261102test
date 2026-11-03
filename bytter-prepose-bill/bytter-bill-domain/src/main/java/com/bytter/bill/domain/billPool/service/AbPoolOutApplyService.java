package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.billPool.dao.AbPoolIntoApplyDao;
import com.bytter.bill.domain.billPool.dao.AbPoolOutApplyDao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolOutApplyVO;
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
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
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
　 * @Title: 出池申请service类
　 * @author jy.cao
  * @Description:AbPoolOutApplyService.java
　 * @date 2020年2月28日
　 * @version 1.0
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POLL_OUTAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POLL_OUTAPPLY)
@Service("abPoolOutApplyService")
@Transactional
public class AbPoolOutApplyService  extends BaseService<AbPoolOutApplyVO,String> implements IWorkFlowListener{
	
	
	@Autowired
	private AbPoolOutApplyDao abPoolOutApplyDao; 
	
	@Autowired
	private IWorkFlowService workFlowService;
	
	@Autowired
	private AbCorpService corpService;
	
	@Autowired
	private AbLogService abLogService;
	
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
	
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(AbPoolConstant.AB_POLL_OUTAPPLY_RESOURCE_OP,
                input.getCurrentUserId());
        return abPoolOutApplyDao.listAbPoolOutApply(input,corpAuthList);
    }
    
    /** 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(readOnly = true)
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowService
				.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POLL_OUTAPPLY, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		// 调用公共查询当前审批人审批
		DataTablesOutput dp = abPoolOutApplyDao.dataTableApprove(dataTablesInput, businessList);
		return workFlowBusiness.merge(dp);
	}
    
    /**
     * 执行出池申请
     * @param abPooIntoBeanVO
     * @return
     */
    public AjaxJson saveOutApply(AbPooIntoBeanVO abPooIntoBeanVO,ICurrentUser user){
    	AjaxJson reJson = new AjaxJson();
		if(StringUtils.isNotEmpty(abPooIntoBeanVO.getIds())) {
			String[] ridArray = abPooIntoBeanVO.getIds().split(",");
			for (String rid : ridArray) {
				if (!StringUtils.isBlank(rid)) {
					AbPoolDtl ap = abPoolIntoApplyDao.get(rid,true);
					AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(ap.getVvId(),true);
	        		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abVoucherinfoVice.getVid(),true);
					//当前流水
					AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), ap.getId(), true);
	        		AbPool  abPool = abPoolService.get(ap.getBpId());
	        		AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
	        		
	        		
	        		 //修改票据池交易明细流水表状态为不可用
	        		Map<String,Object> refMap = new HashMap<String,Object>();
	        		refMap.put("rid", ap.getId());
	        		refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
	        		abPoolIntoApplyDao.updateRecordById(refMap, user);
	        		
	        		
	                //入池申请的时候产生票据池交易AB_POOL_DT
	                AbPoolDtl abPoolDtl = new AbPoolDtl();
	                abPoolDtl.setVvId(recordVO.getVvid());
	                abPoolDtl.setInoutType(ap.getInoutType());//默认之前的入池方式到出池
	                abPoolDtl.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);//出池申请
	                abPoolDtl.setAppNo(SequenceSingleton.getSequence(SequenceEnum.BILL_POOL_INTO_APPNO));
	                abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
	                abPoolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
	                abPoolDtl.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
	                abPoolDtl.setOrigNo(abVoucherinfo.getOrigNo());
	                abPoolDtl.setCreateTime(new Date());
	                abPoolDtl.setCreateId(user.getId());
	                abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
	                abPoolDtl.setLastid(ap.getId());
	                abPoolDtl.setBpId(ap.getBpId());//默认之前的票据池id
	                abPoolIntoApplyDao.save(abPoolDtl);
	                
					//保存新业务流水
					AbAccrecords pdAbAccrecords = getAbAccrecordsNew(recordVO,user);
					//兴业银行、浙商银行、中国银行
					if(abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_9015)
							| abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_CZB)){
						if(abVoucherinfo.getBillFlag().toString().equals(AccebillConstant.AB_BILL_FLAG_PAPER)){
							pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
						}else if(abVoucherinfo.getBillFlag().toString().equals(AccebillConstant.AB_BILL_FLAG_ELECT)
								&&ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE1)){
							pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_019);
						}else{
							pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
						}
					}else if(abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_9018)
							| abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_ABC)
							){
						//判断入池方式是质押
						if(ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE1)){
							pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_019);
						}else if(ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE2)){
							pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
						}
					}else if(abPoolif.getBifCode().equals(AbPoolConstant.BIF_CODE_CITIC_9001)
							| abPoolif.getBifCode().equals(AbPoolConstant.BIF_CODE_BOC_9021)){
						//中信银行出池为电票质押出池
						pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
					}else{
						pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
					}
					pdAbAccrecords.setPdId(abPoolDtl.getId());
	                pdAbAccrecords.setLastid(recordVO.getId());
	                pdAbAccrecords.setStatus(AbPoolConstant.AB_POOL_STATUS11);
	                pdAbAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
	                pdAbAccrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
	                pdAbAccrecords.setBifCode(abPoolif.getBifCode());
	                pdAbAccrecords.setCreateDate(new Date());
					pdAbAccrecords.setCreateBy(user.getUsername());
					pdAbAccrecords.setCreateName(user.getLoginName());
					boolean flag =abAccrecordsService.saveAccrecords(pdAbAccrecords,user);
					if(flag){
						///处理票据池明细交易
						Map<String,Object> mstMap = new HashMap<String,Object>();
						mstMap.put("sid", pdAbAccrecords.getId());
						mstMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
						mstMap.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
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
					 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POLL_OUTAPPLY, abPoolDtl.getId(), null,
							abVoucherinfoVice.getCorpId(), user.getId());
					abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfoVice.getId(), null, 
	        				AccebillConstant.ABLOG_LOGTYPE_ADD,
	        				AbPoolConstant.BILL_LOG_BILLPOOL_OUT, user);
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
   	protected AbAccrecords getAbAccrecordsNew(AbAccrecords abAccrecords,ICurrentUser user) {
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
//   		abAccrecordsNew.setReqNm(abAccrecords.getRecNm());//
//   		abAccrecordsNew.setReqAcctId(abAccrecords.getRecAcctId());
//   		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getRecAcctsvcr());
//
//   		abAccrecordsNew.setRecNm(abAccrecords.getReqNm());// 业务接收人名称
//   		abAccrecordsNew.setRecAcctId(abAccrecords.getReqAcctId());// 业务接收人账号
//   		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getReqAcctsvcr());// 业务接收人开户行号
   		abAccrecordsNew.setReqNm(abAccrecords.getReqNm());//
   		abAccrecordsNew.setReqAcctId(abAccrecords.getReqAcctId());
   		abAccrecordsNew.setReqAcctsvcr(abAccrecords.getReqAcctsvcr());
   		abAccrecordsNew.setRecNm(abAccrecords.getRecNm());// 业务接收人名称
   		abAccrecordsNew.setRecAcctId(abAccrecords.getRecAcctId());// 业务接收人账号
   		abAccrecordsNew.setRecAcctsvcr(abAccrecords.getRecAcctsvcr());// 业务接收人开户行号
   		
   		abAccrecordsNew.setIsCreateByNull(AccebillConstant.AB_BILL_ISCREATEBY_NULL);// 设置流水创建人为空
  		abAccrecordsNew.setVid(abAccrecords.getVid());
  		abAccrecordsNew.setVvid(abAccrecords.getVvid());
   		return abAccrecordsNew;
   	}
    
    
    /**
     * 指令处理/重发继续
     * @param id
     * @param opt
     * @param user
     * @return
     */
	public AjaxJson updateOptStatus(String id, Integer opt, ICurrentUser user) {
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
			
			//设置上一笔票据表状态为可用
			Map<String,Object> baseMap = new HashMap<String,Object>();
			baseMap.put("rid", abPoolDtl.getLastid());
			baseMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			baseMap.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
			abPoolIntoApplyDao.updateRecordById(baseMap, user);
			
			//上一笔流水为可用
			AbAccrecords lastVO = abAccrecordsDao.get(recordVO.getLastid(),true);
			lastVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			lastVO.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
//		    abAccrecordsService.update(lastVO, user);
		    abAccrecordsService.updateEntity(lastVO);
		}
    	reJson.setMessageEnum(AbPoolMessageEnum.AB_POOL_MESSAGE_SUCCESS);
        reJson.setSuccess(true);
        return reJson;
	}
    
    /**
	 * 查询出池信息
	 * @param bpid
	 * @return
	 */
	public AbPoolOutApplyVO queryPoolOutVObyId(String bpid){
		AbPoolOutApplyVO abPoolOutApplyVO =abPoolOutApplyDao.queryAbPoolOutApplyVOByBpid(bpid);
		AbAccrecords abAccrecords =abAccrecordsDao.getBillByPdIdAndVvId(abPoolOutApplyVO.getViceId(), abPoolOutApplyVO.getId(), true);
		abPoolOutApplyVO.setAccId(abAccrecords.getId());
	    return abPoolOutApplyVO;
	}
	/**
	 * 出池申请引用
	 * @param beanVo 出池信息
	 * @param  user 操作用户
	 * @param  flag 是否审批通过
	 */
	public void outPoolRefFegin(AbPooIntoBeanVO beanVo,ICurrentUser user, boolean flag){
		AbVoucherinfoVice vice = abVoucherinfoViceService.get(beanVo.getVvid(),true);
		AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(vice.getVid(),true);
		AbPoolDtl ad = abPoolIntoApplyDao.get(beanVo.getPdId());
		AbPool  abPool = abPoolService.get(ad.getBpId());
		AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
		AbPoolDtl ap = abPoolIntoApplyDao.get(ad.getLastid());
		//兴业银行 || 建设银行
		if(abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_9015)
				| abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_9018)
				| abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_CZB)
				| abPoolif.getBifCode().equals(AbPoolConstant.AB_POOL_INTO_APPLE_ABC)
				|AbPoolConstant.BIF_CODE_CITIC_9001.equals(abPoolif.getBifCode())
				|AbPoolConstant.BIF_CODE_BOC_9021.equals(abPoolif.getBifCode())){
			//生成新的业务流水
			AbAccrecords pdAbAccrecords = new AbAccrecords();	
			pdAbAccrecords.setVid(vice.getVid());
			pdAbAccrecords.setVvid(vice.getId());
			pdAbAccrecords.setBankendrsmtmk(beanVo.getBanendrsmtmk());
			//是否审批通过
			if(flag){
				pdAbAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志
				pdAbAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态
			}else{
				pdAbAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
				pdAbAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态
			}
			pdAbAccrecords.setReqAcctId(vice.getBankAcc());
			pdAbAccrecords.setRecNm(beanVo.getRecNm());// 业务接收人信息
	   		pdAbAccrecords.setRecAcctId(beanVo.getRecAcctId());
	   		pdAbAccrecords.setRecAcctsvcr(beanVo.getRecAcctsvcr());
			//根据票据从表id和票据池交易明细上一笔id查询流水信息
			AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(beanVo.getVvid(), beanVo.getLastPdId());
			if (accrecords != null) {
				pdAbAccrecords.setLastid(accrecords.getId());
			}
	   		
			if(AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abVoucherinfo.getBillFlag()))){
				//判断入池方式是质押
				if(ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE1)){
					pdAbAccrecords.setBriefId(AccebillConstant.AB_BUSTYPE_PLEDGE_RELIEVE);
				}else if(ap.getInoutType().equals(AbPoolConstant.AB_POOL_INOUT_TYPE2)){
					pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
				}
			}else{
				pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
			}
			if(AbPoolConstant.BIF_CODE_CITIC_9001.equals(abPoolif.getBifCode())
					||AbPoolConstant.BIF_CODE_BOC_9021.equals(abPoolif.getBifCode())){
				pdAbAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
			}
			pdAbAccrecords.setApplyDate(new Date());
			pdAbAccrecords.setBifCode(abPoolif.getBifCode());
			pdAbAccrecords.setCreateDate(new Date());
			pdAbAccrecords.setCreateBy(user.getUsername());
			pdAbAccrecords.setCreateName(user.getLoginName());
			pdAbAccrecords.setPdId(beanVo.getPdId());
			boolean flagAcc =abAccrecordsService.saveAccrecords(pdAbAccrecords,user);
			if(flagAcc){
				///处理票据池明细交易
				Map<String,Object> refMap = new HashMap<String,Object>();
				refMap.put("sid", pdAbAccrecords.getId());
				refMap.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS16);
				refMap.put("sendSign", AbPoolConstant.OPT_INI_Y);
				abAccrecordsService.updateRecordByPdId(refMap, null,user);
			}
		}else{
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR);
		}
	}
    
	@Override
	public BaseDao<AbPoolOutApplyVO, String> getEntityDao() {
		// TODO Auto-generated method stub
		return abPoolOutApplyDao;
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
			refMap.put("status", AbPoolConstant.AB_POOL_STATUS_2);
			refMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
			abPoolIntoApplyDao.updateRecordById(refMap, workFlowResult.getUser());
			
			
			//设置上一笔票据表状态为可用
			Map<String,Object> baseMap = new HashMap<String,Object>();
			baseMap.put("rid", abPoolDtl.getLastid());
			baseMap.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
			abPoolIntoApplyDao.updateRecordById(baseMap, workFlowResult.getUser());
			
			//当前流水
			AbAccrecords recordVO =abAccrecordsDao.getBillByPdIdAndVvId(abVoucherinfoVice.getId(), abPoolDtl.getId(), true);
			
			//上一笔流水
			AbAccrecords lastVO = abAccrecordsDao.get(recordVO.getLastid(),true);
		    abAccrecordsService.updateValidSign(lastVO.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, workFlowResult.getUser());
		    //设置打回信息
		    abVoucherinfoVice.setBackAcid(recordVO.getId());
		    abVoucherinfoVice.setBackBriefId(AbPoolConstant.AB_POOL_BRIEF_8019);
		    abVoucherinfoViceService.update(abVoucherinfoVice);
		    
		    //当前票据流水表修改
		    baseMap.put("sid", recordVO.getId());
			num = abAccrecordsService.updateRecordByPdId(baseMap,null,workFlowResult.getUser());
		}
		if (num != 1) {
			throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
       abLogService.saveAbLog(abVoucherinfoVice.getId(), abVoucherinfoVice.getId(), AbPoolConstant.AB_POOL_BRIEF_8019, 
    		   workFlowResult.isPass() ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE, 
    				   workFlowResult.isPass() ? AbPoolConstant.BILL_LOG_APP_SUCC : AbPoolConstant.BILL_LOG_APP_BACK, workFlowResult.getUser());	
	}
}
