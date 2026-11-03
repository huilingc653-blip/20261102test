package com.bytter.biz.job;

import cn.hutool.core.util.NumberUtil;
import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccPlanFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.core.constant.CommonConstant;
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
 * 定时任务-冻结申请全部冻结成功提醒
 *
 * @author fangcf
 * @date 2025-02-17
 */
@Slf4j
@Component
public class AutoAccBalAllFreezeNoticeJob implements ITaskService {

    @Autowired
    private BtBankAccPlanFreezeService btBankAccPlanFreezeService;
    @DubboReference(check = false)
    private INotificationService notificationService;

    /**
     * 定时任务-冻结申请全部冻结成功提醒
     * <p>
     * 满足以下条件提醒：
     * 单据状态=“完成”；
     * 冻结状态=“全部冻结”
     * 冻结申请单的“实际已冻结金额”=“申请冻结金额”；
     * 是否冻结完成提醒=0；
     * 是否解冻=0；
     * <p>
     * 提醒内容如下：
     * 该笔账户冻结申请已全部冻结成功，请关注。
     * 计划冻结单号：XXXX
     * 单位：XXXX
     * 冻结账号：XXX
     * 冻结账户开户行：（具体开户行）
     * 申请冻结金额：XXX
     * 已冻结金额：XXX
     * 未冻结金额：XXX
     *
     * @param arguments 定时任务接受参数
     */
    @XxlJob("AutoAccBalAllFreezeNoticeJob")
    @Override
    public void run(String arguments) {
        log.info("【冻结申请全部冻结成功提醒】定时任务开始======");

        // 查询符合全部冻结成功的记录
        List<BtBankAccPlanFreezeVO> resultList = btBankAccPlanFreezeService.listAllFreezeSuccessRecord();

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
                notificationService.sendToUser("", item.getCreateBy(), NotificationConstant.NETBANK.BANKACC_ALL_FREEZE_NOTICE, paramMap);

                // 提醒成功后，将该笔计划冻结申请单的“是否冻结完成提醒”修改为1（已提醒），避免全部冻结成功后还重复提醒
                BtBankAccPlanFreeze entity = btBankAccPlanFreezeService.get(item.getId(), true);
                entity.setIsFreezeComplete(CommonConstant.YES);
                entity.setUpdateBy(item.getCreateBy());
                entity.setUpdateDate(DateUtils.getDate());
                btBankAccPlanFreezeService.saveOrUpdate(entity);
            });
        }
        log.info("【冻结申请全部冻结成功提醒】定时任务结束。");
    }
}
