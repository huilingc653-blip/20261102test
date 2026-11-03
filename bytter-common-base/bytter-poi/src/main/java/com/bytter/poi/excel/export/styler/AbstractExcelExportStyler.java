package com.bytter.poi.excel.export.styler;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.params.ExcelForEachParams;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
/**
 * Created by zhouj on 2017/6/2.
 */
public abstract class AbstractExcelExportStyler implements IExcelExportStyler {
    protected CellStyle stringNoneStyle;
    protected CellStyle stringNoneWrapStyle;
    protected CellStyle stringSeptailStyle;
    protected CellStyle stringSeptailWrapStyle;
    protected Workbook workbook;
    protected static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
    public AbstractExcelExportStyler() {
    }
    protected void createStyles(Workbook workbook) {
        this.stringNoneStyle = this.stringNoneStyle(workbook, false);
        this.stringNoneWrapStyle = this.stringNoneStyle(workbook, true);
        this.stringSeptailStyle = this.stringSeptailStyle(workbook, false);
        this.stringSeptailWrapStyle = this.stringSeptailStyle(workbook, true);
        this.workbook = workbook;
    }
    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
        return !noneStyler || entity != null && !entity.isWrap()
                ? (noneStyler ? this.stringNoneStyle : (noneStyler || entity != null && !entity.isWrap()
                        ? this.stringSeptailStyle : this.stringSeptailWrapStyle))
                : this.stringNoneWrapStyle;
    }
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        return null;
    }
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        return null;
    }
    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;
    }
}
