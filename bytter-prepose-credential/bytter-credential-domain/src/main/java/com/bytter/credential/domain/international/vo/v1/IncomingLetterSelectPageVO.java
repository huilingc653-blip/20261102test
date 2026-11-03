package com.bytter.credential.domain.international.vo.v1;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("信用证数据选取")
public class IncomingLetterSelectPageVO {

    private String corpId;

    private String curId;

    private Date transTime;
}
