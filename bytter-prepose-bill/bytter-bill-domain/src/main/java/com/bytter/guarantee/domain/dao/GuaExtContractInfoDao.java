package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.GuaContractInfo;
import com.bytter.guarantee.domain.vo.GuaExtContractInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * @author luxy
 * @Title: GuaExtContractInfoDao
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同登记数据访问层
 * @date 2020/5/9 9:43
 */
@Repository
public class GuaExtContractInfoDao extends BaseDao<GuaContractInfo, String> {
    public StringBuilder commonSql(StringBuilder sql, List<String> corpAuth) {
        sql.append(" SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,")
                .append(" B.DEBTOR_PERSON  AS debtorPerson,B.DEBTOR_PERSON as debtorPersonName,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,")
                .append(" B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,b.cur_name as curName,b.corp_name as corpName, ")
                .append(" B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate, ")
                .append(" B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,B.CON_ID as conId,BT_NVL(B.INNER_GUA,'0') AS innerGua,B.DRAWMONEY_DATE as drawMoneyDate  ")
                .append(" FROM GUA_CONTRACT_INFO B ")
                .append(" where B.business_type = 1 ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return sql;
    }
    /**
     * 获取对外担保合同登记列表数据
     */
    public DataTablesOutput extContractInfoData(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, corpAuth);
        sql.append("AND B.STATUS IN(0,11,-1) ");// 登记暂存，待审批，打回的数据
        return getDynamicBySQL(input, sql.toString(), GuaExtContractInfoVO.class, "", false);
    }
    /**
     * 获取对外担保变更列表数据
     *
     * @param input
     * @param corpAuth
     * @return
     */
    public DataTablesOutput queryChangeList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, corpAuth);
        sql.append("AND B.STATUS IN(95) ");// 登记审批通过的数据
        return getDynamicBySQL(input, sql.toString(), GuaExtContractInfoVO.class, "", false);
    }
    /**
     * 获取对外担保审批通过数据数据
     *
     * @param corpAuth
     * @return
     */
    public List<GuaExtContractInfoVO> queryExCorpAmount(List<String> corpAuth, String infoId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        this.commonSql(sql, corpAuth);
        sql.append("AND B.STATUS IN(95) ");// 登记审批通过的数据
        if (StringUtils.isNotEmpty(infoId)) {
            sql.append(" and B.id<>:infoId");
            param.put("infoId", infoId);
        }
        return listBySQLAliasToBean(sql.toString(), param, GuaExtContractInfoVO.class);
    }
    /**
     * 获取对外担保登记待审批数据
     */
    public DataTablesOutput getApproveDataTables(DataTablesInput input, String corpSql) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, null);
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append("AND B.STATUS=11 ");// 登记待审批的数据
        }
        if (EmptyUtils.isNotEmpty(corpSql)){
            sql.append("and " + corpSql);
        }
        return getDynamicBySQL(input, sql.toString(), GuaExtContractInfoVO.class, "", false);
    }
    /**
     * 获取审批通过和终止的对外担保合同数据
     */
    public DataTablesOutput endDataTables(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, corpAuth);
        String status = (String) input.getFormdata().get("ini_status");
        if (EmptyUtils.isEmpty(status)) {
            sql.append(" AND B.STATUS IN(95,104,106) ");// 登记审批通过，终止中，终止打回
        }
        return getDynamicBySQL(input, sql.toString(), GuaExtContractInfoVO.class, "", false);
    }
    /**
     * 获取对外担保合同待审批数据
     */
    public DataTablesOutput endApproveData(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, corpAuth);
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append("AND B.STATUS=104 ");
        }
        return getDynamicBySQL(input, sql.toString(), GuaExtContractInfoVO.class, "", false);
    }
    /**
     * 担保合同查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput searchDataTables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, corpAuth);
        sql.append(" AND B.STATUS <> -2 ");
        if (null != dataTablesInput.getFormdata().get("ins_guaWay")) {
            String guaType = dataTablesInput.getFormdata().get("ins_guaWay").toString();
            if (StringUtils.isNotEmpty(guaType)) {
                sql.append(" AND B.ID IN (SELECT E.GUA_ID FROM GUA_CONTRACT_WAY E WHERE ");
                sql.append(getSQLIn(Arrays.asList(guaType.split(",")), 900, "E.WAY_CODES") + ") ");
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaExtContractInfoVO.class, "", false);
    }
    /**
     * 根据担保合同id得到对外担保合同信息
     *
     * @param id
     * @return
     */
    public GuaExtContractInfoVO getGuaExtContractInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, null);
        sql.append(" and B.ID=:id ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaExtContractInfoVO.class);
    }
    /** 即将到期对外担保合同 */
    public List<GuaExtContractInfoVO> endContracts(List<String> corpAuth, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        this.commonSql(sql, corpAuth);
        sql.append(" AND B.STATUS IN(95) ");// 登记审批通过
        sql.append(" and b.BUSINESS_TYPE = '1' ");// 担保合同类型为被担保合同
        sql.append(" and (b.end_date between to_date(:startDate, 'yyyy-MM-dd') and to_date(:endDate, 'yyyy-MM-dd')) ");
        Map<String, Object> param = new HashMap<>();
        param.put("startDate", DateUtils.format(startDate, "yyyy-MM-dd"));
        param.put("endDate", DateUtils.format(endDate, "yyyy-MM-dd"));
        return listBySQLAliasToBean(sql.toString(), param, GuaExtContractInfoVO.class);
    }
}
