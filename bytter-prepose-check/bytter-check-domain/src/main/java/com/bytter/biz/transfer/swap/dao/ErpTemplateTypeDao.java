package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateType;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/19.
 */
@Repository
public class ErpTemplateTypeDao extends BaseDao<ErpTemplateType,String> {
    public ErpTemplateTypeVO selectTypeAndNameById(String id) {
        String sql = "select t.ID as id,t.TYPE_CODE as typeCode,t.TYPE_NAME as typeName,t.MATCHING_FIELD as matchingField,d.FIELD_NAME as matchingName,t.RMK as rmk "+
                "from erp_template_type t left join erp_template_datadefine d on t.MATCHING_FIELD=d.FIELD_CODE where t.ID=:id";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        List<ErpTemplateTypeVO> list = listBySQLAliasToBean(sql,params,ErpTemplateTypeVO.class);
        if(EmptyUtils.isEmptyList(list)){
            return new ErpTemplateTypeVO();
        }
        return list.get(0);
    }
}
