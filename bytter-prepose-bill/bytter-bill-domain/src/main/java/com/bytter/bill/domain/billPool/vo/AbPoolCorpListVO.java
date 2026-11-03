package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 票据池单位维护列表页面数据(列表/下拉列表共用)
 */
@Data
public class AbPoolCorpListVO implements Serializable {
    private List<AbPoolCorpVO> listInfo;
    private AbPoolCorpVO queryVO;
}
