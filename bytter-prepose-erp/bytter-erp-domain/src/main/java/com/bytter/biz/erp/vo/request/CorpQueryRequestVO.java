package com.bytter.biz.erp.vo.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 单位信息查询请求参数。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
public class CorpQueryRequestVO extends BaseRequest implements Serializable {

    /** 单位代码。 */
    private String code;

    /** 单位名称。 */
    private String name;

    /** 更新日期（格式：yyyy-MM-dd）。 */
    private String updateDate;
}
