package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.AccountManagementStatisticsService;
import com.bytter.biz.report.service.PaymentStatisticsService;
import com.bytter.biz.report.vo.AccountManagementStatisticsVO;
import com.bytter.biz.report.vo.PaymentStatisticsVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.guarantee.vo.FcBondApplyVO;
import com.bytter.guarantee.vo.FcBondUseDtlVO;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Jiangw
 * @version 1.0
 * @Title: Controller
 * @Description: 支付统计表
 * @date 2024-08-14
 */
@Controller
@RequestMapping("/report/paymentStatistics")
public class PaymentStatisticsController extends BaseController {
	@Autowired
	private PaymentStatisticsService paymentStatisticsService;

	@RequestMapping("/list")
	public String list(PaymentStatisticsVO vo, Model model) {
		List<PaymentStatisticsVO> paymentStatisticsVOS = paymentStatisticsService.getList(vo, getCurrentUser());
		List<String> titleList = paymentStatisticsService.getTitleList(vo.getBeginDate(), vo.getEndDate());
		model.addAttribute("corpId", vo.getCorpId());
		model.addAttribute("beginDate", vo.getBeginDate());
		model.addAttribute("endDate", vo.getEndDate());
		model.addAttribute("titleList", titleList);
		model.addAttribute("list", paymentStatisticsVOS);
		return "report/listPaymentStatistics";
	}

	@LogMessage(content = "导出支付统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@RequestMapping("/excel")
	public String excel(PaymentStatisticsVO vo, HttpServletResponse response) {
		List<PaymentStatisticsVO> list = paymentStatisticsService.getList(vo, getCurrentUser());
		List<String> titleList = paymentStatisticsService.getTitleList(vo.getBeginDate(), vo.getEndDate());
		String filename = "支付统计表.xls";
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet(filename);
		//创建表格样式
		creatExcel(wb, sheet, titleList);
		//填充数据
		wb = this.addDataToSheet(list, sheet, wb);
		try {
			//实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
			OutputStream out = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "iso-8859-1"));
			response.setContentType("application/x-download");
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建表格样式
	 */
	public void creatExcel(HSSFWorkbook wb, HSSFSheet sheet, List<String> titleList) {
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 18);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		//创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		//设置表头字体
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 14);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		style.setFont(headfont);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		this.setBorder(style);
		//给表头第一行一次创建单元格
		String[] heads = titleList.toArray(new String[titleList.size()]);
		row.setHeightInPoints(20);
		for (int i = 0; i < heads.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			cell.setCellValue(heads[i]);
			cell.setCellStyle(style);
			if(i<2){
				sheet.setColumnWidth(i, 35 * 256);
			}else{
				sheet.setColumnWidth(i, 25 * 256);
			}
		}
	}

	/**
	 * 设置表格所有边框
	 */
	public void setBorder(HSSFCellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	}

	/**
	 * 填充数据
	 */
	private HSSFWorkbook addDataToSheet(List<PaymentStatisticsVO> list, HSSFSheet sheet, HSSFWorkbook wb) {
		//填充明细数据
		DecimalFormat df = new DecimalFormat("#,##0.00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
		HSSFCellStyle rightCellStyle = getRightCellStyle(wb);
		HSSFCellStyle centerColorCellStyle = getCenterColorCellStyle(wb);

		for(int i = 0; i < list.size(); i++) {
			PaymentStatisticsVO paymentStatisticsVO = list.get(i);
			int isSum = paymentStatisticsVO.getIsSum();
			HSSFRow row = sheet.createRow(i+1);
            int columnSize=0;
			HSSFCell cell1 = row.createCell(columnSize++);
			if(isSum == 1){
				cell1.setCellStyle(centerColorCellStyle);
			}else{
				cell1.setCellStyle(centerCellStyle);
			}
			cell1.setCellValue(paymentStatisticsVO.getAreaName());
			HSSFCell cell4 = row.createCell(columnSize++);
			if(isSum == 1){
				cell4.setCellStyle(centerColorCellStyle);
			}else{
				cell4.setCellStyle(centerCellStyle);
			}
			cell4.setCellValue(paymentStatisticsVO.getProvinceName());
			HSSFCell cell2 = row.createCell(columnSize++);
			if(isSum == 1){
				cell2.setCellStyle(centerColorCellStyle);
			}else{
				cell2.setCellStyle(centerCellStyle);
			}
			cell2.setCellValue(paymentStatisticsVO.getCorpName());
			List<BigDecimal> dataList = paymentStatisticsVO.getDataList();
			for(int j = 0; j < dataList.size(); j++) {
				HSSFCell cell3 = row.createCell(j+columnSize);
				if(isSum == 1){
					cell3.setCellStyle(centerColorCellStyle);
				}else{
					cell3.setCellStyle(centerCellStyle);
				}
				cell3.setCellValue(dataList.get(j).intValue());
			}
		}

		return wb;
	}

	/**
	 * 获取居中样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook) {
		return initStyle(workbook, HorizontalAlignment.CENTER);
	}

	/**
	 * 获取居中样式+背景色
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getCenterColorCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 边框设置
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 字体居中
		style.setAlignment(HorizontalAlignment.CENTER);
		//设置背景色
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return style;
	}

	/**
	 * 获取靠右样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook) {
		return initStyle(workbook, HorizontalAlignment.RIGHT);
	}

	/**
	 * 获取一个初始的样式
	 */
	public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 边框设置
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 字体居中
		style.setAlignment(align);
		return style;
	}
}