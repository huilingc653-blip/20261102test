package com.bytter.olap.dto;

import com.bytter.core.utils.JsonUtils;
import com.bytter.olap.pojo.DashboardWidget;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

public class ViewDashboardWidget implements Serializable {

    private String id;

    private String name;
    private String categoryName;

    private String createTime;
    private String updateTime;
    private Map<String, Object> data;

    public static final Function TO = new Function<DashboardWidget, ViewDashboardWidget>() {
        @Nullable
        @Override
        public ViewDashboardWidget apply(@Nullable DashboardWidget input) {
            return new ViewDashboardWidget(input);
        }
    };

    public ViewDashboardWidget() {

    }

    public ViewDashboardWidget(DashboardWidget widget) {
        this.id = widget.getId();
        this.name = widget.getName();
        this.categoryName = widget.getCategoryName();
        if(widget.getCreateTime() != null) {
            this.createTime = widget.getCreateTime().toString();
            this.updateTime = widget.getUpdateTime().toString();
        }
        // this.data = JSONObject.parseObject(widget.getData());
        this.data = JsonUtils.json2Map(widget.getData());
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
