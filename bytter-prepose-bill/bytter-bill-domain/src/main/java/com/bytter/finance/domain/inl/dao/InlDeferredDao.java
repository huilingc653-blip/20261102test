package com.bytter.finance.domain.inl.dao;
import com.bytter.finance.domain.inl.entity.CmsInlDeferred;
import com.bytter.finance.domain.inl.vo.CmsInlDeferredVO;
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
public class InlDeferredDao extends BaseDao<CmsInlDeferred, String> {
    /**
     * 打回和暂存列表
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput input, List<String> corpIds) {
        String sql = " select deferred.id,deferred.status, deferred.inl_info_id, deferred.now_deferred_date nowDeferredDate,info.bill_No infoBillNO, info.money infoMoney, info.rate infoRate, info.interest infoInterest, info.purpose infoPurpose, " +
                " send.id inlSendId,send.send_code sendCode,send.borrow_in_corp,send.borrow_out_corp,send.send_date,send.end_date,send.term,send.unit,send.cur_id,send.send_money,send.repay_type,rt.repay_name as repayTypeName, " +
                " bt_nvl_num(repay.repay_Money_Total,0) repayMoneyTotal,info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName,bt_nvl_num(repay.repay_Interest_Total,0) repayInterestTotal " +
                " from CMS_INL_SEND send " +
                "          left join CMS_INL_INFO info " +
                "                    on send.inl_info_id = info.id " +
                "           left join cms_repay_type rt on send.repay_type=rt.id "+
                "          left join (select CMS_INL_DEFERRED.* " +
                "                     from CMS_INL_DEFERRED " +
                "                              inner join (select INL_SEND_ID, max(CREATE_DATE) create_date " +
                "                                          from CMS_INL_DEFERRED where status  != -2" +
                "                                          group by INL_SEND_ID) maxdef " +
                "                                         on CMS_INL_DEFERRED.INL_SEND_ID = maxdef.INL_SEND_ID and " +
                "                                            CMS_INL_DEFERRED.CREATE_DATE = maxdef.create_date) deferred " +
                "                    on send.id = deferred.inl_send_id" +
                "          left join (select inl_send_id, " +
                "                            sum(repay_money)    repay_Money_Total, " +
                "                            sum(repay_Interest) repay_Interest_Total " +
                "                     from CMS_INL_REPAY where status = 96" +
                "                     group by inl_send_id) repay " +
                "                    on repay.inl_send_id = send.id " +
                " where 1 = 1 " +
                "       and send.send_money > bt_nvl_num(repay.repay_Money_Total,0)" +// 过滤掉已还款完成数据
                "    and send.status in (" + InlConstant.APPROVED + ", " + InlConstant.CONFIRM + ") " +
                " and (" + getSQLIn(corpIds, 900, "send.borrow_in_corp") + ")";
        OrderString orderString = new OrderString("info.bill_No", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql, CmsInlDeferredVO.class, "info", false);
    }
    /**
     * 通过发放id获取所有得展期数据
     *
     * @return
     */
    public List<CmsInlDeferredVO> getCmsInlDeferredVOBySendId(Map<String, Object> params) {
        String sql = "select cms_inl_deferred.* " +
                "  from cms_inl_deferred " +
                " where cms_inl_deferred.status = " + InlConstant.APPROVED + " " +
                "   and cms_inl_deferred.inl_send_id = :inlSendId " +
                " order by cms_inl_deferred.create_date desc ";
        return listBySQLAliasToBean(sql, params, CmsInlDeferredVO.class);
    }
    /**
     * 审批列表
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select deferred.id,");
        sql.append("       deferred.status,");
        sql.append("       deferred.inl_info_id,");
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
        sql.append("  from CMS_INL_DEFERRED deferred");
        sql.append("  left join CMS_INL_SEND send");
        sql.append("    on deferred.INL_SEND_ID = send.id");
        sql.append("  left join CMS_INL_INFO info");
        sql.append("    on deferred.inl_info_id = info.id");
        sql.append("  left join cms_repay_type rt on send.repay_type=rt.id ");
        sql.append("  left join (select inl_send_id,");
        sql.append("                    sum(repay_money) repay_Money_Total,");
        sql.append("                    sum(repay_Interest) repay_Interest_Total");
        sql.append("               from CMS_INL_REPAY");
        sql.append("              group by inl_send_id) repay");
        sql.append("    on deferred.inl_send_id = repay.inl_send_id");
        sql.append(" where 1 = 1");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append("    and deferred.status in (" + InlConstant.COMMIT_APPROVE + ") ");
        }
        // sql.append(" and ("+getSQLIn(corpIds,900,"send.borrow_in_corp")+")");
        return getDynamicBySQL(input, sql.toString(), CmsInlDeferredVO.class, "info", false);
    }
}
