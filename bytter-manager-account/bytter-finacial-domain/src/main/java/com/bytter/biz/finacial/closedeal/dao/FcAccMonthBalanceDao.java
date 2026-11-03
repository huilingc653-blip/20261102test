package com.bytter.biz.finacial.closedeal.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.closedeal.entity.FcAccMonthBalance;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: FC_ACC_MONTH_BALANCE
* @author onlineGenerator
* @date 2017-07-17 18:38:13
* @version 1.0
*
*/
@Repository
public class FcAccMonthBalanceDao extends BaseDao<FcAccMonthBalance,String>{
	
	/**
	 * 功能介绍 : 转移月总账总量数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void deleteAccMonthBalance(FcMonthCloseDTO closeDTO, List<String> blnctrAccList) {
		/*String sql = "  Delete From fc_acc_month_balance  Where fc_acc_month_balance.Year_Month2 = :yearMonth "
				+ " and exists (select 1 from fc_acc where fc_acc.blnctr_acc = fc_acc_month_balance.account and fc_acc.net_id = :netId )";*/
		String sql = "  delete from fc_acc_month_balance  where year_month2 = :yearMonth "
				+ " and account in (:blnctrAccList) ";
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("netId", closeDTO.getNetCode());
		params.put("blnctrAccList", blnctrAccList);
		params.put("yearMonth", closeDTO.getYearMonth2());
		createSQLQuery(sql, params).executeUpdate();
	}
	
}
