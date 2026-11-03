package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillNotificationDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillReceivebiz;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillInvenTransferVO;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IEquityMaintenanceService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.EquityMaintenanceDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 票据消息推送公共服务类
 * @author zhengg
 *
 */
@Service("abBillNotificationService")
public class AbBillNotificationService extends BaseService<T, String> implements ITaskService {
	
	@Autowired
	private AbBillNotificationDao abBillNotificationDao;
	@DubboReference(check = false)
	private INotificationService notificationService;
	
	@Autowired
	private AbBillReceivebizService abBillReceivebizService;
	
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	
	@Autowired
	private AbBillMiddleService abBillMiddleService;
	@Autowired
	private AbBillTransferService abBillTransferService;
	@Autowired
	private AbUserService abUserService;
	@Autowired
	private AbCorpService abCorpService;
	@DubboReference(check = false)
	private IBisAccBalService iBisAccBalService;
	@DubboReference(check = false)
	private IEquityMaintenanceService iEquityMaintenanceService;
	@Autowired
	private AbCalendarService abCalendarService;
	@Override
	public BaseDao<T, String> getEntityDao() {
		return null;
	}
	
	
	/**
	 * 根据流水id推送消息信息
	 * @param acId 流水表id
	 */
	public void sendMsg(String acId){
		if(StringUtils.isNotEmpty(acId)){
			sendMsg(acId, null);
		}
	}
	
	/**
	 * 根据流水信息推送消息信息
	 * @param acInfo 流水表信息
	 */
	public void sendMsg(AbAccrecords acInfo){
		sendMsg(acInfo, null);
	}
	/**
	 * 根据流水id推送消息信息
	 * @param acId 流水表信息
	 * @param briefIdParams 业务类型 
	 */
	public void sendMsg(String acId,String briefIdParams){
		if(StringUtils.isNotEmpty(acId)){
			sendMsg(abAccrecordsService.get(acId, true), briefIdParams);
		}
	}
	
	/**
	 * 指令打回消息发送
	 * @param acInfo
	 * @param userBy
	 */
	public void excRepulseSendMsg(AbAccrecords acInfo,String userBy){
		if(acInfo!=null){
			AbAccrecordsVO acVoInfo=initAbAccrecordsToVO(acInfo);
			if(acVoInfo!=null){
				sendMsg(acVoInfo.getId(), acVoInfo.getCorpId(), acVoInfo.getBriefId(), acVoInfo, userBy);
			}
		}
	}
	
	/**
	 * 待签收消息发送
	 * @param rebizId 签收表ID
	 */
	public void receivebizSendMsg(String rebizId){
		if(StringUtils.isNotEmpty(rebizId)){
			AbBillReceivebiz rebizInfo=abBillReceivebizService.get(rebizId, true);
			receivebizSendMsg(rebizInfo);
		}
	}
	
	/**
	 * 待签收消息发送
	 * @param rebizInfo 签收表对象
	 */
	public void receivebizSendMsg(AbBillReceivebiz rebizInfo){
		if(rebizInfo!=null){
			sendMsg(rebizInfo.getId(), rebizInfo.getRecCorpId(), "9"+rebizInfo.getMsgtype(), rebizInfo, null);
		}
	}
	/**
	 * 根据流水表推送消息信息
	 * @param acInfo 流水表信息
	 */
	public void sendMsg(AbAccrecords acInfo,String briefIdParams){
		sendMsg(acInfo, briefIdParams, null);
	}
	
	/**
	 * 根据流水表推送消息信息
	 * @param acInfo 流水表信息
	 * @param userBy 发送用户
	 */
	public void sendMsg(AbAccrecords acInfo,String briefIdParams, String userBy){
		if(acInfo!=null){
			sendMsg(initAbAccrecordsToVO(acInfo), briefIdParams, userBy);
		}
	}
	
	/**
	 * 根据流水VO推送消息信息
	 * @param acVoInfo 流水信息VO
	 * @param  briefIdParams 业务类型
	 */
	public void sendMsg(AbAccrecordsVO acVoInfo, String briefIdParams){
		sendMsg(acVoInfo, briefIdParams, null);
	}
	
