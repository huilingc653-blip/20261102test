package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.EConsignBill;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 委托收款电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:29:50
* @version 1.0
*
*/
@Repository
public class EConsignBillDao extends BaseDao<EConsignBill,String>{
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
		sql.append("nc.id as netid, ");
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
	 * 根据凭证流水和账号获取记账信息
	 * @param orderNo 凭证流水
	 * @param accounts 内部账号
	 * @return
	 */
	public List<ELoanBillInfoVo> listAccbookBlnedAcc(String orderNo,String[] accounts){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT t.account as accNo, ");
		sql.append("SUM(t.debit_money) as debitmoney, ");
		sql.append("SUM(t.credit_money) as creditmoney, ");
		sql.append("MAX(t.summary_content) as summaryContent ");
		sql.append("FROM fc_accbook_blned_dtl t  ");
		sql.append("inner join fc_accbook_blned b on t.book_id=b.id ");
		sql.append("where t.account in (:accounts) ");
		sql.append("and b.order_no = :orderNo ");
		sql.append("group by t.account ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("accounts", accounts);
		params.put("orderNo", orderNo);
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		return infoList;
	}
	
	/**
	 * 根据条件获取满足条件的数据信息
	 * @param params
	 * @return
	 */
	public ELoanBillInfoVo getCgsBillByConsignCode(String consignCode){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT consign_date as consignDate,");
		sql.append("pact_code as contractCode,");
		sql.append("abs summaryContent ");
		sql.append("FROM cgs_bill ");
		sql.append("WHERE 1=1 ");
		sql.append("AND status <> -1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(consignCode)){
			sql.append(" and consign_code =:consignCode ");
			params.put("consignCode", consignCode);
		}
		List<ELoanBillInfoVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		if(listInfo!=null&&listInfo.size()>0){
			return listInfo.get(0);
		}
		return null;
	}
	
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
		sql.append("FROM E_CONSIGN_BILL bill ");
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
	 * 根据条件删除满足条件的数据信息
	 * @param params
	 * @return
	 */
	public int deleteELoanBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("delete from EConsignBill where voucherNo=:voucherNo ");
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
		StringBuilder hql=new StringBuilder("update EConsignBill set updateBy=:updateBy ");
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
