package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author bytter
 * @Date 2025/6/24 下午8:19
 * @Version 1.0
 **/
@Data
public class ReceivableIndexQueryVo implements Serializable {

    private String corpId;
    private String moneyType;
    private String startDate;
    private String endDate;
    private String category;
    // 如果 传入 cycle  就只查询因收账款周转分析 两个值
    private String type;
}
