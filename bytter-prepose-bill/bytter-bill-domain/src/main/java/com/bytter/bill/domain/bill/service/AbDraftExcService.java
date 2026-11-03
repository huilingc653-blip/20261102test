package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.domain.bill.entity.*;
import com.bytter.bill.domain.bill.vo.AbBillCollectionApplyVo;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.bytter.bill.domain.bill.dao.AbDraftExcDao;
import com.bytter.bill.domain.bill.dao.AbPaymentCreditDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.bill.domain.bill.vo.AbPaymentCreditVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("abDraftExcService")
@Transactional
public class AbDraftExcService extends BaseService<AbDraftExc, String> implements ITaskService {
	@Autowired
	private AbDraftExcDao abDraftExcDao;
	
	@Autowired
	private AbVoucherinfoDao abVoucherinfoDao;
	
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	
	@Autowired
	private AbBisBifInitService abBisBifInitService;
	
	@Autowired
	private AbBillReceivebizService abBillReceivebizService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbUserService abUserService;
	@Autowired
	private AbPaymentCreditDao abPaymentCreditDao;

    @Autowired
    private AbBillCollectionApplyService abBillCollectionApplyService;
	/**
	 * 根据票据号获取支付信用数据
	 * @param origNo 票据主表id
	 * @return 支付信用数据
	 */
	public AbPaymentCreditVo getAbPaymentCreditVoByOrigNo(String origNo){
		if(StringUtils.isNotEmpty(origNo)){
			AbPaymentCredit paymentCredit = abPaymentCreditDao.getAbPaymentCreditVoByOrigNo(origNo);
			if(paymentCredit!=null){
				AbPaymentCreditVo paymentCreditVo = new AbPaymentCreditVo();
				BeanUtils.copyNotNullProperties(paymentCredit, paymentCreditVo);
				return paymentCreditVo;
			}else{
				return null;
			}
		}
		return null;
	}
	public AjaxJson creditInformation(String billCode,String acctId,String acId,String msgType){
		int msg=0;
		String message=null; 
		AjaxJson json = new AjaxJson();
		boolean flag=false;
		Map<String, Object> messMsg = new HashMap<String, Object>();
		try{
			if(!StringUtils.isBlank(msgType)){
				if(AccebillConstant.MSG_TYPE_PAYMENTCREDIT.equals(msgType)){
					msg = this.addEDraftExcByParam(billCode, acctId ,acId, AccebillConstant.MSG_TYPE_PAYMENTCREDIT);
				}else if(AccebillConstant.MSG_TYPE_BACKINFO.equals(msgType)){
					msg = this.addEDraftExcByParam(billCode, acctId ,acId, AccebillConstant.MSG_TYPE_BACKINFO);
				}else if(AccebillConstant.MSG_TYPE_BILLINFO.equals(msgType)){
					msg = this.addEDraftExcByParam(billCode, acctId ,acId, AccebillConstant.MSG_TYPE_BILLINFO);
				}
				
				if(msg==0){
					message = AccebillConstant.AB_DRAFT_EXC_SEND_sWAIT_MSG;
				}else if(msg==-1){
					message = "nis.netbank.message.bankaccinfo.null";
				}else{
					message = AccebillConstant.AB_DRAFT_EXC_SEND_SUCESS_MSG;
					flag=true;
				}
			}else{
				message = AccebillConstant.AB_DRAFT_EXC_MSGTYPE_MSG;
			}
			messMsg.put("MSG_INFO",message);
			json.setAttributes(messMsg);
			json.setSuccess(flag);
		}catch(Exception e){
			messMsg.put("MSG_INFO", AccebillConstant.AB_DRAFT_EXC_ERROR_MSG);
			json.setSuccess(flag);
			json.setAttributes(messMsg);
			return json;
		}
		return json;
	}
	public AjaxJson creditInformationSign(String billCode,String acctId,String acId,String recId,String msgType){
		int msg=0;
		String message=null;
		AjaxJson json = new AjaxJson();
		boolean flag=false;
		Map<String, Object> messMsg = new HashMap<String, Object>();
		try{
			if(!StringUtils.isBlank(msgType)){
				if(AccebillConstant.MSG_TYPE_PAYMENTCREDIT.equals(msgType)){
					msg = this.addEDraftExcByParam(billCode, acctId ,null,recId, AccebillConstant.MSG_TYPE_PAYMENTCREDIT);
				}else if(AccebillConstant.MSG_TYPE_BACKINFO.equals(msgType)){
					msg = this.addEDraftExcByParam(billCode, acctId ,null, recId,AccebillConstant.MSG_TYPE_BACKINFO);
				}else if(AccebillConstant.MSG_TYPE_BILLINFO.equals(msgType)){
					msg = this.addEDraftExcByParam(billCode, acctId ,null, recId,AccebillConstant.MSG_TYPE_BILLINFO);
				}
				if(msg==0){
					message = AccebillConstant.AB_DRAFT_EXC_SEND_sWAIT_MSG;
				}else if(msg==-1){
					message = "nis.netbank.message.bankaccinfo.null";
				}else{
					message = AccebillConstant.AB_DRAFT_EXC_SEND_SUCESS_MSG;
					flag=true;
				}
			}else{
				message = AccebillConstant.AB_DRAFT_EXC_MSGTYPE_MSG;
			}
			messMsg.put("MSG_INFO",message);
			json.setAttributes(messMsg);
			json.setSuccess(flag);
		}catch(Exception e){
			messMsg.put("MSG_INFO", AccebillConstant.AB_DRAFT_EXC_ERROR_MSG);
			json.setSuccess(flag);
			json.setAttributes(messMsg);
			return json;
		}
		return json;
	}
	
