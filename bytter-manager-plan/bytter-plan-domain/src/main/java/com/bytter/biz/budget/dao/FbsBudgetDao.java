package com.bytter.biz.budget.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.bytter.core.constant.BudgetConstant;
import org.springframework.stereotype.Repository;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.vo.FbsBudgetSuperAddApproveVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 资金计划上报主表
 * @date 2017-07-14 14:51:48
 */
@Repository
public class FbsBudgetDao extends BaseDao<FbsBudget, String> {
	/**
	 * 根据单位ID和周期类型获取对应的预算上报信息
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public FbsBudget getCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		String hql = " from FbsBudget b where b.corpId = :corpId and b.timeType = :timeType "
				+ "and b.beginDate = :beginDate and b.endDate = :endDate ";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		List<FbsBudget> list=this.listByHQL(hql,params);
		FbsBudget budget=null;
		if(list!=null && list.size()>0){
			budget=list.get(0);
			for(FbsBudget budgetNew:list){
				if(budgetNew.getStatus()>budget.getStatus()){
					budget=budgetNew;
				}
			}
		}
		return budget;
	}
	/**
	 * 根据单位ID集合和周期类型获取对应的预算上报信息
	 *
	 * @param corpIds
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudget> listCorpBudget(List<String> corpIds, String timeType, Date beginDate, Date endDate) {
		String hql = " from FbsBudget b where b.timeType = :timeType "
				+ "and b.beginDate = :beginDate and b.endDate = :endDate ";
		hql += " and " + SqlUtil.getInSql(corpIds, "b.corpId", true);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return this.listByHQL(hql,params);
	}
	/**
	 * 根据单位ID和周期类型获取对应的预算上报信息
	 *
	 * @param id
	 * @return
	 */
	public FbsBudgetVO getByUnion(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select b.id as id,b.corpId as corpId,b.enrolDate as enrolDate,b.beginDate as beginDate,");
		hql.append(" b.endDate as endDate,b.timeType as timeType,b.checkMoney as checkMoney,b.status as status,b.attachmentId as attachmentId, ");
		hql.append(" b.curId as curId,b.exchangeRate as exchangeRate,b.corpName as corpName,b.curName as curName,");
		hql.append(" b.createBy as createBy, b.createDate as createDate, b.updateBy as updateBy, b.updateDate as updateDate ");
		hql.append(" from FbsBudget b where b.id = :id ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getByHQLAliasToBean(hql.toString(), params, FbsBudgetVO.class);
	}
	/**
	 * 根据单位ID和周期类型获取往期上报完成的计划
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudget> getPreCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		String hql = " from FbsBudget b where b.corpId = :corpId and b.timeType = :timeType "
				+ "and b.beginDate >= :beginDate and b.endDate <= :endDate and b.status>=:status";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("status", BudgetConstant.STATUE_CHECK);
		return (List<FbsBudget>) this.getSession().createQuery(hql).setProperties(params).list();
	}
	/**
	 * 查询审核列表信息
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fbs.id as id ,fbs.corp_id as corpId , fbs.enrol_date as enrolDate, fbs.begin_date as beginDate ,");
		sql.append(" fbs.end_date as endDate , fbs.time_type as timeType, fbs.cur_Id as curId,fbs.exchange_Rate as exchangeRate,fbs.corp_name as corpName,fbs.cur_name as curName ");
		sql.append(" from fbs_budget fbs ");
		sql.append(" where 1=1");
		sql.append(" and ").append(businessSql);
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetVO.class, "fbs", false);
	}
	/**
	 * 查询审核列表信息
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	public DataTablesOutput dataTableSuperAddApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fbs.id as id ,super.id as superaddId,fbs.corp_id as corpId , fbs.enrol_date as enrolDate, fbs.begin_date as beginDate ,");
		sql.append(" fbs.end_date as endDate , fbs.time_type as timeType, fbs.cur_Id as curId,fbs.exchange_Rate as exchangeRate,fbs.corp_name as corpName,fbs.cur_name as curName ");
		sql.append(" from fbs_budget fbs,fbs_budget_superadd super ");
		sql.append(" where  fbs.id = super.budget_id ");
		sql.append(" and ").append(businessSql);
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetSuperAddApproveVO.class, "fbs", false);
	}
	/**
	 * 获取单位上报的资金计划
	 *
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @return
	 */
	public List<FbsBudget> getCorpBudget(String corpId, String timeType, Date beginDate) {
		String hql = " from FbsBudget b where b.corpId = :corpId and b.timeType = :timeType "
				+ "and b.beginDate >= :beginDate ";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		return (List<FbsBudget>) this.getSession().createQuery(hql).setProperties(params).list();
	}
	/**
	 * 获取时间段内上报的计划
	 * @param corpId
	 * @param timeType
	 * @param beginStartDate
	 * @param beginEndDate
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudget> listBudgetByBeginDate(String corpId, String timeType, Date beginStartDate, Date beginEndDate, String budgetId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from FbsBudget b where b.corpId = :corpId ");
		hql.append(" and b.beginDate >= :beginStartDate and b.beginDate <= :beginEndDate");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("corpId", corpId);
		if(StringUtils.isNotBlank(timeType)) {
			hql.append(" and b.timeType = :timeType");
			params.put("timeType", timeType);
		}
		params.put("beginStartDate", beginStartDate);
		params.put("beginEndDate", beginEndDate);
		if(StringUtils.isNotBlank(budgetId)){
			hql.append(" and b.id != :budgetId");
			params.put("budgetId", budgetId);
		}
		return (List<FbsBudget>) this.getSession().createQuery(hql.toString()).setProperties(params).list();
	}
	/**
	 * 根据单位统计上报的资金计划
	 * @param corpId
	 * @param busType del:单位删除 disable:禁用 change:上级变更
	 * @return
	 */
	public int getBudgetCountByCorpId(String corpId, String busType){
		String sql="select count(*) from fbs_budget where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		if(!"del".equals(busType)){
			//0-暂存、11-待审批、-1-审批打回、80-待汇总、81-汇总中、82-汇总审批中、83-汇总审批打回
			sql+=" and status in ('0','11','-1','80','81','82','83')";
		}
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 手机端上报情况表
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetVO> getBudgetInputMoblieList(String corpId, String timeType, String beginDate,String endDate,String  joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fbs.id as id, fbs.corp_id as corpId, fbs.enrol_date as enrolDate, fbs.begin_date as beginDate,");
		sql.append(" fbs.end_date as endDate, fbs.time_type as timeType, fbs.status as status,fbs.cur_id as curId ");
		sql.append(" from fbs_budget fbs where 1 = 1 and fbs.status = 95 ");
//		sql.append(" from fbs_budget fbs left join bt_currency cur on fbs.cur_id = cur.id where 1 = 1 and fbs.status = 95 ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(corpId)){
			sql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "fbs.corp_id", true));
		}
		if(StringUtils.isNotEmpty(timeType)){
			sql.append(" and fbs.time_type = :timeType");
			params.put("timeType", timeType);
		}
		if(StringUtils.isNotEmpty(beginDate)){
			sql.append(" and bt_to_char (fbs.begin_date, 'YYYY-MM-DD' ) >= :beginDate");
			params.put("beginDate", beginDate);
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql.append(" and bt_to_char (fbs.end_date, 'YYYY-MM-DD' ) <= :endDate");
			params.put("endDate", endDate);
		}
		sql.append(" and "+joinSql);
		return listBySQLAliasToBean(sql.toString(),params,FbsBudgetVO.class);
	}

    public List<FbsBudget> listByCorpids(List<String> corpIds){
        StringBuilder hql = new StringBuilder();
        hql.append(" from FbsBudget b where b.corpId in (:corpIds) ");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpIds", corpIds);
        return (List<FbsBudget>) this.getSession().createQuery(hql.toString()).setProperties(params).list();
    }
}
