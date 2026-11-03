package com.bytter.biz.finacial.inneraccount.dao;

import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.inneraccount.entity.FcOnlineLedger;
import com.bytter.core.base.BaseDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FC_ONLINE_LEDGER
 * @date 2017-06-07 10:03:54
 */
@Repository
public class FcOnlineLedgerDao extends BaseDao<FcOnlineLedger, String> {
    /**
     * 功能介绍 : 根据账号查询账户联机余额。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcOnlineLedgerDTO> getLedgerDTOsByAccSet(List<String> accList) {
        StringBuilder sql = new StringBuilder();
		/*sql.append(" select fl.account as account, fc.cur_id as curId,fl.TALLY_CNT as tallyCnt,fl.freeze_money as freezeMoney,")
				.append(" fl.CHECK_CNT as checkCnt,fl.ONLINE_BALANCE as onlineBalance,fl.CHECK_BALANCE as checkBalance,")
				.append(" fc.OVERDRAFT_SIGN as overdraftSign,fc.OVERDRAFT_MONEY as overdraftMoney,fc.balance_Way as balanceWay,")
				.append(" fc.net_id as netId, bc.code as netCode, fc.acc_type as accType,fc.charge_acc as chargeAcc,")
				.append(" fc.subject_id as subjectCode,cur.cur_code as curCode ")
				.append(" from fc_acc fc  ")
				.append(" left join bt_bank bank on fc.bank_code = bank.id ")
				.append(" ,fc_online_ledger fl,sys_corp bc, bt_currency cur ")
				.append(" where fc.blnctr_acc = fl.account and fc.net_id = bc.id and cur.id = fc.cur_id and ");
		String where = getSQLIn(accList, 900, "fl.account");*/
        sql.append(" select fl.account as account,fl.tally_cnt as tallyCnt,fl.freeze_money as freezeMoney,")
                .append(" fl.check_cnt as checkCnt,fl.online_balance as onlineBalance,fl.check_balance as checkBalance")
                .append(" from fc_online_ledger fl ")
                .append(" where 1=1 and ");
        String where = getSQLIn(accList, 900, "fl.account");
        return listBySQLAliasToBean(sql.append(where).toString(), null, FcOnlineLedgerDTO.class);
    }
    /**
     * 功能介绍 : 根据账号查询账户联机余额。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcOnlineLedgerDTO> getPoolLedgerDTOsByAccSet(List<String> accList) {
        StringBuilder sql = new StringBuilder();
		/*sql.append(" select fl.account as account, fc.cur_id as curId,fl.TALLY_CNT as tallyCnt,fl.freeze_money as freezeMoney,")
				.append(" fl.CHECK_CNT as checkCnt,fl.ONLINE_BALANCE as onlineBalance,fl.CHECK_BALANCE as checkBalance,")
				.append(" fc.OVERDRAFT_SIGN as overdraftSign,fc.OVERDRAFT_MONEY as overdraftMoney,fc.balance_Way as balanceWay,")
				.append(" fc.net_id as netId, bc.pool_code as netCode, fc.acc_type as accType,fc.charge_acc as chargeAcc,")
				.append(" fc.subject_id as subjectCode,cur.cur_code as curCode ")
				.append(" from fc_acc fc ")
				.append(" left join bt_bank bank on fc.bank_code = bank.id ")
				.append(" ,fc_online_ledger fl,fc_corp_pool bc, bt_currency cur ")
				.append(" where fc.blnctr_acc = fl.account and fc.net_id = bc.id and cur.id = fc.cur_id and ");
		String where = getSQLIn(accList, 900, "fl.account");*/
        sql.append(" select fl.account as account,fl.TALLY_CNT as tallyCnt,fl.freeze_money as freezeMoney,")
                .append(" fl.CHECK_CNT as checkCnt,fl.ONLINE_BALANCE as onlineBalance,fl.CHECK_BALANCE as checkBalance ")
                .append(" from fc_online_ledger fl ")
                .append(" where 1=1 and ");
        String where = getSQLIn(accList, 900, "fl.account");
        //fc.subject_id as subjectCode
        return listBySQLAliasToBean(sql.append(where).toString(), null, FcOnlineLedgerDTO.class);
    }
    /**
     * 功能介绍 : 更新账户记账余额信息。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateOnlineBalance(FcOnlineLedgerDTO onlineLedgerDTO) {
        String sql = " update fc_online_ledger set tally_cnt = :tallyCnt,online_balance = :onlineBalance " +
                " where account = :account ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tallyCnt", onlineLedgerDTO.getTallyCnt());
        params.put("onlineBalance", onlineLedgerDTO.getOnlineBalance());
        params.put("account", onlineLedgerDTO.getAccount());
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 更新账户记账余额信息
     *
     * @param onlineLedgerDTO     余额信息
     * @param origOnlineLedgerDTO 原始余额信息
     */
    public int updateOnlineBalance(FcOnlineLedgerDTO onlineLedgerDTO, FcOnlineLedgerDTO origOnlineLedgerDTO) {
        String sql = " update fc_online_ledger set tally_cnt = :tallyCnt,online_balance = :onlineBalance " +
                " where account = :account " +
                " and tally_cnt = :origTallyCnt " +
                " and online_balance = :origOnlineBalance";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tallyCnt", onlineLedgerDTO.getTallyCnt());
        params.put("onlineBalance", onlineLedgerDTO.getOnlineBalance());
        params.put("account", onlineLedgerDTO.getAccount());
        params.put("origTallyCnt", origOnlineLedgerDTO.getTallyCnt());
        params.put("origOnlineBalance", origOnlineLedgerDTO.getOnlineBalance());
        return createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 更新账户记账余额信息
     *
     * @param account 账户
     * @param money   发生额
     * @param cnt     记账笔数
     */
    public int updateAccOnlineBalance(String account, BigDecimal money, Integer cnt) {
        String sql = " update fc_online_ledger set tally_cnt = tally_cnt + (:cnt) , online_balance = online_balance + (:money) " +
                " where account = :account ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cnt", cnt);
        params.put("money", money);
        params.put("account", account);
        return createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 更新账户记账余额信息
     *
     * @param account 账户
     * @param money   发生额
     * @param cnt     记账笔数
     */
    public int updateAccCheckBalance(String account, BigDecimal money, Integer cnt) {
        String sql = " update fc_online_ledger set check_cnt = check_cnt + (:cnt), check_balance = check_balance + (:money) " +
                " where account = :account ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cnt", cnt);
        params.put("money", money);
        params.put("account", account);
        return createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 功能介绍 : 更新账户复核余额信息。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateCheckBalance(FcOnlineLedgerDTO onlineLedgerDTO) {
        String sql = " update fc_online_ledger set check_cnt = :checkCnt,check_balance = :checkBalance " +
                " where account = :account ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("checkCnt", onlineLedgerDTO.getCheckCnt());
        params.put("checkBalance", onlineLedgerDTO.getCheckBalance());
        params.put("account", onlineLedgerDTO.getAccount());
        createSQLQuery(sql, params).executeUpdate();
    }
    public FcOnlineLedger getEntityByAccount(String account) {
        List<FcOnlineLedger> list = listBy("account", account);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 更新账户当前冻结金额
     *
     * @param freezeMoney
     * @param curDate
     * @param updateBy
     * @param account
     */
    public void updateFreezeMoney(BigDecimal freezeMoney, Date curDate, String updateBy, String account) {
        String hql = "update FcOnlineLedger set freezeMoney=:freezeMoney,updateTime=:updateDate,updateBy=:updateBy where account=:account";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("freezeMoney", freezeMoney);
        params.put("updateDate", curDate);
        params.put("updateBy", updateBy);
        params.put("account", account);
        this.updateDelByHql(hql, params);
    }
    public void updateFreezeMoney(String account, BigDecimal money, BigDecimal oriFreezeMoney) {
        String hql = " update FcOnlineLedger set freezeMoney = :money where account = :account and freezeMoney = :oriFreezeMoney ";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("money", money);
        params.put("oriFreezeMoney", oriFreezeMoney);
        updateDelByHql(hql, params);
    }
    public void updateAccBal(String account, BigDecimal balance) {
        String hql = " update FcOnlineLedger set onlineBalance = :onlineBal, checkBalance = :checkBal " +
                " where account = :account ";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("onlineBal", balance);
        params.put("checkBal", balance);
        updateDelByHql(hql, params);
    }
    @SuppressWarnings("rawtypes")
    public int updateDelByHql(String hql, Map params) {
        Query query = getSession().createQuery(hql);
        if (params != null) {
            query.setProperties(params);
        }
        return query.executeUpdate();
    }
    public List<FcOnlineLedgerDTO> getBalanceByAcc(List<String> accList) {
        String sb = " select fl.account as account," +
                " nvl(fl.online_balance,0) - nvl(fl.freeze_money,0) as tallyBalance, " +
                " nvl(fl.check_balance,0) - nvl(fl.freeze_money,0) as checkBalance " +
                " from fc_online_ledger fl " +
                " where 1 = 1 " + " and " + getSQLIn(accList, 900, "fl.account");
        return listBySQLAliasToBean(sb, null, FcOnlineLedgerDTO.class);
    }

    public void updateBalance(FcOnlineLedgerDTO  ledgerDTO) {
        String hql = " update FcOnlineLedger set onlineBalance = :onlineBal, checkBalance = :checkBal " +
                " where account = :account and curCode = :curCode   ";
        Map<String, Object> params = new HashMap<>();
        params.put("account", ledgerDTO.getAccount());
        params.put("curCode", ledgerDTO.getCurId());
        params.put("onlineBal", ledgerDTO.getOnlineBalance());
        params.put("checkBal", ledgerDTO.getCheckBalance());
        updateDelByHql(hql, params);
    }
}
