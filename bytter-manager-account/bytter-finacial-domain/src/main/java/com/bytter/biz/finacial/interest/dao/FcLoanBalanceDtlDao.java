package com.bytter.biz.finacial.interest.dao;
import com.bytter.biz.finacial.interest.entity.FcLoanBalanceDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_LOAN_BALANCE_DTL
* @author onlineGenerator
* @date 2017-08-26 15:09:34
* @version 1.0
*
*/
@Repository
public class FcLoanBalanceDtlDao extends BaseDao<FcLoanBalanceDtl,String>{
	/**
	 * <b>功能介绍：</b>根据还款ID查找所有对应还款数据<br>
	 * <b>日期：</b>2017年9月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param repayId
	 * @return
	 */
	public List<FcLoanBalanceDtl> getFcLoanBalanceDtlByLoanRepayId(String repayId){
		StringBuilder hql = new StringBuilder("from FcLoanBalanceDtl where loanRepayId =:repayId ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("repayId", repayId);
		return listByHQL(hql.toString(), params);
	}
}
