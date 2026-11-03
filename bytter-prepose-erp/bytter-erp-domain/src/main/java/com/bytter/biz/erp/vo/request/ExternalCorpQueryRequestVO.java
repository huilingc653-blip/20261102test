package com.bytter.biz.erp.vo.request;

import lombok.Data;

/**
 * 客商信息查询请求参数。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
public class ExternalCorpQueryRequestVO extends BaseRequest {

    /**
     * 客商编码。
     */
    private String code;

    /**
     * 客商名称。
     */
    private String name;
}
