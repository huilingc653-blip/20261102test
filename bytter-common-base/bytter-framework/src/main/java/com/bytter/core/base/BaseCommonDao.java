package com.bytter.core.base;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.persistence.Page;
import com.bytter.core.common.persistence.PropertyFilter;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.*;
import com.google.common.base.Splitter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
/**
 * 封装扩展功能的Hibernat DAO泛型基类.
 * <p>
 * 扩展功能包括分页查询,按属性过滤条件列表查询. 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 *
 * @param <T>
 *            DAO操作的对象类型
 */
public abstract class BaseCommonDao<T, PK extends Serializable> extends SimpleHibernateDao<T, PK> {
    /**
     * 用于Dao层子类的构造函数. 通过子类的泛型定义取得对象类型Class. eg. public class UserDao extends HibernateDao&lt;User, Long&gt;{ }
     */
    public BaseCommonDao() {
        super();
    }
    /**
     * 分页获取全部对象.
     *
     * @deprecated 不推荐使用
     */
    @Deprecated
    public Page<T> getAll(final Page<T> page) {
        return findPage(page);
    }
    /**
     * 按HQL分页查询.
     *
     * @param page
     *            分页参数. 注意不支持其中的orderBy参数.
     * @param hql
     *            hql语句.
     * @param values
     *            数量可变的查询参数,按顺序绑定.
     * @return 分页查询结果, 附带结果列表及所有查询输入参数.
     */
    @Deprecated
    public Page<T> findPage(final Page<T> page, final String hql, final Object... values) {
        Assert.notNull(page, "page不能为空");
        Query q = createQuery(hql, values);
        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }
        setPageParameterToQuery(q, page);
        List result = q.list();
        page.setResult(result);
        return page;
    }
    /**
     * 按HQL分页查询.
     *
     * @param page
     *            分页参数. 注意不支持其中的orderBy参数.
     * @param hql
     *            hql语句.
     * @param values
     *            命名参数,按名称绑定.
     * @return 分页查询结果, 附带结果列表及所有查询输入参数.
     */
    @Deprecated
    public Page<T> findPage(final Page<T> page, final String hql, final Map<String, ?> values) {
        Assert.notNull(page, "page不能为空");
        Query q = createQuery(hql, values);
        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }
        setPageParameterToQuery(q, page);
        List result = q.list();
        page.setResult(result);
        return page;
    }
    @Deprecated
    public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
        return findPage(page, Boolean.FALSE, criterions);
    }
    @Deprecated
    public Page<T> findPage(final Page<T> page, Boolean cache, final Criterion... criterions) {
        return findPage(page, cache, null, criterions);
    }
    /**
     * 按Criteria分页查询.
     *
     * @param page
     *            分页参数.
     * @param criterionList
     *            数量可变的Criterion.
     * @return 分页查询结果.附带结果列表及所有查询输入参数.
     */
    @Deprecated
    public Page<T> findPage(final Page<T> page, Boolean cache, String region, final Criterion... criterionList) {
        Assert.notNull(page, "page不能为空");
        Criteria c = createCriteria(criterionList);
        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c, cache, region);
            page.setTotalCount(totalCount);
        }
        setPageParameterToCriteria(c, page);
        if (StringUtils.isNotBlank(region)) {
            c.setCacheRegion(region);
        }
        List result = c.list();
        page.setResult(result);
        return page;
    }
    @Deprecated
    public Page<T> findPage(ProjectionList pList, final Page<T> page, final Criterion... criterionList) {
        Assert.notNull(page, "page不能为空");
        Criteria c = createCriteria(pList, criterionList);
        if (page.isAutoCount()) {
            long totalCount = countCriteriaResult(c);
            page.setTotalCount(totalCount);
        }
        setPageParameterToCriteria(c, page);
        List result = c.list();
        page.setResult(result);
        return page;
    }
    /**
     * 设置分页参数到Query对象,辅助函数.
     *
     * @param q
     *            Query
     * @param page
     *            分页
     * @return Query
     */
    protected Query setPageParameterToQuery(final Query q, final Page<T> page) {
        Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");
        // hibernate的firstResult的序号从0开始
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());
        return q;
    }
    /**
     * 设置分页参数到Criteria对象,辅助函数.
     *
     * @param c
     *            Criteria
     * @param page
     *            分页
     * @return Query
     */
    protected Criteria setPageParameterToCriteria(final Criteria c, final Page<T> page) {
        Assert.isTrue(page.getPageSize() >= 0, "Page Size must larger than zero");
        // hibernate的firstResult的序号从0开始
        c.setFirstResult(page.getPageNo());
        c.setMaxResults(page.getPageSize());
        if (page.isOrderBySetted()) {
            String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
            String[] orderArray = StringUtils.split(page.getOrder(), ',');
            Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
            for (int i = 0; i < orderByArray.length; i++) {
                if (Page.ASC.equals(orderArray[i])) {
                    c.addOrder(Order.asc(orderByArray[i]));
                } else {
                    c.addOrder(Order.desc(orderByArray[i]));
                }
            }
        }
        return c;
    }
    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数. <br>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(final String hql, final Object... values) {
        String countHql = prepareCountHQL(hql);
        try {
            Long count = getUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
        }
    }
    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数. <br>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected long countHqlResult(final String hql, final Map<String, ?> values) {
        String countHql = prepareCountHQL(hql);
        try {
            Long count = getUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
        }
    }
    /**
     * 根据查询语句获取统计语句
     *
     * @param orgHql
     *            统计sql
     * @return sql
     */
    private String prepareCountHQL(String orgHql) {
        String fromHql = orgHql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBeforeLast(fromHql, "order by");
        String countHql = "select count(*) " + fromHql;
        return countHql;
    }
    protected String prepareCountSQL(String orgHql) {
        StringBuilder sql = new StringBuilder();
        orgHql = StringUtils.substringBeforeLast(orgHql, "order by");
        sql.append("select count(*) from( ").append(orgHql).append(" ) total");
        return sql.toString();
    }
    protected long countCriteriaResult(final Criteria c) {
        return countCriteriaResult(c, Boolean.FALSE);
    }
    protected long countCriteriaResult(final Criteria c, final Boolean cache) {
        return countCriteriaResult(c, Boolean.FALSE, null);
    }
    /**
     * 执行count查询获得本次Criteria查询所能获得的对象总数.
     */
    protected long countCriteriaResult(final Criteria c, Boolean cache, String region) {
        CriteriaImpl impl = (CriteriaImpl) c;
        // 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
        Projection projection = impl.getProjection();
        ResultTransformer transformer = impl.getResultTransformer();
        List<CriteriaImpl.OrderEntry> orderEntries = null;
        try {
            orderEntries = (List) Reflections.getFieldValue(impl, "orderEntries");
            Reflections.setFieldValue(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e);
            ExceptionStackUtils.getStack(e);
        }
        c.setCacheable(cache);
        if (StringUtils.isNotBlank(region)) {
            c.setCacheRegion(region);
        }
        // 执行Count查询
        Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
        long totalCount = (totalCountObject != null) ? totalCountObject : 0;
        // 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
        c.setProjection(projection);
        if (projection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (transformer != null) {
            c.setResultTransformer(transformer);
        }
        try {
            Reflections.setFieldValue(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            logger.error("不可能抛出的异常:{}", e);
        }
        return totalCount;
    }
    /**
     * 按属性查找对象列表,支持多种匹配方式.
     *
     * @param matchType
     *            匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
     */
    public List<T> listBy(final String propertyName, final Object value, final MatchTypeEnum matchType) {
        Criterion[] criterion = buildCriterion(propertyName, value, matchType);
        return list(criterion);
    }
    /**
     * 按属性过滤条件列表查找对象列表.
     */
    public List<T> list(List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return list(criterions);
    }
    /**
     * 按属性过滤条件列表分页查找对象.
     */
    public Page<T> findPage(final Page<T> page, final List<PropertyFilter> filters) {
        Criterion[] criterions = buildCriterionByPropertyFilter(filters);
        return findPage(page, criterions);
    }
    public DataTablesOutput<T> findPage(DataTablesInput input) {
        return findPage(input, Boolean.FALSE);
    }
    public DataTablesOutput<T> findPage(DataTablesInput input, Boolean cache) {
        return findPage(input, cache, null);
    }
    public DataTablesOutput<T> findPage(DataTablesInput input, Boolean cache, String region) {
        String orderby = DataTableHelper.orderby(input);
        String order = DataTableHelper.order(input);
        // 查询条件
        // ProjectionList pList = DataTableHelper.select(input);
        // 表单搜索
        List<PropertyFilter> propertyFilters = DataTableHelper.search(input);
        Criterion[] searchFilters = buildCriterionByPropertyFilter(propertyFilters);
        // 列搜索
        Criterion[] columnFilters = DataTableHelper.filter(input);
        // 合并
        Criterion[] criterions = Arrays.copyOf(searchFilters, searchFilters.length + columnFilters.length);
        System.arraycopy(columnFilters, 0, criterions, searchFilters.length, columnFilters.length);
        Page<T> page = new Page<T>(input.getStart(), input.getLength(), orderby, order);
        page.setAutoCount(Boolean.TRUE);
        page = findPage(page, cache, region, criterions);
        return new DataTablesOutput<T>(input.getDraw(), page.getResult(), page.getTotalCount(), page.getTotalCount());
    }
    /**
     * 按属性条件参数创建Criterion,辅助函数. <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/20<br>
     */
    protected Criterion[] buildCriterion(final String propertyName, final Object propertyValue,
            final MatchTypeEnum matchType) {
        Assert.hasText(propertyName, "propertyName不能为空");
        // Criterion criterion = null;
        List<Criterion> criterion = new ArrayList<Criterion>();
        // 根据MatchType构造criterion
        switch (matchType) {
        case EQ:
            criterion.add(Restrictions.eq(propertyName, propertyValue));
            break;
        case NE:
            criterion.add(Restrictions.ne(propertyName, propertyValue));
            break;
        case LLIKE:
            criterion.add(Restrictions.like(propertyName, (String) propertyValue, MatchMode.END));
            break;
        case RLIKE:
            criterion.add(Restrictions.like(propertyName, (String) propertyValue, MatchMode.START));
            break;
        case LIKE:
            // zhoujian 2018年6月14日15:52:08 B20180600221新增支持中间带空格模糊搜索
            String val = (String) propertyValue;
            if (StringUtils.isNotBlank(val)) {
                val = val.trim().replaceAll("\\s+", "%");
            }
            criterion.add(Restrictions.like(propertyName, val, MatchMode.ANYWHERE));
            break;
        case LE:
            criterion.add(Restrictions.le(propertyName, propertyValue));
            break;
        case LT:
            criterion.add(Restrictions.lt(propertyName, propertyValue));
            break;
        case GE:
            criterion.add(Restrictions.ge(propertyName, propertyValue));
            break;
        case GT:
            criterion.add(Restrictions.gt(propertyName, propertyValue));
            break;
        case IN:
            if (propertyValue instanceof String) {
                addCriterionIn(propertyName, Arrays.asList(((String) propertyValue).split(",")), criterion);
            } else {
                criterion.add(Restrictions.in(propertyName, new Object[] { propertyValue }));
            }
            break;
        case IS_NULL:
            criterion.add(Restrictions.isNull(propertyName));
            break;
        default:
        }
        return criterion.toArray(new Criterion[criterion.size()]);
    }
    /**
     * in条件拼组 <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/05/<br>
     *
     * @param propertyName
     *            属性名称
     * @param list
     *            列表
     * @param criterion
     *            Criterion
     */
    private void addCriterionIn(String propertyName, List<?> list, List<Criterion> criterion) {
        Disjunction or = Restrictions.disjunction();
        if (list.size() > 1000) {
            while (list.size() > 1000) {
                List<?> subList = list.subList(0, 1000);
                or.add(Restrictions.in(propertyName, subList));
                list.subList(0, 1000).clear();
            }
        }
        or.add(Restrictions.in(propertyName, list));
        criterion.add(or);
    }
    /**
     * 按属性条件列表创建Criterion数组,辅助函数.
     *
     * @param filters
     *            过滤组装
     * @return riterion数组
     */
    protected Criterion[] buildCriterionByPropertyFilter(final List<PropertyFilter> filters) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        if (!filters.isEmpty()) {
            for (PropertyFilter filter : filters) {
                if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
                    // 处理兼容IN查询
                    Criterion[] criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(),
                            filter.getMatchType());
                    if (criterion.length > 1) {
                        Disjunction disjunction = Restrictions.disjunction();
                        for (Criterion c : criterion) {
                            disjunction.add(c);
                        }
                        criterionList.add(disjunction);
                    } else {
                        criterionList.add(criterion[0]);
                    }
                } else {// 包含多个属性需要比较的情况,进行or处理.
                    Disjunction disjunction = Restrictions.disjunction();
                    for (String param : filter.getPropertyNames()) {
                        // 处理兼容IN查询
                        Criterion[] criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
                        for (Criterion c : criterion) {
                            disjunction.add(c);
                        }
                        // disjunction.add(criterion);
                    }
                    criterionList.add(disjunction);
                }
            }
        }
        return criterionList.toArray(new Criterion[criterionList.size()]);
    }
    /**
     * 使用sql语句进行分页查询 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/20<br>
     *
     * @param sql
     *            sql语句
     * @param input
     *            DataTablesInput
     * @param params
     *            命名参数,按名称绑定.
     * @deprecated Use {@link BaseDao}
     */
    @Deprecated
    public DataTablesOutput getDataTablesOutputBySQL(DataTablesInput input, String sql, final Map<String, ?> params) {
        String countSQL = prepareCountSQL(sql);
        BigDecimal count = new BigDecimal(createSQLQuery(countSQL, params).uniqueResult().toString());
        List list;
        // 若统计出来的信息大于0，则发送查询语句，否则返回空的ArrayList
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            list = listBySQL(sql, input.getStart(), input.getLength(), params);
        } else {
            list = new ArrayList(0);
        }
        return new DataTablesOutput<T>(input.getDraw(), list, count.intValue(), count.intValue());
    }
    /**
     * 使用sql语句进行分页查询，并转成对应实体 注意：查询hql中的语句必须有别名 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/20<br>
     *
     * @param input
     * @param sql
     * @param params
     * @return
     * @deprecated Use {@link BaseDao}
     */
    @Deprecated
    public DataTablesOutput getDataTablesOutputBySQLAliasToBean(DataTablesInput input, String sql,
            final Map<String, ?> params, final Class<?> clazz) {
        DataTablesOutput dataTablesOutput = getDataTablesOutputBySQL(input, sql, params);
        List list = dataTablesOutput.getData();
        if (list.isEmpty()) {
            return dataTablesOutput;
        }
        list = copyListAndInject(list, clazz);
        dataTablesOutput.setData(list);
        return dataTablesOutput;
    }
    /**
     * 根据HQL查询出实体对象
     *
     * @param hql
     *            HQL
     * @param params
     *            Map参数
     * @return Map结果集
     */
    public Map getByHQL(String hql, final Map<String, ?> params) {
        return getByHQL(hql, params, Boolean.FALSE);
    }
    /**
     * 根据HQL查询出实体对象，缓存 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param hql
     *            HQL
     * @param params
     *            Map参数
     * @param cache
     *            是否使用缓存
     * @return Map结果集
     */
    public Map getByHQL(String hql, final Map<String, ?> params, Boolean cache) {
        Query query = createQuery(hql, params);
        query.setCacheable(cache);
        if (hql.contains("select")) {
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        return (Map) query.uniqueResult();
    }
    /**
     * 根据HQL查询出实体对象，转换为实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/20<br>
     *
     * @param hql
     *            HQL
     * @param params
     *            Map参数
     * @param clazz
     *            转换为Bean
     * @param <B>
     *            bean
     * @return 转换后的Bean
     */
    public <B> B getByHQLAliasToBean(String hql, final Map<String, ?> params, final Class<B> clazz) {
        return getByHQLAliasToBean(hql, params, clazz, Boolean.FALSE);
    }
    /**
     * 根据HQL查询出实体对象，转换为实体 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/20<br>
     *
     * @param hql
     *            HQL
     * @param params
     *            Map参数
     * @param clazz
     *            转换为Bean
     * @param cache
     *            是否使用缓存
     * @param <B>
     *            bean
     * @return 转换后的Bean
     */
    public <B> B getByHQLAliasToBean(String hql, final Map<String, ?> params, final Class<B> clazz,
            final Boolean cache) {
        Map map = getByHQL(hql, params, cache);
        B bean = null;
        if (map != null) {
            bean = BeanUtils.copyMapProperties(map, clazz);
        }
        return bean;
    }
    /**
     * 使用hql语句进行分页查询 <br>
     * <b>作者：</b>zhoul<br>
     * <b>日期：</b>2017/6/10<br>
     * <p>
     * <br>
     * 此方法已废弃，建议使用getPageResultByHQL方法
     *
     * @param hql
     *            hql语句
     * @param input
     *            DataTablesInput
     * @param params
     *            命名参数,按名称绑定.
     * @return 封装好后的DataTablesOutput对象
     */
    // @Deprecated
    public DataTablesOutput getDataTablesOutputByHQL(DataTablesInput input, String hql, final Map<String, ?> params) {
        Long count = countHqlResult(hql, params);
        List list;
        // 若统计出来的信息大于0，则发送查询语句，否则返回空的ArrayList
        if (count > 0) {
            list = listByHQL(hql, input.getStart(), input.getLength(), params);
        } else {
            list = new ArrayList(0);
        }
        return new DataTablesOutput(input.getDraw(), list, count.intValue(), count.intValue());
    }
    /**
     * sql查询 自动拼接条件和排序 查询 <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input
     *            DataTablesInput
     * @param sql
     *            SQL语句 (select ... from) or (select ... from where)
     * @param voClazz
     *            vo实体类 bean.class
     * @param alias
     *            别名
     * @param hasWhere
     *            是否加where关键字
     * @param needParams
     *            所需要拼接的值 null根据vo查询所有 有值根据list集合里拼接
     * @return String
     */
    public DataTablesOutput getDynamicBySQL(DataTablesInput input, String sql, Class<?> voClazz, String alias,
            final Boolean hasWhere, Object... needParams) {
        return dealDynamicBySQL(input, sql, voClazz, alias, hasWhere, needParams);
    }
    /**
     * sql查询 自动拼接条件和排序 查询(票据专用，解决场景：假设第二页有一条数据，提交之后前端页面会保持在第二页上，但是列表数据没有)
     * <br>
     *
     * @param input      DataTablesInput
     * @param sql        SQL语句 (select ... from) or (select ... from where)
     * @param voClazz    vo实体类 bean.class
     * @param alias      别名
     * @param hasWhere   是否加where关键字
     * @param needParams 所需要拼接的值 null根据vo查询所有 有值根据list集合里拼接
     * @return String
     */
    public DataTablesOutput getDynamicBySQLForBill(DataTablesInput input, String sql, Class<?> voClazz, String alias, final Boolean hasWhere, Object... needParams) {
        DataTablesOutput outPut = dealDynamicBySQL(input, sql, voClazz, alias, hasWhere, needParams);
        /**
         * 解决场景：假设第二页有一条数据，提交之后前端页面会保持在第二页上，但是列表数据没有
         */
        if(outPut != null && CollectionUtils.isEmpty(outPut.getData()) && input.getStart() > 0){
            input.setStart(input.getStart() - input.getLength());
            outPut = dealDynamicBySQL(input, sql, voClazz, alias, hasWhere, needParams);
        }
        return outPut;
    }
    /**
     * sql查询 自动拼接条件和排序 查询 <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input
     *            dataTable请求对象
     * @param sql
     *            SQL语句
     * @param alias
     *            别名
     * @param hasWhere
     *            是否加where关键字
     * @param needParams
     *            String数组所需要拼接的值 不传或者null根据vo查询所有 有值根据list集合里拼接
     * @return 经过统一转换小写key的map集合
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DataTablesOutput getDynamicMapBySQL(DataTablesInput input, String sql, String alias, Boolean hasWhere,
            Object... needParams) {
        DataTablesOutput dataInfo = dealDynamicBySQL(input, sql, null, alias, hasWhere, needParams);
        // 把返回MAP中数据的KEY转换为小写
        if (EmptyUtils.isNotEmpty(dataInfo.getData()) && dataInfo.getData().get(0) instanceof Map) {
            List<Map> sourceList = new ArrayList<>();
            // 遍历MAP结果集
            for (Object obj : dataInfo.getData()) {
                Map map = (Map) obj;
                Map<String, Object> newMap = new HashMap<>();
                Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, Object> entry = entries.next();
                    newMap.put(entry.getKey().toLowerCase(), entry.getValue());
                }
                sourceList.add(newMap);
            }
            dataInfo.setData(sourceList);
        }
        return dataInfo;
    }
    /**
     * sql查询 返回小写key的map集合 <br>
     * <b>作者：</b>zhengg<br>
     * <b>日期：</b>2019/10/8<br>
     *
     * @param sql
     *            SQL语句
     * @param params
     *            传递参数
     * @return 经过统一转换小写key的map集合
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List listMapBySQL(String sql, Map params) {
        List list = this.listBySQL(sql, params);
        // 把返回MAP中数据的KEY转换为小写
        if (EmptyUtils.isNotEmpty(list) && list.get(0) instanceof Map) {
            List<Map> sourceList = new ArrayList<>();
            // 遍历MAP结果集
            for (Object obj : list) {
                Map map = (Map) obj;
                Map<String, Object> newMap = new HashMap<>();
                Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, Object> entry = entries.next();
                    newMap.put(entry.getKey().toLowerCase(), entry.getValue());
                }
                sourceList.add(newMap);
            }
            return sourceList;
        }
        return null;
    }
    /**
     * sql查询 自动拼接条件和排序 附加params <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017年7月26日<br>
     *
     * @param input
     *            dataTable请求对象
     * @param sql
     *            SQL语句
     * @param alias
     *            别名
     * @param hasWhere
     *            是否加where关键字
     * @param params
     *            针对sql带有查询条件的
     * @param needParams
     *            String数组所需要拼接的值 不传或者null根据vo查询所有 有值根据list集合里拼接
     * @return 经过统一转换小写key的map集合
     */
    public DataTablesOutput getDynamicParamsBySQL(DataTablesInput input, String sql, Class<?> voClazz, String alias,
            Boolean hasWhere, final DynamicallyObject params, Object... needParams) {
        return dealDynamicBySQL(input, sql, voClazz, alias, hasWhere, params, needParams);
    }
    /**
     * 处理 自动拼接条件和排序 <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input
     *            dataTable请求对象
     * @param sql
     *            SQL
     * @param voClazz
     *            转换为VO
     * @param alias
     *            别名
     * @param hasWhere
     *            是否存在where
     * @param needParams
     *            String数组所需要拼接的值 不传或者null根据vo查询所有 有值根据list集合里拼接
     * @return dataTable响应对象
     */
    protected DataTablesOutput dealDynamicBySQL(DataTablesInput input, String sql, Class<?> voClazz, String alias,
            Boolean hasWhere, Object... needParams) {
        Object bean = null;
        try {
            if (null != voClazz) {
                bean = voClazz.newInstance();
            }
            // 组合附带传值问题
            List<Object> tempList = new ArrayList<Object>();
            Object[] needList = null;
            Map<String, Object> paramMap = new HashMap<String, Object>();
            if (needParams != null) {
                for (Object o : needParams) {
                    if (o instanceof Map) {
                        paramMap.putAll((Map) o);
                    } else if (o instanceof DynamicallyObject) {
                        paramMap.putAll(((DynamicallyObject) o).getParams());
                    } else {
                        if (o != null) {
                            if (o.getClass().isArray()) {
                                Collections.addAll(tempList, (Object[]) o);
                            } else {
                                tempList.add(o);
                            }
                        }
                    }
                }
            }
            if (tempList.size() > 0) {
                needList = tempList.toArray(new Object[tempList.size()]);
            }
            String allCondition = getWhereAllCondition(input, bean, alias, hasWhere, needList);
            Map<String, Object> values = input.getSearchValues(needList);
            // 赋值
            if (values == null) {
                values = new HashMap<>();
            }
            values.putAll(paramMap);
            List list;
            BigDecimal count = new BigDecimal(0);
            // 针对获取合计单独处理：不需要分页和排序
            if (input.isMoneySum()) {
                list = listBySQL(sql + allCondition, values);
                if (null != voClazz) {
                    list = BeanUtils.copyMapListProperties(list, voClazz);
                }
            } else {
                int pageSize = input.getPageSize();
                // 页面大于100万不分页
                if (pageSize < 1000000) {
                    String countSQL = prepareCountSQL(sql + allCondition);
                    count = new BigDecimal(createSQLQuery(countSQL, values).uniqueResult().toString());
                    if (count.compareTo(BigDecimal.ZERO) > 0) {
                        // 修改: zuoy 优化查询方法
                        Integer fetchSize = input.getLength();
                        if (count.intValue() >= 500 && count.intValue() < 5000) {
                            fetchSize = 500;
                        } else if (count.intValue() >= 5000 && count.intValue() < 10000) {
                            fetchSize = 1000;
                        } else if (count.intValue() >= 10000) {
                            fetchSize = 5000;
                        }
                        list = listBySQL(sql + allCondition, input.getStart(), input.getLength(), values, fetchSize);
                        if (null != voClazz) {
                            list = BeanUtils.copyMapListProperties(list, voClazz);
                        }
                    } else {
                        list = new ArrayList(0);
                    }
                } else {
                    list = listBySQL(sql + allCondition, values);
                    if (null != voClazz) {
                        list = BeanUtils.copyMapListProperties(list, voClazz);
                    }
                }
            }
            return new DataTablesOutput<T>(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            logger.error(e.toString());
            // 如果是runtime直接抛出，非runtime进行转换
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new ParameterException(e);
            }
        }
    }
    /**
     * 拼接where and orderby <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input
     *            DataTablesInput
     * @param bean
     *            vo实体类
     * @param alias
     *            别名
     * @param hasWhere
     *            是否加where关键字
     * @param needParams
     *            String数组所需要拼接的值 不传或者null根据vo查询所有 有值根据list集合里拼接
     * @return String
     */
    protected String getWhereAllCondition(DataTablesInput input, Object bean, String alias, final Boolean hasWhere,
            Object... needParams) {
        String where = getWhereCondition(input, bean, alias, hasWhere, needParams);
        // 针对获取合计单独处理：不需要分页和排序
        if (!input.isMoneySum()) {
            String order = getWhereOrderBy(input, bean, alias, needParams);
            return where + order;
        }
        return where;
    }
    /**
     * 拼接where and orderby <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017年6月26日<br>
     *
     * @param input
     *            DataTablesInput
     * @param bean
     *            vo实体类
     * @param alias
     *            别名
     * @param hasWhere
     *            是否加where关键字
     * @param needParams
     *            String数组所需要拼接的值 不传或者null根据vo查询所有 有值根据list集合里拼接
     * @return Map&lt;String, Object&gt; key:where 代替拼接的条件String key:value 条件里别名的值Map&lt;String, Object&gt;
     */
    protected Map<String, Object> getWhereConditionAndValues(DataTablesInput input, Object bean, String alias,
            Boolean hasWhere, Object... needParams) {
        Map<String, Object> map = new HashMap<String, Object>();
        String where = getWhereCondition(input, bean, alias, hasWhere, needParams);
        String order = getWhereOrderBy(input, bean, alias, needParams);
        Map<String, Object> values = input.getSearchValues(needParams);
        map.put("where", where + order);
        map.put("value", values);
        return map;
    }
    /**
     * 拼接where <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input
     *            DataTablesInput
     * @param bean
     *            vo实体
     * @param alias
     *            别名
     * @param hasWhere
     *            是否加where关键字
     * @param needParams
     *            String数组所需要拼接的值 不传或者null根据vo查询所有 有值根据list集合里拼接
     * @return String
     */
    protected String getWhereCondition(DataTablesInput input, Object bean, String alias, Boolean hasWhere,
            Object... needParams) {
        StringBuffer sb = new StringBuffer();
        Map<String, Map<String, Object>> map = input.getSearchAnnotation(bean, alias, needParams);
        if (map.isEmpty()) {
            return "";
        }
        // input.getOrder()
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            String key = entry.getKey();
            Map<String, Object> value = entry.getValue();
            if (!value.isEmpty()) {
                String[] opCodes = (String[]) value.get("operCode");
                for (int i = 0; i < opCodes.length; i++) {
                    String opCode = opCodes[i];
                    if (sb.length() > 0) {
                        sb.append(" and ");
                    }
                    if (MatchTypeEnum.LLIKE.getValue().equalsIgnoreCase(opCode)) {
                        opCode = MatchTypeEnum.LIKE.getValue();
                    } else if (MatchTypeEnum.RLIKE.getValue().equalsIgnoreCase(opCode)) {
                        opCode = MatchTypeEnum.LIKE.getValue();
                    }
                    String[] fieldNameList = (String[]) value.get("fieldName");
                    String fieldName;
                    if (fieldNameList.length > 1) {
                        fieldName = fieldNameList[i];
                    } else {
                        fieldName = fieldNameList[0];
                    }
                    if (!fieldName.contains(",")) {
                        fieldName = MatchTypeEnum.getTypeName(opCode).name().toLowerCase() + key;
                        if (MatchTypeEnum.IN.getValue().equalsIgnoreCase(opCode)) {
                            Object object = input.getSearchValues().get(fieldName);
                            String inStr = "";
                            if (!EmptyUtils.isEmpty(object)) {
                                List<?> result = Splitter.on(",").omitEmptyStrings().trimResults()
                                        .splitToList(String.valueOf(object));
                                inStr = getSQLIn(result, 1000, ((String[]) value.get("fieldName"))[0]);
                                sb.append(inStr);
                                // input.getFormdata().remove(MatchTypeEnum.getTypeName(opCode).name().toUpperCase() +
                                // "s_" + key);
                            }
                            if (StringUtils.isBlank(inStr)) {
                                sb.setLength(sb.length() - 5);
                            }
                        } else if (MatchTypeEnum.NI.getValue().equalsIgnoreCase(opCode)) {
                            Object object = input.getSearchValues().get(fieldName);
                            String inStr = "";
                            if (!EmptyUtils.isEmpty(object)) {
                                List<?> result = Splitter.on(",").omitEmptyStrings().trimResults()
                                        .splitToList(String.valueOf(object));
                                inStr = SqlUtil.getNotinSql(result, ((String[]) value.get("fieldName"))[0], true);
                                sb.append(inStr);
                            }
                            if (StringUtils.isBlank(inStr)) {
                                sb.setLength(sb.length() - 5);
                            }
                        } else {
                            sb.append(((String[]) value.get("fieldName"))[0]).append(" ").append(opCode).append(" :")
                                    .append(fieldName);
                        }
                    } else {
                        String[] fieldNames = fieldName.split(",");
                        if (fieldNames.length > 0) {
                            StringBuffer orStr = new StringBuffer();
                            for (int j = 0; j < fieldNames.length; j++) {
                                String name = fieldNames[j];
                                String mapKey = (String) value.get("mapKey");
                                if (orStr.length() > 0) {
                                    orStr.append(" or ");
                                }
                                String reg = alias + "\\.";
                                fieldName = "or" + MatchTypeEnum.getTypeName(opCode).name().toLowerCase()
                                        + mapKey.split("#")[j];
                                if (MatchTypeEnum.IN.getValue().equalsIgnoreCase(opCode)) {
                                    Object object = input.getSearchValues().get(fieldName);
                                    String inStr = "";
                                    if (!EmptyUtils.isEmpty(object)) {
                                        List<?> result = Splitter.on(",").omitEmptyStrings().trimResults()
                                                .splitToList(String.valueOf(object));
                                        inStr = getSQLIn(result, 1000, name);
                                        // input.getFormdata().remove(MatchTypeEnum.getTypeName(opCode).name().toUpperCase()
                                        // + "s_" + key);
                                    }
                                    /*
                                     * if (object instanceof String) { List<?> result =
                                     * Splitter.on(",").omitEmptyStrings().trimResults().splitToList((String) object);
                                     * inStr = getSQLIn(result, 1000, name);
                                     * input.getFormdata().remove(MatchTypeEnum.getTypeName(opCode).name().toUpperCase()
                                     * + "s_" + key); }
                                     */
                                    if (StringUtils.isBlank(inStr)) {
                                        orStr.setLength(orStr.length() - 4);
                                    } else {
                                        orStr.append(" (").append(inStr).append(" )");
                                    }
                                } else if (MatchTypeEnum.NI.getValue().equalsIgnoreCase(opCode)) {
                                    Object object = input.getSearchValues().get(fieldName);
                                    String inStr = "";
                                    if (!EmptyUtils.isEmpty(object)) {
                                        List<?> result = Splitter.on(",").omitEmptyStrings().trimResults()
                                                .splitToList(String.valueOf(object));
                                        inStr = SqlUtil.getNotinSql(result, ((String[]) value.get("fieldName"))[0],
                                                true);
                                    }
                                    if (StringUtils.isBlank(inStr)) {
                                        orStr.setLength(orStr.length() - 4);
                                    } else {
                                        orStr.append(" (").append(inStr).append(" )");
                                    }
                                } else {
                                    orStr.append(name).append(" ").append(opCode).append(" :").append(fieldName);
                                }
                            }
                            if (orStr.length() < 1) {
                                sb.setLength(sb.length() - 5);
                            } else {
                                sb.append(" (").append(orStr.toString()).append(" )");
                            }
                        } else {
                            sb.setLength(sb.length() - 5);
                        }
                    }
                    sb.append(" ");
                }
            }
        }
        if (sb.length() > 0) {
            sb.insert(0, hasWhere ? " where " : " and ");
        }
        return sb.toString();
    }
    /**
     * 拼接orderby <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param input
     *            DataTablesInput
     * @param bean
     *            vo实体
     * @param alias
     *            别名
     * @return String
     */
    protected String getWhereOrderBy(DataTablesInput input, Object bean, String alias, Object... needParams) {
        StringBuffer sb = new StringBuffer();
        Map<String, Map<String, Object>> map = input.getSearchAnnotation(bean, alias, needParams);
        if (map.isEmpty()) {
            return "";
        }
        Map<String, String> orderMap = input.getOrderByMap();
        for (Map.Entry<String, String> entry : orderMap.entrySet()) {
            String key = entry.getKey();
            if (map.containsKey(key)) {
                Map<String, Object> value = map.get(key);
                if (!value.isEmpty()) {
                    if (StringUtils.isBlank((String) value.get("orderBy"))) {
                        continue;
                    }
                    String[] nameStr = (String[]) value.get("fieldName");
                    for (String fieldName : nameStr) {
                        if (fieldName.contains(",")) {
                            String[] nameList = fieldName.split(",");
                            for (String name : nameList) {
                                if (!sb.toString().contains(name)) {
                                    sb.append(name).append(" ").append((String) value.get("orderBy")).append(",");
                                }
                            }
                        } else {
                            if (!sb.toString().contains(fieldName)) {
                                sb.append(fieldName).append(" ").append((String) value.get("orderBy")).append(",");
                            }
                        }
                    }
                }
            }
        }
        /*
         * for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) { Map<String, Object> value =
         * entry.getValue(); if (!value.isEmpty()) { if (StringUtils.isBlank((String) value.get("orderBy"))) { continue;
         * } String[] nameStr = (String[]) value.get("fieldName"); for (String fieldName : nameStr) { if
         * (fieldName.contains(",")) { String[] nameList = fieldName.split(","); for (String name : nameList) { if
         * (!sb.toString().contains(name)) { sb.append(name).append(" ").append((String)
         * value.get("orderBy")).append(","); } } } else { if (!sb.toString().contains(fieldName)) {
         * sb.append(fieldName).append(" ").append((String) value.get("orderBy")).append(","); } } } } }
         */
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
            sb.insert(0, " order by ");
        }
        if (input.getOrderString() != null) {
            OrderString orderString = input.getOrderString();
            if (sb.length() <= 0) {
                sb.append(" order by ").append(orderString.getColumn()).append(" ").append(orderString.getSort());
            } else {
                sb.append(" , ").append(orderString.getColumn()).append(" ").append(orderString.getSort());
            }
        }
        return sb.toString();
    }
    /**
     * 处理oracle sql 语句in子句中超过1000项就会报错 <br>
     * <b>作者：</b>zhouj<br>
     * <b>日期：</b>2017/6/10<br>
     *
     * @param ids
     *            in语句中的集合对象
     * @param count
     *            in语句中出现的条件个数
     * @param field
     *            in语句对应的数据库查询字段
     * @return field in (...) or field in (...) 字符串
     */
    protected String getSQLIn(List<?> ids, int count, String field) {
        count = Math.min(count, 1000);
        int len = ids.size();
        int size = len % count;
        if (size == 0) {
            size = len / count;
        } else {
            size = (len / count) + 1;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int fromIndex = i * count;
            int toIndex = Math.min(fromIndex + count, len);
            String productId = StringUtils.defaultIfEmpty(StringUtils.join(escapeSqlLiterals(ids.subList(fromIndex, toIndex)), "','"), "");
            if (i != 0) {
                builder.append(" or ");
            }
            builder.append(field).append(" in ('").append(productId).append("')");
        }
        if (EmptyUtils.isNotEmpty(builder.toString())) {
            return "(" + builder.toString() + ")";
        }
        return field + " in ('')";
    }
    protected String getSQLNotIn(List<?> ids, int count, String field) {
        count = Math.min(count, 1000);
        int len = ids.size();
        int size = len % count;
        if (size == 0) {
            size = len / count;
        } else {
            size = (len / count) + 1;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int fromIndex = i * count;
            int toIndex = Math.min(fromIndex + count, len);
            String productId = StringUtils.defaultIfEmpty(StringUtils.join(escapeSqlLiterals(ids.subList(fromIndex, toIndex)), "','"), "");
            if (i != 0) {
                builder.append(" or ");
            }
            builder.append(field).append(" not in ('").append(productId).append("')");
        }
        if (EmptyUtils.isNotEmpty(builder.toString())) {
            return "(" + builder.toString() + ")";
        }
        return field + " not in ('')";
    }

    private List<String> escapeSqlLiterals(List<?> values) {
        List<String> result = new ArrayList<String>();
        if (values == null) {
            return result;
        }
        for (Object value : values) {
            result.add(value == null ? "" : String.valueOf(value).replace("'", "''"));
        }
        return result;
    }
}
