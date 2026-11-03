package com.bytter.third.dto;

import lombok.Data;

import java.util.Date;

/**
 * 大参林接口返回对象。
 *
 * @author zhaohc
 * @since 2024/5/29 9:51
 */
@Data
public class DslResponse {

    private String code;
    private String msg;
    private Date now;
    private Object data;
    private Boolean next;
    private Integer total;
    private Integer pageNum;
    private Integer pageSize;

}
