package com.bytter.tally.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.BtNetChalkDTO;
import com.bytter.biz.finacial.api.dto.BtNetOtherDtlDTO;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefineDTO;
import com.bytter.biz.finacial.api.dto.FcOtherBusinessRuleDefineDTO;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.CorpDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lijinquan
 * @description:总账户资金下拨流水自动记账
 * @date 2024-11-11 10:09
 */
@Service(value = "BtNetDtlDownAutoTallyService")
@Slf4j
public class BtNetDtlDownAutoTallyService implements ITaskService {

    @Autowired
    private BtNetDtlDownService btNetDtlDownService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcDayCloseService dayCloseService;

    @Override
    @XxlJob("BtNetDtlDownAutoTallyService")
    public synchronized void run(String arguments) {
        try {
            log.info("------------------------总账户下拨流水自动记账定时任务开始------------------------");
            String num = XxlJobHelper.getJobParam();
            if (StringUtils.isNotBlank(num)) Integer.parseInt(num); //一次执行多少条
            //查询待记账数据
            List<BtNetOtherDtlDTO> datas = btNetDtlDownService.queryTallyData(num);//查询
            if (!EmptyUtils.isEmptyList(datas)) {
                List<String> netIds = new ArrayList<>();
                for (BtNetOtherDtlDTO dto : datas) {
                    if (!netIds.contains(dto.getCorpId()))
                        netIds.add(dto.getCorpId());
                }
                //查询网点的记账规则Map
//                Map<String, FcOtherBusinessRuleDefineDTO> defineMap = btNetDtlDownService.getBusinessRuleDefines(netIds);
                for (BtNetOtherDtlDTO dto : datas) {
                    String message = "";
                    try {
                        CorpDTO corp = corpService.getNetByCorpId(dto.getCorpId());
                        Date closeDate = dayCloseService.getDayCloseFinalDate(corp.getNetId());
                        if (dto.getTransTime().compareTo(closeDate) <= 0) {
                            message += "交易日期必须在日结日期之后";
                        }
                        FcBusinessRuleDefineDTO ruleDefineDTO = btNetDtlDownService.getBusinessRuleExp(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
//                        FcOtherBusinessRuleDefineDTO ruleDefineDTO = defineMap.get(dto.getCorpId());
                        if (ruleDefineDTO == null) { //网点没有维护记账规则
                            message += "没有维护总账户下拨记账规则";
                        }

                        if (StringUtils.isBlank(message)) {
                            BtNetChalkDTO chalk = new BtNetChalkDTO();
                            BeanUtils.copyProperties(dto, chalk);
                            message += btNetDtlDownService.processAutoTally(chalk, ruleDefineDTO);
                        }
                    } catch (ParameterException e) {
                        message += btNetDtlDownService.getLang(e.getIMessageEnum(), e.getParams());
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
                        btNetDtlDownService.updateTallyFailMsg(dto.getId(), NbconsoleConstant.NbcEstate.E_STATE_CHECK.get(), message);
                    }
                }
            }
            log.info("------------------------总账户下拨流水自动记账定时任务结束------------------------");
        } catch (NumberFormatException e) {
            System.out.println("参数错误！");
            e.printStackTrace();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }
}
