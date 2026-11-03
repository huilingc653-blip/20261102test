package com.bytter.biz.transfer.swap.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.transfer.swap.dao.ErpTemplateDtlStoreDao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtlStore;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
 * Created by zhouj on 2018/5/29.
 */
@Service("erpTemplateDtlStoreService")
@Transactional
public class ErpTemplateDtlStoreService extends BaseService<ErpTemplateDtlStore,String> {
    @Autowired
    private ErpTemplateDtlStoreDao templateDtlStoreDao;
    @Override
    public BaseDao<ErpTemplateDtlStore, String> getEntityDao() {
        return templateDtlStoreDao;
    }
    public void deleteByStoreId(String storeId) {
        templateDtlStoreDao.deleteByStoreId(storeId);
    }
    
    public List<ErpTemplateDtlStore> selectDtlMapById(String id) {
        String hql = "from ErpTemplateDtlStore where storeId=:id";
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",id);
        return templateDtlStoreDao.listByHQLAliasToBean(hql,map,ErpTemplateDtlStore.class);
    }
}
