package com.bytter.core.common.datatable;

import cn.hutool.core.collection.CollUtil;
import com.bytter.core.base.DataTableHelper;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.page.ParamImpl;
import com.bytter.core.common.persistence.PropertyFilter;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.enums.PropertyTypeEnum;
import com.bytter.core.proxy.BtSearchDynamically;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

public class DataTablesInput implements IParameterInput, Serializable {
    private Integer draw = 1;
    private Integer start = 0;
    private Integer length = 10;
    private Search search = new Search();
    private List<Order> order = new ArrayList<Order>();
    private List<Column> columns = new ArrayList<Column>();
    private Map<String, Object> formdata = new HashMap<String, Object>();

    private String currentUserId;
    private String resource;
    private ICurrentUser currentUser;
    private Locale locale;
    private String filename;
    private String title;
    /**
     * 是否金额合计：false否true是
     */
    private boolean isMoneySum;
    /**
     * 统计列名（比如：XX合计）
     */
    private String totalColumn;
    /**
     * 统计数据列名（具体需要统计的数据的列名）
     */
    private List<String> totalList = new ArrayList<String>();

    public DataTablesInput() {
        this.locale = LocaleContextHolder.getLocale();
        ICurrentUser currentUser1 = UserContextHelper.helper().getCurrentUser();
        //定时任务等误用会造成问题
        if (Objects.nonNull(currentUser1)) {
            this.currentUser = currentUser1;
            this.currentUserId = this.currentUser.getId();
        }
    }

    public DataTablesInput(Locale locale, ICurrentUser currentUser, String currentUserId) {
        this.locale = locale;
        this.currentUser = currentUser;
        this.currentUserId = currentUserId;
    }

    /**
     * <p>
     * 加上根据字段搜索
     * </p>
     * 修改: zhoul 2017-08-14
     */
    private OrderString orderString;

    public static final String OR_SEPARATOR = "_OR_";

    public Map<String, Column> getColumnsAsMap() {
        Map<String, Column> map = new HashMap<String, Column>();
        for (Column column : columns) {
            map.put(column.getData(), column);
        }
        return map;
    }

    public Column getColumn(String columnName) {
        if (columnName == null) {
            return null;
        }
        for (Column column : columns) {
            if (columnName.equals(column.getData())) {
                return column;
            }
        }
        return null;
    }

    public void addColumn(String columnName, boolean searchable, boolean orderable, String searchValue) {
        this.columns.add(new Column(columnName, "", searchable, orderable, new Search(searchValue, false)));
    }

    public void addOrder(String columnName, boolean ascending) {
        if (columnName == null) {
            return;
        }
        for (int i = 0; i < columns.size(); i++) {
            // if (!columnName.equals(columns.get(i).getData())) {
            if (!columnName.equals(columns.get(i).getName())) {
                continue;
            }
            order.add(new Order(i, ascending ? "asc" : "desc"));
        }
    }

    public void addTotalList(String... columnName) {
        if (columnName == null) {
            return;
        }
        totalList.addAll(Arrays.asList(columnName));
        // 去重
        totalList = CollUtil.distinct(totalList);
    }


    /**
     * 添加搜索条件
     *
     * @param matchType  条件类型
     * @param columnName 字段名称
     * @param value      值
     */
    public void addFilter(MatchTypeEnum matchType, String columnName, Object value) {
        if (columnName == null) {
            return;
        }
        PropertyTypeEnum propertyTypeEnum;
        try {
            propertyTypeEnum = PropertyTypeEnum.valueOf(value.getClass());
            propertyTypeEnum.getValue();
            // Enum.valueOf(PropertyTypeEnum.class, propertyTypeCode.toUpperCase()).getValue();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + columnName + "没有按规则编写,无法得到属性值类型.", e);
        }
        String filterName = matchType.name();
        formdata.put(filterName + propertyTypeEnum.name().toLowerCase() + "_" + columnName, value);
    }

    /**
     * 添加搜索条件
     *
     * @param matchType  条件类型
     * @param columnName 字段名称
     * @param value      值
     * @param valueClazz 值所属的类
     */
    public void addFilter(MatchTypeEnum matchType, String columnName, Object value, Class valueClazz) {
        if (columnName == null) {
            return;
        }
        PropertyTypeEnum propertyTypeEnum;
        try {
            propertyTypeEnum = PropertyTypeEnum.valueOf(valueClazz);
            // Enum.valueOf(PropertyTypeEnum.class, propertyTypeCode.toUpperCase()).getValue();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + columnName + "没有按规则编写,无法得到属性值类型.", e);
        }
        String filterName = matchType.name();
        formdata.put(filterName + propertyTypeEnum.name().toLowerCase() + "_" + columnName, value);
    }

