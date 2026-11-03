package com.bytter.increment.domain.dao;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * 理财产品净值维护dao
 *
 * @author ouyj
 */
@Repository
public class ChoProductNetValDao extends BaseDao<ChoProductNetVal, String> {
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_info_id as bankInfoId, pr.bank_name as bankName, ");
        sql.append(" pr.product_code as productCode, pt.product_name as productName, pr.product_type as productType,pr.bank_Type_Id as bankTypeId,pr.bank_type_name as bankTypeName,");
        sql.append(" pr.INCOME_CLAUSE as incomeClause,pr.bank_acc as bankAcc,pr.buy_money as buyMoney,pr.cur_id as curId, pr.cur_name as curName, ");
        sql.append(" pr.interest_start_date as interestStartDate, pr.Deadline_Date as deadlineDate,pr.refer_income_rate as referIncomeRate, ");
        sql.append(" pr.is_move_up as isMoveUp, pr.status as status, pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" case when pr.biz_status in (5,6,7) then 4 else pr.biz_status end as bizStatus,");
        sql.append(" pr.buy_date as buyDate, pn.daily_net_val AS latestNetVal, pn.net_val_date AS latestDate ");
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" LEFT JOIN cho_product_redeem prd ON prd.req_id = pr.id ");
        sql.append(" LEFT JOIN (SELECT MAX(create_date) AS maxDate, req_id AS reqId  ");
        sql.append(" FROM cho_product_redeem GROUP BY req_id) prd2 ON prd2.reqId = pr.id ");
        sql.append(" LEFT JOIN cho_product_netval pn ON pn.req_id = pr.id");
        sql.append(" LEFT JOIN (SELECT MAX(net_val_date) AS MAXDATE, REQ_ID AS REQID  FROM cho_product_netval GROUP BY REQ_ID) pn2 ");
        sql.append(" ON pn2.REQID = pr.id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and (pr.is_maintain_val is null or pr.is_maintain_val = :validSign1) ");
        sql.append(" and pr.status in (:statusArr) and pt.FINANCIAL_TYPE = :type2 ");
        sql.append(" AND  pn2.MAXDATE = pn.net_val_date ");
        sql.append(" AND (prd2.maxDate = prd.create_date OR prd2.maxDate IS NULL) ");
        HashMap<String, Object> params = new HashMap<>(4);
        params.put("validSign1", CommonConstant.YES);
        params.put("status95", ChoProductReqConstant.QUERY_STATUS_CHECK);
        params.put("statusArr", new String[]{ChoProductReqConstant.QUERY_STATUS_CHECK, ChoProductReqConstant.QUERY_STATUS_FINISH});
        params.put("status_2", ChoProductReqConstant.STATUE_DEL);
        params.put("type2", 2);
        Object bizStatus = dataTablesInput.getFormdata().get("eqi_bizStatus");
        if (ChoProductReqConstant.BIZ_STATUS4.toString().equals(bizStatus)) {
            sql.append(" and pr.biz_status in (:bizStatusList) ");
            params.put("bizStatusList", new Integer[]{ChoProductReqConstant.BIZ_STATUS4, ChoProductReqConstant.BIZ_STATUS5,
                    ChoProductReqConstant.BIZ_STATUS6, ChoProductReqConstant.BIZ_STATUS7});
            dataTablesInput.getFormdata().remove("eqi_bizStatus");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductReqVo.class, "pr", false, params);
    }
    /**
     * 查询净值维护详情列表
     *
     * @param dataTablesInput 前端参数
     * @return 净值列表
     */
    public DataTablesOutput getEditDataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        commonColumn(sql);
        sql.append(" from CHO_PRODUCT_NETVAL pn ");
        sql.append(" where pn.req_id = :reqId ");
        sql.append(" order by pn.NET_VAL_DATE desc ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("reqId", dataTablesInput.getFormdata().get("reqId"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductNetVal.class, "", false, params);
    }
    /**
     * 根据登记id查询对应净值维护数据(可根据净值维护日期来筛选)
     *
     * @param reqId      登记id
     * @param netValDate 净值维护日期
     * @param comparator 比较符号
     * @return 净值维护集合
     */
    public List<ChoProductNetVal> listByReqId(String reqId, Date netValDate, String comparator) {
        HashMap<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        commonColumn(sql);
        sql.append(" from CHO_PRODUCT_NETVAL pn ");
        sql.append(" where pn.req_id = :reqId ");
        if (StringUtils.isNotEmpty(comparator)) {
            sql.append(" and pn.NET_VAL_DATE ").append(comparator).append(" :netValDate ");
            params.put("netValDate", netValDate);
        }
        sql.append(" order by pn.NET_VAL_DATE desc ");
        params.put("reqId", reqId);
        return listBySQLAliasToBean(sql.toString(), params, ChoProductNetVal.class);
    }
    /**
     * 净值维护表公共查询列
     *
     * @param sql 净值维护表公共查询列
     */
    private void commonColumn(StringBuilder sql) {
        sql.append(" pn.id as id, pn.req_id as reqId, pn.NET_VAL_DATE as netValDate, pn.BUY_MONEY as buyMoney, ");
        sql.append(" pn.BUY_PORTION as buyPortion, pn.DAILY_NET_VAL as dailyNetVal, pn.TEN_THOUSAND_INCOME as tenThousandIncome, ");
        sql.append(" pn.DAY_INCOME as dayIncome, pn.ACTUAL_INCOME as actualIncome, pn.bal as bal ");
    }
}
