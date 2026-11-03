package com.bytter.biz.job;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 定时任务-直联账户系统冻结金额同步
 *
 * @author fangcf
 * @date 2025-03-27
 */
@Slf4j
@Component
public class AutoSyncSysFrzBalJob implements ITaskService {

    @DubboReference(check = false)
    private IBtBankAccService btBankAccServiceImpl;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @Autowired(required = false)
    @Qualifier(value = "asyncThreadPoolExecutor")
    private ThreadPoolTaskExecutor asyncThreadPoolExecutor;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;


    /**
     * 定时任务-直联账户系统冻结金额同步
     *
     * @param arguments 定时任务接受参数
     */
    @XxlJob("AutoSyncSysFrzBalJob")
    @Override
    public void run(String arguments) {
        log.info("【直联账户系统冻结金额同步】定时任务开始。开始时间：" + DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
        List<BtBankAccDTO> list = btBankAccServiceImpl.getBankAccListByIsOnline(BtBankAccConstant.ISONLINE_1);

        // 多线程异步同步系统冻结金额，同步前一天的系统冻结金额
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (BtBankAccDTO btBankAccDTO : list) {
            log.info("同步系统冻结金额的账号：" + btBankAccDTO.getBankAcc());
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> updateSysFrzBal(btBankAccDTO), asyncThreadPoolExecutor);
            futures.add(future);
        }
        // 使用CompletableFuture.allOf等待所有任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.info("【直联账户系统冻结金额同步】定时任务结束。结束时间：" + DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
    }

    /**
     * 更新系统冻结金额
     *
     * @param dto 账号DTO
     */
    private void updateSysFrzBal(BtBankAccDTO dto) {
        // 获取系统时间
        Date now = DateUtils.getDate();
        //判断当日是否有余额
        BisAccBalDTO balCurr = bisAccBalService.getBisAccBalBy(dto.getBankAcc(), dto.getCurId(), now, true);
        if (!EmptyUtils.isEmpty(balCurr)) {
            //获取前一天的余额
            BisAccBalDTO balPre = bisAccBalService.getBisAccBalBy(dto.getBankAcc(), dto.getCurId(), DateUtils.addDay(now, -1), true);
            if (!EmptyUtils.isEmpty(balPre)) {
                log.info("当日有余额，余额日期为：" + DateUtils.formatDate(now) + "。同步前一天的系统冻结金额为：" + balPre.getSysFrzBal());
                // 更新当日余额中“系统冻结金额”字段，更改的值为前一天的系统冻结金额
                bisAccBalService.updateSysFrzBal(balCurr.getId(), balPre.getSysFrzBal(), null);
            }
        } else {
            // 获取账户最新余额
            BisAccBalDTO balLast = bisAccBalService.getLastBisAccBalBy(dto.getBankAcc(), dto.getCurId());
            if (!EmptyUtils.isEmpty(balLast)) {
                Date lastDate = balLast.getBalDate();
                //获取前一天的余额
                BisAccBalDTO balPre = bisAccBalService.getBisAccBalBy(dto.getBankAcc(), dto.getCurId(), DateUtils.addDay(lastDate, -1), true);
                if (!EmptyUtils.isEmpty(balPre)) {
                    log.info("当日没有余额，最新的余额日期为：" + DateUtils.formatDate(lastDate) + "。同步前一天的系统冻结金额为：" + balPre.getSysFrzBal());
                    // 更新当日余额中“系统冻结金额”字段，更改的值为前一天的系统冻结金额
                    bisAccBalService.updateSysFrzBal(balLast.getId(), balPre.getSysFrzBal(), null);
                }
            }
        }
    }

}
