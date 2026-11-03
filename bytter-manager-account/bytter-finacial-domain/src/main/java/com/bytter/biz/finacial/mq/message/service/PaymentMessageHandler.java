package com.bytter.biz.finacial.mq.message.service;

import com.alibaba.fastjson.JSON;
import com.bytter.biz.finacial.api.dto.VoucherWrapperDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.service.IBisGatherCmdService;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.core.base.entity.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YonJar
 * @date 2024/3/8.
 * @description 付款/收款/外汇 消息处理
 */
@Slf4j
@Component
public class PaymentMessageHandler {

    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;

    /**
     * 业务处理
     * @param data
     */
    @Transactional
    public void handler(TransactionMqMsg data){
        BusinessType businessType = data.getBusinessType(); // 根据 type 来反序列化对象
        switch (businessType){
            case FUND_ALLOCATION:
                this.fundAllocation(data);
                break;
//            case ALLOCATION_TYPE_UP:
//            case ALLOCATION_TYPE_TURN:
//            case ALLOCATION_TYPE_ASK:
//            case ALLOCATION_TYPE_POS:
//            case ALLOCATION_TYPE_DOWN:
            default:

        }

    }

    private void fundAllocation(TransactionMqMsg data) {
        String jsonString = JSON.toJSONString(data.getMessage());
        //根据具体业务 反序列化对象
        VoucherWrapperDTO dto = JSON.parseObject(jsonString, VoucherWrapperDTO.class);
        log.info("Deserialization successful：{}",dto.toString());
//        CurrentUser user = data.getUser();
        log.info("处理 资金调拨-记账 业务逻辑：fcAccbookService.submitVoucher");
        fcAccbookService.submitVoucher(dto);
    }

}
