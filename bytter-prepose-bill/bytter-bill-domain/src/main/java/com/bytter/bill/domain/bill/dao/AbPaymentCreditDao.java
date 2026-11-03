package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbPaymentCredit;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>类名：</b>支付信用Dao<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2018-04-19 <br>
 *
 * @author zhenghj
 */
@Repository
public class AbPaymentCreditDao extends BaseDao<AbPaymentCredit,String> {
    /**
     * 根据票据号获取支付信用数据
     * @param origNo 票据主表id
     * @return 支付信用数据
     */
    public AbPaymentCredit getAbPaymentCreditVoByOrigNo(String origNo){
        String hql="from AbPaymentCredit where billCode=:origNo ";
        Map<String,Object> params=new HashMap<>();
        params.put("origNo", origNo);
        hql+=" order by enterDate asc ";
        List<AbPaymentCredit> list=this.listByHQL(hql, params);
        if(list!=null&&!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
}
