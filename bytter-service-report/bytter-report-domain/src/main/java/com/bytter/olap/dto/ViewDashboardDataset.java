package com.bytter.olap.dto;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.pojo.DashboardDataset;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Map;

public class ViewDashboardDataset {
    private String id;
    private String name;
    private String categoryName;
    private String createTime;
    private String updateTime;
    private Map<String, Object> data;


    public static final Function TO = new Function<DashboardDataset, ViewDashboardDataset>() {
        @Nullable
        @Override
        public ViewDashboardDataset apply(@Nullable DashboardDataset input) {
            return new ViewDashboardDataset(input);
        }
    };

    public ViewDashboardDataset(){}

    public ViewDashboardDataset(DashboardDataset dataset) {
        this.id = dataset.getId();
        this.name = dataset.getName();
        this.categoryName = dataset.getCategoryName();
        if(dataset.getCreateTime() != null) {
            this.createTime = dataset.getCreateTime().toString();
            this.updateTime = dataset.getUpdateTime().toString();
        }
        this.data = JsonUtils.json2Map(dataset.getData());
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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
