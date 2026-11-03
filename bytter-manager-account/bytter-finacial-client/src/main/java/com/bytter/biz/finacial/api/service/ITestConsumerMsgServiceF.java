//package com.bytter.biz.finacial.api.service;
//
//import io.seata.rm.tcc.api.BusinessActionContext;
//import io.seata.rm.tcc.api.BusinessActionContextParameter;
//import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
//
///**
// * 没有加LocalTCC 也会有注册
// */
//public interface ITestConsumerMsgServiceF {
//
//    /**
//     * name 全局唯一；commitMethod和rollbackMethod和下面一一对应；useTCCFence需要为true,全局事务才会提交
//     * @param context
//     * @param id
//     * @param businessId
//     * @return
//     */
//    @TwoPhaseBusinessAction(name = "ITestConsumerMsgServiceF-TCC-test", commitMethod = "commit", rollbackMethod = "rollback", useTCCFence = true)
//    boolean prepare(BusinessActionContext context,
//                    @BusinessActionContextParameter(paramName = "id") String id,
//                    @BusinessActionContextParameter(paramName = "businessId") String businessId);
//
//    boolean commit(BusinessActionContext context);
//
//    boolean rollback(BusinessActionContext context);
//
//    /**
//     * 测试跨服务调用 AT 模式
//     * @param id
//     * @param businessId
//     * @return
//     */
//    boolean AToSave(String id, String businessId);
//}
