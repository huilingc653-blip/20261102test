package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.corpCashier.entity.NisCorpCashierUser;
import com.bytter.biz.corpCashier.service.NisCorpCashierService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.UserDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jw
 * @date 2024/9/21
 * 付款失败提醒-仅针对外付网银付款单
 */
@Slf4j
@Component
public class AutoPaymentBisExcFail implements ITaskService {
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private BisExcService bisExcService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private NisCorpCashierService nisCorpCashierService;

    @XxlJob("AutoPaymentBisExcFail")
    @Override
    public void run(String arguments) {
        //获取 付款状态为“银行处理失败”或“接口处理失败”指令  外付网银付款单   时间范围20天内
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -20);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<BisExcVO> list =  bisExcService.listByVoucherStatForPaymentFailJob(simpleDateFormat.format(calendar.getTime()));

        notificationService.completeListByTag(NotificationConstant.NETBANK.BISEXC_PAYMENT_FAIL);
        if(EmptyUtils.isNotEmpty(list) && !list.isEmpty()) {
            // 统一查询失败单位的出纳
            List<String> corpIds = list.stream().map(bisExcVO -> bisExcVO.getCorpId()).collect(Collectors.toList());
            List<NisCorpCashierUser> cashierUserList = nisCorpCashierService.getCashierUser(corpIds);
            // 根据corpId分组，组成以corpId为key，value为List<NisCorpCashierUser>的map
            Map<String, List<NisCorpCashierUser>> cashierUserMap = new HashMap<>();
            if (ObjectUtil.isNotEmpty(cashierUserList)){
                cashierUserMap = cashierUserList.stream().collect(Collectors.groupingBy(NisCorpCashierUser::getCorpId));
            }
            for(BisExcVO bisExcVO : list){
                Map<String, Object> noticeMap = new HashMap<>();
                noticeMap.put("payerAccName", bisExcVO.getPayerAccName());
                noticeMap.put("payeeAccName", bisExcVO.getPayeeAccName());
                noticeMap.put("payMoney", bisExcVO.getPayMoney());
                noticeMap.put("failReason", bisExcVO.getFailReason());
                noticeMap.put("cbsBillNo", bisExcVO.getCbsBillNo());
                // 改成付款单位维护的出纳发送消息
                /*UserDTO userDTO = userService.getByLoginName(bisExcVO.getCreateBy());
                if(!EmptyUtils.isEmpty(userDTO)) {
                    notificationService.sendToUser(bisExcVO.getId(), userDTO.getId(), NotificationConstant.NETBANK.BISEXC_PAYMENT_FAIL, noticeMap);
                }*/
                if (cashierUserMap.containsKey(bisExcVO.getCorpId())){
                    List<NisCorpCashierUser> corpCashierUsers = cashierUserMap.get(bisExcVO.getCorpId());
                    for(NisCorpCashierUser cashierUser : corpCashierUsers){
                        notificationService.sendToUser(bisExcVO.getId(), cashierUser.getUserId(), NotificationConstant.NETBANK.BISEXC_PAYMENT_FAIL, noticeMap);
                    }
                }else{
                    XxlJobHelper.log("付款单：{},对应的单位：{} 未维护出纳，请维护！", bisExcVO.getCbsBillNo(), bisExcVO.getCorpName());
                }
            }
        }
    }
}
