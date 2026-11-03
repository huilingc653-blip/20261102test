/*
package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.service.BisGatherPolicyAccountService;
import com.bytter.biz.allocation.vo.BisGatherPolicyAccountVO;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

*/
/**
 * @author qijh
 * @date 2024/5/6.
 * 自动根据付款类需求单生成下拨指令
 *//*

@Slf4j
@Component
public class AutoAllocationNisDemandForNisJob implements ITaskService {
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBisExcService bisExcService;
    @DubboReference(check = false)
    private ICalendarService iCalendarService;
    @Autowired
    private BisGatherPolicyAccountService policyAccountService;

    @XxlJob("AutoAllocationNisDemandForNisJob")
    @Override
    public void run(String arguments) {
        log.info("Execute task: AutoAllocationNisDemandForNisJob");
        XxlJobHelper.log("开始运行：自动根据付款类需求单生成下拨指令");
        // 校验当前日期是否工作日，是工作日才执行
        Integer workDaySign = iCalendarService.getWorkDaySignByTime(new Date());
        if (ObjectUtil.isNotEmpty(workDaySign) && 1 == workDaySign) {
            List<NisDemandVO> payList = nisDemandService.listByAutoAllocationPay();
            if (ObjectUtil.isNotEmpty(payList)) {
                ICurrentUser user = userService.getByLoginName("admin");
                List<BisGatherPolicyAccountVO> policyAccounts = policyAccountService.getAllIsDown();
                //生成下拨指令
                for (NisDemandVO nisDemandVO : payList) {
                    if(nisDemandVO.getCorpLevel() > 2) {
                        //如果有上级的需求单则判断上级指令是否生成
                        if(ObjectUtil.isNotEmpty(nisDemandVO.getSuperId())) {
                            BisExcDTO bisExcDTO = bisExcService.getBisExcBycbsBillId(nisDemandVO.getSuperId());
                            if (ObjectUtil.isNotEmpty(bisExcDTO)) {
                                //上级单据的指令交易成功则生成下拨指令
                                if (BdlinkConstant.SUCCESS.equals(bisExcDTO.getVoucherStat())) {
                                    //设置需求单需求时间为当天
                                    nisDemandVO.setDemandDate(DateUtils.getDateYMD());
                                    nisDemandService.generateAllocation(nisDemandVO, user,"nis",policyAccounts);
                                }else{
                                    XxlJobHelper.log("需求单【{}】中止，上级单据还未付款成功！", nisDemandVO.getBillNo());
                                }
                            }
                        }else{//如果没有上级需求单则直接生成指令
                            nisDemandVO.setDemandDate(DateUtils.getDateYMD());
                            nisDemandService.generateAllocation(nisDemandVO, user,"nis",policyAccounts);
                        }
                    }else if(nisDemandVO.getCorpLevel() <= 2){
                        //已经是顶级了就直接生成下拨指令不用确定上级单据的指令是否交易成功
                        //设置需求单需求时间为当天
                        nisDemandVO.setDemandDate(DateUtils.getDateYMD());
                        nisDemandService.generateAllocation(nisDemandVO, user,"nis",policyAccounts);
                    }

                }
            }else{
                XxlJobHelper.log("自动根据付款类需求单生成下拨指令任务结束；没有需要生成下拨指令的需求单。");
            }
        }else{
            XxlJobHelper.log("自动根据付款类需求单生成下拨指令任务结束；当前为非工作日。");
        }

    }
}
*/