	/**
	 * 根据流水VO推送消息信息
	 * @param acVoInfo 流水信息VO
	 * @param briefIdParams 业务类型
	 */
	public void sendMsg(AbAccrecordsVO acVoInfo, String briefIdParams, String userBy){
		if(acVoInfo!=null
				&&StringUtils.isNotEmpty(acVoInfo.getCorpId())){
			String briefId=briefIdParams;
			if(StringUtils.isEmpty(briefId)){
				briefId=acVoInfo.getBriefId();
			}
			//检查是否需求做消息推送
			if(!checkIsSendMsg(briefId,acVoInfo)){
				return;
			}
			sendMsg(acVoInfo.getId(), acVoInfo.getCorpId(), briefId, acVoInfo, userBy);
		}
	}
	
	/**
	 * 进行发生消息处理
	 * @param msgId 消息ID
	 * @param corpId 所属单位
	 * @param briefId 业务类型
	 * @param object 转对象
	 */
	private void sendMsg(String msgId,String corpId,String briefId,Object object, String userBy){
		//如果消息id或者业务类型为空则不往后走
		if(StringUtils.isEmpty(msgId)
				||StringUtils.isEmpty(corpId)
				||StringUtils.isEmpty(briefId)
				||object==null){
			return;
		}
		String tag=AbAccebillUtils.getMsgCodeByBuCode(briefId);
		//业务对应消息类型为空则不做业务
		if(StringUtils.isNotEmpty(tag)){
			if(StringUtils.isNotEmpty(userBy)){
				notificationService.sendToUserByLoginName(msgId, userBy, tag, object);
			}else{
				notificationService.send(msgId, corpId, tag, object);
			}
		}
	}
	
	/**
	 * 流水信息转出vo信息
	 * @param acInfo
	 * @return
	 */
	private AbAccrecordsVO initAbAccrecordsToVO(AbAccrecords acInfo){
		if(acInfo==null){
			return null;
		}
		AbAccrecordsVO acVoInfo=new AbAccrecordsVO();
		//设置下拨申请对象
		if(AccebillConstant.BRIEF_STATUS_6203.equals(acInfo.getBriefId())){
			acVoInfo=new AbBillInvenTransferVO();
		}
		//设置票据主信息
		if(acInfo.getVouInfo()==null
				&&StringUtils.isNotEmpty(acInfo.getVid())){
			acInfo.setVouInfo(abVoucherinfoService.get(acInfo.getVid(), true));
		}
		if(acInfo.getVouInfo()!=null){
			BeanUtils.copyNotNullProperties(acInfo.getVouInfo(), acVoInfo);
		}
		//设置从表信息
		if(acInfo.getVouViceInfo()==null
				&&StringUtils.isNotEmpty(acInfo.getVvid())){
			acInfo.setVouViceInfo(abVoucherinfoViceService.get(acInfo.getVvid(), true));
		}
		if(acInfo.getVouInfo()!=null){
			BeanUtils.copyNotNullProperties(acInfo.getVouViceInfo(), acVoInfo);
		}
		
		if(StringUtils.isNotEmpty(acInfo.getTrid())){
			AbBillTransfer abBillTransfer=abBillTransferService.get(acInfo.getTrid(), true);
			if(abBillTransfer!=null){
				BeanUtils.copyNotNullProperties(abBillTransfer, acVoInfo);
				acVoInfo.setCorpId(abBillTransfer.getRecevieCorpId());
			}
		}
		BeanUtils.copyNotNullProperties(acInfo, acVoInfo);
//		dictServiceImpl.inject(acVoInfo);
		return acVoInfo;
	}
	
