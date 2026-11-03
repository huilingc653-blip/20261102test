package com.bytter.finance.domain.bond.dao;
import com.bytter.finance.domain.bond.entity.CmsBondSwap;
import com.bytter.finance.domain.bond.vo.CmsBondSwapVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsBondSwapDao extends BaseDao<CmsBondSwap, String> {
    public DataTablesOutput<CmsBondSwap> getSwaps(DataTablesInput dataTablesInput, String issueId) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = this.getCommonSql();
        sql.append(" where t.status <> -2 ");
        if (StringUtils.isNotBlank(issueId)) {
            sql.append(" and t.issue_id=:issueId");
            params.put("issueId", issueId);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondSwapVO.class, "", false, null, params);
    }
    /**
     * 交债审批列表数据 返回列表的顺序与给定id的顺序相同，避免工作流merge掉，从而出现没有待审批的数据
     *
     * @param input
     *            前端查询参数
     * @param businessIdList
     *            业务id集合
     * @return 债券注册审批列表数据
     */
    public DataTablesOutput<CmsBondSwapVO> dataTableApprove(DataTablesInput input, List<String> businessIdList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = getCommonSql();
        sql.append(" where status = 11 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "t.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        DataTablesOutput<CmsBondSwapVO> output = getDynamicBySQL(input, sql.toString(), CmsBondSwapVO.class, "", false,
                null, params);
        if (EmptyUtils.isEmpty(output.getData())) {
            return output;
        }
        Map<String, CmsBondSwapVO> cmsBondSwapVO = Maps.newHashMap();
        for (CmsBondSwapVO vo : output.getData()) {
            cmsBondSwapVO.put(vo.getId(), vo);
        }
        List<CmsBondSwapVO> data = Lists.newArrayList();
        for (String id : businessIdList) {
            if (cmsBondSwapVO.containsKey(id)) {
                data.add(cmsBondSwapVO.get(id));
            }
        }
        output.setData(data);
        return output;
    }
    private StringBuilder getCommonSql() {
        StringBuilder sql = new StringBuilder(
                "select t.id,t.issue_id as issueId,t.swap_date as swapDate,t.swap_price as swapPrice,t.swap_amount as swapAmount");
        sql.append(" ,t.total_swap_amount as totalSwapAmount,t.total_swap_money as totalSwapMoney,t.status");
        sql.append(" from cms_bond_swap t");
        return sql;
    }
}
