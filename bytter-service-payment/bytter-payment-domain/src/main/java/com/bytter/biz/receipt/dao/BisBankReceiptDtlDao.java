package com.bytter.biz.receipt.dao;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.biz.receipt.vo.EBankMoreBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * 银行电子回单Dao
 *
 * @author zuoy 2018-04-09
 */
@Repository
public class BisBankReceiptDtlDao extends BaseDao<BisBankReceiptDtl, String> {
    /**
     * 付款单类型通过id获取
     * @param id
     * @return
     */
    public VNisPayBillDTO getVNisPayBill(String id){
        String sql="select * from v_nis_pay_bill where id=:id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, VNisPayBillDTO.class);
    }
    /**
     * 流水ID得到勾对的付款单id
     * @param id
     * @return
     */
    public List<EBankMoreBillVO> getMoreBill(String id){
        String sql="select b.BILL_ID,b.IS_FROM,b.DTL_ID from NIS_BILL_BANK_SERIAL_NO b where b.DTL_ID=:id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return listBySQLAliasToBean(sql, params, EBankMoreBillVO.class);
    }
    /**
     * 根据付款表id得到电子回单表ID集合
     * @param payId
     * @return 回单表ID集合
     */
    public List<String> getIdsByPayId(String payId){
        String sql="select brd.RECEIPT_ID from nis_bill_bank_serial_no nis inner join bis_receipt_dtl brd on nis.DTL_ID=brd.DTL_ID where nis.BILL_ID=:payId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("payId", payId);
        List list = this.listBySQL(sql,params);
        List<String> listId=new ArrayList<>();
        for(Object info:list){
            listId.add(((Map)info).get("RECEIPT_ID").toString());
        }
        return listId;
    }
}
