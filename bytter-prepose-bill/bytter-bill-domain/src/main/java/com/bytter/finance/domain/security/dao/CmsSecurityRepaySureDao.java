package com.bytter.finance.domain.security.dao;
import com.bytter.finance.domain.security.entity.CmsSecurityRepaySure;
import com.bytter.finance.domain.security.vo.CmsSecurityInfoVO;
import com.bytter.finance.domain.security.vo.CmsSecurityRepaySureVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsSecurityRepaySureDao extends BaseDao<CmsSecurityRepaySure, String> {
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
     * 获取主信息对应的还款确认信息
     */
    public List<CmsSecurityRepaySure> listRepaySure(CmsSecurityRepaySureVO vo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>(1);
        sql.append(" select * from CMS_SECURITY_REPAY_SURE sure where 1=1");
        sql.append(" and sure.IS_REPAY<>'1'");
        if (!EmptyUtils.isEmpty(vo.getSecurityInfoId())) {
            sql.append(" and sure.SECURITY_INFO_ID=:securityInfoId");
            params.put("securityInfoId", vo.getSecurityInfoId());
        }
        if (!EmptyUtils.isEmpty(vo.getSecurityIssueCode())) {
            sql.append(" and sure.SECURITY_ISSUE_CODE like '%" + vo.getSecurityIssueCode() + "%'");
        }
        if (!EmptyUtils.isEmpty(vo.getLevelName())) {
            sql.append(" and sure.LEVEL_NAME like '%" + vo.getLevelName() + "%'");
        }
        if (!EmptyUtils.isEmpty(vo.getProductName())) {
            sql.append(" and sure.PRODUCT_NAME like '%" + vo.getProductName() + "%'");
        }
        if (!EmptyUtils.isEmpty(vo.getGedRepayDate())) {
            sql.append(" and sure.REPAY_DATE>=to_date(:gedRepayDate,'yyyy-MM-dd')");
            params.put("gedRepayDate", DateUtils.format(vo.getGedRepayDate(), "yyyy-MM-dd"));
        }
        if (!EmptyUtils.isEmpty(vo.getLedRepayDate())) {
            sql.append(" and sure.REPAY_DATE<=to_date(:ledRepayDate,'yyyy-MM-dd')");
            params.put("ledRepayDate", DateUtils.format(vo.getLedRepayDate(), "yyyy-MM-dd"));
        }
        if (!EmptyUtils.isEmpty(vo.getIsChose())) {
            sql.append(" and sure.IS_CHOSE = :isChose");
            params.put("isChose", vo.getIsChose());
        }
        return listBySQLAliasToBean(sql.toString(), params, CmsSecurityRepaySure.class);
    }
    /**
     * 获取主信息对应的还款确认信息
     */
    public List<CmsSecurityRepaySure> listRepaySureView(CmsSecurityRepaySureVO vo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>(1);
        sql.append(" select * from CMS_SECURITY_REPAY_SURE sure where 1=1");
        if (!EmptyUtils.isEmpty(vo.getSecurityInfoId())) {
            sql.append(" and sure.SECURITY_INFO_ID=:securityInfoId");
            params.put("securityInfoId", vo.getSecurityInfoId());
        }
        if (!EmptyUtils.isEmpty(vo.getSecurityIssueCode())) {
            sql.append(" and sure.SECURITY_ISSUE_CODE like '%" + vo.getSecurityIssueCode() + "%'");
        }
        if (!EmptyUtils.isEmpty(vo.getLevelName())) {
            sql.append(" and sure.LEVEL_NAME like '%" + vo.getLevelName() + "%'");
        }
        if (!EmptyUtils.isEmpty(vo.getProductName())) {
            sql.append(" and sure.PRODUCT_NAME like '%" + vo.getProductName() + "%'");
        }
        if (!EmptyUtils.isEmpty(vo.getGedRepayDate())) {
            sql.append(" and sure.REPAY_DATE>=to_date(:gedRepayDate,'yyyy-MM-dd')");
            params.put("gedRepayDate", DateUtils.format(vo.getGedRepayDate(), "yyyy-MM-dd"));
        }
        if (!EmptyUtils.isEmpty(vo.getLedRepayDate())) {
            sql.append(" and sure.REPAY_DATE<=to_date(:ledRepayDate,'yyyy-MM-dd')");
            params.put("ledRepayDate", DateUtils.format(vo.getLedRepayDate(), "yyyy-MM-dd"));
        }
        return listBySQLAliasToBean(sql.toString(), params, CmsSecurityRepaySure.class);
    }
    /**
     * 还款确认审批列表数据
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
        sql.append(" select * ");
        sql.append(" from CMS_SECURITY_REPAY_SURE sure ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "sure.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsSecurityRepaySureVO.class, "", false, null, params);
    }
    /**
     * 删除发行对应的还未还款的数据
     */
    public void deleteBySecurityIssueCode(String securityIssueCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " delete from CMS_SECURITY_REPAY_SURE where IS_REPAY<>'1' and SECURITY_ISSUE_CODE=:securityIssueCode");
        Map<String, Object> params = new HashMap<>(1);
        params.put("securityIssueCode", securityIssueCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
