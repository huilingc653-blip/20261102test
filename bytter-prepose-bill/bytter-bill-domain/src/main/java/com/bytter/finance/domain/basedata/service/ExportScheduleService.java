package com.bytter.finance.domain.basedata.service;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.vo.RepayPlanVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
@Service("exportScheduleService")
@Transactional
public class ExportScheduleService implements IUserExcelVerifyHandler<RepaymentScheduleVO> {
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    /**
     * 导出预算固定模式模板
     *
     * @param request
     * @param response
     */
    public void exportBudget(HttpServletRequest request, HttpServletResponse response, RepayPlanVO repayPlanVO) {
        String fileName = "还款计划导出";
        String codedFileName = fileName;
        try {
            if (ServletUtils.isIE(request)) {
                codedFileName = URLEncoder.encode(codedFileName, "UTF8");
            } else {
                codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(fileName);
            // 锁定表格
            // sheet.protectSheet("edit");
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 组装标题头
            String[] head = new String[] { "序号", "预计还款日", "还款本金", "还款利息", "本期还款金额", "剩余本金", "是否已还", "备注" };
            this.setHeadCell(workbook, sheet, head, 0);
            HSSFCellStyle rightCellStyle = initStyle(workbook, HorizontalAlignment.RIGHT, true);
            HSSFCellStyle leftUnCellStyle = initStyle(workbook, HorizontalAlignment.LEFT, true);
            HSSFCellStyle centerUnCellStyle = initStyle(workbook, HorizontalAlignment.CENTER, true);
            // 组装数据
            List<RepaymentScheduleVO> dtlList = new ArrayList<RepaymentScheduleVO>();
            if (!EmptyUtils.isEmpty(repayPlanVO.getId())) {
                dtlList = this.listUpdateRepayPlan(repayPlanVO.getId());
            } else {
                dtlList = this.listRepayMentSchedule(repayPlanVO);
            }
            if (!EmptyUtils.isEmptyList(dtlList)) {
                int index = 1;
                int num = 1;
                for (RepaymentScheduleVO vo : dtlList) {
                    HSSFRow row = sheet.createRow(index);
                    // 序号
                    HSSFCell cell = row.createCell(0);
                    cell.setCellStyle(centerUnCellStyle);
                    cell.setCellValue(num);
                    // 预计还款日
                    HSSFCell cell1 = row.createCell(1);
                    cell1.setCellStyle(centerUnCellStyle);
                    cell1.setCellValue(DateUtils.formatDate(vo.getPrediceRepayDate()));
                    // 还款本金
                    HSSFCell cell2 = row.createCell(2);
                    cell2.setCellStyle(rightCellStyle);
                    cell2.setCellValue(NumberUtil.formatNumeric(vo.getRepaymentMoney()));
                    // 还款利息
                    HSSFCell cell3 = row.createCell(3);
                    cell3.setCellStyle(rightCellStyle);
                    BigDecimal bMoney = EmptyUtils.isEmpty(vo.getRepaymentIst()) ? BigDecimal.ZERO
                            : vo.getRepaymentIst();
                    cell3.setCellValue(NumberUtil.formatNumeric(bMoney));
                    // 本期还款金额
                    HSSFCell cell4 = row.createCell(4);
                    cell4.setCellStyle(rightCellStyle);
                    BigDecimal bsuperMoney = EmptyUtils.isEmpty(vo.getCurrentRepayMoney()) ? BigDecimal.ZERO
                            : vo.getCurrentRepayMoney();
                    cell4.setCellValue(NumberUtil.formatNumeric(bsuperMoney));
                    // 剩余本金
                    HSSFCell cell5 = row.createCell(5);
                    cell5.setCellStyle(rightCellStyle);
                    BigDecimal bMoneySum = EmptyUtils.isEmpty(vo.getSurplusMoney()) ? BigDecimal.ZERO
                            : vo.getSurplusMoney();
                    cell5.setCellValue(NumberUtil.formatNumeric(bMoneySum));
                    // 是否已还
                    HSSFCell cell6 = row.createCell(6);
                    cell6.setCellStyle(centerUnCellStyle);
                    if (!EmptyUtils.isEmpty(repayPlanVO.getId())) {
                        cell6.setCellValue(vo.getIsRepay().equals("0") ? "否" : "是");
                    } else {
                        cell6.setCellValue("否");
                    }
                    // 备注
                    HSSFCell cell7 = row.createCell(7);
                    cell7.setCellStyle(centerUnCellStyle);
                    cell7.setCellValue(vo.getRemark());
                    index++;
                    num++;
                }
            }
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 写入标题头
     *
     * @param sheet
     * @param head
     * @throws Exception
     */
    public void setHeadCell(HSSFWorkbook workbook, HSSFSheet sheet, String[] head, int rowTitle) throws Exception {
        HSSFRow row = sheet.createRow(rowTitle);
        for (int i = 0; i < head.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(getCenterCellStyle(workbook, true));
            cell.setCellValue(head[i]);
        }
    }
    /**
     * 获取居中样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.CENTER, locked);
    }
    /**
     * 获取一个初始的样式
     */
    public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align, Boolean locked) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 边框设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体居中
        style.setAlignment(align);
        style.setLocked(EmptyUtils.isEmpty(locked) || locked ? true : locked);
        return style;
    }
    /**
     * 功能介绍：根据id号获取还款计划<br>
     *
     * @param
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> listUpdateRepayPlan(String id) {
        List<RepaymentScheduleVO> rsList = repaymentScheduleService.getRepayMentScheduleByBillCode(id);
        if (ListUtils.isNullOrEmpty(rsList)) {
            rsList = new ArrayList<RepaymentScheduleVO>();
            RepaymentScheduleVO rs = new RepaymentScheduleVO();
            rs.setIsRepay(CmsConstant.IS_FALSE);
            rsList.add(rs);
        }
        return rsList;
    }
    /**
     * 查询还款计划数据
     * 
     * @param repayPlanVo
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> listRepayMentSchedule(RepayPlanVO repayPlanVo) {
        // planId非空：则回显还款计划数据
        if (!StringUtils.isEmpty(repayPlanVo.getPlanId())) {
            return listRepayMentScheduleById(repayPlanVo.getPlanId());
        }
        List<RepaymentScheduleVO> rsList = repaymentScheduleDao
                .getRepayMentScheduleByBillCode(repayPlanVo.getBillCode());
        List<RepaymentScheduleVO> list = new ArrayList<RepaymentScheduleVO>();
        BigDecimal sumRepaymentIst = BigDecimal.ZERO;
        for (RepaymentScheduleVO vo : rsList) {
            if (StringUtils.isNotEmpty(vo.getRepaymentIst())) {
                sumRepaymentIst = sumRepaymentIst.add(vo.getRepaymentIst());
            }
        }
        for (int i = 0, length = rsList.size(); i < length; i++) {
            RepaymentScheduleVO vo = rsList.get(i);
            vo.setSumRepaymentIst(sumRepaymentIst);
            if (i == rsList.size() - 1) {
                vo.setLastSign(true);
            }
            list.add(vo);
        }
        return list;
    }
    /**
     * 根据id获取还款计划子表信息
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<RepaymentScheduleVO> listRepayMentScheduleById(String id) {
        return repaymentScheduleDao.listRepayMentScheduleById(id);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(RepaymentScheduleVO vo, Object... objects) {
        List<String> msgList = new ArrayList<>();
        if (EmptyUtils.isEmptyList(msgList)) {
            return new ExcelVerifyHanlderResult(true, msgList);
        } else {
            return new ExcelVerifyHanlderResult(false, msgList);
        }
    }
    @Override
    public void verifyHandler(List<RepaymentScheduleVO> list, Object... objects) {
    }
}
