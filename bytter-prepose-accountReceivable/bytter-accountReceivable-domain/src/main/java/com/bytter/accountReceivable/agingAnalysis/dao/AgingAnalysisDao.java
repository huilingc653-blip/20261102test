package com.bytter.accountReceivable.agingAnalysis.dao;

import com.bytter.accountReceivable.agingAnalysis.vo.AgingVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AgingAnalysisDao extends BaseDao<AgingVo, String> {

    //按客商
    public DataTablesOutput queryAgingByCus(DataTablesInput input, String corpInSql){
        Map<String, Object> params = input.getFormdata();
        //params.put("tenantId",getCurrentUserTenantId());
        StringBuilder sql = new StringBuilder();

        sql.append(" select * from( ");
        sql.append("with detail as(");
        sql.append(" select");
        sql.append(" dm.EXTERNAL_NAME externalName, ");
        sql.append(" dm.CORP_ID corpId,");
        sql.append(" dm.CUR_ID curId,");
        sql.append(" sc.name as corpName,");
        sql.append(" bc.cur_name as curName,");
        sql.append(" BT_NVL_NUM(dm.WRITE_OFF_BAL, dm.RECEIVABLE_MONEY) writeOffBal,");
        sql.append(" BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE) agingDay");
        sql.append(" from doc_manage dm");
        sql.append(" left join sys_corp sc on sc.id = dm.corp_id ");
        sql.append(" left join bt_currency bc on bc.id = dm.cur_id");
        sql.append(" where 1 = 1 and dm.status = 95");
        sql.append(" and BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE) >= 0");
        sql.append(" and (dm.WRITE_OFF_BAL <> 0 or dm.WRITE_OFF_BAL is null)");
        sql.append( StringUtils.isNotEmpty(corpInSql) ? " and" + corpInSql : "");
        sql.append( StringUtils.isNotEmpty(params.get("curId")) ? " and dm.CUR_ID = :curId" : "");
        //sql.append(  " and dm.TENANT_ID = :tenantId" );
        sql.append( ")");
        sql.append(" select s.externalName,s.corpId,s.curId,s.corpName,s.curName,");
        sql.append(" sum(s.writeOffBal) writeOffBal,");
        sql.append(" sum(CASE WHEN s.agingDay = 'thirtyDay' THEN s.writeOffBal ELSE 0 END) AS thirtyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'sixtyDay' THEN s.writeOffBal ELSE 0 END) AS sixtyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'ninetyDay' THEN s.writeOffBal ELSE 0 END) AS ninetyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'overNinetyDay' THEN s.writeOffBal ELSE 0 END) as overNinetyDay ");
        sql.append(" from( ");
        sql.append(" select t.externalName,t.corpId,t.curId,t.writeOffBal,t.corpName,t.curName,");
        sql.append(" case when t.agingDay>=0 and t.agingDay<= 30 then 'thirtyDay'");
        sql.append(" when t.agingDay>30 and t.agingDay<= 60 then 'sixtyDay'");
        sql.append(" when t.agingDay>60 and t.agingDay<= 90 then 'ninetyDay'");
        sql.append(" else 'overNinetyDay' end agingDay");
        sql.append(" from detail t");
        sql.append(" ) s group by s.externalName, s.corpId, s.curId, s.corpName, s.curName) tt order by tt.corpId");
        return getDynamicBySQL(input, sql.toString(), AgingVo.class,"", false, params);
    }

    public DataTablesOutput queryAgingByBill(DataTablesInput input, String corpInSql){
        Map<String,Object> params = input.getFormdata();
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from( ");
        sql.append("with detail as(");
        sql.append(" select  dm.code,");
        sql.append(" dm.EXTERNAL_NAME externalName,");
        sql.append(" dm.CORP_ID corpId,");
        sql.append(" dm.CUR_ID curId,");
        sql.append(" sc.name as corpName,");
        sql.append(" bc.cur_name as curName,");
        sql.append(" BT_NVL_NUM(dm.WRITE_OFF_BAL, dm.RECEIVABLE_MONEY) writeOffBal,");
        sql.append(" BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE) agingDay");
        sql.append(" from doc_manage dm");
        sql.append(" left join sys_corp sc on sc.id = dm.corp_id ");
        sql.append(" left join bt_currency bc on bc.id = dm.cur_id");
        sql.append(" where 1 = 1 and dm.status = 95");
        sql.append(" and BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE) >= 0");
        sql.append(" and (dm.WRITE_OFF_BAL <> 0 or dm.WRITE_OFF_BAL is null)");
        sql.append(StringUtils.isNotEmpty(corpInSql) ? " and" + corpInSql : "");
        sql.append(StringUtils.isNotEmpty(params.get("curId")) ? " and dm.CUR_ID = :curId" : "");
        sql.append(" )");
        sql.append(" select s.code,s.externalName,s.corpId,s.curId,s.corpName,s.curName,");
        sql.append(" sum(s.writeOffBal) writeOffBal,");
        sql.append(" sum(CASE WHEN s.agingDay = 'thirtyDay' THEN s.writeOffBal ELSE 0 END) AS thirtyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'sixtyDay' THEN s.writeOffBal ELSE 0 END) AS sixtyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'ninetyDay' THEN s.writeOffBal ELSE 0 END) AS ninetyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'overNinetyDay' THEN s.writeOffBal ELSE 0 END) as overNinetyDay ");
        sql.append(" from(");
        sql.append(" select t.code,t.externalName,t.corpId,t.curId,t.writeOffBal,t.corpName,t.curName,");
        sql.append(" case when t.agingDay>=0 and t.agingDay<= 30 then 'thirtyDay'");
        sql.append(" when t.agingDay>30 and t.agingDay<= 60 then 'sixtyDay'");
        sql.append(" when t.agingDay>60 and t.agingDay<= 90 then 'ninetyDay'");
        sql.append(" else 'overNinetyDay' end agingDay");
        sql.append(" from detail t");
        sql.append(" ) s group by s.code,s.externalName,s.corpId,s.curId,s.corpName,s.curName) tt order by tt.code");

        return getDynamicBySQL(input, sql.toString(), AgingVo.class,"", false, params);
    }

    public DataTablesOutput queryAgingByContract(DataTablesInput input, String corpInSql){
        Map<String, Object> params = input.getFormdata();
        StringBuilder sql = new StringBuilder();

        sql.append("select * from( ");
        sql.append("with detail as(");
        sql.append(" select dm.contract_code contractCode, dm.code,");
        sql.append(" dm.EXTERNAL_NAME externalName,");
        sql.append(" dm.CORP_ID corpId,");
        sql.append(" dm.CUR_ID curId,");
        sql.append(" sc.name as corpName,");
        sql.append(" bc.cur_name as curName,");
        sql.append(" BT_NVL_NUM(dm.WRITE_OFF_BAL, dm.RECEIVABLE_MONEY) writeOffBal,");
        sql.append(" BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE) agingDay");
        sql.append(" from doc_manage dm");
        sql.append(" left join sys_corp sc on sc.id = dm.corp_id ");
        sql.append(" left join bt_currency bc on bc.id = dm.cur_id");
        sql.append(" where 1 = 1 and dm.status = 95");
        sql.append(" and BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE) >= 0");
        sql.append(" and dm.contract_code is not null");
        sql.append(" and (dm.WRITE_OFF_BAL <> 0 or dm.WRITE_OFF_BAL is null)");
        sql.append(StringUtils.isNotEmpty(corpInSql) ? " and" + corpInSql : "");
        sql.append(StringUtils.isNotEmpty(params.get("curId")) ? " and dm.CUR_ID = :curId" : "");
        sql.append(" )");
        sql.append(" select s.contractCode,s.code,s.externalName,s.corpId,s.curId,s.corpName,s.curName,");
        sql.append(" sum(s.writeOffBal) writeOffBal,");
        sql.append(" sum(CASE WHEN s.agingDay = 'thirtyDay' THEN s.writeOffBal ELSE 0 END) AS thirtyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'sixtyDay' THEN s.writeOffBal ELSE 0 END) AS sixtyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'ninetyDay' THEN s.writeOffBal ELSE 0 END) AS ninetyDay, ");
        sql.append(" sum(CASE WHEN s.agingDay = 'overNinetyDay' THEN s.writeOffBal ELSE 0 END) as overNinetyDay ");
        sql.append(" from(");
        sql.append(" select t.contractCode,t.code,t.externalName,t.corpId,t.curId,t.writeOffBal,t.corpName,t.curName,");
        sql.append(" case when t.agingDay>=0 and t.agingDay<= 30 then 'thirtyDay'");
        sql.append(" when t.agingDay>30 and t.agingDay<= 60 then 'sixtyDay'");
        sql.append(" when t.agingDay>60 and t.agingDay<= 90 then 'ninetyDay'");
        sql.append(" else 'overNinetyDay' end agingDay");
        sql.append(" from detail t");
        sql.append(" ) s group by s.contractCode,s.code,s.externalName,s.corpId,s.curId,s.corpName,s.curName) tt order by tt.contractCode");

        return getDynamicBySQL(input, sql.toString(), AgingVo.class,"", false, params);
    }
}
