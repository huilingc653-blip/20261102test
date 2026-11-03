/**
 * 
 */
package com.bytter.poi.excel.entity;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import java.util.List;
import java.util.Map;
/**
 * 导入集合
 * 
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExportExcelItem {
    private List<ExcelExportEntity> entityList;
    private List<Map<String, Object>> resultList;
    public List<ExcelExportEntity> getEntityList() {
        return this.entityList;
    }
    public void setEntityList(List<ExcelExportEntity> entityList) {
        this.entityList = entityList;
    }
    public List<Map<String, Object>> getResultList() {
        return this.resultList;
    }
    public void setResultList(List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }
}
