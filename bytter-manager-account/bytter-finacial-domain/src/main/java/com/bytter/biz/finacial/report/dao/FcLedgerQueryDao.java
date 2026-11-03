package com.bytter.biz.finacial.report.dao;
import com.bytter.biz.finacial.report.vo.FcLedgerListVO;
import com.bytter.biz.finacial.report.vo.FcLedgerSearchVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public class FcLedgerQueryDao extends BaseDao<FcLedgerListVO,String>{
	
	/**
	 * 查询分户账账户信息
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public DataTablesOutput getFcLedgerHeads(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select distinct info.account,info.corpName,");//info.voucherNo,
			sql.append("info.accType,info.subjectCode,info.netId ");//,info.debitMoney,info.creditMoney,info.tallyDate,info.orderNo,info.voucherState
			//sql.append(",info.curCode,info.curName,info.accName, ");//
			sql.append(" from (");
			sql.append("select distinct dtl.account as account,corp.name as corpName,cur.id as curCode,cur.cur_name as curName,");
			sql.append("dtl.acc_name as accName,acc.acc_type as accType,book.voucher_no as voucherNo,");//,book.voucher_no as voucherNo
			sql.append("dtl.credit_money  as creditMoney,dtl.debit_money as debitMoney,");
			sql.append("acc.subject_id as subjectCode,book.net_id as netId,book.tally_date as tallyDate, ");//,book.order_no as orderNo
			sql.append("book.voucher_state as voucherState ");
            sql.append(" from fc_accbook_blned book inner join fc_accbook_blned_dtl dtl on book.id=dtl.book_id ");
            sql.append(" inner join fc_acc acc on dtl.account=acc.blnctr_acc "); // 报表查询 暂不处理
            sql.append(" inner join fc_subject sub on acc.subject_id=sub.id ");
            sql.append(" inner join sys_corp corp on book.net_id=corp.id ");
            sql.append(" left join bt_currency cur on acc.cur_id=cur.id ");
			//sql.append(" order by dtl.account");
			sql.append(" ) info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(input, FcLedgerSearchVO.class.newInstance(), "", false));
			sql.append(" order by info.account ");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return getDynamicBySQL(input,sql.toString(),FcLedgerListVO.class,null,false,DynamicallyObject.getInstance(params));
	}
	
	/**
	 * 查询分户账明细
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public DataTablesOutput getFcLedgerDtl(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select info.account,info.corpName,info.curCode,info.curName,info.accName,info.voucherNo,info.orderNo,");
			sql.append("info.accType,info.debitMoney,info.creditMoney,info.subjectCode,info.tallyDate,info.netId,info.voucherState, ");
			sql.append("info.busRaceName,info.raceid,info.voucherTypeName,info.balance,info.balanceWay,info.summaryContent ");
			sql.append("from (");
			sql.append("select distinct dtl.account as account,corp.name as corpName,cur.id as curCode,cur.cur_name as curName,");
			sql.append("dtl.acc_name as accName,book.voucher_no as voucherNo,acc.acc_type as accType,");
			sql.append("acc.subject_id as subjectCode,book.tally_date as tallyDate,book.net_id as netId, ");
			sql.append("race.id as raceId,bt_nvl(race.race_name,'') as busRaceName,bt_nvl(voucherType.Voucher_Name,'') as voucherTypeName,  ");
			sql.append("dtl.summary_content as summaryContent,dtl.credit_money as creditMoney, ");
			sql.append("dtl.debit_money as debitMoney, ");
			sql.append("dtl.balance as balance,acc.balance_way as balanceWay,book.order_no as orderNo, ");
			sql.append("book.voucher_state as voucherState ");
            sql.append(" from fc_accbook_blned book inner join fc_accbook_blned_dtl dtl on book.id=dtl.book_id ");
            sql.append(" inner join fc_acc acc on dtl.account=acc.blnctr_acc "); // 报表查询 暂不处理
            sql.append(" inner join fc_subject sub on acc.subject_id=sub.id ");
            sql.append(" inner join sys_corp corp on book.net_id=corp.id ");
            sql.append(" left join fc_voucher_type voucherType on voucherType.id = book.voucher_type ");
            sql.append(" left join fc_bus_race race on book.bus_race_id = race.id ");
            sql.append(" left join bt_currency cur on acc.cur_id=cur.id ");
			//sql.append(" order by book.tally_date,book.voucher_no ");
			sql.append(" ) info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(input, FcLedgerSearchVO.class.newInstance(), "", false));
			sql.append(" order by info.tallyDate,info.voucherNo");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return getDynamicBySQL(input,sql.toString(),FcLedgerListVO.class,null,false,DynamicallyObject.getInstance(params));
	}
	
	/**
	 * 统计分户账明细总数
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public Map<String,Object> queryFcLedgerDtlNum(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select count(*) as TATOLNUM from( ");
			sql.append("select info.account,info.corpName,info.curCode,info.curName,info.accName,info.voucherNo,info.orderNo,");
			sql.append("info.accType,info.debitMoney,info.creditMoney,info.subjectCode,info.tallyDate,info.netId,info.voucherState, ");
			sql.append("info.busRaceName,info.voucherTypeName,info.balance,info.balanceWay ");
			sql.append("from (");
			sql.append("select distinct dtl.account as account,corp.name as corpName,cur.id as curCode,cur.cur_name as curName,");
			sql.append("dtl.acc_name as accName,book.voucher_no as voucherNo,acc.acc_type as accType,");
			sql.append("acc.subject_id as subjectCode,book.tally_date as tallyDate,book.net_id as netId, ");
			sql.append("race.race_name as busRaceName,voucherType.Voucher_Name as voucherTypeName, ");
			sql.append("dtl.summary_content as summaryContent,dtl.credit_money as creditMoney, ");
			sql.append("dtl.debit_money as debitMoney, ");
			sql.append("dtl.balance as balance,acc.balance_way as balanceWay,book.order_no as orderNo, ");
			sql.append("book.voucher_state as voucherState ");
            sql.append(" from fc_accbook_blned book inner join fc_accbook_blned_dtl dtl on book.id=dtl.book_id ");
            sql.append(" inner join fc_acc acc on dtl.account=acc.blnctr_acc "); // 报表查询 暂不处理
            sql.append(" inner join fc_subject sub on acc.subject_id=sub.id ");
            sql.append(" inner join sys_corp corp on book.net_id=corp.id ");
            sql.append(" left join fc_voucher_type voucherType on voucherType.id = book.voucher_type ");
            sql.append(" left join fc_bus_race race on book.bus_race_id = race.id ");
            sql.append(" left join bt_currency cur on acc.cur_id=cur.id ");
			//sql.append(" order by book.tally_date,book.voucher_no ");
			sql.append(" ) info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(input, FcLedgerSearchVO.class.newInstance(), "", false));
			sql.append(" ) main_info where 1=1 ");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return getBySQL(sql.toString(),params);
	}
	/**
	 * 导出-查询分户账账户信息
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public List<FcLedgerListVO> excelFcLedgerHeads(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select info.account,info.corpName,info.curCode,info.curName,info.accName,");//info.voucherNo,
			sql.append("info.accType,info.subjectCode,info.netId, ");//info.debitMoney,info.creditMoney,info.tallyDate,info.orderNo
			sql.append("info.voucherState from (");
			sql.append("select distinct dtl.account as account,corp.name as corpName,cur.id as curCode,cur.cur_name as curName,");
			sql.append("dtl.acc_name as accName,acc.acc_type as accType,book.voucher_no as voucherNo,");
			sql.append("dtl.credit_money  as creditMoney,dtl.debit_money as debitMoney,");
			sql.append("acc.subject_id as subjectCode,book.net_id as netId,book.tally_date as tallyDate,book.order_no as orderNo, ");
			sql.append("book.voucher_state as voucherState ");
			sql.append("from fc_accbook_blned book,fc_accbook_blned_dtl dtl,fc_acc acc,sys_corp corp,bt_currency cur,fc_subject sub "); // 报表查询 暂不处理
			sql.append("where book.id=dtl.book_id and dtl.account=acc.blnctr_acc and book.net_id=corp.id ");
			sql.append("and book.cur_code=cur.id and acc.subject_id=sub.id  ");
			//sql.append(" order by dtl.account");
			sql.append(" ) info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(input, FcLedgerSearchVO.class.newInstance(), "", false));
			sql.append(" order by info.account ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBySQLAliasToBean(sql.toString(),params,FcLedgerListVO.class);
	}
	/**
	 * 导出-分户账明细
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public List<FcLedgerListVO> excelFcLedgerDtl(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select info.account,info.corpName,info.curCode,info.curName,info.accName,info.voucherNo,info.orderNo,");
			sql.append("info.accType,info.debitMoney,info.creditMoney,info.subjectCode,info.tallyDate,info.netId,info.voucherState, ");
			sql.append("info.busRaceName,info.raceid,info.voucherTypeName,info.balance,info.balanceWay,info.summaryContent ");
			sql.append("from (");
			sql.append("select distinct dtl.account as account,corp.name as corpName,cur.id as curCode,cur.cur_name as curName,");
			sql.append("dtl.acc_name as accName,book.voucher_no as voucherNo,acc.acc_type as accType,");
			sql.append("acc.subject_id as subjectCode,book.tally_date as tallyDate,book.net_id as netId, ");
			sql.append("race.id as raceId,race.race_name as busRaceName,voucherType.Voucher_Name as voucherTypeName, ");
			sql.append("dtl.summary_content as summaryContent,dtl.credit_money as creditMoney, ");
			sql.append("dtl.debit_money as debitMoney, ");
			sql.append("dtl.balance as balance,acc.balance_way as balanceWay,book.order_no as orderNo, ");
			sql.append("book.voucher_state as voucherState ");
			sql.append("from fc_accbook_blned book,fc_accbook_blned_dtl dtl,fc_voucher_type voucherType,");
			sql.append("fc_bus_race race,fc_acc acc,sys_corp corp,bt_currency cur,fc_subject sub "); // 报表查询 暂不处理
			sql.append("where book.id=dtl.book_id and dtl.account=acc.blnctr_acc ");
			sql.append("and voucherType.id=book.voucher_type and book.bus_race_id = race.id ");
			sql.append("and book.net_id=corp.id and book.cur_code=cur.id and acc.subject_id=sub.id  ");
			//sql.append(" order by book.tally_date,book.voucher_no ");
			sql.append(" ) info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.account"));
			sql.append(getWhereAllCondition(input, FcLedgerSearchVO.class.newInstance(), "", false));
			sql.append(" order by info.account,info.tallyDate,info.voucherNo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBySQLAliasToBean(sql.toString(),params,FcLedgerListVO.class);
	}
}
