package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCustFieldRule;
import com.bytter.biz.foreign.entity.FexCustFieldRuleTenant;
import com.bytter.biz.foreign.vo.FexCustFieldRuleTenantVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户自定义字段规则DAO。
 */
@Repository
public class FexCustFieldRuleTenantDao extends BaseDao<FexCustFieldRuleTenant, String> {
    public DataTablesOutput getDataTable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id as id,f.field_code as fieldCode,f.field_name as fieldName,f.field_desc as fieldDesc,f.app_range_1 as appRange1 ,f.field_type as fieldType ,f.valid_sign as validSign, ");
        sql.append(" f.label_bank as labelBank, f.app_range_2 as appRange2,f.dict_group as dictGroup ");
        sql.append(" from FEX_CUST_FIELD_RULE_TENANT f where 1=1");
        if (null != input.getFormdata().get("excludeIds")){
            String excludeIds = input.getFormdata().get("excludeIds").toString();
            if(StringUtils.isNotEmpty(excludeIds)){
                String[] idArray = excludeIds.split(",");
                StringBuilder inSql = new StringBuilder();
                for (String id : idArray) {
                    if (inSql.length() > 0) {
                        inSql.append(",");
                    }
                    inSql.append("'").append(id).append("'");
                }
                sql.append(" and f.id not in (").append(inSql).append(") ");
            }
        }
        return getDynamicBySQL(input, sql.toString(), FexCustFieldRuleTenantVO.class, "f", false);
    }

    /***
     * 根据字段名称查询
     */
    public List<FexCustFieldRule> findAllBy(String id , String fieldName, String fieldCode){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" from FexCustFieldRule where validSign= '1' ");
        if(!EmptyUtils.isEmpty(id)){
            sb.append(" and id<>:id ");
            params.put("id", id);
        }
        if(!EmptyUtils.isEmpty(fieldName)){
            sb.append(" and fieldName=:fieldName ");
            params.put("fieldName", fieldName);
        }
        if(!EmptyUtils.isEmpty(fieldCode)){
            sb.append(" and fieldCode=:fieldCode ");
            params.put("fieldCode", fieldCode);
        }
        return createQuery(sb.toString(),params).list();
    }

    public List<FexCustFieldRuleTenantVO> listByCotp(String cotpGroupId){
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from FEX_CUST_FIELD_RULE_TENANT where 1=1 ");
        sb.append(" and app_range_1 = '0'  ");
        if (StringUtils.isNotEmpty(cotpGroupId)) {
            sb.append(" and app_range_2 = '" + cotpGroupId + "' ");
        } else {
            sb.append(" and app_range_2 is null ");
        }
        sb.append(" and valid_Sign= '1'");
        return listBySQLAliasToBean(sb.toString(),null,FexCustFieldRuleTenantVO.class);
    }

    public FexCustFieldRuleTenantVO getRuleById(String id){
        String sql = "  select field_name as fieldName ," +
                "  r.field_desc as fieldDesc," +
                "  r.label_bank as labelBank," +
                "  r.dict_group as dictGroup," +
                "  r.app_range_1 as appRange1,  " +
                "  r.app_range_2 as appRange2,  " +
                "  r.control_rule as controlRule, " +
                "  r.preset_val  as presetVal,  " +
                "  r.decimal_digit as decimalDigit," +
                "  r.min_val as  minVal,     " +
                "  r.maxt_val  as maxtVal,    " +
                "  r.cur_id as  curId,      " +
                "  r.valid_sign as validSign ,   "+
                " r.field_type as fieldType ,"+
                " r.field_code as fieldCode,"+
                " b.english_code as curEnglishCode "+

                " from FEX_CUST_FIELD_RULE_TENANT r " +
                "left join bt_currency b on r.cur_id = b.id " +
                "where r.id = :id";
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        return getBySQLAliasToBean(sql,param,FexCustFieldRuleTenantVO.class);
    }

    /**
     * 根据应用范围获取自定义规则
     * @param appRange1
     * @param appRange2
     * @return
     */
    public List<FexCustFieldRuleTenantVO> listByAppRange(String appRange1, String appRange2){
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from FEX_CUST_FIELD_RULE_TENANT where 1=1");
        HashMap<String,String> params = new HashMap<>();
        if(StringUtils.isNotBlank(appRange1)){
            sb.append(" and app_range_1=:appRange1");
            params.put("appRange1",appRange1);
        }
        if(StringUtils.isNotBlank(appRange2)){
            sb.append(" and (app_range_2=:appRange2 or app_range_2 is null)");
            params.put("appRange2",appRange2);
        }
        sb.append(" and valid_Sign= '1'");
        return listBySQLAliasToBean(sb.toString(),params,FexCustFieldRuleTenantVO.class);
    }

    public List<FexCustFieldRuleTenantVO> getByIds(List<String> custFieldIds) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from FEX_CUST_FIELD_RULE_TENANT where 1=1").append(" and ").append(getSQLIn(custFieldIds, 500, "id"));
        HashMap<String,String> params = new HashMap<>();

        sb.append(" and valid_Sign= '1'");
        return listBySQLAliasToBean(sb.toString(),params,FexCustFieldRuleTenantVO.class);
    }

    public List<FexCustFieldRuleTenantVO> getByFieCodes(List<String> fexCustFieCodeList) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from FEX_CUST_FIELD_RULE_TENANT where 1=1").append(" and ").append(getSQLIn(fexCustFieCodeList, 500, "field_code"));
        HashMap<String,String> params = new HashMap<>();

        sb.append(" and valid_Sign= '1'");
        return listBySQLAliasToBean(sb.toString(),params,FexCustFieldRuleTenantVO.class);
    }

    public List<FexCustFieldRuleTenantVO> listCustInfoByBusId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fcfr.id, ");
        sql.append("       fcfr.field_name, ");
        sql.append("       fcfr.field_code, ");
        sql.append("       fcfr.control_rule, ");
        sql.append("       fcfr.preset_val, ");
        sql.append("       fcfr.decimal_digit, ");
        sql.append("       fcfr.min_val, ");
        sql.append("       fcfr.maxt_val, ");
        sql.append("       fcfr.field_type, ");
        sql.append("       fcfr.label_bank, ");
        sql.append("       fcci.field_val ");
        sql.append("  from FEX_CUST_INFO_TENANT fcci ");
        sql.append("  left join FEX_CUST_FIELD_RULE_TENANT fcfr ");
        sql.append("    on fcci.field_id = fcfr.id ");
        sql.append(" where fcci.bus_id = :id  and fcfr.valid_Sign= '1'");
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",id);
        return listBySQLAliasToBean(sql.toString(), params, FexCustFieldRuleTenantVO.class);
    }
}
