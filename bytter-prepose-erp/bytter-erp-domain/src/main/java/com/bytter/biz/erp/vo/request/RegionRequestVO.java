package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行账户查询请求参数。
 *
 * @author zx
 * @date 2026/4/28
 */
@Data
public class RegionRequestVO extends BaseRequest implements Serializable {

    /** 市代码。 */
    private String cityCode;

    /** 市名称。 */
    private String cityName;

    /** 省代码。 */
    private String provCode;

    /** 省名称。 */
    private String provName;
}
