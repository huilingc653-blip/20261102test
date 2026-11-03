package com.bytter.biz.foreign.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.foreign.entity.FexCotp;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FexCotpDao  extends BaseDao<FexCotp,String> {

    /**
     *查询所有数据
     * @param input
     * @return
     */
    public DataTablesOutput getDataTable(DataTablesInput input, List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.create_by createBy, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("where 1 = 1");
        sql.append(" and cotp.status != -2");
        sql.append(" and ( " + getSQLIn(corpIds, 900, "cotp.corp_id") + ")");
        return getDynamicParamsBySQL(input, sql.toString(), FexCotpVO.class, "cotp", false, null);
    }

    /**
     *查询待审批数据
     * @param input
     * @return
     */
    public DataTablesOutput getDataTableApprove(DataTablesInput input,List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  where 1=1");
        String flowStatus = input.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)){
            sql.append("  and cotp.status = 11");
        }
        sql.append(" and ( " + getSQLIn(corpIds, 900, "cotp.corp_id") + ")");
        return getDynamicParamsBySQL(input, sql.toString(), FexCotpVO.class, "", false, null);
    }


    /**
     *查询组内数据
     * @param input
     * @return
     */
    public DataTablesOutput getChildDataTable(DataTablesInput input){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  where 1 = 1");
        return getDynamicParamsBySQL(input, sql.toString(), FexCotpVO.class, "cotp", false, null);
    }

    /**
     *查询组内数据
     * @param params
     * @return
     */
    public List<FexCotpVO> listChildren(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  where 1 = 1");
        sql.append(" and cotp.parent_id = :id");
        return listBySQLAliasToBean(sql.toString(), params, FexCotpVO.class);
    }

    /**
     *查询组内数据
     * @param input
     * @return
     */
    public DataTablesOutput getChildDataTableByIds(DataTablesInput input,List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  where 1 = 1");
        sql.append(" and ( " + getSQLIn(ids, 900, "cotp.id") + " ) ");
        return getDynamicParamsBySQL(input, sql.toString(), FexCotpVO.class, "cotp", false, null);
    }

    /**
     *查询交易对手组
     * @param input
     * @return
     */
    public DataTablesOutput getCotpGroupDataTable(DataTablesInput input,List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  where cotp.cotp_level = '"+ ForeignConstant.FEX_COTP_LEVEL_0 +"'");
        sql.append(" and ( " + getSQLIn(corpIds, 900, "cotp.corp_id") + ")");
        sql.append(" and cotp.status = " + ForeignConstant.STATUE_CHECK);
        return getDynamicParamsBySQL(input, sql.toString(), FexCotpVO.class, "cotp", false, null);
    }


    /**
     *查询所有无组数据
     * @param input
     * @return
     */
    public DataTablesOutput getNullParentCotpDataTable(DataTablesInput input,List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  where cotp.parent_id is null ");
        sql.append(" and ( " + getSQLIn(corpIds, 900, "cotp.corp_id") + ")");
        sql.append(" and cotp.status = " + ForeignConstant.STATUE_CHECK);
        return getDynamicParamsBySQL(input, sql.toString(), FexCotpVO.class, "cotp", false, null);
    }

    /**
     * 组内增加交易对手或者交易对手组
     * @param params
     */
    public void updateCotpbyIds(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append(" update fex_cotp set parent_id = :parentId where id in (:ids)");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 查询交易对手组列表
     * @param corpIds
     * @return
     */
    public List<FexCotpVO> listCotpGroup(List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.corp_id corpId, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  where cotp.cotp_level = '"+ ForeignConstant.FEX_COTP_LEVEL_0 +"'");
//        sql.append(" and ( " + getSQLIn(corpIds, 900, "cotp.corp_id") + ")");
        sql.append(" and cotp.status = 95 ");
        return listBySQLAliasToBean(sql.toString(), null, FexCotpVO.class);
    }


    /**
     * 查询交易对手组列表
     * @param cotpTypes
     * @return
     */
    public List<FexCotpVO> listBankCotp(List<String> cotpTypes) {
        String cotpTypeSql = "";
        if (null != cotpTypes && cotpTypes.size() > 0) {
            for (String type : cotpTypes) {
                if (StringUtils.isNotEmpty(cotpTypeSql)) {
                    cotpTypeSql += " or cotp.cotp_Type like '%" + type + "%'";
                } else {
                    cotpTypeSql = " cotp.cotp_Type like '%" + type + "%'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("       cotp.corp_id corpId, ");
        sql.append("       cotp.cotp_name cotpName, ");
        sql.append("       cotp.parent_id parentId, ");
        sql.append("       cotp2.cotp_name parentName, ");
        sql.append("       cotp.cotp_level cotpLevel, ");
        sql.append("       cotp.short_name shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.post_addr postAddr, ");
        sql.append("       cotp.street_addr streetAddr, ");
        sql.append("       cotp.building_Number buildingNumber, ");
        sql.append("       cotp.address_En as addressEn,");
        sql.append("       cotp.tel tel, ");
        sql.append("       cotp.bic_code BicCode, ");
        sql.append("       cotp.country_id countryId, ");
        sql.append("       cotp.address       address, ");
        sql.append("       scc.name_zh        countryName, ");
        sql.append("       scc2.name_zh       addressName, ");
        sql.append("       cotp.state       state, ");
        sql.append("       cotp.status status, ");
        sql.append("       cotp.local_Clear_No localClearNo,");
        sql.append("       cotp.city, ");
        sql.append("       cotp.prov, ");
        sql.append("       cotp.product_id productId, ");
        sql.append("       cotp.fps_id fpsId, ");
        sql.append("       cotp.e_mail  eMail, ");
        sql.append("       cotp.isd_code isdCode, ");
        sql.append("       cotp.phone_code phoneCode, ");
        sql.append("       cotp.aiin, ");
        sql.append("       cotp.hkid, ");
        sql.append("       scc.en_code enCode ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("  on cotp.parent_id = cotp2.id ");
        sql.append("  left join sys_country_cust scc ");
        sql.append("    on cotp.country_id = scc.id ");
        sql.append("  left join sys_country_cust scc2 ");
        sql.append("    on cotp.address = scc2.id ");
        sql.append("  where cotp.cotp_level = '" + ForeignConstant.FEX_COTP_LEVEL_1 + "'");
        sql.append(" and cotp.status = 95 ");
        if (null != cotpTypes && cotpTypes.size() > 0) {
            sql.append(" and  ( " + cotpTypeSql + " ) ");
        }
        return listBySQLAliasToBean(sql.toString(), null, FexCotpVO.class);
    }

    /**
     * 根据id获取数据
     * @param params
     * @return
     */
    public FexCotpVO getFexcotpById(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id            id, ");
        sql.append("       cotp.corp_id       corpId, ");
        sql.append("       cotp.opp_Id        oppId,");
        sql.append("       cotp.cotp_name     cotpName, ");
        sql.append("       cotp.short_name    shortName, ");
        sql.append("       cotp.official_name officialName, ");
        sql.append("       cotp.status        status, ");
        sql.append("       cotp.parent_id     parentId, ");
        sql.append("       cotp2.cotp_name    parentName, ");
        sql.append("       cotp.cotp_level    cotpLevel, ");
        sql.append("       cotp.cotp_type     cotpType, ");
        sql.append("       cotp.post_addr     postAddr, ");
        sql.append("       cotp.street_addr   streetAddr, ");
        sql.append("       cotp.building_Number buildingNumber, ");
        sql.append("       cotp.tel           tel, ");
        sql.append("       cotp.country_id    countryId, ");
        sql.append("       scc.name_zh        countryName, ");
        sql.append("       scc.name_en        countryNameEn, ");
        sql.append("       cotp.address       address, ");
        sql.append("       scc2.name_zh       addressName, ");
        sql.append("       scc2.name_en       addressNameEn, ");
        sql.append("       cotp.state       state, ");
        sql.append("       cotp.bic_code      bicCode, ");
        sql.append("       cotp.bank_code     bankCode, ");
        sql.append("       cotp.rmk           rmk, ");
        sql.append("       cotp.cotp_name_en     cotpNameEn, ");
        sql.append("       cotp.address_En    addressEn, ");
        sql.append("       cotp.cls_Flag           clsFlag, ");
        sql.append("       cotp.create_by     createBy, ");
        sql.append("       cotp.create_date   createDate, ");
        sql.append("       cotp.update_by     updateBy, ");
        sql.append("       cotp.update_date   updateDate, ");
        sql.append("       cotp.local_Clear_No localClearNo,");
        sql.append("       cotp.city,");
        sql.append("       cotp.prov,");
        sql.append("       cotp.PROV_NAME  provName,");
        sql.append("       cotp.CITY_NAME  cityName,");
        sql.append("       cotp.product_id productId, ");
        sql.append("       cotp.fps_id fpsId, ");
        sql.append("       cotp.e_mail  eMail, ");
        sql.append("       cotp.isd_code isdCode, ");
        sql.append("       cotp.phone_code phoneCode, ");
        sql.append("       cotp.aiin, ");
        sql.append("       cotp.hkid, ");
        sql.append("       scc.en_code enCode ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 ");
        sql.append("    on cotp.parent_id = cotp2.id ");
        sql.append("  left join sys_country_cust scc ");
        sql.append("    on cotp.country_id = scc.id ");
        sql.append("  left join sys_country_cust scc2 ");
        sql.append("    on cotp.address = scc2.id ");
        sql.append(" where cotp.id = :id ");
        return getBySQLAliasToBean(sql.toString(), params, FexCotpVO.class);
    }

    /**
     * 通过名称查询数据
     * @param params
     * @return
     */
    public List<FexCotpVO> getCotpByName(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fex_cotp  where cotp_name = :name and status <> -2");
        return listBySQLAliasToBean(sql.toString(), params, FexCotpVO.class);
    }


    /**
     * 通过名称查询数据
     * @param params
     * @return
     */
    public List<FexCotpVO> listCotp(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fex_cotp  where status = :status");
        return listBySQLAliasToBean(sql.toString(), params, FexCotpVO.class);
    }


    /**
     * 返回当前数据及组内数据
     * @param params
     * @return
     */
    public List<FexCotpVO> listCotpAndChild(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.* ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  where 1 = 1");
        sql.append(" and (cotp.id = :id or cotp.parent_id = :id)");
        return listBySQLAliasToBean(sql.toString(), params, FexCotpVO.class);
    }

    public List<FexCotpVO> listByOppId(String oppId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fex_cotp  where opp_Id = :oppId");
        params.put("oppId", oppId);
        return listBySQLAliasToBean(sql.toString(), params, FexCotpVO.class);
    }



    public DataTablesOutput getCotpDataTable(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cotp.id, ");
        sql.append("  cotp.cotp_name cotpName, ");
        sql.append("  cotp.parent_id parentId, ");
        sql.append("  cotp2.cotp_name parentName, ");
        sql.append("  cotp.cotp_level cotpLevel, ");
        sql.append("  cotp.short_name shortName, ");
        sql.append("  cotp.official_name officialName, ");
        sql.append("  cotp.status status ");
        sql.append("  from fex_cotp cotp ");
        sql.append("  left join fex_cotp cotp2 on cotp.parent_id = cotp2.id ");
        sql.append("  where cotp.cotp_level = '1'");
        sql.append(" and cotp.status = " + com.bytter.core.constant.ForeignConstant.STATUE_CHECK);
        sql.append(" and ( " + getSQLIn(corpIds, 900, "cotp.corp_id") + ")");
        return getDynamicBySQL(input, sql.toString(), FexCotpVO.class, "cotp", false, null);
    }
}
