package com.bytter.olap.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.cache.CacheManager;
import com.bytter.olap.cache.HeapCacheManager;
import com.bytter.olap.dataprovider.DataProvider;
import com.bytter.olap.dataprovider.DataProviderManager;
import com.bytter.olap.dataprovider.Initializing;
import com.bytter.olap.dataprovider.aggregator.Aggregatable;
import com.bytter.olap.dataprovider.annotation.DatasourceParameter;
import com.bytter.olap.dataprovider.annotation.ProviderName;
import com.bytter.olap.dataprovider.annotation.QueryParameter;
import com.bytter.olap.dataprovider.config.AggConfig;
import com.bytter.olap.dataprovider.result.AggregateResult;
import com.bytter.olap.dataprovider.util.DPCommonUtils;
import com.bytter.olap.dataprovider.util.SqlHelper;
import com.bytter.olap.exception.OlapException;
import com.bytter.olap.util.SqlBadWordUtil;
import com.bytter.olap.util.SqlUtils;
import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import com.google.common.hash.Hashing;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by yfyuan on 2016/8/17.
 */
@ProviderName(name = "jdbc")
public class JdbcDataProvider extends DataProvider implements Aggregatable, Initializing {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcDataProvider.class);

    /**
     * 当数据量过大时只加载设置的N条数据。
     */
    @Value("${dataprovider.datasetShowResultLimitCount:10000}")
    private int datasetShowResultLimitCount;

    /**
     * SQL查询数据量限制功能开关
     * 如果开启功能开关，当数据集行数超过设置的数目时，抛出异常，提示用户数据量过大，且不再执行下一步查询。
     */
    @Value("${dataprovider.resultLimit.switch}")
    private boolean resultLimitSwitch;
    @Value("${dataprovider.resultLimit.maximum:0}")
    private int resultLimitMaximum;

    /**
     * 数据列查询最大数目 created by chenlw on 2019/12/03
     */
    @Value("${dataprovider.columnShowResultLimitCount.maximum:1000}")
    private int columnShowResultLimitCount;


    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.DRIVER'|translate}} *",
            type = DatasourceParameter.Type.Input,
            required = true,
            order = 1)
    private String DRIVER = "driver";

    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.JDBCURL'|translate}} *",
            type = DatasourceParameter.Type.Input,
            required = true,
            order = 2)
    private String JDBC_URL = "jdbcurl";

    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.USERNAME'|translate}} *",
            type = DatasourceParameter.Type.Input,
            required = true,
            order = 3)
    private String USERNAME = "username";

    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.PASSWORD'|translate}}",
            type = DatasourceParameter.Type.Password,
            order = 4)
    private String PASSWORD = "password";

    /**
     * 是否使用连接池：true表示使用；false表示不使用
     */
    @DatasourceParameter(label = "{{'DATAPROVIDER.POOLEDCONNECTION'|translate}}", checked = true, type = DatasourceParameter.Type.Checkbox, order = 5)
    private String POOLED = "pooled";

    /**
     * 是否启用数据源聚合：true表示启用；false表示不启用
     */
    @DatasourceParameter(label = "{{'DATAPROVIDER.AGGREGATABLE_PROVIDER'|translate}}", type = DatasourceParameter.Type.Checkbox, order = 100)
    private String aggregateProvider = "aggregateProvider";

    @QueryParameter(label = "{{'DATAPROVIDER.JDBC.SQLTEXT'|translate}}",
            type = QueryParameter.Type.TextArea,
            required = true,
            order = 1)
    private String SQL = "sql";

    private static final CacheManager<Map<String, Integer>> typeCahce = new HeapCacheManager<>();

    /**
     * 支持并发访问的ConcurrentMap，存放数据源，实现连接池。
     */
    private static final ConcurrentMap<String, DataSource> datasourceMap = new ConcurrentHashMap<>();

    private SqlHelper sqlHelper;


    /**
     * 是否启用数据源聚合功能
     *
     * @return 是否启用数据源聚合功能
     */
    @Override
    public boolean doAggregationInDataSource() {
        String v = dataSource.get(aggregateProvider);
        return v != null && "true".equals(v);
    }


    /**
     * 执行SQL，查询数据
     *
     * 测试SQL执行、未启用数据源聚合，这两种情况都会调用getData()方法来获取数据。
     *
     * @return SQL查询出来的数据
     * @throws Exception 异常
     */
    @Override
    public String[][] getData() throws Exception {
        final int batchSize = 100000;
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOG.debug("Execute JdbcDataProvider.getData() Start!");
        String sql = getAsSubQuery(query.get(SQL));

        // 解析SQL语句里面的具体参数值
        sql = SqlUtils.resolveFreemarkerSql(sql, freemarkerSqlParams);

        List<String[]> list = null;
        LOG.info("SQL String: " + sql);

        try (Connection con = getConnection();
             Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            list = new LinkedList<>();
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = metaData.getColumnLabel(i + 1);
            }

            final String[] header = row;
            getInnerAggregator().beforeLoad(header);

            int resultCount = 0;
            ExecutorService executor = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(5));
            try {
                while (rs.next()) {
                    resultCount++;
                    row = new String[columnCount];
                    for (int j = 0; j < columnCount; j++) {
                        row[j] = rs.getString(j + 1);
                    }
                    list.add(row);

                    if (resultCount % batchSize == 0) {
                        LOG.info("JDBC load batch {}", resultCount);
                        final String[][] batchData = list.toArray(new String[][]{});

                        executor.execute(() -> getInnerAggregator().loadBatch(header, batchData));
                        list.clear();
                    }
                    if (resultCount > datasetShowResultLimitCount) {
                        // 数据量已经超过限制
                        throw new OlapException("Cube result count " + resultCount + ", is greater than limit " + datasetShowResultLimitCount);
                    }
                }
                // 有界等待已提交批次全部完成，再同步加载最后一批（不足 batchSize 的部分），保持批次顺序
                shutdownAndAwaitTermination(executor);
                final String[][] batchData = list.toArray(new String[][]{});
                getInnerAggregator().loadBatch(header, batchData);
            } finally {
                // 异常/中断路径：取消未开始任务，有界等待运行中任务结束，避免后台任务继续修改 aggregator
                shutdownAndAwaitTermination(executor);
            }
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        getInnerAggregator().afterLoad();
        stopwatch.stop();
        LOG.info("getData() using time: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return null;
    }

    /**
     * 优雅关闭加载执行器：先停止接收新任务，有界等待已提交任务完成；
     * 超时则强停（取消未开始任务并中断运行中任务），并确认线程真正终止。
     */
    private void shutdownAndAwaitTermination(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    LOG.error("JDBC load executor did not terminate in time");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 测试SQL
     *
     * @throws Exception 异常
     */
    @Override
    public void test() throws Exception {
        String queryStr = query.get(SQL);

        //解析SQL语句里面的具体参数值
        queryStr = SqlUtils.resolveFreemarkerSql(queryStr, freemarkerSqlParams);

        LOG.info("Execute query: {}", queryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            ps.executeQuery(queryStr);
        } catch (Exception e) {
            LOG.error("Error when execute: {}", queryStr);
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
    }


    /**
     * Convert the sql text to subquery string:
     * remove blank line
     * remove end semicolon ;
     *
     * @param rawQueryText 查询类型
     * @return
     */
    private String getAsSubQuery(String rawQueryText) {
        String deletedBlankLine = rawQueryText.replaceAll("(?m)^[\\s\t]*\r?\n", "").trim();
        return deletedBlankLine.endsWith(";") ? deletedBlankLine.substring(0, deletedBlankLine.length() - 1) : deletedBlankLine;
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象
     * @throws Exception 获取连接异常
     */
    private Connection getConnection() throws Exception {
        String usePool = dataSource.get(POOLED);
        String username = dataSource.get(USERNAME);
        String password = dataSource.get(PASSWORD);
        Connection conn = null;
        // 是否使用连接池
        if (usePool != null && "true".equals(usePool)) {
            String key = Hashing.md5().newHasher().putString(JsonUtils.toJSONString(dataSource), Charsets.UTF_8).hash().toString();
            DataSource ds = datasourceMap.get(key);
            if (ds == null) {
                synchronized (key.intern()) {
                    ds = datasourceMap.get(key);
                    if (ds == null) {
                        Map<String, String> conf = new HashedMap();
                        conf.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, dataSource.get(DRIVER));
                        conf.put(DruidDataSourceFactory.PROP_URL, dataSource.get(JDBC_URL));
                        conf.put(DruidDataSourceFactory.PROP_USERNAME, dataSource.get(USERNAME));
                        if (StringUtils.isNotBlank(password)) {
                            conf.put(DruidDataSourceFactory.PROP_PASSWORD, dataSource.get(PASSWORD));
                        }
                        conf.put(DruidDataSourceFactory.PROP_INITIALSIZE, "3");
                        DruidDataSource druidDS = (DruidDataSource) DruidDataSourceFactory.createDataSource(conf);
                        druidDS.setBreakAfterAcquireFailure(true);
                        druidDS.setConnectionErrorRetryAttempts(5);
                        datasourceMap.put(key, druidDS);
                        ds = datasourceMap.get(key);
                    }
                }
            }
            try {
                conn = ds.getConnection();
            } catch (SQLException e) {
                datasourceMap.remove(key);
                throw e;
            }
            return conn;
        } else {
            String driver = dataSource.get(DRIVER);
            String jdbcurl = dataSource.get(JDBC_URL);

            Class.forName(driver);
            Properties props = new Properties();
            props.setProperty("user", username);
            if (StringUtils.isNotBlank(password)) {
                props.setProperty("password", password);
            }
            return DriverManager.getConnection(jdbcurl, props);
        }
    }

    @Override
    public String[] queryDimVals(String columnName, AggConfig config) throws Exception {
        String fsql = null;
        String exec = null;
        String sql = getAsSubQuery(query.get(SQL));
        List<String> filtered = new ArrayList<>();
        String whereStr = "";
        if (config != null) {
            whereStr = sqlHelper.assembleFilterSql(config);
        }

        //解析SQL语句里面的具体参数值
        sql = SqlUtils.resolveFreemarkerSql(sql, freemarkerSqlParams);

        // 检测SQL语句敏感词
        checkSqlBadWorld(sql);

        fsql = "SELECT cb_view.%s FROM (\n%s\n) cb_view %s GROUP BY cb_view.%s";
        exec = String.format(fsql, columnName, sql, whereStr, columnName);

        // 2019-12-03，限制数据列的查询数量，最多加载设置的N条数据
        exec = SqlUtils.getLimitDatasetCountSql(exec, DataProviderManager.getT2DbType(), columnShowResultLimitCount);

        LOG.info(exec);
        try (Connection connection = getConnection();
             Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(exec)) {
            Object strTemp;
            while (rs.next()) {
                strTemp = rs.getObject(1);
                if (strTemp != null && StringUtils.isNotBlank(String.valueOf(strTemp))) {
                    filtered.add(String.valueOf(strTemp));
                }
            }
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        Set<String> resultSet = new TreeSet<>(filtered);
        return resultSet.toArray(new String[]{});
    }


    private ResultSetMetaData getMetaData(String subQuerySql, Statement stat) throws Exception {
        ResultSetMetaData metaData;
        try {
            stat.setMaxRows(100);
            String fsql = "\nSELECT * FROM (\n%s\n) cb_view WHERE 1=0";
            String sql = String.format(fsql, subQuerySql);

            sql = SqlUtils.resolveFreemarkerSql(sql, freemarkerSqlParams);

            LOG.info(sql);
            ResultSet rs = stat.executeQuery(sql);
            metaData = rs.getMetaData();
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return metaData;
    }

    /**
     * 获取列维类型
     *
     * @return map
     * @throws Exception 异常
     */
    private Map<String, Integer> getColumnType() throws Exception {
        Map<String, Integer> result;
        String key = getLockKey();
        result = typeCahce.get(key);
        if (result != null) {
            return result;
        } else {
            try (
                    Connection connection = getConnection();
                    Statement stat = connection.createStatement()
            ) {
                String subQuerySql = getAsSubQuery(query.get(SQL));
                ResultSetMetaData metaData = getMetaData(subQuerySql, stat);
                int columnCount = metaData.getColumnCount();
                result = new HashedMap();
                for (int i = 0; i < columnCount; i++) {
                    result.put(metaData.getColumnLabel(i + 1).toUpperCase(), metaData.getColumnType(i + 1));
                }
                typeCahce.put(key, result, 12 * 60 * 60 * 1000);
                return result;
            }
        }
    }

    /**
     * 获取列维
     *
     * @return 列维数组
     * @throws Exception 异常
     */
    @Override
    public String[] getColumn() throws Exception {
        String subQuerySql = getAsSubQuery(query.get(SQL));

        //解析SQL语句里面的具体参数值
        subQuerySql = SqlUtils.resolveFreemarkerSql(subQuerySql, freemarkerSqlParams);

        // 检测SQL语句敏感词
        checkSqlBadWorld(subQuerySql);

        // SQL语句查询数据量检查
        checkSqlResultLimit(subQuerySql);

        try (
                Connection connection = getConnection();
                Statement stat = connection.createStatement()
        ) {
            ResultSetMetaData metaData = getMetaData(subQuerySql, stat);
            int columnCount = metaData.getColumnCount();
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = metaData.getColumnLabel(i + 1);
            }
            return row;
        }
    }

    /**
     * SQL语句查询数据量检查
     *
     * @param sql SQL
     * @throws Exception 如果SQL语句查询的数据量超过限制，则抛出异常
     */
    private void checkSqlResultLimit(String sql) throws Exception {
        if (resultLimitSwitch) {
            String countSql = SqlUtils.prepareCountSql(sql);
            long sqlExecuteTime = System.currentTimeMillis();
            LOG.info("Count SQL：\n{}", countSql);
            try (Connection con = getConnection();
                 Statement ps = con.createStatement();
                 ResultSet rs = ps.executeQuery(countSql)) {
                LOG.info("COUNT QUERY SQL执行所耗费时间: {} ms", (System.currentTimeMillis() - sqlExecuteTime));
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > resultLimitMaximum) {
                        LOG.info("SQL语句查询数据量已经超过限制！SQL:{}", sql);
                        throw new Exception("读取数据失败！失败原因：SQL语句查询数据量已经超过限制！（为保证资金系统运行的安全和稳定，程序对SQL查询数据量进行了限制。）");
                    }
                }
            } catch (Exception e) {
                LOG.error("SQL COUNT QUERY ERROR: " + e.getMessage());
                throw new Exception("SQL COUNT QUERY ERROR: " + e.getMessage(), e);
            }
        }
    }

    /**
     * SQL语句查询数据量检查（避免聚合SQL）
     *
     * @param config 聚合配置类
     * @throws Exception
     */
    private void checkSqlResultLimit(AggConfig config) throws Exception {
        String sql = sqlHelper.assembleCountQuerySql(config);
        checkSqlResultLimit(sql);
    }




    /**
     * 检查SQL语句是否含有敏感词
     *
     * @param config 聚合配置类
     * @throws Exception 若SQL脚本有敏感词，则抛出异常
     */
    private void checkSqlBadWorld(AggConfig config) throws Exception {
        String sql = sqlHelper.assembleCountQuerySql(config);
        checkSqlBadWorld(sql);
    }

    /**
     * 检查SQL语句是否含有敏感词
     *
     * @param sql SQL脚本
     * @throws Exception 若SQL脚本有敏感词，则抛出异常
     */
    private void checkSqlBadWorld(String sql) throws Exception {
        LOG.info("检查SQL语句是否含有敏感词，SQL: {}", sql);
        Set<String> badWorldSet = SqlBadWordUtil.checkSql(sql);
        if (badWorldSet != null && badWorldSet.size() > 0) {
            String tip = String.format("警告！！！系统检测到本次执行的SQL脚本包含有危险操作的敏感词，SQL的执行已被取消。请剔除敏感词后再执行查询操作！ SQL脚本含有的敏感词集合：%s"
                    , badWorldSet.toString());
            LOG.info("SQL语句包含操作敏感词！敏感词集合: {}", badWorldSet.toString());
            throw new Exception(tip);
        }
    }




    /**
     * 获取聚合数据
     *
     * @param config 聚合配置类
     * @return 聚合数据结果
     * @throws Exception 异常
     */
    @Override
    public AggregateResult queryAggData(AggConfig config) throws Exception {
        // 组装查询聚合数据的SQL
        String exec = sqlHelper.assembleAggDataSql(config);

        // created by chenlw on 2019/12/03  限制最大查询数据量，若超过则显示前N条
        exec = SqlUtils.getLimitDatasetCountSql(exec, DataProviderManager.getT2DbType(), datasetShowResultLimitCount);

        // 解析SQL语句里面的具体参数值
        exec = SqlUtils.resolveFreemarkerSql(exec, freemarkerSqlParams);

        // 检测SQL语句敏感词
        checkSqlBadWorld(config);

        // SQL语句查询数据量检查
        checkSqlResultLimit(config);


        List<String[]> list = new LinkedList<>();
        long sqlExecuteTime = System.currentTimeMillis();
        LOG.info(exec);
        try (
                Connection connection = getConnection();
                Statement stat = connection.createStatement();
                ResultSet rs = stat.executeQuery(exec)
        ) {
            LOG.info("SQL执行所耗费时间: {} ms", (System.currentTimeMillis() - sqlExecuteTime));
            long programExecuteTime = System.currentTimeMillis();
            // 优化程序处理性能，当数据量大，setFetchSize可以提升程序处理的效率
            rs.setFetchSize(2000);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Object valueTemp;
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    valueTemp = rs.getObject(j + 1);
                    if (valueTemp != null) {
                        row[j] = valueTemp.toString();
                    } else {
                        row[j] = NULL_STRING;
                    }
                }
                list.add(row);
            }
            LOG.info("后台程序处理数据所耗费时间: {} ms", (System.currentTimeMillis() - programExecuteTime));
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return DPCommonUtils.transform2AggResult(config, list);
    }


    @Override
    public String viewAggDataQuery(AggConfig config) throws Exception {
        return sqlHelper.assembleAggDataSql(config);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String subQuery = null;
        if (query != null) {
            subQuery = getAsSubQuery(query.get(SQL));
            //解析SQL语句里面的具体参数值
            subQuery = SqlUtils.resolveFreemarkerSql(subQuery, freemarkerSqlParams);
        }

        SqlHelper sqlHelper = new SqlHelper(subQuery, true);
        if (!isUsedForTest()) {
            Map<String, Integer> columnTypes = null;
            try {
                columnTypes = getColumnType();
            } catch (Exception e) {
                //  getColumns() and test() methods do not need columnTypes properties,
                // it doesn't matter for these methods even getMeta failed
                LOG.warn("getColumnType failed: {}", e.getMessage());
            }
            sqlHelper.getSqlSyntaxHelper().setColumnTypes(columnTypes);
        }
        this.sqlHelper = sqlHelper;
    }
}
