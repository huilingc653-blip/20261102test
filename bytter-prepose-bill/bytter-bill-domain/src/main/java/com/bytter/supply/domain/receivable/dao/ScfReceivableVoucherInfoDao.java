package com.bytter.supply.domain.receivable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.supply.enums.ApprovalStatusEnum;
import com.bytter.supply.enums.BusinessStatusEnum;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherInfoVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztd
 * @description 应收债权凭证信息DAO
 * @since 2026/6/30
 */
@Repository
public class ScfReceivableVoucherInfoDao extends BaseDao<ScfReceivableVoucherInfo, String> {

    /**
     * 列表查询（带分页）
     *
     * @param dataTablesInput 查询条件
     * @return 分页结果
     */
    public DataTablesOutput<ScfReceivableVoucherInfoVO> dataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.voucher_no, t.voucher_source, t.voucher_code, ");
        sql.append("        t.voucher_amt, t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.corp_id, t.corp_name, t.sign_date, t.sign_operation, t.voucher_type, ");
        sql.append("        t.expire_date, t.interest_mode, t.promise_pay_date, t.transfer_company, t.transfer_company_id, t.voucher_status, t.approval_status, t.cancel_date, t.cancel_reason, t.core_company_id, t.core_company_name, ");
        sql.append("        t.cur_id, t.cur_name, t.issue_transfer_company, t.issue_transfer_company_id, t.source_voucher_code, ");
        sql.append("        t.forehand_voucher_code, t.online_sign, t.allow_split, t.contract_code, t.contract_name, ");
        sql.append("        t.contract_amt, t.contract_date, t.contract_file_id, t.invoice_file_id, t.enable, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_voucher_info t where 1=1 ");
        // 默认按创建时间倒序排序，前端指定排序时作为次要排序
        OrderString orderString = new OrderString("t.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivableVoucherInfoVO.class, "", false);
    }


