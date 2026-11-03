package com.bytter.finance.domain.bond.dao;
import com.bytter.finance.domain.bond.entity.CmsBondRegisterInfo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 债券注册dao
 *
 * @author ouyj
 */
@Repository
public class CmsBondRegisterInfoDao extends BaseDao<CmsBondRegisterInfo, String> {
    /**
     * 获取债券注册列表数据
     *
     * @param input
     *            前端参数
     * @param corpList
     *            单位集合
     * @return 债券注册列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input, List<String> corpList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = getCommonSql();
        sql.append(" where (r.is_delete = :isDelete or r.is_delete is null) ");
        String corpSql = SqlUtil.getInSqlByParams(corpList, "r.CORP_ID", params);
        sql.append(" and ").append(corpSql).append(" ");
        sql.append(" and r.status != :status95 ");
        // 过滤审批通过数据
        params.put("status95", CmsConstant.BUSINESSSTATE_95);
        // 查询未删除的债券注册
        params.put("isDelete", CmsConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsBondRegisterInfoVo.class, "", false, null, params);
    }
    /**
     * 获取债券注册公共查询sql
     *
     * @return 公共查询sql
     */
    private StringBuilder getCommonSql() {
        StringBuilder sql = new StringBuilder(
                "SELECT r.ID as id, r.TYPE_ID AS typeId, r.CORP_ID AS corpId, r.CORP_NAME AS corpName,");
        sql.append(
                " r.ISSUE_NAME AS issueName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, r.BOND_CODE as bondCode,");
        sql.append(
                " r.BOND_SHORT_NAME as bondShortName, r.BOND_DEADLINE as bondDeadline, r.DEADLINE_UNIT as deadlineUnit, ");
        //TODO 补充币种名称相关信息
        sql.append(" r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName,r.ISSUE_CUR_TOTAL as issueCurTotal, r.ISSUE_RMB_TOTAL as issueRmbTotal, ");
        sql.append(
                " r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDate, ");
        sql.append(
                " r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk,");
        sql.append(
                " r.IS_TERRITORY as isTerritory, r.STATUS as status, r.is_delete as isDelete, r.ATTACHMENT_ID as attachmentId, t.bond_name as bondTypeName ");
        sql.append(" from CMS_BOND_REGISTER_INFO r left join CMS_BOND_TYPE t on r.type_id = t.id ");
        return sql;
    }
    /**
     * 获取债券注册列表数据
     *
     * @param input
     *            前端参数
     * @param corpList
     *            单位集合
     * @param flag
     *            可转债或可交债
     * @return 债券注册列表数据
     */
    public DataTablesOutput getTransferableDataTable(DataTablesInput input, List<String> corpList, boolean flag) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder(
                "SELECT r.ID as id, r.TYPE_ID AS typeId, r.CORP_ID AS corpId, r.CORP_NAME AS corpName,");
        sql.append(
                " r.ISSUE_NAME AS issueName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, r.BOND_CODE as bondCode,");
        sql.append(
                " r.BOND_SHORT_NAME as bondShortName, r.BOND_DEADLINE as bondDeadline, r.DEADLINE_UNIT as deadlineUnit, ");
        //TODO 补充币种名称信息
        sql.append(" r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName, r.ISSUE_CUR_TOTAL as issueCurTotal, r.ISSUE_RMB_TOTAL as issueRmbTotal, ");
        sql.append(" r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate,  ");
        sql.append("  r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk,");
        sql.append(
                " r.IS_TERRITORY as isTerritory, r.STATUS as status, r.is_delete as isDelete, r.ATTACHMENT_ID as attachmentId, t.bond_name as bondTypeName,i.id as issueId,i.status as issueStatus");
        sql.append(
                " from CMS_BOND_REGISTER_INFO r left join CMS_BOND_TYPE t on r.type_id = t.id left join CMS_BOND_ISSUE_INFO i on r.id = i.REGISTER_ID ");
        sql.append(" where (r.is_delete = :isDelete or r.is_delete is null) ");
        sql.append(" and t.BOND_NAME = :bondName ");
        String corpSql = SqlUtil.getInSqlByParams(corpList, "r.CORP_ID", params);
        sql.append(" and ").append(corpSql).append(" ");
        sql.append(" and i.status = :status95 ");
        sql.append(" AND (I.CREATE_DATE IN (SELECT MAX(s.CREATE_DATE) ");
        sql.append(" FROM CMS_BOND_ISSUE_INFO S ");
        sql.append(" LEFT JOIN CMS_BOND_REGISTER_INFO R ON S.REGISTER_ID = R.ID GROUP BY S.REGISTER_ID)");
        sql.append(" OR i.create_date IS NULL) ");
        params.put("bondName", flag ? "可转债" : "可交债");
        // 过滤审批通过数据
        params.put("status95", CmsConstant.BUSINESSSTATE_95);
        // 查询未删除的债券注册
        params.put("isDelete", CmsConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsBondRegisterInfoVo.class, "", false, null, params);
    }
    /**
     * 获取债券注册列表数据
     *
     * @param input
     *            前端参数
     * @param corpList
     *            单位集合
     * @return 债券注册列表数据
     */
    public DataTablesOutput getExtendDataTable(DataTablesInput input, List<String> corpList) {
        Map<String, Object> params = new HashMap<>(1);
        //TODO 补充币种名称
        StringBuilder sql = new StringBuilder(
                "SELECT r.ID as id, r.TYPE_ID AS typeId, r.CORP_ID AS corpId, r.CORP_NAME AS corpName,r.BOND_CODE as bondCode,r.CUR_NAME as bondCurName,");
        sql.append(
                " t.bond_name as bondTypeName, r.BOND_FULL_NAME as bondFullName,r.BOND_SHORT_NAME as bondShortName, i.LEAD_UNDERWRITER as underwriter,");
        sql.append(" r.STATUS as status,i.id as issueId,i.status as issueStatus");
        sql.append(
                " from CMS_BOND_REGISTER_INFO r left join CMS_BOND_TYPE t on r.type_id = t.id left join CMS_BOND_ISSUE_INFO i on r.id = i.REGISTER_ID ");
        sql.append(" where (r.is_delete = :isDelete or r.is_delete is null) ");
        /*
         * String corpSql = SqlUtil.getInSqlByParams(corpList, "r.CORP_ID", params);
         * sql.append(" and ").append(corpSql).append(" "); sql.append(" and r.status != :status95 "); // 过滤审批通过数据 TODO
         * 债券注册提交审批通过的信息 params.put("status95", CmsConstant.BUSINESSSTATE_95);
         */
        // 查询未删除的债券注册
        params.put("isDelete", CmsConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsBondRegisterInfoVo.class, "", false, null, params);
    }
    /**
     * 判断该债券注册是否被引用
     *
     * @param id
     *            债券注册id
     * @return 是否被引用 true被引用
     */
    public boolean isQuoteType(String id) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder("select i.id from CMS_BOND_ISSUE_INFO i ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on r.id = i.register_id where i.register_id = :id ");
        params.put("id", id);
        List<CmsBondRegisterInfoVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRegisterInfoVo.class);
        return !CollectionUtils.isEmpty(list);
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
        StringBuilder sql = getCommonSql();
        sql.append(" where 1 = 1");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "r.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsBondRegisterInfoVo.class, "", false, null, params);
    }
    /**
     * 根据id集合查询债券注册表数据
     *
     * @param dataTablesInput
     *            前端传参
     * @param registerIdList
     *            id集合
     * @return 债券注册表数据
     */
    public DataTablesOutput getDataTablesByIds(DataTablesInput dataTablesInput, List<String> registerIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select r.id as id, ");
        sql.append(" r.CORP_ID as corpId, r.CORP_NAME as corpName, r.TYPE_ID as typeId, t.BOND_NAME as bondTypeName, ");
        sql.append(" r.ISSUE_NAME as issueName, r.BOND_CODE as bondCode, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_SHORT_NAME as bondShortName, r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName, r.ISSUE_RMB_TOTAL as issueCurTotal, ");
        sql.append(" r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDte, ");
        sql.append(" r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk, ");
        sql.append(" r.IS_TERRITORY as isTerritory, r.STATUS as status, r.IS_DELETE as isDelete, r.ATTACHMENT_ID as attachmentId, ");
        sql.append(" r.create_By as createBy, r.create_Date as createDate, r.update_By as updateBy, r.update_Date as updateDate ");
        sql.append(" from CMS_BOND_REGISTER_INFO r ");
        sql.append(" left join CMS_BOND_TYPE t on r.TYPE_ID = t.id ");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(registerIdList, 900, "r.id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondRegisterInfoVo.class, null, false);
    }
    /**
     * 根据id获取详情数据
     *
     * @param id
     *            id
     * @return 债券注册vo
     */
    public CmsBondRegisterInfoVo getInfoById(String id) {
        StringBuilder sql = getCommonSql();
        sql.append(" where 1 = 1 and r.id = :id  ");
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, CmsBondRegisterInfoVo.class);
    }
    /**
     * 根据债券注册id获取对于债券vo信息
     *
     * @param registerId
     *            id
     * @return 债券vo信息
     */
    public List<CmsBondRegisterInfoVo> getRegisterById(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select r.id as id, ");
        sql.append(" r.CORP_ID as corpId, r.CORP_NAME as corpName, r.TYPE_ID as typeId, t.BOND_NAME as bondTypeName, ");
        sql.append(
                " r.ISSUE_NAME as issueName, r.BOND_CODE as bondCode, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, ");
        //TODO 补充币种名称
        sql.append(
                " r.BOND_SHORT_NAME as bondShortName, r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName, r.ISSUE_RMB_TOTAL as issueCurTotal, r.issue_rmb_total as issueRmbTotal,  ");
        sql.append(
                " r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDate, ");
        sql.append(
                " r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk, ");
        sql.append(" r.BOND_DEADLINE as bondDeadline, r.DEADLINE_UNIT as deadlineUnit, ");
        sql.append(
                " r.IS_TERRITORY as isTerritory, r.STATUS as status, r.IS_DELETE as isDelete, r.ATTACHMENT_ID as attachmentId, ");
        sql.append(
                " r.create_By as createBy, r.create_Date as createDate, r.update_By as updateBy, r.update_Date as updateDate ");
        sql.append(" from CMS_BOND_REGISTER_INFO r ");
        sql.append(" left join CMS_BOND_TYPE t on r.TYPE_ID = t.id ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and r.id= :registerId ");
            params.put("registerId", registerId);
        }
        List<CmsBondRegisterInfoVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRegisterInfoVo.class);
        return list;
    }
    /**
     * 根据发行id获取单位id
     * 
     * @param issueId
     *            发行id
     * @return 单位id
     */
    public String getRegisterCorpByIssueId(String issueId) {
        String sql = "select r.corp_id as corpId from cms_bond_issue_info i left join cms_bond_register_info r on r.id = i.register_id where i.id = :id ";
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", issueId);
        List<CmsBondRegisterInfo> registerInfoList = this.listBySQLAliasToBean(sql, params, CmsBondRegisterInfo.class);
        return CollectionUtils.isEmpty(registerInfoList) ? null : registerInfoList.get(0).getCorpId();
    }
    /**
     * 通过债券代码或债券全称获取对应债券注册信息
     *
     * @param bondCode
     *            债券代码
     * @param bondFullName
     *            债券全称
     * @param id
     *            id
     * @return 债券注册信息
     */
    public CmsBondRegisterInfoVo getRegisterByCodeOrFullName(String bondCode, String bondFullName, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap(4);
        sql.append(" select i.id as id from CMS_BOND_REGISTER_INFO i where 1 = 1 ");
        sql.append(" and (i.is_delete = :isDelete or i.is_delete is null) ");
        if (id != null) {
            sql.append(" and i.id != :id ");
            params.put("id", id);
        }
        if (StringUtils.isNotEmpty(bondCode)) {
            sql.append(" and i.bond_code = :bondCode ");
            params.put("bondCode", bondCode);
        }
        if (StringUtils.isNotEmpty(bondFullName)) {
            sql.append(" and i.bond_full_name = :bondFullName ");
            params.put("bondFullName", bondFullName);
        }
        params.put("isDelete", CmsConstant.IS_FALSE);
        return this.getBySQLAliasToBean(sql.toString(), params, CmsBondRegisterInfoVo.class);
    }
}
