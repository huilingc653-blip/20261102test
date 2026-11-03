package com.bytter.biz.allocation.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
//import com.bytter.biz.payment.api.service.IPaymentBlendingService;

import com.bytter.biz.allocation.vo.NisallocationPosApproveVO;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.allocation.dao.NisAllocationReqDao;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.vo.BtBankAccBalVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookExtDTO;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.netbank.service.BisBifRuleService;
import com.bytter.biz.netbank.service.NisAccbookService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.NisAccbookVO;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.NisWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IBisExcBackService;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IBisGatherCmdService;
import com.bytter.biz.payment.api.service.IBtBankAccBalService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.IWorkFlowSubmitVerifyService;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.api.ICmsProvideLoanService;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.enums.AccountGrantMessageEnum;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.hutool.core.util.ObjectUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: NIS_ALLOCATION_REQ
 * @date 2017-05-31 10:25:52
 */
@WorkFlow({
		NisWorkFlowConstant.NIS_ALLOCATION_DOWN,
		NisWorkFlowConstant.NIS_ALLOCATION_ASK,
		NisWorkFlowConstant.NIS_ALLOCATION_POS,
		NisWorkFlowConstant.NIS_ALLOCATION_UP,
		NisWorkFlowConstant.NIS_ALLOCATION_TURN})
@Service("nisAllocationReqService")
@DubboService(interfaceClass = IWorkFlowListener.class, group = "netbankAllocation")
@Transactional
public class NisAllocationReqService extends BaseService<NisAllocationReq, String> implements IWorkFlowListener, IWorkFlowRevokeListener, ICorpDisableVerifyService, ICorpParentChangVerifyService, IWorkFlowSubmitVerifyService, IUserExcelVerifyHandler<NisAllocationReqVO> {
	@Autowired
	private NisAllocationReqDao nisAllocationReqDao;
	@DubboReference(check = false)
	private IBisExcService bisExcServiceImpl;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@Autowired
	private BisBifRuleService bisBifRuleService;
	@DubboReference(check = false)
	private IBisGatherCmdService bisGatherCmdService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@Autowired
	private NisMessageService nisMessageService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@Autowired
	private NisAccbookService nisAccbookService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@DubboReference(check = false)
	private ICAService caService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private ICmsProvideLoanService cmsProvideLoanService;
	@DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
	@Resource
	private IMultiLangService multiLangServiceI;
	@Autowired(required = false)
	private IFcBondHookService IFcBondHookService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IPaymentBlendingService paymentBlendingService;
	@DubboReference(check = false)
	private IUserService iUserService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired
	private IBisExcBackService allocationBisExcBackService;
	@DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;
	@Autowired
	private IBtBankAccBalService btBankAccBalService;
	@DubboReference(check = false)
	private IBtBankAccCurService btBankAccCurService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IDictService dictService;


	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisAllocationReq entity) {
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
	public void save(NisAllocationReq entity) {
		super.saveEntity(entity);
	}

	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisAllocationReq entity) {
		super.updateEntity(entity);
	}

	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisAllocationReq entity) {
		super.saveOrUpdate(entity);
	}

	@Override
	public BaseDao<NisAllocationReq, String> getEntityDao() {
		return nisAllocationReqDao;
	}

	@SuppressWarnings("rawtypes")
	public DataTablesOutput<NisAllocationReqListDTO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		Object viewType = dataTablesInput.getFormdata().get("viewType");
		if (!EmptyUtils.isEmpty(viewType) && "fund".equalsIgnoreCase(viewType.toString())) {
			String resource = ResourceConstant.NETBANK_ALLOCATION_TYPE_FUND_QUERY;
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "ar.corp_Id", true);
			sql.append(" and ").append(corpAuthSql);
			//过滤掉删除状态的付款单
			sql.append(" and ar.status<>" + AllocationConstant.STATUE_DEL);
		} else {
			Object obj = dataTablesInput.getFormdata().get("eqi_type");
			if (StringUtils.isNotEmpty(obj)) {
				String resource = ResourceConstant.ALLOCATION_TYPE_MAP.get(Integer.valueOf(String.valueOf(obj)));
				if (StringUtils.isNotBlank(resource)) {
					String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "ar.corp_Id", true);
					sql.append(" and ").append(corpAuthSql);
				}
			}
			if (!EmptyUtils.isBlank(sql.toString())) {
				sql.append(" and ");
			}
			sql.append(" (( ar.create_by ='").append(user.getLoginName()).append("' and ar.bill_from='").append(AllocationConstant.BILLFROM_1).append("')");
			sql.append(" or ar.bill_from<>'").append(AllocationConstant.BILLFROM_1).append("')");
		}
		DataTablesOutput dataTablesOutput = this.nisAllocationReqDao.getDataTablesUnion(dataTablesInput, sql.toString(), params);
		List<NisAllocationReqListDTO> dataList = dataTablesOutput.getData();
		if (ObjectUtil.isNotEmpty(dataList)){
			// 获取dataList中getPayerAccId和getPayeeAccId组成的集合
			List<String> accIds = new ArrayList<>();
			accIds.addAll(dataList.stream().map(NisAllocationReqListDTO::getPayerAccId).collect(Collectors.toSet()));
			accIds.addAll(dataList.stream().map(NisAllocationReqListDTO::getPayeeAccId).collect(Collectors.toSet()));
			// 查询账号
			Map<String, BtBankAccDTO> accMap = btBankAccService.getBankAccUnByIds(accIds);
			// 设置业务类型
			Map<String, DictDTO> dictTypeMap = dictService.listMap("nisAllocationReq.type");
			Map<String, DictDTO> dictStatusMap = dictService.listMap("netbank.statusAll");
			Map<String, DictDTO> dictVStatMap = dictService.listMap("bisExc.voucherStat");
			for (NisAllocationReqListDTO reqDTO : dataList) {
				if (accMap.containsKey(reqDTO.getPayerAcc())) {
					reqDTO.setPayerBankName(accMap.get(reqDTO.getPayerAcc()).getBankName());
					reqDTO.setPayerCorpId(accMap.get(reqDTO.getPayerAcc()).getCorpId());
					reqDTO.setPayerCorpName(accMap.get(reqDTO.getPayerAcc()).getCorpName());
				}
				if (accMap.containsKey(reqDTO.getPayeeAcc())) {
					reqDTO.setPayeeBankName(accMap.get(reqDTO.getPayeeAcc()).getBankName());
					reqDTO.setPayeeCorpId(accMap.get(reqDTO.getPayeeAcc()).getCorpId());
					reqDTO.setPayeeCorpName(accMap.get(reqDTO.getPayeeAcc()).getCorpName());
				}
				if (ObjectUtil.isNotEmpty(reqDTO.getType())){
					reqDTO.setTypeName(dictTypeMap.get(reqDTO.getType().toString()) == null ? "" :dictTypeMap.get(reqDTO.getType().toString()).getName());
				}
				if (ObjectUtil.isNotEmpty(reqDTO.getStatus())){
					String statsName = "";
					if(reqDTO.getStatus() == 95 && "0".equals(reqDTO.getTallySign())){
						statsName = "待记账";
					}else if(reqDTO.getStatus() == 95 && "1".equals(reqDTO.getTallySign())){
						statsName = "记账复核通过";
					}else if(reqDTO.getStatus() == 95 && "2".equals(reqDTO.getTallySign())){
						statsName = "记账无效";
					}else if(reqDTO.getStatus() == 95 && "3".equals(reqDTO.getTallySign())){
						statsName = "记账待复核";
					}else if(reqDTO.getStatus() == 95 && "4".equals(reqDTO.getTallySign())){
						statsName = "记账打回";
					}else if(reqDTO.getStatus() == 95 && "5".equals(reqDTO.getTallySign())){
						statsName = "记账暂存";
					}else if(reqDTO.getStatus() == 95 && "8".equals(reqDTO.getTallySign())){
						statsName = "自动记账失败";
					}else{
						statsName  =dictStatusMap.get(reqDTO.getStatus().toString()) == null ? "" :dictStatusMap.get(reqDTO.getStatus().toString()).getName();
					}
					reqDTO.setStatusName(statsName);
				}

				String voucherStat = "";
				if (reqDTO.getType() == 10 || reqDTO.getType() == 14){
					if (ObjectUtil.isNotEmpty(reqDTO.getCmdStat())){
						voucherStat = dictVStatMap.get(reqDTO.getCmdStat()) == null ? "" :dictVStatMap.get(reqDTO.getCmdStat()).getName();
					}
				}else if (ObjectUtil.isNotEmpty(reqDTO.getVoucherStat())){
					voucherStat = dictVStatMap.get(reqDTO.getVoucherStat()) == null ? "" :dictVStatMap.get(reqDTO.getVoucherStat()).getName();
				}
				reqDTO.setVoucherStatName(voucherStat);
			}
			/*for (NisAllocationReqListDTO nisAllocationReqListDTO : dataList) {
				this.setBankName(nisAllocationReqListDTO);
				this.setCreateByName(nisAllocationReqListDTO);
				this.setCurName(nisAllocationReqListDTO);
				this.setCorpName(nisAllocationReqListDTO);
			}*/
		}
		return dataTablesOutput;
	}

	/**
	 * 根据id查找资金调拨信息
	 *
	 * @param id
	 * @return NisAllocationReqVO
	 * @author zuoy 2017-06-22
	 */
	public NisAllocationReqListDTO getByUnion(String id) {
		return nisAllocationReqDao.getByUnion(id);
	}

	/**
	 * 根据id查找资金调拨信息
	 * 带账户余额信息
	 *
	 * @param id
	 * @return NisAllocationReqVO
	 * @author zuoy 2017-09-25
	 */
	public NisAllocationReqListDTO getVOAllByUnion(String id) {
		NisAllocationReqListDTO vo = nisAllocationReqDao.getByUnion(id);
		if (EmptyUtils.isEmpty(vo)) {
			return null;
		}
		this.setBankName(vo);
		this.setCurName(vo);
		this.setCorpName(vo);
		// 设置收款方账户余额
		BtBankAccDTO payeeAcc = this.btBankAccService.getBankAccByIdAndCurId(vo.getPayeeAccId(), vo.getCurId());
		if (!EmptyUtils.isEmpty(payeeAcc)) {
			//vo.setPayeeAccBalance(payeeAcc.getBalance());
			vo.setPayeeCorpId(payeeAcc.getCorpId());
			vo.setPayeeCorpName(payeeAcc.getCorpName());
			vo.setPayeeBankCode(payeeAcc.getBankCode());
			vo.setPayeeBankTypeId(payeeAcc.getBankTypeId());
			BigDecimal balance = bisAccBalService.getLastByBalDate(payeeAcc.getBankAcc(), vo.getCurId());
			if (!EmptyUtils.isEmpty(balance)) {
				vo.setPayeeAccBalance(balance);
			} else {
				vo.setPayeeAccBalance(BigDecimal.ZERO);
			}
		}
		// 设置付款方账户余额
		BtBankAccDTO payerAcc = this.btBankAccService.getBankAccByIdAndCurId(vo.getPayerAccId(), vo.getCurId());
		if (!EmptyUtils.isEmpty(payerAcc)) {
			//vo.setPayerAccBalance(payerAcc.getBalance());
			vo.setPayerCorpId(payerAcc.getCorpId());
			vo.setPayerCorpName(payerAcc.getCorpName());
			BigDecimal balance = bisAccBalService.getLastByBalDate(payerAcc.getBankAcc(), vo.getCurId());
			if (!EmptyUtils.isEmpty(balance)) {
				vo.setPayerAccBalance(balance);
			} else {
				vo.setPayerAccBalance(BigDecimal.ZERO);
			}
		}

		String blnctrAcc = this.btBankAccService.getBlnctrAcc(vo.getPayerAcc(), vo.getCurId());
		FcOnlineLedgerDTO dto = this.fcOnlineLedgerService.getByBlnctrAcc(blnctrAcc);
		//查询 记账联机余额=内部账户余额
		if (ObjectUtil.isNotEmpty(dto) && !EmptyUtils.isEmpty(dto.getOnlineBalance())){
			vo.setPayerFcAccBalance(dto.getOnlineBalance());
		}else {
			vo.setPayerFcAccBalance(BigDecimal.ZERO);
		}

		String blnctrAcc2 = this.btBankAccService.getBlnctrAcc(vo.getPayeeAcc(), vo.getCurId());
		FcOnlineLedgerDTO dto2 = this.fcOnlineLedgerService.getByBlnctrAcc(blnctrAcc2);
		if (ObjectUtil.isNotEmpty(dto2) && !EmptyUtils.isEmpty(dto2.getOnlineBalance())){
			vo.setPayeeFcAccBalance(dto2.getOnlineBalance());
		}else {
			vo.setPayeeFcAccBalance(BigDecimal.ZERO);
		}
		return vo;
	}

	/**
	 * 保存下拨/上划资金调拨数据
	 *
	 * @param nisAllocationReqListDTO
	 */
	public String saveUpAndDown(NisAllocationReqListDTO nisAllocationReqListDTO, ICurrentUser user) {
		//校验时间戳，是否存在重复提交
		logger.error("下拨入口！");
		if (this.isExistTimeStamp(nisAllocationReqListDTO.getTimeStamp())) {
			throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
		}
		List<Map<String, Object>> nisAllocationList = nisAllocationReqListDTO.getNisAllocationList();
		List<NisAllocationReq> entityList = new ArrayList();
		List<String> billNos = new ArrayList<>();
		Date curDate = DateUtils.getDate();
		String loginName = user.getLoginName();
		List<String[]> itemCodeList = new ArrayList();
		List<String[]> itemNameList = new ArrayList();
		List<BigDecimal[]> processMoneyList = new ArrayList();
		List<BigDecimal[]> exchangeRateList = new ArrayList();
		List<BigDecimal[]> processCurMoneyList = new ArrayList();
		if (!ListUtils.isNullOrEmpty(nisAllocationList)) {
			for (Map<String, Object> map : nisAllocationList) {
				if (EmptyUtils.isEmpty(map.get("money")) || EmptyUtils.isBlank(String.valueOf(map.get("money")))) {
					continue;
				}
				BigDecimal money = new BigDecimal(String.valueOf(map.get("money")));
				NisAllocationReq entity = new NisAllocationReq();
				BeanUtils.copyNotNullProperties(nisAllocationReqListDTO, entity);
				//判断是否挂证书
				if (!EmptyUtils.isEmpty(map.get("caCoding")) && !EmptyUtils.isBlank(String.valueOf(map.get("caCoding")))) {
					SysCaInfoDTO caDto = new SysCaInfoDTO();
					caDto.setRandom(String.valueOf(map.get("random")));
					caDto.setEncryDate(DateUtils.getDate());
					caDto.setEncrySource(String.valueOf(map.get("encrySource")));
					caDto.setCaCoding(String.valueOf(map.get("caCoding")));
					entity.setCaInfoId(caService.saveOrUpdateCaInfo(caDto));
				}
				if (AllocationConstant.ALLOCATION_TYPE_UP.equals(nisAllocationReqListDTO.getType())) {
					entity.setPayerAcc(String.valueOf(map.get("payerAcc")));
					entity.setPayerAccId(String.valueOf(map.get("payerAccId")));
					entity.setPayerAccName(String.valueOf(map.get("payerAccName")));
				} else if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReqListDTO.getType())) {
					entity.setPayeeAcc(String.valueOf(map.get("payeeAcc")));
					entity.setPayeeAccId(String.valueOf(map.get("payeeAccId")));
					entity.setPayeeAccName(String.valueOf(map.get("payeeAccName")));
					//收款方中心账号
					entity.setPayeeCounterAcc(String.valueOf(map.get("payeeCounterAcc")));
				}
				entity.setMoney(money);
				String billNo = SequenceSingleton.getSequence(SequenceEnum.NIS_ALLOCATION_REQ);
				entity.setBillNo(billNo);
				entity.setStatus(nisAllocationReqListDTO.getStatus());
				entity.setCreateBy(loginName);
				entity.setCreateDate(curDate);
				entity.setUpdateBy(loginName);
				entity.setUpdateDate(curDate);
				entity.setTallyCode(user.getId());
				// 初始化跨行标识、地域标识
				//  待确认 : 仅仅根据id 查下下面需要的字段 是否需要 	STATUS =95  and valid_sign =1 这2个条件的过滤  已确认以前就有
				// BtBankAccDTO payeeAcc = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
				List<String> bankAccIds = Lists.newArrayListWithCapacity(2);
				bankAccIds.add(entity.getPayeeAccId());
				bankAccIds.add(entity.getPayerAccId());
				List<BtBankAccDTO> btBankAccDTOS = this.btBankAccService.getBankAccByBankAccIds(bankAccIds);
				Map<String, BtBankAccDTO> idBtBankAccDTOMap = Maps.newHashMap();
				for (BtBankAccDTO btBankAccDTO : btBankAccDTOS) {
					idBtBankAccDTOMap.put(btBankAccDTO.getId(), btBankAccDTO);
				}
				BtBankAccDTO payeeAcc = idBtBankAccDTOMap.get(entity.getPayeeAccId());
				BtBankAccDTO payerAcc = idBtBankAccDTOMap.get(entity.getPayerAccId());
				if (!EmptyUtils.isEmpty(payeeAcc) && !EmptyUtils.isEmpty(payerAcc)) {
					if (EmptyUtils.isNotEmpty(payeeAcc.getBankTypeId()) && EmptyUtils.isNotEmpty(payerAcc.getBankTypeId())
							&& payeeAcc.getBankTypeId().equalsIgnoreCase(payerAcc.getBankTypeId())) {
						entity.setDifBank(NetbankConstant.DIFBANK_0);
					} else {
						entity.setDifBank(NetbankConstant.DIFBANK_1);
					}
					String payerProveAndCity = payerAcc.getProv() + payerAcc.getCity();
					String payeeProveAndCity = payeeAcc.getProv() + payeeAcc.getCity();
					if (payerProveAndCity.equalsIgnoreCase(payeeProveAndCity)) {
						entity.setAreaSign(NetbankConstant.AREASIGN_0);
					} else {
						entity.setAreaSign(NetbankConstant.AREASIGN_1);
					}
				}
				//资金池标识 0-非资金池 1-资金池 默认0
				entity.setPoolSign(AllocationConstant.POOL_SIGN_0);
				//下拨/请款根据付款单位判断是否是资金池主体单位
				//上划/上缴根据收款单位判断是否是资金池主体单位
				if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
						|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
					// BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayerAccId());
					BtBankAccDTO bankAcc = payerAcc;
					if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
						entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
					}
				} else if (AllocationConstant.ALLOCATION_TYPE_UP.intValue() == entity.getType().intValue()
						|| AllocationConstant.ALLOCATION_TYPE_TURN.intValue() == entity.getType().intValue()) {
					// BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
					BtBankAccDTO bankAcc = payeeAcc;
					if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
						entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
					}
				}
				// 金额小于等于0的不保存
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					entityList.add(entity);
					billNos.add(entity.getBillNo());
				}
				if (!EmptyUtils.isEmpty(map.get("budget")) && !EmptyUtils.isBlank(String.valueOf(map.get("budget")))) {
					String[] budgets = String.valueOf(map.get("budget")).split(",");
					if (!EmptyUtils.isEmptyArray(budgets)) {
						String[] itemCode = new String[budgets.length];
						String[] itemName = new String[budgets.length];
						BigDecimal[] processMoney = new BigDecimal[budgets.length];
						BigDecimal[] exchangeRate = new BigDecimal[budgets.length];
						BigDecimal[] processCurMoney = new BigDecimal[budgets.length];
						for (int i = 0; i < budgets.length; i++) {
							String[] budget = budgets[i].split(BudgetConstant.BUDGET_PROCESS_SPLIT);
							itemCode[i] = budget[0];
							itemName[i] = budget[1];
							processMoney[i] = new BigDecimal(budget[2]);
							exchangeRate[i] = new BigDecimal(budget[3]);
							processCurMoney[i] = new BigDecimal(budget[4]);
						}
						itemCodeList.add(itemCode);
						itemNameList.add(itemName);
						processMoneyList.add(processMoney);
						exchangeRateList.add(exchangeRate);
						processCurMoneyList.add(processCurMoney);
					}
				}
			}
			super.batchSave(entityList); //保存 资金调拨制单表
			for (int i = 0; i < entityList.size(); i++) {
				NisAllocationReq entity = entityList.get(i);
				// 保存用途
				entity.setPurpose(nisAllocationReqListDTO.getPurpose());
				Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
				// 启动流程
				if (AllocationConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
					messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
					String sign = this.getSign(entity.getType());
					getStartHandler().handler(sign, entity.getId(), entity, entity.getCorpId(), user.getId());
				}
				if (nisAllocationReqListDTO.getType().intValue() == AllocationConstant.ALLOCATION_TYPE_DOWN && !EmptyUtils.isEmptyList(itemCodeList)) {
					// 挂接资金计划
					this.writeBudgetProcess(entity, itemCodeList.get(i), itemNameList.get(i), processMoneyList.get(i), exchangeRateList.get(i), processCurMoneyList.get(i), user);
				}
				//生成日志
				nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
			}
		}
		return billNos.stream().collect(Collectors.joining(","));
	}

	/**
	 * 保存资金调拨单
	 *
	 * @param nisAllocationReqListDTO
	 */
	public String saveNisAllocationReq(NisAllocationReqListDTO nisAllocationReqListDTO, ICurrentUser user) {
		//校验时间戳，是否存在重复提交
		if (this.isExistTimeStamp(nisAllocationReqListDTO.getTimeStamp())) {
			throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
		}
		NisAllocationReq entity = new NisAllocationReq();
		BeanUtils.copyNotNullProperties(nisAllocationReqListDTO, entity);
		//判断是否挂证书
		if (!StringUtils.isEmpty(nisAllocationReqListDTO.getCaCoding())) {
			SysCaInfoDTO caDto = new SysCaInfoDTO();
			caDto.setRandom(nisAllocationReqListDTO.getRandom());
			caDto.setEncrySource(nisAllocationReqListDTO.getEncrySource());
			caDto.setEncryDate(DateUtils.getDate());
			caDto.setCaCoding(nisAllocationReqListDTO.getCaCoding());
			entity.setCaInfoId(caService.saveOrUpdateCaInfo(caDto));
		}
		// 付款账户和收款账户不能一致
		if (entity.getPayeeAcc().equalsIgnoreCase(entity.getPayerAcc())) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
		}
		String billNo = SequenceSingleton.getSequence(SequenceEnum.NIS_ALLOCATION_REQ); //生成单据号
		//外部单据Id
		if (StringUtils.isNotEmpty(nisAllocationReqListDTO.getErpId())) {
			entity.setErpId(nisAllocationReqListDTO.getErpId());
			entity.setCreateBy(null);
			entity.setCreateDate(null);
		} else {
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
		}
		entity.setBillNo(billNo); //设置 单据号
		//资金池标识 0-非资金池 1-资金池 默认0
		entity.setPoolSign(AllocationConstant.POOL_SIGN_0);
		//下拨/请款根据 付款单位 判断是否是资金池主体单位
		//上划/上缴根据 收款单位 判断是否是资金池主体单位
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayerAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		} else if (AllocationConstant.ALLOCATION_TYPE_UP.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_TURN.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		}
		this.save(entity);  //保存资金调拨单
		// 挂接资金计划
		this.writeBudgetProcess(entity, nisAllocationReqListDTO.getItemCode(), nisAllocationReqListDTO.getItemName(), nisAllocationReqListDTO.getProcessMoney(), nisAllocationReqListDTO.getExchangeRate(), nisAllocationReqListDTO.getProcessCurMoney(), user);
		// 保存用途信息
		if (!StringUtils.isBlank(nisAllocationReqListDTO.getPurpose())) {
			entity.setPurpose(nisAllocationReqListDTO.getPurpose());
		}
		Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;

		// 启动流程
		if (AllocationConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
			//获取工作流类型
			String sign = this.getSign(entity.getType()); //资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
			getStartHandler().handler(sign, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		// 生成日志
		nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		return entity.getBillNo();
	}

	public void saveNisAllocationReqPosForJob(NisAllocationReq entity,ICurrentUser user){
		this.save(entity);  //保存资金调拨单
		Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;

		// 启动流程
		if (AllocationConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
			//获取工作流类型
			String sign = this.getSign(entity.getType()); //资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
			getStartHandler().handler(sign, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		// 生成日志
		nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
	}

	/**
	 * 修改资金调拨单
	 *
	 * @param nisAllocationReq
	 */
	public void updateNisAllocationReq(NisAllocationReqListDTO nisAllocationReq, ICurrentUser user) {
		NisAllocationReqListDTO entity = this.getByUnion(nisAllocationReq.getId());
		//暂存和编辑状态才可以提交
		if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
		}
		// 付款账户和收款账户不能一致
		if (nisAllocationReq.getPayeeAcc().equalsIgnoreCase(nisAllocationReq.getPayerAcc())) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
		}
		nisAllocationReq.setErpId(entity.getErpId());
		nisAllocationReq.setUpdateBy(user.getLoginName());
		nisAllocationReq.setUpdateDate(DateUtils.getDate());
		//判断是否挂证书
		if (!EmptyUtils.isEmpty(nisAllocationReq.getCaCoding())) {
			nisAllocationReq.setCaInfoId(getCaInfoId(nisAllocationReq));
		}
		//资金池标识 0-非资金池 1-资金池 默认0
		nisAllocationReq.setPoolSign(AllocationConstant.POOL_SIGN_0);
		//下拨/请款根据付款单位判断是否是资金池主体单位
		//上划/上缴根据收款单位判断是否是资金池主体单位
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayerAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				nisAllocationReq.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		} else if (AllocationConstant.ALLOCATION_TYPE_UP.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_TURN.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				nisAllocationReq.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		}
		//编辑资金调拨单，控制原状态为0暂存/-1打回
		Integer[] oldStatus = {AllocationConstant.STATUE_TEMP, AllocationConstant.STATUE_BACK};
		this.updateNisAllocationReqStatus(nisAllocationReq, oldStatus);
		Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
		// 启动流程
		if (AllocationConstant.STATUE_WAIT_CHECK.equals(nisAllocationReq.getStatus())) {
			String sign = this.getSign(entity.getType());
			messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
			getStartHandler().handler(sign, nisAllocationReq.getId(), nisAllocationReq, nisAllocationReq.getCorpId(), user.getId());
		}
		// 生成日志
		nisMessageService.saveMessage(nisAllocationReq.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		//完成通知提醒
		String tag = getTag(nisAllocationReq.getType());
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, nisAllocationReq.getId(), user.getId());
		}
	}

	/**
	 * 修改请款/下拨资金调拨单
	 *
	 * @param nisAllocationReqListDTO
	 */
	public void updateAskAndDown(NisAllocationReqListDTO nisAllocationReqListDTO, ICurrentUser user) {
		NisAllocationReq entity = this.get(nisAllocationReqListDTO.getId(), true);
		//暂存和编辑状态才可以提交
		if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
		}
		nisAllocationReqListDTO.setUpdateDate(DateUtils.getDate());
		nisAllocationReqListDTO.setUpdateBy(user.getLoginName());
		//判断是否挂证书
		if (!EmptyUtils.isEmpty(nisAllocationReqListDTO.getCaCoding())) {
			nisAllocationReqListDTO.setCaInfoId(this.getCaInfoId(nisAllocationReqListDTO));
		}
		//资金池标识 0-非资金池 1-资金池 默认0
		nisAllocationReqListDTO.setPoolSign(AllocationConstant.POOL_SIGN_0);
		//下拨/请款根据付款单位判断是否是资金池主体单位
		//上划/上缴根据收款单位判断是否是资金池主体单位
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayerAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				nisAllocationReqListDTO.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		} else if (AllocationConstant.ALLOCATION_TYPE_UP.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_TURN.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				nisAllocationReqListDTO.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		}
		//编辑资金调拨单，控制原状态为0暂存/-1打回
		Integer[] oldStatus = {AllocationConstant.STATUE_TEMP, AllocationConstant.STATUE_BACK};
		int result = this.updateNisAllocationReqStatus(nisAllocationReqListDTO, oldStatus);
		// 挂接资金计划
