package com.bytter.biz.report.dao;
import com.bytter.biz.budget.vo.FbsBudgetProcessVO;
import com.bytter.biz.budget.vo.FbsBudgetSuperaddDetailVO;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import com.bytter.biz.report.vo.FbsBudgetSuperaddViewVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-03-06
 */
@Repository
public class FbsBudgetProcessReportDao extends BaseDao<FbsBudgetProcessReportVO, String> {
	/**
	 * 查询列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTable(DataTablesInput dataTablesInput, String tableName) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fbs.corp_id, f.item_code,f.item_name,f.b_money,f.bsuper_money,(f.b_money+f.bsuper_money) bMoneySum,f.btemp_money from fbs_budget fbs");
		sql.append(" left join ").append(tableName).append(" f on fbs.id = f.budget_id where 1 = 1 ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetProcessReportVO.class, "fbs", false);
	}
	/**
	 * 获取资金计划数据
	 *
	 * @param timeType  预算类型
	 * @param corpId    单位ID
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @param tableName
	 * @return
	 */
	public List<FbsBudgetProcessReportVO> listFbsBudget(String timeType, String corpId, Date beginDate, Date endDate, String tableName) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select fbs.corp_id, f.item_code, f.item_name, i.id as itemId,f.b_money, bt_nvl_num(f.bsuper_money,0.00) bsuperMoney, bt_nvl_num((f.b_money+f.bsuper_money),0.00) bMoneySum, bt_nvl_num(f.btemp_money,0.00) btempMoney ");
		sql.append(" from fbs_budget fbs left join ").append(tableName).append(" f on fbs.id = f.budget_id ");
		sql.append(" left join fbs_item i on f.item_code=i.item_code");
		sql.append(" where fbs.corp_id = :corpId and fbs.time_type = :timeType and fbs.begin_date >= :beginDate and fbs.end_date <= :endDate ");
		params.put("corpId", corpId);
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		// 只汇总审批结束的
		sql.append(" and fbs.status >= :status ");
		params.put("status", BudgetConstant.STATUE_CHECK);
		sql.append(" and f.item_code is not null ");
		sql.append(" order by f.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
	}
	/**
	 * 获取资金计划数据
	 *
	 * @param timeType  预算类型
	 * @param corpId    单位ID
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @param tableName
	 * @return
	 */
	public List<FbsBudgetProcessReportVO> listFbsBudget(String timeType, String[] corpId, Date beginDate, Date endDate, String tableName,String[] itemCode) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select  f.item_code, f.item_name, i.id as itemId,sum(f.b_money*fbs.EXCHANGE_RATE) bMoney, sum(bt_nvl_num(f.bsuper_money,0.00)*fbs.EXCHANGE_RATE) bsuperMoney, " +
				" sum(bt_nvl_num((f.b_money+f.bsuper_money),0.00)*fbs.EXCHANGE_RATE) bMoneySum, sum(bt_nvl_num(f.btemp_money,0.00)*fbs.EXCHANGE_RATE) btempMoney ");
		sql.append(" from fbs_budget fbs left join ").append(tableName).append(" f on fbs.id = f.budget_id ");
		sql.append(" left join fbs_item i on f.item_code=i.item_code");
		sql.append(" where "+SqlUtil.getInSql(corpId,"fbs.corp_id",true));
		sql.append(" and fbs.time_type = :timeType and fbs.begin_date >= :beginDate and fbs.end_date <= :endDate");
		sql.append(" and "+SqlUtil.getInSql(itemCode,"f.item_code",true));
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		// 只汇总审批结束的
		sql.append(" and fbs.status >= :status ");
		params.put("status", BudgetConstant.STATUE_CHECK);
		sql.append(" and f.item_code is not null ");
		sql.append(" group by f.item_code,f.item_name,i.id ");
		sql.append(" order by f.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
	}
	/**
	 * 获取资金计划执行数据(执行金额取折合人民币)
	 *
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetProcessReportVO> listByFbsBudgetProcess(String corpId, Date beginDate, Date endDate, String itemCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.item_code,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as inProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as inTempProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as outProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as outTempProcessMoney");
		sql.append(" from fbs_budget_process t where t.corp_id = :corpId and t.process_date >= :beginDate and t.process_date <= :endDate ");
		Map<String, Object> params = new HashMap<>();
		params.put("corpId", corpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		sql.append(" group by t.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
	}
	/**
	 * 获取资金计划执行数据(执行金额取折合人民币)
	 *
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetProcessReportVO> listByFbsBudgetProcess(List<String> corpId, Date beginDate, Date endDate, String itemCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.item_code,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as inProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as inTempProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as outProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as outTempProcessMoney");
		sql.append(" from fbs_budget_process t");
		sql.append(" where t.process_date >= :beginDate and t.process_date <= :endDate ");
		sql.append(" and "+SqlUtil.getInSql(corpId," t.corp_id",true));
		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		sql.append(" group by t.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
	}
	/**
	 * 获取资金计划执行数据(执行金额取折合人民币)
	 *
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public FbsBudgetProcessReportVO getFbsBudgetProcess(String corpId, Date beginDate, Date endDate, String itemCode) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		StringBuilder temp = new StringBuilder("and t.item_code = pro.item_code and t.corp_id = :corpId and t.process_date >= :beginDate and t.process_date <= :endDate and t.item_code = :itemCode");
		sql.append(" select pro.item_code, ");
		sql.append(" bt_nvl_num((select sum(t.process_cur_money) from fbs_budget_process t where t.status=95 and t.inner_sign = 1 ");
		sql.append(temp).append(" ),0) inProcessMoney, ");
		sql.append(" bt_nvl_num((select sum(t.process_cur_money) from fbs_budget_process t where t.status>-2 and t.status < 95 and t.inner_sign = 1 ");
		sql.append(temp).append(" ),0) inTempProcessMoney, ");
		sql.append(" bt_nvl_num( (select sum(t.process_cur_money) from fbs_budget_process t where t.status = 95 and t.inner_sign = 0 ");
		sql.append(temp).append(" ),0) outProcessMoney, ");
		sql.append(" bt_nvl_num((select sum(t.process_cur_money) from fbs_budget_process t where t.status>-2 and t.status < 95 and t.inner_sign = 0 ");
		sql.append(temp).append(" ),0) outTempProcessMoney ");
		sql.append(" from fbs_budget_process pro  where pro.corp_id = :corpId   and pro.item_code = :itemCode ");
		params.put("corpId", corpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("itemCode", itemCode);
		sql.append(" group by pro.item_code ");
		return this.getBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
	}
	/**
	 * 获取资金计划追加数据
	 *
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetSuperaddDetailVO> listByFbsBudgetSuperadd(String timeType, String corpId, Date beginDate, Date endDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select sd.item_code,sum(bt_abs(sd.bsuper_money)) bsuper_money from fbs_budget_superadd s ");
		sql.append(" left join fbs_budget_superadd_detail sd on s.id = sd.superadd_id left join fbs_budget fbs on s.budget_id = fbs.id ");
		sql.append(" where fbs.corp_id = :corpId and fbs.time_type = :timeType and fbs.begin_date >= :beginDate and fbs.end_date <= :endDate ");
		// 只汇总审批结束的
		sql.append(" and s.status = 95 ");
		params.put("corpId", corpId);
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		sql.append(" group by sd.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetSuperaddDetailVO.class);
	}
	/**
	 * 获取资金计划追加数据
	 *
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetSuperaddDetailVO> listByFbsBudgetSuperadd(String timeType, List<String> corpId, Date beginDate, Date endDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select sd.item_code,sum(bt_abs(sd.bsuper_money)) bsuper_money from fbs_budget_superadd s ");
		sql.append(" left join fbs_budget_superadd_detail sd on s.id = sd.superadd_id left join fbs_budget fbs on s.budget_id = fbs.id ");
		sql.append(" where  fbs.time_type = :timeType and fbs.begin_date >= :beginDate and fbs.end_date <= :endDate ");
		// 只汇总审批结束的
		sql.append(" and s.status = 95 ");
		sql.append(" and " + SqlUtil.getInSqlByParams(corpId,"fbs.corp_id",params));
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		sql.append(" group by sd.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetSuperaddDetailVO.class);
	}
	/**
	 * 显示资金计划执行明细数据(执行金额取折合人民币)
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesProcessInfo(DataTablesInput dataTablesInput,String joinsql) {
		StringBuilder sql = new StringBuilder("");
		sql.append("select t.id as id,t.CORP_ID as corpId,t.PAYEE_ACC_NAME as payeeAccName,t.ITEM_CODE as itemCode,f.ITEM_NAME as itemName, ");
		sql.append(" t.process_cur_money as processMoney,t.PROCESS_DATE as processDate,t.BILL_NO as billNo,t.BILL_ID as billId,t.status as status, ");
		sql.append(" t.business_Name as businessName,t.process_Sysname as processSysname,t.inner_sign as innerSign,t.cur_id as curId,t.corp_Name as corpName,t.cur_Name as curName ");
		sql.append(" from FBS_BUDGET_PROCESS t left join fbs_item f on t.ITEM_CODE=f.ITEM_CODE where 1 = 1 and status <> -2 ");
		if(EmptyUtils.isNotEmpty(joinsql)){
			sql.append(" and "+joinsql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetProcessVO.class, null, false);
	}
	/**
	 * 显示资金计划追加明细数据
	 * TODO 后续需要同步冗余字段数据
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesSuperaddInfo(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select s.corp_id, sd.item_code, sd.item_name, s.create_date, sd.bsuper_money, s.status,fbs.cur_name as curName, fbs.corp_name as corpName from fbs_budget_superadd s ");
		sql.append(" left join fbs_budget_superadd_detail sd on s.id = sd.superadd_id  ");
		sql.append(" left join fbs_budget fbs on fbs.id = s.budget_id   where 1=1 ");
//		sql.append(" left join bt_currency cur on fbs.cur_id = cur.id   where 1=1 ");
		String timeType =  (String)dataTablesInput.getFormdata().get("timeType");
		sql.append(" and fbs.time_type = " ).append(timeType);
		String corpId = (String)dataTablesInput.getFormdata().get("eqs_corpId");
		if(corpId.contains(",")){
			String[] corpIds = corpId.split(",");
			sql.append(" and " + SqlUtil.getInSql(corpIds,"fbs.corp_id",true));
			dataTablesInput.getFormdata().remove("eqs_corpId");
		}
		//申请日期
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
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetSuperaddViewVO.class, null, false);
	}
	/**
	 * 获取资金计划执行数据
	 * TODO 后续需要同步冗余字段数据
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public FbsBudgetProcessReportVO collectFbsBudgetProcess(String corpId, Date beginDate, Date endDate, String itemCode,Integer itemSign) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select t.item_code,t.corp_name as corpName,i.item_name as itemName,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money*i.sign");
		sql.append(" end)), 0) as inProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money*i.sign");
		sql.append(" end)), 0) as inTempProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money*i.sign");
		sql.append(" end)), 0) as outProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money*i.sign");
		sql.append(" end)), 0) as outTempProcessMoney");
		sql.append(" from fbs_budget_process t left join fbs_item i on t.item_code=i.item_code");
		sql.append(" where t.corp_id = :corpId and t.process_date >= :beginDate and t.process_date <= :endDate and t.item_code like :itemCode");
		params.put("corpId", corpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("itemCode", itemCode + "%");
		sql.append(" group by t.item_code,t.corp_name,i.item_name ");
		List<FbsBudgetProcessReportVO> list = this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
		FbsBudgetProcessReportVO newFbsBudgetProcessReportVO = new FbsBudgetProcessReportVO();
		newFbsBudgetProcessReportVO.setInProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setInTempProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setOutProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setOutTempProcessMoney(BigDecimal.ZERO);
		if (!EmptyUtils.isEmptyList(list)) {
			for(FbsBudgetProcessReportVO vo : list){
				newFbsBudgetProcessReportVO.setInProcessMoney(newFbsBudgetProcessReportVO.getInProcessMoney().add(vo.getInProcessMoney()));
				newFbsBudgetProcessReportVO.setInTempProcessMoney(newFbsBudgetProcessReportVO.getInTempProcessMoney().add(vo.getInTempProcessMoney()));
				newFbsBudgetProcessReportVO.setOutProcessMoney(newFbsBudgetProcessReportVO.getOutProcessMoney().add(vo.getOutProcessMoney()));
				newFbsBudgetProcessReportVO.setOutTempProcessMoney(newFbsBudgetProcessReportVO.getOutTempProcessMoney().add(vo.getOutTempProcessMoney()));
			}
			newFbsBudgetProcessReportVO.setOutProcessMoney(newFbsBudgetProcessReportVO.getOutProcessMoney().multiply(new BigDecimal(itemSign)));
			newFbsBudgetProcessReportVO.setOutTempProcessMoney(newFbsBudgetProcessReportVO.getOutTempProcessMoney().multiply(new BigDecimal(itemSign)));
		}
		return newFbsBudgetProcessReportVO;
	}
	/**
	 * 获取资金计划执行数据
	 * TODO 后续需要同步冗余字段数据
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public FbsBudgetProcessReportVO collectFbsBudgetProcess(List<String> corpId, Date beginDate, Date endDate, String itemCode) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select t.item_code,t.corp_name as corpName,i.item_name as itemName,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as inProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 1 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as inTempProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status = 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as outProcessMoney,");
		sql.append(" bt_nvl_num(sum((case");
		sql.append(" when t.status > -2 and t.status < 95 and t.inner_sign = 0 then");
		sql.append(" t.process_cur_money");
		sql.append(" end)), 0) as outTempProcessMoney");
		sql.append(" from fbs_budget_process t left join fbs_item i on t.item_code=i.item_code");
		sql.append(" where t.process_date >= :beginDate and t.process_date <= :endDate and t.item_code like :itemCode");
		sql.append(" and "+SqlUtil.getInSql(corpId," t.corp_id",true));
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("itemCode", itemCode + "%");
		sql.append(" group by t.item_code ,t.corp_name, i.item_name");
		List<FbsBudgetProcessReportVO> list = this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
		FbsBudgetProcessReportVO newFbsBudgetProcessReportVO = new FbsBudgetProcessReportVO();
		newFbsBudgetProcessReportVO.setInProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setInTempProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setOutProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setOutTempProcessMoney(BigDecimal.ZERO);
		if (!EmptyUtils.isEmptyList(list)) {
			for(FbsBudgetProcessReportVO vo : list){
				newFbsBudgetProcessReportVO.setInProcessMoney(newFbsBudgetProcessReportVO.getInProcessMoney().add(vo.getInProcessMoney()));
				newFbsBudgetProcessReportVO.setInTempProcessMoney(newFbsBudgetProcessReportVO.getInTempProcessMoney().add(vo.getInTempProcessMoney()));
				newFbsBudgetProcessReportVO.setOutProcessMoney(newFbsBudgetProcessReportVO.getOutProcessMoney().add(vo.getOutProcessMoney()));
				newFbsBudgetProcessReportVO.setOutTempProcessMoney(newFbsBudgetProcessReportVO.getOutTempProcessMoney().add(vo.getOutTempProcessMoney()));
			}
		}
		return newFbsBudgetProcessReportVO;
	}
	/**
	 * 手机端显示资金计划执行明细数据(执行金额取折合人民币)
	 * @param itemCode
	 * @param corpId
	 * @param beginDate
	 * @param endDate
	 * @param statusFlag
	 * @return
	 */
	public List<FbsBudgetProcessVO> mobileFbsBudgetProessDetailList(String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag) {
		StringBuilder sql = new StringBuilder("");
		sql.append("select t.id as id,t.CORP_ID as corpId,t.PAYEE_ACC_NAME as payeeAccName,t.ITEM_CODE as itemCode,f.ITEM_NAME as itemName, ");
		sql.append(" t.process_cur_money as processMoney,t.PROCESS_DATE as processDate,t.BILL_NO as billNo,t.BILL_ID as billId,t.status as status, ");
		sql.append(" t.business_Name as businessName,t.process_Sysname as processSysname,t.inner_sign as innerSign ");
		sql.append(" from FBS_BUDGET_PROCESS t left join fbs_item f on t.ITEM_CODE=f.ITEM_CODE where 1 = 1 and status <> -2 ");
		Map<String, Object> params = new HashMap<>();
		if(StringUtils.isNotEmpty(itemCode)){
			sql.append(" and t.item_code = :itemCode ");
			params.put("itemCode",itemCode);
		}
		if(StringUtils.isNotEmpty(corpId)){
			sql.append(" and t.corp_id = :corpId ");
			params.put("corpId",corpId);
		}
		if(StringUtils.isNotEmpty(beginDate)){
			sql.append(" and t.process_Date >= :beginDate ");
			params.put("beginDate",beginDate);
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and t.process_Date >= :endDate ");
			params.put("endDate",endDate);
		}
		if(statusFlag){
			sql.append(" and t.status = 95 ");
		}else{
			sql.append(" and t.status > -2 and t.status < 95 ");
		}
		return this.listBySQLAliasToBean(sql.toString(),params,FbsBudgetProcessVO.class);
	}
	/**
	 * 获取明细执行
	 * TODO 后续需要同步冗余字段数据
	 * @param beginDate
	 * @param endDate
	 * @param itemCode
	 * @param corpList
	 * @param reportVO
	 * @return
	 */
	public FbsBudgetProcessReportVO getBudgetProcessMoneyByDetail(Date beginDate, Date endDate, String itemCode, List<String> corpList, FbsBudgetProcessReportVO reportVO) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		StringBuilder temp = new StringBuilder("and t.item_code = pro.item_code  and t.process_date >= :beginDate and t.process_date <= :endDate and t.item_code like :itemCode ");
		temp.append(" and t.payee_acc_name = :payeeAccName ");
		temp.append(" and "+SqlUtil.getInSql(corpList," t.corp_id",true));
		sql.append(" select pro.item_code, pro.corp_name as corpName, i.item_name as itemName,");
		sql.append(" bt_nvl_num((select sum(t.process_cur_money) from fbs_budget_process t where t.status=95 and t.inner_sign = 1 ");
		sql.append(temp).append(" ),0) inProcessMoney, ");
		sql.append(" bt_nvl_num((select sum(t.process_cur_money) from fbs_budget_process t where t.status>-2 and t.status < 95 and t.inner_sign = 1 ");
		sql.append(temp).append(" ),0) inTempProcessMoney, ");
		sql.append(" bt_nvl_num( (select sum(t.process_cur_money) from fbs_budget_process t where t.status = 95 and t.inner_sign = 0 ");
		sql.append(temp).append(" ),0) outProcessMoney, ");
		sql.append(" bt_nvl_num((select sum(t.process_cur_money) from fbs_budget_process t where t.status>-2 and t.status < 95 and t.inner_sign = 0 ");
		sql.append(temp).append(" ),0) outTempProcessMoney ");
		sql.append(" from fbs_budget_process pro  left join fbs_item i on pro.item_code=i.item_code where   ");
		sql.append(SqlUtil.getInSql(corpList,"pro.corp_id",true));
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("itemCode", itemCode + "%");
		sql.append("and pro.process_date >= :beginDate and pro.process_date <= :endDate and pro.item_code like :itemCode ");
		sql.append(" and pro.payee_acc_name = :payeeAccName ");
		params.put("payeeAccName", reportVO.getPayeeAccName());
		sql.append(" group by pro.item_code,pro.payee_acc_name ");
		List<FbsBudgetProcessReportVO> list = this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessReportVO.class);
		FbsBudgetProcessReportVO newFbsBudgetProcessReportVO = new FbsBudgetProcessReportVO();
		newFbsBudgetProcessReportVO.setInProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setInTempProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setOutProcessMoney(BigDecimal.ZERO);
		newFbsBudgetProcessReportVO.setOutTempProcessMoney(BigDecimal.ZERO);
		if (!EmptyUtils.isEmptyList(list)) {
			for(FbsBudgetProcessReportVO vo : list){
				newFbsBudgetProcessReportVO.setInProcessMoney(newFbsBudgetProcessReportVO.getInProcessMoney().add(vo.getInProcessMoney()));
				newFbsBudgetProcessReportVO.setInTempProcessMoney(newFbsBudgetProcessReportVO.getInTempProcessMoney().add(vo.getInTempProcessMoney()));
				newFbsBudgetProcessReportVO.setOutProcessMoney(newFbsBudgetProcessReportVO.getOutProcessMoney().add(vo.getOutProcessMoney()));
				newFbsBudgetProcessReportVO.setOutTempProcessMoney(newFbsBudgetProcessReportVO.getOutTempProcessMoney().add(vo.getOutTempProcessMoney()));
			}
		}
		return newFbsBudgetProcessReportVO;
	}
	/**
	 * 查询资金计划明细执行
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesDetailProcessInfo(DataTablesInput dataTablesInput, String tableName) {
		StringBuilder sql = new StringBuilder("");
		sql.append("select b.corp_id as corpId,b.corp_name as corpName,t.payee_acc_name as payeeAccName,t.item_code as itemCode,t.item_name as itemName, ");
		sql.append(" t.b_money as bMoney,t.bsuper_Money as bsuperMoney,t.btemp_Money as btempMoney,b.begin_Date as beginDate,b.end_Date as endDate ");
		sql.append(" from fbs_budget_detail t left join " + tableName + " fbs on t.detail_id=fbs.id");
		sql.append(" left join fbs_budget b on b.id=fbs.budget_id where 1 = 1");
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetProcessReportVO.class, null, false);
	}
}
