package com.bytter.olap.services;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.dao.DatasetDao;
import com.bytter.olap.dataprovider.DataProvider;
import com.bytter.olap.dataprovider.DataProviderManager;
import com.bytter.olap.dataprovider.config.AggConfig;
import com.bytter.olap.dataprovider.config.DimensionConfig;
import com.bytter.olap.dataprovider.result.AggregateResult;
import com.bytter.olap.dto.DataProviderResult;
import com.bytter.olap.exception.OlapException;
import com.bytter.olap.pojo.DashboardDataset;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/15.
 */
@Repository
public class DataProviderService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatasetDao datasetDao;


    /**
     * 获取T2数据源的DataProvider
     *
     * @param query     查询map
     * @param dataset   数据集对象
     * @param sqlParams SQL查询参数map
     * @return DataProvider
     * @throws Exception 异常
     */
    private DataProvider getT2DataProvider(Map<String, String> query,
                                           Dataset dataset,
                                           Map<String, Object> sqlParams) throws Exception {
        if (dataset != null) {
            query = dataset.getQuery();
        }
        DataProvider dataProvider = DataProviderManager.getT2DataProvider(query, false, sqlParams);
        if (dataset != null && dataset.getInterval() != null && dataset.getInterval() > 0) {
            dataProvider.setInterval(dataset.getInterval());
        }
        return dataProvider;
    }

    /**
     * 获取聚合数据
     * 扩展Freemarker语法的SQL语句
     * Created by chenlw on 2019/05/24.
     *
     * @param datasourceId 数据源ID
     * @param query        查询map
     * @param datasetId    数据集ID
     * @param config       图表聚合配置对象
     * @param reload       是否重新加载
     * @param sqlParams    SQL查询参数map
     * @return AggregateResult
     */
    public AggregateResult queryAggData(String datasourceId,
                                        Map<String, String> query,
                                        String datasetId,
                                        AggConfig config,
                                        boolean reload,
                                        Map<String, Object> sqlParams) {
        try {
            Dataset dataset = getDataset(datasetId);
            attachCustom(dataset, config);
            DataProvider dataProvider = getT2DataProvider(query, dataset, sqlParams);
            return dataProvider.getAggData(config, reload);
        } catch (Exception e) {
            LOG.error("", e);
            throw new OlapException(e.getMessage());
        }
    }


    public DataProviderResult getColumns(String datasourceId,
                                         Map<String, String> query,
                                         String datasetId,
                                         boolean reload) {
        DataProviderResult dps = new DataProviderResult();
        try {
            Dataset dataset = getDataset(datasetId);
            DataProvider dataProvider = getT2DataProvider(query, dataset, null);
            String[] result = dataProvider.getColumn(reload);
            dps.setColumns(result);
            dps.setMsg("1");
        } catch (Exception e) {
            LOG.error("", e);
            dps.setMsg(e.getMessage());
        }
        return dps;
    }


    /**
     * 扩展Freemarker语法的SQL语句
     *
     * @param datasourceId 数据源ID
     * @param query        查询map
     * @param datasetId    数据集ID
     * @param reload       是否重新加载
     * @param sqlParams    SQL查询参数map
     * @return DataProviderResult
     */
    public DataProviderResult getColumns(String datasourceId,
                                         Map<String, String> query,
                                         String datasetId,
                                         boolean reload,
                                         Map<String, Object> sqlParams) {
        DataProviderResult dps = new DataProviderResult();
        try {
            Dataset dataset = getDataset(datasetId);
            DataProvider dataProvider = getT2DataProvider(query, dataset, sqlParams);
            String[] result = dataProvider.getColumn(reload);
            dps.setColumns(result);
            dps.setMsg("1");
        } catch (Exception e) {
            LOG.error("", e);
            dps.setMsg(e.getMessage());
        }
        return dps;
    }



    /**
     * 获取维度列表值
     *
     * @param datasourceId 数据源ID
     * @param query        查询map
     * @param datasetId    数据集ID
     * @param columnName   列名
     * @param config       聚合配置
     * @param reload       是否重新加载
     * @param sqlParams    SQL参数
     * @return 维度列表值
     */
    public String[] getDimensionValues(String datasourceId,
                                       Map<String, String> query,
                                       String datasetId,
                                       String columnName,
                                       AggConfig config,
                                       boolean reload,
                                       Map<String, Object> sqlParams) {
        try {
            Dataset dataset = getDataset(datasetId);
            attachCustom(dataset, config);
            DataProvider dataProvider = getT2DataProvider(query, dataset, sqlParams);
            String[] result = dataProvider.getDimVals(columnName, config, reload);
            return result;
        } catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }


    /**
     * 获取聚合数据的SQL查询语句
     *
     * @param datasourceId 数据源ID
     * @param query        查询map
     * @param datasetId    数据集ID
     * @param config       聚合配置
     * @return 聚合数据的SQL查询语句
     */
    public String viewAggDataQuery(String datasourceId, Map<String, String> query, String datasetId, AggConfig config) {
        try {
            Dataset dataset = getDataset(datasetId);
            attachCustom(dataset, config);
            DataProvider dataProvider = getT2DataProvider(query, dataset, null);
            return dataProvider.getViewAggDataQuery(config);
        } catch (Exception e) {
            LOG.error("", e);
            throw new OlapException(e.getMessage());
        }
    }

