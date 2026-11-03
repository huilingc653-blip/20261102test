package com.bytter.check.dao;
import com.bytter.check.entity.FcBankCheckDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_BANK_CHECK_DTL
* @author onlineGenerator
* @date 2017-09-29 14:32:52
* @version 1.0
*
*/
@Repository
public class FcBankCheckDtlDao extends BaseDao<FcBankCheckDtl, String> {
	/**
	 * <b>功能介绍：</b>反结转更新银行对账明细<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adId
	 */
	public void updateBankRedoAdjustOver(String[] adId) {
		String sql = "update fc_bank_check_dtl d set d.status = -2 where exists ( select 1 from fc_bank_bill_proc p " +
                " where p.ajust_id in (:ajustId) and p.fc_bank_bill_id = d.bank_id ) ";
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("ajustId", adId);
        createSQLQuery(sql, params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>反结转更新中心对账明细<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adId
	 */
	public void updateAccRedoAdjustOver(String[] adId){
		String sql = "update fc_bank_check_dtl d set d.status = -2 where exists(select 1 from fc_compare_acc_proc p " +
                " where p.ajust_id in (:ajustId) and p.fc_compare_acc_id = d.erp_id)";
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("ajustId", adId);
        createSQLQuery(sql, params).executeUpdate();
	}
}
