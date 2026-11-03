package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpDataTypeField;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/28.
 */
@Repository
public class ErpDataTypeFieldDao extends BaseDao<ErpDataTypeField,String> {
    public List<ErpDataTypeField> queryErpDataTypeFieldValid(String type) {
        StringBuilder sql=new StringBuilder("from ErpDataTypeField e where e.validSign='1' and type =:type order by e.orderNum asc");
        Map<String,Object> map =new HashMap<>();
        map.put("type",type);
        List<ErpDataTypeField> listInfo= this.listByHQLAliasToBean(sql.toString(),map,ErpDataTypeField.class);
        return listInfo;
    }
}
