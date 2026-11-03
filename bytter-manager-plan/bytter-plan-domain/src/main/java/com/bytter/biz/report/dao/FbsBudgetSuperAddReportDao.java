package com.bytter.biz.report.dao;

import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.biz.report.vo.FbsBudgetSuperAddReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FbsBudgetSuperAddReportDao extends BaseDao<FbsBudgetSuperadd, String> {
    /**
     * 计划明细查询列表
     * TODO 后续需要同步冗余字段数据 修改：
     * 把 cur_name 字段冗余到 fbs_budget 表
     * 把 corp_name 字段冗余到 fbs_budget 表
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput dataTablesInput, String joinSql) {

        return dataTableHasAddCountOrder(dataTablesInput, joinSql);
/*        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id as budgetId,case when f.corp_name is null then fcc.corp_name else f.corp_name end  as corpName,f.begin_date as beginDate, ");
        sql.append(" f.end_date as endDate,f.status as status,f.time_type as timeType,f.cur_id as curId,f.cur_name as curName, fcc.CORP_ID as corpId, ");
        sql.append(" (select count(*) from fbs_budget_superadd where budget_id = f.id  ) as addCount ");
        sql.append(" from fbs_budget f ");
//        sql.append(" left join sys_corp s on f.corp_id = s.id ");
//        sql.append(" left join bt_currency cur on f.cur_id = cur.id ");
        sql.append(" left join FBS_COLLECT_CORP fcc on f.corp_id = fcc.ID ");
        sql.append(" where 1 = 1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetSuperAddReportVO.class, "d", false);*/
    }


    public DataTablesOutput dataTableHasAddCountOrder(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from  (select f.id as budgetId,case when f.corp_name is null then fcc.corp_name else f.corp_name end  as corpName,f.begin_date as beginDate, ");
        sql.append(" f.end_date as endDate,f.status as status,f.time_type as timeType,f.cur_id as curId,f.cur_name as curName, fcc.CORP_ID as corpId, ");
        sql.append(" (select count(*) from fbs_budget_superadd where budget_id = f.id  ) as addCount ");
        sql.append(" from fbs_budget f ");
        sql.append(" left join FBS_COLLECT_CORP fcc on f.corp_id = fcc.ID ");
        sql.append(" where 1 = 1 ");
        Object eqs_timeType = dataTablesInput.getFormdata().get("eqs_timeType");
        Object ged_beginDate = dataTablesInput.getFormdata().get("ged_beginDate");
        Object ins_corpId = dataTablesInput.getFormdata().get("ins_corpId");
        Object led_endDate = dataTablesInput.getFormdata().get("led_endDate");
        dataTablesInput.getFormdata().remove("eqs_timeType");
        dataTablesInput.getFormdata().remove("ged_beginDate");
        dataTablesInput.getFormdata().remove("ins_corpId");
        dataTablesInput.getFormdata().remove("led_endDate");
        if (!EmptyUtils.isEmpty(eqs_timeType)) {
            sql.append(" and f.time_type = '").append(eqs_timeType).append("' ");
        }
        String dialect = ((SessionImpl) getSession()).getFactory().getDialect().getClass().getName();
        if (!EmptyUtils.isEmpty(ged_beginDate)&&EmptyUtils.isNotEmpty(ged_beginDate.toString())) {
            if (dialect.toLowerCase().contains("oracle")) {//如果是oracle数据库
                sql.append(" and f.begin_date >= to_date('").append(ged_beginDate).append("', 'yyyy-MM-dd')  ");
            } else {
                sql.append(" and f.begin_date >= '").append(ged_beginDate).append("' ");
            }
        }
        if (!EmptyUtils.isEmpty(ins_corpId)) {
            sql.append(" and ").append(getSQLIn(Arrays.asList(ins_corpId.toString().split(",")), 900, "f.corp_id"));
        }
        if (!EmptyUtils.isEmpty(led_endDate)&&EmptyUtils.isNotEmpty(led_endDate.toString())) {
            if (dialect.toLowerCase().contains("oracle")) {//如果是oracle数据库
                sql.append(" and f.end_date <= to_date('").append(led_endDate).append("', 'yyyy-MM-dd') ");
            } else {
                sql.append(" and f.end_date <= '").append(led_endDate).append("' ");
            }

        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql).append(" ) f ");
        }
//        Column column=getOrder(dataTablesInput);
//        if (!EmptyUtils.isEmpty(column)) {
//            dataTablesInput.setOrder(new ArrayList<>());
//            sql.append(" order by ").append(column.getName()).append(" ").append(column.getData());
//        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetSuperAddReportVO.class, "d", false);
    }


    private Column getOrder(DataTablesInput dataTablesInput) {
        List<Column> columns = dataTablesInput.getColumns();
        List<Order> orders = dataTablesInput.getOrder();
        if (!EmptyUtils.isEmpty(orders) && !EmptyUtils.isEmpty(columns)) {
            Column column=new Column();
            String columName = columns.get(orders.get(0).getColumn()).getName();
            if ("statusStr".equals(columName)) {
                columName = "status";
            }
            if ("timeTypeName".equals(columName)) {
                columName = "timeType";
            }
            column.setData(orders.get(0).getDir());
            column.setName(columName);
            return column;
        }
        return null;
    }



}