//		entity.setMoney(nisAllocationReqListDTO.getMoney()); //注释此处，因为 Money的修改，在上面 this.updateNisAllocationReqStatus（）已处理。如果单独再 使用 entity进行set，会把前面的修改进行覆盖(Hibernate自动提交问题)。status本应该改为 11，又被改为 0；
		this.writeBudgetProcess(entity, nisAllocationReqListDTO.getItemCode(), nisAllocationReqListDTO.getItemName(), nisAllocationReqListDTO.getProcessMoney(), nisAllocationReqListDTO.getExchangeRate(), nisAllocationReqListDTO.getProcessCurMoney(), user);

		Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
		// 启动流程
		if (result > 0 && AllocationConstant.STATUE_WAIT_CHECK.equals(nisAllocationReqListDTO.getStatus())) {
			String sign = this.getSign(nisAllocationReqListDTO.getType());
			messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
			getStartHandler().handler(sign, nisAllocationReqListDTO.getId(), nisAllocationReqListDTO, nisAllocationReqListDTO.getCorpId(), user.getId());
		}
		// 生成日志
		nisMessageService.saveMessage(nisAllocationReqListDTO.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		//完成通知提醒
		String tag = this.getTag(nisAllocationReqListDTO.getType());
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, nisAllocationReqListDTO.getId(), user.getId());
		}
	}

	/**
	 * 审批修改金额
	 *
	 * @param nisAllocationReqListDTO
	 */
	public void approveUpdate(NisAllocationReqListDTO nisAllocationReqListDTO, ICurrentUser user) {
		NisAllocationReq entity = this.get(nisAllocationReqListDTO.getId(), true);
		entity.setMoney(nisAllocationReqListDTO.getMoney());
		entity.setUpdateDate(DateUtils.getDate());
		entity.setUpdateBy(user.getLoginName());

		//增加修改
		entity.setPayerAcc(nisAllocationReqListDTO.getPayerAcc());
		entity.setPayerAccName(nisAllocationReqListDTO.getPayerAccName());
		entity.setPayerAccId(nisAllocationReqListDTO.getPayerAccId());
		entity.setAreaSign(nisAllocationReqListDTO.getAreaSign());
		entity.setDifBank(nisAllocationReqListDTO.getDifBank());
		//判断是否挂证书
		if (!EmptyUtils.isEmpty(nisAllocationReqListDTO.getCaCoding())) {
			entity.setCaInfoId(this.getCaInfoId(nisAllocationReqListDTO));
		}
		//审批编辑资金调拨单，控制原状态为11待审批
		Integer[] oldStatus = {AllocationConstant.STATUE_WAIT_CHECK};
		BeanUtils.copyNotNullProperties(entity, nisAllocationReqListDTO);
		this.updateNisAllocationReqStatus(nisAllocationReqListDTO, oldStatus);
		// 挂接资金计划
		this.writeBudgetProcess(entity, nisAllocationReqListDTO.getItemCode(), nisAllocationReqListDTO.getItemName(), nisAllocationReqListDTO.getProcessMoney(), nisAllocationReqListDTO.getExchangeRate(), nisAllocationReqListDTO.getProcessCurMoney(), user);
		// 生成日志
		nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_APPROVE_UPDATE, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
	}

	/**
	 * 删除资金调拨单
	 *
	 * @param entity
	 */
	public void deleteNisAllocationReq(NisAllocationReq entity, ICurrentUser user) {
		// 逻辑删除
		NisAllocationReqListDTO vo = new NisAllocationReqListDTO();
		BeanUtils.copyNotNullProperties(entity, vo);
		vo.setStatus(AllocationConstant.STATUE_DEL);
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		// 删除费用报销单，控制0暂存/-1打回
		Integer[] oldStatus = {ExpenseConstant.STATUE_TEMP, ExpenseConstant.STATUE_BACK};
		this.updateNisAllocationReqStatus(vo, oldStatus);
		//增加如果是银行借款的数据，删除时同时删除掉账务
		if (entity.getBillFrom().equals(AllocationConstant.BILL_FROM_2)) {
			cmsProvideLoanService.invalidVoucher(entity.getErpId());
		}
		//如果是存入保证金和追加保证金的，同时删除保证金
		if (entity.getBillFrom().equals(AllocationConstant.BILL_FROM_3) || entity.getBillFrom().equals(AllocationConstant.BILL_FROM_4)) {
			IFcBondHookService.backBondMoney(entity.getBillFrom(), entity.getErpId(), entity.getMoney(), user);
		}
		// 挂接资金计划，且为请款/下拨则释放计划执行
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
			this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_ALLOCATION, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo());
		}
		//完成通知提醒
		String tag = this.getTag(entity.getType());
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, entity.getId(), user.getId());
		}
	}

	/**
	 * 获取资金调拨消息通知的常量类型
	 *
	 * @param type 资金调拨类型
	 * @return
	 */
	public String getTag(Integer type) {
		String tag = "";
		if (AllocationConstant.ALLOCATION_TYPE_UP.equals(type)) {
			tag = NotificationConstant.NETBANK.ALLOCATION_UP;
		} else if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(type)) {
			tag = NotificationConstant.NETBANK.ALLOCATION_DOWN;
		} else if (AllocationConstant.ALLOCATION_TYPE_TURN.equals(type)) {
			tag = NotificationConstant.NETBANK.ALLOCATION_TURN;
		} else if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(type)) {
			tag = NotificationConstant.NETBANK.ALLOCATION_ASK;
		} else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(type)) {
			tag = NotificationConstant.NETBANK.ALLOCATION_POS;
		}
		return tag;
	}

	/**
	 * 审批完成处理
	 * this.saveBisGatherCmd 收款
	 * this.saveBisExc 付款
	 *
	 * @param businessKey
	 * @param result
	 */
	public void approvePassEnd(String businessKey, boolean result, ICurrentUser user) {
		// 更新资金调拨
		NisAllocationReq nisAllocationReq = this.nisAllocationReqDao.get(businessKey, true);
		logger.error("nisAllocationReq value=========="+nisAllocationReq!=null ? "nisAllocationReq is not null" : "nisAllocationReq is null");
		Integer status = result ? AllocationConstant.STATUE_CHECK : AllocationConstant.STATUE_BACK;
		NisAllocationReqListDTO vo = new NisAllocationReqListDTO();
		BeanUtils.copyNotNullProperties(nisAllocationReq, vo);
		logger.error("vo value=========="+vo!=null && StringUtils.isNotEmpty(vo.getId())  ? vo.getId() : " vo is null");
		vo.setStatus(status);
		vo.setVoucherNo(nisAllocationReq.getId());
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());

		// 删除费用报销单，控制0暂存/-1打回
//		Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK, NetbankConstant.STATUE_WAIT_CHECK};
//		if(AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())){
			this.updateNisAllocationReqStatus(vo);
