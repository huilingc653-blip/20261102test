package com.bytter.bill.domain.bill.utils;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.NotificationConstant;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.logging.Logger;
public class AbAccebillUtils {
	
	/**
	 * 票据业务代码对应消息类型代码map
	 */
	private static final Map<String,String> billBu2MsgMap=new HashMap<>();
	
	/**
	 * 票据业务代码对应service服务map
	 */
	private static final Map<String,String> billServiceMap=new HashMap<>();
	
	static{
//		票据业务代码对应service服务初始化
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_001, "abEmitBillService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_017, "abBillGuaranteeApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9017, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_002, "abBillAcceptApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9002, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_003, "abBillReceiveApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9003, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6101, "abEmitBillSupplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6120, "abEmitBillPayService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6001, "abReceiveBillService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6005, "abBillInvenTransferService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6006, "abBillInvenFetchService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6201, "abBillTransferUpApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6202, "abBillTransferCollectionService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6203, "abBillTransferDownApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6204, "abBillTransferDownManageService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_010, "abBillEndorseService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9010, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6011, "abBillDiscountApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_011, "abBillDiscountManagerService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6012, "abBillDiscountAccountService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9012, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_018, "abBillPledgeApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9018, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_019, "abBillPledgeRelieveService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9019, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6019, "abBillPledgeCollectService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_020, "abBillCollectionApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9020, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_021, "abBillCollectionApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9021, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6020, "abBillCollectionAccountService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_022, "abBillSearchforApplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9022, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_023, "abBillSearchforManageService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9023, "abBillReceivebizService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_004, "abEmitBillBackService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_032, "abBillRecallService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6004, "abReceiveBillBackService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6007, "abReceiveBillLockService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6008, "abReceiveBillLockService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_012, "abBillDiscountRansomService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_6212, "abBillDiscountCollectService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9004, "abBillDeleteService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9101, "abBillSignSupplyService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_013, "abBillEndorseRequestService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_014, "abBillEndorseResponseService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_024, "abBillNonassignableCancelService");
		billServiceMap.put(AccebillConstant.BRIEF_STATUS_9024, "abBillNonassignableCancelService");
//		票据业务代码对应消息类型代码初始化
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_001, NotificationConstant.ACCEBILL.EMIT_BILL);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6101, NotificationConstant.ACCEBILL.EMIT_BILL_SUPPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_004, NotificationConstant.ACCEBILL.EMIT_BILL_BACK);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6120, NotificationConstant.ACCEBILL.EMIT_BILL_PAY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6120_1, NotificationConstant.ACCEBILL.EMIT_BILL_PAY_1);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6120_2, NotificationConstant.ACCEBILL.EMIT_BILL_PAY_2);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6001, NotificationConstant.ACCEBILL.RECEIVE_BILL);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6005, NotificationConstant.ACCEBILL.INVEN_TRANSFER);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6006, NotificationConstant.ACCEBILL.INVEN_FETCH);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_010, NotificationConstant.ACCEBILL.ENDORSE);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6011, NotificationConstant.ACCEBILL.DISCOUNT_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_011, NotificationConstant.ACCEBILL.DISCOUNT_MANAGER);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6012, NotificationConstant.ACCEBILL.DISCOUNT_ACCOUNT);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6212, NotificationConstant.ACCEBILL.DISCOUNT_COLLECT);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_012, NotificationConstant.ACCEBILL.DISCOUNT_RANSOM);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_018, NotificationConstant.ACCEBILL.PLEDGE_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_019, NotificationConstant.ACCEBILL.PLEDGE_RELIEVE);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6019, NotificationConstant.ACCEBILL.PLEDGE_COLLECT);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6201, NotificationConstant.ACCEBILL.TRANSFER_UPAPPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6202, NotificationConstant.ACCEBILL.TRANSFER_COLLECTION);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6203, NotificationConstant.ACCEBILL.TRANSFER_DOWNAPPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6204, NotificationConstant.ACCEBILL.TRANSFER_DOWNMANAGE);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6004, NotificationConstant.ACCEBILL.RECEIVE_BILLBACK);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_020, NotificationConstant.ACCEBILL.COLLECTION_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_021, NotificationConstant.ACCEBILL.COLLECTION_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_6020, NotificationConstant.ACCEBILL.COLLECTION_ACCOUNT);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9002, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9003, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9010, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9012, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9017, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9018, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9019, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9020, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9021, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9023, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_017, NotificationConstant.ACCEBILL.GUARANTEE_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_002, NotificationConstant.ACCEBILL.ACCEPT_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_003, NotificationConstant.ACCEBILL.RECEIVE_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_032, NotificationConstant.ACCEBILL.RECALL);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_022, NotificationConstant.ACCEBILL.SEARCHFOR_APPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9022, NotificationConstant.ACCEBILL.SEARCHFOR_MANAGE);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_023, NotificationConstant.ACCEBILL.SEARCHFOR_MANAGE);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_4000, NotificationConstant.ACCEBILL.BILL_INSTRUCTION_MANAGE);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9101, NotificationConstant.ACCEBILL.SIGN_SUPPLY);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_9024, NotificationConstant.ACCEBILL.BILL_SIGN);
		billBu2MsgMap.put(AccebillConstant.BRIEF_STATUS_024, NotificationConstant.ACCEBILL.BILL_NONASSIGNABLECANCEL);
	}
	
	/**
	 * 获取票据流程业务Service对象信息
	 * @param briefId
	 * @return
	 */
	public static IAccebillBaseService getAbAccebillService(String briefId){
		String beanClass = billServiceMap.get(briefId);
		if(StringUtils.isNotEmpty(beanClass)){
			IAccebillBaseService baseService=(IAccebillBaseService) ApplicationContextUtil.getContext().getBean(beanClass);
			return baseService;
		}
		return null;
	}
	
	/**
	 * 获取票据流程业务Service对象信息
	 * @param briefId
	 * @return
	 */
	public static IAccebillAccbookService getAbAccebillAccbookService(String briefId){
		String beanClass = billServiceMap.get(briefId);
		IAccebillAccbookService baseService=null;
		if(StringUtils.isNotEmpty(beanClass)){
			Object objService=ApplicationContextUtil.getContext().getBean(beanClass);
			if(objService instanceof IAccebillAccbookService){
				baseService=(IAccebillAccbookService)objService;
			}
		}
		return baseService;
	}
	
	/**
	 * 通过业务代码获取消息提醒代码
	 * @param buCode 业务代码
	 * @return 消息提醒代码
	 */
	public static String getMsgCodeByBuCode(String buCode){
		if(StringUtils.isNotEmpty(buCode)){
			return billBu2MsgMap.get(buCode);
		}
		return "";
	}
	
	/**
	 * 获取消息ID
	 * @return
	 */
	public static String getMsgId() {
		return getMsgId(4);
	}
	/**
	 * 获取消息ID
	 * @return
	 */
	public static String getMsgId(int length) {
//		return new java.util.Date().getTime()+String.valueOf((int)((Math.random()*9+1)*1000));
		return new java.util.Date().getTime()+String.valueOf(Math.random()*9).substring(2, length+2);
	}
	
	/**
	 * 是否第三方票据类型非商票和非银票即为第三方票据类型
	 * @param billType 票据类型
	 * @return 10 银票11商票 为false 其他为true
	 */
	public static boolean isBillTypeTP(Integer billType){
        if (billType == null) {
            return false;  // 或其他业务逻辑
        }
		return !AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(billType))
				&& !AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(billType));
	}
	/**
	 *
	 * @param abVoucherinfo 主表信息
	 * @param abVoucherinfoDao
	 * @param returnRecodes 被打回流水
	 * @param logger
	 * @param abVoucherinfoViceDao
	 * @param abAccrecordsService
	 * @param userVo 用户信息
	 * @param genNewRecodes 合并票据后是否生成一条打回流水，默认是
	 */
	public static void newDraftDeal(AbVoucherinfo abVoucherinfo, AbVoucherinfoDao abVoucherinfoDao, AbAccrecords returnRecodes, Logger logger, AbVoucherinfoViceDao abVoucherinfoViceDao,
									AbAccrecordsService abAccrecordsService, ICurrentUser userVo, Boolean genNewRecodes){
		//如果为拆分新票据，需要将新票据进行合并
		if(!"1".equals(abVoucherinfo.getNewStmIssuofpprInd()) || !"1".equals(abVoucherinfo.getBlgrdbagcrclPrmtInd())
				|| StringUtil.isBlank_new(abVoucherinfo.getOldDraftId()) ){
			return;
		}
		//使用oldDraftId查询出已拆分的票据信息
		List<AbVoucherinfo> newDraftMainInfos = abVoucherinfoDao.listByOldDraftId(abVoucherinfo.getOldDraftId());
		if(CollectionUtils.isEmpty(newDraftMainInfos) || newDraftMainInfos.size() != 2){
			logger.info("已拆分的票据信息newDraftMainInfos.size()======" + newDraftMainInfos.size());
			return;
		}
		String vids = "";
		for(AbVoucherinfo voucherinfo : newDraftMainInfos){
			vids = vids + voucherinfo.getId() + ",";
		}
		vids = vids.substring(0,vids.length()-1);
		List<AbVoucherinfoVice> voucherinfoVices = abVoucherinfoViceDao.getVoucherinfoViceByVids(vids);
		if(CollectionUtils.isEmpty(voucherinfoVices)){
			logger.info("已拆分的票据信息voucherinfoVices.size()======" + voucherinfoVices.size());
			return;
		}
		//被打回的从表数据
		AbVoucherinfoVice returnViceInfo = null;
		//被打回的从表数据对应的未拆分前的从表数据
		AbVoucherinfoVice returnViceInfoOld = null;
		//更新拆分出来的子票据从表信息为2已删除
		for(AbVoucherinfoVice vice : voucherinfoVices){
			vice.setValidSign("2");
			vice.setUpdateDate(new Date());
			vice.setUpdateBy(userVo.getId());
			vice.setUpdateName(userVo.getUsername());
			//获取相应流水表数据
			List<AbAccrecordsVO> abAccrecordsVOList = abAccrecordsService.listAbAccrecordsIdByVvId(vice.getId());
			if(CollectionUtils.isEmpty(abAccrecordsVOList)){
				logger.info("未能找到已拆分流水信息");
				continue;
			}
			List<AbAccrecords> updAccrecordsList = new ArrayList<>();
			for(AbAccrecordsVO v : abAccrecordsVOList){
				AbAccrecords upd = new AbAccrecords();
				//更新相应的流水状态为-2以删除
				v.setStatus(-2);
				v.setUpdateDate(new Date());
				v.setUpdateBy(userVo.getUsername());
				BeanUtils.copyNotNullProperties(v,upd);
				updAccrecordsList.add(upd);
			}
			abAccrecordsService.batchSaveOrUpdate(updAccrecordsList);
			if(returnViceInfo == null && returnRecodes.getVvid().equals(vice.getId())){
				returnViceInfo = vice;
			}
		}
		if(returnViceInfo == null){
			logger.info("未能找到已拆分票据从表信息");
			return;
		}
		abVoucherinfoViceDao.batchSaveOrUpdate(voucherinfoVices);
		//AbVoucherinfo oldDraftMainInfo = abVoucherinfoDao.get(abVoucherinfo.getOldDraftId());
		List<AbVoucherinfoVice> oldDraftViceInfos = abVoucherinfoViceDao.getVoucherinfoViceByVidAndValidSign(abVoucherinfo.getOldDraftId(),"3");
		if(CollectionUtils.isEmpty(oldDraftViceInfos)){
			logger.info("票据拆分前数据未找到");
			return;
		}
		//更新拆票前票据从表信息为有效
		for(AbVoucherinfoVice oldVice : oldDraftViceInfos){
			oldVice.setValidSign("1");
			oldVice.setUpdateDate(new Date());
			oldVice.setUpdateBy(userVo.getId());
			oldVice.setUpdateName(userVo.getUsername());
			//获取相应流水表数据
			List<AbAccrecordsVO> abAccrecordsVOList = abAccrecordsService.listAbAccrecordsIdByVvIdAndStatus(oldVice.getId(),AccebillConstant.ACCE_BILL_STATUS_96);
			if(CollectionUtils.isEmpty(abAccrecordsVOList)){
				logger.info("未能找到已拆分流水信息");
				continue;
			}
			List<AbAccrecords> updAccrecordsList = new ArrayList<>();
			for(AbAccrecordsVO v : abAccrecordsVOList){
				AbAccrecords upd = new AbAccrecords();
				//更新相应的流水状态为95已完成
				v.setStatus(95);
				v.setUpdateDate(new Date());
				v.setUpdateBy(userVo.getId());
				v.setUpdateName(userVo.getUsername());
				BeanUtils.copyNotNullProperties(v,upd);
				updAccrecordsList.add(upd);
			}
			if(returnViceInfoOld == null
					&& (returnViceInfo.getBillSource() == oldVice.getBillSource() || returnViceInfo.getBillSource().equals(oldVice.getBillSource()))
					&& (returnViceInfo.getCorpId() == oldVice.getCorpId() || returnViceInfo.getCorpId().equals(oldVice.getCorpId()))
					&& (returnViceInfo.getBoxStatus() == oldVice.getBoxStatus() || returnViceInfo.getBoxStatus().equals(oldVice.getBoxStatus()))
					&& (returnViceInfo.getCnAccrecords() == oldVice.getCnAccrecords() || returnViceInfo.getCnAccrecords().equals(oldVice.getCnAccrecords()))){
				returnViceInfoOld = oldVice;
			}
		}
		if(returnViceInfoOld == null){
			logger.info("未能找到未拆分前原票据信息");
			return;
		}
		List<AbAccrecords> newAddRecrdesList = new ArrayList<>();
		AbAccrecords newAddRecrdes = new AbAccrecords();
		BeanUtils.copyNotNullProperties(returnRecodes, newAddRecrdes,"id","vid", "vvid");
		newAddRecrdes.setUpdateDate(new Date());
		newAddRecrdes.setUpdateBy(userVo.getId());
		newAddRecrdes.setUpdateName(userVo.getUsername());
		newAddRecrdes.setVid(abVoucherinfo.getOldDraftId());
		newAddRecrdes.setVvid(returnViceInfoOld.getId());
		//genNewRecodes 为false时不为合并后数据插入打回流水
		if(genNewRecodes == null || genNewRecodes){
			abAccrecordsService.batchSaveOrUpdate(newAddRecrdesList);
		}
		returnViceInfoOld.setBackAcid(StringUtils.isBlank(newAddRecrdes.getId()) ? returnRecodes.getId() : newAddRecrdes.getId());
		abVoucherinfoViceDao.batchSaveOrUpdate(oldDraftViceInfos);
	}
	/**
	 *
	 * @param origNo
	 * @param params
	 * @return
	 */
	public static String getOrigNoQuerySql(String fieldName,String origNo,Map<String,Object> params){
		StringBuilder sql=new StringBuilder();
		if(StringUtil.isNotEmpty(origNo)){
			origNo=origNo.replace(" ",",").replace("，",",");
			String[] origNos=origNo.split(",");
			sql.append(" ( ");
			boolean isOr=false;
			String key=null;
			for(int i=0;i<origNos.length;i++){
				String orignoNum = "";
				String sbbStrtNo = "";
				String sbbEndNo = "";
				if(origNos[i].contains("-")){
					String[] splitArr = origNos[i].split("-");
					orignoNum = splitArr[0];
					sbbStrtNo = splitArr.length > 1 ? splitArr[1] : "";
					sbbEndNo = splitArr.length > 2 ? splitArr[2] : "";
				}else {
					orignoNum = origNos[i];
				}
				String sql1 = "";
				if(StringUtil.isNotEmpty(orignoNum)){
					key="origNoLike"+i;
					sql1 = sql1 + " ("+fieldName+" like :"+key;
					params.put(key,"%"+orignoNum+"%");
				}
				if(!"".equals(sbbStrtNo)){
					key="sbbStrtNoLike"+i;
					if(!"".equals(sql1)){
						sql1 = sql1 + " and ";
					}
					sql1 = sql1 + " z.SBBLL_RNG_STRT_SN like:"+key;
					params.put(key,"%"+sbbStrtNo+"%");
				}
				if(!"".equals(sbbEndNo)){
					key="sbbEndNoLike"+i;
					if(!"".equals(sql1)){
						sql1 = sql1 + " and ";
					}
					sql1 = sql1 + " z.SBBLL_RNG_END_SN like:"+key;
					params.put(key,"%"+sbbEndNo+"%");
				}
				sql1 = isOr && !"".equals(sql1) ? (" or " + sql1 + ") ")
						: (!"".equals(sql1) ? (sql1 + ") ") : sql1);
				isOr = !"".equals(sql1) || isOr ? true : false;
				sql.append(sql1);
			}
			sql.append(" ) ");
		}
		return sql.toString();
	}

	/**
	 * 设置离线票据的票据状态和流通标志，并返回从票数据
	 * @param abVoucherinfoService
	 * @param vid 主表id
	 * @param vvid 从表id
	 * @param sendSign 是否发送指令标志
	 * @param abVoucherinfoViceService
	 * @param billStatusOld 旧票据状态
	 * @param billStatusNew 新票据状态
	 * @param ebillBlCrclIndc 票据流通标志
	 * @return
	 */
	public static AbVoucherinfoVice setOffLineVoucherViceBillStatus(AbVoucherinfoService abVoucherinfoService, String vid, String vvid, String sendSign
			, AbVoucherinfoViceService abVoucherinfoViceService, String billStatusOld, String billStatusNew, String ebillBlCrclIndc){
		AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(vvid,true);
		AbVoucherinfo mainInfo = abVoucherinfoService.get(vid,true);
		//在线票据只需同步银行主表的数据就行
		if("1".equals(sendSign)){
			return abVoucherinfoVice;
		}
		if(!"1".equals(mainInfo.getNewStmIssuofpprInd())){
			abVoucherinfoVice.setBillStatus(billStatusOld);
		}else {
			abVoucherinfoVice.setBillStatus(billStatusNew);
			abVoucherinfoVice.setEbillBlCrclIndc(ebillBlCrclIndc);
		}
		abVoucherinfoViceService.update(abVoucherinfoVice);
		return abVoucherinfoVice;
	}
	
}
