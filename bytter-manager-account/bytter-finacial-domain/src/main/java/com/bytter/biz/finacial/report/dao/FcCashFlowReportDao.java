package com.bytter.biz.finacial.report.dao;
import com.bytter.biz.finacial.closedeal.vo.FcCashFlowVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public class FcCashFlowReportDao extends BaseDao {
    /**
     * 现金流量表数据查询
     * @param input
     * @param ids
     * @param params
     * @return
     */
    /*public DataTablesOutput datatable(DataTablesInput input, List<String> ids, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(leftItemName)  leftItemName, ");
        sql.append("       max(leftOrderNo)   leftOrderNo, ");
        sql.append("       sum(leftMoney)     leftMoney, ");
        sql.append("       max(leftItemId)    leftItemId,      ");
        sql.append("       max(rightItemName) rightItemName, ");
        sql.append("       max(rightOrderNo)  rightOrderNo, ");
        sql.append("       sum(rightMoney)    rightMoney, ");
        sql.append("       max(rightItemId)   rightItemId,  ");
        sql.append("       ");
        sql.append("       cur_code                        ");
        sql.append("from (select item.ITEM_NAME          leftItemName, ");
        sql.append("             item.id                 leftItemId, ");
        sql.append("             format.ORDER_NO         leftOrderNo, ");
        sql.append("             sum(leftCashflow.MONEY) leftMoney, ");
        sql.append("             '' as                   rightItemName, ");
        sql.append("             0  as                   rightMoney, ");
        sql.append("             null                    rightOrderNo, ");
        sql.append("             format.ORDER_NO         orderNo, ");
        sql.append("             leftCashflow.cur_code   cur_code");
        sql.append("      from fc_cash_flow leftCashflow ");
        sql.append("               left join fc_report_format format on leftCashflow.CASH_ITEM = format.ITEM_ID ");
        sql.append("               left join FC_CASH_ITEM item on format.ITEM_ID = item.ID ");
        sql.append("      where format.DIRECTION = '1' ");
        if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
            sql.append(" and leftCashflow.CUR_CODE = '" + params.get("curId").toString() + "'");
        }
        if (null != params.get("netCode") && StringUtils.isNotEmpty(params.get("netCode").toString())) {
            sql.append(" and leftCashflow.net_code = '" + params.get("netCode").toString() + "'");
        }
        if (null != params.get("startDate") && StringUtils.isNotEmpty(params.get("startDate").toString())) {
            sql.append(" and leftCashflow.tally_date >= bt_to_date('" + params.get("startDate").toString() + "','yyyy-MM-dd hh24:mi:ss')");
        }
        if (null != params.get("startDate") && StringUtils.isNotEmpty(params.get("endDate").toString())) {
            sql.append(" and leftCashflow.tally_date <= bt_to_date('" + params.get("endDate").toString() + "','yyyy-MM-dd hh24:mi:ss')");
        }
        sql.append("      group by item.id, item.ITEM_NAME, format.ORDER_NO, leftCashflow.cur_code ");
        sql.append("      union ");
        sql.append("      select ''                      leftItemName, ");
        sql.append("             null                    leftOrderNo, ");
        sql.append("             0                       leftMoney, ");
        sql.append("             item.ITEM_NAME          rightItemName, ");
        sql.append("             item.id                 rightItemId, ");
        sql.append("             sum(rightCashflow.MONEY) rightMoney, ");
        sql.append("             format.ORDER_NO         rightOrderNo, ");
        sql.append("             format.ORDER_NO         orderNo, ");
        sql.append("             rightCashflow.CUR_CODE  cur_code");
        sql.append("      from fc_cash_flow rightCashflow ");
        sql.append("               left join fc_report_format format on rightCashflow.CASH_ITEM = format.ITEM_ID ");
        sql.append("               left join FC_CASH_ITEM item on format.ITEM_ID = item.ID ");
        sql.append("      where format.DIRECTION = '2' ");
        if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
            sql.append(" and rightCashflow.CUR_CODE = '" + params.get("curId").toString() + "'");
        }
        if (null != params.get("netCode") && StringUtils.isNotEmpty(params.get("netCode").toString())) {
            sql.append(" and rightCashflow.net_code = '" + params.get("netCode").toString() + "'");
        }
        if (null != params.get("startDate") && StringUtils.isNotEmpty(params.get("startDate").toString())) {
            sql.append(" and rightCashflow.tally_date >= bt_to_date('" + params.get("startDate").toString() + "','yyyy-MM-dd hh24:mi:ss')");
        }
        if (null != params.get("startDate") && StringUtils.isNotEmpty(params.get("endDate").toString())) {
            sql.append(" and rightCashflow.tally_date <= bt_to_date('" + params.get("endDate").toString() + "','yyyy-MM-dd hh24:mi:ss')");
        }
        sql.append("      group by item.id, item.ITEM_NAME, format.ORDER_NO, rightCashflow.CUR_CODE ");
        sql.append("     ) ");
        sql.append("group by orderNo ,cur_code");
        OrderString orderString = new OrderString("orderNo","");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), FcCashFlowReportVO.class, "", false, null);
    }*/
    /**
     * 获取数据
     * @param params
     * @return
     */
    public List<FcCashFlowVO> getCashFlowData(Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select case ");
        sql.append("       when format.rect = 1 or format.rect is null then bt_nvl_num(cashFlow.money,0) ");
        sql.append("       when format.rect = 0 then bt_nvl_num(cashFlow.money,0) * -1 ");
        sql.append("            else bt_nvl_num(cashFlow.money,0) ");
        sql.append("             end as money, ");
        sql.append("       '" + params.get("curId").toString() + "' curCode, ");
        sql.append("       format.item_id cashItem, ");
        sql.append("       format.ITEM   cashItemName, ");
        sql.append("       format.LINE_NO   lineNo, ");
        sql.append("       format.ORDER_NO  orderNo, ");
        sql.append("       format.FORMULA   formula, ");
        sql.append("       bt_nvl_num(format.rect,1)      rect , ");
        sql.append("       format.DIRECTION direction ");
        sql.append(" from fc_report_format format ");
        sql.append("       left join (select sum(fc_cash_flow.MONEY) money, fc_cash_flow.cur_code curCode, fc_cash_flow.cash_item cashItem ");
        sql.append("       from fc_cash_flow ");
        sql.append("         where 1 = 1 ");
        if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
            sql.append(" and fc_cash_flow.CUR_CODE = :curId ");
        }
        if (null != params.get("netCode") && StringUtils.isNotEmpty(params.get("netCode").toString())) {
            sql.append(" and fc_cash_flow.net_code = :netCode ");
        }
        if (null != params.get("startDate") && StringUtils.isNotEmpty(params.get("startDate").toString())) {
            sql.append(" and fc_cash_flow.tally_date >= bt_to_date('" + params.get("startDate").toString() + "','yyyy-MM-dd hh24:mi:ss')");
        }
        if (null != params.get("startDate") && StringUtils.isNotEmpty(params.get("endDate").toString())) {
            sql.append(" and fc_cash_flow.tally_date <= bt_to_date('" + params.get("endDate").toString() + "','yyyy-MM-dd hh24:mi:ss')");
        }
        sql.append("       group by fc_cash_flow.cur_code, fc_cash_flow.cash_item) cashFlow ");
        sql.append("     on format.ITEM_ID = cashflow.cashItem ");
        sql.append("          left join FC_CASH_ITEM item on format.ITEM_ID = item.ID ");
        sql.append(" where 1 = 1");
        sql.append(" and format.report_type = '3' ");
        sql.append(" and format.net_code = :netCode");
        sql.append(" order by cashFlow.curCode, format.LINE_NO, format.ORDER_NO");
        return listBySQLAliasToBean(sql.toString(), params, FcCashFlowVO.class);
    }
    /**
     *
     * @param params
     * @return
     */
    public List<FcCashFlowVO> listAllCur(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select CUR_CODE from fc_cash_flow where 1 = 1");
        sql.append(" and NET_CODE = :netCode ");
        sql.append(" and TALLY_DATE >= bt_to_date(:startDate,'yyyy-MM-dd hh24:mi:ss')  ");
        sql.append(" and TALLY_DATE <= bt_to_date(:endDate,'yyyy-MM-dd hh24:mi:ss') ");
        if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
            sql.append(" and CUR_CODE = :curId");
        }
        sql.append(" group by CUR_CODE ");
        return listBySQLAliasToBean(sql.toString(), params, FcCashFlowVO.class);
    }
}
