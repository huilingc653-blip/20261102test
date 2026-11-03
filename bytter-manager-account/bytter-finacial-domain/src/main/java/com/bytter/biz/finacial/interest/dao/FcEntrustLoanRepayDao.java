package com.bytter.biz.finacial.interest.dao;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.interest.bean.AccInstrateBean;
import com.bytter.biz.finacial.interest.bean.InstrateAdjustBean;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.entity.FcLoanBalanceDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
@Repository
public class FcEntrustLoanRepayDao extends BaseDao<FcInterestDtl, String> implements Serializable {
	/**
	 * <b>功能介绍：</b>查询账户积数<br>
	 * <b>日期：</b>2017年8月24日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account 账号
	 * @param endInstDate 止息日期
	 * @return
	 */
	public List<FcAccuBalancef> queryAccuBalances(String account,Date endInstDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		//		sql.append("select b.* from FC_ACCU_BALANCE b left join fc_acc a on b.account = a.blnctr_acc where b.interest_sign='"+InterestConstant.INTEREST_SIGN_NO+"' ");
		sql.append("select b.* from FC_ACCU_BALANCE b where b.interest_sign='"+InterestConstant.INTEREST_SIGN_NO+"' ");
		sql.append("and b.account = :account ");
		if(!EmptyUtils.isEmpty(endInstDate)){
			sql.append(" and b.balance_date < :endInstDate ");
			param.put("endInstDate", endInstDate);
		}
		sql.append(" order by b.balance_date,b.cumu desc");
		param.put("account", account);
		return listBySQLAliasToBean(sql.toString(), param, FcAccuBalancef.class);
	}
	
	/**
	 * <b>功能介绍：</b>获取账号指定区间的未计息积数<br>
	 * <b>日期：</b>2017年8月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<FcAccuBalancef> getUnInterestBalances(String account, Date startDate, Date endDate) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append("from FcAccuBalancef  where interestSign = :interestSign and account = :account and balanceDate >= :startDate and balanceDate <= :endDate order by balanceDate,cumu desc");
		params.put("account", account);
		params.put("interestSign", InterestConstant.INTEREST_SIGN_NO);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return listByHQL(hql.toString(), params);
	}
	
	/**
	 * <b>功能介绍：</b>获取业务ID对应的积数信息<br>
	 * <b>日期：</b>2017年8月24日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param businessId
	 * @return
	 */
	public List<FcLoanBalanceDtl> getAccuBalanceDtlsByBusinessId(String businessId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("select dtl.* from FC_LOAN_BALANCE_DTL dtl left join FC_ACCU_BALANCE b on dtl.balance_id = b.id where dtl.loan_repay_id = :businessId order by b.balance_date,b.cumu ");
		params.put("businessId", businessId);
		return listBySQLAliasToBean(sql.toString(), params, FcLoanBalanceDtl.class);
	}
	
	/**
	 * <b>功能介绍：</b>根据账户获取账户利率调整信息<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	/*public Map<String, InstrateAdjustBean> queryInterestAdjustsByAcc(String blnctrAcc) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("select acc.blnctr_acc,ad.instrt_type,ad.new_instrate,ad.old_instrate,ad.adjust_time from FC_INSTRATE_ADJUST ad left join fc_acc acc on ad.account = acc.blnctr_acc "); //2张表都是基础模块的
		sql.append("where ad.status = :status and acc.blnctr_acc = :blnctrAcc ");
		params.put("status", CommonConstant.STATUE_CHECK);
		params.put("blnctrAcc", blnctrAcc);
		List<Object[]> adjustInfos = createSQLQuery(sql.toString(), params).list();
		Map<String, InstrateAdjustBean> result = new HashMap<String, InstrateAdjustBean>();
		for (Object[] adjustInfo : adjustInfos) {
			String account = (String) adjustInfo[0];
            String instrtType = (String) adjustInfo[1];
            InstrateAdjustBean accAdjstInfo = result.get(account);	
            if (accAdjstInfo == null) {
                accAdjstInfo = new InstrateAdjustBean();
                result.put(account, accAdjstInfo);
            }
            accAdjstInfo.getAccInstratesByAdjustType(instrtType, true).add(
                    new AccInstrateBean(account, instrtType, null, (Date) adjustInfo[4], (BigDecimal)adjustInfo[2],
                    		(BigDecimal)adjustInfo[3]));
		}
		return result;
	}*/
	
	/**
	 * <b>功能介绍：</b>更新积数计息标识<br>
	 * <b>日期：</b>2017年8月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @param businessId
	 */
	public void updateAccuBalanceByInterest(String account, String businessId) {
		StringBuilder sql = new StringBuilder();
		Map<String,Object> params = new HashMap<String,Object>();
		sql.append("update fc_accu_balance a set a.interest_sign = :interestSign,a.interest_time = null ");
		sql.append("where exists(select 1 from fc_loan_balance_dtl b where b.balance_id = a.id and b.loan_repay_id = :businessId) and a.account = :account ");
		params.put("interestSign", InterestConstant.INTEREST_SIGN_NO);
		params.put("businessId", businessId);
		params.put("account", account);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>删除还款关联积数信息<br>
	 * <b>日期：</b>2017年8月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param businessId
	 */
	public void deleteAccuBalanceDtlsByOrderNo(String businessId) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from FC_LOAN_BALANCE_DTL where loan_repay_id =? ");
		createSQLQuery(sql.toString(), businessId).executeUpdate();
	}
    /**
     * <b>功能介绍：</b>查询账户积数<br>
     * <b>日期：</b>2017年8月24日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param ids 账号
     * @return
     */
    public List<FcAccuBalancef> queryAccuBalancesByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.* from FC_ACCU_BALANCE b where 1 = 1 ");
        String whereIn = " and " + getSQLIn(ids, 900, "b.id");
        sql.append(whereIn.toString());
        sql.append(" order by b.balance_date,b.cumu desc ");
        return listBySQLAliasToBean(sql.toString(), null, FcAccuBalancef.class);
    }
    /**
     * <br>
     * <b>功能：</b> 更新积数的计息日期(不能是已计息的数据) <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/22 0022 <br>
     */
    public void updateAccuBalancesInterestSign(List<String> ids, Date interestTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("interestTime", interestTime);
        params.put("predrawDtls", ids);
        StringBuilder sql = new StringBuilder();
        sql.append("update FC_ACCU_BALANCE a set a.interest_sign = '1', a.interest_time = :interestTime ");
        sql.append("where 1 = 1  ");
        String whereIn = " and " + getSQLIn(ids, 900, "a.id");
        sql.append(whereIn);
        sql.append(" and (a.interest_sign = '0' or a.interest_sign is null) ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * <br>
     * <b>功能：</b> 更新积数的计息日期(不能是已计息的数据) <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/22 0022 <br>
     */
    public void deleteAccuBalances(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from FC_ACCU_BALANCE ");
        sql.append("where 1 = 1  ");
        String whereIn = " and " + getSQLIn(ids, 900, "id");
        sql.append(whereIn);
        createSQLQuery(sql.toString()).executeUpdate();
    }
}
