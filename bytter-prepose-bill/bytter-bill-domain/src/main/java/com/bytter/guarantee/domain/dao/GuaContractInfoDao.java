package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.GuaContractInfo;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @version 1.0
 * @Title:GuaContractInfoDao
 * @Description:
 * @date 2018年12月12日 上午9:48:32
 */
@Repository
public class GuaContractInfoDao extends BaseDao<GuaContractInfo, String> {
    /**
     * 查询被担保合同
     *
     * @param dataTablesInput 查询参数
     * @param corpAuth        有权限的单位集合id
     * @return 分页数据
     */
    public DataTablesOutput<GuaContractInfoVO> getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
        sql.append("B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
        sql.append("B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
        sql.append("B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,");
        sql.append("B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,B.CON_TYPE_ID AS conTypeId,B.CON_ID as conId,BT_NVL(B.INNER_GUA,'0') AS innerGua, ");
        sql.append("B.BUSINESS_TYPE as bussinessType,b.corp_name as corpName,b.cur_name as curName,b.create_name as operator ");
        sql.append(" FROM GUA_CONTRACT_INFO B ");
        sql.append(" WHERE (B.BUSINESS_TYPE=0 or B.BUSINESS_TYPE is null) AND B.STATUS IN(0,11,-1) AND B.IS_CHANGE='0' ");
        if (null != dataTablesInput.getFormdata().get("ins_guaType")) {
            // 担保方式查询条件
            String guaType = dataTablesInput.getFormdata().get("ins_guaType").toString();
            if (StringUtils.isNotEmpty(guaType)) {
                sql.append(" AND B.ID IN (SELECT E.GUA_ID FROM GUA_CONTRACT_WAY E WHERE ");
                sql.append(getSQLIn(Arrays.asList(guaType.split(",")), 900, "E.WAY_CODES") + ") ");
            }
        }
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        } else {
            sql.append(" AND 1 <> 1 ");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractInfoVO.class, "", false);
    }
    /**
     * 查询担保方式
     *
     * @return
     */
    public List<GuaWayVO> getListGuaWay() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT b.id,b.code as code,b.name as name,b.is_pledge as isPledge,");
        sql.append(" b.pledge_property as pledgeProperty,b.rmk as rmk FROM GUA_WAY B ");
        return listBySQLAliasToBean(sql.toString(), null, GuaWayVO.class);
    }
    /**
     * 通过id查询担保方式
     *
     * @return
     */
    public GuaWayVO getGuaWayById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CODE AS code,B.NAME AS name,B.IS_PLEDGE AS isPledge,B.PLEDGE_PROPERTY AS pledgeProperty FROM GUA_WAY B ");
        sql.append("WHERE B.ID=:id");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaWayVO.class);
    }
    /**
     * 根据的质押物id得到抵质押物信息
     */
    public List<GuaPledgeInfoVO> getListGuaPledgeInfo(List<String> pledgeIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append(
                "B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        //TODO 补充名称相关信息
        sql.append("B.CORP_NAME AS corpName, B.CUR_NAME AS curName, B.REG_CORP_NAME AS regCorpName,");
        sql.append(
                "B.PLEDGE AS pledge,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append(
                "B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        sql.append(
                "B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,B.KEEPER AS keeper,");
        sql.append(
                "B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS createBy,B.PLACE AS  place,B.UPDATE_DATE AS updateDate,");
        sql.append(
                "B.UPDATE_BY AS updateBy,(CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney, ");
        sql.append("(CASE WHEN B.OCCUPY_MONEY IS NULL THEN 0 ELSE B.OCCUPY_MONEY END) AS occupyMoney,");
        sql.append(
                "(BT_NVL_NUM(B.PLEDGE_MONEY,0)-BT_NVL_NUM(B.FREEZE_MONEY,0)-BT_NVL_NUM(B.OCCUPY_MONEY,0)) AS availMoney  ");
//        sql.append(
//                "FROM GUA_PLEDGE_INFO B LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append(
                "FROM GUA_PLEDGE_INFO B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append("WHERE 1=1 ");
        if (!EmptyUtils.isEmptyList(pledgeIds)) {
            sql.append(" AND ").append(SqlUtil.getInSql(pledgeIds, "B.ID", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, GuaPledgeInfoVO.class);
    }
    /**
     * 根据担保合同id得到担保合同信息
     *
     * @param id
     * @return
     */
    public GuaContractInfoVO getGuaContractInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
        sql.append(" B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
        sql.append(" B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
        sql.append(" B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.create_name AS operator,B.create_name AS createName,B.create_by AS createBy,B.CREATE_DATE AS createDate,");
        sql.append(" B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,B.IS_CHANGE AS isChange, ");
        sql.append(" B.drawmoney_date as drawMoneyDate,B.CORP_NAME AS corpName, B.BUSINESS_TYPE as businessType, ");
//        sql.append(" B.CON_ID AS conId,B.CON_TYPE_ID,BT_NVL(B.INNER_GUA,'0') AS innerGua,b.cur_name as curName ");
        sql.append(" B.CON_ID AS conId,B.CON_TYPE_ID,case when B.INNER_GUA is null then '0' else B.INNER_GUA end AS innerGua,b.cur_name as curName ");
        sql.append(" FROM GUA_CONTRACT_INFO B ");
        sql.append(" WHERE B.ID=:id ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaContractInfoVO.class);
    }
    /**
     * 根据担保编号查询担保合同
     *
     * @param guaNo 担保编号
     * @param id    排除的担保合同id
     * @return
     */
    public List<GuaContractInfo> findAllByGuaNo(String guaNo, String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" from GuaContractInfo where 1 = 1 and guaNo=:guaNo ");
        if (EmptyUtils.isNotEmpty(id)) {
            sb.append(" and id <> :id ");
            params.put("id", id);
        }
        params.put("guaNo", guaNo);
        return createQuery(sb.toString(), params).list();
    }
    /**
     * 查询被担保合同待审批数据
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getApproveDataTables(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
        sql.append(
                "B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
        sql.append(
                "B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
        //TODO 补充名称单位信息
        sql.append("B.CORP_NAME AS corpName,B.CUR_NAME as curName,");
        sql.append(
                "B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_NAME operator,B.CREATE_DATE AS createDate,");
        sql.append("B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,BT_NVL(B.INNER_GUA,'0') AS innerGua ");
//        sql.append(
//                "FROM GUA_CONTRACT_INFO B LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID ");
        sql.append(
                "FROM GUA_CONTRACT_INFO B ");
//        sql.append(" left join sys_user sysUser on B.create_by = sysUser.id ");
        sql.append("WHERE (B.BUSINESS_TYPE=0 or B.BUSINESS_TYPE is null) AND B.STATUS=11 AND B.IS_CHANGE='0' ");
        if (EmptyUtils.isNotEmpty(businessSql)){
            sql.append(" and " + businessSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractInfoVO.class, "", false);
    }
    /**
     * 被担保合同查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput searchGuaContractTables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
        sql.append("B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
        sql.append("B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
        sql.append("B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,");
        sql.append("B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,B.CON_ID AS conId,BT_NVL(B.INNER_GUA,'0') AS innerGua, ");
        sql.append("b.corp_name as corpName,b.cur_name as curName,b.create_name as operator ");
        sql.append("FROM GUA_CONTRACT_INFO B ");
        sql.append("WHERE (B.BUSINESS_TYPE=0 or B.BUSINESS_TYPE is null) AND B.STATUS IN(0,11,-1,95)");
        if (null != dataTablesInput.getFormdata().get("ins_guaType")) {
            String guaType = dataTablesInput.getFormdata().get("ins_guaType").toString();
            if (StringUtils.isNotEmpty(guaType)) {
                sql.append(" AND B.ID IN (SELECT E.GUA_ID FROM GUA_CONTRACT_WAY E WHERE ");
                sql.append(getSQLIn(Arrays.asList(guaType.split(",")), 900, "E.WAY_CODES") + ") ");
            }
        }
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractInfoVO.class, "", false);
    }
    /**
     * 查询审批通过的担保合同
     *
     * @param dataTablesInput 查询参数
     * @param corpAuth        有权限的单位id集合
     * @return 列表数据
     */
    public DataTablesOutput<GuaContractInfoVO> getApprovedDataTables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
        sql.append("B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
        sql.append("B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
        sql.append("B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,");
        sql.append("B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,BT_NVL(B.INNER_GUA,'0') AS innerGua, ");
        sql.append("B.CUR_NAME as curName,B.CREATE_NAME as operator,B.CORP_NAME as corpName ");
        sql.append("FROM GUA_CONTRACT_INFO B ");
        sql.append("WHERE (B.BUSINESS_TYPE=0 or B.BUSINESS_TYPE is null) AND B.STATUS IN(95) ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractInfoVO.class, "", false);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String busType, String status) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from GUA_CONTRACT_INFO where corp_id=:corpId and BUSINESS_TYPE=:busType");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        params.put("corpId", corpId);
        params.put("busType", busType);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    public List<GuaWayVO> getGuaWayByIds(List<String> wayCodes) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT g.ID AS id,g.CODE AS code,g.NAME AS name,g.IS_PLEDGE AS isPledge,g.PLEDGE_PROPERTY AS pledgeProperty FROM GUA_WAY g ");
        sql.append("WHERE g.ID in (:id) ");
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", wayCodes);
        return listBySQLAliasToBean(sql.toString(), params, GuaWayVO.class);
    }
    /**
     * 检查是否存在挂接的担保合同
     * @param input
     * @param corpId
     * @return
     */
    public boolean checkContractList(String corpId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT id FROM GUA_CONTRACT_INFO where (BUSINESS_TYPE=0 or BUSINESS_TYPE is null) AND STATUS IN('95') ");
        sql.append("AND CORP_ID = :ID ");
        params.put("ID", corpId);
        List<String> result = createSQLQuery(sql.toString(), params).list();
        if (result.size() > 0) {
            return true;
        }
        return false;
    }
}
