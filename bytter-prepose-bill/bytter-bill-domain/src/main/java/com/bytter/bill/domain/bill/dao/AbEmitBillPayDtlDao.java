package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbEmitBillPay;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Service
 * @Description: 票据线上兑付信息Dao
 * @author onlineGenerator
 * @date 2017-11-8 16:40:01
 * @version 1.0
 *
 */
@Repository
public class AbEmitBillPayDtlDao extends BaseDao<AbEmitBillPay,String>{
    /**
     * @param aid
     * 根据兑付流水id获取线上兑付信息
     */
    @SuppressWarnings("unchecked")
	public AbEmitBillPay getAbEmitBillPayByAid(String aid){
        String hql="from AbEmitBillPay e where e.aid = :aid order by e.createDate desc ";
        Map<String,Object> params=new HashMap<>();
        params.put("aid", aid);
        List<AbEmitBillPay> list = this.listByHQL(hql, params);
        if(list!=null&&!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
    /**
     * @param params
     * 根据票据业务流水id删除线上兑付信息
     */
    public int updateValidSignByAid(Map<String,Object> params){
        StringBuilder hql=new StringBuilder("update AbEmitBillPay "
				+ " set validSign = :validSign ");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("updateName")){
			hql.append(",updateName = :updateName ");
		}
        if(params.containsKey("updateDate")){
            hql.append(",updateDate = :updateDate ");
        }
		hql.append(" where aid = :aid ");
		hql.append(" and validSign  = :oldValidSign ");
		return this.batchExecute(hql.toString(), params);
    }
}
