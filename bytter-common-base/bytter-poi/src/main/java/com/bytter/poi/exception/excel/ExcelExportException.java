package com.bytter.poi.exception.excel;
import com.bytter.core.common.exception.ExcelException;
import com.bytter.poi.exception.excel.enums.ExcelExportEnum;
/**
 * excel导出异常类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelExportException extends ExcelException {
    private static final long serialVersionUID = 1L;
    private ExcelExportEnum type;
    public ExcelExportException() {
    }
    public ExcelExportException(ExcelExportEnum type) {
        super(type.getMsg());
        this.type = type;
    }
    public ExcelExportException(ExcelExportEnum type, Throwable cause) {
        super(type.getMsg(), cause);
    }
    public ExcelExportException(String message) {
        super(message);
    }
    public ExcelExportException(String message, ExcelExportEnum type) {
        super(message);
        this.type = type;
    }
    public ExcelExportEnum getType() {
        return this.type;
    }
    public void setType(ExcelExportEnum type) {
        this.type = type;
    }
}
