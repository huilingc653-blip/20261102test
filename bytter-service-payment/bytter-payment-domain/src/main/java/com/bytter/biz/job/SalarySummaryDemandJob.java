package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 薪酬类需求清单汇总
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/11/23
 */
@Component
public class SalarySummaryDemandJob implements ITaskService {
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private IUserService userService;

    @XxlJob("SalaryConfigSummaryDemandJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("开始运行：自动汇总需求单任务");
        //查询所有没有汇总的
        List<String> types = Arrays.asList(NisDemandConstant.DEMANDTYPE_04);
        List<NisDemandVO> list =  nisDemandService.listByAutoSummary(types);
        if (ObjectUtil.isNotEmpty(list)){
            ICurrentUser user = userService.getByLoginName("admin");
            // list通过corpLevel  组成map, key为corpLevel value为list
            Map<Integer,List<NisDemandVO>> mapList = list.stream().collect(Collectors.groupingBy(NisDemandVO::getCorpLevel));
            Set<Integer> integers = mapList.keySet();
            // 获取integers最大值
            int max = integers.stream().max(Comparator.comparingInt(Integer::intValue)).get();
            for (int i = max; i >= 1; i--) {
                XxlJobHelper.log("开始处理需求单位层级为：{}的数据", i);
                List<NisDemandVO> levelList = mapList.get(i);
                if (ObjectUtil.isNotEmpty(levelList)){
                    XxlJobHelper.log("需求单位层级为：{}的数据共有{}条", i, levelList.size());
                    nisDemandService.autoSummaryDemand(i,levelList,mapList,user);
                }
            }
        }else{
            XxlJobHelper.log("没有需要汇总的需求单");
        }
        XxlJobHelper.log("自动汇总需求单任务运行结束");
    }

    @XxlJob("SalaryTableSummaryDemandJob")
    public void runTable(String arguments) {
        XxlJobHelper.log("开始运行：自动汇总需求单任务");
        //查询所有没有汇总的
        List<String> types = Arrays.asList(NisDemandConstant.DEMANDTYPE_05);
        List<NisDemandVO> list =  nisDemandService.listByAutoSummary(types);
        if (ObjectUtil.isNotEmpty(list)){
            ICurrentUser user = userService.getByLoginName("admin");
            // list通过corpLevel  组成map, key为corpLevel value为list
            Map<Integer,List<NisDemandVO>> mapList = list.stream().collect(Collectors.groupingBy(NisDemandVO::getCorpLevel));
            Set<Integer> integers = mapList.keySet();
            // 获取integers最大值
            int max = integers.stream().max(Comparator.comparingInt(Integer::intValue)).get();
            for (int i = max; i >= 1; i--) {
                XxlJobHelper.log("开始处理需求单位层级为：{}的数据", i);
                List<NisDemandVO> levelList = mapList.get(i);
                if (ObjectUtil.isNotEmpty(levelList)){
                    XxlJobHelper.log("需求单位层级为：{}的数据共有{}条", i, levelList.size());
                    nisDemandService.autoSummaryDemand(i,levelList,mapList,user);
                }
            }
        }else{
            XxlJobHelper.log("没有需要汇总的需求单");
        }
        XxlJobHelper.log("自动汇总需求单任务运行结束");
    }
}
