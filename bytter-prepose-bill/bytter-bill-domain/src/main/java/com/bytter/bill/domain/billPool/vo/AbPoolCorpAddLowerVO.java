package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 票据池下级单位新增页面数据
 */
@Data
public class AbPoolCorpAddLowerVO implements Serializable {
    private String parentId;
    private String bustypeLevel;
    private String corpIds;
}
