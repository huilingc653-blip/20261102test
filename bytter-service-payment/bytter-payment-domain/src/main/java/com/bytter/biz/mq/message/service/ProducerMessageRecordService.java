package com.bytter.biz.mq.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytter.biz.mq.message.entity.ProducerMessageRecord;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.base.entity.ICurrentUser;

/**na
 * @author luoyj
 * @date 2024/3/1.
 */
public interface ProducerMessageRecordService extends IService<ProducerMessageRecord> {

    boolean saveProducerMsgRecord(BusinessType businessType, String businessId, Object data, ICurrentUser user);
    boolean saveProducerMsgRecord(BusinessType businessType, String businessId, String exchangeName, String routingKey, Object data, ICurrentUser user);

    //下面都是用于测试
//    boolean saveProducerMsgRecord_test(String id);
//
//    boolean saveProducerMsgRecord_test4(String id);
//
//    boolean saveProducerMsgRecord_test2(String id);
//
//    void saveProducerMsgRecord_test6(String id);
}
