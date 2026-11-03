//package com.bytter.biz.bdlinks.service;
//
//import com.bytter.biz.bdlinks.entity.BisExc;
//import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
//import com.bytter.biz.bdlinks.entity.BisForeignExc;
//import com.bytter.biz.bdlinks.entity.BisGatherCmd;
//import com.bytter.core.common.queue.CommonMqMsg;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.function.StreamBridge;
//import org.springframework.stereotype.Service;
//import org.springframework.util.MimeType;
//
///**
// * 消息发送 ; 弃用
// */
//@Service
//public class BisMQService {
//
//    @Autowired
//    private StreamBridge streamBridge;
//
//    /**
//     * 发送付款指令变更消息
//     */
///*    public void sendBisExcChangeMsg(BisExc bisExc,String voucherStat) {
//        BisExcChangeMsg bisExcChangeMsg = BisExcChangeMsg.builder().cbsBillId(bisExc.getCbsBillId())
//                .voucherStat(voucherStat)
//                .voucherFrom(bisExc.getVoucherFrom()).build();
//        CommonMqMsg<BisExcChangeMsg> commonMqMsg = CommonMqMsg.buildMsg(CommonMqMsg.Type.BIS_EXC, bisExcChangeMsg);
//        streamBridge.send("bis-exc-to-payment-out-0", commonMqMsg, MimeType.valueOf("application/json"));
//    }*/
//
//    /**
//     * 发送收款指令变更消息
//     */
//    /*public void sendBisGatherExcChangeMsg(BisGatherCmd bisGatherCmd, String voucherStat) {
//        BisExcChangeMsg bisExcChangeMsg = BisExcChangeMsg.builder().cbsBillId(bisGatherCmd.getCbsBillId())
//                .voucherStat(voucherStat)
//                .voucherFrom(bisGatherCmd.getVoucherFrom()).build();
//        CommonMqMsg<BisExcChangeMsg> commonMqMsg = CommonMqMsg.buildMsg(CommonMqMsg.Type.BIS_GATHER_EXC, bisExcChangeMsg);
//        streamBridge.send("bis-exc-to-payment-out-0", commonMqMsg, MimeType.valueOf("application/json"));
//    }*/
//
//    /**
//     * 发送外汇指令变更消息
//     */
//    /*public void sendBisForeignExcChangeMsg(BisForeignExc bisForeignExc, String voucherStat) {
//        BisExcChangeMsg bisExcChangeMsg = BisExcChangeMsg.builder().cbsBillId(bisForeignExc.getCbsBillId())
//                .voucherStat(voucherStat)
//                .voucherFrom(bisForeignExc.getVoucherFrom())
//                .build();
//        CommonMqMsg<BisExcChangeMsg> commonMqMsg = CommonMqMsg.buildMsg(CommonMqMsg.Type.BIS_FOREIGN_EXC, bisExcChangeMsg);
//        streamBridge.send("bis-exc-to-payment-out-0", commonMqMsg, MimeType.valueOf("application/json"));
//    }*/
//
//    /**
//     * 发送理财指令变更消息 TODO 理财在 increment 不在此处
//     */
//    /*public void sendBisFinanceExcChangeMsg(BisFinanceExc bisFinanceExc, String voucherStat) {
//        BisExcChangeMsg bisExcChangeMsg = BisExcChangeMsg.builder().cbsBillId(bisFinanceExc.getCbsBillId())
//                .voucherStat(voucherStat)
//                .build();
//        CommonMqMsg<BisExcChangeMsg> commonMqMsg = CommonMqMsg.buildMsg(CommonMqMsg.Type.BIS_FINANCE_EXC, bisExcChangeMsg);
//        streamBridge.send("bis-exc-to-payment-out-0", commonMqMsg, MimeType.valueOf("application/json"));
//    }*/
//}
