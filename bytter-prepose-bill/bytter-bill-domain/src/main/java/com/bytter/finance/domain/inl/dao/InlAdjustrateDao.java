package com.bytter.finance.domain.inl.dao;
import com.bytter.finance.domain.inl.entity.CmsInlAdjustrate;
import com.bytter.finance.domain.inl.vo.CmsInlAdjustrateVO;
import com.bytter.finance.domain.provide.vo.RateAdjustBean;
import com.bytter.finance.constant.InlConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public class InlAdjustrateDao extends BaseDao<CmsInlAdjustrate, String> {
    /**
     * 打回和暂存列表
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput input, List<String> corpIds) {
        String sql = "select adjust.id," +
                "       adjust.status," +
                "       adjust.inl_info_id," +
                "       info.bill_No               infoBillNO," +
                "       info.money               infoMoney," +
                "       info.rate               infoRate," +
                "       info.interest               infoInterest," +
                "       info.purpose               infoPurpose," +
                "       send.id             inlSendId," +
                "       send.send_code             sendCode," +
                "       send.borrow_in_corp," +
                "       send.borrow_out_corp," +
                "       send.send_date," +
                "       send.end_date," +
                "       send.term," +
                "       send.unit," +
                "       send.cur_id," +
                "       send.send_money," +
                "       send.repay_type," +
                "       rt.repay_name as repayTypeName,"+
                "       info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName, " +
                "       bt_nvl_num(repay.repay_money_total,0) repayMoneyTotal," +
                "       bt_nvl_num(repay.repay_interest_total,0) repayInterestTotal" +
                "  from CMS_INL_INFO info" +
                "  left join CMS_INL_SEND send" +
                "    on info.id = send.inl_info_id" +
                "   left join cms_repay_type rt on send.repay_type=rt.id "+
                "  left join CMS_INL_ADJUSTRATE adjust" +
                "    on adjust.inl_info_id = info.id and adjust.status in (-1,11)" +
                "  left join (select inl_send_id," +
                "                    sum(repay_money) repay_money_total," +
                "                    sum(repay_Interest) repay_interest_total" +
                "               from CMS_INL_REPAY where status = 96" +
                "              group by inl_send_id) repay" +
                "    on repay.inl_send_id = send.id" +
                " where 1 = 1" +
                "    and send.FLOAT_WAY = '01'" +// 查询固定利率数据，只有固定利率才可以进行利率调整
                "    and send.send_money > bt_nvl_num(repay.repay_money_total,0)" +
                "    and send.status in (" + InlConstant.APPROVED + ", " + InlConstant.CONFIRM + ") " +
                " and (" + getSQLIn(corpIds, 900, "send.borrow_in_corp") + ")";
        OrderString orderString = new OrderString("info.bill_No", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql, CmsInlAdjustrateVO.class, "", false);
    }
    /**
     * 打回和暂存列表
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select adjust.id,");
        sql.append("       adjust.status,");
        sql.append("       adjust.inl_info_id,");
        sql.append("       info.bill_No               infoBillNO,");
        sql.append("       info.money               infoMoney,");
        sql.append("       info.rate               infoRate,");
        sql.append("       info.interest               infoInterest,");
        sql.append("       info.purpose               infoPurpose,");
        sql.append("       send.id             inlSendId,");
        sql.append("       send.send_code             sendCode,");
        sql.append("       send.borrow_in_corp,");
        sql.append("       send.borrow_out_corp,");
        sql.append("       send.send_date,");
        sql.append("       send.end_date,");
        sql.append("       send.term,");
        sql.append("       send.unit,");
        sql.append("       send.cur_id,");
        sql.append("       send.send_money,");
        sql.append("       send.repay_type,");
        sql.append("       rt.repay_name as repayTypeName,");
        //TODO 补充名称信息
        sql.append(" info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName, ");
        sql.append("       repay.repay_Money_Total ,");
        sql.append("       repay.repay_Interest_Total");
        sql.append("  from CMS_INL_INFO info");
        sql.append("  left join CMS_INL_SEND send");
        sql.append("    on info.id = send.inl_info_id");
        sql.append("  left join cms_repay_type rt on send.repay_type=rt.id ");
        sql.append("  left join CMS_INL_ADJUSTRATE adjust");
        sql.append("    on adjust.inl_info_id = info.id and adjust.status in (-1,11)");
        sql.append("  left join (select inl_send_id,");
        sql.append("                    sum(repay_money) repay_Money_Total,");
        sql.append("                    sum(repay_Interest) repay_Interest_Total");
        sql.append("               from CMS_INL_REPAY where status = 96");
        sql.append("              group by inl_send_id) repay");
        sql.append("    on repay.inl_send_id = send.id");
        sql.append(" where 1 = 1");
        sql.append(" and send.status in (" + InlConstant.APPROVED + ", " + InlConstant.CONFIRM + ") ");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append("    and adjust.status in (" + InlConstant.COMMIT_APPROVE + ") ");
        }
        // sql.append(" and ("+getSQLIn(corpIds,900,"send.borrow_in_corp")+")");
        return getDynamicBySQL(input, sql.toString(), CmsInlAdjustrateVO.class, "", false);
    }
    /**
     * 查询审批通过的利率调整记录
     *
     * @return
     */
    public List<CmsInlAdjustrateVO> getCmsInlAdjustrateVOBySendId(Map<String, Object> params) {
        String sql = "select CMS_INL_ADJUSTRATE.* " +
                "  from CMS_INL_ADJUSTRATE " +
                " where CMS_INL_ADJUSTRATE.status = " + InlConstant.APPROVED + " " +
                "   and CMS_INL_ADJUSTRATE.inl_send_id = :inlSendId " +
                " order by CMS_INL_ADJUSTRATE.EXEC_DATE desc ";
        return listBySQLAliasToBean(sql, params, CmsInlAdjustrateVO.class);
    }
    /**
     * 获取调整记录
     *
     * @param sendId
     * @return
     */
    public List<CmsInlAdjustrate> listCmsInlAdjustrate(String sendId) {
        return listByHQL(" from CmsInlAdjustrate where status = 95 and inlSendId = ?0 ", sendId);
    }
    /**
     * 查询审批通过的利率调整记录
     *
     * @return
     */
    public List<RateAdjustBean> getRateAdjustBeanVOBySendId(Map<String, Object> params) {
        String sql = "select cia.inl_info_id billCode, " +
                "       cia.ADJUST_RATE plRate," +
                "       cia.ADJUST_DEFERRED_RATE deferredRate," +
                "       cia.ADJUST_OVERDUE_RATE overdueRate," +
                "       cia.EXEC_DATE enterDate" +
                "  from CMS_INL_ADJUSTRATE cia" +
                " where cia.status = " + InlConstant.APPROVED + " " +
                "   and cia.inl_send_id = :inlSendId " +
                " order by cia.EXEC_DATE desc ";
        return listBySQLAliasToBean(sql, params, RateAdjustBean.class);
    }
}
