package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpInterConfig;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
/**
 * Created by zhouj on 2018/5/28.
 */
@Repository
public class ErpInterConfigDao extends BaseDao<ErpInterConfig,String> {
    public ErpInterConfig getByTypeAndCode(ErpInterConfig config) {
        String hql = "from ErpInterConfig where type=?0 and code=?1";
        return getUnique(hql, config.getType(), config.getCode());
    }
    public ErpInterConfig getByTypeAndCode(String type,String code) {
        String hql = "from ErpInterConfig where type=?0 and code=?1";
        return getUnique(hql, type, code);
    }
}