//    /**
//     * 测试数据源
//     *
//     * @param dataSource 数据源对象
//     * @param query      查询map
//     * @return 状态
//     */
//    public ServiceStatus test(JSONObject dataSource, Map<String, String> query) {
//        try {
//            DataProvider dataProvider = DataProviderManager.getDataProvider(
//                    dataSource.getString("type"),
//                    Maps.transformValues(dataSource.getJSONObject("config"), Functions.toStringFunction()),
//                    query, true, null);
//            if (dataProvider != null) {
//                dataProvider.test();
//            }
//            return new ServiceStatus(ServiceStatus.Status.Success, null);
//        } catch (Exception e) {
//            LOG.error("", e);
//            return new ServiceStatus(ServiceStatus.Status.Fail, e.getMessage());
//        }
//    }

//    /**
//     * 扩展Freemarker语法的SQL语句
//     *
//     * @param dataSource 数据源对象
//     * @param query      查询map
//     * @param sqlParams  SQL查询参数map
//     * @return ServiceStatus
//     */
//    public ServiceStatus test(JSONObject dataSource, Map<String, String> query, Map<String, Object> sqlParams) {
//        try {
//            DataProvider dataProvider = DataProviderManager.getDataProvider(
//                    dataSource.getString("type"),
//                    Maps.transformValues(dataSource.getJSONObject("config"), Functions.toStringFunction()),
//                    query, true, sqlParams);
//            dataProvider.test();
//            return new ServiceStatus(ServiceStatus.Status.Success, null);
//        } catch (Exception e) {
//            LOG.error("", e);
//            return new ServiceStatus(ServiceStatus.Status.Fail, e.getMessage());
//        }
//    }


    public boolean isDataSourceAggInstance(String datasourceId, Map<String, String> query, String datasetId) {
        try {
            Dataset dataset = getDataset(datasetId);
            // DataProvider dataProvider = getDataProvider(datasourceId, query, dataset);
            DataProvider dataProvider = getT2DataProvider(query, dataset, null);
            return dataProvider.isDataSourceAggInstance();
        } catch (Exception e) {
            LOG.error("", e);
            throw new OlapException(e.getMessage());
        }
    }

    /**
     * 判断数据源是否启用了数据源聚合选项
     *
     * @param datasourceId 数据源ID
     * @param query        查询map
     * @param datasetId    数据集ID
     * @param sqlParams    sql参数map
     * @return 是否启用了数据源聚合选项
     */
    public boolean isDataSourceAggInstance(String datasourceId, Map<String, String> query, String datasetId, Map<String, Object> sqlParams) {
        try {
            Dataset dataset = getDataset(datasetId);
            DataProvider dataProvider = getT2DataProvider(query, dataset, sqlParams);
            return dataProvider.isDataSourceAggInstance();
        } catch (Exception e) {
            LOG.error("", e);
            throw new OlapException(e.getMessage());
        }
    }

    /**
     * 给图表聚合配置对象加入数据集的配置
     *
     * @param dataset   数据集对象
     * @param aggConfig 图表聚合配置对象
     */
    private void attachCustom(Dataset dataset, AggConfig aggConfig) {
        if (dataset == null || aggConfig == null) {
            return;
        }
        List<DimensionConfig> dimensionConfigList = new ArrayList<>();
        // 获取行维
        dimensionConfigList.addAll(aggConfig.getRows());
        // 获取列维
        dimensionConfigList.addAll(aggConfig.getColumns());
//        for (DimensionConfig config : dimensionConfigList) {
//            if (StringUtils.isNotEmpty(config.getId())) {
//                String custom = (String) JSONPath.eval(dataset.getSchema(), "$.dimension[id='" + config.getId() + "'][0].custom");
//                if (custom == null) {
//                    custom = (String) JSONPath.eval(dataset.getSchema(), "$.dimension[type='level'].columns[id='" + config.getId() + "'][0].custom");
//                }
//                config.setCustom(custom);
//            }
//        }
    }


    protected Dataset getDataset(String datasetId) {
        if (datasetId == null) {
            return null;
        }
        return new Dataset(datasetDao.getDataset(datasetId));
    }

    /**
     * 数据集
     */
    protected class Dataset {
        private String datasourceId;
        private Map<String, String> query;
        private Long interval;
        // private JSONObject schema;
        private JsonNode schema;

        public Dataset(DashboardDataset dataset) {
            JsonNode jsonNode = JsonUtils.getJsonNodeWithUtf8(dataset.getData());

            JsonNode queryNode = jsonNode.get("query");
            JsonNode datasourceNode = jsonNode.get("datasource");
            JsonNode intervalNode = jsonNode.get("interval");

            this.query = JsonUtils.json2Map(JsonUtils.toJSONString(queryNode));
            this.datasourceId = datasourceNode != null ? datasourceNode.textValue() : null;
            this.interval = intervalNode != null ? intervalNode.asLong() : null;
            this.schema = jsonNode.get("schema");
        }

        public JsonNode getSchema() {
            return schema;
        }

        public void setSchema(JsonNode schema) {
            this.schema = schema;
        }

        public String getDatasourceId() {
            return datasourceId;
        }

        public void setDatasourceId(String datasourceId) {
            this.datasourceId = datasourceId;
        }

        public Map<String, String> getQuery() {
            return query;
        }

        public void setQuery(Map<String, String> query) {
            this.query = query;
        }

        public Long getInterval() {
            return interval;
        }

        public void setInterval(Long interval) {
            this.interval = interval;
        }
    }
}