//		}else{
//			this.updateNisAllocationReqStatus(vo, oldStatus);
//		}
		String isAccount = "";
		//生成业务日志
		Integer messageType = result ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		nisMessageService.saveMessage(nisAllocationReq.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		BisExcDTO bisExc = null;
		if (AllocationConstant.STATUE_CHECK.equals(status)) {
			//资金调拨类型
			if (AllocationConstant.ALLOCATION_TYPE_UP.equals(nisAllocationReq.getType())
					|| AllocationConstant.ALLOCATION_TYPE_TURN.equals(nisAllocationReq.getType())) {
				//10-上划 和 14-上缴 生成 收款指令
				this.saveBisGatherCmd(vo, user);  //入口，使用定时任务进行发送 ;

			} else if (AllocationConstant.POOL_SIGN_1.equals(nisAllocationReq.getPoolSign())
					&& (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())
					|| AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType()))) {
				//资金池 11-下拨 12-请款 直接生成指令，无需判断记账
				bisExc = this.saveBisExc(vo, user);

			} else {
				//  非资金池 11-下拨 12-请款 13-头寸调拨 生成付款指令
				//判断资金调拨是否需要记账(头寸调拨单据来源是银行借款的话，是否记账是根据银行借款，借款管理是否记账参数来确定)
				if (AllocationConstant.BILL_FROM_2.equals(nisAllocationReq.getBillFrom())) {
					isAccount = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_TALLY).getValue();
				} else {
					isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
				}
				//头寸调拨是否记账
				String isAccountPos = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
				if ((CommonConstant.NO.equals(isAccount) && !AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType()))
						|| (CommonConstant.NO.equals(isAccountPos) && AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType()))) {
					//13=头寸调拨
					bisExc = this.saveBisExc(vo, user);
				} else {
					// 判断是否自动记账
					logger.error("自动记账入口");
					String isAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();
					if (CommonConstant.YES.equalsIgnoreCase(isAutoAccount)) {
						bisExc = this.saveBisExc(vo, user);
					} else {
						// 手工记账
						String payAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_PAYACCOUNT).getValue();
						if (CommonConstant.YES.equalsIgnoreCase(payAccount)) {
							// 先付款后记账
							bisExc = this.saveBisExc(vo, user);
						} else {
							CorpDTO corp = this.corpService.get(nisAllocationReq.getCorpId());
							// 先记账后付款，成员单位头寸调拨直接生成付款指令
							if (!EmptyUtils.isEmpty(corp) && CorpConstant.CORP_TYPE_CODE_CORP.equals(corp.getType()) &&
									AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
								bisExc = this.saveBisExc(vo, user);
							}
						}
					}
				}
			}
		}



		//如果是自动指令 调用记账
		String bisExcAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BISEXC_AUTOACCOUNT).getValue();
		if (CommonConstant.YES.equals(isAccount) && CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount) && Objects.nonNull(bisExc)) {
			allocationBisExcBackService.sendBisExcAccounting(bisExc, user);  // TODO 最里面有调用核算；程序是怎么识别使用哪个实现类的？
		}

		//更新计划执行数据
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == nisAllocationReq.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == nisAllocationReq.getType().intValue()) {
			fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_ALLOCATION, BudgetConstant.PROCESS_SYSNAME_NIS, nisAllocationReq.getBillNo(), status, user);
		}

		//付款明细勾对消息提醒
//		paymentBlendingService.sendNotification();
	}

	/**
	 * 付款指令生成
	 *
	 * @param nisAllocationReq
	 */
	public BisExcDTO saveBisExc(NisAllocationReqListDTO nisAllocationReq, ICurrentUser user) {
		//获取付款账号
		BtBankAccDTO payerAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayerAccId());
		if (payerAcc == null) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, nisAllocationReq.getPayerAcc());
		}
		logger.error("自动记账新增指令方法！");
		//获取收款账号
		BtBankAccDTO payeeAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayeeAccId());
		if (payeeAcc == null) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, nisAllocationReq.getPayeeAcc());
		}
		if (EmptyUtils.isEmpty(payerAcc.getBifCode())) {
			throw new ParameterException(com.bytter.biz.bdlink.api.enums.NetbankMessageEnum.NIS_WAGE_MESSAGE_BIFCODE_NULL, payerAcc.getBankAcc());
		}
		//获取接口业务规则
		BisBifInitDTO bifCode = bisBifInitService.getObjectById(payerAcc.getBifCode());
		BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(payerAcc.getBifCode());
		if (EmptyUtils.isEmpty(bisBifRule) || StringUtils.isEmpty(bisBifRule.getId())) {
			throw new ParameterException(com.bytter.biz.bdlink.api.enums.NetbankMessageEnum.NIS_WAGE_MESSAGE_BISBIFRULE_NULL, bifCode.getBifCode(), bifCode.getName());
		}
		//生成付款指令
		BisExcDTO bisExc = new BisExcDTO();
		bisExc.setCbsBillId(nisAllocationReq.getId());
		bisExc.setCbsBillNo(nisAllocationReq.getBillNo());
		bisExc.setCorpId(nisAllocationReq.getCorpId());
		bisExc.setReqDate(DateUtils.getDate());
		//付款方信息
		bisExc.setPayerAcc(nisAllocationReq.getPayerAcc());
		bisExc.setPayerAccName(nisAllocationReq.getPayerAccName());
		bisExc.setPayerBank(payerAcc.getBankName());
		bisExc.setPayerProv(payerAcc.getProv());
		bisExc.setPayerCity(payerAcc.getCity());
		bisExc.setCurId(nisAllocationReq.getCurId());
		bisExc.setPayMoney(nisAllocationReq.getMoney());
		bisExc.setBifCode(payerAcc.getBifCode());
		//收款方信息
		bisExc.setPayeeAcc(nisAllocationReq.getPayeeAcc());
		bisExc.setPayeeAccName(nisAllocationReq.getPayeeAccName());
		bisExc.setPayeeBank(payeeAcc.getBankName());
		bisExc.setPayeeProv(payeeAcc.getProv());
		bisExc.setPayeeCity(payeeAcc.getCity());
		bisExc.setPayeeBankCode(payeeAcc.getBankCode());
		bisExc.setWishPayday(nisAllocationReq.getWishPayday());
		//交易模式 3-对公 4-对私 默认4
		bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
		bisExc.setIsQuick(nisAllocationReq.getIsQuick());
		bisExc.setDifBank(nisAllocationReq.getDifBank());
		bisExc.setAreaSign(nisAllocationReq.getAreaSign());
		bisExc.setPurpose(nisAllocationReq.getPurpose());
		bisExc.setAddWord(nisAllocationReq.getPurpose());
		// 根据调拨类型设置结算方式
		FcVoucherTypeDTO voucherType = null;
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())) {
			bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN);
			bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_33);
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
		} else if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())) {
			bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_ASK);
			bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_32);
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
		} else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_POS);
			bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_34);
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_37);
		}
		if (voucherType != null) {
			bisExc.setVoucherTypeId(voucherType.getId());
		}
		//结算方式为空
		if (StringUtils.isBlank(bisExc.getVoucherTypeId())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYTYPE_NULL);
		}
		bisExc.setVoucherStat(BdlinkConstant.WAIT_FILL);
		// 判断资金调拨-下拨/请款   是否自动生成指令
		String bisExcAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BISEXC_AUTOACCOUNT).getValue();
		// todo:数据转换
