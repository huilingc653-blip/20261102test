package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.closedeal.entity.FcBalanceSheet;
import com.bytter.biz.finacial.closedeal.vo.FcReportFormatBean;
import com.bytter.core.base.BaseDao;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: FC_BALANCE_SHEET
 * @author onlineGenerator
 * @date 2017-07-17 18:39:24
 * @version 1.0
 *
 */
@Repository
public class FcBalanceSheetDao extends BaseDao<FcBalanceSheet,String>{
	/**
	 * 功能介绍 : 更新年初余额。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 * @param lastYear
	 */
	public void updateInitYearBalance(FcMonthCloseDTO closeDTO, Integer lastYear) {
		StringBuilder sql = new StringBuilder();
		sql.append("  Update Fc_Balance_Sheet fa ")
				.append("  Set fa.a_Year_Balance = ")
				.append(" (Select b.a_Month_Balance ")
				.append("  From Fc_Balance_Sheet b ")
				.append(" Where fa.a_Item = b.a_Item ")
				.append("  And fa.Net_Code = b.Net_Code ")
				.append("  And fa.Cur_Code = b.Cur_Code ")
				.append("  And b.Year_Month2 = :lastYear), ")
				.append(" fa.l_Year_Balance = ")
				.append("  (Select c.l_Month_Balance ")
				.append(" From Fc_Balance_Sheet c ")
				.append(" Where fa.l_Item = c.l_Item ")
				.append("  And fa.Net_Code = c.Net_Code ")
				.append("  And fa.Cur_Code = c.Cur_Code ")
				.append("  And c.Year_Month2 = :lastYear) ")
				.append(" Where fa.Net_Code = :netId ")
				.append("  And fa.Year_Month2 = :yearMonth ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		params.put("yearMonth", closeDTO.getYearMonth2());
		params.put("lastYear", lastYear + "12");//前年的月末
		createSQLQuery(sql.toString(), params).executeUpdate();
	}
	/**
	 * 功能介绍 : 查询网点的格式定义数据(资产负债)。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcReportFormatBean> getNetReportFormat(FcMonthCloseDTO closeDTO, String curCode, String reportType) {
		StringBuilder sql = new StringBuilder();
		sql.append("  Select Fc_Report_Format.Line_No as lineNo,  ")
				.append("   Fc_Report_Format.Direction as direction, ")
				.append("   Fc_Report_Format.Item as item, ")
				.append("   Fc_Report_Format.Order_No as orderNo, ")
				.append("   Fc_Report_Format.Rect as rect, ")
				.append("   Fc_Report_Format.Subject as subject, ")
				.append("   Fc_Report_Format.Formula as formula, ")
				.append("   Fc_Report_Format.Net_Code as netCode , ")
				.append("   sum(BT_NVL_NUM(Fc_Month_Ledger.Balance,0.00)) as  balance ")
				.append("  From Fc_Report_Format ")
				.append("  Left Join Fc_Month_Ledger  On (Fc_Month_Ledger.Year_Month2 = :yearMonth) ")
				.append("  	And (Fc_Month_Ledger.Net_Code = Fc_Report_Format.net_code) ")
				.append("  	And (Fc_Month_Ledger.Net_Code = :netId) ")
				.append("  	And (Fc_Report_Format.Net_Code = :netId) ")
				.append("  	And (Fc_Month_Ledger.Currency_Code = :curId) ")
				.append("  	And (Fc_Report_Format.Subject = Fc_Month_Ledger.Subject_Code) ")
				.append("  Where (Fc_Report_Format.Report_Type = :reportType ) ")
				.append("   And (Fc_Report_Format.Net_Code = :netId) ")
				.append("  Group By Fc_Report_Format.Line_No, ")
				.append("   Fc_Report_Format.Direction, ")
				.append("   Fc_Report_Format.Item, ")
				.append("   Fc_Report_Format.Order_No, ")
				.append("   Fc_Report_Format.Rect, ")
				.append("   Fc_Report_Format.Subject, ")
				.append("   Fc_Report_Format.Formula, ")
				.append("   Fc_Report_Format.Net_Code ")
				.append("   order by Fc_Report_Format.Order_No");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		params.put("yearMonth", closeDTO.getYearMonth2());
		params.put("curId", curCode);
		params.put("reportType", reportType);
		return listBySQLAliasToBean(sql.toString(), params, FcReportFormatBean.class);
	}
	/**
	 * 功能介绍 : 转移资产负债数据。<br>
	 * <b>日期：</b>2017年7月18日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void deletebalanceSheet(FcMonthCloseDTO closeDTO) {
		String sql = "  Delete From Fc_Balance_Sheet  Where Year_Month2 = :yearMonth "
				+ " and net_code = :netId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", closeDTO.getNetCode());
		params.put("yearMonth", closeDTO.getYearMonth2());
		createSQLQuery(sql, params).executeUpdate();
	}
	public List<FcBalanceSheet> getBalanceSheet(FcMonthCloseDTO closeDTO, Integer lastYear,Integer closeYear){
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.a_item,b.l_item, b.A_MONTH_BALANCE aYearBalance, b.L_MONTH_BALANCE lYearBalance,b.cur_code  ");
		sql.append(" From Fc_Balance_Sheet b ");
		sql.append(" where 1=1 and  (b.Year_Month2 = :lastYear)");
		sql.append(" and b.net_code = :netId order by b.a_year_balance desc");
		Map<String,Object> params = new HashMap<>();
		params.put("netId", closeDTO.getNetCode());
		params.put("yearMonth", closeDTO.getYearMonth2());
		params.put("lastYear", lastYear + "12");//前年的月末
//		params.put("closeYear", closeYear + "01");//年初余额
		return listBySQLAliasToBean(sql.toString(),params,FcBalanceSheet.class);
	}
}
