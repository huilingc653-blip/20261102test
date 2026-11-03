package com.bytter.supply.domain.payable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.constant.ScfPayableVoucherConstant;
import com.bytter.supply.domain.payable.entity.ScfPayableVoucher;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScfPayableVoucherDao extends BaseDao<ScfPayableVoucher, String> {

    private static final String BASE_COLUMNS =
        " c.ID as id, c.VOUCHER_NO as voucherNo, c.VOUCHER_CODE as voucherCode, " +
        " c.TEMPLATE_ID as templateId, c.VERSION_ID as versionId, " +
        " c.PLATFORM_ID as platformId, p.PLATFORM_NAME as platformName, " +
        " c.PRODUCT_ID as productId, pr.PRODUCT_NAME as productName, " +
        " c.CORP_ID as corpId, cpt.NAME as corpName, " +
        " c.OPEN_DATE as openDate, c.EXPIRE_DATE as expireDate, " +
        " c.CORE_CORP as coreCorp, c.CORE_CORP_NAME as coreCorpName, " +
        " c.CUR_ID as curId, cur.CUR_NAME as curName, " +
        " c.VOUCHER_MONEY as voucherMoney, c.SUPPLY_ID as supplyId, c.SUPPLY_NAME as supplyName, " +
        " c.RECEIVE_ACCOUNT as receiveAccount, c.RECEIVE_BANK as receiveBank, c.ONLINE_SIGN as onlineSign, " +
        " c.QUOTA_CODE as quotaCode, c.QUOTA_NAME as quotaName, c.QUOTA_BALANCE as quotaBalance, " +
        " c.CONTRACT_CODE as contractCode, c.CONTRACT_NAME as contractName, " +
        " c.CONTRACT_MONEY as contractMoney, c.CONTRACT_DATE as contractDate, " +
        " c.CONTRACT_FILE as contractFile, c.INVOICE_FILE as invoiceFile, " +
        " c.VOUCHER_SOURCE as voucherSource, c.INTEREST_MODE as interestMode, " +
        " c.PROMISE_PAY_DATE as promisePayDate, c.SUPPLY_CREDIT_CODE as supplyCreditCode, " +
        " c.VOUCHER_STATUS as voucherStatus, c.APPROVAL_STATUS as approvalStatus, " +
        " c.PAY_ID as payId, c.PAY_NO as payNo, " +
        " c.CANCEL_REASON as cancelReason, c.CANCEL_DATE as cancelDate, " +
        " c.CREATE_BY as createBy, c.CREATE_DATE as createDate, " +
        " c.UPDATE_BY as updateBy, c.UPDATE_DATE as updateDate ";

    private static final String BASE_JOINS =
        " LEFT JOIN scf_platform p ON c.PLATFORM_ID = p.ID " +
        " LEFT JOIN scf_product pr ON c.PRODUCT_ID = pr.ID " +
        " LEFT JOIN bt_currency cur ON c.CUR_ID = cur.ID " +
        " LEFT JOIN sys_corp cpt ON c.CORP_ID = cpt.ID ";

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_payable_voucher c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfPayableVoucherVO.class, "c", false);
    }

    public DataTablesOutput<ScfPayableVoucherVO> getEstablishedPayVoucher(DataTablesInput tablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_payable_voucher c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 and c.voucher_status = '").append(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED).append("' ");
        return getDynamicBySQL(tablesInput, sql.toString(), ScfPayableVoucherVO.class, "c", false);
    }

    /**
     * 付款办理51-应付债权凭证选票（datatable 分页）
     * <p>
     * 仅查询单据状态"审批通过"、业务状态"已开立"的应付债权凭证，
     * 且排除已被付款单关联（pay_id 非空）的凭证。
     * 开立单位 corp_id、平台 platform_id、融资产品 product_id、线上线下 online_sign 等通过列检索传入。
     *
     * @param tablesInput datatable 分页/排序/检索参数
     * @return 符合条件的应付债权凭证
     */
    public DataTablesOutput<ScfPayableVoucherVO> getPayVoucherForSelection(DataTablesInput tablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_payable_voucher c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 ");
        sql.append(" AND c.approval_status = '").append(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK).append("' ");
        sql.append(" AND c.voucher_status = '").append(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED).append("' ");
        // 排除已关联付款单的凭证（付款单审批通过后会在凭证上写入 pay_id/pay_no）
        sql.append(" AND (c.PAY_ID IS NULL OR c.PAY_ID = '') ");
        return getDynamicBySQL(tablesInput, sql.toString(), ScfPayableVoucherVO.class, "c", false);
    }

    /**
     * 付款办理-按凭证ID集合查询应付债权凭证（view回显，不限状态）
     *
     * @param ids 凭证ID集合
     * @return 应付债权凭证
     */
    public List<ScfPayableVoucherVO> listPayableVoucherByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_payable_voucher c ").append(BASE_JOINS);
        sql.append(" WHERE c.id IN (:ids) ");
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return listBySQLAliasToBean(sql.toString(), params, ScfPayableVoucherVO.class);
    }

    public DataTablesOutput getDataTablesUnionApproval(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_payable_voucher c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 and c.approval_status = ").append(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_WAIT_OPENED_CHECK).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfPayableVoucherVO.class, "c", false);
    }

    public ScfPayableVoucherVO getPayableVoucherVOById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_payable_voucher c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE c.id = :id ");
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, ScfPayableVoucherVO.class);
    }

    public DataTablesOutput<ScfPayableVoucherVO> getDataTablesUnionQuery(DataTablesInput tablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_payable_voucher c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 ");
        // 开立审批通过/兑付审批通过/兑付审批中/兑付打回/已作废
        sql.append(" and c.approval_status in('").append(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_OPENED_CHECK).append("',")
                .append("'").append(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_REDEMPTION_CHECK).append("',")
                .append("'").append(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_WAIT_REDEMPTION_CHECK).append("',")
                .append("'").append(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_REDEMPTION_BACK).append("',")
                .append("'").append(ScfPayableVoucherConstant.SCF_PAYABLE_STATUS_CHANGE_REFUSE).append("') ");
        // 已开立/已兑付/兑付中/已作废
        sql.append(" and c.voucher_status in('").append(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_OPENED).append("',")
                .append("'").append(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_REDEEMED).append("',")
                .append("'").append(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_REDEEMING).append("',")
                .append("'").append(ScfPayableVoucherConstant.SCF_PAYABLE_BUSINESS_VOIDED).append("') ");
        return getDynamicBySQL(tablesInput, sql.toString(), ScfPayableVoucherVO.class, "c", false);
    }

    /**
     * 根据债权凭证编号统计记录数（用于校验凭证编号唯一性）
     *
     * @param voucherCode 债权凭证编号
     * @param excludeId   需要排除的主键ID（修改时排除当前记录，新增校验传 null）
     * @return 记录数
     */
    public long countByVoucherCode(String voucherCode, String excludeId) {
        StringBuilder sql = new StringBuilder("select count(1) from scf_payable_voucher t where t.voucher_code = :voucherCode");
        Map<String, Object> params = new HashMap<>();
        params.put("voucherCode", voucherCode);
        if (excludeId != null && !excludeId.isEmpty()) {
            sql.append(" and t.id <> :excludeId ");
            params.put("excludeId", excludeId);
        }
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return count != null ? count.longValue() : 0L;
    }
}
