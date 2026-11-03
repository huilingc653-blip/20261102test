package com.bytter.finance.domain.inl.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.SqlUtil;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.inl.entity.CmsInlChange;
import com.bytter.finance.domain.inl.vo.CmsInlChangeVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InlChangeDao extends BaseDao<CmsInlChange, String> {
    /**
     * 打回和暂存列表
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select inchange.id, inchange.status, inchange.inl_info_id, info.bill_no infoBillNo, send.id inlSendId, send.send_code sendCode, ");
        sql.append(" send.send_date, send.end_date, send.term, send.unit, send.send_money, send.cur_id, info.cur_name as curName, ");
        sql.append(" send.borrow_in_corp, info.borrow_in_corp_name borrowInCorpName, send.borrow_out_corp, info.borrow_out_corp_name borrowOutCorpName, ");
        sql.append(" send.repay_type, rt.repay_name as repayTypeName, ");
        sql.append(" bt_nvl_num(repay.repay_Money_Total, 0) repayMoneyTotal, bt_nvl_num(repay.repay_Interest_Total, 0) repayInterestTotal ");
        sql.append(" from CMS_INL_SEND send ");
        sql.append(" left join CMS_INL_INFO info on info.id = send.inl_info_id ");
        sql.append(" left join cms_repay_type rt on send.repay_type = rt.id ");
        sql.append(" left join CMS_INL_REPAYPLAN_CHANGE inchange on inchange.inl_send_id = send.id and inchange.status in (-1,11) ");
        sql.append(" left join (select inl_send_id,  ");
        sql.append("                    sum(repay_money) repay_Money_Total, ");
        sql.append("                    sum(repay_Interest) repay_Interest_Total ");
        sql.append("                from CMS_INL_REPAY where status in (" + InlConstant.CONFIRM + ") ");
        sql.append("                group by inl_send_id) repay on repay.inl_send_id = send.id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and CASE WHEN repay.repay_Money_Total IS NULL THEN 0 ELSE repay.repay_Money_Total END < send.send_money ");
        sql.append(" and send.status in (" + InlConstant.APPROVED + ", " + InlConstant.CONFIRM + ") ");
        // 单位权限
        sql.append(" and (" + SqlUtil.getInSql(corpIds, "send.borrow_in_corp", true) + ")");
        OrderString orderString = new OrderString("info.bill_No", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), CmsInlChangeVO.class, "", false);
    }

    /**
     * 待审批列表
     *
     * @param input
     * @param businessIds
     * @return
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select inchange.id,");
        sql.append("       inchange.status,");
        sql.append("       inchange.inl_info_id,");
        sql.append("       info.bill_No               infoBillNO,");
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
        sql.append(" info.borrow_in_corp_name borrowInCorpName,info.borrow_out_corp_name borrowOutCorpName,info.cur_name as curName, ");
        sql.append("       bt_nvl_num(repay.repay_Money_Total,0) repayMoneyTotal, ");
        sql.append("       bt_nvl_num(repay.repay_Interest_Total,0) repayInterestTotal ");
        sql.append("  from CMS_INL_SEND send ");
        sql.append("  left join CMS_INL_INFO info on info.id = send.inl_info_id ");
        sql.append("  left join cms_repay_type rt on send.repay_type = rt.id ");
        sql.append("  left join CMS_INL_REPAYPLAN_CHANGE inchange on inchange.inl_send_id = send.id and inchange.status in (-1, 11) ");
        sql.append("  left join (select inl_send_id,");
        sql.append("                    sum(repay_money) repay_Money_Total,");
        sql.append("                    sum(repay_Interest) repay_Interest_Total");
        sql.append("               from CMS_INL_REPAY where status = ").append(InlConstant.CONFIRM);
        sql.append("              group by inl_send_id) repay");
        sql.append("    on repay.inl_send_id = send.id");
        sql.append(" where 1 = 1");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and inchange.status in (" + InlConstant.COMMIT_APPROVE + ") ");
        }
        sql.append(" and (" + SqlUtil.getInSql(businessIds, "inchange.id", true) + ")");
        return getDynamicBySQL(input, sql.toString(), CmsInlChangeVO.class, "", false);
    }

    /**
     * 通过发放id获取所有得展期数据
     *
     * @return
     */
    public List<CmsInlChangeVO> getCmsInlChangeBySendId(Map<String, Object> params) {
        String sql = "select type.REPAY_NAME as repayTypeName,inchange.*,cp.period_name repayCycelName " +
                "  from cms_inl_repayplan_change inchange left join cms_repay_type type on type.id = inchange.repay_type " +
                "left join cms_inl_send send on send.id = inchange.inl_send_id left join tb_credit_period cp on cp.id = send.repay_cycle " +
                " where inchange.status = :status " +
                "   and inchange.inl_send_id = :inlSendId " +
                " order by inchange.CREATE_DATE desc ";
        return listBySQLAliasToBean(sql, params, CmsInlChangeVO.class);
    }

    /***
     * 根据billCode删除还款计划 -- 物理删除
     *
     * @param billCode
     */
    public void deleteRepaymentByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        String sql1 = "delete from  cms_repayment_scheduledetailed where bill_code=:billCode and VALID_SIGN = '1'";
        String sql2 = "delete from  cms_repayment_schedule where bill_no=:billCode and status = 1";
        createSQLQuery(sql1, params).executeUpdate();
        createSQLQuery(sql2, params).executeUpdate();
        getSession().flush();
    }

    /***
     * 根据billCode删除还款计划 -- 物理删除
     *
     * @param billCode
     */
    public void deleteRepaymentApproveByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        String sql1 = "delete from  cms_repayment_scheduledetailed where bill_code=:billCode and VALID_SIGN = '11'";
        String sql2 = "delete from  cms_repayment_schedule where bill_no=:billCode and status = 11";
        createSQLQuery(sql1, params).executeUpdate();
        createSQLQuery(sql2, params).executeUpdate();
        getSession().flush();
    }

    /***
     * 根据billCode删除还款计划 -- 物理删除
     *
     * @param billCode
     */
    public void updateRepaymentByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        String sql1 = "update cms_repayment_scheduledetailed set VALID_SIGN = '1' where bill_code=:billCode and VALID_SIGN = '11'";
        String sql2 = "update cms_repayment_schedule set status = 1 where bill_no=:billCode and status = 11";
        createSQLQuery(sql1, params).executeUpdate();
        createSQLQuery(sql2, params).executeUpdate();
        getSession().flush();
    }

    /**
     * 查询所有还款计划(内部拆借使用)
     *
     * @param billCode
     * @return
     */
    public List<RepaymentSchedule> listRepaymentScheduleApproveByBillNo(String billCode) {
        return listByHQL(" from RepaymentSchedule where billCode = ?0 and valid_Sign = '11' order by prediceRepayDate", billCode);
    }
}
