package com.bytter.core.base;
import cn.hutool.core.bean.BeanUtil;
import com.bytter.core.base.entity.EntityTableFieldInfo;
import com.bytter.core.common.hibernate.id.UUIDGenerator;
import com.bytter.core.common.hibernate.internal.SQLQueryImplAdapter;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
/**
 * DAO层操作基础类
 *
 * @author zhoul
 * @since 2018/1/15.
 */
public abstract class AbstractDao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    // protected final Logger logger = Logger.getLogger(getClass());
    @Autowired
    protected ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager entityManager;
    private Map<String, IdentifierGenerator> idGenNameIdGenMap = new HashMap<>();
    /**
     * 取得当前Session.
     *
     * @return Session
     */
    protected Session getSession() {
        return (Session) entityManager.getDelegate();
    }
    /**
     * 获得hibernate的session工厂
     *
     * @return SessionFactory
     */
    protected SessionFactory getSessionFactory() {
        Session session = getSession();
        if (session == null) {
            return null;
        }
        return session.getSessionFactory();
    }
    /**
     * 创建HQL查询语句
     *
     * @param queryString
     *            HQL
     * @return HQL查询对象
     */
    protected Query createQuery(String queryString) {
        return getSession().createQuery(queryString);
    }
    /**
     * @param queryString
     *            执行的SQL语句
     * @return SQLQueryImpl适配器
     */
    protected SQLQuery createSQLQuery(String queryString) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        return new SQLQueryImplAdapter(sqlQuery);
    }
    public void evict(Object object) {
        getSession().evict(object);
    }
    public void replicate(Object object, ReplicationMode replicationMode) {
        getSession().replicate(object, replicationMode);
    }
    public void replicate(String entityName, Object object, ReplicationMode replicationMode) {
        getSession().replicate(entityName, object, replicationMode);
    }
    /**
     * 保持Hibernate实体
     *
     * @param object
     *            Hibernate实体对象
     * @return Hibernate实体对象
     */
    public Serializable save(Object object) {
        return getSession().save(object);
    }
    /**
     * 保持Hibernate实体
     *
     * @param entityName
     *            实体名称
     * @param object
     *            Hibernate实体对象
     * @return Hibernate实体对象
     */
    public Serializable save(String entityName, Object object) {
        return getSession().save(entityName, object);
    }
    /**
     * 批量保持
     *
     * @param list
     *            批量保存Hibernate实体
     * @return 影响数量
     */
    public int batchSave(List list) {
        if (EmptyUtils.isEmpty(list)) {
            return 0;
        }
        return batchSave(list, 500);
    }
    /**
     * 批量保存 <b>作者：</b>zhoul <b>日期：</b>2017/6/20
     *
     * @param list
     *            批量提交集合
     * @param batchSize
     *            批量提交大小
     */
    public int batchSave(List list, final Integer batchSize) {
        int size = 0;
        Session session = getSession();
        for (int i = 0; i < list.size(); i++) {
            Object b = list.get(i);
            session.save(b);
            if (i % batchSize == 0) {
                session.flush();
                session.clear();
            }
            size++;
        }
        session.flush();
        session.clear();
        return size;
    }
    /**
     * 保存或更新
     *
     * @param object
     *            Hibernate实体对象
     */
    public void saveOrUpdate(Object object) {
        getSession().saveOrUpdate(object);
    }
    /**
     * 保存或更新
     *
     * @param entityName
     *            实体名称
     * @param object
     *            Hibernate实体对象
     */
    public void saveOrUpdate(String entityName, Object object) {
        getSession().saveOrUpdate(entityName, object);
    }
    /**
     * 批量保存或更新
     *
     * @param list
     *            Hibernate实体集合
     * @return 影响数量
     */
    public int batchSaveOrUpdate(List list) {
        return batchSaveOrUpdate(list, 100);
    }
    /**
     * 批量保存更新 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/20<br>
     *
     * @param list
     *            Hibernate实体集合
     * @param batchSize
     *            批量提交大小
     * @return 影响数量
     */
    public int batchSaveOrUpdate(List list, final Integer batchSize) {
        int size = 0;
        Session session = getSession();
        for (int i = 0; i < list.size(); i++) {
            Object b = list.get(i);
            session.saveOrUpdate(b);
            if (i % batchSize == 0) {
                session.flush();
                session.clear();
            }
            size++;
        }
        session.flush();
        session.clear();
        return size;
    }
    /**
     * 更新操作
     *
     * @param object
     *            Hibernate实体对象
     */
    public void update(Object object) {
        getSession().update(object);
    }
    /**
     * 更新操作
     *
     * @param entityName
     *            实体名称
     * @param object
     *            Hibernate实体对象
     */
    public void update(String entityName, Object object) {
        getSession().update(entityName, object);
    }
    /**
     * 批量更新
     *
     * @param list
     *            Hibernate实体集合
     * @return 影响数量
     */
    public int batchUpdate(List list) {
        return batchUpdate(list, 500);
    }
    /**
     * 批量更新 <br>
     * <b>作者：</b>zhoul <br>
     * <b>日期：</b>2017/6/20<br>
     *
     * @param list
     *            Hibernate实体集合
     * @param batchSize
     *            批量提交大小
     * @return 影响数量
     */
    public int batchUpdate(List list, final Integer batchSize) {
        int size = 0;
        Session session = getSession();
        for (int i = 0; i < list.size(); i++) {
            Object b = list.get(i);
            session.update(b);
            if (i % batchSize == 0) {
                session.flush();
                session.clear();
            }
            size++;
        }
        session.flush();
        session.clear();
        return size;
    }
    public Object merge(Object object) {
        return getSession().merge(object);
    }
    public Object merge(String entityName, Object object) {
        return getSession().merge(entityName, object);
    }
    public void persist(Object object) {
        getSession().persist(object);
    }
    public void persist(String entityName, Object object) {
        getSession().persist(entityName, object);
    }
    public void delete(Object object) {
        getSession().delete(object);
    }
    public void delete(String entityName, Object object) {
        getSession().delete(entityName, object);
    }
    public void flush() {
        getSession().flush();
    }
    public void refresh(Object object) {
        getSession().refresh(object);
    }
    public void refresh(String entityName, Object object) {
        getSession().refresh(entityName, object);
    }
    public void refresh(Object object, LockMode lockMode) {
        getSession().refresh(object, lockMode);
    }
    public void refresh(Object object, LockOptions lockOptions) {
        getSession().refresh(object, lockOptions);
    }
    public void refresh(String entityName, Object object, LockOptions lockOptions) {
        getSession().refresh(entityName, object, lockOptions);
    }
    public void clear() {
        getSession().clear();
    }
    public <T> T get(Class<T> entityType, Serializable id) {
        return getSession().get(entityType, id);
    }
    public <T> T get(Class<T> entityType, Serializable id, LockMode lockMode) {
        return getSession().get(entityType, id, lockMode);
    }
    public <T> T get(Class<T> entityType, Serializable id, LockOptions lockOptions) {
        return getSession().get(entityType, id, lockOptions);
    }
    public Object get(String entityName, Serializable id) {
        return getSession().get(entityName, id);
    }
    public Object get(String entityName, Serializable id, LockMode lockMode) {
        return getSession().get(entityName, id, lockMode);
    }
    public Object get(String entityName, Serializable id, LockOptions lockOptions) {
        return getSession().get(entityName, id, lockOptions);
    }
    public <T> T load(Class<T> theClass, Serializable id, LockMode lockMode) {
        return getSession().load(theClass, id, lockMode);
    }
    public <T> T load(Class<T> theClass, Serializable id, LockOptions lockOptions) {
        return getSession().load(theClass, id, lockOptions);
    }
    public Object load(String entityName, Serializable id, LockMode lockMode) {
        return getSession().load(entityName, id, lockMode);
    }
    public Object load(String entityName, Serializable id, LockOptions lockOptions) {
        return getSession().load(entityName, id, lockOptions);
    }
    public <T> T load(Class<T> theClass, Serializable id) {
        return getSession().load(theClass, id);
    }
    public Object load(String entityName, Serializable id) {
        return getSession().load(entityName, id);
    }
    public void load(Object object, Serializable id) {
        getSession().load(object, id);
    }
    /**
     * 拷贝集合，并且注入基础数据
     *
     * @param list
     *            被注入集合
     * @param clazz
     *            转换的Class
     * @param <B>
     *            转换Class 泛型
     * @return 注入完成后的集合
     */
    public <B> List<B> copyListAndInject(List list, final Class<B> clazz) {
        List<B> resultList = new ArrayList<>();
        if (!EmptyUtils.isEmpty(list)) {
            if (list.get(0) instanceof Map) {
                resultList.addAll(BeanUtils.copyMapListProperties(list, clazz));
            } else {
                resultList.addAll(BeanUtils.copyListProperties(list, clazz));
            }
        }
        return resultList;
    }
    /**
     * 根据查询HQL与参数列表创建Query对象.
     *
     * @param queryString
     *            HQL
     * @param values
     *            数量可变的参数,按顺序绑定.
     * @return Query
     */
    public Query createQuery(final String queryString, final Object... values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(String.valueOf(i), values[i]);
            }
        }
        return query;
    }
    /**
     * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
     *
     * @param queryString
     *            HQL
     * @param values
     *            命名参数,按名称绑定.
     * @return Query
     */
    public Query createQuery(final String queryString, final Map<String, ?> values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = createQuery(queryString);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }
    /**
     * 根据查询SQL与参数列表创建Query对象.
     *
     * @param queryString
     *            SQL
     * @param values
     *            数量可变的参数,按顺序绑定.
     * @return 结果集
     */
    public SQLQuery createSQLQuery(String queryString, final Object... values) {
        SQLQuery sqlQuery = createSQLQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                sqlQuery.setParameter(i, values[i]);
            }
        }
        return sqlQuery;
    }
    /**
     * 根据查询SQL与参数列表创建Query对象.
     *
     * @param queryString
     *            SQL
     * @param values
     *            命名参数,按名称绑定.
     * @return SQLQuery
     */
    public SQLQuery createSQLQuery(String queryString, final Map<String, ?> values) {
        SQLQuery sqlQuery = createSQLQuery(queryString);
        if (values != null) {
            sqlQuery.setProperties(values);
        }
        return sqlQuery;
    }
    /**
     * 根据SQL查询出object对象
     *
     * @param sql
     *            SQL
     * @param params
     *            Map条件参数
     * @return Map结果集
     */
    public Map getBySQL(String sql, final Map<String, ?> params) {
        return getBySQL(sql, params, Boolean.FALSE);
    }
    /**
     * 根据SQL查询出object对象，带缓存
     *
     * @param sql
     *            SQL
     * @param params
     *            Map条件参数
     * @param cache
     *            是否使用缓存
     * @return Map结果集
     */
    public Map getBySQL(String sql, final Map<String, ?> params, Boolean cache) {
        Query sqlQuery = createSQLQuery(sql, params);
        sqlQuery.setCacheable(cache);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Map) sqlQuery.uniqueResult();
    }
    /**
     * 根据SQL查询出对象，并转换为实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            SQL
     * @param params
     *            Map条件参数
     * @param clazz
     *            需要转换的Bean
     * @param <B>
     *            泛型
     * @return 转换后的Bean结果
     */
    public <B> B getBySQLAliasToBean(String sql, final Map<String, ?> params, final Class<B> clazz) {
        Map map = getBySQL(sql, params);
        B bean = null;
        if (map != null) {
            bean = BeanUtils.copyMapProperties(map, clazz);
        }
        return bean;
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            SQL
     * @param params
     *            参数
     * @return 结果集
     */
    public List listBySQL(String sql, final Map<String, ?> params) {
        return listBySQL(sql, params, null);
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            SQL
     * @param params
     *            参数
     * @param fetchSize
     *            每次读取笔数
     * @return 结果集
     */
    public List listBySQL(String sql, final Map<String, ?> params, Integer fetchSize) {
        Query sqlQuery = createSQLQuery(sql, params);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (fetchSize != null) {
            sqlQuery.setFetchSize(fetchSize);
        }
        List list = sqlQuery.list();
        return list;
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            SQL
     * @param first
     *            开始记录
     * @param maxResult
     *            最大记录
     * @param params
     *            参数
     * @param fetchSize
     *            每次读取笔数
     * @return 结果集
     */
    public List listBySQL(String sql, Integer first, Integer maxResult, final Map<String, ?> params,
            Integer fetchSize) {
        return listBySQL(sql, first, maxResult, params, Boolean.FALSE, fetchSize);
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            SQL
     * @param first
     *            开始记录
     * @param maxResult
     *            最大记录
     * @param params
     *            参数
     * @return 结果集
     */
    public List listBySQL(String sql, Integer first, Integer maxResult, final Map<String, ?> params) {
        return listBySQL(sql, first, maxResult, params, Boolean.FALSE);
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体、带缓存 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017年6月23日<br>
     *
     * @param sql
     *            SQL
     * @param first
     *            开始记录
     * @param maxResult
     *            最大记录
     * @param params
     *            参数
     * @param cache
     *            是否使用缓存
     * @return 结果集
     */
    public List listBySQL(String sql, Integer first, Integer maxResult, final Map<String, ?> params, Boolean cache) {
        return listBySQL(sql, first, maxResult, params, cache, null);
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体、带缓存 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017年6月23日<br>
     *
     * @param sql
     *            SQL
     * @param first
     *            开始记录
     * @param maxResult
     *            最大记录
     * @param params
     *            参数
     * @param cache
     *            是否使用缓存
     * @param fetchSize
     *            每次读取笔数
     * @return 结果集
     */
    public List listBySQL(String sql, Integer first, Integer maxResult, final Map<String, ?> params, Boolean cache,
            Integer fetchSize) {
        SQLQuery sqlQuery = createSQLQuery(sql, params);
        sqlQuery.setFirstResult(first);
        sqlQuery.setMaxResults(maxResult);
        sqlQuery.setCacheable(cache);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (fetchSize != null && fetchSize >= BigDecimal.ZERO.intValue()) {
            sqlQuery.setFetchSize(fetchSize);
        }
        return sqlQuery.list();
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            SQL
     * @param params
     *            参数
     * @param clazz
     *            转换的Class
     * @param fetchSize
     *            每次读取笔数
     * @return 结果集
     */
    public <B> List<B> listBySQLAliasToBean(String sql, final Map<String, ?> params, final Class<B> clazz,
            Integer fetchSize) {
        List list = listBySQL(sql, params, fetchSize);
        return copyListAndInject(list, clazz);
    }
    /**
     * 根据SQL查询出结果集，并转换对于实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            SQL
     * @param params
     *            参数
     * @param clazz
     *            转换的Class
     * @return 结果集
     */
    public <B> List<B> listBySQLAliasToBean(String sql, final Map<String, ?> params, final Class<B> clazz) {
        List list = listBySQL(sql, params);
        return copyListAndInject(list, clazz);
    }

    public <B> List<B> listBySQLAliasToBeanNew(String sql, final Map<String, ?> params, final Class<B> clazz) {
        return BeanUtil.copyToList(listBySQL(sql, params), clazz);
    }

    /**
     * 根据SQL和分页信息查询结果集并转换为指定对象 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            sql语句
     * @param first
     *            从第几条记录开始
     * @param maxResult
     *            查询最大记录数
     * @param params
     *            参数
     * @param clazz
     *            要转换的class
     * @return 及国际
     */
    public <B> List<B> listBySQLAliasToBean(String sql, Integer first, Integer maxResult, final Map<String, ?> params,
            final Class<B> clazz) {
        List list = listBySQL(sql, first, maxResult, params);
        return copyListAndInject(list, clazz);
    }
    /**
     * 根据SQL和分页信息查询结果集并转换为指定对象 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param sql
     *            sql语句
     * @param first
     *            从第几条记录开始
     * @param maxResult
     *            查询最大记录数
     * @param params
     *            参数
     * @param clazz
     *            要转换的class
     * @param fetchSize
     *            每次读取笔数
     * @return 及国际
     */
    public <B> List<B> listBySQLAliasToBean(String sql, Integer first, Integer maxResult, final Map<String, ?> params,
            final Class<B> clazz, Integer fetchSize) {
        List list = listBySQL(sql, first, maxResult, params, fetchSize);
        return copyListAndInject(list, clazz);
    }
    /**
     * 按HQL查询对象列表.
     *
     * @param hql
     *            查询HQL
     * @param values
     *            数量可变的参数,按顺序绑定.
     * @return 结果集合
     */
    public <X> List<X> listByHQL(final String hql, final Object... values) {
        return createQuery(hql, values).list();
    }
    /**
     * 根据HQL搜索 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            HQL
     * @param params
     *            参数
     * @return 结果集
     */
    public List listByHQL(String hql, final Map<String, ?> params) {
        return listByHQL(hql, params, Boolean.FALSE);
    }
    /**
     * 根据HQL搜索 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            HQL
     * @param params
     *            参数
     * @param cache
     *            是否使用缓存
     * @return 结果集
     */
    public List listByHQL(String hql, final Map<String, ?> params, final Boolean cache) {
        return listByHQLAliasToBean(hql, params, null, cache);
    }
    /**
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            HQL
     * @param params
     *            参数
     * @param clazz
     *            需要转换的对象
     * @return 结果集
     */
    public <B> List<B> listByHQLAliasToBean(String hql, final Map<String, ?> params, final Class<B> clazz) {
        return listByHQLAliasToBean(hql, params, clazz, Boolean.FALSE);
    }
    /**
     * <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            HQL
     * @param clazz
     *            需要转换的对象
     * @return 结果集
     */
    public <B> List<B> listByHQLAliasToBean(String hql, final Class<B> clazz) {
        return listByHQLAliasToBean(hql, null, clazz, Boolean.FALSE);
    }
    /**
     * 根据HQL查询出集合 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            hql查询语句
     * @param params
     *            查询参数
     * @param clazz
     *            需要转换的对象
     * @param cache
     *            是否使用缓存
     * @param <B>
     *            泛型
     * @return 查询后并转换的对象
     */
    public <B> List<B> listByHQLAliasToBean(final String hql, final Map<String, ?> params, final Class<B> clazz,
            final Boolean cache) {
        Query query = createQuery(hql, params);
        if (cache != null) {
            query.setCacheable(cache);
        }
        if (clazz != null) {
            if (hql.contains("select")) {
                query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            }
            List list = query.list();
            return copyListAndInject(list, clazz);
        } else {
            return query.list();
        }
    }
    /**
     * 根据HQL和分页信息查询出结果集 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            hql查询语句
     * @param clazz
     *            查询参数
     * @param cache
     *            是否使用缓存
     * @param <B>
     *            泛型
     * @return 查询后并转换的对象
     */
    public <B> List<B> listByHQLAliasToBean(final String hql, final Class<B> clazz, final Boolean cache) {
        return listByHQLAliasToBean(hql, null, clazz, cache);
    }
    /**
     * 根据HQL和分页信息查询出结果集 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            HQL
     * @param first
     *            开始记录
     * @param maxResult
     *            最大记录
     * @param params
     *            参数
     * @return 结果集
     */
    public List listByHQL(String hql, Integer first, Integer maxResult, final Map<String, ?> params) {
        return listByHQL(hql, first, maxResult, params, Boolean.FALSE);
    }
    /**
     * 根据HQL和分页信息查询出结果集 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            HQL
     * @param first
     *            开始记录
     * @param maxResult
     *            最大记录
     * @param params
     *            参数
     * @param cache
     *            是否使用缓存
     * @return 结果集
     */
    public List listByHQL(String hql, Integer first, Integer maxResult, final Map<String, ?> params,
            final Boolean cache) {
        Query query = createQuery(hql, params);
        query.setFirstResult(first);
        query.setMaxResults(maxResult);
        if (cache != null) {
            query.setCacheable(cache);
        }
        // if (hql.contains("select")) {
        // query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        // }
        return query.list();
    }
    /**
     * HQL查询的集合转换为指定的Bean对象 <br>
     *
     * @param hql
     *            hql查询语句
     * @param first
     *            从第几条开始查询
     * @param maxResult
     *            最大行数
     * @param params
     *            查询参数
     * @param clazz
     *            需要转换的对象
     * @param cache
     *            是否使用缓存
     * @param <B>
     *            泛型
     * @return 查询后并转换的对象
     */
    public <B> List<B> listByHQLAliasToBean(String hql, Integer first, Integer maxResult, final Map<String, ?> params,
            final Class<B> clazz, final Boolean cache) {
        Query query = createQuery(hql, params);
        query.setFirstResult(first);
        query.setMaxResults(maxResult);
        if (cache != null) {
            query.setCacheable(cache);
        }
        if (hql.contains("select")) {
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        List list = query.list();
        return copyListAndInject(list, clazz);
    }
    /**
     * HQL查询的集合转换为指定的Bean对象 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            hql查询语句
     * @param first
     *            从第几条开始查询
     * @param maxResult
     *            最大行数
     * @param params
     *            查询参数
     * @param clazz
     *            需要转换的对象
     * @return 查询后并转换的对象
     */
    public <B> List<B> listByHQLAliasToBean(String hql, Integer first, Integer maxResult, final Map<String, ?> params,
            final Class<B> clazz) {
        return listByHQLAliasToBean(hql, first, maxResult, params, clazz, null);
    }
    /**
     * 批量jdbc新增操作
     *
     * @param infoList
     *            新增对象
     * @return
     */
    public int batchJdbcSave(final List<?> infoList) {
        this.getSession().doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                if (infoList != null && infoList.size() > 0) {
                    Object obj = infoList.get(0);
                    EntityTableFieldInfo entityInfo = EntityTableFieldInfo.getEntityTableFieldInfo(obj.getClass());
                    String sql = entityInfo.getInsetSql();
                    if (StringUtils.isNotEmpty(sql)) {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        for (int i = 0; i < infoList.size(); i++) {
                            obj = infoList.get(i);
                            int index = 0;
                            for (Entry<String, String> entry : entityInfo.getTableField().entrySet()) {
                                Object value = entityInfo.getValueByEntity(obj, entry.getKey());
                                if (value instanceof Date) {
                                    Timestamp dateValue = new Timestamp(((Date) value).getTime());
                                    ps.setTimestamp(++index, dateValue);
                                } else {
                                    ps.setObject(++index, value);
                                }
                            }
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }
            }
        });
        return infoList.size();
    }
    public void batchJdbcSave(final List<?> data, final int batchSize) {
        if (EmptyUtils.isEmpty(data)) {
            return;
        }
        final long startT0 = System.currentTimeMillis();
        this.getSession().doWork(new Work() {
            @Override
            public void execute(Connection conn) throws SQLException {
                long startT1 = System.currentTimeMillis();
                logger.info("threadId :{} ,wait for starting exec : {} ms", Thread.currentThread().getId(),
                        (startT1 - startT0));
                specificBatchSave(conn, data.subList(0, 1), 1);
                if (data.size() == 1) {
                    return;
                }
                int lastBatchSize = (data.size() - 1) % batchSize;
                specificBatchSave(conn, data.subList(1, data.size() - lastBatchSize), batchSize);
                if (lastBatchSize > 0) {
                    specificBatchSave(conn, data.subList(data.size() - lastBatchSize, data.size()), lastBatchSize);
                }
                logger.info(" real executeBatch spends for direct sql : {} ms ",
                        (System.currentTimeMillis() - startT1));
            }
        });
    }
    protected int specificBatchSave(Connection conn, List<?> list, int batchSize) throws SQLException {
        return 0;
    }
    public void batchJdbcSave(final List<?> data, final List<String> saveAttrs, final int batchSize) {
        if (EmptyUtils.isEmpty(data)) {
            return;
        }
        // 目前是为了看效果，后面可以把这个放到缓存 start
        final Map<String, String> attrValueTypeMap = Maps.newHashMap();
        final Map<String, Method> attrGetMethodsMap = Maps.newHashMap();
        Class clazz = data.get(0).getClass();
        Table tableNameAnn = (Table) clazz.getAnnotation(Table.class);
        final String tableName = tableNameAnn.name();
        StringBuilder insertColsSB = new StringBuilder();
        StringBuilder placeholderOfOneValueSB = new StringBuilder();
        List<String> errorAttrs = Lists.newArrayList();
        for (String saveAttr : saveAttrs) {
            try {
                Method method = clazz.getMethod("get" + saveAttr.substring(0, 1).toUpperCase() + saveAttr.substring(1),
                        null);
                attrGetMethodsMap.put(saveAttr, method);
                attrValueTypeMap.put(saveAttr, method.getReturnType().getSimpleName());
                String colName = method.getAnnotation(Column.class).name();
                // attrColMap.put(saveAttr,EmptyUtils.isBlank(colName)?saveAttr:colName);
                insertColsSB.append(colName + ",");
                placeholderOfOneValueSB.append("?,");
            } catch (Exception e) {
                logger.error(" to save attribute :{} get method error: {}", saveAttr, e.getMessage(), e);
                errorAttrs.add(saveAttr);
                attrGetMethodsMap.remove(saveAttr);
                attrValueTypeMap.remove(saveAttr);
            }
        }
        if (EmptyUtils.isNotEmpty(errorAttrs)) {
            saveAttrs.removeAll(errorAttrs);
        }
        Method setIdMethod = null;
        try {
            Class idType = clazz.getMethod("getId", null).getReturnType();
            setIdMethod = clazz.getMethod("setId", new Class[] { idType });
        } catch (NoSuchMethodException e) {
            logger.warn(String.format("entity, %s ,no setId method。", clazz.getSimpleName()), e);
        }
        final Method setIdMethodFin = setIdMethod;
        final IdentifierGenerator idGen = getIdGen(data.get(0).getClass());
        final String insertCols = insertColsSB.substring(0, insertColsSB.length() - 1);
        final String placeholderOfOneValue = "("
                + placeholderOfOneValueSB.substring(0, placeholderOfOneValueSB.length() - 1) + ")";
        // 目前是为了看效果，后面可以把这个放到缓存 end
        final long startT0 = System.currentTimeMillis();
        this.getSession().doWork(new Work() {
            @Override
            public void execute(Connection conn) {
                try {
                    long startT1 = System.currentTimeMillis();
                    logger.info("threadId :{} ,wait for starting exec : {} ms", Thread.currentThread().getId(),
                            (startT1 - startT0));
                    specificBatchSave(idGen, setIdMethodFin, attrGetMethodsMap, attrValueTypeMap, tableName, insertCols,
                            placeholderOfOneValue, conn, data.subList(0, 1), saveAttrs, 1);
                    if (data.size() == 1) {
                        return;
                    }
                    int lastBatchSize = (data.size() - 1) % batchSize;
                    specificBatchSave(idGen, setIdMethodFin, attrGetMethodsMap, attrValueTypeMap, tableName, insertCols,
                            placeholderOfOneValue, conn, data.subList(1, data.size() - lastBatchSize), saveAttrs,
                            batchSize);
                    if (lastBatchSize > 0) {
                        specificBatchSave(idGen, setIdMethodFin, attrGetMethodsMap, attrValueTypeMap, tableName,
                                insertCols, placeholderOfOneValue, conn,
                                data.subList(data.size() - lastBatchSize, data.size()), saveAttrs, lastBatchSize);
                    }
                    logger.info(" real executeBatch spends : {} ms ", (System.currentTimeMillis() - startT1));
                } catch (SQLException ex) {
                    logger.error(
                            String.format("entity, %s , batch save error.", data.get(0).getClass().getSimpleName()),
                            ex);
                    throw new HibernateException(ex);
                }
            }
        });
    }
    private IdentifierGenerator getIdGen(Class<?> entityClazz) {
        try {
            GenericGenerator idGenAnn = entityClazz.getMethod("getId", null).getAnnotation(GenericGenerator.class);
            String idGenStrategy = idGenAnn.strategy();
            Class idGenStrategyClazz = entityClazz.getClassLoader().loadClass(idGenStrategy);
            IdentifierGenerator identifierGenerator = this.idGenNameIdGenMap.get(idGenStrategyClazz.getSimpleName());
            if (!EmptyUtils.isEmpty(identifierGenerator)) {
                return identifierGenerator;
            }
            if ("UUIDGenerator".equals(idGenStrategyClazz.getSimpleName())) {
                identifierGenerator = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator();
            } else {
                identifierGenerator = (IdentifierGenerator) idGenStrategyClazz.newInstance();
            }
            this.idGenNameIdGenMap.put(idGenStrategyClazz.getSimpleName(), identifierGenerator);
            return identifierGenerator;
        } catch (Exception e) {
            logger.error(String.format(
                    "entity : %s has no IdentifierGenerator or IdentifierGenerator is error.this entity will use UUIDGenerator",
                    entityClazz.getName()), e);
            IdentifierGenerator uuidGen = this.idGenNameIdGenMap.get("UUIDGenerator");
            if (EmptyUtils.isEmpty(uuidGen)) {
                uuidGen = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator();
                this.idGenNameIdGenMap.put("UUIDGenerator", uuidGen);
            }
            return uuidGen;
        }
    }
    private int specificBatchSave(IdentifierGenerator idGen, Method setIdMethod, Map<String, Method> attrGetMethodsMap,
            Map<String, String> attrValueTypeMap, String tableName, String insertCols, String placeholderOfOneValue,
            Connection conn, List data, List<String> saveAttrs, int batchSize) throws SQLException {
        if (EmptyUtils.isEmpty(data) || data.size() % batchSize > 0) {
            return 0;
        }
        long startT1 = System.currentTimeMillis();
        String oneValSql = "insert into " + tableName + "(" + insertCols + ")values" + placeholderOfOneValue;
        String batchSql = oneValSql;
        if (batchSize > 1) {
            StringBuilder leftValSql = new StringBuilder();
            for (int j = 1; j < batchSize; j++) {
                leftValSql.append("," + placeholderOfOneValue);
            }
            batchSql = oneValSql + leftValSql.toString();
        }
        PreparedStatement batchPS = conn.prepareStatement(batchSql);
        long startT2 = System.currentTimeMillis();
        logger.info(" real PreparedStatement spends : " + (startT2 - startT1) + " ,sql:" + batchSql);
        int startIndex = 1;
        int colNum = saveAttrs.size();
        long startTForABatch = System.currentTimeMillis();
        for (int i = 0; i < data.size(); i++) {
            setPreparedStatementValueForOneRecord(idGen, setIdMethod, startIndex, batchPS, data.get(i), saveAttrs,
                    attrGetMethodsMap, attrValueTypeMap);
            startIndex = startIndex + colNum;
            if ((i + 1) % batchSize == 0) {
                batchPS.addBatch();
                batchPS.executeBatch();
                startIndex = 1;
                logger.info(" real save spends {} ms for a batch,batchsize = {},i= {}",
                        System.currentTimeMillis() - startTForABatch, batchSize, i);
                startTForABatch = System.currentTimeMillis();
            }
        }
        logger.info(" real save spends {} ms for this batch,batchsize = {}", System.currentTimeMillis() - startT1,
                data.size());
        return data.size();
    }
    private void setPreparedStatementValueForOneRecord(IdentifierGenerator idGen, Method setIdMethod, int startIndex,
            PreparedStatement ps, Object entity, List<String> saveAttrs, Map<String, Method> attrgetMethodsMap,
            Map<String, String> attrValueTypeMap) throws SQLException {
        int index = startIndex;
        for (String saveAttr : saveAttrs) {
            Method method = attrgetMethodsMap.get(saveAttr);
            Object valObj = null;
            try {
                valObj = method.invoke(entity, null);
            } catch (Exception e) {
                logger.error("get entity value error:{}", e.getMessage(), e);
            }
            if (saveAttr.equalsIgnoreCase("id")) {
                if (EmptyUtils.isEmpty(valObj)) {
                    valObj = idGen.generate(null, entity);
                }
                if (!EmptyUtils.isEmpty(setIdMethod)) {
                    try {
                        setIdMethod.invoke(entity, valObj);
                    } catch (Exception e) {
                        logger.error(String.format("entity , %s , set id error.", entity.getClass().getSimpleName()),
                                e);
                    }
                }
            }
            if (valObj != null && "Date".equals(attrValueTypeMap.get(saveAttr))) {
                ps.setTimestamp(index, new Timestamp(((Date) valObj).getTime()));
            } else {
                ps.setObject(index, valObj);
            }
            index++;
        }
    }
}
