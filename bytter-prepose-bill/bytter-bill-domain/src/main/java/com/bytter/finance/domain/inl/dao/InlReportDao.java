package com.bytter.finance.domain.inl.dao;
import com.bytter.finance.domain.inl.entity.CmsInlHistory;
import com.bytter.finance.domain.inl.vo.CmsInlReportInterestVO;
import com.bytter.finance.domain.inl.vo.CmsInlReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public class InlReportDao extends BaseDao<CmsInlHistory, String> {
    /**
     * 内部拆借台账
     *
     * @param input
     * @return
     */
    public DataTablesOutput listReport(DataTablesInput input, List<String> ids, List repayStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select info.*");
        sql.append("   from ( select ");
        sql.append(" c.bill_no,");
        sql.append(" c.BORROW_IN_CORP,");
        sql.append(" c.BORROW_OUT_CORP,");
        sql.append(" c.cur_id,");
        sql.append(" c.money,");
        sql.append(" c.start_date,");
        sql.append(" c.end_date,");
        sql.append(" c.term,");
        sql.append(" c.unit,");
        sql.append(" c.borrow_in_corp_name borrowInCorpName,c.borrow_out_corp_name borrowOutCorpName,c.cur_name as curName, ");
        sql.append(" bt_nvl_num(send.send_Money, 0) sendMoney, ");// 已放款金额
        sql.append(" (c.money - bt_nvl_num(send.send_Money, 0) - ");
        sql.append(" bt_nvl_num(sending.sending_money, 0)) waitSendMoney, ");// 待放款金额
        sql.append(" bt_nvl_num(sending.sending_money, 0) sendingMoney, ");// 放款在途金额
        sql.append(" case ");
        sql.append("   when c.money - bt_nvl_num(send.send_Money, 0) = 0 then ");
        sql.append("    1 ");// 全部放款
        sql.append("   when c.money - bt_nvl_num(send.send_Money, 0) = c.money then ");
        sql.append("    2 ");// 未放款
        sql.append("   else ");
        sql.append("    3 ");// 部分放款
        sql.append(" end sendStatus, ");
        sql.append(" repay.repay_money repayMoneyTotal, ");
        sql.append(" repay.REPAY_INTEREST repayInterestTotal, ");
        sql.append(" case ");
        sql.append("   when c.money - bt_nvl_num(repay.repay_money, 0) = 0 then ");
        sql.append("    1 ");// 已全部还款
        sql.append("   when bt_nvl_num(repay.repay_money,0) = 0 and bt_nvl_num(repay.REPAY_INTEREST,0) = 0 then ");
        sql.append("   2 ");// 未还款
        sql.append("   else ");
        sql.append("    3 ");// 部分还款
        sql.append(" end repayStatus ");
        sql.append(" from CMS_INL_INFO c ");
        sql.append(" left join (select inl_info_id info_id, ");
        sql.append("                  sum(send_money) sending_money ");
        sql.append("             from CMS_INL_SEND ");
        sql.append("            where status in (11, 95) ");
        sql.append("            group by inl_info_id) sending ");
        sql.append("   on c.id = sending.info_id ");
        sql.append(" left join (select inl_info_id info_id, sum(send_money) send_Money ");
        sql.append("             from CMS_INL_SEND ");
        sql.append("            where status = 96 ");
        sql.append("            group by inl_info_id) send ");
        sql.append("   on c.id = send.info_id ");
        sql.append(" left join (select inl_info_id, ");
        sql.append("                  sum(repay_money) repay_money, ");
        sql.append("                  sum(REPAY_INTEREST) REPAY_INTEREST ");
        sql.append("             from CMS_INL_REPAY ");
        sql.append("            where status = 96 ");
        sql.append("            group by inl_info_id) repay ");
        sql.append("   on c.id = repay.inl_info_id ");
        sql.append(" where c.status = 95 ");
        sql.append(" union");
        sql.append(" select ");
        sql.append(" his.bill_no,");
        sql.append(" his.borrow_in_corp,");
        sql.append(" his.borrow_out_corp,");
        sql.append(" his.cur_id,");
        sql.append(" his.money,");
        sql.append(" his.start_date,");
        sql.append(" his.end_date,");
        sql.append(" his.term,");
        sql.append(" his.unit,");
        sql.append(" his.borrow_in_corp_name borrowInCorpName,");
        sql.append(" his.borrow_out_corp_name borrowOutCorpName,");
        sql.append(" his.cur_name AS curName,");
        sql.append(" his.send_money sendMoney,");
        sql.append(" his.left_send_money waitSendMoney,");
        sql.append(" 0 AS sendingMoney,");
        sql.append(" his.send_status sendStatus,");
        sql.append(" his.repay_money repayMoneyTotal,");
        sql.append(" his.repay_interest repayInterestTotal,");
        sql.append(" his.repay_status repayStatus");
        sql.append("    from CMS_IN_HISTORY his    ");
        sql.append("         ) info ");
        sql.append("  where 1 = 1 ");
        if (ListUtils.isNotNullOrEmpty(repayStatus)) {
            sql.append(" and info.repayStatus in (");
            String repayStatusStr = null;
            for (Object status : repayStatus) {
                if (StringUtils.isEmpty(repayStatusStr)) {
                    repayStatusStr = status.toString();
                } else {
                    repayStatusStr += "," + status.toString();
                }
            }
            sql.append(repayStatusStr);
            sql.append(" ) ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsInlReportVO.class, "info", false, null);
    }
    /**
     * 内部拆借利息统计
     *
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput listReportInterest(DataTablesInput input, List<String> ids, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select send.* from (");
        sql.append("select info.bill_no infoBillNo, ");
        sql.append("       c.bill_no, ");
        sql.append("       c.borrow_in_corp, ");
        sql.append("       c.borrow_out_corp, ");
        sql.append("       c.send_code, ");
        sql.append("       c.cur_id, ");
        sql.append("       c.send_money, ");
        sql.append("       c.send_date, ");
        sql.append("       c.end_date, ");
        sql.append("       c.deferred_end_date, ");
        sql.append("       info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName, ");
        sql.append("       c.repay_type, ");
        sql.append("       rt.repay_name as repayTypeName,");
        boolean filterNextRepayDate = hasNextRepayDateFilter(params);
        if (filterNextRepayDate) {
            sql.append("        cpi.REPAY_DATE nextRepayDate,");
            sql.append("        (bt_nvl_num(cpi.repay_money,0)) nextRepayMoney,");
            sql.append("        (bt_nvl_num(cpi.total_interest,0)) nextRepayInterest,");
            sql.append("        (bt_nvl_num(cpi.repay_money,0)+bt_nvl_num(cpi.total_interest,0)) nextRepayTotal,");
        }
        sql.append("       (c.send_money-bt_nvl_num(repay.repay_money,0)) leftRepayMoneyTotal, ");
        sql.append("       bt_nvl_num(repay.repay_money,0) repayMoneyTotal, ");
        sql.append("       bt_nvl_num(repay.REPAY_INTEREST,0) repayInterestTotal,");
        sql.append("       bt_nvl_num(interest.interest_total,0) interestTotal");
        sql.append("  from CMS_INL_SEND c ");
        sql.append("  left join CMS_INL_INFO info ");
        sql.append("       on c.inl_info_id = info.id");
        sql.append(" left join cms_repay_type rt on c.repay_type=rt.id ");
        sql.append("  left join (select inl_send_id, ");
        sql.append("                    sum(repay_money) repay_money, ");
        sql.append("                    sum(REPAY_INTEREST) REPAY_INTEREST ");
        sql.append("               from CMS_INL_REPAY ");
        sql.append("              where status = 96 ");
        sql.append("              group by inl_send_id) repay ");
        sql.append("    on c.id = repay.inl_send_id ");
        sql.append("  left join (select BILL_CODE, sum(TOTAL_INTEREST) interest_total ");
        sql.append("               from CMS_PROVIDE_INTEREST ");
        sql.append("              group by BILL_CODE) interest ");
        sql.append("    on c.bill_no = interest.BILL_CODE ");
        if (filterNextRepayDate) {
            sql.append("        left join CMS_PROVIDE_INTEREST cpi");
            sql.append("        on c.bill_no = cpi.bill_code and cpi.status = '0' and cpi.INTEREST_SIGN = '1'");
        }
        sql.append(" where c.status = 96 ");
        sql.append("  ) send");
        sql.append("   where 1 = 1");
        sql.append("         and send.leftRepayMoneyTotal > 0");
        // sql.append(" and (" + getSQLIn(corpIds, 900, "info.borrow_in_corp") + ")");
        return getDynamicBySQL(input, sql.toString(), CmsInlReportInterestVO.class, "info", false, null);
    }

    private boolean hasNextRepayDateFilter(Map<String, Object> params) {
        if (params == null) {
            return false;
        }
        Object ged = params.get("ged_nextRepayDate");
        Object led = params.get("led_nextRepayDate");
        return (ged != null && StringUtils.isNotEmpty(ged.toString()))
                || (led != null && StringUtils.isNotEmpty(led.toString()));
    }
}
