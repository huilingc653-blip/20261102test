package com.bytter.supply.domain.payable.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.supply.domain.payable.dao.ScfPayFieldConfigDao;
import com.bytter.supply.domain.payable.entity.ScfPayFieldConfig;
import com.bytter.supply.domain.payable.vo.ScfPayFieldConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("scfPayFieldConfigService")
@Transactional(rollbackFor = Exception.class)
public class ScfPayFieldConfigService extends BaseService<ScfPayFieldConfig, String> {

    @Autowired
    private ScfPayFieldConfigDao scfPayFieldConfigDao;

    @Override
    public BaseDao<ScfPayFieldConfig, String> getEntityDao() {
        return scfPayFieldConfigDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return scfPayFieldConfigDao.getDataTablesUnion(dataTablesInput);
    }

    public void saveScfPayFieldConfig(ScfPayFieldConfigVO vo) {
        ScfPayFieldConfig entity = new ScfPayFieldConfig();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayFieldConfigDao.save(entity);
    }

    public void modify(ScfPayFieldConfigVO vo) {
        ScfPayFieldConfig entity = new ScfPayFieldConfig();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayFieldConfigDao.update(entity);
    }

    public void deleteColumn(String id) {
        ScfPayFieldConfig entity = this.get(id);
        this.deleteEntity(entity);
    }
}
