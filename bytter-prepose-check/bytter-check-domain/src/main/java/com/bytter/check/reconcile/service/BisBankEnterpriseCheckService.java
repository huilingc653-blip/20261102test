package com.bytter.check.reconcile.service;

import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.check.reconcile.dao.BisBankEnterpriseCheckDao;
import com.bytter.check.reconcile.dao.BisCheckDtlDao;
import com.bytter.check.reconcile.dao.BisCheckExcDao;
import com.bytter.check.reconcile.entity.BisCheckExc;
import com.bytter.check.reconcile.vo.BisBankEnterpriseCheckVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("bisBankEnterpriseCheckService")
@Transactional
public class BisBankEnterpriseCheckService extends BaseService<BisBankEnterpriseCheckVo, String> {
    @Autowired
    private BisBankEnterpriseCheckDao bisBankEnterpriseCheckDao;

    @DubboReference(check = false)
	private ICorpService  iCorpService;

    @Autowired
    private BisCheckExcDao bisCheckExcDao;

    @Autowired
    private BisCheckDtlDao bisCheckDtlDao;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;

	@DubboReference(check = false)
	private IDictService dictService;

	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		List<String> corpAuthList = iCorpService.getUserCorpIds(ReceiptConstant.BIS_BANK_ENTERPRISE_CHECK_QUERY, input.getCurrentUserId());

