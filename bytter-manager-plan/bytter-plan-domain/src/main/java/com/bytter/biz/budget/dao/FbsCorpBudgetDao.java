package com.bytter.biz.budget.dao;

import com.bytter.biz.budget.entity.FbsCorpBudget;
import com.bytter.biz.budget.vo.CorpVO;
import com.bytter.biz.budget.vo.FbsCorpBudgetVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FBS_CORP_ITEM
 * @date 2017-07-11 10:46:42
 */
@Repository
public class FbsCorpBudgetDao extends BaseDao<FbsCorpBudget, String> {
    private static final String corpSql = "select id,account_licence,address,city,code,collect_flag,ctl_budget,head_person,listed_company,\n" +
            "name,parent_id,parent_ids,province,sort,status,type,use_account_code,net_id,rat_group,create_time,\n" +
            "create_by,update_time,update_by,is_limit_quota,day_limit_quota,month_limit_quota,name_en,address_en,\n" +
            "cur_id,unit_attribute,unit_cm,country_id,cert_type,cert_no,soc_code,auto_signnature\n" +
            " from sys_corp \n" +
            " union all\n" +
            "  select id,account_licence,address,city,code,collect_flag,ctl_budget,head_person,listed_company,\n" +
            "name,parent_id,parent_ids,province,sort,status,type,use_account_code,net_id,rat_group,create_time,\n" +
            "create_by,update_time,update_by,is_limit_quota,day_limit_quota,month_limit_quota,name_en,address_en,\n" +
            "cur_id,unit_attribute,unit_cm,country_id,cert_type,cert_no,soc_code,auto_signnature \n" +
            "  from sys_corp_manager";
	/**
	 * 根据单位id设置上报标志
	 *
	 * @param reportCorpIds
	 */
	public void updateReportBudget(List<String> reportCorpIds) {
		if (!EmptyUtils.isEmptyList(reportCorpIds) && !EmptyUtils.isBlank(reportCorpIds.get(0))) {
			createSQLQuery("update fbs_corp_budget set report_budget=0 where "+SqlUtil.getNotinSql(reportCorpIds,"corp_id",true)).executeUpdate();
			createSQLQuery("update fbs_corp_budget set report_budget=1 where "+getSQLIn(reportCorpIds,900,"corp_id")).executeUpdate();
		} else {
			createSQLQuery("update fbs_corp_budget set report_budget=0").executeUpdate();
		}
	}
	/**
	 * 根据单位id设置控制标志
	 *
	 * @param controlCorpIds
	 */
	public void updateControlBudget(List<String> controlCorpIds) {
		if (!EmptyUtils.isEmptyList(controlCorpIds) && !EmptyUtils.isBlank(controlCorpIds.get(0))) {
			createSQLQuery("update fbs_corp_budget set control_budget=0 where " + SqlUtil.getNotinSql(controlCorpIds, "corp_id", true)).executeUpdate();
			createSQLQuery("update fbs_corp_budget set control_budget=1 where " + getSQLIn(controlCorpIds, 900, "corp_id")).executeUpdate();
		} else {
			createSQLQuery("update fbs_corp_budget set control_budget=0").executeUpdate();
		}
	}
	/**
	 * 根据单位id设置汇总标志
	 *
	 * @param corpIds
	 */
	public void updateCollectFlag(List<String> corpIds) {
		if (!EmptyUtils.isEmptyList(corpIds) && !EmptyUtils.isBlank(corpIds.get(0))) {
			createSQLQuery("update fbs_corp_budget set collect_flag=0 where " + SqlUtil.getNotinSql(corpIds, "corp_id", true)).executeUpdate();
			createSQLQuery("update fbs_corp_budget set collect_flag=1 where " + getSQLIn(corpIds, 900, "corp_id")).executeUpdate();
		} else {
			createSQLQuery("update fbs_corp_budget set collect_flag=0").executeUpdate();
		}
	}
	/**
	 * 查询未保存的资金计划控制表信息
	 *
	 * @return
	 */
	public List<FbsCorpBudget> listFbsCorpBudgetByNotCorp() {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		//改为在service层 获取差集
		/*sql.append(" select c.id corpId, 0 collectFlag, 0 reportBudget, 0 controlBudget from sys_corp c ");
		sql.append(" where c.status =1 and not exists (select 1 from fbs_corp_budget fcb where fcb.corp_id = c.id) ");
		sql.append(" order by c.sort, c.code ");*/
		sql.append(" select fcb.corp_id as corpId, 0 collectFlag, 0 reportBudget, 0 controlBudget from fbs_corp_budget fcb ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudget.class);
	}
	/**
	 * 查询资金计划控制表
	 * @param corpIds 查询条件-单位 ids
	 * @return
	 */
	public List<FbsCorpBudgetVO> listFbsCorpBudget(List<String> corpIds) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		/*sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
		sql.append(" bt_nvl_num(fcb.collect_flag, 0) collectFlag,bt_nvl_num(fcb.report_budget, 0) reportBudget, bt_nvl_num(fcb.control_budget, 0) controlBudget, ");
		sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
		sql.append(" from sys_corp c left join fbs_corp_budget fcb on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 ");
		if (!EmptyUtils.isBlank(seach)) {
			sql.append(" and (c.code like :seach or c.name like :seach) ");
			params.put("seach", "%" + seach + "%");
		}
		sql.append(" order by c.sort, c.code ");
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);*/
		sql.append(" select fcb.corp_id corpId, bt_nvl_num(fcb.collect_flag, 0) collectFlag,bt_nvl_num(fcb.report_budget, 0) reportBudget, bt_nvl_num(fcb.control_budget, 0) controlBudget ");
		sql.append(" from fbs_corp_budget fcb where 1=1 ");
		sql.append(" and ").append(SqlUtil.getInSql(corpIds, "fcb.corp_id", true));
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
	}
	/**
	 * 查询资金计划控制表
	 * @param seach 查询条件-单位编码或单位名称
	 * @return
	 */
	public List<FbsCorpBudgetVO> listFbsCorpBudgets(String seach) {
		//改为调用：ISystemForeignService.listCorpBySeach(String seach);
		return null;
		/*StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
		sql.append(" fcb.collect_flag as collectFlag,fcb.report_budget as reportBudget,fcb.control_budget as controlBudget ");
		sql.append(" from sys_corp c left join fbs_corp_budget fcb on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 ");
		if (!EmptyUtils.isBlank(seach)) {
			sql.append(" and (c.code like :seach or c.name like :seach) ");
			params.put("seach", "%" + seach + "%");
		}
		sql.append(" order by c.sort, c.code ");
		List<FbsCorpBudgetVO> fbsCorpBudgetVOList = this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
		if(EmptyUtils.isEmpty(fbsCorpBudgetVOList)){
			return fbsCorpBudgetVOList;
		}
		List<String> corpIds= Lists.newArrayList();
		for(FbsCorpBudgetVO fbsCorpBudgetVO:fbsCorpBudgetVOList){
			corpIds.add(fbsCorpBudgetVO.getCorpId());
		}
		String totalSqlSelect="SELECT c1.parent_id as corpId,count(*) as childCount FROM sys_corp c1 WHERE c1.status =1 and ";
		String parentIdCon=SqlUtil.getInSql(corpIds,"c1.parent_id",true);
		String totalSql=totalSqlSelect+parentIdCon+" group by c1.parent_id";
		List<Map> mapList = listBySQL(totalSql,null);
		Map<String,Integer> corpId2ChildCount = Maps.newHashMap();
		for(Map<String,Object> map:mapList){
			if(map.get("corpId") != null){
				BigDecimal childCount = NumberUtil.objToBigDecimal(map.get("childCount"));
				corpId2ChildCount.put((String) map.get("corpId"), childCount.intValue());
			} else {
				BigDecimal childCount = NumberUtil.objToBigDecimal(map.get("CHILDCOUNT"));
				corpId2ChildCount.put((String) map.get("CORPID"), childCount.intValue());
			}
		}
		for(FbsCorpBudgetVO fbsCorpBudgetVO:fbsCorpBudgetVOList){
			fbsCorpBudgetVO.setChildCount(corpId2ChildCount.get(fbsCorpBudgetVO.getCorpId()));
		}
		return fbsCorpBudgetVOList;*/
	}
	//这块查询逻辑 得写在 sys模块
	/*public List<CorpVO> listCorp(String netId){
		StringBuilder sql=new StringBuilder();
		Map<String , Object> param=new HashMap<>();
		sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId,");
		sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount");
		sql.append(" from sys_corp c  where c.status = 1 ");
		if (!EmptyUtils.isBlank(netId)) {
			sql.append(" and c.net_id = :netId or c.id= :netId");
			param.put("netId", netId );
		}
		sql.append(" order by c.sort, c.code ");
		return this.listBySQLAliasToBean(sql.toString(), param, CorpVO.class);
	}*/
	/**
	 * 查询资金计划控制表
	 * @param seach 查询条件-单位编码或单位名称
	 * 改为从sys模块调用
	 * @return
	 */
	/*public List<String> listCorpIdsBySeach(String seach) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select c.id corpId  from sys_corp c ");
		sql.append(" where c.status = 1 ");
		if (!EmptyUtils.isBlank(seach)) {
			sql.append(" and (c.code like :seach or c.name like :seach) ");
			params.put("seach", "%" + seach + "%");
		}
		sql.append(" order by c.sort, c.code ");
		return this.createSQLQuery(sql.toString()).setProperties(params).list();
	}*/
	/**
	 * 获取可上报的单位
	 * @return
	 */
	public List<FbsCorpBudgetVO> listFbsCorpBudgetVO(Integer reportBudget, Integer controlBudget, Integer collectFlag) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		/*sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
		sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget, ");
		sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1) childCount ");
		sql.append(" from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 ");
		if (!EmptyUtils.isEmpty(reportBudget)) {
			sql.append(" and fcb.report_budget = :reportBudget ");
			params.put("reportBudget", reportBudget);
		}
		if (!EmptyUtils.isEmpty(controlBudget)) {
			sql.append(" and fcb.control_budget = :controlBudget ");
			params.put("controlBudget", controlBudget);
		}
		if (!EmptyUtils.isEmpty(collectFlag)) {
			sql.append(" and fcb.collect_flag = :collectFlag ");
			params.put("collectFlag", collectFlag);
		}
		sql.append(" order by c.sort, c.code ");*/
		sql.append(" select fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
		sql.append(" from fbs_corp_budget fcb where 1 = 1 ");
		if (!EmptyUtils.isEmpty(reportBudget)) {
			sql.append(" and fcb.report_budget = :reportBudget ");
			params.put("reportBudget", reportBudget);
		}
		if (!EmptyUtils.isEmpty(controlBudget)) {
			sql.append(" and fcb.control_budget = :controlBudget ");
			params.put("controlBudget", controlBudget);
		}
		if (!EmptyUtils.isEmpty(collectFlag)) {
			sql.append(" and fcb.collect_flag = :collectFlag ");
			params.put("collectFlag", collectFlag);
		}
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
	}
	/**
	 * 获取可上报的下属单位
	 * @param parentCorpId
	 * @return
	 */
	public List<FbsCorpBudgetVO> listReportBudgetByParentCorpId(String parentCorpId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
		sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget, ");
		sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1) childCount ");
		sql.append(" from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 and fcb.report_budget = :reportBudget and c.parent_id = :parentCorpId ");
		params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
		params.put("parentCorpId", parentCorpId);
		sql.append(" order by c.sort, c.code ");
		// sql.append(" select fcb.corp_id corpId, fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
		// sql.append(" from fbs_corp_budget fcb where fcb.report_budget = :reportBudget ");
		// params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
	}
	/**
	 * 获取可汇总的下属单位
	 * 必须可上报才能可汇总
	 * @param parentCorpId
	 * @return
	 */
	public List<FbsCorpBudgetVO> listReportAndCollectByParentCorpId(String parentCorpId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
		sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget, ");
		sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1) childCount ");
		sql.append(" from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 and fcb.report_budget = :reportBudget and fcb.collect_flag = :collectFlag and c.parent_id = :parentCorpId ");
		params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
		params.put("collectFlag", BudgetConstant.CORP_BUDGET_COLLECT_FLAG_YES);
		params.put("parentCorpId", parentCorpId);
		sql.append(" order by c.sort, c.code ");
		// sql.append(" select fcb.corp_id corpId, fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
		// sql.append(" from fbs_corp_budget fcb where fcb.report_budget = :reportBudget and fcb.collect_flag = :collectFlag ");
		// params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
		// params.put("collectFlag", BudgetConstant.CORP_BUDGET_COLLECT_FLAG_YES);
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
	}
	/**
	 * 获取汇总单位集合
	 *
	 * @param parentCorpIds
	 * @return
	 */
	public List<FbsCorpBudgetVO> listReportAndCollectByParentCorpId(List<String> parentCorpIds) {
		//没有条件查询排序，可以不增加冗余字段；
		//根据 parentCorpIds,c.status = 1 查询 sys_corp表，返回 corp_id,parent_id；拿sys_corp 返回的数据
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		/*sql.append(" select distinct c.parent_id as corpId from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 and fcb.report_budget = :reportBudget and fcb.collect_flag = :collectFlag ");
		sql.append(" and " + SqlUtil.getInSql(parentCorpIds, "c.parent_id", true));*/
		sql.append(" select fcb.id as id, fcb.corp_id as corpId from fbs_corp_budget fcb ");
		sql.append(" where fcb.report_budget = :reportBudget and fcb.collect_flag = :collectFlag ");
		params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
		params.put("collectFlag", BudgetConstant.CORP_BUDGET_COLLECT_FLAG_YES);
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
	}
	/**
	 * 根据单位ID查询
	 *
	 * @param corpId
	 * @return
	 */
	public FbsCorpBudgetVO getCorpBudgetVOByCorpId(String corpId) {
		//sys_corp 的查询，通过调用接口查询
		//拆解成 fcb.corp_id = corpId，返回 fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		/*sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
		sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
		sql.append(" from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 and c.id = :corpId ");
		params.put("corpId", corpId);
		sql.append(" order by c.sort, c.code ");*/
		sql.append(" select fcb.corp_id corpId, fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
		sql.append(" from fbs_corp_budget fcb where fcb.corp_id = :corpId ");
		params.put("corpId", corpId);
		return this.getBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
	}
	/**
	 * 根据单位集合获取单位资金计划设置表
	 *
	 * @return
	 */
	public List<FbsCorpBudgetVO> listByCorpIds(List<String> corpIds) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		/*sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
		sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
		sql.append(" from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
		sql.append(" where c.status = 1 ");
		if (!EmptyUtils.isEmptyList(corpIds)) {
			sql.append(" and ").append(SqlUtil.getInSql(corpIds, "c.id", true));
		}
		sql.append(" order by c.sort, c.code ");*/
		sql.append(" select fcb.corp_id corpId, fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
		sql.append(" from fbs_corp_budget fcb where 1=1 ");
		if (!EmptyUtils.isEmptyList(corpIds)) {
			sql.append(" and ").append(SqlUtil.getInSql(corpIds, "fcb.corp_id", true));
		}
		return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
	}
	/**
	 * 获取具有上报权限的单位集合
	 *
	 * @return
	 */
	public List<String> listCorpIdsByReportBudget(List<String> corpIds) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		//sql.append(" select fcb.corp_id from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id where c.status = 1 and fcb.report_budget = :reportBudget ");
		sql.append(" select fcb.corp_id from fbs_corp_budget fcb where fcb.report_budget = :reportBudget ");
		params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
		if (!EmptyUtils.isEmptyList(corpIds)) {
			sql.append(" and ").append(SqlUtil.getInSql(corpIds, "fcb.corp_id", true));
		}
		return this.createSQLQuery(sql.toString()).setProperties(params).list();
	}
	/**
	 * 根据单位统计
	 * @param corpId
	 * @return
	 */
	public int getCorpBudgetCountByCorpId(String corpId, String flag){
		String sql="select count(*) from fbs_corp_budget where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		if(CommonConstant.NO.equals(flag)){
			sql+=" and collect_flag=:collectFlag";
			params.put("collectFlag",CommonConstant.YES);
		}else if(CommonConstant.YES.equals(flag)){
			sql+=" and (control_budget=:controlBudget or report_budget=:reportBudget)";
			params.put("controlBudget",CommonConstant.YES);
			params.put("reportBudget",CommonConstant.YES);
		}
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}

    /**
     * 获取可汇总的下属单位
     * 必须可上报才能可汇总
     *
     * @param parentCorpIds
     * @return
     */
    public List<FbsCorpBudgetVO> listReportAndCollectByParentCorpIds(List<String> parentCorpIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
        sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget, ");
        sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1) childCount ");
        sql.append(" from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
        sql.append(" where c.status = 1 and fcb.report_budget = :reportBudget and fcb.collect_flag = :collectFlag and c.parent_id in (:parentCorpIds) ");
        params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
        params.put("collectFlag", BudgetConstant.CORP_BUDGET_COLLECT_FLAG_YES);
        params.put("parentCorpIds", parentCorpIds);
        sql.append(" order by c.sort, c.code ");
        return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
    }


    /**
     * 获取可上报的下属单位
     *
     * @param parentCorpIds
     * @return
     */
    public List<FbsCorpBudgetVO> listReportBudgetByParentCorpId(List<String> parentCorpIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
        sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget, ");
        sql.append(" (select count(*) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1) childCount ");
        sql.append(" from fbs_corp_budget fcb left join sys_corp c on c.id = fcb.corp_id ");
        sql.append(" where c.status = 1 and fcb.report_budget = :reportBudget and c.parent_id in (:parentCorpIds) ");
        params.put("reportBudget", BudgetConstant.CORP_BUDGET_REPORT_BUDGET_YES);
        params.put("parentCorpIds", parentCorpIds);
        sql.append(" order by c.sort, c.code ");
        return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
    }

    /**
     * 根据单位ID查询
     *
     * @param corpIds
     * @return
     */
    public List<FbsCorpBudgetVO> queryCorpBudgetVOByCorpId(List<String> corpIds,String releaseId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select c.id corpId,c.name corpName,c.code corpCode, o.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
        sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
        sql.append(" from fbs_corp_budget fcb left join ("+corpSql+") c on c.id = fcb.corp_id ");
        sql.append(" left join sys_organtype_corp_release o on c.id = o.corp_id and release_id= :releaseId ");
        sql.append(" where c.status = 1 and c.id in (:corpIds)  ");
        params.put("corpIds", corpIds);
        params.put("releaseId", releaseId);
        sql.append(" order by c.sort, c.code ");
        return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
    }

    /**
     * 根据单位ID查询
     *
     * @param corpIds
     * @return
     */
    public  List<FbsCorpBudgetVO>  queryCorpBudgetVOByCorpId(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId, ");
        sql.append(" fcb.collect_flag collectFlag, fcb.report_budget reportBudget, fcb.control_budget controlBudget ");
        sql.append(" from fbs_corp_budget fcb left join ("+corpSql+") c on c.id = fcb.corp_id ");
        sql.append(" where c.status = 1 and c.id in( :corpIds ) ");
        params.put("corpIds", corpIds);
        sql.append(" order by c.sort, c.code ");
        return this.listBySQLAliasToBean(sql.toString(), params, FbsCorpBudgetVO.class);
    }

	public List<CorpVO> listCorp(String netId){
		StringBuilder sql=new StringBuilder();
		Map<String , Object> param=new HashMap<>();
		sql.append(" select c.id corpId,c.name corpName,c.code corpCode, c.parent_id corpParentId, c.type corpType, c.net_id corpNetId,");
		sql.append(" (select count(1) from sys_corp c1 where c1.parent_id = c.id and c1.status = 1 ) childCount");
		sql.append(" from sys_corp c  where c.status = 1 ");
		if (!EmptyUtils.isBlank(netId)) {
			if(netId.contains(",")){
				String[] netIds = netId.split(",");
				sql.append(" and c.net_id in (:netIds) or c.id in (:netIds)");
				param.put("netIds", netIds );
			}else {
				sql.append(" and c.net_id = :netId or c.id= :netId");
				param.put("netId", netId );
			}
		}
		sql.append(" order by c.sort, c.code ");
		return this.listBySQLAliasToBean(sql.toString(), param, CorpVO.class);
	}
}
