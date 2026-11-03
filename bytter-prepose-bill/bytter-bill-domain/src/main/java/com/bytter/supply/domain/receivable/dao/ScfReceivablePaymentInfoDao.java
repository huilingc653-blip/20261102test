package com.bytter.supply.domain.receivable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.receivable.entity.ScfReceivablePaymentInfo;
import com.bytter.supply.domain.receivable.vo.ScfReceivablePaymentInfoVO;
import org.springframework.stereotype.Repository;

/**
 * @author ztd
 * @description 债权凭证收款明细DAO
 * @since 2026/7/10
 */
@Repository
public class ScfReceivablePaymentInfoDao extends BaseDao<ScfReceivablePaymentInfo, String> {

    /** 查询付款审批中的记录，并按工作流业务ID过滤。 */
    public DataTablesOutput<ScfReceivablePaymentInfoVO> getPendingApproval(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, (select v.id from scf_receivable_voucher_info v where v.voucher_code = t.voucher_code) as business_key, ");
        sql.append("        t.voucher_code, t.payment_no, t.payment_status, ");
        sql.append("        t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.receipt_company, t.receipt_company_id, t.sign_date, t.expire_date, ");
        sql.append("        t.issue_transfer_company, t.issue_transfer_company_id, ");
        sql.append("        t.cur_id, t.cur_name, t.voucher_amt, ");
        sql.append("        t.core_company_id, t.core_company_name, ");
        sql.append("        t.rec_account_no, t.rec_account_name, t.rec_bank, t.rec_amt, t.receipt_date, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_payment_info t where t.enable = 1 and t.payment_status = 'approval-ing' ");
        sql.append(businessSql == null || businessSql.isEmpty() ? " and 1 = 0 " : " and " + businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivablePaymentInfoVO.class, "t", false);
    }

    /**
     * 列表查询
     *
     * @param dataTablesInput 查询条件
     * @return 分页结果
     */
    public DataTablesOutput<ScfReceivablePaymentInfoVO> dataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.voucher_code, t.payment_no, t.payment_status, ");
        sql.append("        t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.receipt_company, t.receipt_company_id, t.sign_date, t.expire_date, ");
        sql.append("        t.issue_transfer_company, t.issue_transfer_company_id, ");
        sql.append("        t.cur_id, t.cur_name, t.voucher_amt, ");
        sql.append("        t.core_company_id, t.core_company_name, ");
        sql.append("        t.rec_account_no, t.rec_account_name, t.rec_bank, t.rec_amt, t.receipt_date, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_payment_info t where 1=1 and t.enable = 1 ");

        // payment_status 为空时查询全部状态，传值时按指定状态筛选
        Object paymentStatus = dataTablesInput.getFormdata().get("payment_status");
        if (paymentStatus != null && !String.valueOf(paymentStatus).trim().isEmpty()) {
            sql.append(" and t.payment_status = '").append(paymentStatus).append("' ");
        }


        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivablePaymentInfoVO.class, "", false);
    }
}
