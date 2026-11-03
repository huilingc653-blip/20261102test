package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpTemplateTypeDtlDao;
import com.bytter.biz.transfer.swap.entity.ErpTemplateTypeDtl;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/19.
 */
@Service("erpTemplateTypeDtlService")
@Transactional
public class ErpTemplateTypeDtlService extends BaseService<ErpTemplateTypeDtl,String> {
    @Autowired
    private ErpTemplateTypeDtlDao templateTypeDtlDao;
    @Override
    public BaseDao<ErpTemplateTypeDtl, String> getEntityDao() {
        return templateTypeDtlDao;
    }
    public List<ErpTemplateTypeDtlVO> selectTypeDtlByTypeId(String id) {
        return templateTypeDtlDao.selectTypeDtlWithSourceCode(id);
    }
    public List<ErpTemplateTypeDtl> selectShowTypeDtlByTypeId(String id, boolean isshow) {
        String hql = "from ErpTemplateTypeDtl where typeId=:id and templateDtl=:isshow and " +
                " (show=1 or notNull=1)  order by orderNum ";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("id",id);params.put("isshow",isshow);
        return templateTypeDtlDao.listByHQLAliasToBean(hql,params,ErpTemplateTypeDtl.class);
    }
}