//		com.bytter.biz.bdlink.api.dto.BisExcDTO bisExcDTO = BeanUtils.copyNewInstanceProperties(bisExc, com.bytter.biz.bdlink.api.dto.BisExcDTO.class);
		if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount)) {
			bisExcServiceImpl.autoSendBisExcNoAccounting(bisExc, user); //不记账?
//			bisExcServiceImpl.autoSendBisExc(bisExc, user); //这是原有逻辑调用
			nisAllocationReq.setVoucherStat(BdlinkConstant.PRE_SEND);
		} else {
			bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
			bisExcServiceImpl.addBisExc(bisExc, user);
			nisAllocationReq.setVoucherStat(BdlinkConstant.WAIT_FILL);
		}
		return bisExc;
	}

	/**
	 * 收款指令生成
	 *
	 * @param nisAllocationReq
	 */
	public void saveBisGatherCmd(NisAllocationReqListDTO nisAllocationReq, ICurrentUser user) {
		//获取付款账号
		BtBankAccDTO payerAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayerAccId());
		if (payerAcc == null) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, nisAllocationReq.getPayerAcc());
		}
		BisGatherCmdDTO gatherCmd = new BisGatherCmdDTO();
		gatherCmd.setCbsBillId(nisAllocationReq.getId());
		gatherCmd.setCbsBillNo(nisAllocationReq.getBillNo());
		gatherCmd.setBifCode(payerAcc.getBifCode());
		//付款账号
		gatherCmd.setChildAccId(nisAllocationReq.getPayerAccId());
		gatherCmd.setChildAcc(nisAllocationReq.getPayerAcc());
		gatherCmd.setChildAccName(nisAllocationReq.getPayerAccName());
		gatherCmd.setChildBank(nisAllocationReq.getPayerBankName());
		//收款账号
		gatherCmd.setParentAccId(nisAllocationReq.getPayeeAccId());
		gatherCmd.setParentAcc(nisAllocationReq.getPayeeAcc());
		gatherCmd.setParentAccName(nisAllocationReq.getPayeeAccName());
		gatherCmd.setParentBank(nisAllocationReq.getPayeeBankName());
		gatherCmd.setGatherAmt(nisAllocationReq.getMoney());
		gatherCmd.setCurId(nisAllocationReq.getCurId());
		gatherCmd.setIsQuick(nisAllocationReq.getIsQuick());
		gatherCmd.setDifBank(nisAllocationReq.getDifBank());
		gatherCmd.setAreaSign(nisAllocationReq.getAreaSign());
		gatherCmd.setRunTime(DateUtils.getDate());
		gatherCmd.setTransTime(nisAllocationReq.getWishPayday());
		if (AllocationConstant.ALLOCATION_TYPE_UP.equals(nisAllocationReq.getType())) {
			gatherCmd.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_UP);
			gatherCmd.setBusOriType(BdlinkConstant.BUSORITYPE_ALLOCATION_UP);
		} else if (AllocationConstant.ALLOCATION_TYPE_TURN.equals(nisAllocationReq.getType())) {
			gatherCmd.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_TURN);
			gatherCmd.setBusOriType(BdlinkConstant.BUSORITYPE_ALLOCATION_TURN);
		}
		gatherCmd.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
		gatherCmd.setCmdStat(BdlinkConstant.WAIT_FILL);
		gatherCmd.setPurpose(nisAllocationReq.getPurpose());
		gatherCmd.setAddWord(nisAllocationReq.getPurpose());
		gatherCmd.setCorpId(nisAllocationReq.getCorpId());
		// 判断资金调拨-下拨/请款   是否自动生成指令             -- 不是上划上缴吗？判断 下拨、请款？？？
		String bisExcAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BISEXC_AUTOACCOUNT).getValue();
		// todo:数据转换
		BisGatherCmdDTO gatherCmdDTO = BeanUtils.copyNewInstanceProperties(gatherCmd, BisGatherCmdDTO.class);
		if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount)) {
			bisGatherCmdService.autoSendBisGatherCmd(gatherCmdDTO, user);

			nisAllocationReq.setVoucherStat(BdlinkConstant.PRE_SEND);
		} else {
			gatherCmdDTO.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
			bisGatherCmdService.addBisGatherCmd(gatherCmdDTO, user);
			nisAllocationReq.setVoucherStat(BdlinkConstant.WAIT_FILL);
		}
	}


	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}

	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		this.approvePassEnd(workFlowResult.getBusinessKey(), workFlowResult.isPass(), workFlowResult.getUser());
	}

	/**
	 * 获取资金调拨审批页面列表信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput<WorkFlowPersistence> getDataTablesApprove(DataTablesInput dataTablesInput) {
		String sign = "";
		String eqiType = "eqi_type";
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get(eqiType))) {
			String type = String.valueOf(dataTablesInput.getFormdata().get(eqiType));
			if (EmptyUtils.isNumeric(type)) {
				sign = this.getSign(new Integer(type));
			}
		}
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
		String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "ar.id", true);
		DataTablesOutput<NisAllocationReqListDTO> dp = this.nisAllocationReqDao.getDataTablesApprove(dataTablesInput, businessSql);
		List<NisAllocationReqListDTO> nisAllocationReqListDTOList = dp.getData();
		for (NisAllocationReqListDTO nisAllocationReqListDTO : nisAllocationReqListDTOList) {
			this.setBankName(nisAllocationReqListDTO);
			this.setCreateByName(nisAllocationReqListDTO);
			this.setCurName(nisAllocationReqListDTO);
			this.setCorpName(nisAllocationReqListDTO);
		}
		return workFlowBusiness.merge(dp);
	}

	/**
	 * 根据资金调拨类型获取工作流类型
	 *
	 * @param type
	 * @return
	 */
	private String getSign(Integer type) {
		String sign = "";
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == type.intValue()) {
			sign = NisWorkFlowConstant.NIS_ALLOCATION_DOWN;
		} else if (AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == type.intValue()) {
			sign = NisWorkFlowConstant.NIS_ALLOCATION_ASK;
		} else if (AllocationConstant.ALLOCATION_TYPE_POS.intValue() == type.intValue()) {
			sign = NisWorkFlowConstant.NIS_ALLOCATION_POS;
		} else if (AllocationConstant.ALLOCATION_TYPE_UP.intValue() == type.intValue()) {
			sign = NisWorkFlowConstant.NIS_ALLOCATION_UP;
		} else if (AllocationConstant.ALLOCATION_TYPE_TURN.intValue() == type.intValue()) {
			sign = NisWorkFlowConstant.NIS_ALLOCATION_TURN;
		}
		return sign;
	}

	/**
	 * 占用预算
	 *
	 * @param entity   资金调拨
	 * @param itemCode 预算信息
	 */
	public void writeBudgetProcess(NisAllocationReq entity, String[] itemCode, String[] itemName, BigDecimal[] processMoney, BigDecimal[] exchangeRate, BigDecimal[] processCurMoney, ICurrentUser user) {
		if (!EmptyUtils.isEmptyArray(itemCode)) {
			List<FbsBudgetProcessDTO> list = new ArrayList();
			for (int i = 0; i < itemCode.length; i++) {
				FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				fbsBudgetProcessDTO.setCreateBy(entity.getCreateBy());
				fbsBudgetProcessDTO.setCreateDate(entity.getUpdateDate());
				fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
				fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
				fbsBudgetProcessDTO.setStatus(entity.getStatus());
				fbsBudgetProcessDTO.setBillNo(entity.getBillNo());
				fbsBudgetProcessDTO.setBillId(entity.getId());
				fbsBudgetProcessDTO.setItemCode(itemCode[i]);
				fbsBudgetProcessDTO.setItemName(itemName[i]);
				fbsBudgetProcessDTO.setProcessMoney(processMoney[i]);
				fbsBudgetProcessDTO.setExchangeRate(exchangeRate[i]);
				fbsBudgetProcessDTO.setProcessCurMoney(processCurMoney[i]);
				fbsBudgetProcessDTO.setProcessDate(entity.getWishPayday());
				fbsBudgetProcessDTO.setCorpId(entity.getCorpId());
				fbsBudgetProcessDTO.setCurId(entity.getCurId());
				fbsBudgetProcessDTO.setPayerAcc(entity.getPayerAcc());
				fbsBudgetProcessDTO.setPayeeAcc(entity.getPayeeAcc());
				fbsBudgetProcessDTO.setPayeeAccName(entity.getPayeeAccName());
				BtBankAccDTO btBankDTO = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
				if (!EmptyUtils.isEmpty(btBankDTO)) {
					fbsBudgetProcessDTO.setPayeeBank(btBankDTO.getBankName());
				}
				fbsBudgetProcessDTO.setPurpose(entity.getPurpose());
				fbsBudgetProcessDTO.setWishPayday(entity.getWishPayday());
				fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_ALLOCATION);
				fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
				fbsBudgetProcessDTO.setInnerSign(BudgetConstant.INNER_SIGN_1);
				list.add(fbsBudgetProcessDTO);
			}
			// 修改时，先删除之前的计划执行数据再保存
			this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_ALLOCATION, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo());
			this.fbsBudgetProcessService.writeBudgetProcess(list, user);
		}
	}

	/**
	 * 自动记账
	 *
	 * @param nisAllocationReq
	 */
	public void submitVoucher(NisAllocationReq nisAllocationReq, ICurrentUser user) {
		CorpDTO corp = this.corpService.get(nisAllocationReq.getCorpId());
		logger.error("进入submitVoucher");
		// 单位头寸调拨，不用记账
		if (!EmptyUtils.isEmpty(corp) && CorpConstant.CORP_TYPE_CODE_CORP.equals(corp.getType()) &&
				AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			return;
		}
		NisAccbookVO accBook = new NisAccbookVO();
		accBook.setBillId(nisAllocationReq.getId());
		accBook.setBillNo(nisAllocationReq.getBillNo());
		//获取收款方
		BtBankAccDTO payeeAcc = btBankAccService.getBankAccByIdAndCurId(nisAllocationReq.getPayeeAccId(), nisAllocationReq.getCurId());
		//下拨、请款，收款账户对应的内部账户以制单界面为准
		if (StringUtils.isNotBlank(nisAllocationReq.getPayeeCounterAcc())) {
			payeeAcc.setCounterAccId(nisAllocationReq.getPayeeCounterAcc());
		}
		//获取收款单位
		CorpDTO payeeAccCorp = corpService.get(payeeAcc.getCorpId());
		//获取付款方
		BtBankAccDTO payerAcc = btBankAccService.getBankAccByIdAndCurId(nisAllocationReq.getPayerAccId(), nisAllocationReq.getCurId());
		//获取付款单位
		CorpDTO payerAccCorp = corpService.get(payerAcc.getCorpId());
		FcAccbookExtDTO bookExt = new FcAccbookExtDTO();
		bookExt.setExternalBank(payeeAcc.getBankName());
		bookExt.setExternalCorp(payeeAccCorp.getName());
		//取银行账号对应的内部帐号（下拨、请款，收款账户对应的内部账户以制单界面为准）
		String blnctrAcc = nisAllocationReq.getPayeeCounterAcc();
		if (StringUtils.isBlank(blnctrAcc)) {
			blnctrAcc = btBankAccService.getBlnctrAcc(payeeAcc.getBankAcc(), payeeAcc.getCurId());
		}
		bookExt.setExternalAcc(blnctrAcc);
		//判断收款方是否是网点
		if (!EmptyUtils.isEmpty(payeeAccCorp.getType()) && payeeAccCorp.getType() == 0
				&& !AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			String payeeSuperNetId = payeeAccCorp.getParentId();
			//获取父账号
			if (StringUtils.isNotBlank(payeeSuperNetId) && payeeSuperNetId.equals(payerAccCorp.getId())) {
				//获取收款方对应的上级内部账号
				accBook.setFcAcc(innerAccountService.getByBlnctrAcc(payeeAcc.getSuperCounterAcc()));
				bookExt.setStartBank(payeeAcc.getBankName());
				bookExt.setStartCorp(payeeAcc.getCorpName());
				// 第三方账号,子账号银行活期
				bookExt.setStartCorpAcc(blnctrAcc);
			}
		} else {
			//获取收款方对应的内部账号
			accBook.setFcAcc(innerAccountService.getByBlnctrAcc(payeeAcc.getCounterAccId()));
		}
		accBook.setBookExt(bookExt);
		accBook.setBankAcc(payerAcc);
		accBook.setPayeeAcc(payeeAcc.getBankAcc());
		accBook.setPayMoney(nisAllocationReq.getMoney());
		accBook.setWishPayday(nisAllocationReq.getWishPayday());
		accBook.setIsQuick(nisAllocationReq.getIsQuick());
		accBook.setPurpose(nisAllocationReq.getPurpose());
		//资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
		FcVoucherTypeDTO voucherType = null;
		if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())) {
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
			accBook.setBusOriType(FinaBookConstant.BusOriType.NB_UNIT_ASK_PAYMENT.get());
			accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_UNIT_ASK_PAYMENT.get());
		} else if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())) {
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
			accBook.setBusOriType(FinaBookConstant.BusOriType.NB_CNT_DOWN_PAYMENT.get());
			accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_DOWN_PAYMENT.get());
		} else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_37);
			accBook.setBusOriType(FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get());
			accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_POS_PAYMENT.get());
		}
		if (voucherType != null) {
			accBook.setVoucherTypeId(voucherType.getId());
		}
		//结算方式为空
		if (StringUtils.isBlank(accBook.getVoucherTypeId())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYTYPE_NULL);
		}
		//未维护企业活期账号
		if (EmptyUtils.isEmpty(accBook.getFcAcc())) {
			if (FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get().equals(accBook.getBusOriType())) {
				throw new ParameterException(NetbankMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, payeeAcc.getBankAcc());
			}
		}
		//判断是否要记账，记账则自动生成账务
		logger.error("进入nisAccbookService.submitVoucher方法前");
		String voucherNo = nisAccbookService.submitVoucher(accBook, user); // 里面有调用核算
		nisAllocationReq.setVoucherNo(voucherNo);
		nisAllocationReq.setStatus(AllocationConstant.STATUE_TALLY_CHECKPASS);
		//更新记账标识
		nisAllocationReq.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
		this.saveOrUpdate(nisAllocationReq); // 返回的 voucherNo 为了更新到 资金调拨制单表 NIS_ALLOCATION_REQ
	}


	/**
	 * 自动记账前置校验
	 *
	 * @param nisAllocationReq
	 */
	public void checkSubmitVoucher(NisAllocationReq nisAllocationReq, ICurrentUser user) {
		CorpDTO corp = this.corpService.get(nisAllocationReq.getCorpId());
		logger.error("进入checkSubmitVoucher");
		// 单位头寸调拨，不用记账
		if (!EmptyUtils.isEmpty(corp) && CorpConstant.CORP_TYPE_CODE_CORP.equals(corp.getType()) &&
				AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			return;
		}
		NisAccbookVO accBook = new NisAccbookVO();
		accBook.setBillId(nisAllocationReq.getId());
		accBook.setBillNo(nisAllocationReq.getBillNo());
		//获取收款方
		BtBankAccDTO payeeAcc = btBankAccService.getBankAccByIdAndCurId(nisAllocationReq.getPayeeAccId(), nisAllocationReq.getCurId());
		//下拨、请款，收款账户对应的内部账户以制单界面为准
		if (StringUtils.isNotBlank(nisAllocationReq.getPayeeCounterAcc())) {
			payeeAcc.setCounterAccId(nisAllocationReq.getPayeeCounterAcc());
		}
		//获取收款单位
		CorpDTO payeeAccCorp = corpService.get(payeeAcc.getCorpId());
		//获取付款方
		BtBankAccDTO payerAcc = btBankAccService.getBankAccByIdAndCurId(nisAllocationReq.getPayerAccId(), nisAllocationReq.getCurId());
		//获取付款单位
		CorpDTO payerAccCorp = corpService.get(payerAcc.getCorpId());
		FcAccbookExtDTO bookExt = new FcAccbookExtDTO();
		bookExt.setExternalBank(payeeAcc.getBankName());
		bookExt.setExternalCorp(payeeAccCorp.getName());
		//取银行账号对应的内部帐号（下拨、请款，收款账户对应的内部账户以制单界面为准）
		String blnctrAcc = nisAllocationReq.getPayeeCounterAcc();
		if (StringUtils.isBlank(blnctrAcc)) {
			blnctrAcc = btBankAccService.getBlnctrAcc(payeeAcc.getBankAcc(), payeeAcc.getCurId());
		}
		bookExt.setExternalAcc(blnctrAcc);
		//判断收款方是否是网点
		if (!EmptyUtils.isEmpty(payeeAccCorp.getType()) && payeeAccCorp.getType() == 0
				&& !AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			String payeeSuperNetId = payeeAccCorp.getParentId();
			//获取父账号
			if (StringUtils.isNotBlank(payeeSuperNetId) && payeeSuperNetId.equals(payerAccCorp.getId())) {
				//获取收款方对应的上级内部账号
				accBook.setFcAcc(innerAccountService.getByBlnctrAcc(payeeAcc.getSuperCounterAcc()));
				bookExt.setStartBank(payeeAcc.getBankName());
				bookExt.setStartCorp(payeeAcc.getCorpName());
				// 第三方账号,子账号银行活期
				bookExt.setStartCorpAcc(blnctrAcc);
			}
		} else {
			//获取收款方对应的内部账号
			accBook.setFcAcc(innerAccountService.getByBlnctrAcc(payeeAcc.getCounterAccId()));
		}
		accBook.setBookExt(bookExt);
		accBook.setBankAcc(payerAcc);
		accBook.setPayeeAcc(payeeAcc.getBankAcc());
		accBook.setPayMoney(nisAllocationReq.getMoney());
		accBook.setWishPayday(nisAllocationReq.getWishPayday());
		accBook.setIsQuick(nisAllocationReq.getIsQuick());
		accBook.setPurpose(nisAllocationReq.getPurpose());
		//资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
		FcVoucherTypeDTO voucherType = null;
		if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())) {
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
			accBook.setBusOriType(FinaBookConstant.BusOriType.NB_UNIT_ASK_PAYMENT.get());
			accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_UNIT_ASK_PAYMENT.get());
		} else if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())) {
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
			accBook.setBusOriType(FinaBookConstant.BusOriType.NB_CNT_DOWN_PAYMENT.get());
			accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_DOWN_PAYMENT.get());
		} else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_37);
			accBook.setBusOriType(FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get());
			accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_POS_PAYMENT.get());
		}
		if (voucherType != null) {
			accBook.setVoucherTypeId(voucherType.getId());
		}
		if (!AllocationConstant.ALLOCATION_TYPE_UP.equals(nisAllocationReq.getType())
				&& !AllocationConstant.ALLOCATION_TYPE_TURN.equals(nisAllocationReq.getType())) {//上划,上缴不需要判断结算方式
			//结算方式为空
			if (StringUtils.isBlank(accBook.getVoucherTypeId())) {
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_PAY_MESSAGE_PAYTYPE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
			}
		}
		//未维护企业活期账号
		if (EmptyUtils.isEmpty(accBook.getFcAcc())) {
			if (FinaBookConstant.BusOriType.NB_CNT_POS_PAYMENT.get().equals(accBook.getBusOriType())) {
				String[] params = {StringUtils.isEmpty(payerAcc.getAccName()) ? "" :payerAcc.getAccName()};
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_ACCBOOK_1020_NOTEXISTS, WorkFlowConstant.VERITY_STATUS_CONTROLLER,params);
			}
		}
		//判断是否要记账，记账则自动生成账务
		logger.error("进入nisAccbookService.checkSubmitVoucher方法前");
		String voucherNo = nisAccbookService.checkSubmitVoucher(accBook, user); // 里面有调用核算
	}
	/**
	 * 批量提交
	 *
	 * @param ids
	 */
	public void batchSubmit(String ids, String randoms, String sourceDatas, String caCodings, ICurrentUser user) {
		String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
		String[] randomsStr = randoms.split(CommonConstant.COMMA_SPLIT);
		String[] sourceDatasStr = sourceDatas.split(CommonConstant.COMMA_SPLIT);
		String[] caCodingsStr = caCodings.split(CommonConstant.COMMA_SPLIT);
		for (int i = 0; i < idStr.length; i++) {
			String id = idStr[i];
			NisAllocationReq entity = this.get(id, true);
			//暂存和编辑状态才可以提交
			if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
			}
			if (entity.getPayerAccId() == null) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NULL, entity.getBillNo());
			}
			NisAllocationReqListDTO vo = new NisAllocationReqListDTO();
			BeanUtils.copyNotNullProperties(entity, vo);
			vo.setStatus(AllocationConstant.STATUE_WAIT_CHECK);
			if (!EmptyUtils.isEmpty(caCodings)) {
				SysCaInfoDTO caDto = new SysCaInfoDTO();
				caDto.setRandom(randomsStr[i]);
				caDto.setEncryDate(DateUtils.getDate());
				caDto.setEncrySource(sourceDatasStr[i]);
				caDto.setCaCoding(caCodingsStr[i]);
				if (!StringUtils.isEmpty(entity.getCaInfoId())) {
					caDto.setId(entity.getCaInfoId());
				}
				caService.saveOrUpdateCaInfo(caDto);
				vo.setCaInfoId(caDto.getId());
			}
			Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
			this.updateNisAllocationReqStatus(vo, oldStatus);
			Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
			// 启动流程
			if (AllocationConstant.STATUE_WAIT_CHECK.equals(vo.getStatus())) {
				String sign = this.getSign(vo.getType());
				messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
				getStartHandler().handler(sign, vo.getId(), vo, vo.getCorpId(), user.getId());
			}
			// 生成日志
			nisMessageService.saveMessage(vo.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		}
	}

	/**
	 * 根据外部编号查找资金调拨单据
	 *
	 * @param erpId
	 * @return
	 */
	public NisAllocationReqListDTO getNisAllocationReqByErpId(String erpId) {
		NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqDao.getNisAllocationReqByErpId(erpId);
		if (ObjectUtils.isNotEmpty(nisAllocationReqListDTO)) {
			this.setBankName(nisAllocationReqListDTO);
			this.setCreateByName(nisAllocationReqListDTO);
			this.setCurName(nisAllocationReqListDTO);
			this.setCorpName(nisAllocationReqListDTO);
		}
		return nisAllocationReqListDTO;
	}

	/**
	 * 手工记账查询
	 *
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesAccount(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		Integer type = Integer.parseInt(String.valueOf(dataTablesInput.getFormdata().get("menuType")));
		Object opType = dataTablesInput.getFormdata().get("opType");
		sql.append(" and ar.type = ").append(type);
		sql.append(" and ar.status = ").append(AllocationConstant.STATUE_CHECK);
		if ("Init".equals(opType)) {
			if (AllocationConstant.ALLOCATION_TYPE_POS.equals(type)) {
				//头寸调拨校验是否记账
				String isAccountPos = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
				if (CommonConstant.NO.equals(isAccountPos)) {
					sql.append(" and 1<>1");
				}
			} else if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(type) || AllocationConstant.ALLOCATION_TYPE_ASK.equals(type)) {
				//是否记账
				String isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
				if (CommonConstant.NO.equals(isAccount)) {
					sql.append(" and 1<>1");
				}
			}
			sql.append(" and (ar.tally_Sign ='' or ar.tally_Sign in('0','6') or  ar.tally_sign is null) ");
		} else if ("Check".equals(opType)) {
			sql.append(" and (ar.tally_Sign = '3' ) and ar.tally_code != '").append(user.getId()).append("'");
		} else if ("Edit".equals(opType)) {
			sql.append(" and (ar.tally_Sign in(5,4)) and ar.tally_code = '").append(user.getId()).append("'");
		}
		// 判断是否自动记账
		String isAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();
		DataTablesOutput<NisAllocationReqListDTO> output = null;
		if (CommonConstant.NO.equalsIgnoreCase(isAutoAccount)) {
			// 手工记账 是否先付款后记账
			String payAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_PAYACCOUNT).getValue();
			//新增状态字段
			if (CommonConstant.YES.equalsIgnoreCase(payAccount)) {//先付款后记账
				sql.append(" and ar.voucher_Stat = " + BdlinkConstant.SUCCESS);
			}
			// 记账时，头寸调拨过滤成员单位数据
			if (AllocationConstant.ALLOCATION_TYPE_POS.intValue() == type.intValue()) {
//				sql.append(" and exists (select 1 from sys_corp c where c.id = ar.corp_id and c.type = 0) ");
				List<String> corpIds = corpService.getCorpIdsByType("0");
				String corpIdSql = SqlUtil.getInSql(corpIds, "ar.corp_id", true);
				sql.append(" and " + corpIdSql);
			}

			//做权限过滤
			List<String> corpAuth = null;
			if (FinacialManualConstant.MANUAL_STATUS_CHECK.equals(opType)) {
				corpAuth = corpService.getUserCorpIds(ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_CHECK, user.getId());
			} else {
				corpAuth = corpService.getUserCorpIds(ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, user.getId());
			}

			if (CommonConstant.YES.equalsIgnoreCase(payAccount)) {// 判断是否手工记账 是否先付款后记账
				String bisExcVoucherFroms = "";
				if (AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == type.intValue()) {
					bisExcVoucherFroms = BdlinkConstant.FROM_ALLOCATION_TYPE_ASK;
				} else if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == type.intValue()) {
					bisExcVoucherFroms = BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN;
				} else if (AllocationConstant.ALLOCATION_TYPE_POS.intValue() == type.intValue()) {
					bisExcVoucherFroms = BdlinkConstant.FROM_ALLOCATION_TYPE_POS;
				}
				DataTablesOutput dataTablesOutput = nisAllocationReqDao.getDataTablesAccountAndBisExc(dataTablesInput, sql.toString(), corpAuth);
				List<String> billIds = new ArrayList<>();
				List<NisAllocationReqListDTO> nisAllocationReqListDTOList = dataTablesOutput.getData();
				for (NisAllocationReqListDTO nisAllocationReqListDTO : nisAllocationReqListDTOList) {
					this.setBankName(nisAllocationReqListDTO);
					this.setCreateByName(nisAllocationReqListDTO);
					this.setCurName(nisAllocationReqListDTO);
					this.setCorpName(nisAllocationReqListDTO);
					billIds.add(nisAllocationReqListDTO.getId());
				}
				this.setBisExcValue(billIds, nisAllocationReqListDTOList, bisExcVoucherFroms);
				return dataTablesOutput;
			} else {
				DataTablesOutput dataTablesOutput = nisAllocationReqDao.getDataTablesAccount(dataTablesInput, sql.toString(), corpAuth);
				List<NisAllocationReqListDTO> nisAllocationReqListDTOList = dataTablesOutput.getData();
				for (NisAllocationReqListDTO nisAllocationReqListDTO : nisAllocationReqListDTOList) {
					this.setBankName(nisAllocationReqListDTO);
					this.setCreateByName(nisAllocationReqListDTO);
					this.setCurName(nisAllocationReqListDTO);
					this.setCorpName(nisAllocationReqListDTO);
				}
				return dataTablesOutput;
			}
		}
		return new DataTablesOutput<NisAllocationReqListDTO>(dataTablesInput.getDraw(), new ArrayList(0), 0, 0);
	}

	/**
	 * 根据资源获取条件
	 *
	 * @param resource
	 * @return
	 */
	private String getAccountAuthSql(String resource, ICurrentUser user) {
		List<String> accountList = accountGrantService.listByMultiValAccIds(user.getId(), resource, null, AccountGrantConstant.ONLINE_BANK_TYPE, "");
		StringBuilder accountAuthSql = new StringBuilder();
		if (!EmptyUtils.isEmptyList(accountList)) {
			accountAuthSql.append("( ar.payer_acc_id in ( ");
			for (int i = 0; i < accountList.size(); i++) {
				if (i != 0) {
					accountAuthSql.append(CommonConstant.COMMA_SPLIT);
				}
				accountAuthSql.append("'").append(accountList.get(i)).append("'");
			}
			accountAuthSql.append(" ) ");
			accountAuthSql.append(" or (ar.bill_from = " + AllocationConstant.BILL_FROM_2 + " and ar.payer_acc_id is null)) ");
		}
		return accountAuthSql.toString();
	}

	/**
	 * 根据单号查找资金调拨信息
	 * zuoy 2018-03-08
	 *
	 * @param billNo
	 * @return NisAllocationReqVO
	 */
	public NisAllocationReqListDTO getNisAllocationReqByBillNo(String billNo) {
		NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqDao.getNisAllocationReqByBillNo(billNo);
		if(nisAllocationReqListDTO == null){
			return null;
		}
		this.setBankName(nisAllocationReqListDTO);
		this.setCreateByName(nisAllocationReqListDTO);
		this.setCurName(nisAllocationReqListDTO);
		this.setCorpName(nisAllocationReqListDTO);
		this.setAccBalance(nisAllocationReqListDTO);
		return nisAllocationReqListDTO;
	}

	private void setAccBalance(NisAllocationReqListDTO nisAllocationReqListDTO) {
		if(nisAllocationReqListDTO == null){
			return;
		}
		//不取联机余额
		Map<String, BigDecimal> PayerAccBal = btBankAccBalService.getBalByAccAndCur(nisAllocationReqListDTO.getPayerAcc(), nisAllocationReqListDTO.getCurId());
		Map<String, BigDecimal> payeeAccBal = btBankAccBalService.getBalByAccAndCur(nisAllocationReqListDTO.getPayeeAcc(), nisAllocationReqListDTO.getCurId());
		nisAllocationReqListDTO.setPayeeAccBalance(payeeAccBal.getOrDefault(nisAllocationReqListDTO.getPayeeAcc(), BigDecimal.ZERO));
		nisAllocationReqListDTO.setPayerAccBalance(PayerAccBal.getOrDefault(nisAllocationReqListDTO.getPayerAcc(), BigDecimal.ZERO));
	}

	/**
	 * 撤回
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowRevoke(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		NisAllocationReq entity = this.get(workFlowResult.getBusinessKey(), true);
		//更新审批状态
		entity.setStatus(AllocationConstant.STATUE_TEMP);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		//更新计划执行数据
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
			fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_ALLOCATION, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo(), AllocationConstant.STATUE_TEMP, user);
		}
		//生成日志
		nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, NisMessageConstant.NIS_LOG_WITHDRAW_SUCC, NisMessageConstant.BUS_TYPE_ALLOCATION, user);
	}

	/**
	 * 保存资金调拨单(接口专用)
	 *
	 * @param nisAllocationReqListDTO
	 */
	public void saveNisAllocationReqForImpl(NisAllocationReqListDTO nisAllocationReqListDTO, ICurrentUser user) {
		NisAllocationReq entity = new NisAllocationReq();
		BeanUtils.copyNotNullProperties(nisAllocationReqListDTO, entity);
		//判断是否挂证书
		if (!StringUtils.isEmpty(nisAllocationReqListDTO.getCaCoding())) {
			SysCaInfoDTO caDto = new SysCaInfoDTO();
			caDto.setRandom(nisAllocationReqListDTO.getRandom());
			caDto.setEncrySource(nisAllocationReqListDTO.getEncrySource());
			caDto.setEncryDate(DateUtils.getDate());
			caDto.setCaCoding(nisAllocationReqListDTO.getCaCoding());
			entity.setCaInfoId(caService.saveOrUpdateCaInfo(caDto));
		}
		// 付款账户和收款账户不能一致
		if (entity.getPayeeAcc().equalsIgnoreCase(entity.getPayerAcc())) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
		}
		String billNo = SequenceSingleton.getSequence(SequenceEnum.NIS_ALLOCATION_REQ);
		//外部单据Id
		if (StringUtils.isNotEmpty(nisAllocationReqListDTO.getErpId())) {
			entity.setErpId(nisAllocationReqListDTO.getErpId());
		}
		entity.setBillNo(billNo);
		//非手工录入的单据不写创建人
		if (StringUtils.isBlank(nisAllocationReqListDTO.getErpId())) {
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
		} else {
			entity.setCreateBy(null);
			entity.setCreateDate(null);
		}
		//资金池标识 0-非资金池 1-资金池 默认0
		entity.setPoolSign(AllocationConstant.POOL_SIGN_0);
		//下拨/请款根据付款单位判断是否是资金池主体单位
		//上划/上缴根据收款单位判断是否是资金池主体单位
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayerAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		} else if (AllocationConstant.ALLOCATION_TYPE_UP.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_TURN.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		}
		this.save(entity);
		// 挂接资金计划
		this.writeBudgetProcess(entity, nisAllocationReqListDTO.getItemCode(), nisAllocationReqListDTO.getItemName(), nisAllocationReqListDTO.getProcessMoney(), nisAllocationReqListDTO.getExchangeRate(), nisAllocationReqListDTO.getProcessCurMoney(), user);
		// 保存用途信息
		if (!StringUtils.isBlank(nisAllocationReqListDTO.getPurpose())) {
			entity.setPurpose(nisAllocationReqListDTO.getPurpose());
		}
		Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
		// 启动流程
		if (AllocationConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
			String sign = this.getSign(entity.getType());
			getStartHandler().handler(sign, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		// 生成日志
		nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
	}

	/**
	 * 修改资金调拨单(接口专用)
	 *
	 * @param nisAllocationReq
	 */
	public void updateNisAllocationReqForImpl(NisAllocationReqListDTO nisAllocationReq, ICurrentUser user) {
		NisAllocationReq entity = this.get(nisAllocationReq.getId(), true);
		BeanUtils.copyNotNullProperties(nisAllocationReq, entity);
		// 付款账户和收款账户不能一致
		if (entity.getPayeeAcc().equalsIgnoreCase(entity.getPayerAcc())) {
			throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
		}
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		//判断是否挂证书
		if (!EmptyUtils.isEmpty(nisAllocationReq.getCaCoding())) {
			entity.setCaInfoId(getCaInfoId(nisAllocationReq));
		}
		//资金池标识 0-非资金池 1-资金池 默认0
		entity.setPoolSign(AllocationConstant.POOL_SIGN_0);
		//下拨/请款根据付款单位判断是否是资金池主体单位
		//上划/上缴根据收款单位判断是否是资金池主体单位
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayerAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		} else if (AllocationConstant.ALLOCATION_TYPE_UP.intValue() == entity.getType().intValue()
				|| AllocationConstant.ALLOCATION_TYPE_TURN.intValue() == entity.getType().intValue()) {
			BtBankAccDTO bankAcc = this.btBankAccService.getBankAccById(entity.getPayeeAccId());
			if (fcCorpPoolService.isCorpPool(bankAcc.getCorpId())) {
				entity.setPoolSign(AllocationConstant.POOL_SIGN_1);
			}
		}
		this.saveOrUpdate(entity);
		// 保存用途信息
		if (!StringUtils.isBlank(nisAllocationReq.getPurpose())) {
			entity.setPurpose(nisAllocationReq.getPurpose());
		}
		// 启动流程
		if (AllocationConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			String sign = this.getSign(entity.getType());
			getStartHandler().handler(sign, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		//完成通知提醒
		String tag = getTag(entity.getType());
		if (!StringUtils.isBlank(tag)) {
			notificationService.complete(tag, entity.getId(), user.getId());
		}
	}

	/**
	 * 返回证书ID
	 *
	 * @param nisAllocationReq
	 * @return
	 */
	private String getCaInfoId(NisAllocationReqListDTO nisAllocationReq) {
		SysCaInfoDTO caDto = new SysCaInfoDTO();
		caDto.setRandom(nisAllocationReq.getRandom());
		caDto.setEncrySource(nisAllocationReq.getEncrySource());
		caDto.setEncryDate(DateUtils.getDate());
		caDto.setCaCoding(nisAllocationReq.getCaCoding());
		if (!EmptyUtils.isBlank(nisAllocationReq.getCaInfoId())) {
			caDto.setId(nisAllocationReq.getCaInfoId());
		}
		caService.saveOrUpdateCaInfo(caDto);
		return caDto.getId();
	}

	/**
	 * 获取在途的资金调拨单
	 *
	 * @param corpId
	 * @return
	 */
	public List<NisAllocationReqListDTO> listNotFinishedNisAllocationReq(String corpId) {
		//新增状态字段
		StringBuilder sql = new StringBuilder();
		sql.append(" and ar.voucher_Stat = " + BdlinkConstant.SUCCESS);
		List<NisAllocationReqListDTO> nisAllocationReqListDTOList = this.nisAllocationReqDao.listNotFinishedNisAllocationReq(corpId, sql.toString());
		List<String> billIds = new ArrayList<>();
		for (NisAllocationReqListDTO nisAllocationReqListDTO : nisAllocationReqListDTOList) {
			this.setBankName(nisAllocationReqListDTO);
			this.setCreateByName(nisAllocationReqListDTO);
			this.setCurName(nisAllocationReqListDTO);
			this.setCorpName(nisAllocationReqListDTO);
			billIds.add(nisAllocationReqListDTO.getId());
		}
		String bisExcVoucherFroms = BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN + CommonConstant.COMMA_SPLIT +
				BdlinkConstant.FROM_ALLOCATION_TYPE_ASK + CommonConstant.COMMA_SPLIT +
				BdlinkConstant.FROM_ALLOCATION_TYPE_POS;
		String bisGatherCmdVoucherFroms = BdlinkConstant.FROM_ALLOCATION_TYPE_UP + CommonConstant.COMMA_SPLIT +
				BdlinkConstant.FROM_ALLOCATION_TYPE_TURN;
		this.setBisExcValue(billIds, nisAllocationReqListDTOList, bisExcVoucherFroms);
		this.setBisGatherCmdValue(billIds, nisAllocationReqListDTOList, bisGatherCmdVoucherFroms);
		return nisAllocationReqListDTOList;
	}

	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if (StringUtils.isNotEmpty(corpId)) {
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在途的上划单据，不允许禁用！
			int num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_UP);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_UP,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_ALLOCATION_UP_ERROR)));
			}
			//单位有在途的下拨单据，不允许禁用！
			num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_DOWN);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_DOWN,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_ALLOCATION_DOWN_ERROR)));
			}
			//单位有在途的请款单据，不允许禁用！
			num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_ASK);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_ASK,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_ALLOCATION_ASK_ERROR)));
			}
			//单位有在途的上缴单据，不允许禁用！
			num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_TURN);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_TURN,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_ALLOCATION_TURN_ERROR)));
			}
			//单位有在途的头寸调拨单据，不允许禁用！
			num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_POS);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_POS,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_ALLOCATION_POS_ERROR)));
			}
			//单位有记账中的资金调拨单，不允许禁用！
			String isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
			if (CommonConstant.YES.equals(isAccount)) {
				num = this.nisAllocationReqDao.getAllocationReqCountByCorpIdForTally(corpId);
				if (num > 0) {
					verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
							SysConfigConstant.Module.FCL,
							SysConfigConstant.BusType.FCL_ALLOCATION_TALLY,
							multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_ALLOCATION_TALLY_ERROR)));
				}
			}
			return verifyErrorDTOS;
		}
		return null;
	}

	@Override
	public List<CorpVerifyErrorDTO> verifyCorpParentChang(String corpId) {
		if (StringUtils.isNotEmpty(corpId)) {
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在途的上划单据，不允许变更！
			int num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_UP);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_UP,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_CHANGE_ALLOCATION_UP_ERROR)));
			}
			//单位有在途的下拨单据，不允许变更！
			num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_DOWN);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_DOWN,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_CHANGE_ALLOCATION_DOWN_ERROR)));
			}
			//单位有在途的请款单据，不允许变更！
			num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_ASK);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_ASK,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_CHANGE_ALLOCATION_ASK_ERROR)));
			}
			//单位有在途的上缴单据，不允许变更！
			num = this.nisAllocationReqDao.getAllocationReqCountByCorpId(corpId, AllocationConstant.ALLOCATION_TYPE_TURN);
			if (num > 0) {
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_ALLOCATION_TURN,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_CHANGE_ALLOCATION_TURN_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}

	/**
	 * 待记账查询
	 */
	public List<NisAllocationReqDTO> queryTallyNisAllReq(Integer menuType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" and ar.type = ").append(menuType);
		sql.append(" and ar.status = ").append(AllocationConstant.STATUE_CHECK);
		sql.append(" and (ar.tally_Sign ='' or ar.tally_Sign ='0' or  ar.tally_sign is null) ");
		// 记账时，头寸调拨过滤成员单位数据
		if (AllocationConstant.ALLOCATION_TYPE_POS.intValue() == menuType.intValue()) {
			sql.append(" and exists (select 1 from sys_corp c where c.id = ar.corp_id and c.type = 0) ");
			//头寸调拨是否记账
			String isAccountPos = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
			if (!CommonConstant.YES.equals(isAccountPos)) {
				return new ArrayList<>();
			}
		} else {
			//是否记账
			String isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
			if (!CommonConstant.YES.equals(isAccount)) {
				return new ArrayList<>();
			}
		}
		// 手工记账 是否先付款后记账
		String payAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_PAYACCOUNT).getValue();
		if (CommonConstant.YES.equalsIgnoreCase(payAccount)) {
			if (AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == menuType.intValue()) {
				sql.append(" and b.voucher_From = ").append(BdlinkConstant.FROM_ALLOCATION_TYPE_ASK);
			} else if (AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == menuType.intValue()) {
				sql.append(" and b.voucher_From = ").append(BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN);
			} else if (AllocationConstant.ALLOCATION_TYPE_POS.intValue() == menuType.intValue()) {
				sql.append(" and b.voucher_From = ").append(BdlinkConstant.FROM_ALLOCATION_TYPE_POS);
			}
			return nisAllocationReqDao.getTallyBisExc(sql.toString());
		} else {
			return nisAllocationReqDao.getTallyAccount(sql.toString());
		}
	}

	/**
	 * 判断是否存在相同的时间戳
	 *
	 * @param timeStamp
	 * @return
	 */
	public boolean isExistTimeStamp(String timeStamp) {
		if (StringUtils.isNotBlank(timeStamp)) {
			List<NisAllocationReq> list = this.nisAllocationReqDao.listBy("timeStamp", timeStamp);
			if (EmptyUtils.isNotEmpty(list)) {
				return true;
			}
		}
		return false;
	}

	public String checkBal(String ids,Integer type) {
		String msg = "";
		if (ObjectUtil.isEmpty(ids)) return msg;

		// 头寸是否记账
		String isAccountPos = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
		// 银行账户是否校验余额
		String bankaccOver = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER).getValue();
		// 内部账户是否校验余额
		String accountOver = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ACCOUNTOVER).getValue();

		// 是否需要校验银行账号
		Boolean isBankBal = false;
		if (bankaccOver.equals("1") || bankaccOver.equals("2")){
			isBankBal = true;
		}

		// 是否需要校验内部账户余额
		Boolean isAccountBal = false;
		// 头寸调拨
		if (AllocationConstant.ALLOCATION_TYPE_POS.equals(type)){
			if ("1".equals(isAccountPos) && (accountOver.equals("1") || accountOver.equals("2"))) {
				isAccountBal = true;
			}
		}else{
			if ( accountOver.equals("1") || accountOver.equals("2")) {
				isAccountBal = true;
			}
		}

		// 提示消息
		String bankErrorMsg = multiLangServiceI.getLang("nis.allocation.message.money.gt.balance.stop");
		String accountErrorMsg = "金额大于内部账户余额！";
		// 银行账户余额不足单据
		List<String> bankErrorBillNos = new ArrayList<>();
		// 内部账户余额不足单据
		List<String> accountErrorBillNos = new ArrayList<>();

		String[] idStr = ids.split(",");
		for (int i = 0; i < idStr.length; i++) {
			//获取调拨单
			NisAllocationReq nisAllocationReq = nisAllocationReqDao.get(idStr[i], true);
			// 校验银行账户余额是否大于交易金额
			if (isBankBal){
				BisAccBalDTO balDTO = bisAccBalService.getBisAccBalBy(nisAllocationReq.getPayerAcc(), nisAllocationReq.getCurId(), null, true);
				if (EmptyUtils.isEmpty(balDTO) || EmptyUtils.isEmpty(balDTO.getBal()) || balDTO.getBal().compareTo(nisAllocationReq.getMoney()) < 0) {
					bankErrorBillNos.add(nisAllocationReq.getBillNo());
				}
			}
			// 校验内部账户余额是否大于交易金额
			if(isAccountBal){
				String blnctrAcc = this.btBankAccService.getBlnctrAcc(nisAllocationReq.getPayerAcc(), nisAllocationReq.getCurId());
				FcOnlineLedgerDTO dto = this.fcOnlineLedgerService.getByBlnctrAcc(blnctrAcc);
				if (EmptyUtils.isEmpty(dto) || EmptyUtils.isEmpty(dto.getCheckBalance()) || dto.getCheckBalance().compareTo(nisAllocationReq.getMoney()) < 0) {
					accountErrorBillNos.add(nisAllocationReq.getBillNo());
				}
			}
		}

		if (ObjectUtil.isNotEmpty(bankErrorBillNos)){
			msg = String.join(",", bankErrorBillNos) + " " +bankErrorMsg;
		}

		if (ObjectUtil.isNotEmpty(accountErrorBillNos)){
			if (ObjectUtil.isNotEmpty(msg)){
				msg = msg + " " + String.join(",", accountErrorBillNos) + " " +accountErrorMsg;
			}else{
				msg = String.join(",", accountErrorBillNos) + " " +accountErrorMsg;
			}
		}

		return msg;
	}

	/**
	 * 带原状态更新资金调拨单
	 *
	 * @param vo        资金调拨单对象
	 * @param oldStatus 原状态控制
	 * @return
	 */
	public int updateNisAllocationReqStatus(NisAllocationReqListDTO vo, Integer[] oldStatus) {
		int result = this.nisAllocationReqDao.updateNisAllocationReqStatus(vo, oldStatus);
		if (result < 1) {
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getBillNo());
		}
		return result;
	}

	/**
	 * 带原状态更新资金调拨单
	 *
	 * @param vo        资金调拨单对象
	 * @return
	 */
	public void updateNisAllocationReqStatus(NisAllocationReqListDTO vo) {
		int result = this.nisAllocationReqDao.updateNisAllocationReqStatus(vo);
	}

	/**
	 * 工作流审批校验
	 *
	 * @param params       Map<流程分类KEY, 业务ID列表>
	 * @param workflowPass 审批操作标识：1，同意；3，打回上一级；4，打回制单处。
	 * @param isNeedVerify 是否要校验工作流审批：true表示要校验；false表示不用校验。用户在看到校验失败提示信息后，仍然确定提交审批，则isNeedVerify为false。
	 * @throws WorkFlowVerifyException
	 */
	@Override
	public void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify) throws WorkFlowVerifyException {
		if (isNeedVerify) {            // 模拟校验审批
			if ("1".equals(workflowPass)) {
				List<String> ids = null;
				for (String key : params.keySet()) {
                    ids = params.get(key);
                    break;
                }
				if (ids == null) {
					return;
				}

				// 银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
				String bankaccOver = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER_APPROVE).getValue();

				// 账户余额（批量提交校验账户余额)
				for (String id : ids) {
					// 审批时前置校验
					System.out.println("=====================================================进入审批" + id);
					checkBeforeApprove(id, workflowPass, bankaccOver);
				}
			}
		}
	}

	/**
	 * 审批前置校验
	 *
	 * @param id
	 * @param workflowPass
	 */
	public void checkBeforeApprove(String id, String workflowPass, String bankaccOver) {
		NisAllocationReq nisAllocationReq = this.nisAllocationReqDao.get(id, true);
		Integer status = "1".equals(workflowPass) ? AllocationConstant.STATUE_CHECK : AllocationConstant.STATUE_BACK;
		NisAllocationReqListDTO vo = new NisAllocationReqListDTO();
		BeanUtils.copyNotNullProperties(nisAllocationReq, vo);
		vo.setStatus(status);
		vo.setVoucherNo(nisAllocationReq.getId());
		vo.setUpdateBy(UserContextHelper.helper().getUserLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		BisExcDTO bisExc = new BisExcDTO();
		boolean flag = false;
		// 银行账户余额不足，不允许提交单据
		BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisAllocationReq.getPayerAcc(), nisAllocationReq.getCurId());
		BigDecimal curBal = bal == null ? BigDecimal.ZERO : bal.getBal();
		if (nisAllocationReq.getMoney().compareTo(curBal) > 0) {
			// throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_BALANCE, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
			if ("1".equals(bankaccOver)) {
				// 1-只提醒不控制
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_CONFIRM_BALANCE, WorkFlowConstant.VERITY_STATUS_CONFIRM);
			} else if ("2".equals(bankaccOver)) {
				// 2-控制
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_BALANCE, WorkFlowConstant.VERITY_STATUS_ALERT);
			}
		}

		String responseFlag = WorkFlowConstant.VERITY_STATUS_CONTROLLER;
		if (AllocationConstant.STATUE_CHECK.equals(status)) {
			if (AllocationConstant.ALLOCATION_TYPE_UP.equals(nisAllocationReq.getType())
					|| AllocationConstant.ALLOCATION_TYPE_TURN.equals(nisAllocationReq.getType())) {
				// 10-上划 和 14-上缴 生成 收款指令
				// 获取付款账号  通用，都需要判断银行账户余额
				BtBankAccDTO payerAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayerAccId());
				if (payerAcc == null) {
					String[] params = {StringUtils.isEmpty(nisAllocationReq.getPayeeAccName()) ? "" : nisAllocationReq.getPayeeAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, WorkFlowConstant.VERITY_STATUS_CONTROLLER, params);
				}
				// 指令重复校验


			} else if (AllocationConstant.POOL_SIGN_1.equals(nisAllocationReq.getPoolSign())
					&& (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())
					|| AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType()))) {
				// 资金池 11-下拨 12-请款 直接生成指令，无需判断记账
				// 获取收款账号
				BtBankAccDTO payeeAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayeeAccId());
				if (payeeAcc == null) {
					String[] params = {StringUtils.isEmpty(nisAllocationReq.getPayeeAccName()) ? "" : nisAllocationReq.getPayeeAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, WorkFlowConstant.VERITY_STATUS_CONTROLLER, params);
				}
				BtBankAccDTO payerAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayerAccId());
				// 没有维护接口代码
				if (EmptyUtils.isEmpty(payerAcc.getBifCode())) {
					String[] params = {StringUtils.isEmpty(payerAcc.getAccName()) ? "" : payerAcc.getAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_WAGE_MESSAGE_BIFCODE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER, params);
				}
				// 获取接口业务规则
				BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(payerAcc.getBifCode());
				if (EmptyUtils.isEmpty(bisBifRule) || StringUtils.isEmpty(bisBifRule.getId())) {
					String[] params = {StringUtils.isEmpty(payerAcc.getBifCode()) ? "" : payerAcc.getBifCode(), StringUtils.isEmpty(payerAcc.getAccName()) ? "" : payerAcc.getAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_WAGE_MESSAGE_BISBIFRULE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER, params);
				}
				// 根据调拨类型设置结算方式
				FcVoucherTypeDTO voucherType = null;
				if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())) {
					bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN);
					bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_33);
					voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
				} else if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())) {
					bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_ASK);
					bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_32);
					voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
				} else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
					bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_POS);
					bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_34);
					voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_37);
				}
				if (voucherType != null) {
					bisExc.setVoucherTypeId(voucherType.getId());
				}
				if (!AllocationConstant.ALLOCATION_TYPE_UP.equals(nisAllocationReq.getType())
						&& !AllocationConstant.ALLOCATION_TYPE_TURN.equals(nisAllocationReq.getType())) {// 上划,上缴不需要判断结算方式
					// 结算方式为空
					if (StringUtils.isBlank(bisExc.getVoucherTypeId())) {
						throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_PAY_MESSAGE_PAYTYPE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
					}
				}

			} else {
				// 获取付款账号  通用，都需要判断银行账户余额
				BtBankAccDTO payerAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayerAccId());
				if (payerAcc == null) {
					String[] params = {StringUtils.isEmpty(nisAllocationReq.getPayeeAccName()) ? "" : nisAllocationReq.getPayeeAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, WorkFlowConstant.VERITY_STATUS_CONTROLLER, params);
				}
				// 获取收款账号
				BtBankAccDTO payeeAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayeeAccId());
				if (payeeAcc == null) {
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, nisAllocationReq.getPayeeAcc());
				}
				// 生成付款指令
				bisExc.setCbsBillId(nisAllocationReq.getId());
				bisExc.setCbsBillNo(nisAllocationReq.getBillNo());
				bisExc.setCorpId(nisAllocationReq.getCorpId());
				bisExc.setReqDate(DateUtils.getDate());
				// 付款方信息
				bisExc.setPayerAcc(nisAllocationReq.getPayerAcc());
				bisExc.setPayerAccName(nisAllocationReq.getPayerAccName());
				bisExc.setPayerBank(payerAcc.getBankName());
				bisExc.setPayerProv(payerAcc.getProv());
				bisExc.setPayerCity(payerAcc.getCity());
				bisExc.setCurId(nisAllocationReq.getCurId());
				bisExc.setPayMoney(nisAllocationReq.getMoney());
				bisExc.setBifCode(payerAcc.getBifCode());
				// 收款方信息
				bisExc.setPayeeAcc(nisAllocationReq.getPayeeAcc());
				bisExc.setPayeeAccName(nisAllocationReq.getPayeeAccName());
				bisExc.setPayeeBank(payeeAcc.getBankName());
				bisExc.setPayeeProv(payeeAcc.getProv());
				bisExc.setPayeeCity(payeeAcc.getCity());
				bisExc.setPayeeBankCode(payeeAcc.getBankCode());
				bisExc.setWishPayday(nisAllocationReq.getWishPayday());
				// 交易模式 3-对公 4-对私 默认4
				bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
				bisExc.setIsQuick(nisAllocationReq.getIsQuick());
				bisExc.setDifBank(nisAllocationReq.getDifBank());
				bisExc.setAreaSign(nisAllocationReq.getAreaSign());
				bisExc.setPurpose(nisAllocationReq.getPurpose());
				bisExc.setAddWord(nisAllocationReq.getPurpose());
				// 根据调拨类型设置结算方式
				FcVoucherTypeDTO voucherType = null;
				if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())) {
					bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN);
					bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_33);
					voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
				} else if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())) {
					bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_ASK);
					bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_32);
					voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
				} else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
					bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_POS);
					bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_34);
					voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_37);
				}
				if (voucherType != null) {
					bisExc.setVoucherTypeId(voucherType.getId());
				}
				// 结算方式为空
				if (StringUtils.isBlank(bisExc.getVoucherTypeId())) {
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_PAY_MESSAGE_PAYTYPE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
				}
				// 通用审批前置校验
				// 没有维护接口代码
				if (EmptyUtils.isEmpty(payerAcc.getBifCode())) {
					String[] params = {StringUtils.isEmpty(payerAcc.getAccName()) ? "" : payerAcc.getAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_WAGE_MESSAGE_BIFCODE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER, params);
				}
				// 获取接口业务规则
				BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(payerAcc.getBifCode());
				if (EmptyUtils.isEmpty(bisBifRule) || StringUtils.isEmpty(bisBifRule.getId())) {
					String[] params = {StringUtils.isEmpty(payerAcc.getBifCode()) ? "" : payerAcc.getBifCode(), StringUtils.isEmpty(payerAcc.getAccName()) ? "" : payerAcc.getAccName()};
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_WAGE_MESSAGE_BISBIFRULE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER, params);
				}
				//  非资金池 11-下拨 12-请款 13-头寸调拨 生成付款指令
				// 判断资金调拨是否需要记账(头寸调拨单据来源是银行借款的话，是否记账是根据银行借款，借款管理是否记账参数来确定)
				logger.error("自动记账入口");
				String isAccountPos = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue();
				String isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
				//不记账跳过
				if ((CommonConstant.NO.equals(isAccount) && !AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType()))
						|| (CommonConstant.NO.equals(isAccountPos) && AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType()))) {
					return;
				}
				// 付方账号
				BtBankAccCurDTO bankAccCurPayer = btBankAccCurService.getBankAccCurByBankAccId(nisAllocationReq.getPayerAccId(), nisAllocationReq.getCurId());
				FcAccDTO fcAccDTOPayer = fcAccService.getAccDTOByAccount(bankAccCurPayer.getCounterAcc());
				if (StringUtils.isEmpty(bankAccCurPayer.getCounterAcc()) || ObjectUtils.isEmpty(fcAccDTOPayer)) {
					// 抛异常
					throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_PAY_AUTO_NO_SF_FCACC, responseFlag);
				}
				FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(bankAccCurPayer.getCounterAcc());
				// 自动
				// 2.校验内部账号透支与余额开始
				if (StringUtils.isEmpty(fcAccDTOPayer.getOverdraftSign()) || InnerAccountConstant.OVERDRAFT_SIGN_N.equals(fcAccDTOPayer.getOverdraftSign())) {
					// 内部账号不能透支的情况下，校验余额是否大于等于付款金额，为否时置为失败
					if (fcOnlineLedgerDTO.getOnlineBalance().compareTo(nisAllocationReq.getMoney()) < 0) {
						// 抛异常
						throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_PAY_NO_OVER_LESS_THAN_PAYMONEY, responseFlag);
					}
				} else if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(fcAccDTOPayer.getOverdraftSign())) {
					// 内部账户允许透支（判断透支金额overdraft_money），限额透支的情况下，校验可透支余额是否大于等于付款金额，为否时置为失败
					BigDecimal totalMoney = fcAccDTOPayer.getOverdraftMoney().add(fcOnlineLedgerDTO.getOnlineBalance());
					if (totalMoney.compareTo(nisAllocationReq.getMoney()) < 0) {
						// 抛异常
						throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_PAY_PERMIT_OVER_LESS_THAN_PAYMONEY, responseFlag);
					}
				} else {
					// DO NOTHING
				}
			}

			// 如果是自动指令 调用记账
			String bisExcAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BISEXC_AUTOACCOUNT).getValue();
			if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount) && Objects.nonNull(bisExc)) {
				if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())
						|| AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())
						|| AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {// 下拨,请款，头寸需要校验自动记账
					BisExcDTO bisExcDto = wrapBisExc(vo, UserContextHelper.helper().getCurrentUser());// 封装指令，不保存
					allocationBisExcBackService.checkSendBisExcAccounting(bisExcDto, UserContextHelper.helper().getCurrentUser());
				}
			}
		}
	}

	/**
	 * 封装指令
	 *
	 * @param nisAllocationReq
	 */
	public BisExcDTO wrapBisExc(NisAllocationReqListDTO nisAllocationReq, ICurrentUser user) {
		//获取付款账号
		BtBankAccDTO payerAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayerAccId());
		if (payerAcc == null) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
		logger.error("自动记账新增指令方法！");
		//获取收款账号
		BtBankAccDTO payeeAcc = btBankAccService.getBankAccById(nisAllocationReq.getPayeeAccId());
		if (payeeAcc == null) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
		if (EmptyUtils.isEmpty(payerAcc.getBifCode())) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_WAGE_MESSAGE_BIFCODE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
		//获取接口业务规则
		BisBifInitDTO bifCode = bisBifInitService.getObjectById(payerAcc.getBifCode());
		BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(payerAcc.getBifCode());
		if (EmptyUtils.isEmpty(bisBifRule) || StringUtils.isEmpty(bisBifRule.getId())) {
			throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_WAGE_MESSAGE_BISBIFRULE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
		}
		//生成付款指令
		BisExcDTO bisExc = new BisExcDTO();
		bisExc.setCbsBillId(nisAllocationReq.getId());
		bisExc.setCbsBillNo(nisAllocationReq.getBillNo());
		bisExc.setCorpId(nisAllocationReq.getCorpId());
		bisExc.setReqDate(DateUtils.getDate());
		//付款方信息
		bisExc.setPayerAcc(nisAllocationReq.getPayerAcc());
		bisExc.setPayerAccName(nisAllocationReq.getPayerAccName());
		bisExc.setPayerBank(payerAcc.getBankName());
		bisExc.setPayerProv(payerAcc.getProv());
		bisExc.setPayerCity(payerAcc.getCity());
		bisExc.setCurId(nisAllocationReq.getCurId());
		bisExc.setPayMoney(nisAllocationReq.getMoney());
		bisExc.setBifCode(payerAcc.getBifCode());
		//收款方信息
		bisExc.setPayeeAcc(nisAllocationReq.getPayeeAcc());
		bisExc.setPayeeAccName(nisAllocationReq.getPayeeAccName());
		bisExc.setPayeeBank(payeeAcc.getBankName());
		bisExc.setPayeeProv(payeeAcc.getProv());
		bisExc.setPayeeCity(payeeAcc.getCity());
		bisExc.setPayeeBankCode(payeeAcc.getBankCode());
		bisExc.setWishPayday(nisAllocationReq.getWishPayday());
		//交易模式 3-对公 4-对私 默认4
		bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
		bisExc.setIsQuick(nisAllocationReq.getIsQuick());
		bisExc.setDifBank(nisAllocationReq.getDifBank());
		bisExc.setAreaSign(nisAllocationReq.getAreaSign());
		bisExc.setPurpose(nisAllocationReq.getPurpose());
		bisExc.setAddWord(nisAllocationReq.getPurpose());
		// 根据调拨类型设置结算方式
		FcVoucherTypeDTO voucherType = null;
		if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())) {
			bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN);
			bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_33);
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
		} else if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())) {
			bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_ASK);
			bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_32);
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
		} else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
			bisExc.setVoucherFrom(BdlinkConstant.FROM_ALLOCATION_TYPE_POS);
			bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_34);
			voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_37);
		}
		if (voucherType != null) {
			bisExc.setVoucherTypeId(voucherType.getId());
		}
		if (!AllocationConstant.ALLOCATION_TYPE_UP.equals(nisAllocationReq.getType())
				&& !AllocationConstant.ALLOCATION_TYPE_TURN.equals(nisAllocationReq.getType())) {//上划,上缴不需要判断结算方式
			//结算方式为空
			if (StringUtils.isBlank(bisExc.getVoucherTypeId())) {
				throw new WorkFlowVerifyException(WorkFlowMessageEnum.NIS_PAY_MESSAGE_PAYTYPE_NULL, WorkFlowConstant.VERITY_STATUS_CONTROLLER);
			}
		}
		bisExc.setVoucherStat(BdlinkConstant.WAIT_FILL);
		// 判断资金调拨-下拨/请款   是否自动生成指令
		String bisExcAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BISEXC_AUTOACCOUNT).getValue();
		// todo:数据转换
		if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount)) {
			nisAllocationReq.setVoucherStat(BdlinkConstant.PRE_SEND);
		} else {
			bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
			nisAllocationReq.setVoucherStat(BdlinkConstant.WAIT_FILL);
		}
		return bisExc;
	}

	public void setBankName(NisAllocationReqListDTO nisAllocationReqListDTO) {
		if(nisAllocationReqListDTO == null){
			return;
		}
		BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(nisAllocationReqListDTO.getPayerAccId());
		if (!EmptyUtils.isEmpty(btBankAccDTO)) {
			nisAllocationReqListDTO.setPayerBankName(btBankAccDTO.getBankName());
			nisAllocationReqListDTO.setPayerCorpId(btBankAccDTO.getCorpId());
			nisAllocationReqListDTO.setPayerCorpName(btBankAccDTO.getCorpName());
			nisAllocationReqListDTO.setPayerCounterAcc(btBankAccDTO.getCounterAccId());

		}
		btBankAccDTO = btBankAccService.getBankAccById(nisAllocationReqListDTO.getPayeeAccId());
		if (!EmptyUtils.isEmpty(btBankAccDTO)) {
			nisAllocationReqListDTO.setPayeeBankName(btBankAccDTO.getBankName());
			nisAllocationReqListDTO.setPayeeCorpId(btBankAccDTO.getCorpId());
			nisAllocationReqListDTO.setPayeeCorpName(btBankAccDTO.getCorpName());
			if(EmptyUtils.isEmpty(nisAllocationReqListDTO.getPayeeCounterAcc())) {
				nisAllocationReqListDTO.setPayeeCounterAcc(btBankAccDTO.getCounterAccId());
			}
		}
	}

	public void setBisExcValue(List<String> billIds, List<NisAllocationReqListDTO> nisAllocationReqListDTOList, String voucherFroms) {
		Map<String, BisExcDTO> bisExcMap = bisExcServiceImpl.getBisExcMapByCbsBillIds(billIds, voucherFroms);
		for (NisAllocationReqListDTO nisAllocationReqListDTO : nisAllocationReqListDTOList) {
			BisExcDTO bisExcDTO = bisExcMap.get(nisAllocationReqListDTO.getId());
			if (!EmptyUtils.isEmpty(bisExcDTO)) {
				nisAllocationReqListDTO.setBisExcId(bisExcDTO.getId());
				nisAllocationReqListDTO.setTransTime(bisExcDTO.getTransTime());
				nisAllocationReqListDTO.setVoucherStat(bisExcDTO.getVoucherStat());
			}
		}
	}

	public void setBisGatherCmdValue(List<String> billIds, List<NisAllocationReqListDTO> nisAllocationReqListDTOList, String voucherFroms) {
		Map<String, BisGatherCmdDTO> bisGatherCmdMap = bisGatherCmdService.getBisGatherCmdMapByCbsBillIds(billIds, voucherFroms);
		for (NisAllocationReqListDTO nisAllocationReqListDTO : nisAllocationReqListDTOList) {
			BisGatherCmdDTO bisGatherCmdDTO = bisGatherCmdMap.get(nisAllocationReqListDTO.getId());
			if (!EmptyUtils.isEmpty(bisGatherCmdDTO)) {
				nisAllocationReqListDTO.setBisGatherCmdId(bisGatherCmdDTO.getId());
				nisAllocationReqListDTO.setTransTime(bisGatherCmdDTO.getTransTime());
				nisAllocationReqListDTO.setCmdStat(bisGatherCmdDTO.getCmdStat());
			}
		}
	}

	public void setCreateByName(NisAllocationReqListDTO nisAllocationReqListDTO) {
		if(nisAllocationReqListDTO == null){
			return;
		}
		UserDTO userDTO = iUserService.getByLoginName(nisAllocationReqListDTO.getCreateBy());
		if (!EmptyUtils.isEmpty(userDTO)) {
			nisAllocationReqListDTO.setCreateByName(userDTO.getUsername());
		}
	}

	public void setCurName(NisAllocationReqListDTO nisAllocationReqListDTO) {
		if(nisAllocationReqListDTO == null){
			return;
		}
		BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(nisAllocationReqListDTO.getCurId());
		if (!EmptyUtils.isEmpty(btCurrencyDTO)) {
			nisAllocationReqListDTO.setCurName(btCurrencyDTO.getCurName());
		}
	}

	public void setCorpName(NisAllocationReqListDTO nisAllocationReqListDTO) {
		if(nisAllocationReqListDTO == null){
			return;
		}
		String corpId = nisAllocationReqListDTO.getCorpId();
		if (!EmptyUtils.isEmpty(corpId)) {
			CorpDTO corpDTO = corpService.get(corpId);
			if (!EmptyUtils.isEmpty(corpDTO)) {
				nisAllocationReqListDTO.setCorpName(corpDTO.getName());
			}
		}
		String payerCorpId = nisAllocationReqListDTO.getPayerCorpId();
		if (!EmptyUtils.isEmpty(payerCorpId)) {
			CorpDTO corpDTO = corpService.get(payerCorpId);
			if (!EmptyUtils.isEmpty(corpDTO)) {
				nisAllocationReqListDTO.setPayerCorpName(corpDTO.getName());
			}
		}
		String payeeCorpId = nisAllocationReqListDTO.getPayeeCorpId();
		if (!EmptyUtils.isEmpty(payeeCorpId)) {
			CorpDTO corpDTO = corpService.get(payeeCorpId);
			if (!EmptyUtils.isEmpty(corpDTO)) {
				nisAllocationReqListDTO.setPayeeCorpName(corpDTO.getName());
			}
		}
	}

	public List<NisAllocationReqDTO> getNisAllocationReqBillFrom(NisAllocationReqDTO dto) {
		return nisAllocationReqDao.getNisAllocationReqBillFrom(dto);
	}

	public int updateFromBisExcChange(BisExcChangeMsg bisExcChangeMsg) {
		return nisAllocationReqDao.updateVoucherState(bisExcChangeMsg.getCbsBillId(), bisExcChangeMsg.getVoucherStat());
	}

	/**
	 * 根据单位、账号类型、权限，查询帐号列表
	 *
	 * @param accountSearchDTO
	 * @return
	 */
	public List<AccountDTO> listBy(AccountSearchDTO accountSearchDTO) {
		logger.info("开始执行AccountGrantService.getListBy()方法。corpId：{}，accountType：{}，resource：{}，isMustCorp：{}",
				accountSearchDTO.getCorpId(), accountSearchDTO.getAccountType(), accountSearchDTO.getResource(), accountSearchDTO.getIsMustCorp());
		String accountType = accountSearchDTO.getAccountType();
		//校验参数是否正确
		if (EmptyUtils.isBlank(accountType)) {
			throw new ParameterException(AccountGrantMessageEnum.SYS_ACCOUNT_TYPE_NOT_CORRECT);
		}
		String corpId = accountSearchDTO.getCorpId();
		List<AccountDTO> list = new ArrayList<AccountDTO>();
		//单位需要必填且单位为空的情况下，直接不查询账号
		if (CommonConstant.YES.equals(accountSearchDTO.getIsMustCorp()) && StringUtils.isBlank(accountSearchDTO.getCorpId())) {
			return list;
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			// 获取资源下的单位ID
			List<String> userCorpIds = new ArrayList<String>();
			if (!EmptyUtils.isBlank(corpId)) {
				userCorpIds.addAll(Arrays.asList(corpId.split(CommonConstant.COMMA_SPLIT)));
			} else {
				if (!EmptyUtils.isBlank(accountSearchDTO.getResource())) {
					userCorpIds = corpService.getUserCorpIds(accountSearchDTO.getResource(), accountSearchDTO.getUserId());
				}
			}
			logger.info("AccountGrantService.getListBy()：有权限资源的用户单位集合：{}", gson.toJson(userCorpIds));
			//权限控制类型  1-单位级 2-账号级
			String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
			logger.info("AccountGrantService.getListBy()：权限控制类型：{}（1-单位级，2-账号级）。", controlType);
			if (CommonConstant.YES.equals(controlType)) {
				String joinSql = "";
				if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
						|| AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
					if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
						// 仅查询内部账户
						joinSql = " and t.pool_sign = '0' ";
					} else {
						// 仅查询内部资金池账户
						joinSql = " and t.pool_sign = '1' ";
					}
					List<FcAccDTO> listFcAcc = innerAccountService.getFcAccByCorpIdList(userCorpIds, joinSql, accountSearchDTO.getContainDelBankAcc());
					if (!EmptyUtils.isEmptyList(listFcAcc)) {
						list = copyFcAccDTOList(listFcAcc);
					}
				} else {
					accountSearchDTO.setCorpGrantList(userCorpIds);
					List<BtBankAccDTO> listBankAcc = btBankAccService.getBankAccByAccountDTO(accountSearchDTO);
					if (!EmptyUtils.isEmptyList(listBankAcc)) {
						List<String> bankAccs = new ArrayList<>();
						List<String> curIds = new ArrayList<>();
						for (BtBankAccDTO bankAccVO : listBankAcc) {
							bankAccs.add(bankAccVO.getBankAcc());
							curIds.add(bankAccVO.getCurId());
						}
						//根据参数设置判断是否需要查询账户余额 //TODO屏蔽
						if (accountSearchDTO.getSearchBal() != null && accountSearchDTO.getSearchBal()) {
							//查询账户余额 //TODO
							List<BtBankAccBalVO> accBalList = bisAccBalService.queryBankAccBalanceById(bankAccs, curIds);
							Map<String, BigDecimal> accBalMap = new HashMap<>();
							for (BtBankAccBalVO accBalVO : accBalList) {
								String bankAcc = accBalVO.getBankAcc();
								String curId = accBalVO.getCurId();
								BigDecimal bal = accBalVO.getBal();
								accBalMap.put(bankAcc + "_" + curId, bal);
							}

							for (BtBankAccDTO bankAccVO : listBankAcc) {
								String bankAcc = bankAccVO.getBankAcc();
								String curId = bankAccVO.getCurId();
								BigDecimal bal = accBalMap.get(bankAcc + "_" + curId);
								if (null == bal) {
									bal = BigDecimal.ZERO;
								}
								bankAccVO.setBalance(bal);
							}
						}
						list = copyBtBankAccDTOList(listBankAcc, accountSearchDTO);
					}
				}
			} else {
				// 查询单位权限下的账户
				List<String> accountGrantList = null;
				// 账号为空时，是否忽略授权
				boolean ignorePermission = EmptyUtils.isBlank(accountSearchDTO.getPermission()) && accountSearchDTO.getIgnorePermission();
				if (!ignorePermission) {
					accountGrantList = accountGrantService.listByMultiVal(accountSearchDTO.getUserId(), userCorpIds, accountType, accountSearchDTO.getPermission());
				}
				if (EmptyUtils.isEmptyList(accountGrantList) && !ignorePermission) {
					return list;
				}
				if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
						|| AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
					List<FcAccDTO> listFcAcc = innerAccountService.listFcAccInfo(accountGrantList, accountSearchDTO.getContainDelBankAcc());
					if (!EmptyUtils.isEmptyList(listFcAcc)) {
						list = copyFcAccDTOList(listFcAcc);
					}
				} else {
//					accountSearchDTO.setAccountGrantList(accountGrantList);
					if (accountSearchDTO.getContainDelBankAcc() == null) {
						accountSearchDTO.setContainDelBankAcc(true);
					}
					List<BtBankAccDTO> listBankAcc = btBankAccService.getBankAccByAccountDTO(accountSearchDTO);
					if (!EmptyUtils.isEmptyList(listBankAcc)) {
						list = copyBtBankAccDTOList(listBankAcc, accountSearchDTO);
					}
//                    list = getBankAccByAccountDTO(accountSearchDTO);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
			ExceptionStackUtils.getStack(e);
			return null;
		}
		logger.info("结束执行AccountGrantService.getListBy()方法，返回账号集合：{}", gson.toJson(list));
		return list;
	}

	/**
	 * 将FcAccDTO集合cope到AccountDTO集合
	 * zuoy 2017-07-13
	 *
	 * @param listFcAcc 内部账户集合
	 * @return
	 */
	public List<AccountDTO> copyFcAccDTOList(List<FcAccDTO> listFcAcc) {
		List<AccountDTO> list = new ArrayList<AccountDTO>();
		for (FcAccDTO fcAcc : listFcAcc) {
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.setBankAccId(fcAcc.getId());
			accountDTO.setCorpId(fcAcc.getCorpId());
			accountDTO.setCorpName(fcAcc.getCorpName());
			accountDTO.setBankAcc(fcAcc.getBlnctrAcc());
			accountDTO.setAccName(fcAcc.getAccName());
			accountDTO.setBankName(fcAcc.getCorpName());
			accountDTO.setBankCode(fcAcc.getBankCode());
			accountDTO.setCurId(fcAcc.getCurId());
			accountDTO.setCurName(fcAcc.getCurName());
			accountDTO.setFcBalance(fcAcc.getFcBalance());
			list.add(accountDTO);
		}
		return list;
	}

	/**
	 * 将BtBankAccDTO集合cope到AccountDTO集合
	 * zuoy 2017-07-13
	 *
	 * @param listBankAcc      银行账户集合
	 * @param accountSearchDTO
	 * @return
	 */
	public List<AccountDTO> copyBtBankAccDTOList(List<BtBankAccDTO> listBankAcc, AccountSearchDTO accountSearchDTO) {
		List<AccountDTO> list = new ArrayList<>();
		Map<String, BtBankAccDTO> parentBankAccMap = new HashMap<>();
		if (!EmptyUtils.isEmpty(accountSearchDTO.getContainParent()) && accountSearchDTO.getContainParent()) {
			//获取所有的父账号集合
			List<BtBankAccDTO> parentBankAccList = btBankAccService.getAllParentBankAcc();
			for (BtBankAccDTO dto : parentBankAccList) {
				parentBankAccMap.put(dto.getId(), dto);
			}
		}
		for (BtBankAccDTO bankAcc : listBankAcc) {
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.setBankAccId(bankAcc.getId());
			accountDTO.setCorpId(bankAcc.getCorpId());
			accountDTO.setCorpName(bankAcc.getCorpName());
			accountDTO.setBankAcc(bankAcc.getBankAcc());
			accountDTO.setAccName(bankAcc.getAccName());
			accountDTO.setBankName(bankAcc.getBankName());
			accountDTO.setBankCode(bankAcc.getBankCode());
			accountDTO.setCurId(bankAcc.getCurId());
			accountDTO.setCurName(bankAcc.getCurName());
			accountDTO.setBifCode(bankAcc.getBifCode());
			accountDTO.setProv(bankAcc.getProv());
			accountDTO.setProvName(bankAcc.getProvName());
			accountDTO.setCity(bankAcc.getCity());
			accountDTO.setCityName(bankAcc.getCityName());
			accountDTO.setCirculationMode(bankAcc.getCirculationMode());
			accountDTO.setBalance(bankAcc.getBalance());
			accountDTO.setFcBalance(bankAcc.getFcBalance());
			accountDTO.setCurEnglishCode(bankAcc.getCurEnglishCode());
			accountDTO.setBankTypeId(bankAcc.getBankTypeId());
			accountDTO.setBankTypeName(bankAcc.getBankTypeName());
			// containParent参数为true时，加载父账户信息
			if (!EmptyUtils.isEmpty(accountSearchDTO.getContainParent()) && accountSearchDTO.getContainParent()) {
//                BtBankAccDTO parentBankAcc = btBankAccService.getParentAccByAccId(bankAcc.getId(), bankAcc.getCurId());
//                if (EmptyUtils.isEmpty(parentBankAcc)) {
//                	continue;
//				}
				BtBankAccDTO parentBankAcc = parentBankAccMap.get(bankAcc.getPrentAccId());
				if (!EmptyUtils.isEmpty(parentBankAcc)) {
					AccountDTO parentAccountDTO = new AccountDTO();
					parentAccountDTO.setBankAccId(parentBankAcc.getId());
					parentAccountDTO.setCorpId(parentBankAcc.getCorpId());
					parentAccountDTO.setCorpName(parentBankAcc.getCorpName());
					parentAccountDTO.setBankAcc(parentBankAcc.getBankAcc());
					parentAccountDTO.setAccName(parentBankAcc.getAccName());
					parentAccountDTO.setBankName(parentBankAcc.getBankName());
					parentAccountDTO.setBankCode(parentBankAcc.getBankCode());
					parentAccountDTO.setCurId(parentBankAcc.getCurId());
					parentAccountDTO.setCurName(parentBankAcc.getCurName());
					parentAccountDTO.setBifCode(parentBankAcc.getBifCode());
					parentAccountDTO.setProv(parentBankAcc.getProv());
					parentAccountDTO.setProvName(parentBankAcc.getProvName());
					parentAccountDTO.setCity(parentBankAcc.getCity());
					parentAccountDTO.setCityName(parentBankAcc.getCityName());
					parentAccountDTO.setCurEnglishCode(parentBankAcc.getCurEnglishCode());
					parentAccountDTO.setBankTypeId(parentBankAcc.getBankTypeId());
					parentAccountDTO.setBankTypeName(parentBankAcc.getBankTypeName());
//                    BigDecimal parentBalance = this.bisaccBalService.getLastByBalDate(parentBankAcc.getBankAcc(), parentBankAcc.getCurId());
					parentAccountDTO.setBalance(EmptyUtils.isEmpty(parentBankAcc.getBalance()) ? BigDecimal.ZERO : parentBankAcc.getBalance());
					accountDTO.setParentAccount(parentAccountDTO);
				}
			}
			list.add(accountDTO);
		}
		return list;
	}

	/**
	 * 查询还款状态
	 */
	public List<NisAllocationReqDTO> getRepayBisExc(NisAllocationReqDTO nisAllocationReqDTO) {
		return nisAllocationReqDao.getRepayBisExc(nisAllocationReqDTO);
	}

	public void saveImport(List<NisAllocationReqVO> list, ICurrentUser user) {
		for (NisAllocationReqVO nisAllocationReqVO : list) {
			NisAllocationReq entity = new NisAllocationReq();
			BeanUtils.copyNotNullProperties(nisAllocationReqVO, entity);
			String billNo = SequenceSingleton.getSequence(SequenceEnum.NIS_ALLOCATION_REQ);
			//设置单据号码
			entity.setBillNo(billNo);
			//设置单据状态为暂存
			entity.setStatus(AllocationConstant.STATUE_TEMP);
			//设置创建人
			entity.setCreateBy(user.getLoginName());
			//设置创建时间
			entity.setCreateDate(DateUtils.getDate());
			//设置资金池标识 默认0-非资金池
			entity.setPoolSign(AllocationConstant.POOL_SIGN_0);
			//设置单据来源 默认1-资金调拨
			entity.setBillFrom(AllocationConstant.BILLFROM_1);
			//设置跨行标识 默认0-同行
			if(EmptyUtils.isEmpty(entity.getDifBank())){
				entity.setDifBank(NetbankConstant.DIFBANK_0);
			}
			//设置同城异地标识 默认0-同城
			entity.setAreaSign(NetbankConstant.AREASIGN_0);
			//保存单据
			this.saveEntity(entity);

			//生成日志
			nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, "", NisMessageConstant.BUS_TYPE_ALLOCATION, user);
		}
	}

	@Override
	public ExcelVerifyHanlderResult verifyHandler(NisAllocationReqVO obj, Object... user) {
		return null;
	}

	@Override
	public void verifyHandler(List<NisAllocationReqVO> list, Object... user) {
		if (!EmptyUtils.isEmptyList(list)) {
			//付款账号集合
			HashMap<String, BtBankAccDTO> payerAccMap = new HashMap<>();
			//收款账号集合
			HashMap<String, BtBankAccDTO> payeeAccMap = new HashMap<>();
			//获取有效币种
			Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.listCurrencyNameMap();
			String userId = "";
			if (user[0] != null) {
				ICurrentUser userInfo = (ICurrentUser) user[0];
				userId = userInfo.getId();
			}
			//获取所有单位
			List<CorpDTO> corpDTOList = corpService.listEffectiveCorp();
			//根据单位id获取单位信息
			Map<String, CorpDTO> corpMap = corpDTOList.stream().collect(Collectors.toMap(CorpDTO::getName, Function.identity()));

			String typeName = NetbankConstant.NIS_ALLOCATION_REQ_TYPE_NAME_12;
			for (NisAllocationReqVO nisAllocationReqVO : list) {
				if(EmptyUtils.isEmpty(nisAllocationReqVO.getPurpose())){
					nisAllocationReqVO.setPurpose("资金下拨");
				}
				List<String> msgList = new ArrayList<>();
				//校验业务类型
				if (StringUtils.isBlank(nisAllocationReqVO.getTypeName())) {
					msgList.add("业务类型不能为空");
				} else if (typeName.equals(nisAllocationReqVO.getTypeName())) {
					nisAllocationReqVO.setType(NetbankConstant.NIS_ALLOCATION_REQ_TYPE_12);
				} else {
					msgList.add("业务类型不是请款");
				}
				//获取付款单位信息
				CorpDTO corp = corpMap.get(nisAllocationReqVO.getCorpName());
				//校验付款单位是否为空
				if (EmptyUtils.isBlank(nisAllocationReqVO.getCorpName())) {
					msgList.add("单位名称不能为空");
				} else if (null == corp) {//校验是否有付款单位权限
					msgList.add("单位名称系统不存在");
				} else {
					//设置付款单位id
					nisAllocationReqVO.setCorpId(corp.getId());
				}
				//校验币别
				BtCurrencyDTO btCurrencyDTO = currencyMap.get(nisAllocationReqVO.getCurName());
				//币别是否为空
				if (EmptyUtils.isBlank(nisAllocationReqVO.getCurName())) {
					msgList.add("币别不能为空");
				} else if (btCurrencyDTO == null) {//币别是否存在
					msgList.add("不是有效币别");
				} else {
					nisAllocationReqVO.setCurId(btCurrencyDTO.getId());
				}
				//校验付款账号
				//获取付款账号
				BtBankAccDTO payerAcc = null;
				if (null != corp) {
					payerAcc = payerAccMap.get(corp.getId() + nisAllocationReqVO.getPayerAcc());
					if (payerAcc == null) {
						payerAcc = btBankAccService.getBankAccByBankAccAndCurId(nisAllocationReqVO.getPayerAcc(),nisAllocationReqVO.getCurId());
						payerAccMap.put(corp.getId() + nisAllocationReqVO.getPayerAcc(), payerAcc);
					}
					if (EmptyUtils.isBlank(nisAllocationReqVO.getPayerAcc())) {
						msgList.add("付款账号不能为空");
					} else if (EmptyUtils.isEmpty(payerAcc)) {
						msgList.add("付款账号不存在");
					} else {
						nisAllocationReqVO.setPayerAccId(payerAcc.getId());
						nisAllocationReqVO.setPayerAccName(payerAcc.getAccName());
					}
				}
				//校验收款账号
				//获取收款账号
				BtBankAccDTO payeeAcc = payeeAccMap.get(nisAllocationReqVO.getPayeeAcc());
				if (payeeAcc == null) {
					payeeAcc = btBankAccService.getBankAccByBankAccAndCurId(nisAllocationReqVO.getPayeeAcc(), nisAllocationReqVO.getCurId());
					payeeAccMap.put(nisAllocationReqVO.getPayeeAcc(), payeeAcc);
				}
				if (EmptyUtils.isBlank(nisAllocationReqVO.getPayeeAcc())) {
					msgList.add("收款账号不能为空");
				} else if (EmptyUtils.isEmpty(payeeAcc)) {
					msgList.add("收款账号不存在");
				} else {
					nisAllocationReqVO.setPayeeAccId(payeeAcc.getId());
					nisAllocationReqVO.setPayeeAccName(payeeAcc.getAccName());
					nisAllocationReqVO.setPayeeCounterAcc(payeeAcc.getCounterAccId());
				}
				//判断跨行标志
				if (!EmptyUtils.isEmpty(payeeAcc) && !EmptyUtils.isEmpty(payeeAcc.getBankCode())
					&& !EmptyUtils.isEmpty(payerAcc) && !EmptyUtils.isEmpty(payerAcc.getBankCode())) {
					String payeepri = payeeAcc.getBankCode().substring(0, 3);
					String payerpri = payerAcc.getBankCode().substring(0, 3);
					String difbank = "";
					if (payeepri.equals(payerpri)) {
						if ("313".equals(payeepri) || "402".equals(payeepri) || "314".equals(payeepri)) {
							if (StringUtils.isNotBlank(payerAcc.getBankTypeId())
									&& StringUtils.isNotBlank(payeeAcc.getBankTypeId())
									&& payeeAcc.getBankTypeId().indexOf(payerAcc.getBankTypeId()) != -1) {
								difbank = NetbankConstant.DIFBANK_0;
							} else {
								difbank = NetbankConstant.DIFBANK_1;
							}
						} else {
							difbank = NetbankConstant.DIFBANK_0;
						}
					} else {
						difbank = NetbankConstant.DIFBANK_1;
					}
					if(EmptyUtils.isNotEmpty(difbank)) {
						nisAllocationReqVO.setDifBank(difbank);
					}
				}
				//校验期望日期
				if (nisAllocationReqVO.getWishPayday() == null) {
					msgList.add("期望支付日期不能为空");
				} else {
					if (nisAllocationReqVO.getWishPayday().compareTo(DateUtils.getDateYMD()) == -1) {
						msgList.add("期望支付日期不能小于当前日期");
					}
				}
				String isQuickName1 = NetbankConstant.IS_QUICK_NAME_1;
				String isQuickName2 = NetbankConstant.IS_QUICK_NAME_0;
				//校验是否加急
				if (StringUtils.isBlank(nisAllocationReqVO.getIsQuickName())) {
					msgList.add("是否加急不能为空");
				} else if (isQuickName1.equals(nisAllocationReqVO.getIsQuickName()) || isQuickName2.equals(nisAllocationReqVO.getIsQuickName())) {
					if (isQuickName1.equals(nisAllocationReqVO.getIsQuickName())) {
						nisAllocationReqVO.setIsQuick(NetbankConstant.IS_QUICK_1);
					} else {
						nisAllocationReqVO.setIsQuick(NetbankConstant.IS_QUICK_0);
					}
				} else {
					msgList.add("加急标志不正确");
				}
				//校验必填项目
				if (EmptyUtils.isBlank(nisAllocationReqVO.getRmk())) {
					msgList.add("必填项目不能为空");
				}

				//校验必填项目
				if (nisAllocationReqVO.getMoney() == null || nisAllocationReqVO.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
					msgList.add("金额不能为空且必须是大于0的数字");
				}

				nisAllocationReqVO.addErrorMsg(msgList);
			}
		}
	}

	/**
	 * 判断银行账号是否授权
	 *
	 * @param userAccountCode
	 * @param acc
	 * @param curId
	 */
	public BtBankAccDTO isExistBankAcc(String userAccountCode, String acc, String curId) {
		if (StringUtils.isBlank(userAccountCode)) {
			return null;
		}
		List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
		String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
		for (String corpId : userAccountCodes) {
			//获取转账权限的银行在线账号
			List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
			//获取转账权限的银行离线账号
			List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
			if (!EmptyUtils.isEmptyList(listOnlineBankAcc)) {
				listAcc.addAll(listOnlineBankAcc);
			}
			if (!EmptyUtils.isEmptyList(listOfflineBankAcc)) {
				listAcc.addAll(listOfflineBankAcc);
			}
		}
		BtBankAccDTO btBankAccDTO = null;
		if (!EmptyUtils.isEmptyList(listAcc)) {
			for (BtBankAccDTO bankAcc : listAcc) {
				if (bankAcc.getBankAcc().equals(acc) && bankAcc.getCurId().equals(curId)) {
					return bankAcc;
				}
			}
		}
		return btBankAccDTO;
	}

	/**
	 * 根据月份，资金调拨类型获取调拨单据信息
	 *
	 * @param queryDate 月份
	 * @param types     业务类型集合
	 * @return
	 */
	public List<NisAllocationReqVO> listNisAllocationReq(Date queryDate, List<Integer> types) {

		return nisAllocationReqDao.listNisAllocationReq(queryDate, types);
	}

	/**
	 * 根据id获取当前日期的待审批数（用于定时任务请款待审批）
	 * @param ids
	 * @return
	 */
	public int countByIds(List<String> ids) {
		return nisAllocationReqDao.countByIds(ids);
	}

	/**
	 * 根据id获取待审批数（用于定时任务请款待审批）
	 * @param ids
	 * @return
	 */
	public int countByIdsNoDate(List<String> ids) {
		return nisAllocationReqDao.countByIdsNoDate(ids);
	}

    public List<NisallocationPosApproveVO> queryPosApproveList() {
		return nisAllocationReqDao.queryPosApproveList();
    }

	public HSSFWorkbook createExcelContent(List<NisAllocationReqListDTO> list, String fileName) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(fileName);
		HSSFCellStyle style = createExcelStyle(workbook);
		HSSFCellStyle headerStyle = createExcelHeaderStyle(workbook);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);       // 水平居中
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
		HSSFRow row0 = sheet.createRow(0);
		//HSSFCell cell00 = row0.createCell(0);
		// 给单元格设置样式
