package com.bytter.poi.pdf.entity;
import com.bytter.poi.excel.entity.ExcelBaseParams;
import com.bytter.poi.pdf.styler.IPdfExportStyler;
/**
 * PDF 导出参数设置
 */
public class PdfExportParams extends ExcelBaseParams {
    /**
     * 表格名称
     */
    private String title;
    /**
     * 表格名称
     */
    private short titleHeight = 30;
    /**
     * 第二行名称
     */
    private String secondTitle;
    /**
     * 表格名称
     */
    private short secondTitleHeight = 25;
    /**
     * 过滤的属性
     */
    private String[] exclusions;
    /**
     * 是否添加需要需要
     */
    private boolean addIndex;
    /**
     * 是否添加需要需要
     */
    private String indexName = "序号";
    private IPdfExportStyler styler;
    public PdfExportParams() {
    }
    public PdfExportParams(String title) {
        this.title = title;
    }
    public PdfExportParams(String title, String secondTitle) {
        this.title = title;
        this.secondTitle = secondTitle;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public short getTitleHeight() {
        return titleHeight;
    }
    public void setTitleHeight(short titleHeight) {
        this.titleHeight = titleHeight;
    }
    public String getSecondTitle() {
        return secondTitle;
    }
    public void setSecondTitle(String secondTitle) {
        this.secondTitle = secondTitle;
    }
    public short getSecondTitleHeight() {
        return secondTitleHeight;
    }
    public void setSecondTitleHeight(short secondTitleHeight) {
        this.secondTitleHeight = secondTitleHeight;
    }
    public String[] getExclusions() {
        return exclusions;
    }
    public void setExclusions(String[] exclusions) {
        this.exclusions = exclusions;
    }
    public boolean isAddIndex() {
        return addIndex;
    }
    public void setAddIndex(boolean addIndex) {
        this.addIndex = addIndex;
    }
    public String getIndexName() {
        return indexName;
    }
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
    public IPdfExportStyler getStyler() {
        return styler;
    }
    public void setStyler(IPdfExportStyler styler) {
        this.styler = styler;
    }
}
