package com.bytter.check.dao;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.finacial.api.constant.CheckConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.check.dto.FcCompareAccBeanDTO;
import com.bytter.check.entity.FcBankBalanceAdjust;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.vo.FcBankBillSearchVo;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Dao
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-05-31 14:43:17
* @version 1.0
*
*/
@Repository
public class FcCompareCenterDao extends BaseDao<FcCompareAcc,String>{
	
	/**
	 * 功能介绍:查询中心对账数据(未达、已对账、已勾兑、已对账等)<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryCntrCheckDatas(String bankAcc,String account,Date transDate,String[] compareSigns,BigDecimal beginMoney,BigDecimal endMoney){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		List<Map<String, Object>> list = new ArrayList<>();
		sql.append("select a.tally_date as tallyDate,a.order_no as orderNo,a.comp_acc as compAcc,a.redblue_sign as redblueSign,a.line_no as lineNo,a.summary_Content as summaryContent, ");
		sql.append("case when BT_NVL_NUM(a.DEBIT_MONEY, 0) <> 0 then '1' else '0' end as cdSign,BT_NVL_NUM(a.debit_money, 0) + BT_NVL_NUM(a.credit_money, 0) as money, ");
		sql.append("a.voucher_No as voucherNo,a.compare_sign as compareSign,a.cur_Id as curCode,a.id,a.BILL_NO as billNo ");
		sql.append("from fc_compare_acc a ");
		sql.append("inner join fc_check_init i on i.account = a.comp_acc and i.check_date < a.tally_date and i.bank_acc in (:bankAcc) ");
		sql.append(" where 1 = 1 ");
		int i=0;
		if (null == compareSigns || compareSigns.length == 0) {
        } else {
            StringBuilder sqladd = new StringBuilder();
            for (String compareSign : compareSigns) {
                if (FinacialConstant.COMPARE_SIGN_STAY.equals(compareSign)) {//未达账记录
                    if (i != 0) {
                        sqladd.append(" or ");
                    }
                    sqladd.append(" a.compare_sign = '" + FinacialConstant.COMPARE_SIGN_STAY + "' ");
                    i++;
                    continue;
                }
                if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)) {//已经勾兑记录
                    if (i != 0) {
                        sqladd.append(" or ");
                    }
                    sqladd.append(" a.compare_sign = '" + FinacialConstant.COMPARE_SIGN_HAND + "' ");
                    i++;
                    continue;
                }
                if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign)) {//已经对账记录
                    if (i != 0) {
                        sqladd.append(" or ");
                    }
                    sqladd.append(" a.compare_sign = '" + FinacialConstant.COMPARE_SIGN_YES + "' ");
                    i++;
                    continue;
                }
                if (FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)) {//未对账
                    if (i != 0) {
                        sqladd.append(" or ");
                    }
                    sqladd.append(" a.compare_sign = '" + FinacialConstant.COMPARE_SIGN_NO + "'  or a.compare_sign = '' or a.compare_sign is null ");
                    i++;
                }
            }
            if (EmptyUtils.isNotEmpty(sqladd.toString())) {
                sql.append(" and ( ");
                sql.append(sqladd);
                sql.append(" ) ");
            } else {
                return list;
            }
        }
		sql.append(" and a.comp_acc in (:account) and a.tally_date <= :transDate ");
		if (!EmptyUtils.isEmpty(beginMoney) && !EmptyUtils.isEmpty(endMoney)) {
            if(beginMoney.compareTo(BigDecimal.ZERO)>0 || endMoney.compareTo(BigDecimal.ZERO)>0) {
                sql.append(" and (a.DEBIT_MONEY + a.CREDIT_MONEY >=:beginMoney) ");
                sql.append(" and (a.DEBIT_MONEY + a.CREDIT_MONEY <=:endMoney) ");
                params.put("beginMoney", beginMoney);
                params.put("endMoney", endMoney);
            }
        }
		sql.append(" order by a.comp_acc,tally_date,money ");
		params.put("bankAcc", bankAcc.split(","));
		params.put("account", account.split(","));
		params.put("transDate", transDate);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
		sqlQuery.addScalar("tallyDate", StandardBasicTypes.DATE);
		sqlQuery.addScalar("orderNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("compAcc", StandardBasicTypes.STRING);
		sqlQuery.addScalar("redblueSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("lineNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("summaryContent", StandardBasicTypes.STRING);
		sqlQuery.addScalar("cdSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("money", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("voucherNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("compareSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("curCode", StandardBasicTypes.STRING);
		sqlQuery.addScalar("billNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("id", StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		list = sqlQuery.list();
		return list;
	}
	

	
	/**
	 * 功能介绍:查询中心对帐数据<br>
	 * <b>日期：</b>2017年6月8日<br>
	 * @version 1.0
	 * @author caozx
	 */
	 public List<FcCompareAccVO> queryFcCompareAccs(FcBankBillSearchVo searchBean,
													String orderBy, String userCode) {
		 Map<String, Object> params = new HashMap<String, Object>();
		 StringBuilder sql= new StringBuilder();
		 sql.append("select ac.id,ac.order_no,ac.voucher_no,ac.tally_date,BT_NVL_NUM(ac.debit_money,0) as debit_money,BT_NVL_NUM(ac.credit_money,0) as credit_money,ac.bill_no,ac.redblue_sign,ac.compare_sign,ac.line_no,ac.balance,ac.cur_id ");
		 sql.append("from fc_compare_acc ac ");
		 sql.append("inner join fc_check_init i on i.account = ac.comp_acc and i.check_date < ac.tally_date ");
		 sql.append("where 1=1 ");
		 sql.append("and ac.tally_date <= :tallyDate ");
		 sql.append("and ac.comp_acc = :account ");
		 sql.append("and (ac.compare_sign = '0' or ac.compare_sign = '' or ac.compare_sign is null) ");
		 sql.append("order by ac.comp_acc,ac.tally_date,ac.order_no ");
		 params.put("tallyDate", searchBean.getImportDateEnd());
		 params.put("account", searchBean.getAccount());
		 return listBySQLAliasToBean(sql.toString(), params, FcCompareAccVO.class);
	 }
	 
