package com.bytter.finance.domain.security.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.security.entity.CmsSecurityPlan;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsSecurityPlanDao extends BaseDao<CmsSecurityPlan, String> {
    /**
     * 功能介绍 : 产证券化登记主信息审批通过数据
     */
    public DataTablesOutput getDataTableInfo(DataTablesInput dataTablesInput, List<String> corpAuth,
            List<String> bizStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select info.id,info.SECURITY_TYPE_ID as securityTypeId,type.TYPE_NAME as securityTypeName,info.SECURITY_CODE as securityCode,");
        sql.append(
                " info.SECURITY_SNAME as securitySname,info.SECURITY_NAME as securityName,info.CUR_ID as curId,info.CORP_ID as corpId,info.MONEY,info.Status,");
        //TODO 补充单位及币种名称
        sql.append(" info.CORP_NAME as corpName,info.CUR_NAME as curName ");
        sql.append(" from CMS_SECURITY_INFO info ");
        sql.append(" left join CMS_SECURITY_TYPE type on info.SECURITY_TYPE_ID=type.id ");
        sql.append(" where 1=1 ");
        sql.append(" and info.status in (95)");
        sql.append(
                "  and exists (select 1 from CMS_SECURITY_ISSUE iss where iss.SECURITY_INFO_ID=info.id and iss.biz_status in (:bizStatus)) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        OrderString orderString = new OrderString("info.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bizStatus", bizStatus);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsSecurityInfoVO.class, null, false, params);
    }
    /**
     * 计划审批列表数据
     *
     * @param input
     *            前端查询参数
     * @param businessIdList
     *            业务id集合
     * @return 债券注册审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessIdList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select plan.id as id,plan.SECURITY_ISSUE_CODE as securityIssueCode,iss.UNDERWRITER as underwriter,iss.BATCH_NO as batchNo,iss.LEVEL_NAME as levelName, ");
        sql.append(
                " plan.ISSUE_MONEY as issueMoney,iss.ISSUE_DATE as issueDate,iss.EXPECT_END_DATE as expectEndDate,iss.RMK as rmk ");
        sql.append(" from CMS_SECURITY_PLAN plan ");
        sql.append(" left join CMS_SECURITY_ISSUE iss on plan.SECURITY_ISSUE_CODE=iss.SECURITY_ISSUE_CODE ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "plan.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsSecurityPlanVO.class, "", false, null, params);
    }
    /**
     * 根据编号查主计划信息
     */
    public CmsSecurityPlanVO getPlanBySecurityCode(String securityIssueCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>(1);
        params.put("securityIssueCode", securityIssueCode);
        String sqlStr = " select * from CMS_SECURITY_PLAN  where SECURITY_ISSUE_CODE=:securityIssueCode order by create_date desc ";
        List<CmsSecurityPlanVO> list = listBySQLAliasToBean(sqlStr, params, CmsSecurityPlanVO.class);
        return list.isEmpty() ? null : list.get(0);
    }
}
