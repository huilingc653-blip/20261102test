package com.bytter.olap.dao;

import com.bytter.olap.pojo.DashboardDataset;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyuan on 2016/10/11.
 */
@Repository
public interface DatasetDao {

    List<String> getCategoryList();

    List<DashboardDataset> getAllDatasetList();

    int save(DashboardDataset dataset);

    long countExistDatasetName(Map<String, Object> map);

    int update(DashboardDataset dataset);

    int delete(String id);

    DashboardDataset getDataset(String id);

}
