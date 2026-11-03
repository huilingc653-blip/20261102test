package com.bytter.poi.excel.entity;
import com.bytter.poi.handler.inter.IExcelDataHandler;
import java.io.Serializable;
/**
 * 导入基础参数
 * 
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelBaseParams implements Serializable {
    private IExcelDataHandler dataHanlder;
    public ExcelBaseParams() {
    }
    public IExcelDataHandler getDataHanlder() {
        return this.dataHanlder;
    }
    public void setDataHanlder(IExcelDataHandler dataHanlder) {
        this.dataHanlder = dataHanlder;
    }
}
