package com.bytter.poi.excel.export.styler;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.params.ExcelForEachParams;
import org.apache.poi.ss.usermodel.CellStyle;
/**
 * Created by zhouj on 2017/6/2.
 */
public interface IExcelExportStyler {
    CellStyle getHeaderStyle(short var1);
    CellStyle getTitleStyle(short var1);
    CellStyle getStyles(boolean var1, ExcelExportEntity var2);
    /**
     * 模板使用的样式设置
     * 
     * @param isSingle
     * @param excelForEachParams
     * @return
     */
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams);
}
