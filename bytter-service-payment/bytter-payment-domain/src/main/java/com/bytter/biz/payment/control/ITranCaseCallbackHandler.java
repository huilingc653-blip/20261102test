package com.bytter.biz.payment.control;

import com.bytter.biz.payment.api.dto.TranCaseCallbackDTO;

/**
 * 落地限额处理单（BT_TR_LIMIT_CASE）回调处理 SPI（企业端各来源业务实现，通用）
 * <p>
 * 企业端所有会触发落地限额的来源业务（资金收付交易、付款办理等）各自实现本接口，
 * 登记各自的 {@link #sourceBizType()}；MQ 消费端按 dto 的 sourceBizType
 * 找到对应实现，执行审批通过/打回收尾。
 *
 * @author zhukr
 */
public interface ITranCaseCallbackHandler {

    /**
     * 来源业务类型（BT_TR_LIMIT_CASE.SOURCE_BIZ_TYPE），如 fexReq=资金收付交易 / pay=付款办理 / wage=代发工资。
     * 回调门面按该值分发到对应处理器。
     */
    String sourceBizType();

    /**
     * 按处理单结果执行该来源业务的企业端收尾（审批通过/打回）。
     *
     * @param dto 落地限额处理单结果（含业务对应ID/审批状态/租户ID等）
     */
    void handle(TranCaseCallbackDTO dto);
}
