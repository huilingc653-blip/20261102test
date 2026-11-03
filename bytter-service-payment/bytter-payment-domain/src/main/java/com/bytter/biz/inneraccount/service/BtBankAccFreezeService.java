package com.bytter.biz.inneraccount.service;

import com.bytter.biz.inneraccount.entity.BtBankAccFreeze;
import com.bytter.biz.inneraccount.vo.BtBankAccFreezeVO;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccFreezePageVO;
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
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
/**
* @Title: Service
* @Description: BT_BANK_ACC_FREEZE
* @author onlineGenerator
* @date 2017-06-14 14:26:46
* @version 1.0
*
*/
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_FREEZE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.BT_BANKACC_FREEZE)
@Service("btBankAccFreezeService")
@Transactional
public class BtBankAccFreezeService implements IWorkFlowListener, ICorpDisableVerifyService {

	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBtBankAccFreezeService ibtBankAccFreezeService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;

	/**
	* 删除对象
	* @param dto
	*/
	public void deleteEntity(BtBankAccFreezeDTO dto){

		ibtBankAccFreezeService.deleteEntity(dto);
	}
	/**
	* 根据id删除
	* @param id
	*/
	public void delete(String id){
		ibtBankAccFreezeService.delete(id);
 	}
	/**
	* 保存
	* @param btBankAccFreeze
	*/
 	public void freezeSaveTemp(BtBankAccFreeze btBankAccFreeze, ICurrentUser currentUser){
		btBankAccFreeze.setStatus(com.bytter.biz.bdlink.api.constant.BtBankAccConstant.STATUE_TEMP);
		/**
		 * 冻结暂存
		 */
		btBankAccFreeze.setUfFlag(com.bytter.biz.bdlink.api.constant.BtBankAccConstant.UFFLAG_0.toString());
		btBankAccFreeze.setFreezeNo(SequenceSingleton.getSequence(SequenceEnum.FREEZE_NO));
		btBankAccFreeze.setCreateBy(currentUser.getLoginName());
		btBankAccFreeze.setCreateDate(DateUtils.getDate());
		btBankAccFreeze.setUpdateBy(currentUser.getLoginName());
		btBankAccFreeze.setUpdateDate(DateUtils.getDate());

		BtBankAccFreezeDTO dto = new BtBankAccFreezeDTO();
		BeanUtils.copyNotNullProperties(btBankAccFreeze,dto);
		ibtBankAccFreezeService.save(dto);
 	}
	/**
	* 更新
	* @param dto
	*/
	public void update(BtBankAccFreezeDTO dto){
		ibtBankAccFreezeService.update(dto);
	}
	/**
	* 保存or更新
	* @param dto
	*/
	public void saveOrUpdate(BtBankAccFreezeDTO dto){

		ibtBankAccFreezeService.saveOrUpdate(dto);
 	}


	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		List<BtBankAccDTO> bankAccs = accountGrantService.listByMultiVal(corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_QUERY, user.getId()),
				AccountGrantConstant.OFFLINE_BANK_TYPE,AccountGrantConstant.TRANSFER_ACCOUNT);
		if(bankAccs == null){
			List<BtBankAccFreezeVO> listVO = new ArrayList<>();
			return new DataTablesOutput(dataTablesInput.getDraw(), listVO, 0L, 0L);
		}
		Set<String> list = new HashSet<>();
		for (BtBankAccDTO acc : bankAccs) {
			list.add(acc.getBankAcc());
		}
		List<String> bankAccStrings = new ArrayList<>(list);
		String bankAccSql = SqlUtil.getInSql(bankAccStrings,"acc.bank_acc",true);
		sql.append(" and ").append(bankAccSql);
		return ibtBankAccFreezeService.getDataTablesUnion(dataTablesInput,  sql.toString());
	}
	public BtBankAccFreezePageVO getAccInfoBeforeFreeze(String bankAccId, String curId){
		BtBankAccDTO accVO = setVoParams(bankAccId,curId);
		List<BtBankAccFreezeDTO> list = ibtBankAccFreezeService.getBtBankAccFreeze(bankAccId,curId);
		List<BtBankAccFreezeDTO> tempList = ibtBankAccFreezeService.getBtBankAccFreezeTemp(bankAccId,curId);
		List<BtBankAccFreezeDTO> waitList = ibtBankAccFreezeService.getBtBankAccFreezeWait(bankAccId,curId);
		List<BtBankAccFreezeDTO> list1 = new ArrayList<>();
		list1.addAll(tempList);
		list1.addAll(waitList);
		/*** 得到账户冻结金额*/
		BigDecimal freezeNum = ibtBankAccFreezeService.getFreezeMoney(bankAccId,curId);
		/*** 得到账户解冻金额*/
		BigDecimal ufFreezeNum = ibtBankAccFreezeService.getUfFreezeMoney(bankAccId,curId);
		if(!EmptyUtils.isEmptyList(tempList)){
			for (int i = 0; i < tempList.size(); i++) {
				tempList.get(i).setUfFreezeMoney(ufFreezeNum);
				tempList.get(i).setUfavalBal(freezeNum.subtract(ufFreezeNum));
			}
		}
		BtBankAccFreezeVO btBankAccFreezeVO = new BtBankAccFreezeVO();
		btBankAccFreezeVO.setCurId(curId);
		btBankAccFreezeVO.setBankAccId(bankAccId);
		toFreeze(btBankAccFreezeVO,list,accVO.getBankAcc(),list1);

		BtBankAccFreezePageVO btBankAccFreezePageVO = new BtBankAccFreezePageVO();
		btBankAccFreezePageVO.setAccVO(accVO);
		btBankAccFreezePageVO.setBtBankAccFreezeVO(btBankAccFreezeVO);
		//未见到tempList的使用
//		model.addAttribute("tempList", tempList);
		return btBankAccFreezePageVO;
	}
	public void toAddFreeze(BtBankAccFreezeVO vo, Model model){
		BtBankAccDTO accVO = setVoParams(vo);
		List<BtBankAccFreezeDTO> list = ibtBankAccFreezeService.getBtBankAccFreeze(vo.getBankAccId(), vo.getCurId());
		List<BtBankAccFreezeDTO> tempList = ibtBankAccFreezeService.getBtBankAccFreezeTemp(vo.getBankAccId(), vo.getCurId());
        List<BtBankAccFreezeDTO> waitList = ibtBankAccFreezeService.getBtBankAccFreezeWait(vo.getBankAccId(),vo.getCurId());
        List<BtBankAccFreezeDTO> list1 = new ArrayList<>();
        list1.addAll(tempList);
        list1.addAll(waitList);
		/*** 得到账户冻结金额*/
		BigDecimal freezeNum = ibtBankAccFreezeService.getFreezeMoney(vo.getBankAccId(), vo.getCurId());
		/*** 得到账户解冻金额*/
		BigDecimal ufFreezeNum = ibtBankAccFreezeService.getUfFreezeMoney(vo.getBankAccId(), vo.getCurId());
		if(!EmptyUtils.isEmptyList(tempList)){
			for (int i = 0; i < tempList.size(); i++) {
				tempList.get(i).setUfFreezeMoney(ufFreezeNum);
				tempList.get(i).setUfavalBal(freezeNum.subtract(ufFreezeNum));
			}
		}
		toFreeze(vo,list,accVO.getBankAcc(),list1);
		model.addAttribute("accVO", accVO);
		model.addAttribute("btBankAccFreezeVO", vo);
		model.addAttribute("tempList", tempList);
	}

	public void toFreeze(BtBankAccFreezeVO vo,List<BtBankAccFreezeDTO> list,String bankAcc,List<BtBankAccFreezeDTO> tempList){
		/** 冻结次数*/
		int freezeCount = 0;
		/** 解冻次数*/
		int unfreezeCount = 0;
		/**  冻结金额合计*/
		BigDecimal freezeMoneySum=new BigDecimal(0);
		/** 解冻金额合计*/
		BigDecimal unFreezeMoneySum=new BigDecimal(0);
		/** 计算冻结金额和解冻金额*/
		for (BtBankAccFreezeDTO btBankAccFreezeDTO : list) {
			if ("0".endsWith(btBankAccFreezeDTO.getUfFlag())) {
				freezeCount++;
				freezeMoneySum = freezeMoneySum.add(btBankAccFreezeDTO.getFreezeMoney());
			} else if ("1".endsWith(btBankAccFreezeDTO.getUfFlag())) {
				unfreezeCount++;
				unFreezeMoneySum = unFreezeMoneySum.add(btBankAccFreezeDTO.getFreezeMoney());
			}
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
		//这里的余额计算 有问题 因为账户可用余额已经减去了冻结金额 所以暂时加上冻结金额 和 系统
//		balDto.setBal(balDto.getBal().add(balDto.getFrzBal()).add(balDto.getSysFrzBal()));
		if(!EmptyUtils.isEmpty(balDto)){
			vo.setBal(balDto.getBal());
			BigDecimal availBal = balDto.getAvailBal();
//			vo.setAvalBal(availBal);
			// 先还原余额表已扣除的冻结金额，再按冻结、解冻流水统一计算。
			availBal = availBal.add(balDto.getFrzBal() == null ? BigDecimal.ZERO : balDto.getFrzBal())
					.add(balDto.getSysFrzBal() == null ? BigDecimal.ZERO : balDto.getSysFrzBal());
			vo.setAvalBal(availBal.add(unFreezeMoneySum).subtract(freezeMoneySum));
//			for(int i = 0;i< tempList.size();i++){
//				vo.setAvalBal(vo.getAvalBal().subtract(tempList.get(i).getFreezeMoney()));
//			}
		}

	}

	/**
	 * 修改
	 * @param vo
	 * @param model
	 */
	public void toUpdateFreeze(BtBankAccFreezeVO vo, Model model){
		BtBankAccDTO accVO = setVoParams(vo);
		List<BtBankAccFreezeDTO> list = ibtBankAccFreezeService.getBtBankAccFreeze(vo.getBankAccId(), vo.getCurId());
		List<BtBankAccFreezeDTO> tempList = ibtBankAccFreezeService.getBtBankAccFreezeTemp(vo.getBankAccId(), vo.getCurId());
        List<BtBankAccFreezeDTO> waitList = ibtBankAccFreezeService.getBtBankAccFreezeWait(vo.getBankAccId(),vo.getCurId());
        List<BtBankAccFreezeDTO> list1 = new ArrayList<>();
        list1.addAll(tempList);
        list1.addAll(waitList);
		toFreeze(vo,list,accVO.getBankAcc(),list1);
        vo.setAvalBal(vo.getAvalBal().add(vo.getFreezeMoney()));
		model.addAttribute("accVO", accVO);
		model.addAttribute("btBankAccFreezeVO", vo);
	}

	public void toUpdateFreeze(BtBankAccFreezeVO vo,BtBankAccFreezePageVO pageVO){
		BtBankAccDTO accVO = setVoParams(vo);
		List<BtBankAccFreezeDTO> list = ibtBankAccFreezeService.getBtBankAccFreeze(vo.getBankAccId(), vo.getCurId());
		List<BtBankAccFreezeDTO> tempList = ibtBankAccFreezeService.getBtBankAccFreezeTemp(vo.getBankAccId(), vo.getCurId());
        List<BtBankAccFreezeDTO> waitList = ibtBankAccFreezeService.getBtBankAccFreezeWait(vo.getBankAccId(),vo.getCurId());
        List<BtBankAccFreezeDTO> list1 = new ArrayList<>();
        list1.addAll(tempList);
        list1.addAll(waitList);
		toFreeze(vo,list,accVO.getBankAcc(),list1);
        vo.setAvalBal(vo.getAvalBal());
		pageVO.setAccVO(accVO);
		pageVO.setBtBankAccFreezeVO(vo);
	}

	private BtBankAccDTO setVoParams(BtBankAccFreezeVO vo) {
		BtBankAccDTO accVO = ibtBankAccFreezeService.getAccVOById(vo.getBankAccId(),vo.getCurId());
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

	/**
	 * 得到冻结暂存
	 * @param bankAccId
	 * @param curId
	 * @return
	 */
	public List<BtBankAccFreezeVO> getTempFreeze(String bankAccId,String curId){
		List<BtBankAccFreezeDTO> tempList = ibtBankAccFreezeService.getBtBankAccFreezeTemp(bankAccId, curId);
		for(BtBankAccFreezeDTO btBankAccFreezeDTO:tempList){
			btBankAccFreezeDTO.setStatusName(dictService.get("bisExc.status",btBankAccFreezeDTO.getStatus()));
		}
		return BeanUtils.copyListProperties(tempList, BtBankAccFreezeVO.class);
	}
    /**
     * 得到冻结待审批
     * @param accId
     * @param curId
     * @return
     */
    public List<BtBankAccFreezeVO> getBtBankAccFreezeWait(String accId, String curId) {
		List<BtBankAccFreezeDTO> listDTO = ibtBankAccFreezeService.getBtBankAccFreezeWait(accId,curId);
	    return BeanUtils.copyListProperties(listDTO, BtBankAccFreezeVO.class);
    }

	public void toSubmit(String  ids, ICurrentUser user){
		String[] idStr = ids.split(",");
		for (int i = 0; i < idStr.length; i++) {
			BtBankAccFreezeDTO dto = ibtBankAccFreezeService.getBtBankAccFreezeDTO(idStr[i]);
			dto.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
			dto.setFreezeDate(new Date());
			dto.setFreezeNo(SequenceSingleton.getSequence(SequenceEnum.FREEZE_NO));
			dto.setUpdateBy(user.getLoginName());
			dto.setUpdateDate(DateUtils.getDate());

			this.saveOrUpdate(dto);
			if (!BtBankAccConstant.STATUE_TEMP.equals(dto.getStatus())) {
				 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_FREEZE, dto.getId(), dto, dto.getCorpId(), user.getId());
			}
		}
	}

	/**
	 * 审批完成后的方法更新余额表
	 * @param btBankAccFreeze
	 */
	public void toAppreEnd(BtBankAccFreeze btBankAccFreeze, ICurrentUser user){
		bisAccBalService.updateFrzBalAndAvailBal(btBankAccFreeze.getBankAcc(), btBankAccFreeze.getCurId(), btBankAccFreeze.getFreezeDate(), btBankAccFreeze.getFreezeMoney(), true, user);
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub

	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		BtBankAccFreezeDTO dto = ibtBankAccFreezeService.getBtBankAccFreezeDTO(workFlowResult.getBusinessKey());
		Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
		dto.setStatus(status);
		dto.setUpdateBy(user.getLoginName());
		dto.setUpdateDate(DateUtils.getDate());
		ibtBankAccFreezeService.saveOrUpdate(dto);
		BtBankAccFreeze btBankAccFreeze = new BtBankAccFreeze();
		BeanUtils.copyNotNullProperties(dto, btBankAccFreeze);
		if(workFlowResult.isPass()) {
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
		String sign = NisWorkFlowConstant.BT_BANKACC_FREEZE;
		WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(sign, dataTablesInput);
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "freeze.id", true);
		DataTablesOutput dp = ibtBankAccFreezeService.dataTableApprove(dataTablesInput, businessSql);
		List<BtBankAccFreezeDTO> voList=dp.getData();
		for(BtBankAccFreezeDTO vo:voList){
			BtBankAccDTO accVO = btBankAccService.getBtBankAcc(vo.getBankAccId());
			if(null != accVO){
				vo.setBankAcc(accVO.getBankAcc());
				vo.setAccName(accVO.getAccName());
				vo.setBankName(accVO.getBankName());
			}
		}
		return workFlowBusiness.merge(dp);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在办的银行账户信息，不允许禁用！
			int num = ibtBankAccFreezeService.getBankAccFreezeCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_BANKACC,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BANKACC_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 获取冻结/解冻金额
	 * @param bankAcc
	 * @return
	 */
	public BigDecimal getFreezeMoneyByBankAcc(String bankAcc, String curId, Date balDate){
		//冻结金额
		BigDecimal freezeMoney = ibtBankAccFreezeService.getFreezeMoneyByBankAcc(bankAcc, curId, BtBankAccConstant.UFFLAG_0.toString(), balDate);
		//解冻金额
		BigDecimal unFreezeMoney = ibtBankAccFreezeService.getFreezeMoneyByBankAcc(bankAcc, curId, BtBankAccConstant.UFFLAG_1.toString(), balDate);
		return freezeMoney.subtract(unFreezeMoney);
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String resource = ResourceConstant.NETBANK_BANKACC_BTBANKACCFREEZE_REPORTQUERY;
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "freeze.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		sql.append(" and freeze.status<>'"+ CommonConstant.STATUE_DELETE +"'");
		DataTablesOutput output = ibtBankAccFreezeService.getDataTablesQuery(dataTablesInput, sql.toString());
		List<BtBankAccFreezeDTO> voList=output.getData();
		//获取单位信息
		Map<String, CorpDTO> corpMap = corpService.getCorpMap();
		//获取币别信息
		Map<String, BtCurrencyDTO> btCurrencyMap = btCurrencyService.getCurrency();
		for(BtBankAccFreezeDTO dto:voList) {
			BtBankAccDTO accVO = btBankAccService.getBtBankAcc(dto.getBankAccId());
			if(null != accVO){
				dto.setBankAcc(accVO.getBankAcc());
				dto.setAccName(accVO.getAccName());
				dto.setBankName(accVO.getBankName());
			}

			if(btCurrencyMap != null){
				if(btCurrencyMap.containsKey(dto.getCurId())){
					String curName  = btCurrencyMap.get(dto.getCurId()) != null ? btCurrencyMap.get(dto.getCurId()).getCurName() : "";
					dto.setCurName(curName);
				}
			}
			if(corpMap != null){
				if(corpMap.containsKey(dto.getCorpId())){
					String corpName = corpMap.get(dto.getCorpId()) != null ? corpMap.get(dto.getCorpId()).getName(): "";
					dto.setCorpName(corpName);
				}
			}
		}
		return output;
	}

	public BtBankAccFreezeDTO get(String id){
		return ibtBankAccFreezeService.getBtBankAccFreezeDTO(id);
	}

	public BtBankAccFreezeDTO get(String id,boolean eager){
		return ibtBankAccFreezeService.getBtBankAccFreezeDTO(id,eager);
	}
}
