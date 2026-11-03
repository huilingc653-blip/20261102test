package com.bytter.biz.report.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.report.service.AccountManagementStatisticsService;
import com.bytter.biz.report.vo.AccountManagementStatisticsVO;
import com.bytter.biz.report.vo.PaymentStatisticsVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
 * @Description: 账户管理统计表
 * @date 2024-08-14
 */
@Controller
@RequestMapping("/report/accountManagementStatistics")
public class AccountManagementStatisticsController extends BaseController {
	@Autowired
	private AccountManagementStatisticsService accountManagementStatisticsService;
	@DubboReference
	private IDictService dictService;
	@DubboReference
	private IUserService userService;

	@RequestMapping("/list")
	public String list(AccountManagementStatisticsVO vo, Model model) {
		List<AccountManagementStatisticsVO> accountManagementStatisticsVOList = accountManagementStatisticsService.getList(vo, getCurrentUser());

		model.addAttribute("corpId", vo.getCorpId());
		model.addAttribute("bankTypeId", vo.getBankTypeId());
		model.addAttribute("bankAcc", vo.getBankAcc());
		model.addAttribute("status", vo.getStatus());
		model.addAttribute("bindingBankAcc", vo.getBindingBankAcc());
		model.addAttribute("isBindThreeAcc", vo.getIsBindThreeAcc());
		model.addAttribute("list", accountManagementStatisticsVOList);
		return "report/listAccountManagementStatistics";
	}

