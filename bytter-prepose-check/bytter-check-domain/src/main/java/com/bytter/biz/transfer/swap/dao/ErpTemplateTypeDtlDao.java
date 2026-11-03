package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateTypeDtl;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/19.
 */
@Repository
public class ErpTemplateTypeDtlDao extends BaseDao<ErpTemplateTypeDtl,String> {
    public List<ErpTemplateTypeDtlVO> selectTypeDtlWithSourceCode(String id) {
        String sql = "select t.ID as id,t.TYPE_ID as typeId,t.FIELD_CODE as fieldCode,t.FIELD_NAME as fieldName,t.SHOW_NAME as showName" +
                ",t.IS_SHOW as isShow,CASE WHEN t.is_show='0' THEN '否' ELSE '是' END AS showStr" +
                ",t.IS_NOT_NULL as isNotNull,CASE WHEN t.is_not_null='0' THEN '否' ELSE '是' END AS notNullStr," +
                "t.IS_TEMPLATE_DTL as isTemplateDtl,t.SOURCE_CODE as sourceCode,d.NAME as sourceName,t.INPUT_TYPE_CODE as inputTypeCode from erp_template_type_dtl t " +
                "left join ERP_DATA_TYPE d on t.SOURCE_CODE=d.CODE where t.TYPE_ID=:id order by t.order_num";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);
        List<ErpTemplateTypeDtlVO> list = listBySQLAliasToBean(sql,params,ErpTemplateTypeDtlVO.class);
        if(EmptyUtils.isEmptyList(list)){
            return new ArrayList<ErpTemplateTypeDtlVO>();
        }
        return list;
    }
}
