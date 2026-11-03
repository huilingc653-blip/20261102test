package com.bytter.poi.handler;
import com.bytter.poi.handler.inter.IExcelModel;
import com.bytter.poi.util.StringPoiUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 校验实体父类 用于封装错误提示属性和获取检验接口类 如果需要指定子类中字段用来存储错误信息则子类只需要实现IExcelModel无需集成ExcelModel
 *
 * @author zhouj
 * @since 2017/9/12.
 */
public abstract class ExcelModel implements IExcelModel {
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 服务名称
     */
    @JsonIgnore
    private String beanName;
    /**
     * 字段对应数据字典的code
     */
    private Map<String, String> langMap = new HashMap<String, String>();
    public String getBeanName() {
        return beanName;
    }
    public Map<String, String> getLangMap() {
        return langMap;
    }
    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    /**
     * 添加错误信息
     *
     * @param msgList
     *            错误信息集合
     */
    public void addErrorMsg(List<String> msgList) {
        this.errorMsg = ((StringPoiUtils.isNoneBlank(this.getErrorMsg()) ? this.getErrorMsg() + "," : "")
                + ((msgList != null && msgList.size() > 0) ? StringPoiUtils.join(msgList.toArray(), ',') : ""));
    }
    /**
     * 添加字段与字典关系
     *
     * @param fieldName
     *            字段
     * @param langCode
     *            字典
     */
    public void addLangMap(String fieldName, String langCode) {
        langMap.put(fieldName, langCode);
    }
}
