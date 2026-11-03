package com.bytter.core.common;


import com.bytter.core.common.context.LocalMessageService;
import com.bytter.core.common.exception.AbstractBytterException;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.BusinessExceptionDetail;
import com.bytter.core.common.exception.ExcelException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * 全局异常捕获类。
 */
@Slf4j
@ControllerAdvice
@Configuration
public class GlobalExceptionHandlerAdvice {

    @Autowired
    private LocalMessageService localMessageService;

    @ExceptionHandler(value = {ServletException.class})
    public ModelAndView handler(HttpServletResponse response, Exception ex) {
        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        ex.printStackTrace();
        String traceId = mdcAdapter.get("traceId");
        printEx(ex,traceId);
        if(ex!=null) {
            ex.printStackTrace();
        }
        return processNormalAction(ex,traceId);
    }
	
    @ExceptionHandler(value = {Exception.class})
    public ModelAndView handler(HttpServletResponse response, Exception ex, HandlerMethod handlerMethod) {
        MDCAdapter mdcAdapter = MDC.getMDCAdapter();
        String traceId = mdcAdapter.get("traceId");
        printEx(ex,traceId);
        if(ex!=null) {
        	ex.printStackTrace();
        }
        if (isRest(handlerMethod)) {
            processAjaxAction(response, ex,traceId);
            return null;
        }else {
            return processNormalAction(ex,traceId);
        }
    }
    private void printEx(Exception exception, String traceId) {
        log.error("服务器异常:::traceId---[[{}-{}]]", traceId, StringUtils.isNotEmpty(exception.getMessage()) ? exception.getMessage().toString() : exception);
    }
    /**
     * 是否ajax数据风格
     */
    public boolean isRest(HandlerMethod handlerMethod) {
        if (handlerMethod == null) {
            return false;
        }
        return handlerMethod.hasMethodAnnotation(ResponseBody.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(RestController.class);
    }
    /**
     * 根据异常获取消息枚举
     *
     */
    private IMessageEnum getMessageEnum(Exception ex) {
        IMessageEnum messageEnum = null;
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            messageEnum = businessException.getIMessageEnum();
        } else if (ex instanceof ParameterException) {
            ParameterException parameterException = (ParameterException) ex;
            messageEnum = parameterException.getIMessageEnum();
        }else if(ex instanceof WorkFlowVerifyException){
            WorkFlowVerifyException parameterException = (WorkFlowVerifyException) ex;
            messageEnum = parameterException.getIMessageEnum();
        }
        return messageEnum;
    }
    private void processAjaxAction(HttpServletResponse response, Exception ex,String traceId) {
        response.setStatus(HttpStatus.OK.value());
        // 设置ContentType
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);   // 避免乱码
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try(PrintWriter writer = response.getWriter()) {
            StringBuilder sb = new StringBuilder();
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setSuccess(false);
            //获取异常消息枚举
            IMessageEnum messageEnum = getMessageEnum(ex);
            //若不是业务异常和参数异常，则记录为未知错误。
            if (messageEnum != null) {
                ajaxJson.setMessageEnum(messageEnum);
                AbstractBytterException abstractBytterException = (AbstractBytterException) ex;
                sb.append(localMessageService.getLangByEnum(messageEnum, abstractBytterException.getParams()));
            } else {
                if (ex instanceof BusinessException || ex instanceof ParameterException || ex instanceof ExcelException) {
                    sb.append(ex.getMessage());
                }else {
                    sb.append("未知错误\n异常编号[[").append(traceId).append("]]");
                }
                ajaxJson.setMessageEnum(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
            }
            ajaxJson.setMsg(sb.toString());
            if (ex instanceof BusinessExceptionDetail) {
                ajaxJson.setObj(((BusinessExceptionDetail) ex).getDetail());
            }
            writer.write(JsonUtils.toJSONString(ajaxJson));
        } catch (IOException e) {
            log.error("异常处理失败", e);
        }
    }
    private ModelAndView processNormalAction(Exception ex,String traceId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ex instanceof ParameterException ? "common/parameterError" : "common/error");
        IMessageEnum messageEnum = getMessageEnum(ex);
        //若不是业务异常和参数异常，则记录为未知错误。
        if (messageEnum != null) {
            AbstractBytterException exception= (AbstractBytterException) ex;
            modelAndView.addObject("message", "[" + localMessageService.getLangByEnum((messageEnum), exception.getParams()) + "]");
        }else {
            modelAndView.addObject("message", "未知异常!【【异常编号:" + traceId + "】】");
        }
        return modelAndView;
    }
}
