package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeRuleDTO;

import java.util.List;

/**
 * 业务类别管理业务方法接口类。
 *
 * @author zhaohc
 */
public interface INisBusTypeService {
    /**
     * 获取有效的业务类别
     *
     * @return
     */
    List<NisBusTypeDTO> getNisBusTypeAll(String corpId, String cdSign);

    /**
     * 根据业务类别获取匹配规则修改成数段集合
     *
     * @param busId
     * @return
     */
    List<List<NisBusTypeRuleDTO>> getNisBusTypeRuleDTOByBusId(String busId);

    /**
     * 根据系统业务类别得到业务类别
     *
     * @return
     */
    List<NisBusTypeDTO> getBySysBusType(String corpId, String cdSign, String sysBusType);

    /**
     * 根据业务类型id
     *
     * @param id
     * @return
     */
    NisBusTypeDTO getById(String id);

    /**
     * 根据业务编码获取业务类型。
     *
     * @param busType 业务编码。
     * @return 业务类型。
     */
    NisBusTypeDTO getByBusType(String busType);

}
