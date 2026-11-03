package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbReceiveBillLockDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbBillDiscountApplyVO;
import com.bytter.bill.domain.bill.vo.AbReceiveBillLockVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.*;
/**
 * @Title: Service
 * @Description: 票据锁定/解锁
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Service("abReceiveBillLockService")
@Transactional
public class AbReceiveBillLockService extends BaseService<AbVoucherinfo, String> implements IAccebillBaseService {
	@Autowired
	private AbReceiveBillLockDao abReceiveBillLockDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoService abVoucherinfoService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	public BaseDao<AbVoucherinfo, String> getEntityDao() {
		return abReceiveBillLockDao;
	}
	/**
	 * 加载票据锁定/解锁数据
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbReceiveBillLock(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_BILLLOCK_QUERY,dataTablesInput.getCurrentUserId());
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_RESOURCE_BILLLOCK_QUERY,corpIds);
		DataTablesOutput<AbReceiveBillLockVo> dataTablesOutput = abReceiveBillLockDao.listAbReceiveBillLock(dataTablesInput, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
					, "AB_BILL_AbReceiveBillLock_STATUS", "AB_ENTER_STOCK", "AB_BILL_OPENWAY", "abReceiveBillLock.isLock"
					, "AB_BRIEF_STATUS", "AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			for (AbReceiveBillLockVo bill : listData) {
				injectValues(bill, listDictMap);
				if(StrUtil.isNotBlank(bill.getApplyCorpId())){
					corpIdSet.add(bill.getApplyCorpId());
				}
				if(StrUtil.isNotBlank(bill.getCorpId())){
					corpIdSet.add(bill.getCorpId());
				}
				if(StrUtil.isNotBlank(bill.getBusCorpId())){
					corpIdSet.add(bill.getBusCorpId());
				}
			}
		});
		if(corpIdSet.size() <= 0){
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesOutput;
		}
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbReceiveBillLockVo bill : listData){
				CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
				CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
				CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
				bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
				bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
				bill.setCorpIdName(corpDTO2 == null ? null : corpDTO2.getName());
				bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
				bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
			}
		});
		return dataTablesOutput;
	}
	private void injectValues(AbReceiveBillLockVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
		// 票据类型：10 银票、11 商票
		Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
					.ifPresent(dict -> bill.setBillTypeName(dict.getName()));
		});
		// 流通介质：10 纸票、11 电票
		Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
					.ifPresent(dict -> bill.setBillFlagName(dict.getName()));
		});
		// 开出方式：0  对外开出；1 对内自开
		Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
					.ifPresent(dict -> bill.setInsideSingName(dict.getName()));
		});
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbReceiveBillLock_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 业务状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 是否锁定
		Optional.ofNullable(bill.getLockSign()).ifPresent(lockSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("abReceiveBillLock.isLock");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(lockSign)))
					.ifPresent(dict -> bill.setLockSignName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 是否发送指令标志：0否； 1是
		Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
					.ifPresent(dict -> bill.setSendSignName(dict.getName()));
		});
		//出票标志: 1 新系统出票 、0 老系统出票
		Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
					.ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
		});
		//是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
		Optional.ofNullable(bill.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
					.ifPresent(dict -> bill.setBlgrdbagcrclPrmtIndName(dict.getName()));
		});
	}
	/**
	 * 锁定
	 * 
	 * @return
	 * @throws ParseException
	 */
	public void lock(AbReceiveBillLockVo back, ICurrentUser userVo) {
		batchLockOrUnLock(back, AccebillConstant.BRIEF_STATUS_6007, false, userVo);
	}
	/**
	 * 解锁
	 * 
	 * @return
	 * @throws ParseException
	 */
	public void unLock(AbReceiveBillLockVo back, ICurrentUser userVo) {
		batchLockOrUnLock(back, AccebillConstant.BRIEF_STATUS_6008, false, userVo);
	}
	/**
	 * 批量锁定
	 * 
	 * @return
	 * @throws ParseException
	 */
	public void batchLock(AbReceiveBillLockVo back, ICurrentUser userVo) {
		batchLockOrUnLock(back, AccebillConstant.BRIEF_STATUS_6007, true, userVo);
	}
	/**
	 * 批量解锁
	 * 
	 * @return
	 * @throws ParseException
	 */
	public void batchUnLock(AbReceiveBillLockVo back, ICurrentUser userVo) {
		try {
			batchLockOrUnLock(back, AccebillConstant.BRIEF_STATUS_6008, true, userVo);
		}catch (BusinessException e){
			if (!(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR.equals(e.getIMessageEnum()))) {
				throw new BusinessException(e.getIMessageEnum());
			}
		}
	}
	/**
	 * 批量锁定或者解锁操作
	 */
	public void batchLockOrUnLock(AbReceiveBillLockVo back, String briefId, boolean isBatch, ICurrentUser userVo) {
		// 获取需要修改的条数
		String[] aids = back.getIds().split(",");
		String[] vvids = back.getVvids().split(",");
		int len = aids.length;
		// 批量插入流水表数据
		List<AbAccrecords> abList = getAbAccrecords(aids, briefId, back,userVo);
		int num = 0;
		String validSignNew = AccebillConstant.BRIEF_STATUS_6007.equals(briefId) ? AccebillConstant.AB_BILL_VALID_SIGN_0
				: AccebillConstant.AB_BILL_VALID_SIGN_1;
		String validSignOld = AccebillConstant.BRIEF_STATUS_6008.equals(briefId) ? AccebillConstant.AB_BILL_VALID_SIGN_0
				: AccebillConstant.AB_BILL_VALID_SIGN_1;
		// 更改从表的valid_sign
		num = abVoucherinfoViceService.batchUpdateValidSign(vvids, validSignNew, validSignOld, userVo);
//		if (num != len) {
//			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//		}

		String logType = isBatch ? AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT : AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
		String msg = isBatch ? AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC : AccebillConstant.LANG_LOG_SUBMIT_SUCC;
		if (AccebillConstant.BRIEF_STATUS_6007.equals(briefId)) {
			msg = back.getReason();
		} else if (AccebillConstant.BRIEF_STATUS_6008.equals(briefId)) {
			// 解锁需要把之前锁定的流水变为失效
			num = abAccrecordsService.setAbAccrecordsValidSign2ByLastIdAndValidSign1(aids, userVo);//update ab_accrecords
			if (num != len) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
			}
		}
		num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
		if (num != len) {
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
		}
		abLogService.batchSaveAbLog(vvids, aids, briefId, logType, msg, userVo);
	}
	/**
	 * 获取流水信息
	 * 
	 * @param back
	 * @param flag
	 *            true:审批通过 false:审批中
	 * @return
	 */
	@Transactional(readOnly = true)
	protected List<AbAccrecords> getAbAccrecords(String[] aids, String briefId, AbReceiveBillLockVo back, ICurrentUser userVo) {
		List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
		for (String id : aids) {
			// 获取流水表信息
			AbAccrecords abAccrecords = abAccrecordsService.get(id);
			abBillNotificationService.completeMsg(abAccrecords,userVo);
			// 修改从表的BACK_BRIEF_ID为空
			abVoucherinfoViceService.updateBackBriefIdIsNull(abAccrecords.getVvid());
			// 获取当前票据主，从表信息
			AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid());
			AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
			// 新的流水数据
			AbAccrecords entity = new AbAccrecords();
			// 票据主表ID
			entity.setVid(ab.getId());
			// 票据从表ID
			entity.setVvid(abVice.getId());
			entity.setVouInfo(ab);
			entity.setVouViceInfo(abVice);
			if (AccebillConstant.BRIEF_STATUS_6008.equals(briefId)) {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
			} else if (AccebillConstant.BRIEF_STATUS_6007.equals(briefId)) {
				entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
			}
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
			entity.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);
			entity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
			// 业务类型
			entity.setBriefId(briefId);
			entity.setLastid(id);
			entity.setLastbillcode(ab.getBillStatus());
			entity.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
			// 申请日期
			entity.setBusinessDate(back.getDate());// 业务归属日期
			entity.setApplyDate(new Date());// 申请日期
			entity.setRmk(back.getReason());
			abList.add(entity);
		}
		return abList;
	}
	@Override
	public AbVoucherinfoVO getAbBillInfoById(String aid) {
		// todo:未实现
		return null;
	}
}
