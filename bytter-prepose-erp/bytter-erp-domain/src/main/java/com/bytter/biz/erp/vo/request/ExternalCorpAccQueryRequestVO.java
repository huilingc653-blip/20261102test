package com.bytter.biz.erp.vo.request;

import lombok.Data;

/**
 * 客商账号信息查询请求参数。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
public class ExternalCorpAccQueryRequestVO extends BaseRequest {

    /**
     * 客商编码。
     */
    private String code;

    /**
     * 客商名称。
     */
    private String name;

    /**
     * 银行账号。
     */
    private String externalAcc;
}
