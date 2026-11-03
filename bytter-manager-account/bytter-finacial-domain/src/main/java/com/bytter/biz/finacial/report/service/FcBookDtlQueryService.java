package com.bytter.biz.finacial.report.service;
import com.bytter.biz.finacial.accbook.vo.VFcAccbookAndBlnedsVO;
import com.bytter.biz.finacial.report.dao.FcBookDtlQueryDao;
import com.bytter.biz.finacial.report.vo.FcBookDtlListVO;
import com.bytter.check.api.IFcCompareCenterService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
@Service("fcBookDtlQueryService")
@Transactional
public class FcBookDtlQueryService extends BaseService<VFcAccbookAndBlnedsVO, String> {
    @Autowired
    private FcBookDtlQueryDao fcBookDtlQueryDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    /*@Autowired
    private FcCompareCenterDao fcCompareAccDao;*/
    @DubboReference
    private IFcCompareCenterService iFcCompareCenterService;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    @Override
    public BaseDao<VFcAccbookAndBlnedsVO, String> getEntityDao() {
        return fcBookDtlQueryDao;
    }
    /**
     * 功能介绍 : 明细账。<br>
     * <b>日期：</b>2018年1月11日<br>
     *
     * @version 1.0
     * @author yangl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryAccbookDtls(DataTablesInput input, String resource, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        //查询账户信息
        String gedTallyDate = (String) input.getFormdata().get("GEd_tallyDate");
        String ledTallyDate = (String) input.getFormdata().get("LEd_tallyDate");
        DataTablesInput headInput = new DataTablesInput();
        BeanUtils.copyNotNullProperties(input, headInput);
        headInput.setStart(input.getStart() - 1);
        DataTablesOutput headData = fcBookDtlQueryDao.queryAccbookDtlHeads(headInput, corpAuth);
        List<FcBookDtlListVO> headVo = headData.getData();
        input.getFormdata().put("GEd_tallyDate", gedTallyDate);
        input.getFormdata().put("LEd_tallyDate", ledTallyDate);
        //账户数据为空则直接返回，不为空则查询明细
        if (EmptyUtils.isEmptyList(headVo)) {
            return headData;
        }
        //根据分页获取账户
        String account = headVo.get(0).getAccount();
        Date tallyDate;
        if (null == input.getFormdata().get("GEd_tallyDate")) {
            //查询账户信息
            FcAccDTO accDTO = fcAccService.getAccDTOByAccount(account);
            //账户启用日期
            tallyDate = accDTO.getAccStartDate();
        } else {
            tallyDate = DateUtils.parse(String.valueOf(input.getFormdata().get("GEd_tallyDate")), DateUtils.PATTERN_CLASSICAL_SIMPLE);
        }
        //账户期初余额
        BigDecimal balance = BigDecimal.ZERO;
        //查询账户余额
        List<BigDecimal> list = iFcCompareCenterService.queryAccBalanceByDate(account, tallyDate);
        if (!EmptyUtils.isEmptyList(list)) {
            balance = list.get(0);
        }
        //将帐号添加到查询条件中
        input.addFilter(MatchTypeEnum.EQ, "account", account);
        //查询明细数量
        Map<String, Object> dtlNum = fcBookDtlQueryDao.queryAccbookDtlTatol(input);
        DataTablesInput dtlInput = new DataTablesInput();
        BeanUtils.copyNotNullProperties(input, dtlInput);
        dtlInput.setLength(Integer.valueOf(String.valueOf(dtlNum.get("TATOLNUM"))));
        dtlInput.setStart(0);
        dtlInput.addFilter(MatchTypeEnum.EQ, "account", account);
        //查询账号记账明细
        DataTablesOutput accDtlOut = fcBookDtlQueryDao.queryAccbookDtlDatas(dtlInput);
        List<FcBookDtlListVO> accVo = accDtlOut.getData();
        if (EmptyUtils.isNotEmpty(accVo)) {
            // 期初余额
            accVo.get(0).setBeginningBalance(balance);
            accVo.get(0).setAccCount((int) headData.getRecordsTotal());
            // 网点名称
            accVo.get(0).setNetName(headVo.get(0).getNetName());
        }
        return accDtlOut;
    }
    /**
     * 导出
     *
     * @param input
     * @param resource
     * @param userId
     * @return
     */
    public HSSFWorkbook createExcelContent(DataTablesInput input, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcBookDtlListVO> headVo = fcBookDtlQueryDao.excelAccbookDtlHeads(input, corpAuth);//表头信息
        List<FcBookDtlListVO> res = setVoInfo(input, headVo);//结果
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        String content = "流水账查询表";
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(content);
        // 创建Excel表中的内容
        this.createExcelBody(workbook, sheet, res);
        return workbook;
    }
    /**
     * 创建Excel内容样式
     *
     * @param workbook
     * @return
     */
    private HSSFCellStyle createExcelBodyStyle(HSSFWorkbook workbook) {
        HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
        HSSFFont columnHeadFont = workbook.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 10);
        columnHeadStyle.setFont(columnHeadFont);
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);
        columnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
        columnHeadStyle.setTopBorderColor(HSSFColor.BLACK.index);//上边框
        columnHeadStyle.setBorderTop(BorderStyle.THIN);
        columnHeadStyle.setBorderLeft(BorderStyle.THIN);// 边框的大小
        columnHeadStyle.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色
        columnHeadStyle.setBorderRight(BorderStyle.THIN);// 边框的大小
        columnHeadStyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
        columnHeadStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色
        // 设置单元格的背景颜色（单元格的样式会覆盖列或行的样式）
        columnHeadStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        return columnHeadStyle;
    }
    /**
     * 创建Excel表内容
     */
    private void createExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<FcBookDtlListVO> list) {
        // Excel表格内容样式
        HSSFCellStyle cs = this.createExcelBodyStyle(workbook);
        String account = "";
        // 指向所在行
        int index = 0;
        // 借方发生额
        BigDecimal sumDebitMoney = null;
        // 贷方发生额
        BigDecimal sumCreatMoney = null;
        for (FcBookDtlListVO res : list) {
            if (StringUtils.isEmpty(account)) {
                account = res.getAccount();
                sumDebitMoney = new BigDecimal("0");
                sumCreatMoney = new BigDecimal("0");
            }
            /*********************创建表头**********************/
            if (index == 0 || !account.equals(res.getAccount())) {
                account = res.getAccount();
                if (index != 0) {
                    // 增加上一个账户的合计行
                    HSSFRow sumRow = sheet.createRow(index);
                    sumRow.createCell(0).setCellValue("合计");
                    sumRow.getCell(0).setCellStyle(cs);
                    sumRow.createCell(1).setCellStyle(cs);
                    sumRow.createCell(2).setCellStyle(cs);
                    sumRow.createCell(3).setCellStyle(cs);
                    if (sumDebitMoney != null) {
                        sumRow.createCell(4).setCellValue(String.format("%.2f", sumDebitMoney));
                        sumRow.getCell(4).setCellStyle(cs);
                    }
                    if (sumCreatMoney != null) {
                        sumRow.createCell(5).setCellValue(String.format("%.2f", sumCreatMoney));
                        sumRow.getCell(5).setCellStyle(cs);
                    }
                    sumRow.createCell(6).setCellStyle(cs);
                    sumRow.createCell(7).setCellStyle(cs);
                    sumDebitMoney = new BigDecimal("0");
                    sumCreatMoney = new BigDecimal("0");
                    index = index + 1;
                    //多个表添加空行,分隔
                    sheet.createRow(index);
                    index = index + 1;
                }
                // 添加网点标题
                HSSFRow titleRow = sheet.createRow(index);
                titleRow.createCell(3).setCellValue(res.getNetName());
                index = index + 1;
                // 第一行
                sheet.createRow(index);
                HSSFRow row1 = sheet.getRow(index);
                row1.createCell(0).setCellValue("账号:" + res.getAccount());
                row1.createCell(3).setCellValue("账户名称:" + res.getAccName());
                row1.createCell(6).setCellValue("币别:" + res.getCurName());
                sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
                sheet.addMergedRegion(new CellRangeAddress(index, index, 3, 5));
                sheet.addMergedRegion(new CellRangeAddress(index, index, 6, 7));
                index = index + 1;
                // 第二行
                sheet.createRow(index);
                HSSFRow row2 = sheet.getRow(index);
                row2.createCell(0).setCellValue("科目:" + res.getSubjectName());
                row2.createCell(6).setCellValue("期初余额:" + String.format("%.2f", res.getBeginningBalance()));
                sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
                sheet.addMergedRegion(new CellRangeAddress(index, index, 3, 5));
                sheet.addMergedRegion(new CellRangeAddress(index, index, 6, 7));
                index = index + 1;
                //第三行
                sheet.createRow(index);
                HSSFRow row3 = sheet.getRow(index);
                HSSFCell r = row3.createCell(0);
                r.setCellValue("记账日期");
                r.setCellStyle(cs);
                HSSFCell r1 = row3.createCell(1);
                r1.setCellValue("凭证号");
                r1.setCellStyle(cs);
                HSSFCell r2 = row3.createCell(2);
                r2.setCellValue("凭证种类");
                r2.setCellStyle(cs);
                HSSFCell r3 = row3.createCell(3);
                r3.setCellValue("摘要");
                r3.setCellStyle(cs);
                HSSFCell r4 = row3.createCell(4);
                r4.setCellValue("借方发生额");
                r4.setCellStyle(cs);
                HSSFCell r5 = row3.createCell(5);
                r5.setCellValue("贷方发生额");
                r5.setCellStyle(cs);
                HSSFCell r6 = row3.createCell(6);
                r6.setCellValue("期末");
                r6.setCellStyle(cs);
                row3.createCell(7).setCellStyle(cs);
                // 设置跨行跨列格式：CellRangeAddress(A, B,C,D) A：开始行，B：结束行，C：开始列，D：结束列
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 0, 0));
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 2, 2));
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 3, 3));
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 4, 4));
                sheet.addMergedRegion(new CellRangeAddress(index, index + 1, 5, 5));
                sheet.addMergedRegion(new CellRangeAddress(index, index, 6, 7));
                index = index + 1;
                //第四行
                sheet.createRow(index);
                HSSFRow row4 = sheet.getRow(index);
                row4.createCell(0).setCellStyle(cs);
                row4.createCell(1).setCellStyle(cs);
                row4.createCell(2).setCellStyle(cs);
                row4.createCell(3).setCellStyle(cs);
                row4.createCell(4).setCellStyle(cs);
                row4.createCell(5).setCellStyle(cs);
                HSSFCell r7 = row4.createCell(6);
                r7.setCellValue("借贷方向");
                r7.setCellStyle(cs);
                HSSFCell r8 = row4.createCell(7);
                r8.setCellValue("余额");
                r8.setCellStyle(cs);
                index = index + 1;
                /*********************表头结束**********************/
            }
            sheet.createRow(index);
            HSSFRow row5 = sheet.getRow(index);
            HSSFCell r = row5.createCell(0);
            r.setCellValue(DateUtils.formatDate(res.getTallyDate()));
            r.setCellStyle(cs);
            HSSFCell r1 = row5.createCell(1);
            r1.setCellValue(res.getVoucherNo());
            r1.setCellStyle(cs);
            HSSFCell r2 = row5.createCell(2);
            r2.setCellValue(res.getVoucherName());
            r2.setCellStyle(cs);
            HSSFCell r3 = row5.createCell(3);
            r3.setCellValue(res.getSummaryContent());
            r3.setCellStyle(cs);
            HSSFCell r4 = row5.createCell(4);
            r4.setCellValue(String.format("%.2f", res.getDebitMoney()));
            sumDebitMoney = sumDebitMoney.add(res.getDebitMoney());
            r4.setCellStyle(cs);
            HSSFCell r5 = row5.createCell(5);
            r5.setCellValue(String.format("%.2f", res.getCreditMoney()));
            sumCreatMoney = sumCreatMoney.add(res.getCreditMoney());
            r5.setCellStyle(cs);
            HSSFCell r6 = row5.createCell(6);
            r6.setCellValue(res.getBalanceWay());
            r6.setCellStyle(cs);
            HSSFCell r7 = row5.createCell(7);
            r7.setCellValue(String.format("%.2f", res.getBalance()));
            r7.setCellStyle(cs);
            index++;
        }
        // 增加上一个账户的合计行
        // 增加上一个账户的合计行
        HSSFRow sumRow = sheet.createRow(index);
        sumRow.createCell(0).setCellValue("合计");
        sumRow.getCell(0).setCellStyle(cs);
        sumRow.createCell(1).setCellStyle(cs);
        sumRow.createCell(2).setCellStyle(cs);
        sumRow.createCell(3).setCellStyle(cs);
        if (sumDebitMoney != null) {
            sumRow.createCell(4).setCellValue(String.format("%.2f", sumDebitMoney));
            sumRow.getCell(4).setCellStyle(cs);
        }
        if (sumCreatMoney != null) {
            sumRow.createCell(5).setCellValue(String.format("%.2f", sumCreatMoney));
            sumRow.getCell(5).setCellStyle(cs);
        }
        sumRow.createCell(6).setCellStyle(cs);
        sumRow.createCell(7).setCellStyle(cs);
        index++;
    }
    /**
     * 判断是否为ie浏览器
     *
     * @param request
     * @return
     */
    public boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
    public List<FcBookDtlListVO> setVoInfo(DataTablesInput input, List<FcBookDtlListVO> headList) {
        List<String> accounts = new ArrayList<>();
        for (FcBookDtlListVO h : headList) {
            accounts.add(h.getAccount());
        }
        //查询账号记账明细
        List<FcBookDtlListVO> accDtls = fcBookDtlQueryDao.listExcel(input, accounts);
        Map<String,BigDecimal> accountBalance = new HashMap<>();
        for(String account : accounts){
            Date tallyDate;
            if (null == input.getFormdata().get("GEd_tallyDate")) {
                //查询账户信息
                FcAccDTO accDTO = fcAccService.getAccDTOByAccount(account);
                //账户启用日期
                tallyDate = accDTO.getAccStartDate();
            } else {
                tallyDate = DateUtils.parse(String.valueOf(input.getFormdata().get("GEd_tallyDate")), DateUtils.PATTERN_CLASSICAL_SIMPLE);
            }
            //账户期初余额
            BigDecimal balance = BigDecimal.ZERO;
            //查询账户余额
            List<BigDecimal> list = iFcCompareCenterService.queryAccBalanceByDate(account, tallyDate);
            if (!EmptyUtils.isEmptyList(list)) {
                balance = list.get(0);
            }
            accountBalance.put(account,balance);
        }
        String account = "";
        for (FcBookDtlListVO d : accDtls) {
            d.setBeginningBalance(accountBalance.get(d.getAccount()));
        }
        return accDtls;
    }
    /**
     * 获取批量打印数据
     * @param input
     * @param resource
     * @param userId
     * @return
     */
    public List<FcBookDtlListVO> batchPrint(DataTablesInput input, String resource, String userId){
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        List<FcBookDtlListVO> headVo = fcBookDtlQueryDao.excelAccbookDtlHeads(input, corpAuth);//表头信息
        List<FcBookDtlListVO> res = setVoInfo(input, headVo);//结果
        return res;
    }
}