	/***
	 * 检查是否需求做消息推送
	 * @param briefId 业务类型
	 * @return
	 */
	private boolean checkIsSendMsg(String briefId,AbAccrecordsVO acVoInfo){
		boolean reBoo=false;
		boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
		//付款开票可以推送消息
		if(AccebillConstant.BRIEF_STATUS_001.equals(briefId)
				&&AccebillConstant.AB_BILL_BUS_STATUS0.equals(acVoInfo.getStatus())){
			reBoo = true;
		}else if(AccebillConstant.BRIEF_STATUS_6101.equals(briefId)
				&&AccebillConstant.AB_BILL_OPT_STATUS19.equals(acVoInfo.getOptStatus())){
			//如果启用不须补录模式则不需要提醒补录代办
			if(!abVoucherinfoViceService.isEmitMake()){
				reBoo = false;
			}
			//票据补录提醒，补录（纸票线下电票）开票审批完成、定时器线上电票提示收票完成
			if(AccebillConstant.BRIEF_STATUS_001.equals(acVoInfo.getBriefId())
					&&AccebillConstant.AB_BILL_SEND_SIGN_N.equals(acVoInfo.getSendSign())){
				reBoo = true;
			}else if(AccebillConstant.BRIEF_STATUS_003.equals(acVoInfo.getBriefId())){
				reBoo = true;
			}
		}else if(((AccebillConstant.BRIEF_STATUS_6120.equals(briefId))
				&& ((AccebillConstant.BRIEF_STATUS_6101.equals(acVoInfo.getBriefId()) && isEmitMake)
					|| (!isEmitMake && (AccebillConstant.BRIEF_STATUS_001.equals(acVoInfo.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_003.equals(acVoInfo.getBriefId())
						|| AccebillConstant.BRIEF_STATUS_6101.equals(acVoInfo.getBriefId()))))
				&&AccebillConstant.AB_BILL_OPT_STATUS19.equals(acVoInfo.getOptStatus())) || AccebillConstant.BRIEF_STATUS_6120_1.equals(briefId) || AccebillConstant.BRIEF_STATUS_6120_2.equals(briefId)){
//			到期兑付提醒，定时任务票据到期允许兑付确认
			reBoo = true;
		}else if(AccebillConstant.BRIEF_STATUS_6001.equals(briefId)
				&&AccebillConstant.AB_BILL_BUS_STATUS0.equals(acVoInfo.getStatus())
				&&AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(acVoInfo.getInsideSing()))){
//			内部收票提醒，票据（纸票线下电票）对内自开票据补录、对内质押、对内贴现、对内背书
			if(AccebillConstant.AB_BILL_SOURCE_REC.equals(acVoInfo.getBillSource())
					||AccebillConstant.AB_BILL_SOURCE_PLEDGE.equals(acVoInfo.getBillSource())
					||AccebillConstant.AB_BILL_SOURCE_DISCOUNT.equals(acVoInfo.getBillSource())
					||AccebillConstant.AB_BILL_SOURCE_ENDORSE.equals(acVoInfo.getBillSource())){
				reBoo = true;
			}
		}else if(AccebillConstant.BRIEF_STATUS_011.equals(briefId)
				&&AccebillConstant.BRIEF_STATUS_6011.equals(acVoInfo.getBriefId())
				&&AccebillConstant.AB_BILL_OPT_STATUS19.equals(acVoInfo.getOptStatus())){
//			贴现办理提醒，贴现申请审批完成,进行贴现办理提醒
			reBoo = true;
		}else if(String.valueOf(briefId).startsWith("9")
				||AccebillConstant.BRIEF_STATUS_023.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_020.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_021.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6204.equals(briefId)){
//			电票收到数据定时器提醒
//			收到待清偿数据定时器提醒
//			定时器票据到期自动提醒、票据到期托收拒付以后定时器提醒
//			票据下拨申请完成
			reBoo = true;
		}else if(AccebillConstant.BRIEF_STATUS_4000.equals(briefId)){
//			指令失败提醒
			reBoo = true;
		}
		return reBoo;
	}
	/**
	 * 根据流水ID完成通知提醒消息信息
	 * @param acId 流水ID
	 */
	public void completeMsg(String acId, ICurrentUser user){
		if(StringUtils.isNotEmpty(acId)){
			completeMsg(abAccrecordsService.get(acId, true),user);
		}
	}
	
