//package com.bytter.biz.netbank.service;
//
//import com.bytter.biz.netbank.entity.NisBillhead;
//import com.bytter.biz.netbank.vo.NisBillheadVO;
//import com.bytter.biz.payment.api.dto.NisBillheadDTO;
//import com.bytter.core.base.entity.ICurrentUser;
//import io.seata.rm.tcc.api.BusinessActionContext;
//import io.seata.rm.tcc.api.BusinessActionContextParameter;
//import io.seata.rm.tcc.api.LocalTCC;
//import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
//
///**
// * create by LuoYJ on 2026/2/5.
// * 本服务端，注解可写在 interface；Dubbo接口则写在实现类中
// */
//@LocalTCC
//public interface INisBillheadTCCService {
//
//
//    /**
//     * TCC：自动审批
//     * 自动审批和手动审批无法共用，需要分开写
//     * name = 全局唯一
//     * BusinessActionContext actionContext 参数，可不填
//     * @param id
//     * @param nisBillheadVO
//     * @param user
//     * @return
//     */
//    @TwoPhaseBusinessAction(name = "INisBillheadTCCService-save", commitMethod = "commitAuto", rollbackMethod = "rollbackAuto", useTCCFence = true)
//    String prepareAuto(
//                   @BusinessActionContextParameter(paramName = "id") String id,
//                   @BusinessActionContextParameter(paramName = "sysVoucherCode") String sysVoucherCode,
//                   @BusinessActionContextParameter(paramName = "nisBillheadVO") NisBillheadVO nisBillheadVO,
//                   @BusinessActionContextParameter(paramName = "user") ICurrentUser user);
//    boolean commitAuto(BusinessActionContext actionContext);
//
//    boolean rollbackAuto(BusinessActionContext actionContext);
//
//
//}
