package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBillTransferDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Service
 * @Description: 票据下拨流水关联信息
 * @author onlineGenerator
 * @date 2017-11-1 16:40:01
 * @version 1.0
 *
 */
@Repository
public class AbBillTransferDtlDao extends BaseDao<AbBillTransferDtl,String>{
    /**
     * 根据下拨单id批量删除选票信息
     */
    public int deleteByTransferId(String transferId){
        return createSQLQuery("delete from AB_BILL_TRANSFER_DTL where TRANSFER_ID=?",transferId).executeUpdate();
    }
    /**
     * 根据下拨单id获取选票票据流水id
     */
    @SuppressWarnings("unchecked")
	public List<AbBillTransferDtl> getBillAidArrayByTransferId(String transferId){
        String hql = "select t from AbBillTransferDtl t where t.transferId = :transferId ";
        Map<String, Object> params = new HashMap<String, Object> ();
        params.put("transferId", transferId);
        return createQuery(hql,params).list();
    }
    /**
     * 根据下拨单id获取选票票据流水id
     */
    @SuppressWarnings("unchecked")
	public List<AbBillTransferDtl> getAbBillTransferDtlByAid(String billAid){
        String hql = "select t from AbBillTransferDtl t where t.billAid = :billAid ";
        Map<String, Object> params = new HashMap<String, Object> ();
        params.put("billAid", billAid);
        return createQuery(hql,params).list();
    }
}
