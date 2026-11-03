package com.bytter.poi.excel.entity.result;
import java.util.List;
/**
 * 校验结果
 *
 * @author zhouj
 * @since 2017/6/2.
 */
public class ExcelVerifyHanlderResult {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 错误信息
     */
    private List<String> msg;
    public ExcelVerifyHanlderResult() {
    }
    public ExcelVerifyHanlderResult(boolean success) {
        this.success = success;
    }
    public ExcelVerifyHanlderResult(boolean success, List<String> msg) {
        this.success = success;
        this.msg = msg;
    }
    public List<String> getMsg() {
        return this.msg;
    }
    public boolean isSuccess() {
        return this.success;
    }
    public void setMsg(List<String> msg) {
        this.msg = msg;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
