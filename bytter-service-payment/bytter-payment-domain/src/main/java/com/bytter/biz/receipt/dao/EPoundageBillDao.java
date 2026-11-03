package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.EPoundageBill;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.vo.EPoundageBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 手续费电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:13
* @version 1.0
*
*/
@Repository
public class EPoundageBillDao extends BaseDao<EPoundageBill,String>{
	/**
	 * 查询通知单电子回单
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listForUnion(DataTablesInput input, String commonSql){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.ID as id,A.BILL_NO as billNo,A.CORP_ID as corpId,A.PAYER_NAME as payerName,A.PAYER_ACC as payerAcc,");
		sql.append("A.PAYER_BANK as payerBank,A.PAYEE_NAME as payeeName,A.PAYEE_ACC as payeeAcc,");
		sql.append("A.PAYEE_BANK as payeeBank, A.CUR_id as curId,A.PAY_MONEY as payMoney, A.PURPOSE as purpose,");
		sql.append("A.ACCRUAL_DATE as accrualDate,A.PAY_TYPE as payType, A.VOUCHER_NO as voucherNo,");
		sql.append("A.STATUS as status,A.BILL_TYPE as billType, A.tally_date as tallyDate,A.encry_date as encryDate,B.PRINTS as prints");
		sql.append(" FROM E_POUNDAGE_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id=b.corp_id ");
		if(StringUtils.isNotEmpty(commonSql)){
			sql.append(commonSql);
		}
		return getDynamicBySQL(input, sql.toString(), EPoundageBillVO.class, "A", false);
	}
	/**
	 * 根据单据号获取VO
	 * @param billNo 单据号
	 * @param id 流水号
	 * @return
	 */
	public EPoundageBillVO getVOByBillNo(String billNo, String id){
		StringBuilder sql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		sql.append("SELECT A.ID AS id,A.BILL_NO AS billNo,A.CORP_ID as corpId,A.PAYER_NAME as payerName,");
		sql.append("A.PAYER_ACC as payerAcc,A.PAYER_BANK as payerBank,A.PAYEE_NAME as payeeName,");
		sql.append("A.PAYEE_ACC as payeeAcc,A.PAYEE_BANK as payeeBank,A.CUR_Id as curId,");
		sql.append("A.PAY_MONEY as payMoney,A.PURPOSE as purpose,A.ACCRUAL_DATE as accrualDate,");
		sql.append("A.PAY_TYPE as payType,a.checker_code as checkerCode,A.VOUCHER_NO as voucherNo,");
		sql.append("A.OLD_VOUCHER as oldVoucher, A.OLD_VOUCHER_MONEY as oldVoucherMoney,");
		sql.append("A.SUBSCRIBER as subscriber,A.STATUS as status,A.BILL_TYPE as billType,");
		sql.append("A.tallier_code as tallierCode,A.CREDIT as credit,A.DEBIT as debit,");
		sql.append("A.ENCRY_DATE as encryDate, A.TALLY_DATE as tallyDate, A.VALIDATE_CODE as validateCode,");
		sql.append("A.ACCESSORY as accessory, B.PRINTS as prints");
		sql.append(" FROM E_POUNDAGE_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id=b.corp_id ");
		if(StringUtils.isNotEmpty(billNo)){
			sql.append("AND A.BILL_NO =:billNo");
			params.put("billNo", billNo);
		}
		if(StringUtils.isNotEmpty(id)){
			sql.append("AND A.id =:id");
			params.put("id", id);
		}
		List<EPoundageBillVO> infoList=this.listBySQLAliasToBean(sql.toString(), params, EPoundageBillVO.class);
		if(EmptyUtils.isNotEmpty(infoList)){
			return infoList.get(0);
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
		sql.append("MAX(fv.voucher_name) as voucherName, ");
		sql.append("MAX(t.summary_content) as summaryContent ");
		sql.append("FROM fc_accbook_blned_dtl t  ");
		sql.append("inner join fc_accbook_blned b on t.book_id=b.id ");
		sql.append("left join fc_voucher_type fv on fv.id=b.voucher_type ");
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
	public ELoanBillInfoVo getPrintInfoByVoucherNo(Map<String,Object> params){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT bill.bill_no as oldbillno, ");
		sql.append("max(bill.prints) as prints, ");
		sql.append("max(print.prints) as prints2 ");
		sql.append("FROM e_poundage_bill bill ");
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
		StringBuilder hql=new StringBuilder("delete from EPoundageBill where voucherNo=:voucherNo ");
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
		StringBuilder hql=new StringBuilder("update EPoundageBill set updateBy=:updateBy ");
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
