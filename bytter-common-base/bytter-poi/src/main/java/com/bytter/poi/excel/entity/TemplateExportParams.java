package com.bytter.poi.excel.entity;
import com.bytter.poi.excel.export.styler.ExcelExportStylerDefaultImpl;
/**
 * 导出模板参数
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class TemplateExportParams extends ExcelBaseParams {
    private boolean scanAllsheet = false;
    private String templateUrl;
    private Integer[] sheetNum = new Integer[] { Integer.valueOf(0) };
    private String[] sheetName;
    private int headingRows = 1;
    private int headingStartRow = 1;
    private int dataSheetNum = 0;
    private Class<?> style = ExcelExportStylerDefaultImpl.class;
    private String tempParams = "t";
    private boolean colForEach = false;
    public TemplateExportParams() {
    }
    public TemplateExportParams(String templateUrl, boolean scanAllsheet, String... sheetName) {
        this.templateUrl = templateUrl;
        this.scanAllsheet = scanAllsheet;
        if (sheetName != null && sheetName.length > 0) {
            this.sheetName = sheetName;
        }
    }
    public TemplateExportParams(String templateUrl, Integer... sheetNum) {
        this.templateUrl = templateUrl;
        if (sheetNum != null && sheetNum.length > 0) {
            this.sheetNum = sheetNum;
        }
    }
    public TemplateExportParams(String templateUrl, String sheetName, Integer... sheetNum) {
        this.templateUrl = templateUrl;
        this.sheetName = new String[] { sheetName };
        if (sheetNum != null && sheetNum.length > 0) {
            this.sheetNum = sheetNum;
        }
    }
    public int getHeadingRows() {
        return this.headingRows;
    }
    public int getHeadingStartRow() {
        return this.headingStartRow;
    }
    public String[] getSheetName() {
        return this.sheetName;
    }
    public Integer[] getSheetNum() {
        return this.sheetNum;
    }
    public String getTemplateUrl() {
        return this.templateUrl;
    }
    public void setHeadingRows(int headingRows) {
        this.headingRows = headingRows;
    }
    public void setHeadingStartRow(int headingStartRow) {
        this.headingStartRow = headingStartRow;
    }
    public void setSheetName(String[] sheetName) {
        this.sheetName = sheetName;
    }
    public void setSheetName(String sheetName) {
        this.sheetName = new String[] { sheetName };
    }
    public void setSheetNum(Integer[] sheetNum) {
        this.sheetNum = sheetNum;
    }
    public void setSheetNum(Integer sheetNum) {
        this.sheetNum = new Integer[] { sheetNum };
    }
    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }
    public Class<?> getStyle() {
        return this.style;
    }
    public void setStyle(Class<?> style) {
        this.style = style;
    }
    public int getDataSheetNum() {
        return this.dataSheetNum;
    }
    public void setDataSheetNum(int dataSheetNum) {
        this.dataSheetNum = dataSheetNum;
    }
    public boolean isScanAllsheet() {
        return this.scanAllsheet;
    }
    public void setScanAllsheet(boolean scanAllsheet) {
        this.scanAllsheet = scanAllsheet;
    }
    public String getTempParams() {
        return this.tempParams;
    }
    public void setTempParams(String tempParams) {
        this.tempParams = tempParams;
    }
    public boolean isColForEach() {
        return colForEach;
    }
    public void setColForEach(boolean colForEach) {
        this.colForEach = colForEach;
    }
}
