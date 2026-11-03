package com.bytter.guarantee.domain.dao;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.GuaPledgeInfo;
import com.bytter.guarantee.domain.vo.GuaPledgeInfoVO;
import com.bytter.guarantee.domain.vo.GuaPledgeOccupyInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.dto.ConfigDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class GuaPledgeInfoDao extends BaseDao<GuaPledgeInfo, String> {
    /**
     * 查询抵质押物信息
     *
     * @param dataTablesInput
     * @param corpAuth
     * @return
     */
    public DataTablesOutput<GuaPledgeInfoVO> getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth,
                                                                ConfigDTO configDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.CORP_NAME AS corpName,B.reg_corp_name as regCorpName,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append("B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        sql.append("B.PLEDGE AS pledge,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append("B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        sql.append("B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,");
        sql.append("B.PLACE AS  place,B.KEEPER AS keeper,B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,");
        sql.append("B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS updateBy,(CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney,B.cur_name as curName, ");
        sql.append("(CASE WHEN B.OCCUPY_MONEY IS NULL THEN 0 ELSE B.OCCUPY_MONEY END) AS occupyMoney, ");
        sql.append("B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,");
        sql.append("B.LAND_AREA AS landArea,B.RMK as rmk ");
        sql.append("FROM GUA_PLEDGE_INFO B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID = B.PLEDGE_TYPE ");
        sql.append("WHERE 1=1 AND B.STATUS IN(0,11,-1)");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaPledgeInfoVO.class, "", false);
    }
    /**
     * 通过id查询抵质押物信息
     *
     * @param id
     * @return
     */
    public GuaPledgeInfoVO getGuaPledgeInfoVOById(String id, ConfigDTO configDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append("B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        sql.append("B.PLEDGE AS pledge,B.PLEDGEE AS pledgee,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append("B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        sql.append("B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,");
        sql.append("B.PLACE AS  place,B.KEEPER AS keeper,B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,");
        sql.append("B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS updateBy,(CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney,");
        sql.append("(CASE WHEN B.OCCUPY_MONEY IS NULL THEN 0 ELSE B.OCCUPY_MONEY END)  AS occupyMoney, B.ATTACHMENT_ID AS attachmentId,");
        if (CommonConstant.YES.equals(configDTO.getValue())) {
            sql.append("  case when B.PLEDGE_ATTR = '0' then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        } else {
            sql.append("  case when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) > 0 or BT_NVL_NUM(B.OCCUPY_MONEY,0) > 0) then 0 ");
            sql.append("  when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) = 0 and BT_NVL_NUM(B.OCCUPY_MONEY,0) = 0) then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        }
        sql.append(" else (BT_NVL_NUM(B.PLEDGE_MONEY,0)-BT_NVL_NUM(B.FREEZE_MONEY,0)-BT_NVL_NUM(B.OCCUPY_MONEY,0)) end as availMoney, ");
        sql.append("B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,");
        sql.append("B.LAND_AREA AS landArea,B.RMK as rmk,b.corp_name as corpName,b.cur_name as curName,b.REG_CORP_NAME as regCorpName ");
        sql.append("FROM GUA_PLEDGE_INFO B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append(" WHERE B.ID=:id");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaPledgeInfoVO.class);
    }
    public GuaPledgeInfoVO getHisGuaPledgeInfoVOById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append(
                "B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        sql.append(
                "B.PLEDGE AS pledge,B.PLEDGEE AS pledgee,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append(
                "B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        sql.append(
                "B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,");
        sql.append(
                "B.PLACE AS  place,B.KEEPER AS keeper,B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,");
        //TODO 补充名称信息
        sql.append("info.CORP_NAME as corpName,info.CUR_NAME as curName,B.REG_CORP_NAME as regCorpName,");
        sql.append("B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS updateBy,");
        sql.append(" B.ATTACHMENT_ID AS attachmentId,");
        sql.append(
                "B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,");
        sql.append("B.LAND_AREA AS landArea,B.RMK as rmk ");
//        sql.append(
//                "FROM GUA_PLEDGE_INFO_HIS B LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
//        sql.append(" LEFT JOIN GUA_PLEDGE_INFO info on info.id = B.PLEDGE_ID");
        sql.append(
                "FROM GUA_PLEDGE_INFO_HIS B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append(" LEFT JOIN GUA_PLEDGE_INFO info on info.id = B.PLEDGE_ID");
        sql.append(" WHERE B.ID=:id");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaPledgeInfoVO.class);
    }
    /**
     * 查询审批通过的抵质押物数据列表
     *
     * @author luxy
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput querypledgeInfoPassList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.CORP_NAME as corpName,B.PLEDGE_NO AS pledgeNo,");
        sql.append(" B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.type_name as typeName,");
        sql.append(" B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append(" B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,B.PLEDGE AS pledge,B.ASSET_STATE AS assetState,");
        //TODO 补充名称相关信息
        sql.append(" B.CUR_NAME AS curName, B.REG_CORP_NAME AS regCorpName,");
        sql.append(" B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append(" B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,");
        sql.append(" B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.PLEDGE_INIT_MONEY AS pledgeInitMoney,");
        sql.append(" B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,");
        sql.append(" B.PLACE AS  place,B.KEEPER AS keep,B.INVESTIGATORS AS investigators,");
        sql.append(" B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,");
        sql.append(" B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS updateBy,");
        sql.append(
                " (CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney,B.OCCUPY_MONEY AS occupyMoney, ");
        sql.append("B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,");
        sql.append("B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,B.LAND_AREA AS landArea,B.RMK as rmk ");
        sql.append(" FROM GUA_PLEDGE_INFO B");
        sql.append(" LEFT JOIN gua_pledge_type E ON B.PLEDGE_TYPE=E.ID");
//        sql.append(" LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID");
//        sql.append(" LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID");
        sql.append(" where B.status=95");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaPledgeInfoVO.class, null, false);
    }
    /**
     * 抵质押物变更列表查询
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<GuaPledgeInfoVO> querypledgeInfoPass(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,");
        sql.append(" B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.type_name as typeName,");
        sql.append(" B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,");
        sql.append(" B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,B.PLEDGE AS pledge,B.PLEDGEE AS pledgee,B.ASSET_STATE AS assetState,");
        sql.append(" B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append(" B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,");
        sql.append(" B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.PLEDGE_INIT_MONEY AS pledgeInitMoney,");
        sql.append(" B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,");
        sql.append(" B.PLACE AS  place,B.KEEPER AS keep,B.INVESTIGATORS AS investigators,");
        sql.append(" B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,");
        sql.append(" B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,B.cur_name as curName,B.corp_name as corpName,B.REG_CORP_NAME as regCorpName, ");
        sql.append(" (CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney,B.OCCUPY_MONEY AS occupyMoney,");
        sql.append("B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,");
        sql.append("B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,B.LAND_AREA AS landArea,B.RMK as rmk ");
        sql.append(" FROM GUA_PLEDGE_INFO B  ");
        sql.append(" LEFT JOIN gua_pledge_type E ON B.PLEDGE_TYPE=E.ID");
        sql.append(" where B.status=95 ");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(input, sql.toString(), GuaPledgeInfoVO.class, "B", false);
    }
    /**
     * 查询待审批的抵质押物数据列表
     *
     * @author luxy
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getApproveDataTables(DataTablesInput input, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append(
                "B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        sql.append(
                "B.PLEDGE AS pledge,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append(
                "B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        //TODO 补充名称相关信息
        sql.append("B.CUR_NAME AS curName, B.REG_CORP_NAME AS regCorpName,B.CORP_NAME as corpName,");
        sql.append(
                "B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,");
        sql.append(
                "B.PLACE AS  place,B.KEEPER AS keeper,B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,");
        sql.append(
                "B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS updateBy,B.FREEZE_MONEY AS freezeMoney,B.OCCUPY_MONEY AS occupyMoney, ");
        sql.append(
                "B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,B.PLEDGE_AREA AS pledgeArea, ");
        sql.append("B.LAND_NO AS landNo,B.LAND_AREA AS landArea,B.RMK as rmk ");
        sql.append(
                "FROM GUA_PLEDGE_INFO B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append(" where 1=1 ");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and  B.status=11 ");
        }
        if (!StringUtils.isBlank(businessSql)) {
            sql.append(" AND ").append(businessSql);
        }
        return getDynamicBySQL(input, sql.toString(), GuaPledgeInfoVO.class, null, false);
    }
    /**
     * 抵质押物信息查询
     *
     * @param dataTablesInput 查询参数
     * @param corpAuth        有权限的单位id
     * @return 列表数据
     */
    public DataTablesOutput<GuaPledgeInfoVO> searchDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth,
                                                                   ConfigDTO configDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append("B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        sql.append("B.PLEDGE AS pledge,B.PLEDGEE AS pledgee,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append("B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        sql.append("B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,B.KEEPER AS keeper,");
        sql.append("B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,B.PLACE AS  place,B.UPDATE_DATE AS updateDate,");
        sql.append("B.UPDATE_BY AS updateBy,(CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney,B.cur_name as curName,B.corp_name as corpName, ");
        sql.append("(CASE WHEN B.OCCUPY_MONEY IS NULL THEN 0 ELSE B.OCCUPY_MONEY END) AS occupyMoney,B.REG_CORP_NAME as regCorpName,");
        if (CommonConstant.YES.equals(configDTO.getValue())) {
            sql.append("  case when B.PLEDGE_ATTR = '0' then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        } else {
            sql.append(" case when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) > 0 or BT_NVL_NUM(B.OCCUPY_MONEY,0) > 0) then 0 ");
            sql.append("  when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) = 0 and BT_NVL_NUM(B.OCCUPY_MONEY,0) = 0) then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        }
        sql.append(" else (BT_NVL_NUM(B.PLEDGE_MONEY,0)-BT_NVL_NUM(B.FREEZE_MONEY,0)-BT_NVL_NUM(B.OCCUPY_MONEY,0)) end as availMoney, ");
        sql.append("B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,");
        sql.append("B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,B.LAND_AREA AS landArea,B.RMK as rmk ");
        sql.append("FROM GUA_PLEDGE_INFO B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append(" WHERE 1=1 AND B.STATUS IN(0,11,-1,95)");
        if (corpAuth != null && corpAuth.size() > 0) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "B.CORP_ID"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaPledgeInfoVO.class, "", false);
    }
    /**
     * 查询抵质押物审批通过的数据
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput querypledgeInfoPassTables(DataTablesInput dataTablesInput, List<String> pDids,
                                                      ConfigDTO configDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append("B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        sql.append("B.PLEDGE AS pledge,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append("B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        sql.append("B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,B.KEEPER AS keeper,");
        sql.append("B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,B.PLACE AS  place,B.UPDATE_DATE AS updateDate,");
        //TODO 补充某人未新增的单位名称
        sql.append("B.CORP_NAME AS corpName,B.REG_CORP_NAME AS regCorpName, B.CUR_NAME AS curName, ");
        sql.append("B.UPDATE_BY AS updateBy,(CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney, ");
        sql.append("(CASE WHEN B.OCCUPY_MONEY IS NULL THEN 0 ELSE B.OCCUPY_MONEY END) AS occupyMoney,");
        if (CommonConstant.YES.equals(configDTO.getValue())) {
            sql.append("  case when B.PLEDGE_ATTR = '0' then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        } else {
            sql.append(" case when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) > 0 or BT_NVL_NUM(B.OCCUPY_MONEY,0) > 0) then 0 ");
            sql.append("  when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) = 0 and BT_NVL_NUM(B.OCCUPY_MONEY,0) = 0) then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        }
        sql.append(" else (BT_NVL_NUM(B.PLEDGE_MONEY,0)-BT_NVL_NUM(B.FREEZE_MONEY,0)-BT_NVL_NUM(B.OCCUPY_MONEY,0)) end as availMoney, ");
        sql.append("B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,");
        sql.append("B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,B.LAND_AREA AS landArea,B.RMK as rmk ");
        sql.append("FROM GUA_PLEDGE_INFO B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append("WHERE B.STATUS=95 ");
        if (CommonConstant.YES.equals(configDTO.getValue())) {
            sql.append(" AND ( ");
            sql.append(" ( b.PLEDGE_ATTR = '1' and (BT_NVL_NUM(B.PLEDGE_MONEY,0)-BT_NVL_NUM(B.FREEZE_MONEY,0)-BT_NVL_NUM(B.OCCUPY_MONEY,0))>0 ) ");
            sql.append(" or ");
            sql.append(" b.PLEDGE_ATTR = '0' ");
            sql.append(" ) ");
        } else {
            sql.append(" AND (BT_NVL_NUM(B.PLEDGE_MONEY,0)-BT_NVL_NUM(B.FREEZE_MONEY,0)-BT_NVL_NUM(B.OCCUPY_MONEY,0))>0 ");
        }
        if (!EmptyUtils.isEmptyList(pDids)) {
            sql.append(" AND ").append(SqlUtil.getNotinSql(pDids, "B.ID", true));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaPledgeInfoVO.class, "", false);
    }
    /**
     * 通过id查询抵质押物信息
     *
     * @param id
     * @return
     */
    public GuaPledgeInfoVO getPledgeInfoByIdAndPledgeAttr(String id, String pledgeAttr, ConfigDTO configDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT B.ID AS id,B.CORP_ID AS corpId,B.PLEDGE_NO AS pledgeNo,B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,");
        sql.append(
                "B.PLEDGE_NAME AS pledgeName,B.START_DATE AS startDate,B.END_DATE AS endDate,B.CUR_ID AS curId,B.PLEDGE_MONEY AS pledgeMoney,");
        sql.append(
                "B.PLEDGE AS pledge,B.ASSET_STATE AS assetState,B.ASSET_NUMBER AS assetNumber,B.BUILD_AREA AS buildArea,B.BUILD_AREA_RATE AS buildAreaRate,");
        sql.append(
                "B.REG_CORP AS regCorp,B.REG_DATE AS regDate,B.REG_NUMBER AS regNumber,B.PLEDGE_DEPRECIATION AS pledgeDepreciation,B.DEPRECIATION_RATE AS depreciationRate,");
        //TODO 补充名称相关信息
        sql.append("B.CORP_NAME AS corpName,B.REG_CORP_NAME AS regCorpName, B.CUR_NAME AS curName,");
        sql.append(
                "B.PLEDGE_INIT_MONEY AS pledgeInitMoney,B.EVALUATION_DEP AS evaluationDep,B.AMOUNT AS amount,B.SPECIFICATION AS specification,");
        sql.append(
                "B.PLACE AS  place,B.KEEPER AS keeper,B.INVESTIGATORS AS investigators,B.STATUS AS status,B.CREATE_DATE AS createDate,B.CREATE_BY AS userName,");
        sql.append(
                "B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS updateBy,(CASE WHEN B.FREEZE_MONEY IS NULL THEN 0 ELSE B.FREEZE_MONEY END) AS freezeMoney,");
        sql.append("(CASE WHEN B.OCCUPY_MONEY IS NULL THEN 0 ELSE B.OCCUPY_MONEY END)  AS occupyMoney, ");
        if (CommonConstant.YES.equals(configDTO.getValue())) {
            sql.append("  case when B.PLEDGE_ATTR = '0' then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        } else {
            sql.append(
                    "  case when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) > 0 or BT_NVL_NUM(B.OCCUPY_MONEY,0) > 0) then 0 ");
            sql.append(
                    "  when B.PLEDGE_ATTR = '0' and (BT_NVL_NUM(B.FREEZE_MONEY,0) = 0 and BT_NVL_NUM(B.OCCUPY_MONEY,0) = 0) then BT_NVL_NUM(B.PLEDGE_MONEY,0) ");
        }
        sql.append(
                " else (BT_NVL_NUM(B.PLEDGE_MONEY,0)-BT_NVL_NUM(B.FREEZE_MONEY,0)-BT_NVL_NUM(B.OCCUPY_MONEY,0)) end as availMoney, ");
        sql.append("B.PLEDGE_ORG AS pledgeOrg,B.PLEDGE_NET_WORTH AS pledgeNetWorth,B.LAND_CARD_NO AS landCardNo,");
        sql.append("B.PLEDGE_AREA AS pledgeArea,B.LAND_NO AS landNo,B.LAND_AREA AS landArea,B.RMK as rmk ");
//        sql.append(
//                "FROM GUA_PLEDGE_INFO B LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append("FROM GUA_PLEDGE_INFO B LEFT JOIN GUA_PLEDGE_TYPE E ON E.ID=B.PLEDGE_TYPE ");
        sql.append(" WHERE B.ID=:id AND B.PLEDGE_ATTR=:pledgeAttr ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("pledgeAttr", pledgeAttr);
        return getBySQLAliasToBean(sql.toString(), params, GuaPledgeInfoVO.class);
    }
    /**
     * 通过抵质押id得到抵质押物被担保合同占用的信息
     *
     * @param id
     * @return
     */
    public List<GuaPledgeOccupyInfoVO> getGuaPledgeOccupyInfo(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT G.ID AS guaId,G.CONTRACT_CODE AS contractCode,G.START_DATE AS startDate,G.END_DATE AS endDate,");
        sql.append("G.CORP_ID AS corpId,G.GUA_NO AS guaNo,G.GUA_PERSON AS guaPerson,G.DEBTOR_PERSON AS debtorPerson,");
        //TODO 补充名称单位信息
        sql.append("G.CORP_NAME AS corpName,B.CUR_NAME as curName,");
        sql.append(
                "G.CREDITOR_PERSON AS creditorPerson,A.GUA_MONEY AS guaMoney,B.ID AS id,B.PLEDGE_NO AS pledgeNo,B.CUR_ID AS curId,");
        sql.append(
                "B.PLEDGE_ATTR AS pledgeAttr,B.PLEDGE_TYPE AS pledgeType,E.TYPE_NAME AS typeName,B.PLEDGE_NAME AS pledgeName ");
        sql.append(
                " FROM GUA_CONTRACT_PLEDGE A LEFT JOIN GUA_PLEDGE_INFO B ON A.PLEDGE_ID=B.ID LEFT JOIN GUA_PLEDGE_TYPE E  ");
        sql.append(
                "ON E.ID=B.PLEDGE_TYPE LEFT JOIN GUA_CONTRACT_WAY F ON A.GUA_CONTRACT_WAY_ID=F.ID LEFT JOIN GUA_CONTRACT_INFO G ");
//        sql.append(
//                "ON G.ID=F.GUA_ID LEFT JOIN  SYS_CORP C ON B.CORP_ID=C.ID  LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID ");
        sql.append("ON G.ID=F.GUA_ID ");
        sql.append("WHERE 1=1 AND B.ID=:id AND G.STATUS=95 ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, GuaPledgeOccupyInfoVO.class);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from GUA_PLEDGE_INFO where corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
