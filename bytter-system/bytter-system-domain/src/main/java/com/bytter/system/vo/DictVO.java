package com.bytter.system.vo;
import com.bytter.core.utils.StringUtils;
/**
 * Created by zhoul on 2017/5/22.
 */
public class DictVO {
    private String id;
    private String groupName;
    private String groupCode;
    private String name;//类型名称
    private String code;//类型编码
    private Integer sort;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getGroupCode() {
        return groupCode;
    }
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
    public String getName() {
        if (StringUtils.isNotBlank(name)) {
            return name.replace("\n", "").replace("\r", "").replace("\"", "\\\"");
        }
        return name;
    }
    public void setName(String name) {
        if (StringUtils.isNotBlank(name)) {
            name = name.replace("\n", "").replace("\r", "").replace("\"", "\\\"");
        }
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
