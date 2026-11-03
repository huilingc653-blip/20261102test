package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpTemplateDatadefineDao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDatadefine;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * Created by zhouj on 2018/5/18.
 */
@Service("erpTemplateDatadefineService")
@Transactional
public class ErpTemplateDatadefineService extends BaseService<ErpTemplateDatadefine,String> {
    @Autowired
    private ErpTemplateDatadefineDao erpTemplateDatadefineDao;
    public List<ErpTemplateDatadefine> listDataByDtl(boolean flag) {
        return erpTemplateDatadefineDao.listBy("templateDtl",flag,"orderNum",true);
    }
    @Override
    public BaseDao<ErpTemplateDatadefine, String> getEntityDao() {
        return erpTemplateDatadefineDao;
    }
}
