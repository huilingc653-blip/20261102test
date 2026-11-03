package com.bytter.poi.excel.entity.sax;
import com.bytter.poi.excel.entity.enmus.CellValueType;
/**
 * 读取列值内容
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class SaxReadCellEntity {
    /**
     * 值类型
     */
    private CellValueType cellType;
    /**
     * 值
     */
    private Object value;
    public SaxReadCellEntity(CellValueType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }
    public CellValueType getCellType() {
        return this.cellType;
    }
    public void setCellType(CellValueType cellType) {
        this.cellType = cellType;
    }
    public Object getValue() {
        return this.value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "[type=" + this.cellType.toString() + ",value=" + this.value + "]";
    }
}
