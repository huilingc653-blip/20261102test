package com.bytter.biz.job;

import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 自动根据需求单生成下拨指令
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/8/12
 */
@Component
public class AutoAllocationNisDemandJob implements ITaskService {
    @Autowired
    private NisDemandService nisDemandService;

    @XxlJob("AutoAllocationNisDemandForNisJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("开始运行：自动根据付款类需求单生成下拨指令。");
        nisDemandService.demandGenerateDownExc(NisDemandConstant.DEMANDTYPE_01);
        XxlJobHelper.log("自动根据付款类需求单生成下拨指令，运行结束。");
    }

    @XxlJob("AutoAllocationNisDemandForBillJob")
    public void runBill(String arguments) {
        XxlJobHelper.log("开始运行：自动根据承兑到期类需求单生成下拨指令。");
        nisDemandService.demandGenerateDownExc(NisDemandConstant.DEMANDTYPE_02);
        XxlJobHelper.log("自动根据承兑到期类需求单生成下拨指令，运行结束。");
    }

    @XxlJob("AutoAllocationNisDemandForCmsJob")
    public void runCms(String arguments) {
        XxlJobHelper.log("开始运行：自动根据融资到期类需求单生成下拨指令。");
        nisDemandService.demandGenerateDownExc(NisDemandConstant.DEMANDTYPE_03);
        XxlJobHelper.log("自动根据融资到期类需求单生成下拨指令，运行结束。");
    }
}
