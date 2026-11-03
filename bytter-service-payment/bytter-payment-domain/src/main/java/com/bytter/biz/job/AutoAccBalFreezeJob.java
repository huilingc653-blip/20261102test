package com.bytter.biz.job;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.entity.BtBankAccPlanFreeze;
import com.bytter.biz.inneraccount.service.BtBankAccPlanFreezeService;
import com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 定时任务-银行账户同步冻结金额
 *
 * @author fangcf
 * @date 2025-02-17
 */
@Slf4j
@Component
public class AutoAccBalFreezeJob implements ITaskService {

    @Autowired
    private BtBankAccPlanFreezeService btBankAccPlanFreezeService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;

    /**
     * 定时任务-银行账户同步冻结金额
     *
     * @param arguments 定时任务接受参数
     */
    @XxlJob("AutoAccBalFreezeJob")
    @Override
    public void run(String arguments) {
        log.info("【银行账户同步冻结金额】定时任务开始。开始时间：{}", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
        // 获取系统时间
        Date now = DateUtils.getDate();

        List<BtBankAccPlanFreezeVO> resultList = btBankAccPlanFreezeService.listAutoFreezeRecord(now);
        if (!CollectionUtils.isEmpty(resultList)) {
            // 一个账号+币别，满足冻结时间范围的，可多条
            // 开始循环自动冻结
            for (BtBankAccPlanFreezeVO item : resultList) {
                // 剩余冻结金额 = 申请冻结金额 - 实际已冻结金额
                BigDecimal subFreezeMoney = item.getApplyFreezeMoney().subtract(item.getRealFreezeMoney());
                // 获取账号可用余额
                BisAccBalDTO balDto = bisAccBalService.getLastBisAccBalBy(item.getBankAcc(), item.getCurId());
                if (EmptyUtils.isEmpty(balDto)) {
                    continue;
                }
                // 注：可用余额为已减去系统冻结余额（frz_bal银行冻结金额不用管）
                BigDecimal availBal = balDto.getAvailBal();
                // 1、当剩余冻结金额 > 银行账户可用余额且银行账户可用余额等于0时，不生成任何实际冻结单，定时任务结束
                if (subFreezeMoney.compareTo(availBal) > 0 && availBal.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                // 2、当剩余冻结金额 > 银行账户可用余额且银行账户可用余额不等于0
                if (subFreezeMoney.compareTo(availBal) > 0 && availBal.compareTo(BigDecimal.ZERO) > 0) {
                    // 部分冻结
                    BtBankAccPlanFreeze entity = new BtBankAccPlanFreeze();
                    BeanUtils.copyNotNullProperties(item, entity);
                    entity.setRealFreezeMoney(entity.getRealFreezeMoney().add(availBal));
                    entity.setFreezeStatus(BtBankAccConstant.FREEZE_STATUS_1);
                    entity.setUpdateDate(DateUtils.getDate());
                    // 生成冻结记录
                    String freezeNo = btBankAccPlanFreezeService.saveFreezeOrUnfreezeRecord(entity, availBal, BtBankAccConstant.PLAN_FREEZE);
                    entity.setFreezeNo(freezeNo);
                    // 更新计划申请表
                    btBankAccPlanFreezeService.saveOrUpdate(entity);
                    // 更新余额表中“系统冻结金额”
                    bisAccBalService.updateSysFrzBal(balDto.getId(), availBal, BtBankAccConstant.PLAN_FREEZE);

                }
                // 3、当剩余冻结金额 <= 银行账户可用余额
                if (subFreezeMoney.compareTo(availBal) <= 0) {
                    // 全部冻结
                    BtBankAccPlanFreeze entity = new BtBankAccPlanFreeze();
                    BeanUtils.copyNotNullProperties(item, entity);
                    entity.setRealFreezeMoney(entity.getApplyFreezeMoney());
                    entity.setFreezeStatus(BtBankAccConstant.FREEZE_STATUS_2);
                    entity.setUpdateDate(DateUtils.getDate());
                    // 生成冻结记录
                    String freezeNo = btBankAccPlanFreezeService.saveFreezeOrUnfreezeRecord(entity, subFreezeMoney, BtBankAccConstant.PLAN_FREEZE);
                    entity.setFreezeNo(freezeNo);
                    // 更新计划申请表
                    btBankAccPlanFreezeService.saveOrUpdate(entity);
                    // 更新余额表中“系统冻结金额”
                    bisAccBalService.updateSysFrzBal(balDto.getId(), subFreezeMoney, BtBankAccConstant.PLAN_FREEZE);
                }
            }
        }
        log.info("【银行账户同步冻结金额】定时任务结束。结束时间：{}", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
    }
}
