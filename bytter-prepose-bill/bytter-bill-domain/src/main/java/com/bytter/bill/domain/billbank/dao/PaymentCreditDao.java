package com.bytter.bill.domain.billbank.dao;
import com.bytter.bill.domain.billbank.entity.PaymentCredit;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * <br>
 * <b>类名：</b>支付信用Dao<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2018-04-19 <br>
 *
 */
@Repository("paymentCreditDao")
public class PaymentCreditDao extends BaseDao<PaymentCredit,String> {
    /**
     * <br>
     * <b>功能：</b>根据票据号删除支付信用信息<br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param billCode 票据号
     */
    public void deletePaymentCreditListByBillCode(String billCode, String sbbllRngStrtSn, String sbbllRngEndSn) {
        String hql = "DELETE PaymentCredit q WHERE q.billCode = :billCode and q.sbbllRngStrtSn = :sbbllRngStrtSn and q.sbbllRngEndSn = :sbbllRngEndSn";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        params.put("sbbllRngStrtSn", sbbllRngStrtSn);
        params.put("sbbllRngEndSn", sbbllRngEndSn);
        this.batchExecute(hql,params);
    }
}
