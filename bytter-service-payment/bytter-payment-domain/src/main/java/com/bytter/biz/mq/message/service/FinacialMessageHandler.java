package com.bytter.biz.mq.message.service;

import com.alibaba.fastjson.JSON;
import com.bytter.biz.finacial.api.dto.VoucherWrapperDTO;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.entity.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YonJar
 * @date 2024/4/11.
 * @description 付款 消息处理
 */
@Slf4j
@Component
public class FinacialMessageHandler {

    @DubboReference(check = false)
    private IBisExcService bisExcService;

    /**
     * 业务处理
     * @param data
     */
    @Transactional
    public void handler(TransactionMqMsg data){
        BusinessType businessType = data.getBusinessType(); // 根据 type 来反序列化对象
        switch (businessType){
            case AUTO_PAYMENT_INSTRUCTIONS:
                this.autoPaymentInstructions(data);
                break;
            case MANUAL_PAYMENT_INSTRUCTIONS:
                this.manualPaymentInstructions(data);
                break;
//            case ALLOCATION_TYPE_UP:
//            case ALLOCATION_TYPE_TURN:
//            case ALLOCATION_TYPE_ASK:
//            case ALLOCATION_TYPE_POS:
//            case ALLOCATION_TYPE_DOWN:
            default:

        }

    }

    private void autoPaymentInstructions(TransactionMqMsg data) {
        String jsonString = JSON.toJSONString(data.getMessage());
        BisExcDTO dto = JSON.parseObject(jsonString, BisExcDTO.class);
        log.info("Deserialization successful：{}",dto.toString());
        CurrentUser user = data.getUser();
        log.info("处理 自动指令 业务逻辑：bisExcService.autoSendBisExc");
        bisExcService.autoSendBisExc(dto, user);
    }

    private void manualPaymentInstructions(TransactionMqMsg data) {
        String jsonString = JSON.toJSONString(data.getMessage());
        BisExcDTO dto = JSON.parseObject(jsonString, BisExcDTO.class);
        log.info("Deserialization successful：{}",dto.toString());
        CurrentUser user = data.getUser();
        log.info("处理 手动指令 业务逻辑：bisExcService.addBisExc");
        bisExcService.addBisExc(dto, user);
    }


}
