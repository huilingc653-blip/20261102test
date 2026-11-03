package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.NisShareBusTypeDTO;

import java.util.List;

/**
 * 共享业务类型接口类
 *
 * @author fangcf
 * @since 2025-5-20
 */
public interface INisShareBusTypeService {

    /**
     * 根据条件获取正常的共享业务类型
     *
     * @param busType      业务类型
     * @param shareBusType 共享业务类型
     * @return 收款单据DTO
     */
    List<NisShareBusTypeDTO> listValidShareBusType(String busType, String shareBusType);
}
