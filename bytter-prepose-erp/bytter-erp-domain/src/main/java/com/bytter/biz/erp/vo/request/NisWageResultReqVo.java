package com.bytter.biz.erp.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 代发工作结果查询请求参数vo
 *
 * @author fangcf
 * @date 2026-05-13
 */
@Data
public class NisWageResultReqVo extends BaseRequest implements Serializable {
    private String wageNo;
    private String wageDtlNo;
}