//		cell00.setCellStyle(headerStyle);

		for (int i = 0; i <= 15; i++) {
			HSSFCell cell = row0.createCell(i);
			if(i == 0){
				cell.setCellValue(fileName);
			}
			row0.getCell(i).setCellStyle(headerStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(
				0, // 开始行
				0, // 结束行
				0, // 开始列
				15  // 结束列
		));

		int rowIndex = 1;

		HSSFRow row1 = sheet.createRow(rowIndex);
		HSSFCell cell0 = row1.createCell(0);
		cell0.setCellValue("单据号码");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				0, // 开始列
				0  // 结束列
		));
		cell0.setCellStyle(headerStyle);
		HSSFCell cell1 = row1.createCell(1);
		cell1.setCellValue("付款账号");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				1, // 结束行
				1, // 开始列
				4  // 结束列
		));
		cell1.setCellStyle(headerStyle);
		row1.createCell(2).setCellStyle(headerStyle);
		row1.createCell(3).setCellStyle(headerStyle);
		row1.createCell(4).setCellStyle(headerStyle);
		HSSFCell cell2 = row1.createCell(5);
		cell2.setCellValue("收款账号");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				1, // 结束行
				5, // 开始列
				8  // 结束列
		));
		cell2.setCellStyle(headerStyle);
		HSSFCell cell3 = row1.createCell(9);
		row1.createCell(6).setCellStyle(headerStyle);
		row1.createCell(7).setCellStyle(headerStyle);
		row1.createCell(8).setCellStyle(headerStyle);
		cell3.setCellValue("币别");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				9, // 开始列
				9  // 结束列
		));
		cell3.setCellStyle(headerStyle);
		HSSFCell cell4 = row1.createCell(10);
		cell4.setCellValue("期望支付日期");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				10, // 开始列
				10  // 结束列
		));
		cell4.setCellStyle(headerStyle);
		HSSFCell cell5 = row1.createCell(11);
		cell5.setCellValue("用途");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				11, // 开始列
				11  // 结束列
		));
		cell5.setCellStyle(headerStyle);
		HSSFCell cell6 = row1.createCell(12);
		cell6.setCellValue("下拨金额");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				12, // 开始列
				12  // 结束列
		));
		cell6.setCellStyle(headerStyle);
		HSSFCell cell7 = row1.createCell(13);
		cell7.setCellValue("制单人");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				13, // 开始列
				13  // 结束列
		));
		cell7.setCellStyle(headerStyle);
		HSSFCell cell8 = row1.createCell(14);
		cell8.setCellValue("制单日期");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				14, // 开始列
				14  // 结束列
		));
		cell8.setCellStyle(headerStyle);
		HSSFCell cell9 = row1.createCell(15);
		cell9.setCellValue("状态");
		sheet.addMergedRegion(new CellRangeAddress(
				1, // 开始行
				2, // 结束行
				15, // 开始列
				15  // 结束列
		));
		cell9.setCellStyle(headerStyle);

		rowIndex++;
		HSSFRow row2 = sheet.createRow(rowIndex);
		row2.createCell(0).setCellStyle(headerStyle);
		HSSFCell cell10 = row2.createCell(1);
		cell10.setCellValue("账户");
		cell10.setCellStyle(headerStyle);
		HSSFCell cell11 = row2.createCell(2);
		cell11.setCellValue("单位");
		cell11.setCellStyle(headerStyle);
		HSSFCell cell12 = row2.createCell(3);
		cell12.setCellValue("户名");
		cell12.setCellStyle(headerStyle);
		HSSFCell cell13 = row2.createCell(4);
		cell13.setCellValue("开户行");
		cell13.setCellStyle(headerStyle);
		HSSFCell cell14 = row2.createCell(5);
		cell14.setCellValue("账户");
		cell14.setCellStyle(headerStyle);
		HSSFCell cell15 = row2.createCell(6);
		cell15.setCellValue("单位");
		cell15.setCellStyle(headerStyle);
		HSSFCell cell16 = row2.createCell(7);
		cell16.setCellValue("户名");
		cell16.setCellStyle(headerStyle);
		HSSFCell cell17 = row2.createCell(8);
		cell17.setCellValue("开户行");
		cell17.setCellStyle(headerStyle);
		for(int i = 9; i <= 15; i++){
			row2.createCell(i);
			row2.getCell(i).setCellStyle(headerStyle);
		}

		rowIndex++;
		for (NisAllocationReqListDTO dto : list) {
			HSSFRow dataRow = sheet.createRow(rowIndex);

			dataRow.createCell(0).setCellValue(dto.getBillNo() != null ? dto.getBillNo() : "");
			dataRow.createCell(1).setCellValue(dto.getPayerAcc() != null ? dto.getPayerAcc() : "");
			dataRow.createCell(2).setCellValue(dto.getPayerCorpName() != null ? dto.getPayerCorpName() : "");
			dataRow.createCell(3).setCellValue(dto.getPayerAccName() != null ? dto.getPayerAccName() : "");
			dataRow.createCell(4).setCellValue(dto.getPayerBankName() != null ? dto.getPayerBankName() : "");
			dataRow.createCell(5).setCellValue(dto.getPayeeAcc() != null ? dto.getPayeeAcc() : "");
			dataRow.createCell(6).setCellValue(dto.getPayeeCorpName() != null ? dto.getPayeeCorpName() : "");
			dataRow.createCell(7).setCellValue(dto.getPayeeAccName() != null ? dto.getPayeeAccName() : "");
			dataRow.createCell(8).setCellValue(dto.getPayeeBankName() != null ? dto.getPayeeBankName() : "");
			dataRow.createCell(9).setCellValue(dto.getCurName() != null ? dto.getCurName() : "");
			dataRow.createCell(10).setCellValue(dto.getWishPayday() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(dto.getWishPayday()) : "");
			dataRow.createCell(11).setCellValue(dto.getPurpose() != null ? dto.getPurpose() : "");
			dataRow.createCell(12).setCellValue(dto.getMoney() != null ? dto.getMoney().toString() : "");
			dataRow.createCell(13).setCellValue(dto.getCreateByName() != null ? dto.getCreateByName() : "");
			dataRow.createCell(14).setCellValue(dto.getCreateDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(dto.getCreateDate()) : "");
			dataRow.createCell(15).setCellValue(dto.getStatusName() != null ? dto.getStatusName() : "");
			for (int i = 0; i <= 15; i++) {
				dataRow.getCell(i).setCellStyle(style);
			}
			rowIndex++;
		}

		for (int i = 0; i <= 15; i++) {
			sheet.setColumnWidth(i, 4000);
		}

		return workbook;
	}

	private HSSFCellStyle createExcelStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	private HSSFCellStyle createExcelHeaderStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return style;
	}

	public boolean isIE(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		return userAgent != null && (userAgent.contains("MSIE") || userAgent.contains("Trident"));
	}
}
