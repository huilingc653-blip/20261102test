package com.bytter.biz.bond.service;

import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.guarantee.api.IFcBondAddService;
import com.bytter.guarantee.api.IFcBondBalService;
import com.bytter.guarantee.api.IFcBondDetailService;
import com.bytter.guarantee.api.IFcBondLogService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondBalVO;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:FcBondAddService
 * @Description:追加保证金服务层
 * @date 2018年9月11日 上午10:20:03
 */
@Service("FcBondAddService")
@WorkFlow(FcBondConstant.FC_FCBONDADD_FLOW)
@DubboService(interfaceClass = IWorkFlowListener.class,group = FcBondConstant.FC_FCBONDADD_FLOW)
@Transactional
public class FcBondAddService extends AbstractService implements IWorkFlowListener, IExcelVerifyHandler<FcBondAddDTO> {

	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IFcBondAddService fcBondAddServiceI;
	@DubboReference(check = false)
	private IFcBondLogService fcBondLogService;
	@DubboReference(check = false)
	private IFcBondBalService fcBondBalService;
	@DubboReference(check = false)
	private IFcBondDetailService fcBondDetailService;
	@DubboReference(check = false)
	private INotificationService notificationService;

	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@DubboReference(check = false)
	private INisAllocationReqService iNisAllocationReqService;



	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	/**
	 * 保存追加保证金
	 */
	public void saveFcBondAdd(FcBondAddDTO fcBondAddDTO, String saveType, ICurrentUser user) {
		FcBondAddDTO fcBondAdd = new FcBondAddDTO();
		BeanUtils.copyNotNullProperties(fcBondAddDTO, fcBondAdd);
		String flag = fcBondAddDTO.getAddOrUpdateFlag();// 获取新增/修改标志位
		String msg = null;
		Integer messageType = null;
		if (flag.equals((FcBondConstant.MESSAGE_TYPE_ADD).toString())) {// 新增数据
			fcBondAdd.setBillNo(fcBondAddServiceI.getSequenceBillNO());
			fcBondAdd.setCreateBy(user.getLoginName());
			fcBondAdd.setCreateDate(DateUtils.getDate());
			if (saveType.equals((FcBondConstant.MESSAGE_TYPE_SUBMIT).toString())) {// 提交
				fcBondAdd.setStatus(FcBondConstant.STATUE_WAIT_CHECK);
				fcBondAdd.setAddDate(fcBondAddDTO.getAddDate());
				msg = CmsProcessMsgConstant.MSGA_INPUT_SUBMIT;// "录入提交"
			} else {// 暂存
				fcBondAdd.setStatus(FcBondConstant.STATUE_TEMP);
				msg = CmsProcessMsgConstant.MSGA_INPUT+"";// "录入暂存"
			}
			fcBondAdd = fcBondAddServiceI.saveEntity(fcBondAdd);
		} else if(flag.equals((FcBondConstant.MESSAGE_TYPE_UPDATE).toString())){// 修改数据
			fcBondAdd.setId(fcBondAddDTO.getId());
			fcBondAdd.setUpdateBy(user.getLoginName());
			fcBondAdd.setUpdateDate(DateUtils.getDate());
			if (saveType.equals((FcBondConstant.MESSAGE_TYPE_SUBMIT).toString())) {// 提交
				fcBondAdd.setStatus(FcBondConstant.STATUE_WAIT_CHECK);
				fcBondAdd.setAddDate(DateUtils.getDate());
				msg = CmsProcessMsgConstant.MSGA_REPAIR_MODIFY+"";// "修改提交"
			} else {// 暂存
				fcBondAdd.setStatus(FcBondConstant.STATUE_TEMP);
				msg = CmsProcessMsgConstant.MSGA_MODIFY+"";// "修改暂存"
			}
			fcBondAddServiceI.updateEntity(fcBondAdd);
		}
		// 启动审批
		if (FcBondConstant.STATUE_WAIT_CHECK.equals(fcBondAdd.getStatus())) {// 判断是否待审批
			// 表示审批的业务，id为fcBond的id，entity为fcBond，单位编号，userId用户编号
			messageType = FcBondConstant.MESSAGE_TYPE_SUBMIT;// 提交
			getStartHandler().handler(FcBondConstant.FC_FCBONDADD_FLOW, fcBondAdd.getId(), fcBondAdd,
					fcBondAddDTO.getCorpId(), user.getId());
			// 完成通知提醒
			notificationService.complete(NotificationConstant.NETBANK.FCBOND_ADD_BACK, fcBondAdd.getId(), user.getId());
		}else{
			messageType = FcBondConstant.MESSAGE_TYPE_TEMP;// 暂存
		}
		fcBondLogService.saveFcBondMessge(fcBondAdd.getId(), fcBondAdd.getAddMoney(), messageType, msg, user);
	}
	/**
	 * 审批完成
	 * @param workFlowResult
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		updatedata(workFlowResult);
	}
	/**
	 * 审批完成后更新各个表中的数据
	 */
	public void updatedata(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		FcBondAddDTO fcBondadd = fcBondAddServiceI.queryAddVOById(workFlowResult.getBusinessKey());
		FcBondBalVO fcbondbal = fcBondBalService.getBondBalByBondId(fcBondadd.getBondId());
		Integer status = workFlowResult.isPass() ? FcBondConstant.STATUE_CHECK : FcBondConstant.STATUE_BACK;
		FcBondDetailVO fcBondVO = fcBondDetailService.getFcBondById(fcBondadd.getBondId());
		if (status.equals(FcBondConstant.STATUE_CHECK)) {// 审批通过
			// 将追加金额记录到保证金余额表
			fcbondbal.setBondId(fcBondadd.getBondId());
			java.math.BigDecimal addMoney = fcbondbal.getAddMoney();
			java.math.BigDecimal sum = null;
			if (addMoney == null) {
				sum = fcBondadd.getAddMoney();
			} else {
				sum = addMoney.add(fcBondadd.getAddMoney());
			}
			fcbondbal.setAddMoney(sum);
			fcBondBalService.updateFcBondBal(fcbondbal);
			// 未付款时生成付款单
			if (fcBondadd.getIsPayed().equals(FcBondConstant.PAYED_NOT)) {
				NisBillheadDTO nisBillhead = new NisBillheadDTO();
                FcVoucherTypeDTO fcVoucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
				// 获取网点对应的付款方式
				nisBillhead.setCorpId(fcBondVO.getCorpId());// 单位
				nisBillhead.setPayeeAcc(fcBondVO.getBankAcc());// 账户
				nisBillhead.setPayeeAccName(fcBondVO.getAccName());// 账户名称
				nisBillhead.setPayerBank(fcBondVO.getBankName());// 开户行名称
				nisBillhead.setPayMoney(fcBondadd.getAddMoney());// 金额
				Date wishPayday = DateUtils.parseDate(fcBondadd.getAddDate());
				nisBillhead.setWishPayday(wishPayday);// 支付日期
				nisBillhead.setVoucherTypeId(fcVoucherType.getId());// 付款方式默认外付网银
				nisBillhead.setPurpose(fcBondadd.getRemark());// 用途
				nisBillhead.setStatus(ExpenseConstant.STATUE_TEMP);// 状态
				nisBillhead.setBillFrom(ExpenseConstant.BILLFROM_1);// 单据来源:1-支付结算
				nisBillheadService.saveNisBillhead(nisBillhead, user);
			}
			//若存出账号为在线且未付款时，需要生成一条暂存状态的头寸调拨单
			if(StringUtils.isNotEmpty(fcBondadd.getOutBankAcc())) {
				BtBankAccDTO bankDto = iBtBankAccService.getBankAccInfo(fcBondadd.getOutBankAcc());
				if ("1".equals(bankDto.getIsOnline()) && "0".equals(fcBondadd.getIsPayed())) {
					saveNisAllocationReq(fcBondadd, bankDto, user);
				}
			}
			// 审批通过，记入日志
			fcBondLogService.saveFcBondMessge(fcBondadd.getId(), fcBondadd.getAddMoney(),
					FcBondConstant.MESSAGE_TYPE_CHECK_PASS, CmsProcessMsgConstant.MSGA_CHECKOK+"", user);
			// 追加保证金审批通过时,发出通知告知已审批通过
			notificationService.send(fcBondadd.getId(), fcBondVO.getCorpId(),
					NotificationConstant.NETBANK.FCBOND_ADD_PASS, fcBondadd);
		} else {
			// 审批打回，记入日志
			fcBondLogService.saveFcBondMessge(fcBondadd.getId(), fcBondadd.getAddMoney(),
					FcBondConstant.MESSAGE_TYPE_CHECK_BACK,CmsProcessMsgConstant.MSGA_CHECKBACK, user);
			// 追加保证金审批打回时,发出通知告知已审批通过
			notificationService.send(fcBondadd.getId(), fcBondVO.getCorpId(),
					NotificationConstant.NETBANK.FCBOND_ADD_BACK, fcBondadd);
		}
		// 更新追加保证金表中的信息
		fcBondadd.setStatus(status);
		fcBondadd.setUpdateBy(workFlowResult.getUser().getLoginName());
		fcBondadd.setUpdateDate(DateUtils.getDate());
		fcBondadd.setCreateDate(new Date());
		fcBondAddServiceI.updateEntity(fcBondadd);
	}
	/**
	 * 查询待审批追加保证金列表
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput getApproveDataTables(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FcBondConstant.FC_FCBONDADD_FLOW,dataTablesInput);
		DataTablesOutput dp = fcBondAddServiceI.getApproveDataTables(dataTablesInput);
		return workFlowBusiness.merge(dp);
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
	//生成头寸调拨
	public void saveNisAllocationReq(FcBondAddDTO bondAdd, BtBankAccDTO bankDto, ICurrentUser user){
		FcBondDetailVO bond = fcBondDetailService.getFcBondById(bondAdd.getBondId());
		BtBankAccDTO payeeDto = iBtBankAccService.getBankAccById(bond.getBankAccId());
		NisAllocationReqDTO dto = new NisAllocationReqDTO();
		dto.setErpId(bondAdd.getId());
		dto.setStatus(AllocationConstant.STATUE_TEMP);
		dto.setBillFrom(AllocationConstant.BILL_FROM_4);
		dto.setType(AllocationConstant.ALLOCATION_TYPE_POS);//头寸调拨
		dto.setIsQuick("0");//不加急
		dto.setCurId(bond.getCurId());//币别ID
		dto.setCorpId(bond.getCorpId());
		dto.setPayerAccId(bankDto.getId());
		dto.setPayerAcc(bankDto.getBankAcc());
		dto.setPayerAccName(bankDto.getBankName());
		dto.setMoney(bondAdd.getAddMoney());
		dto.setPayeeAccId(payeeDto.getId());
		dto.setPayeeAcc(payeeDto.getBankAcc());
		dto.setPayeeAccName(payeeDto.getBankName());
		dto.setPurpose(bondAdd.getPurpose());
		dto.setWishPayday(new Date());
		iNisAllocationReqService.saveNisAllocationReq(dto,user);
	}

	public void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify) throws WorkFlowVerifyException {
		System.out.println(3242342);

	};

}
