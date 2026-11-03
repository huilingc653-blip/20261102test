package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpTemplateDtlDao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/26.
 */
@Service("erpTemplateDtlService")
@Transactional
public class ErpTemplateDtlService extends BaseService<ErpTemplateDtl,String> {
    @Autowired
    private ErpTemplateDtlDao erpTemplateDtlDao;
    @Override
    public BaseDao<ErpTemplateDtl, String> getEntityDao() {
        return erpTemplateDtlDao;
    }
    public List<ErpTemplateDtl> selectDtlByTypeId(String id) {
        return erpTemplateDtlDao.listBy("templateId",id);
    }
    public List<ErpTemplateDtl> selectDtlMapById(String id) {
        String hql = "from ErpTemplateDtl where templateId=:id order by loanDirection desc";
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",id);
        return erpTemplateDtlDao.listByHQLAliasToBean(hql,map,ErpTemplateDtl.class);
    }
    public boolean deleteByTemplateId(String templateId) {
        return erpTemplateDtlDao.deleteByTemplateId(templateId)>0;
    }
}
