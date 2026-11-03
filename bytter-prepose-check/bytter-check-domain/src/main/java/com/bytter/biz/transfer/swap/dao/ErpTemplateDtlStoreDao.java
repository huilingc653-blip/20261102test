package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtlStore;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/29.
 */
@Repository
public class ErpTemplateDtlStoreDao extends BaseDao<ErpTemplateDtlStore,String> {
    public int deleteByStoreId(String storeId) {
        StringBuilder hql = new StringBuilder("delete from ErpTemplateDtlStore where storeId=:storeId ");
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("storeId",storeId);
        return this.batchExecute(hql.toString(), params);
    }
}
