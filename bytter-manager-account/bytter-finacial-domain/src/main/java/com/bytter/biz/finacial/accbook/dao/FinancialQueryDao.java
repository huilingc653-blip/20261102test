package com.bytter.biz.finacial.accbook.dao;
import com.bytter.biz.finacial.accbook.vo.FcAccbookSearchVO;
import com.bytter.biz.finacial.accbook.vo.VFcAccbookAndBlnedsVO;
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
public class FinancialQueryDao extends BaseDao<VFcAccbookAndBlnedsVO,String> {
	
	private static final String fcAccBook="fc_accbook";
	private static final String fcAccBookDtl="fc_accbook_dtl";
	private static final String fcAccBookBlned="fc_accbook_blned";
	private static final String fcAccBookBlnedDtl="fc_accbook_blned_dtl";
	
	/**
	 * 功能介绍 : 记账凭证查询。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * 1.复核员代码：checkerCode -- 有字段，但没有值 -- 需要复核，才会录入
	 * 2.凭证号：voucherNo -- 有字段，但是没有值 -- 凭证复核通过后才会写入
	 * @version 1.0
	 * @author yangl
	 */
	public DataTablesOutput listOutputData(DataTablesInput input, String corpAuthSql){
 		Map<String, Object> params = input.getSearchValues();
		StringBuilder sql = new StringBuilder();
		//1.复核员代码：checkerCode -- 有字段，但没有值-- 需要复核，才会录入
		//2.凭证号：voucherNo，有字段，但是没有值 -- 凭证复核通过后才会写入
		sql.append(getAccbookHeadSql()).append(" from (");
		sql.append(getAccbookAndBlnedSql()); //这里使用了 su\se
        sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBook,FinancialQueryDao.fcAccBookDtl, corpAuthSql,false, params));
        sql.append(" union ");
        sql.append(getAccbookAndBlnedSql());
        sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBookBlned,FinancialQueryDao.fcAccBookBlnedDtl, corpAuthSql,false, params));
        sql.append(" ) fab order by fab.tallyDate desc,fab.voucherNo desc,fab.orderNo desc ");
        return getDynamicBySQL(input, sql.toString(), VFcAccbookAndBlnedsVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	/**
	 * 待设置现金流项目列表
	 * @param input
	 * @param corpAuthSql
	 * @return
	 */
	public DataTablesOutput listUpdateCashItemDataTable(DataTablesInput input, String corpAuthSql){
		Map<String, Object> params = input.getSearchValues();
		StringBuilder sql = new StringBuilder();
		sql.append(getAccbookHeadSql()).append(" from (");
		sql.append(getAccbookAndBlnedSql());
		sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBook,FinancialQueryDao.fcAccBookDtl, corpAuthSql,false, params));
		sql.append(" union ");
		sql.append(getAccbookAndBlnedSql());
		sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBookBlned,FinancialQueryDao.fcAccBookBlnedDtl, corpAuthSql,false, params));
		sql.append(" ) fab order by fab.tallyDate desc,fab.voucherNo desc,fab.orderNo desc ");
		return getDynamicBySQL(input, sql.toString(), VFcAccbookAndBlnedsVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	/**
	 * 功能介绍 : 凭证列表。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public String getAccbookHeadSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select fab.voucherState as voucherState,fab.voucherType as voucherType,fab.tallyDate as tallyDate, fab.checkerCode as checkerCode, fab.operatorCode as operatorCode,");
//		sql.append("select fab.voucherState as voucherState,fab.voucherName as voucherName,fab.tallyDate as tallyDate,");
		sql.append("fab.interestStartDate as interestStartDate,fab.voucherNo as voucherNo,fab.orderNo as orderNo,fab.origVoucherNo as origVoucherNo,");
//		sql.append("fab.busGlobalId as busGlobalId,fab.id as id,fab.checkerCode as checkerCode,fab.operatorCode as operatorCode,");
		sql.append("fab.busGlobalId as busGlobalId,fab.id as id,");
		sql.append("fab.creditMoney as creditMoney,fab.rmk as rmk,fab.debitMoney as debitMoney,fab.raceName as raceName, fab.raceId as raceId,fab.busOriType as busOriType,");
		sql.append("fab.emergencySign as emergencySign,fab.curCode as curCode ");
		return sql.toString();
	}
	
	/**
	 * 功能介绍 : 凭证列表。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public String getAccbookAndBlnedSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select voucher_state as voucherState,voucher_type as voucherType, tally_date as tallyDate,");
//		sql.append("select voucher_state as voucherState,voucher_name as voucherName,tally_date as tallyDate,");
		sql.append("interest_start_date as interestStartDate,voucher_no as voucherNo,order_no as orderNo,orig_voucher_no as origVoucherNo,");
//		sql.append("bus_global_id as busGlobalId,main_info.id as id,se.login_name as checkerCode,su.login_name as operatorCode,");
		sql.append("bus_global_id as busGlobalId,main_info.id as id, main_info.checker_code as checkerCode, main_info.operator_code as operatorCode, ");
//		sql.append("bus_global_id as busGlobalId,main_info.id as id,main_info.login_name as checkerCode,main_info.login_name as operatorCode,");
		sql.append("creditMoney as creditMoney,summary as rmk,debitMoney as debitMoney,race.race_name as raceName,race.id as raceId,bus_ori_type as busOriType,");
		sql.append("emergency_sign as emergencySign,cur_code as curCode ");
		return sql.toString();
	}
	
	/**
	 * 功能介绍 : 账务查询。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public String getAccbookSql(DataTablesInput input,String dataTable,String dataTableDtl, String corpAuthSql,boolean isPool, Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		Object account = input.getFormdata().get("EQs_account");
		Object summaryContent = input.getFormdata().get("summaryContent");
		try {
			sql.append(" from (");
//			sql.append("select accbook.login_name, accbook.voucher_state ,voucherType.voucher_name,accbook.tally_date,accbook.interest_start_date,");
			sql.append("select  accbook.voucher_state,accbook.voucher_type, accbook.net_id , accbook.tally_date,accbook.interest_start_date,");
			sql.append("accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,accbook.checker_code,accbook.orig_voucher_no,");
			sql.append("accbook.operator_code,accbook.bus_race_id,sum(case when accbook.redblue_sign='");
			sql.append(FinacialConstant.RB_SIGN_R);
			sql.append("' then 0-dtl.credit_money else dtl.credit_money end) as creditMoney,max(dtl.summary_content) as summary, ");
			sql.append("sum(case when accbook.redblue_sign='");
			sql.append(FinacialConstant.RB_SIGN_R);
			sql.append("' then 0-dtl.debit_money else dtl.debit_money end) as debitMoney, ");
			sql.append(" accbook.bus_ori_type,accbook.emergency_sign,accbook.cur_code  ");
//			sql.append(" accbook.bus_ori_type,accbook.emergency_sign,accbook.cur_code,accbook.net_id ");
			sql.append("from "+dataTable+" accbook ");
			if(isPool == false){
				sql.append(" left join fc_bus_race race on accbook.bus_race_id = race.id ");
			}else {
				sql.append(" left join fc_bus_race_pool race on accbook.bus_race_id = race.id ");
			}
			/*sql.append(",fc_voucher_type voucherType,fc_acc acc, "+dataTableDtl+" dtl,(");
			sql.append("select distinct a.book_id,a.net_id from (");
			sql.append("select dtl.book_id,acc.net_id from "+dataTableDtl+"");
			sql.append(" dtl ,fc_acc acc where dtl.account=acc.blnctr_acc and dtl.debit_money + dtl.credit_money<>0 ");
			if (!EmptyUtils.isEmpty(account)) {
			    sql.append(" and dtl.account = '").append(account).append("' ");
            }
			sql.append(") a,(");
			sql.append("select dtl.book_id from "+dataTableDtl+"");
			sql.append(" dtl ,fc_acc acc where dtl.account=acc.blnctr_acc and dtl.debit_money + dtl.credit_money<>0 ");*/
			// fc_acc 此处没有作用，删除
			sql.append(", "+dataTableDtl+" dtl,(");
			sql.append("select distinct a.book_id  from (");
			sql.append("select dtl.book_id from "+dataTableDtl+"");
			sql.append(" dtl where dtl.debit_money + dtl.credit_money<>0 ");
			if (!EmptyUtils.isEmpty(account)) {
				sql.append(" and dtl.account =:account0 ");
				params.put("account0",account);
			}
			if (!EmptyUtils.isEmpty(summaryContent)) {
				sql.append(" and dtl.SUMMARY_CONTENT like '%").append(summaryContent).append("%' ");
			}
			sql.append(") a,(");
			sql.append("select dtl.book_id from "+dataTableDtl+"");
			sql.append(" dtl where dtl.debit_money + dtl.credit_money<>0 ");
            if (!EmptyUtils.isEmpty(account)) {
                sql.append(" and dtl.account = :account1 ");
				params.put("account1",account);
            }
            if (!EmptyUtils.isEmpty(summaryContent)) {
				sql.append(" and dtl.SUMMARY_CONTENT like '%").append(summaryContent).append("%' ");
            }
			sql.append(") b where a.book_id=b.book_id");
			sql.append(") dtl_info where  dtl_info.book_id=accbook.id ");
			sql.append(" and dtl.book_id = dtl_info.book_id ");
			sql.append(getWhereAllCondition(input, FcAccbookSearchVO.class.newInstance(), "", false));
			sql.append(" group by accbook.voucher_state,accbook.voucher_type,  accbook.tally_date,accbook.operator_code,accbook.checker_code,");
			sql.append("accbook.bus_race_id,accbook.bus_ori_type,accbook.orig_voucher_no,");
			sql.append("accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id, ");
			sql.append(" accbook.emergency_sign,accbook.cur_code,accbook.net_id) main_info");
			if(isPool == false) {
				sql.append(" left join fc_bus_race race on  race.id = main_info.bus_race_id ");
			}else{
				sql.append(" left join fc_bus_race_pool race on  race.id = main_info.bus_race_id ");
			}
			//根据使用场景 getAccbookAndBlnedSql() --> se.login_name as checkerCode,su.login_name as operatorCode ，在表 fc_accbook 新增冗余字段：login_name
			/*sql.append("left join sys_user su on su.id=main_info.operator_code ");
			sql.append("left join sys_user se on se.id=main_info.checker_code ");*/
			//fc_corp_pool 不在改模块，如果用到需要通过dubbo调用 system 模块
//			if(isPool == false) {
//				//没有检查到有使用
////				sql.append("left join sys_corp sc on sc.id=main_info.net_id where 1 = 1 ");
//			}else{
//				sql.append("left join fc_corp_pool sc on sc.id=main_info.net_id where 1 = 1 ");
//			}
			sql.append(" where 1 = 1 ");
			if (!EmptyUtils.isEmpty(corpAuthSql)) {
				sql.append(" and ").append(corpAuthSql);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	/**
	 * 功能介绍 : 记账资金池凭证查询。<br>
	 */
	public DataTablesOutput listPoolOutputData(DataTablesInput input, String corpAuthSql){
		Map<String, Object> params = input.getSearchValues();
		StringBuilder sql = new StringBuilder();
		sql.append(getAccbookHeadSql()).append(" from (");
		sql.append(getAccbookAndBlnedSql());
		sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBook,FinancialQueryDao.fcAccBookDtl, corpAuthSql,true, params));
		sql.append(" union ");
		sql.append(getAccbookAndBlnedSql());
		sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBookBlned,FinancialQueryDao.fcAccBookBlnedDtl, corpAuthSql,true, params));
		sql.append(" ) fab order by fab.tallyDate desc,fab.voucherNo desc,fab.orderNo desc ");
		return getDynamicBySQL(input, sql.toString(), VFcAccbookAndBlnedsVO.class, null, false, DynamicallyObject.getInstance(params));
	}
	/**
	 * 功能介绍 : 导出-记账凭证查询。<br>
	 */
	public List<VFcAccbookAndBlnedsVO> listExcel(DataTablesInput input, String corpAuthSql){
		Map<String, Object> params = input.getSearchValues();
		StringBuilder sql = new StringBuilder();
		sql.append(getAccbookHeadSql()).append(" from (");
		sql.append(getAccbookAndBlnedSql());
		sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBook,FinancialQueryDao.fcAccBookDtl, corpAuthSql,false, params));
		sql.append(" union ");
		sql.append(getAccbookAndBlnedSql());
		sql.append(getAccbookSql(input,FinancialQueryDao.fcAccBookBlned,FinancialQueryDao.fcAccBookBlnedDtl, corpAuthSql,false, params));
		sql.append(" ) fab order by fab.tallyDate desc,fab.voucherNo desc,fab.orderNo desc ");
		return listBySQLAliasToBean(sql.toString(), params, VFcAccbookAndBlnedsVO.class);
	}
}
