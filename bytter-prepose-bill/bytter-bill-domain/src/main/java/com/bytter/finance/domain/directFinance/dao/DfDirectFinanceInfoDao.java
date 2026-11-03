package com.bytter.finance.domain.directFinance.dao;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceInfo;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceInfoReportVO;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceInfoVO;
import com.bytter.finance.constant.DfConstant;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 10:41
 * @Version 1.0
 */
@Repository
public class DfDirectFinanceInfoDao extends BaseDao<DfDirectFinanceInfo, String> {
    /**
     * 列表数据显示
     * 
     * @param input
     * @return
     */
    public DataTablesOutput listDfDirectFinanceInfo(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, ");
        sql.append("        t.bill_code, ");
        sql.append("        t.stock_name, ");
        sql.append("        t.stock_code, ");
        sql.append("        t.start_date, ");
        sql.append("        t.peroid, ");
        sql.append("        t.unit, ");
        sql.append("        t.surplus_money, ");
        sql.append("        t.bond_rating, ");
        sql.append("        t.bond_main_rating, ");
        sql.append("        t.money, ");
        sql.append("        t.status, ");
        sql.append("        t.end_date endDate, ");
        sql.append("        bt.business_name businessName ");
        sql.append("   from DF_DIRECT_FINANCE_INFO t ");
        sql.append("   left join DF_BUSINESS_TYPE bt on t.DF_BUSINESS_TYPE_ID = bt.id");
        sql.append(" where 1 = 1 ");
        sql.append(" and (t.status = 0 or  t.status = -1) ");
        OrderString orderString = new OrderString("t.create_date", "desc");
        sql.append(" and (" + getSQLIn(corpIds, 900, "t.corp_id") + ")");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), DfDirectFinanceInfoVO.class, "t", false);
    }
    /**
     * 列表数据显示
     *
     * @param input
     * @return
     */
    public DataTablesOutput listDfDirectFinanceInfoApprove(DataTablesInput input, List<String> Ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, ");
        sql.append("        t.bill_code, ");
        sql.append("        t.stock_name, ");
        sql.append("        t.stock_code, ");
        sql.append("        t.start_date, ");
        sql.append("        t.peroid, ");
        sql.append("        t.unit, ");
        sql.append("        t.surplus_money, ");
        sql.append("        t.bond_rating, ");
        sql.append("        t.bond_main_rating, ");
        sql.append("        t.money, ");
        sql.append("        t.status, ");
        sql.append("        t.end_date endDate, ");
        sql.append("        bt.business_name businessName ");
        sql.append("   from DF_DIRECT_FINANCE_INFO t ");
        sql.append("   left join DF_BUSINESS_TYPE bt on t.DF_BUSINESS_TYPE_ID = bt.id");
        sql.append(" where 1 = 1 ");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and (t.status >= 11 or  t.status < 95)");
        }
        OrderString orderString = new OrderString("t.create_date", "desc");
        if (!EmptyUtils.isEmpty(Ids) && Ids.size() > 0) {
            sql.append("and (" + getSQLIn(Ids, 900, "t.id") + ")");
        }
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), DfDirectFinanceInfoVO.class, "t", false);
    }
    /**
     * 报表查询
     * 
     * @param input
     * @return
     */
    public DataTablesOutput listDfDirectFinanceReport(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (");
        sql.append(" select df.id, ");
        sql.append("        df.bill_code, ");
        sql.append("        df.create_date, ");
        sql.append("        df.stock_name, ");
        sql.append("        df.stock_code, ");
        sql.append("        df.start_date, ");
        sql.append("        df.peroid, ");
        sql.append("        df.unit, ");
        sql.append("        df.surplus_money, ");
        sql.append("        df.bond_rating, ");
        sql.append("        df.Bond_Main_Rating, ");
        sql.append("        df.money, ");
        sql.append("        df.status, ");
        sql.append("        df.end_date end_Date, ");
        sql.append("        bt.business_name businessName, ");
        sql.append("        df.df_business_type_id df_business_type_id, ");
        sql.append("        allRepay.allRepayMoney, ");
        sql.append("        case ");
        sql.append("        when bt_nvl_num(allRepay.allRepayMoney,0) = 0 then ");
        sql.append("                  1 ");
        sql.append("        when df.surplus_money = AllRepay.allRepayMoney then ");
        sql.append("                  3 ");
        sql.append("        else ");
        sql.append("                  2 ");
        sql.append("        end as repayBizStatus ");
        sql.append("   from DF_DIRECT_FINANCE_INFO df ");
        sql.append("   left join DF_BUSINESS_TYPE bt on df.DF_BUSINESS_TYPE_ID = bt.id");
        sql.append("          left join (select DIRECT_FINANCE_ID, ");
        sql.append("                            BT_NVL_NUM(sum(REPAY_MONEY), 0) allRepayMoney ");
        sql.append("                       from DF_DIRECT_FINANCE_REPAY ");
        sql.append("                      where status >= " + DfConstant.APPROVED + " ");
        sql.append("                      group by DIRECT_FINANCE_ID) allRepay ");
        sql.append("            on df.id = AllRepay.DIRECT_FINANCE_ID ");
        OrderString orderString = new OrderString("t.create_date", "desc");
        input.setOrderString(orderString);
        sql.append(" ) t  where 1 = 1");
        return getDynamicBySQL(input, sql.toString(), DfDirectFinanceInfoReportVO.class, "t", false);
    }
    /**
     * 单位校验使用
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDeleteByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from DF_DIRECT_FINANCE_INFO where 1 = 1 and status != " + DfConstant.DELETED
                + " and corp_id = ?");
        return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
    }
    /**
     * 单位校验使用
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select count(*) from DF_DIRECT_FINANCE_INFO where 1 = 1 and (status = -1 or status = 0 or status = 11) and corp_id = ?");
        return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
    }
    /**
     * <br>
     * <b>功能：</b> 查询直接融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryInlFinance(List<String> corpAuth, String curId, Date queryDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select '03' as busType, sd.corpId , sd.corpName, sd.money ");
        sql.append(" from ");
        sql.append(" (select ");
        //TODO 补充单位名称
        sql.append(" info.corp_id as corpId, info.corp_name as corpName, ");
        sql.append(" sum((BT_NVL_NUM(info.surplus_money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0))) as money ");
        sql.append(" from df_direct_finance_info info ");
        sql.append(" left join (select dr.direct_finance_id, ");
        sql.append(" sum(dr.repay_money) as sum_repay_money ");
        sql.append("   from df_direct_finance_repay dr ");
        sql.append("   where dr.status >=  ").append(DfConstant.APPROVED);
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and dr.repay_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append("   group by dr.direct_finance_id) repay on repay.direct_finance_id = info.id ");
        sql.append(" where info.status >= ").append(DfConstant.APPROVED);
        sql.append(" and BT_NVL_NUM(info.surplus_money, 0) > BT_NVL_NUM(repay.sum_repay_money, 0) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and info.start_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append(" group by info.corp_id ,info.corp_name ) sd ");
//        sql.append(" group by info.corp_id ) sd, sys_corp sc ");
        sql.append(" where 1=1 ");
//        sql.append(" where sd.corpId = sc.id ");
        sql.append(" order by sd.corpId ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceLoanDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询直接融资的合计融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceTypeLoanDTO> queryInlTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select BT_NVL_NUM(sum(sd.money), 0) as money ");
        sql.append(" from ");
        sql.append(" (select ");
        sql.append(" info.corp_id as corpId, ");
        sql.append(" sum((BT_NVL_NUM(info.surplus_money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0))) as money ");
        sql.append(" from df_direct_finance_info info ");
        sql.append(" left join (select dr.direct_finance_id, ");
        sql.append(" sum(dr.repay_money) as sum_repay_money ");
        sql.append("   from df_direct_finance_repay dr ");
        sql.append("   where dr.status >=  ").append(DfConstant.APPROVED);
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and dr.repay_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append("   group by dr.direct_finance_id) repay on repay.direct_finance_id = info.id ");
        sql.append(" where info.status >= ").append(DfConstant.APPROVED);
        sql.append(" and BT_NVL_NUM(info.surplus_money, 0) > BT_NVL_NUM(repay.sum_repay_money, 0) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append("and info.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append("and info.start_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append(" group by info.corp_id ) sd ");
//        sql.append(" group by info.corp_id ) sd, sys_corp sc ");
        sql.append(" where 1=1 ");
//        sql.append(" where sd.corpId = sc.id ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceTypeLoanDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询直接融资的利息支持情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryInlInterest(List<String> corpAuth, String curId, Date queryDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        //TODO 补充单位名称
        sql.append(" select '04' as busType, sd.corpId , sd.corpName, sd.money ");
        sql.append(" from ");
        sql.append(" (select ");
        //TODO 补充单位名称
        sql.append(" info.corp_id as corpId, info.corp_name as corpName, ");
        sql.append(" sum(BT_NVL_NUM(repay.interest, 0)) as money ");
        sql.append(" from df_direct_finance_info info, df_direct_finance_repay repay ");
        sql.append("   where repay.direct_finance_id = info.id and repay.status >=  ").append(DfConstant.APPROVED);
        sql.append(" and info.status >= ").append(DfConstant.APPROVED);
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and repay.repay_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append(" group by info.corp_id ,info.corp_name ) sd ");
//        sql.append(" group by info.corp_id ) sd, sys_corp sc ");
//        sql.append(" where sd.corpId = sc.id ");
        sql.append(" where 1=1 ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceLoanDTO.class);
    }
}
