package com.bytter.finance.domain.bond.dao;
import com.bytter.finance.domain.bond.entity.CmsBondTransfer;
import com.bytter.finance.domain.bond.vo.CmsBondTransferVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsBondTransferDao extends BaseDao<CmsBondTransfer, String> {
    public DataTablesOutput<CmsBondTransfer> getTransfers(DataTablesInput dataTablesInput, String issueId) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = this.getCommonSql();
        sql.append(" where t.status <> -2 ");
        /*
         * if (StringUtils.isNotBlank(issueId)) { sql.append(" and t.issue_id=:issueId"); params.put("issueId",
         * issueId); }
         */
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondTransferVO.class, "", false, null, params);
    }
    /**
     * 转债审批列表数据 返回列表的顺序与给定id的顺序相同，避免工作流merge掉，从而出现没有待审批的数据
     *
     * @param input
     *            前端查询参数
     * @param businessIdList
     *            业务id集合
     * @return 债券注册审批列表数据
     */
    public DataTablesOutput<CmsBondTransferVO> dataTableApprove(DataTablesInput input, List<String> businessIdList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = getCommonSql();
        sql.append(" where status = 11 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "t.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        DataTablesOutput<CmsBondTransferVO> output = getDynamicBySQL(input, sql.toString(), CmsBondTransferVO.class, "",
                false, null, params);
        if (EmptyUtils.isEmpty(output.getData())) {
            return output;
        }
        Map<String, CmsBondTransferVO> id2CmsBondTransferVO = Maps.newHashMap();
        for (CmsBondTransferVO cmsBondTransferVO : output.getData()) {
            id2CmsBondTransferVO.put(cmsBondTransferVO.getId(), cmsBondTransferVO);
        }
        List<CmsBondTransferVO> data = Lists.newArrayList();
        for (String id : businessIdList) {
            if (id2CmsBondTransferVO.containsKey(id)) {
                data.add(id2CmsBondTransferVO.get(id));
            }
        }
        output.setData(data);
        return output;
    }
    private StringBuilder getCommonSql() {
        StringBuilder sql = new StringBuilder(
                "select t.id as id,t.issue_id as issueId,t.transfer_date as transferDate,t.transfer_price as transferPrice,t.transfer_amount as transferAmount,");
        sql.append(
                " t.total_transfer_amount as totalTransferAmount,t.total_transfer_rate as totalTransferRate,t.total_transfer_money as totalTransferMoney,t.status as status");
        sql.append(" from CMS_BOND_TRANSFER t ");
        return sql;
    }
}
