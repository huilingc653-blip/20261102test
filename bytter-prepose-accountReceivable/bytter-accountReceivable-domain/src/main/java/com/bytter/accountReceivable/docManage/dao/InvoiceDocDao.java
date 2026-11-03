package com.bytter.accountReceivable.docManage.dao;

import com.bytter.accountReceivable.docManage.entity.InvoiceDoc;
import com.bytter.accountReceivable.docManage.vo.InvoiceDocVo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @author yangxd
 * @date 2025-03-26
 * @version 1.0
 *
 */
@Repository
public class InvoiceDocDao extends BaseDao<InvoiceDoc, String>{

    private List<InvoiceDocVo> getInvoiceDocByBillIdOr(String id){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder("select");


       return listBySQLAliasToBean(sql.toString(),params, InvoiceDocVo.class);
    }

    public List<InvoiceDocVo> queryInvoiceDocbYDocId(String docId) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ID, CODE, type, NUMBER, PAR_AMT as parAmt, TAXINC_AMT as taxincAmt,");
        sql.append(" CHECK_CODE as checkCode, DRA_DATE as draDate, BILL_ID as billId,");
        sql.append(" BILL_NO as billNo from doc_invoice dm where 1=1");
        sql.append(" and bill_id =:docId");
        params.put("docId",docId);
        return listBySQLAliasToBean(sql.toString(),params, InvoiceDocVo.class);
    }




    public int delInvoiceDocByBillId(String billId) {
        Map<String,String> params = new HashMap<String,String>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from DOC_INVOICE where BILL_ID =:billId ");
        params.put("billId",billId);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
