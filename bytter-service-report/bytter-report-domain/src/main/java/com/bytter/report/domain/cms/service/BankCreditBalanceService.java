package com.bytter.report.domain.cms.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.bill.api.bill.dto.AbBillCreditInfoMaintainDTO;
import com.bytter.bill.api.bill.dto.AbBillPlanAmtMaintainDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.bill.service.IAbBillCreditMaintainService;
import com.bytter.bill.api.bill.service.IAbBillPlanAmtMaintainService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.report.domain.cms.dao.BankCreditBalanceDao;
import com.bytter.report.domain.cms.vo.BankCreditBalanceVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 银行授信余额表业务层
 *
 * @author fangcf
 * @date 2025-07-01
 */
@Transactional
@Service("bankCreditBalanceService")
public class BankCreditBalanceService extends BaseService<BankCreditBalanceVO, String> {

    @Autowired
    private BankCreditBalanceDao bankCreditBalanceDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IAbBillPlanAmtMaintainService abBillPlanAmtMaintainService;
    @DubboReference(check = false)
    private IAbBillCreditMaintainService abBillCreditMaintainService;

    @Override
    public BaseDao<BankCreditBalanceVO, String> getEntityDao() {
        return bankCreditBalanceDao;
    }

    /**
     * 查询银行授信余额数据
     *
     * @param corpId       单位id
     * @param weekStartDay 周开始日
     * @param weekEndDay   周结束日
     * @param user         用户对象
     * @return 银行授信余额数据
     */
    public List<BankCreditBalanceVO> listBankCreditBalance(String corpId, Date weekStartDay, Date weekEndDay, ICurrentUser user) {
        // 用户权限单位
        List<String> authCorpList = corpService.getUserCorpIds(ResourceConstant.CMS_REPORT_CREDITBALANCE_QUERY, user.getId());
        if (StringUtils.isNotEmpty(corpId)) {
            // 表单选择单位过滤条件
            List<String> formCorpList = Arrays.asList(corpId.split(CommonConstant.COMMA_SPLIT));
            // 取交集
            authCorpList = formCorpList.stream().filter(authCorpList::contains).collect(Collectors.toList());
        }
        // 获取需要汇总主数据单位
        List<String> collectCorpList = bankCreditBalanceDao.listCorpId();
        // 取交集
        collectCorpList = authCorpList.stream().filter(collectCorpList::contains).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collectCorpList)) {
            // 无单位权限，则返回空
            return Lists.newArrayList();
        }
        // 查询银行授信余额明细表
        List<BankCreditBalanceVO> dtlList = bankCreditBalanceDao.listBankCreditBalanceDetail(collectCorpList, weekStartDay);

        // 区域字段（二级单位）设置
        this.setParamsToSecondLevCorp(dtlList);
        // 计算汇总银行授信额度，剩余额度，可用额度
        this.calculateCreditBalance(dtlList);
        // 分组及汇总 -> 同单位，同银行类型下最多两条数据，对应的两个授信品种：1、银行承兑汇票（敞口）。2、银行承兑汇票（低风险）
        List<BankCreditBalanceVO> resultList = this.groupAndCollect(dtlList);
        // 已使用银承额度、流贷金额、并购贷金额占用计算
        this.setParamsToOccupyBal(resultList, weekStartDay);
        // 银行开票授信资料设置
        this.setParamsToCreditInfo(resultList);
        // 银行计划开票金额设置
        this.setParamsToPlanAmt(resultList, weekStartDay, weekEndDay);
        // 数据封装，前端展示
        return this.packageData(resultList);
    }

    /**
     * 分组及汇总
     *
     * @param dtlList 授信余额明细集合
     */
    private List<BankCreditBalanceVO> groupAndCollect(List<BankCreditBalanceVO> dtlList) {
        // 初始化结果集
        List<BankCreditBalanceVO> resultList = Lists.newArrayList();

        // 根据单位，银行类型，授信品种分组
        Map<String, List<BankCreditBalanceVO>> groupMap = dtlList
                .stream()
                .collect(Collectors.groupingBy(item -> item.getCorpId() + CommonConstant.COMMA_SPLIT + item.getCorpCode() + CommonConstant.COMMA_SPLIT + item.getBankTypeId() + CommonConstant.COMMA_SPLIT + item.getItemCode()));

        groupMap.forEach((k, v) -> {
            if (v.size() > 1) {
                // 超过一条数据需要合并汇总
                BankCreditBalanceVO collectVo = new BankCreditBalanceVO();
                // 任取一条数据，赋值给汇总对象
                BeanUtils.copyNotNullProperties(v.get(0), collectVo);
                // 汇总银行授信额度、剩余额度、可使用额度
                BigDecimal bankCreditBalSum = v.stream().map(BankCreditBalanceVO::getBankCreditBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal surplusBalSum = v.stream().map(BankCreditBalanceVO::getSurplusBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal availBalSum = v.stream().map(BankCreditBalanceVO::getAvailBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 重新设置汇总值
                collectVo.setBankCreditBal(bankCreditBalSum);
                collectVo.setSurplusBal(surplusBalSum);
                collectVo.setAvailBal(availBalSum);
                resultList.add(collectVo);
            } else {
                resultList.addAll(v);
            }
        });
        return resultList;
    }

    /**
     * 数据封装，前端展示
     *
     * @param resultList 授信余额明细集合
     */
    private List<BankCreditBalanceVO> packageData(List<BankCreditBalanceVO> resultList) {
        List<BankCreditBalanceVO> resList = Lists.newArrayList();

        Map<String, LinkedList<BankCreditBalanceVO>> secondLevMap = resultList
                .stream()
                .collect(Collectors.groupingBy(item -> item.getSecondLevCorpId() + CommonConstant.COMMA_SPLIT + item.getSecondLevCorpName() + CommonConstant.COMMA_SPLIT + item.getSort()
                        , TreeMap::new, Collectors.toCollection(LinkedList::new)));

        secondLevMap.forEach((k, v) -> {
            // 定义每一个区域汇总的合计行
            BankCreditBalanceVO sumVo = new BankCreditBalanceVO();
            String[] keys = k.split(CommonConstant.COMMA_SPLIT);
            sumVo.setCorpId("-1");// 区域汇总行区分，用于前端展示判断
            sumVo.setSecondLevCorpId(keys[0]);
            sumVo.setSecondLevCorpName(keys[1]);
            sumVo.setSort(Integer.parseInt(keys[2]));

            // 银行授信额度
            sumVo.setBankCreditBal(v.stream().map(BankCreditBalanceVO::getBankCreditBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 剩余额度
            sumVo.setSurplusBal(v.stream().map(BankCreditBalanceVO::getSurplusBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 可用额度
            sumVo.setAvailBal(v.stream().map(BankCreditBalanceVO::getAvailBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 已使用银承额度
            sumVo.setUsedBillBal(v.stream().map(BankCreditBalanceVO::getUsedBillBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 流贷金额
            sumVo.setLdzjLoanBal(v.stream().map(BankCreditBalanceVO::getLdzjLoanBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 并购贷金额
            sumVo.setBgLoanBal(v.stream().map(BankCreditBalanceVO::getBgLoanBal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本周计划开票金额
            sumVo.setCurrWeekBillMoney(v.stream().map(BankCreditBalanceVO::getCurrWeekBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 下周计划开票金额
            sumVo.setNextWeekBillMoney(v.stream().map(BankCreditBalanceVO::getNextWeekBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

            // 大区里面的公司按照单位编码升序
            v = v.stream().sorted(Comparator.comparing(BankCreditBalanceVO::getCorpCode, Comparator.nullsLast(String::compareTo))
                    .thenComparing(BankCreditBalanceVO::getBankTypeId)
                    .thenComparing(BankCreditBalanceVO::getItemCode, Comparator.reverseOrder())).collect(Collectors.toCollection(LinkedList::new));

            // 设置序号
            AtomicInteger rowNum = new AtomicInteger(1);
            v.forEach(item -> {
                item.setSerialNum(rowNum.get());
                rowNum.getAndIncrement();
            });

            // 合计行放入首行
            v.addFirst(sumVo);

            resList.addAll(v);
        });
        // 按照单位表中“排序”字段升序
        return resList.stream().sorted(Comparator.comparing(BankCreditBalanceVO::getSort, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
    }

    /**
     * 计算汇总银行授信额度，剩余额度，可用额度
     *
     * @param dtlList 授信余额明细集合
     */
    private void calculateCreditBalance(List<BankCreditBalanceVO> dtlList) {
        // 单品种
        List<BankCreditBalanceVO> singleDtlList = dtlList.stream().filter(item -> CommonConstant.NO.equals(item.getItemFlag())).collect(Collectors.toList());
        singleDtlList.forEach(single -> {
            // 银行授信额度：单品种取值：授信金额+被调剂金额-调剂金额
            BigDecimal bankCreditBal = single.getMoney().add(single.getSuperByReliefMoney()).subtract(single.getSuperReliefMoney());
            single.setBankCreditBal(bankCreditBal);
            //													   （授信额度）	 （被调剂金额）		   （调剂金额）			 （占用金额）		  （冻结金额）
            // 单品种可使用额度 = rat_bkration_dis_detail(授信分配明细表).MONEY + SUPER_BY_RELIEF_MONEY - SUPER_RELIEF_MONEY - SUPER_PROCESS_MONEY - FREEZE_MONEY
            //																（虚拟调剂额度）	     （虚拟占用额度）     （虚拟调剂占用金额）
            //														   - VIRTUAL_RELIEF_MONEY - VIRTUAL_URATION - RELIEF_AND_URATION_MONEY
            single.setAvailBal(bankCreditBal.subtract(single.getSuperProcessMoney()).subtract(single.getFreezeMoney())
                    .subtract(single.getVirtualReliefMoney()).subtract(single.getVirtualUration()).subtract(single.getReliefAndUrationMoney()));
            // 剩余额度（无需减去在途）
            single.setSurplusBal(bankCreditBal.subtract(single.getSuperProcessMoney()).subtract(single.getReliefAndUrationMoney()));
        });
        // 综合品种
        List<BankCreditBalanceVO> compDtlList = dtlList.stream().filter(item -> CommonConstant.YES.equals(item.getItemFlag())).collect(Collectors.toList());
        compDtlList.forEach(comp -> {
            // 银行授信额度：综合品种取值：最高额度+被调剂最高金额-调剂最高金额
            BigDecimal bankCreditBal = comp.getMaxMoney().add(comp.getByReliefMaxMoney()).subtract(comp.getReliefMaxMoney());
            comp.setBankCreditBal(bankCreditBal);
            //														 （授信最高额度）	 （被调剂最高额度）	  （调剂最高额度）	  （占用最高额度）	 （冻结最高额度）    （虚拟占用最高额度）
            // 综合品种可使用额度 = rat_bkration_dis_detail(授信分配明细表).MAX_MONEY + BY_RELIEF_MAX_MONEY - RELIEF_MAX_MONEY - PROCESS_MAX_MONEY - FREEZE_MAX_MONEY - VIRTUAL_MAX_MONEY
            comp.setAvailBal(bankCreditBal.subtract(comp.getProcessMaxMoney()).subtract(comp.getFreezeMaxMoney()).subtract(comp.getVirtualMaxMoney()));
            // 剩余额度（无需减去在途）
            comp.setSurplusBal(bankCreditBal.subtract(comp.getProcessMaxMoney()).subtract(comp.getVirtualMaxMoney()));
        });
        // 根据disCode分配id分组：综合品种的可用额度取值需要比较，因为综合品种授信额度是共享的
        Map<String, List<BankCreditBalanceVO>> groupDisCode = compDtlList.stream().collect(Collectors.groupingBy(BankCreditBalanceVO::getDisCode));

        groupDisCode.forEach((k, v) -> {
            BigDecimal money = BigDecimal.ZERO; // 综合品种授信额度
            try {
                // 找到综合额度标识
                List<BankCreditBalanceVO> filterList = v.stream().filter(item -> StringUtils.isNotEmpty(item.getCompSign()) && "1".equals(item.getCompSign())).collect(Collectors.toList());
                // 正常入库的数据综合品种有且仅有一条compSign为1的一条数据。这里如果出现异常，证明数据有问题
                if (!CollectionUtils.isEmpty(filterList)) {
                    // 找到了，只有一条。
                    BankCreditBalanceVO compSignVo = filterList.get(0);
                    money = compSignVo.getMoney();
                } else {
                    // 为空，则需要根据disCode去查询
                    List<BankCreditBalanceVO> disDtlList = bankCreditBalanceDao.listByDisCode(k);
                    BankCreditBalanceVO compSignVo = disDtlList.stream().filter(item -> StringUtils.isNotEmpty(item.getCompSign()) && "1".equals(item.getCompSign())).collect(Collectors.toList()).get(0);
                    money = compSignVo.getMoney();
                }
            } catch (Exception e) {
                logger.error("compSign标识异常！", e);
            }
            BigDecimal totalProcessMaxMoney = v.stream().map(BankCreditBalanceVO::getProcessMaxMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);// 占用最高额度合计
            BigDecimal totalFreezeMaxMoney = v.stream().map(BankCreditBalanceVO::getFreezeMaxMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);// 冻结最高额度合计
            BigDecimal totalVirtualMaxMoney = v.stream().map(BankCreditBalanceVO::getVirtualMaxMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);// 虚拟占用最高额度合计

            // 可使用额度
            BigDecimal availBal = money.subtract(totalProcessMaxMoney).subtract(totalFreezeMaxMoney).subtract(totalVirtualMaxMoney);
            // 剩余额度（无需减去在途）
            BigDecimal surplusBal = money.subtract(totalProcessMaxMoney).subtract(totalVirtualMaxMoney);

            v.forEach(item -> {
                // 可用额度比较大小，取值最小额度
                if (item.getAvailBal().compareTo(availBal) > 0) item.setAvailBal(availBal);
                // 剩余额度比较大小，取值最小额度
                if (item.getSurplusBal().compareTo(surplusBal) > 0) item.setSurplusBal(surplusBal);
            });
        });
        // 计算剩余额度与可使用额度为负数时,则置0
        dtlList.forEach(dtl -> {
            if (dtl.getAvailBal().compareTo(BigDecimal.ZERO) < 0) {
                dtl.setAvailBal(BigDecimal.ZERO);
            }
            if (dtl.getSurplusBal().compareTo(BigDecimal.ZERO) < 0) {
                dtl.setSurplusBal(BigDecimal.ZERO);
            }
        });
    }

    /**
     * 银行计划开票金额设置
     *
     * @param resultList   授信余额明细集合
     * @param weekStartDay 周开始日
     * @param weekEndDay   周结束日
     */
    private void setParamsToPlanAmt(List<BankCreditBalanceVO> resultList, Date weekStartDay, Date weekEndDay) {
        resultList.forEach(item -> {
            AbBillPlanAmtMaintainDTO dto = abBillPlanAmtMaintainService.getOneBy(item.getCorpId(), item.getBankTypeId(), item.getCreditType(), weekStartDay, weekEndDay);
            if (!EmptyUtils.isEmpty(dto)) {
                // 本周计划开票金额
                item.setCurrWeekBillMoney(dto.getCurrWeekBillMoney());
                // 下周计划开票金额
                item.setNextWeekBillMoney(dto.getNextWeekBillMoney());
            } else {
                item.setCurrWeekBillMoney(BigDecimal.ZERO);
                item.setNextWeekBillMoney(BigDecimal.ZERO);
            }
        });
    }

    /**
     * 银行开票授信资料设置
     *
     * @param resultList 授信余额明细集合
     */
    private void setParamsToCreditInfo(List<BankCreditBalanceVO> resultList) {
        resultList.forEach(item -> {
            AbBillCreditInfoMaintainDTO dto = abBillCreditMaintainService.getOneBy(item.getCorpId(), item.getBankTypeId(), item.getCreditType());
            if (!EmptyUtils.isEmpty(dto)) {
                // 保证金比率
                item.setBondRatio(dto.getBondRatio());
                // 开票手续费
                item.setOpenBillRate(dto.getOpenBillRate());
                // 保证金利率
                item.setBondRate(dto.getBondRate());
                // 是否能正常使用
                item.setIsUsed(dto.getIsUsed());
            }
        });
    }

    /**
     * 已使用银承额度、流贷金额、并购贷金额占用计算
     *
     * @param resultList   授信余额明细集合
     * @param weekStartDay 查询开始日期-周开始日
     */
    private void setParamsToOccupyBal(List<BankCreditBalanceVO> resultList, Date weekStartDay) {
        resultList.forEach(item -> {
            // 已使用银承额度
            BigDecimal usedBillBal = bankCreditBalanceDao.calculateOccupyBal(item.getCorpId(), item.getBankTypeId(), item.getItemCode(), weekStartDay);
            item.setUsedBillBal(usedBillBal);
        });

        // 根据单位，银行类型分组
        Map<String, List<BankCreditBalanceVO>> groupMap = resultList
                .stream()
                .collect(Collectors.groupingBy(item -> item.getCorpId() + CommonConstant.COMMA_SPLIT + item.getBankTypeId()));

        groupMap.forEach((k, v) -> {
            v.forEach(ite -> {
                ite.setLdzjLoanBal(BigDecimal.ZERO);
                ite.setBgLoanBal(BigDecimal.ZERO);
            });
            // 流贷金额与并购贷金额固定赋值给银行承兑汇票(敞口)那一行
            BankCreditBalanceVO item = v.stream().filter(bankCreditBal -> "1".equals(bankCreditBal.getCreditType())).findFirst().orElse(null);
            if (null != item) {
                BigDecimal ldzjLoanBal = bankCreditBalanceDao.calculateOccupyBal(item.getCorpId(), item.getBankTypeId(), weekStartDay, "流动资金贷款");
                item.setLdzjLoanBal(ldzjLoanBal);
                BigDecimal bgLoanBal = bankCreditBalanceDao.calculateOccupyBal(item.getCorpId(), item.getBankTypeId(), weekStartDay, "并购贷款");
                item.setBgLoanBal(bgLoanBal);
            }
        });
    }

    /**
     * 区域（二级单位）赋值
     *
     * @param dtlList 授信余额明细集合
     */
    private void setParamsToSecondLevCorp(List<BankCreditBalanceVO> dtlList) {
        // 获取区域（二级单位）
        List<BankCreditBalanceVO> secondLevCorpList = bankCreditBalanceDao.listSecondLevCorp();
        // 循环设值
        for (BankCreditBalanceVO vo : dtlList) {
            try {
                // 获取该单位所有的父单位集合（包含自己）
                List<String> parentIdList = Arrays.asList(vo.getParendIds().split(CommonConstant.COMMA_SPLIT));
                // 从区域（二级单位）中匹配到其父单位对象
                BankCreditBalanceVO filterVo = secondLevCorpList.stream().filter(item -> parentIdList.contains(item.getSecondLevCorpId())).collect(Collectors.toList()).get(0);
                vo.setSecondLevCorpId(filterVo.getSecondLevCorpId());
                vo.setSecondLevCorpName(filterVo.getSecondLevCorpName());
                vo.setSort(filterVo.getSort());
            } catch (NullPointerException e) {
                logger.error("区域字段赋值异常", e);
            }
        }
    }

    /**
     * 表单参数校验
     *
     * @param weekStartDay 周开始日
     * @param weekEndDay   周结束日
     */
    public void formCheck(Date weekStartDay, Date weekEndDay) {
        // 非空校验
        if (EmptyUtils.isEmpty(weekStartDay)) {
            // 周开始日不能为空
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_WEEKSTARTDAY_NULL);
        }
        if (EmptyUtils.isEmpty(weekEndDay)) {
            // 周结束日不能为空
            throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_WEEKENDDAY_NULL);
        }
        // 周开始日必须是周一
        LocalDate startLocalDate = Instant.ofEpochMilli(weekStartDay.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (startLocalDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_MUST_MONDAY);
        }
        // 周结束日必须是对应周开始日的周日
        LocalDate endLocalDate = Instant.ofEpochMilli(weekEndDay.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        if (startLocalDate.plusDays(6).getDayOfWeek() != endLocalDate.getDayOfWeek() || endLocalDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_PLANAMTMAINTAIN_MUST_SUNDAY);
        }
    }

    /**
     * 导出银行授信余额表
     *
     * @param resultList 导出结果
     * @return 导出对象
     */
    public HSSFWorkbook exportBankCreditBalance(List<BankCreditBalanceVO> resultList, Date weekStartDay, Date weekEndDay) {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet("银行授信余额表 ");
        // 设置列宽
        this.setColumnWidth(sheet);
        // 创建表格样式
        this.createExcel(workbook, sheet, weekStartDay, weekEndDay);
        // 填充数据
        this.createData(workbook, sheet, resultList);
        return workbook;
    }

    /**
     * 设置列宽
     *
     * @param sheet sheet页
     */
    private void setColumnWidth(HSSFSheet sheet) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 18);
        // 自定义列宽
        sheet.setColumnWidth(0, 8 * 256);//序号
        sheet.setColumnWidth(1, 30 * 256);//区域
        sheet.setColumnWidth(2, 30 * 256);//银行名称
        sheet.setColumnWidth(3, 20 * 256);//授信品种
        sheet.setColumnWidth(4, 20 * 256);//银行授信额度
        sheet.setColumnWidth(5, 20 * 256);//保证金比率
        sheet.setColumnWidth(6, 20 * 256);//开票手续费
        sheet.setColumnWidth(7, 30 * 256);//保证金利率
        sheet.setColumnWidth(8, 20 * 256);//是否能正常使用
        for (int i = 9; i <= 15; i++) {
            sheet.setColumnWidth(i, 20 * 256);//设置第9-15列列宽
        }
        sheet.setColumnWidth(16, 20 * 256);//备注
    }

    /**
     * 创建表格样式
     *
     * @param wb    workbook对象
     * @param sheet sheet页
     */
    public void createExcel(HSSFWorkbook wb, HSSFSheet sheet, Date weekStartDay, Date weekEndDay) {
        // 获取居中样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(wb, true);
        HSSFCellStyle secondStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置一级表头字体
        headStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 14));
        // 设置二级表头字体
        secondStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        // 创建第一行
        HSSFRow row0 = sheet.createRow(0);
        // 创建行高
        row0.setHeight((short) 600);
        // 创建第一行第一列
        HSSFCell cell00 = row0.createCell(0);
        // 表格标题
        String weekRange = "（" + DateUtils.format(weekStartDay, DateUtils.PATTERN_YYYYMMDD) + "-" + DateUtils.format(weekEndDay, DateUtils.PATTERN_YYYYMMDD) + "）";
        cell00.setCellValue(new HSSFRichTextString(weekRange + "银行授信余额表"));
        this.setCellStyle(row0, headStyle);
        // 第一行合并
        CellRangeAddress range0 = new CellRangeAddress(0, 0, 0, 16);
        sheet.addMergedRegion(range0);
        // 创建第二行
        HSSFRow row1 = sheet.createRow(1);
        String[] heads = new String[]{"序号", "区域", "银行名称", "授信品种", "银行授信额度", "保证金比率", "开票手续费", "保证金利率", "是否能正常使用",
                "已使用银承额度", "流贷金额", "并购贷金额", "剩余额度", "可使用额度", "本周计划开票金额", "下周计划开票金额", "备注"};
        row1.setHeightInPoints(20);
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = row1.createCell((short) i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(secondStyle);
        }
    }

    /**
     * 填充数据
     *
     * @param wb         结果集
     * @param sheet      sheet页
     * @param resultList workbook对象
     */
    private void createData(HSSFWorkbook wb, HSSFSheet sheet, List<BankCreditBalanceVO> resultList) {
        // 文字居中
        HSSFCellStyle centerCellStyleTotal = ExportUtils.getCenterCellStyle(wb, false);
        // 设置字体
        centerCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(wb, false);
        // 金额靠右（合计行）
        HSSFCellStyle rightCellStyleTotal = ExportUtils.getRightCellStyle(wb, false);
        DataFormat currencyDataFormat = wb.createDataFormat();
        // 设置字体
        rightCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        rightCellStyleTotal.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));
        // 金额靠右
        HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(wb, false);
        rightCellStyle.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));
        // 百分比靠右
        HSSFCellStyle rightRateCellStyle = ExportUtils.getRightCellStyle(wb, false);
        rightRateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));

        // 数据开始行
        AtomicInteger rowIndex = new AtomicInteger(2);
        resultList.forEach(item -> {
            // 创建数据行
            HSSFRow row = sheet.createRow(rowIndex.get());
            if ("-1".equals(item.getCorpId())) {
                // 区域汇总行
                this.addTotalDataRow(item, row, centerCellStyleTotal, rightCellStyleTotal);
            } else {
                // 基础数据行
                this.addBasicDataRow(item, row, centerCellStyle, rightCellStyle, rightRateCellStyle);
            }
            rowIndex.getAndIncrement();
        });
    }

    /**
     * 新增区域汇总行
     *
     * @param item                 基础数据对象
     * @param row                  行
     * @param centerCellStyleTotal 单元格居中
     * @param rightCellStyleTotal  单元格居右
     */
    private void addTotalDataRow(BankCreditBalanceVO item, HSSFRow row, HSSFCellStyle centerCellStyleTotal, HSSFCellStyle rightCellStyleTotal) {
        for (int a = 0; a <= 16; a++) {
            HSSFCell cell = row.createCell(a);
            if (a == 1) {
                // 区域
                cell.setCellStyle(centerCellStyleTotal);
                cell.setCellValue(new HSSFRichTextString(item.getSecondLevCorpName()));
            } else if (a == 4) {
                // 银行授信额度
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getBankCreditBal().doubleValue());
            } else if (a == 9) {
                // 已使用银承额度
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getUsedBillBal().doubleValue());
            } else if (a == 10) {
                // 流贷金额
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getLdzjLoanBal().doubleValue());
            } else if (a == 11) {
                // 并购贷金额
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getBgLoanBal().doubleValue());
            } else if (a == 12) {
                // 剩余额度
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getSurplusBal().doubleValue());
            } else if (a == 13) {
                // 可使用额度
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getAvailBal().doubleValue());
            } else if (a == 14) {
                // 本周计划开票金额
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getCurrWeekBillMoney().doubleValue());
            } else if (a == 15) {
                // 下周计划开票金额
                cell.setCellStyle(rightCellStyleTotal);
                cell.setCellValue(item.getNextWeekBillMoney().doubleValue());
            } else {
                // 其他为空行
                cell.setCellStyle(centerCellStyleTotal);
            }
        }
    }

    /**
     * 新增基础数据行
     *
     * @param item               基础数据对象
     * @param row                行
     * @param centerCellStyle    单元格居中
     * @param rightCellStyle     单元格居右
     * @param rightRateCellStyle 利率单元格居右
     */
    private void addBasicDataRow(BankCreditBalanceVO item, HSSFRow row, HSSFCellStyle centerCellStyle, HSSFCellStyle rightCellStyle, HSSFCellStyle rightRateCellStyle) {
        // 序号
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellStyle(centerCellStyle);
        cell1.setCellValue(new HSSFRichTextString(item.getSerialNum().toString()));
        // 单位名称
        HSSFCell cell2 = row.createCell(1);
        cell2.setCellStyle(centerCellStyle);
        cell2.setCellValue(new HSSFRichTextString(item.getCorpName()));
        // 银行名称
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellStyle(centerCellStyle);
        cell3.setCellValue(new HSSFRichTextString(item.getBankTypeName()));
        // 授信品种
        HSSFCell cell4 = row.createCell(3);
        cell4.setCellStyle(centerCellStyle);
        cell4.setCellValue(new HSSFRichTextString(item.getItemName()));
        // 银行授信额度
        HSSFCell cell5 = row.createCell(4);
        cell5.setCellStyle(rightCellStyle);
        cell5.setCellValue(item.getBankCreditBal().doubleValue());
        // 保证金比率
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(rightRateCellStyle);
        if (null != item.getBondRatio()) {
            cell6.setCellValue(item.getBondRatio().doubleValue());
        } else {
            cell6.setCellValue(new HSSFRichTextString());
        }
        // 开票手续费
        HSSFCell cell7 = row.createCell(6);
        cell7.setCellStyle(rightRateCellStyle);
        if (null != item.getOpenBillRate()) {
            cell7.setCellValue(item.getOpenBillRate().doubleValue());
        } else {
            cell7.setCellValue(new HSSFRichTextString());
        }
        // 保证金利率
        HSSFCell cell8 = row.createCell(7);
        cell8.setCellStyle(centerCellStyle);
        cell8.setCellValue(new HSSFRichTextString(item.getBondRate()));
        // 是否能正常使用
        HSSFCell cell9 = row.createCell(8);
        cell9.setCellStyle(centerCellStyle);
        cell9.setCellValue(new HSSFRichTextString(dictService.get("sf_yn", item.getIsUsed())));
        // 已使用银承额度
        HSSFCell cell10 = row.createCell(9);
        cell10.setCellStyle(rightCellStyle);
        cell10.setCellValue(item.getUsedBillBal().doubleValue());
        // 流贷金额
        HSSFCell cell11 = row.createCell(10);
        cell11.setCellStyle(rightCellStyle);
        cell11.setCellValue(item.getLdzjLoanBal().doubleValue());
        // 并购贷金额
        HSSFCell cell12 = row.createCell(11);
        cell12.setCellStyle(rightCellStyle);
        cell12.setCellValue(item.getBgLoanBal().doubleValue());
        // 剩余额度
        HSSFCell cell13 = row.createCell(12);
        cell13.setCellStyle(rightCellStyle);
        cell13.setCellValue(item.getSurplusBal().doubleValue());
        // 可使用额度
        HSSFCell cell14 = row.createCell(13);
        cell14.setCellStyle(rightCellStyle);
        cell14.setCellValue(item.getAvailBal().doubleValue());
        // 本周计划开票金额
        HSSFCell cell15 = row.createCell(14);
        cell15.setCellStyle(rightCellStyle);
        cell15.setCellValue(item.getCurrWeekBillMoney().doubleValue());
        // 下周计划开票金额
        HSSFCell cell16 = row.createCell(15);
        cell16.setCellStyle(rightCellStyle);
        cell16.setCellValue(item.getCurrWeekBillMoney().doubleValue());
        // 备注
        HSSFCell cell17 = row.createCell(16);
        cell17.setCellStyle(centerCellStyle);
        cell17.setCellValue(new HSSFRichTextString(item.getRmk()));
    }

    /**
     * 设置表头单元格样式
     *
     * @param row       某行
     * @param cellStyle 单元格样式
     */
    private void setCellStyle(HSSFRow row, HSSFCellStyle cellStyle) {
        Iterator<Cell> iterator = row.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.setCellStyle(cellStyle);
        }
    }
}
