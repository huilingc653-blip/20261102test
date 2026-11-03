package com.bytter.biz.inneraccount.dao;

import com.bytter.biz.inneraccount.vo.FcAccSummonsVO;
import com.bytter.biz.inneraccount.vo.VoucherInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FcAccSummonsDao extends BaseDao<FcAccSummonsVO, String>{

	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		
		StringBuilder sql = new StringBuilder();
		String corpId = dataTablesInput.getPara("corpId");
		String where = "";
		if(StringUtils.isNotBlank(corpId)){
			String[] corpIdTemp = corpId.split(",");
			List list = new ArrayList();
			for (int i = 0; i < corpIdTemp.length; i++) {
				list.add(corpIdTemp[i]);
			}
			where = SqlUtil.getInSql(list,"fc_acc.corp_id",true);
		}
		StringBuilder debitTemp = new StringBuilder(" max(case when b.debit_money <> 0 then s.sys_subject_code end) debit,");
		StringBuilder creditTemp = new StringBuilder(" max(case when b.credit_money <> 0 then s.sys_subject_code end) credit,");
		StringBuilder subjectCode = new StringBuilder(" and (s.sys_subject_code is not null or s.sys_subject_code <> '') ");
		StringBuilder orderVoucher = new StringBuilder(" a.order_no,a.voucher_type as pay_type");
		StringBuilder whereJoin = new StringBuilder(" where a.id = b.book_id and b.account = c.blnctr_acc and c.subject_id = s.id and a.voucher_state <> 'B' and a.voucher_no is not null");

		sql.append("select voucher_no as voucherNo,tally_date as tallyDate,sum_money as sumMoney,summary_content as summaryContent,cc.voucher_type as voucherType,");
		sql.append(" order_no as orderNo,pay_type as payType,d.id as voucherId,voucher_name as voucherName, case when cc.voucher_type = 10 or cc.voucher_type = 20 or ");
		sql.append(" cc.voucher_type = 30 then 0 else 1 end as checkboxHide ");
		sql.append(" from ( select voucher_no,tally_date,sum_money,summary_content,");
		sql.append(" case when (debit = '1010' or debit = '1020') and credit = '2120' then '20'");
		sql.append(" when debit = '2120' and (credit = '1010' or credit = '1020') then '10'");
		sql.append(" when (debit = '2120' or debit = '5212') and credit = '2120' then '30'");
		sql.append(" when debit = '2120' and credit like '213%' then '40'");
		sql.append(" when debit = '2120' and credit like '214%' then '50' end voucher_type,");
		sql.append(" order_no,pay_type from ( ");
		sql.append("select a.voucher_no,a.tally_date,max(b.debit_money + b.credit_money) as sum_money,max(b.summary_content) as summary_content,");
		sql.append(debitTemp.toString());
		sql.append(creditTemp.toString());
		sql.append(orderVoucher.toString());
		sql.append(" from fc_accbook a, fc_accbook_dtl b, fc_acc c, fc_subject s");
		sql.append(whereJoin.toString());
		sql.append(subjectCode.toString());
//		sql.append(" and a.generate_mode = 'D'");
		sql.append(" and a.id in (select book_id from fc_accbook_dtl, fc_acc where account = blnctr_acc ");
		if(StringUtils.isNotBlank(corpId)){
			sql.append(" and " + where +" )");
		}else{
			sql.append(" )");
		}
		sql.append(" group by a.voucher_no,a.tally_date,a.order_no,a.voucher_type");

		sql.append(" union all ");

		sql.append(" select a.voucher_no,a.tally_date,max(b.debit_money + b.credit_money) as sum_money,b.summary_content,");
		sql.append(debitTemp.toString());
		sql.append(creditTemp.toString());
		sql.append(orderVoucher.toString());
		sql.append(" from fc_accbook_blned a,fc_accbook_blned_dtl b,fc_acc c,fc_subject s");
		sql.append(whereJoin.toString());
		sql.append(subjectCode.toString());
//		sql.append(" and a.generate_mode = 'D'");
		sql.append(" and a.id in (select book_id from fc_accbook_blned_dtl, fc_acc where account = blnctr_acc ");
		if(StringUtils.isNotBlank(corpId)){
			sql.append(" and " + where +" )");
		}else{
			sql.append(" )");
		}
		sql.append(" group by a.voucher_no,a.tally_date,b.summary_content,a.order_no,a.voucher_type");
		sql.append(" ) bb ) cc");
		sql.append(" left join fc_voucher_type d on cc.pay_type = d.id where 1 = 1");

		//前台过滤查询字段拼接及排序字段拼接
		String[] needParams = {"voucherType","voucherNo","voucherId","summaryContent","tallyDate","sumMoney","voucherName"};
		Map map = super.getWhereConditionAndValues(dataTablesInput, new FcAccSummonsVO(), "", false, needParams);
		Map<String, Object> params = (Map<String, Object>)map.get("value");
		sql.append(map.get("where"));
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, FcAccSummonsVO.class);
	}

	/**
	 * 根据条件得到凭证详细信息
	 * @param vou
	 * @return
	 */
	public List<VoucherInfoVO> getInfo(VoucherInfoVO vou){
		StringBuilder sql = new StringBuilder();
		sql.append("select order_no as orderNo,voucher_no as voucherNo,tally_date as tallyDate,enrol_time as enrolTime,");
		sql.append(" interest_start_date as interestStartDate,voucher_type as voucherType,orig_voucher_no as origVoucherNo,bus_ori_type as busOriType,emergency_sign as emergencySign,");
		sql.append(" external_corp as externalName, external_acc as externalAcc, external_bank as bank, external_prov as province, external_city as city,");
		sql.append(" operator_code as operatorCode,opName as opName,checker_code as checkerCode,checkName as checkName,affix_amount as affixAmount,redblue_sign as redblueSign,");
		sql.append(" voucher_state as voucherState,line_no as lineNo,summary_content as summaryContent,account as account,acc_name as accname,");
		sql.append(" price as price,amount as amount,debit_money as debitMoney,credit_money as creditMoney,balance as balance,cash_flux as cashFlux,orig_voucher_id as origVoucherId,");
		sql.append(" cur_id as curId,cur_name as curName,subject_id as subjectId,subject_name as subjectName,corp_id as corpId,corpName as corpName,code as code,net_id as netId,netName as netName,type as type,");
		sql.append(" balance_way as balanceWay,voucher_name as voucherName,sys_voucher_code as sysVoucherCode,rmk as rmk,manageName as manageName from (");
		sql.append(" select a.order_no,a.voucher_no,a.tally_date,a.enrol_time,a.interest_start_date,a.voucher_type,");
		sql.append(" (case when a.generate_mode='R' then a.voucher_no else a.orig_voucher_no end) as orig_voucher_no,a.bus_ori_type,a.emergency_sign,");
		sql.append(" ext.external_corp, ext.external_acc, ext.external_bank, ext.external_prov, ext.external_city,");
		sql.append(" a.operator_code,g.username as opName,a.checker_code,h.username as checkName,a.affix_amount,a.redblue_sign,a.voucher_state,b.line_no,b.summary_content,b.account,b.acc_name,b.price,b.amount,");
		sql.append(" b.debit_money,b.credit_money,b.balance,b.cash_flux,b.orig_voucher_id, c.cur_id,d.cur_name,c.subject_id,f.subject_name,c.corp_id,s.name as corpName,");
		sql.append(" s.code,c.net_id,j.name as  netName,s.type,c.balance_way,e.voucher_name,e.sys_voucher_code,a.rmk, '' as manageName ");
		sql.append(" from fc_accbook a left join fc_accbook_ext ext on a.id = ext.book_id, fc_accbook_dtl b, fc_acc c,sys_corp s,");
		sql.append(" bt_currency d, fc_voucher_type e, fc_subject f, sys_user g, sys_user h,sys_corp j");
		sql.append(" where a.id = b.book_id and b.account = c.blnctr_acc and c.corp_id = s.id and c.net_id = j.id and c.cur_id = d.id and a.voucher_type = e.id");
		sql.append(" and a.operator_code = g.id and a.checker_code = h.id and c.subject_id = f.id");
		
		sql.append(" union ");
		
		sql.append(" select a.order_no,a.voucher_no,a.tally_date,a.enrol_time,a.interest_start_date,a.voucher_type, ");
		sql.append(" (case when a.generate_mode='R' then a.voucher_no else a.orig_voucher_no end) as orig_voucher_no,a.bus_ori_type,a.emergency_sign,");
		sql.append(" ext.external_corp, ext.external_acc, ext.external_bank, ext.external_prov, ext.external_city,");
		sql.append(" a.operator_code, g.username as opName,a.checker_code,h.username as checkName,a.affix_amount,");
		sql.append(" a.redblue_sign,a.voucher_state, b.line_no, b.summary_content, b.account, b.acc_name, b.price, b.amount,");
		sql.append(" b.debit_money, b.credit_money, b.balance, b.cash_flux, b.orig_voucher_id, c.cur_id,d.cur_name, c.subject_id, f.subject_name, c.corp_id,s.name as corpName,");
		sql.append(" s.code, c.net_id, j.name as netName, s.type, c.balance_way, e.voucher_name, e.sys_voucher_code,a.rmk, ");
		sql.append("  (select u.username from fc_carry c left join sys_user u on c.operator_code = u.id where c.carry_type = 'D' and c.restore_carry_optor is null  ");
		sql.append(" and c.voucher_start_date <= a.TALLY_DATE  and c.voucher_end_date >= a.TALLY_DATE ");
		sql.append(" and c.net_id = a.net_id) as manageName ");
		sql.append(" from fc_accbook_blned a left join fc_accbook_blned_ext ext on a.id = ext.book_id, fc_accbook_blned_dtl b, fc_acc c, sys_corp s,");
		sql.append(" bt_currency d, fc_voucher_type e, fc_subject f, sys_user g, sys_user h,sys_corp j");
		sql.append(" where a.id = b.book_id and b.account = c.blnctr_acc and c.corp_id = s.id and c.net_id = j.id and c.cur_id = d.id and a.voucher_type = e.id");
		sql.append(" and a.operator_code = g.id and a.checker_code = h.id and c.subject_id = f.id) c ");
		Map params = new HashMap();
		if(StringUtils.isNotBlank(vou.getOrderNo())){
			sql.append( " WHERE (order_no =:orderNo ) AND (voucher_no is not null or voucher_no<>'') AND (voucher_state<>'B')");
			params.put("orderNo",vou.getOrderNo());
		}
		sql.append("  order by order_no,abs(debit_money) desc ");
		return listBySQLAliasToBean(sql.toString(), params,VoucherInfoVO.class);
	}
	
	/**
	 * 根据内部账号查找凭证的银行信息
	 * @param account
	 * @return
	 */
	public List getBank(String account){
		String sql = "select acc.bank_acc, acc.bank_name from bt_bank_acc acc left join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id where cur.counter_acc = :counterAcc";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("counterAcc", account);
		return listBySQL(sql, params);
	}
}
