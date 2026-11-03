package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.EAccrualBill;
import com.bytter.biz.receipt.vo.EAccrualBillVO;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 利息清单电子回单
* @author onlineGenerator
* @date 2017-07-04 15:27:13
* @version 1.0
*
*/
@Repository
public class EAccrualBillDao extends BaseDao<EAccrualBill,String>{
	/**
	 * 查询利息清单电子回单
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<EAccrualBillVO> listForUnion(DataTablesInput input,String commonSql){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.ID as id,A.CORP_ID AS corpId,A.BILL_NO as billNo,A.BILL_TYPE as billType,");
		sql.append("A.ACC_NO as accNO,A.ACC_NAME as accName,A.CUR_ID as curId,");
		sql.append("A.START_DATE as startDate,A.END_DATE as endDate,A.ACCRUAL as accrual,");
		sql.append("A.CUMU as cumu,A.INTEREST as interest,A.DAYS as days,");
		sql.append("A.OTHER_SUBJECT as otherSubject,A.HOME_SUBJECT as homeSubject,");
		sql.append("A.RMK as rmk,A.checker_Code as checkerCode,A.STATUS as status,");
		sql.append("B.PRINTS as prints,A.TALLY_DATE as tallyDate,A.encry_date as encryDate,a.voucher_No as voucherNo ");
		sql.append(" from e_accrual_bill A,E_BILL_PRINT B WHERE A.BILL_NO=B.BILL_NO and a.corp_id =b.corp_id and A.accrual <> 0");
		if(StringUtils.isNotEmpty(commonSql)){
			sql.append(commonSql);
		}
		return getDynamicBySQL(input, sql.toString(), EAccrualBillVO.class, "A", false);
	}
	/**
	 * 根据单据号获取VO
	 * @param billNo 单据号
	 * @param id 流水号
	 * @return
	 */
	public EAccrualBillVO getVOByBillNo(String billNo,String id){
		StringBuilder sql=new StringBuilder();
		Map<String,Object> params=new HashMap<String,Object>();
		sql.append("SELECT a.corp_id as corpId,A.id as id,A.BILL_NO as billNo,A.BILL_TYPE as billType,A.ACC_no as accNO, A.ACC_NAME as accName,");
		sql.append("A.CUR_id as curId,A.START_DATE as startDate,A.END_DATE as endDate, A.ACCRUAL as accrual,");
		sql.append("A.CUMU as cumu, A.INTEREST as Interest,A.DAYS as days,A.OTHER_SUBJECT as otherSubject,");
		sql.append("A.HOME_SUBJECT as homeSubject,A.RMK as rmk,A.SUBSCRIBER as subscriber,A.VALIDATE_CODE as validateCode,");
		sql.append("A.STATUS as status,A.MONEY as money, A.NUMMONEY as nummoney, A.ENCRY_DATE as encryDate,");
		sql.append("A.TALLY_DATE as tallyDate,B.PRINTS as prints,A.CHECKER_code as checkerCode,");
		sql.append("A.precation_acc as precationAcc,a.contract_code as contractCode,a.voucher_No as voucherNo");
//		sql.append(" FROM (select b.precation_corp,(case when b.precation_acc is null then c.deposit_acc else b.precation_acc end) as precation_acc,");
//		sql.append("c.contract_code, a.* from e_accrual_bill a left join fc_acc b on a.acc_no = b.blnctr_acc");
//		sql.append(" left join cms_precative_loan_send c on a.acc_no = c.loan_acc) A,");
		sql.append(" FROM (select '' precation_corp,'' as precation_acc,");
		sql.append("'' contract_code, a.* from e_accrual_bill a ) A,");
		sql.append("E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO ");
		if(StringUtils.isNotEmpty(billNo)){
			sql.append("AND A.BILL_NO =:billNo");
			params.put("billNo", billNo);
		}
		if(StringUtils.isNotEmpty(id)){
			sql.append("AND A.id =:id");
			params.put("id", id);
		}
		List<EAccrualBillVO> infoList=this.listBySQLAliasToBean(sql.toString(), params, EAccrualBillVO.class);
		if(infoList!=null&&infoList.size()>0){
			return infoList.get(0);
		}
		return null;
	}
	/**
	 * 根据凭证流水号获取利息明细信息
	 * @param orderNo 流水号
	 * @return
	 */
	public List<ELoanBillInfoVo> listAccrualsInfoByOrderNo(String orderNo){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT dtl.interest_time as tallydate, ");
		sql.append("dtl.account as accno, ");
		sql.append("dtl.interest_start_date as startdate, ");
		sql.append("dtl.bln_inst_date as enddate, ");
		sql.append("dtl.interest, ");
		sql.append("dtl.cumulation as cumu, ");
		sql.append("dtl.interest_rate AS rate, ");
		sql.append("dtl.days, ");
		sql.append("dtl.repay_money as repayMoney, ");
		sql.append("dtl.id as interestdtlid, ");
		sql.append("dtl.interest_sign as interestsign, ");
		sql.append("sc.id as corpId, ");
		sql.append("fa.acc_name as accname, ");
		sql.append("fa.acc_type as accTypeCode, ");
		sql.append("fa.predraw_sign as predrawsign, ");
		sql.append("bc.id as cur, ");
		sql.append("bc.cur_name as curName ");
		sql.append("FROM fc_interest_dtl dtl ");
		sql.append("inner join fc_acc fa on fa.blnctr_acc=dtl.account ");
		sql.append("inner join sys_corp sc on fa.corp_id=sc.id ");
		sql.append("inner join bt_currency bc on bc.id=fa.cur_id ");
		sql.append("WHERE dtl.order_no =:orderNo ");
		sql.append("ORDER BY dtl.account ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orderNo", orderNo);
		return this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
	}
	/**
	 * 根据凭证流水号和账号获取利息信息
	 * @param orderNo 流水号
	 * @param accNo 账号
	 * @return
	 */
	public List<ELoanBillInfoVo> listInterestInfoByOrderNoAndAccNo(String orderNo,String accNo){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT DTL.Tally_Date as tallydate, ");
		sql.append("DTL.ACCOUNT as accno, ");
		sql.append("DTL.ORDER_NO as orderno, ");
		sql.append("SUM(DTL.INTEREST) AS interest, ");
		sql.append("SUM(DTL.CUMULATION) as cumu, ");
		sql.append("MAX(DTL.INTEREST_RATE) AS rate, ");
		sql.append("SUM(DTL.DAYS) AS days, ");
		sql.append("SUM(DTL.REPAY_MONEY) as repayMoney, ");
		sql.append("SUM(BT_NVL_NUM(DTL.REPAY_MONEY, 0)) as money, ");
		sql.append("MAX(DTL.id) as interestdtlid ");
		sql.append("FROM fc_interest_dtl DTL left join fc_interest_single s on s.id = dtl.single_id ");
		sql.append("WHERE DTL.ACCOUNT=:accNo ");
		sql.append(" and (((dtl.single_id is null or dtl.single_id = '') and DTL.ORDER_NO = :orderNo)");
		sql.append(" or (dtl.single_id is not null and s.order_no = :orderNo))  ");
		sql.append("GROUP BY DTL.Tally_Date,DTL.ACCOUNT,DTL.ORDER_NO ");
		Map<String,Object> params=new HashMap<String,Object>();
		//params.put("voucherNo", voucherNo);
		params.put("orderNo", orderNo);
		params.put("accNo", accNo);
		return this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
	}
	
