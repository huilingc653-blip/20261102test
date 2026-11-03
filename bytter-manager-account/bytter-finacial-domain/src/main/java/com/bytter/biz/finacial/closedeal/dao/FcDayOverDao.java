package com.bytter.biz.finacial.closedeal.dao;

import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import com.bytter.biz.finacial.closedeal.vo.*;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.FcDayCloseDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日结处理dao
 * 因涉及到的业务太多，将核算类的表操作都放在该dao下面处理
 *
 * @author liyh
 */
@Repository
public class FcDayOverDao extends FcCloseBaseDao {
    /**
     * 功能介绍 : 查询流水号的借贷方金额是否平衡。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public List<Map<String, Object>> checkMoneyGroupByOrderNo(FcDayCloseVO dayClose, List<String> accounts) {
		/*sql.append("  SELECT a.ORDER_NO as orderNo ")
			.append(" FROM   FC_ACCBOOK a, FC_ACCBOOK_DTL b, FC_ACC c ")
			.append(" WHERE  a.ID = b.BOOK_ID ")
			.append(" AND c.BLNCTR_ACC = b.ACCOUNT ")
			.append(" AND c.net_id = :netId ")
			.append(" AND a.TALLY_DATE = :closeDate ")
			.append(" GROUP BY a.ORDER_NO ")
			.append(" HAVING SUM(DEBIT_MONEY) <> SUM(CREDIT_MONEY)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", dayClose.getNetCode());
		params.put("closeDate", dayClose.getCloseDate());*/
        String sql = "  SELECT a.ORDER_NO as orderNo " +
                " FROM   FC_ACCBOOK a, FC_ACCBOOK_DTL b " +
                " WHERE  a.ID = b.BOOK_ID " +
                " AND b.account in (:accounts) " +
                " AND a.TALLY_DATE = :closeDate " +
                " GROUP BY a.ORDER_NO " +
                " HAVING SUM(DEBIT_MONEY) <> SUM(CREDIT_MONEY)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accounts", accounts);
        params.put("closeDate", dayClose.getCloseDate());
        return listBySQL(sql, params);
    }

    /**
     * 功能介绍 : 检查各科目的复核联机余额借贷平衡。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccountBalanceVo> checkSubjectMoney(FcDayCloseVO dayClose, List<String> accounts) {
		/*sql.append("  SELECT c.BALANCE_WAY as balanceWay, ")
			.append(" SUM(b.CHECK_BALANCE) as balance ")
			.append(" FROM   FC_ACC c, FC_ONLINE_LEDGER b ")
			.append(" WHERE  c.acc_type <> :accType and c.BLNCTR_ACC = b.ACCOUNT ")
			.append(" AND c.NET_ID = :netId ")
			.append(" group by c.BALANCE_WAY ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", dayClose.getNetCode());
		params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);//表外账号*/
        // 拆分，分组求和在service处理； 根据条件 c.acc_type <> :accType and c.NET_ID = :netId,查询 fc_acc c ，返回 c.BLNCTR_ACC c.BALANCE_WAY
        String sql = "  SELECT b.account as account," +
                " b.CHECK_BALANCE as balance " +
                " FROM FC_ONLINE_LEDGER b " +
                " WHERE b.account in (:accounts) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accounts", accounts);
        return listBySQLAliasToBean(sql, params, FcAccountBalanceVo.class);
    }

    /**
     * 功能介绍 : 检查各科目每日批量余额借贷是否平衡。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccountBalanceVo> checkAccBalanceMoney(FcDayCloseVO dayClose, List<String> accounts) {
		/*sql.append("  SELECT c.BALANCE_WAY as balanceWay, ")
			.append(" SUM(a.BALANCE) as balance ")
			.append(" FROM   FC_ACC c, FC_ACC_BALANCE a ")
			.append(" WHERE c.acc_type <> :accType and c.BLNCTR_ACC = a.ACCOUNT ")
			.append(" AND c.NET_ID = :netId ")
			.append(" AND a.BALANCE_DATE = :closeDate ")
			.append(" group by c.BALANCE_WAY ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);//表外账号
		params.put("netId", dayClose.getNetCode());
		params.put("closeDate", dayClose.getCloseDate());*/
        String sql = " SELECT a.account as account, " +
                " a.balance as balance " +
                " FROM FC_ACC_BALANCE a " +
                " WHERE a.account in (:accounts)" +
                " AND a.BALANCE_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accounts", accounts);
        params.put("closeDate", dayClose.getCloseDate());
        return listBySQLAliasToBean(sql, params, FcAccountBalanceVo.class);
    }

    /**
     * 功能介绍 : 检查账户的联机笔数与批量笔数是否平衡(实时余额的笔数=日结日的笔数+账务笔数)。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<String> checkAccountCnt(FcDayCloseVO dayClose, boolean checkSign, List<String> blnctrAccs) {
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT A.ACCOUNT ");
        sql.append("  FROM  FC_ONLINE_LEDGER A INNER JOIN FC_ACC_BALANCE B ON A.ACCOUNT = B.ACCOUNT ");
//        sql.append("  INNER JOIN FC_ACC C ON A.ACCOUNT = C.BLNCTR_ACC ");
//        sql.append("  WHERE c.acc_type <> :accType and B.BALANCE_DATE = :closeDate and c.NET_ID = :netId ");
        // 拆 fc_acc , 通过参数 传入 blnctrAccs
        //        sql.append("  WHERE A.ACCOUNT in (:blnctrAccs) and B.BALANCE_DATE = :closeDate ");
        sql.append("  WHERE B.BALANCE_DATE = :closeDate ");
        if (!EmptyUtils.isEmpty(blnctrAccs)) {
            sql.append(" and ").append(SqlUtil.getInSql(blnctrAccs, "A.ACCOUNT", true));
        }
        if (checkSign) {//复核
            sql.append("  AND ( A.CHECK_CNT - (SELECT  COUNT(1) ");
        } else {//记账
            sql.append("  AND ( A.TALLY_CNT - (SELECT  COUNT(1) ");
        }
        sql.append("  FROM   FC_ACCBOOK, FC_ACCBOOK_DTL ");
        sql.append("  WHERE  FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID ");
        sql.append("  AND FC_ACCBOOK_DTL.ACCOUNT = A.ACCOUNT ");
        sql.append("  AND FC_ACCBOOK.TALLY_DATE > :closeDate ");
        sql.append("  AND FC_ACCBOOK.NET_ID = :netId ");
        sql.append("  AND FC_ACCBOOK.CHECKER_CODE IS NOT NULL ");
        if (checkSign) {//复核
            sql.append("  AND FC_ACCBOOK.VOUCHER_STATE in ('M','D','W','N') ) <> B.CNT) ");
        } else {//记账
            sql.append("  AND FC_ACCBOOK.VOUCHER_STATE in ('F','T','C','N') ) <> B.CNT)");
        }
        Map<String, Object> params = new HashMap<String, Object>();
//		params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);//表外账号
//		params.put("netId", dayClose.getNetCode());
//        params.put("blnctrAccs", blnctrAccs);
        params.put("closeDate", dayClose.getCloseDate());
        params.put("netId", dayClose.getNetCode());
        List<String> accounts = createSQLQuery(sql.toString()).setProperties(params).list();
        return accounts;
        //return listBySQL(sql.toString(), params);
    }

    /**
     * 功能介绍 : 更新账户的联机复核笔数与批量笔数不平衡(实时余额的笔数=日结日的笔数+账务笔数)。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public int updateAccountTallyCnt(FcDayCloseVO dayClose, List<String> accounts, boolean checkSign) {
        StringBuilder sql = new StringBuilder();
        sql.append("  UPDATE fc_online_ledger fo SET ");
        if (checkSign) {//复核
            sql.append("  fo.check_cnt ");
        } else {//记账
            sql.append("  fo.tally_cnt ");
        }
        sql.append("  = ( ");
        sql.append("  SELECT  ");
        sql.append("  fl.cnt +  fb.cnt ");
        sql.append("  FROM ( ");
//        sql.append("  SELECT D.BLNCTR_ACC as account, bt_nvl_num(c.cnt,0) as cnt ");
        sql.append("  SELECT C.ACCOUNT as account, bt_nvl_num(c.cnt,0) as cnt from ");
//        sql.append("     FROM FC_ACC D "); // 暂不处理
//        sql.append("     left join ");
        sql.append("   (SELECT B.ACCOUNT,COUNT(1) cnt ");
        sql.append("    FROM FC_ACCBOOK_DTL B,fc_accbook a WHERE a.id = b.book_id ");
        if (checkSign) {//复核
            sql.append("   AND a.voucher_state in ('M','D','W','N') ");
        } else {//记账
            sql.append("   AND a.voucher_state in ('F','T','C','N') ");
        }
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "B.Account", true));
//        sql.append("      GROUP BY B.ACCOUNT) C on C.ACCOUNT = D.BLNCTR_ACC  ");
        sql.append("      GROUP BY B.ACCOUNT) C  ");
        sql.append("    where 1 = 1   ");
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "C.ACCOUNT", true));
        sql.append("    ) fl, ");
        sql.append("  (SELECT ab.account,ab.cnt FROM fc_acc_balance ab WHERE ab.balance_date = :balanceDate  ");
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "ab.account", true));
        sql.append(" ) fb WHERE fl.account = fo.account and fb.account = fo.account ");
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "fo.account", true));
//        sql.append("  limit 1 )  "); //oracle 不支持 limit，具体得问黄云龙，看看为什么加 limit 1
        sql.append("   )  ");
        sql.append("  WHERE 1 = 1 ");
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "fo.account", true));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("balanceDate", dayClose.getLastCloseDate());
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 功能介绍 : 更新账户的联机复核余额与批量余额不平衡(实时余额的余额=日结日的余额+账务发生额)。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public int updateAccountTallyBalance(FcDayCloseVO dayClose, List<String> accounts, boolean checkSign) {
        StringBuilder sql = new StringBuilder();
        sql.append("  UPDATE fc_online_ledger fo SET ");
        if (checkSign) {//复核
            sql.append("  fo.check_balance ");
        } else {//记账
            sql.append("  fo.online_balance ");
        }
        sql.append("  = ( ");
        sql.append("  SELECT  ");
        sql.append("  fl.balance +  fb.balance ");
        sql.append("  FROM ( ");
        sql.append("  select d.account ,sum(d.balance) balance from ( ");
        sql.append("  SELECT C.ACCOUNT as account, bt_nvl_num(C.BALANCE_WAY * nvl(C.balance,0),0) AS balance  ");
//        sql.append("     FROM FC_ACC D "); // 暂不处理
//        sql.append("     left join ");
        sql.append(" FROM (SELECT B.ACCOUNT,SUM(B.DEBIT_MONEY - B.CREDIT_MONEY) AS balance,B.BALANCE_WAY ");
        sql.append("    FROM FC_ACCBOOK_DTL B,fc_accbook a WHERE a.id = b.book_id ");
        if (checkSign) {//复核
            sql.append("   AND a.voucher_state in ('M','D','W','N') ");
        } else {//记账
            sql.append("   AND a.voucher_state in ('F','T','C','N') ");
        }
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "B.Account", true));
//        sql.append("      GROUP BY B.ACCOUNT) C on C.ACCOUNT = D.BLNCTR_ACC  ");
        sql.append("      GROUP BY B.ACCOUNT ,B.BALANCE_WAY) C  "); // 兼容Oracle 增加了 B.BALANCE_WAY
        sql.append("    where 1 = 1   ");
        //TODO 不需要再次判断账号
//        sql.append(" and ").append(SqlUtil.getInSql(accounts, "D.BLNCTR_ACC", true));
        sql.append("    ) d group by d.account ");  // 兼容Oracle 增加了 B.BALANCE_WAY，但是同时因为增加了 分组条件导致有多条数据，所以中间再增加一个sum函数
        sql.append("    ) fl, ");
        sql.append("  (SELECT ab.account,ab.balance FROM fc_acc_balance ab WHERE ab.balance_date = :balanceDate  ");
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "ab.account", true));
        sql.append(" ) fb WHERE fl.account = fo.account and fb.account = fo.account ");
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "fo.account", true));
//        sql.append("  limit 1)  ");
        sql.append("  )  ");
        sql.append("  WHERE 1 = 1 ");
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "fo.account", true));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("balanceDate", dayClose.getLastCloseDate());
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 功能介绍 : 检查账户的联机复核余额与批量余额是否相等(实时余额=日结日的余额+账务金额)。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @param dayClose  日结日期
     * @param checkSign 复核记账标志
     * @param accList   内部账号集合
     * @return liyh
     * @version 1.0
     * @author liyh
     */
    public List<String> checkAccountBalance(FcDayCloseVO dayClose, boolean checkSign, List<String> accList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT A.ACCOUNT ");
        sql.append(" FROM FC_ONLINE_LEDGER A INNER JOIN FC_ACC_BALANCE B ON A.ACCOUNT = B.ACCOUNT ");
        sql.append(" WHERE B.BALANCE_DATE = :closeDate ");
        if (checkSign) {//复核
            sql.append("  AND (A.CHECK_BALANCE - (SELECT   SUM( (FC_ACCBOOK_DTL.DEBIT_MONEY ");
        } else {//记账
            sql.append("  AND (A.ONLINE_BALANCE - (SELECT  SUM( (FC_ACCBOOK_DTL.DEBIT_MONEY ");
        }
        sql.append("  - FC_ACCBOOK_DTL.CREDIT_MONEY) * FC_ACCBOOK_DTL.BALANCE_WAY) ");
        sql.append(" FROM  FC_ACCBOOK, FC_ACCBOOK_DTL "); // 暂不处理
//        sql.append(" WHERE c.acc_type <> :accType and FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID  ");
        sql.append(" WHERE FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID  ");
        sql.append(" and ").append(SqlUtil.getInSql(accList, "FC_ACCBOOK_DTL.ACCOUNT", true));
//        sql.append(" AND c.BLNCTR_ACC = FC_ACCBOOK_DTL.ACCOUNT ");
        sql.append(" AND FC_ACCBOOK_DTL.ACCOUNT = A.ACCOUNT ");
        sql.append(" AND FC_ACCBOOK.TALLY_DATE > :closeDate ");
        sql.append(" AND FC_ACCBOOK.NET_ID = :netId ");
        sql.append(" AND FC_ACCBOOK.CHECKER_CODE IS NOT NULL ");
        if (checkSign) {//复核
            sql.append("  AND FC_ACCBOOK.VOUCHER_STATE in ('M','D','W','N') ) <> B.BALANCE) ");
        } else {//记账
            sql.append("  AND FC_ACCBOOK.VOUCHER_STATE in ('F','T','C','N') ) <> B.BALANCE) ");
        }
        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);//表外账号
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        List<String> accounts = createSQLQuery(sql.toString()).setProperties(params).list();
        return accounts;
        //return listBySQL(sql.toString(), params);
    }

    /**
     * 功能介绍 : 生成银行对账单数据。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcCompareAccBean> generateCompareAcc(FcDayCloseVO dayClose, List<String> accounts) {
		/*sql.append(" SELECT a.ORDER_NO as orderNo, ")
	        .append(" a.VOUCHER_NO as voucherNo, ")
	        .append(" a.TALLY_DATE as tallyDate, ")
	        .append(" a.INTEREST_START_DATE as interestStartDate, ")
	        .append(" a.OPERATOR_CODE as createBy, ")
	        .append(" a.ORIG_VOUCHER_NO as billNo, ")
	        .append(" b.LINE_NO as lineNo, ")
	        .append(" b.SUMMARY_CONTENT as summaryContent, ")
	        .append(" b.ACCOUNT as compAcc, ")
	        .append(" b.DEBIT_MONEY as debitMoney, ")
	        .append(" b.CREDIT_MONEY as creditMoney, ")
	        .append(" b.BALANCE as balance, ")
	        .append(" a.REDBLUE_SIGN as redblueSign,")
	        .append(" c.cur_id as curId ")
	        .append(" FROM   FC_ACCBOOK a, FC_ACCBOOK_DTL b, FC_ACC c ")
	        .append(" WHERE  a.ID = b.BOOK_ID ")
	        .append(" AND c.BLNCTR_ACC = b.ACCOUNT ")
	        .append(" AND c.NET_ID = :netId ")
	        .append(" AND a.TALLY_DATE = :closeDate ")
		    .append(" and not exists(select 1 from FC_COMPARE_ACC comp where comp.order_no = a.order_no and comp.line_no = b.LINE_NO)")
	        .append(" AND c.IF_COMPARE = '1' ")
	        .append(" AND a.VOUCHER_STATE <> 'B' ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", dayClose.getNetCode());
		params.put("closeDate", dayClose.getCloseDate());*/
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.ORDER_NO as orderNo, ")
                .append(" a.VOUCHER_NO as voucherNo, ")
                .append(" a.TALLY_DATE as tallyDate, ")
                .append(" a.INTEREST_START_DATE as interestStartDate, ")
                .append(" a.OPERATOR_CODE as createBy, ")
                .append(" a.ORIG_VOUCHER_NO as billNo, ")
                .append(" b.LINE_NO as lineNo, ")
                .append(" b.SUMMARY_CONTENT as summaryContent, ")
                .append(" b.ACCOUNT as compAcc, ")
                .append(" b.DEBIT_MONEY as debitMoney, ")
                .append(" b.CREDIT_MONEY as creditMoney, ")
                .append(" b.BALANCE as balance, ")
                .append(" a.REDBLUE_SIGN as redblueSign,")
                .append(" b.CUR_CODE as curId ")
                .append(" FROM FC_ACCBOOK a, FC_ACCBOOK_DTL b ")
                .append(" WHERE  a.ID = b.BOOK_ID ");
        //TODO 调整
        if (!EmptyUtils.isEmpty(accounts)) {
            sql.append(" and ").append(SqlUtil.getInSql(accounts, "b.ACCOUNT", true));
        }
        sql.append(" AND a.TALLY_DATE = :closeDate ")
                .append(" AND a.VOUCHER_STATE <> 'B' ");
        //TODO 不存在企业对账数据   程序调整
