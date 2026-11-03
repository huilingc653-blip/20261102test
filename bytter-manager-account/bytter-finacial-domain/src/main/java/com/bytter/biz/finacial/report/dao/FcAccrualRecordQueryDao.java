package com.bytter.biz.finacial.report.dao;
import com.bytter.biz.finacial.report.vo.FcAccrualRecordListVO;
import com.bytter.biz.finacial.report.vo.FcLedgerSearchVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
@Repository
public class FcAccrualRecordQueryDao extends BaseDao<FcAccrualRecordListVO,String>{
	
	
	/**
	 * 手续费计提 查询头
	 * @param Input
	 * @param corpAuth
	 * @return
	 */
	public DataTablesOutput queryFcAccrualRecordHead(DataTablesInput Input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = Input.getSearchValues();
		sql.append("select min(main_info.tallyDate) as startDate,max(main_info.tallyDate) as endDate,main_info.curCode as curId,main_info.curName,main_info.netId from (");
		sql.append(getAccrualRecordHeadSql("fc_accbook","fc_accbook_dtl",Input,corpAuth));
		sql.append(" UNION ALL ");
		sql.append(getAccrualRecordHeadSql("fc_accbook_blned","fc_accbook_blned_dtl",Input,corpAuth));
		sql.append(" ) main_info  where 1=1 ");
		sql.append(" GROUP BY main_info.curCode,main_info.curName,main_info.netId ");
		return getDynamicBySQL(Input,sql.toString(),FcAccrualRecordListVO.class,null,false,DynamicallyObject.getInstance(params));
	}
	
