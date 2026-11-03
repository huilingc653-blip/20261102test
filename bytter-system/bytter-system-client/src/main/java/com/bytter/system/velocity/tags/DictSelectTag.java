package com.bytter.system.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.DictGroupDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.velocity.tools.config.DefaultKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 单选、多选、下拉框
 */
@DefaultKey("dictSelect")
public class DictSelectTag {
    private String typeGroupCode; //数据字典类型
    private String field; // 选择表单的Name EAMPLE:<select name="selectName" id = ""
    private String id; // 选择表单ID EAMPLE:<select name="selectName" id = "" />
    private String defaultVal; // 默认值
    private String title; //label显示值
    private String type; //控件类型 radio|switch|checkbox|select|selectTree   list==select
    private String dictTable;// 自定义字典表
    private String dictField;// 自定义字典表的匹配字段-字典的编码值
    private String dictText;// 自定义字典表的显示文本-字典的显示值
    private String extendJson;//扩展参数
    private String readonly;// 只读属性
    private String dictCondition;
    private String datatype;
    private static IDictService dictService;
    private static IMultiLangService mutiLangServiceImpl;
    public DictSelectTag instance(Map<String, Object> map) throws Exception {
        DictSelectTag tag = new DictSelectTag();
        if (!EmptyUtils.isEmpty(map)) {
            BeanUtils.populate(tag, map);
        }
        return tag;
    }
    /**
     * 生成html
     *
     * @return
     */
    public String html() {
        return end().toString();
    }
    public StringBuffer end() {
        if (EmptyUtils.isEmpty(mutiLangServiceImpl)) {
            mutiLangServiceImpl = ApplicationContextUtil.getContext().getBean("multiLangServiceI", IMultiLangService.class);
        }
        StringBuffer sb = new StringBuffer();
        if (dictTable != null) {
            List<Map<String, Object>> list = queryDic();//查询字典
            if ("radio".equals(type)) {
                for (Map<String, Object> map : list) {
                    radio(map.get("text").toString(), map.get("field").toString(), sb);
                }
            } else if ("switch".equals(type)) {
                viewswitch(list, sb);
            } else if ("checkbox".equals(type)) {
                for (Map<String, Object> map : list) {
                    checkbox(map.get("text").toString(), map.get("field").toString(), sb);
                }
            } else if ("text".equals(type)) {
                for (Map<String, Object> map : list) {
                    text(map.get("text").toString(), map.get("field").toString(), sb);
                }
            } else {
                sb.append("<select name=\"" + field + "\"");
                this.readonly(sb);
                //增加扩展属性
                if (!StringUtils.isBlank(this.extendJson)) {
                    Map<String, String> mp = null;
                    try {
                        mp = new ObjectMapper().readValue(extendJson, Map.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (Map.Entry<String, String> entry : mp.entrySet()) {
                        sb.append(entry.getKey() + "=\"" + entry.getValue() + "\"");
                    }
                }
                if (!StringUtils.isBlank(this.id)) {
                    sb.append(" id=\"" + id + "\"");
                }
                sb.append(">");
                select("common.placeholder.select", "", sb);
                for (Map<String, Object> map : list) {
                    select(map.get("text").toString(), map.get("field").toString(), sb);
                }
                sb.append("</select>");
            }
        } else {
            if (EmptyUtils.isEmpty(dictService)) {
                dictService = ApplicationContextUtil.getContext().getBean("dictServiceI", IDictService.class);
            }
            DictGroupDTO typeGroup = dictService.get(this.typeGroupCode);
            List<DictDTO> types = dictService.list(this.typeGroupCode);
            if (typeGroup != null) {
                if ("radio".equals(type)) {
                    for (DictDTO type : types) {
                        radio(type.getName(), type.getCode(), sb);
                    }
                } else if ("switch".equals(type)) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    for (DictDTO type : types) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("field", type.getCode());
                        map.put("text", type.getName());
                        list.add(map);
                    }
                    viewswitch(list, sb);
                } else if ("checkbox".equals(type)) {
                    for (DictDTO type : types) {
                        checkbox(type.getName(), type.getCode(), sb);
                    }
                } else if ("text".equals(type)) {
                    for (DictDTO type : types) {
                        text(type.getName(), type.getCode(), sb);
                    }
                } else {
                    sb.append("<select name=\"" + field + "\"");
                    this.readonly(sb);
                    //增加扩展属性
                    if (!StringUtils.isBlank(this.extendJson)) {
                        Map<String, String> mp = null;
                        try {
                            mp = new ObjectMapper().readValue(extendJson, Map.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (Map.Entry<String, String> entry : mp.entrySet()) {
                            sb.append(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
                        }
                    }
                    if (!StringUtils.isBlank(this.id)) {
                        sb.append(" id=\"" + id + "\"");
                    }
                    this.datatype(sb);
                    sb.append(">");
                    select("common.placeholder.select", "", sb);
                    for (DictDTO type : types) {
                        select(type.getName(), type.getCode(), sb);
                    }
                    sb.append("</select>");
                }
            }
        }
        return sb;
    }
    /**
     * 文本框方法
     *
     * @param name
     * @param code
     * @param sb
     */
    private void text(String name, String code, StringBuffer sb) {
        if (code.equals(this.defaultVal)) {
            sb.append("<input name='" + field + "'" + " id='" + id + "' value='" + mutiLangServiceImpl.getLang(name) + "' readOnly = 'readOnly' />");
            this.readonly(sb);
            this.datatype(sb);
            sb.append(" />");
        } else {
        }
    }
    /**
     * 单选框方法
     *
     * @param name
     * @param code
     * @param sb
     */
    private void radio(String name, String code, StringBuffer sb) {
        if (code.equals(this.defaultVal)) {
            sb.append("<input type=\"radio\" name=\"" + field + "\" title=\"" + mutiLangServiceImpl.getLang(name) + "\" checked=\"checked\" value=\"" + code + "\"");
            if (!StringUtils.isBlank(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            this.readonly(sb);
            this.datatype(sb);
            sb.append(" />");
        } else {
            sb.append("<input type=\"radio\" name=\"" + field + "\" title=\"" + mutiLangServiceImpl.getLang(name) + "\" value=\"" + code + "\"");
            if (!StringUtils.isBlank(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            this.readonly(sb);
            this.datatype(sb);
            sb.append(" />");
        }
    }
    /**
     * switch单选框方法
     *
     * @param sb
     */
    public void viewswitch(List<Map<String, Object>> list, StringBuffer sb) {
        if (this.defaultVal == null) this.defaultVal = "";
        Boolean checked = false;
        String text = "";
        if (StringUtils.isNotBlank(this.defaultVal)) {
            for (Map<String, Object> map : list) {
                if (defaultVal.equals(map.get("field").toString())) {
                    checked = true;
                    break;
                }
            }
        }
        for (Map<String, Object> map : list) {
            text += mutiLangServiceImpl.getLang(map.get("text").toString()) + "|";
        }
        if (checked) {
            sb.append("<input type=\"checkbox\" lay-skin=\"switch\" name=\"" + field + "\" checked=\"checked\" lay-text=\"" + text + "\"");
            if (!StringUtils.isBlank(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            this.readonly(sb);
            this.datatype(sb);
            sb.append(" />");
        } else {
            sb.append("<input type=\"checkbox\" lay-skin=\"switch\" name=\"" + field + "\" lay-text=\"" + text + "\"");
            if (!StringUtils.isBlank(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            this.readonly(sb);
            this.datatype(sb);
            sb.append(" />");
        }
    }
    /**
     * 复选框方法
     *
     * @param name
     * @param code
     * @param sb
     */
    private void checkbox(String name, String code, StringBuffer sb) {
        if (this.defaultVal == null) this.defaultVal = "";
        String[] values = this.defaultVal.split(",");
        Boolean checked = false;
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (code.equals(value)) {
                checked = true;
                break;
            }
            checked = false;
        }
        if (checked) {
            sb.append("<input type=\"checkbox\" lay-skin=\"primary\" title=\"" + mutiLangServiceImpl.getLang(name) + "\" name=\"" + field + "\" checked=\"checked\" value=\"" + code + "\"");
            if (!StringUtils.isBlank(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            this.readonly(sb);
            this.datatype(sb);
            sb.append(" />");
        } else {
            sb.append("<input type=\"checkbox\" lay-skin=\"primary\" title=\"" + mutiLangServiceImpl.getLang(name) + "\" name=\"" + field + "\" value=\"" + code + "\"");
            if (!StringUtils.isBlank(this.id)) {
                sb.append(" id=\"" + id + "\"");
            }
            this.readonly(sb);
            this.datatype(sb);
            sb.append(" />");
        }
    }
    /**
     * 选择框方法
     *
     * @param name
     * @param code
     * @param sb
     */
    private void select(String name, String code, StringBuffer sb) {
        if (code.equals(this.defaultVal)) {
            sb.append(" <option value=\"" + code + "\" selected=\"selected\">");
        } else {
            sb.append(" <option value=\"" + code + "\">");
        }
        sb.append(mutiLangServiceImpl.getLang(name));
        sb.append("</option>");
    }
    /**
     * 查询自定义数据字典
     */
    private List<Map<String, Object>> queryDic() {
        String sql = "select " + dictField + " as field," + dictText + " as text from " + dictTable;
        if (dictCondition != null) {
            sql += dictCondition;
        }
        if (EmptyUtils.isEmpty(dictService)) {
            dictService = ApplicationContextUtil.getContext().getBean("dictServiceI", IDictService.class);
        }
        List<Map<String, Object>> list = dictService.getExtendTableData(sql);
        return list;
    }
    /**
     * 加入datatype属性,并加入非空验证作为默认值
     *
     * @param sb
     * @return
     */
    private StringBuffer datatype(StringBuffer sb) {
        if (!StringUtils.isBlank(this.datatype)) {
            sb.append(" datatype=\"" + datatype + "\"");
        }
        return sb;
    }
    /**
     * 加入readonly 属性,当此属性值为 readonly时，设置控件只读
     *
     * @param sb
     * @return sb
     */
    private StringBuffer readonly(StringBuffer sb) {
        if (!StringUtils.isBlank(readonly) && readonly.equals("readonly")) {
            if ("radio".equals(type)) {
                sb.append(" disable= \"disabled\" disabled=\"disabled\" ");
            } else if ("checkbox".equals(type)) {
                sb.append(" disable= \"disabled\" disabled=\"disabled\" ");
            } else if ("text".equals(type)) {
            } else {
                sb.append(" disable= \"disabled\" disabled=\"disabled\" ");
            }
        }
        return sb;
    }
    public String getTypeGroupCode() {
        return typeGroupCode;
    }
    public void setTypeGroupCode(String typeGroupCode) {
        this.typeGroupCode = typeGroupCode;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDefaultVal() {
        return defaultVal;
    }
    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDictTable() {
        return dictTable;
    }
    public void setDictTable(String dictTable) {
        this.dictTable = dictTable;
    }
    public String getDictField() {
        return dictField;
    }
    public void setDictField(String dictField) {
        this.dictField = dictField;
    }
    public String getDictText() {
        return dictText;
    }
    public void setDictText(String dictText) {
        this.dictText = dictText;
    }
    public String getExtendJson() {
        return extendJson;
    }
    public void setExtendJson(String extendJson) {
        this.extendJson = extendJson;
    }
    public String getDictCondition() {
        return dictCondition;
    }
    public void setDictCondition(String dicCondition) {
        this.dictCondition = dicCondition;
    }
    public String getDatatype() {
        return datatype;
    }
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
    public String getReadonly() {
        return readonly;
    }
    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }
}
