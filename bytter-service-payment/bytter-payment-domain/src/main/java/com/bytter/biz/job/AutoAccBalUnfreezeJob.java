package com.bytter.biz.job;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccPlanFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务-银行账户资金自动解冻
 *
 * @author fangcf
 * @date 2025-02-17
 */
@Slf4j
@Component
public class AutoAccBalUnfreezeJob implements ITaskService {

    @Autowired
    private BtBankAccPlanFreezeService btBankAccPlanFreezeService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;

    /**
     * 定时任务-银行账户资金自动解冻
     *
     * @param arguments 定时任务接受参数
     */
    @XxlJob("AutoAccBalUnfreezeJob")
    @Override
    public void run(String arguments) {
        log.info("【银行账户资金自动解冻】定时任务开始======");
        // 获取系统时间
        Date now = DateUtils.getDate();
        List<BtBankAccPlanFreezeVO> resultList = btBankAccPlanFreezeService.listAutoUnfreezeRecord(now);
        if (!CollectionUtils.isEmpty(resultList)) {
            // 开始循环自动解冻
            for (BtBankAccPlanFreezeVO item : resultList) {
                BtBankAccPlanFreeze entity = new BtBankAccPlanFreeze();
                BeanUtils.copyNotNullProperties(item, entity);
                //已解冻
                entity.setIsUnfreeze(CommonConstant.YES);
                entity.setUpdateDate(DateUtils.getDate());
                // 更新计划申请表
                btBankAccPlanFreezeService.saveOrUpdate(entity);
                // 更新余额表中“系统冻结金额”
                // 获取账号可用余额
                BisAccBalDTO balDto = bisAccBalService.getLastBisAccBalBy(item.getBankAcc(), item.getCurId());
                if (EmptyUtils.isEmpty(balDto)) {
                    continue;
                }
                bisAccBalService.updateSysFrzBal(balDto.getId(), entity.getRealFreezeMoney(), BtBankAccConstant.PLAN_UNFREEZE);
                // 生成冻结记录
                // 这里如果是解冻的话 需要将原先的冻结金额 里面的 已解冻金额做修改
                btBankAccPlanFreezeService.saveUnfreezeRecord(entity,entity.getFreezeNo(),entity.getRealFreezeMoney());
//                btBankAccPlanFreezeService.saveFreezeOrUnfreezeRecord(entity, entity.getRealFreezeMoney(), BtBankAccConstant.PLAN_UNFREEZE);
            }
        }
        log.info("【银行账户资金自动解冻】定时任务结束。");
    }
}
