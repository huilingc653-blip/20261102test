//package com.bytter.biz.finacial.mq.message.service.impl;
//
//import com.bytter.biz.finacial.api.service.ITestConsumerMsgServiceF;
//import com.bytter.biz.finacial.mq.message.entity.ConsumerMessageRecord;
//import com.bytter.biz.payment.api.enums.BusinessType;
//import com.bytter.core.enums.MsgHandleStatus;
//import io.seata.rm.tcc.api.BusinessActionContext;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Slf4j
//@Service("iTestConsumerMsgServiceImplF")
//@DubboService
//public class ITestConsumerMsgServiceImplF implements ITestConsumerMsgServiceF {
//
//    @Autowired
//    private ConsumerMessageRecordServiceImpl consumerMessageRecordService;
//
//    @Transactional
//    @Override
//    public boolean prepare(BusinessActionContext context, String id, String businessId) {
//        ConsumerMessageRecord cmr = ConsumerMessageRecord.builder().msgId(id)
//                .businessType(BusinessType.FUND_ALLOCATION)
//                .businessId(businessId)
//                .message("测试seata,借用表使用")
//                .msgHandleStatus(MsgHandleStatus.F_HANDLE).createTime(LocalDateTime.now()).build();
//        consumerMessageRecordService.save(cmr);
//        log.info("################ ITestConsumerMsgServiceImplF prepare 消息插入完成 ###############");
//        if (id.equals("200")){
//            throw new RuntimeException("模拟 prepare 阶段异常");
//        }
//        return true;
//    }
//
//    @Transactional
//    @Override
//    public boolean commit(BusinessActionContext context) {
//        log.info("################# ITestConsumerMsgServiceImplF commit ###############");
//        String id = (String) context.getActionContext().get("id");
//        if (id.equals("2000")){
//            throw new RuntimeException("模拟 commit 阶段异常");
//        }
//        return true;
//    }
//
//    @Transactional
//    @Override
//    public boolean rollback(BusinessActionContext context) {
//        log.info("############## ITestConsumerMsgServiceImplF rollback ################");
//        String id = (String) context.getActionContext().get("id");
//        consumerMessageRecordService.removeById(id);
//        return true;
//    }
//
//    @Transactional
//    @Override
//    public boolean AToSave(String id, String businessId) {
//        ConsumerMessageRecord cmr = ConsumerMessageRecord.builder().msgId(id)
//                .businessType(BusinessType.FUND_ALLOCATION)
//                .businessId(businessId)
//                .message("测试seata,借用表使用")
//                .msgHandleStatus(MsgHandleStatus.Y_HANDLE).createTime(LocalDateTime.now()).build();
//        consumerMessageRecordService.save(cmr);
//        log.info("############### ITestConsumerMsgServiceImplF AToSave 消息插入完成 ##############");
//        return true;
//    }
//}
