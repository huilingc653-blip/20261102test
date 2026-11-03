package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.biz.wage.service.NisWageGenerateDemandService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 工资单据自动生成需求清单
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/11/23
 */
@Component
public class SalaryTableGenerateDemandJob implements ITaskService {
    @DubboReference(check = false)
    private ICalendarService iCalendarService;
    @Autowired
    private NisWageGenerateDemandService nisWageGenerateDemandService;
    @DubboReference(check = false)
    private IConfigService iConfigService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;

    @XxlJob("SalaryTableGenerateDemandJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("开始运行：工资单据自动生成需求清单。");
        // 当前日期
        String currentDate = DateUtils.formatDate(new Date());
        // 需求日期
        String demandDate = "";
        // 发薪日
        Integer salaryDay = iConfigService.getConfig("wage", "demandDate").toInteger(15);

        Date time15 = DateUtils.parseDate(DateUtils.format(new Date(),"yyyy-MM")+"-"+salaryDay);
        Integer workDaySign = iCalendarService.getWorkDaySignByTime(time15);
        if (ObjectUtil.isNotEmpty(workDaySign) && 1 == workDaySign) {
            demandDate = DateUtils.formatDate(time15);
        }else{
            // 从15号开始向前查询一个工作日作为需求日期
            Date prevWorkDay = iCalendarService.getPrevWorkDay(time15);
            if (ObjectUtil.isNotEmpty(prevWorkDay)){
                demandDate = DateUtils.formatDate(prevWorkDay);
            }
        }

        // 判断当前日期是否等于demandDate日期。如果是则生成生成需求清单 否则不执行
        if (currentDate.equals(demandDate)) {
            XxlJobHelper.log("当前时间:{}是工作日，开始生成需求清单。", demandDate);

            // 增加账号是否参与资金需求判断
            List<BtBankAccDTO> btBankAccDTOList = iBtBankAccService.getBankAccList();
            List<String> accIdList = btBankAccDTOList.stream().filter(btBankAccDTO -> CommonConstant.YES.equals(btBankAccDTO.getIsDemand())).map(BtBankAccDTO::getId).collect(Collectors.toList());

            // 查询待生成清单数据
            List<NisDemandVO> dataList = nisWageGenerateDemandService.listSalaryTableData(new Date(),accIdList);
            if (ObjectUtil.isNotEmpty(dataList)){
                // 设置日期为当前日期
                dataList.stream().forEach(data -> {data.setDemandDate(DateUtils.parseDate(new Date()));});
                // 生成需求清单
                nisWageGenerateDemandService.generateDemand(dataList, NisDemandConstant.DEMANDTYPE_05);
            }else{
                XxlJobHelper.log("没有数据需要生成需求清单。");
            }
        }else{
            XxlJobHelper.log("生成资金需求清单任务停止：当前日期非15号或者前一个工作日。");
        }
    }
}
