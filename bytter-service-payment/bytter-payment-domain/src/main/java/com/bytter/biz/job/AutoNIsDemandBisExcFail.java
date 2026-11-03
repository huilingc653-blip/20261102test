package com.bytter.biz.job;

import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qijh
 * @date 2024/5/23
 * 下拨资金付款失败提醒
 */
@Slf4j
@Component
public class AutoNIsDemandBisExcFail implements ITaskService {
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private BisExcService bisExcService;

    @XxlJob("AutoNIsDemandBisExcFail")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("此任务停用，功能转到：需求单下拨/头寸指令失败提醒；任务中。");
        /*//取未生成消息提醒的指令
        List<BisExcVO> list =  bisExcService.listNisDemandDialExcResult();

        //提醒内容
        //业务类型：资金调拨\n
        //业务描述：$!{businessDescription}\n
        //付款单位：$!{payerCorp}\n
        //付款账号：$!{payerBankAcc}\n
        //付款银行类型：$!{payerBankType}\n
        //收款单位：$!{payeeCorp}\n
        //收款账号：$!{payeeBankAcc}\n
        //收款银行类型：$!{payeeBankType}\n
        //金额：$!{payMoney}\n
        //失败原因：$!{returnMsg}
        notificationService.completeListByTag(NotificationConstant.NETBANK.ALLOCATION_NISDEMAND_BISEXCFAIL);
        if(EmptyUtils.isNotEmpty(list) && list.size()>0) {
            List<String> ids = new ArrayList<>();
            for(BisExcVO bisExcVO : list){
                ids.add(bisExcVO.getId());
                Map<String, Object> noticeMap = new HashMap<>();
                String demandType = bisExcVO.getDemandType();
                String businessDescription = "";
                //01-付款,02-承兑到期,03-融资到期
                if("01".equals(demandType)){
                    businessDescription = "主动付款业务";
                }else if("02".equals(demandType)){
                    businessDescription = "承兑到期业务";
                }else if("03".equals(demandType)){
                    businessDescription = "融资到期业务";
                }
                noticeMap.put("businessDescription", businessDescription);
                noticeMap.put("payerCorp", bisExcVO.getPayerCorp());
                noticeMap.put("payerBankAcc", bisExcVO.getPayerAcc());
                noticeMap.put("payerBankType", bisExcVO.getPayerBankType());

                noticeMap.put("payeeCorp", bisExcVO.getPayeeCorp());
                noticeMap.put("payeeBankAcc", bisExcVO.getPayeeAcc());
                noticeMap.put("payeeBankType", bisExcVO.getPayeeBankType());

                noticeMap.put("payMoney", bisExcVO.getPayMoney());
                noticeMap.put("returnMsg", bisExcVO.getNextReturnMsg());
                notificationService.sendToUserByResPermission(ResourceConstant.NETBANK_BDLINK_BISEXC_TRACEAPPLY, NotificationConstant.NETBANK.ALLOCATION_NISDEMAND_BISEXCFAIL, noticeMap);
            }
            //更新消息提醒标志
            this.bisExcService.updateNoticeSign(ids);
        }*/
    }
}
