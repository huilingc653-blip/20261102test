//package com.bytter.biz.bdlinks.controller;
//
//import com.bytter.biz.mq.message.service.ProducerMessageRecordService;
//import io.seata.spring.annotation.GlobalTransactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/bdlink/seata")
//public class TestSeataController {
//
//    @Autowired
//    private ProducerMessageRecordService producerMessageRecordService;
//
//    /**
//     * 本地和远程都有使用 LocalTCC 注解
//     * 测试AT+TCC混合模式
//     * @param id
//     * @return
//     */
//    @GetMapping("/test1")
//    public Boolean test(String id){
//        log.info("开始执行test1方法，id={}", id);
//
//        try {
//            producerMessageRecordService.saveProducerMsgRecord_test(id);
//            log.info("saveProducerMsgRecord_test执行完成");
//
//            log.info("############## 判断id={} ###################", id);
//            if (id.equals("2")){
//                throw new RuntimeException("测试异常回滚");
//            }
//            log.info("test1方法执行成功");
//            return true;
//        } catch (Exception e) {
//            log.error("test1方法执行异常", e);
//            throw e;
//        }
//    }
//
//    /**
//     * 远程服务 没有使用 LocalTCC 注解
//     * 分支事务会注册，但是confirm和cancel不会触发
//     * 在 id = 2 时，数据没有回滚
//     * @param id
//     * @return
//     */
//
//    @GetMapping("/test2")
//    public Boolean test2(String id){
//        producerMessageRecordService.saveProducerMsgRecord_test2(id);
//        if (id.equals("2")){
//            throw new RuntimeException("测试异常回滚");
//        }
//        return true;
//    }
//
//    /**
//     * A --> B
//     * 先调用远程服务 dubbo 接口，再调用远程服务本地的 TCC 接口; 有 LocalTCC
//     * 不添加 LocalTCC ，无法创建分支事务
//     * 也就是当使用TCC模式，跨服务调用的接口，必须要先有TCC接口；如果 B --> C 也是需要先调用C的TCC接口
//     * @param id
//     * @return
//     */
//
//
//    /**
//     * AT和TCC 混合
//     * 先调用本地save, 远程服务模拟在 commit 时异常
//     * 在commit和rollback异常，无法回滚；需要保证在 try阶段成功的情况下，commit、rollback也必须成功
//     * @param id
//     * @return
//     */
//    @GlobalTransactional
//    @GetMapping("/test4")
//    public Boolean test4(String id){
//
//        producerMessageRecordService.saveProducerMsgRecord_test4(id);
//        log.info("############## 判断id ###################");
//        if (id.equals("4")){
//            throw new RuntimeException("测试异常回滚");
//        }
//        return true;
//    }
//
//
//    /**
//     * 纯AT模式
//     * @param id
//     * @return
//     */
//    @GetMapping("/test6")
//    public Boolean test6(String id){
//        producerMessageRecordService.saveProducerMsgRecord_test6(id);
//        return true;
//    }
//}
