package com.bytter.bill.domain.bill.mq.message.service;

import com.alibaba.fastjson.JSON;
import com.bytter.bill.domain.bill.service.AbAccrecordsService;
import com.bytter.bill.domain.bill.service.AbBillEndorseService;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.dto.TransactionMqMsg;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.entity.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author YonJar
 * @date 2024/4/11.
 * @description 付款 消息处理
 */
@Slf4j
@Component
public class BillEndorsementMessageHandler {

    @Autowired
    private AbBillEndorseService abBillEndorseService;

    /**
     * 业务处理
     *
     * @param data
     */
    @Transactional
    public void handler(TransactionMqMsg data) {
        BusinessType businessType = data.getBusinessType(); // 根据 type 来反序列化对象
        switch (businessType) {
            case BILL_ENDORSEMENT:
                this.billEndorsementInstructions(data);
                break;
            default:
        }
    }

    private void billEndorsementInstructions(TransactionMqMsg data) {
        String jsonString = JSON.toJSONString(data.getMessage());
        NisBillheadDTO dto = JSON.parseObject(jsonString, NisBillheadDTO.class);
        dto.setId(data.getBusinessId());
        log.info("Deserialization successful：{}", dto.toString());
        CurrentUser user = data.getUser();
        log.info("处理 自动指令 业务逻辑：abBillEndorseService.paymentBillEndorsement");
        abBillEndorseService.paymentBillEndorsement(dto, user);
    }
}
