package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.dto.FinanceLoanDTO;
import com.bytter.bill.api.bill.dto.FinanceTypeLoanDTO;
import com.bytter.bill.domain.bill.dao.AbBillQueryDao;
import com.bytter.bill.domain.bill.entity.AbBriefStatus;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.*;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.enmus.ExcelType;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.vo.MapExcelConstants;
import com.bytter.poi.excel.export.ExcelExportServer;
import com.bytter.poi.handler.inter.IExcelExportServer;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 票据查询处理
 * @date 2017-06-21 17:35:25
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_RECALL)
@Service("abBillQueryService")
@Transactional
public class AbBillQueryService extends BaseService<AbAccrecordsVO, String> implements IExcelExportServer {
    @Autowired
    private AbBillQueryDao abBillQuery;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbDictService abDictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IOperationAreaService operationAreaService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference
    private IBankTypeService iBankTypeService;

    @Override
    public BaseDao<AbAccrecordsVO, String> getEntityDao() {
        return abBillQuery;
    }

    /**
     * 银承占比及额度使用情况统计表
     *
     * @param resultList 导出结果
     * @param ym         年月
     * @return 导出对象
     */
    public HSSFWorkbook exportAcUsageRatioStatistics(List<AbBillTargetProportionAndQuotaUsageVo> resultList, String ym) {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet("银承占比及额度使用情况统计表");
        // 设置列宽
        this.setColumnWidth(sheet);
        // 创建表格样式
        this.createExcel(workbook, sheet, ym);
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
        sheet.setColumnWidth(2, 30 * 256);//仓库
        for (int i = 3; i <= 12; i++) {
            sheet.setColumnWidth(i, 25 * 256);//设置第4-13列列宽
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb    workbook对象
     * @param sheet sheet页
     * @param ym    年月
     */
    public void createExcel(HSSFWorkbook wb, HSSFSheet sheet, String ym) {
        // 获取居中样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(wb, true);
        HSSFCellStyle secondStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置一级表头字体
        headStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 14));
        // 设置二级表头字体
        secondStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        // 创建第一行
        HSSFRow row1 = sheet.createRow(0);
        // 创建行高
        row1.setHeight((short) 600);
        // 创建第一行第一列
        HSSFCell cell11 = row1.createCell(0);
        // 表格标题
        String ymString = DateUtils.format(DateUtil.parseDate(ym, "yyyy-MM"), DateUtils.PATTERN_CH_YYYYMM);
        cell11.setCellValue(new HSSFRichTextString(ymString + "银承结算占比及额度使用情况统计表"));
        this.setCellStyle(row1, headStyle);
        // 第一行合并
        CellRangeAddress range0 = new CellRangeAddress(0, 0, 0, 12);
        sheet.addMergedRegion(range0);
        // 创建第二行
        HSSFRow row2 = sheet.createRow(1);
        // 创建第二行第一列
        HSSFCell cell21 = row2.createCell(0);
        cell21.setCellStyle(secondStyle);
        cell21.setCellValue(new HSSFRichTextString("序号"));
        // 第二行第三行第一列合并
        CellRangeAddress range2311 = new CellRangeAddress(1, 2, 0, 0);
        sheet.addMergedRegion(range2311);
        // 创建第二行第二列
        HSSFCell cell22 = row2.createCell(1);
        cell22.setCellStyle(secondStyle);
        cell22.setCellValue(new HSSFRichTextString("区域"));
        // 第二行第三行第二列合并
        CellRangeAddress range2322 = new CellRangeAddress(1, 2, 1, 1);
        sheet.addMergedRegion(range2322);
        // 创建第二行第三列
        HSSFCell cell23 = row2.createCell(2);
        cell23.setCellStyle(secondStyle);
        cell23.setCellValue(new HSSFRichTextString("仓库"));
        // 第二行第三行第二列合并
        CellRangeAddress range2333 = new CellRangeAddress(1, 2, 2, 2);
        sheet.addMergedRegion(range2333);
        // 创建第二行第4-7列
        HSSFCell cell24 = row2.createCell(3);
        cell24.setCellStyle(secondStyle);
        cell24.setCellValue(new HSSFRichTextString("银承开票占比情况"));
        for (int i = 4; i <= 6; i++) {
            HSSFCell cell = row2.createCell(i);
            cell.setCellStyle(secondStyle);
        }
        // 第二行第4-7合并
        CellRangeAddress range2247 = new CellRangeAddress(1, 1, 3, 6);
        sheet.addMergedRegion(range2247);
        // 创建第二行第8-10列
        HSSFCell cell28 = row2.createCell(7);
        cell28.setCellStyle(secondStyle);
        cell28.setCellValue(new HSSFRichTextString("银行授信情况"));
        for (int i = 8; i <= 9; i++) {
            HSSFCell cell = row2.createCell(i);
            cell.setCellStyle(secondStyle);
        }
        // 第二行第8-10合并
        CellRangeAddress range22810 = new CellRangeAddress(1, 1, 7, 9);
        sheet.addMergedRegion(range22810);
        // 创建第二行第11-13列
        HSSFCell cell211 = row2.createCell(10);
        cell211.setCellStyle(secondStyle);
        cell211.setCellValue(new HSSFRichTextString("开票情况"));
        for (int i = 11; i <= 12; i++) {
            HSSFCell cell = row2.createCell(i);
            cell.setCellStyle(secondStyle);
        }
        // 第二行第11-13合并
        CellRangeAddress range221113 = new CellRangeAddress(1, 1, 10, 12);
        sheet.addMergedRegion(range221113);

        // 创建第三行
        HSSFRow row3 = sheet.createRow(2);
        for (int i = 0; i <= 2; i++) {
            // 创建第三行第1-3列
            HSSFCell cell = row3.createCell(i);
            cell.setCellStyle(secondStyle);
        }
        String[] heads = new String[]{"目标", "实际", "差异", "对比上期提升比例", "银行额度", "本月末授信剩余额度", "本月止未到期开票金额", "本月货款支付总额", "本月货款电汇金额", "本月总开票金额"};
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = row3.createCell((short) i + 3);
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
    private void createData(HSSFWorkbook wb, HSSFSheet sheet, List<AbBillTargetProportionAndQuotaUsageVo> resultList) {
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

        // 百分比靠右（合计行）
        HSSFCellStyle rightRateCellStyleTotal = ExportUtils.getRightCellStyle(wb, false);
        rightRateCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        rightRateCellStyleTotal.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
        // 百分比靠右
        HSSFCellStyle rightRateCellStyle = ExportUtils.getRightCellStyle(wb, false);
        rightRateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));

