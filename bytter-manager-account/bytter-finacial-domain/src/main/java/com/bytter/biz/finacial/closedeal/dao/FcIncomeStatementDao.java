package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.closedeal.entity.FcIncomeStatement;
import com.bytter.biz.finacial.closedeal.vo.FcReportFormatBean;
import com.bytter.biz.payment.api.dto.FcInitSubjectDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FC_INCOME_STATEMENT
 * @date 2017-07-17 18:37:10
 */
@Repository
public class FcIncomeStatementDao extends BaseDao<FcIncomeStatement, String> {
    /**
     * 功能介绍 : 更新年初余额。<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @param lastYear
     * @version 1.0
     * @author liyh
     */
    public void updateInitYearBalance(FcMonthCloseDTO closeDTO, Integer lastYear) {
        String sql = "  Update Fc_Income_Statement  a " +
                "   Set a.Year_Total = BT_NVL_NUM(a.Year_Total,0.00) + " +
                "      (Select sum(BT_NVL_NUM(b.Balance,0.00)) " +
                "    From Fc_Income_Statement b " +
                "    Where a.Item = b.Item " +
                "      And a.Net_Code = b.Net_Code " +
                "      And a.Cur_Code = b.Cur_Code " +
                "      And b.Year_Month2 like :opYear ) " +
                "  Where a.Net_Code = :netId " +
                "  And a.Year_Month2 = :yearMonth ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", closeDTO.getNetCode());
        params.put("yearMonth", closeDTO.getYearMonth2());
        params.put("opYear", lastYear + "%");
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 功能介绍 : 查询网点的格式定义数据(损益表)。<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcReportFormatBean> getNetReportFormat(FcMonthCloseDTO closeDTO, String curCode, String reportType, List<FcAccDTO> fcList) {
        List<String> subjectIds = fcList.stream().map(FcAccDTO::getSubjectId).collect(Collectors.toList());
        List<String> accList = fcList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        String sql = "   Select Fc_Report_Format.Line_No as lineNo, " +
                " 	Fc_Report_Format.Direction as direction, " +
                " 	Fc_Report_Format.Item as item, " +
                " 	Fc_Report_Format.Order_No as orderNo, " +
                " 	Fc_Report_Format.Rect as rect, " +
                " 	Fc_Report_Format.Subject as subject, " +
                " 	Fc_Report_Format.Formula as formula, " +
                " 	Fc_Report_Format.Net_Code as netCode, " +
                " 	Fc_Report_Format.Init_Year as initYear, " +
                " 	BT_NVL_NUM((Select BT_NVL_NUM(sum(BT_NVL_NUM(Fc_Accbook_Blned_Dtl.Debit_Money,0.00) - BT_NVL_NUM(Fc_Accbook_Blned_Dtl.Credit_Money,0.00)),0.00) " +
                " From Fc_Accbook_Blned_Dtl " +
                " Where " + SqlUtil.getInSql(accList, "Fc_Accbook_Blned_Dtl.Account", true) +
                // " And " + SqlUtil.getInSql(subjectIds, "Fc_Report_Format.Subject", true) +
                " And (Fc_Accbook_Blned_Dtl.SUBJECT_ID = Fc_Report_Format.SUBJECT) " +
                " And Fc_Accbook_Blned_Dtl.book_id In " +
                " (Select Distinct Fc_Accbook_Blned.id " +
                " 	From Fc_Accbook_Blned, " +
                " 	Fc_Accbook_Blned_Dtl, " +
                " 	Fc_Bus_Race " +
                " 	Where Fc_Accbook_Blned.id = Fc_Accbook_Blned_Dtl.book_id " +
                " 	And Fc_Accbook_Blned.Voucher_State <> 'B' " +
                " 	And " + SqlUtil.getInSql(accList, "Fc_Accbook_Blned_Dtl.Account", true) +
                " 	And Fc_Accbook_Blned.bus_race_id = Fc_Bus_Race.id " +
                " 	And Fc_Accbook_Blned.Tally_Date >= :startDate " +
                " 	And Fc_Accbook_Blned.Tally_Date <= :endDate " +
                " 	And Fc_Accbook_Blned.generate_mode <> 'P' " +
                " 	And Fc_Bus_Race.Sys_Race_Code not in ('" + FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_INCOME.get() +
                "', '" + FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_PAYOUT.get() + "') " +
                " ) ), 0.00) as balance " +
                " From Fc_Report_Format " +
                " Where (Fc_Report_Format.Report_Type = :reportType )  " +//损益类
                " And (Fc_Report_Format.Net_Code = :netId ) " +
                " Group By Fc_Report_Format.Line_No, " +
                " Fc_Report_Format.Direction, " +
                " Fc_Report_Format.Item, " +
                " Fc_Report_Format.Order_No, " +
                " Fc_Report_Format.Rect, " +
                " Fc_Report_Format.Subject, " +
                " Fc_Report_Format.Formula, " +
                " Fc_Report_Format.Net_Code, " +
                " Fc_Report_Format.Init_Year ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", closeDTO.getNetCode());
        params.put("yearMonth", closeDTO.getYearMonth2());
        params.put("startDate", closeDTO.getCloseDate());
        params.put("endDate", closeDTO.getEndDate());
        params.put("curId", curCode);
        params.put("reportType", reportType);
        return listBySQLAliasToBean(sql, params, FcReportFormatBean.class);
    }
    /**
     * 功能介绍 : 转移资产损益数据。<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteIncomStatement(FcMonthCloseDTO closeDTO) {
        String sql = "  Delete From Fc_Income_Statement Where Year_Month2 = :yearMonth "
                + " and net_code = :netId ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", closeDTO.getNetCode());
        params.put("yearMonth", closeDTO.getYearMonth2());
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 功能介绍 : 查询损益类账户的本年累计。<br>
     * <b>日期：</b>2020年6月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcInitSubjectDTO> getTotalSubjectBal(FcMonthCloseDTO closeDTO, Date startDate, String reportType, List<FcAccDTO> fcAccList) {
        StringBuilder sql = new StringBuilder();
        List<String> subjectIds = fcAccList.stream().map(FcAccDTO::getSubjectId).collect(Collectors.toList());
        List<String> accList = fcAccList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        List<String> netList = fcAccList.stream().map(FcAccDTO::getNetId).collect(Collectors.toList());
        sql.append(" Select ")
                .append("  BT_NVL_NUM(Fc_Accbook_Blned_Dtl.Debit_Money,0.00) as debitMoney, BT_NVL_NUM(Fc_Accbook_Blned_Dtl.Credit_Money,0.00) as creditMoney,")
                .append(" Fc_Accbook_Blned_Dtl.account as account From Fc_Accbook_Blned, ")
                .append(" Fc_Accbook_Blned_Dtl, ")
                .append(" Fc_Bus_Race ")
                .append(" Where Fc_Accbook_Blned.id = Fc_Accbook_Blned_Dtl.book_id ")
                .append(" And Fc_Accbook_Blned.Voucher_State <> 'B' ")
                .append(" And ").append(SqlUtil.getInSql(accList, "Fc_Accbook_Blned_Dtl.Account", true))
                .append(" And Fc_Accbook_Blned.bus_race_id = Fc_Bus_Race.id ")
                .append(" And Fc_Accbook_Blned.generate_mode <> 'P' ")
                .append(" And Fc_Bus_Race.Sys_Race_Code not in ('").append(FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_INCOME.get())
                .append("', '").append(FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_PAYOUT.get()).append("') ")
                .append(" And Fc_Accbook_Blned.Tally_Date > :startDate ")
                .append(" And Fc_Accbook_Blned.Tally_Date <= :endDate ")
                .append(" and exists ( ")
                .append(" select 1 from fc_report_format ")
                .append(" where ").append(SqlUtil.getInSql(subjectIds, "fc_report_format.subject", true))
                .append(" and ").append(SqlUtil.getInSql(netList, "fc_report_format.net_code", true))
                .append(" and fc_report_format.report_type = :reportType ) ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", startDate);
        params.put("endDate", closeDTO.getEndDate());
        params.put("reportType", reportType);
        return listBySQLAliasToBean(sql.toString(), params, FcInitSubjectDTO.class);
    }
}
