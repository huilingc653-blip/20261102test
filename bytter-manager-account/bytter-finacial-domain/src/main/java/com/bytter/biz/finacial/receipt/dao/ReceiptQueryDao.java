package com.bytter.biz.finacial.receipt.dao;

import com.bytter.biz.finacial.api.vo.EAccbookBlnedVo;
import com.bytter.biz.finacial.api.vo.ELoanBillInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author zhengg
* @Title: Dao
* @Description: 票据签章处理dao
* @date 2017-08-22 17:34:11
* @version 1.0
*
*/
@SuppressWarnings("rawtypes")
@Repository
public class ReceiptQueryDao extends BaseDao<EAccbookBlnedVo, String> {
	
	/**
	 * 根据参数查询记账信息
	 * @param params
	 * @return
	 */
	public List<EAccbookBlnedVo> listAccbookBlneInfo(Map<String,Object> params){
		if(!params.containsKey("beginDate")
				||!params.containsKey("endDate")
				||!params.containsKey("netId")){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT fc.ORDER_NO as orderno, ");
		sql.append("fc.VOUCHER_TYPE as VOUCHERTYPEID, ");
		sql.append("fc.GENERATE_MODE as GENERATEMODE, ");
		sql.append("fc.OPERATOR_CODE as OPERATORCODE, ");
		sql.append("fc.CHECKER_CODE as CHECKERCODE, ");
		sql.append("fc.VOUCHER_NO as VOUCHERNO, ");
		sql.append("fc.TALLY_DATE as TALLYDATE, ");
		sql.append("ext.EXTERNAL_CORP as EXTERNALCORP, ");
		sql.append("ext.external_acc as externalacc, ");
		sql.append("ext.EXTERNAL_bank as EXTERNALbank, ");
		sql.append("fc.INTEREST_START_DATE as INTERESTSTARTDATE, ");
		sql.append("fc.AFFIX_AMOUNT as AFFIXAMOUNT, ");
		sql.append("fc.ORIG_VOUCHER_NO as ORIGVOUCHERNO, ");
		sql.append("dtl.ORIG_VOUCHER_ID as ORIGVOUCHERID, ");
		sql.append("fc.VOUCHER_STATE as VOUCHERSTATE, ");
		sql.append("fc.BUS_GLOBAL_ID as BUSGLOBALID, ");
		sql.append("fc.BUS_ORI_TYPE as BUSORITYPE, ");
		sql.append("fc.RMK ");
		sql.append(" FROM FC_ACCBOOK_BLNED fc ");
		sql.append(" left join fc_accbook_blned_ext ext on fc.id=ext.book_id ");
		sql.append(" left join fc_accbook_blned_dtl dtl on fc.id=dtl.book_id and dtl.line_no=1");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND (fc.TALLY_DATE < :endDate ");
		sql.append(" AND fc.TALLY_DATE >=:beginDate ) ");
		sql.append(" AND fc.net_id = :netId ");
		sql.append(" ORDER BY fc.ORDER_NO ");
		List<EAccbookBlnedVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, EAccbookBlnedVo.class);
		return listInfo;
	}
	/**
	 * 根据参数查询记账信息
	 * @param orderNo
	 * @return
	 */
	public EAccbookBlnedVo getEAccbookBlnedInfo(String orderNo){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT fc.ORDER_NO as orderno, ");
		sql.append("fc.VOUCHER_TYPE as VOUCHERTYPEID, ");
		sql.append("fc.GENERATE_MODE as GENERATEMODE, ");
		sql.append("fc.OPERATOR_CODE as OPERATORCODE, ");
		sql.append("fc.CHECKER_CODE as CHECKERCODE, ");
		sql.append("fc.VOUCHER_NO as VOUCHERNO, ");
		sql.append("fc.TALLY_DATE as TALLYDATE, ");
		sql.append("fc.INTEREST_START_DATE as INTERESTSTARTDATE, ");
		sql.append("fc.AFFIX_AMOUNT as AFFIXAMOUNT, ");
		sql.append("fc.ORIG_VOUCHER_NO as ORIGVOUCHERNO, ");
		sql.append("fc.VOUCHER_STATE as VOUCHERSTATE, ");
		sql.append("fc.BUS_GLOBAL_ID as BUSGLOBALID, ");
		sql.append("fc.BUS_ORI_TYPE as BUSORITYPE, ");
		sql.append("fc.RMK ");
		sql.append(" FROM FC_ACCBOOK_BLNED fc ");
		sql.append(" WHERE 1 = 1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(orderNo)){
			sql.append("and fc.order_no = :orderNo ");
			params.put("orderNo", orderNo);
		}
		sql.append("ORDER BY fc.ORDER_NO ");
		List<EAccbookBlnedVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, EAccbookBlnedVo.class);
		if(EmptyUtils.isNotEmpty(listInfo)){
			return listInfo.get(0);
		}
		return null;
	}
	
