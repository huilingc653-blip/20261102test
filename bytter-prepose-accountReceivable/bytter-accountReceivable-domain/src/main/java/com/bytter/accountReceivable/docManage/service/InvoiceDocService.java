package com.bytter.accountReceivable.docManage.service;

import com.bytter.accountReceivable.docManage.dao.InvoiceDocDao;
import com.bytter.accountReceivable.docManage.entity.InvoiceDoc;
import com.bytter.accountReceivable.docManage.vo.InvoiceDocVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @author yangxd
 * @date 2025-03-26
 * @version 1.0
 *
 */
@Transactional
@Repository
public class InvoiceDocService extends BaseService<InvoiceDoc, String>{

    @Autowired
    InvoiceDocDao invoiceDocDao;

    @Override
    public BaseDao<InvoiceDoc, String> getEntityDao() {
        return invoiceDocDao;
    }

    public void saveInvoiceDoc(InvoiceDoc invoiceDoc){
        invoiceDocDao.save(invoiceDoc);
    }
    public void updateInvoiceDoc(InvoiceDoc invoiceDoc){
        invoiceDocDao.update(invoiceDoc);
    }


    public List<InvoiceDocVo> queryInvoiceDocbYDocId(String docId){
        return invoiceDocDao.queryInvoiceDocbYDocId(docId);
    }

    public int delInvoiceDocByBillId(String billId){
       return invoiceDocDao.delInvoiceDocByBillId(billId);
    }
}
