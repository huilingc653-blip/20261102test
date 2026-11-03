package com.bytter.biz.inneraccount.service;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccUnFreezePageVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankAccFreezeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC_FREEZE
 * @date 2017-06-14 14:26:46
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_UNFREEZE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.BT_BANKACC_UNFREEZE)
@Service("btBankAccUnFreezeService")
@Transactional
public class BtBankAccUnFreezeService implements IWorkFlowListener {
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBtBankAccFreezeService ibtBankAccFreezeService;
	@DubboReference(check = false)
	private IBtBankAccUnFreezeService btBankAccUnFreezeServiceI;
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@DubboReference(check = false)
	private IDictService dictService;

	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;

	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		List<BtBankAccDTO> bankAccs = accountGrantService.listByMultiVal(corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_QUERY, user.getId()),
				AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
		if (bankAccs == null) {
			List<BtBankAccFreezeVO> listVO = new ArrayList<>();
			return new DataTablesOutput(dataTablesInput.getDraw(), listVO, 0L, 0L);
		}
		Set<String> list = new HashSet<>();
		for (BtBankAccDTO acc : bankAccs) {
			list.add(acc.getBankAcc());
		}
		List<String> bankAccStrings = new ArrayList<>(list);
		String bankAccSql = SqlUtil.getInSql(bankAccStrings, "acc.bank_acc", true);
		sql.append(" and ").append(bankAccSql);
		return btBankAccUnFreezeServiceI.getDataTablesUnion(dataTablesInput,sql.toString());
	}
	public void toAddFreeze(BtBankAccFreezeVO vo, Model model) {
		BtBankAccDTO accVO = btBankAccCommonService.getBtBankAccVOById(vo.getBankAccId(), vo.getCurId());
		List<BtBankAccFreezeDTO> list = btBankAccUnFreezeServiceI.getBtBankAccFreeze(vo.getBankAccId(), vo.getCurId());
		List<BtBankAccFreezeDTO> tempList = btBankAccUnFreezeServiceI.getBtBankAccFreezeTemp(vo.getBankAccId(), vo.getCurId());
		/*** 得到账户冻结金额*/
		BigDecimal freezeNum = btBankAccUnFreezeServiceI.getFreezeMoney(vo.getBankAccId(), vo.getCurId());
		/*** 得到账户解冻金额*/
		BigDecimal ufFreezeNum = btBankAccUnFreezeServiceI.getUfFreezeMoney(vo.getBankAccId(), vo.getCurId());
		if (!EmptyUtils.isEmpty(tempList)) {
			for (int i = 0; i < tempList.size(); i++) {
				tempList.get(i).setUfFreezeMoney(ufFreezeNum);
				tempList.get(i).setUfavalBal(freezeNum.subtract(ufFreezeNum));
			}
		}
		accVO.setAccTypeName(dictService.getByName("btBankAcc.accType",accVO.getAccType()));
		toFreeze(vo, list, accVO.getBankAcc());
		model.addAttribute("accVO", accVO);
		model.addAttribute("btBankAccFreezeVO", vo);
		model.addAttribute("tempList", tempList);
	}

	public BtBankAccUnFreezePageVO getAccInfoBeforeUnFreeze(String bankAccId, String curId,String fuuid) {
		BtBankAccDTO accVO = setVoParams(bankAccId,curId);
		List<BtBankAccFreezeDTO> list = btBankAccUnFreezeServiceI.getBtBankAccFreeze(bankAccId,curId);
		List<BtBankAccFreezeDTO> tempList = btBankAccUnFreezeServiceI.getBtBankAccFreezeTemp(bankAccId,curId);
		/*** 得到账户冻结金额*/
		BigDecimal freezeNum = btBankAccUnFreezeServiceI.getFreezeMoney(bankAccId,curId);
		/*** 得到账户解冻金额*/
		BigDecimal ufFreezeNum = btBankAccUnFreezeServiceI.getUfFreezeMoney(bankAccId,curId);
		if (!EmptyUtils.isEmpty(tempList)) {
			for (int i = 0; i < tempList.size(); i++) {
				tempList.get(i).setUfFreezeMoney(ufFreezeNum);
				tempList.get(i).setUfavalBal(freezeNum.subtract(ufFreezeNum));
			}
		}
		accVO.setAccTypeName(dictService.getByName("btBankAcc.accType",accVO.getAccType()));
		BtBankAccFreezeVO btBankAccFreezeVO = new BtBankAccFreezeVO();
		btBankAccFreezeVO.setBankAccId(bankAccId);
		btBankAccFreezeVO.setCurId(curId);
		btBankAccFreezeVO.setFUuid(fuuid);
		toFreeze(btBankAccFreezeVO, list, accVO.getBankAcc());
		BtBankAccUnFreezePageVO btBankAccUnFreezePageVO = new BtBankAccUnFreezePageVO();
		btBankAccUnFreezePageVO.setAccVO(accVO);
		btBankAccUnFreezePageVO.setBtBankAccFreezeVO(btBankAccFreezeVO);
		//暂未看到使用
//		model.addAttribute("tempList", tempList);
		return btBankAccUnFreezePageVO;
	}


	private BtBankAccDTO setVoParams(String bankAccId,String curId) {
		BtBankAccDTO accVO = ibtBankAccFreezeService.getAccVOById(bankAccId,curId);
		String corpName  = corpService.getCorpName(accVO.getCorpId());
		if(StringUtils.isNotBlank(corpName)){
			accVO.setCorpName(corpName);
		}
		BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(accVO.getCurId());
		if(null != btCurrencyDTO){
			accVO.setCurName(btCurrencyDTO.getCurName());
		}
		String accTypeName = dictService.getByName("btBankAcc.accType",accVO.getAccType());
		if(StringUtils.isNotBlank(accTypeName)){
			accVO.setAccTypeName(accTypeName);
		}

		//开户日期处理
		if (accVO.getRegDate() != null){
			Date regDate = accVO.getRegDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = sdf.format(regDate);
			accVO.setRegDateStr(format);
		}
		return accVO;
	}

	public void toFreeze(BtBankAccFreezeVO vo, List<BtBankAccFreezeDTO> list, String bankAcc) {
		/** 冻结次数*/
		int freezeCount = 0;
		/** 解冻次数*/
		int unfreezeCount = 0;
		/**  冻结金额合计*/
		BigDecimal freezeMoneySum = new BigDecimal(0);
		/** 解冻金额合计*/
		BigDecimal unFreezeMoneySum = new BigDecimal(0);
		/** 计算冻结金额和解冻金额*/
		for (BtBankAccFreezeDTO bankAccFreeze : list) {
			if ("0".endsWith(bankAccFreeze.getUfFlag())) {
				freezeCount++;
				freezeMoneySum = freezeMoneySum.add(bankAccFreeze.getFreezeMoney());
			} else if ("1".endsWith(bankAccFreeze.getUfFlag())) {
				unfreezeCount++;
				unFreezeMoneySum = unFreezeMoneySum.add(bankAccFreeze.getFreezeMoney());
			}
		}
		if (!EmptyUtils.isBlank(vo.getfUuid())) {
			/**冻结金额*/
			BigDecimal freezeMoney = btBankAccUnFreezeServiceI.getFreezeMoney(vo.getfUuid());
			/**解冻暂存金额和解冻成功的金额*/
			BigDecimal ufFreezeMoney = btBankAccUnFreezeServiceI.getUfFreezeMoney(vo.getBankAccId(), vo.getCurId(), vo.getfUuid());
			vo.setUfFreezeMoney(ufFreezeMoney);
			vo.setUfavalBal(freezeMoney.subtract(ufFreezeMoney));
		}
		/** 冻结金额合计*/
		vo.setFreezeMoneySum(freezeMoneySum);
		/** 解冻金额合计*/
		vo.setUnFreezeMoneySum(unFreezeMoneySum);
		/** 冻结次数*/
		vo.setFreezeCount(freezeCount);
		/** 解冻次数*/
		vo.setUnfreezeCount(unfreezeCount);
		/**余额*/
		BisAccBalDTO balDto = bisAccBalService.getLastBisAccBalBy(bankAcc, vo.getCurId());
		//这里的余额计算 有问题 因为账户可用余额已经减去了系统冻结金额
//		balDto.setBal(balDto.getBal().add(balDto.getFrzBal()).add(balDto.getSysFrzBal()));
		if(!EmptyUtils.isEmpty(balDto)){
			vo.setBal(balDto.getBal());
			BigDecimal availBal = balDto.getAvailBal();
//			vo.setAvalBal(availBal);
			availBal = availBal.add(balDto.getFrzBal()).add(balDto.getSysFrzBal());
			vo.setAvalBal(availBal.add(unFreezeMoneySum).subtract(freezeMoneySum));
//			for(int i = 0;i< tempList.size();i++){
//				vo.setAvalBal(vo.getAvalBal().subtract(tempList.get(i).getFreezeMoney()));
//			}
		}
	}
	/**
	 * 修改
	 *
	 * @param vo
	 * @param model
	 */
	public void toUpdateFreeze(BtBankAccFreezeVO vo, Model model) {
		BtBankAccDTO accVO = btBankAccCommonService.getBtBankAccVOById(vo.getBankAccId(), vo.getCurId());
		List<BtBankAccFreezeDTO> list = btBankAccUnFreezeServiceI.getBtBankAccFreeze(vo.getBankAccId(), vo.getCurId());
		toFreeze(vo, list, accVO.getBankAcc());
		model.addAttribute("accVO", accVO);
		model.addAttribute("btBankAccFreezeVO", vo);
	}

	public void toUpdateFreeze(BtBankAccFreezeVO vo, BtBankAccUnFreezePageVO pageVO){
		BtBankAccDTO accVO = btBankAccCommonService.getBtBankAccVOById(vo.getBankAccId(), vo.getCurId());
		List<BtBankAccFreezeDTO> list = btBankAccUnFreezeServiceI.getBtBankAccFreeze(vo.getBankAccId(), vo.getCurId());
		toFreeze(vo, list, accVO.getBankAcc());
		pageVO.setAccVO(accVO);
		pageVO.setBtBankAccFreezeVO(vo);
	}
	/**
	 * 得到解冻暂存
	 *
	 * @param bankAccId
	 * @param curId
	 * @return
	 */
	public List<BtBankAccFreezeDTO> getTempFreeze(String bankAccId, String curId) {
		List<BtBankAccFreezeDTO> tempList = btBankAccUnFreezeServiceI.getBtBankAccFreezeTemp(bankAccId, curId);
		if (!EmptyUtils.isEmptyList(tempList)) {
			for (int i = 0; i < tempList.size(); i++) {
				if (!EmptyUtils.isBlank(tempList.get(i).getFUuid())) {
					/**冻结金额*/
					BigDecimal freezeMoney = btBankAccUnFreezeServiceI.getFreezeMoney(tempList.get(i).getFUuid());
					BtBankAccFreezeDTO dto = ibtBankAccFreezeService.getBtBankAccFreezeDTO(tempList.get(i).getFUuid());
					/**解冻成功的金额*/
					BigDecimal ufFreezeMoney = btBankAccUnFreezeServiceI.getUfFreezeMoney(tempList.get(i).getBankAccId(), tempList.get(i).getCurId(), tempList.get(i).getfUuid());
					BigDecimal unWaitMoney = btBankAccUnFreezeServiceI.getUfWaitFreezeMoney(tempList.get(i).getBankAccId(), tempList.get(i).getCurId(), tempList.get(i).getfUuid());
					tempList.get(i).setUfFreezeMoney(ufFreezeMoney);
					tempList.get(i).setUnwaitMoney(unWaitMoney);
					tempList.get(i).setUfavalBal(freezeMoney.subtract(ufFreezeMoney).subtract(unWaitMoney));
					tempList.get(i).setUnfreezeNo(dto.getFreezeNo());
				}
				tempList.get(i).setStatusName(dictService.get("bisExc.status",tempList.get(i).getStatus()));
			}
		}
		return tempList;
	}
	public void toSubmit(String ids, ICurrentUser user) {
		String[] idStr = ids.split(",");
		for (int i = 0; i < idStr.length; i++) {
			BtBankAccFreezeDTO btBankAccFreeze = btBankAccUnFreezeServiceI.getObject(idStr[i]);
			btBankAccFreeze.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
			btBankAccFreeze.setFreezeDate(new Date());
			btBankAccFreeze.setFreezeNo(btBankAccAppService.getSequence(SequenceEnum.FREEZE_NO));
			btBankAccFreeze.setUpdateBy(user.getLoginName());
			btBankAccFreeze.setUpdateDate(DateUtils.getDate());
			btBankAccUnFreezeServiceI.saveOrUpdate(btBankAccFreeze);
			if (!BtBankAccConstant.STATUE_TEMP.equals(btBankAccFreeze.getStatus())) {
				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_UNFREEZE, btBankAccFreeze.getId(), btBankAccFreeze, btBankAccFreeze.getCorpId(), user.getId());
			}
		}
	}
	/**
	 * 审批完成后的方法更新余额表
	 *
	 * @param btBankAccFreeze
	 */
	public void toAppreEnd(BtBankAccFreezeDTO btBankAccFreeze, ICurrentUser user) {
		bisAccBalService.updateFrzBalAndAvailBal(btBankAccFreeze.getBankAcc(), btBankAccFreeze.getCurId(), btBankAccFreeze.getFreezeDate(),btBankAccFreeze.getPlanFlag(), btBankAccFreeze.getFreezeMoney(), false, user);
	}
	/**
	 * 得到已冻结
	 *
	 * @param bankAccId
	 * @param curId
	 * @return
	 */
	public List<BtBankAccFreezeDTO> getUnFreeze(String bankAccId, String curId) {
		List<BtBankAccFreezeDTO> tempList = btBankAccUnFreezeServiceI.getBtBankAccUnFreeze(bankAccId, curId);
		List<BtBankAccFreezeDTO> delList = new ArrayList<BtBankAccFreezeDTO>();
		for (BtBankAccFreezeDTO vo : tempList) {
			BigDecimal ufFreezeMoney = btBankAccUnFreezeServiceI.getUfFreezeMoney(vo.getBankAccId(), vo.getCurId(), vo.getId());
			BigDecimal unWaitMoney = btBankAccUnFreezeServiceI.getUfWaitFreezeMoney(vo.getBankAccId(), vo.getCurId(), vo.getId());
			vo.setUfFreezeMoney(ufFreezeMoney);
			vo.setUnwaitMoney(unWaitMoney);
			vo.setUfavalBal(vo.getFreezeMoney().subtract(ufFreezeMoney).subtract(unWaitMoney));
//			if (vo.getUfavalBal() == null || vo.getUfavalBal().doubleValue() == BigDecimal.ZERO.doubleValue()) {
//				delList.add(vo);
//			}
			String statusName=dictService.getByName("bisExc.status",vo.getStatus()+"");
			vo.setStatusName(statusName);
		}
//		tempList.removeAll(delList);
		return tempList;
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		BtBankAccFreezeDTO btBankAccFreeze = btBankAccUnFreezeServiceI.getObject(workFlowResult.getBusinessKey());
		Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
		btBankAccFreeze.setStatus(status);
		btBankAccFreeze.setUpdateBy(user.getLoginName());
		btBankAccFreeze.setUpdateDate(DateUtils.getDate());
		btBankAccUnFreezeServiceI.saveOrUpdate(btBankAccFreeze);
		if (workFlowResult.isPass()) {
			this.toAppreEnd(btBankAccFreeze, user);
		}
	}
	/**
	 * 查询审核列表信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		String sign = NisWorkFlowConstant.BT_BANKACC_UNFREEZE;
		WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(sign, dataTablesInput);
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "freeze.id", true);
		DataTablesOutput dp = this.btBankAccUnFreezeServiceI.dataTableApprove(dataTablesInput, businessSql);
		List<BtBankAccFreezeDTO> voList=dp.getData();
		for(BtBankAccFreezeDTO vo:voList) {
			BtBankAccDTO accVO = btBankAccService.getBtBankAcc(vo.getBankAccId());
			if(null != accVO){
				vo.setBankAcc(accVO.getBankAcc());
				vo.setAccName(accVO.getAccName());
				vo.setBankName(accVO.getBankName());
			}
		}
		return workFlowBusiness.merge(dp);
	}

	public BtBankAccFreezeDTO get(String id) {
		return btBankAccUnFreezeServiceI.getObject(id);
	}

	public void freezeSaveTemp(BtBankAccFreezeDTO btBankAccFreeze, ICurrentUser currentUser){
		btBankAccFreeze.setStatus(com.bytter.biz.bdlink.api.constant.BtBankAccConstant.STATUE_TEMP);
		/**
		 * 解冻暂存
		 */
		btBankAccFreeze.setUfFlag(com.bytter.biz.bdlink.api.constant.BtBankAccConstant.UFFLAG_1.toString());
		btBankAccFreeze.setFreezeNo(btBankAccAppService.getSequence(SequenceEnum.FREEZE_NO));
		btBankAccFreeze.setfUuid(btBankAccFreeze.getId());
		//判断当前冷冻记录是否为 计划冻结的记录
		BtBankAccFreezeDTO btBankAccFreezeDTO = ibtBankAccFreezeService.getBtBankAccFreezeDTO(btBankAccFreeze.getId(), true);
		if(null != btBankAccFreezeDTO && CommonConstant.YES.equals(btBankAccFreezeDTO.getPlanFlag())){
			btBankAccFreeze.setPlanFlag(btBankAccFreezeDTO.getPlanFlag());
		}
		btBankAccFreeze.setCreateBy(currentUser.getLoginName());
		btBankAccFreeze.setCreateDate(DateUtils.getDate());
		btBankAccFreeze.setFreezeDate(DateUtils.getDate());
		btBankAccFreeze.setId("");
		btBankAccUnFreezeServiceI.save(btBankAccFreeze);
	}

	public void saveOrUpdate(BtBankAccFreezeDTO btBankAccFreezeDTO){
		btBankAccUnFreezeServiceI.saveOrUpdate(btBankAccFreezeDTO);
	}

	public void deleteEntity(BtBankAccFreezeDTO btBankAccFreezeDTO){
		btBankAccUnFreezeServiceI.deleteEntity(btBankAccFreezeDTO);
	}

}
