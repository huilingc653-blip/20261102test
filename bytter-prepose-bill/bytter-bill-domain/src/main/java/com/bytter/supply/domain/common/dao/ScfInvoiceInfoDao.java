package com.bytter.supply.domain.common.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.common.entity.ScfInvoiceInfo;
import com.bytter.supply.domain.common.vo.ScfInvoiceInfoVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfInvoiceInfoDao
 * @Description: 应付/应收债券发票信息DAO
 * @since 2026/7/3
 */
@Repository
public class ScfInvoiceInfoDao extends BaseDao<ScfInvoiceInfo, String> {

    public void deleteByPayId(String payId) {
        String sql = "DELETE FROM scf_invoice_info WHERE PAY_ID = :payId";
        Map<String, Object> params = new HashMap<>();
        params.put("payId", payId);
        createSQLQuery(sql, params).executeUpdate();
    }

    public void deleteByReceivableId(String receivableId) {
        String sql = "DELETE FROM scf_invoice_info WHERE RECEIVABLE_ID = :receivableId";
        Map<String, Object> params = new HashMap<>();
        params.put("receivableId", receivableId);
        createSQLQuery(sql, params).executeUpdate();
    }

    public List<com.bytter.supply.domain.common.vo.ScfInvoiceInfoVO> getInvoiceInfoByPayId(String payId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.id as id, t.invoice_type as invoiceType, t.invoice_number as invoiceNumber, t.invoice_code as invoiceCode, ");
        sql.append("        t.bill_date as billDate, t.cur_name as curName, t.cur_id as curId, ");
        sql.append("        t.total_amt as totalAmt, t.tax_rate as taxRate, t.tax_amt as taxAmt, t.bill_amt as billAmt, ");
        sql.append("        t.seller_name as sellerName, t.buyer_name as buyerName, t.pay_id as payId, ");
        sql.append("        t.receivable_id as receivableId, t.sign as sign, ");
        sql.append("        t.create_by as createBy, t.create_date as createDate, t.update_by as updateBy, t.update_date as updateDate ");
        sql.append(" FROM scf_invoice_info t WHERE t.PAY_ID = :payId ");
        Map<String, Object> params = new HashMap<>();
        params.put("payId", payId);
        return listBySQLAliasToBean(sql.toString(), params, ScfInvoiceInfoVO.class);
    }

    /**
     * 列表查询
     *
     * @param dataTablesInput 查询条件
     * @return 分页结果
     */
    public DataTablesOutput<ScfInvoiceInfoVO> dataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.invoice_type, t.invoice_number, t.invoice_code, t.bill_date, t.cur_name, t.cur_id, ");
        sql.append("        t.total_amt, t.tax_rate, t.tax_amt, t.bill_amt, ");
        sql.append("        t.seller_name, t.buyer_name, t.pay_id, t.receivable_id, t.sign, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_invoice_info t where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfInvoiceInfoVO.class, "", false);
    }
}
