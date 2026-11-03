package com.bytter.guarantee.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("担保合同选择业务查看数据获取")
public class GuaContractSelectPageVO {

    private String sysCode;

    private String curId;

    private String corpId;

    private String businessIds;
    private String corpName;
    private Date endDate;
    private Date startDate;
}
