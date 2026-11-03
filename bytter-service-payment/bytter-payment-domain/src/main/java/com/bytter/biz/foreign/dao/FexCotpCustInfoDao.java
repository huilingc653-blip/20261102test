package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCotpCustInfo;
import com.bytter.biz.foreign.vo.FexCotpCustInfoVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FexCotpCustInfoDao extends BaseDao<FexCotpCustInfo,String> {


    /**
     * 根据主表id删除数据
     * @param id
     */
    public void deleteCustFieldInfoByBusId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from FEX_COTP_CUST_INFO where bus_id = '" + id + "'");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /**
     * 查询自定义信息
     * @return
     */
    public List<FexCotpCustInfoVO> listCustInfoByBusId(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select fcfr.id, ");
        sql.append("       fcfr.field_name, ");
        sql.append("       fcfr.control_rule, ");
        sql.append("       fcfr.preset_val, ");
        sql.append("       fcfr.decimal_digit, ");
        sql.append("       fcfr.min_val, ");
        sql.append("       fcfr.maxt_val, ");
        sql.append("       fcfr.field_type, ");
        sql.append("       fcci.field_val ");
        sql.append("  from FEX_COTP_CUST_INFO fcci ");
        sql.append("  left join FEX_CUST_FIELD_RULE fcfr ");
        sql.append("    on fcci.field_id = fcfr.id ");
        sql.append(" where fcci.bus_id = :id and fcfr.valid_Sign= '1' ");
        return listBySQLAliasToBean(sql.toString(), params, FexCotpCustInfoVO.class);
    }

    public Boolean getCustInfoByBusFieldId(String fieldId){
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from  FEX_COTP_CUST_INFO where  field_id = :fieldId ");
        Map<String,Object> param = new HashMap<>();
        param.put("fieldId",fieldId);
        List<FexCotpCustInfo> list = listBySQL(sql.toString(),param);
        if(list.size() > 0){
            return true;
        }else {
            return false;
        }
    }

}