	/**
	 * 根据流水信息完成通知提醒消息信息
	 * @param acInfo 流水信息
	 */
	public void completeMsg(AbAccrecords acInfo, ICurrentUser user){
		if(acInfo!=null){
			completeMsg(acInfo,acInfo.getBriefId(),user);
		}
	}
	/**
	 * 根据流水信息完成通知提醒消息信息
	 * @param acInfo 流水信息
	 * @param briefId 业务类型
	 */
	public void completeMsg(AbAccrecords acInfo,String briefId, ICurrentUser user){
		if(acInfo==null){
			return;
		}
		if(StringUtils.isEmpty(briefId)){
			briefId=acInfo.getBriefId();
		}
		
		//去除主动推送消息的 消息信息
//		if(AccebillConstant.BRIEF_STATUS_001.equals(briefId)
//				||AccebillConstant.BRIEF_STATUS_6001.equals(briefId)
//				||AccebillConstant.BRIEF_STATUS_6203.equals(briefId)){
////			自开登记\收票登记\下拨申请,且判断是付款来源才需要去除提醒（其实会和下面业务打回重复做）
////			complete(acInfo.getId(),briefId);
		if(AccebillConstant.BRIEF_STATUS_011.equals(briefId)){
//			贴现办理打回去除主动推送消息
			complete(acInfo.getId(),briefId,user);
//			贴现办理提交处理主动推送消息
			complete(acInfo.getLastid(), briefId,user);
		}else if(AccebillConstant.BRIEF_STATUS_6101.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6120.equals(briefId)
//				||AccebillConstant.BRIEF_STATUS_011.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_012.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_020.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_021.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6204.equals(briefId)){
//			自开补录\兑付确认\贴现办理\赎回申请\托收申请\票据下拨
			complete(acInfo.getLastid(), briefId,user);
		}else if(String.valueOf(briefId).startsWith("9")
				||AccebillConstant.BRIEF_STATUS_023.equals(briefId)){
//			票据签收\追索清偿
			complete(acInfo.getRecid(), briefId,user);
			String listid=abAccrecordsService.getListidByRecordIdAndAcid(acInfo.getRecid(),acInfo.getId());
			//追加审批打回的提醒去除
			complete(listid, briefId,user);
		}else if(AccebillConstant.BRIEF_STATUS_004.equals(briefId)&&StringUtils.isNotEmpty(acInfo.getLastid())){
			//退票时候考虑如果进行有消息提醒到补录时候需要把此消息提醒去除
			AbAccrecords lastAcInfo=abAccrecordsService.get(acInfo.getLastid());
			if(lastAcInfo!=null
					&&AccebillConstant.BRIEF_STATUS_001.equals(lastAcInfo.getBriefId())
					&&AccebillConstant.AB_BILL_SEND_SIGN_N.equals(lastAcInfo.getSendSign())){
				complete(acInfo.getLastid(), AccebillConstant.BRIEF_STATUS_6101,user);
			}
		}
		else if(AccebillConstant.BRIEF_STATUS_4000.equals(briefId)){
			//指令失败打回指令或重发去除提醒
			complete(acInfo.getId(), briefId,user);
			//删除票据中间表数据
			abBillMiddleService.deleteBillMiddleByBuIdAndType(acInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_7);
		}
		//自开登记、收票登记、下拨申请、票据下拨  业务打回直接把流水ID去除即可 
		if(AccebillConstant.BRIEF_STATUS_001.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6001.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6203.equals(briefId)
				||AccebillConstant.BRIEF_STATUS_6204.equals(briefId)){
			complete(acInfo.getId(),briefId,user);
		}else{//其他业务打回 根据流水找到从表的打回业务和打回流水Id 去除提醒
			AbVoucherinfoVice viceInfo=acInfo.getVouViceInfo();
			if(viceInfo==null&&StringUtils.isNotEmpty(acInfo.getVvid())){
				viceInfo=abVoucherinfoViceService.get(acInfo.getVvid(), true);
			}
			if(viceInfo!=null){
				String backAcid = viceInfo.getBackAcid();
				String backBriefId = viceInfo.getBackBriefId();
				if(StringUtils.isNotEmpty(backAcid)
						&&StringUtils.isNotEmpty(backBriefId)){
					complete(backAcid,backBriefId,user);
				}
			}
		}
	}
	
