package com.bytter.biz.job;

import cn.hutool.core.util.NumberUtil;
import com.bytter.biz.inneraccount.service.BtBankAccPlanFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 定时任务-冻结申请无冻结金额提醒
 *
 * @author xiay
 * @date 2025-10-15
 */
@Slf4j
@Component
public class AutoAccBalNoFreezeNoticeJob implements ITaskService {

    @Autowired
    private BtBankAccPlanFreezeService btBankAccPlanFreezeService;
    @DubboReference(check = false)
    private INotificationService notificationService;

    /**
     * 定时任务-冻结申请无冻结金额提醒
     * <p>
     单据状态=“完成”；
     冻结状态=“未冻结”；
     当前时间在“冻结开始时间”和“冻结结束时间”之间；
     冻结申请单的“实际已冻结金额”=0；
     是否解冻=“否”；
     * <p>
     该笔账户冻结申请无资金可冻结，请及时补款。
     单位：XXXX
     冻结账号：XXX
     冻结账户开户行：（具体开户行）
     申请冻结金额：XXX
     已冻结金额：XXX
     未冻结金额：XXX
     *
     * @param arguments 定时任务接受参数
     */
    @XxlJob("AutoAccBalNoFreezeNoticeJob")
    @Override
    public void run(String arguments) {
        log.info("【冻结申请无冻结金额提醒】定时任务开始======");

        // 查询符合部分冻结成功的记录
        List<BtBankAccPlanFreezeVO> resultList = btBankAccPlanFreezeService.listNoFreezeSuccessRecord(DateUtils.getDate());

        if (!CollectionUtils.isEmpty(resultList)) {
            resultList.forEach(item -> {
                Map<String, Object> paramMap = Maps.newHashMap();
                paramMap.put("planFreezeNo", item.getPlanFreezeNo());
                paramMap.put("corpName", item.getCorpName());
                paramMap.put("bankAcc", item.getBankAcc());
                paramMap.put("bankName", item.getBankName());
                paramMap.put("applyFreezeMoney", NumberUtil.decimalFormat("#,##0.00", item.getApplyFreezeMoney().doubleValue()));
                paramMap.put("realFreezeMoney", NumberUtil.decimalFormat("#,##0.00", item.getRealFreezeMoney().doubleValue()));
                BigDecimal subFreezeMoney = item.getApplyFreezeMoney().subtract(item.getRealFreezeMoney());
                paramMap.put("subFreezeMoney", NumberUtil.decimalFormat("#,##0.00", subFreezeMoney.doubleValue()));
                notificationService.sendToUser("", item.getCreateBy(), NotificationConstant.NETBANK.BANKACC_NO_FREEZE_NOTICE, paramMap);
            });
        }

        log.info("【冻结申请无冻结金额提醒】定时任务结束。");
    }
}
