package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.FcFreezeDTO;

import java.util.Date;
import java.util.List;

/**
 * @author luoyj
 * @date 2023/9/13.
 */
public interface IFcFreezeService {

    List<FcFreezeDTO> findNoCheckFreeze(Date closeDate,List<String> accList);
}
