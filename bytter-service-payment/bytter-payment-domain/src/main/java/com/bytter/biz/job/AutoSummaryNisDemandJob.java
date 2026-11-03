package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.NisDemand;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author qijh
 * @date 2024/5/6.
 * 自动汇总需求单
 */
@Slf4j
@Component
public class AutoSummaryNisDemandJob implements ITaskService {
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private IUserService userService;

    @XxlJob("AutoSummaryNisDemandJobOld")
    @Override
    public void run(String arguments) {
        log.info("Execute task: AutoSummaryNisDemandJob");
        //查询所有没有汇总的
        List<String> types = Arrays.asList(NisDemandConstant.DEMANDTYPE_01,NisDemandConstant.DEMANDTYPE_02,NisDemandConstant.DEMANDTYPE_03);
        List<NisDemandVO> nisDemandVOList =  nisDemandService.listByAutoSummary(types);
        if (ObjectUtil.isEmpty(nisDemandVOList)) return;
        // 根据单位层级corpLevel分成2个list，corpLevel=4的为一个，小于4的为一个
        Map<Boolean, List<NisDemandVO>> partitionedLists = nisDemandVOList.stream()
                .collect(Collectors.partitioningBy(nisDemandVO -> nisDemandVO.getCorpLevel() == 4));

        // 先处理层级为4的数据
        List<NisDemandVO> levelFourList = partitionedLists.get(true); // corpLevel等于4的列表
        List<NisDemand> list = null;
        if (ObjectUtil.isNotEmpty(levelFourList)){
            //key规则，需求账号+需求类型+日期+上级账户
            Map<String,List<NisDemandVO>> mapList = new HashMap<>();
            //需要更新为已汇总的
            List<String> ids = new ArrayList<>();
            nisDemandService.groupSummaryDataList(levelFourList,mapList,ids);

            // 先汇总那些4级单位的明细
            list = nisDemandService.summarizeNisDemand(mapList,false);
            // 在汇总4级单位汇总的明细
            List<NisDemandVO> tempList = BeanUtils.copyListProperties(list,NisDemandVO.class);
            mapList = new HashMap<>();
            ids = new ArrayList<>();
            nisDemandService.groupSummaryDataList(tempList,mapList,ids);
            list = nisDemandService.summarizeNisDemand(mapList,true);
            //更新需求单状态为已汇总
            nisDemandService.updateSummary(ids);
        }


        // 处理层级为3的数据
        List<NisDemandVO> otherList = partitionedLists.get(false);    // corpLevel小于4的列表
        if (ObjectUtil.isNotEmpty(list)){
            List<NisDemandVO> tempList = BeanUtils.copyListProperties(list,NisDemandVO.class);
            otherList.addAll(tempList);
        }
        if (ObjectUtil.isNotEmpty(otherList)){
            //key规则，需求账号+需求类型+日期+上级账户
            Map<String,List<NisDemandVO>> mapList = new HashMap<>();
            //需要更新为已汇总的
            List<String> ids = new ArrayList<>();
            nisDemandService.groupSummaryDataList(otherList,mapList,ids);
            //循环map生成上级需求单
            nisDemandService.summarizeNisDemand(mapList,true);
            //更新需求单状态为已汇总
            nisDemandService.updateSummary(ids);
        }

    }

    @XxlJob("AutoSummaryNisDemandJob")
    public void autoSummaryNisDemand(String arguments){
        XxlJobHelper.log("开始运行：自动汇总需求单任务");
        //查询所有没有汇总的
        List<String> types = Arrays.asList(NisDemandConstant.DEMANDTYPE_01,NisDemandConstant.DEMANDTYPE_02,NisDemandConstant.DEMANDTYPE_03);
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
