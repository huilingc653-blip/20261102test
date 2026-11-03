package com.bytter.olap.dataprovider.aggregator;

import com.bytter.core.utils.JsonUtils;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.util.Map;

/**
 * 内部数据聚合器
 * Created by zyong on 2017/1/9.
 */
public abstract class InnerAggregator implements Aggregatable {

    protected Map<String, String> dataSource;
    protected Map<String, String> query;

//    /**
//     * 离线数据源数据会加载至服务端缓存，即把离线数据存储在rawDataCache
//     */
//    @Autowired()
//    @Qualifier("rawDataCache")
//    protected CacheManager<String[][]> rawDataCache;

    public InnerAggregator() {}

    public abstract void loadData(String[][] data, long interval);

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    /**
     * 根据数据源和查询SQL生成  缓存key
     *
     * @return 缓存key
     */
    protected String getCacheKey() {
//        return Hashing.md5().newHasher().putString(
//                JSONObject.toJSON(dataSource).toString() + JSONObject.toJSON(query).toString(),
//                Charsets.UTF_8).hash().toString();
        return Hashing.md5().newHasher().putString(
                JsonUtils.toJSONString(dataSource) + JsonUtils.toJSONString(query),
                Charsets.UTF_8).hash().toString();
    }

//    /**
//     * 判断缓存中已经存在离线数据，如果存在离线数据，则无须从数据库查询数据。
//     *
//     * @return 缓存中已经存在离线数据
//     */
//    public boolean checkExist() {
//        return rawDataCache.get(getCacheKey()) != null;
//    }

    /**
     * 判断缓存中已经存在离线数据，如果存在离线数据，则无须从数据库查询数据。
     *
     * @return 缓存中已经存在离线数据
     */
    public boolean checkExist() {
        return false;
    }


    public void cleanExist() {
        // rawDataCache.remove(getCacheKey());
    }

    public void beforeLoad(String[] header) {}
    public void loadBatch(String[] header, String[][] data) {}
    public void afterLoad(){}
}
