package com.bytter.biz.mq.message.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.bytter.biz.finacial.api.service.ITestConsumerMsgService;
//import com.bytter.biz.finacial.api.service.ITestConsumerMsgServiceF;
import com.bytter.biz.mq.message.entity.ProducerMessageRecord;
import com.bytter.biz.mq.message.dao.ProducerMessageRecordMapper;
import com.bytter.biz.mq.message.service.ProducerMessageRecordService;
//import com.bytter.biz.mq.message.service.TestSeataService;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.base.entity.CurrentUser;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.core.enums.SendStatus;
//import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author luoyj
 * @date 2024/3/1.
 */
@Slf4j
@Service
public class ProducerMessageRecordServiceImpl extends ServiceImpl<ProducerMessageRecordMapper,ProducerMessageRecord> implements ProducerMessageRecordService {

    /**
     * TODO data 抽象 增加 业务id
     * @param businessType
     * @param data
     * @param user
     * @return
     */
    @Transactional
    @Override
    public boolean saveProducerMsgRecord(BusinessType businessType, String businessId, Object data, ICurrentUser user) {
//        CurrentUser currentUser = new CurrentUser();
//        currentUser.setId(user.getId());
//        currentUser.setCorpId(user.getCorpId());
//        currentUser.setUsername(user.getUsername());
//        currentUser.setLoginName(user.getLoginName());
//
//        CommonMqMsg<TransactionMqMsg> commonMqMsg = CommonMqMsg.buildMsg(TransactionMqMsg.buildMsg(businessType,businessId,data,currentUser) );
//        String jsonMsg = JSON.toJSONString(commonMqMsg, SerializerFeature.DisableCircularReferenceDetect);
//        //保存到消息表
//        return this.save(
//                ProducerMessageRecord.builder()
//                        .msgId(commonMqMsg.getMsgId())
//                        .sendStatus(SendStatus.N_SEND)
//                        .businessType(businessType)
//                        .businessId(businessId)
//                        .createTime(LocalDateTime.now())
//                        .createBy(currentUser.getId())
//                        .message(jsonMsg)
//                        .exchangeName("payment-finacial-exchange").routingKey("bookkeeping")
//                        .build() );
        return saveProducerMsgRecord(businessType,businessId,"payment-finacial-exchange","bookkeeping",data,user);

    }
    /**
     * TODO data 抽象 增加 业务id
     * @param businessType
     * @param data
     * @param user
     * @return
     */
    @Transactional
    @Override
    public boolean saveProducerMsgRecord(BusinessType businessType, String businessId,String exchangeName,String routingKey, Object data, ICurrentUser user) {
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
                        .exchangeName(exchangeName)
                        .routingKey(routingKey)
                        .build() );

    }

    /*@DubboReference(check = false, retries = 0)
    private ITestConsumerMsgService iTestConsumerMsgService;

//    @DubboReference(check = false)
//    private ITestConsumerMsgService2 iTestConsumerMsgService2;

    @DubboReference(check = false)
    private ITestConsumerMsgServiceF iTestConsumerMsgServiceF;

    @Autowired
    private TestSeataService testSeataService;

    @GlobalTransactional
    @Override
    public boolean saveProducerMsgRecord_test(String id) {
        String businessId = UUID.randomUUID().toString().replace("-", "");
        //调用结算域自己本身的服务，可以使用TCC也可以使用AT
        if (id.equals("20")){
            log.info("############## 调用自身服务，调用结算，AT模式， testSeataService ###############");
            ProducerMessageRecord entity = ProducerMessageRecord.builder()
                    .msgId(id)
                    .sendStatus(SendStatus.ACK)
                    .businessType(BusinessType.FUND_ALLOCATION)
                    .businessId(businessId)
                    .createTime(LocalDateTime.now())
                    .createBy("test")
                    .message("测试seata,借用表使用")
                    .exchangeName("payment-collection-exchange").routingKey("bookkeeping")
                    .build();
            this.save(entity);
        }else {
            log.info("########## 调用自身服务，调用结算， TCC模式， testSeataService ###########");
            testSeataService.prepare(null, id, businessId);
        }

        log.info("########### 跨服务调用,结算-->核算 Dubbo接口 有 @LocalTCC ##############");;
        iTestConsumerMsgService.prepare(null, id, businessId);

        if (id.equals("111")){
            throw new RuntimeException("测试异常回滚");
        }
        return true;
    }*/

    /**
     * 远程服务 没有使用 LocalTCC 注解
     * @param id
     * @return
     */
    /*@GlobalTransactional
    @Override
    public boolean saveProducerMsgRecord_test2(String id) {
        String businessId = UUID.randomUUID().toString().replace("-", "");
        log.info("############### 自身调用结算， TCC模式， testSeataService ################");
        testSeataService.prepare(null, id, businessId);
        log.info("############### 跨服务调用核算， 没有使用@LocalTCC注解，no localTCC ###############");
        iTestConsumerMsgServiceF.prepare(null, id, businessId);
        return true;
    }*/

    /**
     * 嵌套AT和TCC
     * @param id
     * @return
     */
    /*@Override
    public boolean saveProducerMsgRecord_test4(String id) {
        String businessId = UUID.randomUUID().toString().replace("-", "");

        log.info("############### 自身服务调用，AT模式， 普通方法 ###############");
        ProducerMessageRecord entity = ProducerMessageRecord.builder()
                .msgId(id)
                .sendStatus(SendStatus.ACK)
                .businessType(BusinessType.FUND_ALLOCATION)
                .businessId(businessId)
                .createTime(LocalDateTime.now())
                .createBy("test")
                .message("测试seata,借用表使用")
                .exchangeName("payment-collection-exchange").routingKey("bookkeeping")
                .build();
        this.save(entity);


        log.info("############# 调用核算 ###############");
        iTestConsumerMsgService.prepare(null, id, businessId);

        return true;
    }*/

    /**
     * AT 模式
     * @param id
     */
   /* @GlobalTransactional
    @Override
    public void saveProducerMsgRecord_test6(String id) {
        //本地
        String businessId = UUID.randomUUID().toString().replace("-", "");
        log.info("############### 调用结算 普通方法 ###############");
        ProducerMessageRecord entity = ProducerMessageRecord.builder()
                .msgId(id)
                .sendStatus(SendStatus.ACK)
                .businessType(BusinessType.FUND_ALLOCATION)
                .businessId(businessId)
                .createTime(LocalDateTime.now())
                .createBy("test")
                .message("测试seata,借用表使用")
                .exchangeName("payment-collection-exchange").routingKey("bookkeeping")
                .build();
        this.save(entity);
        //dubbo
        log.info("############### 跨服务调用，调用核算，AT模式， dubbo方法 ###############");
        iTestConsumerMsgServiceF.AToSave(id,businessId);
    }*/
}