        // 数据开始行
        AtomicInteger rowIndex = new AtomicInteger(3);
        resultList.forEach(item -> {
            // 创建数据行
            HSSFRow row = sheet.createRow(rowIndex.get());
            if ("-1".equals(item.getCorpId())) {
                // 区域汇总行
                this.addDataRow(item, row, centerCellStyleTotal, rightCellStyleTotal, rightRateCellStyleTotal);
            } else {
                // 基础数据行
                this.addDataRow(item, row, centerCellStyle, rightCellStyle, rightRateCellStyle);
            }
            rowIndex.getAndIncrement();
        });
    }

    /**
     * 填充数据
     *
     * @param item               基础数据对象
     * @param row                行
     * @param centerCellStyle    单元格居中
     * @param rightCellStyle     单元格居右
     * @param rightRateCellStyle 利率单元格居右
     */
    private void addDataRow(AbBillTargetProportionAndQuotaUsageVo item, HSSFRow row, HSSFCellStyle centerCellStyle, HSSFCellStyle rightCellStyle, HSSFCellStyle rightRateCellStyle) {
        // 序号
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellStyle(centerCellStyle);
        if ("-1".equals(item.getCorpId())) {
            // 区域合计行不需要序号
            cell1.setCellValue("");
        } else {
            cell1.setCellValue(new HSSFRichTextString(item.getSerialNum().toString()));
        }
        // 区域
        HSSFCell cell2 = row.createCell(1);
        cell2.setCellStyle(centerCellStyle);
        if ("-1".equals(item.getCorpId())) {
            // 区域合计行显示区域（二级单位）
            cell2.setCellValue(new HSSFRichTextString(item.getSecondLevCorpName()));
        } else {
            // 显示单位名称
            cell2.setCellValue(new HSSFRichTextString(item.getCorpName()));
        }
        // 仓库
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellStyle(centerCellStyle);
        cell3.setCellValue(new HSSFRichTextString(item.getUnitStoreMaintenanceName()));
        // 目标
        HSSFCell cell4 = row.createCell(3);
        cell4.setCellStyle(rightRateCellStyle);
        cell4.setCellValue(item.getTargetProportion().doubleValue());
        // 实际
        HSSFCell cell5 = row.createCell(4);
        cell5.setCellStyle(rightRateCellStyle);
        cell5.setCellValue(item.getActual().doubleValue());
        // 差异
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(rightRateCellStyle);
        cell6.setCellValue(item.getDifference().doubleValue());
        // 对比上期提升比例
        HSSFCell cell7 = row.createCell(6);
        cell7.setCellStyle(rightRateCellStyle);
        cell7.setCellValue(item.getIncreaseProportion().doubleValue());
        // 银行额度
        HSSFCell cell8 = row.createCell(7);
        cell8.setCellStyle(rightCellStyle);
        cell8.setCellValue(item.getBankCredit().doubleValue());
        // 本月末授信剩余额度
        HSSFCell cell9 = row.createCell(8);
        cell9.setCellStyle(rightCellStyle);
        cell9.setCellValue(item.getLeftQuota().doubleValue());
        // 本月止未到期开票金额
        HSSFCell cell10 = row.createCell(9);
        cell10.setCellStyle(rightCellStyle);
        cell10.setCellValue(item.getUnexpiredBillMoney().doubleValue());
        // 本月货款支付总额
        HSSFCell cell11 = row.createCell(10);
        cell11.setCellStyle(rightCellStyle);
        cell11.setCellValue(item.getTotalPay().doubleValue());
        // 本月货款电汇金额
        HSSFCell cell12 = row.createCell(11);
        cell12.setCellStyle(rightCellStyle);
        cell12.setCellValue(item.getEBankPay().doubleValue());
        // 本月总开票金额
        HSSFCell cell13 = row.createCell(12);
        cell13.setCellStyle(rightCellStyle);
        cell13.setCellValue(item.getOpenBillPay().doubleValue());
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

    /**
     * 银承占比及额度使用情况统计表数据
     *
     * @param ym   查询年月
     * @param user 用户对象
     * @return 返回列表数据信息
     */
    public List<AbBillTargetProportionAndQuotaUsageVo> listAcUsageRatioStatistics(String ym, ICurrentUser user) {
        // 用户权限单位
        List<String> authCorpList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_TARGETPROPORTION_QUOTAUSAGE_STATISTICS_QUERY_CORPOP, user.getId());
        if (CollectionUtil.isEmpty(authCorpList)) {
            // 无单位权限，则返回空
            return Lists.newArrayList();
        }
        // 银承占比及额度使用情况统计表数据
        List<AbBillTargetProportionAndQuotaUsageVo> dataList = abBillQuery.listAcUsageRatioStatistics(ym, authCorpList);

        if (CollectionUtil.isEmpty(dataList)) {
            // 无基础数据，则无需汇总操作了
            return Lists.newArrayList();
        }

        // 获取区域（二级单位）
        List<CorpDTO> secondLevCorpList = abBillQuery.listSecondLevCorp();

        // 设置区域字段（二级单位）
        this.setParamsToSecondLevCorp(dataList, secondLevCorpList);

        // 封装区域（二级单位）的单位id
        List<String> secondLevCorpIdList = secondLevCorpList.stream().map(CorpDTO::getId).collect(Collectors.toList());
        // 获取年份
        String year = ym.substring(0, 4);
        // 根据区域（二级单位）集合和当前年份查询银承开票占比目标维护维护表实体信息
        List<AbEmitBillTargetProportionInfoVO> entityList = abBillQuery.listByCorpIdAndCurrentYear(year, secondLevCorpIdList);
        // 区域（二级单位）计算求和
        LinkedList<AbBillTargetProportionAndQuotaUsageVo> resultList = this.calculateSumSecondLevCorp(dataList, entityList);

        // 设置仓库名称
        this.setParamsToUnitStore(resultList);

        // 添加区域（二级单位）合计行。前端展示
        return this.packageData(year, resultList);
    }

    /**
     * 添加区域（二级单位）合计行。前端展示
     *
     * @param resultList 结果集
     * @return 结果集
     */
    private List<AbBillTargetProportionAndQuotaUsageVo> packageData(String year, LinkedList<AbBillTargetProportionAndQuotaUsageVo> resultList) {
        // 只获取区域（二级单位）行，过滤corpId = -1
        LinkedList<AbBillTargetProportionAndQuotaUsageVo> secondLevList = resultList.stream().filter(item -> "-1".equals(item.getCorpId())).collect(Collectors.toCollection(LinkedList::new));

        AbBillTargetProportionAndQuotaUsageVo totalRow = new AbBillTargetProportionAndQuotaUsageVo();
        totalRow.setCorpId("-1");
        totalRow.setSecondLevCorpName("合计");
        totalRow.setUnitStoreMaintenanceName("");
        // 获取资金中心单位（编码=100）
        CorpDTO corp = corpService.getCorpByCode("100");
        // 合计目标为资金中心（编码=100）银行开票占比目标维护的值
        List<String> corpIdList = Lists.newArrayList();
        corpIdList.add(corp.getId());
        // 根据资金中心单位和当前年份查询银承开票占比目标维护维护表实体信息
        List<AbEmitBillTargetProportionInfoVO> entityList = abBillQuery.listByCorpIdAndCurrentYear(year, corpIdList);
        BigDecimal targetProportion = BigDecimal.ZERO;
        if (!CollectionUtil.isEmpty(entityList)) {
            AbEmitBillTargetProportionInfoVO info = entityList.stream().filter(item -> corp.getId().equals(item.getCorpId())).findFirst().orElse(null);
            if (null != info && null != info.getTargetProportion()) {
                targetProportion = info.getTargetProportion();
            }
        }
        // 目标
        totalRow.setTargetProportion(targetProportion);

        // 银行额度
        totalRow.setBankCredit(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getBankCredit).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月末授信剩余额度
        totalRow.setLeftQuota(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getLeftQuota).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月止未到期开票金额
        totalRow.setUnexpiredBillMoney(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月货款支付总额
        totalRow.setTotalPay(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getTotalPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月货款电汇金额
        totalRow.setEBankPay(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getEBankPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月总开票金额
        totalRow.setOpenBillPay(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getOpenBillPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 上月贷款电汇金额
        totalRow.setPreEBankPay(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getPreEBankPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 上月总开票金额
        totalRow.setPreOpenBillPay(secondLevList.stream().map(AbBillTargetProportionAndQuotaUsageVo::getPreOpenBillPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

        // 实际 = 本月总开票金额 / （本月货款电汇金额 + 本月总开票金额）
        BigDecimal total = totalRow.getOpenBillPay().add(totalRow.getEBankPay());
        if (BigDecimal.ZERO.compareTo(total) != 0) {
            totalRow.setActual(totalRow.getOpenBillPay().divide(total, 4, BigDecimal.ROUND_HALF_UP));
        } else {
            totalRow.setActual(BigDecimal.ZERO);
        }
        // 上月实际 = 上月总开票金额 / （上月货款电汇金额 + 上月总开票金额）
        BigDecimal preTotal = totalRow.getPreOpenBillPay().add(totalRow.getPreEBankPay());
        if (BigDecimal.ZERO.compareTo(preTotal) != 0) {
            totalRow.setPreActual(totalRow.getPreOpenBillPay().divide(preTotal, 4, BigDecimal.ROUND_HALF_UP));
        } else {
            totalRow.setPreActual(BigDecimal.ZERO);
        }
        // 差异
        totalRow.setDifference(totalRow.getActual().subtract(totalRow.getTargetProportion()).setScale(4, BigDecimal.ROUND_HALF_UP));
        // 对比上期提升比例
        totalRow.setIncreaseProportion(totalRow.getActual().subtract(totalRow.getPreActual()).setScale(4, BigDecimal.ROUND_HALF_UP));

        secondLevList.addFirst(totalRow);
        resultList.addAll(secondLevList);
        return resultList;
    }

    /**
     * 区域（二级单位）计算求和
     *
     * @param dataList   银承占比及额度使用情况统计表数据集合
     * @param entityList 区域（二级单位）银承开票占比目标维护表
     */
    private LinkedList<AbBillTargetProportionAndQuotaUsageVo> calculateSumSecondLevCorp(List<AbBillTargetProportionAndQuotaUsageVo> dataList, List<AbEmitBillTargetProportionInfoVO> entityList) {
        // 初始化结果集
        LinkedList<AbBillTargetProportionAndQuotaUsageVo> resultList = new LinkedList<>();
        // 根据区域（二级单位）进行分组
        Map<String, LinkedList<AbBillTargetProportionAndQuotaUsageVo>> secondLevMap = dataList
                .stream()
                .collect(Collectors.groupingBy(item -> item.getSecondLevCorpId() + CommonConstant.COMMA_SPLIT + item.getSecondLevCorpName() + CommonConstant.COMMA_SPLIT + item.getSort()
                        , TreeMap::new, Collectors.toCollection(LinkedList::new)));

        // 分完组后，对区域（二级单位）进行计算求和
        secondLevMap.forEach((k, v) -> {
            // 定义每一个区域汇总的合计行
            AbBillTargetProportionAndQuotaUsageVo sumVo = new AbBillTargetProportionAndQuotaUsageVo();
            String[] keys = k.split(CommonConstant.COMMA_SPLIT);
            sumVo.setCorpId("-1");// 区域汇总行区分，用于前端展示判断
            sumVo.setSecondLevCorpId(keys[0]);
            sumVo.setSecondLevCorpName(keys[1]);
            sumVo.setSort(Integer.parseInt(keys[2]));
            // 获取区域（二级单位）的目标以及仓库名称
            AbEmitBillTargetProportionInfoVO info = entityList.stream().filter(item -> item.getCorpId().equals(sumVo.getSecondLevCorpId())).findFirst().orElse(null);
            if (null == info) {
                // 银承开票占比目标维护：未维护
                sumVo.setTargetProportion(BigDecimal.ZERO);
            } else {
                // 银承开票占比目标维护：已维护
                sumVo.setTargetProportion(info.getTargetProportion());
                sumVo.setUnitStoreMaintenanceId(info.getUnitStoreMaintenanceId());
            }

            // 银行额度
            sumVo.setBankCredit(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getBankCredit).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月末授信剩余额度
            sumVo.setLeftQuota(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getLeftQuota).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月止未到期开票金额
            sumVo.setUnexpiredBillMoney(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月货款支付总额
            sumVo.setTotalPay(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getTotalPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月货款电汇金额
            sumVo.setEBankPay(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getEBankPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月总开票金额
            sumVo.setOpenBillPay(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getOpenBillPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 上月贷款电汇金额
            sumVo.setPreEBankPay(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getPreEBankPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 上月总开票金额
            sumVo.setPreOpenBillPay(v.stream().map(AbBillTargetProportionAndQuotaUsageVo::getPreOpenBillPay).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

            // 实际 = 本月总开票金额 / （本月货款电汇金额 + 本月总开票金额）
            BigDecimal total = sumVo.getOpenBillPay().add(sumVo.getEBankPay());
            if (BigDecimal.ZERO.compareTo(total) != 0) {
                sumVo.setActual(sumVo.getOpenBillPay().divide(total, 4, BigDecimal.ROUND_HALF_UP));
            } else {
                sumVo.setActual(BigDecimal.ZERO);
            }
            // 上月实际 = 上月总开票金额 / （上月货款电汇金额 + 上月总开票金额）
            BigDecimal preTotal = sumVo.getPreOpenBillPay().add(sumVo.getPreEBankPay());
            if (BigDecimal.ZERO.compareTo(preTotal) != 0) {
                sumVo.setPreActual(sumVo.getPreOpenBillPay().divide(preTotal, 4, BigDecimal.ROUND_HALF_UP));
            } else {
                sumVo.setPreActual(BigDecimal.ZERO);
            }
            // 差异
            sumVo.setDifference(sumVo.getActual().subtract(sumVo.getTargetProportion()).setScale(4, BigDecimal.ROUND_HALF_UP));
            // 对比上期提升比例
            sumVo.setIncreaseProportion(sumVo.getActual().subtract(sumVo.getPreActual()).setScale(4, BigDecimal.ROUND_HALF_UP));

            // 按照【银承开票占比情况】“实际”字段倒序
            v = v.stream().sorted(Comparator.comparing(AbBillTargetProportionAndQuotaUsageVo::getActual, Comparator.nullsLast(BigDecimal::compareTo)).reversed()).collect(Collectors.toCollection(LinkedList::new));

            // 设置序号
            AtomicInteger rowNum = new AtomicInteger(1);
            v.forEach(item -> {
                item.setSerialNum(rowNum.get());
                rowNum.getAndIncrement();
            });

            // 合计行放入首行
            v.addFirst(sumVo);

            resultList.addAll(v);
        });
        // 按照单位表中“排序”字段升序
        return resultList.stream().sorted(Comparator.comparing(AbBillTargetProportionAndQuotaUsageVo::getSort, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * 区域（二级单位）赋值
     *
     * @param dataList          银承占比及额度使用情况统计表数据集合
     * @param secondLevCorpList 区域（二级单位）
     */
    private void setParamsToSecondLevCorp(List<AbBillTargetProportionAndQuotaUsageVo> dataList, List<CorpDTO> secondLevCorpList) {
        // 循环设值
        for (AbBillTargetProportionAndQuotaUsageVo vo : dataList) {
            try {
                // 获取该单位所有的父单位集合（包含自己）
                List<String> parentIdList = Arrays.asList(vo.getParendIds().split(CommonConstant.COMMA_SPLIT));
                // 从区域（二级单位）中匹配到其父单位对象
                CorpDTO filterVo = secondLevCorpList.stream().filter(item -> parentIdList.contains(item.getId())).collect(Collectors.toList()).get(0);
                vo.setSecondLevCorpId(filterVo.getId());
                vo.setSecondLevCorpName(filterVo.getName());
                vo.setSort(filterVo.getSort());
            } catch (NullPointerException e) {
                logger.error("区域字段赋值异常", e);
            }
        }
    }

    /**
     * 设置仓库名称
     *
     * @param resultList 结果集
     */
    private void setParamsToUnitStore(LinkedList<AbBillTargetProportionAndQuotaUsageVo> resultList) {
        // 定义仓库map集合，key为仓库编码，value为仓库名称
        Map<String, String> unitStoreMaintenanceMap = Maps.newHashMap();

        // 1、过滤结果集中仓库编码不为空的
        // 2、循环设值，根据仓库编码获取仓库名称，由于仓库编码id有多个，需要拼接
        resultList.stream().filter(vo -> StringUtils.isNotEmpty(vo.getUnitStoreMaintenanceId())).forEach(vo -> {
            // 拼接仓库名称，用英文逗号隔开
            StringBuilder sb = new StringBuilder();
            String[] unitStoreMaintenanceIds = vo.getUnitStoreMaintenanceId().split(CommonConstant.COMMA_SPLIT);
            for (String unitStoreMaintenanceId : unitStoreMaintenanceIds) {
                String unitStoreMaintenanceName = unitStoreMaintenanceMap.computeIfAbsent(
                        unitStoreMaintenanceId,
                        key -> {
                            String name = corpService.getUnitStoreMaintenanceNameByCode(key);
                            return StringUtils.isEmpty(name) ? null : name;
                        }
                );
                if (null != unitStoreMaintenanceName) {
                    sb.append(unitStoreMaintenanceName).append(CommonConstant.COMMA_SPLIT);
                }
            }
            if (sb.length() > 0) {
                vo.setUnitStoreMaintenanceName(sb.substring(0, sb.length() - 1));
            }
        });
    }

    /**
     * 银承开票金额统计表
     *
     * @param form 表单数据
     * @param user 用户对象
     * @return 返回列表数据信息
     */
    public List<AbBillBankOpenBillAmtStatisticsVo> listBankOpenBillAmtStatistics(AbBillBankOpenBillAmtStatisticsVo form, ICurrentUser user) {
        List<String> authCorpList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BANK_OPEN_BILL_AMT_STATISTICS_QUERY_CORPOP, user.getId());
        if (CollectionUtil.isEmpty(authCorpList)) {
            // 无单位权限，则返回空
            return Lists.newArrayList();
        }
        // 银承开票金额统计表数据
        List<AbBillBankOpenBillAmtStatisticsVo> dataList = abBillQuery.AbBillBankOpenBillAmtStatisticsVo(form, authCorpList);

        if (CollectionUtil.isEmpty(dataList)) {
            // 无基础数据，则无需汇总操作了
            return Lists.newArrayList();
        }
        // 获取区域（二级单位）
        List<CorpDTO> secondLevCorpList = abBillQuery.listSecondLevCorp();
        // 设置区域字段（二级单位）
        this.setParamsToSecondLevCorp2(dataList, secondLevCorpList);
        // 区域（二级单位）计算求和
        LinkedList<AbBillBankOpenBillAmtStatisticsVo> resultList = this.calculateSumSecondLevCorp2(dataList);
        // 添加区域（二级单位）合计行。前端展示
        return this.packageData(resultList);
    }

    /**
     * 区域（二级单位）赋值
     *
     * @param dataList          银承占比及额度使用情况统计表数据集合
     * @param secondLevCorpList 区域（二级单位）
     */
    private void setParamsToSecondLevCorp2(List<AbBillBankOpenBillAmtStatisticsVo> dataList, List<CorpDTO> secondLevCorpList) {
        // 循环设值
        for (AbBillBankOpenBillAmtStatisticsVo vo : dataList) {
            try {
                // 获取该单位所有的父单位集合（包含自己）
                List<String> parentIdList = Arrays.asList(vo.getParendIds().split(CommonConstant.COMMA_SPLIT));
                // 从区域（二级单位）中匹配到其父单位对象
                CorpDTO filterVo = secondLevCorpList.stream().filter(item -> parentIdList.contains(item.getId())).collect(Collectors.toList()).get(0);
                vo.setSecondLevCorpId(filterVo.getId());
                vo.setSecondLevCorpName(filterVo.getName());
                vo.setSort(filterVo.getSort());
            } catch (NullPointerException e) {
                logger.error("区域字段赋值异常", e);
            }
        }
    }

    /**
     * 区域（二级单位）计算求和
     *
     * @param dataList 银承开票金额统计表数据集合
     */
    private LinkedList<AbBillBankOpenBillAmtStatisticsVo> calculateSumSecondLevCorp2(List<AbBillBankOpenBillAmtStatisticsVo> dataList) {
        // 初始化结果集
        LinkedList<AbBillBankOpenBillAmtStatisticsVo> resultList = new LinkedList<>();
        // 根据区域（二级单位）进行分组
        Map<AbBillBankOpenBillAmtStatisticsVo, LinkedList<AbBillBankOpenBillAmtStatisticsVo>> secondLevMap = dataList
                .stream()
                .collect(Collectors.groupingBy(item -> new AbBillBankOpenBillAmtStatisticsVo(item.getSecondLevCorpId(), item.getSecondLevCorpName(), item.getSort())
                        , HashMap::new, Collectors.toCollection(LinkedList::new)));

        // 分完组后，对区域（二级单位）进行计算求和
        secondLevMap.forEach((k, v) -> {
            k.setCorpId("-1");// 区域汇总行区分，用于前端展示判断
            // 授信总额
            k.setRatMoneySum(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getRatMoneySum).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 其中：敞口
            k.setOpenMoneySum(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenMoneySum).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 其中：低风险
            k.setLowRiskMoneySum(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskMoneySum).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 流贷金额
            k.setLoanMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLoanMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月止未到期开票金额
            k.setUnexpiredBillMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月末授信剩余额度
            k.setLeftQuota(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLeftQuota).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月止保证金余额
            k.setUnexpiredBailMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUnexpiredBailMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 支付总额
            k.setPayTotalMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getPayTotalMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 其中：电汇总额
            k.setEbankMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getEbankMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 其中：开票总额
            k.setOpenBillTotalMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenBillTotalMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 敞口：三个月
            k.setOpenBillThreeMonth(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenBillThreeMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 敞口：六个月
            k.setOpenBillSixMonth(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenBillSixMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 低风险：三个月
            k.setLowRiskThreeMonth(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskThreeMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 低风险：六个月
            k.setLowRiskSixMonth(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskSixMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 商承：三个月
            k.setBusinessBillThreeMonth(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getBusinessBillThreeMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 商承：六个月
            k.setBusinessBillSixMonth(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getBusinessBillSixMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 敞口额度已使用总额（票面口径）
            k.setUseBillMoneyByBill(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUseBillMoneyByBill).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 敞口额度已使用总额（银行口径）
            k.setUseBillMoneyByBank(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUseBillMoneyByBank).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月未到期（其中：敞口）
            k.setOpenUnexpiredBillMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
            // 本月未到期（其中：低风险）
            k.setLowRiskUnexpiredBillMoney(v.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

            // 大区里面的公司按照单位编码升序倒序
            v = v.stream().sorted(Comparator.comparing(AbBillBankOpenBillAmtStatisticsVo::getCorpCode, Comparator.nullsLast(String::compareTo))).collect(Collectors.toCollection(LinkedList::new));

            // 设置序号
            AtomicInteger rowNum = new AtomicInteger(1);
            v.forEach(item -> {
                item.setSerialNum(rowNum.get());
                rowNum.getAndIncrement();
            });

            // 合计行放入首行
            v.addFirst(k);

            resultList.addAll(v);
        });
        // 按照单位表中“排序”字段升序
        return resultList.stream().sorted(Comparator.comparing(AbBillBankOpenBillAmtStatisticsVo::getSort, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * 添加区域（二级单位）合计行。前端展示
     *
     * @param resultList 结果集
     * @return 结果集
     */
    private List<AbBillBankOpenBillAmtStatisticsVo> packageData(LinkedList<AbBillBankOpenBillAmtStatisticsVo> resultList) {
        // 只获取区域（二级单位）行，过滤corpId = -1
        LinkedList<AbBillBankOpenBillAmtStatisticsVo> secondLevList = resultList.stream().filter(item -> "-1".equals(item.getCorpId())).collect(Collectors.toCollection(LinkedList::new));

        AbBillBankOpenBillAmtStatisticsVo totalRow = new AbBillBankOpenBillAmtStatisticsVo();
        totalRow.setCorpId("-1");
        totalRow.setSecondLevCorpName("合计");

        // 授信总额
        totalRow.setRatMoneySum(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getRatMoneySum).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 其中：敞口
        totalRow.setOpenMoneySum(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenMoneySum).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 其中：低风险
        totalRow.setLowRiskMoneySum(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskMoneySum).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 流贷金额
        totalRow.setLoanMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLoanMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月止未到期开票金额
        totalRow.setUnexpiredBillMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月末授信剩余额度
        totalRow.setLeftQuota(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLeftQuota).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月止保证金余额
        totalRow.setUnexpiredBailMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUnexpiredBailMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 支付总额
        totalRow.setPayTotalMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getPayTotalMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 其中：电汇总额
        totalRow.setEbankMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getEbankMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 其中：开票总额
        totalRow.setOpenBillTotalMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenBillTotalMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 敞口：三个月
        totalRow.setOpenBillThreeMonth(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenBillThreeMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 敞口：六个月
        totalRow.setOpenBillSixMonth(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenBillSixMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 低风险：三个月
        totalRow.setLowRiskThreeMonth(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskThreeMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 低风险：六个月
        totalRow.setLowRiskSixMonth(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskSixMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 商承：三个月
        totalRow.setBusinessBillThreeMonth(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getBusinessBillThreeMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 商承：六个月
        totalRow.setBusinessBillSixMonth(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getBusinessBillSixMonth).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 敞口额度已使用总额（票面口径）
        totalRow.setUseBillMoneyByBill(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUseBillMoneyByBill).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 敞口额度已使用总额（银行口径）
        totalRow.setUseBillMoneyByBank(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getUseBillMoneyByBank).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月未到期（其中：敞口）
        totalRow.setOpenUnexpiredBillMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getOpenUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 本月未到期（其中：低风险）
        totalRow.setLowRiskUnexpiredBillMoney(secondLevList.stream().map(AbBillBankOpenBillAmtStatisticsVo::getLowRiskUnexpiredBillMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));

        secondLevList.addFirst(totalRow);
        resultList.addAll(secondLevList);
        return resultList;
    }

    /**
     * 银承占比及额度使用情况统计表
     *
     * @param resultList 导出结果
     * @return 导出对象
     */
    public HSSFWorkbook exportBankOpenBillAmtStatistics(List<AbBillBankOpenBillAmtStatisticsVo> resultList) {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet("银承开票金额统计表");
        // 设置列宽
        this.setColumnWidth2(sheet);
        // 创建表格样式
        this.createExcel2(workbook, sheet);
        // 填充数据
        this.createData2(workbook, sheet, resultList);
        return workbook;
    }

    /**
     * 设置列宽
     *
     * @param sheet sheet页
     */
    private void setColumnWidth2(HSSFSheet sheet) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 18);
        // 自定义列宽
        sheet.setColumnWidth(0, 8 * 256);//序号
        sheet.setColumnWidth(1, 30 * 256);//区域
        sheet.setColumnWidth(2, 30 * 256);//仓库
        for (int i = 3; i <= 5; i++) {
            sheet.setColumnWidth(i, 25 * 256);//设置第4-6列列宽
        }
        sheet.setColumnWidth(6, 18 * 256);//合同到期日
        for (int i = 7; i <= 25; i++) {
            sheet.setColumnWidth(i, 25 * 256);//设置第4-6列列宽
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb    workbook对象
     * @param sheet sheet页
     */
    public void createExcel2(HSSFWorkbook wb, HSSFSheet sheet) {
        // 获取居中样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(wb, true);
        HSSFCellStyle secondStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置一级表头字体
        headStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 14));
        // 设置二级表头字体
        secondStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        // 创建第一行
        HSSFRow row1 = sheet.createRow(0);
        // 创建行高
        row1.setHeight((short) 600);
        // 创建第一行第一列
        HSSFCell cell11 = row1.createCell(0);
        // 表格标题
        cell11.setCellValue(new HSSFRichTextString("银承开票金额统计表"));
        this.setCellStyle(row1, headStyle);
        // 第一行合并
        CellRangeAddress range0 = new CellRangeAddress(0, 0, 0, 25);
        sheet.addMergedRegion(range0);
        // 创建第二行
        HSSFRow row2 = sheet.createRow(1);

        String[] heads = new String[]{"序号", "区域", "银行名称", "授信总额", "其中:敞口", "其中:低风险", "合同到期日", "流贷金额", "本月止未到期开票金额", "本月末授信剩余额度",
                "本月止保证金余额", "支付总额", "其中：电汇总额", "其中：开票总额", "敞口：三个月", "敞口：六个月", "低风险：三个月", "低风险：六个月", "商承：三个月", "商承：六个月",
                "开票占比", "6个月银承占比", "敞口额度已使用总额（票面口径）", "敞口额度已使用总额（银行口径）", "本月未到期（其中：敞口）", "本月未到期（其中：低风险）"};
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = row2.createCell((short) i);
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
    private void createData2(HSSFWorkbook wb, HSSFSheet sheet, List<AbBillBankOpenBillAmtStatisticsVo> resultList) {
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

        // 百分比靠右（合计行）
        HSSFCellStyle rightRateCellStyleTotal = ExportUtils.getRightCellStyle(wb, false);
        rightRateCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        rightRateCellStyleTotal.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
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
                this.addDataRow2(item, row, centerCellStyleTotal, rightCellStyleTotal, rightRateCellStyleTotal);
            } else {
                // 基础数据行
                this.addDataRow2(item, row, centerCellStyle, rightCellStyle, rightRateCellStyle);
            }
            rowIndex.getAndIncrement();
        });
    }

    /**
     * 填充数据
     *
     * @param item               基础数据对象
     * @param row                行
     * @param centerCellStyle    单元格居中
     * @param rightCellStyle     单元格居右
     * @param rightRateCellStyle 利率单元格居右
     */
    private void addDataRow2(AbBillBankOpenBillAmtStatisticsVo item, HSSFRow row, HSSFCellStyle centerCellStyle, HSSFCellStyle rightCellStyle, HSSFCellStyle rightRateCellStyle) {
        // 序号
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellStyle(centerCellStyle);
        if ("-1".equals(item.getCorpId())) {
            // 区域合计行不需要序号
            cell1.setCellValue("");
        } else {
            cell1.setCellValue(new HSSFRichTextString(item.getSerialNum().toString()));
        }
        // 区域
        HSSFCell cell2 = row.createCell(1);
        cell2.setCellStyle(centerCellStyle);
        if ("-1".equals(item.getCorpId())) {
            // 区域合计行显示区域（二级单位）
            cell2.setCellValue(new HSSFRichTextString(item.getSecondLevCorpName()));
        } else {
            // 显示单位名称
            cell2.setCellValue(new HSSFRichTextString(item.getCorpName()));
        }
        // 银行名称
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellStyle(centerCellStyle);
        if ("-1".equals(item.getCorpId())) {
            cell3.setCellValue("");
        } else {
            cell3.setCellValue(new HSSFRichTextString(item.getBankName()));
        }
        // 授信总额
        HSSFCell cell4 = row.createCell(3);
        cell4.setCellStyle(rightCellStyle);
        cell4.setCellValue(item.getRatMoneySum().doubleValue());
        // 其中:敞口
        HSSFCell cell5 = row.createCell(4);
        cell5.setCellStyle(rightCellStyle);
        cell5.setCellValue(item.getOpenMoneySum().doubleValue());
        // 其中:低风险
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(rightCellStyle);
        cell6.setCellValue(item.getLowRiskMoneySum().doubleValue());
        // 合同到期日
        HSSFCell cell7 = row.createCell(6);
        cell7.setCellStyle(centerCellStyle);
        if ("-1".equals(item.getCorpId())) {
            cell7.setCellValue("");
        } else {
            cell7.setCellValue(DateUtils.formatDate(item.getContractEndDate()));
        }
        // 流贷金额
        HSSFCell cell8 = row.createCell(7);
        cell8.setCellStyle(rightCellStyle);
        cell8.setCellValue(item.getLoanMoney().doubleValue());
        // 本月止未到期开票金额
        HSSFCell cell9 = row.createCell(8);
        cell9.setCellStyle(rightCellStyle);
        cell9.setCellValue(item.getUnexpiredBillMoney().doubleValue());
        // 本月末授信剩余额度
        HSSFCell cell10 = row.createCell(9);
        cell10.setCellStyle(rightCellStyle);
        cell10.setCellValue(item.getLeftQuota().doubleValue());
        // 本月止保证金余额
        HSSFCell cell11 = row.createCell(10);
        cell11.setCellStyle(rightCellStyle);
        cell11.setCellValue(item.getUnexpiredBailMoney().doubleValue());
        // 支付总额
        HSSFCell cell12 = row.createCell(11);
        cell12.setCellStyle(rightCellStyle);
        cell12.setCellValue(item.getPayTotalMoney().doubleValue());
        // 其中：电汇总额
        HSSFCell cell13 = row.createCell(12);
        cell13.setCellStyle(rightCellStyle);
        cell13.setCellValue(item.getEbankMoney().doubleValue());
        // 其中：开票总额
        HSSFCell cell14 = row.createCell(13);
        cell14.setCellStyle(rightCellStyle);
        cell14.setCellValue(item.getOpenBillTotalMoney().doubleValue());
        // 敞口：三个月
        HSSFCell cell15 = row.createCell(14);
        cell15.setCellStyle(rightCellStyle);
        cell15.setCellValue(item.getOpenBillThreeMonth().doubleValue());
        // 敞口：六个月
        HSSFCell cell16 = row.createCell(15);
        cell16.setCellStyle(rightCellStyle);
        cell16.setCellValue(item.getOpenBillSixMonth().doubleValue());
        // 低风险：三个月
        HSSFCell cell17 = row.createCell(16);
        cell17.setCellStyle(rightCellStyle);
        cell17.setCellValue(item.getLowRiskThreeMonth().doubleValue());
        // 低风险：六个月
        HSSFCell cell18 = row.createCell(17);
        cell18.setCellStyle(rightCellStyle);
        cell18.setCellValue(item.getLowRiskSixMonth().doubleValue());
        // 商承：三个月
        HSSFCell cell19 = row.createCell(18);
        cell19.setCellStyle(rightCellStyle);
        cell19.setCellValue(item.getBusinessBillThreeMonth().doubleValue());
        // 商承：六个月
        HSSFCell cell20 = row.createCell(19);
        cell20.setCellStyle(rightCellStyle);
        cell20.setCellValue(item.getBusinessBillSixMonth().doubleValue());
        // 开票占比
        HSSFCell cell21 = row.createCell(20);
        cell21.setCellStyle(rightRateCellStyle);
        if ("-1".equals(item.getCorpId())) {
            cell21.setCellValue("");
        } else {
            cell21.setCellValue(item.getOpenBillPercent().doubleValue());
        }
        // 6个月银承占比
        HSSFCell cell22 = row.createCell(21);
        cell22.setCellStyle(rightRateCellStyle);
        if ("-1".equals(item.getCorpId())) {
            cell22.setCellValue("");
        } else {
            cell22.setCellValue(item.getSixMonthBankBillPercent().doubleValue());
        }
        // 敞口额度已使用总额（票面口径）
        HSSFCell cell23 = row.createCell(22);
        cell23.setCellStyle(rightCellStyle);
        cell23.setCellValue(item.getUseBillMoneyByBill().doubleValue());
        // 敞口额度已使用总额（银行口径）
        HSSFCell cell24 = row.createCell(23);
        cell24.setCellStyle(rightCellStyle);
        cell24.setCellValue(item.getUseBillMoneyByBank().doubleValue());
        // 本月未到期（其中：敞口）
        HSSFCell cell25 = row.createCell(24);
        cell25.setCellStyle(rightCellStyle);
        cell25.setCellValue(item.getOpenUnexpiredBillMoney().doubleValue());
        // 本月未到期（其中：低风险）
        HSSFCell cell26 = row.createCell(25);
        cell26.setCellStyle(rightCellStyle);
        cell26.setCellValue(item.getLowRiskUnexpiredBillMoney().doubleValue());

    }

    /**
     * 加载票据解付与解付账户前一天余额对比表数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput dataQueryBankAccBalCompareData(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BANKACC_BAL_COMPARE_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput, AccebillConstant.AB_BILL_BANKACC_BAL_COMPARE_QUERY_CORPOP, corpIds);
        DataTablesOutput<AbBillBankAccBalCompareVo> dataTablesOutput = abBillQuery.dataQueryBankAccBalCompareData(dataTablesInput, corpAuthList);
        List<AbBillBankAccBalCompareVo> list = dataTablesOutput.getData();
        if (CollectionUtil.isNotEmpty(list)) {
            //设置到期日
            String endDateSign = null;
            String endDateStr = (String) dataTablesInput.getFormdata().get("ged_endDate");
            String ledEndDateStr = (String) dataTablesInput.getFormdata().get("led_endDate");
            if (endDateStr.equals(ledEndDateStr)) {
                endDateSign = endDateStr;
            } else {
                endDateSign = endDateStr + "~" + ledEndDateStr;
            }
            Map<String, BigDecimal> loanMoneyMap = new HashMap();
            Map<String, BigDecimal> repayMap = new HashMap();
            Map<String, BigDecimal> balMap = new HashMap();
            Map<String, String> operationAreaMap = new HashMap();
            for (AbBillBankAccBalCompareVo vo : list) {
                //设置营运区
                if (StringUtils.isNotEmpty(vo.getOperationAreaCode())) {
                    StringBuilder operationAreaNames = new StringBuilder();
                    String operationAreaName = null;
                    List<String> operationAreaCodes = Arrays.asList(vo.getOperationAreaCode().split(","));
                    for (String code : operationAreaCodes) {
                        if (operationAreaMap.containsKey(code)) {
                            operationAreaName = operationAreaMap.get(code);
                        } else {
                            List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByCode(code);
                            if (CollectionUtil.isNotEmpty(operationAreaDTOS)) {
                                operationAreaDTOS.stream().forEach(operationAreaDTO -> {
                                    operationAreaNames.append(operationAreaDTO.getOperationAreaName()).append(",");
                                });
                            }
                            operationAreaName = StringUtils.isNotEmpty(operationAreaNames.toString()) ? operationAreaNames.toString().substring(0, operationAreaNames.toString().length() - 1) : "";
                            operationAreaMap.put(code, operationAreaName);
                        }
                    }
                    vo.setOperationAreaName(operationAreaName);
                }
                //一个账号有多行记录时，最上面的贷款金额正常显示，其他行的留空
                if (vo.getLoanMoney() != null) {
                    if (loanMoneyMap.containsKey(vo.getBankAcc())) {
                        vo.setLoanMoney(BigDecimal.ZERO);
                    } else {
                        loanMoneyMap.put(vo.getBankAcc(), vo.getLoanMoney());
                    }
                }
                if (vo.getBal() != null) {
                    if (balMap.containsKey(vo.getBankAcc())) {
                        vo.setBal(BigDecimal.ZERO);
                    } else {
                        balMap.put(vo.getBankAcc(), vo.getBal());
                    }
                }
                if (vo.getRepayTotalMoney() != null) {
                    if (repayMap.containsKey(vo.getBankAcc())) {
                        vo.setRepayTotalMoney(BigDecimal.ZERO);
                    } else {
                        repayMap.put(vo.getBankAcc(), vo.getRepayTotalMoney());
                    }
                }
                if (vo.getBailPercent() != null && vo.getBailPercent().compareTo(BigDecimal.ONE) == 0) {
                    vo.setApplyRepayMoney(vo.getRepayTotalMoney());
                } else {
                    vo.setApplyRepayMoney(vo.getRepayTotalMoney().add(vo.getBillMoney()));
                }
                vo.setDiscrepancy(vo.getBal().subtract(vo.getApplyRepayMoney()));
                vo.setEndDateStr(endDateSign);
            }
        }
        return dataTablesOutput;
    }

    /**
     * 加载月度还银承表列表数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput dataQueryBillBabByMonthTable(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLBAB_BYMONTH_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput, AccebillConstant.AB_BILL_BILLBAB_BYMONTH_QUERY_CORPOP, corpIds);
        DataTablesOutput<AbBillBabVo> dataTablesOutput = abBillQuery.dataQueryBillBabByMonthTable(dataTablesInput, corpAuthList);
        List<AbBillBabVo> list = dataTablesOutput.getData();
        if (CollectionUtil.isNotEmpty(list)) {
            for (AbBillBabVo vo : list) {
                if (StringUtils.isNotEmpty(vo.getOperationAreaCode())) {
                    StringBuilder operationAreaNames = new StringBuilder();
                    List<String> operationAreaCodes = Arrays.asList(vo.getOperationAreaCode().split(","));
                    for (String code : operationAreaCodes) {
                        List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByCode(code);
                        if (CollectionUtil.isNotEmpty(operationAreaDTOS)) {
                            operationAreaDTOS.stream().forEach(operationAreaDTO -> {
                                operationAreaNames.append(operationAreaDTO.getOperationAreaName()).append(",");
                            });
                        }
                    }
                    vo.setOperationAreaName(StringUtils.isNotEmpty(operationAreaNames.toString()) ? operationAreaNames.toString().substring(0, operationAreaNames.toString().length() - 1) : "");
                }
                if (StringUtils.isNotBlank(vo.getCorpId())) {
                    CorpDTO corpDTO = abCorpService.get(vo.getCorpId());
                    vo.setProvinceName(corpDTO.getProvinceName());
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 加载票据明细信息数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillQueryDtlInfo(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLDTL_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        if (dataTablesInput.getFormdata().containsKey("led_applyDate")) {
            String dateValue = dataTablesInput.getFormdata().get("led_applyDate") + " 23:59:59";
            dataTablesInput.getFormdata().put("led_applyDate", dateValue);
        }
        //排序初始化功能
        orderInit(dataTablesInput);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_BILLDTL_QUERY_CORPOP, corpIds);
        DataTablesOutput<AbAccrecordsVO> dataTablesOutput = abBillQuery.listAbBillQueryDtl(dataTablesInput, corpAuthList);
        injectRecordsValues(dataTablesOutput);
        return dataTablesOutput;
    }

    private void injectRecordsValues(DataTablesOutput<AbAccrecordsVO> dataTablesOutput) {
        Set<String> corpIdSet = new HashSet<>();
        Set<String> accSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_SEND_SIGN", "AB_BRIEF_STATUS", "AB_ABBILLDISCOUNT_RECTYPE"
                    , "AB_ENTER_STOCK", "AB_BILL_ROLE", "AB_STTLMMK", "AB_DISCOUNT_RPDMK", "ACCEBILL_EMIT_TYPE", "sf_yn", "AB_BILL_BUS_TYPE", "AB_BILL_FLAG"
                    , "AB_BILL_TYPE", "AB_BILL_BUSSTATUS", "AB_BILL_INSOPTSTATUS", "AB_BILL_SOURCE_QUERY", "AB_BILL_STATUS", "AB_BILL_POOL_STATUS"
                    , "AB_BILL_COUNT_POOL_STATUS", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbAccrecordsVO records : listData) {
                injectValues(records, listDictMap);
                if(StrUtil.isNotBlank(records.getApplyCorpId())){
                    corpIdSet.add(records.getApplyCorpId());
                }
                if(StrUtil.isNotBlank(records.getCorpId())){
                    corpIdSet.add(records.getCorpId());
                }
                if(StrUtil.isNotBlank(records.getBusCorpId())){
                    corpIdSet.add(records.getBusCorpId());
                }
                if(StrUtil.isNotBlank(records.getBankAcc())){
                    accSet.add(records.getBankAcc());
                }
            }
        });
        Map<String, CorpDTO> stringCorpDTOMap = new HashMap<>();
        if(corpIdSet.size() > 0){
            Optional.ofNullable(abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet))).ifPresent(stringCorpDTOMap::putAll);
        }
        Map<String,BankTypeByAccDTO> BankTypeByAccDTOMap = new HashMap<>();
        if(accSet.size() > 0){
            Optional.ofNullable(iBankTypeService.listBankTypeByAcc(new ArrayList<>(accSet))).ifPresent(a -> {
                for(BankTypeByAccDTO dto : a){
                    BankTypeByAccDTOMap.put(dto.getBankAcc(),dto);
                }
            });
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (AbAccrecordsVO bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                BankTypeByAccDTO dto = BankTypeByAccDTOMap.get(bill.getBankAcc());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
                bill.setCorpCodeAndName("("+bill.getCorpCode()+")"+bill.getCorpName());
                bill.setBankTypeName(dto == null ? "" : dto.getBankTypeName());
            }
        });
    }

    /**
     * 加载票据业务信息数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillQueryBusInfo(DataTablesInput dataTablesInput) {
        List<String> bursarBillNoList = new ArrayList<>();
        //如果共享系统单号是通过空格隔开的，做一个批量查询
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bursarBillNo"))) {
            String bursarBillNo = dataTablesInput.getFormdata().get("likes_bursarBillNo").toString();
            bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
            if (bursarBillNoList.size() >= 2) {
                dataTablesInput.getFormdata().remove("likes_bursarBillNo");
                dataTablesInput.getFormdata().put("bursarBillNoList", bursarBillNoList);
            }
        }
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLBUS_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        if (dataTablesInput.getFormdata().containsKey("led_applyDate")) {
            String dateValue = dataTablesInput.getFormdata().get("led_applyDate") + " 23:59:59";
            dataTablesInput.getFormdata().put("led_applyDate", dateValue);
        }
        //排序初始化功能
//		orderInit(dataTablesInput);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput, AccebillConstant.AB_BILL_BILLBUS_QUERY_CORPOP, corpIds);
        DataTablesOutput<AbAccrecordsVO> dataTablesOutput = abBillQuery.listAbBillQueryBus(dataTablesInput, corpAuthList);
        injectRecordsValues(dataTablesOutput);

        if (bursarBillNoList.size() >= 2) {
            List<AbAccrecordsVO> list = dataTablesOutput.getData();
            List<AbAccrecordsVO> list1 = new ArrayList<>();
            for (String bursarBillNo : bursarBillNoList) {
                for (AbAccrecordsVO abEmitBillVo : list) {
                    if (bursarBillNo.equals(abEmitBillVo.getBursarBillNo())) {
                        list1.add(abEmitBillVo);
                    }
                }
            }
            dataTablesOutput.setData(list1);
        }
        return dataTablesOutput;
    }

    /**
     * 加载票据贴现信息数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillQueryDiscountInfo(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_DISCOUNTQUERY_QUERY_CORPOP, dataTablesInput.getCurrentUserId());
        if (dataTablesInput.getFormdata().containsKey("led_applyDate")) {
            String dateValue = dataTablesInput.getFormdata().get("led_applyDate") + " 23:59:59";
            dataTablesInput.getFormdata().put("led_applyDate", dateValue);
        }
        //排序初始化功能
//		orderInit(dataTablesInput);
        DataTablesOutput<AbAccrecordsVO> dataTablesOutput = abBillQuery.listAbBillQueryDiscountInfo(dataTablesInput, corpAuthList);
        injectRecordsValues(dataTablesOutput);
        return dataTablesOutput;
    }

    /**
     * 进行排序初始化处理
     *
     * @param dataTablesInput
     */
    @Transactional(readOnly = true)
    public void orderInit(DataTablesInput dataTablesInput) {
        Map<Integer, Order> orderMap = new LinkedHashMap<>();
        for (Order order : dataTablesInput.getOrder()) {
            orderMap.put(order.getColumn(), order);
        }
        //查询条件有票据号 设置固定排序规则
        if (dataTablesInput.getFormdata().containsKey("likes_origNo")
                && StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("likes_origNo"))) {
            if (!orderMap.containsKey(11)) {
                dataTablesInput.getOrder().add(new Order(11, "asc"));
            }
        } else if (dataTablesInput.getOrder().isEmpty()) {
            //查询条件没有排序信息 默认排序规则
            List<Order> listOrder = new ArrayList<>();
//			按照单位分类，并且按单位代码进行顺序排序（默认顺序排序，点击持票单位列的按钮时进行倒序顺序排序切换），
            if (orderMap.containsKey(1)) {
                listOrder.add(orderMap.get(1));
                orderMap.remove(1);
            } else {
                listOrder.add(new Order(1, "asc"));
            }
//			按照票据号码相同展示在一起，
            if (orderMap.containsKey(3)) {
                listOrder.add(orderMap.get(3));
                orderMap.remove(3);
            } else {
                listOrder.add(new Order(3, "asc"));
            }
//			按照票据来源相同展示在一起，
            if (orderMap.containsKey(5)) {
                listOrder.add(orderMap.get(5));
                orderMap.remove(5);
            } else {
                listOrder.add(new Order(5, "asc"));
            }
//			按照流通介质相同展示在一起，
            if (orderMap.containsKey(7)) {
                listOrder.add(orderMap.get(7));
                orderMap.remove(7);
            } else {
                listOrder.add(new Order(7, "asc"));
            }
//			按照票据类型相同展示在一起，
            if (orderMap.containsKey(8)) {
                listOrder.add(orderMap.get(8));
                orderMap.remove(8);
            } else {
                listOrder.add(new Order(8, "asc"));
            }
//			按照申请人相同展示在一起，
            if (orderMap.containsKey(10)) {
                listOrder.add(orderMap.get(10));
                orderMap.remove(10);
            } else {
                listOrder.add(new Order(10, "asc"));
            }
//			按照申请日期的倒序顺序排序（可切换顺序排序），
            if (orderMap.containsKey(11)) {
                listOrder.add(orderMap.get(11));
                orderMap.remove(11);
            } else {
                listOrder.add(new Order(11, "desc"));
            }
//			按照票据归属日期的倒序顺序排序（可切换顺序排序）；
            if (orderMap.containsKey(12)) {
                listOrder.add(orderMap.get(12));
                orderMap.remove(12);
            } else {
                listOrder.add(new Order(12, "desc"));
            }
//			按照出票日期的倒序顺序排序（可切换顺序排序），
            if (orderMap.containsKey(13)) {
                listOrder.add(orderMap.get(13));
                orderMap.remove(13);
            } else {
                listOrder.add(new Order(13, "desc"));
            }
//			按照到期日期的倒序顺序排序（可切换顺序排序）
            if (orderMap.containsKey(14)) {
                listOrder.add(orderMap.get(14));
                orderMap.remove(14);
            } else {
                listOrder.add(new Order(14, "desc"));
            }
            listOrder.addAll(orderMap.values());
            dataTablesInput.setOrder(listOrder);
        }
    }

    /**
     * 查询业务流水表所有业务类型
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<AbBriefStatus> listBusTypeInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("notStatus", AccebillConstant.BRIEF_STATUS_6203);
        return listBriefStatusInfo(params);
    }

    /**
     * 根据条件查询业务类型
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<AbBriefStatus> listBriefStatusInfo(Map<String, Object> params) {
        return abBillQuery.listBriefStatusInfo(params);
    }

    /**
     * 根据所有业务类型
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<AbBriefStatus> listBriefStatusInfo() {
        return listBriefStatusInfo(null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<Object> selectListForExcelExport(Object obj, int page) {
        if (obj instanceof DataTablesInput) {
            DataTablesInput dataTablesInput = (DataTablesInput) obj;
            dataTablesInput.setStart((page - 1) * 100);
            dataTablesInput.setLength(100);
            DataTablesOutput output = listAbBillQueryDtlInfo(dataTablesInput);
            return output.getData();
        }
        return null;
    }

    /**
     * 获取单位票据统计表数据
     *
     * @param vo 查询条件
     * @return 返回列表数据
     */
    public void getBillUnitLstatisticsInfo(AbAccrecordsVO vo, Model model, ICurrentUser user) {
        //获取票据类型数据字典信息
        List<DictDTO> billTypeDictList = abDictService.list(AccebillConstant.AB_BILL_TYPE_DICT);
        List<Map<String, Object>> infoList = new ArrayList<>();
        billUnitLstatisticsCommon(vo, user, billTypeDictList, infoList);
        if (vo.getCountIsPool() == null) {
            vo.setCountIsPool("0");
        }
        model.addAttribute("billTypeDictList", billTypeDictList);
        model.addAttribute("listInfo", infoList);
        model.addAttribute("infoParams", vo);
    }

    /**
     * 获取查询的单位票据统计表业务类型
     *
     * @param dictList
     * @param i
     * @param size
     * @return
     */
    public String[] getUnitLstatisticsBusTypes(List<DictDTO> dictList, int i, int size) {
        String[] busTypes = new String[size - i];
        int len = 0;
        for (; i < size; i++) {
            DictDTO dict = dictList.get(i);
            busTypes[len++] = dict.getCode();
        }
        return busTypes;
    }

    /**
     * 加载单位票据统计表明细信息数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput queryBillUnitLstatisticsDtlData(DataTablesInput dataTablesInput) {
        List<DictDTO> dictList = abDictService.list(AccebillConstant.AB_BILL_CORPTJ_BUSTYPE);
        String[] allBusTypes = getUnitLstatisticsBusTypes(dictList, 0, dictList.size());
        DataTablesOutput<AbAccrecordsVO> dataTablesOutput = abBillQuery.queryBillUnitLstatisticsDtlData(dataTablesInput, allBusTypes);
        injectRecordsValues(dataTablesOutput);
        return dataTablesOutput;
    }

    private void injectValues(AbAccrecordsVO records, Map<String, Map<String, DictDTO>> listDictMap) {
        // 是否发送指令标志
        Optional.ofNullable(records.getSendSign()).ifPresent(sendSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
                    .ifPresent(dict -> records.setSendSignName(dict.getName()));
        });
        // 业务类型
        Optional.ofNullable(records.getBriefId()).ifPresent(briefId -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
                    .ifPresent(dict -> records.setBriefIdName(dict.getName()));
        });
        // 内部业务标志
        Optional.ofNullable(records.getInsideSing()).ifPresent(insideSing -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ABBILLDISCOUNT_RECTYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
                    .ifPresent(dict -> records.setInsideSingName(dict.getName()));
        });
        // 所属库存
        Optional.ofNullable(records.getEnterStock()).ifPresent(enterStock -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
                    .ifPresent(dict -> records.setEnterStockName(dict.getName()));
        });
        // 业务申请人类别
        Optional.ofNullable(records.getReqRole()).ifPresent(reqRole -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ROLE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(reqRole)))
                    .ifPresent(dict -> records.setReqRoleName(dict.getName()));
        });
        // 业务接收人类别
        Optional.ofNullable(records.getRecRole()).ifPresent(recRole -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ROLE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(recRole)))
                    .ifPresent(dict -> records.setRecRoleName(dict.getName()));
        });
        // 清算方式
        Optional.ofNullable(records.getSttlmmk()).ifPresent(sttlmmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_STTLMMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sttlmmk)))
                    .ifPresent(dict -> records.setSttlmmkName(dict.getName()));
        });
        // 贴现种类
        Optional.ofNullable(records.getRpdmk()).ifPresent(rpdmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_DISCOUNT_RPDMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rpdmk)))
                    .ifPresent(dict -> records.setRpdmkName(dict.getName()));
        });
        // 开出方式
        Optional.ofNullable(records.getEmitType()).ifPresent(emitType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("ACCEBILL_EMIT_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitType)))
                    .ifPresent(dict -> records.setEmitTypeName(dict.getName()));
        });
        // 是否开票保贴
        Optional.ofNullable(records.getEmitGuandisSign()).ifPresent(emitGuandisSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitGuandisSign)))
                    .ifPresent(dict -> records.setEmitGuandisSignName(dict.getName()));
        });
        // 业务类型
        Optional.ofNullable(records.getBusType()).ifPresent(busType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUS_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busType)))
                    .ifPresent(dict -> records.setBusTypeName(dict.getName()));
        });
        // 流通介质
        Optional.ofNullable(records.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> records.setBillFlagName(dict.getName()));
        });
        // 票据类型
        Optional.ofNullable(records.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> records.setBillTypeName(dict.getName()));
        });
        // 业务状态
        Optional.ofNullable(records.getBusStatus()).ifPresent(busStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSSTATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busStatus + "")))
                    .ifPresent(dict -> records.setBusStatusName(dict.getName()));
        });
        // 指令状态
        Optional.ofNullable(records.getInoptStatus()).ifPresent(inoptStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSOPTSTATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(inoptStatus)))
                    .ifPresent(dict -> records.setInstructStatusName(dict.getName()));
        });
        // 票据来源
        Optional.ofNullable(records.getSource()).ifPresent(source -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SOURCE_QUERY");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(source)))
                    .ifPresent(dict -> records.setSourceName(dict.getName()));
        });
        // 票据状态
        Optional.ofNullable(records.getBillStatus()).ifPresent(billStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billStatus)))
                    .ifPresent(dict -> records.setBillStatusName(dict.getName()));
        });
        // 票据池状态
        Optional.ofNullable(records.getPoolStatus()).ifPresent(poolStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_POOL_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(poolStatus)))
                    .ifPresent(dict -> records.setPoolStatusName(dict.getName()));
        });
        // 自动签收
        Optional.ofNullable(records.getIsAutosign()).ifPresent(isAutosign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isAutosign)))
                    .ifPresent(dict -> records.setIsAutosignName(dict.getName()));
        });
        // 票据池状态空或
        Optional.ofNullable(records.getCountPoolStatus()).ifPresent(countPoolStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_COUNT_POOL_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(countPoolStatus)))
                    .ifPresent(dict -> records.setCountPoolStatusName(dict.getName()));
        });
        // 是否统计在池和非在池
        Optional.ofNullable(records.getCountIsPool()).ifPresent(countIsPool -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(countIsPool)))
                    .ifPresent(dict -> records.setCountIsPoolName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(records.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> records.setNewStmIssuofpprIndName(dict.getName()));
        });
        //是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
        Optional.ofNullable(records.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
                    .ifPresent(dict -> records.setBlgrdbagcrclPrmtIndName(dict.getName()));
        });
    }

    /**
     * 应收票据台账列表
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput billReceDataTable(DataTablesInput dataTablesInput) {
        List<String> bursarBillNoList = new ArrayList<>();
        //如果共享系统单号是通过空格隔开的，做一个批量查询
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bursarBillNo"))) {
            String bursarBillNo = dataTablesInput.getFormdata().get("likes_bursarBillNo").toString();
            bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
            if (bursarBillNoList.size() >= 2) {
                dataTablesInput.getFormdata().remove("likes_bursarBillNo");
                dataTablesInput.getFormdata().put("bursarBillNoList", bursarBillNoList);
            }
        }
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLBUS_QUERY_RECECORP, dataTablesInput.getCurrentUserId());
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        Object way = formdata.get("ins_transferWay");
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput, AccebillConstant.AB_BILL_BILLBUS_QUERY_RECECORP, corpIds);
        DataTablesOutput<AbBillPayAndReceVO> dataTablesOutput = abBillQuery.billReceDataTable(dataTablesInput, corpAuthList);
        formdata.put("ins_transferWay", way);
        dataTablesInput.setFormdata(formdata);
        dataTablesInput.setMoneySum(true);
        DataTablesOutput<AbBillPayAndReceVO> dataSumTablesOutput = abBillQuery.billReceDataTable(dataTablesInput, corpAuthList);
//		BigDecimal receiveMoneySum = abBillQuery.getBillReceiveMoneySum(dataTablesInput, corpAuthList);
        BigDecimal receiveMoneySum = dataSumTablesOutput.getData().get(0).getBillMoney();
        if (StringUtils.isNotEmpty(receiveMoneySum)) {
            dataTablesOutput.setInsertInformation(receiveMoneySum.toString());
        }
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERWAY", "AB_BILL_TYPE", "AB_BILL_FLAG","AB_BILL_BUSSTATUS","AB_BILL_INSOPTSTATUS"
                    , "AB_BRIEF_STATUS", "ACCEBILL_EMIT_TYPE", "sf_yn", "AB_BILL_STATUS", "AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND", "AB_BILL_CRCL_INDCD");
            for (AbBillPayAndReceVO bill : listData) {
                injectValues(bill, listDictMap);
                if(StrUtil.isNotBlank(bill.getApplyCorpId())){
                    corpIdSet.add(bill.getApplyCorpId());
                }
                if(StrUtil.isNotBlank(bill.getCorpId())){
                    corpIdSet.add(bill.getCorpId());
                }
                if(StrUtil.isNotBlank(bill.getBusCorpId())){
                    corpIdSet.add(bill.getBusCorpId());
                }
            }
        });

        if (CollectionUtils.isNotEmpty(dataTablesOutput.getData())) {
            List<String> cnAccrecords = dataTablesOutput.getData().stream().map(AbBillPayAndReceVO::getCnAccrecords).collect(Collectors.toList());
            Map<String, CorpDTO> corpDTOMap = abCorpService.listCorpDtoByCorpIds(cnAccrecords);
            if (CollectionUtil.isNotEmpty(corpDTOMap)) {
                dataTablesOutput.getData().stream().forEach(item -> {
                    if (StringUtils.isNotBlank(item.getCnAccrecords())) {
                        if (corpDTOMap.containsKey(item.getCnAccrecords())) {
                            CorpDTO corpDTO = corpDTOMap.get(item.getCnAccrecords());
                            item.setCnAccrecords(corpDTO.getName());
                        }
                    }
                });
            }
        }
        if (bursarBillNoList.size() >= 2) {
            List<AbBillPayAndReceVO> list = dataTablesOutput.getData();
            List<AbBillPayAndReceVO> list1 = new ArrayList<>();
            for (String bursarBillNo : bursarBillNoList) {
                for (AbBillPayAndReceVO abEmitBillVo : list) {
                    if (bursarBillNo.equals(abEmitBillVo.getBursarBillNo())) {
                        list1.add(abEmitBillVo);
                    }
                }
            }
            dataTablesOutput.setData(list1);
        }
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (AbBillPayAndReceVO bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setCorpCodeAndName(corpDTO2 == null ? null : "("+corpDTO2.getCode()+")"+corpDTO2.getName());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }

    /**
     * 加载应付票据台账数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput billPayDataTable(DataTablesInput dataTablesInput, boolean isEmitMake) {
        List<String> bursarBillNoList = new ArrayList<>();
        //如果共享系统单号是通过空格隔开的，做一个批量查询
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bursarBillNo"))) {
            String bursarBillNo = dataTablesInput.getFormdata().get("likes_bursarBillNo").toString();
            bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
            if (bursarBillNoList.size() >= 2) {
                dataTablesInput.getFormdata().remove("likes_bursarBillNo");
                dataTablesInput.getFormdata().put("bursarBillNoList", bursarBillNoList);
            }
        }
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLBUS_QUERY_PAYCORP, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput, AccebillConstant.AB_BILL_BILLBUS_QUERY_PAYCORP, corpIds);
        DataTablesOutput<AbBillPayAndReceVO> data = abBillQuery.billPayDataTable(dataTablesInput, isEmitMake, corpAuthList);
        dataTablesInput.setMoneySum(true);
        DataTablesOutput<AbBillPayAndReceVO> dataSum = abBillQuery.billPayDataTable(dataTablesInput, isEmitMake, corpAuthList);
//		BigDecimal payMoneySum = abBillQuery.getBillPayMoneySum(dataTablesInput, corpAuthList);
        BigDecimal payMoneySum = dataSum.getData().get(0).getBillMoney();
        BigDecimal allPayMoneySum = dataSum.getData().get(0).getPayMoney();
        if (StringUtils.isNotEmpty(payMoneySum)) {
            data.setInsertInformation(payMoneySum.toString());
        }
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(data.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERWAY", "AB_BILL_TYPE", "AB_BILL_FLAG"
                    , "AB_BRIEF_STATUS", "ACCEBILL_EMIT_TYPE", "sf_yn", "AB_BILL_STATUS", "AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND", "AB_BILL_CRCL_INDCD");
            for (AbBillPayAndReceVO bill : listData) {
                bill.setSumPayMoney(allPayMoneySum);
                injectValues(bill, listDictMap);
                if(StrUtil.isNotBlank(bill.getApplyCorpId())){
                    corpIdSet.add(bill.getApplyCorpId());
                }
                if(StrUtil.isNotBlank(bill.getCorpId())){
                    corpIdSet.add(bill.getCorpId());
                }
                if(StrUtil.isNotBlank(bill.getBusCorpId())){
                    corpIdSet.add(bill.getBusCorpId());
                }
            }
        });

        if (bursarBillNoList.size() >= 2) {
            List<AbBillPayAndReceVO> list = data.getData();
            List<AbBillPayAndReceVO> list1 = new ArrayList<>();
            for (String bursarBillNo : bursarBillNoList) {
                for (AbBillPayAndReceVO abEmitBillVo : list) {
                    if (bursarBillNo.equals(abEmitBillVo.getBursarBillNo())) {
                        list1.add(abEmitBillVo);
                    }
                }
            }
            data.setData(list1);
        }
        if(corpIdSet.size() <= 0){
            return data;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return data;
        }
        Optional.ofNullable(data.getData()).ifPresent(listData->{
            for (AbBillPayAndReceVO bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setCorpCodeAndName(corpDTO2 == null ? null : "("+corpDTO2.getCode()+")"+corpDTO2.getName());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return data;
    }

    private void injectValues(AbBillPayAndReceVO bill, Map<String, Map<String, DictDTO>> listDictMap) {
        // 转让方式
        Optional.ofNullable(bill.getTransferWay()).ifPresent(transferWay -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TRANSFERWAY");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(transferWay)))
                    .ifPresent(dict -> bill.setTransferWayName(dict.getName()));
        });
        // 票据类型
        Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        // 流通介质
        Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> bill.setBillFlagName(dict.getName()));
        });
        // 业务类型
        Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
                    .ifPresent(dict -> bill.setBriefIdName(dict.getName()));
        });
        // 开票方式
        Optional.ofNullable(bill.getEmitType()).ifPresent(emitType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("ACCEBILL_EMIT_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitType)))
                    .ifPresent(dict -> bill.setEmitTypeName(dict.getName()));
        });
        // 是否开票保贴
        Optional.ofNullable(bill.getEmitGuandisSign()).ifPresent(emitGuandisSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitGuandisSign)))
                    .ifPresent(dict -> bill.setEmitGuandisSignName(dict.getName()));
        });
        // 开票完成
        Optional.ofNullable(bill.getEmitOpenOk()).ifPresent(emitOpenOk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitOpenOk)))
                    .ifPresent(dict -> bill.setEmitOpenOkName(dict.getName()));
        });
        // 是否已付款
        Optional.ofNullable(bill.getIsPay()).ifPresent(isPay -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("sf_yn");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isPay)))
                    .ifPresent(dict -> bill.setIsPayName(dict.getName()));
        });
        // 票据状态
        Optional.ofNullable(bill.getBillStatus()).ifPresent(billStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billStatus)))
                    .ifPresent(dict -> bill.setBillStatusName(dict.getName()));
        });
        // 是否发送指令标志
        Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
                    .ifPresent(dict -> bill.setSendSignName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
        });
        //是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
        Optional.ofNullable(bill.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
                    .ifPresent(dict -> bill.setBlgrdbagcrclPrmtIndName(dict.getName()));
        });

        if (StringUtils.isNotBlank(bill.getDueRemindStr()) && bill.getDueRemindStr().contains("null") && StringUtils.isNotBlank(bill.getDueRemind())) {
            String lang = multiLangServiceI.getLang(bill.getDueRemind());
            bill.setDueRemind(bill.getDueRemindStr().substring(0, bill.getDueRemindStr().length() - 4) + (lang == null ? "天" : lang));
            bill.setDueRemindStr(lang);
        }

        if (StringUtils.isNotBlank(bill.getBillAnomalies())) {
            String lang = multiLangServiceI.getLang(bill.getBillAnomalies());
            bill.setBillAnomaliesStr(lang);
        }

        Optional.ofNullable(bill.getEbillBlCrclIndc()).ifPresent(ebillBlCrclIndc -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_CRCL_INDCD");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(ebillBlCrclIndc)))
                    .ifPresent(dict -> bill.setEbillBlCrclIndcName(dict.getName()));
        });

        //业务状态
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSSTATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });

        //指令状态
        Optional.ofNullable(bill.getOptStatus()).ifPresent(optStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_INSOPTSTATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(optStatus)))
                    .ifPresent(dict -> bill.setOptStatusName(dict.getName()));
        });
    }

    /**
     * 应收票据导出数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    public void listReceiveData(DataTablesInput dataTablesInput, ModelMap modelMap, List<ExcelExportEntity> entityList,
                                HttpServletRequest request, HttpServletResponse response) throws IOException {
        dataTablesInput.setPageSize(10000000);
        if (ListUtils.isNotNullOrEmpty(dataTablesInput.getOrder())
                && ListUtils.isNotNullOrEmpty(dataTablesInput.getColumns())
                && !"id".equals(dataTablesInput.getColumns().get(0).getName())) {
            for (Order order : dataTablesInput.getOrder()) {
                order.setColumn(order.getColumn() - 1);
            }
        }
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLBUS_QUERY_RECECORP, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        List<String> bursarBillNoList = new ArrayList<>();
        //如果共享系统单号是通过空格隔开的，做一个批量查询
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bursarBillNo"))) {
            String bursarBillNo = dataTablesInput.getFormdata().get("likes_bursarBillNo").toString();
            bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
            if (bursarBillNoList.size() >= 2) {
                dataTablesInput.getFormdata().remove("likes_bursarBillNo");
                dataTablesInput.getFormdata().put("bursarBillNoList", bursarBillNoList);
            }
        }
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput, AccebillConstant.AB_BILL_BILLBUS_QUERY_RECECORP, corpIds);
        List<AbBillPayAndReceVO> listReceiveData = abBillQuery.listReceData(dataTablesInput, corpAuthList);
        Optional.ofNullable(listReceiveData).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERWAY", "AB_BILL_TYPE", "AB_BILL_FLAG"
                    , "AB_BRIEF_STATUS", "ACCEBILL_EMIT_TYPE", "sf_yn", "AB_BILL_STATUS", "AB_BILL_SEND_SIGN");
            for (AbBillPayAndReceVO bill : listData) {
                injectValues(bill, listDictMap);
            }
        });

        if (bursarBillNoList.size() >= 2) {
            List<AbBillPayAndReceVO> list = listReceiveData;
            List<AbBillPayAndReceVO> list1 = new ArrayList<>();
            for (String bursarBillNo : bursarBillNoList) {
                for (AbBillPayAndReceVO abEmitBillVo : list) {
                    if (bursarBillNo.equals(abEmitBillVo.getBursarBillNo())) {
                        list1.add(abEmitBillVo);
                    }
                }
            }
            listReceiveData = list1;
        }
        // 添加导出要素
        this.setDataColumn(dataTablesInput, entityList, modelMap, listReceiveData);
        // 导出
        this.excelExport(dataTablesInput, modelMap, request, response);
    }

    public void listSelectData(DataTablesInput dataTablesInput, ModelMap modelMap,
                               List<ExcelExportEntity> entityList, HttpServletRequest request, HttpServletResponse response, List<AbEmitBillVo> listData) throws IOException {
        // 添加导出要素
        this.setDataColumn(dataTablesInput, entityList, modelMap, listData);
        // 导出
        this.excelExport(dataTablesInput, modelMap, request, response);
    }

    /**
     * 应付票据导出数据
     *
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    public void listPayData(DataTablesInput dataTablesInput, boolean isEmitMake, ModelMap modelMap,
                            List<ExcelExportEntity> entityList, HttpServletRequest request, HttpServletResponse response) throws IOException {
        dataTablesInput.setPageSize(10000000);
        if (ListUtils.isNotNullOrEmpty(dataTablesInput.getOrder())
                && ListUtils.isNotNullOrEmpty(dataTablesInput.getColumns())
                && !"id".equals(dataTablesInput.getColumns().get(0).getName())) {
            for (Order order : dataTablesInput.getOrder()) {
                order.setColumn(order.getColumn() - 1);
            }
        }
        List<String> bursarBillNoList = new ArrayList<>();
        //如果共享系统单号是通过空格隔开的，做一个批量查询
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bursarBillNo"))) {
            String bursarBillNo = dataTablesInput.getFormdata().get("likes_bursarBillNo").toString();
            bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
            if (bursarBillNoList.size() >= 2) {
                dataTablesInput.getFormdata().remove("likes_bursarBillNo");
                dataTablesInput.getFormdata().put("bursarBillNoList", bursarBillNoList);
            }
        }
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLBUS_QUERY_PAYCORP, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccountIsDetail(dataTablesInput, AccebillConstant.AB_BILL_BILLBUS_QUERY_PAYCORP, corpIds);
        List<AbBillPayAndReceVO> listPayData = abBillQuery.listPayData(dataTablesInput, isEmitMake, corpAuthList);
        if (CollectionUtil.isNotEmpty(listPayData)) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERWAY", "AB_BILL_TYPE", "AB_BILL_FLAG"
                    , "AB_BRIEF_STATUS", "ACCEBILL_EMIT_TYPE", "sf_yn", "AB_BILL_STATUS", "AB_BILL_SEND_SIGN", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND", "AB_BILL_CRCL_INDCD");
            for (AbBillPayAndReceVO records : listPayData) {
                injectValues(records, listDictMap);
            }
        }

        if (bursarBillNoList.size() >= 2) {
            List<AbBillPayAndReceVO> list = listPayData;
            List<AbBillPayAndReceVO> list1 = new ArrayList<>();
            for (String bursarBillNo : bursarBillNoList) {
                for (AbBillPayAndReceVO abEmitBillVo : list) {
                    if (bursarBillNo.equals(abEmitBillVo.getBursarBillNo())) {
                        list1.add(abEmitBillVo);
                    }
                }
            }
            listPayData = list1;
        }
        // 添加导出要素
        this.setDataColumn(dataTablesInput, entityList, modelMap, listPayData);
        // 导出
        this.excelExport(dataTablesInput, modelMap, request, response);
    }

    /**
     * 应收应付票据导出数据设置列
     *
     * @param dataTablesInput 查询入参
     * @param entityList
     * @param modelMap
     * @param list            数据
     * @return 返回列表数据信息
     */
    public void setDataColumn(DataTablesInput dataTablesInput, List<ExcelExportEntity> entityList, ModelMap modelMap,
                              List list) {
        List<Column> columns = dataTablesInput.getColumns();
        List<Map<String, Object>> dataResult = new ArrayList<Map<String, Object>>();
        if (columns != null && columns.size() > 0) {
            // 配置导出数据和列头
            this.setColumnTitleAndData(columns, entityList, list, dataResult);
            modelMap.put(MapExcelConstants.PARAMS, new ExportParams(dataTablesInput.getTitle(), dataTablesInput.getTitle(), ExcelType.XSSF));
            modelMap.put(MapExcelConstants.FILE_NAME, dataTablesInput.getFilename());
            modelMap.put(MapExcelConstants.MAP_LIST, dataResult);
            modelMap.put(MapExcelConstants.ENTITY_LIST, entityList);
        }
    }

    /**
     * 导出数据设置列和数据
     *
     * @param columns    列头
     * @param entityList
     * @param list       数据
     * @param dataResult 数据Map集合
     */
    public void setColumnTitleAndData(List<Column> columns, List entityList, List list,
                                      List<Map<String, Object>> dataResult) {
        // 将数据对象转成MAP存进集合
        for (Object abBillPayAndReceVO : list) {
            Map<String, Object> dataMap = BeanUtils.beanToMap(abBillPayAndReceVO);
            if (abBillPayAndReceVO instanceof AbBillPayAndReceVO) {
                AbBillPayAndReceVO info = (AbBillPayAndReceVO) abBillPayAndReceVO;
                if (info.getStartDate() != null) {
                    dataMap.put("startDate", DateUtils.formatDate(info.getStartDate()));
                }
                if (info.getEndDate() != null) {
                    dataMap.put("endDate", DateUtils.formatDate(info.getEndDate()));
                }
                if (info.getEmitOpenDate() != null) {
                    dataMap.put("emitOpenDate", DateUtils.formatDate(info.getEmitOpenDate()));
                }
                if (info.getPayDate() != null) {
                    dataMap.put("payDate", DateUtils.formatDate(info.getPayDate()));
                }
                if (info.getReceDate() != null) {
                    dataMap.put("receDate", DateUtils.formatDate(info.getReceDate()));
                }
                if (info.getTransferDate() != null && "19".equals(info.getOptStatus())) {
                    dataMap.put("transferDate", DateUtils.formatDate(info.getTransferDate()));
                } else {
                    dataMap.put("transferDate", "");
                }
                if (info.getApplyDate() != null) {
                    dataMap.put("applyDate", DateUtils.formatDate(info.getApplyDate()));
                }
                if (info.getBailPercent() != null) {
                    if (info.getBailPercent().compareTo(BigDecimal.ZERO) != 0) {
                        dataMap.put("bailPercent", NumberUtil.formatNumeric(info.getBailPercent().multiply(new BigDecimal("100")), 2) + "%");
                    } else {
                        dataMap.put("bailPercent", "0");
                    }
                }
            } else if (abBillPayAndReceVO instanceof AbEmitBillVo) {
                AbEmitBillVo info = (AbEmitBillVo) abBillPayAndReceVO;
                // 日期字段格式化为 yyyy-MM-dd，去掉时分秒
                if (info.getStartDate() != null) {
                    dataMap.put("startDate", DateUtils.formatDate(info.getStartDate()));
                }
                if (info.getEndDate() != null) {
                    dataMap.put("endDate", DateUtils.formatDate(info.getEndDate()));
                }
                if (info.getApplyDate() != null) {
                    dataMap.put("applyDate", DateUtils.formatDate(info.getApplyDate()));
                }
                if (info.getBusinessDate() != null) {
                    dataMap.put("businessDate", DateUtils.formatDate(info.getBusinessDate()));
                }
                if (info.getPayerCdtRatgDuedt() != null) {
                    dataMap.put("payerCdtRatgDuedt", DateUtils.formatDate(info.getPayerCdtRatgDuedt()));
                }
                // 是否敞口字段显示是/否（isOpenRatName已在查询时通过字典转换）
                if (StringUtils.isNotEmpty(info.getIsOpenRatName())) {
                    dataMap.put("isOpenRat", info.getIsOpenRatName());
                }
            }
            dataResult.add(dataMap);
        }
        // 遍历列头并将需要总计的列存进map
        Map<String, Object> map = new HashMap<String, Object>();
        for (Column column : columns) {
            entityList.add(new ExcelExportEntity(column.getTitle(), column.getData()));
            if (column.getIsPagesum()) {
                BigDecimal moneySum = BigDecimal.ZERO;
                map.put(column.getData(), moneySum);
            }
        }
        // 有总计数据时添加总计列
        if (map.size() > 0) {
            // 计算导出数据中需要总计的数据和
            for (Map.Entry<String, Object> sumMoney : map.entrySet()) {
                for (Map<String, Object> object : dataResult) {
                    if (object.get(sumMoney.getKey()) != null) {
                        BigDecimal money = BigDecimal.valueOf(NumberUtil.formatNumeric(object.get(sumMoney.getKey()).toString()));
                        sumMoney.setValue(money.add(NumberUtil.objToBigDecimal(sumMoney.getValue())));
                    }
                }
            }
            // 数据格式化
            for (Map.Entry<String, Object> sumMoney : map.entrySet()) {
                BigDecimal money = (BigDecimal) sumMoney.getValue();
                sumMoney.setValue(NumberUtil.formatNumeric(money));
            }
            map.put(columns.get(0).getData(), multiLangServiceI.getLang("table.datatable.subtotal"));
            dataResult.add(map);
        }
    }

    /**
     * 导出
     *
     * @param dataTablesInput
     * @param modelMap
     * @param request
     */
    public void excelExport(DataTablesInput dataTablesInput, ModelMap modelMap, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        String codedFileName = "临时文件";
        ExportParams entity = (ExportParams) modelMap.get("params");
        List entityList = (List) modelMap.get("data");
        Collection dataSet = (Collection) modelMap.get("mapList");
        try (
                // 生成工作簿
                Workbook workbook = new XSSFWorkbook();
                ServletOutputStream out = response.getOutputStream();
        ) {

            (new ExcelExportServer()).createSheetForMap(workbook, entity, entityList, dataSet);
            // 设置样式
            this.setStyle(dataTablesInput, workbook);
            // 设置文件名
            if (modelMap.containsKey("fileName")) {
                codedFileName = (String) modelMap.get("fileName");
            }
            codedFileName = codedFileName + ".xlsx";
//		if(request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0
//				|| request.getHeader("USER-AGENT").toLowerCase().indexOf("edge") > 0
//				|| request.getHeader("USER-AGENT")
//				.toLowerCase().indexOf("rv:11.0") > 0) {
            codedFileName = URLEncoder.encode(codedFileName, "UTF-8");
//		} else {
//			codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
//		}
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
    }

    /**
     * 设置样式
     *
     * @param dataTablesInput 列头
     * @param workbook
     */
    public void setStyle(DataTablesInput dataTablesInput, Workbook workbook) {
        // 配置样式
        Sheet sheet = workbook.getSheet(dataTablesInput.getTitle());
        List<Column> columns = dataTablesInput.getColumns();
        // 表头样式
        Font font = workbook.createFont();
        CellStyle cs = workbook.createCellStyle();
        Row head = sheet.getRow(1);
        head.setHeightInPoints(16);
        font.setFontName("宋体"); //字体
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        // 设置样式
        cs.setFont(font);
        // 表体字体
        Font bodyFont = workbook.createFont();
        bodyFont.setFontName("宋体"); //字体
        bodyFont.setFontHeightInPoints((short) 11);
        for (int i = 0; i < columns.size(); i++) {
            // 表头样式
            Cell cell = head.getCell(i);
            cell.setCellStyle(cs);
            // 列头宽度
            int columnWidth = cell.getStringCellValue().getBytes().length;
            CellStyle cellStyle = workbook.createCellStyle();
            // 表体样式
            for (int r = 2; r < sheet.getLastRowNum() + 1; r++) {
                Row row = sheet.getRow(r);
                row.setHeightInPoints(16);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                // 靠右数据
                if ("right".equals(columns.get(i).getAlign())) {
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);        //水平居右对齐
                }
                // 单元格样式
                cellStyle.setFont(bodyFont);
                Cell ce = row.getCell(i);
                int length = ce.getStringCellValue().getBytes().length;
                // 取每列最长宽度
                if (length > columnWidth) {
                    columnWidth = length;
                }
                String columnName = columns.get(i).getName();
                //设置票据金额列为数值可统计计算
                if (columnName.equals("billMoney") || columnName.equals("bailMoney") || columnName.equals("interest") || columnName.equals("realMoney")) {
                    if (StringUtils.isNotEmpty(ce.toString())) {
                        ce.setCellValue(Double.parseDouble(ce.toString().replace(",", "")));
                    }
                    cellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
                }
                ce.setCellStyle(cellStyle);
            }
            //列样式
            sheet.setColumnWidth(i, columnWidth * 256 + 500);
        }
    }

    /**
     * <br>
     * <b>功能：</b> 查询票据融资余额情况 直接融资票据余额、保证金金额、敞口金额（票据金额-保证金） <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/9 <br>
     */
    public List<FinanceLoanDTO> queryProvideFinance(List<String> corpAuth, String curId, Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOS = null;
        //币别不为人民币时则为空
        if (curId.equals(abVoucherinfoViceService.getCurIdByCNY())) {
            //是否启用票据补录模式
            boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
            if (corpAuth != null && !corpAuth.isEmpty() && queryDate != null) {
                financeLoanDTOS = abBillQuery.queryProvideFinance(corpAuth, curId, queryDate, isEmitMake);
            }
        }
        return financeLoanDTOS;
    }

    /**
     * <br>
     * <b>功能：</b> 查询票据利息支出情况 <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/10 <br>
     */
    public List<FinanceLoanDTO> queryInterestFinance(List<String> corpAuth, String curId, Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOS = null;
        //币别不为人民币时则为空
        if (curId.equals(abVoucherinfoViceService.getCurIdByCNY())) {
            if (corpAuth != null && !corpAuth.isEmpty() && queryDate != null) {
                financeLoanDTOS = abBillQuery.queryInterestFinance(corpAuth, curId, queryDate);
            }
        }
        return financeLoanDTOS;
    }

    /**
     * <br>
     * <b>功能：</b> 查询票据融资品种融资余额（第三方票据品种）,融资品种融资余额、保证金金额、敞口金额（票据金额-保证金） <br>
     * <b>作者：</b>zhenghj<br>
     * <b>日期：</b> 2020/12/11 <br>
     */
    public List<FinanceTypeLoanDTO> queryProvideTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        List<FinanceTypeLoanDTO> financeTypeLoanDTOS = null;
        //币别不为人民币时则为空
        if (curId.equals(abVoucherinfoViceService.getCurIdByCNY())) {
            //是否启用票据补录模式
            boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
            if (corpAuth != null && !corpAuth.isEmpty() && queryDate != null) {
                financeTypeLoanDTOS = abBillQuery.queryProvideTypeFinance(corpAuth, curId, queryDate, isEmitMake);
                Optional.ofNullable(financeTypeLoanDTOS).ifPresent(listData -> {
                    Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE");
                    for (FinanceTypeLoanDTO loanDTO : listData) {
                        injectValues(loanDTO, listDictMap);
                    }
                });
            }
        }
        return financeTypeLoanDTOS;
    }

    private void injectValues(FinanceTypeLoanDTO loanDTO, Map<String, Map<String, DictDTO>> listDictMap) {
        Optional.ofNullable(loanDTO.getTypeCode()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType)))
                    .ifPresent(dict -> loanDTO.setTypeName(dict.getName()));
        });
    }

    /**
     * 获取单位统计表数据
     *
     * @param vo   流水vo
     * @param user 当前用户
     * @return 单位统计表数据
     */
    public List<Map<String, Object>> listUnitCountExcelList(AbAccrecordsVO vo, ICurrentUser user) {
        //获取票据类型数据字典信息
        List<DictDTO> billTypeDictList = abDictService.list(AccebillConstant.AB_BILL_TYPE_DICT);
        List<Map<String, Object>> infoList = new ArrayList<>();
        billUnitLstatisticsCommon(vo, user, billTypeDictList, infoList);
        return infoList;
    }

    /**
     * 查询票据单位统计表数据
     *
     * @param vo               流水对象
     * @param user             用户
     * @param billTypeDictList 票据类型集合
     * @param infoList         票据单位统计表数据
     */
    private void billUnitLstatisticsCommon(AbAccrecordsVO vo, ICurrentUser user, List<DictDTO> billTypeDictList, List<Map<String, Object>> infoList) {
        if (StringUtils.isEmpty(vo.getCorpId()) && user != null) {
            List<String> authCorpIdList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCBAL_BILLUNITLSTATISTICS_CORPOP, user.getId());
            if (ListUtils.isNotNullOrEmpty(authCorpIdList) && authCorpIdList.contains(user.getCorpId())) {
                vo.setCorpId(user.getCorpId());
            } else {
                vo.setCorpId(" ");
            }
        }
        if (vo.getEndDate() == null) {
            vo.setEndDate(new Date());
        }
        if (StringUtils.isNotEmpty(vo.getCorpId())) {
            List<DictDTO> dictList = abDictService.list(AccebillConstant.AB_BILL_CORPTJ_BUSTYPE);
            int i = 0, size = dictList.size();
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(vo.getType())) {
                size = 3;
            } else if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(vo.getType())) {
                i = 3;
            }
            String[] allBusTypes = getUnitLstatisticsBusTypes(dictList, 0, dictList.size());
            String[] busTypes = getUnitLstatisticsBusTypes(dictList, i, size);
            List<Map<String, Object>> getList = abBillQuery.getBillUnitLstatisticsInfo(vo, busTypes, allBusTypes, billTypeDictList);
            Map<String, Map<String, Object>> mapInfo = new HashMap<>();
            if (ListUtils.isNotNullOrEmpty(getList)) {
                for (Map<String, Object> info : getList) {
                    mapInfo.put(String.valueOf(info.get("bustype")), info);
                }
            }
            for (; i < size; i++) {
                DictDTO dict = dictList.get(i);
                //根据是否贴现到账确认和是否托收到账初始加载查询数据
                if (AccebillConstant.BRIEF_STATUS_6012.equals(dict.getCode())
                        && !abVoucherinfoViceService.isDiscountConfirm()) {
                    continue;
                }
                if (AccebillConstant.BRIEF_STATUS_6020.equals(dict.getCode())
                        && !abVoucherinfoViceService.isCollectionApplyConfirm()) {
                    continue;
                }
                Map<String, Object> info = mapInfo.get(dict.getCode());
                if (info == null) {
                    info = new HashMap<String, Object>();
                    info.put("bustype", dict.getCode());
                }
                info.put("bustypename", dict.getName());
                infoList.add(info);
            }
        }
    }

    /**
     * 创建单位票据统计表导出对象
     *
     * @param vo    流水对象
     * @param model model
     * @param user  用户
     * @return 导出对象
     */
    public HSSFWorkbook createUnitCountExcelContent(AbAccrecordsVO vo, Model model, ICurrentUser user) {
        List<Map<String, Object>> mapList = this.listUnitCountExcelList(vo, user);
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        String content = "单位票据统计表";
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(content);
        // 创建行
        sheet.createRow(0);
        boolean isCount = AccebillConstant.AB_BILL_VALID_SIGN_1.equals(vo.getCountIsPool());
        // 创建Excel表头
        this.createExcelHeader(sheet, isCount);
        // 创建Excel表中的内容
        this.createExcelBody(workbook, sheet, mapList, isCount);
        return workbook;
    }

    /**
     * 创建表头
     */
    public void createExcelHeader(HSSFSheet sheet, boolean isCount) {
        // 第一行
        HSSFRow row1 = sheet.getRow(0);
        row1.createCell(0).setCellValue("业务类型");
        row1.createCell(1).setCellValue("流通介质");
        row1.createCell(2).setCellValue("票据类型");
        row1.createCell(3).setCellValue("金额");
        if (isCount) {
            row1.createCell(4).setCellValue("在池金额");
            row1.createCell(5).setCellValue("非在池金额");
            row1.createCell(6).setCellValue("总笔数");
            row1.createCell(7).setCellValue("在池笔数");
            row1.createCell(8).setCellValue("非在池笔数");
            row1.createCell(9).setCellValue("在途笔数");
        } else {
            row1.createCell(4).setCellValue("总笔数");
            row1.createCell(5).setCellValue("在途笔数");
        }
    }

    /**
     * 创建Excel表内容
     *
     * @param workbook excel表
     * @param sheet    标签页
     * @param listInfo 单位票据统计表
     * @param isCount  是否统计在池、不在池数据
     */
    private void createExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<Map<String, Object>> listInfo, boolean isCount) {
        // Excel表格内容样式
        HSSFCellStyle cellStyle = this.createExcelBodyStyle(workbook);
        // 指向所在行
        int index = 1;
        int dIndex = 0;
        //获取票据类型数据字典信息
        List<DictDTO> billTypeDictList = abDictService.list(AccebillConstant.AB_BILL_TYPE_DICT);
        for (Map<String, Object> info : listInfo) {
            // 创建行
            HSSFRow row = sheet.createRow(index);
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellStyle(cellStyle);
            cell1.setCellValue((String) info.get("bustypename"));
            sheet.addMergedRegion(new CellRangeAddress(index, index + billTypeDictList.size() * 2 + 2, 0, 0));
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellValue("电票");
            cell2.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(index, index + billTypeDictList.size(), 1, 1));
            index = createRow(sheet, index, dIndex, billTypeDictList, info, row, "dp", isCount);
            // 创建行
            HSSFRow row2 = sheet.createRow(index);
            HSSFCell cell2Zp = row2.createCell(1);
            cell2Zp.setCellValue("纸票");
            cell2Zp.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(index, index + billTypeDictList.size(), 1, 1));
            index = createRow(sheet, index, dIndex, billTypeDictList, info, row2, "zp", isCount);
            dIndex = 0;
            HSSFRow rowDtl = sheet.createRow(index);
            HSSFCell cell3 = rowDtl.createCell(1);
            cell3.setCellValue("总计");
            cell3.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(index, index, 1, 2));
            rowDtl.createCell(3).setCellValue(transferMoney(info, "zmoney"));
            if (isCount) {
                rowDtl.createCell(3).setCellValue(transferMoney(info, "zmoney"));
                rowDtl.createCell(4).setCellValue(transferMoney(info, "zcmoney"));
                rowDtl.createCell(5).setCellValue(transferMoney(info, "fzcmoney"));
                rowDtl.createCell(6).setCellValue(transferToInt(info, "zcount"));
                rowDtl.createCell(7).setCellValue(transferToInt(info, "zccount"));
                rowDtl.createCell(8).setCellValue(transferToInt(info, "fzccount"));
                rowDtl.createCell(9).setCellValue(transferToInt(info, "zztcount"));
            } else {
                rowDtl.createCell(4).setCellValue(transferToInt(info, "zcount"));
                rowDtl.createCell(5).setCellValue(transferToInt(info, "zztcount"));
            }
            index++;
        }
    }

    /**
     * 转换金额格式
     **/
    private String transferMoney(Map<String, Object> info, String param) {
        return info.get(param) == null ? "0.00" : String.format("%.2f", (BigDecimal) info.get(param));
    }

    /**
     * 创建每列数据
     *
     * @param sheet            表格
     * @param index            列索引
     * @param dIndex           标志
     * @param billTypeDictList billType字典集合
     * @param info             单位统计数据
     * @param row              列对象
     * @param dpOrZp           电票或纸票
     * @param isCount          是否导出在池数据
     * @return 当前列索引
     */
    private int createRow(HSSFSheet sheet, int index, int dIndex, List<DictDTO> billTypeDictList,
                          Map<String, Object> info, HSSFRow row, String dpOrZp, boolean isCount) {
        for (DictDTO billTypeInfo : billTypeDictList) {
            if (0 != dIndex) {
                HSSFRow rowDtl = sheet.createRow(index);
                rowDtl.createCell(2).setCellValue(billTypeInfo.getName());
                rowDtl.createCell(3).setCellValue(transferMoney(info, dpOrZp + billTypeInfo.getCode() + "money"));
                if (isCount) {
                    rowDtl.createCell(4).setCellValue(transferMoney(info, dpOrZp + billTypeInfo.getCode() + "zcmoney"));
                    rowDtl.createCell(5).setCellValue(transferMoney(info, dpOrZp + billTypeInfo.getCode() + "fzcmoney"));
                    rowDtl.createCell(6).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "count"));
                    rowDtl.createCell(7).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "zccount"));
                    rowDtl.createCell(8).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "fzccount"));
                    rowDtl.createCell(9).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "ztcount"));
                } else {
                    rowDtl.createCell(4).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "count"));
                    rowDtl.createCell(5).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "ztcount"));
                }
            } else {
                row.createCell(2).setCellValue(billTypeInfo.getName());
                row.createCell(3).setCellValue(transferMoney(info, dpOrZp + billTypeInfo.getCode() + "money"));
                if (isCount) {
                    row.createCell(4).setCellValue(transferMoney(info, dpOrZp + billTypeInfo.getCode() + "zcmoney"));
                    row.createCell(5).setCellValue(transferMoney(info, dpOrZp + billTypeInfo.getCode() + "fzcmoney"));
                    row.createCell(6).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "count"));
                    row.createCell(7).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "zccount"));
                    row.createCell(8).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "fzccount"));
                    row.createCell(9).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "ztcount"));
                } else {
                    row.createCell(4).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "count"));
                    row.createCell(5).setCellValue(transferToInt(info, dpOrZp + billTypeInfo.getCode() + "ztcount"));
                }
                dIndex = 1;
            }
            index++;
        }
        HSSFRow rowDtl = sheet.createRow(index);
        rowDtl.createCell(2).setCellValue("合计");
        rowDtl.createCell(3).setCellValue(transferMoney(info, dpOrZp + "money"));
        if (isCount) {
            rowDtl.createCell(4).setCellValue(transferMoney(info, dpOrZp + "zcmoney"));
            rowDtl.createCell(5).setCellValue(transferMoney(info, dpOrZp + "fzcmoney"));
            rowDtl.createCell(6).setCellValue(transferToInt(info, dpOrZp + "count"));
            rowDtl.createCell(7).setCellValue(transferToInt(info, dpOrZp + "zccount"));
            rowDtl.createCell(8).setCellValue(transferToInt(info, dpOrZp + "fzccount"));
            rowDtl.createCell(9).setCellValue(transferToInt(info, dpOrZp + "ztcount"));
        } else {
            rowDtl.createCell(4).setCellValue(transferToInt(info, dpOrZp + "count"));
            rowDtl.createCell(5).setCellValue(transferToInt(info, dpOrZp + "ztcount"));
        }
        index++;
        return index;
    }

    /**
     * 将值转为int类型
     **/
    private int transferToInt(Map<String, Object> info, String params) {
        Object objVal = info.get(params);
        BigDecimal decimal;
        if (objVal instanceof BigInteger) {
            decimal = new BigDecimal(((BigInteger) objVal).intValue());
        } else {
            decimal = (BigDecimal) objVal;
        }
        return decimal == null ? 0 : decimal.intValue();
    }

    /**
     * 创建Excel内容样式
     *
     * @param workbook 工作表对象
     * @return Excel内容样式
     */
    private HSSFCellStyle createExcelBodyStyle(HSSFWorkbook workbook) {
        HSSFCellStyle cs = workbook.createCellStyle();
//		HSSFFont font = workbook.createFont();
        //字体
//		font.setFontName("宋体");
//		font.setFontHeightInPoints((short) 12);
        //列的字体
//		cs.setFont(font);
        //垂直居中对齐
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        //水平居中对齐
        cs.setAlignment(HorizontalAlignment.CENTER);
        return cs;
    }

    /**
     * 获取票据池信息
     **/
    public AbVoucherinfoVO getBillPoolInfo(AbVoucherinfoVO abVoucherinfoVO) {
        abBillQuery.getBillPoolInfo(abVoucherinfoVO);
        return abVoucherinfoVO;
    }

    /**
     * 查询背书转让详情数据
     *
     * @param dataTablesInput 参数
     * @return 列表数据
     */
    public DataTablesOutput endorsementCountData(DataTablesInput dataTablesInput) {
        return abBillQuery.getEndorsementCountData(dataTablesInput);
    }
}
