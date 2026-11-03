package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigFill;
import com.bytter.biz.transfer.swap.vo.ErpInterConfigFillVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/28.
 */
@Repository
public class ErpInterConfigFillDao extends BaseDao<ErpInterConfigFill,String> {
    public List<ErpInterConfigFill> queryErpFieldValid(String type) {
        StringBuilder sql=new StringBuilder("from ErpInterConfigFill e where e.validSign='1' and type =:type order by e.orderNum asc");
        Map<String,Object> map =new HashMap<>();
        map.put("type",type);
        List<ErpInterConfigFill> listInfo= this.listByHQLAliasToBean(sql.toString(),map,ErpInterConfigFill.class);
        return listInfo;
    }
    /**
     * 配置明细信息
     *
     * @return list集合
     */
    public List<ErpInterConfigFillVO> listErpInterConfigFillInfoByType(String type) {
        StringBuilder sql=new StringBuilder("select ")
                .append("f.id as id,f.type as type,e.field_code as fieldCode,e.field_name as fieldName,")
                .append("f.show_name as showName,f.is_not_null as isNotNull,f.valid_sign as validSign, ")
                .append("f.input_type as inputType,f.is_input as isInput,f.data_source as dataSource,f.ordernum as orderNum ")
                .append("from erp_inter_config_fill f ")
                .append("join erp_datatype_field e on f.type=e.type and f.field_code=e.field_code ")
                .append("where e.valid_sign='1' and f.type=:type ")
                .append("order by f.ordernum asc");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("type", type);
        List<ErpInterConfigFillVO> listInfo= this.listBySQLAliasToBean(sql.toString(), params, ErpInterConfigFillVO.class);
        return listInfo;
    }
    /**
     * 清空当前
     * @param type
     * @return
     */
    public int deleteEntity(String type){
        StringBuilder HQL=new StringBuilder("delete from ErpInterConfigFill where type =:type ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("type", type);
        return this.batchExecute(HQL.toString(),params);
    }
}
