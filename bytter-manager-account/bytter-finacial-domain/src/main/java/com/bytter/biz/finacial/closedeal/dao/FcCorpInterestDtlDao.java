package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.closedeal.entity.FcCorpInterestDtl;
import com.bytter.biz.finacial.closedeal.vo.FcCorpInterestDtlVO;
import com.bytter.biz.finacial.closedeal.vo.FcDayCloseVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FC_CORP_INTEREST_DTL
 * @date 2017-07-12 14:23:15
 */
@Repository
public class FcCorpInterestDtlDao extends BaseDao<FcCorpInterestDtl, String> {
    /**
     * 功能介绍 : 查询可以产生单位利息明细的账务数据。<br>
     * <b>日期：</b>2017年7月12日<br>
     *
     * @param dayClose  查询实体类
     * @param fcAccList 网点id 和 accType = 3 的账号集合
     * @return 账务数据
     */
    public List<FcCorpInterestDtlVO> queryBookInterestDtl(FcDayCloseVO dayClose, List<String> singleAccByNetList, List<String> allAccByNetList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" Select Al.Tally_date    as interestDate, ")
                .append("    Al.Corp_id          as corpId, ")
                .append("    Al.Subject_id       as subjectCode, ")
                .append("    Al.Money            as interestMoney, ")
                .append("    Al.Net_id           as netId, ")
                .append("    Al.Cur_id           as curCode, ")
                .append("    Al.Sys_Subject_Code as sysSubjectCode, ")
                //TODO 用bank_code 代替 Bank_Type_id 后续程序赋值
                .append("    Al.Bank_Code as bankTypeId ")
                .append("    From (Select k.Corp_id, ")
                .append("      k.net_id, ")
                .append("      k.Bank_Code, ")
                .append("      k.Subject_id, ")
                .append("      Sum(k.Money) Money, ")
                .append("      k.Cur_id, ")
                .append("      k.Tally_date, ")
                .append("      k.Sys_Subject_code ")
                .append("   From ((Select d.Net_id, ")
                .append("      d.Corp_id, ")
                .append("      d.Bank_Code, ")
                .append("      Inorder.Subject_id, ")
                .append("      d.Cur_id, ")
                .append("      sum(d.Debit_Money - d.Credit_Money) * d.Balance_Way * ")
                //TODO 下面这个条件 不会自相矛盾嘛  永远都只会是  else  按照现在的表结构调整
                .append("      (case when (Inorder.Sys_Subject_code like '501%' and ")
                .append("          d.Sys_Subject_code = '2120') or ")
                .append("          (Inorder.Sys_Subject_code like '521%' and ")
                .append("          d.Sys_Subject_code = '1020') then ")
                .append("        -1 ")
                .append("        else ")
                .append("        1 ")
                .append("      end) as Money, ")
                .append("      Inorder.Tally_date, ")
                .append("      Inorder.Sys_Subject_code ")
                .append("   From Fc_Accbook_Blned_Dtl d, ")
                .append("      (Select distinct b.book_id, ")
                .append("              b.Subject_id, ")
                .append("              b.Balance_Way, ")
                .append("              a.Tally_date, ")
                .append("              b.Sys_Subject_code ")
                .append("       From Fc_Accbook_Blned     a, ")
                .append("            Fc_Accbook_Blned_Dtl b ")
                .append("       Where a.id = b.book_id ")
                //TODO 所属该网点下的账户列表
                .append("        And ").append(SqlUtil.getInSql(allAccByNetList, "b.account", true))
                .append("        And a.Voucher_State = 'N' ")
                .append("        And b.Summary_Content not like '%损益结转本年利润%' ")
                .append("        And b.Summary_Content Not Like '%贴现%' ")
                .append("        And b.Summary_Content Not Like '%税%' ")
                //TODO 目前这个补充  科目代码在  Fc_Accbook_Blned_Dtl 表中
                .append("        And (b.Sys_Subject_Code Like '501%' Or ")
                .append("             b.Sys_Subject_Code Like '521%') ")
                .append("        And a.Tally_Date >= :lastCloseDate ")
                .append("        And a.Tally_Date <= :closeDate ");
        if (singleAccByNetList.size() > 0) {
            sql.append("        And a.id Not In ")
                    .append("         (Select distinct b.book_id ")
                    .append("          From Fc_Accbook_Blned     a, ")
                    .append("               Fc_Accbook_Blned_Dtl b ")
                    .append("          Where a.id = b.book_id ")
                    //TODO 剔除账户类型为 03，并且在该网点下的  账户列表
                    .append("           And ").append(SqlUtil.getInSql(singleAccByNetList, "b.account", true))
                    .append("           And a.Tally_Date >= :lastCloseDate ")
                    .append("           And a.Tally_Date <= :closeDate) ");
        }
        sql.append(") Inorder ")
//                .append("   Where 1=1 and ").append(getSQLIn(accList, 900, "d.Account "))
//                .append("   And d.book_id = Inorder.book_id ")
                .append("   Where d.book_id = Inorder.book_id and ")
                //TODO 在该网点下的  账户列表
                .append(SqlUtil.getInSql(allAccByNetList, "d.account", true))
                //TODO 不允许同一凭证下  同时出现科目为501或502的利息账务数据
                .append("   And ((d.Sys_Subject_Code not Like '501%' and ")
                .append("          d.Sys_Subject_Code not Like '521%') or ")
                .append("        d.Sys_Subject_Code is null) ")
                //TODO Net_id\Corp_id\Bank_Code\Cur_id\Balance_Way\Sys_Subject_code以下字段都是需要补充的
                .append("   Group By d.Net_id, ")
                .append("         d.Corp_id, ")
                .append("         d.Bank_Code, ")
                .append("         Inorder.Subject_id, ")
                .append("         d.Cur_id, ")
                .append("         d.Balance_Way, ")
                .append("         Inorder.Tally_date, ")
                .append("         d.Sys_Subject_code, ")
                .append("         Inorder.Sys_Subject_code)  ")
                .append("  Union All ")
                .append("   (Select e.Net_id, ")
                .append("           d.Corp_id, ")
                .append("           e.Bank_Code, ")
                .append("           e.Subject_id, ")
                .append("           e.Cur_id, ")
                .append("           Sum((e.Debit_Money - e.Credit_Money) * e.Balance_Way) as money, ")
                .append("           d.Tally_date, ")
                .append("           e.Sys_Subject_code ")
                .append("    From Fc_Accbook_Blned_Dtl e, ")
                .append("         (Select Distinct a.id, b.Corp_id, a.Tally_date ")
                .append("          From Fc_Accbook_Blned     a, ")
                .append("               Fc_Accbook_Blned_Dtl b ")
                .append("          Where a.id = b.book_id and (");
        if (singleAccByNetList.size() > 0) {
            //TODO 账户类型为 03，并且在该网点下的  账户列表
            sql.append(SqlUtil.getInSql(singleAccByNetList, "b.account", true)).append(" or ");
        }
        sql.append("       b.Summary_Content Like '%贴现%') ")
                .append("          And a.Voucher_State = 'N'  ")
                .append("          And a.Tally_Date >= :lastCloseDate  ")
                .append("          And a.Tally_Date <= :closeDate) d ")
                .append("         Where d.id = e.book_id ")
                //TODO 在该网点下的  账户列表
                .append("          And ").append(SqlUtil.getInSql(allAccByNetList, "e.account", true))
                .append("          And (e.Sys_Subject_Code Like '501%' Or e.Sys_Subject_Code Like '521%') ")
                .append("         Group By e.Net_id, ")
                .append("                  d.Corp_id, ")
                .append("                  e.Bank_Code, ")
                .append("                  e.Subject_id, ")
                .append("                  e.Cur_id, ")
                .append("                  d.Tally_date, ")
                .append("                  e.Sys_Subject_code)) k ")
                .append("   Group by k.Corp_id, ")
                //TODO 用bank_code 代替 Bank_Type_id
                .append("           k.Bank_Code, ")
                .append("           k.Subject_id, ")
                .append("           k.Net_id, ")
                .append("           k.Cur_id, ")
                .append("           k.Tally_date, ")
                .append("          k.Sys_Subject_code ")
                //TODO 用bank_code 代替 Bank_Type_id 排序
                .append("   Order By k.Tally_date, Corp_id, k.Bank_Code, k.Sys_Subject_code) Al ");
        // 改为远程调用
//	        .append("   left join bt_bank_type bt on bt.BANK_TYPE = al.Bank_Type_id")
//	        .append("   inner join sys_corp sc on Al.corp_id = sc.id ");
        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("netId", dayClose.getNetCode());
        params.put("closeDate", dayClose.getCloseDate());
        params.put("lastCloseDate", dayClose.getLastCloseDate());
        return listBySQLAliasToBean(sql.toString(), params, FcCorpInterestDtlVO.class);
    }
}