	/**
	 * 根据流水信息完成通知提醒消息信息
	 * @param msgId 消息ID
	 * @param briefId 业务类型
	 */
	private void complete(String msgId,String briefId, ICurrentUser user){
		//如果消息id或者业务类型为空则不往后走
		if(StringUtils.isEmpty(msgId)||StringUtils.isEmpty(briefId)){
			return;
		}
		String tag=AbAccebillUtils.getMsgCodeByBuCode(briefId);
		//业务对应消息类型为空则不做业务
		if(StringUtils.isNotEmpty(tag)){
			notificationService.complete(tag, msgId,null==user?null:user.getId());
		}
	}
	/**
	 * 消息提醒定时任务信息
	 * @param arguments 定时任务消息类型
	 */
	@Override
	@XxlJob("AbBillNotificationService")
	public void run(String arguments) {
		String jobParam = XxlJobHelper.getJobParam();
		arguments = jobParam;
		logger.info("定时任务接收参数：【{}】", jobParam);
		if(NotificationConstant.ACCEBILL.EMIT_BILL_SUPPLY.equals(arguments)){
//			票据补录提醒，线上电票提示收票完成进行提醒推送
			runTask003To6101(arguments);
		}else if(NotificationConstant.ACCEBILL.EMIT_BILL_PAY.equals(arguments)){
//			自开票据兑付提醒，票据到期以后提醒推送
			runTaskEmitBillPay(arguments);
		}else if(NotificationConstant.ACCEBILL.COLLECTION_APPLY.equals(arguments)){
//			托收提醒，票据到期自动提醒、票据到期托收拒付提醒
			runTaskCollectionApply(arguments);
		}else if(NotificationConstant.ACCEBILL.BILL_SIGN.equals(arguments)){
//			票据待签收提醒，电票收到签收数据推送提醒
			runTaskSign(arguments);
		}else if(NotificationConstant.ACCEBILL.SEARCHFOR_MANAGE.equals(arguments)){
//			追索清偿提醒，电票收到追索数据推送提醒
			runTaskSearchforManage(arguments);
		}else if(NotificationConstant.ACCEBILL.BILL_INSTRUCTION_MANAGE.equals(arguments)){
//			电票指令失败消息推送提醒
			runTaskInstructionManage(arguments);
		}
		//提前五天及提前两天预警信息
		else if(NotificationConstant.ACCEBILL.EMIT_BILL_PAY_1.equals(arguments)){
//			提前五天及提前两天预警信息
			runTaskBillExpirationWarning();
		}
		//承兑账户资金余额不足承兑扣款消息提醒
		else if(NotificationConstant.ACCEBILL.EMIT_BILL_PAY_2.equals(arguments)){
//			承兑账户资金余额不足承兑扣款消息提醒
			runTaskInsufficientBalanceReminder();
		}
	}
	
	/***
	 * 票据补录提醒，线上电票提示收票完成进行提醒推送
	 * @param arguments
	 */
	private void runTask003To6101(String arguments){
		if(abVoucherinfoViceService.isEmitMake()){
			this.logger.info("---票据补录提醒，线上电票提示收票完成进行提醒推送，开始执行定时任务---");
			List<AbAccrecordsVO> acInfoList=abBillNotificationDao.listTask003To6101Info();
			if(acInfoList==null||acInfoList.isEmpty()){
				this.logger.info("---票据补录提醒，线上电票提示收票完成进行提醒推送，【0】条数据。");
			}else{
				ICurrentUser userVo=abUserService.getScheduler();
				this.logger.info("---票据补录提醒，线上电票提示收票完成进行提醒推送，【"+acInfoList.size()+"】条数据。");
				for(AbAccrecordsVO acInfo:acInfoList){
					this.sendMsg(acInfo.getId(), AccebillConstant.BRIEF_STATUS_6101);
					abBillMiddleService.saveBillMiddle(acInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_6, userVo);
				}
			}
			this.logger.info("---票据补录提醒，线上电票提示收票完成进行提醒推送，结束执行定时任务---");
		}else{
			this.logger.info("---票据补录提醒，不需要执行系统启用不进行补录操作，结束执行定时任务---");
		}
	}
	
	/***
	 * 自开票据兑付提醒，票据到期以后提醒推送
	 * @param arguments
	 */
	private void runTaskEmitBillPay(String arguments){
		this.logger.info("---自开票据兑付提醒，票据到期以后提醒推送，开始执行定时任务---");
		Map<String,Object> params=new HashMap<>();
		Date endDate=DateUtils.addDay(DateUtils.getDateYMD(),abVoucherinfoViceService.getEmitWarnDayNum());
		params.put("endDate", endDate);
		boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
		List<AbAccrecordsVO> acInfoList=abBillNotificationDao.listTaskEmitBillPay(params, isEmitMake);
		if(acInfoList==null||acInfoList.isEmpty()){
			this.logger.info("---自开票据兑付提醒，票据到期以后提醒推送，【0】条数据。");
		}else{
			ICurrentUser userVo=abUserService.getScheduler();
			this.logger.info("---自开票据兑付提醒，票据到期以后提醒推送，【"+acInfoList.size()+"】条数据。");
			for(AbAccrecordsVO acInfo:acInfoList){
				this.sendMsg(acInfo.getId(), AccebillConstant.BRIEF_STATUS_6120);
				abBillMiddleService.saveBillMiddle(acInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_5, userVo);
			}
		}
		this.logger.info("---自开票据兑付提醒，票据到期以后提醒推送，结束执行定时任务---");
		
	}
	
