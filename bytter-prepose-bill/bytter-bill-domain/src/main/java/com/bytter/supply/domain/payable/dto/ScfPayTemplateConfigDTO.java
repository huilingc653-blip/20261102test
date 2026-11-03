package com.bytter.supply.domain.payable.dto;


import com.bytter.supply.domain.payable.vo.ScfPayConfigVO;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ScfPayTemplateConfigDTO
 * @Description 应付债权模板字段配置保存请求DTO
 */
@Data
public class ScfPayTemplateConfigDTO {
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 版本号
     */
    private String versionNo;
    /**
     * 字段配置列表
     */
    private List<ScfPayConfigVO> templateConfigs;
}
