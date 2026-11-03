package com.bytter.biz.transfer.swap.vo;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/25.
 */
public class ErpTemplateVO {
    private String id;
    @NotNull(message = "templateTypeId不能为空")
    private String templateTypeId;
    @Length(max = 50, message = "templateCode长度不能超过50")
    @NotNull(message = "templateCode不能为空")
    private String templateCode;
    @Length(max = 100, message = "templateName长度不能超过100")
    @NotNull(message = "templateName不能为空")
    private String templateName;
    /**
     * 补录是否编辑 1是0否
     */
    private String enteringIsEdit;
    private String rmk;
    private Map<String,Object> map;//表头数据
    private List<Map<String,Object>> list;//凭证数据
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTemplateTypeId() {
        return templateTypeId;
    }
    public void setTemplateTypeId(String templateTypeId) {
        this.templateTypeId = templateTypeId;
    }
    public String getTemplateCode() {
        return templateCode;
    }
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public List<Map<String,Object>> getList() {
        return list;
    }
    public void setList(List<Map<String,Object>> list) {
        this.list = list;
    }
    public Map<String, Object> getMap() {
        return map;
    }
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
	public String getEnteringIsEdit() {
		return enteringIsEdit;
	}
	public void setEnteringIsEdit(String enteringIsEdit) {
		this.enteringIsEdit = enteringIsEdit;
	}
    
}
