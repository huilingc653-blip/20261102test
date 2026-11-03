package com.bytter.bill.domain.bill.dao;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.bill.api.bill.dto.AbBillPlanAmtMaintainDTO;
import com.bytter.bill.domain.bill.entity.AbBillPlanAmtMaintain;
import com.bytter.bill.domain.bill.vo.AbBillPlanAmtMaintainVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行计划开票金额维护业务层DAO层
 *
 * @author fangcf
 * @date 2025-06-30
 */
@Repository
public class AbBillPlanAmtMaintainDao extends BaseDao<AbBillPlanAmtMaintain, String> {

    /**
     * 查询银行计划开票金额维护列表
     *
     * @param input 入参
     * @return 出参
     */
    public DataTablesOutput dataTable(DataTablesInput input, List<String> authCorpList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.ID as id, m.corp_id as corpId, sc.name as corpName, m.bank_type_id as bankTypeId, bbt.type_name as bankTypeName, ");
        sql.append(" m.credit_type as creditType, m.WEEK_START_DAY as weekStartDay, m.WEEK_END_DAY as weekEndDay, m.CURR_WEEK_BILL_MONEY as currWeekBillMoney, m.NEXT_WEEK_BILL_MONEY as nextWeekBillMoney, ");
        sql.append(" m.create_by as createBy, m.create_time as createTime, m.update_by as updateBy, m.update_time as updateTime ");
        sql.append(" from AB_BILL_PLAN_AMT_MAINTAIN m ");
        sql.append(" left join SYS_CORP sc on sc.ID = m.CORP_ID ");
        sql.append(" left join BT_BANK_TYPE bbt on bbt.ID = m.BANK_TYPE_ID ");
        sql.append(" where 1 = 1 ");
        sql.append(" and ").append(SqlUtil.getInSql(authCorpList, "m.corp_id", true));
        // 修改时间倒序
        input.setOrderString(new OrderString("m.update_time", "desc"));
        return this.getDynamicBySQL(input, sql.toString(), AbBillPlanAmtMaintainVO.class, "m", false);
    }

    /**
     * 银行计划开票金额，相同单位、相同银行、相同周开始日、相同周结束日只能有一条记录
     *
     * @param corpId       单位id
     * @param bankTypeId   银行类型
     * @param weekStartDay 周开始日
     * @param weekEndDay   周结束日
     * @param id           主键id
     */
    public boolean checkUnique(String corpId, String bankTypeId, Date weekStartDay, Date weekEndDay, String id) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from AbBillPlanAmtMaintain where corpId = :corpId and bankTypeId = :bankTypeId and weekStartDay = TO_DATE(:weekStartDay, 'yyyy-MM-dd') and weekEndDay = TO_DATE(:weekEndDay, 'yyyy-MM-dd') ";
        if (StringUtils.isNotEmpty(id)) {
            hql += " and id != :id ";
            params.put("id", id);
        }
        params.put("corpId", corpId);
        params.put("bankTypeId", bankTypeId);
        params.put("weekStartDay", DateUtils.formatDate(weekStartDay));
        params.put("weekEndDay", DateUtils.formatDate(weekEndDay));
        List<AbBillPlanAmtMaintain> resultList = this.listByHQLAliasToBean(hql, params, AbBillPlanAmtMaintain.class);
        return CollectionUtils.isEmpty(resultList);
    }

    /**
     * 根据单位，银行类型，周开始日，周结束日，获取唯一的一条银行计划开票金额数据
     *
     * @param corpId       单位id
     * @param bankTypeId   银行类型
     * @param creditType   授信类型
     * @param weekStartDay 周开始日
     * @param weekEndDay   周结束日
     */
    public AbBillPlanAmtMaintainDTO getOneBy(String corpId, String bankTypeId, String creditType, Date weekStartDay, Date weekEndDay) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from AbBillPlanAmtMaintain where corpId = :corpId and bankTypeId = :bankTypeId and creditType = :creditType and weekStartDay = TO_DATE(:weekStartDay, 'yyyy-MM-dd') and weekEndDay = TO_DATE(:weekEndDay, 'yyyy-MM-dd') ";
        params.put("corpId", corpId);
        params.put("bankTypeId", bankTypeId);
        params.put("creditType", creditType);
        params.put("weekStartDay", DateUtils.formatDate(weekStartDay));
        params.put("weekEndDay", DateUtils.formatDate(weekEndDay));
        List<AbBillPlanAmtMaintainDTO> resultList = this.listByHQLAliasToBean(hql, params, AbBillPlanAmtMaintainDTO.class);
        return CollectionUtils.isEmpty(resultList) ? null : resultList.get(0);
    }
}
