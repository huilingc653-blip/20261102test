package com.bytter.poi.word.entity.params;
import com.bytter.poi.excel.entity.ExcelBaseParams;
import com.bytter.poi.handler.inter.IExcelDataHandler;
import java.util.List;
/**
 * Created by zhouj on 2017/6/2.
 */
public class ExcelListEntity extends ExcelBaseParams {
    private List<?> list;
    private Class<?> clazz;
    private int headRows = 1;
    public ExcelListEntity() {
    }
    public ExcelListEntity(List<?> list, Class<?> clazz) {
        this.list = list;
        this.clazz = clazz;
    }
    public ExcelListEntity(List<?> list, Class<?> clazz, IExcelDataHandler dataHanlder) {
        this.list = list;
        this.clazz = clazz;
        this.setDataHanlder(dataHanlder);
    }
    public ExcelListEntity(List<?> list, Class<?> clazz, IExcelDataHandler dataHanlder, int headRows) {
        this.list = list;
        this.clazz = clazz;
        this.headRows = headRows;
        this.setDataHanlder(dataHanlder);
    }
    public ExcelListEntity(List<?> list, Class<?> clazz, int headRows) {
        this.list = list;
        this.clazz = clazz;
        this.headRows = headRows;
    }
    public Class<?> getClazz() {
        return this.clazz;
    }
    public int getHeadRows() {
        return this.headRows;
    }
    public List<?> getList() {
        return this.list;
    }
    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
    public void setHeadRows(int headRows) {
        this.headRows = headRows;
    }
    public void setList(List<?> list) {
        this.list = list;
    }
}
