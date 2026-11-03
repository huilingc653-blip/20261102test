package com.bytter.system.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Description: 配置信息
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/8/13
 */
@Data
public class ConfigEntityDTO implements Serializable {
    private String type;
    private String name;
    private String value;
}
