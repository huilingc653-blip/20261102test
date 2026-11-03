package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.closedeal.entity.ClFcAccuBalance;
import com.bytter.biz.finacial.closedeal.vo.AccAdjustDtlBean;
import com.bytter.biz.finacial.closedeal.vo.AccuBalanceDtlBean;
import com.bytter.biz.finacial.closedeal.vo.FcAccuBalanceBean;
import com.bytter.biz.finacial.closedeal.vo.FcDayCloseVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_ACCU_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:03:48
* @version 1.0
*
*/
@Repository
public class ClFcAccuBalanceDao extends BaseDao<ClFcAccuBalance,String>{
	
	/**
	 * 功能介绍 : 查询账户日结区间的每日余额。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcAccuBalanceBean> queryAccountAccuBalance(String account, Date balanceDate) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fa from ClFcAccuBalance fa where ")
			.append(" fa.balanceDate = :balanceDate  ")
			.append(" and fa.account = :account ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("balanceDate", balanceDate);
		List<ClFcAccuBalance> list = listByHQL(sql.toString(), params);
		return copyListAndInject(list, FcAccuBalanceBean.class);
	}
	/**
	 * 功能介绍 : 查询账户日结区间的每日积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcAccuBalanceBean> queryAccountAccuBalanceBySql(String account, Date balanceDate) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fa.id as id, fa.account as account,fa.balance_Date as balanceDate, ")
			.append(" fa.interest_Sign as interestSign,fa.pre_sign as preSign,fa.cumu as cumu, ")
			.append(" fa.a_adjusted as adjustedCumu,fa.adjust_Cumu as adjustCumu ")
			.append(" from fc_accu_balance fa where ")
			.append(" fa.balance_Date = :balanceDate  ")
			.append(" and fa.account = :account ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("balanceDate", balanceDate);
		return listBySQLAliasToBean(sql.toString(), params, FcAccuBalanceBean.class);
	}
	/**
	 * 功能介绍 : 查询账户日结区间的每日余额。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<AccuBalanceDtlBean> queryCumuDtl(FcDayCloseVO dayClose, List<String> accounts) {
		StringBuilder sql = new StringBuilder();
		/*sql.append(" Select a.Acc_Start_Date as accStartDate, a.cur_id as curId,a.acc_type as accType,")
			.append(" b.Balance_Date as balanceDate, b.Account as account, b.Balance as balance ")
			.append(" From Fc_Acc a Inner Join Fc_Acc_Balance b ")
			.append(" On a.Blnctr_Acc = b.Account ")
			.append(" Where a.Interest_Sign = '1' ")
			.append(" And a.Net_ID = :netId ")
			.append(" And a.Acc_Type Not In ('05', '06', '08' ,'15','16') ")
			.append(" And b.Balance_Date > :lastCloseDate ")
			.append(" And b.Balance_Date <= :closeDate ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", dayClose.getNetCode());
		params.put("closeDate", dayClose.getCloseDate());
		params.put("lastCloseDate", dayClose.getLastCloseDate());*/
		sql.append(" Select ")
				.append(" b.Balance_Date as balanceDate, b.Account as account, b.Balance as balance ")
				.append(" From Fc_Acc_Balance b ")
				.append(" Where 1=1 ")
				.append(" And b.Balance_Date > :lastCloseDate ")
				.append(" And b.Balance_Date <= :closeDate ");
		if (!EmptyUtils.isEmpty(accounts)) {
			sql.append(" and ").append(SqlUtil.getInSql(accounts, "b.account", true));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("closeDate", dayClose.getCloseDate());
		params.put("lastCloseDate", dayClose.getLastCloseDate());
		return listBySQLAliasToBean(sql.toString(), params, AccuBalanceDtlBean.class);
	}
	/**
	 * 功能介绍 : 查询账户日结区间的每日余额。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcAccuBalanceBean> queryNetAccuBalance(FcDayCloseVO dayClose, List<String> accountList) {
		StringBuilder sql = new StringBuilder();
//		sql.append(" select fa from ClFcAccuBalance fa where ")
//				.append(" fa.balanceDate = :closeDate  ")
//				.append(" and exists ( select 1 from FcAcc fc where fc.blnctrAcc = fa.account ")
//				.append(" and fc.netId = :netId ) ");
		sql.append(" select fa from ClFcAccuBalance fa where ")
				.append(" fa.balanceDate = :closeDate ");
		//TODO 上层已获取该网点下的账户
		if (!EmptyUtils.isEmpty(accountList)) {
			sql.append(" and ").append(SqlUtil.getInSql(accountList, "fa.account", true));
		}
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("netId", dayClose.getNetCode());
		params.put("closeDate", dayClose.getCloseDate());
//		params.put("lastCloseDate", dayClose.getLastCloseDate());
		List<ClFcAccuBalance> list = listByHQL(sql.toString(), params);
		return copyListAndInject(list, FcAccuBalanceBean.class);
	}
	/**
	 * 功能介绍 : 获取需要调整积数的明细账。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<AccAdjustDtlBean> queryAdjustBookDtl(String netId, Date tallyDate, List<String> accounts) {
		StringBuilder sql = new StringBuilder();
		/*sql.append("  SELECT FC_ACCBOOK.tally_date AS tallyDate,FC_ACCBOOK.Interest_Start_Date AS interestStartDate,")
			.append("  FC_ACCBOOK_DTL.account AS account,FC_ACC.BALANCE_WAY AS balanceWay,FC_ACC.cur_id as curId, ")
			.append("  sum(FC_ACCBOOK_DTL.DEBIT_MONEY - FC_ACCBOOK_DTL.CREDIT_MONEY ) * FC_ACC.BALANCE_WAY as money ")
			.append("  FROM  FC_ACCBOOK, FC_ACCBOOK_DTL, FC_ACC ")
			.append("  WHERE FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID ")
			.append("  AND FC_ACCBOOK_DTL.ACCOUNT = FC_ACC.BLNCTR_ACC ")
			.append("  And Fc_Acc.Interest_Sign = '1' ")
			.append("  And Fc_Acc.Acc_State <> 'D' ")
			.append("  And Fc_Accbook.Tally_Date <> Fc_Accbook.Interest_Start_Date ")
			.append("  AND FC_ACCBOOK.VOUCHER_STATE <> 'B' ")
			.append("  AND FC_ACCBOOK.TALLY_DATE <= :closeDate ")
			.append("  AND FC_ACC.NET_ID = :netId ")
		.append(" group by FC_ACCBOOK.tally_date, FC_ACCBOOK.Interest_Start_Date,FC_ACCBOOK_DTL.account,FC_ACC.BALANCE_WAY,FC_ACC.cur_id ")
		.append(" order by FC_ACCBOOK.tally_date, FC_ACCBOOK.Interest_Start_Date,FC_ACCBOOK_DTL.account");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("closeDate", tallyDate);*/
		// fc_accbook_dtl b 冗余字段：balance_way、cur_id，条件 b.account = fc_acc.blnctr_acc；查询条件拆分出去
		sql.append("  SELECT a.tally_date AS tallyDate,a.Interest_Start_Date AS interestStartDate,")
				.append("  b.account AS account,b.balance_way AS balanceWay,a.cur_code as curId, ")
				.append("  sum(b.DEBIT_MONEY - b.CREDIT_MONEY ) * b.balance_way as money ")
				.append("  FROM  FC_ACCBOOK a, FC_ACCBOOK_DTL b ")
				.append("  WHERE a.ID = b.BOOK_ID ")
				.append("  And a.Tally_Date <> a.Interest_Start_Date ")
				.append("  AND a.VOUCHER_STATE <> 'B' ")
				.append("  AND a.TALLY_DATE <= :closeDate ")
				.append("  AND b.account in (:accounts) ")
				.append(" group by a.tally_date, a.interest_start_date,b.account,b.balance_way,b.cur_id,a.cur_code  ")
				.append(" order by a.tally_date, a.interest_start_date,b.account");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accounts", accounts);
		params.put("closeDate", tallyDate);
		return listBySQLAliasToBean(sql.toString(), params, AccAdjustDtlBean.class);
	}
	
	/**
	 * 功能介绍 : 更新账户积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void updateAccuCumu(String id, BigDecimal cumu) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update fc_accu_balance set cumu = :cumu where id = :id ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("cumu", cumu);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * 功能介绍 : 更新账户当天累计调整积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void updateAccuAdjustedCumu(String id, BigDecimal adjustedCumu) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update fc_accu_balance set a_adjusted = :adjustedCumu where id = :id ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("adjustedCumu", adjustedCumu);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	/**
	 * 功能介绍 : 更新账户当天调整积数。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void updateAccuAdjustCumu(String id, BigDecimal adjustCumu) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update fc_accu_balance set adjust_cumu = :adjustCumu where id = :id ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("adjustCumu", adjustCumu);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	/**
	 * 功能介绍 : 查询账户当日同类型未计息积数 <br>
	 * <b>日期：</b>2019年5月15日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<ClFcAccuBalance> queryUnIstAccountAccu(ClFcAccuBalance accuBalance) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(" select fa from ClFcAccuBalance fa where ")
				.append(" fa.balanceDate = :balanceDate  ")
				.append(" and fa.account = :account ")
				.append(" and (fa.interestSign = :interestSign or fa.preSign = :preSign) ");
		if (!EmptyUtils.isEmpty(accuBalance.getAgreedType())) {
			sql.append(" and fa.agreedType = :agreeType ");
			params.put("agreeType", accuBalance.getAgreedType());
		}
		params.put("account", accuBalance.getAccount());
		params.put("balanceDate", accuBalance.getBalanceDate());
		params.put("interestSign", accuBalance.getInterestSign());
		params.put("preSign", accuBalance.getPreSign());
		return listByHQL(sql.toString(), params);
	}
	/**
	 * 功能介绍 : 查询账户积数 <br>
	 */
	public List<ClFcAccuBalance> queryAgreeAccu(String account,Date balanceDate,Integer agreedType) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append(" select fa from ClFcAccuBalance fa where ")
				.append(" fa.balanceDate = :balanceDate  ")
				.append(" and fa.account = :account ");
		if (!EmptyUtils.isEmpty(agreedType)) {
			sql.append(" and fa.agreedType = :agreeType ");
			params.put("agreeType", agreedType);
		}
		params.put("account", account);
		params.put("balanceDate", balanceDate);
		return listByHQL(sql.toString(), params);
	}
	/**
	 * 功能介绍 : 删除日初积数。<br>
	 */
	public void deleteArggerAccu(String id) {
		String sql = "delete from fc_accu_balance where id = :id ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id",id);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	/**
	* <br>
	* <b>功能：</b> 根据ID集合获取对应的账户积数 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2020/7/9 <br>
	*/
    public List<ClFcAccuBalance> getAccuList(List<String> idList) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select fa from ClFcAccuBalance fa ");
        sql.append(" where 1=1 and ");
        sql.append(getSQLIn(idList, 900, "fa.id"));
        return listByHQL(sql.toString());
    }
}