	/**
	 * 
	 * 根据票据号+账号 查询状态不等于（-1，19）时数据为空时则新增指令，状态为16，有数据则不新增
	 */
	public int addEDraftExcByParam(String origNo,String payerAcc,String acId,String msgType) {
		return addEDraftExcByParam(origNo, payerAcc, acId, null, msgType);
	}
	
	/**
	 * 
	 * 根据票据号+账号 查询状态不等于（-1，19）时数据为空时则新增指令，状态为16，有数据则不新增
	 */
	public int addEDraftExcByParam(String origNo,String payerAcc,String acId, String signId,String msgType) {
        AbVoucherinfo voucherinfo = null;
        List<AbDraftExc> eDraftExcList = null;
        AbBillReceivebiz info=null;
        //如果票据流水不为空则根据流水表获取票据号和所属账号以及签收表ID
        if (StringUtils.isNotEmpty(acId)) {
            AbAccrecords accrecords = abAccrecordsService.get(acId, true);
            if (accrecords != null && StringUtils.isNotEmpty(accrecords.getId())) {
                voucherinfo = abVoucherinfoDao.get(accrecords.getVid(), true);
                if (voucherinfo != null && StringUtils.isNotEmpty(voucherinfo.getId())) {
                    origNo = voucherinfo.getOrigNo();
                }
                payerAcc = accrecords.getReqAcctId();
                signId = accrecords.getRecid();
            }
        } else if (StringUtils.isNotEmpty(signId)) {
            info = abBillReceivebizService.get(signId, true);
            abBillReceivebizService.setBillReceivebizBankName(info, null);
            payerAcc=info.getRecAcctId();
        }
        //票据号和所属账号为空的做银行同步
        if (StringUtils.isEmpty(origNo) || StringUtils.isEmpty(payerAcc)) {
            return -1;
        }
        int msg = 0;
        String sbbllRngStrtSn=null;
        String sbbllRngEndSn=null;
        if(voucherinfo!=null){
            sbbllRngStrtSn=voucherinfo.getSbbllRngStrtSn();
            sbbllRngEndSn=voucherinfo.getSbbllRngEndSn();
        }else if(info!=null){
            sbbllRngStrtSn=info.getSbbllRngStrtSn();
            sbbllRngEndSn=info.getSbbllRngEndSn();
        }
        eDraftExcList = abDraftExcDao.queryAbDraftExcByParam(origNo, payerAcc, msgType, sbbllRngStrtSn, sbbllRngEndSn);
//        if (StringUtils.isNotEmpty(signId)) {
//            eDraftExcList = abDraftExcDao.queryAbDraftExcByParam(origNo, payerAcc, msgType, info.getSbbllRngStrtSn(), info.getSbbllRngEndSn());
//        } else {
//            eDraftExcList = abDraftExcDao.queryAbDraftExcByParam(origNo, payerAcc, msgType, voucherinfo.getSbbllRngStrtSn(), voucherinfo.getSbbllRngEndSn());
//        }
        //根据账号获得银行接口代码
        String bifCode = this.getBankBifCodeByBankAcc(payerAcc);
        if (StringUtils.isBlank(bifCode)) {
            return -1;
        }
        if (eDraftExcList.size() == 0) {
            AbDraftExc eDraftExc = new AbDraftExc();
            eDraftExc.setPayerAcc(payerAcc);//持票人账号
            eDraftExc.setBifCode(bifCode);//银行接口代码
            eDraftExc.setOrigNo(origNo);//票据号
            if(voucherinfo==null){
                eDraftExc.setSbbllRngStrtSn(info.getSbbllRngStrtSn());
                eDraftExc.setSbbllRngEndSn(info.getSbbllRngEndSn());
            }else {
                eDraftExc.setSbbllRngStrtSn(voucherinfo.getSbbllRngStrtSn());
                eDraftExc.setSbbllRngEndSn(voucherinfo.getSbbllRngEndSn());
            }
            eDraftExc.setMsgType(msgType);//消息类型
            eDraftExc.setRecordId(acId);
            eDraftExc.setSignId(signId);
            this.add(eDraftExc);
            msg = 1;
		} else {
			for(AbDraftExc exc :eDraftExcList){
				exc.setStatus(16);//状态 16待发送
				exc.setOperDate(new Date());//操作日期
				exc.setPayerAcc(payerAcc);//持票人账号
				exc.setBifCode(bifCode);//银行接口代码
				exc.setMsgType(msgType);//消息类型
				exc.setRecordId(acId);
				exc.setSignId(signId);
				exc.setReturnCode("");
			}
			this.batchUpdate(eDraftExcList);
			msg=eDraftExcList.size();
		}
		return msg;
	}
	
	
    /**
     * 新增指令表数据
     *
     * @param abDraftExc
     */
    public void add(AbDraftExc abDraftExc) {
        abDraftExc.setOperDate(new Date());//操作日期
        abDraftExc.setStatus(16);//状态 16待发送
        if (!StringUtils.isBlank(abDraftExc.getOrigNo()) && abDraftExc.getVoucherId() == null) {
            AbVoucherinfo voucherInfo = abVoucherinfoDao.getAbVoucherinfoByOrigNo(abDraftExc.getOrigNo(), abDraftExc.getSbbllRngStrtSn(), abDraftExc.getSbbllRngEndSn());
            if (voucherInfo != null) {
                abDraftExc.setVoucherId(voucherInfo.getId());
            }
        }
        abDraftExcDao.save(abDraftExc);
    }
	
	
	public String getBankBifCodeByBankAcc(String bankAcc) throws BusinessException {
		try {
			BtBankAccDTO bankAccDTO = abAccrecordsService.getBtBankAccInfo(bankAcc);
			
			if(bankAccDTO!=null){
				//必须是线上账号才需要发送指令查询正面和背面
				if("1".equals(bankAccDTO.getCirculationMode())){
					String bifCode = bankAccDTO.getBifCode();
					if (!StringUtils.isBlank(bifCode)) {
						BisBifInitDTO bifInfo = abBisBifInitService.getObjectById(bifCode);
						return bifInfo.getBifCode();
					}
				}
			}
		} catch (BusinessException e) {
			return null;
		}
		return null;
	}
	
	
	public int getIsSendSign(String vid){
		return abDraftExcDao.getIsSendSign(vid);
	}
	
