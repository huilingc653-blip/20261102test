package com.bytter.system.vo;
/**
 * 单位组件查询VO对象
 *
 * @author zuoy
 */
public class CorpComponentVO {
    private String resource;
    private String netId;
    private String type;
    private String parentId;
    private Boolean underNetCorp;
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public Boolean getUnderNetCorp() {
        return underNetCorp;
    }
    public void setUnderNetCorp(Boolean underNetCorp) {
        this.underNetCorp = underNetCorp;
    }
}
