package com.bytter.poi.excel.html.css;
import com.bytter.poi.excel.html.entity.style.CellStyleEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
/**
 * CSS Cell Style 转换类
 */
public interface ICssConvertToHtml {
    /**
     * 把Excel单元格样式转换成HTML样式
     * 
     * @param cell
     */
    public String convertToHtml(Cell cell, CellStyle cellStyle, CellStyleEntity style);
}
