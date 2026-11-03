package com.bytter.increment.api.service;

import java.util.List;
import com.bytter.increment.api.dto.BiRemainTermDTO;

public interface IBiRemainTermService {
    /**
    *@Description 查询剩余期限维护信息
    *@Param deadLine	到期日字段名称
    *@return java.util.List<com.bytter.facade.bi.dto.BiRemainTermDTO>
    *@Date 2020/1/15 18:19 
    *@Author fusb 
    */
    public List<BiRemainTermDTO> getBiRemainTermDTOs(String deadLine);
    /**
     *@Description 查询剩余期限维护信息,不拼接查询SQL
     *@Param
     *@return java.util.List<com.bytter.facade.bi.dto.BiRemainTermDTO>
     *@Date 2020/1/15 18:19
     *@Author fusb
     */
    public List<BiRemainTermDTO> getBiRemainTermDTOList();
}
