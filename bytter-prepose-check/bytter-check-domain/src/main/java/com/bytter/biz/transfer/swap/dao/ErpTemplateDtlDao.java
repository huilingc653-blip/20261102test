package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/26.
 */
@Repository
public class ErpTemplateDtlDao extends BaseDao<ErpTemplateDtl,String> {
    public int deleteByTemplateId(String templateId) {
        StringBuilder hql=new StringBuilder("delete from ErpTemplateDtl where templateId=:templateId ");
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("templateId",templateId);
        return this.batchExecute(hql.toString(), params);
    }
}
