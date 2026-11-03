package com.bytter.core.common.hibernate.internal;
import org.hibernate.*;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryReturn;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
/**
 * 解决多数据库统计原生SQL查询报错问题 Created by zhoul on 2018/1/18.
 */
public class SQLQueryImplAdapter implements SQLQuery {
    private SQLQuery sqlQuery;
    public SQLQueryImplAdapter(SQLQuery sqlQuery) {
        this.sqlQuery = sqlQuery;
    }
    @Override
    public Collection<String> getSynchronizedQuerySpaces() {
        return sqlQuery.getSynchronizedQuerySpaces();
    }
    @Override
    public SQLQuery addSynchronizedQuerySpace(String querySpace) {
        return sqlQuery.addSynchronizedQuerySpace(querySpace);
    }
    @Override
    public SQLQuery addSynchronizedEntityName(String entityName) throws MappingException {
        return sqlQuery.addSynchronizedEntityName(entityName);
    }
    @Override
    public SQLQuery addSynchronizedEntityClass(Class entityClass) throws MappingException {
        return sqlQuery.addSynchronizedEntityClass(entityClass);
    }
    @Override
    public SQLQuery setResultSetMapping(String name) {
        return sqlQuery.setResultSetMapping(name);
    }
    @Override
    public boolean isCallable() {
        return sqlQuery.isCallable();
    }
    @Override
    public List<NativeSQLQueryReturn> getQueryReturns() {
        return sqlQuery.getQueryReturns();
    }
    @Override
    public SQLQuery addScalar(String columnAlias) {
        return sqlQuery.addScalar(columnAlias);
    }
    @Override
    public SQLQuery addScalar(String columnAlias, Type type) {
        return sqlQuery.addScalar(columnAlias, type);
    }
    @Override
    public RootReturn addRoot(String tableAlias, String entityName) {
        return sqlQuery.addRoot(tableAlias, entityName);
    }
    @Override
    public RootReturn addRoot(String tableAlias, Class entityType) {
        return sqlQuery.addRoot(tableAlias, entityType);
    }
    @Override
    public SQLQuery addEntity(String entityName) {
        return sqlQuery.addEntity(entityName);
    }
    @Override
    public SQLQuery addEntity(String tableAlias, String entityName) {
        return sqlQuery.addEntity(tableAlias, entityName);
    }
    @Override
    public SQLQuery addEntity(String tableAlias, String entityName, LockMode lockMode) {
        return sqlQuery.addEntity(tableAlias, entityName, lockMode);
    }
    @Override
    public SQLQuery addEntity(Class entityType) {
        return sqlQuery.addEntity(entityType);
    }
    @Override
    public SQLQuery addEntity(String tableAlias, Class entityType) {
        return sqlQuery.addEntity(tableAlias, entityType);
    }
    @Override
    public SQLQuery addEntity(String tableAlias, Class entityName, LockMode lockMode) {
        return sqlQuery.addEntity(tableAlias, entityName, lockMode);
    }
    @Override
    public FetchReturn addFetch(String tableAlias, String ownerTableAlias, String joinPropertyName) {
        return sqlQuery.addFetch(tableAlias, ownerTableAlias, joinPropertyName);
    }
    @Override
    public SQLQuery addJoin(String tableAlias, String path) {
        return sqlQuery.addJoin(tableAlias, path);
    }
    @Override
    public SQLQuery addJoin(String tableAlias, String ownerTableAlias, String joinPropertyName) {
        return sqlQuery.addJoin(tableAlias, ownerTableAlias, joinPropertyName);
    }
    @Override
    public SQLQuery addJoin(String tableAlias, String path, LockMode lockMode) {
        return sqlQuery.addJoin(tableAlias, path, lockMode);
    }
    @Override
    public String getQueryString() {
        return sqlQuery.getQueryString();
    }
    @Override
    public Integer getMaxResults() {
        return sqlQuery.getMaxResults();
    }
    @Override
    public Query setMaxResults(int maxResults) {
        return sqlQuery.setMaxResults(maxResults);
    }
    @Override
    public Integer getFirstResult() {
        return sqlQuery.getFirstResult();
    }
    @Override
    public Query setFirstResult(int firstResult) {
        return sqlQuery.setFirstResult(firstResult);
    }
    @Override
    public FlushMode getFlushMode() {
        return sqlQuery.getFlushMode();
    }
    @Override
    public Query setFlushMode(FlushMode flushMode) {
        return sqlQuery.setFlushMode(flushMode);
    }
    @Override
    public CacheMode getCacheMode() {
        return sqlQuery.getCacheMode();
    }
    @Override
    public Query setCacheMode(CacheMode cacheMode) {
        return sqlQuery.setCacheMode(cacheMode);
    }
    @Override
    public boolean isCacheable() {
        return sqlQuery.isCacheable();
    }
    @Override
    public Query setCacheable(boolean cacheable) {
        return sqlQuery.setCacheable(cacheable);
    }
    @Override
    public String getCacheRegion() {
        return sqlQuery.getCacheRegion();
    }
    @Override
    public Query setCacheRegion(String cacheRegion) {
        return sqlQuery.setCacheRegion(cacheRegion);
    }
    @Override
    public Integer getTimeout() {
        return sqlQuery.getTimeout();
    }
    @Override
    public Query setTimeout(int timeout) {
        return sqlQuery.setTimeout(timeout);
    }
    @Override
    public Integer getFetchSize() {
        return sqlQuery.getFetchSize();
    }
    @Override
    public Query setFetchSize(int fetchSize) {
        return sqlQuery.setFetchSize(fetchSize);
    }
    @Override
    public boolean isReadOnly() {
        return sqlQuery.isReadOnly();
    }
    @Override
    public Query setReadOnly(boolean readOnly) {
        return sqlQuery.setReadOnly(readOnly);
    }
    @Override
    public Type[] getReturnTypes() {
        return sqlQuery.getReturnTypes();
    }
    @Override
    public LockOptions getLockOptions() {
        return sqlQuery.getLockOptions();
    }
    @Override
    public Query setLockOptions(LockOptions lockOptions) {
        return sqlQuery.setLockOptions(lockOptions);
    }
    @Override
    public Query setLockMode(String alias, LockMode lockMode) {
        return sqlQuery.setLockMode(alias, lockMode);
    }
    @Override
    public String getComment() {
        return sqlQuery.getComment();
    }
    @Override
    public Query setComment(String comment) {
        return sqlQuery.setComment(comment);
    }
    @Override
    public Query addQueryHint(String hint) {
        return sqlQuery.addQueryHint(hint);
    }
    @Override
    public String[] getReturnAliases() {
        return sqlQuery.getReturnAliases();
    }
    @Override
    public String[] getNamedParameters() {
        return sqlQuery.getNamedParameters();
    }
    @Override
    public Iterator iterate() {
        return sqlQuery.iterate();
    }
    @Override
    public ScrollableResults scroll() {
        return sqlQuery.scroll();
    }
    @Override
    public ScrollableResults scroll(ScrollMode scrollMode) {
        return sqlQuery.scroll(scrollMode);
    }
    @Override
    public List list() {
        return sqlQuery.list();
    }
    @Override
    public Object uniqueResult() {
        Object object = sqlQuery.uniqueResult();
        if (object instanceof BigInteger) {
            BigInteger bigInteger = (BigInteger) object;
            return new BigDecimal(bigInteger.intValue());
        } else if (object instanceof Integer) {
            Integer integer = (Integer) object;
            return new BigDecimal(integer);
        } else if (object instanceof Double) {
            Double doubleValue = (Double) object;
            String doubleStr = String.valueOf(doubleValue);
            return new BigDecimal(doubleStr);
        }
        return object;
    }
    @Override
    public int executeUpdate() {
        return sqlQuery.executeUpdate();
    }
    @Override
    public Query setParameter(int position, Object val, Type type) {
        return sqlQuery.setParameter(position, val, type);
    }
    @Override
    public Query setParameter(String name, Object val, Type type) {
        return sqlQuery.setParameter(name, val, type);
    }
    @Override
    public Query setParameter(int position, Object val) {
        return sqlQuery.setParameter(position, val);
    }
    @Override
    public Query setParameter(String name, Object val) {
        return sqlQuery.setParameter(name, val);
    }
    @Override
    public Query setParameters(Object[] values, Type[] types) {
        return sqlQuery.setParameters(values, types);
    }
    @Override
    public Query setParameterList(String name, Collection values, Type type) {
        return sqlQuery.setParameterList(name, values, type);
    }
    @Override
    public Query setParameterList(String name, Collection values) {
        return sqlQuery.setParameterList(name, values);
    }
    @Override
    public Query setParameterList(String name, Object[] values, Type type) {
        return sqlQuery.setParameterList(name, values, type);
    }
    @Override
    public Query setParameterList(String name, Object[] values) {
        return sqlQuery.setParameterList(name, values);
    }
    @Override
    public Query setProperties(Object bean) {
        return sqlQuery.setProperties(bean);
    }
    @Override
    public Query setProperties(Map bean) {
        return sqlQuery.setProperties(bean);
    }
    @Override
    public Query setString(int position, String val) {
        return sqlQuery.setString(position, val);
    }
    @Override
    public Query setCharacter(int position, char val) {
        return sqlQuery.setCharacter(position, val);
    }
    @Override
    public Query setBoolean(int position, boolean val) {
        return sqlQuery.setBoolean(position, val);
    }
    @Override
    public Query setByte(int position, byte val) {
        return sqlQuery.setByte(position, val);
    }
    @Override
    public Query setShort(int position, short val) {
        return sqlQuery.setShort(position, val);
    }
    @Override
    public Query setInteger(int position, int val) {
        return sqlQuery.setInteger(position, val);
    }
    @Override
    public Query setLong(int position, long val) {
        return sqlQuery.setLong(position, val);
    }
    @Override
    public Query setFloat(int position, float val) {
        return sqlQuery.setFloat(position, val);
    }
    @Override
    public Query setDouble(int position, double val) {
        return sqlQuery.setDouble(position, val);
    }
    @Override
    public Query setBinary(int position, byte[] val) {
        return sqlQuery.setBinary(position, val);
    }
    @Override
    public Query setText(int position, String val) {
        return sqlQuery.setText(position, val);
    }
    @Override
    public Query setSerializable(int position, Serializable val) {
        return sqlQuery.setSerializable(position, val);
    }
    @Override
    public Query setLocale(int position, Locale locale) {
        return sqlQuery.setLocale(position, locale);
    }
    @Override
    public Query setBigDecimal(int position, BigDecimal number) {
        return sqlQuery.setBigDecimal(position, number);
    }
    @Override
    public Query setBigInteger(int position, BigInteger number) {
        return sqlQuery.setBigInteger(position, number);
    }
    @Override
    public Query setDate(int position, Date date) {
        return sqlQuery.setDate(position, date);
    }
    @Override
    public Query setTime(int position, Date date) {
        return sqlQuery.setTime(position, date);
    }
    @Override
    public Query setTimestamp(int position, Date date) {
        return sqlQuery.setTimestamp(position, date);
    }
    @Override
    public Query setCalendar(int position, Calendar calendar) {
        return sqlQuery.setCalendar(position, calendar);
    }
    @Override
    public Query setCalendarDate(int position, Calendar calendar) {
        return sqlQuery.setCalendarDate(position, calendar);
    }
    @Override
    public Query setString(String name, String val) {
        return sqlQuery.setString(name, val);
    }
    @Override
    public Query setCharacter(String name, char val) {
        return sqlQuery.setCharacter(name, val);
    }
    @Override
    public Query setBoolean(String name, boolean val) {
        return sqlQuery.setBoolean(name, val);
    }
    @Override
    public Query setByte(String name, byte val) {
        return sqlQuery.setByte(name, val);
    }
    @Override
    public Query setShort(String name, short val) {
        return sqlQuery.setShort(name, val);
    }
    @Override
    public Query setInteger(String name, int val) {
        return sqlQuery.setInteger(name, val);
    }
    @Override
    public Query setLong(String name, long val) {
        return sqlQuery.setLong(name, val);
    }
    @Override
    public Query setFloat(String name, float val) {
        return sqlQuery.setFloat(name, val);
    }
    @Override
    public Query setDouble(String name, double val) {
        return sqlQuery.setDouble(name, val);
    }
    @Override
    public Query setBinary(String name, byte[] val) {
        return sqlQuery.setBinary(name, val);
    }
    @Override
    public Query setText(String name, String val) {
        return sqlQuery.setText(name, val);
    }
    @Override
    public Query setSerializable(String name, Serializable val) {
        return sqlQuery.setSerializable(name, val);
    }
    @Override
    public Query setLocale(String name, Locale locale) {
        return sqlQuery.setLocale(name, locale);
    }
    @Override
    public Query setBigDecimal(String name, BigDecimal number) {
        return sqlQuery.setBigDecimal(name, number);
    }
    @Override
    public Query setBigInteger(String name, BigInteger number) {
        return sqlQuery.setBigInteger(name, number);
    }
    @Override
    public Query setDate(String name, Date date) {
        return sqlQuery.setDate(name, date);
    }
    @Override
    public Query setTime(String name, Date date) {
        return sqlQuery.setTime(name, date);
    }
    @Override
    public Query setTimestamp(String name, Date date) {
        return sqlQuery.setTimestamp(name, date);
    }
    @Override
    public Query setCalendar(String name, Calendar calendar) {
        return sqlQuery.setCalendar(name, calendar);
    }
    @Override
    public Query setCalendarDate(String name, Calendar calendar) {
        return sqlQuery.setCalendarDate(name, calendar);
    }
    @Override
    public Query setEntity(int position, Object val) {
        return sqlQuery.setEntity(position, val);
    }
    @Override
    public Query setEntity(String name, Object val) {
        return sqlQuery.setEntity(name, val);
    }
    @Override
    public Query setResultTransformer(ResultTransformer transformer) {
        return sqlQuery.setResultTransformer(transformer);
    }
}
