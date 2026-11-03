package com.bytter.erp.exception;

import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.handler.MessageBean;import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description: 全局异常处理器
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MessageBean handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        // 流水号
        String serialNo = bindingResult.getTarget() == null ? "" : ((MessageBean) bindingResult.getTarget()).getSerialNo();

        // 字段校验失败信息
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (errorMsg.length() > 0){
                errorMsg.append(";");
            }
            errorMsg.append(fieldError.getDefaultMessage());
        }

        log.error("请求:{} 参数校验失败:{}",serialNo, errorMsg.toString());

        // 创建错误响应报文
        MessageBean response = new MessageBean();
        response.setSerialNo(serialNo);
        response.setStatus(ErpConstant.SERVER_FAIL_STATUS); // 设置失败状态
        response.setMsg(errorMsg.toString()); // 设置错误消息
        response.setData(""); // 数据为空

        return response;
    }
}