	@LogMessage(content = "导出账户管理统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@RequestMapping("/excel")
	public String excel(AccountManagementStatisticsVO vo, HttpServletResponse response) {
		List<AccountManagementStatisticsVO> list = accountManagementStatisticsService.getList(vo, getCurrentUser());
		String filename = "账户管理统计表.xls";
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet(filename);
		//创建表格样式
		creatExcel(wb, sheet);
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
	public void creatExcel(HSSFWorkbook wb, HSSFSheet sheet) {
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
		String[] heads = new String[]{
				"区域",
				"省区",
				"单位名称",
				"股权属性",
				"账户组",
				"户名",
				"银行名称",
				"开户行名称",
				"账号",
				"省",
				"市",
				"是否已上银企",
				"账号用途",
				"账号状态",
				"关联第三方平台",
				"第三方账户组",
				"第三方平台账号",
				"实时余额",
				"绑定人名称",
				"绑定人手机",
				"登录密码",
				"支付密码",
		};
		row.setHeightInPoints(20);
		for (int i = 0; i < heads.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			cell.setCellValue(heads[i]);
			cell.setCellStyle(style);
		}
		sheet.setColumnWidth(0, 30 * 256);
		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 40 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(6, 50 * 256);
		sheet.setColumnWidth(7, 30 * 256);
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
	private HSSFWorkbook addDataToSheet(List<AccountManagementStatisticsVO> list, HSSFSheet sheet, HSSFWorkbook wb) {
		//填充明细数据
		DecimalFormat df = new DecimalFormat("#,##0.00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
		HSSFCellStyle rightCellStyle = getRightCellStyle(wb);
		HSSFCellStyle centerColorCellStyle = getCenterColorCellStyle(wb);

		for(int i = 0; i < list.size(); i++) {
			AccountManagementStatisticsVO accountManagementStatisticsVO = list.get(i);
			HSSFRow row = sheet.createRow(i+1);
			HSSFCell cell = row.createCell(0);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue(accountManagementStatisticsVO.getAreaName());

			HSSFCell cell1 = row.createCell(1);
			cell1.setCellStyle(centerCellStyle);
			cell1.setCellValue(accountManagementStatisticsVO.getSysProvinceName());

			HSSFCell cell2 = row.createCell(2);
			cell2.setCellStyle(centerCellStyle);
			cell2.setCellValue(accountManagementStatisticsVO.getCorpName());

			HSSFCell cell3 = row.createCell(3);
			cell3.setCellStyle(centerCellStyle);
			cell3.setCellValue(accountManagementStatisticsVO.getEquityMaintenanceName());

			HSSFCell cell4 = row.createCell(4);
			cell4.setCellStyle(centerCellStyle);
			cell4.setCellValue(accountManagementStatisticsVO.getFcAccGroupName());

			HSSFCell cell5 = row.createCell(5);
			cell5.setCellStyle(centerCellStyle);
			cell5.setCellValue(accountManagementStatisticsVO.getAccName());

			HSSFCell cell6 = row.createCell(6);
			cell6.setCellStyle(centerCellStyle);
			cell6.setCellValue(accountManagementStatisticsVO.getBankTypeName());

			HSSFCell cell7 = row.createCell(7);
			cell7.setCellStyle(centerCellStyle);
			cell7.setCellValue(accountManagementStatisticsVO.getBankName());

			HSSFCell cell8 = row.createCell(8);
			cell8.setCellStyle(centerCellStyle);
			cell8.setCellValue(accountManagementStatisticsVO.getBankAcc());

			HSSFCell cell9 = row.createCell(9);
			cell9.setCellStyle(centerCellStyle);
			cell9.setCellValue(accountManagementStatisticsVO.getProvName());

			HSSFCell cell10 = row.createCell(10);
			cell10.setCellStyle(centerCellStyle);
			cell10.setCellValue(accountManagementStatisticsVO.getCityName());

			HSSFCell cell11 = row.createCell(11);
			cell11.setCellStyle(centerCellStyle);
			cell11.setCellValue(dictService.get("sf_yn", accountManagementStatisticsVO.getIsOnline()));

			HSSFCell cell12 = row.createCell(12);
			cell12.setCellStyle(centerCellStyle);
			cell12.setCellValue(accountManagementStatisticsVO.getAccountPurpose());

			HSSFCell cell13 = row.createCell(13);
			cell13.setCellStyle(centerCellStyle);
			Integer status = accountManagementStatisticsVO.getStatus();
			if(EmptyUtils.isEmpty(status)){
				status = -2;
			}
			if(status == -2){
				cell13.setCellValue("已销户");
			}else{
				cell13.setCellValue("正常");
			}

			HSSFCell cell14 = row.createCell(14);
			cell14.setCellStyle(centerCellStyle);
			cell14.setCellValue(accountManagementStatisticsVO.getBindingNatureName());
			// 14
			cell14 = row.createCell(15);
			cell14.setCellStyle(centerCellStyle);
			cell14.setCellValue(accountManagementStatisticsVO.getBindingAccGroupCode());

			HSSFCell cell15 = row.createCell(16);
			cell15.setCellStyle(centerCellStyle);
			cell15.setCellValue(accountManagementStatisticsVO.getBindingBankAcc());

			HSSFCell cell16 = row.createCell(17);
			cell16.setCellStyle(centerCellStyle);
			if(EmptyUtils.isNotEmpty(accountManagementStatisticsVO.getBindingBankAcc())){
				cell16.setCellValue(df.format(accountManagementStatisticsVO.getRealTimeBal()));
			}else{
				cell16.setCellValue("");
			}
			HSSFCell cell17 = row.createCell(18);
			cell17.setCellStyle(centerCellStyle);
			cell17.setCellValue(accountManagementStatisticsVO.getBindingByName());

			HSSFCell cell18 = row.createCell(19);
			cell18.setCellStyle(centerCellStyle);
			cell18.setCellValue(accountManagementStatisticsVO.getBindingByMobile());

			HSSFCell cell19 = row.createCell(20);
			cell19.setCellStyle(centerCellStyle);
			if (ObjectUtil.isNotEmpty(accountManagementStatisticsVO.getLoginPassword())){
				if (userService.hasPermissionV1(getUserId(),"netbank:bankAcc:btBankAcc:binder",this.getCurrentUser().getTenantId())){
					cell19.setCellValue(accountManagementStatisticsVO.getLoginPassword());
				}else{
					cell19.setCellValue("******");
				}
			}else{
				cell19.setCellValue("");
			}
			HSSFCell cell20 = row.createCell(21);
			cell20.setCellStyle(centerCellStyle);
			if (ObjectUtil.isNotEmpty(accountManagementStatisticsVO.getPayPassword())){
				if (userService.hasPermissionV1(getUserId(),"netbank:bankAcc:btBankAcc:binder",this.getCurrentUser().getTenantId())){
					cell20.setCellValue(accountManagementStatisticsVO.getPayPassword());
				}else{
					cell20.setCellValue("******");
				}
			}else{
				cell20.setCellValue("");
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
