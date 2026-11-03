package com.bytter.biz.report.service;

import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.report.dao.GroupTransferReportDao;
import com.bytter.biz.report.vo.GroupTransferYearVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 集团资金划拨报表服务
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/7
 */
@Service("groupTransferReportService")
public class GroupTransferReportService {
    @Autowired
    private GroupTransferReportDao dao;
    @DubboReference(check = false)
    private ICorpService corpService;

    /***
     * 年度分析表
     * @param dataTablesInput
     * @param currentUser
     * @return TODO
     */
    public DataTablesOutput yearDatatable(DataTablesInput dataTablesInput, ICurrentUser currentUser) {
        String queryYear = (String) dataTablesInput.getFormdata().get("queryYear");
        String beforeYear = DateUtils.format(DateUtils.addYear(DateUtils.parse(queryYear, "yyyy"), -1),"yyyy");
        List<String> corpIds = new ArrayList<>();
        String ins_corpId = dataTablesInput.getPara("ins_corpId");
        if (EmptyUtils.isNotEmpty(ins_corpId)) {
            corpIds = Arrays.asList(ins_corpId.split(","));
        } else {
            corpIds = corpService.getUserCorpIds("report:groupTransfer:year", currentUser.getId());
        }
        // 上划下拨对应的业务类别
        List<String> busTypes = Arrays.asList(NisBusTypeConstant.SYS_BUS_TYPE_BS005,NisBusTypeConstant.SYS_BUS_TYPE_BS006, NisBusTypeConstant.SYS_BUS_TYPE_BS007, NisBusTypeConstant.SYS_BUS_TYPE_BS008, NisBusTypeConstant.SYS_BUS_TYPE_BS009, NisBusTypeConstant.SYS_BUS_TYPE_BS010);
        List<GroupTransferYearVO> list = dao.queryYearDatatable(beforeYear, queryYear, corpIds, busTypes);
        // 根据业务类别分成2组数据，
        // 资金上划：业务类别编码为“bs005”、“bs006”和“bs007”的流水金额之和；
        List<GroupTransferYearVO> upList = list.stream().filter(vo -> NisBusTypeConstant.SYS_BUS_TYPE_BS005.equals(vo.getBusType()) || NisBusTypeConstant.SYS_BUS_TYPE_BS006.equals(vo.getBusType()) || NisBusTypeConstant.SYS_BUS_TYPE_BS007.equals(vo.getBusType()))
                .collect(Collectors.toList());
        // 资金下拨：业务类别编码为“bs008”、“bs009”和“bs010”的流水金额之和；
        List<GroupTransferYearVO> downList = list.stream().filter(vo -> NisBusTypeConstant.SYS_BUS_TYPE_BS008.equals(vo.getBusType()) || NisBusTypeConstant.SYS_BUS_TYPE_BS009.equals(vo.getBusType()) || NisBusTypeConstant.SYS_BUS_TYPE_BS010.equals(vo.getBusType()))
                .collect(Collectors.toList());

        // 根据日期分组数据，相同日期的数据相加
        Map<String, BigDecimal> upMap = upList.stream().filter(vo -> vo.getTransTime() != null && vo.getAmt() != null) // 过滤无效数据
                .collect(Collectors.groupingBy(
                        GroupTransferYearVO::getTransTime,
                        Collectors.reducing(BigDecimal.ZERO,GroupTransferYearVO::getAmt,BigDecimal::add)
                ));
        Map<String, BigDecimal> downMap = downList.stream().filter(vo -> vo.getTransTime() != null && vo.getAmt() != null) // 过滤无效数据
                .collect(Collectors.groupingBy(
                        GroupTransferYearVO::getTransTime,
                        Collectors.reducing(BigDecimal.ZERO,GroupTransferYearVO::getAmt,BigDecimal::add)
                ));
        Map<String,BigDecimal> remainMap = new HashMap<>();
        // 组装返回数据
        List<GroupTransferYearVO> result = new ArrayList<>();
        String _thisYearDate = queryYear+"-01-01";
        String _beforeYearDate = beforeYear+"-01-01";
        for (int i = 0 ; i < 12 ; i++){
            int month = 1;
            if (i == 0) {
                month = 0;
            }
            _thisYearDate = DateUtils.format(DateUtils.addMonth(DateUtils.parse(_thisYearDate, "yyyy-MM-dd"), month),"yyyy-MM-dd");
            _beforeYearDate = DateUtils.format(DateUtils.addMonth(DateUtils.parse(_beforeYearDate, "yyyy-MM-dd"), month),"yyyy-MM-dd");

            String _thisTemp = DateUtils.format(DateUtils.parse(_thisYearDate, "yyyy-MM-dd"),"yyyy-MM");
            String _beforeTemp = DateUtils.format(DateUtils.parse(_beforeYearDate, "yyyy-MM-dd"),"yyyy-MM");
            GroupTransferYearVO vo = initGroupTransferYearVO();
            vo.setTransTime(_thisTemp);
            // 上划
            if (upMap.containsKey(_thisTemp)){
                vo.setThisUpAmt(upMap.get(_thisTemp));
            }
            if (upMap.containsKey(_beforeTemp)){
                vo.setBeforeUpAmt(upMap.get(_beforeTemp));
            }
            // 环比
            String huanbiDay = DateUtils.format(DateUtils.addMonth(DateUtils.parse(_thisYearDate, "yyyy-MM-dd"), -1),"yyyy-MM");
            if (upMap.containsKey(huanbiDay)){
                vo.setUpChainRatio(vo.getThisUpAmt().subtract(upMap.get(huanbiDay)).divide(upMap.get(huanbiDay),4,BigDecimal.ROUND_HALF_UP));
            }
            // 同比
            if (vo.getBeforeUpAmt().compareTo(BigDecimal.ZERO) != 0){
                vo.setUpRatio(vo.getThisUpAmt().subtract(vo.getBeforeUpAmt()).divide(vo.getBeforeUpAmt(),4,BigDecimal.ROUND_HALF_UP));
            }

            // 下拨
            if (downMap.containsKey(_thisTemp)){
                vo.setThisDownAmt(downMap.get(_thisTemp));
            }
            if (downMap.containsKey(_beforeTemp)){
                vo.setBeforeDownAmt(downMap.get(_beforeTemp));
            }
            if (downMap.containsKey(huanbiDay)){
                vo.setDownChainRatio(vo.getThisDownAmt().subtract(downMap.get(huanbiDay)).divide(downMap.get(huanbiDay),4,BigDecimal.ROUND_HALF_UP));
            }
            if (vo.getBeforeDownAmt().compareTo(BigDecimal.ZERO) != 0){
                vo.setDownRatio(vo.getThisDownAmt().subtract(vo.getBeforeDownAmt()).divide(vo.getBeforeDownAmt(),4,BigDecimal.ROUND_HALF_UP));
            }

            // 存放留存map
            remainMap.put(_thisTemp,vo.getThisUpAmt().subtract(vo.getThisDownAmt()));
            remainMap.put(_beforeTemp,vo.getBeforeUpAmt().subtract(vo.getBeforeDownAmt()));

            result.add(vo);
        }

        // 计算留存同比环比
        int i = 1;
        for (GroupTransferYearVO vo: result){
            String _thisTemp = vo.getTransTime();
            String _beforeTemp = DateUtils.format(DateUtils.addYear(DateUtils.parse(_thisTemp+"-01", "yyyy-MM-dd"), -1),"yyyy-MM");
            String huanbiDay = DateUtils.format(DateUtils.addMonth(DateUtils.parse(_thisTemp+"-01", "yyyy-MM-dd"), -1),"yyyy-MM");

            if (remainMap.containsKey(_thisTemp)){
                vo.setThisRemainAmt(remainMap.get(_thisTemp));
            }
            if (remainMap.containsKey(_beforeTemp)){
                vo.setBeforeRemainAmt(remainMap.get(_beforeTemp));
            }
            if (remainMap.containsKey(huanbiDay) && remainMap.get(huanbiDay).compareTo(BigDecimal.ZERO) != 0){
                vo.setRemainChainRatio(vo.getThisRemainAmt().subtract(remainMap.get(huanbiDay)).divide(remainMap.get(huanbiDay),4,BigDecimal.ROUND_HALF_UP));
            }
            if (vo.getBeforeRemainAmt().compareTo(BigDecimal.ZERO) != 0){
                vo.setRemainRatio(vo.getThisRemainAmt().subtract(vo.getBeforeRemainAmt()).divide(vo.getBeforeRemainAmt(),4,BigDecimal.ROUND_HALF_UP));
            }
            // 留存率
            if (vo.getThisUpAmt().compareTo(BigDecimal.ZERO) != 0){
                vo.setRemainRate(vo.getThisRemainAmt().divide(vo.getThisUpAmt(),4,BigDecimal.ROUND_HALF_UP));
            }
            vo.setTransTime(i+"月");
            i++;
        }
        DataTablesOutput output = new DataTablesOutput();
        output.setData(result);
        return output;
    }

    private GroupTransferYearVO initGroupTransferYearVO() {
        GroupTransferYearVO vo = new GroupTransferYearVO();
        vo.setThisUpAmt(BigDecimal.ZERO);
        vo.setBeforeUpAmt(BigDecimal.ZERO);
        vo.setUpChainRatio(BigDecimal.ZERO);
        vo.setUpRatio(BigDecimal.ZERO);
        vo.setThisDownAmt(BigDecimal.ZERO);
        vo.setBeforeDownAmt(BigDecimal.ZERO);
        vo.setDownChainRatio(BigDecimal.ZERO);
        vo.setDownRatio(BigDecimal.ZERO);
        vo.setThisRemainAmt(BigDecimal.ZERO);
        vo.setBeforeRemainAmt(BigDecimal.ZERO);
        vo.setRemainChainRatio(BigDecimal.ZERO);
        vo.setRemainRatio(BigDecimal.ZERO);
        vo.setRemainRate(BigDecimal.ZERO);
        return vo;
    }
}
