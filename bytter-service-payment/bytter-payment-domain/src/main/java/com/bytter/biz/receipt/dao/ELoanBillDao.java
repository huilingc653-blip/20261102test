package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.ELoanBill;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.vo.ELoanBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 贷款电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:01
* @version 1.0
*
*/
@Repository
public class ELoanBillDao extends BaseDao<ELoanBill,String>{
	/**
	 * 查询贷款电子回单表
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listForUnion(DataTablesInput input,String commonSql){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT B.CORP_ID as corpId,A.BILL_NO as bill_no,A.BILL_TYPE as billType,");
		sql.append("A.LOAN_CORP as loanCorp,A.LOAN_RATE as loanRate,A.LOAN_DATE as loanDate,");
		sql.append("A.CUR_id as curId,A.DEPOSIT_ACC as depositAcc,A.loan_Type AS loanType,");
		sql.append("A.CONTRACT_CODE as contractCode, A.RETURN_DATE as returnDate,A.LOAN_MONEY as loanMoney,");
		sql.append("A.VOUCHER_NO as voucherNo,A.OLD_VOUCHER as oldVoucher,A.CREDIT_ACC as creditAcc,A.STATUS as status,");
		sql.append("A.TALLY_DATE as tallyDate,B.PRINTS as prints,a.ID AS ID,A.encry_date as encryDate");
		sql.append(" FROM E_LOAN_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id =b.corp_id");
		if(StringUtils.isNotEmpty(commonSql)){
			sql.append(commonSql);
		}
		return getDynamicBySQL(input, sql.toString(), ELoanBillVO.class, "A", false);
	}
	/**
	 * 根据单据号获取VO
	 * @param billNo 单据号
	 * @param id 流水号
	 * @return
	 */
	public ELoanBillVO getVOByBillNo(String billNo, String id){
		StringBuilder sql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		sql.append("SELECT A.ID as id, B.CORP_ID as corpId,A.BILL_NO as billNo,A.loan_Type AS loanType,");
		sql.append("A.BILL_TYPE as billType,A.LOAN_CORP as loanCorp,A.LOAN_RATE as loanRate,");
		sql.append("A.LOAN_DATE as loanDate,A.CUR_ID as curId,A.DEPOSIT_ACC as depositAcc,");
		sql.append("A.CONTRACT_CODE as contractCode,A.RETURN_DATE as returnDate,A.LOAN_MONEY as loanMoney,");
		sql.append("A.VOUCHER_NO as voucherNo,A.CREDIT_ACC as creditAcc,A.DEBIT_ACC as debitAcc,");
		sql.append("A.STATUS as status,A.TALLY_DATE as tallyDate,A.VALIDATE_CODE as validateCode,");
		sql.append("A.ENCRY_DATE as encryDate,A.SUBSCRIBER as subscriber,B.PRINTS as prints,A.ACCRUAL_DATE as accrualDate ");
		sql.append(" FROM E_LOAN_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id =b.corp_id");
		if(StringUtils.isNotEmpty(billNo)){
			sql.append(" AND A.BILL_NO =:billNo");
			params.put("billNo", billNo);
		}
		if(StringUtils.isNotEmpty(id)){
			sql.append(" AND A.id =:id");
			params.put("id", id);
		}
		List<ELoanBillVO> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillVO.class);
		if(infoList!=null&&infoList.size()>0){
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
		sql.append("fa.blnctr_acc as accNo, ");
		sql.append("fa.acc_name as accName, ");
		sql.append("fa.acc_start_date as loandate, ");
		sql.append("fa.interest_rate as loanrate, ");
		sql.append("fa.deadline_date as returndate, ");
		sql.append("fa.contract_no as contractcode ");
		sql.append("FROM fc_acc fa  ");
		sql.append("inner join sys_corp sc on fa.corp_id=sc.id ");
		sql.append("inner join fc_subject fs on fa.subject_id=fs.id ");
		sql.append("where fa.blnctr_acc in (:blnctrAcc) ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("blnctrAcc", blnctrAcc);
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
		sql.append("FROM E_LOAN_BILL bill ");
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
		StringBuilder hql=new StringBuilder("delete from ELoanBill where voucherNo=:voucherNo ");
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
		StringBuilder hql=new StringBuilder("update ELoanBill set updateBy=:updateBy ");
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
