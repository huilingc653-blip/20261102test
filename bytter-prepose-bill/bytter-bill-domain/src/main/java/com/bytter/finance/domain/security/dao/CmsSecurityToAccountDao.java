package com.bytter.finance.domain.security.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.security.entity.CmsSecurityToAccount;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityToAccountVO;
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
public class CmsSecurityToAccountDao extends BaseDao<CmsSecurityToAccount, String> {
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
     * 到账确认审批列表数据
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
        sql.append("select *");
        sql.append(" from CMS_SECURITY_TO_ACCOUNT toa ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "toa.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsSecurityToAccountVO.class, "", false, null, params);
    }
    /**
     * 到账确认的数据
     *
     * @param Code
     *            资产证券化编号
     * @return 到账确认列表
     */
    public List<CmsSecurityToAccountVO> getIssueByCode(String Code) {
        Map<String, Object> parms = new HashMap<>(1);
        StringBuilder sql = new StringBuilder();
        sql.append("select *");
        sql.append(" from CMS_SECURITY_TO_ACCOUNT toa ");
        sql.append(" where 1=1 ");
        // sql.append(" and toa.STATUS in (95,11)");
        sql.append(" and toa.SECURITY_ISSUE_CODE=:Code");
        parms.put("Code", Code);
        return listBySQLAliasToBean(sql.toString(), parms, CmsSecurityToAccountVO.class);
    }
}