	/**
	 * 获取手续费表头sql
	 * @param Input
	 * @param corpAuth
	 * @param bookTable
	 * @param bookDtlTable
	 * @return
	 */
	public String getAccrualRecordHeadSql(String bookTable,String bookDtlTable,DataTablesInput Input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		try {
			/*sql.append("select info.tallyDate,info.curCode,info.curName,info.netId ");
			sql.append("from (");
			sql.append("select b.tally_date as tallyDate,d.cur_id as curCode,e.cur_name as curName,b.net_id as netId ");
			sql.append("from fc_charge_accrue a,"+bookTable+" b,"+bookDtlTable+" c,fc_acc d,bt_currency e ");
			sql.append("where a.order_no=b.id and a.target_order_no=c.book_id and c.account=d.blnctr_acc ");
			sql.append("and d.cur_id=e.id ");
			sql.append(") info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(Input, FcLedgerSearchVO.class.newInstance(), "", false));*/
			// fc_charge_accrue 手续费计提表--> target_order_no 关联流水号 = bookDtlTable 流水明细表--> book_id
			//fc_charge_accrue 表里有借方账号，可以直接通过 冗余cur_code，cur_name 用来查币种；--> TODO 填充数据时，根据账号，查询 fc_acc 表，拿到 cur_id
			sql.append("select info.tallyDate,info.curCode,info.curName,info.netId ");
			sql.append("from (");
			sql.append("select b.tally_date as tallyDate,a.cur_code as curCode,a.cur_name as curName,b.net_id as netId ");
			sql.append("from fc_charge_accrue a,"+bookTable+" b,"+bookDtlTable+" c ");
			sql.append("where a.order_no=b.id and a.target_order_no=c.book_id ");
			sql.append(") info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(Input, FcLedgerSearchVO.class.newInstance(), "", false));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sql.toString();
	}
	
	
	/**
	 * 手续费明细总数
	 * @param input
	 * @param corpAuth
	 * @return
	 */
	public Map<String,Object> queryFcAccrualRecordDtlNum(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		try {
			sql.append("select count(*) as tatolNum");
			sql.append(" from ( ");
			sql.append(getAccrualRecordDataSql(input,corpAuth,"fc_accbook","fc_accbook_dtl"));
			sql.append(" UNION ALL ");
			sql.append(getAccrualRecordDataSql(input,corpAuth,"fc_accbook_blned","fc_accbook_blned_dtl"));
			sql.append(" ) main_info where 1=1 ");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return getBySQL(sql.toString(),params);
	}
	
	/**
	 * 手续费计提结转查询
	 * @param
	 * @param corpAuth
	 * @return
	 */
	public DataTablesOutput queryFcAccrualRecordData(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		sql.append("select  main_info.id, main_info.curCode,main_info.curName,main_info.targetOrderNo,min(main_info.tallyDate) as startDate,max(main_info.tallyDate) as endDate,");
		sql.append("main_info.account,main_info.operatorCode,max(main_info.accrueTime) as accrueTime,main_info.netId as netId ");
		sql.append("from ( ");
		sql.append(getAccrualRecordDataSql(input,corpAuth,"fc_accbook","fc_accbook_dtl"));
		sql.append(" UNION ALL ");
		sql.append(getAccrualRecordDataSql(input,corpAuth,"fc_accbook_blned","fc_accbook_blned_dtl"));
		sql.append(" ) main_info where 1=1 ");
		sql.append(" group by main_info.id,main_info.curCode, main_info.curName, main_info.targetOrderNo, main_info.account, main_info.operatorCode,main_info.netId ");
		return getDynamicBySQL(input,sql.toString(),FcAccrualRecordListVO.class,null,false,DynamicallyObject.getInstance(params));
	}
	
	/**
	 * 获取手续费明细sql
	 * @param
	 * @param corpAuth
	 * @param bookTable
	 * @param bookDtlTable
	 * @return TODO fc_charge_accrue 没办法冗余字段，没有直接和 sys_user\ bt_currency \ fc_acc 关联字段
	 */
	public String getAccrualRecordDataSql(DataTablesInput input,List<String> corpAuth,String bookTable,String bookDtlTable){
		StringBuilder sql=new StringBuilder();
		try {
			sql.append("select info.id,info.curCode,info.curName,info.targetOrderNo,info.tallyDate,");
			sql.append("info.account,info.operatorCode,info.accrueTime,info.netId as netId ");
			sql.append("from ( ");
			sql.append("select a.target_order_no as id,fa.cur_id as curCode,e.cur_name as curName,b.order_no as targetOrderNo,b.tally_date as tallyDate,");
			sql.append("c.account as account,sy.username as operatorCode,a.accrue_time as accrueTime,b.net_id as netId ");
			sql.append("from fc_charge_accrue a,"+bookTable+" b,"+bookDtlTable+" c,fc_acc fa,sys_user sy,bt_currency e "); // 暂不处理
			sql.append("where a.target_order_no = c.book_id and a.order_no = b.id and c.account = fa.blnctr_acc and fa.cur_id = e.id ");
			sql.append("and c.credit_money <> 0 and a.operator_code = sy.id ");
			sql.append(" ) info where 1=1 ");
			sql.append(" and ").append(getSQLIn(corpAuth,900,"info.netId"));
			sql.append(getWhereAllCondition(input, FcLedgerSearchVO.class.newInstance(), "", false));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sql.toString();
	}
	/**
	 * 导出 - 手续费计提 查询头
	 * @param Input
	 * @param corpAuth
	 * @return
	 */
	public List<FcAccrualRecordListVO> excelFcAccrualRecordHead(DataTablesInput Input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = Input.getSearchValues();
		sql.append("select min(main_info.tallyDate) as startDate,max(main_info.tallyDate) as endDate,main_info.curCode as curId,main_info.curName,main_info.netId from (");
		sql.append(getAccrualRecordHeadSql("fc_accbook","fc_accbook_dtl",Input,corpAuth));
		sql.append(" UNION ALL ");
		sql.append(getAccrualRecordHeadSql("fc_accbook_blned","fc_accbook_blned_dtl",Input,corpAuth));
		sql.append(" ) main_info  where 1=1 ");
		sql.append(" GROUP BY main_info.curCode,main_info.curName,main_info.netId ");
		return listBySQLAliasToBean(sql.toString(),params,FcAccrualRecordListVO.class);
	}
	/**
	 * 导出-手续费计提结转明细
	 */
	public List<FcAccrualRecordListVO> excelFcAccrualRecordData(DataTablesInput input,List<String> corpAuth){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = input.getSearchValues();
		sql.append("select  main_info.id, main_info.curCode,main_info.curName,main_info.targetOrderNo,min(main_info.tallyDate) as startDate,max(main_info.tallyDate) as endDate,");
		sql.append("main_info.account,main_info.operatorCode,max(main_info.accrueTime) as accrueTime,main_info.netId as netId ");
		sql.append("from ( ");
		sql.append(getAccrualRecordDataSql(input,corpAuth,"fc_accbook","fc_accbook_dtl"));
		sql.append(" UNION ALL ");
		sql.append(getAccrualRecordDataSql(input,corpAuth,"fc_accbook_blned","fc_accbook_blned_dtl"));
		sql.append(" ) main_info where 1=1 ");
		sql.append(" group by main_info.id,main_info.curCode, main_info.curName, main_info.targetOrderNo, main_info.account, main_info.operatorCode,main_info.netId ");
		return listBySQLAliasToBean(sql.toString(),params,FcAccrualRecordListVO.class);
	}
}
