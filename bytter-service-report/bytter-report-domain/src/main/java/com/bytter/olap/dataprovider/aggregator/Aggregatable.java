package com.bytter.olap.dataprovider.aggregator;

import com.bytter.olap.dataprovider.config.AggConfig;
import com.bytter.olap.dataprovider.result.AggregateResult;

/**
 * 数据源聚合接口
 * Created by yfyuan on 2017/1/13.
 */
public interface Aggregatable {

    /**
     * 支持DataSource端聚合的数据提供程序必须实现此方法。
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @param columnName
     * @return
     */
    String[] queryDimVals(String columnName, AggConfig config) throws Exception;

    /**
     * 支持DataSource端聚合的数据提供程序必须实现此方法。
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @return
     */
    String[] getColumn() throws Exception;

    /**
     * 支持DataSource端聚合的数据提供程序必须实现此方法。
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @param ac aggregate configuration
     * @return 图表数据封装对象
     */
    AggregateResult queryAggData(AggConfig ac) throws Exception;

    /**
     * 查看数据查询的SQL
     *
     * @param ac 图表聚合配置对象
     * @return 数据查询的SQL
     * @throws Exception 异常
     */
    String viewAggDataQuery(AggConfig ac) throws Exception;

}
