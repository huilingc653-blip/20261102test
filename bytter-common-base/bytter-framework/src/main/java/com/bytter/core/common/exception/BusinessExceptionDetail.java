package com.bytter.core.common.exception;

/** 业务异常可选的结构化详情，由 AJAX 响应透传给对应页面。 */
public interface BusinessExceptionDetail {
    Object getDetail();
}
