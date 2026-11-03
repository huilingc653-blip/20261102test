package com.bytter.olap.dataprovider;

import com.bytter.olap.dataprovider.aggregator.InnerAggregator;
import com.bytter.olap.dataprovider.aggregator.h2.H2Aggregator;
import com.bytter.olap.dataprovider.annotation.DatasourceParameter;
import com.bytter.olap.dataprovider.annotation.ProviderName;
import com.bytter.olap.jdbc.JdbcDataProvider;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by yfyuan on 2016/8/15.
 */
@Service
public class DataProviderManager implements ApplicationContextAware {

    private static Logger LOG = LoggerFactory.getLogger(DataProviderManager.class);

    private static Map<String, Class<? extends DataProvider>> providers = new HashMap<>();

    private static ApplicationContext applicationContext;

    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String SQLSERVER_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

    private static final String DM_DRIVER = "dm.jdbc.driver.DmDriver";
    /**
     * T2数据源的配置map
     */
    private static Map<String, String> t2DataSource;

    private static String t2DbType;
    private static String t2JdbcUrl;
    private static String t2JdbcUsername;
    private static String t2JdbcPassword;

    static {
        Set<Class<?>> classSet = new Reflections("com.bytter.olap").getTypesAnnotatedWith(ProviderName.class);
        for (Class c : classSet) {
            if (!c.isAssignableFrom(DataProvider.class)) {
                providers.put(((ProviderName) c.getAnnotation(ProviderName.class)).name(), c);
            } else {
                System.out.println("自定义DataProvider需要继承org.cboard.dataprovider.DataProvider");
            }
        }
    }

    public static Set<String> getProviderList() {
        return providers.keySet();
    }

    public static DataProvider getDataProvider(String type,
                                               Map<String, String> dataSource,
                                               Map<String, String> query) throws Exception {
        return getDataProvider(type, dataSource, query, false, null);
    }

    /**
     * 获取DataProvider
     * <p>
     * 扩展Freemarker语法的SQL语句
     * Created by chenlw on 2019/05/24.
     *
     * @param type       数据源类型
     * @param dataSource 数据源属性map
     * @param query      查询map
     * @param sqlParams  SQL查询参数map
     * @return DataProvider
     * @throws Exception 异常
     */
    public static DataProvider getDataProvider(String type,
                                               Map<String, String> dataSource,
                                               Map<String, String> query,
                                               Map<String, Object> sqlParams) throws Exception {
        return getDataProvider(type, dataSource, query, false, sqlParams);
    }

    /**
     * 获取DataProvider
     * <p>
     * 扩展Freemarker语法的SQL语句
     * Created by chenlw on 2019/05/24.
     *
     * @param type         数据源类型
     * @param dataSource   数据集
     * @param query        查询SQL
     * @param isUseForTest 是否用于测试
     * @param sqlParams    SQL参数值map
     * @return 数据提供者
     * @throws Exception 异常
     */
    public static DataProvider getDataProvider(String type,
                                               Map<String, String> dataSource,
                                               Map<String, String> query,
                                               boolean isUseForTest,
                                               Map<String, Object> sqlParams) throws Exception {
        Class c = providers.get(type);
        // 根据数据源类型选择DataProvider
        ProviderName providerName = (ProviderName) c.getAnnotation(ProviderName.class);
        if (providerName.name().equals(type)) {
            DataProvider provider = (DataProvider) c.newInstance();
            setDataProvider(provider, dataSource, query, isUseForTest, sqlParams);
            return provider;
        }
        return null;
    }

    /**
     * 获取JdbcDataProvider
     *
     * @param dataSource   数据集
     * @param query        查询SQL
     * @param isUseForTest 是否用于测试
     * @param sqlParams    SQL参数值map
     * @return 数据提供者
     * @throws Exception 异常
     */
    public static DataProvider getJdbcDataProvider(Map<String, String> dataSource,
                                                   Map<String, String> query,
                                                   boolean isUseForTest,
                                                   Map<String, Object> sqlParams) throws Exception {
        DataProvider provider = new JdbcDataProvider();
        setDataProvider(provider, dataSource, query, isUseForTest, sqlParams);
        return provider;
    }


    public static DataProvider getT2DataProvider(Map<String, String> query,
                                                 boolean isUseForTest,
                                                 Map<String, Object> sqlParams) throws Exception {
        DataProvider provider = new JdbcDataProvider();
        if (t2DataSource == null) {
            initT2Datasource();
        }
        setDataProvider(provider, t2DataSource, query, isUseForTest, sqlParams);
        return provider;
    }

