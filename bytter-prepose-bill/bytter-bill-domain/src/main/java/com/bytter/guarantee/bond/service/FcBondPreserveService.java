package com.bytter.guarantee.bond.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.guarantee.bond.constant.CmsProcessMsgConstant;
import com.bytter.guarantee.bond.dao.FcBondApplyDao;
import com.bytter.guarantee.bond.entity.FcBondApply;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.guarantee.vo.FcBondPreserveVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondPreserveService
 * @Description:保证金维护服务层
 * @date 2018年9月18日 上午10:12:31
 */
@Service("FcBondPreserveService")
@WorkFlow(value = { FcBondConstant.FC_FCBONDPRESERVE_FLOW })
@Transactional
public class FcBondPreserveService extends BaseService<FcBondApply, String>
		implements IWorkFlowListener, IExcelVerifyHandler<FcBondAddDTO> {
	@Autowired
	private FcBondApplyDao fcBondApplyDao;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	@Autowired
	private IWorkFlowService workFlowService;
	@Autowired
	private FcBondBalService fcBondBalService;
	@Autowired
	private FcBondLogService fcBondLogService;
	@Autowired
	private FcBondDetailService fcBondDetailService;
	@DubboReference(check = false)
	private IInputBankInfoService bankInfoService;
    @DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;

	@DubboReference(check = false)
	private IBankTypeService iBankTypeService;
	/**
	 * 获取保证金维护列表数据
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<FcBondPreserveVO> getPreserveTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = FcBondConstant.NETBANK_FCBONDORESERVE_DEPOSIT_QUERY;//查询资源
        List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "m.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
        } else {
            sql.append(" and 1 <> 1");
        }

		//账号条件
		String likes_bankName = (String) dataTablesInput.getFormdata().get("likes_bankName");
		String likes_bankAcc = (String) dataTablesInput.getFormdata().get("likes_bankAcc");
		String likes_accName = (String) dataTablesInput.getFormdata().get("likes_accName");
		if (!StringUtil.isBlank_new(likes_bankName) ||!StringUtil.isBlank_new(likes_bankAcc)||!StringUtil.isBlank_new(likes_accName)) {
			StringBuilder accSql = new StringBuilder();
			Map<String, Object> params = new HashMap();
			accSql.append(" SELECT a.id FROM bt_bank_acc a WHERE 1=1");
			if (!StringUtil.isBlank_new(likes_bankName))
				accSql.append(" and a.bank_name like '%" + likes_bankName + "%'");
			if (!StringUtil.isBlank_new(likes_bankAcc))
				accSql.append(" and a.bank_acc like '%" + likes_bankAcc + "%'");
			if (!StringUtil.isBlank_new(likes_accName))
				accSql.append(" and a.acc_name like '%" + likes_accName + "%'");
			List<BtBankAccDTO> accDTOList = iBtBankAccService.listBankAccBySql(accSql.toString(), params);
			if (accDTOList.size() > 0) {
				List<String> accList = accDTOList.stream().map(BtBankAccDTO::getId).collect(Collectors.toList());
				if (accList != null && accList.size() > 0) {
					sql.append(" and ").append(SqlUtil.getInSql(accList, " P.BANK_ACC_ID", true));
				}
				dataTablesInput.getFormdata().remove("likes_bankName");
				dataTablesInput.getFormdata().remove("likes_bankAcc");
				dataTablesInput.getFormdata().remove("likes_accName");
			} else {
				return new DataTablesOutput<>();
			}
		}
		return fcBondApplyDao.getPreserveTables(dataTablesInput, sql.toString());
	}
	/**
	 * 保存保证金项目信息
	 */
	public void tempOrsubmit(FcBondPreserveVO fcBondPreserveVO, String saveType, ICurrentUser user) {
		FcBondApply fcBondApply = new FcBondApply();
		String message = null;
		Integer messageType = null;
		if (fcBondPreserveVO != null) {
			BeanUtils.copyNotNullProperties(fcBondPreserveVO, fcBondApply);
		}
		BigDecimal applyMoney = fcBondPreserveVO.getApplyMoney();
		BigDecimal itemMoney = fcBondPreserveVO.getItemMoney();
		// 判断项目金额是否为0
		if (itemMoney.compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal bondPercent = applyMoney.divide((itemMoney), 5, RoundingMode.CEILING);
			fcBondApply.setBondPercent(bondPercent);
		}
		// 获取新增/修改标志位
		String flag = fcBondPreserveVO.getAddOrUpdateFlag();
		// 新增
		if (flag.equals((FcBondConstant.MESSAGE_TYPE_ADD).toString())) {
			//项目类型
			fcBondApply.setItemType(FcBondConstant.ITEM_TYPE_OTHER);
			//保证金维护时只能维护手工占用的数据
			fcBondApply.setApplyType(FcBondConstant.APPLY_TYPE_HANDWORK);//手工占用
			fcBondApply.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_FCBOND_APPPLY));
			fcBondApply.setCreateBy(user.getLoginName());
			fcBondApply.setCreateDate(DateUtils.getDate());
			if (saveType.equals((FcBondConstant.MESSAGE_TYPE_SUBMIT).toString())) {// 提交
				fcBondApply.setStatus(FcBondConstant.STATUE_WAIT_CHECK);
				message = CmsProcessMsgConstant.MSGA_INPUT_SUBMIT;// 录入提交
			} else {// 暂存
				fcBondApply.setStatus(FcBondConstant.STATUE_TEMP);
				message = CmsProcessMsgConstant.MSGA_INPUT+"";// 录入暂存
			}
			super.saveEntity(fcBondApply);
			//更新当前保证金在余额表中的占用额度
			FcBondBal fcBondBal = fcBondBalService.getBondBalById(fcBondApply.getBondId());//当前保证金账户占用情况
			BigDecimal applyMoneyBal = fcBondApply.getApplyMoney();
			BigDecimal sumApplyMoney = null;
			if (applyMoneyBal == null) {
				sumApplyMoney = fcBondBal.getApplyMoney();
			} else {
				sumApplyMoney = applyMoneyBal.add(fcBondBal.getApplyMoney());
			}
			fcBondBal.setApplyMoney(sumApplyMoney);
			fcBondBalService.updateEntity(fcBondBal);
		} else if(flag.equals((FcBondConstant.MESSAGE_TYPE_UPDATE).toString())){// 修改
			fcBondApply.setUpdateBy(user.getLoginName());
			fcBondApply.setUpdateDate(DateUtils.getDate());
			if (saveType.equals((FcBondConstant.MESSAGE_TYPE_SUBMIT).toString())) {// 提交
				fcBondApply.setStatus(FcBondConstant.STATUE_WAIT_CHECK);
				message = CmsProcessMsgConstant.MSGA_REPAIR_MODIFY+"";// 修改提交
			} else {// 暂存
				fcBondApply.setStatus(FcBondConstant.STATUE_TEMP);
				message = CmsProcessMsgConstant.MSGA_MODIFY+"";// 修改暂存
			}
			super.updateEntity(fcBondApply);
			//得到修改前的保证金占用信息
			FcBondPreserveVO fcBondPreserveBefore = this.getFcBondItemById(fcBondApply.getId());
			FcBondBal fcBondBalBefore = fcBondBalService.getBondBalById(fcBondPreserveBefore.getBondId());
			//更新余额表中的占用额度
			if(!((fcBondPreserveBefore.getBondId()).equals(fcBondApply.getBondId()))){//判断更新前后保证金的id是否一致
				//如果不一致则更新原占用的保证金的占用额度
				BigDecimal applyMoneyBefore = fcBondBalBefore.getApplyMoney();//修改之前总的占用金额
				fcBondBalBefore.setApplyMoney(applyMoneyBefore.subtract(fcBondPreserveBefore.getApplyMoney()));//减去该项目修改之前的占用金额
				fcBondBalService.updateEntity(fcBondBalBefore);
				//更新当前保证金在余额表中的占用额度
				FcBondBal fcBondBal = fcBondBalService.getBondBalById(fcBondApply.getBondId());//当前保证金账户占用情况
				BigDecimal applyMoneyBal = fcBondApply.getApplyMoney();
				BigDecimal sumApplyMoney = null;
				if (applyMoneyBal == null) {
					sumApplyMoney = fcBondBal.getApplyMoney();
				} else {
					sumApplyMoney = applyMoneyBal.add(fcBondBal.getApplyMoney());
				}
				fcBondBal.setApplyMoney(sumApplyMoney);
				fcBondBalService.updateEntity(fcBondBal);
			}else{
				//更新当前保证金在余额表中的占用额度
				FcBondBal fcBondBal = fcBondBalService.getBondBalById(fcBondApply.getBondId());//当前占用保证金账户的余额情况
				BigDecimal applyMoneyBal = fcBondApply.getApplyMoney();//修改后的占用金额
				fcBondBal.setApplyMoney(fcBondBal.getApplyMoney().subtract(fcBondPreserveBefore.getApplyMoney()).add(applyMoneyBal));//减去修改前的占用金额再加上修改后的占用金额
				fcBondBalService.updateEntity(fcBondBal);
			}
		}
		// 启动审批
		if (FcBondConstant.STATUE_WAIT_CHECK.equals(fcBondApply.getStatus())) {// 判断是否待审批
			messageType = FcBondConstant.MESSAGE_TYPE_SUBMIT;// 进入审批，消息类型为提交
			// 表示审批的业务，id为fcBond的id，entity为fcBond，单位编号，userId用户编号
			 getStartHandler().handler(FcBondConstant.FC_FCBONDPRESERVE_FLOW, fcBondApply.getId(), fcBondApply,
					fcBondApply.getCorpId(), user.getId());
		}else{
			messageType = FcBondConstant.MESSAGE_TYPE_TEMP;// 未进入审批，消息类型为暂存
		}
		fcBondLogService.saveFcBondMessge(fcBondApply.getId(), fcBondApply.getApplyMoney(), messageType, message, user);
		// 完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.FCBOND_PRESERVE_BACK, fcBondApply.getId(),
				user.getId());
	}
	/**
	 * 删除保证金项目
	 */
	public void deleteFcBondItem(String id, ICurrentUser user) {
		FcBondApply fcBondApply = fcBondApplyDao.get(id);
		fcBondApply.setStatus(FcBondConstant.STATUE_DEL);
		fcBondApply.setUpdateBy(user.getLoginName());
		fcBondApply.setUpdateDate(DateUtils.getDate());
		super.updateEntity(fcBondApply);
		//释放占用的保证金
		this.releaseItemAmount(id, user);
	}
	/**
	 * 释放保证金项目占用金额
	 */
	public void releaseItemAmount(String id, ICurrentUser user) {
		// 更新占用表中的的状态
		FcBondApply fcBondApply = fcBondApplyDao.get(id);
		fcBondApply.setStatus(FcBondConstant.STATUE_RELEASE);
		fcBondApply.setUpdateBy(user.getLoginName());
		fcBondApply.setUpdateDate(DateUtils.getDate());
		super.updateEntity(fcBondApply);
		// 更新余额表中的占用金额
		FcBondBal bondBal = fcBondBalService.getBondBalById(fcBondApply.getBondId());
		BigDecimal applyMoney = bondBal.getApplyMoney();
		bondBal.setApplyMoney(applyMoney.subtract(fcBondApply.getApplyMoney()));
		fcBondBalService.updateEntity(bondBal);
		//对释放保证金进行日志记录
		//操作类型
		Integer messageType = FcBondConstant.MESSAGE_TYPE_RELEASE;
		//操作信息
		String message = CmsProcessMsgConstant.MSGA_RELEASE_BOND;
		fcBondLogService.saveFcBondMessge(fcBondApply.getId(), fcBondApply.getApplyMoney(), messageType, message, user);
	}
	/**
	 * 根据id获取保证金项目的相关信息
	 */
	public FcBondPreserveVO getFcBondItemById(String id) {
		FcBondPreserveVO fcBondPreserveVO = fcBondApplyDao.getFcBondItemByID(id);
		if(!EmptyUtils.isEmpty(fcBondPreserveVO)){
			String corpName=corpService.getCorpName(fcBondPreserveVO.getCorpId());
			fcBondPreserveVO.setCorpName(corpName);
			BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(fcBondPreserveVO.getCurId());
			fcBondPreserveVO.setCurName(currencyDTO.getCurName());
		}
		return fcBondPreserveVO;
	}
	/**
	 * 获取审批列表数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput<WorkFlowPersistence> getApproveDataTables(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl
				.getWorkFlowBusiness(FcBondConstant.FC_FCBONDPRESERVE_FLOW, dataTablesInput);
		DataTablesOutput dp = fcBondApplyDao.getItemApproveDataTables(dataTablesInput);
		List<FcBondPreserveVO> voList =	dp.getData();
		for(FcBondPreserveVO vo:voList){
			//查询账号
			BtBankAccDTO accDTO= iBtBankAccService.getBankAccById(vo.getBankAccId());
			if(null != accDTO){
				vo.setBankAcc(accDTO.getBankAcc());
				vo.setAccName(accDTO.getAccName());
				vo.setBankName(accDTO.getBankName());
				vo.setCorpId(accDTO.getCorpId());
				vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
				vo.setCurId(accDTO.getCurId());
				vo.setCurName(accDTO.getCurName());
			}
		}
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 根据网点和银行查询存出保证金的账户列表
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<FcBondAccVO> queryBondAccDataTables(DataTablesInput dataTablesInput, String corpId, String bankCode, String curId,
                                                   ICurrentUser user) {
		StringBuffer joinWhere = new StringBuffer();
		//用户单位权限
//		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCACC_QUERY, user.getId()), "a.corp_id", true);
//		joinWhere.append(" and ").append(corpAuthSql);
		String bailBankControlType = "";
		//保证金银行控制选择  0银行行号相同 1银行类别相同
		if (dataTablesInput.getFormdata().containsKey("bailBankControlType")) {
			bailBankControlType =  dataTablesInput.getFormdata().get("bailBankControlType").toString();
		}
		//1银行类别相同
		if (FcBondConstant.BAIL_BANK_CONTROL_TYPE_1.equals(bailBankControlType)) {
			if(EmptyUtils.isNotEmpty(bankCode)) {
				joinWhere.append(" and" );
				//TODO
				InputBankInfoDTO bankInfoDTO = bankInfoService.getByBankCode(bankCode);
				if (EmptyUtils.isEmpty(bankInfoDTO)) {
					return new DataTablesOutput();
				}
				List<BankTypeDTO> bankTypeDTOS = iBankTypeService.listBySignOrPrefix("1", bankInfoDTO.getBankPrefix());
				if(EmptyUtils.isEmpty(bankTypeDTOS)){
					return new DataTablesOutput();
				}
				List<String> bankTypeIds = new ArrayList<>();
				for(BankTypeDTO dto : bankTypeDTOS){
					if(dto.getBankNamePrefix() == null || bankInfoDTO.getBankName().contains(dto.getBankNamePrefix())){
						bankTypeIds.add(dto.getId());
					}
				}
				if(EmptyUtils.isEmpty(bankTypeIds)){
					return new DataTablesOutput();
				}
				//拼这个老慢了
//				List<InputBankInfoDTO> byBankCodesList = bankInfoService.getBankCodeInfoByBankTypeIds(bankTypeIds);
//				if(EmptyUtils.isEmpty(byBankCodesList)){
//					return new DataTablesOutput();
//				}
//				List<String> collect = byBankCodesList.stream().map(InputBankInfoDTO::getBankCode).collect(Collectors.toList());
				// joinWhere.append(SqlUtil.getInSql(collect, "a.bank_code", true));
				// -- and (ibi.BANK_TYPE_ID in ('547E2684F53B23DDE063D900A8C01661'))
				joinWhere.append(SqlUtil.getInSql(bankTypeIds, "ibi.BANK_TYPE_ID", true));
				if (StringUtils.isNotEmpty(corpId)) {
					joinWhere.append(" and a.corp_id='").append(corpId).append("'");
				}
			}
		} else {
			if (StringUtils.isNotEmpty(corpId)) {
				joinWhere.append(" and a.corp_id='" + corpId + "' ");
			}
			if (StringUtils.isNotEmpty(bankCode)) {
				joinWhere.append(" and a.bank_code='" + bankCode + "' ");
			}
		}
		if (StringUtils.isNotEmpty(curId)) {
			joinWhere.append(" and a.cur_id='" + curId + "' ");
		}
		DataTablesOutput<FcBondAccVO> dataTablesOutput=fcBondApplyDao.queryFcBondVOByTerms(dataTablesInput, joinWhere.toString());
//		List<FcBondAccVO> list=dataTablesOutput.getData();
		List data = dataTablesOutput.getData();
		ArrayList<FcBondAccVO> fcBondAccVOs = new ArrayList<>();
		for (Object datum : data) {
			FcBondAccVO vo = (FcBondAccVO)datum;
			String corpName=corpService.getCorpName(((FcBondAccVO) datum).getCorpId());
			vo.setCorpName(corpName);
			BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(((FcBondAccVO) datum).getCurId());
			vo.setCurName(currencyDTO.getCurName());
			fcBondAccVOs.add(vo);
		}
		dataTablesOutput.setData(fcBondAccVOs);
		return dataTablesOutput;

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BaseDao getEntityDao() {
		// TODO Auto-generated method stub
		return fcBondApplyDao;
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		FcBondApply fcBondApply = this.get(workFlowResult.getBusinessKey());
		FcBondDetailVO fcBondVO = fcBondDetailService.getFcBondById(fcBondApply.getBondId());
		Integer status = workFlowResult.isPass() ? FcBondConstant.STATUE_CHECK : FcBondConstant.STATUE_BACK;
		// 更新保证金占用表中的信息
		fcBondApply.setStatus(status);
		fcBondApply.setUpdateBy(user.getLoginName());
		fcBondApply.setUpdateDate(DateUtils.getDate());
		super.updateEntity(fcBondApply);
		Integer messageType = null;
		String msg = null;
		if (status.equals(FcBondConstant.STATUE_CHECK)) {
			messageType = FcBondConstant.MESSAGE_TYPE_CHECK_PASS;
			msg = CmsProcessMsgConstant.MSGA_CHECKOK+"";
			// 保证金维护审批通过时,发出通知告知已审批通过
			notificationService.send(fcBondApply.getId(), fcBondVO.getCorpId(),
					NotificationConstant.NETBANK.FCBOND_PRESERVE_PASS, fcBondApply);
		} else if (status.equals(FcBondConstant.STATUE_BACK)) {
			messageType = FcBondConstant.MESSAGE_TYPE_CHECK_BACK;
			msg = CmsProcessMsgConstant.MSGA_CHECKBACK;
			// 保证金维护审批未通过时,发出待处理消息告知已打回
			notificationService.send(fcBondApply.getId(), fcBondVO.getCorpId(),
					NotificationConstant.NETBANK.FCBOND_PRESERVE_BACK, fcBondApply);
		}
		fcBondLogService.saveFcBondMessge(fcBondApply.getId(), fcBondApply.getApplyMoney(), messageType, msg, user);
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(FcBondAddDTO obj) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void verifyHandler(List<FcBondAddDTO> list) {
		// TODO Auto-generated method stub
	}
}
