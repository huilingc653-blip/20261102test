package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillAccDtl;
import com.bytter.core.base.BaseDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
* @Title: 票据账户余额Dao
* @Description: AB_BILL_ACC_BAL
* @author zhengg
* @date 2017-09-28 19:20:01
* @version 1.0
*
*/
@Repository
public class AbBillAccDtlDao extends BaseDao<AbBillAccDtl,String>{
	/**
	 * 根据流水ID获取有效的票据余额明细数据
	 * @param recordId 流水ID
	 * @return
	 */
	public AbBillAccDtl getAbBillAccDtlByRecordIdAndValid(String recordId){
		String hql="from AbBillAccDtl where recordId=:recordId and validSign=:validSign order by createDate desc";
		Map<String,Object> params=new HashMap<>();
		params.put("recordId", recordId);
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		Query query=this.createQuery(hql, params).setMaxResults(1);
		return (AbBillAccDtl) query.uniqueResult();
	}
}