	 /**
	  * 功能介绍:获取中心未达账金额合计<br>
	  * <b>日期：</b>2017年6月8日<br>
	  * @version 1.0
	  * @author caozx
	  */
	 @SuppressWarnings("unchecked")
	 public Map<String, Object> queryStayOverCtrSum(Date transDate, String account, String bankAcc, String overType) {
		 StringBuilder sqlSelect = new StringBuilder();
		 sqlSelect.append("SELECT BT_NVL_NUM(count(a.debit_money),0) moneyCount, BT_NVL_NUM(sum(a.debit_money),0) debitMoney,BT_NVL_NUM(sum(a.credit_money),0) creditMoney ");
	        
		 sqlSelect.append(" FROM FC_COMPARE_ACC a where ");
		 sqlSelect.append(" exists (select 1 from fc_check_init i where i.account = a.comp_acc and i.check_date < a.tally_date ");
		 sqlSelect.append(" and i.bank_acc = :bankAcc) and (a.compare_sign = :compareSign ) ");
	        if (CheckConstant.CTR_PAY_BANK_NO_PAY.equals(overType)) {//中心已付、银行未付
	        	sqlSelect.append(" and a.debit_money = 0 and a.credit_money <> 0");
	        } else if (CheckConstant.CTR_REC_BANK_NO_REC.equals(overType)) {//中心已收、银行未收
	        	sqlSelect.append(" and a.debit_money <> 0 and a.credit_money = 0");
	        }
	        sqlSelect.append(" and a.comp_acc= :compAcc and a.tally_date <= :transDate ");
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("bankAcc", bankAcc);
	        params.put("compAcc", account);
	        params.put("transDate", transDate);
	        params.put("compareSign", FinacialConstant.COMPARE_SIGN_STAY);
	        
	        SQLQuery sqlQuery = createSQLQuery(sqlSelect.toString(),params);
	        sqlQuery.addScalar("moneyCount", StandardBasicTypes.LONG);
			sqlQuery.addScalar("debitMoney", StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.addScalar("creditMoney", StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
	        List<Map<String, Object>> list = sqlQuery.list();
	        Map<String, Object> resultMap = new HashMap<String, Object>();
	        if (list != null && list.size() > 0) {
	            resultMap = list.get(0);
	        }
	        return resultMap;
	  }
	
	 /**
	  * <b>功能介绍：</b>统计未对账总数<br>
	  * <b>日期：</b>2017年10月10日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  * @param transDate
	  * @param account
	  * @param overType
	  * @return
	  */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryUnOverCtrSum(Date transDate, String account, String overType) {
		 StringBuilder sqlSelect = new StringBuilder();
		 sqlSelect.append("SELECT BT_NVL_NUM(count(a.debit_money),0) moneyCount, BT_NVL_NUM(sum(a.debit_money),0) debitMoney,BT_NVL_NUM(sum(a.credit_money),0) creditMoney ");
		 sqlSelect.append("FROM FC_COMPARE_ACC a inner join fc_check_init i on i.account = a.comp_acc and i.check_date < a.tally_date ");
		 sqlSelect.append("WHERE (a.compare_sign = :compareSign or a.compare_sign = '' or a.compare_sign is null ) ");
	        if (CheckConstant.CTR_PAY_BANK_NO_PAY.equals(overType)) {//中心已付、银行未付
	        	sqlSelect.append(" and a.debit_money = 0 and a.credit_money <> 0 ");
	        } else if (CheckConstant.CTR_REC_BANK_NO_REC.equals(overType)) {//中心已收、银行未收
	        	sqlSelect.append(" and a.debit_money <> 0 and a.credit_money = 0 ");
	        }
	        sqlSelect.append(" and a.comp_acc= :compAcc and a.tally_date <= :transDate ");
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("compAcc", account);
	        params.put("transDate", transDate);
	        params.put("compareSign", FinacialConstant.COMPARE_SIGN_NO);
	        
	        SQLQuery sqlQuery = createSQLQuery(sqlSelect.toString(),params);
	        sqlQuery.addScalar("moneyCount", StandardBasicTypes.LONG);
			sqlQuery.addScalar("debitMoney", StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.addScalar("creditMoney", StandardBasicTypes.BIG_DECIMAL);
			sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			
	        List<Map<String, Object>> list = sqlQuery.list();
	        Map<String, Object> resultMap = new HashMap<String, Object>();
	        if (list != null && list.size() > 0) {
	            resultMap = list.get(0);
	        }
	        return resultMap;
	 }
	
	/**
	 * <b>功能介绍：</b>查询上期留存未对账的中心数据<br>
	 * <b>日期：</b>2017年10月10日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @param overType
	 * @param transDate
	 * @return
	 */
	public List<FcCompareAccVO> queryUnOverCtrStayDatas(String account, String overType, Date transDate) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.order_no,a.voucher_no,a.COMP_ACC,a.tally_date, ");
		sql.append("a.debit_money,a.credit_money,a.bill_no,a.redblue_sign,a.compare_sign,a.line_no,a.balance,a.summary_content,a.cur_id ");
		sql.append("from fc_compare_acc a where (a.compare_sign = :compareSign0 or a.compare_sign = :compareSign1) ");
		if (CheckConstant.CTR_PAY_BANK_NO_PAY.equals(overType)) {// 中心已付、银行未付
			sql.append(" and a.debit_money = 0 and a.credit_money <> 0 ");
		} else if (CheckConstant.CTR_REC_BANK_NO_REC.equals(overType)) {// 中心已收、银行未收
			sql.append(" and a.debit_money <> 0 and a.credit_money = 0 ");
		}
		sql.append("and a.comp_acc = :account and a.tally_date <= :transDate order by a.tally_date");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("transDate", transDate);
		params.put("compareSign0", FinacialConstant.COMPARE_SIGN_STAY);
		params.put("compareSign1", FinacialConstant.COMPARE_SIGN_STAY_UP);
		return listBySQLAliasToBean(sql.toString(), params, FcCompareAccVO.class);
	}
	 
	 /**
	  * <b>功能介绍:更新状态为已对账</b><br>
	  * <b>日期：</b>2017年6月12日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  */
	 public void updateComparesignOverCheck(List<String> compareAccPKList,String compareSign){
		 Map<String, Object> params = new HashMap<String, Object>();
		 StringBuilder sql = new StringBuilder();
		 sql.append("update fc_compare_acc a set a.compare_sign= :compareSign ");
		 sql.append("where 1=1 and ");
		 sql.append(getSQLIn(compareAccPKList, 900, "a.id"));
		 params.put("compareSign", compareSign);
		 createSQLQuery(sql.toString(), params).executeUpdate();
	 }
	 
	 /**
	  * <b>功能介绍：</b>获取账户上次结转日期<br>
	  * <b>日期：</b>2017年9月27日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  * @param account
	  * @param bankAcc
	  * @return
	  */
	 public FcBankBalanceAdjust getLastAdjustOver(String account, String bankAcc) {
		 StringBuilder hql = new StringBuilder();
		 hql.append("from FcBankBalanceAdjust a where a.endDate = (select max(b.endDate) from FcBankBalanceAdjust b where ");
		 hql.append("b.account = :account and b.bankAcc = :bankAcc and (b.status is null or b.status <> :status)) ");
		 hql.append("and a.account = :account and a.bankAcc = :bankAcc and (a.status is null or a.status <> :status) ");
         Map<String, Object> params = new HashMap<String, Object>();
         params.put("account", account);
         params.put("bankAcc", bankAcc);
         params.put("status", CommonConstant.STATUE_DELETE);
	     return getUnique(hql.toString(), params);
	 }
	 
	 /**
	  * <b>功能介绍：</b>查询账户余额<br>
	  * <b>日期：</b>2017年9月27日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  * @param account
	  * @param balanceDate
	  * @return
	  */
	@SuppressWarnings("unchecked")
	public List<BigDecimal> queryAccBalanceByDate(String account, Date balanceDate){
		 String sql = "SELECT balance FROM fc_acc_balance  WHERE account= :account AND balance_date = :balanceDate";
         Map<String, Object> params = new HashMap<String, Object>();
         params.put("account", account);
         params.put("balanceDate", balanceDate);
         return createSQLQuery(sql.toString(), params).list();
	 }
	
	/**
	 * <b>功能介绍：</b>获取银行账号余额(根据明细)<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param transTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BigDecimal> getBankAccBalance(String bankAcc,Date transTime){
		StringBuilder sql = new StringBuilder();
		sql.append("select dtl.bal from bis_acc_dtl dtl where dtl.bank_acc=:bankAcc and dtl.trans_time < :transTime order by dtl.trans_time desc ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("transTime", DateUtils.addDay(transTime, 1));
		return createSQLQuery(sql.toString(), params).list();
	}
	
	/**
	 * <b>功能介绍：</b>获取银行账号余额(根据余额)<br>
	 * <b>日期：</b>2017年9月30日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param transTime
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BigDecimal> getBankAccBalanceByBal(String bankAcc,Date transTime){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.bal from bis_acc_bal a where a.bank_acc = :bankAcc and a.bal_date < :transTime order by a.bal_date desc");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("transTime", DateUtils.addDay(transTime, 1));
		return createSQLQuery(sql.toString(), params).list();
	}
	
	/**
	 * <b>功能介绍：</b>删除接口导入且未对账的数据<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param bankAcc
	 */
	public void deleteInterfaceImportDatas(Date transDate, Date checkDate, String bankAcc,String curId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("delete from fc_bank_bill  where tally_date <= :transDate and tally_date > :checkDate  ");
		sql.append("and (compare_sign = '1' or compare_sign is null or compare_sign = '0') ");
		sql.append("and bank_acc = :bankAcc and cur_id = :curId and import_type = :importType ");
		params.put("transDate", transDate);
		params.put("checkDate", checkDate);
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("importType", FinacialConstant.IMPORT_TYPE_INTERFACE);
		createSQLQuery(sql.toString(),params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>更新手工录入或文本导入且已对账的数据为未对账<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param bankAcc
	 */
	public void updateBankBillStatus(Date transDate, Date checkDate, String bankAcc,String curId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_bank_bill b set b.compare_sign = '0' where b.tally_date <= :transDate and b.tally_date > :checkDate ");
		sql.append("and b.compare_sign = '1' and b.bank_acc = :bankAcc and b.cur_id = :curId ");
		//sql.append("and (b.import_type = :importTypeHand or b.import_type = :importTypeFile) ");
		params.put("transDate", transDate);
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("checkDate", checkDate);
		//params.put("importTypeHand", FinacialConstant.IMPORT_TYPE_HAND);
		//params.put("importTypeFile", FinacialConstant.IMPORT_TYPE_FILE);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>查询银行对账表中不存在的对账数据<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param bankAcc
	 * @param userCode
	 */
	public List<FcBankBillVo> findNoExistNeedCheckBills(Date transDate, Date checkDate, String bankAcc, String userCode, String curId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select acc.cur_id,bankcc.counter_acc as account,dtls.bank_acc,BT_TRUNC_DATE(dtls.trans_time) tally_date, ");
		sql.append("(case dtls.cd_sign when '1' then dtls.amt else 0 end) debit_money, ");
		sql.append("(case dtls.cd_sign when '0' then dtls.amt else 0 end) credit_money, ");
		sql.append("dtls.bal as balance, ");
		sql.append("(case dtls.rb_sign when '1' then 'R' else 'B' end) redblue_sign, ");
		sql.append("dtls.voucher_no,dtls.id as bank_id,'0' as compare_sign,'3' as import_type ");
		sql.append("from bis_acc_dtl dtls,bt_bank_acc bank,bt_bank_acc_cur bankcc,fc_acc acc ");
		sql.append("where dtls.bank_acc=bank.bank_acc ");
		sql.append("and bank.id = bankcc.bank_acc_id ");
		sql.append("and bankcc.counter_acc = acc.blnctr_acc ");
		sql.append("and bankcc.cur_id = acc.cur_id ");
		sql.append("and acc.cur_id = :curId ");
		sql.append("and bank.bank_acc = :bankAcc ");
		sql.append("and dtls.trans_time > :checkDate ");
		sql.append("and dtls.trans_time < :transDate ");
		sql.append("and not exists(select 1 from fc_bank_bill bill where bill.bank_id = dtls.id) ");
		//FIXME 账号单位权限过滤未加
		params.put("curId", curId);
		params.put("bankAcc", bankAcc);
		params.put("checkDate", checkDate);
		params.put("transDate", DateUtils.addDay(transDate, 1));
		return listBySQLAliasToBean(sql.toString(), params, FcBankBillVo.class);
	}
	
	/**
	 * <b>功能介绍：</b>删除对账时产生的且状态为已对账或未对账的数据<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param account
	 */
	public void deleteCheckCompareAccs(Date transDate, Date checkDate, String account,String curId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("delete from fc_compare_acc  where tally_date <= :transDate ");
		sql.append("and tally_date > :checkDate ");
		sql.append("and (compare_sign = '1' or compare_sign is null or compare_sign = '' or compare_sign = '0') ");
		sql.append("and comp_acc = :account and cur_id = :curId and import_type = :importType ");
		params.put("transDate", transDate);
		params.put("checkDate", checkDate);
		params.put("account", account);
		params.put("curId", curId);
		params.put("importType", FinacialConstant.CTR_IMPORT_TYPE_DEFAULT);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>将手工录入的已对账的数据改为未对账<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param account
	 */
	public void updateCompareAccStatus(Date transDate, Date checkDate, String account,String curId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_compare_acc a set a.compare_sign = '0' where a.tally_date <= :transDate and a.tally_date > :checkDate ");
		sql.append("and a.compare_sign = '1' and a.comp_acc = :account and a.cur_id = :curId ");
		//sql.append("and (a.import_type = :importTypeHand or a.import_type = :importTypeFile) ");
		params.put("transDate", transDate);
		params.put("checkDate", checkDate);
		params.put("account", account);
		params.put("curId", curId);
		params.put("importTypeHand", FinacialConstant.IMPORT_TYPE_HAND);
		params.put("importTypeFile", FinacialConstant.IMPORT_TYPE_FILE);
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>查询对账表中不存在的需对账的数据<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param account
	 */
	public List<FcCompareAccVO> findNoExistNeedCheckAccs(Date transDate, Date checkDate,String checkType, String account,String curId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select acc.cur_id,book.order_no,book.voucher_no,book.tally_date,book.interest_start_date, ");
		sql.append("book.operator_code as import_user,book.orig_voucher_no as bill_no,dtled.summary_content,dtled.account as comp_acc,dtled.debit_money, ");
		sql.append("dtled.credit_money,dtled.balance,book.redblue_sign,dtled.line_no,'0' as compare_sign,'1' as import_type,'"+checkType+"' as check_type ");
		sql.append("from fc_accbook_blned book,fc_accbook_blned_dtl dtled,fc_acc acc ");
		sql.append("where book.id = dtled.book_id and acc.blnctr_acc = dtled.account and acc.cur_id = :curId and acc.if_compare = '1' ");
		sql.append("and book.voucher_state <> 'B' and dtled.account = :account and (book.tally_date > :checkDate and book.tally_date <= :transDate) ");
		sql.append("and not exists (select 1 from fc_compare_acc aa where aa.comp_acc = :account and aa.voucher_no = book.voucher_no and aa.line_no = dtled.line_no) ");
		params.put("account", account);
		params.put("curId", curId);
		params.put("transDate", transDate);
		params.put("checkDate", checkDate);
		return listBySQLAliasToBean(sql.toString(), params, FcCompareAccVO.class);
	}
	
	/**
	 * <b>功能介绍：</b>结转中心未达数据<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @param transDate
	 */
	public void updateOverStayAccInfos(String account, Date transDate){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_compare_acc a set a.compare_sign = :compareSignNo where a.comp_acc = :account and a.compare_sign = :compareSignStay  ");
		sql.append("and a.tally_date <= :transDate ");
		params.put("account", account);
        params.put("transDate", transDate);
        params.put("compareSignNo", FinacialConstant.COMPARE_SIGN_NO);
        params.put("compareSignStay", FinacialConstant.COMPARE_SIGN_STAY);
        createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>结转中心对账数据<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @param transDate
	 */
	public void updateOverAccInfos(String account, Date transDate){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_compare_acc a set a.compare_sign = :compareSignOver ");
		sql.append("where a.comp_acc = :account and (a.compare_sign = :compareSignYes or a.compare_sign = :compareSignHand) and a.tally_date <= :transDate ");
		params.put("account", account);
        params.put("transDate", transDate);
        params.put("compareSignOver", FinacialConstant.COMPARE_SIGN_OVER);
        params.put("compareSignYes", FinacialConstant.COMPARE_SIGN_YES);
        params.put("compareSignHand", FinacialConstant.COMPARE_SIGN_HAND);
        createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>获取中心可结转对账单<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param account
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FcCompareAccVO> getctrDataOverData(Date transDate, String account, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.id,a.compare_sign from fc_compare_acc a where a.comp_acc = :account and a.compare_sign in (:compareSign) and a.tally_date <= :transDate ");
		if (EmptyUtils.isNotEmpty(curId)) {
			sql.append(" and a.cur_id = :curId ");
		}
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        params.put("transDate", transDate);
		params.put("curId", curId);
        params.put("compareSign",
                new String[] { FinacialConstant.COMPARE_SIGN_STAY, FinacialConstant.COMPARE_SIGN_YES,
                		FinacialConstant.COMPARE_SIGN_HAND });
        return listBySQLAliasToBean(sql.toString(), params, FcCompareAccVO.class);
	}
    /**
     * <br>
     * <b>功能：</b> 根据结转ID获取企业对账单数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/1/6 0006 <br>
     */
    public List<Map<String, String>> getOverAccBillId(String[] adId) {
        String sql = " select p.fc_compare_acc_id as ID from fc_compare_acc_proc p where p.ajust_id in (:ajustId) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ajustId", adId);
        return listBySQL(sql, params);
    }
	
	/**
	 * <b>功能介绍：</b>反结转更新<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param accIds
	 */
	public int updateRedoAdjustOver(List<String> accIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_compare_acc a set a.compare_sign = :compareSign where 1=1 ");
		sql.append(" and ").append(getSQLIn(accIds, 900, "a.id"));
		params.put("compareSign", FinacialConstant.COMPARE_SIGN_NO);
    	//params.put("accIds", accIds);
    	return createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>查询中心结转明细清单<br>
	 * <b>日期：</b>2017年10月20日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param ajustId
	 * @param stayRadios
	 * @param handRadios
	 * @param checkRadios
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<FcCompareAccVO> queryCntrOverDatas(String ajustId,String stayRadios,String handRadios,String checkRadios){
		StringBuilder sql = new StringBuilder();
		List<Integer> compareSigns = new ArrayList<Integer>();
		if("1".equals(stayRadios)){//未达账
			compareSigns.add(Integer.valueOf(FinacialConstant.COMPARE_SIGN_STAY));
		}
		if("1".equals(handRadios)){//已勾兑
			compareSigns.add(Integer.valueOf(FinacialConstant.COMPARE_SIGN_HAND));
		}
		if("1".equals(checkRadios)){//已对账
			compareSigns.add(Integer.valueOf(FinacialConstant.COMPARE_SIGN_YES));
		}
		sql.append("select a.tally_date,a.summary_content,BT_NVL_NUM(a.debit_money,0) as debit_money,BT_NVL_NUM(a.credit_money,0) as credit_money,a.voucher_no,a.bill_no,p.compare_sign_his as compare_sign from fc_compare_acc a,fc_compare_acc_proc p ");
		sql.append("where a.id = p.fc_compare_acc_id and p.ajust_id= '"+ajustId+"' ");
		sql.append("and " + getSQLIn(compareSigns, 900, "p.compare_sign_his"));
		sql.append(" order by a.tally_date ");
		return listBySQLAliasToBean(sql.toString(), null, FcCompareAccVO.class);		
	}

	public List<FcCompareAcc> getCompAccByOrderNo(List<FcCompareAccBeanDTO> dtos) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from fc_compare_acc a where 1=1 ");
		if (CollectionUtils.isNotEmpty(dtos)){
			sql.append(" and ");
			dtos.forEach(a ->{
				sql.append(" (a.order_no = ").append("'").append(a.getOrderNo()).append("'").append(" and a.line_no = ").append(a.getLineNo()).append(" )");
				sql.append(" or ");
			});
		}
		int or = sql.lastIndexOf("or");
		String substring = sql.toString().substring(0, or);
		return listBySQLAliasToBean(substring, null, FcCompareAcc.class);
	}
}
