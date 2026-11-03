package com.bytter.poi.exception.excel;
import com.bytter.core.common.exception.ExcelException;
import com.bytter.poi.exception.excel.enums.ExcelImportEnum;
/**
 * excel导入异常类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelImportException extends ExcelException {
    private static final long serialVersionUID = 1L;
    private ExcelImportEnum type;
    public ExcelImportException() {
    }
    public ExcelImportException(ExcelImportEnum type) {
        super(type.getMsg());
        this.type = type;
    }
    public ExcelImportException(ExcelImportEnum type, Throwable cause) {
        super(type.getMsg(), cause);
    }
    public ExcelImportException(String message) {
        super(message);
    }
    public ExcelImportException(String message, ExcelImportEnum type) {
        super(message);
        this.type = type;
    }
    public ExcelImportEnum getType() {
        return this.type;
    }
    public void setType(ExcelImportEnum type) {
        this.type = type;
    }
}
