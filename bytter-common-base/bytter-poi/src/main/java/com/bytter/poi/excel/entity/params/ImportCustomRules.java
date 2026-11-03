package com.bytter.poi.excel.entity.params;
import java.util.Map;
/**
 * 自定义导入规则
 *
 * @author zuoy
 * @since 2019-05-09
 */
public class ImportCustomRules {
    /**
     * 读取起始行
     */
    private int startRow;
    /**
     * 导入列
     */
    private String[] columns;
    /**
     * 是否有分隔符
     */
    private boolean isSeparator;
    /**
     * 分隔符
     */
    private String separator;
    /**
     * 数据来源
     */
    private String dataSources;
    /**
     * 文件后缀
     */
    private String suffix;
    /**
     * 分隔符位置 key-列名，value-位置
     */
    private Map<String, Integer> separatorPosition;
    /**
     * 是否存在列头 xls，csv 使用
     */
    private boolean head;
    /**
     * 列头位置 xls，csv 使用
     */
    private Map<String, String> columnPositio;
    /**
     * 无分隔符列的位置 key-列名，value-值 value为列的起始位和截至位，格式为1-1 or 2-5
     */
    private Map<String, String> notSeparatorColumnPositio;
    public int getStartRow() {
        return startRow;
    }
    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
    public String[] getColumns() {
        return columns;
    }
    public void setColumns(String[] columns) {
        this.columns = columns;
    }
    public boolean isSeparator() {
        return isSeparator;
    }
    public void setSeparator(boolean separator) {
        isSeparator = separator;
    }
    public String getSeparator() {
        return separator;
    }
    public void setSeparator(String separator) {
        this.separator = separator;
    }
    public String getDataSources() {
        return dataSources;
    }
    public void setDataSources(String dataSources) {
        this.dataSources = dataSources;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public Map<String, Integer> getSeparatorPosition() {
        return separatorPosition;
    }
    public void setSeparatorPosition(Map<String, Integer> separatorPosition) {
        this.separatorPosition = separatorPosition;
    }
    public boolean isHead() {
        return head;
    }
    public void setHead(boolean head) {
        this.head = head;
    }
    public Map<String, String> getColumnPositio() {
        return columnPositio;
    }
    public void setColumnPositio(Map<String, String> columnPositio) {
        this.columnPositio = columnPositio;
    }
    public Map<String, String> getNotSeparatorColumnPositio() {
        return notSeparatorColumnPositio;
    }
    public void setNotSeparatorColumnPositio(Map<String, String> notSeparatorColumnPositio) {
        this.notSeparatorColumnPositio = notSeparatorColumnPositio;
    }
}
