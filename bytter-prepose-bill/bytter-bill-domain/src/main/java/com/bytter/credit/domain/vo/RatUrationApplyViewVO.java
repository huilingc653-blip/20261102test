package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.RatUrationApplyListDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 授信占用申请表详情页面数据
 */
@Data
public class RatUrationApplyViewVO implements Serializable {
    private RatBkrationCpAddListDTO ratBkrationPage;
    private RatUrationApplyListDTO applyDTO;
    private String viewFlag;
    private String isOpenRat;
    private String isGuarantee;
    private String guaWayNames;
}
