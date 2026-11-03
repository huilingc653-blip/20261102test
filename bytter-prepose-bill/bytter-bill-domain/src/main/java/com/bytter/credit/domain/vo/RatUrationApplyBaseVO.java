package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 授信占用申请表新增/编辑页面公共配置数据
 */
@Data
public class RatUrationApplyBaseVO implements Serializable {
    private String isOpenRat;
    private String isGuarantee;
    private String isHangGuarantee;
    private String guaWay;
    private String guaWayName;
    private String allGuaWayCode;
}
