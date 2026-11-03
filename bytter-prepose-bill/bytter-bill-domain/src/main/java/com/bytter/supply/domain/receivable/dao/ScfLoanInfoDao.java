package com.bytter.supply.domain.receivable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.receivable.entity.ScfLoanInfo;
import com.bytter.supply.domain.receivable.vo.ScfLoanInfoVO;
import org.springframework.stereotype.Repository;

/**
 * @author ztd
 * @description 融资业务主信息DAO
 * @since 2026/7/3
 */
@Repository
public class ScfLoanInfoDao extends BaseDao<ScfLoanInfo, String> {

    /**
     * 列表查询
     *
     * @param dataTablesInput 查询条件
     * @return 分页结果
     */
    public DataTablesOutput<ScfLoanInfoVO> dataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.loan_no,t.loan_company_id, t.loan_company_name, ");
        sql.append("        t.voucher_code, t.core_company, t.core_company_id, t.funder_id, t.funder_name, ");
        sql.append("        t.product_id, t.product_name, t.platform_id, t.platform_name, t.cur_id, ");
        sql.append("        t.cur_name, t.loan_apply_date, t.loan_amt, t.interest_mode, t.loan_lend_date, ");
        sql.append("        t.loan_actual_date, t.loan_due_date, t.loan_deadline, t.loan_rate, t.loan_interest_amt, ");
        sql.append("        t.loan_hand_fee_rate, t.loan_hand_fee_amt, t.loan_service_fee_rate, t.loan_service_fee_amt, ");
        sql.append("        t.loan_cost_amt, t.loan_actual_amt, t.loan_payment_account, t.loan_payment_account_name, ");
        sql.append("        t.loan_payment_account_bank, t.receivable_id, t.loan_status, t.online_sign,t.enable, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_loan_info t where 1=1 and t.enable = 1 ");

        Object loanStatus = dataTablesInput.getFormdata().get("loan_status");
        if (loanStatus != null && !String.valueOf(loanStatus).trim().isEmpty()) {
            sql.append(" and t.loan_status = '").append(loanStatus).append("' ");
        }

        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfLoanInfoVO.class, "", false);
    }

    /** 查询融资审批中的记录，并按工作流业务ID过滤。 */
    public DataTablesOutput<ScfLoanInfoVO> getPendingApproval(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.loan_no,t.loan_company_id, t.loan_company_name, ");
        sql.append("        t.voucher_code, t.core_company, t.core_company_id, t.funder_id, t.funder_name, ");
        sql.append("        t.product_id, t.product_name, t.platform_id, t.platform_name, t.cur_id, ");
        sql.append("        t.cur_name, t.loan_apply_date, t.loan_amt, t.interest_mode, t.loan_lend_date, ");
        sql.append("        t.loan_actual_date, t.loan_due_date, t.loan_deadline, t.loan_rate, t.loan_interest_amt, ");
        sql.append("        t.loan_hand_fee_rate, t.loan_hand_fee_amt, t.loan_service_fee_rate, t.loan_service_fee_amt, ");
        sql.append("        t.loan_cost_amt, t.loan_actual_amt, t.loan_payment_account, t.loan_payment_account_name, ");
        sql.append("        t.loan_payment_account_bank, t.receivable_id, t.loan_status, t.online_sign,t.enable, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_loan_info t where t.enable = 1 and t.loan_status = 'approval-ing' ");
        sql.append(businessSql == null || businessSql.isEmpty() ? " and 1 = 0 " : " and " + businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfLoanInfoVO.class, "t", false);
    }
}
