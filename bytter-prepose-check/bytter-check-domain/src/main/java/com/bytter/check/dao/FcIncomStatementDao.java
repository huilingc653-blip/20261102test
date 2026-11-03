package com.bytter.check.dao;
import com.bytter.check.vo.FcBanlanceSheetVO;
import com.bytter.check.vo.FcIncomStatementVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FcIncomStatementDao extends BaseDao<FcIncomStatementVO, String>{
	
	@SuppressWarnings("rawtypes")
	public DataTablesOutput  getFcIncomeStatement(DataTablesInput input){
		Map<String , Object> param=input.getFormdata();
		List list=new ArrayList<Order>();
		Order order=new Order(1,"asc");
		Order order1=new Order(0,"asc");
		List<Column> columns = new ArrayList<Column>();
		 Column column=new Column("orderNo","orderNo",true,true,input.getSearch());
		 Column column1=new Column("curCode","curCode",true,true,input.getSearch());
		 columns.add(column);
		 columns.add(column1);
		list.add(order);
		list.add(order1);
		input.setOrder(list);
		input.setColumns(columns);
		String checkDate=(String)input.getFormdata().get("checkDate");
		if(StringUtils.isNotEmpty(checkDate)){
			param.remove("checkDate");
			param.put("eqs_yearMonth2",  checkDate.replaceAll("-", ""));
		}
		StringBuilder sql=new StringBuilder();
		/*sql.append("select  fis.item as item,fis.order_no as orderNo,bt_nvl_num(fis.balance,0) as balance,bt_nvl_num(fis.year_total,0) as yearTotal,");
		sql.append(" fis.cur_code as curCode,bc.cur_name as curName, fis.net_code as netCode,sc.name as netName,fis.year_month2 as yearMonth2");
		sql.append("  from fc_income_statement fis left join sys_corp sc on sc.id=fis.net_code");
		sql.append("  left join bt_currency bc  on bc.id=fis.cur_code where 1=1 ");*/
		sql.append("select  fis.net_code as netCode,fis.cur_code as curCode, fis.item as item,fis.order_no as orderNo,bt_nvl_num(fis.balance,0) as balance,bt_nvl_num(fis.year_total,0) as yearTotal,");
		sql.append(" fis.year_month2 as yearMonth2");
		sql.append("  from fc_income_statement fis ");
		sql.append("  where 1=1 ");
		return getDynamicBySQL(input,sql.toString(),FcIncomStatementVO.class,"fis",false,DynamicallyObject.getInstance(param));
		
	}
	
	
	@SuppressWarnings("rawtypes")
	public DataTablesOutput  getFcBanlanceSheet(DataTablesInput input){
		Map<String , Object> param=input.getFormdata();
		List list=new ArrayList<Order>();
		Order order=new Order(1,"asc");
		Order order1=new Order(0,"asc");
		List<Column> columns = new ArrayList<Column>();
		 Column column=new Column("aOrderNo","aOrderNo",true,true,input.getSearch());
		 Column column1=new Column("curCode","curCode",true,true,input.getSearch());
		 columns.add(column1);
		 columns.add(column);
		list.add(order1);
		list.add(order);
		input.setOrder(list);
		input.setColumns(columns);
		String checkDate=(String)input.getFormdata().get("checkDate");
		if(StringUtils.isNotEmpty(checkDate)){
			param.remove("checkDate");
			param.put("eqs_yearMonth2",  checkDate.replaceAll("-", ""));
		}
		StringBuilder sql=new StringBuilder();
		/*sql.append("select  fis.a_item as aItem,fis.a_order_no as aOrderNo,bt_nvl_num(fis.a_year_balance,0) as aYearBalance,bt_nvl_num(fis.a_month_balance,0) as aMonthBalance,");
		sql.append(" fis.l_item as lItem,fis.l_order_no as lOrderNo,bt_nvl_num(fis.l_year_balance,0) as lYearBalance,bt_nvl_num(fis.l_month_balance,0) as lMonthBalance,");
		sql.append(" fis.cur_code as curCode,bc.cur_name as curName, fis.net_code as netCode,sc.name as netName,fis.year_month2 as yearMonth2");
		sql.append("  from fc_balance_sheet fis left join sys_corp sc on sc.id=fis.net_code");
		sql.append("  left join bt_currency bc  on bc.id=fis.cur_code where 1=1 ");*/
		sql.append("select fis.net_code as netCode,fis.cur_code as curCode, fis.a_item as aItem,fis.a_order_no as aOrderNo,bt_nvl_num(fis.a_year_balance,0) as aYearBalance,bt_nvl_num(fis.a_month_balance,0) as aMonthBalance,");
		sql.append(" fis.l_item as lItem,fis.l_order_no as lOrderNo,bt_nvl_num(fis.l_year_balance,0) as lYearBalance,bt_nvl_num(fis.l_month_balance,0) as lMonthBalance,");
		sql.append(" fis.year_month2 as yearMonth2");
		sql.append(" from fc_balance_sheet fis ");
		sql.append(" where 1=1 ");
		return getDynamicBySQL(input ,sql.toString(), FcBanlanceSheetVO.class,"fis",false,DynamicallyObject.getInstance(param));
		
	}
	/**
	 * 损益表获取币种
	 *@Param params
	 *@return
	 */
	public List<Map<String,String>> getFcIncomeStatementCurId(String netCode,String yearMonth){
		Map<String, Object> params = new HashMap<>();
		params.put("yearMonth2",yearMonth.replaceAll("-",""));
		params.put("netCode",netCode);
		StringBuilder sql = new StringBuilder();
		sql.append("select fis.cur_code from FC_INCOME_STATEMENT fis");
		sql.append(" where fis.year_month2 = :yearMonth2 and fis.net_code = :netCode");
		sql.append(" group by fis.cur_code");
		return listBySQL(sql.toString(), params);
	}
	/**
	 *资产负债表获取币种
	 *@Param params
	 *@return
	 */
	public List<Map<String,String>> getFcBanlanceSheetCurId(String netCode,String yearMonth){
		Map<String, Object> params = new HashMap<>();
		params.put("yearMonth2",yearMonth.replaceAll("-",""));
		params.put("netCode",netCode);
		StringBuilder sql = new StringBuilder();
		sql.append("select fis.cur_code from fc_balance_sheet fis");
		sql.append(" where fis.year_month2 = :yearMonth2 and fis.net_code = :netCode");
		sql.append(" group by fis.cur_code");
		return listBySQL(sql.toString(), params);
	}
}
