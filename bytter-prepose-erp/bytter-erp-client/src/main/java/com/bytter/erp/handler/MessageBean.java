package com.bytter.erp.handler;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description: 报文类
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
@Data
public class MessageBean {
    /**
     * 流水号
     */
    @NotBlank(message = "请求流水号不能为空")
    private String serialNo;

    /**
     * 版本号
     */
    @NotBlank(message = "版本号不能为空")
    private String version;

    /**
     * 租户Code
     */
    @NotBlank(message = "客户号不能为空")
    private String tenantCode;

    /**
     * 服务名
     */
    @NotBlank(message = "请求接口不能为空")
    private String serviceCode;

    /**
     * 响应状态
     */
    private String status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 报文json串
     */
    @NotBlank(message = "请求报文不能为空")
    private String data;
}
