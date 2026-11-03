package com.bytter.biz.finacial.outaccbook.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.closedeal.vo.OutAccBalanceBean;
import com.bytter.biz.finacial.closedeal.vo.OutAccbookBalanceVO;
import com.bytter.biz.finacial.outaccbook.entity.ClFcOutAccBalance;
import com.bytter.core.base.BaseDao;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
/**
* @Title: Dao
* @Description: FC_OUT_ACC_BALANCE
* @author onlineGenerator
* @date 2017-07-19 16:01:24
* @version 1.0
*
*/
@Repository
public class ClFcOutAccBalanceDao extends BaseDao<ClFcOutAccBalance,String>{
	
	/**
	 * 功能介绍 : 查询未复核的表外凭证。<br>
	 * <b>日期：</b>2017年7月19日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<OutAccbookBalanceVO> queryUnCheckOutAccbook(FcDayCloseDTO closeDTO ,List<String> accounts) {
		StringBuilder sql = new StringBuilder();
		/*sql.append("  SELECT d.ID AS dtlId,Fc_Out_Accbook.ORDER_NO AS ORDERNO,")
			.append("  d.DEBIT_MONEY AS DEBITMONEY,d.CREDIT_MONEY AS  CREDITMONEY, ")
			.append("  Fc_Out_Accbook.tally_date AS tallyDate, c.subject_id AS  subjectId, ")
			.append("  c.BLNCTR_ACC AS ACCOUNT, c.ACC_NAME AS ACCNAME, c.BALANCE_WAY AS BALANCEWAY, ")
			.append("  c.cur_id AS curId ")
			.append("  From Fc_Acc c, Fc_Out_Accbook, Fc_Out_Accbook_Dtl d ")
			.append("  Where c.Blnctr_Acc = d.Account ")
			.append("  And Fc_Out_Accbook.id =  d.Out_Accbook_Id ")
			.append("  And Fc_Out_Accbook.Status in (10,11,12,20,21,22,30,31,32)  ")
			.append("  And Fc_Out_Accbook.Over_Sign = '0' ")
			.append("  AND Fc_Out_Accbook.TALLY_DATE <= :closeDate ")
			.append("  AND Fc_Out_Accbook.TALLY_DATE > :startDate ")
			.append("  AND c.NET_ID = :netId ")
			.append("  ORDER BY Fc_Out_Accbook.VOUCHER_NO, d.LINE_NO");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		params.put("closeDate", closeDTO.getCloseDate());
		params.put("startDate", closeDTO.getLastCloseDate());*/
		sql.append("  SELECT d.ID AS dtlId,a.ORDER_NO AS ORDERNO,")
				.append("  d.DEBIT_MONEY AS DEBITMONEY,d.CREDIT_MONEY AS  CREDITMONEY, ")
				.append("  a.tally_date AS tallyDate, ")
				.append("  d.account AS ACCOUNT ")
				.append("  From Fc_Out_Accbook a, Fc_Out_Accbook_Dtl d ")
//				.append("  Where d.account in (:accounts)")
//				.append("  And a.id =  d.Out_Accbook_Id ")
				.append("  Where a.id = d.Out_Accbook_Id ")
				.append("  And a.Status in (10,11,12,20,21,22,30,31,32)  ")
				.append("  And a.Over_Sign = '0' ")
				.append("  AND a.TALLY_DATE <= :closeDate ")
				.append("  AND a.TALLY_DATE > :startDate ");
		if (!EmptyUtils.isEmpty(accounts)){
			sql.append(" and ").append(SqlUtil.getInSql(accounts, "d.account", true));
		}
		sql.append("  ORDER BY a.VOUCHER_NO, d.LINE_NO");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accounts",accounts);
		params.put("closeDate", closeDTO.getCloseDate());
		params.put("startDate", closeDTO.getLastCloseDate());
		