	@Override
	public BaseDao<AbDraftExc, String> getEntityDao() {
		return abDraftExcDao;
	}
	@Override
	@XxlJob("AbDraftExcService")
	public void run(String arguments) {
		arguments = XxlJobHelper.getJobParam();
		logger.info("定时任务接收参数：【{}】", arguments);
		//追加自动产生正面信息和背面信息定时任务
		if(AccebillConstant.ACCEBILL_TASK_SIGNBILLINFO.equals(arguments)){
//			1、待签收数据（未签收、打回）数据自动查询正面信息然后根据正面信息状态不为待签收则自动变为撤回
            autoQueryBillInfoBySign();
        } else if (AccebillConstant.ACCEBILL_TASK_HOLDBILLINFO.equals(arguments)) {
//			2、应付未结清票据和应收持有票据自动查询正面信息和背面信息同步最新的票面和背面
            autoQueryBillInfoByHold();
        } else if (AccebillConstant.ACCEBILL_TASK_HOLDBILLINFOTOPAY.equals(arguments)) {
//			2、已到期持有票据正面信息同步已结清自动托收申请
            autoQueryBillInfoToPayByHold();
        } else if (AccebillConstant.ACCEBILL_TASK_PENDINGTORECALL.equals(arguments)) {
//			3、把所有待签收业务数据变为撤回状态重新同步待签收票据
			autoPendingToRecall();
		}
	}
	
