package com.bytter.check.dao;
import com.bytter.check.entity.FcBankCheckLog;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_BANK_CHECK_LOG
* @author onlineGenerator
* @date 2017-09-29 14:33:58
* @version 1.0
*
*/
@Repository
public class FcBankCheckLogDao extends BaseDao<FcBankCheckLog,String>{
	
	/**
	 * <b>功能介绍：</b>反结转更新对账日志<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adId
	 */
	public void updateRedoCheckLog(String[] adId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_bank_check_log g set g.op_type = 'D' where exists ( ");
		sql.append(" select 1 from fc_bank_balance_adjust a where a.bank_acc = g.bank_acc and a.begin_date <= g.begin_date and a.end_date >= g.end_date ");
		sql.append(" and a.id in (:adjustId) ");
		sql.append(" ) ");
		//params.put("bankAcc", balAdjust.getBankAcc());
    	params.put("adjustId", adId);
    	createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>根据BillId获取日志<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param billId
	 * @return
	 */
	public List<FcBankCheckLog> getCheckLogByBillId(String billId){
		return listBy("billId", billId);
	}
}
