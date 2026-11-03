package com.bytter.poi.excel.entity.params;
import org.apache.poi.ss.usermodel.CellStyle;
import java.io.Serializable;
/**
 * 导入模板
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelTemplateParams implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 模板名字
     */
    private String name;
    /**
     * 样式
     */
    private CellStyle cellStyle;
    /**
     * 行高
     */
    private short height;
    public ExcelTemplateParams() {
    }
    public ExcelTemplateParams(String name, CellStyle cellStyle, short height) {
        this.name = name;
        this.cellStyle = cellStyle;
        this.height = height;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public CellStyle getCellStyle() {
        return this.cellStyle;
    }
    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }
    public short getHeight() {
        return this.height;
    }
    public void setHeight(short height) {
        this.height = height;
    }
}
