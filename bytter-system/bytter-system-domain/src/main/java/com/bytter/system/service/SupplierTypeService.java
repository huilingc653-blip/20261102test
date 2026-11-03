package com.bytter.system.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.dao.SupplierTypeDao;
import com.bytter.system.entity.SupplierType;
@Service("supplierTypeService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SupplierTypeService extends BaseService<SupplierType, String> {
    @Autowired
    private SupplierTypeDao supplierTypeDao;
    @Override
    public BaseDao<SupplierType, String> getEntityDao() {
        return supplierTypeDao;
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(SupplierType entity) {
        entity.setStatus("-2");
        this.updateEntity(entity);
    }
    /**
     * 根据ID删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        SupplierType entity = get(id);
        entity.setStatus("-2");
        this.updateEntity(entity);
    }
    /**
     * 批量删除,逻辑删除
     *
     * @param externalIds
     */
    public void deleteByIds(List<String> externalIds) {
        SupplierType type;
        for (String id : externalIds) {
            type = get(id);
            type.setStatus("-2");
            this.updateEntity(type);
        }
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(SupplierType entity) {
        super.updateEntity(entity);
    }
    public void save(SupplierType entity) {
        super.saveEntity(entity);
    }
    /**
     * 保存或更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(SupplierType entity) {
        super.saveOrUpdate(entity);
    }
    public DataTablesOutput dataTables(DataTablesInput input) {
        return supplierTypeDao.datatables(input);
    }
    /**
     * 获取有效的供应商类别
     *
     * @return
     */
    public List<SupplierType> getValidSign() {
        return supplierTypeDao.getValidSign();
    }
    public SupplierType getEntityByCode(String code) {
        return supplierTypeDao.getEntityByCode(code);
    }
    public SupplierType getEntityByName(String name) {
        return supplierTypeDao.getEntityByName(name);
    }
}
