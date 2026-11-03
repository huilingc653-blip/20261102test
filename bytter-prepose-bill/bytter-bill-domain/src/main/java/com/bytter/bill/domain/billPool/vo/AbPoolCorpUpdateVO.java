package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 票据池单位修改页面数据
 */
@Data
public class AbPoolCorpUpdateVO implements Serializable {
    private AbPoolCorpVO abPoolCorpVO;
    private String corpIds;
    private String id;
}
