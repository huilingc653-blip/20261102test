package com.bytter.olap.dataprovider;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dataprovider.aggregator.Aggregatable;
import com.bytter.olap.dataprovider.aggregator.InnerAggregator;
import com.bytter.olap.dataprovider.config.AggConfig;
import com.bytter.olap.dataprovider.config.CompositeConfig;
import com.bytter.olap.dataprovider.config.ConfigComponent;
import com.bytter.olap.dataprovider.config.DimensionConfig;
import com.bytter.olap.dataprovider.expression.NowFunction;
import com.bytter.olap.dataprovider.result.AggregateResult;
import com.bytter.olap.util.NaturalOrderComparator;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.googlecode.aviator.AviatorEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by zyong on 2017/1/9.
 */
public abstract class DataProvider {

    private InnerAggregator innerAggregator;

    protected Map<String, String> dataSource;

    protected Map<String, String> query;

    /**
     * Freemarker语法SQL语句的参数map
     */
    protected Map<String, Object> freemarkerSqlParams;

    /**
     * 结果集限制数目
     */
    private int resultLimit;
    private boolean isUsedForTest = false;

    /**
     * second
     */
    private long interval = 12 * 60 * 60;

    /**
     * 空值字符串
     */
    public static final String NULL_STRING = "";
    //public static final String NULL_STRING = "#NULL";

    private static final Logger logger = LoggerFactory.getLogger(DataProvider.class);

    static {
        AviatorEvaluator.addFunction(new NowFunction());
    }

    public abstract boolean doAggregationInDataSource();

