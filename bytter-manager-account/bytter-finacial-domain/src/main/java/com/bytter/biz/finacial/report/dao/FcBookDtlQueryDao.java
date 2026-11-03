package com.bytter.biz.finacial.report.dao;
import com.bytter.biz.finacial.accbook.vo.VFcAccbookAndBlnedsVO;
import com.bytter.biz.finacial.report.vo.FcBookDtlListVO;
import com.bytter.biz.finacial.report.vo.FcBookDtlSearchVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public class FcBookDtlQueryDao extends BaseDao<VFcAccbookAndBlnedsVO,String>{
	/**
	 * 查询账户信息
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public DataTablesOutput queryAccbookDtlHeads(DataTablesInput input, List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		params.remove("letallyDate");
		params.remove("getallyDate");
		try {
			sql.append(" select distinct info.account,info.accName,info.netId ");
			//sql.append(",info.curName,info.curCode,info.subjectId,info.subjectName,info.netName,info.netId ");
			//sql.append(",info.voucherState ");// info.tallyDate,info.voucherNo,info.debitMoney,info.creditMoney,
			sql.append(" from (");
			sql.append(" select distinct dtl.account as account,acc.acc_name as accName,cur.cur_name as curName,cur.id as curCode,");
			sql.append(" acc.subject_id as subjectId,sub.subject_name as subjectName,corp.name as netName,corp.id as netId ");
			sql.append(" ,book.tally_date as tallyDate,book.voucher_no as voucherNo,dtl.debit_money as debitMoney,dtl.credit_money as creditMoney ");
			sql.append(" ,book.voucher_state as voucherState ");
			sql.append(" from fc_accbook_blned book inner join fc_accbook_blned_dtl dtl on book.id=dtl.book_id ");
			sql.append(" inner join fc_acc acc on dtl.account=acc.blnctr_acc ");// 报表查询 暂不处理
			sql.append(" inner join fc_subject sub on acc.subject_id=sub.id ");// 报表查询 暂不处理
			sql.append(" inner join sys_corp corp on book.net_id=corp.id ");// 报表查询 暂不处理
			sql.append(" left join bt_currency cur on acc.cur_id=cur.id where 1 = 1 ");// 报表查询 暂不处理
			String gedTallyDate = (String)input.getFormdata().get("GEd_tallyDate");
			String ledTallyDate = (String)input.getFormdata().get("LEd_tallyDate");
			if(StringUtils.isNotEmpty(gedTallyDate)){
				sql.append("  and book.tally_Date >= to_date('"+gedTallyDate+"','yyyy-MM-dd') ");
			}
			if(StringUtils.isNotEmpty(ledTallyDate)){
				sql.append("  and book.tally_Date <= to_date('"+ledTallyDate+"','yyyy-MM-dd') ");
			}
			sql.append(" ) info where 1=1 ");
			input.getFormdata().remove("GEd_tallyDate");
			input.getFormdata().remove("LEd_tallyDate");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(input, FcBookDtlSearchVO.class.newInstance(), "", false));
			sql.append(" order by info.account");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getDynamicBySQL(input, sql.toString(), FcBookDtlListVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	
	/**
	 * 查询当前帐号明细
	 * @param input
	 * @return
	 */
	public DataTablesOutput queryAccbookDtlDatas(DataTablesInput input){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append(getDtlSql());
			sql.append(getWhereAllCondition(input, FcBookDtlSearchVO.class.newInstance(), "", false));
			sql.append(" order by info.voucherNo,info.lineNo");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return getDynamicBySQL(input, sql.toString(), FcBookDtlListVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	
	/**
	 * 统计明细数量
	 * @param input
	 * @return
	 */
	public Map<String,Object> queryAccbookDtlTatol(DataTablesInput input){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select count(*) as TATOLNUM from( ");
			sql.append(getDtlSql());
			sql.append(getWhereAllCondition(input, FcBookDtlSearchVO.class.newInstance(), "", false));
			sql.append(" order by info.voucherNo,info.lineNo");
			sql.append(" ) main_info where 1=1 ");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return getBySQL(sql.toString(),params);
	}
	
	/**
	 * 明细sql
	 * @return
	 */
	public String getDtlSql(){
		StringBuilder sql=new StringBuilder();
		sql.append("select info.id,info.tallyDate,info.voucherNo,info.voucherName,info.summaryContent,info.debitMoney, ");
		sql.append("info.creditMoney,info.balanceway,info.balance,info.orderNo,info.lineNo,info.account,");
		sql.append("info.netId,info.voucherState,info.curCode,info.subjectId,info.accName,info.curName,info.subjectName ");
		sql.append(" from (");
		sql.append("select book.id as id,book.tally_date as tallyDate,book.voucher_no as voucherNo,vou.voucher_name as voucherName,dtl.line_no as lineNo,");
		sql.append("dtl.summary_content as summaryContent,dtl.debit_money as debitMoney,dtl.credit_money as creditMoney,dtl.account as account,");
		sql.append("case when dtl.debit_money <> 0  then '借' else '贷' end balanceway, ");
		sql.append(" dtl.balance as balance,book.order_no as orderNo, ");
		sql.append("book.net_id as netId,book.voucher_state as voucherState,cur.id as curCode, acc.subject_id as subjectId, ");
		sql.append("acc.acc_name as accName,cur.cur_name as curName,sub.subject_name as subjectName ");
		sql.append("from fc_accbook_blned book join fc_accbook_blned_dtl dtl on book.id=dtl.book_id ");
		sql.append(" inner join fc_acc acc on dtl.account=acc.blnctr_acc "); // 报表查询 暂不处理
		sql.append(" inner join fc_subject sub on acc.subject_id=sub.id ");// 报表查询 暂不处理
		sql.append(" inner join sys_corp corp on book.net_id=corp.id ");// 报表查询 暂不处理
		sql.append(" left join bt_currency cur on acc.cur_id=cur.id ");// 报表查询 暂不处理
		sql.append(" left join fc_voucher_type vou on book.voucher_type=vou.id");
		sql.append(" ) info where 1=1 ");
		return sql.toString();
	}
	/**
	 * 导出查询
	 */
	public List<FcBookDtlListVO> listExcel(DataTablesInput input,List<String> accounts){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append(getDtlSql());
			sql.append(getWhereAllCondition(input, FcBookDtlSearchVO.class.newInstance(), "", false));
			if(!EmptyUtils.isEmptyList(accounts)){
				sql.append(" and ").append(getSQLIn(accounts,900,"info.account"));
			}
			sql.append(" order by info.account,info.tallyDate,info.voucherNo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBySQLAliasToBean(sql.toString(), params, FcBookDtlListVO.class);
	}
	/**
	 * 导出-查询账户信息
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public  List<FcBookDtlListVO> excelAccbookDtlHeads(DataTablesInput input, List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append(" select distinct info.account,info.accName,info.curName,info.curCode,info.subjectId,info.subjectName,info.netName,info.netId ");
			sql.append(" from (");
			sql.append(" select distinct dtl.account as account,acc.acc_name as accName,cur.cur_name as curName,cur.id as curCode,");
			sql.append(" acc.subject_id as subjectId,sub.subject_name as subjectName,corp.name as netName,corp.id as netId  ");
			sql.append(" from fc_accbook_blned book inner join fc_accbook_blned_dtl dtl on book.id=dtl.book_id "); // fc_accbook_blned、fc_accbook_blned_dtl
			sql.append(" inner join fc_acc acc on dtl.account=acc.blnctr_acc "); // 报表查询 暂不处理
			sql.append(" inner join fc_subject sub on acc.subject_id=sub.id ");// 报表查询 暂不处理
			sql.append(" inner join sys_corp corp on book.net_id=corp.id ");// 报表查询 暂不处理
			sql.append(" inner join bt_currency cur on book.cur_code=cur.id ) info where 1=1 "); // 报表查询 暂不处理
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(" order by info.curCode,info.account");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBySQLAliasToBean(sql.toString(), params, FcBookDtlListVO.class);
	}
}
