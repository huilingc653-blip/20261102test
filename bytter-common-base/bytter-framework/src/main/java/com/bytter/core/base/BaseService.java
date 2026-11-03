package com.bytter.core.base;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.base.entity.TreeEntity;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.persistence.Page;
import com.bytter.core.common.persistence.PropertyFilter;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
/**
 * 基础service 所有service继承该类 提供基础的实体操作方法 使用HibernateDao&lt;T,PK&gt;进行业务对象的CRUD操作,子类需重载getEntityDao()函数提供该DAO.
 *
 * @param <T>
 *            实体类
 * @param <PK>
 *            实体类主键类型
 * @author zhoul
 * @since 2018/1/15.
 */
public abstract class BaseService<T, PK extends Serializable> extends AbstractService {
    /**
     * 子类需要实现该方法，提供注入的dao
     *
     * @return BaseDao
     */
    public abstract BaseDao<T, PK> getEntityDao();
    protected Session getSession() {
        return getEntityDao().getSession();
    }
    protected Criteria createCriteria() {
        return getEntityDao().createCriteria();
    }
    @Transactional(readOnly = true)
    public T get(final PK id) {
        return getEntityDao().get(id);
    }
    /**
     * 根据主键获取实体
     *
     * @param id
     *            主键
     * @param eager
     *            是否急切获取对象，true是，false否为懒加载
     * @return 实体对象
     */
    @Transactional(readOnly = true)
    public T get(final PK id, boolean eager) {
        return getEntityDao().get(id, eager);
    }
    @Transactional
    public void saveEntity(final T entity) {
        getEntityDao().save(entity);
    }
    @Transactional
    public void batchSave(final List<T> entityList, final Integer batchSize) {
        getEntityDao().batchSave(entityList, batchSize);
    }
    @Transactional
    public void batchSave(final List<T> entityList) {
        getEntityDao().batchSave(entityList);
    }
    @Transactional
    public void updateEntity(final T entity) {
        getEntityDao().update(entity);
    }
    @Transactional
    public void batchUpdate(final List<T> entityList) {
        getEntityDao().batchUpdate(entityList);
    }
    @Transactional
    public void batchUpdate(final List<T> entityList, final Integer batchSize) {
        getEntityDao().batchUpdate(entityList, batchSize);
    }
    @Transactional
    public void saveOrUpdate(final T entity) {
        getEntityDao().saveOrUpdate(entity);
    }
    @Transactional
    public void batchSaveOrUpdate(final List<T> entityList) {
        getEntityDao().batchSaveOrUpdate(entityList);
    }
    @Transactional
    public void batchSaveOrUpdate(final List<T> entityList, final Integer batchSize) {
        getEntityDao().batchSaveOrUpdate(entityList, batchSize);
    }
    /**
     * 注意 实体dao必须重写specificBatchSave方法，否则不会保存数据，该操作的正确与否全由数据库的操作决定
     *
     * @param entityList
     * @param batchSize
     */
    @Transactional
    public void batchJdbcSave(final List<T> entityList, final int batchSize) {
        getEntityDao().batchJdbcSave(entityList, batchSize);
    }
    /**
     * 注意 实体dao不必须重写specificBatchSave方法，一个表需要插入哪些字段是由saveAttrs决定
     *
     * @param entityList
     * @param saveAttrs
     *            需要插入的字段对应的实体属性
     * @param batchSize
     */
    @Transactional
    public void batchJdbcSave(final List<T> entityList, List<String> saveAttrs, final int batchSize) {
        getEntityDao().batchJdbcSave(entityList, saveAttrs, batchSize);
    }
    @Transactional
    public void merge(final T entity) {
        getEntityDao().merge(entity);
    }
    /**
     * 将数据库中的记录同步到实体，数据库 --&gt; 实体
     *
     * @param entity
     *            实体
     */
    public void refresh(final T entity) {
        getEntityDao().refresh(entity);
    }
    /**
     * 清除一级缓存指定实体
     *
     * @param entity
     *            实体
     */
    public void evict(final T entity) {
        getEntityDao().evict(entity);
    }
    @Transactional
    public void deleteEntity(final T entity) {
        getEntityDao().delete(entity);
    }
    @Transactional
    public void delete(final PK id) {
        getEntityDao().delete(id);
    }
    @Transactional
    public void delete(final List<PK> ids) {
        getEntityDao().delete(ids);
    }
    @Transactional(readOnly = true)
    public List<T> listAll() {
        return getEntityDao().listAll();
    }
    @Transactional(readOnly = true)
    public List<T> listAll(Boolean isCache) {
        return getEntityDao().listAll(isCache);
    }
    /**
     * 获取全部对象, 支持按属性行序.
     *
     * @param orderByProperty
     *            排序属性name
     * @param isAsc
     *            是否升序排序
     * @return 查询结果集合
     */
    @Transactional(readOnly = true)
    public List<T> listAll(String orderByProperty, boolean isAsc) {
        return getEntityDao().listAll(orderByProperty, isAsc);
    }
    @Transactional(readOnly = true)
    public List<T> search(final List<PropertyFilter> filters) {
        return getEntityDao().list(filters);
    }
    @Transactional(readOnly = true)
    public Page<T> search(final Page<T> page, final List<PropertyFilter> filters) {
        return getEntityDao().findPage(page, filters);
    }
    /**
     * 获取树形数据结构的树
     *
     * @param mList
     *            需要转换的数据
     * @param <M>
     *            集合中的对象类型
     * @return 带结构的集合
     */
    public <M extends TreeEntity<M>> List<M> listToTree(List<M> mList) {
        return TreeUtil.coverTreeList(mList);
    }
    /**
     * 获取树形顺序的数据
     *
     * @param mList
     *            需要转换的数据
     * @param <M>
     *            集合中的对象类型
     * @return 带结构的集合
     * 这个时间复杂度 O(n^2)
     */
    public <M extends TreeEntity<M>> List<M> listToTreeTable(List<M> mList) {
        return TreeUtil.createTreeTier(mList);
    }
    @Deprecated
    @Transactional(readOnly = true)
    public DataTablesOutput<T> getDataTables(DataTablesInput input) {
        return getEntityDao().findPage(input);
    }
    @Deprecated
    @Transactional(readOnly = true)
    public DataTablesOutput<T> getDataTables(DataTablesInput input, Boolean cache) {
        return getEntityDao().findPage(input, cache);
    }
    @Deprecated
    @Transactional(readOnly = true)
    public DataTablesOutput<T> getDataTables(DataTablesInput input, Boolean cache, String region) {
        return getEntityDao().findPage(input, cache, region);
    }


    /***
     * 使用该方法必须是登录成功以后，如果没有登录返回为空，在使用地方就会报空指针，就知道那里不能调用该方法。
     */
    protected ICurrentUser getCurrentUser() {
        return UserContextHelper.helper().getCurrentUser();
    }

}
