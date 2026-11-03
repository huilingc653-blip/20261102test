package com.bytter.supply.domain.payable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.constant.ScfPayableVoucherConstant;
import com.bytter.supply.domain.payable.entity.ScfVoucherRedemption;
import com.bytter.supply.domain.payable.vo.ScfVoucherRedemptionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ScfVoucherRedemptionDao extends BaseDao<ScfVoucherRedemption, String> {

    private static final String BASE_COLUMNS =
        " c.ID as id, c.VOUCHER_ID as voucherId,v.VOUCHER_NO as voucherNo,v.VOUCHER_CODE as voucherCode, c.CASH_NO as cashNo, " +
        " c.PLATFORM_ID as platformId, p.PLATFORM_NAME as platformName, " +
        " c.PRODUCT_ID as productId, pr.PRODUCT_NAME as productName, " +
        " c.CORP_ID as corpId, " +
        " c.OPEN_DATE as openDate, c.EXPIRE_DATE as expireDate, " +
        " c.CORE_CORP as coreCorp, " +
        " c.CUR_ID as curId, cur.CUR_NAME as curName, " +
        " c.VOUCHER_MONEY as voucherMoney, c.SUPPLY_ID as supplyId, " +
        " c.PAY_ACCOUNT as payAccount, c.CASH_MONEY as cashMoney, " +
        " c.PAY_DATE as payDate, c.RECEIVE_NAME as receiveName, " +
        " c.RECEIVE_ACCOUNT as receiveAccount, c.RECEIVE_BANK as receiveBank, c.SUPPLY_NAME as supplyName, " +
        " c.APPROVAL_STATUS as approvalStatus ";

    private static final String BASE_JOINS =
        " LEFT JOIN scf_payable_voucher v ON c.VOUCHER_ID = v.ID " +
        " LEFT JOIN scf_platform p ON c.PLATFORM_ID = p.ID " +
        " LEFT JOIN scf_product pr ON c.PRODUCT_ID = pr.ID " +
        " LEFT JOIN bt_currency cur ON c.CUR_ID = cur.ID ";

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_voucher_redemption c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 ");
        // 兑付记录已无暂存操作，排除付款办理等内部逻辑产生的暂存数据（审批状态为空的记录仍保留）
        sql.append(" AND (c.approval_status IS NULL OR c.approval_status <> ").append(ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_TEMP).append(") ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfVoucherRedemptionVO.class, "c", false);
    }

    /**
     * 检查应付凭证是否存在审批中或已审批的兑付记录
     * @return 存在冲突记录返回true
     */
    public boolean hasConflictRedemption(String voucherId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from scf_voucher_redemption c ");
        sql.append(" where c.voucher_id = :voucherId ");
        sql.append(" and c.approval_status in (:waitCheck, :checked) ");
        Map<String, Object> params = new HashMap<>();
        params.put("voucherId", voucherId);
        params.put("waitCheck", ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_WAIT_CHECK);
        params.put("checked", ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_CHECK);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return count != null && count.compareTo(BigDecimal.ZERO) > 0;
    }

    public ScfVoucherRedemptionVO getScfVoucherRedemptionVO(String voucherId, String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_voucher_redemption c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 ");
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" AND c.id = :id ");
            params.put("id", id);
        }
        if (StringUtils.isNotEmpty(voucherId)) {
            sql.append(" AND c.voucher_id = :voucherId ");
            params.put("voucherId", voucherId);
        }
        return getBySQLAliasToBean(sql.toString(), params, ScfVoucherRedemptionVO.class);
    }

    public DataTablesOutput<ScfVoucherRedemptionVO> getDataTablesUnionApproval(DataTablesInput tablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ").append(BASE_COLUMNS);
        sql.append(" FROM scf_voucher_redemption c ");
        sql.append(BASE_JOINS);
        sql.append(" WHERE 1=1 and c.approval_status = ").append(ScfPayableVoucherConstant.SCF_REDEMPTION_STATUS_WAIT_CHECK).append(" ");
        return getDynamicBySQL(tablesInput, sql.toString(), ScfVoucherRedemptionVO.class, "c", false);
    }
}
