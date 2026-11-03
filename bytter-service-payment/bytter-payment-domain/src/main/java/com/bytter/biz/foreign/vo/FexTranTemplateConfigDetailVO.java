package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;
import lombok.Data;

import java.io.Serializable;

/**
 * 交易模板配置
 */

@Data
public class FexTranTemplateConfigDetailVO implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = -556469445334493467L;
    private String id;
    private String parentId;
    private String templateNo;
    private String templateNameParent;
    private String templateName;
    private String templateDesc;
    private String bankTypeId;
    private String bankTypeName;
    private String bifId;
    private String bifName;
    private String countryId;
    private String countryZhName;
    private String payTypeId;
    private String payTypeName;
    private String payTypeCode;
    private String remark;
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    private String validSign;
    private String validSignName;
    private Integer sort;
    private String versionNo;
    private String validSignParent;
    private String    customName;
    private String    customCode;
    private Boolean    isSplit;
}
