package com.bytter.finance.domain.inl.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.inl.entity.CmsInlAbolish;
import com.bytter.finance.domain.inl.vo.CmsInlAbolishVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class InlAbolishDao extends BaseDao<CmsInlAbolish, String> {
    /**
     * 查询可作废内部拆借申请数据
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput listAbolishInfoDataTable(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select abolish.id,");
        sql.append(" info.status, ");
        sql.append(" info.id inlInfoId, ");
        sql.append(" info.bill_no inlInfoBillNo, ");
        sql.append(" info.status inlInfoStatus, ");
        sql.append(" info.money infoMoney,");
        sql.append(" info.rate infoRate,");
        sql.append(" info.interest infoInterest,");
        sql.append(" info.purpose infoPurpose,");
        sql.append(" info.con_type_id, ");
        sql.append(" info.con_id, ");
        sql.append(" info.borrow_in_corp, ");
        sql.append(" info.borrow_out_corp, ");
        sql.append(" info.cur_id, ");
        sql.append(" info.money, ");
        sql.append(" info.start_date, ");
        sql.append(" info.end_date, ");
        sql.append(" info.repay_type, ");
        sql.append(" rt.repay_name as repayTypeName,");
        sql.append(" info.term, ");
        sql.append(" info.unit, ");
        sql.append(" info.rate, ");
        sql.append(" info.interest, ");
        sql.append(" info.purpose, ");
        sql.append(" info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName, ");
        sql.append(" info.rmk, ");
        sql.append(" info.attachment_id, ");
        sql.append(" info.gua_contract_id ");
        sql.append("   from cms_inl_info info ");
        sql.append(" left join cms_repay_type rt on info.repay_type=rt.id ");
        sql.append("   left join (select inl_info_id, sum(send_money) send_money ");
        sql.append("                from cms_inl_send ");
        sql.append("               group by inl_info_id) send ");
        sql.append("     on info.id = send.inl_info_id ");
        sql.append("   left join CMS_INL_ABOLISH abolish");
        sql.append("   on info.id = abolish.inl_info_id");
        sql.append("   where 1 = 1");
        sql.append("         and (send.send_money=0 or send.send_money is null) ");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append("         and info.status not in ( " + InlConstant.COMMIT_APPROVE
                    + "," + InlConstant.DELETE + ")");
        }
        sql.append("         and (" + getSQLIn(corpIds, 900, "info.borrow_in_corp") + ")");
        OrderString orderString = new OrderString("info.bill_no", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), CmsInlAbolishVO.class, "info", false);
    }
    /**
     * 查询可作废内部拆借申请数据
     *
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput listInlAbolishApprove(DataTablesInput input, List<String> ids) {
        String sql = " select abolish.id," +
                "       abolish.status, " +
                "       info.id inlInfoId, " +
                "       info.bill_no inlInfoBillNo, " +
                "       info.status inlInfoStatus, " +
                "       info.money               infoMoney," +
                "       info.rate               infoRate," +
                "       info.interest               infoInterest," +
                "       info.purpose               infoPurpose," +
                "       info.con_type_id, " +
                "       info.con_id, " +
                "       info.is_skip_send, " +
                "       info.borrow_in_corp, " +
                "       info.borrow_out_corp, " +
                "       info.send_code, " +
                "       info.send_type, " +
                "       info.cur_id, " +
                "       info.money, " +
                "       info.start_date, " +
                "       info.end_date, " +
                "       info.repay_type, " +
                "       rt.repay_name as repayTypeName,"+
                "       info.term, " +
                "       info.unit, " +
                "       info.interest, " +
                "       info.purpose, " +
                "       info.is_hang_credit, " +
                "       info.ration_code, " +
                "       info.is_hang_guarantee, " +
                "       info.gua_contract, " +
                "       info.is_budget, " +
                //TODO 补充名称信息
                " info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName," +
                "       info.rmk, " +
                "       info.attachment_id, " +
                "       info.is_con, " +
                "       info.gua_contract_id " +
                "   from CMS_INL_ABOLISH abolish" +
                "   left join cms_inl_info info " +
                "   on abolish.inl_info_id = info.id  " +
                "   left join cms_repay_type rt on info.repay_type=rt.id"+
                "   where 1 = 1" +
                "   and abolish.status = " + InlConstant.COMMIT_APPROVE + "";
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            String businessId = " and (" + getSQLIn(ids, 900, "abolish.id") + ")";
            sql.concat(businessId);
        }
        return getDynamicBySQL(input, sql, CmsInlAbolishVO.class, "abolish", false);
    }
}
