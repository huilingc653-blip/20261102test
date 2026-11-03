package com.bytter.biz.comsumer;

import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.core.common.queue.CommonMqMsg;
import com.bytter.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;

/**
 * 弃用
 * 付款指令、收款指令、外汇状态、理财指令 变更时 的消费者
 */
@Configuration
@Slf4j
public class BisExcStatusChangeEvent {
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private NisBillheadService billheadService;
    @Autowired
    private NisWageService nisWageService;
    @Autowired
    private NisAllocationReqService nisAllocationReqService;
    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Bean
    public Consumer<CommonMqMsg<BisExcChangeMsg>> bisExcEvent() {
        return this::process;
    }

    public void process(CommonMqMsg<BisExcChangeMsg> event) {
        transactionTemplate.execute(transactionStatus -> {
            log.info("接收到指令状态变更消息：：{}", JsonUtils.toJSONString(event));
            BisExcChangeMsg bisExcChangeMsg = event.getData();
            CommonMqMsg.Type type = event.getType();
            try {
                switch (type) {
                    case BIS_EXC:
                        processBisExcChangeMsg(bisExcChangeMsg);
                        break;
                    case BIS_GATHER_EXC:
                        processBisGatherExcChangeMsg(bisExcChangeMsg);
                        break;
                    case BIS_FOREIGN_EXC:
                        processBisForeignExcChangeMsg(bisExcChangeMsg);
                        break;
                }
            } catch (Exception exception) {
                log.error("业务数据处理异常", exception);
                transactionStatus.setRollbackOnly();
                //todo 异常数据记录手动补偿 消息日志
                return false;
            }
            return true;
        });
    }

    /**
     * 收款指令状态变更
     */
    private void processBisExcChangeMsg(BisExcChangeMsg bisExcChangeMsg){
        switch (bisExcChangeMsg.getVoucherFrom()) {
            case BdlinkConstant.FROM_NIS_PAY:
                billheadService.updateFromBisExcChange(bisExcChangeMsg);
                break;
            case BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN:
            case BdlinkConstant.FROM_ALLOCATION_TYPE_ASK:
            case BdlinkConstant.FROM_ALLOCATION_TYPE_POS:
                nisAllocationReqService.updateFromBisExcChange(bisExcChangeMsg);
                break;
            case BdlinkConstant.FROM_NIS_WAGE:
                nisWageService.updateFromBisExcChange(bisExcChangeMsg);
                break;
            default:
                break;
        }
    }

    private void processBisGatherExcChangeMsg(BisExcChangeMsg bisExcChangeMsg) {

    }

    private void processBisForeignExcChangeMsg(BisExcChangeMsg bisExcChangeMsg) {
        fexCashFlowService.updateVoucherState(bisExcChangeMsg);
    }

}
