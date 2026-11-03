package com.bytter.poi.excel.html.css;
import com.bytter.poi.excel.html.entity.style.CellStyleEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
/**
 * CSS Cell Style 转换类
 */
public interface ICssConvertToExcel {
    /**
     * 把HTML样式转换成Cell样式
     * 
     * @param cell
     * @param style
     */
    public void convertToExcel(Cell cell, CellStyle cellStyle, CellStyleEntity style);
}