		String insBankTypeId = input.getFormdata().get("ins_bankTypeId") + "";
		input.getFormdata().remove("ins_bankTypeId");
		Map<String,Object> params = new HashMap<>();
		String joinSql = " and acc.bank_type_id=:insBankTypeId ";
		params.put("insBankTypeId", insBankTypeId);
		List<String> bankAccList = iBtBankAccService.getBankAccsByCorpIdList(corpAuthList, joinSql, params);
		DataTablesOutput<BisBankEnterpriseCheckVo> data = bisBankEnterpriseCheckDao.getDataTablesUnion(input, corpAuthList, bankAccList);
		Optional.ofNullable(data.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("btBankAcc.accType", "bisExc.voucherStat",
					"BIS_CHECK_REASON_SCOPE", "bisCheckDtl.mark", "bisCheckDtl.status");
			listData.forEach(item -> injectValues(listDictMap, item));

			// T24090015094 银企对账功能优化：银企对账针对招商银行增加字段【对账账户】以及【是否保证金】用于对账
			listData.forEach(item -> {
				String expandFields = item.getExpandFields();
				if (StringUtils.isNotEmpty(expandFields)) {
					String[] split = expandFields.split("\\|");// 大参林该字段格式以“|”隔开
					// 【对账账户】字段取expandFields第三个值
					if (split.length >= 3 && StringUtils.isNotEmpty(split[2])) {
						item.setCheckAccount(split[2]);
					} else {
						// 如果该字段没有值，则按bank_acc取值显示，即与银行账号一致
						item.setCheckAccount(item.getBankAcc());
					}
					// 【是否保证金】字段取expandFields第五个值，判断如值为Y，则显示“是”，该值为N或者没有值，则显示“否”。
					if (split.length >= 5 && StringUtils.isNotEmpty(split[4])) {
						if ("Y".equals(split[4].toUpperCase())) {
							item.setIsBail(CommonConstant.YES);
						} else {
							item.setIsBail(CommonConstant.NO);
						}
					} else {
						item.setIsBail(CommonConstant.NO);
					}
				} else {
					// 如果该字段没有值，则按bank_acc取值显示，即与银行账号一致
					item.setCheckAccount(item.getBankAcc());
					// 如果该字段没有值，则显示“否”
					item.setIsBail(CommonConstant.NO);
				}
			});

		});
		return data;
	}

	/**
	 * 辅助方法：注入字典表数据。
	 *
	 * @param listDictMap 字典表数据。
	 * @param item        注入对象。
	 */
	private static void injectValues(Map<String, Map<String, DictDTO>> listDictMap, BisBankEnterpriseCheckVo item) {
		Optional.ofNullable(item.getAccType()).flatMap(accType ->
						Optional.ofNullable(listDictMap.get("btBankAcc.accType"))
								.flatMap(dictMap -> Optional.ofNullable(dictMap.get(accType))))
				.ifPresent(dict -> item.setAccTypeName(dict.getName()));
		Optional.ofNullable(item.getVoucherStat()).flatMap(voucherStat ->
						Optional.ofNullable(listDictMap.get("bisExc.voucherStat"))
								.flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherStat))))
				.ifPresent(dict -> item.setVoucherStatName(dict.getName()));
		Optional.ofNullable(item.getAppScope()).flatMap(appScope ->
						Optional.ofNullable(listDictMap.get("BIS_CHECK_REASON_SCOPE"))
								.flatMap(dictMap -> Optional.ofNullable(dictMap.get(appScope))))
				.ifPresent(dict -> item.setAppScopeName(dict.getName()));
		Optional.ofNullable(item.getCheckResult()).flatMap(checkResult ->
						Optional.ofNullable(listDictMap.get("bisCheckDtl.mark"))
								.flatMap(dictMap -> Optional.ofNullable(dictMap.get(checkResult))))
				.ifPresent(dict -> item.setCheckResultName(dict.getName()));
		Optional.ofNullable(item.getStatus()).flatMap(status ->
						Optional.ofNullable(listDictMap.get("bisCheckDtl.status"))
								.flatMap(dictMap -> Optional.ofNullable(dictMap.get(status))))
				.ifPresent(dict -> item.setStatusName(dict.getName()));
	}


	@SuppressWarnings("rawtypes")
	public DataTablesOutput getCheckBusData(DataTablesInput input) {
	  	List<String> corpAuthList = iCorpService.getUserCorpIds(ReceiptConstant.BIS_CHECK_BUS_QUERY,input.getCurrentUserId());
		String bankTypeId=  input.getFormdata().get("ins_bankTypeId")+"";
		input.getFormdata().remove("ins_bankTypeId");
      	DataTablesOutput<BisBankEnterpriseCheckVo> data = bisBankEnterpriseCheckDao.getCheckBusData(input, corpAuthList,bankTypeId);
		Optional.ofNullable(data.getData()).ifPresent(listData -> {
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("btBankAcc.accType", "bisExc.voucherStat",
					"BIS_CHECK_REASON_SCOPE", "bisCheckDtl.mark", "bisCheckDtl.status");
			listData.forEach(item -> injectValues(listDictMap, item));

			// T24090015094 银企对账功能优化：银企对账针对招商银行增加字段【对账账户】以及【是否保证金】用于对账
			listData.forEach(item -> {
				String expandFields = item.getExpandFields();
				if (StringUtils.isNotEmpty(expandFields)) {
					String[] split = expandFields.split("\\|");// 大参林该字段格式以“|”隔开
					// 【对账账户】字段取expandFields第三个值
					if (split.length >= 3 && StringUtils.isNotEmpty(split[2])) {
						item.setCheckAccount(split[2]);
					} else {
						// 如果该字段没有值，则按bank_acc取值显示，即与银行账号一致
						item.setCheckAccount(item.getBankAcc());
					}
					// 【是否保证金】字段取expandFields第五个值，判断如值为Y，则显示“是”，该值为N或者没有值，则显示“否”。
					if (split.length >= 5 && StringUtils.isNotEmpty(split[4])) {
						if ("Y".equals(split[4].toUpperCase())) {
							item.setIsBail(CommonConstant.YES);
						} else {
							item.setIsBail(CommonConstant.NO);
						}
					} else {
						item.setIsBail(CommonConstant.NO);
					}
				} else {
					// 如果该字段没有值，则按bank_acc取值显示，即与银行账号一致
					item.setCheckAccount(item.getBankAcc());
					// 如果该字段没有值，则显示“否”
					item.setIsBail(CommonConstant.NO);
				}
			});

		});
      	return data;
    }
	/**
	 * 手工同步
	 *  @param bean 银企对账vo
	 *  @param currentUser 操作用户
	 */
	public void manualSynchronize(BisBankEnterpriseCheckVo bean, ICurrentUser currentUser){
		bean.setVoucherStat(BdlinkConstant.PRE_SEND);
		bean.setBusType(ReceiptConstant.BIS_BANK_BUS_TYPE_1);
		//设置账单日期为所在账单日期月的最后一天
		Date lastDate= DateUtils.getLastMonthDate(bean.getDateStart());
		bean.setDateStart(lastDate);
		bean.setDateEnd(lastDate);
		List<String> corpIds;
		if (StringUtils.isNotBlank(bean.getCorpId())) {
			corpIds = Arrays.asList(bean.getCorpId().split(","));
		}
		//单位为空时为全选
		else {
			//获取操作用户有操作权限的单位
			corpIds = iCorpService.getUserCorpIds(ReceiptConstant.BIS_BANK_ENTERPRISE_CHECK_OPT,currentUser.getId());
		}
		//根据所选择的单位+银行类别下的所有直联账号(界面银行账号为空时)来生成指令
		if(corpIds != null && !corpIds.isEmpty()){
			BisBankEnterpriseCheckVo checkVo = new BisBankEnterpriseCheckVo();
			//账号查询对象
			AccountSearchDTO accountSearchDTO = new AccountSearchDTO();
			//银行类型id
			accountSearchDTO.setBankTypeId(bean.getBankTypeId());
			//币别
			accountSearchDTO.setCurId(bean.getCurId());
			for (String corpId: corpIds) {
				//复制
				BeanUtils.copyNotNullProperties(bean, checkVo);
				//如果银行账号为空时
				if(StringUtils.isNotBlank(bean.getBankAcc())){
					accountSearchDTO.setBankAcc(bean.getBankAcc());
				}
				//单位id
				accountSearchDTO.setCorpId(corpId);
				//符合条件的账号
				List<AccountDTO> accountDTOS = listBy(accountSearchDTO,currentUser);
				if(accountDTOS != null && !accountDTOS.isEmpty()){
					for (AccountDTO accountDTO : accountDTOS){
						checkVo.setBankAcc(accountDTO.getBankAcc());
						checkVo.setCorpId(corpId);
						//生成发送指令
						sendExc(checkVo, currentUser);
					}
				}
			}
		}
	}
	/**
	 * 根据单位、账号类型、权限，查询帐号列表
	 * zhenghj 2020-10-22
	 *
	 * @param accountSearchDTO
	 * @return
	 */
	public List<AccountDTO> listBy(AccountSearchDTO accountSearchDTO, ICurrentUser user) {
		//查询类型 银行账号
		accountSearchDTO.setAccountType(AccountGrantConstant.ONLINE_BANK_TYPE);
		//查询明细的权限
		accountSearchDTO.setPermission(AccountGrantConstant.QUERY_BALANCE);
		//查询结果是否包含已注销账户
//		accountSearchDTO.setContainDelBankAcc(false);
		//操作用户
		accountSearchDTO.setUserId(user.getId());
		return accountGrantService.listBy(accountSearchDTO);
	}
	/**
	 * 生成指令
	 * @param bean
	 * @return
	 */
	public void sendExc(BisBankEnterpriseCheckVo bean, ICurrentUser currentUser) {
		if(StringUtils.isNotEmpty(bean)){
			BisCheckExc bisCheckExc = new BisCheckExc();
			bisCheckExc.setCorpId(bean.getCorpId());
			bisCheckExc.setDtlId(bean.getDtlId());
			bisCheckExc.setCurId(bean.getCurId());
			bisCheckExc.setBankTypeId(bean.getBankTypeId());
			bisCheckExc.setBeginDate(bean.getDateStart());
			bisCheckExc.setEndDate(bean.getDateEnd());
			bisCheckExc.setBusType(bean.getBusType());
			bisCheckExc.setCurId(bean.getCurId());
			bisCheckExc.setBankAcc(bean.getBankAcc());
			bisCheckExc.setCbsBillNo(bean.getBillNo());
			bisCheckExc.setCheckResult(bean.getOpt());
			bisCheckExc.setCheckDate(bean.getDateStart());
			bisCheckExc.setDtlId(bean.getId());
			bisCheckExc.setVoucherStat(bean.getVoucherStat());
			bisCheckExc.setBal(bean.getBal());
			bisCheckExc.setCreateDate(new Date());
			bisCheckExc.setCreateBy(currentUser.getLoginName());
			//设置拓展字段（用于存储银行返回要素）
			bisCheckExc.setExpandFields(bean.getExpandFields());
			bisCheckExc.setRemark(bean.getRmk());
			bisCheckExcDao.save(bisCheckExc);
		}
	}
	public AjaxJson opSure(BisBankEnterpriseCheckVo bean, ICurrentUser currentUser) {
		AjaxJson reJson = new AjaxJson();
		reJson.setSuccess(false);
		if(StringUtils.isNotEmpty(bean)){
			Map<String, Object> beanParams = new HashMap<String, Object>();
			beanParams.put("ids", Arrays.asList(bean.getIds().split(",")));
			List<BisBankEnterpriseCheckVo> liseBean = bisBankEnterpriseCheckDao.queryList(beanParams);
			if(ListUtils.isNotNullOrEmpty(liseBean)){
				for(BisBankEnterpriseCheckVo vo:liseBean){
					//生成新指令
					vo.setVoucherStat(BdlinkConstant.PRE_SEND);
					vo.setBusType(ReceiptConstant.BIS_BANK_BUS_TYPE_2);
					vo.setDateStart(vo.getToBillDate());
					vo.setDateEnd(vo.getToBillDate());
					vo.setOpt(bean.getOpt());
					vo.setCheckDate(bean.getToBillDate());
					vo.setRmk(bean.getRmk());
					this.sendExc(vo, currentUser);

					//修改当前流水状态状态为3准备发送
					Map<String,Object> params  = new HashMap<String, Object>();
					params.put("updateDate", new Date());
					params.put("updateBy", currentUser.getLoginName());
					params.put("voucherStat", BdlinkConstant.PRE_SEND);
					params.put("ids", vo.getId());
					params.put("mobileNo", bean.getMobileNo());
					params.put("rmk", bean.getRmk());
					params.put("checkDate",new Date());
					params.put("reasonId", bean.getRids());
					params.put("checkResult", bean.getOpt());
					bisCheckDtlDao.updateVoucherStatByIds(params);
				}
			}
			reJson.setSuccess(true);
		}
		return reJson;
	}
	@Override
	public BaseDao<BisBankEnterpriseCheckVo, String> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}

}
