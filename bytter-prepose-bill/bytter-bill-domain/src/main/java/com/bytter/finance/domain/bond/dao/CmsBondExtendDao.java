package com.bytter.finance.domain.bond.dao;
import com.bytter.finance.domain.bond.entity.CmsBondExtend;
import com.bytter.finance.domain.bond.vo.CmsBondExtendVO;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 债券展期Dao
 *
 * @author wangf
 */
@Repository
public class CmsBondExtendDao extends BaseDao<CmsBondExtend, String> {
    /**
     * 获取债券发行列表数据
     *
     * @param input
     *            前端参数
     * @param corpList
     *            单位集合
     * @return 债券发行列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input, List<String> corpList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = getCmsBondRegisterInfoVoSql();
        sql.append(" where (i.is_delete = :isDelete or i.is_delete is null) ");
        sql.append(" and (r.is_delete = :isDelete or r.is_delete is null) ");
        sql.append(" AND (I.CREATE_DATE IN (SELECT MAX(s.CREATE_DATE) ");
        sql.append(" FROM CMS_BOND_ISSUE_INFO S ");
        sql.append(" LEFT JOIN CMS_BOND_REGISTER_INFO R ON S.REGISTER_ID = R.ID GROUP BY S.REGISTER_ID)");
        sql.append(" OR i.create_date IS NULL) ");
        String corpSql = SqlUtil.getInSqlByParams(corpList, "r.CORP_ID", params);
        sql.append(" and ").append(corpSql).append(" ");
        // 状态用发债还是发行
        sql.append(" and r.status = :status95 and i.status= :status95");
        // 过滤审批通过数据
        params.put("status95", CmsConstant.BUSINESSSTATE_95);
        // 查询未删除的债券发行
        params.put("isDelete", CmsConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsBondRegisterInfoVo.class, "", false, null, params);
    }
    public DataTablesOutput getExtends(String registerId, DataTablesInput dataTablesInput) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = this.getIssueAndExendSql();
        sql.append(" where t.issue_id = i.id");
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and i.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" and t.status != :status_2 ");
        params.put("status_2", -2);
        sql.append(" order by i.create_date,t.create_date ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondExtendVO.class, "", false, null, params);
    }
    public CmsBondExtendVO getCmsBondExtendByRegisterId(String registerId) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = this.getIssueAndExendSql();
        sql.append(" where t.status <> -2 and (t.is_delete = :isDelete or t.is_delete is null)");
        // 查询未删除的
        params.put("isDelete", CmsConstant.IS_FALSE);
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and i.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" and t.status = :status95 ");
        // 只查暂存
        params.put("status95", CmsConstant.IS_FALSE);
        sql.append(" order by t.create_Date desc ");
        List<CmsBondExtendVO> list = listBySQLAliasToBean(sql.toString(), params, CmsBondExtendVO.class);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondExtendVO();
        }
        return list.get(0);
    }
    public CmsBondExtendVO getCmsBondExtendByIssueId(String issueId) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = this.getIssueAndExendSql();
        sql.append(" where i.id=t.issue_id ");
        if (StringUtils.isNotBlank(issueId)) {
            sql.append(" and i.id= :issueId ");
            params.put("issueId", issueId);
        }
        sql.append(" and t.status = :status95 ");
        // 过滤审批通过数据
        params.put("status95", CmsConstant.BUSINESSSTATE_95);
        sql.append(" order by t.create_Date desc ");
        List<CmsBondExtendVO> list = listBySQLAliasToBean(sql.toString(), params, CmsBondExtendVO.class);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondExtendVO();
        }
        return list.get(0);
    }
    public CmsBondExtendVO getCmsBondExtendId(String id) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = this.getIssueAndExendSql();
        sql.append(" where i.id=t.issue_id and t.status <> -2 and (t.is_delete = :isDelete or t.is_delete is null)");
        // 查询未删除的
        params.put("isDelete", CmsConstant.IS_FALSE);
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and t.id= :id ");
            params.put("id", id);
        }
        sql.append(" order by t.create_Date desc ");
        List<CmsBondExtendVO> list = listBySQLAliasToBean(sql.toString(), params, CmsBondExtendVO.class);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondExtendVO();
        }
        return list.get(0);
    }
    /**
     * 债券展期审批列表数据 返回列表的顺序与给定id的顺序相同，避免工作流merge掉，从而出现没有待审批的数据
     *
     * @param input
     *            前端查询参数
     * @param businessIdList
     *            业务id集合
     * @return 债券展期审批列表数据
     */
    public DataTablesOutput<CmsBondExtendVO> dataTableApprove(DataTablesInput input, List<String> businessIdList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = getCommonSql();
        sql.append(" where t.status = 11 ");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "t.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        DataTablesOutput<CmsBondExtendVO> output = getDynamicBySQL(input, sql.toString(), CmsBondExtendVO.class, "",
                false, null, params);
        if (EmptyUtils.isEmpty(output.getData())) {
            return output;
        }
        Map<String, CmsBondExtendVO> id2VO = Maps.newHashMap();
        for (CmsBondExtendVO vo : output.getData()) {
            id2VO.put(vo.getId(), vo);
        }
        List<CmsBondExtendVO> data = Lists.newArrayList();
        for (String id : businessIdList) {
            if (id2VO.containsKey(id)) {
                data.add(id2VO.get(id));
            }
        }
        output.setData(data);
        return output;
    }
    private StringBuilder getCommonSql() {
        StringBuilder sql = new StringBuilder(
                "select t.id,t.issue_id as issueId,t.extend_apply_date as extendApplyDate,t.extend_end_date as extendEndDate,t.last_extend_date  as lastExtendDate");
        sql.append(" ,t.extend_code as extendCode,t.extend_deadline extendDeadline ,t.deadline_unit as deadlineUnit");
        sql.append(
                " ,t.basic_point as basicPoint,t.extend_rate as extendRate,t.rmk ,t.is_delete as isDelete,t.status, i.bond_issue_num as bondIssueNum");
        sql.append(" from cms_bond_extend t ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on t.issue_id = i.id ");
        return sql;
    }
    private StringBuilder getIssueAndExendSql() {
        StringBuilder sql = new StringBuilder(
                "select t.id,i.register_id as registerId,i.id as issueId,i.bond_issue_num as bondIssueNum,i.issue_money as issueMoney,i.rmb_issue_total as outstandingAmount");
        sql.append(
                " ,t.extend_apply_date as extendApplyDate,t.extend_end_date as extendEndDate,t.last_extend_date  as lastExtendDate");
        sql.append(" ,t.extend_code as extendCode,t.extend_deadline extendDeadline ,t.deadline_unit as deadlineUnit");
        sql.append(" ,t.basic_point as basicPoint,t.extend_rate as extendRate,t.rmk ,t.is_delete as isDelete,t.status");
        sql.append(" from CMS_BOND_ISSUE_INFO i,cms_bond_extend t ");
        return sql;
    }
    private StringBuilder getCmsBondRegisterInfoVoSql() {
        StringBuilder sql = new StringBuilder(
                "SELECT r.ID as id, i.id as issueId,i.status as issueStatus, r.TYPE_ID AS typeId, r.CORP_ID AS corpId, r.CORP_NAME AS corpName,");
        sql.append(
                " r.ISSUE_NAME AS issueName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, r.BOND_CODE as bondCode,");
        sql.append(
                " r.BOND_SHORT_NAME as bondShortName, r.BOND_DEADLINE as bondDeadline, r.DEADLINE_UNIT as deadlineUnit, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName,r.ISSUE_CUR_TOTAL as issueCurTotal, r.ISSUE_RMB_TOTAL as issueRmbTotal, ");
        sql.append(
                " r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDate, ");
        sql.append(
                " r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk,");
        sql.append(" r.IS_TERRITORY as isTerritory,r.STATUS as status, ");
        sql.append(" r.is_delete as isDelete, r.ATTACHMENT_ID as attachmentId, t.bond_name as bondTypeName, ");
        sql.append(" i.BOND_ISSUE_NUM as bondIssueNum , T.deferredNum  AS deferredNum");
        sql.append(" from CMS_BOND_REGISTER_INFO r left join CMS_BOND_TYPE t on r.type_id = t.id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.REGISTER_ID = r.id ");
        sql.append(" LEFT JOIN (SELECT COUNT(RR.ID) AS deferredNum, RR.ID AS RID FROM CMS_BOND_REGISTER_INFO RR ");
        sql.append(
                " LEFT JOIN CMS_BOND_ISSUE_INFO II ON RR.ID = II.REGISTER_ID LEFT JOIN CMS_BOND_EXTEND D ON D.ISSUE_ID = II.ID");
        sql.append(" WHERE  D.STATUS = 95 GROUP BY RR.ID) T ON T.rID = R.ID ");
        return sql;
    }
    /**
     * 根据债券Id获取有效的债券发行编号
     *
     * @param registerId
     *            债券注册id
     * @return 债券信息
     */
    public List<CmsBondIssueInfoVo> listValidBondIssueNum(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select i.id as id, i.register_id as registerId, i.bond_issue_num as bondIssueNum, ");
        sql.append(" i.issue_money as issueMoney, i.rmb_issue_total as rmbIssueTotal, ");
        sql.append(
                " i.issue_start_date as issueStartDate,i.issue_end_date as issueEndDate, i.issue_price as issuePrice,i.issue_rmk issueRmk ");
        sql.append(" from CMS_BOND_ISSUE_INFO i ");
        sql.append(" LEFT JOIN cms_bond_repay_plan p ON p.issue_id = i.id  ");
        sql.append(" where 1 = 1 and (i.is_delete = :isDelete or i.is_delete is null) ");
        sql.append(" and p.is_save = '0' ");
        Map<String, String> params = new HashMap<String, String>();
        // 查询未删除的债券发行
        params.put("isDelete", CmsConstant.IS_FALSE);
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and i.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" and i.status = :status95 ");
        // 审批通过数据
        params.put("status95", CmsConstant.BUSINESSSTATE_95);
        sql.append(" order by i.create_Date desc ");
        List<CmsBondIssueInfoVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondIssueInfoVo.class);
        return list;
    }
}
