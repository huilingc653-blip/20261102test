package com.bytter.biz.budget.dao;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.vo.FbsBudgetProcessVO;
import com.bytter.biz.budget.vo.FbsBudgetProjectDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 资金计划执行表
 * @date 2017-07-18 10:48:05
 */
@Repository
public class FbsBudgetProcessDao extends BaseDao<FbsBudgetProcess, String> {
	/**
	 * 显示执行列表数据
	 *
	 * @param dataTablesInput
	 * @param sqlWhere
	 * @return
	 */
	public DataTablesOutput<FbsBudgetProcessVO> getDataTablesUnion(DataTablesInput dataTablesInput, String sqlWhere) {
		String sql = getCommonSql(sqlWhere);
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetProcessVO.class, null, false);
	}
	/**
	 * 获取传入用户对应资源的单位或网点id
	 * <br>
	 *
	 * @param resource
	 * @param userId
	 * @return 单位id
	 * @author zuoy 2019-07-01
	 */
	//SYS服务提供
	/*public List<String> getUserCorpIds(String resource, String userId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sql.append("SELECT  distinct cur.CORP_ID AS corpId FROM SYS_RESOURCES r ");
		sql.append("left JOIN SYS_CORP_USER_RESOURCE cur on cur.RESOURCE_ID = r.ID ");
		if(StringUtils.isNotEmpty(resource)
				&&resource.indexOf(",")!=-1){
			map.put("permission", resource.split(","));
			sql.append("AND r.PERMISSION in (:permission) ");
		}else{
			map.put("permission", resource);
			sql.append("AND r.PERMISSION =:permission ");
		}
		if (StringUtils.isNotBlank(userId)) {
			sql.append("WHERE cur.USER_ID =:userId");
			map.put("userId", userId);
		}
		Query query = createSQLQuery(sql.toString(), map);
		List<String> list = query.list();
		return list;
	}*/
	/**
	 * 根据id查对象
	 *
	 * @param id
	 * @return
	 */
	public FbsBudgetProcessVO getVOById(String id) {
		StringBuilder sql = new StringBuilder();
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		sql.append(" where t.id = :id ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(getCommonSql(sql.toString()), params, FbsBudgetProcessVO.class);
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
		StringBuilder hql = new StringBuilder(" from FbsBudgetProcess where billNo= :billNo and businessName= :businessName and processSysname= :processSysname ");
		Map params = new HashMap();
		params.put("billNo", billNo);
		params.put("businessName", businessName);
		params.put("processSysname", processSysname);
		return this.getSession().createQuery(hql.toString()).setProperties(params).list();
	}
	
	
	/**
	 * 根据条件批量查询查询 存在的已执行对象
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	public List<FbsBudgetProcess> getBatchBudgetProcessByBillNo(String businessName, String processSysname, List<String> billNos) {
		StringBuilder hql = new StringBuilder(" from FbsBudgetProcess where billNo in (:billNo) and businessName= :businessName and processSysname= :processSysname ");
		Map params = new HashMap();
		String billNosStr = billNos.stream().collect(Collectors.joining(","));
		params.put("billNo", billNosStr);
		params.put("businessName", businessName);
		params.put("processSysname", processSysname);
		return this.getSession().createQuery(hql.toString()).setProperties(params).list();
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
		StringBuilder hql = new StringBuilder(" from FbsBudgetProcess where billId= :billId and businessName= :businessName and processSysname= :processSysname ");
		Map params = new HashMap();
		params.put("billId", billId);
		params.put("businessName", businessName);
		params.put("processSysname", processSysname);
		return this.getSession().createQuery(hql.toString()).setProperties(params).list();
	}
	/**
	 * 执行计划表通用查@param sqlWhere
	 *
	 * @return
	 */
	private String getCommonSql(String sqlWhere) {
		StringBuilder sql = new StringBuilder("select t.id as id,");
		sql.append("t.CORP_ID as corpId,t.CORP_NAME as corpName,t.PAYEE_ACC_NAME as payeeAccName,");
		sql.append("t.ITEM_CODE as itemCode,f.ITEM_NAME as itemName,f.id as itemId,");
		sql.append("t.PROCESS_MONEY as processMoney,t.PROCESS_NATUAL_MONEY as processNatualMoney,t.PROCESS_DATE as processDate,");
		sql.append("t.BILL_NO as billNo,t.BILL_ID as billId,t.status as status,t.cur_id as curId,t.cur_name as curName,");
		sql.append("t.business_Name as businessName,t.process_Sysname as processSysname,t.inner_sign as innerSign,t.rmk as rmk,");
		sql.append("t.exchange_Rate as exchangeRate,t.process_Cur_Money as processCurMoney,");
		sql.append("t.CREATE_BY as createBy,t.CREATE_DATE as createDate,t.update_BY as updateBy,t.update_DATE as updateDate,f.sign ");
		sql.append(" from FBS_BUDGET_PROCESS t left join fbs_item f on t.ITEM_CODE=f.ITEM_CODE ");
		sql.append(sqlWhere);
		return sql.toString();
	}
	/**
	 * 获取科目的执行金额(不包含资金调拨的计划执行)
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @param billNo
	 * @param billId
	 * @return
	 */
	public BigDecimal collectProcessMoney(Date beginDate, Date endDate, String itemCode, String corpId, String billNo, String billId) {
		StringBuilder sql = new StringBuilder("select sum(PROCESS_CUR_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status<>:status and t.corp_id=:corpId ");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_NULL);
		params.put("corpId", corpId);
		// zuoy 2018-05-31 汇总增加过滤本单号余额
		if (!EmptyUtils.isBlank(billNo)) {
			sql.append(" and t.bill_no <> :billNo ");
			params.put("billNo", billNo);
		} else if (!EmptyUtils.isBlank(billId)) {
			sql.append(" and t.bill_id <> :billId ");
			params.put("billId", billId);
		}
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
	}
	/**
	 * 获取科目的折合人民币执行金额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @param billNo
	 * @param billId
	 * @return
	 */
	public BigDecimal collectProcessNatualMoney(Date beginDate, Date endDate, String itemCode, String corpId, String billNo, String billId) {
		StringBuilder sql = new StringBuilder("select sum(PROCESS_NATUAL_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status<>:status and t.corp_id=:corpId ");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_NULL);
		params.put("corpId", corpId);
		if (!EmptyUtils.isBlank(billNo)) {
			sql.append(" and t.bill_no <> :billNo ");
			params.put("billNo", billNo);
		} else if (!EmptyUtils.isBlank(billId)) {
			sql.append(" and t.bill_id <> :billId ");
			params.put("billId", billId);
		}
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
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
	public BigDecimal collectProcessCurMoney(Date beginDate, Date endDate, String itemCode, String corpId, String billNo, String billId) {
		StringBuilder sql = new StringBuilder("select sum(PROCESS_CUR_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status<>:status ");
		String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
		List<String> corpIdList = new ArrayList<>();
		for(String corp:corpIds){
			corpIdList.add(corp);
		}
		sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "t.corp_id", true));
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_NULL);
		//params.put("corpId", corpId);
		if (!EmptyUtils.isBlank(billNo)) {
			sql.append(" and t.bill_no <> :billNo ");
			params.put("billNo", billNo);
		} else if (!EmptyUtils.isBlank(billId)) {
			sql.append(" and t.bill_id <> :billId ");
			params.put("billId", billId);
		}
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
	}
	/**
	 * 获取科目的执行金额(不包含资金调拨的计划执行)
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @param processSysname
	 * @param businessName
	 * @return
	 */
	public BigDecimal collectProcessMoneyByBusinessName(Date beginDate, Date endDate, String itemCode, String corpId, String processSysname, String businessName) {
		StringBuilder sql = new StringBuilder("select sum(PROCESS_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status=:status ");
		sql.append(" and t.BUSINESS_NAME = :businessName and  t.PROCESS_NAME = :processSysname");
		String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
		List<String> corpIdList = new ArrayList<>();
		for(String corp:corpIds){
			corpIdList.add(corp);
		}
		sql.append(" and ").append(SqlUtil.getInSql(corpIdList, "t.corp_id", true));
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_CHECK);
		//params.put("corpId", corpId);
		params.put("processSysname", processSysname);
		params.put("businessName", businessName);
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
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
		String sql = "update FBS_BUDGET_PROCESS set status=:status where business_Name=:businessName and process_Sysname=:processSysname and bill_No=:billNo";
		HashMap params = new HashMap();
		params.put("status", BudgetConstant.STATUE_NULL);
		params.put("billNo", billNo);
		params.put("businessName", businessName);
		params.put("processSysname", processSysname);
		createSQLQuery(sql).setProperties(params).executeUpdate();
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
		String sql = "update FBS_BUDGET_PROCESS set status=:status where business_Name=:businessName and process_Sysname=:processSysname and bill_Id=:billId";
		HashMap params = new HashMap();
		params.put("status", BudgetConstant.STATUE_NULL);
		params.put("billId", billId);
		params.put("businessName", businessName);
		params.put("processSysname", processSysname);
		createSQLQuery(sql).setProperties(params).executeUpdate();
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
		StringBuilder sql = new StringBuilder("select sum(PROCESS_MONEY) as processMoney from FBS_BUDGET_PROCESS t ");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE >= :beginDate and t.PROCESS_DATE <= :endDate and t.status <> :status and t.corp_id = :corpId ");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_NULL);
		params.put("corpId", corpId);
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
	}
	/**
	 * 根据条件查询 存在的已执行对象
	 *
	 * @param corpId    执行业务名称
	 * @param beginDate 执行系统名称
	 * @param endDate   业务单据号
	 * @return
	 */
	public List<FbsBudgetProcess> listBudgetProcessByCorpId(String corpId, Date beginDate, Date endDate, String itemCode) {
		StringBuilder hql = new StringBuilder(" from FbsBudgetProcess where corpId= :corpId and processDate >= :beginDate and processDate <= :endDate and itemCode like :itemCode");
		Map params = new HashMap();
		params.put("corpId", corpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("itemCode", itemCode + "%");
		return this.getSession().createQuery(hql.toString()).setProperties(params).list();
	}
	/**
	 * 根据明细条件获取计划执行
	 *
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpId
	 * @return
	 * @author zuoy 2018-11-19
	 */
	public List<FbsBudgetProcessVO> listBudgetProcessMoneyByDetail(Date beginDate, Date endDate, String itemCode, String corpId, String billNo, String billId, FbsBudgetProjectDetailVO detailVO, Map<String, String> dtlColMap) {
		StringBuilder sql = new StringBuilder("select * from FBS_BUDGET_PROCESS t ");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE >= :beginDate and t.PROCESS_DATE <= :endDate and t.status <> :status and t.corp_id = :corpId ");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_NULL);
		params.put("corpId", corpId);
		// zuoy 2018-05-31 汇总增加过滤本单号余额
		if (!EmptyUtils.isBlank(billNo)) {
			sql.append(" and t.bill_no <> :billNo ");
			params.put("billNo", billNo);
		} else if (!EmptyUtils.isBlank(billId)) {
			sql.append(" and t.bill_id <> :billId ");
			params.put("billId", billId);
		}
		if (!EmptyUtils.isBlank(detailVO.getCurId()) && dtlColMap.containsKey("curId")) {
			sql.append(" and (t.cur_id = :curId or t.cur_id is null) ");
			params.put("curId", detailVO.getCurId());
		}
		if (!EmptyUtils.isBlank(detailVO.getSettlType()) && dtlColMap.containsKey("settlType")) {
			sql.append(" and (t.settl_type = :settlType or trim(t.settl_type) is null) ");
			params.put("settlType", detailVO.getSettlType());
		}
		if (!EmptyUtils.isBlank(detailVO.getCorpName()) && dtlColMap.containsKey("corpName")) {
			sql.append(" and (t.dept_name = :deptName or t.dept_name is null) ");
			params.put("deptName", detailVO.getCorpName());
		}
		if (!EmptyUtils.isBlank(detailVO.getPurpose()) && dtlColMap.containsKey("purpose")) {
			sql.append(" and (t.purpose = :purpose or t.purpose is null) ");
			params.put("purpose", detailVO.getPurpose());
		}
		if (!EmptyUtils.isEmpty(detailVO.getWishPayday()) && dtlColMap.containsKey("wishPayday")) {
			sql.append(" and (t.wish_payday = :wishPayday or t.wish_payday is null) ");
			params.put("wishPayday", detailVO.getWishPayday());
		}
		if (!EmptyUtils.isBlank(detailVO.getPayerAcc()) && dtlColMap.containsKey("payerAcc")) {
			sql.append(" and (t.payer_acc = :payerAcc or t.payer_acc is null) ");
			params.put("payerAcc", detailVO.getPayerAcc());
		}
		if (!EmptyUtils.isBlank(detailVO.getPayeeAccName()) && dtlColMap.containsKey("payeeAccName")) {
			sql.append(" and (t.payee_acc_name = :payeeAccName or t.payee_acc_name is null) ");
			params.put("payeeAccName", detailVO.getPayeeAccName());
		}
		if (!EmptyUtils.isBlank(detailVO.getPayeeAcc()) && dtlColMap.containsKey("payeeAcc")) {
			sql.append(" and (t.payee_acc = :payeeAcc or t.payee_acc is null) ");
			params.put("payeeAcc", detailVO.getPayeeAcc());
		}
		if (!EmptyUtils.isBlank(detailVO.getPayeeBank()) && dtlColMap.containsKey("payeeBank")) {
			sql.append(" and (t.payee_bank = :payeeBank or t.payee_bank is null) ");
			params.put("payeeBank", detailVO.getPayeeBank());
		}
		if (!EmptyUtils.isBlank(detailVO.getContract()) && dtlColMap.containsKey("contract")) {
			sql.append(" and (t.contract = :contract or t.contract is null) ");
			params.put("contract", detailVO.getContract());
		}
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessVO.class);
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
		StringBuilder sql = new StringBuilder("select sum(PROCESS_NATUAL_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status=:status and t.corp_id=:corpId ");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_CHECK);
		params.put("corpId", corpId);
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
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
		StringBuilder sql = new StringBuilder("select sum(PROCESS_CUR_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
		sql.append(" where t.ITEM_CODE like :itemCode and t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status=:status and t.corp_id=:corpId ");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_CHECK);
		params.put("corpId", corpId);
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
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
		StringBuilder sql = new StringBuilder("select sum(PROCESS_NATUAL_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
		sql.append(" where t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status=:status and t.corp_id=:corpId ");
		HashMap params = new HashMap();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_CHECK);
		params.put("corpId", corpId);
		Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collProMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(processMoney)) {
			collProMoney = (BigDecimal) processMoney;
		}
		return collProMoney;
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
		StringBuilder hql = new StringBuilder(" from FbsBudgetProcess where corpId= :corpId and processDate >= :beginDate and processDate <= :endDate and itemCode like :itemCode and status=:status");
		Map params = new HashMap();
		params.put("corpId", corpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("itemCode", itemCode + "%");
		params.put("status", BudgetConstant.STATUE_CHECK);
		return this.getSession().createQuery(hql.toString()).setProperties(params).list();
	}
	/**
	 * 根据单位统计执行的资金计划
	 * @param corpId
	 * @param busType del:单位删除 disable:禁用 change:上级变更
	 * @return
	 */
	public int getProcessCountByCorpId(String corpId, String busType){
		String sql="select count(*) from fbs_budget_process where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		if(!"del".equals(busType)){
			//0-暂存、11-待审批、-1-审批打回
			sql+=" and status in ('0','11','-1')";
		}
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 根据条件查询 存在的已执行对象(多个业务单合计执行金额)
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param joinSql
	 * @return
	 */
	public List<FbsBudgetProcessVO> getSumBudgetProcessByJoinSql(String businessName, String processSysname, String joinSql) {
		StringBuilder sql = new StringBuilder("select t.corp_id as corpId,t.item_code as itemCode,t.process_Date as processDate,sum(t.process_money*t.exchange_Rate) as processMoney, ");
		sql.append(" c.report_budget as reportBudget,c.control_budget as controlBudget,i.is_Control as isControl, i.item_name as itemName ");
		sql.append(" from fbs_budget_process t");
		sql.append(" left join fbs_corp_budget c on t.corp_id = c.corp_id");
		sql.append(" left join fbs_item i on t.item_code = i.item_code");
		sql.append(" where business_Name= :businessName and process_Sysname= :processSysname ");
		sql.append(" and "+joinSql);
		sql.append("  group by t.corp_id ,t.item_code ,t.process_Date,c.report_budget,c.control_budget,i.is_Control, i.item_name");
		Map params = new HashMap();
		params.put("businessName", businessName);
		params.put("processSysname", processSysname);
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessVO.class);
	}

	public List<FbsBudgetProcess> listPayBadData() {
		StringBuilder sql = new StringBuilder("select * from fbs_budget_process fbp ");
		sql.append(" where fbp.process_sysname = 'nis' ");
		sql.append(" and fbp.business_name = 'pay' ");
		sql.append(" and not exists ( ");
		sql.append(" select 1 from fbs_budget_process a join nis_billhead b on a.bill_id = b.id and a.bill_no = b.bill_no ");
		sql.append(" where a.process_sysname = 'nis' and a.business_name = 'pay' ");
		sql.append(" and a.id = fbp.id ");
		sql.append(" ) ");
		return this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetProcess.class);
	}
}
