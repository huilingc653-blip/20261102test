package com.bytter.poi.excel.entity.result;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.List;
import java.util.Map;
/**
 * 导入结果
 *
 * @param <T>
 *            泛型
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelImportResult<T> {
    /**
     * 结果集
     */
    private List<T> list;
    /**
     * 是否存在校验失败
     */
    private boolean verfiyFail;
    /**
     * 数据源
     */
    private Workbook workbook;
    private Map<String, Object> map;
    public ExcelImportResult() {
    }
    public ExcelImportResult(List<T> list, boolean verfiyFail, Workbook workbook) {
        this.list = list;
        this.verfiyFail = verfiyFail;
        this.workbook = workbook;
    }
    public List<T> getList() {
        return this.list;
    }
    public Workbook getWorkbook() {
        return this.workbook;
    }
    public boolean isVerfiyFail() {
        return this.verfiyFail;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
    public void setVerfiyFail(boolean verfiyFail) {
        this.verfiyFail = verfiyFail;
    }
    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }
    public Map<String, Object> getMap() {
        return map;
    }
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