	/**
	 * 获取结售汇非冲账数据数据
	 * @param params
	 * @return
	 */
	public List<EAccbookBlnedVo> listAccbookBlneExchangeInfo(Map<String,Object> params){
//		String[] voucherTypes={NetbankConstant.PAY_TYPE_46,NetbankConstant.PAY_TYPE_47};
		if(!params.containsKey("beginDate")
				||!params.containsKey("endDate")
				||!params.containsKey("netId")){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT ");
		sql.append("fc.ORIG_VOUCHER_NO as ORIGVOUCHERNO ");
		sql.append(" FROM FC_ACCBOOK_BLNED fc ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND (fc.TALLY_DATE < :endDate ");
		sql.append(" AND fc.TALLY_DATE >=:beginDate ) ");
		sql.append(" AND fc.net_id = :netId ");
		if(StringUtils.isNotEmpty(params.get("voucherTypes"))) {
			sql.append(" AND fc.voucher_type in (:voucherTypes) ");
		}
		sql.append(" GROUP BY fc.ORIG_VOUCHER_NO ");
		@SuppressWarnings("unchecked")
		List<EAccbookBlnedVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, EAccbookBlnedVo.class);
		return listInfo;
	}
	
	/**
	 * 获取结售汇冲账数据数据
	 * @param params
	 * @return
	 */
	public List<EAccbookBlnedVo> listAccbookBlneRushExchangeInfo(Map<String,Object> params){
//		String[] voucherTypes={NetbankConstant.PAY_TYPE_46,NetbankConstant.PAY_TYPE_47};
		if(!params.containsKey("beginDate")
				||!params.containsKey("endDate")
				||!params.containsKey("netId")){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT ");
		sql.append("fab.ORIG_VOUCHER_NO as ORIGVOUCHERNO ");
		sql.append("FROM FC_ACCBOOK_BLNED fab ");
		sql.append("WHERE fab.VOUCHER_NO in ( ");
		sql.append("SELECT ");
		sql.append("fc.ORIG_VOUCHER_NO ");
		sql.append(" FROM FC_ACCBOOK_BLNED fc ");
//		sql.append(" inner join fc_voucher_type vt on vt.id=fc.voucher_type ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND (fc.TALLY_DATE < :endDate ");
		sql.append(" AND fc.TALLY_DATE >=:beginDate ) ");
		sql.append(" AND fc.RUSH_SIGN = '1' ");
		sql.append(" AND fc.net_id = :netId ");
		if(StringUtils.isNotEmpty(params.get("voucherTypes"))) {
			sql.append(" AND fc.voucher_type in (:voucherTypes) ");
		}
		sql.append(" GROUP BY fc.ORIG_VOUCHER_NO ");
		sql.append(") ");
		sql.append(" GROUP BY fab.ORIG_VOUCHER_NO ");
		@SuppressWarnings("unchecked")
		List<EAccbookBlnedVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, EAccbookBlnedVo.class);
		return listInfo;
	}
	
