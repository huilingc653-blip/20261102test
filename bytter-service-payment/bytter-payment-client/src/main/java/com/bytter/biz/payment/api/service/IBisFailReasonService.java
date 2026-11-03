package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BisFailReasonDTO;
/**
 * 交易失败原因接口类
 * @author huangx
 * @since 2019-05-28 09:49:44
 */
public interface IBisFailReasonService {
    /**
     * 根据返回信息获取交易失败原因
     * @param bifCode 银企接口
     * @param returnMsg 失败原因关键字
     * @return 交易失败原因
     */
    BisFailReasonDTO getFailReasonByReturnMsg(String bifCode, String returnMsg);
}
