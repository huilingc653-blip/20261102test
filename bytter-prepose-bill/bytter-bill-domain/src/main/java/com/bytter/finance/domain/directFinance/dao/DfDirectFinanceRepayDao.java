package com.bytter.finance.domain.directFinance.dao;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceRepay;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceInfoVO;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceRepayVO;
import com.bytter.finance.constant.DfConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-13 15:05
 * @Version 1.0
 */
@Repository
public class DfDirectFinanceRepayDao extends BaseDao<DfDirectFinanceRepay, String> {
    /**
     * 列表数据显示
     * 
     * @param input
     * @return
     */
    public DataTablesOutput listDfDirectFinanceRepay(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from ( ");
        sql.append("        select df.id, ");
        sql.append("               df.bill_code, ");
        sql.append("               df.create_date, ");
        sql.append("               df.stock_name, ");
        sql.append("               df.stock_code, ");
        sql.append("               df.start_date, ");
        sql.append("               df.peroid, ");
        sql.append("               df.unit, ");
        sql.append("               df.surplus_money, ");
        sql.append("               df.bond_rating, ");
        sql.append("               df.bond_main_rating, ");
        sql.append("               df.money, ");
        sql.append("               df.end_date, ");
        sql.append("               bt.business_name, ");
        sql.append("               df.df_business_type_id, ");
        sql.append("               allRepay.allRepayMoney, ");
        sql.append("               case ");
        sql.append("                 when bt_nvl_num(allRepay.allRepayMoney,0) = 0 then ");
        sql.append("                  1 ");
        sql.append("                 when df.surplus_money = AllRepay.allRepayMoney then ");
        sql.append("                  3 ");
        sql.append("                 else ");
        sql.append("                  2 ");
        sql.append("               end as repayBizStatus ");
        sql.append("          from DF_DIRECT_FINANCE_INFO df ");
        sql.append("          left join DF_BUSINESS_TYPE bt ");
        sql.append("            on df.DF_BUSINESS_TYPE_ID = bt.id ");
        sql.append("          left join (select DIRECT_FINANCE_ID, ");
        sql.append("                            BT_NVL_NUM(sum(REPAY_MONEY), 0) allRepayMoney ");
        sql.append("                       from DF_DIRECT_FINANCE_REPAY ");
        sql.append("                      where status >= " + DfConstant.APPROVED + " ");
        sql.append("                      group by DIRECT_FINANCE_ID) allRepay ");
        sql.append("            on df.id = AllRepay.DIRECT_FINANCE_ID ");
        sql.append("         where ");
        sql.append("           (df.status >= " + DfConstant.APPROVED + ") ");
        OrderString orderString = new OrderString("t.create_date", "desc");
        sql.append(" and (" + getSQLIn(corpIds, 900, "df.corp_id") + ")");
        input.setOrderString(orderString);
        sql.append(" ) t where  1 = 1");
        return getDynamicBySQL(input, sql.toString(), DfDirectFinanceInfoVO.class, "t", false);
    }
    /**
     * 列表数据显示
     * 
     * @param input
     * @return
     */
    public DataTablesOutput listTempOrback(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id id, ");
        sql.append("        t.bill_code billCode, ");
        sql.append("        t.REPAY_DATE repayDate, ");
        sql.append("        t.REPAY_MONEY repayMoney, ");
        sql.append("        t.INTEREST interest, ");
        sql.append("        t.SERVICE_CHARGE serviceCharge, ");
        sql.append("        t.RMK rmk, ");
        sql.append("        t.status status, ");
        sql.append("        t.CORP_ID corpId");
        sql.append("   from DF_DIRECT_FINANCE_REPAY t ");
        sql.append(" where 1 = 1 ");
        sql.append(" and (t.status = " + DfConstant.TEMP + " or t.status =" + DfConstant.BACK + " ) ");
        OrderString orderString = new OrderString("t.create_date", "desc");
        sql.append(" and (" + getSQLIn(ids, 900, "t.corp_id") + ")");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), DfDirectFinanceRepayVO.class, "t", false);
    }
    /**
     * 列表数据显示
     * 
     * @param input
     * @return
     */
    public DataTablesOutput listApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id id, ");
        sql.append("        t.bill_code billCode, ");
        sql.append("        t.REPAY_DATE repayDate, ");
        sql.append("        t.REPAY_MONEY repayMoney, ");
        sql.append("        t.INTEREST interest, ");
        sql.append("        t.SERVICE_CHARGE serviceCharge, ");
        sql.append("        t.RMK rmk, ");
        sql.append("        t.status status, ");
        sql.append("        t.CORP_ID corpId");
        sql.append("   from DF_DIRECT_FINANCE_REPAY t ");
        sql.append(" where 1 = 1 ");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(
                    " and (t.status >= " + DfConstant.COMMITAPPROVE + " and t.status <" + DfConstant.APPROVED + " ) ");
        }
        OrderString orderString = new OrderString("t.create_date", "desc");
        // sql.append(" and (" + getSQLIn(ids, 900, "t.id") + ")");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), DfDirectFinanceRepayVO.class, "t", false);
    }
    /**
     * 查找已还款数据
     * 
     * @param params
     * @return
     */
    public List<DfDirectFinanceRepayVO> listRepaidMoney(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id id, ");
        sql.append("        t.bill_code billCode, ");
        sql.append("        t.REPAY_DATE repayDate, ");
        sql.append("        t.REPAY_MONEY repayMoney, ");
        sql.append("        t.INTEREST interest, ");
        sql.append("        t.SERVICE_CHARGE serviceCharge, ");
        sql.append("        t.RMK rmk, ");
        sql.append("        t.status status, ");
        sql.append("        t.CORP_ID corpId");
        sql.append("   from DF_DIRECT_FINANCE_REPAY t ");
        sql.append(" where 1 = 1 ");
        sql.append(" and t.DIRECT_FINANCE_ID = :id ");
        sql.append(" and (t.status >= " + DfConstant.APPROVED + " ) order by t.create_date desc");
        return listBySQLAliasToBean(sql.toString(), params, DfDirectFinanceRepayVO.class);
    }
    /**
     * 获取还款金额
     * 
     * @param params
     * @return
     */
    public BigDecimal getAllRepayMoney(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select BT_NVL_NUM(sum(repay_money),0) repayMoney from  DF_DIRECT_FINANCE_REPAY where DIRECT_FINANCE_ID = :id and status = 95");
        return BigDecimal.valueOf(Double.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString()));
    }
    /**
     * 查询审批中数据数量
     * 
     * @param params
     * @return
     */
    public String getUnApprovedById(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select count(*) from DF_DIRECT_FINANCE_REPAY where status = 11 and DIRECT_FINANCE_ID = :id");
        return createSQLQuery(sql.toString(), params).uniqueResult().toString();
    }
}
