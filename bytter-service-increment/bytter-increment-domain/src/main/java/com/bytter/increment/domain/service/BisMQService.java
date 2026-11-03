/*
package com.bytter.increment.domain.service;

import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.increment.api.dto.BisExcChangeMsg;
import com.bytter.increment.domain.entity.BisFinanceExc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;


*/
/**
 * 消息发送 ;弃用--如果使用 需要配置生产者；消费者 在payment：bisExcEvent-in-0，监听函数：bisExcEvent
 *//*

@Service
public class BisMQService {

    @Autowired
    private StreamBridge streamBridge;


    */
/**
     * 发送理财指令变更消息
     *//*

    public void sendBisFinanceExcChangeMsg(BisFinanceExc bisFinanceExc, String voucherStat) {
        BisExcChangeMsg bisExcChangeMsg = BisExcChangeMsg.builder().cbsBillId(bisFinanceExc.getCbsBillId())
                .voucherStat(voucherStat)
                .build();
        CommonMqMsg<BisExcChangeMsg> commonMqMsg = CommonMqMsg.buildMsg(CommonMqMsg.Type.BIS_FINANCE_EXC, bisExcChangeMsg);
        streamBridge.send("bis-exc-to-payment-out-0", commonMqMsg, MimeType.valueOf("application/json"));
    }
}
*/
