package com.bytter.biz.foreign.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 交易模板配置
 */

@Data
public class FexTranTemplateConfigParamVO implements Serializable {
    private static final long serialVersionUID = 779728874154014282L;

    /**
     * 模板编号
     */
    private String templateNo;
    /**
     * 模板名称
     */
    private String templateName;
    /**
     * 银行类型
     */
    private List<String> bankTypeIds;
    /**
     * 银行接口
     */
    private List<String> bifIds;
    /**
     * 国家地区
     */
    private List<String> countryIds;
    /**
     * 支付类型
     */
    private List<String> payTypeIds;
    private String payTypeId;
    private String payTypeName;

    /**
     * 有效标识
     */
    private String validSign;

    /**
     * 父模板id
     */
    private String parentId;

    /**
     * 父模板id集合
     */
    private List<String> parentIds;

    /**
     * 模板id集合
     */
    private List<String> ids;
}
