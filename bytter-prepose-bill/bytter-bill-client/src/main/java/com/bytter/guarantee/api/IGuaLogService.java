package com.bytter.guarantee.api;

import com.bytter.guarantee.dto.GuaLogDTO;


/**
 * 担保日志上报相关
 */
public interface IGuaLogService {

    /**
     * 上报担保日志数据
     * @param guaLogDTO
     */
    void saveGuaLog(GuaLogDTO guaLogDTO);

}
