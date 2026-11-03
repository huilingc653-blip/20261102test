package com.bytter.credit.domain.vo;

import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("查询授信额度录入表详情数据")
public class RatBkrationViewPageVO {

    private RatBkrationPageVO ratBkrationPage;

    private String viewFlag;

    private String isHangBail;

    private String isGuarantee;

    private ConfigDTO relateCon;

    private ConfigDTO conType;

    private String conId;

    private String isOpen;
}
