package com.bytter.biz.receipt.service;
import com.bytter.biz.receipt.dao.BisBankReceiptPdfDao;
import com.bytter.biz.receipt.entity.BisBankReceiptPdf;
import com.bytter.biz.receipt.vo.BisBankReceiptPdfVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @Title: service
 * @description: 电子回单附件表
 * @author: qijh
 * @create: 2019-12-06 16:31
 **/
@Service("bisBankReceiptPdfService")
@Transactional
public class BisBankReceiptPdfService extends BaseService<BisBankReceiptPdf, String> {
    @Autowired
    private BisBankReceiptPdfDao bisBankReceiptPdfDao;
    @Override
    public BaseDao<BisBankReceiptPdf, String> getEntityDao() {
        return bisBankReceiptPdfDao;
    }
    /**
     * 根据明细id得到附件
     * @param receiptId
     * @return
     */
    public BisBankReceiptPdfVO getByReceiptId(String receiptId){
        return bisBankReceiptPdfDao.getByReceiptId(receiptId);
    }
}
