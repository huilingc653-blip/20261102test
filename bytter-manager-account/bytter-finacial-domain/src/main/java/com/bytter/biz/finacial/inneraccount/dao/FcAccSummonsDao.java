package com.bytter.biz.finacial.inneraccount.dao;

import com.bytter.biz.finacial.api.vo.FcAccSummonsVO;
import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FcAccSummonsDao extends BaseDao<FcAccSummonsVO, String>{
//	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
		
		StringBuilder sql = new StringBuilder();
		StringBuilder orderVoucher = new StringBuilder(" MAX(b.debit_money) debit, MAX(b.credit_money ) credit,a.order_no,a.voucher_type as pay_type");
		StringBuilder whereJoin = new StringBuilder(" where a.id = b.book_id and a.voucher_state <> 'B' and a.voucher_no is not null");
		sql.append("select voucher_no as voucherNo, account,debit_money AS debitMoney, credit_money AS creditMoney,tally_date as tallyDate,sum_money as sumMoney,summary_content as summaryContent,");
		sql.append(" debit, credit, order_no as orderNo,pay_type as payType ");
		sql.append(" from ( ");
//		private BigDecimal debit; MAX(b.debit_money) debit, MAX(b.credit_money ) credit,
//		private BigDecimal credit;
		sql.append("select a.voucher_no,b.account,b.debit_money,b.credit_money,a.tally_date,max(b.debit_money + b.credit_money) as sum_money,max(b.summary_content) as summary_content,");
		sql.append(orderVoucher.toString());
		sql.append(" from fc_accbook a, fc_accbook_dtl b");
		sql.append(whereJoin.toString());
		sql.append(" group by a.voucher_no,b.account, a.tally_date, a.order_no, a.voucher_type,b.debit_money,b.credit_money");
		sql.append(" union all ");
		sql.append(" select a.voucher_no,b.account,b.debit_money,b.credit_money,a.tally_date,max(b.debit_money + b.credit_money) as sum_money,b.summary_content,");
		sql.append(orderVoucher.toString());
		sql.append(" from fc_accbook_blned a,fc_accbook_blned_dtl b");
		sql.append(whereJoin.toString());
		sql.append(" group by a.voucher_no,b.account,a.tally_date,b.summary_content, a.order_no,a.voucher_type,b.debit_money,b.credit_money");
		sql.append(" ) cc where 1=1 ");
		List<String> accList = (List<String>)dataTablesInput.getFormdata().get("ins_account");
		if(null != accList && accList.size()>0){
			sql.append(" and "+ SqlUtil.getInSql(accList,"account",true));
		}
		//前台过滤查询字段拼接及排序字段拼接
		String[] needParams = {"voucherType","voucherNo","voucherId","summaryContent","tallyDate","sumMoney"};
		Map map = super.getWhereConditionAndValues(dataTablesInput, new FcAccSummonsVO(), "", false, needParams);
		Map<String, Object> params = (Map<String, Object>)map.get("value");
		sql.append(map.get("where"));
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, FcAccSummonsVO.class);
	}
	/**
	 * 根据条件得到凭证详细信息
	 * @param vou
	 * @return
	 * 上游没有被调用，注释
	 */
	public List<VoucherInfoVO> getInfo(VoucherInfoVO vou){
		StringBuilder sql = new StringBuilder();
		sql.append("select order_no as orderNo,voucher_no as voucherNo,tally_date as tallyDate,enrol_time as enrolTime,");
		sql.append(" interest_start_date as interestStartDate,voucher_type as voucherType,orig_voucher_no as origVoucherNo,bus_ori_type as busOriType,emergency_sign as emergencySign,");
		sql.append(" external_corp as externalName, external_acc as externalAcc, external_bank as bank, external_prov as province, external_city as city,");
		sql.append(" operator_code as operatorCode,checker_code as checkerCode,affix_amount as affixAmount,redblue_sign as redblueSign,");
		sql.append(" voucher_state as voucherState,line_no as lineNo,summary_content as summaryContent,account as account,acc_name as accname,");
		sql.append(" price as price,amount as amount,debit_money as debitMoney,credit_money as creditMoney,balance as balance,cash_flux as cashFlux,orig_voucher_id as origVoucherId,");
		sql.append(" rmk as rmk,manageName from (");
		sql.append(" select a.order_no,a.voucher_no,a.tally_date,a.enrol_time,a.interest_start_date,a.voucher_type,");
		sql.append(" (case when a.generate_mode='R' then a.voucher_no else a.orig_voucher_no end) as orig_voucher_no,a.bus_ori_type,a.emergency_sign,");
		sql.append(" ext.external_corp, ext.external_acc, ext.external_bank, ext.external_prov, ext.external_city,");
		sql.append(" a.operator_code,a.checker_code,a.affix_amount,a.redblue_sign,a.voucher_state,b.line_no,b.summary_content,b.account,b.acc_name,b.price,b.amount,");
		sql.append(" b.debit_money,b.credit_money,b.balance,b.cash_flux,b.orig_voucher_id, a.rmk,'' manageName");
		sql.append(" from fc_accbook a left join fc_accbook_ext ext on a.id = ext.book_id, fc_accbook_dtl b");
		sql.append(" where a.id = b.book_id ");
		
		sql.append(" union ");
		
		sql.append(" select a.order_no,a.voucher_no,a.tally_date,a.enrol_time,a.interest_start_date,a.voucher_type, ");
		sql.append(" (case when a.generate_mode='R' then a.voucher_no else a.orig_voucher_no end) as orig_voucher_no,a.bus_ori_type,a.emergency_sign,");
		sql.append(" ext.external_corp, ext.external_acc, ext.external_bank, ext.external_prov, ext.external_city,");
		sql.append(" a.operator_code, a.checker_code,a.affix_amount,");
		sql.append(" a.redblue_sign,a.voucher_state, b.line_no, b.summary_content, b.account, b.acc_name, b.price, b.amount,");
		sql.append(" b.debit_money, b.credit_money, b.balance, b.cash_flux, b.orig_voucher_id,a.rmk,'1' manageName ");
		sql.append(" from fc_accbook_blned a left join fc_accbook_blned_ext ext on a.id = ext.book_id, fc_accbook_blned_dtl b");
		sql.append(" where a.id = b.book_id ) c ");
		Map params = new HashMap();
		if(StringUtils.isNotBlank(vou.getOrderNo())){
			sql.append( " WHERE (order_no =:orderNo ) AND (voucher_no is not null or voucher_no<>'') AND (voucher_state<>'B')");
			params.put("orderNo",vou.getOrderNo());
		}
		sql.append("  order by order_no,abs(debit_money) desc ");
		return listBySQLAliasToBean(sql.toString(), params,VoucherInfoVO.class);
	}
}
