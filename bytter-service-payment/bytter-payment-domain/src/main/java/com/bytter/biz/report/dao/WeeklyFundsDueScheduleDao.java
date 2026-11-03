package com.bytter.biz.report.dao;

import com.bytter.biz.report.vo.WeeklyFundsDueScheduleVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhous
 * @version 1.0
 * @Title: Service
 * @Description: 每周资金到期用款规划表dao
 * @date 2024-10-24
 */
@Repository
public class WeeklyFundsDueScheduleDao extends BaseDao<WeeklyFundsDueScheduleVo, String> {

    /**
    * @Description 查询每周资金到期用款规划表
    * @author zhous
    * @date 2024/10/25 14:31
    */
    public DataTablesOutput<WeeklyFundsDueScheduleVo> dataQuery(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        String startQueryDate = (String)dataTablesInput.getFormdata().get("ged_endDate");
        String endQueryDate = (String)dataTablesInput.getFormdata().get("led_endDate");
        params.put("startDate",startQueryDate);
        params.put("endDate",endQueryDate);
        StringBuilder sql = new StringBuilder(" select a.* from ( ");
        sql.append(" select sem.Equity_code as equityCode,sem.Equity_name as equityName,m.operationAreaCode as operationAreaCode,");
        sql.append(" m.corpName,m.corpId, m.corpCode, m.bankTypeId,m.typeName,m.bankAcc,m.billMoney,m.bailMoney,m.endDate,BT_NVL_NUM(m.bailPercent,0) as bailPercent,m.parentName,m.parentId,m.parentSort,");
        sql.append("  case when m.bailPercent <> 1 or m.bailPercent is null then BT_NVL_NUM(l.loanMoney,0) + BT_NVL_NUM(m.billMoney,0) else BT_NVL_NUM(l.loanMoney,0) end  as applyRepayMoney, ");
        sql.append("  m.applicant,BT_NVL_NUM(l.loanMoney,0) as loanMoney ");
        sql.append(" from ( ");
        sql.append(" select sum(av.bill_money) as billMoney,sum(avv.bail_money) as bailMoney,avv.corp_id as corpId, sc.code AS corpCode, sc.name as corpName, ");
        sql.append(" avv.BANK_TYPE_ID as bankTypeId,avv.type_name as typeName,avv.bank_acc as bankAcc,av.end_date as endDate, ");
        sql.append(" a.BAILPERCENT  as bailPercent,");
        sql.append(" case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.id when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.id else sc3.id end as parentId,");
        sql.append(" case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.name  when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.name else sc3.name end as parentName,");
        sql.append(" case when sc.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc.sort  when sc2.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') then sc2.sort else sc3.sort end as parentSort, ");
        sql.append(" sc.equityMaintenance_ID as equityMaintenanceId,sc.operationArea_ID as operationAreaCode,group_concat(distinct BT_NVL_CHAR(avv.CREATE_NAME,a.CREATE_NAME) separator ',') as applicant from  ");
        sql.append(" ab_voucherinfo_vice avv ");
        sql.append(" left join ab_voucherinfo av on av.id = avv.vid ");
        sql.append(" left join ab_accrecords a on a.vvid = avv.id and a.brief_id ='001'  ");
        sql.append(" left join sys_corp sc on sc.id = avv.corp_id  ");
        sql.append(" left join sys_corp sc2 on sc2.id = sc.parent_id ");
        sql.append(" left join sys_corp sc3 on sc3.id = sc2.parent_id ");
        sql.append(" where avv.type='0' and AVV.VALID_SIGN='1' and av.orig_no is not null and av.orig_no <>'' and a.opt_status='19' and a.status=95  ");
        sql.append(" and not exists (select 1 from ab_accrecords ba where  avv.id = ba.vvid and ba.brief_id ='004' and ba.opt_status='19' and ba.status=95 ) group by sc.name, avv.BANK_TYPE_ID, avv.type_name, avv.bank_acc, av.end_date, a.BAILPERCENT, sc.equityMaintenance_ID,\n" +
                "         sc.operationArea_ID, sc.id, avv.corp_id, sc2.id, sc2.name,sc3.id, sc3.name,sc.sort,sc2.sort,sc3.sort order by avv.bank_acc, a.BAILPERCENT,av.end_date ");
        sql.append(" ) m  ");
        sql.append(" left join sys_equity_maintenance sem on sem.equity_code = m.equityMaintenanceId ");
        sql.append(" left join ( ");
        sql.append(" select sum(cpi.PL_MONEY) as loanMoney,cpi.ONLINE_PAYMENT_ACC as bankAcc ");
        sql.append(" from cms_provide_loan_info cpi  where cpi.status >= 95 and cpi.REPAY_DATE >= bt_to_date(:startDate,'yyyy-MM-dd') and cpi.REPAY_DATE <= bt_to_date(:endDate,'yyyy-MM-dd') group by cpi.ONLINE_PAYMENT_ACC ");
        sql.append(" ) l on l.bankAcc = m.bankAcc ");
        sql.append(" where 1=1 ");
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "m.corpId", params);
        sql.append("and ").append(corpAuthSql).append(" ");
        sql.append(" ) a ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), WeeklyFundsDueScheduleVo.class, "a", true, params);
    }
}