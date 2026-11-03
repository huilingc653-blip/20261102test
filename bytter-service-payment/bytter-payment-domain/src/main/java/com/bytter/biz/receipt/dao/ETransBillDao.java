package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.ETransBill;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.vo.ETransBillVO;
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
* @Description: 账户转换电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:19
* @version 1.0
*
*/
@Repository
public class ETransBillDao extends BaseDao<ETransBill,String>{

	/**
	 * 根据内部账号获取账户信息
	 * @param blnctrAcc 内部账号
	 * @return
	 */
	public List<ELoanBillInfoVo> listFcAccByAcc2(String[] blnctrAcc){
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT fa.blnctr_acc as account, ");
		sql.append("fa.interest_start_date as startdate, ");
		sql.append("fa.deadline_date as enddate, ");
		sql.append("fa.notify_type as notifytype, ");
		sql.append("fa.interest_rate as rate, ");
		sql.append("sc.id as corpid ");
		sql.append("FROM fc_acc fa ");
		sql.append("inner join sys_corp sc on sc.id=fa.corp_id ");
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
		sql.append("FROM e_trans_bill bill ");
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
	public int deleteETransBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("delete from ETransBill where voucherNo=:voucherNo ");
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
	public int updateETransBill(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update ETransBill set updateBy=:updateBy ");
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
	 * 获取定期存单数据
	 * @param input
	 * @return
	 */
	public DataTablesOutput datatables(DataTablesInput input,String commonSql){
		StringBuilder sql=new StringBuilder();
		sql.append("select a.id  as id,")
				.append(" a.bill_no  as billNo,")
				.append(" a.corp_id     as corpId,")
				.append(" a.bill_type   as billType,")
				.append(" a.cur_id      as curId,")
				.append(" a.notify_type as notifyType,")
				.append(" a.acc_no as accNo,")
				.append(" a.acc_name as accName, ")
				.append(" a.current_acc as currentAcc,")
				.append("a.current_acc_name as currentAccName,")
				.append("a.money as money,")
				.append("a.tally_date as tallyDate,")
				.append("b.prints as prints,")
				.append("a.summary_content as summaryContent,")
				.append("a.voucher_no as voucherNo,")
				.append("a.orig_voucher_no as origVoucherNo,")
				.append("a.rate as rate, ")
				.append("a.start_date as startDate, ")
				.append(" a.end_date as endDate,")
				.append("  a.subscriber as subscriber, ")
				.append(" a.tallier_code as tallierCode, ")
				.append("  a.checker_code as checkerCode,")
				.append(" a.ENCRY_DATE as encryDate")
//				.append(" c.id  as depositRateType, ")
//				.append("c.period_name as depositRateName, ")
//				.append(" c.period  as period,")
//				.append(" c.period_cycle  as periodCycle ")
				.append(" from E_TRANS_BILL a ")
				.append(" left join E_BILL_PRINT b on A.BILL_NO = b.BILL_NO and a.corp_id = b.corp_id  ")
//				.append("left join CHO_INNER_FINANCE d ")
//				.append("on a.orig_voucher_id=d.id ")
//				.append("left join TB_CREDIT_PERIOD c ")
//				.append("on d.deposit_rate_type = c.id ")
				.append("  where 1=1 ");
		if(StringUtils.isNotEmpty(commonSql)){
			sql.append(commonSql);
		}
		return getDynamicBySQL(input, sql.toString(), ETransBillVO.class, "a", false);
	}
}