	/**
	 * 票据正面新背面信息指令定时任务：查询待签收业务数据的正面信息
	 */
	private synchronized void autoQueryBillInfoBySign(){
        this.logger.info("---票据正面新背面信息指令定时任务：查询待签收业务数据的正面信息，开始执行定时任务---");
		//获取待签收和打回的数据
		List<AbBillReceivebiz> signList=abBillReceivebizService.getAbBillReceivebizByFaisable();
		if(ListUtils.isNotNullOrEmpty(signList)){
	        this.logger.info("---票据正面新背面信息指令定时任务：查询待签收业务数据的正面信息条数:"+signList.size());
			for(AbBillReceivebiz info:signList){
				addEDraftExcByParam(info.getOrigNo(), info.getRecAcctId() , null, info.getId(), AccebillConstant.MSG_TYPE_BILLINFO);
			}
		}else{
	        this.logger.info("---票据正面新背面信息指令定时任务：查询待签收业务数据的正面信息条数为0");
		}
        this.logger.info("---票据正面新背面信息指令定时任务：查询待签收业务数据的正面信息，结束执行定时任务---");
    }


    /**
     * 票据正面新背面信息指令定时任务：已到期持有票据正面信息同步已结清自动托收申请
     */
    private synchronized void autoQueryBillInfoToPayByHold() {
        this.logger.info("---已到期持有票据正面信息同步已结清自动托收申请，开始执行定时任务---");
        List<AbAccrecordsVO> abAccrecordsVOList = new ArrayList<>();
        //查询线上票据应付未结清、应收持有业务数据
        List<AbAccrecordsVO> holdList = abAccrecordsService.getAbAccrecordsVOByHold(true);
        if (ListUtils.isNotNullOrEmpty(holdList)) {
            this.logger.info("---已到期持有票据正面信息同步已结清自动托收申请:" + holdList.size());
            for (AbAccrecordsVO info : holdList) {
                //已到期且已结清的票据生成一条提示付款流水
                if(AccebillConstant.NEWBILL_STATUS_CS06.equals(info.getBillStatus())
                        ||AccebillConstant.CIRCULATE_TF0501.equals(info.getEbillBlCrclIndc())
                        ||AccebillConstant.AB_BILL_STATUS_000000.equals(info.getBillStatus())){
                    abAccrecordsVOList.add(info);
                } else {
                    addEDraftExcByParam(info.getOrigNo(), info.getReqAcctId(), info.getId(), AccebillConstant.MSG_TYPE_BILLINFO);
                    addEDraftExcByParam(info.getOrigNo(), info.getReqAcctId(), info.getId(), AccebillConstant.MSG_TYPE_BACKINFO);
                }
            }
            if(!CollectionUtil.isEmpty(abAccrecordsVOList)){
                //线上票据到期日当天已结清票据生成一条提示付款流水
                onLineNewBillDueDeal(abAccrecordsVOList);
                this.logger.info("---票据正面新背面信息指令定时任务：查询应付未结清、应收持有业务数据的正面背面信息理条数为0");
            }
        } else {
            this.logger.info("---已到期持有票据正面信息同步已结清自动托收申请条数为0");
        }
        this.logger.info("---已到期持有票据正面信息同步已结清自动托收申请，结束执行定时任务---");
    }

    /**
     * 票据正面新背面信息指令定时任务：查询应付未结清、应收持有业务数据的正面背面信息理
     */
    private synchronized void autoQueryBillInfoByHold() {
        this.logger.info("---票据正面新背面信息指令定时任务：查询应付未结清、应收持有业务数据的正面背面信息理，开始执行定时任务---");
//        //线上新一代票据到期日当天已结清票据生成一条提示付款流水
//        onLineNewBillDueDeal();
        //查询线上票据应付未结清、应收持有业务数据
        List<AbAccrecordsVO> holdList = abAccrecordsService.getAbAccrecordsVOByHold();
        if (ListUtils.isNotNullOrEmpty(holdList)) {
            this.logger.info("---票据正面新背面信息指令定时任务：查询应付未结清、应收持有业务数据的正面背面信息理条数:" + holdList.size());
            for (AbAccrecordsVO info : holdList) {
                addEDraftExcByParam(info.getOrigNo(), info.getReqAcctId(), info.getId(), AccebillConstant.MSG_TYPE_BILLINFO);
                addEDraftExcByParam(info.getOrigNo(), info.getReqAcctId(), info.getId(), AccebillConstant.MSG_TYPE_BACKINFO);
//                //当天到期的线上新票据需要根据是否已结清生成一条提示付款流水
//                if("1".equals(info.getNewStmIssuofpprInd()) && "1".equals(info.getSendSign()) && info.getEndDate().getTime() - DateUtils.getDateYMD().getTime() == 0
//                        && (info.getBriefId() != null && info.getBriefId().startsWith("9") || "6001".equals(info.getBriefId()))){
//                    abAccrecordsVOMap.put(info.getVid(),info);
//                }
            }
        } else {
            this.logger.info("---票据正面新背面信息指令定时任务：查询应付未结清、应收持有业务数据的正面背面信息理条数为0");
        }
        this.logger.info("---票据正面新背面信息指令定时任务：查询应付未结清、应收持有业务数据的正面背面信息理，结束执行定时任务---");
    }

