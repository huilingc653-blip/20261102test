package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCustInfoTenant;
import com.bytter.biz.foreign.entity.FexTranCustInfo;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户交易自定义信息DAO。
 */
@Repository
public class FexCustInfoTenantDao extends BaseDao<FexCustInfoTenant, String> {

    /**
     * 根据主表id删除数据
     * @param id
     */
    public void deleteCustFieldInfoByBusId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from FEX_CUST_INFO_TENANT where bus_id = '" + id + "'");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /**
     * 查询自定义信息
     * @return
     */
    public List<FexTranCustInfoVO> listCustInfoByBusId(String id){
        StringBuilder sql = new StringBuilder();
        sql.append("select fcfr.id, ");
        sql.append("       coalesce(fcfr.field_name, fcci.field_name) as field_name, ");
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
        sql.append(" where fcci.bus_id = :id");
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",id);
        return listBySQLAliasToBean(sql.toString(), params, FexTranCustInfoVO.class);
    }

    /**
     * 查询企业自定义字段规则及已保存值（规则全量展示，有值则回显）。
     *
     * @param busId 业务主表ID
     * @param appRange1 应用范围1
     * @param appRange2 应用范围2
     * @return 字段规则及值列表
     */
    public List<FexTranCustInfoVO> listCustInfoByBusIdAndAppRange(String busId, String appRange1, String appRange2){
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
        sql.append("  from FEX_CUST_FIELD_RULE_TENANT fcfr ");
        sql.append("  left join FEX_CUST_INFO_TENANT fcci ");
        sql.append("    on fcci.field_id = fcfr.id and fcci.bus_id = :busId ");
        sql.append(" where fcfr.valid_Sign = '1' ");
        sql.append("   and fcfr.app_range_1 = :appRange1 ");
        sql.append("   and (fcfr.app_range_2 = :appRange2 or fcfr.app_range_2 is null)");
        HashMap<String,Object> params = new HashMap<>();
        params.put("busId", busId);
        params.put("appRange1", appRange1);
        params.put("appRange2", appRange2);
        return listBySQLAliasToBean(sql.toString(), params, FexTranCustInfoVO.class);
    }

    public Boolean getCustInfoByBusFieldId(String fieldId){
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from  FEX_CUST_INFO_TENANT where  field_id = :fieldId ");
        Map<String,Object> param = new HashMap<>();
        param.put("fieldId",fieldId);
        List<FexTranCustInfo> list = listBySQL(sql.toString(),param);
        if(list.size() > 0){
            return true;
        }else {
            return false;
        }
    }
}