//                .append(" and not exists(select 1 from FC_COMPARE_ACC comp where comp.order_no = a.order_no and comp.line_no = b.LINE_NO)")

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("closeDate", dayClose.getCloseDate());
        return listBySQLAliasToBean(sql.toString(), params, FcCompareAccBean.class);
    }
    /**
     * 功能介绍 : 将作废的账务的对账单清理<br>
     * <b>日期：</b>2022年4月14日<br>
     *
     * @version 1.0
     * @author chenf
     */
    /*public void cancelCompareAcc(FcDayCloseVO dayClose) {
        // FC_COMPARE_ACC 在 资金核对
        // FC_ACCBOOK 在核算，根据条件查询，拿到 FC_ACCBOOK.VOUCHER_NO;
        String sql = " DELETE FROM FC_COMPARE_ACC" +
                " WHERE EXISTS (SELECT 1" +
                " FROM FC_ACCBOOK " +
                " WHERE FC_COMPARE_ACC.VOUCHER_NO = FC_ACCBOOK.VOUCHER_NO" +
                " AND FC_ACCBOOK.VOUCHER_STATE = 'B'" +
                " AND FC_ACCBOOK.NET_ID = :netId )" +
                " AND FC_COMPARE_ACC.TALLY_DATE =:closeDate";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }*/

    /**
     * 功能介绍 : 按指定日期更新对账单余额<br>
     * <b>日期：</b>2022年4月14日<br>
     *
     * @version 1.0
     * @author chenf
     */
    public void updateCompareAccBalance(FcDayCloseVO dayClose) {
        /*String sql = " UPDATE FC_COMPARE_ACC " +
                " SET FC_COMPARE_ACC.BALANCE = " +
                " (SELECT FC_ACCBOOK_DTL.BALANCE " +
                " FROM FC_ACCBOOK_DTL " +
                " LEFT JOIN FC_ACCBOOK " +
                " ON FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID " +
                " WHERE FC_COMPARE_ACC.VOUCHER_NO = FC_ACCBOOK.VOUCHER_NO " +
                " AND FC_COMPARE_ACC.TALLY_DATE =:closeDate " +
                " AND FC_ACCBOOK.NET_ID = :netId " +
                " AND FC_COMPARE_ACC.COMP_ACC = FC_ACCBOOK_DTL.ACCOUNT " +
                " AND FC_COMPARE_ACC.LINE_NO = FC_ACCBOOK_DTL.LINE_NO  " +
                " AND FC_COMPARE_ACC.BALANCE <> FC_ACCBOOK_DTL.BALANCE )" +
                " WHERE EXISTS (SELECT 1 " +
                " FROM FC_ACCBOOK_DTL " +
                " LEFT JOIN FC_ACCBOOK " +
                " ON FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID " +
                " WHERE FC_COMPARE_ACC.VOUCHER_NO = FC_ACCBOOK.VOUCHER_NO " +
                " AND FC_COMPARE_ACC.TALLY_DATE = :closeDate " +
                " AND FC_ACCBOOK.NET_ID = :netId " +
                " AND FC_COMPARE_ACC.COMP_ACC = FC_ACCBOOK_DTL.ACCOUNT " +
                " AND FC_COMPARE_ACC.LINE_NO = FC_ACCBOOK_DTL.LINE_NO " +
                " AND FC_COMPARE_ACC.BALANCE <> FC_ACCBOOK_DTL.BALANCE )";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());*/

        // update FC_COMPARE_ACC set BALANCE = :balance where id = :id

    }

    /**
     * 功能介绍 : 当天流水账明细转已结流水账表<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void closeAccbookDtl(FcDayCloseVO dayClose) {
        String sql = " INSERT INTO FC_ACCBOOK_BLNED_DTL " +
                "  SELECT FC_ACCBOOK_DTL.* " +
                "  FROM   FC_ACCBOOK, FC_ACCBOOK_DTL " +
                "  WHERE  FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID " +
                "  AND    FC_ACCBOOK.NET_ID = :netId " +
                "  AND    FC_ACCBOOK.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 当天流水账附加信息转已结流水账表<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void closeAccbookExt(FcDayCloseVO dayClose) {
        String sql = " INSERT INTO FC_ACCBOOK_BLNED_EXT " +
                "  SELECT FC_ACCBOOK_EXT.* " +
                "  FROM   FC_ACCBOOK,FC_ACCBOOK_EXT " +
                "  WHERE  FC_ACCBOOK.ID = FC_ACCBOOK_EXT.BOOK_ID " +
                "  AND    FC_ACCBOOK.NET_ID = :netId " +
                "  AND    FC_ACCBOOK.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 当天流水账主表转已结流水账表<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void closeAccbook(FcDayCloseVO dayClose) {
        String sql = " INSERT INTO FC_ACCBOOK_BLNED " +
                "  SELECT FC_ACCBOOK.* " +
                "  FROM   FC_ACCBOOK " +
                "  WHERE    FC_ACCBOOK.NET_ID = :netId " +
                "  AND    FC_ACCBOOK.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除当天流水账明细转已结流水账表<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccbookDtl(FcDayCloseVO dayClose) {
        String sql = "   DELETE FROM FC_ACCBOOK_DTL " +
                "  WHERE  EXISTS (SELECT 1 FROM FC_ACCBOOK " +
                "  WHERE  FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID" +
                "  AND    FC_ACCBOOK.NET_ID = :netId " +
                "  AND    FC_ACCBOOK.TALLY_DATE = :closeDate) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除当天流水账附加信息转已结流水账表<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccbookExt(FcDayCloseVO dayClose) {
        String sql = "   DELETE FROM FC_ACCBOOK_EXT " +
                "  WHERE  EXISTS (SELECT 1 FROM FC_ACCBOOK " +
                "  WHERE  FC_ACCBOOK.ID = FC_ACCBOOK_EXT.BOOK_ID" +
                "  AND    FC_ACCBOOK.NET_ID = :netId " +
                "  AND    FC_ACCBOOK.TALLY_DATE = :closeDate) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除当天流水账主表转已结流水账表<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccbook(FcDayCloseVO dayClose) {
        String sql = "  DELETE FROM FC_ACCBOOK " +
                " WHERE  FC_ACCBOOK.NET_ID = :netId " +
                " AND    FC_ACCBOOK.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 获取账务相关明细信息。<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<AccbookBalanceVO> queryAccbookBalance(String netId, Date tallyDate, List<String> accList) {
		/*sql.append("  SELECT b.ID AS dtlId,a.ORDER_NO AS ORDERNO,a.VOUCHER_NO AS VOUCHERNO,")
			.append("  b.DEBIT_MONEY AS DEBITMONEY,b.CREDIT_MONEY AS  CREDITMONEY, ")
			.append("  a.tally_date AS tallyDate,c.subject_id AS  subjectId, ")
			.append("  c.BLNCTR_ACC AS ACCOUNT,c.ACC_NAME AS ACCNAME,c.BALANCE_WAY AS BALANCEWAY, ")
			.append("  c.cur_id AS curId,a.redblue_Sign AS redblueSign,b.BALANCE AS BALANCE, ")
			.append("  fc_voucher_type.SYS_VOUCHER_CODE AS sysVoucherType,fc_voucher_type.cash_Trans_Sign AS cashTransSign")
			.append("  FROM  FC_ACCBOOK a inner join FC_ACCBOOK_DTL b on a.ID = b.BOOK_ID ")
			.append("  inner join FC_ACC c on b.ACCOUNT = c.BLNCTR_ACC ")
			.append("  left join fc_voucher_type on a.voucher_type = fc_voucher_type.id ")
			.append("  WHERE a.TALLY_DATE <= :closeDate ")
			.append("  AND c.NET_ID = :netId ")
			.append("  AND a.VOUCHER_STATE <> 'B' ")
			.append("  ORDER BY a.VOUCHER_NO, b.LINE_NO");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("closeDate", tallyDate);*/
        // 在service 填充 subjectId account accName balanceway curId
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.ID AS dtlId,b.ACCOUNT as account, a.ORDER_NO AS ORDERNO,a.VOUCHER_NO AS VOUCHERNO, ")
                //TODO 临时补充该数据，程序中在重新赋值
                .append(" a.voucher_type AS sysVoucherType, ")
                .append(" b.DEBIT_MONEY AS DEBITMONEY,b.CREDIT_MONEY AS  CREDITMONEY, ")
                .append(" a.tally_date AS tallyDate, a.redblue_Sign AS redblueSign,b.BALANCE AS BALANCE ")
                .append(" ,b.ACC_NAME AS ACCNAME,b.cur_code AS curId,b.subject_id AS subjectId,b.BALANCE_WAY AS BALANCEWAY ")
