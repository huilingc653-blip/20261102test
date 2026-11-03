package com.bytter.system.vo;
/**
 * Created by songkk on 2017/12/18.
 */
public class RoleVo {
    private String name;
    private String description;
    private String code;
    private String id;
    private Integer useable;//0不启用 1启用 2不可删除
  //是否单位分配了该角色
  	private boolean checked;
  	
    public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getUseable() {
        return useable;
    }
    public void setUseable(Integer useable) {
        this.useable = useable;
    }
}
  
