package com.bytter.biz.transfer.swap.vo;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/6/6.
 */
public class ErpHisDtlTemplateVO {
    private List<String> serialId;//流水号
    private String typeId;//模板类型id
    private String templateId;//模板id
    private String dealType;//处理类型
    
    private Map<String,Object> map;//表头数据
    private List<Map<String,Object>> list;//凭证数据
    public List<String> getSerialId() {
        return serialId;
    }
    public void setSerialId(List<String> serialId) {
        this.serialId = serialId;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    public String getDealType() {
        return dealType;
    }
    public void setDealType(String dealType) {
        this.dealType = dealType;
    }
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
    
}