    /**
     * 线上票据到期日当天已结清票据生成一条提示付款流水
     */
    private void onLineNewBillDueDeal(List<AbAccrecordsVO> abAccrecordsVOList) {
        //新票到期日当天若是已结清票据生成一条已完成的提示付款流水
        if(CollectionUtil.isEmpty(abAccrecordsVOList)) return;
        ICurrentUser userVo = abUserService.getScheduler();
        for(AbAccrecordsVO aif : abAccrecordsVOList){
            //生成提示付款流水
            AbBillCollectionApplyVo abBillCollectionApplyVo = new AbBillCollectionApplyVo();
            abBillCollectionApplyVo.setVids(aif.getVid());
            abBillCollectionApplyVo.setVvids(aif.getVvid());
            abBillCollectionApplyVo.setAids(aif.getId());
            abBillCollectionApplyService.collectionApplyForAotoTask(abBillCollectionApplyVo, userVo);
        }
    }
//
//    /**
//     * 线上新一代票据到期日当天已结清票据生成一条提示付款流水
//     */
//    private void onLineNewBillDueDeal() {
//        //新票到期日当天若是已结清票据生成一条已完成的提示付款流水
//        if(CollectionUtil.isEmpty(abAccrecordsVOMap)) return;
//        for(String key : abAccrecordsVOMap.keySet()){
//            AbAccrecordsVO voVal = abAccrecordsVOMap.get(key);
//            //不是到期日当天不处理
//            if(voVal.getEndDate().getTime() - DateUtils.getDateYMD().getTime() != 0) abAccrecordsVOMap.remove(key);
//        }
//        if(CollectionUtil.isEmpty(abAccrecordsVOMap)) return;
//        String ids = CollectionUtil.join(abAccrecordsVOMap.keySet(),",");
//        List<AbVoucherinfo> infoLists = abVoucherinfoDao.listBy("id",ids, MatchTypeEnum.IN);
//        for(AbVoucherinfo aif : infoLists){
//            //正面信息不是已结清和已结束不处理
//            if(!AccebillConstant.NEWBILL_STATUS_CS06.equals(aif.getBillStatus()) || !AccebillConstant.CIRCULATE_TF0501.equals(aif.getEbillBlCrclIndc())) continue;
//            AbAccrecordsVO voVal = abAccrecordsVOMap.get(aif.getId());
//            if(voVal == null) continue;
//            //生成提示付款流水
//            AbBillCollectionApplyVo abBillCollectionApplyVo = new AbBillCollectionApplyVo();
//            abBillCollectionApplyVo.setVids(aif.getId());
//            abBillCollectionApplyVo.setVvids(voVal.getVvid());
//            abBillCollectionApplyVo.setAids(voVal.getId());
//            abBillCollectionApplyService.collectionApplyForAotoTask(abBillCollectionApplyVo,null);
//            abAccrecordsVOMap.remove(aif.getId());
//        }
//    }

    /**
     * 把所有待签收业务数据变为撤回状态重新同步待签收票据
     */
    private synchronized void autoPendingToRecall() {
        this.logger.info("---把所有待签收业务数据变为撤回状态重新同步待签收票据，开始执行定时任务---");

        //去除代签收数据为作废时候需要把消息提醒删除
        Map<String, Object> params = new HashMap<>();
        params.put("statusArray", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING, AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE});
        List<AbBillReceivebizVO> signList = abBillReceivebizService.getAbBillReceivebizVOByParams(params);
        if (ListUtils.isNotNullOrEmpty(signList)) {
            ICurrentUser userVo = abUserService.getScheduler();
            //代签收提醒去除消息提醒代办
            for (AbBillReceivebizVO sign : signList) {
                if (StringUtils.isNotEmpty(sign.getId())) {
                    AbAccrecords acInfo = new AbAccrecords();
                    acInfo.setRecid(sign.getId());
                    acInfo.setBriefId(AccebillConstant.BRIEF_STATUS_9003);
                    abBillNotificationService.completeMsg(acInfo, userVo);
                }
            }
        }
        int size = abBillReceivebizService.updateAbBillReceivebizByPendingToRecall();
        this.logger.info("---把所有待签收业务数据变为撤回状态重新同步待签收票据。修改成功条数:" + size);
        this.logger.info("---把所有待签收业务数据变为撤回状态重新同步待签收票据，结束执行定时任务---");
	}
}
