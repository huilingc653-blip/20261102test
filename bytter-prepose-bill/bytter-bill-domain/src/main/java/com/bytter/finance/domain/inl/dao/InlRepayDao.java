package com.bytter.finance.domain.inl.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.inl.entity.CmsInlRepay;
import com.bytter.finance.domain.inl.vo.CmsInlRepayVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class InlRepayDao extends BaseDao<CmsInlRepay, String> {


    /**
     * 内部拆借还款申请数据查询
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input, List<String> corpIds, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  ");
        sql.append("        send.*, ");
        sql.append("        cpi.REPAY_DATE nextRepayDate,");
        sql.append("        cpi.repay_money as nextRepayMoney,");
        sql.append("        cpi.pay_interest as nextRepayInterest,");
        sql.append("        info.money inlInfoMoney,");
        sql.append("        info.purpose purpose,");
        sql.append("        info.bill_no infoBillNo, ");
        sql.append("        repayType.repay_name as repayTypeName1");
        sql.append("        from (");
        sql.append(" select  ");
        sql.append("        repay.repayMoney as repayMoney,");
        sql.append("        send1.id inlSendId,");
        sql.append("        send1.inl_info_id inl_info_id, ");
        sql.append("        send1.bill_no sendBillNo,");
        sql.append("        send1.borrow_in_corp,");
        sql.append("        send1.borrow_out_corp,");
        sql.append("        send1.send_date,");
        sql.append("        send1.end_date,");
        sql.append("        send1.term,");
        sql.append("        send1.send_code,");
        sql.append("        send1.unit,");
        sql.append("        send1.cur_Id,");
        sql.append("        send1.send_money,");
        sql.append("        send1.interest,");
        sql.append("        send1.rate,");
        sql.append("        send1.repay_type,");
        sql.append("        send1.create_date,");
        sql.append("        bt_nvl_num(repay.repayMoney,0) repayMoneyTotal,");
        sql.append("        bt_nvl_num(repay.repayInterest,0) repayInterestTotal,");
        sql.append("        case ");
        sql.append("          when send1.send_Money - bt_nvl_num(repay.repayMoney,0) = 0 then ");
        sql.append("           1 ");
        sql.append("          when send1.send_Money - bt_nvl_num(repay.repayMoney,0) = send1.send_Money then ");
        sql.append("           2 ");
        sql.append("          else ");
        sql.append("           3 ");
        sql.append("        end repayStatus  ");
        sql.append("        from cms_inl_send send1 ");
        sql.append("        left join (select inl_send_id, sum(repay_money) repayMoney,sum(REPAY_INTEREST) repayInterest from CMS_INL_REPAY ");
        sql.append("         where status = 96 group by inl_send_id) repay ");
        sql.append("        on send1.id = repay.inl_send_id");
        sql.append("  where 1 = 1");
        sql.append("  and send1.status = 96 ");
        sql.append("  ) send");
        sql.append("        left join cms_inl_info info");
        sql.append("        on send.inl_info_id = info.id");
        sql.append("    left join CMS_REPAY_TYPE repayType on send.repay_type = repayType.id");
        sql.append(" left join( ");
        sql.append(" SELECT  t.bill_code as bill_code,t.repay_date as repay_date,sum(t.repay_money) AS repay_money,sum(pay_interest) AS pay_interest ");
        sql.append(" FROM CMS_PROVIDE_INTEREST t");
        sql.append(" INNER JOIN (SELECT bill_code,min(repay_date) AS min_repay_date FROM CMS_PROVIDE_INTEREST WHERE STATUS = '0' GROUP BY bill_code )  subq");
        sql.append(" ON  t.bill_code = subq.bill_code AND t.repay_date = subq.min_repay_date GROUP BY t.bill_code,t.repay_date ");
        sql.append(" )cpi on send.sendBillNo=cpi.bill_code ");
        sql.append(" where  1 = 1");
        List repayStatus = new ArrayList();
        if (null != params.get("repayStatus")) {
            if (params.get("repayStatus") instanceof ArrayList) {
                repayStatus = (List) params.get("repayStatus");
            } else {
                repayStatus.add(params.get("repayStatus"));
            }
        }
        if (ListUtils.isNotNullOrEmpty(repayStatus)) {
            sql.append(" and send.repayStatus in (");
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
        sql.append("  and (" + getSQLIn(corpIds, 900, "send.borrow_in_corp") + ")");
        OrderString orderString = new OrderString("info.bill_No", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), CmsInlRepayVO.class, "send", false);
    }

    /**
     * 内部拆借还款申请暂存大户数据查询
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput listRepayUpdateDataTable(DataTablesInput input, List<String> corpIds) {
        String sql = " select repay.id,repay.total_money totalMoney,repay.status status,info.bill_No infoBillNo,send.id inlSendId,send.send_code," +
                " send.borrow_in_corp,send.borrow_out_corp,send.send_date,send.end_date,send.term,send.unit,send.cur_Id,send.send_money," +
                " send.interest,send.rate,send.repay_type,rt.repay_name as repayTypeName,info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName " +
                " from cms_inl_repay repay left join cms_inl_info info on repay.inl_info_id = info.id" +
                " left join cms_inl_send send on repay.inl_send_id = send.id" +
                "   left join cms_repay_type rt on send.repay_type=rt.id "+
                "  where repay.status in (" + InlConstant.BACK + "," + InlConstant.TMP + ") " +
                "  and (" + getSQLIn(corpIds, 900, "repay.borrow_in_corp") + ")";
        return getDynamicBySQL(input, sql, CmsInlRepayVO.class, "repay", false);
    }
    /**
     * 内部拆借还款申请审批中数据查询
     *
     * @param input
     * @return
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select repay.id,info.purpose purpose,  ");
        sql.append("        info.bill_No infoBillNo,");
        sql.append("        send.send_code,");
        sql.append("        send.borrow_in_corp,");
        sql.append("        send.borrow_out_corp,");
        sql.append("        send.interest,");
        sql.append("        send.send_date,");
        sql.append("        send.end_date,");
        sql.append("        send.term,");
        sql.append("        send.unit,");
        sql.append("        send.cur_Id,");
        sql.append("        send.send_money,");
        sql.append("        send.rate,");
        sql.append("        send.repay_type,");
        sql.append("       rt.repay_name as repayTypeName,");
        //TODO 补充名称信息
        sql.append(" info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName ");
        sql.append("        from cms_inl_repay repay");
        sql.append("        left join cms_inl_info info");
        sql.append("        on repay.inl_info_id = info.id");
        sql.append("        left join cms_inl_send send");
        sql.append("        on repay.inl_send_id = send.id");
        sql.append("        left join cms_repay_type rt on send.repay_type=rt.id ");
        sql.append(" ");
        sql.append("  where 1 = 1");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append("  and repay.status = (" + InlConstant.COMMIT_APPROVE + ") ");
        }
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append("  and (" + getSQLIn(ids, 900, "repay.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), CmsInlRepayVO.class, "", false);
    }
    /**
     * 内部拆借还款申请待还款确认数据查询
     * @param input
     * @return
     */
    public DataTablesOutput listRepayConfirmDataTable(DataTablesInput input, List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append("select repay.id, ");
        sql.append("       repay.repay_money    repayMoney, ");
        sql.append("       repay.repay_interest repayInterest, ");
        sql.append("       repay.total_money    totalMoney, ");
        sql.append("       repay.status    status, ");
        sql.append("       repay.bill_no    billNo, ");
        sql.append("       repay.repay_date    repayDate, ");
        sql.append("       info.bill_no         infoBillNo, ");
        sql.append("       send.id inlSendId,");
        sql.append("       send.send_code, ");
        sql.append("       send.borrow_in_corp, ");
        sql.append("       send.borrow_out_corp, ");
        sql.append("       send.send_date, ");
        sql.append("       send.end_date, ");
        sql.append("       send.term, ");
        sql.append("       send.unit, ");
        sql.append("       send.cur_id, ");
        sql.append("       send.send_money, ");
        sql.append("       send.interest, ");
        sql.append("       send.rate, ");
        sql.append("       send.repay_type, ");
        sql.append("       rt.repay_name as repayTypeName ");
        sql.append("  from cms_inl_repay repay ");
        sql.append("  left join CMS_INL_INFO info ");
        sql.append("    on repay.inl_info_id = info.id ");
        sql.append("  left join cms_inl_send send ");
        sql.append("    on repay.inl_send_id = send.id ");
        sql.append("  left join cms_repay_type rt on send.repay_type=rt.id ");
        sql.append("  where 1 = 1");
        sql.append("  and repay.status in (" + InlConstant.APPROVED + "," + InlConstant.CONFIRM + ") ");
        sql.append("  and (" + getSQLIn(ids, 900, "repay.borrow_in_corp") + ")");
        OrderString orderString = new OrderString("info.bill_No","desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), CmsInlRepayVO.class, "", false);
    }
    /**
     * 获取所有还款金额
     *
     * @param id
     * @return
     */
    public String getAllReplayMoneyByInfoId(String id) {
        return createSQLQuery(" select bt_nvl_num(sum(repay_money),0) from cms_inl_repay where inl_info_id = '" + id + "' and status = " + InlConstant.CONFIRM + "").uniqueResult().toString();
    }
    /**
     * 获取所有还款金额
     *
     * @param id
     * @return
     */
    public String getAllReplayMoney(String id) {
        return createSQLQuery(" select bt_nvl_num(sum(repay_money),0) from cms_inl_repay where inl_send_id = '" + id + "' and status = " + InlConstant.CONFIRM + "").uniqueResult().toString();
    }
    /**
     * 获取所有已还款利息
     *
     * @param id
     * @return
     */
    public String getAllReplayInterest(String id) {
        return createSQLQuery(" select bt_nvl_num(sum(repay_interest),0) from cms_inl_repay where inl_send_id = '" + id + "' and status = " + InlConstant.CONFIRM + "").uniqueResult().toString();
    }
    /**
     * 获取所有还款在途金额
     *
     * @param id
     * @return
     */
    public String getAllReplayTotal(String id) {
        String sql = " select (bt_nvl_num(sum(repay_interest),0)+bt_nvl_num(sum(repay_money),0)) total from cms_inl_repay " +
                "where inl_send_id = '" + id + "' and status not in( " + InlConstant.CONFIRM + "," + InlConstant.DELETE + ")";
        return createSQLQuery(sql).uniqueResult().toString();
    }
    /**
     * 获取所有审批通过得还款数据
     *
     * @return
     */
    public List<CmsInlRepayVO> getAllRepayInfoBySendId(Map<String, Object> params) {
        return listBySQLAliasToBean(" select * from CMS_INL_REPAY where INL_SEND_ID = :sendId and status != " + InlConstant.DELETE, params, CmsInlRepayVO.class);
    }
    /**
     * 获取所有还款在途数据
     *
     * @return
     */
    public List<CmsInlRepayVO> getRepayInfoBySendId(String inlSendId) {
        return listBySQLAliasToBean(" select * from CMS_INL_REPAY where INL_SEND_ID = '" + inlSendId + "' and status not in ( " + InlConstant.CONFIRM + "," + InlConstant.DELETE + ")", new HashMap<String, Object>(), CmsInlRepayVO.class);
    }
    /**
     * 根据单位id获取单据id
     *
     * @param corpIds 单位id集合
     * @return 单据数据
     */
    public List<CmsInlRepayVO> getListId(List<String> corpIds) {
        String sql = " select id as id from cms_inl_repay repay " + " where repay.status in ( " + InlConstant.APPROVED + "," + InlConstant.CONFIRM  + ") and (" + getSQLIn(corpIds, 900, "repay.borrow_in_corp") + ")";
        return listBySQLAliasToBean(sql, null, CmsInlRepayVO.class);
    }
}
