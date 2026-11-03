package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.closedeal.entity.FcMonthLedger;
import com.bytter.core.base.BaseDao;
import com.bytter.biz.payment.api.dto.FcInitAccDTO;
import com.bytter.biz.payment.api.dto.FcInitSubjectDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_MONTH_LEDGER
* @author onlineGenerator
* @date 2017-07-17 18:36:41
* @version 1.0
*
*/
@Repository
public class FcMonthLedgerDao extends BaseDao<FcMonthLedger,String> {
	
	/**
	 * 功能介绍 : 查询月总账记录中最近的月结记录。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcMonthLedger> queryMaxMonthLedger(FcMonthCloseDTO closeDTO) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from FcMonthLedger fc where fc.netCode = :netId ");
		hql.append(" and fc.yearMonth2 in (select max(yearMonth2) from FcMonthLedger fm where fm.netCode = :netId)  ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		return listByHQL(hql.toString(), params);
	}
	
	/**
	 * 功能介绍 : 查询网点初始化记录。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 * 改为由 结算模块提供
	 */
	/*public List<FcInitAccDTO> queryNetInitAcc(FcMonthCloseDTO closeDTO) {
		StringBuilder sql=new StringBuilder();
		sql.append(" select distinct c.account as account,a.acc_name as accName,a.balance_way as balanceWay, ");
		sql.append(" c.cumulation as cumulation,c.balance as balance, c.status as status, ");
		sql.append(" c.year_balance as yearBalance,c.year_total as yearTotal, ");
		sql.append(" a.net_Id as netId,a.subject_id as subjectId,a.cur_id as curId ");
		sql.append(" from FC_INIT_ACC c  join  fc_acc a ");
		sql.append(" on c.account = a.blnctr_acc  where c.Checker_Code Is Not Null ");
		sql.append(" and a.net_id = :netId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		return listBySQLAliasToBean(sql.toString(), params, FcInitAccDTO.class);
	}*/
	
	/**
	 * 功能介绍 : 查询初始化账户科目的余额合计。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 * 改为由结算领域提供
	 * Fc_Init_Acc 冗余 字段：Cur_id、Subject_id、net_id; 条件：Fc_Init_Acc.Account = Fc_Acc.Blnctr_Acc
	 */
	public List<FcInitSubjectDTO> getInitSubjectDTO(FcMonthCloseDTO closeDTO, String curCode) {
		StringBuilder sql = new StringBuilder();
		/*sql.append(" Select Fc_Acc.Cur_id as curId, ");
		sql.append(" Fc_Acc.Subject_id as subjectId, ");
		sql.append(" Sum(Fc_Init_Acc.Year_Balance) As yearBalance, ");
		sql.append(" Sum(Fc_Init_Acc.Year_Total) As yearTotal ");
		sql.append(" From Fc_Init_Acc, "); // Fc_Init_Acc 在结算领域; 需要分组统计，所以 Fc_Init_Acc 冗余 字段：Cur_id、Subject_id、net_id、
		sql.append(" Fc_Acc, "); // fc_acc 在基础领域
		sql.append(" Bt_Currency, "); // ？多余关联？还是防脏数据？
		sql.append(" Fc_Subject "); // ？多余关联？还是防脏数据？
		sql.append(" Where Fc_Init_Acc.Account = Fc_Acc.Blnctr_Acc ");
		sql.append(" And Fc_Acc.Cur_id = Bt_Currency.id ");
		sql.append(" And Fc_Acc.Subject_id = Fc_Subject.id ");
		sql.append(" And Fc_Init_Acc.status = 95 ");
		sql.append(" And Fc_Acc.net_id = :netId  ");
		sql.append(" Group By Fc_Acc.Cur_id, Fc_Acc.Subject_id ");*/
		// Fc_Init_Acc 在结算领域; 需要分组统计，所以 Fc_Init_Acc 冗余 字段：Cur_id、Subject_id、net_id; 条件：Fc_Init_Acc.Account = Fc_Acc.Blnctr_Acc
		sql.append(" Select a.Cur_id as curId, ");
		sql.append(" a.Subject_id as subjectId, ");
		sql.append(" Sum(a.Year_Balance) As yearBalance, ");
		sql.append(" Sum(a.Year_Total) As yearTotal ");
		sql.append(" From Fc_Init_Acc a ");
		sql.append(" Where a.status = 95 ");
		sql.append(" And a.net_id = :netId  ");
		sql.append(" Group By a.Cur_id, a.Subject_id ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		return listBySQLAliasToBean(sql.toString(), params, FcInitSubjectDTO.class);
	}
	
	/**
	 * 功能介绍 : 有效币别集合。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 *
	 * 改为由基础模块提供
	 */
	/*public List<BtCurrencyDTO> queryValidCur() {
		StringBuilder sql = new StringBuilder();
		sql.append(" Select bc.id as id,bc.cur_code as curCode From Bt_Currency bc ")
				.append(" Where bc.Valid_Sign = '1' ")
				.append(" And exists (Select 1 From Fc_Acc fa where fa.cur_id = bc.id) ");
		return listBySQLAliasToBean(sql.toString(), null, BtCurrencyDTO.class);
	}*/
	
	/**
	 * 功能介绍 : 转移月总账数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void deleteMonthLedger(FcMonthCloseDTO closeDTO) {
		String sql = "  Delete From Fc_Month_Ledger  Where Year_Month2 = :yearMonth And Net_Code = :netId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		params.put("yearMonth", closeDTO.getYearMonth2());
		createSQLQuery(sql, params).executeUpdate();
	}
}
