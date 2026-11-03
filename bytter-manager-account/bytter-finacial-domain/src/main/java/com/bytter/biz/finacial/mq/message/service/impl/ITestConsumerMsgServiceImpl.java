//package com.bytter.biz.finacial.mq.message.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.bytter.biz.finacial.api.service.ITestConsumerMsgService;
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
//@Service("iTestProducerMsgServiceImpl")
//@DubboService
//public class ITestConsumerMsgServiceImpl implements ITestConsumerMsgService {
//
//    @Autowired
//    private ConsumerMessageRecordServiceImpl consumerMessageRecordService;
//
//    @Override
//    public void test(String id, String businessId) {
//
//        ConsumerMessageRecord cmr = ConsumerMessageRecord.builder().msgId(id)
//                .businessType(BusinessType.FUND_ALLOCATION)
//                .businessId(businessId)
//                .message("测试seata,借用表使用")
//                .msgHandleStatus(MsgHandleStatus.F_HANDLE).createTime(LocalDateTime.now()).build();
//        consumerMessageRecordService.save(cmr);
//        log.info("############# consumerMsg消息插入完成 ##############");
//    }
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
//        log.info("############## ITestConsumerMsgServiceImpl prepare  ##############");
//        if (id.equals("200")){
//            throw new RuntimeException("模拟 prepare 阶段异常");
//        }
//        return true;
//    }
//
//    @Transactional
//    @Override
//    public boolean commit(BusinessActionContext context) {
//        log.info("############ ITestConsumerMsgServiceImpl commit  ################");
//        String id = (String) context.getActionContext().get("id");
//        ConsumerMessageRecord cmr = consumerMessageRecordService.getById(id);
//        cmr.setMsgHandleStatus(MsgHandleStatus.Y_HANDLE);
//        consumerMessageRecordService.updateById(cmr);
//        if (id.equals("2000")){
//            throw new RuntimeException("模拟 commit 阶段异常"); //commit阶段的回滚，只能手工人为接入处理；因为TCC模式下，当 try成功，那么confirm必须要成功
//        }
//        return true;
//    }
//
//    @Transactional
//    @Override
//    public boolean rollback(BusinessActionContext context) {
//        log.info("############# ITestConsumerMsgServiceImpl rollback  ###############");
//        String id = (String) context.getActionContext().get("id");
//        consumerMessageRecordService.removeById(id);
//        if (id.equals("20000")){
//            throw new RuntimeException("模拟 rollback 阶段异常");
//        }
//        return true;
//    }
//}