    /**
     * 判断数据源是否启用了数据源聚合选项
     *
     * @return 是否启用了数据源聚合选项
     */
    public boolean isDataSourceAggInstance() {
        if (this instanceof Aggregatable && doAggregationInDataSource()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * get the aggregated data by user's widget designer
     * @param aggConfig   图表配置对象
     * @param reload      是否重新加载
     * @return 图表数据查询结果对象
     * @throws Exception    查询异常
     */
    public final AggregateResult getAggData(AggConfig aggConfig, boolean reload) throws Exception {
        evalValueExpression(aggConfig);
        if (isDataSourceAggInstance()) {
            // 启用了数据源聚合功能，则使用Aggregatable接口的方法查询数据（JdbcDataProvider）
            return ((Aggregatable) this).queryAggData(aggConfig);
        } else {
            // 未启用数据源聚合功能，则调用H2Aggregator来查询数据
            checkAndLoad(reload);
            return innerAggregator.queryAggData(aggConfig);
        }
    }

    /**
     * 获取 查询聚合数据的SQL
     *
     * @param aggConfig 图表聚合配置对象
     * @return 查询聚合数据的SQL
     * @throws Exception 异常
     */
    public final String getViewAggDataQuery(AggConfig aggConfig) throws Exception {
        evalValueExpression(aggConfig);
        // 数据源聚合选项启用才能查看
        if (isDataSourceAggInstance()) {
            return ((Aggregatable) this).viewAggDataQuery(aggConfig);
        } else {
            return "Not Support";
        }
    }

    /**
     * 去除JAVA8特性
     * Created by chenlw on 2019/5/10.
     *
     * @param columnName    列名
     * @param config        图表配置对象
     * @param reload        是否重新加载
     * @return 维度列表数组
     * @throws Exception    查询异常
     */
    public final String[] getDimVals(String columnName, AggConfig config, boolean reload) throws Exception {
        String[] dimVals = null;
        evalValueExpression(config);
        if (isDataSourceAggInstance()) {
            dimVals = ((Aggregatable) this).queryDimVals(columnName, config);
        } else {
            checkAndLoad(reload);
            dimVals = innerAggregator.queryDimVals(columnName, config);
        }

        if (dimVals != null) {
            List<String> returnList = new ArrayList<>();
            for (int i = 0, size = dimVals.length; i < size; i++) {
                returnList.add(dimVals[i] == null ? NULL_STRING : dimVals[i]);
            }
            Collections.sort(returnList, new NaturalOrderComparator());
            return returnList.toArray(new String[0]);
        } else {
            return new String[]{};
        }
    }


    /**
     * 获取列维
     *
     * @param reload 是否重新加载
     * @return 列维数组
     * @throws Exception 异常
     */
    public final String[] getColumn(boolean reload) throws Exception {
        String[] columns = null;
        if (isDataSourceAggInstance()) {
            columns = ((Aggregatable) this).getColumn();
        } else {
            checkAndLoad(reload);
            columns = innerAggregator.getColumn();
        }
        Arrays.sort(columns);
        return columns;
    }

    /**
     * 加载数据
     *
     * @param reload 是否重新加载数据
     * @throws Exception 异常
     */
    private void checkAndLoad(boolean reload) throws Exception {
        String key = getLockKey();
        synchronized (key.intern()) {
            if (reload || !innerAggregator.checkExist()) {
                // JdbcDataProvider的getData()方法是返回null的，但其他DataProvider的getData()方法不一定返回null
                String[][] data = getData();
                if (data != null) {
                    innerAggregator.loadData(data, interval);
                }
                logger.info("loadData {}", key);
            }
        }
    }


    /**
     *
     * @param ac
     */
    private void evalValueExpression(AggConfig ac) {
        if (ac == null) {
            return;
        }

        List<ConfigComponent> filterList = ac.getFilters();
        for (ConfigComponent configComponent : filterList) {
            evaluator(configComponent);
        }

        List<DimensionConfig> columnList = ac.getColumns();
        for (DimensionConfig dimensionConfig : columnList) {
            evaluator(dimensionConfig);
        }

        List<DimensionConfig> rowList = ac.getRows();
        for (DimensionConfig dimensionConfig : rowList) {
            evaluator(dimensionConfig);
        }
    }

    /**
     * 看板参数行的遍历
     * Created by chenlw on 2019/5/10.（去除JAVA8特性）
     *
     * @param e
     */
    private void evaluator(ConfigComponent e) {
        if (e instanceof DimensionConfig) {
            DimensionConfig dc = (DimensionConfig) e;
            //dc.getValues()  选定值
            List<String> list = new ArrayList<>();
            List<String> valueList = dc.getValues();
            for (String value : valueList) {
                list.addAll(getFilterValue(value));
            }
            dc.setValues(list);
        }
        if (e instanceof CompositeConfig) {
            CompositeConfig cc = (CompositeConfig) e;
            List<ConfigComponent> configComponentList = cc.getConfigComponents();
            for (ConfigComponent configComponent : configComponentList) {
                evaluator(configComponent);
            }
        }
    }

    /**
     * 获取过滤器的值
     *
     * @param value 过滤器字符串的值
     * @return 要过滤的数值列表
     */
    private List<String> getFilterValue(String value) {
        List<String> list = new ArrayList<>();
        if (value == null || !(value.startsWith("{") && value.endsWith("}"))) {
            list.add(value);
        } else {
            list.add(AviatorEvaluator.compile(value.substring(1, value.length() - 1), true).execute().toString());
        }
        return list;
    }


    public String getLockKey() {
        // String dataSourceStr = JSONObject.toJSON(dataSource).toString();
        // String queryStr = JSONObject.toJSON(query).toString();
        String dataSourceStr = JsonUtils.toJSONString(dataSource);
        String queryStr = JsonUtils.toJSONString(query);
        return Hashing.md5().newHasher().putString(dataSourceStr + queryStr, Charsets.UTF_8).hash().toString();
    }


    /**
     * Created by chenlw on 2019/5/10.（去除JAVA8特性）
     *
     * @param filters
     * @return
     */
    public List<DimensionConfig> filterCCList2DCList(List<ConfigComponent> filters) {
        List<DimensionConfig> result = new LinkedList<>();
        for (ConfigComponent configComponent : filters) {
            result.addAll(configComp2DimConfigList(configComponent));
        }
        return result;
    }


    public List<DimensionConfig> configComp2DimConfigList(ConfigComponent cc) {
        List<DimensionConfig> result = new LinkedList<>();
        if (cc instanceof DimensionConfig) {
            result.add((DimensionConfig) cc);
        } else {
            Iterator<ConfigComponent> iterator = cc.getIterator();
            while (iterator.hasNext()) {
                ConfigComponent next = iterator.next();
                result.addAll(configComp2DimConfigList(next));
            }
        }
        return result;
    }

    abstract public String[][] getData() throws Exception;

    public void test() throws Exception {
        getData();
    }

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public void setResultLimit(int resultLimit) {
        this.resultLimit = resultLimit;
    }

    public int getResultLimit() {
        return resultLimit;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public InnerAggregator getInnerAggregator() {
        return innerAggregator;
    }

    public void setInnerAggregator(InnerAggregator innerAggregator) {
        this.innerAggregator = innerAggregator;
    }

    /**
     * 判断是否用于测试
     *
     * @return 是否用于测试
     */
    public boolean isUsedForTest() {
        return isUsedForTest;
    }

    public void setUsedForTest(boolean usedForTest) {
        isUsedForTest = usedForTest;
    }

    public Map<String, Object> getFreemarkerSqlParams() {
        return freemarkerSqlParams;
    }

    public void setFreemarkerSqlParams(Map<String, Object> freemarkerSqlParams) {
        this.freemarkerSqlParams = freemarkerSqlParams;
    }

    //    public static ConfigComponent separateNull(ConfigComponent configComponent) {
//        if (configComponent instanceof DimensionConfig) {
//            DimensionConfig cc = (DimensionConfig) configComponent;
//            if (("=".equals(cc.getFilterType()) || "≠".equals(cc.getFilterType())) && cc.getValues().size() > 1 &&
//                    cc.getValues().stream().anyMatch(s -> DataProvider.NULL_STRING.equals(s))) {
//                CompositeConfig compositeConfig = new CompositeConfig();
//                compositeConfig.setType("=".equals(cc.getFilterType()) ? "OR" : "AND");
//                cc.setValues(cc.getValues().stream().filter(s -> !DataProvider.NULL_STRING.equals(s)).collect(Collectors.toList()));
//                compositeConfig.getConfigComponents().add(cc);
//                DimensionConfig nullCc = new DimensionConfig();
//                nullCc.setColumnName(cc.getColumnName());
//                nullCc.setFilterType(cc.getFilterType());
//                nullCc.setValues(new ArrayList<>());
//                nullCc.getValues().add(DataProvider.NULL_STRING);
//                compositeConfig.getConfigComponents().add(nullCc);
//                return compositeConfig;
//            }
//        }
//        return configComponent;
//    }

    /**
     * Created by chenlw on 2019/5/10.（去除JAVA8特性）
     *
     * @param configComponent
     * @return
     */
    public static ConfigComponent separateNull(ConfigComponent configComponent) {
        if (configComponent instanceof DimensionConfig) {
            DimensionConfig cc = (DimensionConfig) configComponent;
            if (("=".equals(cc.getFilterType()) || "≠".equals(cc.getFilterType())) && cc.getValues().size() > 1) {

                boolean anyMatchResult = false;
                List<String> valueList = cc.getValues();
                for (int i = 0, size = valueList.size(); i < size; i++) {
                    if (DataProvider.NULL_STRING.equals(valueList.get(i))) {
                        anyMatchResult = true;
                        break;
                    }
                }

                if (anyMatchResult) {
                    CompositeConfig compositeConfig = new CompositeConfig();
                    compositeConfig.setType("=".equals(cc.getFilterType()) ? "OR" : "AND");

                    List<String> valueListTemp = new ArrayList<>();
                    for (String value : valueList) {
                        if (!DataProvider.NULL_STRING.equals(value)) {
                            valueListTemp.add(value);
                        }
                    }
                    cc.setValues(valueListTemp);

                    compositeConfig.getConfigComponents().add(cc);
                    DimensionConfig nullCc = new DimensionConfig();
                    nullCc.setColumnName(cc.getColumnName());
                    nullCc.setFilterType(cc.getFilterType());
                    nullCc.setValues(new ArrayList<String>());
                    nullCc.getValues().add(DataProvider.NULL_STRING);
                    compositeConfig.getConfigComponents().add(nullCc);
                    return compositeConfig;
                }

            }
        }
        return configComponent;
    }


}
