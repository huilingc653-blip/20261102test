package com.bytter.check.dao;

import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.check.constant.CheckConstant;
import com.bytter.check.constant.FinacialConstant;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.vo.FcBankCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-05-31 14:43:17
* @version 1.0
*
*/
@Repository
public class FcCompareCenterAutoDao extends BaseDao<FcCompareAcc,String>{
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@Autowired
	private FcCompareCenterDao fcCompareAccDao;
	public String queryCheckListTheAccSql(DataTablesInput input, List<String> corpAuth, Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		String bankAcc =  (String) input.getFormdata().get("bankAcc");
		if (!EmptyUtils.isEmpty(bankAcc)) {	//将bankacc 拆分为list，拼接查询条件，避免in条件超长
			List<String> bankAccList = StringUtils.splitToList(",",bankAcc);
			sql.append(" and ").append(getSQLIn(bankAccList, 900, "acc.bank_acc"));
		}
		String netId = (String) input.getFormdata().get("netIdStr");
		if(EmptyUtils.isNotEmpty(netId)){
			sql.append(" and corp.NET_ID =:netId");
			params.put("netId",netId);
		}
		sql.append("  ");
		return sql.toString();
	}
	/**
	 * <br>
	 * <b>功能：</b> 查询一键对账列表 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2020/10/15 0015 <br>
	 */
	public DataTablesOutput queryCheckList(DataTablesInput input, List<String> accList, Date calcDate, String compareSign) {
		StringBuilder sql = new StringBuilder();
		String compareCalDateString = new StringBuilder("bt_to_date('").append(DateUtils.formatDate(calcDate)).append("','yyyy-MM-dd')").toString();
		sql.append(" select  fc.bank_acc as bankAcc,fc.account as account,fc.cur_id as curId, ")
				.append(" fc.check_date as checkDate,fd.begin_date as beginDate, fd.end_date as endDate, ")
				.append(" BT_NVL_NUM(eb.erpCnt,0) as erpCnt, BT_NVL_NUM(bb.bankCnt,0) as bankCnt ")
				.append(" from fc_check_init fc left join ")
				.append(" (select fb.bank_acc,fb.account,fb.cur_code,max(end_date) as end_date,max(begin_date) as begin_date from FC_BANK_BALANCE_ADJUST fb ")
				.append("  where (fb.status <> -2 or fb.status is null) ")
				.append(" and fb.check_type = '0' ")	//FC_BANK_BALANCE_ADJUST.check_type（0中心对账 1ERP对账）
				.append(" group by fb.bank_acc,fb.account,fb.cur_code) fd on fc.bank_acc = fd.bank_acc and fc.account = fd.account and fc.cur_id = fd.cur_code ")
				//comp.compare_sign <> '3' 银行对账数据表，对账数据不等于已结转
				.append(" left join (select comp.comp_acc,comp.cur_id,count(1) as erpCnt from fc_compare_acc comp where comp.compare_sign <> '3' and comp.check_type = '1' ");
		sql.append(" and exists (select 1  from fc_check_init fc2  where fc2.account = comp.comp_acc and comp.cur_id = fc2.cur_id ");
		sql.append(" and ( fc2.check_date < comp.tally_date  or comp.IMPORT_TYPE = '4' ) ) ");
		//comp.IMPORT_TYPE = 4  未达账初始化
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and comp.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" and comp.compare_sign <> '3' group by comp.comp_acc,comp.cur_id) eb on eb.comp_acc = fc.account and eb.cur_id = fc.cur_id ")
				.append(" left join (select bank.bank_acc,bank.cur_id,count(1) as bankCnt from fc_bank_bill bank where bank.compare_sign <> '3' ");
		sql.append(" and exists (select 1  from fc_check_init fc1  where fc1.bank_acc = bank.bank_acc and bank.cur_id = fc1.cur_id " );
		sql.append(" and ( fc1.check_date < bank.tally_date  or bank.IMPORT_TYPE = '4' ) )");
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and bank.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" group by bank.bank_acc,bank.cur_id) bb on bb.bank_acc = fc.bank_acc and bb.cur_id = fc.cur_id ")
				.append(" where fc.check_type = '1' ");
		//过滤无效状态的账号
		if ("0".equals(compareSign)) {
			sql.append(" and fc.check_date < ").append(compareCalDateString);
			sql.append(" and (fd.end_date is null or fd.end_date < ").append(compareCalDateString).append(")");
		} else if ("1".equals(compareSign)) {
			sql.append(" and (fc.check_date >= ").append(compareCalDateString);
			sql.append("or fd.end_date >= ").append(compareCalDateString).append(")");
		}
		if (accList.size()>0) {
			sql.append(" and ").append(getSQLIn(accList, 900, "fc.bank_acc"));
		}
		sql.append("  ");
		return getDynamicBySQL(input, sql.toString(), FcBankCheckVO.class,"",false);
	}
	public DataTablesOutput getAdjustInfo(DataTablesInput input,String compareCalDateString,List<String> accS, Date calcDate, String compareSign){
		StringBuilder sql = new StringBuilder();
				sql.append(" select ")
				.append(" fc.bank_acc as bankAcc,fc.account as account,fc.cur_id as curId, fc.bank_balance AS bankBalance,fc.bln_balance AS blnBalance, ")
				.append(" fc.check_date as checkDate,fd.begin_date as beginDate, fd.end_date as endDate, ")
				.append(" BT_NVL_NUM(eb.erpCnt,0) as erpCnt, BT_NVL_NUM(bb.bankCnt,0) as bankCnt ")
				.append(" from fc_check_init fc left join ")
				.append(" (select fb.bank_acc,fb.account,fb.cur_code,max(end_date) as end_date,max(begin_date) as begin_date from FC_BANK_BALANCE_ADJUST fb ")
				.append("  where (fb.status <> -2 or fb.status is null) ")
				.append(" and fb.check_type = '0' ")	//FC_BANK_BALANCE_ADJUST.check_type（0中心对账 1ERP对账）
				.append(" group by fb.bank_acc,fb.account,fb.cur_code) fd on fc.bank_acc = fd.bank_acc and fc.account = fd.account and fc.cur_id = fd.cur_code ")
//				.append(" inner join bt_bank_acc acc on fc.bank_acc = acc.bank_acc ")
//				.append(" inner join bt_currency bc on fc.cur_id = bc.id ")
//				.append(" inner join sys_corp sc on acc.corp_id = sc.id ")//fc_compare_acc.check_type（1中心对账 2ERP对账）
				//comp.compare_sign <> '3' 银行对账数据表，对账数据不等于已结转
				.append(" left join (select comp.comp_acc,comp.cur_id,count(1) as erpCnt from fc_compare_acc comp where comp.compare_sign <> '3' and comp.check_type = '1' ");
		sql.append(" and exists (select 1  from fc_check_init fc2  where fc2.account = comp.comp_acc and comp.cur_id = fc2.cur_id ");
		sql.append(" and ( fc2.check_date < comp.tally_date  or comp.IMPORT_TYPE = '4' ) ) ");
		//comp.IMPORT_TYPE = 4  未达账初始化
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and comp.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" and comp.compare_sign <> '3' group by comp.comp_acc,comp.cur_id) eb on eb.comp_acc = fc.account and eb.cur_id = fc.cur_id ")
				.append(" left join (select bank.bank_acc,bank.cur_id,count(1) as bankCnt from fc_bank_bill bank where bank.compare_sign <> '3' ");
		sql.append(" and exists (select 1  from fc_check_init fc1  where fc1.bank_acc = bank.bank_acc and bank.cur_id = fc1.cur_id " );
		sql.append(" and ( fc1.check_date < bank.tally_date  or bank.IMPORT_TYPE = '4' ) )");
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and bank.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" group by bank.bank_acc,bank.cur_id) bb on bb.bank_acc = fc.bank_acc and bb.cur_id = fc.cur_id ")
				.append(" where fc.check_type = '1' ");
		if ("0".equals(compareSign)) {
			sql.append(" and fc.check_date < ").append(compareCalDateString);
			sql.append(" and (fd.end_date is null or fd.end_date < ").append(compareCalDateString).append(")");
		} else if ("1".equals(compareSign)) {
			sql.append(" and (fc.check_date >= ").append(compareCalDateString);
			sql.append("or fd.end_date >= ").append(compareCalDateString).append(")");
		}
		if (accS != null) {
			sql.append(" and ").append(getSQLIn(accS, 900, "fc.bank_acc"));
		}
		sql.append("  ");
		return getDynamicBySQL(input, sql.toString(), FcBankCheckVO.class,"",false);
	}

	/**
	 * 功能介绍:获取银行未达账金额合计<br>
	 * <b>日期：</b>2022-6-22<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryStayOverBankSum(Date transDate, String bankAcc, String overType) {
		String compareCalDateString = new StringBuilder("bt_to_date('").append(DateUtils.date2Str(transDate,DateUtils.date_sdf())).append("','yyyy-MM-dd')").toString();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.bank_acc as bankAcc,a.cur_id as curId,BT_NVL_NUM(count(a.debit_money),0) moneyCount, BT_NVL_NUM(sum(a.debit_money),0) debitMoney,BT_NVL_NUM(sum(a.credit_money),0) creditMoney ");
		sql.append(" FROM FC_BANK_BILL a where ");
		sql.append(" exists (select 1 from fc_check_init i where i.bank_acc = a.bank_acc and i.cur_id = a.cur_id ");
		sql.append(" and i.check_date < a.tally_date) and (a.compare_sign = :compareSign ) ");
		if (CheckConstant.BANK_PAY_CTR_NO_PAY.equals(overType)) {//银行已付、中心未付
			sql.append(" and a.debit_money <> 0 and a.credit_money = 0");
		} else if (CheckConstant.BANK_REC_CTR_NO_REC.equals(overType)) {//银行已收、中心未收
			sql.append(" and a.debit_money = 0 and a.credit_money <> 0");
		}
		sql.append(" and a.bank_acc in(:bankAcc) and a.tally_date <= :transDate ");
		sql.append(" GROUP BY a.bank_acc,a.cur_id ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("transDate", compareCalDateString);
		params.put("compareSign", FinacialConstant.COMPARE_SIGN_STAY);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
		sqlQuery.addScalar("bankAcc", StandardBasicTypes.STRING);
		sqlQuery.addScalar("curId", StandardBasicTypes.STRING);
		sqlQuery.addScalar("moneyCount", StandardBasicTypes.LONG);
		sqlQuery.addScalar("debitMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("creditMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	/**
	 * 功能介绍:获取中心未达账金额合计<br>
	 * <b>日期：</b>2017年6月8日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryStayOverCtrSum(Date transDate, String account, String bankAcc, String overType) {
		String compareCalDateString = new StringBuilder("bt_to_date('").append(DateUtils.date2Str(transDate,DateUtils.date_sdf())).append("','yyyy-MM-dd')").toString();
		StringBuilder sqlSelect = new StringBuilder();
		sqlSelect.append("SELECT a.comp_acc as compAcc,a.cur_id as curId,BT_NVL_NUM(count(a.debit_money),0) moneyCount, BT_NVL_NUM(sum(a.debit_money),0) debitMoney,BT_NVL_NUM(sum(a.credit_money),0) creditMoney ");
		sqlSelect.append(" FROM FC_COMPARE_ACC a where ");
		sqlSelect.append(" exists (select 1 from fc_check_init i where i.account = a.comp_acc and i.check_date < a.tally_date ");
		sqlSelect.append(" and i.bank_acc in(:bankAcc)) and (a.compare_sign = :compareSign ) ");
		if (CheckConstant.CTR_PAY_BANK_NO_PAY.equals(overType)) {//中心已付、银行未付
			sqlSelect.append(" and a.debit_money = 0 and a.credit_money <> 0");
		} else if (CheckConstant.CTR_REC_BANK_NO_REC.equals(overType)) {//中心已收、银行未收
			sqlSelect.append(" and a.debit_money <> 0 and a.credit_money = 0");
		}
		sqlSelect.append(" and a.comp_acc in( :compAcc) and a.tally_date <= :transDate ");
		sqlSelect.append(" GROUP BY a.comp_acc,a.cur_id ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("compAcc", account);
		params.put("transDate", compareCalDateString);
		params.put("compareSign", FinacialConstant.COMPARE_SIGN_STAY);
		SQLQuery sqlQuery = createSQLQuery(sqlSelect.toString(),params);
		sqlQuery.addScalar("compAcc", StandardBasicTypes.STRING);
		sqlQuery.addScalar("curId", StandardBasicTypes.STRING);
		sqlQuery.addScalar("moneyCount", StandardBasicTypes.LONG);
		sqlQuery.addScalar("debitMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("creditMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
}
