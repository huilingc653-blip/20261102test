package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillDiscountReleaseDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
import com.bytter.bill.domain.bill.vo.AbBillDiscountApplyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Title: Service
 * @Description:贴现额度释放Service
 *
 */
@Service("abBillDiscountReleaseService")
@Transactional
@DubboService
public class AbBillDiscountReleaseService extends BaseService<AbBillDiscountAccountVO, String>
		implements IUserExcelVerifyHandler<AbBillDiscountAccountVO> {
 
	@Autowired
	private AbBillDiscountReleaseDao abBillDiscountReleaseDao;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbRatUrationService abRatUrationService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private IRatUrationService ratUrationService;
	@Override
	public BaseDao<AbBillDiscountAccountVO, String> getEntityDao() {
		return abBillDiscountReleaseDao;
	}
	/**
	 * 列表显示贴现申请信息
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillDiscountAccount(DataTablesInput input) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTRELEASE_QUERY,input.getCurrentUserId());
		String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTRELEASE_QUERY,corpIds);
		List<String> billIdList = ratUrationService.queryRationBill();
		DataTablesOutput<AbBillDiscountAccountVO> dataTablesOutput = abBillDiscountReleaseDao.listAbBillDiscountAccount(input, corpAuthList);
		List<AbBillDiscountAccountVO> resultData = Lists.newArrayList();
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillDiscountAccountVO listDatum : listData) {
				if ( !billIdList.contains(listDatum.getRecordId())){
					resultData.add(listDatum);
				}
			}
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "ABBILL_DISCOUNTCOLLECT_STATUS"
					, "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM",  "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_ABBILLDISCOUNT_RELEASE_STATUS" );
			for (AbBillDiscountAccountVO bill : resultData){
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
			for (AbBillDiscountAccountVO bill : listData){
				CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
				CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
				CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
				bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
				bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
				bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
				bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
				bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
			}
		});
		return dataTablesOutput;
	}
	private void injectValues(AbBillDiscountAccountVO bill,Map<String, Map<String, DictDTO>> listDictMap){
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
		// 票据状态
		Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> bill.setBriefIdName(dict.getName()));
		});
		// 票据业务状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("ABBILL_DISCOUNTCOLLECT_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 所属库存
		Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
					.ifPresent(dict -> bill.setEnterStockName(dict.getName()));
		});
		// 不得转让标记
		Optional.ofNullable(bill.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
					.ifPresent(dict -> bill.setBanendrsmtmkName(dict.getName()));
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

		Optional.ofNullable(bill.getReleaseState()).ifPresent(releaseState ->{
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ABBILLDISCOUNT_RELEASE_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(releaseState)))
					.ifPresent(dict -> bill.setReleaseStateName(dict.getName()));
		});
	}
	/**
	 * 批量释放贴现申请
	 * 
	 * @param abBillDiscountAccountVO
	 * @return
	 */
	public boolean batchSubmitAbBillDiscountAccount(
			AbBillDiscountAccountVO abBillDiscountAccountVO, ICurrentUser userVo) {
		try {
			String accIds = abBillDiscountAccountVO.getAccIds();
			String viceIds = abBillDiscountAccountVO.getViceIds();
			String[] viceIdsArrays = viceIds.split(",");
			String[] accIdArrays = accIds.split(",");
			for (int i = 0; i < accIdArrays.length && i < viceIdsArrays.length; i++) {
				signRelease(viceIdsArrays[i], accIdArrays[i], userVo);
			}
		} catch (RuntimeException e) {
			return false;
		}
		return true;
	}
	/*
	 * 单笔流水释放
	 */
	public boolean signRelease(String vvid, String id, ICurrentUser userVo) {
		boolean flag = true;
		try {
			// 修改从表状态
			AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService
					.get(vvid);
			// 授信额度释放
			AbAccrecords abAccrecords = abAccrecordsService.get(id);
			abAccrecords.setVouViceInfo(abVoucherinfoVice);
			abRatUrationService.opRatUration(abAccrecords,
					AccebillConstant.BILL_OPERATE_REPULSE, userVo);
			abLogService.saveAbLog(abVoucherinfoVice.getId(), 
					abAccrecords.getId(), AccebillConstant.BRIEF_STATUS_6012,
					AccebillConstant.ABLOG_LOGTYPE_ISDISCOUNTCREDIT,
					AccebillConstant.AB_ADD_BANK_DATA_RELEASE, userVo);
			abVoucherinfoVice.setRatmodel("2");
			abVoucherinfoViceService.update(abVoucherinfoVice);
			
			
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(AbBillDiscountAccountVO obj,
			Object... user) {
		return null;
	}
	@Override
	public void verifyHandler(List<AbBillDiscountAccountVO> list,
			Object... user) {
		
	}
}
