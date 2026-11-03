package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.closedeal.entity.ClFcAccBalance;
import com.bytter.biz.finacial.closedeal.entity.FcCashFlow;
import com.bytter.biz.finacial.closedeal.vo.AccBalanceBean;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
* @Title: Dao
* @Description: FC_ACC_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:04:02
* @version 1.0
*
*/
@Repository
public class ClFcAccBalanceDao extends BaseDao<ClFcAccBalance,String>{
	
	/**
	 * 功能介绍 : 根据账户余额日期获取账号的当日余额。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<ClFcAccBalance> queryAccBalanceByAccDate(String account, Date balanceDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select cf from ClFcAccBalance cf where cf.balanceDate = :balanceDate ");
		hql.append(" and cf.account = :account ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("balanceDate", balanceDate);
		return listByHQL(hql.toString(), params);
	}
	/**
	 * 功能介绍 : 根据余额日期获取网点的账号每日余额。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<AccBalanceBean> queryBalanceByNetDate(String netId, Date balanceDate, List<String> accounts) {
		StringBuilder hql = new StringBuilder();
		/*hql.append(" select fa.blnctr_acc as account,fa.cur_id as curId,fa.subject_id as subjectId,");
		hql.append(" fa.acc_name as accName,fb.balance_date as balanceDate,fb.tr_cnt as trCnt,");
		hql.append(" fb.rush_cnt as rushCnt,fb.cnt as cnt,fb.yester_balance as yesterBalance,fb.balance as balance,");
		hql.append(" fb.tr_debit_money as trDebitMoney,fb.tr_credit_money as tr_creditMoney,");
		hql.append(" fb.rush_debit_money as rushDebitMoney,fb.rush_credit_money as rushCreditMoney,");
		hql.append(" fb.debit_money as debitMoney,fb.credit_money as creditMoney,fb.debit_cnt as debitCnt,");
		hql.append(" fb.credit_cnt as creditCnt,fw.tally_date as cancelDate,fa.acc_start_date as accStartDate");
		hql.append(" from FC_ACC fa left join FC_ACC_BALANCE fb on fa.BLNCTR_ACC = fb.ACCOUNT");
		hql.append(" and fb.BALANCE_DATE = :balanceDate ");
		hql.append(" left join FC_ACC_WRITEOFF fw on fa.BLNCTR_ACC = fw.WRITEOFF_ACC");
		hql.append(" where fa.NET_ID = :netId"); // 提供根据 netId、checkerCode is not null
		hql.append(" and fa.CHECKER_CODE is not null ");*/
		hql.append(" select fb.ACCOUNT as account,");
		hql.append(" fb.balance_date as balanceDate,fb.tr_cnt as trCnt,");
		hql.append(" fb.rush_cnt as rushCnt,fb.cnt as cnt,fb.yester_balance as yesterBalance,fb.balance as balance,");
		hql.append(" fb.tr_debit_money as trDebitMoney,fb.tr_credit_money as tr_creditMoney,");
		hql.append(" fb.rush_debit_money as rushDebitMoney,fb.rush_credit_money as rushCreditMoney,");
		hql.append(" fb.debit_money as debitMoney,fb.credit_money as creditMoney,fb.debit_cnt as debitCnt,");
		hql.append(" fb.credit_cnt as creditCnt,fw.tally_date as cancelDate ");
		hql.append(" from FC_ACC_BALANCE fb ");
		hql.append(" left join FC_ACC_WRITEOFF fw on fb.ACCOUNT = fw.WRITEOFF_ACC ");
		hql.append(" where fb.BALANCE_DATE = :balanceDate ");
		if (!EmptyUtils.isEmpty(accounts)){
			hql.append(" and ").append(SqlUtil.getInSql(accounts, "fb.account", true));
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("balanceDate", balanceDate);
		return listBySQLAliasToBean(hql.toString(), params, AccBalanceBean.class);
	}
	
