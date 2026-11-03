package com.bytter.increment.api.service;

import com.bytter.increment.api.dto.ChoProductReqDTO;

import java.util.Date;
import java.util.List;

/**
 * 理财日志类
 * @author wangzx
 * @since 2019-10-32
 */
public interface IChoProductReqService {

    /**
     * 根据单位id，到期日期获取理财产品信息
     * @param corpId
     * @param deadlineDate
     * @return
     */
    List<ChoProductReqDTO> listChoProductReqByDate(String corpId,Date deadlineDate);
}
