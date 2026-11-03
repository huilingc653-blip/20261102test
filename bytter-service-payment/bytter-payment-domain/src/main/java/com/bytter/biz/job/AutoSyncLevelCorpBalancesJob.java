package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.inneraccount.service.BisCorpPoolBalService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.FcAccDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:自动同步多级单位内部账号余额
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/10
 */
@Component
public class AutoSyncLevelCorpBalancesJob implements ITaskService {
    @Autowired
    private BisCorpPoolBalService  bisCorpPoolBalService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService iFcOnlineLedgerService;

    @XxlJob("AutoSyncLevelCorpBalancesJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("AutoSyncLevelCorpBalancesJob start...");

        // 查询多级单位内部账号余额
        List<BisCorpPoolBalVO> list = bisCorpPoolBalService.getLevelCorpBalances(4);
        if (ObjectUtil.isNotEmpty(list)){
            XxlJobHelper.log("AutoSyncLevelCorpBalancesJob 查询到最新余额数量：{}",list.size());
            // 所有单位ID
            List<String> corpIds = list.stream().map(BisCorpPoolBalVO::getCorpId).collect(Collectors.toList());
            // 所有币种ID
            List<String> curIds = list.stream().map(BisCorpPoolBalVO::getCurId).collect(Collectors.toList());
            // 获取单位企业活期账号
            List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccBySysSubject("2120", null, corpIds, curIds);
            // 把fcAccDTOS里面的corpId和curId拼接成map的key。组成map
            Map<String, FcAccDTO> fcAccMap = new HashMap<>();
            for (FcAccDTO fcAccDTO : fcAccDTOS){
                fcAccMap.put(fcAccDTO.getCorpId() +"_"+ fcAccDTO.getCurId(), fcAccDTO);
            }
            if (ObjectUtil.isNotEmpty(fcAccMap)){
                // 待处理余额列表
                List<FcOnlineLedgerDTO> balanceList = new ArrayList<>();
                for (BisCorpPoolBalVO vo : list){
                    String key = vo.getCorpId() +"_"+ vo.getCurId();
                    if (fcAccMap.containsKey(key)){
                        FcAccDTO fcAccDTO = fcAccMap.get(key);
                        // 待更新余额
                        FcOnlineLedgerDTO fcAccBalance = new FcOnlineLedgerDTO();
                        fcAccBalance.setCurId(vo.getCurId());
                        fcAccBalance.setAccount(fcAccDTO.getBlnctrAcc());
                        fcAccBalance.setOnlineBalance(vo.getBal());
                        fcAccBalance.setCheckBalance(vo.getBal());
                        balanceList.add(fcAccBalance);
                    }else{
                        XxlJobHelper.log("单位："+vo.getCorpName()+"币种："+vo.getCurName()+"的企业活期账号不存在！");
                    }
                }

                if (ObjectUtil.isNotEmpty(balanceList)){
                    XxlJobHelper.log("AutoSyncLevelCorpBalancesJob 待更新最新余额数量：{}",balanceList.size());
                    iFcOnlineLedgerService.updateBalance(balanceList);
                }

            }else{
                XxlJobHelper.log("AutoSyncLevelCorpBalancesJob 未查询对应的企业活期账号。");
            }
        }else{
            XxlJobHelper.log("AutoSyncLevelCorpBalancesJob 未查询到需要更新的余额数据。");
        }

        XxlJobHelper.log("AutoSyncLevelCorpBalancesJob end...");
    }
}
