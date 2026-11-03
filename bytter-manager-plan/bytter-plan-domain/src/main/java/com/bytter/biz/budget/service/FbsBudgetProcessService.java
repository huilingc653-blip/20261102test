package com.bytter.biz.budget.service;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.core.base.entity.IdEntity;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.budget.dao.FbsBudgetProcessDao;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.spi.constant.FbsWorkFlowConstant;
import com.bytter.biz.budget.spi.constant.ResourceConstant;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.budget.spi.vo.NisWageVO;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.budget.vo.FbsBudgetProcessCommonVO;
import com.bytter.biz.budget.vo.FbsBudgetProcessVO;
import com.bytter.biz.budget.vo.FbsBudgetProjectDetailVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpParentChangVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;

import cn.hutool.core.collection.CollectionUtil;

import javax.annotation.Resource;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 资金计划执行表
 * @date 2017-07-18 10:48:05
 */
@Slf4j
@WorkFlow({FbsWorkFlowConstant.FBS_BUDGET_PROCESS})
@Service("fbsBudgetProcessService")
@Transactional
public class FbsBudgetProcessService extends BaseService<FbsBudgetProcess, String> implements IWorkFlowListener, ICorpDisableVerifyService, ICorpParentChangVerifyService, IExcelVerifyHandler<FbsBudgetProcessVO> {
	@Autowired
	private FbsBudgetProcessDao fbsBudgetProcessDao;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	// @DubboReference(check = false)
	// private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@Autowired
	private FbsBudgetService fbsBudgetService;
    @Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IFbsItemService fbsItemService;
	@Autowired(required = false)
	private FbsCorpItemService fbsCorpItemService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FbsBudgetProcess entity) {
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
	public void save(FbsBudgetProcess entity) {
		//存储冗余字段
		this.setRedundantFields(entity);
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FbsBudgetProcess entity, ICurrentUser user) {
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		//存储冗余字段
		this.setRedundantFields(entity);
		super.updateEntity(entity);
		//消息完成通知
		notificationService.complete(NotificationConstant.BUDGET.BUDGET_PROCESS, entity.getId(), user.getId());
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FbsBudgetProcess entity) {
		//存储冗余字段
		this.setRedundantFields(entity);
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FbsBudgetProcess, String> getEntityDao() {
		return fbsBudgetProcessDao;
	}
	/**
	 * 存储冗余字段信息
	 * @param entity
	 */
	public void setRedundantFields(FbsBudgetProcess entity) {
		//存储冗余字段
		if (StringUtil.isBlank(entity.getCorpName())) {
			CorpDTO corp = corpService.get(entity.getCorpId());
			entity.setCorpName(corp.getName());
		}
		if (StringUtil.isBlank(entity.getCurName())) {
			BtCurrencyDTO cur = btCurrencyService.getObjectById(entity.getCurId());
			entity.setCurName(cur.getCurName());
		}
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput<FbsBudgetProcessVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser currentUser,String flag) {
		StringBuilder sql = new StringBuilder();
		//是否查询模式
		String opType = (String) dataTablesInput.getFormdata().get("opType");
		if (CommonConstant.SUBMIT_SEARCH.equals(opType)) {
			sql.append(" where 1=1 ");
		} else {
			sql.append(" where f.valid_sign='1' and (t.status=0 or t.status=-1) and t.business_Name='input'");
		}
		//录入日期
		String gedCreateDate = (String) dataTablesInput.getFormdata().get("ged_createDate");
		String ledCreateDate = (String) dataTablesInput.getFormdata().get("led_createDate");
		if (StringUtils.isNotEmpty(gedCreateDate)) {
			gedCreateDate = gedCreateDate + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_createDate", gedCreateDate);
		}
		if (StringUtils.isNotEmpty(ledCreateDate)) {
			ledCreateDate = ledCreateDate + " 23:59:59";
			dataTablesInput.getFormdata().put("led_createDate", ledCreateDate);
		}
		List<String> corpIds=null;
		if(flag.equals("queryall")){
 			corpIds = getUserCorpIds(ResourceConstant.FBS_PROCESSQUERY_FBSBUDGETPROCESS_QUERY, currentUser.getId());
		}
		if(flag.equals("querys")){
			corpIds = getUserCorpIds(ResourceConstant.FBS_PROCESS_FBSBUDGETPROCESS_QUERY, currentUser.getId());
		}
		String corpAuthSql = SqlUtil.getInSql(corpIds, "t.CORP_ID", true);
		if (StringUtils.isNotEmpty(corpAuthSql)) {
			sql.append(" and ").append(corpAuthSql).append(" ");
		}
		DataTablesOutput<FbsBudgetProcessVO> output = fbsBudgetProcessDao.getDataTablesUnion(dataTablesInput, sql.toString());
		return this.setBudgetProcessStatus(output);
	}
	/**
	 * 设置计划执行状态
	 * @param dataTablesOutput
	 * @return
	 */
	private DataTablesOutput setBudgetProcessStatus(DataTablesOutput dataTablesOutput) {
		if (dataTablesOutput == null) {
			return dataTablesOutput;
		}
		List<FbsBudgetProcessVO> list = dataTablesOutput.getData();
		//状态
		Map<String, DictDTO> statusMap = dictService.listMap(BudgetConstant.BUDGET_PROCESS_STATUS_DICT);
		for(FbsBudgetProcessVO vo:list){
			vo.setStatusName(statusMap.get(vo.getStatus().toString()).getName());
		}
		dataTablesOutput.setData(list);
		return dataTablesOutput;
	}
	/**
	 * 获取传入用户对应资源的单位或网点id
	 * <br>
	 * <b>作者：</b>周亮<br>
	 * <b>日期：</b>2017年6月20日<br>
	 *
	 * @param resource
	 * @param userId
	 * @return 单位id
	 */
	public List<String> getUserCorpIds(String resource, String userId) {
		//改为调用基础模块的接口
//		List<String> lists = iSystemForeignService.getUserCorpIds(resource, userId);
		List<String> lists = corpService.getUserCorpIds(resource,userId);
//		List<String> list = this.fbsBudgetProcessDao.getUserCorpIds(resource, userId);
		return distinctList(lists);
	}
	/**
	 * List非对象去重复
	 *
	 * @param list
	 * @return
	 * @author zuoy 2019-07-01
	 */
	public List<String> distinctList(List<String> list) {
		LinkedHashSet<String> set = new LinkedHashSet<>();
		List<String> newList = new ArrayList<>();
		set.addAll(list);
		newList.addAll(set);
		return newList;
	}
	/**
	 * 查询审核数据集
	 *
	 * @param dataTablesInput
	 * @param ids
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, List<String> ids) {
		StringBuilder sql = new StringBuilder();
		sql.append(" where ").append(SqlUtil.getInSql(ids, "t.id", true));
		DataTablesOutput output = fbsBudgetProcessDao.getDataTablesUnion(dataTablesInput, sql.toString());
		return output;
	}
	/**
	 * 保存
	 *
	 * @param list
	 */
	public void save(List<FbsBudgetProcess> list, String opType, ICurrentUser user) {
		for (FbsBudgetProcess fbsBudgetProcess : list) {
			fbsBudgetProcess.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_FBS);
			fbsBudgetProcess.setBusinessName(BudgetConstant.BUSINESS_NAME_INPUT);
			if (EmptyUtils.isBlank(fbsBudgetProcess.getCreateBy())) {
				fbsBudgetProcess.setCreateBy(user.getLoginName());
				fbsBudgetProcess.setCreateDate(DateUtils.getDate());
			}
			if (CommonConstant.SUBMIT_TEMP.equals(opType)) {
				fbsBudgetProcess.setStatus(CommonConstant.STATUE_TEMP);
			} else {
				fbsBudgetProcess.setStatus(CommonConstant.STATUE_WAIT_CHECK);
			}
			//折合人民币
			fbsBudgetProcess.setProcessNatualMoney(this.setProcessNatualMoney(fbsBudgetProcess));
			this.save(fbsBudgetProcess);
			//提交时调用审批方法
			if (BudgetConstant.STATUE_WAIT_CHECK.equals(fbsBudgetProcess.getStatus())) {
				//调用审批后方法
				 getStartHandler().handler(FbsWorkFlowConstant.FBS_BUDGET_PROCESS, fbsBudgetProcess.getId(), fbsBudgetProcess, fbsBudgetProcess.getCorpId(), user.getId());
			}
		}
	}
	/**
	 * 编辑
	 *
	 * @param fbsBudgetProcess
	 */
	public void modify(FbsBudgetProcess fbsBudgetProcess, ICurrentUser user) {
		FbsBudgetProcess fbs = fbsBudgetProcessDao.get(fbsBudgetProcess.getId());
		BeanUtils.copyNotNullProperties(fbsBudgetProcess, fbs);
		fbs.setUpdateBy(user.getLoginName());
		fbs.setUpdateDate(DateUtils.getDate());
		if (CommonConstant.STATUE_TEMP.equals(fbsBudgetProcess.getStatus())) {
			fbs.setStatus(CommonConstant.STATUE_TEMP);
		} else {
			fbs.setStatus(CommonConstant.STATUE_WAIT_CHECK);
		}
		//折合人民币
		fbs.setProcessNatualMoney(this.setProcessNatualMoney(fbs));
		this.update(fbs, user);
		//提交时调用审批方法
		if (BudgetConstant.STATUE_WAIT_CHECK.equals(fbsBudgetProcess.getStatus())) {
			//调用审批后方法
			 getStartHandler().handler(FbsWorkFlowConstant.FBS_BUDGET_PROCESS, fbs.getId(), fbs, fbs.getCorpId(), user.getId());
		}
		//消息完成通知
		notificationService.complete(NotificationConstant.BUDGET.BUDGET_PROCESS, fbs.getId(), user.getId());
	}
	/**
	 * 根据条件查询 存在的已执行对象
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	public List<FbsBudgetProcess> getBudgetProcessByBillNo(String businessName, String processSysname, String billNo) {
		return fbsBudgetProcessDao.getBudgetProcessByBillNo(businessName, processSysname, billNo);
	}
	
	/**
	 * 根据条件查询 存在的已执行对象
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	public List<FbsBudgetProcess> getBatchBudgetProcessByBillNo(String businessName, String processSysname, List<String> billNos) {
		return fbsBudgetProcessDao.getBatchBudgetProcessByBillNo(businessName, processSysname, billNos);
	}
	/**
	 * 根据条件查询 存在的已执行对象
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	public List<FbsBudgetProcess> getBudgetProcessByBillId(String businessName, String processSysname, String billId) {
		return fbsBudgetProcessDao.getBudgetProcessByBillId(businessName, processSysname, billId);
	}
	/**
	 * 根据条件查询 存在的已执行对象
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	public List<FbsBudgetProcess> getBudgetProcesInfo(String businessName, String processSysname, String billNo, String billId, String id) {
		List<FbsBudgetProcess> list = null;
		if (!StringUtils.isBlank(billId)) {
			list = this.getBudgetProcessByBillId(businessName, processSysname, billId);
		} else if (!EmptyUtils.isBlank(id)) {
			list = new ArrayList<>();
			FbsBudgetProcessVO vo = this.getVOById(id);
			if (!EmptyUtils.isEmpty(vo)) {
				FbsBudgetProcess fbsBudgetProcess = new FbsBudgetProcess();
				BeanUtils.copyNotNullProperties(vo, fbsBudgetProcess);
				list.add(fbsBudgetProcess);
			}
		} else {
			list = this.getBudgetProcessByBillNo(businessName, processSysname, billNo);
		}
		if (EmptyUtils.isEmptyList(list)) {
			list = new ArrayList<FbsBudgetProcess>();
		}
		return list;
	}
	/**
	 * 批量预算释放
	 *
	 * @param ids 用“,”分隔 字符串
	 */
	public void batchReleaseByIds(String ids, ICurrentUser user) {
		String[] is = ids.split(",");
		for (String id : is) {
			FbsBudgetProcess fbs = fbsBudgetProcessDao.get(id);
			if (!CommonConstant.STATUE_CHECK.equals(fbs.getStatus())) {
				throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGETPROCESS_DELETE_STATUE_CHECK);
			}
		}
		for (String id : is) {
			FbsBudgetProcess fbs = fbsBudgetProcessDao.get(id);
			fbs.setUpdateBy(user.getLoginName());
			fbs.setUpdateDate(DateUtils.getDate());
			fbs.setStatus(CommonConstant.STATUE_DELETE);
			this.update(fbs, user);
			//消息完成通知
			notificationService.complete(NotificationConstant.BUDGET.BUDGET_PROCESS, fbs.getId(), user.getId());
		}
	}
	/**
	 * 批量提交资金计划
	 * @param ids
	 * @param user
	 */
	public void batchSubmit(String ids, ICurrentUser user) {
		List<String> idList = new ArrayList<>();
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			FbsBudgetProcessVO processVO = this.getVOById(id);
			//计划执行控制只考虑支出的执行
			if(processVO.getSign() < 0){
				idList.add(id);
			}
		}
		if(EmptyUtils.isNotEmpty(idList)){
			//资金计划余额校验
			List<FbsBudgetProcessVO> listBudget = this.getSumBudgetProcessById(BudgetConstant.BUSINESS_NAME_INPUT, BudgetConstant.PROCESS_SYSNAME_FBS, idList);
			for (FbsBudgetProcessVO processVO : listBudget) {
				//校验单位是否上报/是否控制资金计划：单位上报且受控的情况前提下校验计划
				if (CommonConstant.YES.equals(processVO.getReportBudget()+"") && CommonConstant.YES.equals(processVO.getControlBudget()+"")) {
					//判断科目是否受控
					if (CommonConstant.YES.equals(processVO.getIsControl())) {
						FbsBudgetProcessCommonVO vo = fbsBudgetService.getFbsBudgetProcessByItemCode(processVO.getCorpId(), processVO.getItemCode(), processVO.getProcessDate(), null, null, null);
						//计划执行规则为5-先执行后控制时无需校验
						if (!BudgetConstant.PROCESS_RULE_5.equals(vo.getProcessRule())) {
							//计划执行科目余额控制方式
							String itemBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue();
							if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_ONESELF.equals(itemBalance)) {
								//受本科目控制
								if (processVO.getProcessMoney().doubleValue() > (vo.getItemMoney().add(processVO.getProcessMoney())).doubleValue()) {
									throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_ITEMBALANCE);
								}
							}
							if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_SUPER.equals(itemBalance)) {
								//受上级科目控制
								if (processVO.getProcessMoney().doubleValue() > (vo.getSuperItemMoney().add(processVO.getProcessMoney())).doubleValue()) {
									throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_SUPERITEMBALANCE);
								}
							}
							if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_TOP.equals(itemBalance)) {
								//受大科目控制
								if (processVO.getProcessMoney().doubleValue() > (vo.getTopItemMoney().add(processVO.getProcessMoney())).doubleValue()) {
									throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_TOPITEMBALANCE);
								}
							}
						}
					}
				}
			}
		}
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			FbsBudgetProcess fbsBudgetProcess = new FbsBudgetProcess();
			fbsBudgetProcess.setId(id);
			fbsBudgetProcess.setStatus(BudgetConstant.STATUE_WAIT_CHECK);
			this.modify(fbsBudgetProcess, user);
		}
	}
	/**
	 * 根据id查对象
	 *
	 * @param id
	 * @return
	 */
	public FbsBudgetProcessVO getVOById(String id) {
		return fbsBudgetProcessDao.getVOById(id);
	}
	/**
	 * 启动完成
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	/**
	 * 流程结束
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		String businessKey = workFlowResult.getBusinessKey();
		boolean result = workFlowResult.isPass();
		FbsBudgetProcess entity = this.get(businessKey, true);
		if (result) {
			entity.setStatus(BudgetConstant.STATUE_CHECK);
		} else {
			entity.setStatus(BudgetConstant.STATUE_BACK);
		}
		entity.setUpdateBy(null);
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
	}
	/**
	 * 获取科目的执行金额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @return
	 */
	public BigDecimal collectProcessMoney(Date beginDate, Date endDate, String itemCode, String corpId) {
		return fbsBudgetProcessDao.collectProcessMoney(beginDate, endDate, itemCode, corpId, null, null);
	}
	/**
	 * 获取科目的执行金额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @return
	 */
	public BigDecimal collectProcessMoneyByBusinessName(Date beginDate, Date endDate, String itemCode, String corpId, String processSysname, String businessName) {
		return fbsBudgetProcessDao.collectProcessMoneyByBusinessName(beginDate, endDate, itemCode, corpId, processSysname, businessName);
	}
	/**
	 * 获取科目的执行金额  计算时，过滤本单号余额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @param billNo
	 * @param billId
	 * @return
	 * @author zuoy 2018-05-31
	 */
	public BigDecimal collectProcessMoneyByNotBillNo(Date beginDate, Date endDate, String itemCode, String corpId, String billNo, String billId) {
		return fbsBudgetProcessDao.collectProcessMoney(beginDate, endDate, itemCode, corpId, billNo, billId);
	}
	/**
	 * 获取科目的折合人民币执行金额  计算时，过滤本单号余额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @param billNo
	 * @param billId
	 * @return
	 * @author huangx 2018-12-20processNatualMoney
	 */
	public BigDecimal collectProcessNatualMoneyByNotBillNo(Date beginDate, Date endDate, String itemCode, String corpId, String billNo, String billId) {
		return fbsBudgetProcessDao.collectProcessNatualMoney(beginDate, endDate, itemCode, corpId, billNo, billId);
	}
	/**
	 * 获取科目的折合本身币别的执行金额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @param billNo
	 * @param billId
	 * @return
	 */
	public BigDecimal collectProcessCurMoneyByNotBillNo(Date beginDate, Date endDate, String itemCode, String corpId, String billNo, String billId) {
		return fbsBudgetProcessDao.collectProcessCurMoney(beginDate, endDate, itemCode, corpId, billNo, billId);
	}
	/**
	 * 写入计划执行
	 *
	 * @param list
	 */
	@Transactional
	public void writeBudgetProcess(List<FbsBudgetProcess> list, ICurrentUser user) {
		
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		for (FbsBudgetProcess fbsBudgetProcess : list) {
			fbsBudgetProcess.setCreateBy(user.getLoginName());
			fbsBudgetProcess.setCreateDate(DateUtils.getDate());
			// update 默认非内转 zuoy 2018-03-15
			if (EmptyUtils.isBlank(fbsBudgetProcess.getInnerSign())) {
				fbsBudgetProcess.setInnerSign(BudgetConstant.INNER_SIGN_0);
			}
			//金额为空
			if(EmptyUtils.isEmpty(fbsBudgetProcess.getProcessCurMoney())){
				if(StringUtils.isBlank(fbsBudgetProcess.getCurId())) {
					//获取顶级网点/单位
					CorpDTO topCorp = corpService.getTopCorpByCorpId(fbsBudgetProcess.getCorpId());
					fbsBudgetProcess.setCurId(topCorp.getCurId());
				}
				//根据执行日期获取计划周期
				FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(fbsBudgetProcess.getProcessDate(), timeType);
				//获取单位周期计划
				FbsBudget budget = fbsBudgetService.getCorpBudget(fbsBudgetProcess.getCorpId(), timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
				//避免erp挂接不写计划执行日期，默认写入系统日期
				if(fbsBudgetProcess.getProcessDate() == null){
					fbsBudgetProcess.setProcessDate(DateUtils.getDateYMD());
				}
				if(!EmptyUtils.isEmpty(budget)) {
					//获取汇率
					BigDecimal exchangeRate = fcExcrateAdjustService.getExchangeRateBy(fbsBudgetProcess.getCurId(), budget.getCurId(), DateUtils.setSpecifiedDateOfTime(fbsBudgetProcess.getProcessDate()), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
					if (!EmptyUtils.isEmpty(exchangeRate)) {
						fbsBudgetProcess.setExchangeRate(exchangeRate);
						fbsBudgetProcess.setProcessCurMoney(fbsBudgetProcess.getProcessMoney().multiply(exchangeRate));
					} else {
						fbsBudgetProcess.setExchangeRate(BigDecimal.ONE);
						fbsBudgetProcess.setProcessCurMoney(fbsBudgetProcess.getProcessMoney());
					}
				}else{
					fbsBudgetProcess.setExchangeRate(BigDecimal.ONE);
					fbsBudgetProcess.setProcessCurMoney(fbsBudgetProcess.getProcessMoney());
				}
			}
			//折合人民币
			fbsBudgetProcess.setProcessNatualMoney(this.setProcessNatualMoney(fbsBudgetProcess));
		}
		// 冗余单位名称和币种名称
		List<String> curIdList = list.stream().map(FbsBudgetProcess::getCurId).collect(Collectors.toList());
		Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyByIds(curIdList);
		List<String> corpIdList = list.stream().map(FbsBudgetProcess::getCorpId).collect(Collectors.toList());
		Map<String, CorpDTO> corpMap = corpService.getCorpMapByCorpId(corpIdList);
		list.forEach(item -> {
			Optional.ofNullable(item.getCurId()).flatMap(curId -> Optional.ofNullable(currencyMap.get(curId)))
					.ifPresent(currency -> {
						item.setCurName(currency.getCurName());
					});
			Optional.ofNullable(item.getCorpId()).flatMap(corpId -> Optional.ofNullable(corpMap.get(corpId)))
					.ifPresent(corp -> {
						item.setCorpName(corp.getName());
					});
		});
		this.batchSave(list, 1000);
	}
	/**
	 * 判断是否有科目计划执行
	 * @param corpId
	 * @param itemCode
	 * @param wishDate
	 * @return
	 */
	public boolean isCorpHaveItemCode(String corpId, String itemCode,Date wishDate) {
		boolean result = false;
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		//根据执行日期获取计划周期
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(wishDate, timeType);
		//获取单位周期计划
		FbsBudget budget = fbsBudgetService.getCorpBudget(corpId, timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
		//获取单位分配的科目
		List<FbsItemVO> listFbsItem = fbsCorpItemService.getAllItemByCorpIdNotSort(corpId);
		Map<String,FbsItemVO> map = new HashMap<>();
		for (FbsItemVO fbsItemVO : listFbsItem){
			map.put(fbsItemVO.getItemCode(),fbsItemVO);
		}
		if(!EmptyUtils.isEmpty(budget) && map.containsKey(itemCode)){
			result = true;
		}
		return result;
	}
	/**
	 * 更新计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 */
	public void updateBatchBudgetProcess(String businessName, String processSysname, List<NisWageVO> nisWageList, ICurrentUser user) {
		
		List<String> billNos = nisWageList.stream().map(NisWageVO::getWageNo).collect(Collectors.toList());
		List<String> dealbillNos = new ArrayList<String>();
		//1000条记录处理一次
		int i = 0;
		for(String billno:billNos) {
			i++;
			dealbillNos.add(billno);
			if(dealbillNos.size() % 1000 ==0) {
				dealUpdateBatch(businessName,processSysname,dealbillNos,user,nisWageList);
				dealbillNos.clear();
			}
		}
		//处理剩余的数据
		if(CollectionUtil.isNotEmpty(dealbillNos)) {
			dealUpdateBatch(businessName,processSysname,dealbillNos,user,nisWageList);
		}
	}
	
	public void dealUpdateBatch(String businessName, String processSysname,List<String> billNos,ICurrentUser user,List<NisWageVO> nisWageList) {
		List<FbsBudgetProcess> list = this.getBatchBudgetProcessByBillNo(businessName, processSysname, billNos);
		List<FbsBudgetProcess> entityList = new ArrayList<>();
		Map<String,BigDecimal> mapCurrate = getCurExrate(list);//保存ID对应的汇率
		if (!EmptyUtils.isEmptyList(list)) {
			for(FbsBudgetProcess fbsBudgetProcess :list){
				for(NisWageVO vo : nisWageList) {
					if(vo.getWageNo().equals(fbsBudgetProcess.getBillNo())) {
						fbsBudgetProcess.setStatus(vo.getStatus());
						fbsBudgetProcess.setUpdateBy(user.getLoginName());
						fbsBudgetProcess.setUpdateDate(DateUtils.getDate());
						//避免erp挂接不写计划执行日期，默认写入系统日期
						if(fbsBudgetProcess.getProcessDate() == null){
							fbsBudgetProcess.setProcessDate(DateUtils.getDateYMD());
						}
						//折合人民币
						BigDecimal curExrate = mapCurrate.get(fbsBudgetProcess.getId());
						if (!EmptyUtils.isEmpty(curExrate)) {
							fbsBudgetProcess.setProcessNatualMoney(fbsBudgetProcess.getProcessMoney().multiply(curExrate));
						}
						entityList.add(fbsBudgetProcess);
					}
				}
			}
		}
		this.batchUpdate(entityList);
	}
	
	/**
	 * 批量更新计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @param status		  更新状态
	 */
	public void updateBudgetProcess(String businessName, String processSysname, String billNo, Integer status, ICurrentUser user) {
		List<FbsBudgetProcess> list = this.getBudgetProcessByBillNo(businessName, processSysname, billNo);
		if (!EmptyUtils.isEmptyList(list)) {
			List<FbsBudgetProcess> entityList = new ArrayList<>();
			for(FbsBudgetProcess fbsBudgetProcess :list){
				fbsBudgetProcess.setStatus(status);
				fbsBudgetProcess.setUpdateBy(user.getLoginName());
				fbsBudgetProcess.setUpdateDate(DateUtils.getDate());
				//避免erp挂接不写计划执行日期，默认写入系统日期
				if(fbsBudgetProcess.getProcessDate() == null){
					fbsBudgetProcess.setProcessDate(DateUtils.getDateYMD());
				}
				//折合人民币
				fbsBudgetProcess.setProcessNatualMoney(this.setProcessNatualMoney(fbsBudgetProcess));
				entityList.add(fbsBudgetProcess);
			}
			this.batchUpdate(entityList);
		}
	}
	/**
	 * 释放计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	public void releaseBudgetProcess(String businessName, String processSysname, String billNo) {
		this.fbsBudgetProcessDao.releaseBudgetProcess(businessName, processSysname, billNo);
	}
	/**
	 * 释放计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	public void releaseBudgetProcessByBillId(String businessName, String processSysname, String billId) {
		this.fbsBudgetProcessDao.releaseBudgetProcessByBillId(businessName, processSysname, billId);
	}
	/**
	 * 删除计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	@Transactional
	public void deleteBudgetProcess(String businessName, String processSysname, String billNo) {
		List<FbsBudgetProcess> list = this.getBudgetProcessByBillNo(businessName, processSysname, billNo);
		if (!EmptyUtils.isEmptyList(list)) {
			for (FbsBudgetProcess budget : list) {
				this.deleteEntity(budget);
			}
		}
	}
	
	/**
	 * 批量删除计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNos         业务单据号
	 * @return
	 */
	public void deleteBatchBudgetProcess(String businessName, String processSysname, List<String> billNos) {
		
		List<String> dealbillNos = new ArrayList<String>();
		//1000条记录处理一次
		int i = 0;
		for(String billno:billNos) {
			i++;
			dealbillNos.add(billno);
			if(dealbillNos.size() % 1000 ==0) {
				dealDeletePlan(businessName,processSysname,billNos);
				dealbillNos.clear();
			}
		}
		//处理剩余的数据
		if(CollectionUtil.isNotEmpty(dealbillNos)) {
			dealDeletePlan(businessName,processSysname,billNos);
		}
		
	}
	
	public void dealDeletePlan(String businessName, String processSysname, List<String> billNos) {
		List<FbsBudgetProcess> list = this.getBatchBudgetProcessByBillNo(businessName, processSysname, billNos);
		List<String> ids = null;
		if (!EmptyUtils.isEmptyList(list)) {
			ids = list.stream().map(FbsBudgetProcess::getId).collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(ids)) {
				super.delete(ids);
			}
			
		}
	}
	/**
	 * 删除计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	public void deleteBudgetProcessByBillId(String businessName, String processSysname, String billId) {
		List<FbsBudgetProcess> list = this.getBudgetProcessByBillId(businessName, processSysname, billId);
		if (!EmptyUtils.isEmptyList(list)) {
			for (FbsBudgetProcess budget : list) {
				this.deleteEntity(budget);
			}
		}
	}
	public boolean corpCtlBudget(String corpId) {
		return this.fbsCorpBudgetService.reportBudgetByCorpId(corpId);
	}
	/**
	 * 获取已执行的科目汇总金额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @return
	 * @author zuoy 2018-07-17
	 */
	public BigDecimal collectProcessMoneyByStatus(Date beginDate, Date endDate, String itemCode, String corpId) {
		return fbsBudgetProcessDao.collectProcessMoneyByStatus(beginDate, endDate, itemCode, corpId);
	}
	/**
	 * 根据条件查询 存在的已执行对象
	 *
	 * @param corpId   单位
	 * @param beginDate 开始日期
	 * @param endDate  结束日期
	 * @return
	 */
	public List<FbsBudgetProcess> listBudgetProcessByCorpId(String corpId, Date beginDate, Date endDate, String itemCode) {
		return fbsBudgetProcessDao.listBudgetProcessByCorpId(corpId, beginDate, endDate, itemCode);
	}
	/**
	 * 根据明细获取计划执行金额  计算时，过滤本单号余额
	 * @param corpId
	 * @param itemCode
	 * @param processDate
	 * @param billNo
	 * @param billId
	 * @return
	 */
	public BigDecimal getBudgetProcessMoneyByDetail(String corpId, String itemCode, Date processDate, String billNo, String billId, FbsBudgetProjectDetailVO fbsBudgetProjectDetailVO) {
		String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
		String[] dtlColIds = fbsBudgetProjectDetailVO.getDtlColIds().split(CommonConstant.COMMA_SPLIT);
		Map<String, String> dtlColMap = new HashMap<>();
		if(!EmptyUtils.isEmpty(dtlColIds)){
			for (String dtl : dtlColIds) {
				dtlColMap.put(dtl, dtl);
			}
		}
		//根据执行日期获取计划周期
		FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(processDate, timeType);
		BigDecimal processMoney = BigDecimal.ZERO;
		List<FbsBudgetProcessVO> list = this.fbsBudgetProcessDao.listBudgetProcessMoneyByDetail(fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate(), itemCode, corpId, billNo, billId, fbsBudgetProjectDetailVO, dtlColMap);
		if(!EmptyUtils.isEmptyList(list)){
			for(FbsBudgetProcessVO vo : list){
				processMoney = processMoney.add(vo.getProcessCurMoney());
			}
		}
		return processMoney;
	}
	/**
	 * 获取科目的执行金额(已执行)
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @return
	 */
	public BigDecimal collectAlreadyProcessCurMoney(Date beginDate, Date endDate, String itemCode, String corpId) {
		return this.fbsBudgetProcessDao.collectAlreadyProcessCurMoney(beginDate, endDate, itemCode, corpId);
	}
	/**
	 * 获取科目的执行金额(已执行)
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @return
	 */
	public BigDecimal collectAlreadyProcessMoney(Date beginDate, Date endDate, String itemCode, String corpId) {
		return this.fbsBudgetProcessDao.collectAlreadyProcessMoney(beginDate, endDate, itemCode, corpId);
	}
	/**
	 * 获取科目的执行金额(已执行)
	 *
	 * @param beginDate
	 * @param endDate
	 * @param corpId
	 * @return
	 */
	public BigDecimal collectAlreadyProcessMoney(Date beginDate, Date endDate, String corpId) {
		return this.fbsBudgetProcessDao.collectAlreadyProcessMoney(beginDate, endDate, corpId);
	}
	/**
	 * 查下已执行的计划明细
	 *
	 * @param corpId    执行业务名称
	 * @param beginDate 执行系统名称
	 * @param endDate   业务单据号
	 * @return
	 */
	public List<FbsBudgetProcess> listBudgetAlreadyProcessByCorpId(String corpId, Date beginDate, Date endDate, String itemCode) {
		return this.fbsBudgetProcessDao.listBudgetAlreadyProcessByCorpId(corpId, beginDate, endDate, itemCode);
	}
	/**
	 * 设置执行金额折合人民币后的金额
	 * @param fbsBudgetProcess
	 * @return
	 */
	private BigDecimal setProcessNatualMoney(FbsBudgetProcess fbsBudgetProcess){
		//默认执行金额
		BigDecimal processNatualMoney = fbsBudgetProcess.getProcessMoney();
		//获取人民币
		BtCurrencyDTO rmbCur = btCurrencyService.getRMBCurrency();
		if(StringUtils.isNotEmpty(fbsBudgetProcess.getCurId())) {
			BigDecimal curExrate = fcExcrateAdjustService.getExchangeRateBy(fbsBudgetProcess.getCurId(), rmbCur.getId(),DateUtils.setSpecifiedDateOfTime(fbsBudgetProcess.getProcessDate()), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
			if (!EmptyUtils.isEmpty(curExrate)) {
				processNatualMoney = fbsBudgetProcess.getProcessMoney().multiply(curExrate);
			}
		}
		return processNatualMoney;
	}
	
	/**
	 * 获取各种币对人民币的汇率MAP
	 * @return
	 */
	private Map<String,BigDecimal> getCurExrate(List<FbsBudgetProcess> list){
		//获取人民币汇率
		BtCurrencyDTO rmbCur = btCurrencyService.getRMBCurrency();
		List<com.bytter.system.api.vo.FbsBudgetProcessVo> listVo = BeanUtils.copyListProperties(list, com.bytter.system.api.vo.FbsBudgetProcessVo.class);
		Map<String,BigDecimal> curExrate = fcExcrateAdjustService.getExchangeRateByMap(listVo, rmbCur.getId(), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
		return curExrate;
	}
	
	
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有资金计划执行的在途数据，不允许禁用！
            int num = this.fbsBudgetProcessDao.getProcessCountByCorpId(corpId,"disable");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FBS,
                        SysConfigConstant.BusType.FBS_PROCESS,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_DISABLE_PROCESS_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpParentChang(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有资金计划执行的在途数据，不允许变更！
            int num = this.fbsBudgetProcessDao.getProcessCountByCorpId(corpId,"change");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FBS,
                        SysConfigConstant.BusType.FBS_PROCESS,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.FBS_VALID_CHANGE_PROCESS_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
	/**
	 * 根据条件查询 存在的已执行对象(多个业务单合计执行金额)
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单No
	 * @return
	 */
	public List<FbsBudgetProcessVO> getSumBudgetProcessByBillNo(String businessName, String processSysname, List<String> billNo) {
		String billNoAuthSql = SqlUtil.getInSql(billNo, "t.bill_No", true);
		return fbsBudgetProcessDao.getSumBudgetProcessByJoinSql(businessName, processSysname, billNoAuthSql);
	}
	/**
	 * 根据条件查询 存在的已执行对象(多个业务单合计执行金额)
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param id         计划执行id
	 * @return
	 */
	public List<FbsBudgetProcessVO> getSumBudgetProcessById(String businessName, String processSysname, List<String> id) {
		String idAuthSql = SqlUtil.getInSql(id, "t.id", true);
		return fbsBudgetProcessDao.getSumBudgetProcessByJoinSql(businessName, processSysname, idAuthSql);
	}
	/**
	 * 更新计划执行的执行日期
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @param processDate	 执行日期
	 */
	public void updateBudgetProcessDate(String businessName, String processSysname, String billNo, Date processDate, ICurrentUser user) {
		List<FbsBudgetProcess> list = this.getBudgetProcessByBillNo(businessName, processSysname, billNo);
		if (!EmptyUtils.isEmptyList(list)) {
			List<FbsBudgetProcess> entityList = new ArrayList<>();
			for(FbsBudgetProcess fbsBudgetProcess :list){
				fbsBudgetProcess.setProcessDate(processDate);
				fbsBudgetProcess.setUpdateBy(user.getLoginName());
				fbsBudgetProcess.setUpdateDate(DateUtils.getDate());
				//折合人民币
				fbsBudgetProcess.setProcessNatualMoney(this.setProcessNatualMoney(fbsBudgetProcess));
				entityList.add(fbsBudgetProcess);
			}
			this.batchUpdate(entityList);
		}
	}
	/**
	 * 导入
	 *
	 * @param list
	 */
	public void saveImport(List<FbsBudgetProcessVO> list, ICurrentUser user) {
		List<FbsBudgetProcess> processList = BeanUtils.copyListProperties(list, FbsBudgetProcess.class);
		this.save(processList, CommonConstant.SUBMIT_TEMP, user);
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(FbsBudgetProcessVO vo) {
		List<String> msgList = new ArrayList<>();
		// 判断单位在系统中是否存在
		CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
		if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
			corp = new CorpDTO();
			msgList.add("nis.netbank.message.corp.null");
		} else {
			vo.setCorpId(corp.getId());
		}
		//金额不能为空且必须大于0
		if (EmptyUtils.isEmpty(vo.getProcessMoney())) {
			msgList.add("nis.netbank.message.money.null");
		} else {
			if (vo.getProcessMoney().compareTo(BigDecimal.ZERO) <= 0) {
				msgList.add("nis.netbank.message.money.mismatch");
			}
		}
		//是否内转业务
		if (!CommonConstant.NO.equals(vo.getInnerSign())
				&& !CommonConstant.YES.equals(vo.getInnerSign())) {
			msgList.add("fbs.budget.message.innersign.format");
		}
		//校验币别
		String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
		if (EmptyUtils.isBlank(curId)) {
			msgList.add("nis.netbank.message.cur.null");
		} else {
			vo.setCurId(curId);
		}
		//计划科目
		if (EmptyUtils.isEmpty(vo.getItemCode())) {
			msgList.add("nis.assign.message.itemcode.null");
		} else {
			FbsItemDTO fbsItemDTO = this.fbsItemService.getFbsItemByCode(vo.getItemCode());
			if (EmptyUtils.isEmpty(fbsItemDTO)) {
				//判断计划科目是否存在
				msgList.add("nis.assign.message.bustype.itemcode.null");
			} else {
				vo.setItemName(fbsItemDTO.getItemName());
			}
		}
		//计划执行汇率获取
		if(StringUtils.isNotBlank(vo.getCorpId()) && StringUtils.isNotBlank(vo.getCurId()) && vo.getProcessDate() != null && vo.getProcessMoney() !=null) {
			//根据执行日期获取计划周期
			String timeType = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_TIMETYPE).getValue();
			FbsBudgetPeriodVO fbsBudgetPeriod = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(vo.getProcessDate(), timeType);
			//获取单位周期计划
			FbsBudget budget = fbsBudgetService.getCorpBudget(vo.getCorpId(), timeType, fbsBudgetPeriod.getBeginDate(), fbsBudgetPeriod.getEndDate());
			if (budget!=null) {
				BigDecimal exchangeRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), budget.getCurId(), DateUtils.setSpecifiedDateOfTime(vo.getProcessDate()), FcExcrateAdjustConstant.RATE_TYPE_BUDGET);
				vo.setExchangeRate(exchangeRate);
				vo.setProcessCurMoney(vo.getProcessMoney().multiply(vo.getExchangeRate()));
			}else{
				vo.setExchangeRate(BigDecimal.ONE);
				vo.setProcessCurMoney(vo.getProcessMoney());
			}
		}else{
			vo.setExchangeRate(BigDecimal.ONE);
			vo.setProcessCurMoney(vo.getProcessMoney());
            //当前计划执行周期单位计划未上报或计划未编制完成！
            msgList.add("fbs.budget.process.message.notinputbudget");
		}
		if (EmptyUtils.isEmptyList(msgList)) {
			return new ExcelVerifyHanlderResult(true, msgList);
		} else {
			return new ExcelVerifyHanlderResult(false, msgList);
		}
	}
	@Override
	public void verifyHandler(List<FbsBudgetProcessVO> list) {
	}

	/**
	 * 查询 执行计划表中，付款办理的脏数据，并进行清理 ;
	 * cron: 0 0 6 * * ?
	 * @param arguments
	 */
	@XxlJob("fbsBudgetProcessService")
	public void run(String arguments){
		log.info("Execute task: fbsBudgetProcessService ");
		List<FbsBudgetProcess> fbsBudgetProcesses = fbsBudgetProcessDao.listPayBadData();
		if (CollectionUtil.isNotEmpty(fbsBudgetProcesses)) {
			List<String> ids = fbsBudgetProcesses.stream().map(IdEntity::getId).collect(Collectors.toList());
			log.info("delete fbsBudgetProcess ids: {}", ids);
			fbsBudgetProcessDao.delete(ids);
		}
	}

	public BigDecimal getBudgetProcessMoneyByItem(FcAccDetailBudgetMatchVO matchVO,FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO,FbsBudgetProjectDetailVO itemDetailVO) {
		String corpId = matchVO.getCorpId();
		String itemCode = fbsBudgetProcessCommonVO.getItemCode();
		Date processDate = null;
		try {
			processDate = StringUtils.isNotEmpty(matchVO.getProcessDate()) ? DateUtils.parseDate(matchVO.getProcessDate(), "yyyy-MM-dd") : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		String billNo = matchVO.getVoucherNo();
		BigDecimal processMoney = this.getBudgetProcessMoneyByDetail(corpId, itemCode, processDate, billNo, "", itemDetailVO);
		return processMoney;
	}
}
