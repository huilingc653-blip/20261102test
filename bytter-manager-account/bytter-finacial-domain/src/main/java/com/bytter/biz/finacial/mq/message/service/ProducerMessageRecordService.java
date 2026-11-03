package com.bytter.biz.finacial.mq.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytter.biz.finacial.mq.message.entity.ProducerMessageRecord;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.base.entity.ICurrentUser;

/**
 * @author luoyj
 * @date 2024/4/10.
 */
public interface ProducerMessageRecordService extends IService<ProducerMessageRecord> {

    boolean saveProducerMsgRecord(BusinessType businessType, String businessId, Object data, ICurrentUser user);
}
