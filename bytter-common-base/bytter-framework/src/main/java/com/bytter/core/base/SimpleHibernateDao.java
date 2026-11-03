package com.bytter.core.base;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.Reflections;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 封装Hibernate原生API的DAO泛型基类.<br>
 * 可在Service层直接使用, 也可以扩展泛型DAO子类使用, 见两个构造函数的注释. 取消了HibernateTemplate, 直接使用Hibernate原生API.
 *
 * @param <T>
 *            DAO操作的对象类型
 * @param <PK>
 *            主键类型
 * @author zhoul
 * @since 2018/1/15.
 */
public abstract class SimpleHibernateDao<T, PK extends Serializable> extends AbstractDao {
    protected Class<T> entityClass;
    /**
     * 用于Dao层子类使用的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends SimpleHibernateDao&lt;User, String&gt;
     */
    public SimpleHibernateDao() {
        this.entityClass = Reflections.getClassGenricType(getClass());
    }
    /**
     * 按id删除对象.
     *
     * @param id
     *            实体主键
     */
    public void delete(final PK id) {
        delete(get(id));
    }
    /**
     * 根据主键集合删除数据，执行物理删除
     *
     * @param ids
     *            主键集合
     */
    public void delete(final List<PK> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return;
        }
        StringBuilder hql = new StringBuilder();
        hql.append("delete from ").append(entityClass.getSimpleName()).append(" where id in (:ids)");
        Map<String, List<PK>> map = new HashMap<>();
        map.put("ids", ids);
        Query query = createQuery(hql.toString(), map);
        query.executeUpdate();
    }
    /**
     * 按id获取对象.懒加载
     *
     * @param id
     *            实体主键
     * @return T
     */
    public T get(final PK id) {
        return get(id, false);
    }
    /**
     * 按id获取对象
     *
     * @param id
     *            实体主键
     * @param eager
     *            是否急切获取对象，true是，false否为懒加载
     * @return T
     */
    public T get(final PK id, boolean eager) {
        return eager ? get(entityClass, id) : load(entityClass, id);
    }
    /**
     * 按id列表获取对象列表.
     *
     * @param idList
     * @return 对象集合
     */
    public List<T> list(final Collection<PK> idList) {
        Criterion criterionList = null;
        // 针对ID IN超过1000数据库报错问题处理 zhengg 20200827
        int maxSize = 1000;
        if (idList != null && idList.size() > maxSize && idList instanceof List) {
            List<PK> list = (List<PK>) idList;
            int remainder = idList.size() % maxSize;
            int num = idList.size() / maxSize + (remainder > 0 ? 1 : 0);
            for (int i = 0; i < num; i++) {
                if (i == 0) {
                    criterionList = Restrictions.in(getIdName(), list.subList(0, maxSize));
                } else {
                    int beginNum = maxSize * i;
                    int endNum = maxSize * (i + 1);
                    if ((i + 1) == num) {
                        endNum = beginNum + remainder;
                    }
                    criterionList = Restrictions.or(criterionList,
                            Restrictions.in(getIdName(), list.subList(beginNum, endNum)));
                }
            }
        } else {
            criterionList = Restrictions.in(getIdName(), idList);
        }
        return list(criterionList);
        // return list(Restrictions.in(getIdName(), idList));
    }
    /**
     * 获取全部对象.
     *
     * @return 对象集合.
     */
    public List<T> listAll() {
        return list();
    }
    /**
     * 获取全部对象.
     *
     * @param isCache
     *            是否缓存
     * @return 对象集合.
     */
    public List<T> listAll(Boolean isCache) {
        return list(isCache);
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
    public List<T> listAll(String orderByProperty, boolean isAsc) {
        Criteria c = createCriteria();
        if (isAsc) {
            c.addOrder(Order.asc(orderByProperty));
        } else {
            c.addOrder(Order.desc(orderByProperty));
        }
        return c.list();
    }
    /**
     * 按属性查找对象列表, 匹配方式为相等
     *
     * @param propertyName
     *            属性name
     * @param value
     *            属性值
     * @return 结果集合
     */
    public List<T> listBy(final String propertyName, final Object value) {
        return listBy(propertyName, value, null, false);
    }
    /**
     * 用于单个条件带排序实现QBC查询
     *
     * @param propertyName
     *            属性名称
     * @param value
     *            对象
     * @param orderByProperty
     *            排序属性
     * @param isAsc
     *            是否正序
     * @return 结果集
     */
    public List<T> listBy(final String propertyName, final Object value, String orderByProperty, boolean isAsc) {
        Criteria criteria = createCriteria();
        if (StringUtils.isNotBlank(propertyName)) {
            criteria.add(Restrictions.eq(propertyName, value));
        }
        if (StringUtils.isNotBlank(orderByProperty)) {
            if (isAsc) {
                criteria.addOrder(Order.asc(orderByProperty));
            } else {
                criteria.addOrder(Order.desc(orderByProperty));
            }
        }
        return criteria.list();
    }
    /**
     * 按属性查找唯一对象, 匹配方式为相等
     *
     * @param propertyName
     *            属性name
     * @param value
     *            属性值
     * @return 结果对象
     */
    public T getUniqueBy(final String propertyName, final Object value) {
        Criterion criterion = Restrictions.eq(propertyName, value);
        return (T) createCriteria(criterion).uniqueResult();
    }
    /**
     * 按HQL查询唯一对象.
     *
     * @param hql
     *            查询HQL
     * @param values
     *            数量可变的参数,按顺序绑定.
     * @return X
     */
    public <X> X getUnique(final String hql, final Object... values) {
        return (X) createQuery(hql, values).uniqueResult();
    }
    /**
     * 按HQL查询唯一对象，带缓存
     *
     * @param hql
     *            查询HQL语句
     * @param values
     *            数量可变的参数,按顺序绑定.
     * @param <X>
     *            转换对象的对象类型
     * @return X
     */
    public <X> X getUniqueCache(final String hql, final Object... values) {
        Query query = createQuery(hql, values);
        query.setCacheable(true);
        return (X) query.uniqueResult();
    }
    /**
     * 按HQL查询唯一对象.
     *
     * @param hql
     *            查询HQL语句
     * @param values
     *            命名参数,按名称绑定.
     * @return 对象
     */
    public <X> X getUnique(final String hql, final Map<String, ?> values) {
        return (X) createQuery(hql, values).uniqueResult();
    }
    /**
     * 按HQL查询唯一对象.，带缓存
     *
     * @param hql
     *            查询HQL语句
     * @param values
     *            命名参数,按名称绑定.
     * @return 对象
     */
    public <X> X getUniqueCache(final String hql, final Map<String, ?> values) {
        Query query = createQuery(hql, values);
        query.setCacheable(true);
        return (X) query.uniqueResult();
    }
    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param hql
     *            查询HQL语句
     * @param values
     *            数量可变的参数,按顺序绑定.
     * @return 更新记录数.
     */
    public int batchExecute(final String hql, final Object... values) {
        return createQuery(hql, values).executeUpdate();
    }
    /**
     * 执行HQL进行批量修改/删除操作.
     *
     * @param hql
     *            查询HQL语句
     * @param values
     *            命名参数,按名称绑定.
     * @return 更新记录数.
     */
    public int batchExecute(final String hql, final Map<String, ?> values) {
        return createQuery(hql, values).executeUpdate();
    }
    /**
     * 执行Sql进行批量修改/删除操作.
     *
     * @param sql
     *            查询sql语句
     * @param values
     *            数量可变的参数,按顺序绑定.
     * @return 更新记录数.
     */
    public int batchSQLExecute(final String sql, final Object... values) {
        return createSQLQuery(sql, values).executeUpdate();
    }
    /**
     * 执行Sql进行批量修改/删除操作.
     *
     * @param sql
     *            查询sql语句
     * @param values
     *            命名参数,按名称绑定.
     * @return 更新记录数.
     */
    public int batchSQLExecute(final String sql, final Map<String, ?> values) {
        return createSQLQuery(sql, values).executeUpdate();
    }
    /**
     * 按Criteria查询对象列表.
     *
     * @param criterionList
     *            数量可变的Criterion.
     * @return 结果集合
     */
    public List<T> list(final Criterion... criterionList) {
        return createCriteria(criterionList).list();
    }
    /**
     * 按Criteria查询对象列表.
     *
     * @param isCache
     *            是否使用缓存
     * @param criterionList
     *            数量可变的Criterion.
     * @return 结果集合
     */
    public List<T> list(Boolean isCache, final Criterion... criterionList) {
        return createCriteria(isCache, criterionList).list();
    }
    /**
     * 按Criteria查询唯一对象.
     *
     * @param criterionList
     *            数量可变的Criterion.
     * @return 对象
     */
    public T getUnique(final Criterion... criterionList) {
        return (T) createCriteria(criterionList).uniqueResult();
    }
    public Criteria createCriteria() {
        return getSession().createCriteria(entityClass);
    }
    /**
     * 根据Criterion条件创建Criteria. 与find()函数可进行更加灵活的操作.
     *
     * @param criterionList
     *            数量可变的Criterion.
     * @return {@link Criteria}
     */
    public Criteria createCriteria(final Criterion... criterionList) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterionList) {
            criteria.add(c);
        }
        return criteria;
    }
    public Criteria createCriteria(ProjectionList pList, final Criterion... criterionList) {
        DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
        dc.setProjection(pList);
        dc.setResultTransformer(Transformers.aliasToBean(entityClass));
        Criteria criteria = dc.getExecutableCriteria(getSession());
        for (Criterion c : criterionList) {
            criteria.add(c);
        }
        return criteria;
    }
    public Criteria createCriteria(Boolean isCache, final Criterion... criterionList) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterionList) {
            criteria.add(c);
        }
        criteria.setCacheable(isCache);
        return criteria;
    }
    /**
     * 为Query添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
     *
     * @param query
     *            查询对象
     * @return Query
     */
    public Query distinct(Query query) {
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return query;
    }
    /**
     * 为Criteria添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
     *
     * @param criteria
     *            {@link Criteria}
     * @return Criteria
     */
    public Criteria distinct(Criteria criteria) {
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }
    /**
     * 取得对象的主键名.
     *
     * @return 对象的主键名
     */
    public String getIdName() {
        ClassMetadata meta = getSessionFactory().getClassMetadata(entityClass);
        return meta.getIdentifierPropertyName();
    }
    /**
     * 判断对象的属性值在数据库内是否唯一. 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
     *
     * @param propertyName
     *            属性name
     * @param newValue
     *            新值
     * @param oldValue
     *            旧值
     * @return 是否唯一
     */
    public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
        if (newValue == null || newValue.equals(oldValue)) {
            return true;
        }
        Object object = getUniqueBy(propertyName, newValue);
        return (object == null);
    }
}
