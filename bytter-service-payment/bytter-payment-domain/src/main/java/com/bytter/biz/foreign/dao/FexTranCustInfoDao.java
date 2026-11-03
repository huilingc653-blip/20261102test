package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranCustInfo;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 交易管理自定义信息表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexTranCustInfoDao extends BaseDao<FexTranCustInfo,String> {


    /**
     * 根据主表id删除数据
     * @param id
     */
    public void deleteCustFieldInfoByBusId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from fex_tran_cust_info where bus_id = '" + id + "'");
        createSQLQuery(sql.toString()).executeUpdate();
    }

    /**
     * 查询自定义信息
     * @return
     */
    public List<FexTranCustInfoVO> listCustInfoByBusId(String id){
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
        sql.append("  from fex_tran_cust_info fcci ");
        sql.append("  left join fex_cust_field_rule fcfr ");
        sql.append("    on fcci.field_id = fcfr.id ");
        sql.append(" where fcci.bus_id = :id  and fcfr.valid_Sign= '1'");
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",id);
        return listBySQLAliasToBean(sql.toString(), params, FexTranCustInfoVO.class);
    }

    public Boolean getCustInfoByBusFieldId(String fieldId){
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from  fex_tran_cust_info where  field_id = :fieldId ");
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
