package com.bytter.olap.dto;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.pojo.DashboardDatasource;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 数据源前端展示实体类
 */
public class ViewDashboardDatasource {

    private String id;
    private String name;
    private String type;
    private Map<String, Object> config;
    private String createTime;
    private String updateTime;

    /**
     * 实体类转化方法
     */
    public static final Function TO = new Function<DashboardDatasource, ViewDashboardDatasource>() {
        @Nullable
        @Override
        public ViewDashboardDatasource apply(@Nullable DashboardDatasource input) {
            return new ViewDashboardDatasource(input);
        }
    };

    /**
     * VO对象作为参数接收实体对象时，需要有默认的（无参数的）构造方法
     */
    public ViewDashboardDatasource() {
    }

    public ViewDashboardDatasource(DashboardDatasource datasource) {
        this.id = datasource.getId();
        this.name = datasource.getName();
        this.type = datasource.getType();
        // this.config = JSONObject.parseObject(datasource.getConfig());
        this.config = JsonUtils.json2Map(datasource.getConfig());
        if(datasource.getCreateTime() != null) {
            this.createTime = datasource.getCreateTime().toString();
            this.updateTime = datasource.getUpdateTime().toString();
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