	/***
	 * 托收提醒，票据到期自动提醒、票据到期托收拒付提醒
	 * @param arguments
	 */
	private void runTaskCollectionApply(String arguments){
		ICurrentUser userVo=abUserService.getScheduler();
		List<AbAccrecordsVO> acInfoList=null;
		this.logger.info("---托收提醒，票据到期自动提醒、票据到期托收拒付提醒，开始执行定时任务---");
//		if(abVoucherinfoViceService.isEAutoPay()){
//			this.logger.info("---托收提醒，票据到期自动提醒、票据到期托收拒付提醒，已启用自动托收申请不执行托收提醒功能---");
//			return;
//		}else{
			Map<String,Object> params=new HashMap<>();
			Date endDate=DateUtils.addDay(DateUtils.getDateYMD(),abVoucherinfoViceService.getReserveWarnDayNum());
			params.put("endDate", endDate);
			params.put("isEAutoPay", abVoucherinfoViceService.isEAutoPay());
			acInfoList=abBillNotificationDao.listTaskCollectionApplyDueto(params);
			if(acInfoList==null||acInfoList.isEmpty()){
				this.logger.info("---托收提醒，票据到期自动提醒，【0】条数据。");
			}else{
				this.logger.info("---托收提醒，票据到期自动提醒，【"+acInfoList.size()+"】条数据。");
				for(AbAccrecordsVO acInfo:acInfoList){
					this.sendMsg(acInfo.getId(), AccebillConstant.BRIEF_STATUS_020);
					abBillMiddleService.saveBillMiddle(acInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_2, userVo);
				}
			}
//		}
		acInfoList=abBillNotificationDao.listTaskCollectionApplyRefuse();
		if(acInfoList==null||acInfoList.isEmpty()){
			this.logger.info("---托收提醒，票据到期托收拒付提醒，【0】条数据。");
		}else{
			this.logger.info("---托收提醒，票据到期托收拒付提醒，【"+acInfoList.size()+"】条数据。");
			for(AbAccrecordsVO acInfo:acInfoList){
				this.sendMsg(acInfo.getId(), AccebillConstant.BRIEF_STATUS_020);
				abBillMiddleService.saveBillMiddle(acInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_3, userVo);
			}
		}
		this.logger.info("---托收提醒，票据到期自动提醒、票据到期托收拒付提醒，结束执行定时任务---");
		
	}
	
	/***
	 * 票据待签收提醒，电票收到签收数据推送提醒
	 * @param arguments
	 */
	private void runTaskSign(String arguments){
		this.logger.info("---票据待签收提醒，电票收到签收数据推送提醒，开始执行定时任务---");
		List<AbBillReceivebizVO> receivebizInfoList=abBillNotificationDao.listTaskSign();
		if(receivebizInfoList==null||receivebizInfoList.isEmpty()){
			this.logger.info("---票据待签收提醒，电票收到签收数据推送提醒，【0】条数据。");
		}else{
			ICurrentUser userVo=abUserService.getScheduler();
			this.logger.info("---票据待签收提醒，电票收到签收数据推送提醒，【"+receivebizInfoList.size()+"】条数据。");
			for(AbBillReceivebizVO receivebizInfo:receivebizInfoList){
				this.receivebizSendMsg(receivebizInfo.getId());
				abBillMiddleService.saveBillMiddle(receivebizInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_1, userVo);
			}
		}
		this.logger.info("---票据待签收提醒，电票收到签收数据推送提醒，结束执行定时任务---");
		
	}
	
