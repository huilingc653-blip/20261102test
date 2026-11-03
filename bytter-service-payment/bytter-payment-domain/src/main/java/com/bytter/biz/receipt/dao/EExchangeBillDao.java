package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.EExchangeBill;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 内部结售汇电子回单
* @author onlineGenerator
* @date 2017-07-04 15:30:33
* @version 1.0
*
*/
@Repository
public class EExchangeBillDao extends BaseDao<EExchangeBill,String>{
	/**
	 * 根据条件获取满足条件的数据信息
	 * @param params
	 * @return
	 */
	public ELoanBillInfoVo getPrintInfoByVoucherNo(Map<String,Object> params){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT bill.bill_no as oldbillno, ");
		sql.append("max(bill.prints) as prints, ");
		sql.append("max(print.prints) as prints2 ");
		sql.append("FROM E_EXCHANGE_BILL bill ");
		sql.append("inner join e_bill_print print on bill.bill_no=print.bill_no ");
		sql.append("where 1=1 ");
		if(params.containsKey("voucherNo")){
			sql.append(" and bill.voucher_no=:voucherNo ");
		}
		if(params.containsKey("notStatus")){
			sql.append(" and bill.status<>:notStatus ");
		}
		sql.append("group by bill.bill_no ");
		List<ELoanBillInfoVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		if(listInfo!=null&&listInfo.size()>0){
			return listInfo.get(0);
		}
		return null;
	}
	
	/**
	 * 根据内部账号获取账户信息
	 * @param blnctrAcc 内部账号
	 * @return
	 */
	public List<ELoanBillInfoVo> listFcAccByAcc(String[] blnctrAcc){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT fs.sys_subject_code as syssubjectcode, ");
		sql.append("fs.subject_name as syssubjectcodename, ");
		sql.append("fa.cur_id as cur, ");
		sql.append("sc.id as corpId, ");
		sql.append("sc.name as corpname, ");
		sql.append("nc.name as netname, ");
		sql.append("fa.blnctr_acc as accNo, ");
		sql.append("fa.acc_name as accName, ");
		sql.append("fa.acc_start_date as loandate, ");
		sql.append("fa.interest_rate as loanrate, ");
		sql.append("fa.deadline_date as returndate, ");
		sql.append("fa.contract_no as contractcode ");
		sql.append("FROM fc_acc fa  ");
		sql.append("inner join sys_corp sc on fa.corp_id=sc.id ");
		sql.append("inner join fc_subject fs on fa.subject_id=fs.id ");
		sql.append("left join sys_corp nc on sc.net_id=nc.id ");
		sql.append("where fa.blnctr_acc in (:blnctrAcc) ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("blnctrAcc", blnctrAcc);
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		return infoList;
	}
	/**
	 * 根据原凭证和付款类型获取记账明细信息
	 * @param orderNo 凭证流水
	 * @param accounts 内部账号
	 * @return
	 */
	public List<ELoanBillInfoVo> listAccbookBlnedByOrigVoucherNo(String origVoucherNo,String[] voucherTypes){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT t.account as accNo, ");
		sql.append("t.debit_money as debitmoney, ");
		sql.append("t.credit_money as creditmoney, ");
		sql.append("t.summary_content as summaryContent, ");
		sql.append("b.order_No as orderNo, ");
		sql.append("b.voucher_no as voucherNo, ");
		sql.append("b.affix_Amount as affixAmount, ");
		sql.append("b.tally_date as tallydate, ");
		sql.append("b.interest_start_date as startDate, ");
		sql.append("b.orig_voucher_no as origVoucherNo, ");
		sql.append("b.voucher_type as voucherType, ");
		sql.append("be.trade_rate as tradeRate, ");
		sql.append("bt.sys_voucher_code as voucherCode, ");
		sql.append("b.checker_code as checkerCode, ");
		sql.append("b.operator_code as operatorCode, ");
		sql.append("b.voucher_state as voucherState, ");
		sql.append("fs.subject_name as sysSubjectCodeName, ");
		sql.append("fs.sys_subject_code as syssubjectcode, ");
		sql.append("sc.id as corpid, ");
		sql.append("sc.name as corpName, ");
		sql.append("bc.id as cur, ");
		sql.append("bc.cur_name as curname  ");
		sql.append("FROM fc_accbook_blned_dtl t  ");
		sql.append("inner join fc_accbook_blned b on t.book_id=b.id ");
		sql.append("inner join fc_acc fa on t.account = fa.blnctr_acc ");
		sql.append("inner join fc_subject fs on fa.subject_id = fs.id  ");
		sql.append("inner join sys_corp sc on sc.id=fa.corp_id ");
		sql.append("inner join bt_currency bc on bc.id=fa.cur_id ");
		sql.append("inner join fc_voucher_type vt on vt.id=b.voucher_type ");
		sql.append("left join fc_accbook_blned_ext be on be.book_id=b.id ");
		sql.append("where vt.sys_voucher_code in (:voucherTypes) ");
		sql.append("and b.orig_voucher_no = :origVoucherNo ");
		sql.append("order by b.voucher_no ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("origVoucherNo", origVoucherNo);
		params.put("voucherTypes", voucherTypes);
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		return infoList;
	}
	
	
	/**
	 * 根据条件删除满足条件的数据信息
	 * @param params
	 * @return
	 */
	public int deleteELoanBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("delete from EExchangeBill where voucherNo=:voucherNo ");
		if(!params.containsKey("voucherNo")){
			return 0;
		}
		if(params.containsKey("notStatus")){
			hql.append(" and status<>:notStatus ");
		}
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 根据条件修改满足条件的数据信息
	 * @param params
	 * @return
	 */
	public int updateELoanBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update EExchangeBill set updateBy=:updateBy ");
		hql.append(",updateDate=:updateDate ");
		if(params.containsKey("status")){
			hql.append(",status=:status ");
		}
		hql.append("where voucherNo=:voucherNo ");
		if(!params.containsKey("voucherNo")){
			return 0;
		}
		if(params.containsKey("notStatus")){
			hql.append(" and status<>:notStatus ");
		}
		return this.batchExecute(hql.toString(), params);
	}
}
