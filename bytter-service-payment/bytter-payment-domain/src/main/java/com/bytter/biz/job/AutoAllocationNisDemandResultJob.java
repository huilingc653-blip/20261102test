package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.NotificationConstant;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description: 需求单下拨/头寸失败消息提醒，成功修改需求单状态
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/8/12
 */
@Component
public class AutoAllocationNisDemandResultJob implements ITaskService {

    @Autowired
    private BisExcService bisExcService;

    @Autowired
    private NisDemandService nisDemandService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @DubboReference(check = false)
    private IUserService userService;


    @XxlJob("AutoAllocationNisDemandResultJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("开始运行：需求单下拨/头寸失败消息提醒");
        try {
            XxlJobHelper.log("开始处理需求单下拨指令。。。");
            handleAllocationDownResult();
        }catch (Exception e){
            XxlJobHelper.log("需求单下拨指令处理失败：{}", e.getMessage());
            XxlJobHelper.log(e);
        }

        try {
            XxlJobHelper.log("开始处理需求单头寸指令。。。");
            handleAllocationPositionResult();
        }catch (Exception e){
            XxlJobHelper.log("需求单头寸指令处理失败：{}", e.getMessage());
            XxlJobHelper.log(e);
        }

    }

    /**
     * 处理需求单下拨结果
     */
    private void handleAllocationDownResult() {
        // 需求单自动下拨结果消息提醒
        XxlJobHelper.log("开始获取需求单下拨指令结果");
        List<BisExcVO> list =  bisExcService.listNisDemandDialExcResult();
        if (EmptyUtils.isNotEmpty(list)){
            ICurrentUser user = userService.getByLoginName("admin");
            XxlJobHelper.log("本次获取需求单下拨指令结果数量：{}", list.size());
            // 根据指令状态区分成功与失败的指令
            List<BisExcVO> successList = list.stream().filter(bisExcVO -> BdlinkConstant.SUCCESS.equals(bisExcVO.getVoucherStat())).collect(Collectors.toList());
            // 更新成功的需求单状态
            if (EmptyUtils.isNotEmpty(successList)){
                XxlJobHelper.log("本次更新需求单下拨指令成功数量：{}", successList.size());
                // 获取成功的cbsBillId
                List<String> cbsBillIdList = successList.stream().map(BisExcVO::getCbsBillId).collect(Collectors.toList());
                // 使用try-catch包裹，防止异常导致任务中断
                try {
                    nisDemandService.updateNisDemandStatusSuccess(cbsBillIdList,user.getLoginName());
                    //更新消息提醒标志
                    List<String> excIdList = successList.stream().map(bisExcVO -> bisExcVO.getId()).collect(Collectors.toList());
                    bisExcService.updateNoticeSign(excIdList);
                    XxlJobHelper.log("更新需求单状态为下拨成功数量：{}", cbsBillIdList.size());
                }catch (Exception e){
                    XxlJobHelper.log("更新需求单状态为下拨成功失败：{}", e.getMessage());
                    XxlJobHelper.log(e);
                }
            }
            // 处理失败的指令
            List<BisExcVO> failList = list.stream().filter(bisExcVO -> !BdlinkConstant.SUCCESS.equals(bisExcVO.getVoucherStat())).collect(Collectors.toList());
            if (EmptyUtils.isNotEmpty(failList)){
                XxlJobHelper.log("本次处理需求单下拨指令失败数量：{}", failList.size());
                for (BisExcVO bisExcVO : failList){
                    try {
                        // 修改需求单状态为下拨失败
                        nisDemandService.updateNisDemandStatus(bisExcVO.getCbsBillId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,bisExcVO.getNextReturnMsg(),user.getLoginName());

                        //付款类需求清单下拨失败提醒
                        String tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_DIALING_FAILURE;
                        if (NisDemandConstant.DEMANDTYPE_02.equals(bisExcVO.getDemandType())){
                            //承兑到期类需求单下拨失败提醒
                            tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_BILL_FAILURE;
                        }
                        if (NisDemandConstant.DEMANDTYPE_03.equals(bisExcVO.getDemandType())){
                            //融资到期类需求单下拨失败提醒
                            tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_CMS_FAILURE;
                        }

                        //发送消息
                        Map<String, Object> map = new HashMap<>();
                        map.put("billNo", bisExcVO.getCbsBillNo());
                        map.put("payer", bisExcVO.getPayerAccName());
                        map.put("payerAcc", bisExcVO.getPayerAcc());
                        map.put("payerBank", bisExcVO.getPayerBank());
                        map.put("payee", bisExcVO.getPayeeAccName());
                        map.put("payeeAcc", bisExcVO.getPayeeAcc());
                        map.put("payeeBank", bisExcVO.getPayeeBank());
                        map.put("amt", bisExcVO.getPayMoney());
                        notificationService.send(bisExcVO.getId(),bisExcVO.getCorpId(), tag, map);
                        //更新消息提醒标志
                        bisExcService.updateNoticeSign(Arrays.asList(bisExcVO.getId()));
                        XxlJobHelper.log("更新需求单：{}状态为下拨失败。", bisExcVO.getCbsBillNo());
                    }catch (Exception e){
                        XxlJobHelper.log("处理需求单：{}下拨指令失败：{}",bisExcVO.getCbsBillNo(), e.getMessage());
                        XxlJobHelper.log(e);
                    }
                }
            }
        }else{
            XxlJobHelper.log("当前无需求单下拨指令结果");
        }
    }