	/**
	 * 功能介绍 : 根据账号和余额日期获取账号每日余额。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<AccBalanceBean> queryBalanceByAccDate(String account, Date balanceDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select cf from ClFcAccBalance cf where cf.balanceDate = :balanceDate ");
		hql.append(" and cf.account = :account ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("balanceDate", balanceDate);
		
		return listByHQLAliasToBean(hql.toString(), params, AccBalanceBean.class);
	}
	/**
	 * 功能介绍 : 根据账号和余额日期区间获取账号每日余额。<br>
	 * <b>日期：</b>2019年5月15日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<AccBalanceBean> queryAccDaysBalance(String account, Date startDate, Date endDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select cf.balance_date as balanceDate, cf.account,cf.cur_id as curId, ")
            .append(" cf.acc_name as accName, cf.yester_balance as yesterBalance, cf.balance as balance ")
            .append(" from fc_acc_balance cf where cf.account = :account  ");
		if (!EmptyUtils.isEmpty(endDate)) {
			hql.append(" and cf.balance_Date <= :endDate ");
		}
		if (!EmptyUtils.isEmpty(startDate)) {
			hql.append(" and cf.balance_Date >= :startDate ");
		}
		hql.append(" order by cf.balance_Date ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return listBySQLAliasToBean(hql.toString(), params, AccBalanceBean.class);
	}
	
	/**
	 * 功能介绍 : 查询月结区间内的账户每日余额数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<AccBalanceBean> querySetMonthBalance(FcMonthCloseDTO closeDTO,List<String> accounts) {
		StringBuilder hql = new StringBuilder();
		/*hql.append(" select fa.blnctr_acc as account,fa.cur_id as curId,fa.subject_id as subjectId,");
		hql.append(" fa.acc_name as accName,fb.balance_date as balanceDate,fb.tr_cnt as trCnt,");
		hql.append(" fb.rush_cnt as rushCnt,fb.cnt as cnt,fb.yester_balance as yesterBalance,fb.balance as balance,");
		hql.append(" fb.tr_debit_money as trDebitMoney,fb.tr_credit_money as tr_creditMoney,");
		hql.append(" fb.rush_debit_money as rushDebitMoney,fb.rush_credit_money as rushCreditMoney,");
		hql.append(" fb.debit_money as debitMoney,fb.credit_money as creditMoney,fb.debit_cnt as debitCnt,");
		hql.append(" fb.credit_cnt as creditCnt,fw.tally_date as cancelDate,fa.acc_start_date as accStartDate,");
		hql.append(" fa.balance_way as balanceWay ");
		hql.append(" from FC_ACC fa left join FC_ACC_BALANCE fb on fa.BLNCTR_ACC = fb.ACCOUNT");
		hql.append(" left join FC_ACC_WRITEOFF fw on fa.BLNCTR_ACC = fw.WRITEOFF_ACC");
		hql.append(" where fa.NET_ID = :netId");
		hql.append(" and fa.CHECKER_CODE is not null ");
		hql.append(" and fb.BALANCE_DATE >= :startDate ");
		hql.append(" and fb.BALANCE_DATE <= :endDate ");
		hql.append(" order by fb.BALANCE_DATE ");*/
		hql.append(" select fb.ACCOUNT as account,");
		hql.append(" fb.balance_date as balanceDate,fb.tr_cnt as trCnt,");
		hql.append(" fb.rush_cnt as rushCnt,fb.cnt as cnt,fb.yester_balance as yesterBalance,fb.balance as balance,");
		hql.append(" fb.tr_debit_money as trDebitMoney,fb.tr_credit_money as tr_creditMoney,");
		hql.append(" fb.rush_debit_money as rushDebitMoney,fb.rush_credit_money as rushCreditMoney,");
		hql.append(" fb.debit_money as debitMoney,fb.credit_money as creditMoney,fb.debit_cnt as debitCnt,");
		hql.append(" fb.credit_cnt as creditCnt,fw.tally_date as cancelDate ");
		hql.append(" from FC_ACC_BALANCE fb ");
		hql.append(" left join FC_ACC_WRITEOFF fw on fb.ACCOUNT = fw.WRITEOFF_ACC");
		hql.append(" and fb.BALANCE_DATE >= :startDate ");
		hql.append(" and fb.BALANCE_DATE <= :endDate ");
		hql.append(" and fb.account in (:accounts)");
		hql.append(" order by fb.BALANCE_DATE ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accounts", accounts);
		params.put("startDate", closeDTO.getCloseDate());
		params.put("endDate", closeDTO.getEndDate());
		return listBySQLAliasToBean(hql.toString(), params, AccBalanceBean.class);
	}
    /**
     * 功能介绍 : 根据余额日期获取账号的当日余额。<br>
     * <b>日期：</b>2017年7月10日<br>
     * @version 1.0
     * @author liyh
     */
    public List<ClFcAccBalance> queryAccBalanceByBalDate(Date balanceDate) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select cf from ClFcAccBalance cf where cf.balanceDate = :balanceDate ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("balanceDate", balanceDate);
        return listByHQL(hql.toString(), params);
    }
	/**
	 * 查询当月所有日结数据
	 * @param params
	 * @return
	 */
    public List<FcCashFlow> listDataByMonth(Map<String,Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT fc_accbook_blned.tally_date    tallyDate, ");
		sql.append("        fc_accbook_blned.NET_ID        netCode, ");
		sql.append("        fc_accbook_blned_dtl.CUR_CODE      curCode, ");
		sql.append("        fc_accbook_blned_dtl.CASH_FLUX cashItem, ");
		sql.append("        SUM( ");
		sql.append("                CASE ");
		sql.append("                    WHEN fc_accbook_blned_dtl.debit_money <> 0 THEN ");
		sql.append("                        debit_money ");
		sql.append("                    ELSE ");
		sql.append("                        credit_money ");
		sql.append("                    END ");
		sql.append("            ) AS                       money ");
		sql.append(" from fc_accbook_blned ");
		sql.append("          left join fc_accbook_blned_dtl on fc_accbook_blned.id = fc_accbook_blned_dtl.book_id ");
		sql.append(" WHERE (fc_accbook_blned.voucher_type != :voucherType ");
		sql.append("     OR fc_accbook_blned.voucher_type IS NULL) ");
		sql.append("   AND (fc_accbook_blned.tally_date >= BT_TO_DATE(:startDate, 'yyyy-MM-dd') ");
		sql.append("            AND fc_accbook_blned.tally_date <= BT_TO_DATE(:endDate, 'yyyy-MM-dd') ");
		sql.append("     OR tally_date IS NULL) ");
		sql.append("   and fc_accbook_blned.NET_ID = :netId ");
		sql.append("   and fc_accbook_blned_dtl.CASH_FLUX is not null ");
		sql.append(" GROUP BY fc_accbook_blned.tally_date, ");
		sql.append("          fc_accbook_blned.NET_ID, ");
		sql.append("          fc_accbook_blned_dtl.CUR_CODE, ");
		sql.append("          fc_accbook_blned_dtl.CASH_FLUX ");
		return listBySQLAliasToBean(sql.toString(), params, FcCashFlow.class);
	}
	/**
	 * 判断月账单是否可以核对
	 *
	 * @param date 月账单
	 * @return true 存在，false 不存在
	 */
	public boolean existBalance(String date) {
		String sql = "SELECT id FROM fc_acc_balance WHERE balance_date = BT_TO_DATE(:date,'yyyy-MM-dd')";
		Map<String, Object> params = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date + "-01"));
		} catch (ParseException ignored) {
		}
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		params.put("date", sdf.format(calendar.getTime()));
		List<String> strings = listBySQLAliasToBean(sql, 0, 1, params, String.class);
		return !CollectionUtils.isEmpty(strings);
	}
}
