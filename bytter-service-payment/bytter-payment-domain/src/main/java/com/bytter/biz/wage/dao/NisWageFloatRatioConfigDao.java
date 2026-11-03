package com.bytter.biz.wage.dao;

import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.wage.entity.NisWageFloatRatioConfig;
import com.bytter.biz.wage.vo.NisWageFloatRatioConfigVO;
import com.bytter.biz.wage.vo.NisWageFloatRatioMessageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 代发工资浮动比率配置持久层
 *
 * @author fangcf
 * @date 2025-11-20
 */
@Repository
public class NisWageFloatRatioConfigDao extends BaseDao<NisWageFloatRatioConfig, String> {

    /**
     * 查询银行计划开票金额维护列表
     *
     * @param input 入参
     * @return 出参
     */
    public DataTablesOutput<NisWageFloatRatioConfigVO> dataTable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.ID as id, c.month as month, c.float_rate as floatRate, ");
        sql.append(" c.create_by as createBy, c.create_time as createTime, c.update_by as updateBy, c.update_time as updateTime ");
        sql.append(" from NIS_WAGE_FLOAT_RATIO_CONFIG c ");
        // 过滤已删除的数据
        sql.append(" where c.IS_DELETED = '0' ");
        // 修改时间倒序
        input.setOrderString(new OrderString("c.update_time", "desc"));
        return this.getDynamicBySQL(input, sql.toString(), NisWageFloatRatioConfigVO.class, "c", false);
    }

    /**
     * 同月份只允许有一条数据
     *
     * @param month 月份
     * @param id    主键id
     * @return 是否
     */
    public boolean checkUnique(String month, String id) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from NisWageFloatRatioConfig where isDeleted = '0' and month = :month ";
        if (StringUtils.isNotEmpty(id)) {
            hql += " and id != :id ";
            params.put("id", id);
        }
        params.put("month", month);
        List<NisWageFloatRatioConfig> resultList = this.listByHQLAliasToBean(hql, params, NisWageFloatRatioConfig.class);
        return CollectionUtils.isEmpty(resultList);
    }

    /**
     * 操作记录查询列表
     *
     * @param input 入参
     * @return 操作记录列表
     */
    public DataTablesOutput<NisWageFloatRatioMessageVO> datatableMessage(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id, n.enrol_Date as enrolDate, n.bill_No as billNo, n.op as op, n.op_Name as opName, ");
        sql.append(" n.message_Type as messageType, n.message as message, n.bus_Type as busType, ");
        sql.append(" c.MONTH as month ");
        sql.append(" from NIS_MESSAGE n ");
        sql.append(" inner join NIS_WAGE_FLOAT_RATIO_CONFIG c on c.id = n.bill_No ");
        sql.append(" where n.bus_Type = '").append(NisMessageConstant.BUS_TYPE_WAGE_FLOAT_RATIO).append("' ");
        // 操作时间倒序
        input.setOrderString(new OrderString("n.enrol_Date", "desc"));
        return this.getDynamicBySQL(input, sql.toString(), NisWageFloatRatioMessageVO.class, "n", false);
    }

    /**
     * 根据月份获取代发工资浮动比率配置表
     *
     * @param month 月份
     * @return 对应月份代发工资浮动比率配置表
     */
    public NisWageFloatRatioConfig getByMonth(String month) {
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from NisWageFloatRatioConfig where isDeleted = '0' and month = :month ";
        params.put("month", month);
        List<NisWageFloatRatioConfig> list = this.listByHQLAliasToBean(hql, params, NisWageFloatRatioConfig.class);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().findFirst().orElse(null);
        }
        return null;
    }
}