		return listBySQLAliasToBean(sql.toString(), params, OutAccbookBalanceVO.class);
		
		
		
	}
	/**
	 * 功能介绍 : 获取账务相关明细信息。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<OutAccbookBalanceVO> queryOutAccbookBalance(String netId, Date tallyDate, List<String> accounts) {
		StringBuilder sql = new StringBuilder();
		/*sql.append("  SELECT Fc_Out_Accbook_Dtl.ID AS dtlId,Fc_Out_Accbook.ORDER_NO AS ORDERNO,")
			.append("  Fc_Out_Accbook_Dtl.DEBIT_MONEY AS DEBITMONEY,Fc_Out_Accbook_Dtl.CREDIT_MONEY AS  CREDITMONEY, ")
			.append("  Fc_Out_Accbook.tally_date AS tallyDate, c.subject_id AS  subjectId, ")
			.append("  c.BLNCTR_ACC AS ACCOUNT, c.ACC_NAME AS ACCNAME, c.BALANCE_WAY AS BALANCEWAY, ")
			.append("  c.cur_id AS curId ")
			.append("  From Fc_Acc c, Fc_Out_Accbook, Fc_Out_Accbook_Dtl ")
			.append("  Where c.Blnctr_Acc = Fc_Out_Accbook_Dtl.Account ")
			.append("  And Fc_Out_Accbook.id =  Fc_Out_Accbook_Dtl.Out_Accbook_Id ")
			.append("  And Fc_Out_Accbook.Status <> '23'  ")
			.append("  And Fc_Out_Accbook.Over_Sign = '0'  ")
			.append("  AND Fc_Out_Accbook.TALLY_DATE <= :closeDate ")
			.append("  AND c.NET_ID = :netId ")
			.append("  ORDER BY Fc_Out_Accbook.VOUCHER_NO, Fc_Out_Accbook_Dtl.LINE_NO");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("closeDate", tallyDate);*/
		sql.append("  SELECT d.ID AS dtlId,a.ORDER_NO AS ORDERNO,")
				.append("  d.DEBIT_MONEY AS DEBITMONEY,d.CREDIT_MONEY AS  CREDITMONEY, ")
				.append("  a.tally_date AS tallyDate,  ")
				.append("  d.account AS ACCOUNT ")
				.append("  From Fc_Out_Accbook a, Fc_Out_Accbook_Dtl d")
//				.append("  Where d.account in (:accounts) ")
//				.append("  And a.id =  d.Out_Accbook_Id ")
				.append("  where a.id = d.Out_Accbook_Id ")
				.append("  And a.Status <> '23'  ")
				.append("  And a.Over_Sign = '0'  ")
				.append("  AND a.TALLY_DATE <= :closeDate ");
		if (!EmptyUtils.isEmpty(accounts)){
			sql.append(" and ").append(SqlUtil.getInSql(accounts, "d.account", true));
		}
		sql.append("  ORDER BY a.VOUCHER_NO, d.LINE_NO");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
//		params.put("accounts",accounts);
		params.put("closeDate", tallyDate);
		
		return listBySQLAliasToBean(sql.toString(), params, OutAccbookBalanceVO.class);
	}
	
	/**
	 * 功能介绍 : 根据余额日期获取网点的账号每日余额。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<OutAccBalanceBean> queryBalanceByNetDate(String netId, Date balanceDate, List<String> accounts) {
		StringBuilder hql = new StringBuilder();
		/*hql.append(" select fa.blnctr_acc as account,fa.cur_id as curId,fa.subject_id as subjectId,");
		hql.append(" fa.acc_name as accName,fw.tally_date as cancelDate,fa.acc_start_date as accStartDate,");
		hql.append(" fb.Opt_Date as opDate,y_Balance as yBalance, t_Balance as tBalance,  Debit_Num as debitNum, ");
		hql.append(" Credit_Num as creditNum, y_Num as yNum, t_Num as tNum,  Debit_Money as deditMoney, ");
		hql.append(" Credit_Money as creditMoney, Debit_Count as debitCount, Credit_Count as creditCount,");
		hql.append(" y_Count as yCount, t_Count as tCount,fa.balance_way as balanceWay ");
		hql.append(" from FC_ACC fa left join FC_out_ACC_BALANCE fb on fa.BLNCTR_ACC = fb.ACCOUNT");
		hql.append(" and fb.Opt_Date = :balanceDate ");
		hql.append(" left join FC_ACC_WRITEOFF fw on fa.BLNCTR_ACC = fw.WRITEOFF_ACC");
		hql.append(" where fa.NET_ID = :netId ");
		hql.append(" and fa.CHECKER_CODE is not null and fa.acc_type = :accType ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("balanceDate", balanceDate);
		params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);*/
		hql.append(" select fb.account as account, fw.tally_date as cancelDate, ");
		hql.append(" fb.Opt_Date as opDate,y_Balance as yBalance, t_Balance as tBalance,  Debit_Num as debitNum, ");
		hql.append(" Credit_Num as creditNum, y_Num as yNum, t_Num as tNum,  Debit_Money as deditMoney, ");
		hql.append(" Credit_Money as creditMoney, Debit_Count as debitCount, Credit_Count as creditCount,");
		hql.append(" y_Count as yCount, t_Count as tCount ");
		hql.append(" from FC_out_ACC_BALANCE fb ");
		hql.append(" left join FC_ACC_WRITEOFF fw on fb.ACCOUNT = fw.WRITEOFF_ACC");
