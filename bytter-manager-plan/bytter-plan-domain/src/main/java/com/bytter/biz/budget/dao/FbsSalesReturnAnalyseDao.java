package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.entity.FbsSalesReturnAnalyse;
import com.bytter.biz.budget.vo.FbsBudgetAnalyseResportVO;
import com.bytter.biz.budget.vo.FbsSalesReturnAnalyseVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * @author huangx
 * @date 2017-08-03
 */
@Repository
public class FbsSalesReturnAnalyseDao extends BaseDao<FbsSalesReturnAnalyse, String> {
    /**
     * 根据单位ID和周期类型获取往期上报完成的计划
     *
     * @param corpId
     * @param beginDate
     * @param oppAccName
     * @return
     */
    public List<FbsSalesReturnAnalyse> listByCorpId(String corpId, Date beginDate, String oppAccName) {
        String hql = " from FbsSalesReturnAnalyse b where b.corpId = :corpId and b.beginDate = :beginDate";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("beginDate", beginDate);
        if(StringUtils.isNotEmpty(oppAccName)){
            hql += " and b.oppAccName like :oppAccName";
            params.put("oppAccName", "%"+oppAccName+"%");
        }
        return (List<FbsSalesReturnAnalyse>) this.getSession().createQuery(hql).setProperties(params).list();
    }
    /**
     * 根据外部单号获取付款信息
     *
     * @param detailIds
     */
    public List<FbsSalesReturnAnalyseVO> listByBudgetDetailId(String detailIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select d.settl_type as settlType, d.payee_acc_name as oppAccName, sum(d.natual_money) as planMoney");
        sql.append(" from fbs_budget_detail d where d.detail_id in ("+detailIds+") and d.settl_type in ('0','1')");
        sql.append(" group by d.settl_type, d.payee_acc_name ");
        return listBySQLAliasToBean(sql.toString(), null, FbsSalesReturnAnalyseVO.class);
    }
    /**
     * 获取科目折合人民币的执行金额(不包含资金调拨的计划执行)
     *
     * @param beginDate
     * @param endDate
     * @param itemCodes
     * @param corpId
     * @param oppAccName
     * @param settlType
     * @return
     */
    public BigDecimal collectProcessMoney(Date beginDate, Date endDate, String itemCodes, String corpId, String oppAccName, String settlType) {
        StringBuilder sql = new StringBuilder("select sum(PROCESS_NATUAL_MONEY) as processMoney from FBS_BUDGET_PROCESS t");
        sql.append(" where t.ITEM_CODE in ("+itemCodes+") and t.PROCESS_DATE>=:beginDate and t.PROCESS_DATE<=:endDate and t.status = :status and t.corp_id=:corpId ");
        sql.append(" and t.payee_acc_name = :oppAccName and t.settl_type = :settlType");
        HashMap params = new HashMap();
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        params.put("status", BudgetConstant.STATUE_CHECK);
        params.put("corpId", corpId);
        params.put("oppAccName", oppAccName);
        params.put("settlType", settlType);
        Object processMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal collProMoney = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(processMoney)) {
            collProMoney = (BigDecimal) processMoney;
        }
        return collProMoney;
    }
    /**
     * 收/支执行排行表
     * @param timeType
     * @param beginDate
     * @param endDate
     * @param itemCode
     * @return
     */
    public List<FbsBudgetAnalyseResportVO> listBudgetByTimeType(String timeType, Date beginDate, Date endDate,String itemCode) {
        StringBuilder sql = new StringBuilder();
        String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
        sql.append("select t.id as id,t.corp_id as corpId,t.corp_code as corpCode,t.corp_name as corpName,t.enrol_date as enrolDate,t.begin_date as beginDate,t.end_date as endDate,t.cur_id as curId,t.cur_name as curName,");
        sql.append(" BT_NVL_NUM((select d.b_money + d.bsuper_money  from "+tableName+" d where d.budget_id = t.id and d.item_code = :itemCode),0) as planMoney");
//        sql.append(" from fbs_budget t left join bt_currency cur on t.cur_id =cur.id " );
        sql.append(" from fbs_budget t " );
        sql.append(  " where t.time_type = :timeType and t.begin_date<=:beginDate and t.end_date>=:endDate and t.status >= "+ BudgetConstant.STATUE_CHECK);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("timeType", timeType);
        params.put("itemCode", itemCode);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(sql.toString(), params, FbsBudgetAnalyseResportVO.class);
    }
}
