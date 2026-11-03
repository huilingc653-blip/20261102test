//package com.bytter.biz.mq.message.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.bytter.biz.mq.message.dao.ProducerMessageRecordMapper;
//import com.bytter.biz.mq.message.entity.ProducerMessageRecord;
//import com.bytter.biz.mq.message.service.ProducerMessageRecordService;
//import com.bytter.biz.mq.message.service.TestSeataService;
//import com.bytter.biz.payment.api.enums.BusinessType;
//import com.bytter.core.enums.SendStatus;
//import io.seata.rm.tcc.api.BusinessActionContext;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//
//@Slf4j
//@Service
//public class TestSeataServiceImpl implements TestSeataService {
//
//    @Resource
//    private ProducerMessageRecordMapper producerMessageRecordMapper;
//
//    @Transactional
//    @Override
//    public boolean prepare(BusinessActionContext context, String id, String businessId) {
//        log.info("############# TestSeataServiceImpl prepare ################");
//        ProducerMessageRecord entity = ProducerMessageRecord.builder()
//                .msgId(id)
//                .sendStatus(SendStatus.ACK)
//                .businessType(BusinessType.FUND_ALLOCATION)
//                .businessId(businessId)
//                .createTime(LocalDateTime.now())
//                .createBy("test")
//                .message("测试seata,借用表使用")
//                .exchangeName("payment-collection-exchange").routingKey("bookkeeping")
//                .build();
//        producerMessageRecordMapper.insert(entity);
//        if (id.equals("11")){
//            throw new RuntimeException("测试异常回滚");
//        }
//        return true;
//    }
//
//    @Transactional
//    @Override
//    public boolean commit(BusinessActionContext context) {
//        log.info("############# TestSeataServiceImpl commit ################");
//        String id = (String) context.getActionContext().get("id");
//        LambdaUpdateWrapper<ProducerMessageRecord> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(ProducerMessageRecord::getMsgId, id);
//        updateWrapper.set(ProducerMessageRecord::getMessage, "测试seata,借用表使用,commit成功");
//        producerMessageRecordMapper.update(null, updateWrapper);
//        return true;
//    }
//
//    @Transactional
//    @Override
//    public boolean rollback(BusinessActionContext context) {
//        log.info("############# TestSeataServiceImpl rollback ################");
//        String id = (String) context.getActionContext().get("id");
//        producerMessageRecordMapper.deleteById(id);
//        return true;
//    }
//}
