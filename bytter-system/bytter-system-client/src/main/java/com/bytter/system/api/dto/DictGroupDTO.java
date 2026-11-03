package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/5/15.
 */
public class DictGroupDTO implements Serializable {
    private String id;
    private String groupName;
    private String groupCode;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupCode() {
        return groupCode;
    }
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
