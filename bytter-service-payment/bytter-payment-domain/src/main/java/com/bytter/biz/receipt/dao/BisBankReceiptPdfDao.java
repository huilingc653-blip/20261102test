package com.bytter.biz.receipt.dao;
import com.bytter.biz.receipt.entity.BisBankReceiptPdf;
import com.bytter.biz.receipt.vo.BisBankReceiptPdfVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
/**
 * @Title: dao
 * @description: 电子回单附件
 * @author: qijh
 * @create: 2019-12-06 16:37
 **/
@Repository
public class BisBankReceiptPdfDao extends BaseDao<BisBankReceiptPdf, String> {
    /**
     * 根据明细id得到附件
     * @param receiptId
     * @return
     */
    public BisBankReceiptPdfVO getByReceiptId(String receiptId){
        String sql  = " select b.id as id,b.RECEIPT_ID as receiptId from  BIS_BANK_RECEIPT_PDF b where b.RECEIPT_ID = :receiptId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("receiptId", receiptId);
        return getBySQLAliasToBean(sql,params, BisBankReceiptPdfVO.class);
    }
}
