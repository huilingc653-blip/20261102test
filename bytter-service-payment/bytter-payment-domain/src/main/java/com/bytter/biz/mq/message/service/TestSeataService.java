//package com.bytter.biz.mq.message.service;
//
//import io.seata.rm.tcc.api.BusinessActionContext;
//import io.seata.rm.tcc.api.BusinessActionContextParameter;
//import io.seata.rm.tcc.api.LocalTCC;
//import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
//
//@LocalTCC
//public interface TestSeataService {
//
//    /**
//     * name 全局唯一；commitMethod和rollbackMethod和下面一一对应；useTCCFence需要为true,全局事务才会提交
//     * @param context
//     * @param id
//     * @param businessId
//     * @return
//     */
//    @TwoPhaseBusinessAction(name = "ITestSeataService-TCC-test", commitMethod = "commit", rollbackMethod = "rollback", useTCCFence = true)
//    boolean prepare(BusinessActionContext context,
//                    @BusinessActionContextParameter(paramName = "id") String id,
//                    @BusinessActionContextParameter(paramName = "businessId") String businessId);
//
//    boolean commit(BusinessActionContext context);
//
//    boolean rollback(BusinessActionContext context);
//}
