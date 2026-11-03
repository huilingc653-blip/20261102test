package com.bytter.poi.excel.imports.verifys;
import com.bytter.poi.excel.entity.params.ExcelVerifyEntity;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.poi.util.StringPoiUtils;
import java.util.ArrayList;
import java.util.List;
/**
 * 校验处理类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class VerifyHandlerServer {
    private static final ExcelVerifyHanlderResult DEFAULT_RESULT = new ExcelVerifyHanlderResult(true);
    public VerifyHandlerServer() {
    }
    /**
     * 增加处理结果
     *
     * @param handlerResult
     *            处理结果
     * @param result
     *            增加处理结果集合
     */
    private void addVerifyResult(ExcelVerifyHanlderResult handlerResult, ExcelVerifyHanlderResult result) {
        List<String> list = new ArrayList<String>();
        if (result.getMsg() != null && result.getMsg().size() > 0) {
            list.addAll(result.getMsg());
        }
        list.addAll(handlerResult.getMsg());
        if (!handlerResult.isSuccess()) {
            result.setSuccess(false);
            result.setMsg(list);
        }
    }
    /**
     * 校验值
     *
     * @param object
     *            导入对象
     * @param value
     *            单元格值
     * @param name
     *            名称
     * @param verify
     *            校验规则
     * @param excelVerifyHandler
     *            导入校验接口
     * @return @{@link ExcelVerifyHanlderResult}
     */
    public ExcelVerifyHanlderResult verifyData(Object object, Object value, String name, ExcelVerifyEntity verify,
            IExcelVerifyHandler excelVerifyHandler) {
        if (verify == null) {
            return DEFAULT_RESULT;
        } else {
            ExcelVerifyHanlderResult result = new ExcelVerifyHanlderResult(true, new ArrayList<String>());
            if (verify.isNotNull()) {
                this.addVerifyResult(BaseVerifyHandler.notNull(name, value), result);
            }
            if (verify.isEmail()) {
                this.addVerifyResult(BaseVerifyHandler.isEmail(name, value), result);
            }
            if (verify.isMobile()) {
                this.addVerifyResult(BaseVerifyHandler.isMobile(name, value), result);
            }
            if (verify.isTel()) {
                this.addVerifyResult(BaseVerifyHandler.isTel(name, value), result);
            }
            if (verify.getMaxLength() != -1) {
                this.addVerifyResult(BaseVerifyHandler.maxLength(name, value, verify.getMaxLength()), result);
            }
            if (verify.getMinLength() != -1) {
                this.addVerifyResult(BaseVerifyHandler.minLength(name, value, verify.getMinLength()), result);
            }
            if (StringPoiUtils.isNotEmpty(verify.getRegex())) {
                this.addVerifyResult(BaseVerifyHandler.regex(name, value, verify.getRegex(), verify.getRegexTip()),
                        result);
            }
            return result;
        }
    }
}
