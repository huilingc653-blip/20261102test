package com.bytter.biz.bdlink.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class BisAccBalAutoDTO implements Serializable {
    /**
     * Id
     * */
    private String id;

    /**
     * 账号Id
     * */
    private String bankAccId;
    /**
     * 币别Id
     * */
    private String curId;
    /**
     * 计算截止日期
     **/
    private Date endDate;
}