	/**
	 * 委托贷款利息清单数据数据
	 * @param params
	 * @return
	 */
	public List<EAccbookBlnedVo> lisInterestInfo(Map<String,Object> params){
		if(!params.containsKey("beginDate")
				||!params.containsKey("endDate")
				||!params.containsKey("netId")){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT DTL.ACCOUNT as accno, ");
		sql.append("DTL.ORDER_NO as orderno, ");
		sql.append("MIN(DTL.INTEREST_START_DATE) as INTERESTSTARTDATE, ");
		sql.append("MAX(DTL.voucher_no) as VoucherNo, ");
		sql.append("MAX(DTL.BLN_INST_DATE) as BLNINSTDATE ");
		sql.append(" FROM FC_INTEREST_DTL DTL ");
		sql.append(" inner join fc_acc ACC on acc.blnctr_acc=dtl.account ");
		sql.append(" inner join fc_subject fs on fs.id=acc.subject_id ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND ACC.ACC_TYPE =:accType ");
		sql.append(" AND fs.SYS_SUBJECT_CODE =:subjectCode ");
		sql.append(" AND (DTL.TALLY_DATE < :endDate ");
		sql.append(" AND DTL.TALLY_DATE >=:beginDate ) ");
		sql.append(" AND ACC.net_id = :netId ");
		sql.append(" GROUP BY DTL.ORDER_NO,DTL.ACCOUNT ");
		sql.append(" ORDER BY DTL.ORDER_NO,DTL.ACCOUNT ");
		@SuppressWarnings("unchecked")
		List<EAccbookBlnedVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, EAccbookBlnedVo.class);
		return listInfo;
	}
	/**
	 * 已结流水信息
	 * @param params
	 * @return
	 */
	public List<EAccbookBlnedVo> listInterestOthersInfo(String accSql,Map<String,Object> params){
		if(!params.containsKey("beginDate")
				||!params.containsKey("endDate")
				||!params.containsKey("netId")){
			return null;
		}
		StringBuilder sql=new StringBuilder();
		sql.append("select dtl.accno as accno,fc.order_no as orderNo,dtl.INTERESTSTARTDATE as interestStartDate,");
		sql.append("fc.voucher_no as voucherNo,dtl.BLNINSTDATE as  blnInstDate,fc.checker_code as checkerCode,");
		sql.append("fc.operator_code as operatorCode, fc.generate_mode as generateMode,blned.orig_voucher_no ");
		sql.append("as origVoucherNo,blned.summary_content as summaryContent,blned.orig_voucher_id as origVoucherId ");
		sql.append(" from (SELECT DTL.ACCOUNT as accno, ");
		sql.append(" (case when dtl.single_id is null or dtl.single_id='' then DTL.ORDER_NO else s.order_no end) as orderno,");
		sql.append("MIN(DTL.INTEREST_START_DATE) as interestStartDate, ");
		sql.append("MAX(DTL.voucher_no) as VoucherNo, ");
		sql.append("MAX(DTL.BLN_INST_DATE) as blnInstDate ");
		sql.append(" FROM FC_INTEREST_DTL DTL ");
		sql.append(" left join fc_interest_single s on s.id=dtl.single_id ");
//		sql.append(" inner join fc_acc ACC on acc.blnctr_acc=dtl.account ");
//		sql.append(" inner join fc_subject fs on fs.id=acc.subject_id ");
		sql.append(" WHERE DTL.INTEREST_SIGN = 1 ");
		sql.append(" AND DTL.BLN_INST_DATE <= :endDate ");
		sql.append(accSql);
		sql.append(" GROUP BY DTL.ORDER_NO,DTL.ACCOUNT,dtl.single_id,s.order_no ) dtl ,fc_accbook_blned fc,fc_accbook_blned_dtl blned ");
		sql.append(" where dtl.orderno = fc.order_no and blned.book_id=fc.id");
		sql.append(" and ( blned.summary_content like BT_CONCAT3('%',dtl.accno,'%') ");
		sql.append(" or blned.account = dtl.accno )");
		sql.append(" AND (fc.tally_date < :endDate ");
		sql.append(" AND fc.tally_date >=:beginDate ) ");
		sql.append(" ORDER BY dtl.orderno,dtl.accno ");
		List<EAccbookBlnedVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, EAccbookBlnedVo.class);
		return listInfo;
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
//		sql.append("MAX(fa.acc_type) as accTypeCode, ");
//		sql.append("MAX(fs.sys_subject_code) as syssubjectcode, ");
		sql.append("SUM(t.debit_money) as debitmoney, ");
		sql.append("SUM(t.credit_money) as creditmoney, ");
		sql.append("MAX(t.summary_content) as summaryContent ");
		sql.append(" FROM fc_accbook_blned_dtl t  ");
		sql.append(" inner join fc_accbook_blned b on t.book_id=b.id ");
//		sql.append(" inner join fc_acc fa on fa.blnctr_acc=t.account ");
//		sql.append(" inner join fc_subject fs on fa.subject_id=fs.id ");
		sql.append(" where 1=1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(accounts)){
			sql.append("and t.account in (:accounts) ");
			params.put("accounts", accounts);
		}
		if(StringUtils.isNotEmpty(orderNo)){
			sql.append("and b.order_no = :orderNo ");
			params.put("orderNo", orderNo);
		}
		sql.append("group by t.account ");
		@SuppressWarnings("unchecked")
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		return infoList;
	}

	/**
	 * 根据凭证流水和账号获取记账信息
	 * @param orderNo 凭证流水
	 * @param accounts 内部账号
	 * @return
	 */
	public List<ELoanBillInfoVo> listAccbookBlnedAcc(String orderNo,String[] accounts,boolean debitMoneyNot0,boolean creditMoneyNot0){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT t.account as accNo ");
		sql.append(" FROM fc_accbook_blned_dtl t  ");
		sql.append(" inner join fc_accbook_blned b on t.book_id=b.id ");
//		sql.append(" inner join fc_acc fa on fa.blnctr_acc=t.account ");
//		sql.append(" inner join fc_subject fs on fa.subject_id=fs.id ");
		sql.append(" where 1=1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(accounts)){
			sql.append("and t.account in (:accounts) ");
			params.put("accounts", accounts);
		}
		if(StringUtils.isNotEmpty(orderNo)){
			sql.append("and b.order_no = :orderNo ");
			params.put("orderNo", orderNo);
		}
		if(debitMoneyNot0){
			sql.append("and t.debit_money<>0 ");
		}
		if(creditMoneyNot0){
			sql.append("and t.credit_money<>0 ");
		}
		@SuppressWarnings("unchecked")
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		return infoList;
	}
	/**
	 * 根据凭证流水和账号获取记账信息
	 *
	 * @param orderNo 凭证流水
	 * @param accounts 内部账号
	 * @return hashmap
	 */
	public HashMap<String, List<ELoanBillInfoVo>> listAccbookBlnedAccToHashMap(List<String> orderNo, String[] accounts,
																			   boolean debitMoneyNot0, boolean creditMoneyNot0){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT t.account as accNo, b.order_no as orderNo ");
		sql.append(" FROM fc_accbook_blned_dtl t  ");
		sql.append(" inner join fc_accbook_blned b on t.book_id=b.id ");
		//sql.append(" inner join fc_acc fa on fa.blnctr_acc=t.account ");
		//sql.append(" inner join fc_subject fs on fa.subject_id=fs.id ");
		sql.append(" where 1=1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(accounts)){
			String accountAuthSql = SqlUtil.getInSql(accounts, "t.account", true);
			sql.append(" and ").append(accountAuthSql);
		}
		if(CollectionUtils.isNotEmpty(orderNo)){
			String orderNoAuthSql = SqlUtil.getInSql(orderNo, "b.order_no", true);
			sql.append(" and ").append(orderNoAuthSql);
		}
		if(debitMoneyNot0){
			sql.append("and t.debit_money<>0 ");
		}
		if(creditMoneyNot0){
			sql.append("and t.credit_money<>0 ");
		}
		@SuppressWarnings("unchecked")
		List<ELoanBillInfoVo> infoList = this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		HashMap<String, List<ELoanBillInfoVo>> hashMap = new HashMap<>();
		for (ELoanBillInfoVo eLoanBillInfoVo : infoList) {
			String orderNoKey = eLoanBillInfoVo.getOrderNo();
			if (hashMap.containsKey(orderNoKey)) {
				hashMap.get(orderNoKey).add(eLoanBillInfoVo);
			} else {
				hashMap.put(orderNoKey, Lists.newArrayList(eLoanBillInfoVo));
			}
		}
		return hashMap;
	}
	/**
	 * 功能介绍 : 查询指定区间内余额与内部账户余额不匹配的对账单数量<br>
	 * <b>日期：</b>2022年4月29日<br>
	 * @version 1.0
	 * @author chenf
	 */
	public BigDecimal getCountCompareBalanceInvalid(Map<String,Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(*) FROM FC_ACCBOOK_BLNED b")
				.append(" INNER JOIN FC_ACCBOOK_BLNED_DTL bd ON b.ID = bd.BOOK_ID WHERE ")
//				.append(" WHERE bd.BALANCE not in (:balance)")
				.append( getSQLNotIn(Arrays.asList((BigDecimal[])params.get("balance")), 900, "bd.BALANCE"))
				.append("  and bd.ACCOUNT in (:compAcc) ")
				.append("  and bd.LINE_NO  in (:lineNo)");
		if(StringUtils.isNotEmpty(params.get("voucherNo")) ){
			sql.append(" and b.VOUCHER_NO in (:voucherNo) ");
		}

		BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
		return count;
	}
	/**
	 * 功能介绍 : 按记账日期区间更新对账单余额<br>
	 * <b>日期：</b>2022年4月29日<br>
	 * @version 1.0
	 * @author chenf
	 */
	public List<ELoanBillInfoVo> getCompareAccBalance(Map<String,Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  bd.BALANCE as debitMoney, bd.ACCOUNT,bd.LINE_NO as billNo,b.VOUCHER_NO as voucherNo FROM FC_ACCBOOK_BLNED b")
				.append(" INNER JOIN FC_ACCBOOK_BLNED_DTL bd ON b.ID = bd.BOOK_ID WHERE")
//				.append(" WHERE bd.BALANCE not in (:balance)")
				.append( getSQLNotIn(Arrays.asList((BigDecimal[])params.get("balance")), 900, "bd.BALANCE"))
				.append("  and bd.ACCOUNT in (:compAcc) ")
				.append("  and bd.LINE_NO  in (:lineNo)");
		if(StringUtils.isNotEmpty(params.get("voucherNo")) ){
			sql.append(" and b.VOUCHER_NO in (:voucherNo) ");
		}
		List<ELoanBillInfoVo> infoList=this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
		return infoList;
	}
	/**
	 * 根据凭证流水号获取凭证明细信息和会计科目信息
	 * @param orderNo
	 * @return
	 */
	public List<ELoanBillInfoVo> listFcAccbookBlnedSubjectByOrderNo(String orderNo){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT dtl.account as account, ");
		sql.append("fab.cur_code as cur,");
		sql.append("dtl.debit_money as debitmoney, ");
		sql.append("dtl.credit_money as creditmoney, ");
//		sql.append("fs.sys_subject_code as syssubjectcode, ");
		sql.append("dtl.summary_content as summaryContent,");
		sql.append("dtl.orig_voucher_no as origVoucherNo,");
		sql.append("dtl.orig_voucher_id as origVoucherId");
//		sql.append("fa.acc_name as accname ");
		sql.append(" FROM fc_accbook_blned fab ");
		sql.append(" inner join fc_accbook_blned_dtl dtl on fab.id=dtl.book_id ");
//		sql.append("inner join fc_acc fa on dtl.account = fa.blnctr_acc ");
//		sql.append("inner join fc_subject fs on fa.subject_id = fs.id ");
		sql.append(" where fab.order_no=:orderNo");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("orderNo", orderNo);
		return this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
	}
	/**
	 * 根据全局ID获取凭证明细信息和会计科目信息
	 * @param busGlobalId
	 * @return
	 */
	public List<ELoanBillInfoVo> listFcAccbookBlnedSubjectBybusGlobalId(String busGlobalId){

		StringBuilder sql=new StringBuilder();
		sql.append("SELECT dtl.account as account, ");
		sql.append("fab.cur_code as cur,");
		sql.append("dtl.debit_money as debitmoney, ");
		sql.append("dtl.credit_money as creditmoney, ");
		sql.append("dtl.summary_content as summaryContent,");
		sql.append("dtl.orig_voucher_no as origVoucherNo,");
		sql.append("dtl.orig_voucher_id as origVoucherId");
		sql.append(" FROM fc_accbook_blned fab ");
		sql.append(" inner join fc_accbook_blned_dtl dtl on fab.id=dtl.book_id ");
		sql.append("where fab.bus_global_id=:busGlobalId ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("busGlobalId", busGlobalId);
		return this.listBySQLAliasToBean(sql.toString(), params, ELoanBillInfoVo.class);
	}
}
