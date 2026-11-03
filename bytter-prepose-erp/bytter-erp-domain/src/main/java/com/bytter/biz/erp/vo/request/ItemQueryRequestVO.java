package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 预算科目查询请求参数。
 *
 * @author zx
 * @date 2026/4/28
 */
@Data
public class ItemQueryRequestVO extends BaseRequest implements Serializable {

    /** 科目代码。 */
    private String itemCode;

    /** 科目名称。 */
    private String itemName;
}