    /**
     * 处理需求单头寸结果
     * @param
     * @return TODO
     */
    private void handleAllocationPositionResult() {
        XxlJobHelper.log("开始获取需求单头寸指令结果");
        List<BisExcVO> list =  bisExcService.listNisDemandPositionExcResult();
        if (EmptyUtils.isNotEmpty(list)){
            XxlJobHelper.log("本次获取需求单头寸指令结果数量：{}", list.size());
            // 根据cbsBillNo区分需求清单
            Map<String, List<BisExcVO>> map = list.stream().collect(Collectors.groupingBy(BisExcVO::getCbsBillNo));
            for (Map.Entry<String, List<BisExcVO>> entry : map.entrySet()) {
                String cbsBillNo = entry.getKey();
                List<BisExcVO> bisExcVOList = entry.getValue();
                // 取出第一条数据，获取需求单生成了多少调拨单
                Integer posNum = bisExcVOList.get(0).getPosNum();
                // 是否全部拿到结果
                Boolean isALlExc = posNum == bisExcVOList.size();
                try {
                    XxlJobHelper.log("开始处理需求单：{}头寸指令",cbsBillNo);
                    // 根据指令状态区分成功与失败的指令
                    List<BisExcVO> successList = bisExcVOList.stream().filter(bisExcVO -> BdlinkConstant.SUCCESS.equals(bisExcVO.getVoucherStat())).collect(Collectors.toList());
                    // 处理失败的指令
                    List<BisExcVO> failList = bisExcVOList.stream().filter(bisExcVO -> !BdlinkConstant.SUCCESS.equals(bisExcVO.getVoucherStat())).collect(Collectors.toList());
                    if (ObjectUtil.isNotEmpty(failList)){
                        // 表示有失败的，需要处理
                        XxlJobHelper.log("需求单：{}处理失败指令消息数量：{}",cbsBillNo, failList.size());

                        // 需求单调拨状态
                        String posStatus = NisDemandConstant.FAILED;
                        if (ObjectUtil.isNotEmpty(successList)){
                            posStatus = NisDemandConstant.PART_SUCCESS;
                        }
                        int i = 0;
                        for (BisExcVO bisExcVO : failList){
                            if (!CommonConstant.YES.equals(bisExcVO.getNoticeSign())){
                                // 发送失败消息
                                Map<String, Object> notifMap = new HashMap<>();
                                notifMap.put("reason", bisExcVO.getNextReturnMsg());
                                notifMap.put("billNo", bisExcVO.getCbsBillNo());
                                notifMap.put("payer", bisExcVO.getPayerAccName());
                                notifMap.put("payerAcc", bisExcVO.getPayerAcc());
                                notifMap.put("payerBank", bisExcVO.getPayerBank());
                                notifMap.put("payee", bisExcVO.getPayeeAccName());
                                notifMap.put("payeeAcc", bisExcVO.getPayeeAcc());
                                notifMap.put("payeeBank", bisExcVO.getPayeeBank());
                                notifMap.put("amt", bisExcVO.getDemandMoney());
                                notifMap.put("downAmt", bisExcVO.getPayMoney());

                                notificationService.send(bisExcVO.getId(),bisExcVO.getCorpId(), NotificationConstant.NETBANK.NETBANK_ALLOCATION_POSITION_FAILURE, notifMap);
                                //更新消息提醒标志
                                bisExcService.updateNoticeSign(Arrays.asList(bisExcVO.getId()));
                                XxlJobHelper.log("需求单：{}对应的调拨单交易失败，发送失败消息提醒。",cbsBillNo);
                            }
                            if (i == 0){
                                // 更新需求单调拨状态只要更新一次即可；它是一笔需求单可能对应多笔调拨单
                                nisDemandService.updateNisDemandPosStatus(Arrays.asList(bisExcVO.getCbsBillId()),posStatus);
                                String statusMsg = posStatus.equals(NisDemandConstant.FAILED) ? "失败" : "部分成功";
                                XxlJobHelper.log("需求单：{}更新调拨状态为：{}({})",cbsBillNo,posStatus,statusMsg);
                            }
                            i++;
                        }

                    }else{
                        if (isALlExc){
                            // 需求单头寸指令全部成功
                            XxlJobHelper.log("需求单：{}存在头寸指令：{}条都调拨成功",cbsBillNo, successList.size());
                            // 获取成功的cbsBillId
                            List<String> cbsBillIdList = successList.stream().map(BisExcVO::getCbsBillId).collect(Collectors.toList());
                            nisDemandService.updateNisDemandPosStatus(cbsBillIdList,NisDemandConstant.SUCCESS);
                            //更新消息提醒标志
                            List<String> excIdList = successList.stream().map(bisExcVO -> bisExcVO.getId()).collect(Collectors.toList());
                            bisExcService.updateNoticeSign(excIdList);
                        }else{
                            XxlJobHelper.log("需求单：{}对应的头寸调拨未全部生成调拨指令。",cbsBillNo);
                        }
                    }


                }catch (Exception e){
                    XxlJobHelper.log("处理需求单：{}头寸指令失败：{}",cbsBillNo, e.getMessage());
                    XxlJobHelper.log(e);
                }
            }
        }else{
            XxlJobHelper.log("当前无需求单头寸指令结果");
        }
    }
}
