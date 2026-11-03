package com.bytter.olap.services.persist;

import com.fasterxml.jackson.databind.JsonNode;


public class PersistContext {
    private String dashboardId;
    private JsonNode data;

    public PersistContext(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }
}
