package com.bytter.finance.domain.security.dao;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.dto.GuaContractCmsSecurityDTO;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsSecurityIssueDao extends BaseDao<CmsSecurityIssue, String> {
    /*
     * 根据登记id得到发行明细
     */
    public List<CmsSecurityIssueVO> getIssueForInfoId(String infoId, String flag) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select iss.id as id,iss.SECURITY_ISSUE_CODE as securityIssueCode,iss.UNDERWRITER as underwriter,iss.BATCH_NO as batchNo,iss.LEVEL_NAME as levelName, ");
        sql.append(
                " iss.ISSUE_MONEY as issueMoney,iss.ISSUE_DATE as issueDate,iss.EXPECT_END_DATE as expectEndDate,iss.RMK as rmk,iss.STATUS as status ");
        sql.append(" from CMS_SECURITY_ISSUE iss ");
        sql.append(" where iss.SECURITY_INFO_ID=:infoId");
        if (StringUtils.isEmpty(flag)) {// flag为空则查询发行页面要的数据，不为空为query则查询查询页面所有状态数据
            sql.append(" and iss.status in (-1,0,11)");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("infoId", infoId);
        sql.append("  order by iss.create_date ");
        return listBySQLAliasToBean(sql.toString(), params, CmsSecurityIssueVO.class);
    }
    /*
     * 根据登记id得到发行成功的明细
     */
    public List<CmsSecurityIssueVO> getSuccessIssueForInfoId(String infoId, List<String> bizStatuss) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select iss.id as id,iss.SECURITY_ISSUE_CODE as securityIssueCode,iss.UNDERWRITER as underwriter,iss.BATCH_NO as batchNo,iss.LEVEL_NAME as levelName, ");
        sql.append(
                " iss.ISSUE_MONEY as issueMoney,iss.ISSUE_DATE as issueDate,iss.EXPECT_END_DATE as expectEndDate,iss.RMK as rmk,iss.BIZ_STATUS as bizStatus,iss.RE_TO_ACCOUNT_MONEY as reToAccountMoney ");
        sql.append(" from CMS_SECURITY_ISSUE iss ");
        sql.append(" where iss.SECURITY_INFO_ID=:infoId");
        sql.append(" and iss.status in (95)");
        if (!EmptyUtils.isEmpty(bizStatuss)) {
            sql.append(" and ").append(getSQLIn(bizStatuss, 50, "iss.biz_status"));
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("infoId", infoId);
        sql.append("  order by iss.create_date ");
        return listBySQLAliasToBean(sql.toString(), params, CmsSecurityIssueVO.class);
    }
    /**
     * 根据发行编号查询对应数据
     *
     * @param securityIssueCode
     *            发行编号
     * @param id
     *            id
     * @return 发行数据
     */
    public List<CmsSecurityIssue> getSecurityIssueByCode(String securityIssueCode, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap<String, String>(4);
        sql.append(" select * from CMS_SECURITY_ISSUE iss where iss.SECURITY_ISSUE_CODE=:securityIssueCode ");
        sql.append(" and  iss.status not in (-2) ");
        if (id != null) {
            sql.append(" and iss.id != :id ");
            params.put("id", id);
        }
        params.put("securityIssueCode", securityIssueCode);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsSecurityIssue.class);
    }
    /**
     * 债券注册审批列表数据
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
                " select iss.id as id,iss.SECURITY_ISSUE_CODE as securityIssueCode,iss.UNDERWRITER as underwriter,iss.BATCH_NO as batchNo,iss.LEVEL_NAME as levelName, ");
        sql.append(
                " iss.ISSUE_MONEY as issueMoney,iss.ISSUE_DATE as issueDate,iss.EXPECT_END_DATE as expectEndDate,iss.RMK as rmk,iss.STATUS as status ");
        sql.append(" from CMS_SECURITY_ISSUE iss ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "iss.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsSecurityIssueVO.class, "", false, null, params);
    }
    public List<GuaContractCmsSecurityDTO> getGuaForSecurity() {
        StringBuilder sql = new StringBuilder();
        sql.append("select iss.SECURITY_ISSUE_CODE as securityIssueCode, ");
        sql.append("  iss.UNDERWRITER as underwriter,");
        sql.append("  iss.BATCH_NO as batchNo,");
        sql.append("  iss.LEVEL_NAME as levelName,");
        sql.append("  iss.ISSUE_MONEY as issueMoney,");
        sql.append("  iss.ISSUE_DATE as issueDate,");
        sql.append("  iss.EXPECT_END_DATE as expectEndDate, iss.id as id");
        sql.append("  from CMS_SECURITY_ISSUE iss ");
        sql.append("where iss.is_hang_guarantee = '1' ");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractCmsSecurityDTO.class);
    }

    public int updateByParam(Map<String,Object> updateMap, Map<String,Object> whereMap) {
        if(CollectionUtil.isEmpty(updateMap) || CollectionUtil.isEmpty(whereMap)){
            throw new ParameterException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        StringBuilder sql = new StringBuilder();
        sql.append("update cms_security_issue set ");
        if(updateMap.get("updateDate") != null){
            sql.append(" update_date = :updateDate ");
        }
        sql.append(" where 1=1 ");
        boolean b = false;
        if(whereMap.get("securityIssueCode") != null){
            sql.append(" and security_issue_code = :securityIssueCode ");
            updateMap.put("securityIssueCode",whereMap.get("securityIssueCode"));
            b = true;
        }
        if(whereMap.get("tenantId") != null){
            sql.append(" and tenant_id = :tenantId  ");
            updateMap.put("tenantId",whereMap.get("tenantId"));
            b = true;
        }
        if(whereMap.get("id") != null){
            sql.append(" and id = :id  ");
            updateMap.put("id",whereMap.get("id"));
            b = true;
        }
        if(!b){
            throw new ParameterException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        return createSQLQuery(sql.toString(), updateMap).executeUpdate();
    }
}
