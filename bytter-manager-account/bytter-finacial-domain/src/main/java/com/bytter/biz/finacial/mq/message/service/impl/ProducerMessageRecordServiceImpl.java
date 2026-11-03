package com.bytter.biz.finacial.mq.message.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.biz.finacial.mq.message.dao.ProducerMessageRecordMapper;
import com.bytter.biz.finacial.mq.message.entity.ProducerMessageRecord;
import com.bytter.biz.finacial.mq.message.service.ProducerMessageRecordService;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.base.entity.CurrentUser;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.core.enums.SendStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author luoyj
 * @date 2024/4/10.
 */
@Service
@Transactional
public class ProducerMessageRecordServiceImpl extends ServiceImpl<ProducerMessageRecordMapper, ProducerMessageRecord> implements ProducerMessageRecordService {

    /**
     * TODO data 抽象 增加 业务id
     * @param businessType
     * @param data
     * @param user
     * @return
     */
    @Override
    public boolean saveProducerMsgRecord(BusinessType businessType, String businessId, Object data, ICurrentUser user) {
        CurrentUser currentUser = new CurrentUser();
        currentUser.setId(user.getId());
        currentUser.setCorpId(user.getCorpId());
        currentUser.setUsername(user.getUsername());
        currentUser.setLoginName(user.getLoginName());

        CommonMqMsg<TransactionMqMsg> commonMqMsg = CommonMqMsg.buildMsg(TransactionMqMsg.buildMsg(businessType,businessId,data,currentUser) );
        String jsonMsg = JSON.toJSONString(commonMqMsg, SerializerFeature.DisableCircularReferenceDetect);
        //保存到消息表
        return this.save(
                ProducerMessageRecord.builder()
                        .msgId(commonMqMsg.getMsgId())
                        .sendStatus(SendStatus.N_SEND)
                        .businessType(businessType)
                        .businessId(businessId)
                        .createTime(LocalDateTime.now())
                        .createBy(currentUser.getId())
                        .message(jsonMsg)
                        .exchangeName("finacial-exchange").routingKey("ftopkey")
                        .build() );

    }
}