    /**
     * 初始化T2数据源配置
     */
    private static void initT2Datasource() {
        // 构建dataSource需要4个要素：driver,jdbcurl,username,password
        t2DataSource = new HashMap<>();
        if ("oracle".equalsIgnoreCase(t2DbType)) {
            t2DataSource.put("driver", ORACLE_DRIVER);
        } else if ("mysql".equalsIgnoreCase(t2DbType)) {
            t2DataSource.put("driver", MYSQL_DRIVER);
        } else if ("sqlserver".equalsIgnoreCase(t2DbType)) {
            t2DataSource.put("driver", SQLSERVER_DRIVER);
        } else if ("dm".equalsIgnoreCase(t2DbType)) {
            t2DataSource.put("driver", DM_DRIVER);
        }
        t2DataSource.put("jdbcurl", t2JdbcUrl);
        t2DataSource.put("username", t2JdbcUsername);
        t2DataSource.put("password", t2JdbcPassword);
        // 是否使用线程池
        t2DataSource.put("pooled", "true");
        // 是否启用数据源聚合
        t2DataSource.put("aggregateProvider", "true");
    }


    /**
     * 设置DataProvider
     *
     * @param provider     DataProvider
     * @param dataSource   数据集
     * @param query        查询SQL
     * @param isUseForTest 是否用于测试
     * @param sqlParams    SQL参数值map
     * @throws Exception 异常
     */
    private static void setDataProvider(DataProvider provider,
                                        Map<String, String> dataSource,
                                        Map<String, String> query,
                                        boolean isUseForTest,
                                        Map<String, Object> sqlParams) throws Exception {
        provider.setQuery(query);
        provider.setDataSource(dataSource);
        provider.setUsedForTest(isUseForTest);
        provider.setFreemarkerSqlParams(sqlParams);
        if (provider instanceof Initializing) {
            ((Initializing) provider).afterPropertiesSet();
        }
        // 设置DataProvider内部的数据聚合器，这里用H2Aggregator
        // 如果数据源未启用数据源聚合选项，则会使用H2Aggregator去获取数据
        applicationContext.getAutowireCapableBeanFactory().autowireBean(provider);
        InnerAggregator innerAggregator = applicationContext.getBean(H2Aggregator.class);
        innerAggregator.setDataSource(dataSource);
        innerAggregator.setQuery(query);
        provider.setInnerAggregator(innerAggregator);
    }


    protected static Class<? extends DataProvider> getDataProviderClass(String type) {
        return providers.get(type);
    }

    /**
     * 查询数据源列表数据的时候会调用此方法
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param providerType 数据源类型
     * @param type         输入类型
     * @return 数据源类型列表
     * @throws IllegalAccessException 不合法访问异常
     * @throws InstantiationException 异常
     */
    public static List<String> getProviderFieldByType(String providerType, DatasourceParameter.Type type) throws IllegalAccessException, InstantiationException {
        Class clz = getDataProviderClass(providerType);
        Object o = clz.newInstance();
        Set<Field> fieldSet = ReflectionUtils.getAllFields(clz, ReflectionUtils.withAnnotation(DatasourceParameter.class));
        List<String> resultList = new ArrayList<>();
        Iterator<Field> iterator = fieldSet.iterator();
        Field field;
        while (iterator.hasNext()) {
            field = iterator.next();
            if (field.getAnnotation(DatasourceParameter.class).type().equals(type)) {
                field.setAccessible(true);
                resultList.add(field.get(o).toString());
            }
        }
        return resultList;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataProviderManager.applicationContext = applicationContext;
    }

    @Value("${jdbc.dbType}")
    public void setT2DbType(String t2DbType) {
        DataProviderManager.t2DbType = t2DbType;
    }

    @Value("${jdbc.url}")
    public void setT2JdbcUrl(String t2JdbcUrl) {
        DataProviderManager.t2JdbcUrl = t2JdbcUrl;
    }

    @Value("${jdbc.username}")
    public void setT2JdbcUsername(String t2JdbcUsername) {
        DataProviderManager.t2JdbcUsername = t2JdbcUsername;
    }

    @Value("${jdbc.password}")
    public void setT2JdbcPassword(String t2JdbcPassword) {
        DataProviderManager.t2JdbcPassword = t2JdbcPassword;
    }

    public static String getT2DbType() {
        return t2DbType;
    }
}
