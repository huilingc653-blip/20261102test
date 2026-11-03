package com.bytter.report.domain.cms.service;
import com.bytter.bill.api.bill.service.IAcceBillBalanceQueryService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.finance.api.IProvideLoanQueryService;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.domain.cms.vo.FinancingBalanceVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 各融资品种融资余额Service
 *
 * @author zuoy 2020-12-07
 */
@Service
public class FinancingBalanceService {
    @DubboReference(check = false)
	private IProvideLoanQueryService provideLoanQueryService;
    @DubboReference(check = false)
	private IAcceBillBalanceQueryService billBalanceQueryService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    public List<FinancingBalanceVO> search(Date queryDate, String curId, ICurrentUser user) {
        List<FinancingBalanceVO> list = new ArrayList<>();
        // 总合计
        FinancingBalanceVO total = new FinancingBalanceVO();
        total.setTypeName("合计");
        total.setMoney(BigDecimal.ZERO);
        total.setBondMoney(BigDecimal.ZERO);
        total.setOpenMoney(BigDecimal.ZERO);
        List<BigDecimal> moneyList = new ArrayList<>();
        List<BigDecimal> openMoneyList = new ArrayList<>();
        // 查询有权限的单位
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.BI_CMS_FINANCING_BALANCE_QUERY, user.getId());
        // 借款
        List<FinanceTypeLoanDTO> provideTypeFinance = provideLoanQueryService.queryProvideTypeFinance(corpIds, curId, queryDate);
        setProvideTypeFinance(list, total, moneyList, openMoneyList, provideTypeFinance);
        // 票据
        List<com.bytter.bill.api.bill.dto.FinanceTypeLoanDTO> billTypeFinance = billBalanceQueryService.queryProvideTypeFinance(corpIds, curId, queryDate);
        // todo:数据转换
        List<FinanceTypeLoanDTO> financeTypeLoanDTOS = BeanUtils.copyListProperties(billTypeFinance, FinanceTypeLoanDTO.class);
        setBillTypeFinance(list, total, moneyList, openMoneyList, financeTypeLoanDTOS);
        // 债券
        List<FinanceTypeLoanDTO> inlTypeFinance = provideLoanQueryService.queryInlTypeFinance(corpIds, curId, queryDate);
        setInlTypeFinance(list, total, moneyList, openMoneyList, inlTypeFinance);
        // 计算比例
        setCalculationScale(list, moneyList, openMoneyList);
        // 添加总合计
        list.add(total);
        return list;
    }
    /**
     * 计算比例
     *
     * @param list
     * @param moneyList
     * @param openMoneyList
     */
    public void setCalculationScale(List<FinancingBalanceVO> list, List<BigDecimal> moneyList, List<BigDecimal> openMoneyList) {
        // 计算占比率
        int i = 0;
        Map<String, BigDecimal> map = Maps.newHashMap();
        for (FinancingBalanceVO vo : list) {
            if (null != vo.getCalculationScale() && vo.getCalculationScale()) {
                // 占总额的比例
                String key = vo.getSysCode() + "scale";
                double scale = getPercentValue(moneyList, i, 2);
                vo.setScale(new BigDecimal(scale));
                if (map.containsKey(key)) {
                    BigDecimal decimal = map.get(key).add(vo.getScale());
                    map.put(key, decimal);
                } else {
                    map.put(key, vo.getScale());
                }
                // 占敞口的比例
                String openKey = vo.getSysCode() + "openMoneyScale";
                double openMoneyScale = getPercentValue(openMoneyList, i, 2);
                vo.setOpenMoneyScale(new BigDecimal(openMoneyScale));
                if (map.containsKey(openKey)) {
                    BigDecimal decimal = map.get(openKey).add(vo.getOpenMoneyScale());
                    map.put(openKey, decimal);
                } else {
                    map.put(openKey, vo.getOpenMoneyScale());
                }
                i++;
            }
        }
        for (FinancingBalanceVO vo : list) {
            if (null == vo.getCalculationScale()) {
                // 占总额的比例
                String key = vo.getSysCode() + "scale";
                vo.setScale(map.get(key));
                // 占敞口的比例
                String openKey = vo.getSysCode() + "openMoneyScale";
                vo.setOpenMoneyScale(map.get(openKey));
            }
        }
    }
    /**
     * 设置借款数据
     *
     * @param list
     * @param total
     * @param moneyList
     * @param openMoneyList
     * @param provideTypeFinance
     */
    public void setProvideTypeFinance(List<FinancingBalanceVO> list, FinancingBalanceVO total,
                                      List<BigDecimal> moneyList, List<BigDecimal> openMoneyList, List<FinanceTypeLoanDTO> provideTypeFinance) {
        // 借款合计
        FinancingBalanceVO totalProvideTypeFinance = new FinancingBalanceVO();
        totalProvideTypeFinance.setSysCode("01");
        totalProvideTypeFinance.setTypeName("借款合计");
        totalProvideTypeFinance.setMoney(BigDecimal.ZERO);
        totalProvideTypeFinance.setBondMoney(BigDecimal.ZERO);
        totalProvideTypeFinance.setOpenMoney(BigDecimal.ZERO);
        if (!CollectionUtils.isEmpty(provideTypeFinance)) {
            for (FinanceTypeLoanDTO financeTypeLoanDTO : provideTypeFinance) {
                FinancingBalanceVO dto = new FinancingBalanceVO();
                BeanUtils.copyNotNullProperties(financeTypeLoanDTO, dto);
                dto.setBondMoney(null == dto.getBondMoney() ? BigDecimal.ZERO : dto.getBondMoney());
                dto.setOpenMoney(dto.getMoney().subtract(dto.getBondMoney()));
                dto.setSysCode("01");
                dto.setCalculationScale(true);
                // 设置计算占比
                moneyList.add(dto.getMoney());
                openMoneyList.add(dto.getOpenMoney());
                // 计算借款合计
                totalProvideTypeFinance.setMoney(totalProvideTypeFinance.getMoney().add(dto.getMoney()));
                totalProvideTypeFinance.setBondMoney(totalProvideTypeFinance.getBondMoney().add(dto.getBondMoney()));
                totalProvideTypeFinance.setOpenMoney(totalProvideTypeFinance.getOpenMoney().add(dto.getOpenMoney()));
                list.add(dto);
            }
            // 计算总合计
            total.setMoney(total.getMoney().add(totalProvideTypeFinance.getMoney()));
            total.setBondMoney(total.getBondMoney().add(totalProvideTypeFinance.getBondMoney()));
            total.setOpenMoney(total.getOpenMoney().add(totalProvideTypeFinance.getOpenMoney()));
        }
        list.add(totalProvideTypeFinance);
    }
    /**
     * 设置票据数据
     *
     * @param list
     * @param total
     * @param moneyList
     * @param openMoneyList
     * @param billTypeFinance
     */
    public void setBillTypeFinance(List<FinancingBalanceVO> list, FinancingBalanceVO total,
                                   List<BigDecimal> moneyList, List<BigDecimal> openMoneyList, List<FinanceTypeLoanDTO> billTypeFinance) {
        FinancingBalanceVO totalBillTypeFinance = new FinancingBalanceVO();
        totalBillTypeFinance.setSysCode("02");
        totalBillTypeFinance.setTypeName("票据合计");
        totalBillTypeFinance.setMoney(BigDecimal.ZERO);
        totalBillTypeFinance.setBondMoney(BigDecimal.ZERO);
        totalBillTypeFinance.setOpenMoney(BigDecimal.ZERO);
        if (!CollectionUtils.isEmpty(billTypeFinance)) {
            for (FinanceTypeLoanDTO financeTypeLoanDTO : billTypeFinance) {
                FinancingBalanceVO dto = new FinancingBalanceVO();
                BeanUtils.copyNotNullProperties(financeTypeLoanDTO, dto);
                dto.setSysCode("02");
                dto.setBondMoney(null == dto.getBondMoney() ? BigDecimal.ZERO : dto.getBondMoney());
                dto.setOpenMoney(null == dto.getOpenMoney() ? dto.getMoney().subtract(dto.getBondMoney()) : dto.getOpenMoney());
                dto.setCalculationScale(true);
                // 设置计算占比
                moneyList.add(dto.getMoney());
                openMoneyList.add(dto.getOpenMoney());
                // 计算票据合计
                totalBillTypeFinance.setMoney(totalBillTypeFinance.getMoney().add(dto.getMoney()));
                totalBillTypeFinance.setBondMoney(totalBillTypeFinance.getBondMoney().add(dto.getBondMoney()));
                totalBillTypeFinance.setOpenMoney(totalBillTypeFinance.getOpenMoney().add(dto.getOpenMoney()));
                list.add(dto);
            }
            // 计算总合计
            total.setMoney(total.getMoney().add(totalBillTypeFinance.getMoney()));
            total.setBondMoney(total.getBondMoney().add(totalBillTypeFinance.getBondMoney()));
            total.setOpenMoney(total.getOpenMoney().add(totalBillTypeFinance.getOpenMoney()));
        }
        list.add(totalBillTypeFinance);
    }
    /**
     * 设置债券数据
     *
     * @param list
     * @param total
     * @param moneyList
     * @param openMoneyList
     * @param inlTypeFinance
     */
    public void setInlTypeFinance(List<FinancingBalanceVO> list, FinancingBalanceVO total,
                                  List<BigDecimal> moneyList, List<BigDecimal> openMoneyList, List<FinanceTypeLoanDTO> inlTypeFinance) {
        FinancingBalanceVO totalInlTypeFinance = new FinancingBalanceVO();
        totalInlTypeFinance.setSysCode("03");
        totalInlTypeFinance.setTypeName("债券合计");
        totalInlTypeFinance.setMoney(BigDecimal.ZERO);
        totalInlTypeFinance.setBondMoney(BigDecimal.ZERO);
        totalInlTypeFinance.setOpenMoney(BigDecimal.ZERO);
        if (!CollectionUtils.isEmpty(inlTypeFinance)) {
            // 01-借款 02-票据 03-债劵
            FinanceTypeLoanDTO dto = inlTypeFinance.get(0);
            BeanUtils.copyNotNullProperties(dto, totalInlTypeFinance);
            totalInlTypeFinance.setBondMoney(null == totalInlTypeFinance.getBondMoney() ? BigDecimal.ZERO : totalInlTypeFinance.getBondMoney());
            totalInlTypeFinance.setOpenMoney(totalInlTypeFinance.getMoney().subtract(totalInlTypeFinance.getBondMoney()));
            totalInlTypeFinance.setCalculationScale(true);
            // 设置计算占比
            moneyList.add(totalInlTypeFinance.getMoney());
            openMoneyList.add(totalInlTypeFinance.getOpenMoney());
            // 计算总合计
            total.setMoney(total.getMoney().add(totalInlTypeFinance.getMoney()));
            total.setBondMoney(total.getBondMoney().add(totalInlTypeFinance.getBondMoney()));
            total.setOpenMoney(total.getOpenMoney().add(totalInlTypeFinance.getOpenMoney()));
            list.add(totalInlTypeFinance);
        }
    }
    /**
     * 计算占比率
     *
     * @param list      数组
     * @param idx       索引
     * @param precision 精度
     * @return
     */
    public static double getPercentValue(List<BigDecimal> list, int idx, int precision) {
        if ((list.size() - 1) < idx) {
            return 0;
        }
        BigDecimal sum = BigDecimal.ZERO;
        //求和
        for (BigDecimal dto : list) {
            sum = sum.add(dto);
        }
        if (sum.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        //10的2次幂是100，用于计算精度。
        double digits = Math.pow(10, precision);
        //扩大比例100
        double[] votesPerQuota = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            double temp = list.get(i).doubleValue() / sum.doubleValue();
            double val = temp * digits * 100;
            votesPerQuota[i] = val;
        }
        //总数,扩大比例意味的总数要扩大
        double targetSeats = digits * 100;
        //再向下取值，组成数组
        double[] seats = new double[list.size()];
        for (int i = 0; i < votesPerQuota.length; i++) {
            seats[i] = Math.floor(votesPerQuota[i]);
        }
        //再新计算合计，用于判断与总数量是否相同,相同则占比会100%
        double currentSum = 0;
        for (int i = 0; i < seats.length; i++) {
            currentSum += seats[i];
        }
        //余数部分的数组:原先数组减去向下取值的数组,得到余数部分的数组
        double[] remainder = new double[list.size()];
        for (int i = 0; i < seats.length; i++) {
            remainder[i] = votesPerQuota[i] - seats[i];
        }
        while (currentSum < targetSeats) {
            double max = 0;
            int maxId = 0;
            int len = 0;
            for (int i = 0; i < remainder.length; ++i) {
                if (remainder[i] > max) {
                    max = remainder[i];
                    maxId = i;
                }
            }
            //对最大项余额加1
            ++seats[maxId];
            //已经增加最大余数加1,则下次判断就可以不需要再判断这个余额数。
            remainder[maxId] = 0;
            //总的也要加1,为了判断是否总数是否相同,跳出循环。
            ++currentSum;
        }
        // 这时候的seats就会总数占比会100%
        return seats[idx] / digits;
    }
    public void export(HttpServletRequest request, HttpServletResponse response, Date queryDate, String curId, ICurrentUser user) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + ExportUtils.getFileName(request, "各融资品种融资余额表") + ".xls");
        try (OutputStream os = response.getOutputStream()) {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet("各融资品种融资余额");
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
            setExportTitle(sheet, "各融资品种融资余额", centerNoBorderCellStyle);
            // 表头
            setExportHead(sheet, leftNoBorderCellStyle, centerNoBorderCellStyle, queryDate, curId);
            // 表格列标题
            setExportTableColumnHead(sheet, centerCellStyle);
            // 表格内容
            setExportTableContent(sheet, centerCellStyle, rightCellStyle, queryDate, curId, user);
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
        sheet.setColumnWidth(0, BigDecimal.valueOf((8 + 0.72) * 256).intValue());
        sheet.setColumnWidth(1, BigDecimal.valueOf((13 + 0.72) * 256).intValue());
        sheet.setColumnWidth(2, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(3, BigDecimal.valueOf((8 + 0.72) * 256).intValue());
        sheet.setColumnWidth(4, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(5, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
        sheet.setColumnWidth(6, BigDecimal.valueOf((8 + 0.72) * 256).intValue());
        // 主标题
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(centerCellStyle);
        titleCell.setCellValue(title);
        for (int i = 1; i <= 6; i++) {
            titleRow.createCell(i);
        }
        //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
        //合并列
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
        ExportUtils.setRegionStyle(sheet, region, centerCellStyle);
        sheet.addMergedRegion(region);
    }
    /**
     * 设置表头
     *
     * @param sheet
     * @param centerCellStyle
     */
    public void setExportHead(HSSFSheet sheet, HSSFCellStyle leftCellStyle, HSSFCellStyle centerCellStyle, Date queryDate, String curId) {
        HSSFRow row = sheet.createRow(1);
        HSSFCell subjectCell = row.createCell(0);
        subjectCell.setCellStyle(leftCellStyle);
        subjectCell.setCellValue("日期：" + DateUtils.formatDate(queryDate));
        row.createCell(1);
        //合并列
        CellRangeAddress subjectRegion = new CellRangeAddress(1, 1, 0, 1);
        ExportUtils.setRegionStyle(sheet, subjectRegion, centerCellStyle);
        sheet.addMergedRegion(subjectRegion);
        row.createCell(2);
        HSSFCell dateCell = row.createCell(3);
        row.createCell(4);
        dateCell.setCellStyle(centerCellStyle);
        dateCell.setCellValue("币种：" + currencyService.getObjectById(curId).getCurName());
        //合并列
        CellRangeAddress dateRegion = new CellRangeAddress(1, 1, 3, 4);
        ExportUtils.setRegionStyle(sheet, dateRegion, centerCellStyle);
        sheet.addMergedRegion(dateRegion);
        HSSFCell curNameCell = row.createCell(6);
        curNameCell.setCellStyle(centerCellStyle);
        curNameCell.setCellValue("单位：万元");
    }
    /**
     * 设置表格列标题
     *
     * @param sheet
     * @param centerCellStyle
     */
    public void setExportTableColumnHead(HSSFSheet sheet, HSSFCellStyle centerCellStyle) {
        List<String> headList = new ArrayList<>();
        headList.add("序号");
        headList.add("融资品种");
        headList.add("融资/票面金额");
        headList.add("占总额的比例");
        headList.add("保证金");
        headList.add("风险敞口余额");
        headList.add("占敞口的比例");
        HSSFRow headRow = sheet.createRow(2);
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
     * @param queryDate
     * @param curId
     */
    public void setExportTableContent(HSSFSheet sheet, HSSFCellStyle centerCellStyle, HSSFCellStyle rightCellStyle,
                                      Date queryDate, String curId, ICurrentUser user) {
        int rowNum = 3;
        List<FinancingBalanceVO> detailList = search(queryDate, curId, user);
        int index = 1;
        for (FinancingBalanceVO vo : detailList) {
            HSSFRow row = sheet.createRow(rowNum);
            // 序号
            HSSFCell cellNumber = row.createCell(0);
            cellNumber.setCellStyle(centerCellStyle);
            // 融资品种
            HSSFCell cellTypeName = row.createCell(1);
            cellTypeName.setCellStyle(centerCellStyle);
            // 融资/票面金额
            HSSFCell cellMoney = row.createCell(2);
            cellMoney.setCellStyle(rightCellStyle);
            // 占总额的比例
            HSSFCell cellScale = row.createCell(3);
            cellScale.setCellStyle(rightCellStyle);
            // 保证金
            HSSFCell cellBondMoney = row.createCell(4);
            cellBondMoney.setCellStyle(rightCellStyle);
            // 风险敞口余额
            HSSFCell cellOpenMoney = row.createCell(5);
            cellOpenMoney.setCellStyle(rightCellStyle);
            // 占敞口的比例
            HSSFCell cellOpenMoneyScale = row.createCell(6);
            cellOpenMoneyScale.setCellStyle(rightCellStyle);
            if (StringUtils.hasText(vo.getTypeCode())) {
                cellNumber.setCellValue(index);
                index++;
            }
            cellTypeName.setCellValue(vo.getTypeName());
            cellMoney.setCellValue(NumberUtil.formatNumeric(vo.getMoney()));
            if ("合计".equals(vo.getTypeName())) {
                cellBondMoney.setCellValue(NumberUtil.formatNumeric(vo.getBondMoney()));
                cellOpenMoney.setCellValue(NumberUtil.formatNumeric(vo.getOpenMoney()));
            } else {
                cellScale.setCellValue(NumberUtil.formatNumeric(vo.getScale()) + "%");
                cellBondMoney.setCellValue(NumberUtil.formatNumeric(vo.getBondMoney()));
                cellOpenMoney.setCellValue(NumberUtil.formatNumeric(vo.getOpenMoney()));
                cellOpenMoneyScale.setCellValue(NumberUtil.formatNumeric(vo.getOpenMoneyScale()) + "%");
            }
            rowNum++;
        }
    }
}
