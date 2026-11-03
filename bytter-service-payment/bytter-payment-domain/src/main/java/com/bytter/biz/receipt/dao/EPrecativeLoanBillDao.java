package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.EPrecativeLoanBill;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.vo.EPrecativeLoanBillVO;
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
* @Description: 委托贷款电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:40
* @version 1.0
*
*/
@Repository
public class EPrecativeLoanBillDao extends BaseDao<EPrecativeLoanBill,String>{
	/**
	 * 查询委托贷款电子回单
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listForUnion(DataTablesInput input,String commonSql){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.ID AS ID,A.BILL_NO AS billNo ,A.CORP_ID as corpId,");
		sql.append("A.BILL_TYPE as billType,A.PRECATIVE_CORP as precativeCorp,A.PRECATIVE_ACC as precativeAcc,");
		sql.append("A.LOAN_CORP as loanCorp,A.LOAN_ACC as loanAcc,A.LOAN_RATE as loanRate,");
		sql.append("A.LOAN_DATE as loanDate, A.CUR_ID as curId,A.CONTRACT_CODE as contractCode,");
		sql.append("A.REPAYMENT_DATE as repaymentDate,A.LOAN_MONEY as loanMoney,A.VOUCHER_NO as voucherNo,A.OLD_VOUCHER as oldVoucher,");
		sql.append("A.STATUS as status,A.tally_date as tallyDate,A.encry_date as encryDate,B.PRINTS as prints");
		sql.append(" FROM  E_PRECATIVE_LOAN_BILL A,E_bill_PRINT B ");
		sql.append(" WHERE A.BILL_NO = B.BILL_NO ");
		if(StringUtils.isNotEmpty(commonSql)){
			sql.append(commonSql);
		}
		return getDynamicBySQL(input, sql.toString(), EPrecativeLoanBillVO.class, "A", false);
	}
	/**
	 * 根据单据号获取VO
	 * @param billNo 单据号
	 * @param id 流水号
	 * @return
	 */
	public EPrecativeLoanBillVO getVOByBillNo(String billNo,String id){
		StringBuilder sql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		sql.append("SELECT A.ID AS ID,A.BILL_NO AS billNo ,A.CORP_ID as corpId,");
		sql.append("A.BILL_TYPE as billType,A.PRECATIVE_CORP as precativeCorp,A.PRECATIVE_ACC as precativeAcc,");
		sql.append("A.LOAN_CORP as loanCorp,A.LOAN_ACC as loanAcc,A.LOAN_RATE as loanRate,");
		sql.append("A.LOAN_DATE as loanDate, A.CUR_ID as curId,A.CONTRACT_CODE as contractCode,");
		sql.append("A.REPAYMENT_DATE as repaymentDate,A.LOAN_MONEY as loanMoney,A.VOUCHER_NO as voucherNo,");
		sql.append("A.STATUS as status,A.tally_date as tallyDate,a.tallyer_code AS tallyerCode,a.checker_code AS checkerCode,");
		sql.append("A.deadline_date as deadlineDate,A.debit_acc as debitAcc,A.credit_acc as creditAcc,");
		sql.append("a.validate_code as validateCode, a.subscriber as subscriber,a.encry_date as encryDate");
		/*sql.append(" FROM (select b.precation_corp, b.precation_acc, a.* from E_PRECATIVE_LOAN_BILL a, fc_acc b ");
		sql.append(" where a.precative_loan_acc = b.blnctr_acc) A,E_bill_PRINT B,");*/
		sql.append(" FROM  E_PRECATIVE_LOAN_BILL A,E_bill_PRINT B");
//		sql.append(" cms_precative_loan_send send,cms_precative_loan_info info");
//		sql.append(" WHERE A.BILL_NO = B.BILL_NO AND A.precative_loan_acc = send.loan_acc and send.father_code = info.bill_code ");
		sql.append(" WHERE A.BILL_NO = B.BILL_NO  ");
		if(StringUtils.isNotEmpty(billNo)){
			sql.append("AND A.BILL_NO =:billNo");
			params.put("billNo", billNo);
		}
		if(StringUtils.isNotEmpty(id)){
			sql.append("AND A.id =:id");
			params.put("id", id);
		}
		List<EPrecativeLoanBillVO> infoList=this.listBySQLAliasToBean(sql.toString(), params, EPrecativeLoanBillVO.class);
		if(infoList!=null&&infoList.size()>0){
			return infoList.get(0);
		}
		return null;
	}
	/**
	 * 根据条件获取满足条件的数据条数
	 * @param params
	 * @return
	 */
	public Integer getPrecativeLoanNum(Map<String,Object> params){
		int num=0;
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT COUNT(*) num FROM e_precative_loan_bill WHERE 1=1 ");
		if(params.containsKey("busGlobalId")){
			sql.append(" and bus_global_id=:busGlobalId ");
		}
		if(params.containsKey("notVoucherNo")){
			sql.append(" and voucher_no<>:notVoucherNo ");
		}
		if(params.containsKey("notStatus")){
			sql.append(" and status<>:notStatus ");
		}
		@SuppressWarnings("rawtypes")
		Map reInfo=this.getBySQL(sql.toString(), params);
		if(reInfo.containsKey("num")){
			num=Integer.parseInt(String.valueOf(reInfo.get("num")));
		}
		if(reInfo.containsKey("NUM")){
			num=Integer.parseInt(String.valueOf(reInfo.get("NUM")));
		}
		return num;
	}
	
	/**
	 * 根据全局ID获取凭证明细信息和会计科目信息
	 * @param busGlobalId
	 * @return
	 */
	public List<ELoanBillInfoVo> listFcAccbookBlnedSubjectBybusGlobalId(String busGlobalId){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT dtl.account, ");
		sql.append("dtl.debit_money as debitMoney, ");
		sql.append("dtl.credit_money as creditMoney, ");
		sql.append("fs.subject_name as sysSubjectCodeName, ");
		sql.append("fs.sys_subject_code as sysSubjectCode, ");
		sql.append("sc.net_id as netId, ");
		sql.append("sn.name as netName, ");
		sql.append("sc.id as corpId, ");
		sql.append("sc.name as corpName, ");
		sql.append("fa.acc_start_date as loanDate, ");
		sql.append("fa.interest_rate as loanRate, ");
		sql.append("fa.deadline_date as deadLineDate, ");
		sql.append("fa.contract_no as contractCode, ");
		sql.append("bc.id as cur, ");
		sql.append("bc.cur_name as curName  ");
		sql.append("FROM fc_accbook_blned fab ");
		sql.append("inner join fc_accbook_blned_dtl dtl on fab.id=dtl.book_id ");
		sql.append("inner join fc_acc fa on dtl.account = fa.blnctr_acc ");
		sql.append("inner join fc_subject fs on fa.subject_id = fs.id  ");
		sql.append("inner join sys_corp sc on sc.id=fa.corp_id ");
		sql.append("inner join sys_corp sn on sc.net_id=sn.id ");
		sql.append("inner join bt_currency bc on bc.id=fa.cur_id ");
		sql.append("where fab.bus_global_id=:busGlobalId ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("busGlobalId", busGlobalId);
		return this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
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
		sql.append("FROM e_precative_loan_bill bill ");
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
	public int deleteEPrecativeLoanBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("delete from EPrecativeLoanBill where voucherNo=:voucherNo ");
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
	public int updateEPrecativeLoanBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update EPrecativeLoanBill set updateBy=:updateBy ");
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
