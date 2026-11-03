package com.bytter.biz.finacial.inneraccount.dao;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.inneraccount.entity.FcAccuBalance;
import com.bytter.biz.finacial.interest.vo.FcAccuBalancefVo;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: FC_ACCU_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:03:48
* @version 1.0
*
*/
@Repository
public class FcAccuBalanceDao extends BaseDao<FcAccuBalance,String>{
    /**
     * 根据id 获取 当前对象实体
     * @param id
     * @return
     */
    public FcAccuBalance getFcAccuBalanceById(String id){
        String hql = " from FcAccuBalance where id=:id ";
        Map<String, Object> params=new HashMap<>();
        params.put("id", id);
        return getUnique(hql,params);
    }
    /**
     * 根据日期 和账号 获取 当前对象实体
     * @param date
     * @param account
     * @return
     */
    public FcAccuBalance getEntityByAccountAndDate(Date date, String account){
        String hql = " from FcAccuBalance where balanceDate=:date and account=:account ";
        Map<String, Object> params=new HashMap<>();
        params.put("date", date);
        params.put("account", account);
        return getUnique(hql,params);
    }
    /**
     * 更新账户的积数
     * @param account
     * @param balanceDate
     * @param cumu
     */
    public void updateAccountAccuBalanceByBalanceDate(String account, Date balanceDate, BigDecimal cumu) {
        Map<String,Object> params = new HashMap<>();
        params.put("cumu", cumu == null ? BigDecimal.ZERO : cumu);
        params.put("account", account);
        params.put("balanceDate", balanceDate);
        String hql = "update FcAccuBalance set cumu=:cumu where account=:account and balanceDate=:balanceDate";
        getSession().createQuery(hql).setProperties(params).executeUpdate();
    }
    
	public  List<FcAccuBalancefVo> listFcAccuBalancefVoByAccounts(List<String> accounts){
		StringBuilder sql = new StringBuilder();
		sql.append("select b.account as account, b.balance_date as balanceDate,");
		sql.append(" b.cumu as cumu, b.adjust_cumu as adjustCumu, b.interest_sign as interestSign ");
		sql.append(" from fc_accu_balance b where 1=1 ");
		sql.append(" and ").append(getSQLIn(accounts, 900, "b.account"));
		return this.listBySQLAliasToBean(sql.toString(), null, FcAccuBalancefVo.class);
	}
}
