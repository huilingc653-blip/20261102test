package com.bytter.bill.domain.billPool.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 业务类型列表页面数据
 */
@Data
public class AbPoolBusTypeListVO implements Serializable {
    private List<AbPoolBusTypeVO> listInfo;
    private AbPoolBusTypeVO queryVO;
}
