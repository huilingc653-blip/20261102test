package com.bytter.poi.handler.inter;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import java.util.List;
/**
 * 导入校验-自定义校验规则
 *
 * @param <T>
 *            导入对象
 * @author zuoy 2019-05-08
 */
public interface ICustomVerifyHandler<T> {
    /**
     * 导入校验方法
     *
     * @param obj
     *            当前对象
     * @param objs
     *            第一个参数为CurrentUser对象，第二个参数为ImportCustomRules
     * @return {@link ExcelVerifyHanlderResult}
     */
    ExcelVerifyHanlderResult verifyHandler(T obj, Object... objs);
    /**
     * 导入校验方法
     *
     * @param list
     *            当前集合对象
     * @param objs
     *            第一个参数为CurrentUser对象，第二个参数为ImportCustomRules 最后将有错误的信息调用T里的addErrorMsg方法
     */
    void verifyHandler(List<T> list, Object... objs);
    /**
     * 根据导入参数获取自定义导入规则
     *
     * @param obj
     *            参数
     * @return {@link ImportCustomRules}
     */
    ImportCustomRules getImportCustomRules(Object... obj);
}
