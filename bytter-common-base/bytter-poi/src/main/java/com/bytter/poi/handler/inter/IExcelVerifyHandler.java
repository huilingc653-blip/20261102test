package com.bytter.poi.handler.inter;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import java.util.List;
/**
 * 导入校验接口
 *
 * @param <T>
 *            导入对象
 * @author zhouj
 * @since 2017/6/2.
 */
public interface IExcelVerifyHandler<T> {
    /**
     * 导入校验方法
     *
     * @param obj
     *            当前对象
     * @return {@link ExcelVerifyHanlderResult}
     */
    ExcelVerifyHanlderResult verifyHandler(T obj);
    /**
     * 导入校验方法
     *
     * @param list
     *            当前集合对象 最后将有错误的信息调用T里的addErrorMsg方法
     */
    void verifyHandler(List<T> list);
}