//		hql.append(" where fb.account in (:accounts) ");
//		hql.append(" and fb.Opt_Date = :balanceDate ");
		hql.append(" where fb.Opt_Date = :balanceDate ");
		if (!EmptyUtils.isEmpty(accounts)){
			hql.append(" and ").append(SqlUtil.getInSql(accounts, "fb.account", true));
		}
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("accounts", accounts);
		params.put("balanceDate", balanceDate);
		return listBySQLAliasToBean(hql.toString(), params, OutAccBalanceBean.class);
	}
	
	/**
	 * 功能介绍 : 根据账户余额日期获取账号的当日余额。<br>
	 * <b>日期：</b>2017年7月10日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<ClFcOutAccBalance> queryAccBalanceByAccDate(String account, Date balanceDate) {
		StringBuilder hql = new StringBuilder();
//		hql.append(" select cf from com.bytter.biz.finacial.outaccbook.entity cf where cf.optDate = :balanceDate ");
		hql.append(" select * from FC_OUT_ACC_BALANCE cf where cf.opt_date = :balance_date ");
		hql.append(" and cf.account = :account ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("balance_date", balanceDate);
		return listBySQLAliasToBean(hql.toString(), params, ClFcOutAccBalance.class);
//		return listByHQL(hql.toString(), params);
	}
	
	/**
	 * 功能介绍 : 更新日结标识为已结标识。<br>
	 * <b>日期：</b>2017年7月19日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void updateOutAccbookOverSign(FcDayCloseDTO closeDTO) {
		StringBuilder hql = new StringBuilder();
		hql.append(" Update Fc_Out_Accbook a set a.Over_Sign = :newOverSign ");
		hql.append(" where a.tally_date = :closeDate and a.net_id = :netId and a.over_sign = :overSign ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		params.put("closeDate", closeDTO.getCloseDate());
		params.put("newOverSign", OuterFinacialConstant.OVER_SIGN_Y);
		params.put("overSign", OuterFinacialConstant.OVER_SIGN_N);
		createSQLQuery(hql.toString(), params).executeUpdate();
	}
	
	/**
	 * 功能介绍 : 还原日结标识为已结标识。<br>
	 * <b>日期：</b>2017年7月19日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void reverseOutAccbookOverSign(FcDayCloseDTO closeDTO) {
		StringBuilder hql = new StringBuilder();
		hql.append(" Update Fc_Out_Accbook a set a.Over_Sign = :newOverSign ");
		hql.append(" where a.tally_date <= :closeDate and a.tally_date > :startDate and a.net_id = :netId and a.over_sign = :overSign ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		params.put("closeDate", closeDTO.getCloseDate());
		params.put("startDate", closeDTO.getLastCloseDate());
		params.put("newOverSign", OuterFinacialConstant.OVER_SIGN_N);
		params.put("overSign", OuterFinacialConstant.OVER_SIGN_Y);
		createSQLQuery(hql.toString(), params).executeUpdate();
	}
}
