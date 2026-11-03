package com.bytter.tally.service;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.BtNetChalkDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.tally.entity.TallyBtNetChalk;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
@Service("btNetChalkAutoTallyService")
@Slf4j
public class BtNetChalkAutoTallyService implements ITaskService {
    @DubboReference(check = false)
    private ITallyService tallyService;
    @Autowired
    private BtNetChalkService btNetChalkService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcDayCloseService dayCloseService;
    @Autowired
    private BtNetOtherReceiveDtlService btNetOtherReceiveDtlService;

    @Override
    @XxlJob("BtNetChalkAutoTallyService")
    public void run(String arguments) {
        try {
            log.info("进入银企接口收款记账定时任务");
            String num = XxlJobHelper.getJobParam();
            if(StringUtils.isNotBlank(num)) Integer.parseInt(num); //一次执行多少条
            //查询待记账数据
            List<BtNetChalkDTO> datas = tallyService.queryTallyData(FinacialConstant.BOOLEAN_TRUE,num);//查询
            if (!EmptyUtils.isEmptyList(datas)) {
                TallyBtNetChalk chalk;
                for (BtNetChalkDTO netChalk : datas) {
                    String message = "";
                    try {
                        CorpDTO corp = corpService.getNetByCorpId(netChalk.getCorpId());
                        Date closeDate = dayCloseService.getDayCloseFinalDate(corp.getNetId());
                        if (netChalk.getTransTime().compareTo(closeDate) <= 0) {
                            message += "交易日期必须在日结日期之后";
                        }
                        if (StringUtils.isBlank(message)) {
                            chalk = new TallyBtNetChalk();
                            BeanUtils.copyNotNullProperties(netChalk, chalk);
                            message += btNetChalkService.processAutoTally(chalk);
                        }
                    }  catch (ParameterException e) {
                        message += btNetOtherReceiveDtlService.getLang(e.getIMessageEnum(), e.getParams());
                        e.printStackTrace();
                    } catch (Exception e) {
                        StringBuilder logStr = new StringBuilder();
                        logStr.append(DateUtils.format(new Date()) + "-[ERROR]" + "错误信息：" + e.getMessage()+ e.toString()).append("\n");
                        StackTraceElement[] trace = e.getStackTrace();
                        for (int i = 0; i < trace.length; i++) {
                            logStr.append(DateUtils.format(new Date()) + "-[ERROR]" + "\tat：" + trace[i]).append("\n");
                        }
                        message += logStr.toString();
                        log.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                    //记录自动记账失败原因
                    if (!StringUtils.isBlank(message)) {
                        btNetOtherReceiveDtlService.updateTallyFailMsg(netChalk.getId(), NbconsoleConstant.NbcEstate.E_STATE_CHECK.get(), message);
                    }
                }
            }
        }catch (NumberFormatException e){
            System.out.println("参数错误！");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
