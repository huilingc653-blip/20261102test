package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
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

@Slf4j
@Component
public class AutoNisDemandForPosJob implements ITaskService {
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBisExcService bisExcService;
    @DubboReference(check = false)
    private ICalendarService iCalendarService;
    /**
     * 需求清单自动产生头寸调拨
     * @param arguments
     */
    @XxlJob("AutoNisDemandForPosJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("该任务取消，集成到需求单生成下拨指令的任务中去。");
        /*List<NisDemandVO> payList = nisDemandService.listByAutoAllocationPayForPos();
        if (EmptyUtils.isEmpty(payList)) {
            XxlJobHelper.log("未查询到 待生成指令的需求清单信息, 跳过本次执行");
            return;
        }
        UserDTO userDTO = userService.getByLoginName("admin");
        ICurrentUser user = userDTO;
        // 校验当前日期是否工作日，是工作日才执行
        Integer workDaySign = iCalendarService.getWorkDaySignByTime(new Date());
        if (ObjectUtil.isEmpty(workDaySign) || 1 != workDaySign) {
            XxlJobHelper.log("当前日期不为工作日，跳过本次执行");
            return;
        }
        //生成头寸指令
        for (NisDemandVO nisDemandVO : payList) {
            XxlJobHelper.log("当前待生成指令的需求清单信息: ", JsonUtils.toJSONString(nisDemandVO));
            if(nisDemandVO.getCorpLevel() > 2) {
                //如果有上级的需求单则判断上级指令是否生成
                if(EmptyUtils.isNotEmpty(nisDemandVO.getSuperId())) {
                    BisExcDTO bisExcDTO = bisExcService.getBisExcBycbsBillId(nisDemandVO.getSuperId());
                    if (ObjectUtil.isEmpty(bisExcDTO)) {
                        XxlJobHelper.log("根据业务单号获取指令信息为空，superId：" + nisDemandVO.getSuperId() + "，跳过需求清单执行");
                        continue;
                    }
                    if (!BdlinkConstant.SUCCESS.equals(bisExcDTO.getVoucherStat())) {
                        XxlJobHelper.log("当前业务单交易状态为：" + bisExcDTO.getVoucherStat() + ", 跳过当前需求清单执行");
                        continue;
                    }
                    //设置需求单需求时间为当天
                    nisDemandVO.setDemandDate(DateUtils.getDateYMD());
                    nisDemandService.generatePos(nisDemandVO, user);
                }else{
                    //设置需求单需求时间为当天
                    nisDemandVO.setDemandDate(DateUtils.getDateYMD());
                    nisDemandService.generatePos(nisDemandVO, user);
                }
            }else if(nisDemandVO.getCorpLevel() <= 2){
                //已经是顶级了就直接生成头寸调拨不用确定上级单据的指令是否交易成功
                //设置需求单需求时间为当天
                nisDemandVO.setDemandDate(DateUtils.getDateYMD());
                nisDemandService.generatePos(nisDemandVO, user);
            }
        }*/
    }
}