//                .append(" fc_voucher_type.SYS_VOUCHER_CODE AS sysVoucherType,fc_voucher_type.cash_Trans_Sign AS cashTransSign ")
                .append(" FROM FC_ACCBOOK a inner join FC_ACCBOOK_DTL b on a.ID = b.BOOK_ID ")
//                .append(" left join fc_voucher_type on a.voucher_type = fc_voucher_type.id ")
                .append(" WHERE a.TALLY_DATE <= :closeDate AND a.NET_ID = :netId  AND a.VOUCHER_STATE <> 'B' ");
        if (!EmptyUtils.isEmpty(accList)) {
            sql.append(" and ").append(SqlUtil.getInSql(accList, "b.account", true));
        }
        sql.append(" ORDER BY a.VOUCHER_NO, b.LINE_NO");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", netId);
        params.put("closeDate", tallyDate);

        return listBySQLAliasToBean(sql.toString(), params, AccbookBalanceVO.class);
    }

    /**
     * 功能介绍 : 更新账务明细上的余额。<br>
     * <b>日期：</b>2017年7月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public int updateFcAccbookDtlBalance(String dtlId, BigDecimal balance, BigDecimal origBalance) {
        String sql = " UPDATE FC_ACCBOOK_DTL SET BALANCE = :balance WHERE id= :id and BALANCE=:origBalance";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("balance", balance);
        params.put("id", dtlId);
        params.put("origBalance", origBalance);
        return createSQLQuery(sql, params).executeUpdate();
    }

    private Map<String, Object> getCloseParams(FcDayCloseDTO dayClose) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        Date closeDate = dayClose.getCloseDate();
        String string = closeDate.toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(string);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        Date lastCloseDate = dayClose.getLastCloseDate();
        params.put("endDate", parse);
        params.put("startDate", lastCloseDate);
        return params;
    }

    /**
     * 功能介绍 : 删除当天账户批量余额。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccBalance(FcDayCloseDTO dayClose, List<String> accounts) {
		/*sql.append("delete from fc_acc_balance where balance_date > :startDate ")
			.append(" and balance_date <= :endDate and exists (select 1 from fc_acc c ")
			.append(" where c.blnctr_acc = fc_acc_balance.account and c.net_id = :netId  ")
			.append(" and c.acc_start_date <= fc_acc_balance.balance_date ) ");*/
        String sql = "delete from fc_acc_balance where balance_date > :startDate " +
                " and balance_date <= :endDate and account in (:accounts) ";
        Map<String, Object> params = getCloseParams(dayClose);
        params.put("accounts", accounts);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除当天账户批量余额。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteOutAccBalance(FcDayCloseDTO dayClose, List<String> accounts) {
		/*sql.append("delete from Fc_Out_Acc_Balance where opt_date > :startDate ")
		.append(" and opt_date <= :endDate and exists (select 1 from fc_acc c ")
		.append(" where c.blnctr_acc = Fc_Out_Acc_Balance.account and c.net_id = :netId  ")
		.append(" and c.acc_start_date <= Fc_Out_Acc_Balance.opt_date ) ");*/
        String sql = "delete from Fc_Out_Acc_Balance where opt_date > :startDate " +
                " and opt_date <= :endDate and account in (:accounts) ";
        Map<String, Object> params = getCloseParams(dayClose);
        params.put("accounts", accounts);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除未计息的积数。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteNoIstAccuBalance(FcDayCloseDTO dayClose, List<String> accounts) {
		/*sql.append("delete from fc_accu_balance where balance_date > :startDate ")
			.append(" and balance_date <= :endDate and exists (select 1 from fc_acc c ")
			.append(" where c.blnctr_acc = fc_accu_balance.account and c.net_id = :netId  ")
			.append(" and c.acc_start_date <= fc_accu_balance.balance_date ) ")
			.append(" and fc_accu_balance.interest_sign = '0' and (fc_accu_balance.pre_sign = '0' OR fc_accu_balance.pre_sign IS NULL) ");*/
        String sql = "delete from fc_accu_balance where balance_date > :startDate " +
                " and balance_date <= :endDate and account in (:accounts) " +
                " and interest_sign = '0' and (pre_sign = '0' OR pre_sign IS NULL) ";
        Map<String, Object> params = getCloseParams(dayClose);
        params.put("accounts", accounts);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除中心对账数据。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteCompareAcc(FcDayCloseDTO dayClose, List<String> accounts) {
		/*sql.append(" DELETE FROM fc_compare_acc WHERE tally_date = :endDate ")
			.append(" and EXISTS (SELECT 1 FROM FC_ACC ACC WHERE ")
			.append(" ACC.BLNCTR_ACC = fc_compare_acc.comp_acc AND ACC.net_id = :netId) ");*/
        String sql = " DELETE FROM fc_compare_acc WHERE tally_date = :endDate " +
                " and fc_compare_acc.comp_acc in (:accounts) ";
        Map<String, Object> params = getCloseParams(dayClose);
        params.put("accounts", accounts);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 还原单位利息明细。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteCorpInterest(FcDayCloseDTO dayClose) {
        String sql = "  Delete From Fc_Corp_Interest_Dtl " +
                " Where Interest_Date > :startDate " +
                " and Interest_Date <= :endDate " +
                " and Net_ID = :netId ";
        Map<String, Object> params = getCloseParams(dayClose);
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 已结流水账明细转当天流水账表<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseAccbookDtl(FcDayCloseDTO dayClose) {
        String sql = " INSERT INTO FC_ACCBOOK_DTL " +
                "  SELECT FC_ACCBOOK_BLNED_DTL.* " +
                "  FROM   FC_ACCBOOK_BLNED, FC_ACCBOOK_BLNED_DTL " +
                "  WHERE  FC_ACCBOOK_BLNED.ID = FC_ACCBOOK_BLNED_DTL.BOOK_ID " +
                "  AND    FC_ACCBOOK_BLNED.NET_ID = :netId " +
                "  AND    FC_ACCBOOK_BLNED.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 已结流水账附加信息转已结当天流水账表<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseAccbookExt(FcDayCloseDTO dayClose) {
        String sql = " INSERT INTO FC_ACCBOOK_EXT " +
                "  SELECT FC_ACCBOOK_BLNED_EXT.* " +
                "  FROM   FC_ACCBOOK_BLNED,FC_ACCBOOK_BLNED_EXT " +
                "  WHERE  FC_ACCBOOK_BLNED.ID = FC_ACCBOOK_BLNED_EXT.BOOK_ID " +
                "  AND    FC_ACCBOOK_BLNED.NET_ID = :netId " +
                "  AND    FC_ACCBOOK_BLNED.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 已结流水账主表转当天流水账表<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseAccbook(FcDayCloseDTO dayClose) {
        String sql = " INSERT INTO FC_ACCBOOK " +
                "  SELECT FC_ACCBOOK_BLNED.* " +
                "  FROM   FC_ACCBOOK_BLNED " +
                "  WHERE  FC_ACCBOOK_BLNED.NET_ID = :netId " +
                "  AND    FC_ACCBOOK_BLNED.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除已结流水账明细转已结流水账表<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccbookBlnedDtl(FcDayCloseDTO dayClose) {
        String sql = "   DELETE FROM FC_ACCBOOK_BLNED_DTL " +
                "  WHERE  EXISTS (SELECT 1 FROM FC_ACCBOOK_BLNED " +
                "  WHERE  FC_ACCBOOK_BLNED.ID = FC_ACCBOOK_BLNED_DTL.BOOK_ID" +
                "  AND    FC_ACCBOOK_BLNED.NET_ID = :netId " +
                "  AND    FC_ACCBOOK_BLNED.TALLY_DATE = :closeDate) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除已结流水账附加信息转已结流水账表<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccbookBlnedExt(FcDayCloseDTO dayClose) {
        String sql = "   DELETE FROM FC_ACCBOOK_BLNED_EXT " +
                "  WHERE  EXISTS (SELECT 1 FROM FC_ACCBOOK_BLNED " +
                "  WHERE  FC_ACCBOOK_BLNED.ID = FC_ACCBOOK_BLNED_EXT.BOOK_ID" +
                "  AND    FC_ACCBOOK_BLNED.NET_ID = :netId " +
                "  AND    FC_ACCBOOK_BLNED.TALLY_DATE = :closeDate) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 删除已结流水账主表转已结流水账表<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccbookBlned(FcDayCloseDTO dayClose) {
        String sql = "  DELETE FROM FC_ACCBOOK_BLNED " +
                " WHERE  FC_ACCBOOK_BLNED.NET_ID = :netId " +
                " AND    FC_ACCBOOK_BLNED.TALLY_DATE = :closeDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 功能介绍 : 跨日日结是否存在未结转的账务。<br>
     * <b>日期：</b>2017年7月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean existAccbookOverDays(FcDayCloseVO closeVO) {
        String hql = " from FcAccbook fa where fa.netId = :netCode "
                + " and fa.tallyDate >= :startDate and fa.tallyDate <= :endDate and voucherState <> 'B'";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netCode", closeVO.getNetCode());
        params.put("startDate", closeVO.getLastCloseDate());
        params.put("endDate", closeVO.getCloseDate());
        List accbooks = listByHQL(hql, params);
        return accbooks != null && accbooks.size() != 0;
    }

    public FcAccBalanceVO queryBalanceByDate(String account, Date yesterDate) {
        String sql = " SELECT a.account as account, " +
                " a.balance as balance " +
                " FROM FC_ACC_BALANCE a " +
                " WHERE a.account = :accounts" +
                " AND a.BALANCE_DATE = :yesterDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accounts", account);
        params.put("yesterDate", yesterDate);
        return getBySQLAliasToBean(sql,params, FcAccBalanceVO.class);
    }
}
