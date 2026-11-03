package com.bytter.core.excel.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
/**
 * 导出所有Excel表头信息
 * 
 * @author zuoy
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExportExcelHeadDTO implements Serializable {
    /**
     * 序号
     */
    private String idx;
    /**
     * 编码
     */
    private String name;
    /**
     * 标题
     */
    private String title;
    /**
     * 宽度
     */
    private String width;
    /**
     * 样式
     */
    private String className;
    /**
     * 是否隐藏
     */
    private Boolean visible;
    /**
     * 对齐方式
     */
    private String align;
    private String data;
    public String getIdx() {
        return idx;
    }
    public void setIdx(String idx) {
        this.idx = idx;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getWidth() {
        return width;
    }
    public void setWidth(String width) {
        this.width = width;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public Boolean getVisible() {
        return visible;
    }
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    public String getAlign() {
        return align;
    }
    public void setAlign(String align) {
        this.align = align;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}
