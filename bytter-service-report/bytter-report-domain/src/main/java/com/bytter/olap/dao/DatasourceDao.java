package com.bytter.olap.dao;

import com.bytter.olap.pojo.DashboardDatasource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/15.
 */
@Repository
public interface DatasourceDao {

    /**
     * 获取数据源信息列表
     *
     * @return 数据源信息列表
     */
    List<DashboardDatasource> getDatasourceList();

    /**
     * 获取数据源信息
     * @param datasourceId  主键ID
     * @return
     */
    DashboardDatasource getDatasource(String datasourceId);

    /**
     * 保存数据源
     * @param dashboardDatasource 数据源实体对象
     * @return 保存成功数量
     */
    int save(DashboardDatasource dashboardDatasource);

    /**
     * 获取数据源数量
     *
     * @param map 参数
     * @return 数量
     */
    long countExistDatasourceName(Map<String, Object> map);

    /**
     * 更新数据源
     *
     * @param dashboardDatasource 数据源实体对象
     * @return 更新数量
     */
    int update(DashboardDatasource dashboardDatasource);

    /**
     * 删除数据源
     *
     * @param id 数据源ID
     * @return 删除数量
     */
    int delete(String id);

}
