package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranApp;
import com.bytter.biz.foreign.vo.FexTranAppVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class FexTranAppDao extends BaseDao<FexTranApp, String> {

    /**
     * 查询交易需求暂存喝打回数据
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,");
        sql.append("        t.bill_no,");
        sql.append("        t.corp_id,");
        sql.append("        corp.name corpName,");
        sql.append("        t.rmk,");
        sql.append("        t.status,");
        sql.append("        t.app_date,");
        sql.append("        t.create_by,");
        sql.append("        t.create_date,");
        sql.append("        t.update_by,");
        sql.append("        t.update_date");
        sql.append("        from FEX_TRAN_APP t");
        sql.append(" left join sys_corp corp");
        sql.append("    on t.corp_id = corp.id");
        sql.append(" where 1 = 1 ");
        sql.append(" and ( t.status = " + ForeignConstant.STATUE_TEMP);
        sql.append(" or  t.status = "+ForeignConstant.STATUE_BACK+" ) ");
        sql.append(" and ( " + getSQLIn(ids, 900, "t.corp_id") + " ) ");
        input.setOrderString(new OrderString("t.create_date", "desc"));
        return getDynamicBySQL(input, sql.toString(), FexTranAppVO.class, "t", false, null);
    }

    /**
     * 交易需求查询
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput dataTableQuery(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,");
        sql.append("        t.bill_no,");
        sql.append("        t.corp_id,");
        sql.append("        corp.name corpName,");
        sql.append("        t.rmk,");
        sql.append("        t.status,");
        sql.append("        t.app_date,");
        sql.append("        t.create_by,");
        sql.append("        t.create_date,");
        sql.append("        t.update_by,");
        sql.append("        t.update_date");
        sql.append("        from FEX_TRAN_APP t");
        sql.append(" left join sys_corp corp");
        sql.append("    on t.corp_id = corp.id");
        sql.append(" where 1 = 1 ");
        sql.append(" and  t.status != " + ForeignConstant.STATUE_DEL);
        sql.append(" and ( " + getSQLIn(ids, 900, "t.corp_id") + " ) ");
        input.setOrderString(new OrderString("t.create_date", "desc"));
        return getDynamicBySQL(input, sql.toString(), FexTranAppVO.class, "t", false, null);
    }

    /**
     * 查询交易需求数据
     *
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> corpIds, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,");
        sql.append("        t.bill_no,");
        sql.append("        t.corp_id,");
        sql.append("        corp.name corpName,");
        sql.append("        t.rmk,");
        sql.append("        t.status,");
        sql.append("        t.app_date,");
        sql.append("        t.create_by,");
        sql.append("        t.create_date,");
        sql.append("        t.update_by,");
        sql.append("        t.update_date");
        sql.append("        from FEX_TRAN_APP t");
        sql.append(" left join sys_corp corp");
        sql.append("    on t.corp_id = corp.id");
        sql.append(" where 1 = 1 ");
        String flowStatus = input.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and t.status = " + ForeignConstant.STATUE_WAIT_CHECK);
        }
        sql.append(" and ( " + getSQLIn(corpIds, 900, "t.corp_id") + " ) ");
        sql.append(" and ( " + getSQLIn(ids, 900, "t.id")+") ");
        OrderString orderString = new OrderString("t.create_date","desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), FexTranAppVO.class, "t", false, null);
    }

    /**
     * 根据单位统计交易需求
     * @param corpId
     * @param busType del:单位删除 disable:禁用 change:上级变更
     * @return
     */
    public int getTranAppCountByCorpId(String corpId, String busType){
        String sql="select count(*) from fex_tran_app where corp_id=:corpId and status<>:status";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("status", CommonConstant.STATUE_DELETE);
        if(!"del".equals(busType)){
            //0-暂存、11-待审批、-1-审批打回
            sql+=" and status in ('0','11','-1')";
        }
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }



    /**
     *
     * 获取未结束交易单
     *
     * @param templateIds
     * @return
     */
    public List<String> getUnFinishedBillNos(List<String> templateIds) {
        if(EmptyUtils.isEmpty(templateIds)){
            return new ArrayList<>();
        }
        // 1-暂存；2-打回；4-待审批
        String sql = "select fta.bill_no from FEX_TRAN_APP fta  " +
                " left join fex_tran_app_dtl ftad on ftad.app_id=fta.id where   fta.status in ('0','11','-1') ";
        String sqlWithIn = sql + " and " + getSQLIn(templateIds, 900, "ftad.template_id");
        return this.getSession().createSQLQuery(sqlWithIn).list();
    }
}
