package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpInterConfigSetDao;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigSet;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Created by zhouj on 2018/5/28.
 */
@Transactional
@Service("erpInterConfigSetService")
public class ErpInterConfigSetService extends BaseService<ErpInterConfigSet,String> {
    @Autowired
    private ErpInterConfigSetDao erpInterConfigSetDao;
    @Override
    public BaseDao<ErpInterConfigSet, String> getEntityDao() {
        return erpInterConfigSetDao;
    }
}
