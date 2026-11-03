package com.bytter.biz.erp.vo.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 币别信息查询请求参数。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
public class CurQueryRequestVO extends BaseRequest implements Serializable {

    /**
     * 币别代码。
     */
    private String curCode;

    /**
     * 币别名称。
     */
    private String curName;

    /**
     * 币种简码（如英文缩写）。
     */
    private String englishCode;

    /**
     * 更新日期下界。
     */
    private String updateDate;
}