    /**
     * get order by list
     *
     * @return List Map
     */
    public List<Map<String, String>> getOrderByList() {
        List<Map<String, String>> list = Lists.newArrayList();
        if (!EmptyUtils.isEmptyList(order)) {
            for (Order ord : order) {
                SqlOrderValidator.validateDirection(ord.getDir());
                Column column = columns.get(ord.getColumn());
                if (column.getOrderable() && !EmptyUtils.isBlank(column.getName())) {
                    SqlOrderValidator.validateColumn(column.getName());
                    Map<String, String> map = Maps.newHashMap();
                    map.put("order", column.getName());
                    map.put("by", ord.getDir());
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * get order by Map
     *
     * @return List Map
     */
    public Map<String, String> getOrderByMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        if (!EmptyUtils.isEmptyList(order)) {
            for (Order ord : order) {
                SqlOrderValidator.validateDirection(ord.getDir());
                Column column = columns.get(ord.getColumn());
                if ((column.getOrderable() == null || column.getOrderable()) && !EmptyUtils.isBlank(column.getName())) {
                    SqlOrderValidator.validateColumn(column.getName());
                    map.put(column.getName(), ord.getDir());
                }
            }
        }
        return map;
    }

    /**
     * get order by to field str
     *
     * @return String
     */
    public String getOrderPara() {
        StringBuffer sb = new StringBuffer();
        if (!EmptyUtils.isEmptyList(order)) {
            for (Order ord : order) {
                SqlOrderValidator.validateDirection(ord.getDir());
                Column column = columns.get(ord.getColumn());
                if (column.getOrderable() && !EmptyUtils.isBlank(column.getName())) {
                    SqlOrderValidator.validateColumn(column.getName());
                    sb.append(column.getName()).append(",");
                }
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * get order by to ascending str
     *
     * @return String
     */
    public String getOrderByPara() {
        StringBuffer sb = new StringBuffer();
        if (!EmptyUtils.isEmptyList(order)) {
            for (Order ord : order) {
                SqlOrderValidator.validateDirection(ord.getDir());
                Column column = columns.get(ord.getColumn());
                if (column.getOrderable() && !EmptyUtils.isBlank(column.getName())) {
                    sb.append(ord.getDir()).append(",");
                }
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * get form parameter value
     *
     * @param name 参数名称
     * @return Object
     */
    protected Object getParaValue(String name) {
        for (Map.Entry<String, Object> entry : formdata.entrySet()) {
            String filterName = entry.getKey();
            Object value = entry.getValue();
            String propertyNameStr = StringUtils.substringAfter(filterName, "_");
            String modified = Pattern.compile(OR_SEPARATOR, Pattern.CASE_INSENSITIVE).matcher(propertyNameStr)
                    .replaceAll(OR_SEPARATOR);
            String[] propertyNames = modified.split(OR_SEPARATOR);
            // String[] propertyNames = StringUtils.splitByWholeSeparator(propertyNameStr, OR_SEPARATOR);
            for (String propertyName : propertyNames) {
                if (name.equals(propertyName)) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * Returns the value of a parameter as a String, or null if the parameter does not exist
     *
     * @param name a String specifying the name of the parameter
     * @return a String representing the single value of the parameter
     */
    public String getPara(String name) {
        Object object = getParaValue(name);
        if (EmptyUtils.isEmpty(object)) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }

    /**
     * Returns the value of a parameter as a String, or default value if the parameter does not exist.
     *
     * @param name         a String specifying the name of the parameter
     * @param defaultValue a String value be returned when the value of parameter is null
     * @return a String representing the single value of the parameter
     */
    public String getPara(String name, String defaultValue) {
        String result = getPara(name);
        return result != null && !"".equals(result) ? result : defaultValue;
    }

    /**
     * Get para from url and conver to Integer. The first index is 0
     *
     * @param name 参数名
     * @return 获取Integer类型的参数
     */
    public Integer getParaToInt(String name) {
        String result = getPara(name);
        return result != null && !"".equals(result) ? Integer.parseInt(result) : null;
    }

    /**
     * Get para from url and conver to Integer with default value if it is null.
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @return 获取Integer类型的参数
     */
    public Integer getParaToInt(String name, Integer defaultValue) {
        Integer result = getParaToInt(name);
        return result != null ? result : defaultValue;
    }

    /**
     * Get para from and conver to Long.
     *
     * @param name 参数名
     * @return 获取Long类型的参数
     */
    public Long getParaToLong(String name) {
        String result = getPara(name);
        return result != null && !"".equals(result) ? Long.parseLong(result) : null;
    }

    /**
     * Get para from and conver to Long with default value if it is null.
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @return 获取Long类型的参数
     */
    public Long getParaToLong(String name, Long defaultValue) {
        Long result = getParaToLong(name);
        return result != null ? result : defaultValue;
    }

    /**
     * Returns the values of the parameters as a Map.
     *
     * @return a Map contains all the parameters name and value
     */
    public Map<String, String[]> getParaMap() {
        Map<String, String[]> result = Maps.newConcurrentMap();
        ArrayListMultimap<String, Object> multiMap = ArrayListMultimap.create();
        if (!EmptyUtils.isEmpty(formdata)) {
            for (Map.Entry<String, Object> entry : formdata.entrySet()) {
                String filterName = entry.getKey();
                Object value = entry.getValue();
                if (!EmptyUtils.isEmpty(value) && !EmptyUtils.isBlank((String) value)) {
                    String firstPart = StringUtils.substringBefore(filterName, "_");
                    String propertyTypeCode = StringUtils.substring(firstPart, firstPart.length() - 1,
                            firstPart.length());
                    multiMap.put(propertyTypeCode, value);
                }
            }
        }
        for (String key : multiMap.keySet()) {
            Collection<Object> fruits = multiMap.get(key);
            String[] strs = (String[]) fruits.toArray();
            result.put(key, strs);
        }
        return result;
    }

    /**
     * dynamic set searchAnnotation proxyClass by voClass and alias
     *
     * @param bean       实体vo类
     * @param alias      别名
     * @param needParams 需要的参数
     * @return Class&lt;DynamicSearchBeanProxy&gt;
     */
    public Map<String, Map<String, Object>> getSearchAnnotation(Object bean, String alias, Object... needParams) {
        Map<String, Object> beanMap = Maps.newHashMap();
        // ArrayListMultimap<String,MatchTypeEnum> conditionMap = ArrayListMultimap.create();
        ArrayListMultimap<String, Object> conditionMap = ArrayListMultimap.create();
        Map<String, String> orderMap = this.getOrderByMap();
        // 表单搜索元素
        List<PropertyFilter> propertyFilters = DataTableHelper.search(this);
        // TODO:列搜索元素
        if (!propertyFilters.isEmpty()) {
            for (PropertyFilter filter : propertyFilters) {
                if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
                    beanMap.put(filter.getPropertyName(), filter.getMatchValue());
                    conditionMap.put(filter.getPropertyName(), filter.getMatchType());
                } else {// 包含多个属性需要比较的情况,进行or处理.
                    Map<String, Object> orMap = Maps.newHashMap();
                    for (String propertyName : filter.getPropertyNames()) {
                        beanMap.put(propertyName, filter.getMatchValue());
                        orMap.put(propertyName, filter.getMatchType());
                    }
                    conditionMap.put(filter.getPropertyNames()[0], orMap);
                }
            }
        }
        if (null != bean) {
            bean = BeanUtils.copyMapProperties(beanMap, bean.getClass());
        }
        try {
            // return BtSearchDynamically.searchAnnotation(alias, bean, conditionMap, orderMap);
            return BtSearchDynamically.searchAnnotationMap(alias, bean, conditionMap, orderMap, needParams);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
    }

    /**
     * map to bean
     *
     * @param needParams 需要的参数
     * @return 搜索到的结果集
     */
    public Map<String, Object> getSearchValues(Object... needParams) {
        Map<String, Object> beanMap = Maps.newHashMap();
        // 表单搜索元素
        List<PropertyFilter> propertyFilters = DataTableHelper.searchWithIn(this);
        if (!propertyFilters.isEmpty()) {
            for (PropertyFilter filter : propertyFilters) {
                if (null != needParams && needParams.length > 0) {
                    List<Object> objects = Arrays.asList(needParams);
                    if (!objects.contains(filter.getPropertyName())) {
                        continue;
                    }
                }
                Object value = filter.getMatchValue();
                MatchTypeEnum matchTypeEnum = filter.getMatchType();
                if (MatchTypeEnum.LIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    // zhoujian 2018年6月14日15:52:08 B20180600221新增支持中间带空格模糊搜索
                    String val = String.valueOf(filter.getMatchValue());
                    if (StringUtils.isNotBlank(val)) {
                        val = val.trim().replaceAll("\\s+", "%");
                    }
                    value = "%" + val + "%";
                    // value = "%" + String.valueOf(filter.getMatchValue()) + "%";
                } else if (MatchTypeEnum.LLIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    matchTypeEnum = MatchTypeEnum.LIKE;
                    value = "%" + filter.getMatchValue();
                } else if (MatchTypeEnum.RLIKE.getValue().equalsIgnoreCase(filter.getMatchType().getValue())) {
                    matchTypeEnum = MatchTypeEnum.LIKE;
                    value = filter.getMatchValue() + "%";
                }
                if (!filter.hasMultiProperties()) { // 只有一个属性需要比较的情况.
                    beanMap.put(matchTypeEnum.name().toLowerCase() + filter.getPropertyName(), value);
                } else {// 包含多个属性需要比较的情况,进行or处理.
                    for (String propertyName : filter.getPropertyNames()) {
                        beanMap.put("or" + matchTypeEnum.name().toLowerCase() + propertyName, value);
                    }
                }
            }
        }
        return beanMap;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Map<String, Object> getFormdata() {
        return formdata;
    }

    public void setFormdata(Map<String, Object> formdata) {
        this.formdata = formdata;
    }

    public OrderString getOrderString() {
        SqlOrderValidator.validateOrderString(orderString);
        return orderString;
    }

    public void setOrderString(OrderString orderString) {
        SqlOrderValidator.validateOrderString(orderString);
        this.orderString = orderString;
    }

    public String getTotalColumn() {
        return totalColumn;
    }

    public void setTotalColumn(String totalColumn) {
        this.totalColumn = totalColumn;
    }

    public List<String> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<String> totalList) {
        this.totalList = totalList;
    }

    @Override
    public String toString() {
        return "DataTablesInput{" + "draw=" + draw + ", start=" + start + ", length=" + length + ", search=" + search
                + ", order=" + order + ", columns=" + columns + '}';
    }

    @Override
    public Integer getPageNumber() {
        return getStart() / getPageSize() + 1;
    }

    @Override
    public Integer getPageSize() {
        return getLength();
    }

    /**
     * <p>
     * 设置每页的大小
     * </p>
     * <b>新增：</b>chenlw 2019-01-07
     *
     * @param pageSize 页面大小
     */
    @Override
    public void setPageSize(Integer pageSize) {
        setLength(pageSize);
    }

    @Override
    public String getCurrentUserId() {
        return currentUserId;
    }

    @Override
    public IParameter getParameter() {
        return new ParamImpl(getFormdata());
    }

    public ICurrentUser getCurrentUser() {
        return currentUser;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMoneySum() {
        return isMoneySum;
    }

    public void setMoneySum(boolean moneySum) {
        isMoneySum = moneySum;
    }

    public void addColumnOrder(String columnName, boolean ascending) {
        if (columnName == null) {
            return;
        }
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (!columnName.equals(column.getName())) {
                continue;
            }
            column.setOrderable(true);
            order.add(new Order(i, ascending ? "asc" : "desc"));
        }
    }

    public Integer getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            if (columnName.equals(column.getName())) {
                return i;
            }
        }
        return -1;
    }
    public void addFormData(MatchTypeEnum matchType, String columnName, Object value, Class valueClazz) {
        if (columnName == null) {
            return;
        }
        PropertyTypeEnum propertyTypeEnum;
        try {
            propertyTypeEnum = PropertyTypeEnum.valueOf(valueClazz);
            if (propertyTypeEnum == null) {return;}
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("filter名称" + columnName + "没有按规则编写,无法得到属性值类型.", e);
        }
        formdata.put(matchType.name().toLowerCase() + propertyTypeEnum.name().toLowerCase() + "_" + columnName, value);
    }

    public void addFormData(String columnName, Object value) {
        formdata.put(columnName, value);
    }
}
