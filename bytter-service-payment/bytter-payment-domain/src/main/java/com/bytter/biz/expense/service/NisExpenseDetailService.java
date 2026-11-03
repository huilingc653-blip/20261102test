package com.bytter.biz.expense.service;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.expense.dao.NisExpenseDetailDao;
import com.bytter.biz.expense.entity.NisBusinessType;
import com.bytter.biz.expense.entity.NisExpenseDetail;
import com.bytter.biz.expense.entity.NisOperationType;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.netbank.entity.NisPersonnelDetail;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.service.NisPersonnelDetailService;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 费用报销表
 * @date 2017-05-31 16:04:01
 */
@WorkFlow(NisWorkFlowConstant.NIS_EXPENSEDETAIL)
@Service("nisExpenseDetailService")
@DubboService(interfaceClass = IWorkFlowListener.class,group = NisWorkFlowConstant.NIS_EXPENSEDETAIL)
@Transactional
public class NisExpenseDetailService extends BaseService<NisExpenseDetail, String> implements IWorkFlowListener, IExcelVerifyHandler<NisExpenseDetailVO>, IWorkFlowRevokeListener, ICorpDisableVerifyService {
	@Autowired
	private NisExpenseDetailDao nisExpenseDetailDao;
	@Autowired
	private NisMessageService nisMessageService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IInputBankInfoService inputBankInfoService;
	@Autowired
	private NisBusinessTypeService nisBusinessTypeService;
	@Autowired
	private NisOperationTypeService nisOperationTypeService;
	@Autowired
    private IWorkFlowService workFlowServiceImpl;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@Autowired
	private NisPersonnelDetailService nisPersonnelDetailService;
	@DubboReference(check = false)
	private IRegionService regionService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFbsItemService fbsItemService;
	@DubboReference(check = false)
	private IFbsBudgetService fbsBudgetService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBisExcService bisExcService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisExpenseDetail entity) {
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
	public void save(NisExpenseDetail entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisExpenseDetail entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(NisExpenseDetail entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<NisExpenseDetail, String> getEntityDao() {
		return nisExpenseDetailDao;
	}
	/**
	 * 查询报销单据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<NisExpenseDetailVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String resource = "";
		String opType = (String) dataTablesInput.getFormdata().get("opType");
		if ("touchQuery".equals(opType)) {
			//费用报销制单
			sql.append(" and (n.create_by ='").append(user.getLoginName()).append("' or n.create_by is null)");
			resource = com.bytter.biz.bdlink.api.constant.ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_TOUCHQUERY;
		} else if ("query".equals(opType)) {
			//查询
			return this.getDataTablesQuery(dataTablesInput, user);
		} else {
			resource = com.bytter.biz.bdlink.api.constant.ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_COLLECTQUERY;
		}
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput<NisExpenseDetailVO> output =  nisExpenseDetailDao.getDataTablesUnion(dataTablesInput, sql.toString());
		output = this.setJoinField(output, opType);
		return output;
	}
	/**
	 * 费用报销查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
		String opType = (String) dataTablesInput.getFormdata().get("opType");
		StringBuilder sql = new StringBuilder();
		//过滤掉删除状态的付款单
		sql.append(" and n.status<>"+ com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_DEL);
		String resource = com.bytter.biz.bdlink.api.constant.ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_QUERY;
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput output = nisExpenseDetailDao.getDataTablesQuery(dataTablesInput, sql.toString());
		output = this.setJoinField(output, opType);
		return output;
	}
	/**
	 * 设置费用报销的关联字段
	 * @param output
	 * @param opType
	 * @return
	 */
	public DataTablesOutput setJoinField(DataTablesOutput output, String opType) {
		if(!EmptyUtils.isEmpty(output.getData())) {
			List<NisExpenseDetailVO> list = output.getData();
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			Map<String, BisBifInitDTO> bifCodeMap = bisBifInitService.getBisBifInitMap();
			Map<String, RegionDTO> regionMap = regionService.getRegionMap();
			List<String> billIds = new ArrayList<>();
			for (NisExpenseDetailVO vo:list) {
				if (corpMap.containsKey(vo.getCorpId())) {
					vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
				}
				if (bifCodeMap.containsKey(vo.getBifCode())) {
					vo.setBifName(bifCodeMap.get(vo.getBifCode()).getName());
				}
				if (regionMap.containsKey(vo.getProv())) {
					vo.setProvName(regionMap.get(vo.getProv()).getName());
				}
				if (regionMap.containsKey(vo.getCity())) {
					vo.setCityName(regionMap.get(vo.getCity()).getName());
				}
				billIds.add(vo.getBillId());
			}
		}
		return output;
	}
	/**
	 * 根据ID获取报销信息
	 *
	 * @param id
	 * @return
	 */
	public NisExpenseDetailVO getByIdUnion(String id) {
		NisExpenseDetailVO nisExpenseDetailVO = nisExpenseDetailDao.getByIdUnion(id);
		if (nisExpenseDetailVO != null) {
			CorpDTO corpDTO = corpService.get(nisExpenseDetailVO.getCorpId());
			nisExpenseDetailVO.setCorpName(corpDTO == null ? "" : corpDTO.getName());
			if (!StringUtil.isBlank(nisExpenseDetailVO.getBifCode())) {
				BisBifInitDTO bisBifInitDTO = bisBifInitService.getObjectById(nisExpenseDetailVO.getBifCode());
				nisExpenseDetailVO.setBifName(bisBifInitDTO == null ? "" : bisBifInitDTO.getName());
			}
			if (!StringUtil.isBlank(nisExpenseDetailVO.getProv())) {
				RegionDTO prov = regionService.getById(nisExpenseDetailVO.getProv());
				nisExpenseDetailVO.setProvName(prov == null ? "" : prov.getName());
			}
			if (!StringUtil.isBlank(nisExpenseDetailVO.getCity())) {
				RegionDTO city = regionService.getById(nisExpenseDetailVO.getCity());
				nisExpenseDetailVO.setCityName(city == null ? "" : city.getName());
			}
		}
		return nisExpenseDetailVO;
	}
	/**
	 * 根据付款Id获取费用报销明细列表
	 *
	 * @param billId
	 * @return
	 */
	public List<NisExpenseDetailVO> listByBillId(String billId) {
		return nisExpenseDetailDao.listByBillId(billId);
	}
	/**
	 * 根据付款Id获取交易失败的费用报销明细列表
	 *
	 * @param billId
	 * @return
	 */
	public List<NisExpenseDetailVO> listByBillIdFailure(String billId) {
		return nisExpenseDetailDao.listByBillIdFailure(billId);
	}
	/**
	 * 根据付款Id获取等待结果的费用报销明细列表
	 *
	 * @param billId
	 * @return
	 */
	public List<NisExpenseDetailVO> listByBillIdWaitResult(String billId) {
		return nisExpenseDetailDao.listByBillIdWaitResult(billId);
	}
	/**
	 * 新增费用报销单
	 *
	 * @param nisExpenseDetail
	 */
	public void saveExpenseDetail(NisExpenseDetailVO nisExpenseDetail, ICurrentUser user) {
		NisExpenseDetail entity = new NisExpenseDetail();
		BeanUtils.copyNotNullProperties(nisExpenseDetail, entity);
		entity.setExpenseId(SequenceSingleton.getSequence(SequenceEnum.NIS_EXPENSE_DETAIL));
		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
		super.saveEntity(entity);
		//保存用途
		if (!StringUtils.isBlank(nisExpenseDetail.getPurpose())) {
			entity.setPurpose(nisExpenseDetail.getPurpose());
		}
		//职工信息保存
		this.saveNisPersonnelDetail(entity, user);
		//写入计划执行
		nisExpenseDetail.setId(entity.getId());
		nisExpenseDetail.setExpenseId(entity.getExpenseId());
		this.writeBudgetProcess(nisExpenseDetail, user);
		Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
		//启动审批
		if (com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
			 getStartHandler().handler(NisWorkFlowConstant.NIS_EXPENSEDETAIL, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		//生成日志
		nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
	}
	/**
	 * 修改费用报销单
	 *
	 * @param nisExpenseDetail
	 */
	public void updateExpenseDetail(NisExpenseDetailVO nisExpenseDetail, ICurrentUser user) {
		if (EmptyUtils.isBlank(nisExpenseDetail.getCreateBy())) {
			nisExpenseDetail.setCreateBy(user.getLoginName());
		}
		if (nisExpenseDetail.getCreateDate() == null) {
			nisExpenseDetail.setCreateDate(DateUtils.getDate());
		}
		nisExpenseDetail.setUpdateBy(user.getLoginName());
		nisExpenseDetail.setUpdateDate(DateUtils.getDate());
		//编辑费用报销单，控制原状态为0暂存/-1打回
		if(ExpenseConstant.STATUE_COLLECT.equals(nisExpenseDetail.getStatus())){
			Integer[] oldStatus = { ExpenseConstant.STATUE_COLLECT };
			this.updateNisExpenseDetailStatus(nisExpenseDetail, oldStatus);
		} else {
			Integer[] oldStatus = { ExpenseConstant.STATUE_TEMP, ExpenseConstant.STATUE_BACK };
			this.updateNisExpenseDetailStatus(nisExpenseDetail, oldStatus);
		}
		NisExpenseDetail entity = nisExpenseDetailDao.get(nisExpenseDetail.getId(), true);
		BeanUtils.copyNotNullProperties(nisExpenseDetail, entity);
		//保存用途
		if (!StringUtils.isBlank(nisExpenseDetail.getPurpose())) {
			entity.setPurpose(nisExpenseDetail.getPurpose());
		}
		//职工信息保存
		this.saveNisPersonnelDetail(entity, user);
		//写入计划执行
		this.writeBudgetProcess(nisExpenseDetail, user);
		Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
		//启动审批
		if (com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
			messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
			 getStartHandler().handler(NisWorkFlowConstant.NIS_EXPENSEDETAIL, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		//生成日志
		nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.EXPENSE_APPLY, entity.getId(), user.getId());
	}
	/**
	 * 打回费用报销单
	 *
	 * @param id
	 */
	public void backExpenseDetail(String id, ICurrentUser user) {
		NisExpenseDetail nisExpenseDetail = this.get(id, true);
		NisExpenseDetailVO vo = new NisExpenseDetailVO();
		BeanUtils.copyNotNullProperties(nisExpenseDetail, vo);
		//打回报销明细的同时解除跟付款单的绑定
		vo.setBillId(null);
		vo.setStatus(ExpenseConstant.STATUE_BACK);
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		//打回费用报销单，控制原状态为95审批通过/100已汇总
		Integer[] oldStatus = { ExpenseConstant.STATUE_CHECK, ExpenseConstant.STATUE_COLLECT };
		this.updateNisExpenseDetailStatus(vo, oldStatus);
		//更新资金计划执行
		fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_EXPENSE, BudgetConstant.PROCESS_SYSNAME_NIS,
				nisExpenseDetail.getExpenseId(), ExpenseConstant.STATUE_BACK, user);
		//生成日志
		nisMessageService.saveMessage(nisExpenseDetail.getId(), NisMessageConstant.MESSAGE_TYPE_COLLECT_BACK, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
		//消息提醒
		notificationService.sendToUserByLoginName(nisExpenseDetail.getId(), nisExpenseDetail.getCreateBy(), NotificationConstant.NETBANK.EXPENSE_APPLY, nisExpenseDetail);
	}
	/**
	 * 保存职工信息
	 *
	 * @param nisExpenseDetail
	 */
	public void saveNisPersonnelDetail(NisExpenseDetail nisExpenseDetail, ICurrentUser user) {
		if (StringUtils.isNotEmpty(nisExpenseDetail.getWorkId())) {
			NisPersonnelDetail personnel = nisPersonnelDetailService.get(nisExpenseDetail.getWorkId(), true);
			if (personnel != null) {
				personnel.setUserName(nisExpenseDetail.getUserName());
				personnel.setDeptName(nisExpenseDetail.getDeptName());
				personnel.setAccName(nisExpenseDetail.getAccName());
				personnel.setBankAcc(nisExpenseDetail.getBankAcc());
				personnel.setBankCode(nisExpenseDetail.getBankCode());
				personnel.setBankName(nisExpenseDetail.getBankName());
				personnel.setCardType(nisExpenseDetail.getCardType());
				personnel.setBifCode(nisExpenseDetail.getBifCode());
				personnel.setProv(nisExpenseDetail.getProv());
				personnel.setCity(nisExpenseDetail.getCity());
				personnel.setUpdateBy(user.getLoginName());
				personnel.setUpdateDate(DateUtils.getDate());
				nisPersonnelDetailService.saveOrUpdate(personnel);
			}
		}
	}
	/**
	 * 逻辑删除费用报销单
	 *
	 * @param id
	 */
	public void delExpenseDetail(String id, ICurrentUser user) {
		NisExpenseDetail nisExpenseDetail = this.get(id, true);
		NisExpenseDetailVO vo = new NisExpenseDetailVO();
		BeanUtils.copyNotNullProperties(nisExpenseDetail, vo);
		vo.setStatus(ExpenseConstant.STATUE_DEL);
		vo.setUpdateBy(user.getLoginName());
		vo.setUpdateDate(DateUtils.getDate());
		// 删除费用报销单，控制0暂存/-1打回/100已汇总（单笔对私时更新）
		Integer[] oldStatus = { ExpenseConstant.STATUE_TEMP, ExpenseConstant.STATUE_BACK, ExpenseConstant.STATUE_COLLECT };
		this.updateNisExpenseDetailStatus(vo, oldStatus);
		//删除资金计划
		fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_EXPENSE, BudgetConstant.PROCESS_SYSNAME_NIS, nisExpenseDetail.getExpenseId());
		//生成日志
		nisMessageService.saveMessage(nisExpenseDetail.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.EXPENSE_APPLY, nisExpenseDetail.getId(), user.getId());
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		NisExpenseDetail nisExpenseDetail = this.get(workFlowResult.getBusinessKey(), true);

		// 删除费用报销单，控制0暂存/-1打回
		Integer[] oldStatus = { com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK };
		if(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK==nisExpenseDetail.getStatus()) {//当状态是待审批状态
			Integer status = workFlowResult.isPass() ? ExpenseConstant.STATUE_CHECK : ExpenseConstant.STATUE_BACK;

			NisExpenseDetailVO vo = new NisExpenseDetailVO();
			BeanUtils.copyNotNullProperties(nisExpenseDetail, vo);
			vo.setStatus(status);
			vo.setUpdateBy(workFlowResult.getUser().getLoginName());
			vo.setUpdateDate(DateUtils.getDate());

			this.updateNisExpenseDetailStatus(vo, oldStatus);
			//更新资金计划执行
			fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_EXPENSE, BudgetConstant.PROCESS_SYSNAME_NIS,
					nisExpenseDetail.getExpenseId(), status, user);
		}
		//生成日志
		Integer messageType = workFlowResult.isPass() ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		nisMessageService.saveMessage(nisExpenseDetail.getId(), messageType, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
	}
	/**
	 * 获取审批列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(NisWorkFlowConstant.NIS_EXPENSEDETAIL, dataTablesInput);
		List<String> businessIdList = workFlowBusiness.getBusinessIdList();
		String businessSql = "";
		DataTablesOutput dp =new DataTablesOutput ();
		if(!EmptyUtils.isEmpty(businessIdList)){
			businessSql = SqlUtil.getInSql(businessIdList, "n.id", true);
			dp = this.nisExpenseDetailDao.dataTableApprove(dataTablesInput, businessSql);
			dp = this.setJoinField(dp, "");
			return workFlowBusiness.merge(dp);
		}
		return dp;
	}
	/**
	 * 获取费用报销信息
	 *
	 * @param id
	 * @return
	 */
	public NisExpenseDetailVO getByUnion(String id) {
		return this.nisExpenseDetailDao.getByIdUnion(id);
	}
	/**
	 * 写入计划执行
	 *
	 * @param nisExpenseDetail
	 */
	private void writeBudgetProcess(NisExpenseDetailVO nisExpenseDetail, ICurrentUser user) {
		if (!EmptyUtils.isEmptyArray(nisExpenseDetail.getItemCode())) {
			//写入之前先删除
			fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_EXPENSE, BudgetConstant.PROCESS_SYSNAME_NIS, nisExpenseDetail.getExpenseId());
			List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
			for (int i = 0; i < nisExpenseDetail.getItemCode().length; i++) {
				FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
				fbsBudgetProcessDTO.setCreateBy(nisExpenseDetail.getCreateBy());
				fbsBudgetProcessDTO.setCreateDate(nisExpenseDetail.getUpdateDate());
				fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
				fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
				fbsBudgetProcessDTO.setStatus(nisExpenseDetail.getStatus());
				if (ExpenseConstant.STATUE_COLLECT.intValue() == nisExpenseDetail.getStatus().intValue()) {
					fbsBudgetProcessDTO.setStatus(ExpenseConstant.STATUE_CHECK);
				}
				fbsBudgetProcessDTO.setBillNo(nisExpenseDetail.getExpenseId());
				fbsBudgetProcessDTO.setBillId(nisExpenseDetail.getId());
				fbsBudgetProcessDTO.setItemCode(nisExpenseDetail.getItemCode()[i]);
				fbsBudgetProcessDTO.setItemName(nisExpenseDetail.getItemName()[i]);
				fbsBudgetProcessDTO.setProcessMoney(nisExpenseDetail.getProcessMoney()[i]);
				if(nisExpenseDetail.getExchangeRate()!=null) {
					fbsBudgetProcessDTO.setExchangeRate(nisExpenseDetail.getExchangeRate()[i]);
					fbsBudgetProcessDTO.setProcessCurMoney(nisExpenseDetail.getProcessCurMoney()[i]);
				}
				//获取单位的本位币
				CorpDTO corp = corpService.get(nisExpenseDetail.getCorpId());
				fbsBudgetProcessDTO.setCurId(corp.getCurId());
				fbsBudgetProcessDTO.setProcessDate(nisExpenseDetail.getExpenseDate());
				fbsBudgetProcessDTO.setCorpId(nisExpenseDetail.getCorpId());
				fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_EXPENSE);
				fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
				list.add(fbsBudgetProcessDTO);
			}
			this.fbsBudgetProcessService.writeBudgetProcess(list, user);
		} else {
			Integer status = nisExpenseDetail.getStatus();
			if (ExpenseConstant.STATUE_COLLECT.intValue() == status.intValue()) {
				status = ExpenseConstant.STATUE_CHECK;
			}
			//更新资金计划执行
			fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_EXPENSE, BudgetConstant.PROCESS_SYSNAME_NIS,
					nisExpenseDetail.getExpenseId(), status, user);
		}
	}
	/**
	 * 批量提交
	 *
	 * @param ids
	 */
	public void batchSubmitExpenseDetail(String ids, ICurrentUser user) {
		String budgetSel = configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue();
		String expFbsin = configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue();
		// 挂接资金计划且报销录入环节
		if (CommonConstant.YES.equalsIgnoreCase(budgetSel) && ExpenseConstant.EXPFBSIN_ENTER.equalsIgnoreCase(expFbsin)) {
			for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
				NisExpenseDetailVO nisExpenseDetail = this.getByIdUnion(id);
				List list = this.fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_EXPENSE, BudgetConstant.PROCESS_SYSNAME_NIS, nisExpenseDetail.getExpenseId());
				if (EmptyUtils.isEmptyList(list)) {
					throw new ParameterException(NetbankMessageEnum.NIS_EXPENSE_DETAIL_MESSAGE_EXPFBSIN_INPUT);
				}
			}
		}
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisExpenseDetailVO nisExpenseDetail = this.getByIdUnion(id);
			nisExpenseDetail.setStatus(ExpenseConstant.STATUE_WAIT_CHECK);
			this.updateExpenseDetail(nisExpenseDetail, user);
		}
	}
	/**
	 * 判断职工信息是否已使用（true - 已使用；false - 未使用）
	 *
	 * @param workId 职工ID
	 */
	public boolean isUsedPersonnel(String workId) {
		List<NisExpenseDetail> list = nisExpenseDetailDao.listBy("workId", workId);
		return EmptyUtils.isEmptyList(list) ? false : true;
	}
	/**
	 * 导入
	 *
	 * @param list
	 */
	public void saveImport(List<NisExpenseDetailVO> list, ICurrentUser user) {
		for (NisExpenseDetailVO nisExpenseDetail : list) {
			nisExpenseDetail.setBillFrom(ExpenseConstant.BILLFROM_0);
			nisExpenseDetail.setStatus(ExpenseConstant.STATUE_TEMP);
			nisExpenseDetail.setVoucherStat(com.bytter.biz.bdlink.api.constant.BdlinkConstant.PRE_SEND);
			this.saveExpenseDetail(nisExpenseDetail, user);
		}
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(NisExpenseDetailVO vo) {
		List<String> msgList = new ArrayList<>();
		// 判断单位在系统中是否存在
		CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
		if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
			msgList.add("nis.netbank.message.corp.null");
		} else {
			vo.setCorpId(corp.getId());
		}
		// 判断业务类型是否存在
		NisBusinessType businessType = this.nisBusinessTypeService.getByBusTypeName(vo.getBusTypeName());
		if (EmptyUtils.isEmpty(businessType)) {
			msgList.add("nis.netbank.message.bustype.null");
		}
		// 判断操作类型是否存在
		NisOperationType opType = this.nisOperationTypeService.getByOpTypeName(vo.getOpTypeName());
		if (EmptyUtils.isEmpty(opType)) {
			msgList.add("nis.netbank.message.optype.null");
		}
		//判断业务类型与操作类型是否一致
		if (!EmptyUtils.isEmpty(businessType) && !EmptyUtils.isEmpty(opType)) {
			if (!opType.getBusType().equals(businessType.getBusType())) {
				msgList.add("nis.netbank.message.optypinfo.mismatch");
			} else {
				vo.setBusType(businessType.getBusType());
				vo.setOpType(opType.getOpType());
			}
		}
		// 判断接口代码在系统中是否存在
		if (!EmptyUtils.isBlank(vo.getBifName())) {
			BisBifInitDTO bisBifInitDTO = bisBifInitService.getByName(vo.getBifName());
			if (EmptyUtils.isEmpty(bisBifInitDTO)) {
				msgList.add("nis.netbank.message.bisbifinit.null");
			} else {
				vo.setBifCode(bisBifInitDTO.getId());
			}
		}
		// 判断开户行在系统中是否存在
		if (!EmptyUtils.isBlank(vo.getBankCode())) {
			InputBankInfoDTO btBankDTO = this.inputBankInfoService.getByBankCodeAndImport(vo.getBankCode());
			if (EmptyUtils.isEmpty(btBankDTO)) {
				msgList.add("nis.netbank.message.bank.null");
			} else {
				vo.setBankCode(btBankDTO.getBankCode());
				vo.setBankName(btBankDTO.getBankName());
			}
		}
		//金额不能为空且必须大于0
		if (vo.getExpenseMoney() == null) {
			msgList.add("nis.netbank.message.money.null");
		} else {
			if (vo.getExpenseMoney().compareTo(BigDecimal.ZERO) <= 0) {
				msgList.add("nis.netbank.message.money.mismatch");
			}
		}
		// 判断账户类型
		if (!EmptyUtils.isBlank(vo.getCardTypeName())) {
			String cardType = dictService.getCodeByName("nisPersonnelDetail.cardType", vo.getCardTypeName());
			if (EmptyUtils.isBlank(cardType)) {
				msgList.add("nis.netbank.message.cardtype.mismatch");
			} else {
				vo.setCardType(cardType);
			}
		}
		// 判断省市在系统中是否存在
		RegionDTO provRegion = this.regionService.getByName(vo.getProvName(), true);
		if (EmptyUtils.isEmpty(provRegion)) {
			msgList.add("nis.netbank.message.provname.null");
		}
		RegionDTO cityRegion = this.regionService.getByName(vo.getCityName(), false);
		if (EmptyUtils.isEmpty(cityRegion)) {
			msgList.add("nis.netbank.message.cityname.null");
		}
		//判断省市是否一致
		if (!EmptyUtils.isEmpty(provRegion) && !EmptyUtils.isEmpty(cityRegion)) {
			if (!cityRegion.getParentId().equals(provRegion.getId())) {
				msgList.add("nis.netbank.message.cityname.mismatch");
			} else {
				vo.setProv(provRegion.getId());
				vo.setCity(cityRegion.getId());
			}
		}
		//计划科目校验
		String budgetSel = configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue();
		String expFbsin = configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue();
		if (CommonConstant.YES.equalsIgnoreCase(budgetSel) && ExpenseConstant.EXPFBSIN_ENTER.equalsIgnoreCase(expFbsin)) {
			if (EmptyUtils.isEmpty(vo.getItemCodeImp())) {
				msgList.add("nis.assign.message.itemcode.null");
			} else {
				FbsItemDTO fbsItemDTO = this.fbsItemService.getFbsItemByCode(vo.getItemCodeImp());
				if (EmptyUtils.isEmpty(fbsItemDTO)) {
					//判断计划科目是否存在
					msgList.add("nis.assign.message.bustype.itemcode.null");
				} else {
					vo.setItemCode(new String[]{fbsItemDTO.getItemCode()});
					vo.setItemName(new String[]{fbsItemDTO.getItemName()});
					vo.setProcessMoney(new BigDecimal[]{vo.getExpenseMoney()});
					FbsBudgetProcessCommonDTO dto = fbsBudgetService.getFbsBudgetProcessByItemCode(vo.getCorpId(), fbsItemDTO.getItemCode(), vo.getExpenseDate(), vo.getBillNo(), vo.getBillId());
					//计划执行科目余额控制方式
					String itemBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue();
					if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_ONESELF.equals(itemBalance)) {
						//受本科目控制
						if (vo.getExpenseMoney().doubleValue() > dto.getItemMoney().doubleValue()) {
							msgList.add("fbs.message.detailmoney.gt.itembalance");
						}
					}
					if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_SUPER.equals(itemBalance)) {
						//受上级科目控制
						if (vo.getExpenseMoney().doubleValue() > dto.getSuperItemMoney().doubleValue()) {
							msgList.add("fbs.message.detailmoney.gt.superitembalance");
						}
					}
					if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_TOP.equals(itemBalance)) {
						//受大科目控制
						if (vo.getExpenseMoney().doubleValue() > dto.getTopItemMoney().doubleValue()) {
							msgList.add("fbs.message.detailmoney.gt.topitembalance");
						}
					}
				}
			}
		}
		if (EmptyUtils.isEmptyList(msgList)) {
			return new ExcelVerifyHanlderResult(true, msgList);
		} else {
			return new ExcelVerifyHanlderResult(false, msgList);
		}
	}
	@Override
	public void verifyHandler(List<NisExpenseDetailVO> list) {
		// TODO Auto-generated method stub
	}
	/**
	 * 撤回
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowRevoke(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		NisExpenseDetail nisExpenseDetail = this.get(workFlowResult.getBusinessKey(), true);
		//更新审批状态
		nisExpenseDetail.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP);
		nisExpenseDetail.setUpdateBy(user.getLoginName());
		nisExpenseDetail.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(nisExpenseDetail);
		//更新资金计划执行
		fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_EXPENSE, BudgetConstant.PROCESS_SYSNAME_NIS, nisExpenseDetail.getExpenseId(), NetbankConstant.STATUE_TEMP, user);
		//生成日志
		nisMessageService.saveMessage(nisExpenseDetail.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, NisMessageConstant.NIS_LOG_WITHDRAW_SUCC, NisMessageConstant.BUS_TYPE_EXPENSE, user);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在途的费用报销单据，不允许禁用！
			int num = this.nisExpenseDetailDao.getExpenseCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_EXPENSE,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_EXPENSE_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 获取费用报销单总金额
	 * @param billId
	 * @return
	 */
	public BigDecimal getSumExpenseMoneyByBillId(String billId){
		return nisExpenseDetailDao.getSumExpenseMoneyByBillId(billId);
	}
	/**
	 * 获取费用报销单成功总金额
	 *
	 * @param billId
	 * @return
	 */
	public BigDecimal getSumSuccessExpenseMoneyByBillId(String billId) {
		return nisExpenseDetailDao.getSumSuccessExpenseMoneyByBillId(billId);
	}
	/**
	 * 获取费用报销单失败总金额
	 *
	 * @param billId
	 * @return
	 */
	public BigDecimal getSumFailExpenseMoneyByBillId(String billId) {
		return nisExpenseDetailDao.getSumFailExpenseMoneyByBillId(billId);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @return
	 */
	public int updateVoucherStatByBillId(String billId){
		return this.nisExpenseDetailDao.updateVoucherStatByBillId(billId);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @return
	 */
	public int updateVoucherStatByBillIdQueryResult(String billId){
		return this.nisExpenseDetailDao.updateVoucherStatByBillIdQueryResult(billId);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @param voucherStat 修改的指令
	 * @return
	 */
	public int updateVoucherStatByBillId(String billId, String voucherStat){
		return this.nisExpenseDetailDao.updateVoucherStatByBillId(billId, voucherStat);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @param voucherStat 修改的指令
	 * @param expenseIds 修改的明细单号
	 * @param isIn
	 * @return
	 */
	public int updateVoucherStatByBillId(String billId, String voucherStat, String expenseIds, boolean isIn){
		String[] expenseId = expenseIds.split(CommonConstant.COMMA_SPLIT);
		List<String> expenseIdList = new ArrayList<>();
		for(String id:expenseId){
			expenseIdList.add(id);
		}
		return this.nisExpenseDetailDao.updateVoucherStatByBillId(billId, voucherStat, expenseIdList, isIn);
	}
	/**
	 * 带原状态更新费用报销单
	 * @param vo 费用报销对象
	 * @param oldStatus 原状态控制
	 * @return
	 */
	public void updateNisExpenseDetailStatus(NisExpenseDetailVO vo, Integer[] oldStatus) {
		int result = this.nisExpenseDetailDao.updateNisExpenseDetailStatus(vo, oldStatus);
		if(result<1){
			throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getExpenseId());
		}
	}
	/**
	 * 获取ERP待处理单据
	 * @return
	 */
	public List<NisExpenseDetailVO> listErpTodoBill() {
		return nisExpenseDetailDao.listErpTodoBill();
	}
}
