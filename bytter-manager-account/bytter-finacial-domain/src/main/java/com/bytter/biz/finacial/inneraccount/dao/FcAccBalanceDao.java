package com.bytter.biz.finacial.inneraccount.dao;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.inneraccount.entity.FcAccBalance;
import com.bytter.core.base.BaseDao;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @Title: Dao
* @Description: FC_ACC_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:04:02
* @version 1.0
*
*/
@Repository
public class FcAccBalanceDao extends BaseDao<FcAccBalance,String>{
    /**
     * 根据日期 和账号 获取 当前对象实体
     * @param date
     * @param account
     * @return
     */
    public FcAccBalance getEntityByAccountAndDate(Date date, String account){
        String hql = " from FcAccBalance where balanceDate=:date and account=:account ";
        Map<String, Object> params=new HashMap<>();
        params.put("date", date);
        params.put("account", account);
        return getUnique(hql,params);
    }
    /**
     * 修改余额
     * @param date
     * @param account
     * @param balance
     */
    public void updateAccBalanceByInitAcc(Date date, String account, BigDecimal balance) {
        String hql="update FcAccBalance set balance=:balance where balanceDate=:balanceDate and account=:account";
        Map<String, Object> params=new HashMap<>();
        params.put("balanceDate", date);
        params.put("account", account);
        params.put("balance", balance);
        updateByHql(hql, params);
    }
    public int  updateByHql(String hql, Map params) {
        Query query = getSession().createQuery(hql);
        if (params != null) {
            query.setProperties(params);
        }
        return query.executeUpdate();
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
    /**
     * 根据内部账号分组查询笔数
     * @param account
     * @return
     */
    public FcAccBalanceVO getGroupByAccount(String account) {
        String sql = "SELECT account,COUNT(1) cnt FROM fc_acc_balance where account=:account GROUP BY account";
        Map<String, Object> params=new HashMap<>();
        params.put("account", account);
        return getBySQLAliasToBean(sql, params, FcAccBalanceVO.class);
    }
    /**
     * 按日期分组余额
     * @param dates
     * @return
     */
    public List<FcAccBalanceVO> getGroupByDates(Date[] dates) {
        String sql = "SELECT bal.balance_date as balanceDate,SUM(bal.balance) AS creditMoney,SUM(bal.balance) AS debitMoney, ACCOUNT as account" +
                " FROM fc_acc_balance bal" +
                " WHERE balance_date in(:dates) " +
                " GROUP BY balance_date, ACCOUNT";
        Map<String, Object> params=new HashMap<>();
        params.put("dates", dates);
        return listBySQLAliasToBean(sql, params, FcAccBalanceVO.class);
    }

    public Map<String, FcAccBalanceVO> getGroupByAccount(List<String> accountList) {
        String sql = "SELECT account,COUNT(1) cnt FROM fc_acc_balance where account in (:accountList) GROUP BY account";
        Map<String, Object> params=new HashMap<>();
        params.put("accountList", accountList);
        List<FcAccBalanceVO> fcAccBalanceVOS = listBySQLAliasToBean(sql, params, FcAccBalanceVO.class);
        return fcAccBalanceVOS.stream().collect(Collectors.toMap(FcAccBalanceVO::getAccount, Function.identity()));
    }

    public FcAccBalance getEntityByParams(Date date, String account,String curId){
        String hql = " from FcAccBalance where balanceDate=:date and account=:account and curId=:curId ";
        Map<String, Object> params=new HashMap<>();
        params.put("date", date);
        params.put("account", account);
        params.put("curId", curId);
        return getUnique(hql,params);
    }
}
