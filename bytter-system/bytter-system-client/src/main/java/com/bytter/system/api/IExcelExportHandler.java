package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesInput;
import java.util.List;
/**
 * 导出数据获取接口
 *
 * @param <T>
 * @author zuoy
 */
public interface IExcelExportHandler<T> {
    /**
     * 获取导出数据
     *
     * @param dataTablesInput
     * @param total
     * @return
     */
    List getExportData(DataTablesInput dataTablesInput, int total);
    /**
     * 获取导出Sql脚本
     *
     * @param dataTablesInput
     * @return
     */
    String getExportQuerySql(DataTablesInput dataTablesInput);
}
