package com.bytter.biz.job;

import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.biz.wage.service.NisWageGenerateDemandService;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 新增类自动生成下拨指令
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/11/24
 */
@Component
public class SalaryAllocationFundsJob implements ITaskService {
    @Autowired
    private NisWageGenerateDemandService nisWageGenerateDemandService;

    @XxlJob("SalaryConfigAllocationFundsJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("开始运行：自动根据代发工资配置类需求单生成下拨指令。");
        nisWageGenerateDemandService.demandGenerateDownExc(NisDemandConstant.DEMANDTYPE_04);
        XxlJobHelper.log("自动根据代发工资配置类需求单生成下拨指令，运行结束。");
    }

    @XxlJob("SalaryTableAllocationFundsJob")
    public void runTable(String arguments) {
        XxlJobHelper.log("开始运行：自动根据代发工资单据类需求单生成下拨指令。");
        nisWageGenerateDemandService.demandGenerateDownExc(NisDemandConstant.DEMANDTYPE_05);
        XxlJobHelper.log("自动根据代发工资单据类需求单生成下拨指令，运行结束。");
    }
}
