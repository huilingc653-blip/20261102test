package com.bytter.olap.dto;

import com.bytter.olap.dataprovider.config.AggConfig;
import com.bytter.olap.dataprovider.config.ConfigComponent;
import com.bytter.olap.dataprovider.config.DimensionConfig;
import com.bytter.olap.dataprovider.config.ValueConfig;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端页面图标配置的VO对象
 */
public class ViewAggConfig {

    /**
     * 行维
     */
    private List<DimensionConfig> rows;

    /**
     * 列维
     */
    private List<DimensionConfig> columns;

    /**
     * 过滤器
     */
    private List<DimensionConfig> filters;

    /**
     * 过滤器的数值列表
     */
    private List<ValueConfig> values;

//    public static AggConfig getAggConfig(ViewAggConfig viewAggConfig) {
//        if (viewAggConfig != null) {
//            AggConfig aggConfig = new AggConfig();
//            aggConfig.setRows(viewAggConfig.getRows());
//            aggConfig.setColumns(viewAggConfig.getColumns());
//            if (viewAggConfig.getFilters() != null) {
//                aggConfig.setFilters(viewAggConfig.getFilters().stream().map(e -> (ConfigComponent) e).collect(Collectors.toList()));
//            } else {
//                aggConfig.setFilters(new ArrayList<>());
//            }
//            aggConfig.setValues(viewAggConfig.getValues());
//            return aggConfig;
//        }
//        return null;
//    }


    /**
     * 将前端图标填充的元素模型配置转化成AggConfig对象
     * Created by chenlw on 2019/5/13.（去除JAVA8特性）
     *
     * @param viewAggConfig 前端图标填充的元素模型配置
     * @return AggConfig对象
     */
    public static AggConfig getAggConfig(ViewAggConfig viewAggConfig) {
        if (viewAggConfig != null) {
            AggConfig aggConfig = new AggConfig();
            aggConfig.setRows(viewAggConfig.getRows());
            aggConfig.setColumns(viewAggConfig.getColumns());
            if (viewAggConfig.getFilters() != null) {
                List<ConfigComponent> filterList = new ArrayList<>();
                List<DimensionConfig> dimensionConfigList = viewAggConfig.getFilters();
                if (!CollectionUtils.isEmpty(dimensionConfigList)) {
                    filterList.addAll(dimensionConfigList);
                }
                aggConfig.setFilters(filterList);
            } else {
                aggConfig.setFilters(new ArrayList<ConfigComponent>());
            }
            aggConfig.setValues(viewAggConfig.getValues());
            return aggConfig;
        }
        return null;
    }


    public List<DimensionConfig> getRows() {
        return rows;
    }

    public void setRows(List<DimensionConfig> rows) {
        this.rows = rows;
    }

    public List<DimensionConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<DimensionConfig> columns) {
        this.columns = columns;
    }

    public List<DimensionConfig> getFilters() {
        return filters;
    }

    public void setFilters(List<DimensionConfig> filters) {
        this.filters = filters;
    }

    public List<ValueConfig> getValues() {
        return values;
    }

    public void setValues(List<ValueConfig> values) {
        this.values = values;
    }
}
