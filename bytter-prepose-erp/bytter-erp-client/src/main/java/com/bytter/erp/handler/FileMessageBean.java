package com.bytter.erp.handler;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:文件上传交互实体
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
@Data
public class FileMessageBean extends MessageBean {
    /***
     * 文件名称
     */
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

}