	/***
	 * 追索清偿提醒，电票收到追索数据推送提醒
	 * @param arguments
	 */
	private void runTaskSearchforManage(String arguments){
		this.logger.info("---追索清偿提醒，电票收到追索数据推送提醒，开始执行定时任务---");
		List<AbBillReceivebizVO> receivebizInfoList=abBillNotificationDao.listTaskSearchforManage();
		if(receivebizInfoList==null||receivebizInfoList.isEmpty()){
			this.logger.info("---追索清偿提醒，电票收到追索数据推送提醒，【0】条数据。");
		}else{
			ICurrentUser userVo=abUserService.getScheduler();
			this.logger.info("---追索清偿提醒，电票收到追索数据推送提醒，【"+receivebizInfoList.size()+"】条数据。");
			for(AbBillReceivebizVO receivebizInfo:receivebizInfoList){
				this.receivebizSendMsg(receivebizInfo.getId());
				abBillMiddleService.saveBillMiddle(receivebizInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_4,userVo);
			}
		}
		this.logger.info("---追索清偿提醒，电票收到追索数据推送提醒，结束执行定时任务---");
		
	}
	/***
	 * 电票指令失败消息提醒推送
	 * @param arguments
	 */
	private void runTaskInstructionManage(String arguments){
		this.logger.info("---电票指令失败推送提醒，开始执行定时任务---");
		Map<String,Object> params=new HashMap<>();
		List<AbAccrecordsVO> acInfoList=abBillNotificationDao.listTaskInstructionManage(params);
		if(acInfoList==null||acInfoList.isEmpty()){
			this.logger.info("---电票指令失败提醒推送，【0】条数据。");
		}else{
			ICurrentUser userVo=abUserService.getScheduler();
			this.logger.info("---电票指令失败提醒推送，【"+acInfoList.size()+"】条数据。");
			for(AbAccrecordsVO acInfo:acInfoList){
				this.sendMsg(acInfo.getId(),AccebillConstant.BRIEF_STATUS_4000);
				abBillMiddleService.saveBillMiddle(acInfo.getId(), AccebillConstant.BILL_MIDDLE_TYPE_7, userVo);
			}
		}
		this.logger.info("---电票指令失败提醒推送，结束执行定时任务---");
	}

