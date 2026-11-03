package com.bytter.report.domain.stock.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.stock.vo.FbsBudgetProcessRateVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * 计划执行进度表
 *
 * @author huangx 2018-12-1
 */
@Repository
public class FbsBudgetProcessRateDao extends BaseDao<FbsBudgetProcessRateVO, String> {
    /**
     * 计划执行进度
     * @param corpId
     * @param tableName
     * @param beginDate
     * @param endDate
     * @param joinSql
     * @return
     */
    public List<FbsBudgetProcessRateVO> queryBudgetProcessRate(String corpId, String timeType, Date beginDate, Date endDate, String tableName, String joinSql){
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.corp_id as corpId,t.enrol_date as enrolDate,t.begin_date as beginDate,t.end_date as endDate,t.time_type as timeType,t.cur_id as curId,t.exchange_Rate as exchangeRate,");
        sql.append(" (select sum(f.b_money+f.bsuper_money) from "+tableName+" f where f.budget_id=t.id and f.item_code in ('"+ BudgetConstant.ITEM_CODE_200+"','"+BudgetConstant.ITEM_CODE_400+"')) as bMoney,");
        sql.append(" (select sum(p.process_cur_money) from fbs_budget_process p where p.status="+BudgetConstant.STATUE_CHECK+" and p.process_date>=t.begin_date and p.process_date<=t.end_date and p.corp_id=t.corp_id) as processMoney");
        sql.append(" from fbs_budget t where t.time_type = :timeType and t.status >= "+BudgetConstant.STATUE_CHECK);
        sql.append(" and t.begin_date >= :beginDate and t.end_date <= :endDate");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(corpId)){
            sql.append(" and "+ SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT),"t.corp_id",true));
        }
        sql.append(" order by t.corp_id,t.begin_date");
        params.put("timeType", timeType);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessRateVO.class);
    }
    /**
     * 根据计划Id获取明细列表
     *
     * @param budgetId
     * @param beginDate
     * @return
     */
    public List<FbsBudgetProcessRateVO> queryBudgetProcessRateDtl(String budgetId, Date beginDate, Date endDate, String tableName, boolean barFlag) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.item_code as itemCode,b.item_name as itemName,i.id as itemId,(b.b_money+b.bsuper_money) as bMoney,");
        sql.append(" (select sum(p.process_cur_money) from fbs_budget_process p where p.status="+BudgetConstant.STATUE_CHECK+" and p.process_date>=:beginDate and p.process_date<=:endDate");
        sql.append(" and p.corp_id=f.corp_id and p.item_code like b.item_code||'%') as processMoney");
        sql.append(" from " + tableName + " b");
        sql.append(" left join fbs_budget f on f.id=b.budget_id");
        sql.append(" left join fbs_item i on b.item_code = i.item_code");
        sql.append(" where b.budget_id = :budgetId");
        if(barFlag){
            sql.append(" and b.item_code in ('"+BudgetConstant.ITEM_CODE_200+"','"+BudgetConstant.ITEM_CODE_400+"')");
        }
        sql.append(" order by b.item_code");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("budgetId", budgetId);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(sql.toString(), params, FbsBudgetProcessRateVO.class);
    }
}