    /**
     * 应付债权凭证台账查询（带分页）
     * <p>
     * 固定条件：
     * - 单据状态(approval_status)为"开立审批通过"(open-approval-pass)、
     *   "融资审批通过"(loan-approval-pass)或"提示付款审批通过"(payment-approval-pass)
     * - 业务状态(voucher_status)为"已签收"(signed)、"已融资"(loan_success)或"已收款"(payment_success)
     *
     * @param dataTablesInput 查询条件
     * @return 分页结果
     */
    public DataTablesOutput<ScfReceivableVoucherInfoVO> getVoucherLedger(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.voucher_no, t.voucher_source, t.voucher_code, ");
        sql.append("        t.voucher_amt, t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.corp_id, t.corp_name, t.sign_date, t.sign_operation, t.voucher_type, ");
        sql.append("        t.expire_date, t.interest_mode, t.promise_pay_date, t.transfer_company, t.transfer_company_id, t.voucher_status, t.approval_status, t.cancel_date, t.cancel_reason, t.core_company_id, t.core_company_name, ");
        sql.append("        t.cur_id, t.cur_name, t.issue_transfer_company, t.issue_transfer_company_id, t.source_voucher_code, ");
        sql.append("        t.forehand_voucher_code, t.online_sign, t.allow_split, t.contract_code, t.contract_name, ");
        sql.append("        t.contract_amt, t.contract_date, t.contract_file_id, t.invoice_file_id, t.enable, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_voucher_info t where 1=1 ");
        sql.append(" and t.approval_status = '").append(ApprovalStatusEnum.APPROVAL_PASS.getCode()).append("' ");
        sql.append(" and t.voucher_status in ('").append(BusinessStatusEnum.SIGNED.getCode()).append("','");
        sql.append(BusinessStatusEnum.LOAN_SUCCESS.getCode()).append("','");
//        sql.append(BusinessStatusEnum.TRANSFERRED.getCode()).append("','");
        sql.append(BusinessStatusEnum.PAYMENT_SUCCESS.getCode()).append("') ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivableVoucherInfoVO.class, "", false);
    }

    /**
     * 付款办理（付款方式52）应收债权凭证转让选择（datatable 分页）
     * <p>
     * 可转让：单据状态=开立审批通过 且 业务状态=已签收。
     * 编辑回显：前端回传已选择的凭证id（selectedIds），即使其已进入"转让暂存"也一并返回（or id in selectedIds）。
     * 凭证编号/开立单位/平台/融资产品/线上线下等通过列检索（formdata）传入。
     *
     * @param dataTablesInput datatable 分页/排序/检索参数
     * @param selectedIds     已选择的凭证ID集合（可空）
     * @return 可转让应收债权凭证分页结果
     */
    public DataTablesOutput<ScfReceivableVoucherInfoVO> getReceivableVoucherForSelection(DataTablesInput dataTablesInput, List<String> selectedIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.voucher_no, t.voucher_source, t.voucher_code, ");
        sql.append("        t.voucher_amt, t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.corp_id, t.corp_name, t.sign_date, t.sign_operation, t.voucher_type, ");
        sql.append("        t.expire_date, t.interest_mode, t.promise_pay_date, t.transfer_company, t.transfer_company_id, t.voucher_status, t.approval_status, t.cancel_date, t.cancel_reason, t.core_company_id, t.core_company_name, ");
        sql.append("        t.cur_id, t.cur_name, t.issue_transfer_company, t.issue_transfer_company_id, t.source_voucher_code, ");
        sql.append("        t.forehand_voucher_code, t.online_sign, t.allow_split, t.contract_code, t.contract_name, ");
        sql.append("        t.contract_amt, t.contract_date, t.contract_file_id, t.invoice_file_id, t.enable, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_voucher_info t where 1=1 ");
        sql.append(" and ( (t.approval_status = '").append(ApprovalStatusEnum.APPROVAL_PASS.getCode()).append("' ");
        sql.append(" and t.voucher_status = '").append(BusinessStatusEnum.SIGNED.getCode()).append("') ");
        Map<String, Object> paramMap = new HashMap<>();
        if (selectedIds != null && !selectedIds.isEmpty()) {
            sql.append(" or t.id in (:selectedIds) ");
            paramMap.put("selectedIds", selectedIds);
        }
        sql.append(" ) ");
        // 默认按创建时间倒序排序，前端指定排序时作为次要排序
        OrderString orderString = new OrderString("t.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivableVoucherInfoVO.class, "", false, paramMap);
    }

    /**
     * 查询所有审批中的应收债权凭证（带分页）
     * <p>
     * 审批中包括：开立审批中、融资审批中、转让审批中、提示付款审批中
     *
     * @param dataTablesInput 查询条件
     * @param businessSql     流程业务ID过滤SQL
     * @return 分页结果
     */
    public DataTablesOutput<ScfReceivableVoucherInfoVO> getPendingApprovalVoucher(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.voucher_no, t.voucher_source, t.voucher_code, ");
        sql.append("        t.voucher_amt, t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.corp_id, t.corp_name, t.sign_date, t.sign_operation, t.voucher_type, ");
        sql.append("        t.expire_date, t.interest_mode, t.promise_pay_date, t.transfer_company, t.transfer_company_id, t.voucher_status, t.approval_status, t.cancel_date, t.cancel_reason, t.core_company_id, t.core_company_name, ");
        sql.append("        t.cur_id, t.cur_name, t.issue_transfer_company, t.issue_transfer_company_id, t.source_voucher_code, ");
        sql.append("        t.forehand_voucher_code, t.online_sign, t.allow_split, t.contract_code, t.contract_name, ");
        sql.append("        t.contract_amt, t.contract_date, t.contract_file_id, t.invoice_file_id, t.enable, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_voucher_info t where 1=1 ");
        sql.append(" and t.approval_status = '").append(ApprovalStatusEnum.APPROVAL_ING.getCode()).append("' ");
        if (businessSql != null && !businessSql.isEmpty()) {
            sql.append(" and ").append(businessSql);
        } else {
            // 工作流没有待办业务时不能退化为查询所有审批中记录
            sql.append(" and 1 = 0 ");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivableVoucherInfoVO.class, "t", false);
    }

    /**
     * 查询融资、转让、提示付款审批中的凭证主信息。
     * relatedCondition 使用凭证表别名 t 与业务表别名 r。
     */
    public DataTablesOutput<ScfReceivableVoucherInfoVO> getPendingApprovalVoucherByRelated(
            DataTablesInput dataTablesInput, String businessSql, String relatedCondition) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.voucher_no, t.voucher_source, t.voucher_code, ");
        sql.append("        t.voucher_amt, t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.corp_id, t.corp_name, t.sign_date, t.sign_operation, t.voucher_type, ");
        sql.append("        t.expire_date, t.interest_mode, t.promise_pay_date, t.transfer_company, t.transfer_company_id, t.voucher_status, t.approval_status, t.cancel_date, t.cancel_reason, t.core_company_id, t.core_company_name, ");
        sql.append("        t.cur_id, t.cur_name, t.issue_transfer_company, t.issue_transfer_company_id, t.source_voucher_code, ");
        sql.append("        t.forehand_voucher_code, t.online_sign, t.allow_split, t.contract_code, t.contract_name, ");
        sql.append("        t.contract_amt, t.contract_date, t.contract_file_id, t.invoice_file_id, t.enable, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_voucher_info t where 1=1 ");
        sql.append(" and exists (select 1 from ").append(relatedCondition).append(") ");
        sql.append(businessSql == null || businessSql.isEmpty() ? " and 1 = 0 " : " and ").append(businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivableVoucherInfoVO.class, "t", false);
    }

    /**
     * 根据凭证编码统计数量（用于校验债权编号唯一性）
     *
     * @param voucherCode 凭证编码
     * @return 记录数
     */
    public long countByVoucherCode(String voucherCode) {
        String sql = "select count(1) from scf_receivable_voucher_info t where t.voucher_code = :voucherCode";
        Map<String, Object> params = new HashMap<>();
        params.put("voucherCode", voucherCode);
        BigDecimal count = (BigDecimal) createSQLQuery(sql, params).uniqueResult();
        return count != null ? count.longValue() : 0L;
    }
}
