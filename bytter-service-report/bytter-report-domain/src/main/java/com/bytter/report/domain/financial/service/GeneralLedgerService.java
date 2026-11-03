package com.bytter.report.domain.financial.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.domain.financial.dao.GeneralLedgerDao;
import com.bytter.report.domain.financial.vo.GeneralLedgerDetailVO;
import com.bytter.report.domain.financial.vo.GeneralLedgerSearchVO;
import com.bytter.report.domain.financial.vo.GeneralLedgerVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.google.common.collect.Maps;
import jodd.typeconverter.Convert;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 总账查询Service
 *
 * @author zuoy 2020-11-19
 */
@Service
public class GeneralLedgerService {
    @Autowired
    private GeneralLedgerDao generalLedgerDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IDictService dictService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * 总账查询列表页面
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.BI_FINANCIAL_GENERAL_LEDGER_QUERY, user.getId());
        DataTablesOutput dataTablesOutput=generalLedgerDao.getDataTablesUnion(dataTablesInput, corpIds);
        List<GeneralLedgerVO> data = dataTablesOutput.getData();
        for(GeneralLedgerVO vo:data){
            String netName=corpService.getCorpName(vo.getNetId());
            vo.setNetName(netName);
            BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(vo.getCurId());
            vo.setCurName(btCurrencyDTO.getCurName());
        }
        dataTablesOutput.setData(data);
        return dataTablesOutput;
    }
    /**
     * 总账查询详情页数据加载
     *
     * @param searchVO
     * @return
     */
    public List<GeneralLedgerDetailVO> getGeneralLedgerDetailList(GeneralLedgerSearchVO searchVO) {
        List<GeneralLedgerDetailVO> newDetailList = new ArrayList<>();
        Map<String, GeneralLedgerDetailVO> existDetailMap = Maps.newHashMap();
        // 上年转入
        GeneralLedgerDetailVO lastYearLedgerDetail = getLastYearLedgerDetail(searchVO);
        newDetailList.add(lastYearLedgerDetail);
        Date lastYearLedgerDate = DateUtils.parse(lastYearLedgerDetail.getLedgerDate(), "yyyy-MM");
        existDetailMap.put(DateUtils.getYear(lastYearLedgerDate) + String.valueOf(DateUtils.getMonth(lastYearLedgerDate)), lastYearLedgerDetail);
        // 本年累计发生额
        Map<String, BigDecimal> totalMoneyMap = Maps.newHashMap();
        // 跨年查询，本年年度累计数据
        GeneralLedgerDetailVO totalDetail = getTotalMonthLedgerDetail(searchVO, totalMoneyMap, existDetailMap);
        if (null != totalDetail) {
            newDetailList.add(totalDetail);
        }
        // 查询本月合计
        List<GeneralLedgerDetailVO> detailList = generalLedgerDao.getGeneralLedgerDetailList(searchVO);
        // 循环将已存在的明细数据组织为map集合
        for (GeneralLedgerDetailVO detail : detailList) {
            // 设置月份合计
            Date ledgerDate = DateUtils.parse(detail.getLedgerDate(), "yyyy-MM");
            detail.setYear(String.valueOf(DateUtils.getYear(ledgerDate)));
            detail.setMonth(String.valueOf(DateUtils.getMonth(ledgerDate)));
            detail.setLedgerDate(DateUtils.format(ledgerDate, "yyyy年MM月"));
            // 行数据类型 0-月份合计
            detail.setRowDataType(0);
            existDetailMap.put(detail.getYear() + detail.getMonth(), detail);
        }
        // 补齐无数据的月份总账明细
        List<GeneralLedgerDetailVO> tempDetailList = makeUpDetailMonth(existDetailMap, searchVO.getBeginDate(), searchVO.getEndDate(), searchVO.getBalanceWay());
        // 添加明细累计
        newDetailList.addAll(setDetailTotal(tempDetailList, totalMoneyMap));
        return newDetailList;
    }
    /**
     * 获取上年转入
     *
     * @param searchVO
     * @return
     */
    public GeneralLedgerDetailVO getLastYearLedgerDetail(GeneralLedgerSearchVO searchVO) {
        // 设置上年转入
        GeneralLedgerDetailVO lastYearLedgerDetail = generalLedgerDao.getLastYearLedgerDetail(searchVO);
        if (null == lastYearLedgerDetail) {
            lastYearLedgerDetail = new GeneralLedgerDetailVO();
            Date beginDate = DateUtils.parseDate(searchVO.getBeginDate() + "-01");
            // 本年第一天减一为上年12月
            Date lastYearDate = DateUtils.addMonth(DateUtils.getFirstYearDate(beginDate), -1);
            lastYearLedgerDetail.setLedgerDate(DateUtils.format(lastYearDate, "yyyy-MM"));
            lastYearLedgerDetail.setBalance(generalLedgerDao.getInitBalance(searchVO));
        }
        // 行数据类型 2-上年转入
        lastYearLedgerDetail.setRowDataType(2);
        return lastYearLedgerDetail;
    }
    /**
     * 设置本年年度累计数据
     *
     * @param searchVO
     * @param totalMoneyMap
     * @return
     */
    public GeneralLedgerDetailVO getTotalMonthLedgerDetail(GeneralLedgerSearchVO searchVO,
                                                           Map<String, BigDecimal> totalMoneyMap, Map<String, GeneralLedgerDetailVO> existDetailMap) {
        Date beginDate = DateUtils.parseDate(searchVO.getBeginDate() + "-01");
        GeneralLedgerDetailVO totalDetail = null;
        // 跨年，设置本年年度累计数据
        if (DateUtils.getMonth(beginDate) == 1) {
            return null;
        }
        // 查找开始时间的本年之前每月的数据
        Date startMonthDate = DateUtils.getFirstYearDate(beginDate);
        Date endMonthDate = DateUtils.addMonth(beginDate, -1);
        List<GeneralLedgerDetailVO> list = generalLedgerDao.listLastMonthLedgerDetail(searchVO, startMonthDate, endMonthDate);
        // 循环，设置本年年度累计数据
        BigDecimal totalDebitMoney = BigDecimal.ZERO;
        BigDecimal totalCreditMoney = BigDecimal.ZERO;
        for (GeneralLedgerDetailVO vo : list) {
            totalDebitMoney = totalDebitMoney.add(vo.getDebitMoney());
            totalCreditMoney = totalCreditMoney.add(vo.getCreditMoney());
        }
        totalDetail = new GeneralLedgerDetailVO();
        totalDetail.setDebitMoney(totalDebitMoney);
        totalDetail.setCreditMoney(totalCreditMoney);
        String debitKey = DateUtils.getYear(startMonthDate) + "debit";
        totalMoneyMap.put(debitKey, totalDetail.getDebitMoney());
        String creditKey = DateUtils.getYear(startMonthDate) + "credit";
        totalMoneyMap.put(creditKey, totalDetail.getCreditMoney());
        // 行数据类型 3-本年年度累计数据
        totalDetail.setRowDataType(3);
        GeneralLedgerDetailVO lastDetail = getLastDetail(existDetailMap, startMonthDate);
        if (null != lastDetail) {
            if(lastDetail.getBalance() == null){
                lastDetail.setBalance(BigDecimal.ZERO);
            }
            if (1 == searchVO.getBalanceWay()) {
                BigDecimal creditMoney = lastDetail.getBalance().add(totalDetail.getDebitMoney()).subtract(totalDetail.getCreditMoney());
                totalDetail.setBalance(creditMoney);
            } else {
                BigDecimal debitMoney = lastDetail.getBalance().add(totalDetail.getCreditMoney()).subtract(totalDetail.getDebitMoney());
                totalDetail.setBalance(debitMoney);
            }
        }
        String key = String.valueOf(DateUtils.getYear(beginDate)) + DateUtils.getMonth(DateUtils.addMonth(beginDate, -1));
        existDetailMap.put(key, totalDetail);
        return totalDetail;
    }
    /**
     * 补齐无数据的月份总账明细
     *
     * @param existDetailMap  存在的明细
     * @param searchBeginDate 查询开始日期
     * @param searchEndDate   查看结束日期
     * @return
     */
    public List<GeneralLedgerDetailVO> makeUpDetailMonth(Map<String, GeneralLedgerDetailVO> existDetailMap,
                                                         String searchBeginDate, String searchEndDate, Integer balanceWay) {
        Date tempBeginDate = DateUtils.parseDate(searchBeginDate + "-01");
        Date endDate = DateUtils.parseDate(searchEndDate + "-01");
        List<GeneralLedgerDetailVO> tempDetailList = new ArrayList<>();
        while (tempBeginDate.compareTo(endDate) <= 0) {
            String year = String.valueOf(DateUtils.getYear(tempBeginDate));
            String month = String.valueOf(DateUtils.getMonth(tempBeginDate));
            if (tempBeginDate.compareTo(DateUtils.getDate()) > 0) {
                break;
            }
            GeneralLedgerDetailVO lastDetail = getLastDetail(existDetailMap, tempBeginDate);
            // 将空值转为0
            lastDetail.setBalance(Convert.toBigDecimal(lastDetail.getBalance(), BigDecimal.ZERO));
            lastDetail.setDebitMoney(Convert.toBigDecimal(lastDetail.getDebitMoney(), BigDecimal.ZERO));
            lastDetail.setCreditMoney(Convert.toBigDecimal(lastDetail.getCreditMoney(), BigDecimal.ZERO));
            if (existDetailMap.containsKey(year + month)) {
                GeneralLedgerDetailVO curDetail = existDetailMap.get(year + month);
                if (1 == balanceWay) {
                    BigDecimal creditMoney = lastDetail.getBalance().add(curDetail.getDebitMoney()).subtract(curDetail.getCreditMoney());
                    curDetail.setBalance(creditMoney);
                } else {
                    BigDecimal debitMoney = lastDetail.getBalance().add(curDetail.getCreditMoney()).subtract(curDetail.getDebitMoney());
                    curDetail.setBalance(debitMoney);
                }
                tempDetailList.add(curDetail);
            } else {
                GeneralLedgerDetailVO detail = new GeneralLedgerDetailVO();
                detail.setYear(year);
                detail.setMonth(month);
                detail.setLedgerDate(DateUtils.format(tempBeginDate, "yyyy年MM月"));
                detail.setCreditMoney(BigDecimal.ZERO);
                detail.setDebitMoney(BigDecimal.ZERO);
                if (1 == balanceWay) {
                    detail.setBalance(lastDetail.getBalance().add(detail.getDebitMoney()).subtract(detail.getCreditMoney()));
                } else {
                    detail.setBalance(lastDetail.getBalance().add(detail.getCreditMoney()).subtract(detail.getDebitMoney()));
                }
                // 行数据类型 0-月份合计
                detail.setRowDataType(0);
                existDetailMap.put(detail.getYear() + detail.getMonth(), detail);
                tempDetailList.add(detail);
            }
            tempBeginDate = DateUtils.addMonth(tempBeginDate, 1);
        }
        return tempDetailList;
    }
    public GeneralLedgerDetailVO getLastDetail(Map<String, GeneralLedgerDetailVO> existDetailMap, Date tempBeginDate) {
        // 获取上个月的合计
        Date lastDate = DateUtils.addMonth(tempBeginDate, -1);
        String lastMonthKey = DateUtils.getYear(lastDate) + String.valueOf(DateUtils.getMonth(lastDate));
        GeneralLedgerDetailVO lastDetail = existDetailMap.get(lastMonthKey);
        if (null == lastDetail) {
            return getLastDetail(existDetailMap, lastDate);
        }
        return lastDetail;
    }
    /**
     * 添加明细累计数
     *
     * @param detailList    月份合计集合
     * @param totalMoneyMap 本年年度累计发生额
     * @return
     */
    public List<GeneralLedgerDetailVO> setDetailTotal(List<GeneralLedgerDetailVO> detailList, Map<String, BigDecimal> totalMoneyMap) {
        List<GeneralLedgerDetailVO> newDetailList = new ArrayList<>();
        GeneralLedgerDetailVO totalDetail = null;
        for (GeneralLedgerDetailVO detail : detailList) {
            newDetailList.add(detail);
            // 创建本年累计对象
            totalDetail = new GeneralLedgerDetailVO();
            // 设置累计贷方发生额
            String debitKey = detail.getYear() + "debit";
            if (totalMoneyMap.containsKey(debitKey)) {
                BigDecimal debitMoney = totalMoneyMap.get(debitKey).add(detail.getDebitMoney());
                totalMoneyMap.put(debitKey, debitMoney);
            } else {
                totalMoneyMap.put(debitKey, detail.getDebitMoney());
            }
            totalDetail.setDebitMoney(totalMoneyMap.get(debitKey));
            // 设置累计贷方发生额
            String creditKey = detail.getYear() + "credit";
            if (totalMoneyMap.containsKey(creditKey)) {
                BigDecimal creditMoney = totalMoneyMap.get(creditKey).add(detail.getCreditMoney());
                totalMoneyMap.put(creditKey, creditMoney);
            } else {
                totalMoneyMap.put(creditKey, detail.getCreditMoney());
            }
            totalDetail.setCreditMoney(totalMoneyMap.get(creditKey));
            totalDetail.setBalance(detail.getBalance());
            // 行数据类型 1-本年累计
            totalDetail.setRowDataType(1);
            newDetailList.add(totalDetail);
        }
        return newDetailList;
    }
    /**
     * 导出当前页
     *
     * @param request
     * @param response
     * @param searchVO
     */
    public void exportCurrentPage(HttpServletRequest request, HttpServletResponse response, GeneralLedgerSearchVO searchVO) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + ExportUtils.getFileName(request, "总账查询") + ".xls");
        try (OutputStream os = response.getOutputStream()) {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(searchVO.getSubjectCode() + "-" + searchVO.getSubjectName());
            // 无边框靠左对齐样式
            HSSFCellStyle leftNoBorderCellStyle = workbook.createCellStyle();
            leftNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            leftNoBorderCellStyle.setAlignment(HorizontalAlignment.LEFT);
            // 无边框居中对齐样式
            HSSFCellStyle centerNoBorderCellStyle = workbook.createCellStyle();
            centerNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            centerNoBorderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            // 标题
            setExportTitle(sheet, searchVO.getNetName(), centerNoBorderCellStyle);
            // 副标题
            setExportSubTitle(sheet, centerNoBorderCellStyle);
            // 表头
            setExportHead(sheet, leftNoBorderCellStyle, centerNoBorderCellStyle, searchVO);
            // 表格列标题
            setExportTableColumnHead(sheet, centerCellStyle, searchVO);
            // 表格内容
            setExportTableContent(sheet, centerCellStyle, rightCellStyle, searchVO);
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置标题
     *
     * @param sheet
     * @param title
     * @param centerCellStyle
     */
    public void setExportTitle(HSSFSheet sheet, String title, HSSFCellStyle centerCellStyle) {
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth(15);
        // (实际列宽+列宽基数：0.72) * 256
//            int width = new BigDecimal((80 / 3.5 + 0.72) * 256).intValue();
        sheet.setColumnWidth(0, BigDecimal.valueOf((13 + 0.72) * 256).intValue());
        sheet.setColumnWidth(1, BigDecimal.valueOf((13 + 0.72) * 256).intValue());
        sheet.setColumnWidth(2, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(3, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(4, BigDecimal.valueOf((8 + 0.72) * 256).intValue());
        sheet.setColumnWidth(5, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        // 主标题
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(centerCellStyle);
        titleCell.setCellValue(title);
        for (int i = 1; i <= 5; i++) {
            titleRow.createCell(i);
        }
        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //合并列
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 5);
        ExportUtils.setRegionStyle(sheet, region, centerCellStyle);
        sheet.addMergedRegion(region);
    }
    /**
     * 设置副标题
     *
     * @param sheet
     * @param centerCellStyle
     */
    public void setExportSubTitle(HSSFSheet sheet, HSSFCellStyle centerCellStyle) {
        HSSFRow subtitleRow = sheet.createRow(1);
        HSSFCell subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellStyle(centerCellStyle);
        subtitleCell.setCellValue("总账");
        for (int i = 1; i <= 5; i++) {
            subtitleRow.createCell(i);
        }
        //合并列
        CellRangeAddress subtitleRegion = new CellRangeAddress(1, 1, 0, 5);
        ExportUtils.setRegionStyle(sheet, subtitleRegion, centerCellStyle);
        sheet.addMergedRegion(subtitleRegion);
    }
    /**
     * 设置表头
     *
     * @param sheet
     * @param centerCellStyle
     */
    public void setExportHead(HSSFSheet sheet, HSSFCellStyle leftCellStyle, HSSFCellStyle centerCellStyle, GeneralLedgerSearchVO searchVO) {
        HSSFRow row = sheet.createRow(2);
        HSSFCell subjectCell = row.createCell(0);
        subjectCell.setCellStyle(leftCellStyle);
        subjectCell.setCellValue("科目名称：" + searchVO.getSubjectName());
        row.createCell(1);
        //合并列
        CellRangeAddress subjectRegion = new CellRangeAddress(2, 2, 0, 1);
        ExportUtils.setRegionStyle(sheet, subjectRegion, centerCellStyle);
        sheet.addMergedRegion(subjectRegion);
        row.createCell(2);
        HSSFCell dateCell = row.createCell(3);
        row.createCell(4);
        dateCell.setCellStyle(centerCellStyle);
        dateCell.setCellValue("制表日期：" + DateUtils.formatDate());
        //合并列
        CellRangeAddress dateRegion = new CellRangeAddress(2, 2, 3, 4);
        ExportUtils.setRegionStyle(sheet, dateRegion, centerCellStyle);
        sheet.addMergedRegion(dateRegion);
        HSSFCell curNameCell = row.createCell(5);
        curNameCell.setCellStyle(centerCellStyle);
        curNameCell.setCellValue("制表日期：" + searchVO.getCurName());
    }
    /**
     * 设置表格列标题
     *
     * @param sheet
     * @param centerCellStyle
     */
    public void setExportTableColumnHead(HSSFSheet sheet, HSSFCellStyle centerCellStyle, GeneralLedgerSearchVO searchVO) {
        Date endDate = DateUtils.parseDate(searchVO.getEndDate() + "-01");
        List<String> headList = new ArrayList<>();
        headList.add(String.valueOf(DateUtils.getYear(endDate)));
        headList.add("摘要");
        headList.add("借方发生额");
        headList.add("贷方发生额");
        headList.add("方向");
        headList.add("余额");
        HSSFRow headRow = sheet.createRow(3);
        for (int i = 0; i < headList.size(); i++) {
            HSSFCell cell = headRow.createCell(i);
            cell.setCellStyle(centerCellStyle);
            cell.setCellValue(headList.get(i));
        }
    }
    /**
     * 设置表格内容
     *
     * @param sheet
     * @param centerCellStyle
     * @param rightCellStyle
     * @param searchVO
     */
    public void setExportTableContent(HSSFSheet sheet, HSSFCellStyle centerCellStyle, HSSFCellStyle rightCellStyle,
                                      GeneralLedgerSearchVO searchVO) {
        int rowNum = 4;
        List<GeneralLedgerDetailVO> detailList = getGeneralLedgerDetailList(searchVO);
        for (GeneralLedgerDetailVO vo : detailList) {
            HSSFRow row = sheet.createRow(rowNum);
            // 月份
            HSSFCell cellLedgerDate = row.createCell(0);
            cellLedgerDate.setCellStyle(centerCellStyle);
            // 摘要
            HSSFCell cellSummary = row.createCell(1);
            cellSummary.setCellStyle(centerCellStyle);
            // 借方发生额
            HSSFCell cellDebitMoney = row.createCell(2);
            cellDebitMoney.setCellStyle(rightCellStyle);
            // 贷方发生额
            HSSFCell cellCreditMoney = row.createCell(3);
            cellCreditMoney.setCellStyle(rightCellStyle);
            // 方向
            HSSFCell cellBalanceWay = row.createCell(4);
            cellBalanceWay.setCellStyle(centerCellStyle);
            // 余额
            HSSFCell cellBalance = row.createCell(5);
            cellBalance.setCellStyle(rightCellStyle);
            // 行数据类型 0-月份合计，1-本年累计，2-上年转入，3-本年年度累计数据
            if (vo.getRowDataType() == 1) {
                cellSummary.setCellValue("本年累计");
                cellDebitMoney.setCellValue(NumberUtil.formatNumeric(vo.getDebitMoney()));
                cellCreditMoney.setCellValue(NumberUtil.formatNumeric(vo.getCreditMoney()));
                cellBalance.setCellValue(NumberUtil.formatNumeric(vo.getBalance()));
            } else if (vo.getRowDataType() == 2) {
                cellSummary.setCellValue("上年转入");
                cellBalanceWay.setCellValue(searchVO.getBalanceWayName());
                cellBalance.setCellValue(NumberUtil.formatNumeric(vo.getBalance()));
            } else if (vo.getRowDataType() == 3) {
                cellSummary.setCellValue("本年累计");
                cellDebitMoney.setCellValue(NumberUtil.formatNumeric(vo.getDebitMoney()));
                cellCreditMoney.setCellValue(NumberUtil.formatNumeric(vo.getCreditMoney()));
            } else {
                cellLedgerDate.setCellValue(vo.getLedgerDate());
                cellSummary.setCellValue(vo.getMonth() + "月份合计");
                cellDebitMoney.setCellValue(NumberUtil.formatNumeric(vo.getDebitMoney()));
                cellCreditMoney.setCellValue(NumberUtil.formatNumeric(vo.getCreditMoney()));
                cellBalanceWay.setCellValue(searchVO.getBalanceWayName());
                cellBalance.setCellValue(NumberUtil.formatNumeric(vo.getBalance()));
            }
            rowNum++;
        }
    }
    public void exportAll(HttpServletRequest request, HttpServletResponse response, GeneralLedgerSearchVO searchVO, List<FcSubjectDTO> subjectList) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + ExportUtils.getFileName(request, "总账查询") + ".xls");
        try (OutputStream os = response.getOutputStream()) {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 无边框靠左对齐样式
            HSSFCellStyle leftNoBorderCellStyle = workbook.createCellStyle();
            leftNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            leftNoBorderCellStyle.setAlignment(HorizontalAlignment.LEFT);
            // 无边框居中对齐样式
            HSSFCellStyle centerNoBorderCellStyle = workbook.createCellStyle();
            centerNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            centerNoBorderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            Map<String, DictDTO> dictMap = dictService.listMap("Admin.BalanceWay");
            for (FcSubjectDTO subjectDTO : subjectList) {
                searchVO.setSubjectId(subjectDTO.getId());
                searchVO.setSubjectCode(subjectDTO.getSubjectCode());
                searchVO.setSubjectName(subjectDTO.getSubjectName());
                searchVO.setBalanceWay(subjectDTO.getBalanceWay());
                Optional.ofNullable(subjectDTO.getBalanceWay()).flatMap(balanceWay -> Optional.ofNullable(dictMap.get(String.valueOf(balanceWay)))).ifPresent(dict -> {
                    searchVO.setBalanceWayName(dict.getName());
                });
                // 生成一个表格
                HSSFSheet sheet = workbook.createSheet(searchVO.getSubjectCode() + "-" + searchVO.getSubjectName());
                // 标题
                setExportTitle(sheet, searchVO.getNetName(), centerNoBorderCellStyle);
                // 副标题
                setExportSubTitle(sheet, centerNoBorderCellStyle);
                // 表头
                setExportHead(sheet, leftNoBorderCellStyle, centerNoBorderCellStyle, searchVO);
                // 表格列标题
                setExportTableColumnHead(sheet, centerCellStyle, searchVO);
                // 表格内容
                setExportTableContent(sheet, centerCellStyle, rightCellStyle, searchVO);
            }
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
