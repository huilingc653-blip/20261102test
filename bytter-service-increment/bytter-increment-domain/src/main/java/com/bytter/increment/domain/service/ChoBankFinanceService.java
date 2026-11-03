package com.bytter.increment.domain.service;

import com.bytter.core.common.exception.ParameterException;
import com.bytter.increment.api.dto.BisFinanceExcDTO;
import com.bytter.increment.api.enums.ChoInnerFinanceMessageEnum;
import com.bytter.increment.api.service.IBisFinanceExcService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.BankFinanceConstant;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoWorkFlowConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.domain.dao.ChoBankFinanceDao;
import com.bytter.increment.domain.entity.ChoBankFinance;
import com.bytter.increment.domain.vo.ChoBankFinanceVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: CHO_BANK_FINANCE
 * @date 2017-08-18 14:03:30
 */
@WorkFlow({
		ChoWorkFlowConstant.CHO_BANK_C2T,
		ChoWorkFlowConstant.CHO_BANK_T2C,
		ChoWorkFlowConstant.CHO_BANK_C2N,
		ChoWorkFlowConstant.CHO_BANK_N2C})
@Service("choBankFinanceService")
@Transactional
public class ChoBankFinanceService extends BaseService<ChoBankFinance, String> implements IWorkFlowListener, IWorkFlowRevokeListener, ICorpDisableVerifyService {
	@Autowired
	private ChoBankFinanceDao choBankFinanceDao;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBisFinanceExcService bisFinanceExcService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	@Autowired
	private ChoMessageService choMessageService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@Resource
    private IMultiLangService multiLangServiceI;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(ChoBankFinance entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(ChoBankFinance entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(ChoBankFinance entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(ChoBankFinance entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<ChoBankFinance, String> getEntityDao() {
		return choBankFinanceDao;
	}
	/**
	 * 查询理财信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		String busType = (String) dataTablesInput.getFormdata().get("busType");
		String isBl = (String) dataTablesInput.getFormdata().get("isBl");//是否补录 0-否 1-是
		String opType = (String) dataTablesInput.getFormdata().get("opType");//区分定转活 通知转活
		String joinSql = "";
		String resource = "";
		if (StringUtils.isNotEmpty(opType) && "listSel".equals(opType)) {
			//获取已补录且未取款完成的的活期转定或活期转通知
			joinSql = " and f.bus_type=" + busType + " and f.is_bl = '1'" +
					" and ((f.is_online = '0' and " +
					" (select bt_nvl_num(sum(t.pay_money), 0) " +
					" from cho_bank_finance t" +
					" where t.pay_bill_id = f.id" +
					" and t.status = 95) < f.pay_money) or " +
					" (f.is_online = '1' and" +
					" (select bt_nvl_num(sum(t.pay_money), 0)" +
					" from cho_bank_finance t" +
					" left join bis_finance_exc e on t.id = e.cbs_bill_id " +
					" where t.pay_bill_id = f.id and e.voucher_stat = '0') < f.pay_money))";
			return choBankFinanceDao.getDataTablesQuery(dataTablesInput, joinSql);
		} else {
			if (StringUtils.isNotEmpty(isBl) && BankFinanceConstant.BL_1.equals(isBl)) {
				if (BankFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)) {
					resource = ResourceConstant.CHO_BANKFINANCEC2TBL_CHOBANKFINANCE_QUERY;
				} else if (BankFinanceConstant.BUS_TYPE_C2N.toString().equals(busType)) {
					resource = ResourceConstant.CHO_BANKFINANCEC2NBL_CHOBANKFINANCE_QUERY;
				}
				//获取对应业务类型的审批通过状态的数据：1.离线账号的活期转定期/通知审批通过后；2.在线账号的活期转定期/通知指令成功后
				joinSql = " and f.bus_type=" + busType + " and f.create_by ='" + user.getLoginName() + "' and f.is_bl = '0'";
				joinSql += " and ((f.status = " + BankFinanceConstant.STATUE_CHECK + " and f.is_online='0') or e.voucher_Stat = '0')";
			} else {
				//获取对应业务类型的暂存和打回状态的数据
				if (StringUtils.isNotEmpty(busType)) {
					if (BankFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCEC2T_CHOBANKFINANCE_QUERY;
					} else if (BankFinanceConstant.BUS_TYPE_C2N.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCEC2N_CHOBANKFINANCE_QUERY;
					} else if (BankFinanceConstant.BUS_TYPE_T2C.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCET2C_CHOBANKFINANCE_QUERY;
					} else if (BankFinanceConstant.BUS_TYPE_N2C.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCEN2C_CHOBANKFINANCE_QUERY;
					}
					joinSql = " and f.status in (" + BankFinanceConstant.STATUE_TEMP + "," + BankFinanceConstant.STATUE_BACK + ")";
					joinSql += " and f.bus_type=" + busType + " and f.create_by ='" + user.getLoginName() + "'";
				} else {
					resource = ResourceConstant.CHO_BANKFINANCE_CHOBANKFINANCE_QUERY;
				}
			}
			if (!StringUtils.isBlank(resource)) {
				String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
				joinSql += " and " + corpAuthSql;
			}
			return choBankFinanceDao.getDataTablesUnion(dataTablesInput, joinSql);
		}
	}
	/**
	 * 查询理财信息（组件使用）
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnionForSelect(DataTablesInput dataTablesInput, ICurrentUser user) {
		String busType = (String) dataTablesInput.getFormdata().get("busType");
		String isBl = (String) dataTablesInput.getFormdata().get("isBl");//是否补录 0-否 1-是
		String opType = (String) dataTablesInput.getFormdata().get("opType");//区分定转活 通知转活
		String joinSql = "";
		String resource = "";
		if (StringUtils.isNotEmpty(opType) && "listSel".equals(opType)) {
			//获取已补录且未取款完成的的活期转定或活期转通知
			joinSql = " and f.bus_type=" + busType + " and f.is_bl = '1'" +
					" and ((f.is_online = '0' and " +
					" (select bt_nvl_num(sum(t.pay_money), 0) " +
					" from cho_bank_finance t" +
					" where t.pay_bill_id = f.id" +
					" and t.status = 95) < f.pay_money) or " +
					" (f.is_online = '1' and" +
					" (select bt_nvl_num(sum(t.pay_money), 0)" +
					" from cho_bank_finance t" +
					" left join bis_finance_exc e on t.id = e.cbs_bill_id " +
					" where t.pay_bill_id = f.id and e.voucher_stat = '0') < f.pay_money))";
			return choBankFinanceDao.getDataTablesQuery(dataTablesInput, joinSql);
		} else {
			if (StringUtils.isNotEmpty(isBl) && BankFinanceConstant.BL_1.equals(isBl)) {
				if (BankFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)) {
					resource = ResourceConstant.CHO_BANKFINANCEC2TBL_CHOBANKFINANCE_QUERY;
				} else if (BankFinanceConstant.BUS_TYPE_C2N.toString().equals(busType)) {
					resource = ResourceConstant.CHO_BANKFINANCEC2NBL_CHOBANKFINANCE_QUERY;
				}
				//获取对应业务类型的审批通过状态的数据：1.离线账号的活期转定期/通知审批通过后；2.在线账号的活期转定期/通知指令成功后
				joinSql = " and f.bus_type=" + busType + "and f.create_by ='" + user.getLoginName() + "' and f.is_bl = '0'";
				joinSql += " and ((f.status = " + BankFinanceConstant.STATUE_CHECK + " and f.is_online='0') or e.voucher_Stat = '0')";
			} else {
				//获取对应业务类型的暂存和打回状态的数据
				if (StringUtils.isNotEmpty(busType)) {
					if (BankFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCEC2T_CHOBANKFINANCE_QUERY;
					} else if (BankFinanceConstant.BUS_TYPE_C2N.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCEC2N_CHOBANKFINANCE_QUERY;
					} else if (BankFinanceConstant.BUS_TYPE_T2C.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCET2C_CHOBANKFINANCE_QUERY;
					} else if (BankFinanceConstant.BUS_TYPE_N2C.toString().equals(busType)) {
						resource = ResourceConstant.CHO_BANKFINANCEN2C_CHOBANKFINANCE_QUERY;
					}
					joinSql = " and f.status in (" + BankFinanceConstant.STATUE_TEMP + "," + BankFinanceConstant.STATUE_BACK + ")";
					joinSql += " and f.bus_type=" + busType + " and f.create_by ='" + user.getLoginName() + "'";
				} else {
					resource = ResourceConstant.CHO_BANKFINANCE_CHOBANKFINANCE_QUERY;
				}
			}
			if (!StringUtils.isBlank(resource)) {
				String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
				joinSql += " and " + corpAuthSql;
			}
			return choBankFinanceDao.getDataTablesUnionForSelect(dataTablesInput, joinSql);
		}
	}
	/**
	 * 根据ID获取理财信息
	 *
	 * @param id
	 * @return
	 */
	public ChoBankFinanceVO getByIdUnion(String id) {
		ChoBankFinanceVO choBankFinanceVO = choBankFinanceDao.getByIdUnion(id);
		//定期转活期/通知转活期，获取活期账号的ID
		if (BankFinanceConstant.BUS_TYPE_T2C.equals(choBankFinanceVO.getBusType())
				|| BankFinanceConstant.BUS_TYPE_N2C.equals(choBankFinanceVO.getBusType())) {
			BtBankAccDTO bankAcc = btBankAccService.getBankAccByBankAccAndCurId(choBankFinanceVO.getPayeeAcc(), choBankFinanceVO.getCurId());
			if (bankAcc != null) {
				choBankFinanceVO.setBankAccOnline(bankAcc.getIsOnline());
			}
		}
		return choBankFinanceVO;
	}
	/**
	 * 保存银行理财信息
	 *
	 * @param choBankFinance
	 */
	public void saveBankFinance(ChoBankFinanceVO choBankFinance, ICurrentUser user) {
		ChoBankFinance entity = new ChoBankFinance();
		BeanUtils.copyNotNullProperties(choBankFinance, entity);
		//补充  活转定 相关名称信息
		if (!StringUtils.isEmpty(entity.getPayerAcc())){
			BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(entity.getPayerAcc());
			Optional.ofNullable(bankAccDTO).ifPresent(bankAccDO -> {
				entity.setAccName(bankAccDO.getAccName());
				entity.setBankName(bankAccDO.getBankName());
				entity.setPayerAccId(bankAccDO.getId());
			});
		}
		if (!StringUtils.isEmpty(entity.getCurId())){
			BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(entity.getCurId());
			Optional.ofNullable(currencyDTO).ifPresent(currencyDO -> entity.setCurName(currencyDO.getCurName()));
		}
		if(!StringUtils.isEmpty(entity.getCorpId())){
			CorpDTO corpDTO = corpService.get(entity.getCorpId());
			Optional.ofNullable(corpDTO).ifPresent(corpDO -> entity.setCorpName(corpDO.getName()));
		}
		//定期转活期/通知转活期，获取活期账号的ID
		if (BankFinanceConstant.BUS_TYPE_T2C.equals(entity.getBusType())
				|| BankFinanceConstant.BUS_TYPE_N2C.equals(entity.getBusType())) {
			BtBankAccDTO bankAcc = btBankAccService.getBankAccByBankAccAndCurId(entity.getPayeeAcc(), entity.getCurId());
			if (bankAcc != null) {
				entity.setPayeeAccId(bankAcc.getId());
			}
		}
		//流水ID可能过长
		String billNo=SequenceSingleton.getSequence(SequenceEnum.CHO_BANK_FINANCE);
		if(billNo.length()>12){
			entity.setBillNo(billNo.substring(0,12));
		}else {
			entity.setBillNo(billNo);
		}
		entity.setCreateBy(user.getLoginName());
		entity.setUsername(user.getUsername());
		entity.setCreateDate(DateUtils.getDate());
		this.saveEntity(entity);
		Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
		if (BankFinanceConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
			//调用审批后方法 todo:需要生成对应的指令信息
			 getStartHandler().handler(this.getWorkFlowByBusType(entity.getBusType()), entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		//生成日志
		choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_BANK, user);
	}
	/**
	 * 修改银行理财信息
	 *
	 * @param choBankFinance
	 */
	public void updateBankFinance(ChoBankFinanceVO choBankFinance, ICurrentUser user) {
		//单号长度校验
		if (StringUtils.isNotBlank(choBankFinance.getBankBillNo()) && choBankFinance.getBankBillNo().length() > 20) {
			throw new ParameterException(ChoInnerFinanceMessageEnum.CHO_BANK_FINANCE_BANK_BILL_NO_LENGTH);
		}

		ChoBankFinance entity = this.get(choBankFinance.getId());
		BeanUtils.copyNotNullProperties(choBankFinance, entity);
		Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
		//补录
		if (BankFinanceConstant.STATUE_CHECK.equals(entity.getStatus())) {
			entity.setBankBillNo(choBankFinance.getBankBillNo());//单号
			entity.setBlInterestRate(choBankFinance.getBlInterestRate());//利率
			entity.setBlInterest(choBankFinance.getBlInterest());//利息
			entity.setIsBl(CommonConstant.YES);//是否补录 0-否 1-是
			messageType = ChoMessageConstant.MESSAGE_TYPE_BL;
		} else {
			BeanUtils.copyNotNullProperties(choBankFinance, entity);
			//定期转活期/通知转活期，获取活期账号的ID
			if (BankFinanceConstant.BUS_TYPE_T2C.equals(entity.getBusType())
					|| BankFinanceConstant.BUS_TYPE_N2C.equals(entity.getBusType())) {
				BtBankAccDTO bankAcc = btBankAccService.getBankAccByBankAccAndCurId(entity.getPayeeAcc(), entity.getCurId());
				if (bankAcc != null) {
					entity.setPayeeAccId(bankAcc.getId());
				}
			}
		}
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		if (BankFinanceConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
			//调用审批后方法
			 getStartHandler().handler(this.getWorkFlowByBusType(entity.getBusType()), entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		//生成日志
		choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_BANK, user);
		//完成通知提醒
		String tag = "";
		if (BankFinanceConstant.BUS_TYPE_C2T.equals(entity.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_C2T;
		} else if (BankFinanceConstant.BUS_TYPE_C2N.equals(entity.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_T2C;
		} else if (BankFinanceConstant.BUS_TYPE_T2C.equals(entity.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_C2N;
		} else if (BankFinanceConstant.BUS_TYPE_N2C.equals(entity.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_N2C;
		}
		if (!StringUtils.isBlank(tag)) {
			//消息提醒
			notificationService.complete(tag, entity.getId(), user.getId());
		}
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		ChoBankFinance entity = this.get(workFlowResult.getBusinessKey(), true);
		Integer messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		if (workFlowResult.isPass()) {
			messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_PASS;
			entity.setStatus(BankFinanceConstant.STATUE_CHECK);//审批通过
		} else {
			entity.setStatus(BankFinanceConstant.STATUE_BACK);//审批打回
		}
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		//审批通过，在线账号发指令
		if (BankFinanceConstant.STATUE_CHECK.equals(entity.getStatus()) && CommonConstant.YES.equals(entity.getIsOnline())) {
			this.saveBisFinanceExc(entity, user);
		}
		//生成日志
		choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_BANK, user);
	}
	/**
	 * 理财指令生成
	 *
	 * @param entity
	 */
	public void saveBisFinanceExc(ChoBankFinance entity, ICurrentUser user) {
		//生成付款指令
		BisFinanceExcDTO bisFinanceExc = new BisFinanceExcDTO();
		//获取活期账号
		BtBankAccDTO bankAcc = null;
		//活期转定期/活期转通知时，需要判断活期账号是否是离线账号
		if (BankFinanceConstant.BUS_TYPE_C2T.equals(entity.getBusType())
				|| BankFinanceConstant.BUS_TYPE_C2N.equals(entity.getBusType())) {
			//获取付款账号（活转定/通知，付款方为活期账号）
			bankAcc = btBankAccService.getBankAccByIdAndCurId(entity.getPayerAccId(), entity.getCurId());
			bisFinanceExc.setPayerAccName(bankAcc.getAccName());
		} else {
			//获取收款账号（定/通知转活，收款放为活期账号）
			bankAcc = btBankAccService.getBankAccByIdAndCurId(entity.getPayeeAccId(), entity.getCurId());
			bisFinanceExc.setPayeeAcc(entity.getPayeeAcc());
			bisFinanceExc.setPayeeAccName(bankAcc.getBankName());
		}
		//判断活期账号是否是离线账号，离线账号不生成指令
		if (bankAcc == null || BtBankAccConstant.ISONLINE_0.equals(bankAcc.getIsOnline())) {
			return;
		}
		bisFinanceExc.setCbsBillId(entity.getId());
		bisFinanceExc.setCbsBillNo(entity.getBillNo());
		bisFinanceExc.setCorpId(entity.getCorpId());
		bisFinanceExc.setReqDate(DateUtils.getDate());
		//付款方信息
		bisFinanceExc.setPayerAcc(entity.getPayerAcc());
		bisFinanceExc.setPayerBank(bankAcc.getBankName());
		bisFinanceExc.setBifCode(bankAcc.getBifCode());
		bisFinanceExc.setCurId(entity.getCurId());
		bisFinanceExc.setPayMoney(entity.getPayMoney());
		bisFinanceExc.setBusType(entity.getBusType());
		bisFinanceExc.setDayLimit(entity.getDayLimit());
		bisFinanceExc.setDateUnit(entity.getDateUnit());
		bisFinanceExc.setCertificateWay(entity.getCertificateWay());
		bisFinanceExc.setInterestRate(entity.getInterestRate());
		bisFinanceExc.setBursarBillNo(entity.getBursarBillNo());
		bisFinanceExc.setWishPayday(entity.getWishPayday());
		bisFinanceExc.setPurpose(entity.getPurpose());
		bisFinanceExc.setVoucherStat(BdlinkConstant.WAIT_FILL);
		bisFinanceExcService.addBisFinanceExc(bisFinanceExc, user);
	}
	/**
	 * 根据业务类型返回不同的跳转页面
	 *
	 * @param busType
	 * @return
	 */
	public String getWorkFlowByBusType(Integer busType) {
		/**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期*/
		String workFlow = "";
		if (BankFinanceConstant.BUS_TYPE_C2T.intValue() == busType.intValue()) {
			workFlow = ChoWorkFlowConstant.CHO_BANK_C2T;
		} else if (BankFinanceConstant.BUS_TYPE_T2C.intValue() == busType.intValue()) {
			workFlow = ChoWorkFlowConstant.CHO_BANK_T2C;
		} else if (BankFinanceConstant.BUS_TYPE_C2N.intValue() == busType.intValue()) {
			workFlow = ChoWorkFlowConstant.CHO_BANK_C2N;
		} else if (BankFinanceConstant.BUS_TYPE_N2C.intValue() == busType.intValue()) {
			workFlow = ChoWorkFlowConstant.CHO_BANK_N2C;
		}
		return workFlow;
	}
	/**
	 * 审批列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		String sign = "";
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqi_busType"))) {
			String type = String.valueOf(dataTablesInput.getFormdata().get("eqi_busType"));
			if (EmptyUtils.isNumeric(type)) {
				sign = this.getWorkFlowByBusType(new Integer(type));
			}
		}
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
		String businessSql = null;
		if(!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())){
			businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "f.id", true);
		} else {
			// 无待审批业务数据时直接返回空结果，避免businessSql为null时DAO查询返回所有非审批通过状态的数据
			return new DataTablesOutput(dataTablesInput.getDraw(), new ArrayList<>(), 0, 0);
		}
		DataTablesOutput<ChoBankFinanceVO> dp = this.choBankFinanceDao.dataTableApprove(dataTablesInput, businessSql);
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 逻辑删除理财信息
	 *
	 * @param id
	 */
	public void delChoBankFinance(String id, ICurrentUser user) {
		ChoBankFinance choBankFinance = this.get(id);
		choBankFinance.setStatus(BankFinanceConstant.STATUE_DEL);
		choBankFinance.setUpdateBy(user.getLoginName());
		choBankFinance.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(choBankFinance);
		//生成日志
		choMessageService.saveMessage(choBankFinance.getId(), ChoMessageConstant.MESSAGE_TYPE_DELETE, "", ChoMessageConstant.BUS_TYPE_BANK, user);
		//完成通知提醒
		String tag = "";
		if (BankFinanceConstant.BUS_TYPE_C2T.equals(choBankFinance.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_C2T;
		} else if (BankFinanceConstant.BUS_TYPE_C2N.equals(choBankFinance.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_T2C;
		} else if (BankFinanceConstant.BUS_TYPE_T2C.equals(choBankFinance.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_C2N;
		} else if (BankFinanceConstant.BUS_TYPE_N2C.equals(choBankFinance.getBusType())) {
			tag = NotificationConstant.CHOWMATISTIC.BANK_FINANCE_N2C;
		}
		if (!StringUtils.isBlank(tag)) {
			//消息提醒
			notificationService.complete(tag, choBankFinance.getId(), user.getId());
		}
	}
	/**
	 * 存款类业务查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
		String joinSql = "";
		String resource = ResourceConstant.CHO_BANKFINANCE_CHOBANKFINANCE_LIST_QUERY;
		if (!StringUtils.isBlank(resource)) {
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
			joinSql += " and " + corpAuthSql;
		}
		return choBankFinanceDao.getDataTablesQuery(dataTablesInput, joinSql);
	}
	/**
	 * 银行存款应收未收利息统计表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesReport(DataTablesInput dataTablesInput, ICurrentUser user) {
		String joinSql = "";
		String resource = ResourceConstant.CHO_BANKFINANCE_CHOBANKFINANCE_REPORT_QUERY;
		if (!StringUtils.isBlank(resource)) {
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
			joinSql += " and " + corpAuthSql;
		}
		//获取取款成功的明细
		DataTablesOutput datas = choBankFinanceDao.getDataTablesReport(dataTablesInput, joinSql);
		if (datas != null && datas.getData() != null) {
			List<ChoBankFinanceVO> list = datas.getData();
			for (ChoBankFinanceVO vo : list) {
				//期限 = 到期日期 - 起息日期
				int limit = DateUtils.dayDiffs(vo.getDeadlineDate(), vo.getInterestStartDate());
				//参考收益 = 金额 * 利率 * 期限
				vo.setReferInterest(vo.getPayMoney().multiply(vo.getInterestRate()).multiply(new BigDecimal(limit)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
				//计算天数 = 截止日期 - 起息日期
				int calDays = DateUtils.dayDiffs(vo.getCalEndDate(), vo.getInterestStartDate());
				if (vo.getMaxWishPayday() == null) {
					//截止日内不存在支取：应收利息 = 金额*补录利率*（截止日期-起息日期）
					if (vo.getBlInterestRate() == null) {
						vo.setInterestReceivable(vo.getPayMoney().multiply(vo.getInterestRate()).multiply(new BigDecimal(calDays)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
					} else {
						vo.setInterestReceivable(vo.getPayMoney().multiply(vo.getBlInterestRate()).multiply(new BigDecimal(calDays)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
					}
					vo.setBlInterest(BigDecimal.ZERO);
				} else {
					//截止日内存在支取：应收利息 = 转账金额*支取利率*（转账日期-起息日期）+（金额-转账金额）*补录利率*（截止日期-转账日期）
					//获取定转活或通知转活的数据
					List<ChoBankFinanceVO> drawList = choBankFinanceDao.listByPayBillId(vo.getId());
					BigDecimal sumDrawMoney = BigDecimal.ZERO;//累计取款金额
					BigDecimal sumBlInterest = BigDecimal.ZERO;//累计实收利息
					boolean flag = false;
					for (int i = 0; i < drawList.size(); i++) {
						ChoBankFinanceVO draw = drawList.get(i);
						//到期日期<转账日期（超期提取）
						if (DateUtils.dayDiffs(draw.getWishPayday(), draw.getDeadlineDate()) > 0) {
							flag = true;
							//正常期限利息
							BigDecimal interestTemp1 = draw.getPayMoney().multiply(vo.getBlInterest()).multiply(new BigDecimal(limit)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP);
							//超期期限 = 截止日期 - 到期日期
							int limitTemp = DateUtils.dayDiffs(vo.getCalEndDate(), vo.getDeadlineDate());
							//超期期限利息
							BigDecimal interestTemp2 = (draw.getPayMoney().add(interestTemp1)).multiply(draw.getInterestRate()).multiply(new BigDecimal(limitTemp)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP);
							sumBlInterest = sumBlInterest.add(interestTemp1.add(interestTemp2));
						} else {
							//实收利息 = 转账金额*支取利率*（转账日期-起息日期）
							int calDaysTemp = DateUtils.dayDiffs(draw.getWishPayday(), draw.getInterestStartDate());
							//if (i == 0) {
								sumBlInterest = sumBlInterest.add(draw.getPayMoney().multiply(draw.getInterestRate()).multiply(new BigDecimal(calDaysTemp)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
							/*} else {
								sumBlInterest = sumBlInterest.add((vo.getPayMoney().subtract(sumDrawMoney)).multiply(draw.getInterestRate()).multiply(new BigDecimal(calDaysTemp)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP));
							}*/
						}
						sumDrawMoney = sumDrawMoney.add(draw.getPayMoney());
					}
					if (flag) {
						vo.setInterestReceivable(sumBlInterest);
					} else {
						BigDecimal drawBal = vo.getPayMoney().subtract(vo.getDrawMoney());//剩余本金
						//应收利息 = 剩余本金*补录利率*（截止日期-起息日期）+ 实收利息
						vo.setInterestReceivable(drawBal.multiply(vo.getBlInterestRate()).multiply(new BigDecimal(calDays)).divide(new BigDecimal(360), 6, BigDecimal.ROUND_HALF_UP).add(sumBlInterest));
					}
					vo.setBlInterest(sumBlInterest);
				}
				vo.setCalDays(calDays);//计算天数
				vo.setInterestReceivableNot(vo.getInterestReceivable().subtract(vo.getBlInterest()));//应收未收利息 = 应收利息-实收利息
			}
		}
		return datas;
	}
	/**
	 * 撤回
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowRevoke(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		ChoBankFinance entity = this.get(workFlowResult.getBusinessKey(), true);
		entity.setStatus(BankFinanceConstant.STATUE_TEMP);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		//生成日志
		choMessageService.saveMessage(entity.getId(), ChoMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", ChoMessageConstant.BUS_TYPE_BANK, user);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在途的银行存款（活期转定期）单据，不允许禁用！
			int num = this.choBankFinanceDao.getBankFinanceCountByCorpId(corpId, BankFinanceConstant.BUS_TYPE_C2T);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.CHOWMATISTIC,
						SysConfigConstant.BusType.CHO_BANKFINANCE_C2T,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_BANKFINANCE_C2T_ERROR)));
			}
			//单位有在途的银行存款（定期转活期）单据，不允许禁用！
			num = this.choBankFinanceDao.getBankFinanceCountByCorpId(corpId, BankFinanceConstant.BUS_TYPE_T2C);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.CHOWMATISTIC,
						SysConfigConstant.BusType.CHO_BANKFINANCE_T2C,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_BANKFINANCE_T2C_ERROR)));
			}
			//单位有在途的银行存款（活期转通知）单据，不允许禁用！
			num = this.choBankFinanceDao.getBankFinanceCountByCorpId(corpId, BankFinanceConstant.BUS_TYPE_C2N);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.CHOWMATISTIC,
						SysConfigConstant.BusType.CHO_BANKFINANCE_C2N,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_BANKFINANCE_C2N_ERROR)));
			}
			//单位有在途的银行存款（通知转活期）单据，不允许禁用！
			num = this.choBankFinanceDao.getBankFinanceCountByCorpId(corpId, BankFinanceConstant.BUS_TYPE_N2C);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.CHOWMATISTIC,
						SysConfigConstant.BusType.CHO_BANKFINANCE_N2C,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_BANKFINANCE_N2C_ERROR)));
			}
			//单位有待补录的银行存款（活期转定期）单据，不允许禁用！
			num = this.choBankFinanceDao.getBankFinanceBlCountByCorpId(corpId, BankFinanceConstant.BUS_TYPE_C2T);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.CHOWMATISTIC,
						SysConfigConstant.BusType.CHO_BANKFINANCE_C2T,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_BANKFINANCE_C2T_BL_ERROR)));
			}
			//单位有待补录的银行存款（活期转通知）单据，不允许禁用！
			num = this.choBankFinanceDao.getBankFinanceBlCountByCorpId(corpId, BankFinanceConstant.BUS_TYPE_C2N);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.CHOWMATISTIC,
						SysConfigConstant.BusType.CHO_BANKFINANCE_C2N,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_BANKFINANCE_C2N_BL_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 获取银行存款信息
	 * @param user  用户
	 * @param resource  资源
	 * @param busType   10-活期转定期（定期存款） 12-活期转通知（通知存款）
	 * @return
	 */
	public List<ChoBankFinanceVO> listByBusType(ICurrentUser user, String resource, String busType, Date createDate, Date deadlineDate) {
		String joinSql = "";
		if (!StringUtils.isBlank(resource)) {
			List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
			String corpAuthSql = SqlUtil.getInSql(corpIds, "f.corp_Id", true);
			joinSql += " and " + corpAuthSql;
		}
		return this.choBankFinanceDao.listByBusType(busType, createDate, deadlineDate, joinSql);
	}


	public ChoBankFinance getBankFinanceByVoucherNo(String voucherNo) {
		return choBankFinanceDao.getChoBankFinanceByVoucherNo(voucherNo);
	}

	public int updateBankFinance(ChoBankFinance entity) {
		try {
		choBankFinanceDao.update(entity);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
}
