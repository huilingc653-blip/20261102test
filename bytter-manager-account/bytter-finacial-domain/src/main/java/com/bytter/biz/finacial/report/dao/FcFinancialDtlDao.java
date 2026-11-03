package com.bytter.biz.finacial.report.dao;
import com.bytter.biz.finacial.accbook.vo.FcAccbookSearchVO;
import com.bytter.biz.finacial.accbook.vo.VFcAccbookAndBlnedsVO;
import com.bytter.biz.finacial.report.vo.VFcAccbookSearchVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public class FcFinancialDtlDao extends BaseDao<VFcAccbookAndBlnedsVO,String> {
	
	private static final String fcAccBook="fc_accbook";
	private static final String fcAccBookDtl="fc_accbook_dtl";
	
	/**
	 * 功能介绍 : 流水账明细查询。<br>
	 * <b>日期：</b>2018年1月8日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public DataTablesOutput listOutputData(DataTablesInput input, List<String> corpAuth){
		Map<String, Object> params = input.getSearchValues();
		StringBuilder sql = new StringBuilder();
        try {
        	sql.append(getAccbookHeadSql()).append(" from (");
        	sql.append(getAccbookAndBlnedSql());
        	sql.append(getAccbookSql(input,FcFinancialDtlDao.fcAccBook,FcFinancialDtlDao.fcAccBookDtl, corpAuth));
        	sql.append(" ) fab where 1=1 ");
			sql.append(getWhereAllCondition(input, VFcAccbookSearchVO.class.newInstance(), "", false));
			sql.append(" order by fab.tallyDate desc,fab.voucherNo desc,fab.orderNo desc");
		} catch (Exception e) {
			e.printStackTrace();
		}
        return getDynamicBySQL(input, sql.toString(), VFcAccbookAndBlnedsVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	
	/**
	 * 功能介绍 : 明细列表。<br>
	 * <b>日期：</b>2018年1月8日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public String getAccbookHeadSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select fab.voucherState as voucherState,fab.voucherName as voucherName,fab.tallyDate as tallyDate,");
		sql.append("fab.interestStartDate as interestStartDate,fab.voucherNo as voucherNo,fab.orderNo as orderNo,fab.origVoucherNo as origVoucherNo,");
		sql.append("fab.busGlobalId as busGlobalId,fab.id as id,fab.checkerCode as checkerCode,fab.operatorCode as operatorCode,");
		sql.append("fab.creditMoney as creditMoney,fab.rmk as rmk,fab.debitMoney as debitMoney,fab.raceName as raceName,fab.busOriType as busOriType,");
		sql.append("fab.emergencySign as emergencySign,fab.curCode as curCode,fab.raceId as raceId ");
		return sql.toString();
	}
	
	/**
	 * 功能介绍 : 明细列表。<br>
	 * <b>日期：</b>2018年1月8日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public String getAccbookAndBlnedSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select voucher_state as voucherState,voucher_name as voucherName,tally_date as tallyDate,");
		sql.append("interest_start_date as interestStartDate,voucher_no as voucherNo,order_no as orderNo,orig_voucher_no as origVoucherNo,");
		sql.append("bus_global_id as busGlobalId,main_info.id as id,se.login_name as checkerCode,su.login_name as operatorCode,");
		sql.append("creditMoney as creditMoney,summary as rmk,debitMoney as debitMoney,race_name as raceName,bus_ori_type as busOriType,");
		sql.append("emergency_sign as emergencySign,cur_code as curCode,race.id as raceId ");
		return sql.toString();
	}
	
	/**
	 * 功能介绍 : 流水账查询。<br>
	 * <b>日期：</b>2018年1月8日<br>
	 * @version 1.0
	 * @author yangl
	 * TODO 如果是报表，先不处理
	 */
	public String getAccbookSql(DataTablesInput input,String dataTable,String dataTableDtl, List<String> corpAuth) {
		StringBuilder sql = new StringBuilder();
		try {
			sql.append(" from (");
			sql.append("select accbook.voucher_state ,voucherType.voucher_name,accbook.tally_date,accbook.interest_start_date,");
			sql.append("accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,accbook.checker_code,accbook.orig_voucher_no,");
			sql.append("accbook.operator_code,accbook.bus_race_id,sum(case when accbook.redblue_sign='");
			sql.append(FinacialConstant.RB_SIGN_R);
			sql.append("' then 0-dtl.credit_money else dtl.credit_money end) as creditMoney,max(dtl.summary_content) as summary, ");
			sql.append("sum(case when accbook.redblue_sign='");
			sql.append(FinacialConstant.RB_SIGN_R);
			sql.append("' then 0-dtl.debit_money else dtl.debit_money end) as debitMoney, ");
			sql.append(" accbook.bus_ori_type,accbook.emergency_sign,accbook.cur_code,accbook.net_id ");
			sql.append("from "+dataTable+" accbook,fc_voucher_type voucherType,fc_acc acc, "+dataTableDtl+" dtl,fc_bus_race race,("); //报表，暂不处理
			sql.append("select distinct a.book_id,a.net_id from (");
			sql.append("select dtl.book_id,acc.net_id from "+dataTableDtl+"");
			sql.append(" dtl ,fc_acc acc where dtl.account=acc.blnctr_acc and dtl.debit_money<>0 "); //报表，暂不处理
			sql.append(") a,(");
			sql.append("select dtl.book_id from "+dataTableDtl+"");
			sql.append(" dtl ,fc_acc acc where dtl.account=acc.blnctr_acc and dtl.credit_money<>0 "); //报表，暂不处理
			sql.append(") b where a.book_id=b.book_id");
			sql.append(") dtl_info where voucherType.id=accbook.voucher_type and dtl_info.book_id=accbook.id ");
			sql.append(" and dtl.book_id = dtl_info.book_id  and dtl.account = acc.blnctr_acc and accbook.bus_race_id = race.id ");
			sql.append(getWhereAllCondition(input, FcAccbookSearchVO.class.newInstance(), "", false));
			sql.append(" group by accbook.voucher_state,voucherType.voucher_name,accbook.tally_date,accbook.operator_code,accbook.checker_code,");
			sql.append("accbook.bus_race_id,accbook.bus_ori_type,accbook.orig_voucher_no,");
			sql.append("accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id, ");
			sql.append(" accbook.emergency_sign,accbook.cur_code,accbook.net_id) main_info");
			sql.append(" left join fc_bus_race race on  race.id = main_info.bus_race_id ");
			sql.append("left join sys_user su on su.id=main_info.operator_code "); // 报表，暂不处理
			sql.append("left join sys_user se on se.id=main_info.checker_code "); // 报表，暂不处理
			sql.append("left join sys_corp sc on sc.id=main_info.net_id where 1 = 1 ");
			if (!EmptyUtils.isEmptyList(corpAuth)) {
				sql.append(" and ").append(getSQLIn(corpAuth,900,"main_info.net_id"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	/**
	 * 功能介绍 : 导出查询。<br>
	 */
	public List<VFcAccbookAndBlnedsVO> listExcel(DataTablesInput input, List<String> corpAuth){
		Map<String, Object> param = input.getSearchValues();
		StringBuilder sql = new StringBuilder();
		try {
			sql.append(getAccbookHeadSql()).append(" from (");
			sql.append(getAccbookAndBlnedSql());
			sql.append(getAccbookSql(input,FcFinancialDtlDao.fcAccBook,FcFinancialDtlDao.fcAccBookDtl, corpAuth));
			sql.append(" ) fab where 1=1 ");
			sql.append(getWhereAllCondition(input, VFcAccbookSearchVO.class.newInstance(), "", false));
			sql.append(" order by fab.tallyDate desc,fab.voucherNo desc,fab.orderNo desc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listBySQLAliasToBean(sql.toString(), param, VFcAccbookAndBlnedsVO.class);
	}
	
}