	/***
	 * 提前五天及提前两天预警信息
	 * @param
	 */
	private void runTaskBillExpirationWarning(){
		List<AbAccrecordsVO> acInfoList=null;
		this.logger.info("---提前五天及提前两天预警信息提醒，开始执行定时任务---");
		Map<String,Object> params=new HashMap<>();
		acInfoList=abBillNotificationDao.listTaskBillExpirationWarning(params);
		if(acInfoList==null||acInfoList.isEmpty()){
			this.logger.info("---提前五天及提前两天预警信息提醒，【0】条数据。");
		}else{
			this.logger.info("---提前五天及提前两天预警信息提醒，【"+acInfoList.size()+"】条数据。");
			List<String> corpIdList = acInfoList.stream().map(AbAccrecordsVO::getCorpId).collect(Collectors.toList());
			List<String> bankAccList = acInfoList.stream().map(AbAccrecordsVO::getBankAcc).collect(Collectors.toList());
			Map<String, CorpDTO> corpDTOMap = abCorpService.listCorpDtoByCorpIds(corpIdList);
			List<BisAccBalDTO> bankBalList = iBisAccBalService.getBankBal(StringUtils.join(bankAccList, ","), DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
			Map<String, BisAccBalDTO> balDTOMap= null;
			if(!CollectionUtil.isEmpty(bankBalList)){
				balDTOMap= bankBalList.stream().collect(Collectors.toMap(BisAccBalDTO::getBankAcc, Function.identity()));
			}
			for(AbAccrecordsVO acInfo:acInfoList){
				acInfo.setCorpName(corpDTOMap.get(acInfo.getCorpId()).getName());
				if(balDTOMap == null|| !balDTOMap.containsKey(acInfo.getBankAcc())){
					acInfo.setBankAccBalance(BigDecimal.ZERO);
				}else {
					acInfo.setBankAccBalance(balDTOMap.get(acInfo.getBankAcc()).getAvailBal() == null? balDTOMap.get(acInfo.getBankAcc()).getBal() : balDTOMap.get(acInfo.getBankAcc()).getAvailBal());
				}
				acInfo.setEndDateStr(DateUtils.format(acInfo.getEndDate(),"yyyy-MM-dd"));
				if(acInfo.getReleasePaymentMoney()!=null){
					acInfo.setReleasePaymentMoney(acInfo.getReleasePaymentMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
				}
				EquityMaintenanceDTO equityMaintenanceDTO = iEquityMaintenanceService.queryById(corpDTOMap.get(acInfo.getCorpId()).getEquityMaintenanceId());
				acInfo.setCompanyTYpeName(equityMaintenanceDTO.getEquityName());
				sendMsg(acInfo,AccebillConstant.BRIEF_STATUS_6120_1);
			}
		}
		this.logger.info("---提前五天及提前两天预警信息提醒，结束执行定时任务---");
	}

	/***
	 * 承兑账户资金余额不足承兑扣款消息提醒
	 * @param
	 */
	private void runTaskInsufficientBalanceReminder(){
		List<AbAccrecordsVO> acInfoList=null;
		this.logger.info("---承兑账户资金余额不足承兑扣款消息提醒，开始执行定时任务---");
		Map<String,Object> params=new HashMap<>();
		Date nextDay = DateUtils.addDay(DateUtils.getDateYMD(),1);
		Date workDay = abCalendarService.checkWorkDay(nextDay, true);
		params.put("endDate1", nextDay);
		params.put("endDate2", workDay);
		//是否范围查询
		boolean isRangeQuery = DateUtils.compareDate(nextDay, workDay) != 0;
		acInfoList=abBillNotificationDao.listTaskInsufficientBalanceReminder(params,isRangeQuery);
		if(acInfoList==null||acInfoList.isEmpty()){
			this.logger.info("---承兑账户资金余额不足承兑扣款消息提醒，【0】条数据。");
		}else{
			List<String> corpIdList = acInfoList.stream().map(AbAccrecordsVO::getCorpId).collect(Collectors.toList());
			List<String> bankAccList = acInfoList.stream().map(AbAccrecordsVO::getBankAcc).collect(Collectors.toList());
			Map<String, CorpDTO> corpDTOMap = abCorpService.listCorpDtoByCorpIds(corpIdList);
			List<BisAccBalDTO> bankBalList = iBisAccBalService.getBankBal(StringUtils.join(bankAccList, ","), DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
			Map<String, BisAccBalDTO> balDTOMap= null;
			if(!CollectionUtil.isEmpty(bankBalList)){
				balDTOMap= bankBalList.stream().collect(Collectors.toMap(BisAccBalDTO::getBankAcc, Function.identity()));
			}
			int sendNum = 0;
			for(AbAccrecordsVO acInfo:acInfoList){
				acInfo.setBillMoney(acInfo.getBillMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
				if(balDTOMap == null || !balDTOMap.containsKey(acInfo.getBankAcc())){
					acInfo.setBankAccBalance(BigDecimal.ZERO);
				}else {
					acInfo.setBankAccBalance(balDTOMap.get(acInfo.getBankAcc()).getAvailBal() == null?
							balDTOMap.get(acInfo.getBankAcc()).getBal() : balDTOMap.get(acInfo.getBankAcc()).getAvailBal());
				}
				acInfo.setBankAccBalance(acInfo.getBankAccBalance().setScale(2,BigDecimal.ROUND_HALF_UP));
				if(acInfo.getReleasePaymentMoney()!=null){
					acInfo.setReleasePaymentMoney(acInfo.getReleasePaymentMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
				}
				if(acInfo.getBillMoney().compareTo(acInfo.getBankAccBalance()) < 1){
					continue;
				}
				acInfo.setCorpName(corpDTOMap.get(acInfo.getCorpId()).getName());
				if(isRangeQuery){
					acInfo.setEndDateStr(DateUtils.format(nextDay,"yyyy-MM-dd"));
					acInfo.setEndDate2Str(DateUtils.format(workDay,"yyyy-MM-dd"));
				}else{
					acInfo.setEndDateStr(DateUtils.format(acInfo.getEndDate(),"yyyy-MM-dd"));
				}
				EquityMaintenanceDTO equityMaintenanceDTO = iEquityMaintenanceService.queryById(corpDTOMap.get(acInfo.getCorpId()).getEquityMaintenanceId());
				acInfo.setCompanyTYpeName(equityMaintenanceDTO.getEquityName());
				acInfo.setXbMoney(acInfo.getBillMoney().subtract(acInfo.getBankAccBalance()));
				sendMsg(acInfo,AccebillConstant.BRIEF_STATUS_6120_2);
				sendNum++;
			}
			this.logger.info("---承兑账户资金余额不足承兑扣款消息提醒，【"+sendNum+"】条数据。");
		}
		this.logger.info("---承兑账户资金余额不足承兑扣款消息提醒，结束执行定时任务---");
	}
}