	/**
	 * 根据账号获取放款金额
	 * @param accNo 账号
	 * @return
	 */
	public ELoanBillInfoVo getloanSendMoneyByAccNo(String accNo){
		StringBuilder sql=new StringBuilder();
		sql.append("select SEND.MONEY as money ");
		sql.append("FROM CMS_PRECATIVE_LOAN_SEND SEND ");
		sql.append("where SEND.LOAN_ACC=:accNo ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("accNo", accNo);
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		if(infoList!=null&&infoList.size()>0){
			return infoList.get(0);
		}
		return null;
	}
	/**
	 * 根据账号获取还款金额
	 * @param accNo 账号
	 * @return
	 */
	public ELoanBillInfoVo getloanRepayMoneyByAccNo(String accNo){
		StringBuilder sql=new StringBuilder();
		sql.append("select bt_nvl_num(sum(rep.repay_money),0) as money ");
		sql.append("FROM CMS_PRECATIVE_LOAN_REPAY rep ");
		sql.append("inner join CMS_PRECATIVE_LOAN_SEND send on rep.send_code=send.id ");
		sql.append("where SEND.LOAN_ACC=:accNo ");
		sql.append("and rep.REPAY_TYPE <> '4' ");
		sql.append("and rep.status>=102 ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("accNo", accNo);
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		if(infoList!=null&&infoList.size()>0){
			return infoList.get(0);
		}
		return null;
	}
	
	/**
	 * 根据内部账号获取账户信息
	 * @param accNo 内部账号
	 * @return
	 */
	public ELoanBillInfoVo listFcAccByAccNo(String accNo){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT fs.sys_subject_code as syssubjectcode,");
		sql.append("fs.subject_name as syssubjectcodename, ");
		sql.append("sc.id as corpId, ");
		sql.append("sc.name as corpname, ");
		sql.append("fa.blnctr_acc as accNo, ");
		sql.append("fa.acc_name as accName, ");
		sql.append("fa.acc_type as accTypeCode, ");
		sql.append("fa.predraw_sign as predrawsign, ");
		sql.append("fa.cur_id as cur ");
		sql.append("FROM fc_acc fa  ");
		sql.append("inner join sys_corp sc on fa.corp_id=sc.id ");
		sql.append("inner join fc_subject fs on fa.subject_id=fs.id ");
		sql.append("where fa.blnctr_acc=:accNo ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("accNo", accNo);
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		if(infoList!=null&&infoList.size()>0){
			return infoList.get(0);
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
		sql.append("FROM E_ACCRUAL_BILL bill ");
		sql.append("inner join e_bill_print print on bill.bill_no=print.bill_no ");
		sql.append("where 1=1 ");
		if(params.containsKey("voucherNo")){
			sql.append(" and bill.voucher_no=:voucherNo ");
		}
		if(params.containsKey("interestDtlId")){
			sql.append(" and bill.INTEREST_DTL_ID=:interestDtlId ");
		}
		if(params.containsKey("accNo")){
			sql.append(" and bill.ACC_NO=:accNo ");
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
	public int deleteEAccrualBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("delete from EAccrualBill where voucherNo= :voucherNo and accNo= :accNo");
		if(!params.containsKey("voucherNo")){
			return 0;
		}
		if(!params.containsKey("accNo")){
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
	public int updateEAccrualBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update EAccrualBill set updateBy=:updateBy ");
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
	/**
	 * 查询定期存款利息和通知存款利息清单电子回单
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<EAccrualBillVO> listDepositInterest(DataTablesInput input,String commonSql){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.ID as id,A.CORP_ID AS corpId,A.BILL_NO as billNo,A.BILL_TYPE as billType,");
		sql.append("A.ACC_NO as accNO,A.ACC_NAME as accName,A.CUR_ID as curId,");
		sql.append("A.START_DATE as startDate,A.END_DATE as endDate,A.ACCRUAL as accrual,");
		sql.append("A.CUMU as cumu,A.INTEREST as interest,A.DAYS as days,");
		sql.append("A.OTHER_SUBJECT as otherSubject,A.HOME_SUBJECT as homeSubject,");
		sql.append("A.RMK as rmk,A.STATUS as status,");
		sql.append("  a.money as money,  a.summary_content as summaryContent,");
		sql.append(" a.tallier_code as tallierCode,a.checker_code as checkerCode,");
		sql.append("b.PRINTS as prints,A.TALLY_DATE as tallyDate,A.encry_date as encryDate,a.voucher_No as voucherNo, ");
		sql.append(" a.orig_voucher_no as origVoucherNo,a.orig_voucher_id as origVoucherId ");
		sql.append(" from e_accrual_bill A ");
		sql.append(" left join E_BILL_PRINT b on A.BILL_NO = b.BILL_NO and a.corp_id = b.corp_id  ");
		sql.append(" where 1=1 ");
		sql.append(" and A.accrual <> 0 ");//过滤掉利息为0的数据
		if(StringUtils.isNotEmpty(commonSql)){
			sql.append(commonSql);
		}
		return getDynamicBySQL(input, sql.toString(), EAccrualBillVO.class, "A", false);
	}
	/**
	 * 获取贷款利息单的本金
	 * @param account
	 * @param startDate
	 * @return
	 */
	public BigDecimal getMoney(String account, Date startDate){
		BigDecimal money = new BigDecimal(0);
		//取开始时间不减一天
//		Date oldDate = DateUtils.addDay(startDate,-1);
		String sql = " select balance from fc_acc_balance where account= :account and  balance_date = :startDate ";
		Map<String,Object> params = new HashMap<>();
		params.put("account",account);
		params.put("startDate",startDate);
//		params.put("oldDate",startDate);
		Map periodMoneyMap = this.getBySQL(sql,params);
		BigDecimal periodMoney = new BigDecimal(0);
		if(!EmptyUtils.isEmpty(periodMoneyMap)) {
			periodMoney = (BigDecimal) periodMoneyMap.get("BALANCE");
		}
		sql = " select sum(repay_money) as repayMoney from cms_loan_repay  where loan_acc = :account and repay_date <= :startDate ";
		BigDecimal repayMoney = new BigDecimal(0);
		Map repayMoneyMap = this.getBySQL(sql,params);
		if(!EmptyUtils.isEmpty(repayMoneyMap)) {
			repayMoney = (BigDecimal) repayMoneyMap.get("REPAYMONEY");
		}
		//加上期初金额
		if(!EmptyUtils.isEmpty(periodMoney)){
			money = money.add(periodMoney);
		}
		//减去已还款金额
		if(!EmptyUtils.isEmpty(repayMoney)){
			money = money.subtract(repayMoney);
		}
		return money;
	}
	/**
	 * 根据凭证流水获取记账信息
	 * @param orderNo 凭证流水
	 * @return
	 */
	public List<ELoanBillInfoVo> listAccbookBlned(String orderNo){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT t.account as accNo, ");
		sql.append("SUM(t.debit_money) as debitmoney, ");
		sql.append("SUM(t.credit_money) as creditmoney, ");
		sql.append("MAX(t.summary_content) as summaryContent ");
		sql.append("FROM fc_accbook_blned_dtl t  ");
		sql.append("inner join fc_accbook_blned b on t.book_id=b.id ");
		sql.append("where b.order_no = :orderNo ");
		sql.append("group by t.account ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orderNo", orderNo);
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		return infoList;
	}
}
