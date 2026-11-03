package com.bytter.poi.handler.inter;
import java.util.List;
/**
 * 导出数据接口
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public interface IExcelExportServer {
    /**
     * 导出数据
     *
     * @param obj
     *            导出对象
     * @param page
     *            对出页数
     * @return 导出对象集合
     */
    List<Object> selectListForExcelExport(Object obj, int page);
}
