package com.bytter.biz.report.service;
import com.bytter.biz.budget.service.*;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.biz.budget.vo.FbsBudgetResolveMonthVO;
import com.bytter.biz.budget.vo.FbsBudgetResolveYearVO;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.ServletUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_DTL_ABS
 * @date 2017-07-18 11:25:46
 */
@Service("fbsBudgetDownloadReportService")
@Transactional
public class FbsBudgetDownloadReportService {
	@Autowired
	private FbsBudgetService fbsBudgetService;
	@Autowired
	private FbsBudgetDtlAbsService fbsBudgetDtlAbsService;
	@Autowired
	private FbsItemService fbsItemService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IConfigService iConfigService;
	@Autowired
	private FbsBudgetResolveYearService fbsBudgetResolveYearService;
	@Autowired
	private FbsBudgetResolveMonthService fbsBudgetResolveMonthService;
	@Autowired
	private FbsBudgetPeriodService fbsBudgetPeriodService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	 * 获取一个初始的样式
	 */
	public XSSFCellStyle initStyle(XSSFWorkbook workbook, HorizontalAlignment align) {
		XSSFCellStyle style = workbook.createCellStyle();
		// 边框设置
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 字体居中
		style.setAlignment(align);
		style.setLocked(false);
		return style;
	}
	/**
	 * 获取居中样式
	 *
	 * @throws Exception
	 */
	public XSSFCellStyle getCenterCellStyle(XSSFWorkbook workbook) {
		return initStyle(workbook, HorizontalAlignment.CENTER);
	}
	/**
	 * 获取靠右样式
	 *
	 * @throws Exception
	 */
	public XSSFCellStyle getRightCellStyle(XSSFWorkbook workbook) {
		return initStyle(workbook, HorizontalAlignment.RIGHT);
	}
	/**
	 * 获取靠左样式
	 *
	 * @throws Exception
	 */
	public XSSFCellStyle getLeftCellStyle(XSSFWorkbook workbook) {
		return initStyle(workbook, HorizontalAlignment.LEFT);
	}
	/**
	 * 写入标题头
	 *
	 * @param sheet
	 * @param head
	 * @throws Exception
	 */
	public void setHeadCell(XSSFWorkbook workbook, XSSFSheet sheet, String[] head, int rowTitle) throws Exception {
		XSSFRow row = sheet.createRow(rowTitle);
		for (int i = 0; i < head.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(getCenterCellStyle(workbook));
			cell.setCellValue(head[i]);
		}
	}
	/**
	 * 获取计划周期类型
	 *
	 * @param timeType
	 * @return
	 */
	public String getTimeTypeName(String timeType) {
		return dictService.get("fbs.timeType", timeType);
	}
	/**
	 * 获取文件名
	 *
	 * @param request
	 * @param fileName
	 * @return
	 */
	public String getCodedFileName(HttpServletRequest request, String fileName) {
		String codedFileName = fileName;
		/*try {
			if (ServletUtils.isIE(request)) {
				codedFileName = URLEncoder.encode(codedFileName, "UTF8");
			} else {
				codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return codedFileName;
	}
	public File getFile(String filePath) throws Exception {
		File file = new File(filePath);
		File fileParent = file.getParentFile();
		// 推断文件夹或文件是否存在
		if (!fileParent.exists()) {
			// 不存在返回 false
			fileParent.mkdirs();
		} else {
			// 推断是否为文件
			if (fileParent.isFile()) {
				// 为文件时调用删除文件方法
				fileParent.delete();
			}
		}
		file.createNewFile();
		return file;
	}
	/**
	 * 组装计划基础信息
	 *
	 * @param workbook
	 * @param sheet
	 * @param timeType
	 * @param corpName
	 * @param beginDate
	 * @param endDate
	 * @throws Exception
	 */
	public void setBudgetInfo(XSSFWorkbook workbook, XSSFSheet sheet, String corpName, String timeType, Date beginDate, Date endDate,String curName) throws Exception {
		XSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook);
		XSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook);
		XSSFRow rowCorp = sheet.createRow(0);
		// 单位
		XSSFCell cell = rowCorp.createCell(0);
		cell.setCellStyle(rightLockCellStyle);
		cell.setCellValue("单位");
		XSSFCell cell1 = rowCorp.createCell(1);
		cell1.setCellStyle(leftLockCellStyle);
		cell1.setCellValue(corpName);
		// 计划周期类型
		XSSFCell cell2 = rowCorp.createCell(2);
		cell2.setCellStyle(rightLockCellStyle);
		cell2.setCellValue("计划周期类型");
		XSSFCell cell3 = rowCorp.createCell(3);
		cell3.setCellStyle(leftLockCellStyle);
		cell3.setCellValue(getTimeTypeName(timeType));
		XSSFRow rowDate = sheet.createRow(1);
		// 计划开始时间
		XSSFCell cellDate = rowDate.createCell(0);
		cellDate.setCellStyle(rightLockCellStyle);
		cellDate.setCellValue("计划开始时间");
		XSSFCell cellDate1 = rowDate.createCell(1);
		cellDate1.setCellStyle(leftLockCellStyle);
		cellDate1.setCellValue(DateUtils.formatDate(beginDate));
		// 计划结束时间
		XSSFCell cellDate2 = rowDate.createCell(2);
		cellDate2.setCellStyle(rightLockCellStyle);
		cellDate2.setCellValue("计划结束时间");
		XSSFCell cellDate3 = rowDate.createCell(3);
		cellDate3.setCellStyle(leftLockCellStyle);
		cellDate3.setCellValue(DateUtils.formatDate(endDate));
		XSSFRow rowCur = sheet.createRow(2);
		// 币别
		XSSFCell cellCur = rowCur.createCell(0);
		cellCur.setCellStyle(rightLockCellStyle);
		cellCur.setCellValue("币别");
		XSSFCell cellCur1 = rowCur.createCell(1);
		cellCur1.setCellStyle(leftLockCellStyle);
		cellCur1.setCellValue(curName);
		XSSFCell cellCur2 = rowCur.createCell(2);
		cellCur2.setCellStyle(rightLockCellStyle);
		cellCur2.setCellValue("");
		XSSFCell cellCur3 = rowCur.createCell(3);
		cellCur3.setCellStyle(leftLockCellStyle);
		cellCur3.setCellValue("");
	}
	/**
	 * 导出预算固定模式模板
	 *
	 * @param request
	 * @param list
	 */
	public void exporFbsBudgetProcess(HttpServletRequest request, List<FbsBudgetProcessReportVO> list, String filePath, String title) {
		FileOutputStream fout = null;
		try {
			String codedFileName = this.getCodedFileName(request, "资金计划执行表");
			File file = this.getFile(filePath);
			// 声明一个工作薄
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 生成一个表格
			XSSFSheet sheet = workbook.createSheet(codedFileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			XSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook);
			XSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook);
			XSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook);
			// 组装表格标题头
			String[] head = new String[]{"序号", "科目编号", "科目名称", "上报金额", "追加金额", "", "计划上报合计金额", "执行金额（内部）", "", "执行金额（外部）", "", "可用金额", "可用金额占比", "执行比例", "误差率"};
			this.setHeadByFbsBudgetProcess(workbook, sheet, head, 1);
			// 主标题
			XSSFRow rowTitle = sheet.createRow(0);
			XSSFCell cellTitle = rowTitle.createCell(0);
			cellTitle.setCellStyle(centerLockCellStyle);
			cellTitle.setCellValue(title);
			for (int i = 1; i < head.length; i++) {
				rowTitle.createCell(i);
			}
			CellRangeAddress regionTitle = new CellRangeAddress(0, 0, 0, head.length - 1);
			setRegionStyle(sheet, regionTitle, centerLockCellStyle);
			sheet.addMergedRegion(regionTitle);
			// 组装数据
			if (!EmptyUtils.isEmptyList(list)) {
				int index = 3;
				int no = 1;
				for (FbsBudgetProcessReportVO vo : list) {
					XSSFRow row = sheet.createRow(index);
					// 序号
					XSSFCell cell = row.createCell(0);
					cell.setCellStyle(centerLockCellStyle);
					cell.setCellValue(no++);
					// 科目编号
					XSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(leftLockCellStyle);
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					XSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(leftLockCellStyle);
					cell2.setCellValue(vo.getItemName());
					// 上报金额
					XSSFCell cellBmoney = row.createCell(3);
					cellBmoney.setCellStyle(rightLockCellStyle);
					cellBmoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getbMoney()) ? BigDecimal.ZERO : vo.getbMoney()));
					// 追加金额-已追加
					XSSFCell cellBsuperMoney = row.createCell(4);
					cellBsuperMoney.setCellStyle(rightLockCellStyle);
					cellBsuperMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getBsuperMoney()) ? BigDecimal.ZERO : vo.getBsuperMoney()));
					// 追加金额-在途
					XSSFCell cellBtempMoney = row.createCell(5);
					cellBtempMoney.setCellStyle(rightLockCellStyle);
					cellBtempMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getBtempMoney()) ? BigDecimal.ZERO : vo.getBtempMoney()));
					// 计划上报合计金额
					XSSFCell cellBmoneySum = row.createCell(6);
					cellBmoneySum.setCellStyle(rightLockCellStyle);
					cellBmoneySum.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getbMoneySum()) ? BigDecimal.ZERO : vo.getbMoneySum()));
					// 执行金额（内部）-占用
					XSSFCell cellInProcessMoney = row.createCell(7);
					cellInProcessMoney.setCellStyle(rightLockCellStyle);
					cellInProcessMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getInProcessMoney()) ? BigDecimal.ZERO : vo.getInProcessMoney()));
					// 执行金额（内部）-在途
					XSSFCell cellInTempProcessMoney = row.createCell(8);
					cellInTempProcessMoney.setCellStyle(rightLockCellStyle);
					cellInTempProcessMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getInTempProcessMoney()) ? BigDecimal.ZERO : vo.getInTempProcessMoney()));
					// 执行金额（外部）-占用
					XSSFCell cellOutProcessMoney = row.createCell(9);
					cellOutProcessMoney.setCellStyle(rightLockCellStyle);
					cellOutProcessMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getOutProcessMoney()) ? BigDecimal.ZERO : vo.getOutProcessMoney()));
					// 执行金额（外部）-在途
					XSSFCell cellOutTempProcessMoney = row.createCell(10);
					cellOutTempProcessMoney.setCellStyle(rightLockCellStyle);
					cellOutTempProcessMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getOutTempProcessMoney()) ? BigDecimal.ZERO : vo.getOutTempProcessMoney()));
					// 可用金额
					XSSFCell cellCanBMoney = row.createCell(11);
					cellCanBMoney.setCellStyle(rightLockCellStyle);
					if (vo.getItem().getSign() == 1) {
						cellCanBMoney.setCellValue("--");
					} else {
						cellCanBMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getCanBMoney()) ? BigDecimal.ZERO : vo.getCanBMoney()));
					}
					// 可用金额占比
					XSSFCell cellCanBMoneyRatio = row.createCell(12);
					cellCanBMoneyRatio.setCellStyle(rightLockCellStyle);
					if (vo.getItem().getSign() == 1) {
						cellCanBMoneyRatio.setCellValue("--");
					} else {
						cellCanBMoneyRatio.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getCanBMoneyRatio()) ? BigDecimal.ZERO : vo.getCanBMoneyRatio()) + "%");
					}
					// 执行比例
					XSSFCell cellExerciseRatio = row.createCell(13);
					cellExerciseRatio.setCellStyle(rightLockCellStyle);
					cellExerciseRatio.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getExerciseRatio()) ? BigDecimal.ZERO : vo.getExerciseRatio()) + "%");
					// 误差率
					XSSFCell cellErrorRate = row.createCell(14);
					cellErrorRate.setCellStyle(rightLockCellStyle);
					cellErrorRate.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getErrorRate()) ? BigDecimal.ZERO : vo.getErrorRate()) + "%");
					index++;
				}
			}
			fout = new FileOutputStream(file);
			workbook.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 设置预算分解标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head     标题集合
	 * @param rowTitle 标题行
	 */
	public void setHeadByFbsBudgetProcess(XSSFWorkbook workbook, XSSFSheet sheet, String[] head, int rowTitle) {
		XSSFCellStyle centerCellStyle = getCenterCellStyle(workbook);
		// 第一行标题
		XSSFRow row = sheet.createRow(rowTitle);
		for (int i = 0; i < head.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
		}
		for (Cell cell : row) {
			cell.setCellStyle(centerCellStyle);
		}
		// 第二行标题
		XSSFRow row1 = sheet.createRow(rowTitle + 1);
		row1.createCell(0);
		row1.createCell(1);
		row1.createCell(2);
		row1.createCell(3);
		XSSFCell cell4 = row1.createCell(4);
		cell4.setCellValue("已追加");
		XSSFCell cell5 = row1.createCell(5);
		cell5.setCellValue("在途");
		row1.createCell(6);
		XSSFCell cell7 = row1.createCell(7);
		cell7.setCellValue("占用");
		XSSFCell cell8 = row1.createCell(8);
		cell8.setCellValue("在途");
		XSSFCell cell9 = row1.createCell(9);
		cell9.setCellValue("占用");
		XSSFCell cell10 = row1.createCell(10);
		cell10.setCellValue("在途");
		row1.createCell(11);
		row1.createCell(12);
		row1.createCell(13);
		row1.createCell(14);
		for (Cell c : row1) {
			c.setCellStyle(centerCellStyle);
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
		for (int i = 0; i <= 3; i++) {
			CellRangeAddress region1 = new CellRangeAddress(1, 2, i, i);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
		CellRangeAddress region4 = new CellRangeAddress(1, 1, 4, 5);
		setRegionStyle(sheet, region4, centerCellStyle);
		sheet.addMergedRegion(region4);
		CellRangeAddress region6 = new CellRangeAddress(1, 2, 6, 6);
		setRegionStyle(sheet, region6, centerCellStyle);
		sheet.addMergedRegion(region6);
		CellRangeAddress region7 = new CellRangeAddress(1, 1, 7, 8);
		setRegionStyle(sheet, region7, centerCellStyle);
		sheet.addMergedRegion(region7);
		CellRangeAddress region9 = new CellRangeAddress(1, 1, 9, 10);
		setRegionStyle(sheet, region9, centerCellStyle);
		sheet.addMergedRegion(region9);
		for (int i = 11; i <= 14; i++) {
			CellRangeAddress region1 = new CellRangeAddress(1, 2, i, i);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
	}
	/**
	 * 设置合并单元格的边框
	 *
	 * @param sheet
	 * @param region
	 * @param cs
	 */
	private void setRegionStyle(XSSFSheet sheet, CellRangeAddress region, XSSFCellStyle cs) {
		for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
			XSSFRow row = sheet.getRow(i);
			if (region.getFirstColumn() != region.getLastColumn()) {
				for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
					XSSFCell cell = row.getCell((short) j);
					cell.setCellStyle(cs);
				}
			}
		}
	}
	/**
	 * 导出预算固定模式模板
	 *
	 * @param request
	 * @param map
	 */
	public void exporFbsBudget(HttpServletRequest request, Map<String, Object> map,
							   String filePath, String corpName, String timeType, Date beginDate, Date endDate,String curName) {
		FileOutputStream fout = null;
		try {
			String codedFileName = this.getCodedFileName(request, "计划上报情况表");
			File file = this.getFile(filePath);
			// 声明一个工作薄
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 生成一个表格
			XSSFSheet sheet = workbook.createSheet(codedFileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			XSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook);
			XSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook);
			XSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook);
			// 组装数据
			if (!EmptyUtils.isEmpty(map)) {
				// 组装计划基本信息
				this.setBudgetInfo(workbook, sheet, corpName, timeType, beginDate, endDate,curName);
				// 组装表格标题头
				String[] head = new String[]{"序号", "科目编号", "科目名称"};
				int startRow = 3;
				int endRow = BudgetConstant.TIME_TYPE_DAY.equalsIgnoreCase(timeType) ? (startRow + 1) : (startRow + 2);
				Map<Object, Object> timeMap = (Map<Object, Object>) map.get("timeMap");
				this.setHeadByFbsBudget(workbook, sheet, head, startRow, endRow, head.length, timeType, timeMap);
				int index = endRow + 1;
				int no = 1;
				List<FbsBudgetDtlAbsVO> itemList = (List<FbsBudgetDtlAbsVO>) map.get("itemList");
				Map<String, FbsBudgetDtlAbsVO> dtlMap = (Map<String, FbsBudgetDtlAbsVO>) map.get("dtlMap");
				for (FbsBudgetDtlAbsVO vo : itemList) {
					XSSFRow row = sheet.createRow(index);
					// 序号
					XSSFCell cell = row.createCell(0);
					cell.setCellStyle(centerLockCellStyle);
					cell.setCellValue(no++);
					// 科目编号
					XSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(leftLockCellStyle);
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					XSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(leftLockCellStyle);
					cell2.setCellValue(vo.getItemName());
					int tempIndex = 1;
					for (Map.Entry<Object, Object> timeEntry : timeMap.entrySet()) {
						for (Map.Entry<String, FbsBudgetDtlAbsVO> dtlEntry : dtlMap.entrySet()) {
							boolean flag = (timeEntry.getKey() + BudgetConstant.BUDGET_SPLIT + vo.getItemCode()).equalsIgnoreCase(dtlEntry.getKey());
							if (flag) {
								int tempCol = head.length + (tempIndex - 1) * 3;
								// 上报金额
								XSSFCell cellBmoney = row.createCell(tempCol);
								cellBmoney.setCellStyle(rightLockCellStyle);
								cellBmoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getbMoney()) ? BigDecimal.ZERO : vo.getbMoney()));
								// 追加金额-已追加
								XSSFCell cellBsuperMoney = row.createCell(tempCol + 1);
								cellBsuperMoney.setCellStyle(rightLockCellStyle);
								cellBsuperMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getBsuperMoney()) ? BigDecimal.ZERO : vo.getBsuperMoney()));
								// 追加金额-在途
								XSSFCell cellBtempMoney = row.createCell(tempCol + 2);
								cellBtempMoney.setCellStyle(rightLockCellStyle);
								cellBtempMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(vo.getBtempMoney()) ? BigDecimal.ZERO : vo.getBtempMoney()));
							}
						}
						tempIndex++;
					}
					index++;
				}
			}
			fout = new FileOutputStream(file);
			workbook.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 导出预算固定模式模板
	 *
	 * @param request
	 * @param map
	 */
	public void exporFbsBudgetCollect(HttpServletRequest request, Map<String, Object> map,
							   String filePath, String corpName, String timeType, Date beginDate, Date endDate,String curName) {
		FileOutputStream fout = null;
		try {
			String codedFileName = this.getCodedFileName(request, "计划上报情况表");
			File file = this.getFile(filePath);
			// 声明一个工作薄
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 生成一个表格
			XSSFSheet sheet = workbook.createSheet(codedFileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			XSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook);
			XSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook);
			XSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook);
			// 组装数据
			if (!EmptyUtils.isEmpty(map)) {
				// 组装计划基本信息
				this.setBudgetInfo(workbook, sheet, corpName, timeType, beginDate, endDate,curName);
				// 组装表格标题头
				String[] head = new String[]{"序号", "科目编号", "科目名称"};
				int startRow = 4;
				int endRow = BudgetConstant.TIME_TYPE_DAY.equalsIgnoreCase(timeType) ? (startRow + 2) : (startRow + 3);
				Map<Object, Object> timeMap = (Map<Object, Object>) map.get("timeMap");
				this.setHeadByFbsBudgetCollect(workbook, sheet, head, startRow, endRow, head.length, timeType, timeMap,corpName);
				int index = endRow + 1;
				int no = 1;
				List<FbsBudgetDtlAbsVO> itemList = (List<FbsBudgetDtlAbsVO>) map.get("itemList");
				Map<String, Map<String,BigDecimal>> dtlMap = (Map<String, Map<String,BigDecimal>>) map.get("dtlMap");
				for (FbsBudgetDtlAbsVO vo : itemList) {
					XSSFRow row = sheet.createRow(index);
					// 序号
					XSSFCell cell = row.createCell(0);
					cell.setCellStyle(centerLockCellStyle);
					cell.setCellValue(no++);
					// 科目编号
					XSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(leftLockCellStyle);
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					XSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(leftLockCellStyle);
					cell2.setCellValue(vo.getItemName());
					int tempIndex = 1;
					for (Map.Entry<Object, Object> timeEntry : timeMap.entrySet()) {
						for (Map.Entry<String, Map<String,BigDecimal>> dtlEntry : dtlMap.entrySet()) {
							boolean flag = (timeEntry.getKey() + BudgetConstant.BUDGET_SPLIT + vo.getItemCode()).equalsIgnoreCase(dtlEntry.getKey());
							if (flag) {
								int tempCol = head.length + (tempIndex - 1) * 6;
								// 上报金额
								XSSFCell cellBmoney = row.createCell(tempCol);
								cellBmoney.setCellStyle(rightLockCellStyle);
								cellBmoney.setCellValue(NumberUtil.formatNumeric(dtlEntry.getValue().get("bMoney1")));
								XSSFCell cellBmoney1 = row.createCell(tempCol+1);
								cellBmoney1.setCellStyle(rightLockCellStyle);
								cellBmoney1.setCellValue(NumberUtil.formatNumeric(dtlEntry.getValue().get("bMoney")));
								// 追加金额-已追加
								XSSFCell cellBsuperMoney = row.createCell(tempCol + 2);
								cellBsuperMoney.setCellStyle(rightLockCellStyle);
								cellBsuperMoney.setCellValue(NumberUtil.formatNumeric(dtlEntry.getValue().get("bSuperMoney1")));
								XSSFCell cellBsuperMoney1 = row.createCell(tempCol + 3);
								cellBsuperMoney1.setCellStyle(rightLockCellStyle);
								cellBsuperMoney1.setCellValue(NumberUtil.formatNumeric(dtlEntry.getValue().get("bSuperMoney")));
								// 追加金额-在途
								XSSFCell cellBtempMoney = row.createCell(tempCol + 4);
								cellBtempMoney.setCellStyle(rightLockCellStyle);
								cellBtempMoney.setCellValue(NumberUtil.formatNumeric(dtlEntry.getValue().get("bTempMoney1")));
								XSSFCell cellBtempMoney1 = row.createCell(tempCol + 5);
								cellBtempMoney1.setCellStyle(rightLockCellStyle);
								cellBtempMoney1.setCellValue(NumberUtil.formatNumeric(dtlEntry.getValue().get("bTempMoney")));
							}
						}
						tempIndex++;
					}
					index++;
				}
			}
			fout = new FileOutputStream(file);
			workbook.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 设置计划上报情况表标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head     标题集合
	 * @param startRow 标题起始行
	 */
	public void setHeadByFbsBudget(XSSFWorkbook workbook, XSSFSheet sheet, String[] head, int startRow, int endRow, int startCol, String timeType, Map<Object, Object> timeMap) {
		XSSFCellStyle centerCellStyle = getCenterCellStyle(workbook);
		// 第一行标题
		XSSFRow row = sheet.createRow(startRow);
		for (int i = 0; i < head.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(centerCellStyle);
		}
		for (int i = startRow + 1; i <= endRow; i++) {
			XSSFRow createRow = sheet.createRow(i);
			for (int j = 0; j < head.length; j++) {
				XSSFCell cell = createRow.createCell(j);
				cell.setCellStyle(centerCellStyle);
			}
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）z
		for (int i = 0; i < head.length; i++) {
			CellRangeAddress region1 = new CellRangeAddress(startRow, endRow, i, i);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
		int tempCol = startCol;
		int tempRow = startRow;
		if (!BudgetConstant.TIME_TYPE_DAY.equalsIgnoreCase(timeType)) {
			XSSFRow createRow1 = sheet.getRow(startRow);
			for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
				XSSFCell cell = createRow1.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
					cell.setCellValue(entry.getKey() + "年");
				} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
					cell.setCellValue("第" + entry.getKey() + "季");
				} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
					cell.setCellValue(entry.getKey() + "月");
				} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
					cell.setCellValue("第" + entry.getKey() + "周");
				}
				createRow1.createCell(tempCol + 1);
				createRow1.createCell(tempCol + 2);
				CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol, tempCol + 2);
				setRegionStyle(sheet, region1, centerCellStyle);
				sheet.addMergedRegion(region1);
				tempCol = tempCol + 3;
			}
		} else {
			tempRow = startRow - 1;
		}
		// 第二行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow2 = sheet.getRow(tempRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			XSSFCell cell = createRow2.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue(String.valueOf(entry.getValue()));
			createRow2.createCell(tempCol + 1);
			createRow2.createCell(tempCol + 2);
			CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol, tempCol + 2);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
			tempCol = tempCol + 3;
		}
		// 第三行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow3 = sheet.getRow(tempRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			XSSFCell cell = createRow3.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue("上报金额");
			XSSFCell cell2 = createRow3.createCell(tempCol + 1);
			cell2.setCellStyle(centerCellStyle);
			cell2.setCellValue("已追加金额(已通过)");
			XSSFCell cell3 = createRow3.createCell(tempCol + 2);
			cell3.setCellStyle(centerCellStyle);
			cell3.setCellValue("新追加金额(未通过)");
			tempCol = tempCol + 3;
		}
	}
	/**
	 * 设置计划上报情况表标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head     标题集合
	 * @param startRow 标题起始行
	 */
	public void setHeadByFbsBudgetCollect(XSSFWorkbook workbook, XSSFSheet sheet, String[] head, int startRow, int endRow, int startCol, String timeType, Map<Object, Object> timeMap,String corpName) {
		XSSFCellStyle centerCellStyle = getCenterCellStyle(workbook);
		// 第一行标题
		XSSFRow row = sheet.createRow(startRow);
		for (int i = 0; i < head.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(centerCellStyle);
		}
		for (int i = startRow + 1; i <= endRow; i++) {
			XSSFRow createRow = sheet.createRow(i);
			for (int j = 0; j < head.length; j++) {
				XSSFCell cell = createRow.createCell(j);
				cell.setCellStyle(centerCellStyle);
			}
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）z
		for (int i = 0; i < head.length; i++) {
			CellRangeAddress region1 = new CellRangeAddress(startRow, endRow, i, i);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
		int tempCol = startCol;
		int tempRow = startRow;
		if (!BudgetConstant.TIME_TYPE_DAY.equalsIgnoreCase(timeType)) {
			XSSFRow createRow1 = sheet.getRow(startRow);
			for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
				XSSFCell cell = createRow1.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
					cell.setCellValue(entry.getKey() + "年");
				} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
					cell.setCellValue("第" + entry.getKey() + "季");
				} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
					cell.setCellValue(entry.getKey() + "月");
				} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
					cell.setCellValue("第" + entry.getKey() + "周");
				}
				createRow1.createCell(tempCol + 1);
				createRow1.createCell(tempCol + 2);
				createRow1.createCell(tempCol + 3);
				createRow1.createCell(tempCol + 4);
				createRow1.createCell(tempCol + 5);
				CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol, tempCol + 5);
				setRegionStyle(sheet, region1, centerCellStyle);
				sheet.addMergedRegion(region1);
				tempCol = tempCol + 6;
			}
		} else {
			tempRow = startRow - 1;
		}
		// 第二行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow2 = sheet.getRow(tempRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			XSSFCell cell = createRow2.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue(String.valueOf(entry.getValue()));
			createRow2.createCell(tempCol + 1);
			createRow2.createCell(tempCol + 2);
			createRow2.createCell(tempCol + 3);
			createRow2.createCell(tempCol + 4);
			createRow2.createCell(tempCol + 5);
			CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol, tempCol + 5);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
			tempCol = tempCol + 6;
		}
		// 第三行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow3 = sheet.getRow(tempRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			XSSFCell cell = createRow3.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue("上报金额");
			createRow3.createCell(tempCol + 1);
			CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol, tempCol + 1);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
			XSSFCell cell2 = createRow3.createCell(tempCol + 2);
			cell2.setCellStyle(centerCellStyle);
			cell2.setCellValue("已追加金额(已通过)");
			createRow3.createCell(tempCol + 3);
			CellRangeAddress region2 = new CellRangeAddress(tempRow, tempRow, tempCol+2, tempCol + 3);
			setRegionStyle(sheet, region2, centerCellStyle);
			sheet.addMergedRegion(region2);
			XSSFCell cell3 = createRow3.createCell(tempCol + 4);
			cell3.setCellStyle(centerCellStyle);
			cell3.setCellValue("新追加金额(未通过)");
			createRow3.createCell(tempCol + 5);
			CellRangeAddress region3 = new CellRangeAddress(tempRow, tempRow, tempCol+4, tempCol + 5);
			setRegionStyle(sheet, region3, centerCellStyle);
			sheet.addMergedRegion(region3);
			tempCol = tempCol + 6;
		}
		//第四行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow4 = sheet.getRow(tempRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			XSSFCell cell = createRow4.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue(corpName);
			XSSFCell cell1 = createRow4.createCell(tempCol + 1);
			cell1.setCellStyle(centerCellStyle);
			cell1.setCellValue(corpName+"(汇总)");
			XSSFCell cell2 = createRow4.createCell(tempCol + 2);
			cell2.setCellStyle(centerCellStyle);
			cell2.setCellValue(corpName);
			XSSFCell cell3 = createRow4.createCell(tempCol + 3);
			cell3.setCellStyle(centerCellStyle);
			cell3.setCellValue(corpName+"(汇总)");
			XSSFCell cell4 = createRow4.createCell(tempCol + 4);
			cell4.setCellStyle(centerCellStyle);
			cell4.setCellValue(corpName);
			XSSFCell cell5 = createRow4.createCell(tempCol + 5);
			cell5.setCellStyle(centerCellStyle);
			cell5.setCellValue(corpName+"(汇总)");
			tempCol = tempCol + 6;
		}
	}
	/**
	 * 导出预算分解模板
	 *
	 * @param request
	 * @param map
	 */
	public void exporFbsBudgetResolve(HttpServletRequest request, Map<String, Object> map,
									  String filePath, String corpName, String timeType, Date beginDate, Date endDate,String curName) {
		FileOutputStream fout = null;
		try {
			String codedFileName = this.getCodedFileName(request, "计划上报情况表");
			File file = this.getFile(filePath);
			// 声明一个工作薄
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 生成一个表格
			XSSFSheet sheet = workbook.createSheet(codedFileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			XSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook);
			XSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook);
			XSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook);
			// 组装数据
			if (!EmptyUtils.isEmpty(map)) {
				// 组装计划基本信息
				this.setBudgetInfo(workbook, sheet, corpName, timeType, beginDate, endDate,curName);
				// 组装表格标题头
				String[] head = new String[]{"序号", "科目编号", "科目名称"};
				int startRow = 3;
				int endRow = startRow + 2;
				Map<Object, Object> timeMap = (Map<Object, Object>) map.get("timeMap");
				this.setHeadByFbsBudgetResolve(workbook, sheet, head, startRow, endRow, head.length, timeType, timeMap);
				int index = endRow + 1;
				int no = 1;
				List itemList = (List) map.get("itemList");
				Map<String, Object> dtlMap = (Map<String, Object>) map.get("dtlMap");
				for (Object obj : itemList) {
					String itemCode = "";
					String itemName = "";
					if (obj instanceof FbsBudgetResolveYearVO) {
						itemCode = ((FbsBudgetResolveYearVO) obj).getItemCode();
						itemName = ((FbsBudgetResolveYearVO) obj).getItemName();
					} else if (obj instanceof FbsBudgetResolveMonthVO) {
						itemCode = ((FbsBudgetResolveMonthVO) obj).getItemCode();
						itemName = ((FbsBudgetResolveMonthVO) obj).getItemName();
					} else {
						continue;
					}
					XSSFRow row = sheet.createRow(index);
					// 序号
					XSSFCell cell = row.createCell(0);
					cell.setCellStyle(centerLockCellStyle);
					cell.setCellValue(no++);
					// 科目编号
					XSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(leftLockCellStyle);
					cell1.setCellValue(itemCode);
					// 科目名称
					XSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(leftLockCellStyle);
					cell2.setCellValue(itemName);
					int tempIndex = 1;
					for (Map.Entry<Object, Object> timeEntry : timeMap.entrySet()) {
						Object[] objs = (Object[]) timeEntry.getValue();
						List<Object> monthList = (List<Object>) objs[1];
						for (int m = 0; m < monthList.size(); m++) {
							int tempCol = head.length + (tempIndex - 1) * monthList.size() * 3 + m * 3;
							for (Map.Entry<String, Object> dtlEntry : dtlMap.entrySet()) {
								boolean flag = (timeEntry.getKey() + BudgetConstant.BUDGET_SPLIT + itemCode + BudgetConstant.BUDGET_SPLIT + monthList.get(m)).equalsIgnoreCase(dtlEntry.getKey());
								if (flag) {
									HashMap<Integer, BigDecimal> bMoneyMap = (HashMap<Integer, BigDecimal>) dtlEntry.getValue();
									int temp = 0;
									for (Map.Entry<Integer, BigDecimal> money : bMoneyMap.entrySet()) {
										// 上报金额
										XSSFCell cellBmoney = row.createCell(tempCol + temp);
										cellBmoney.setCellStyle(rightLockCellStyle);
										cellBmoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(money.getValue()) ? BigDecimal.ZERO : money.getValue()));
										temp++;
									}
								}
							}
						}
						tempIndex++;
					}
					index++;
				}
			}
			fout = new FileOutputStream(file);
			workbook.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 设置计划上报情况表标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head     标题集合
	 * @param startRow 标题起始行
	 */
	public void setHeadByFbsBudgetResolve(XSSFWorkbook workbook, XSSFSheet sheet, String[] head, int startRow, int endRow, int startCol, String timeType, Map<Object, Object> timeMap) {
		XSSFCellStyle centerCellStyle = getCenterCellStyle(workbook);
		// 第一行标题
		XSSFRow row = sheet.createRow(startRow);
		for (int i = 0; i < head.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(centerCellStyle);
		}
		for (int i = startRow + 1; i <= endRow; i++) {
			XSSFRow createRow = sheet.createRow(i);
			for (int j = 0; j < head.length; j++) {
				XSSFCell cell = createRow.createCell(j);
				cell.setCellStyle(centerCellStyle);
			}
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）z
		for (int i = 0; i < head.length; i++) {
			CellRangeAddress region1 = new CellRangeAddress(startRow, endRow, i, i);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
		int tempCol = startCol;
		int tempRow = startRow;
		XSSFRow createRow1 = sheet.getRow(startRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			Object[] objs = (Object[]) entry.getValue();
			List<Object> monthList = (List<Object>) objs[1];
			XSSFCell cell = createRow1.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
				cell.setCellValue(entry.getKey() + "年");
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
				cell.setCellValue(entry.getKey() + "月");
			}
			int tempLastCol = tempCol + monthList.size() * 3;
			for (int i = tempCol + 1; i < tempLastCol; i++) {
				createRow1.createCell(i);
			}
			CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol, tempLastCol - 1);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
			tempCol = tempLastCol;
		}
		// 第二行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow2 = sheet.getRow(tempRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			Object[] objs = (Object[]) entry.getValue();
			List<Object> monthList = (List<Object>) objs[1];
			for (Object month : monthList) {
				XSSFCell cell = createRow2.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
					cell.setCellValue(String.valueOf(month) + "月");
				} else {
					cell.setCellValue(String.valueOf(month) + "周");
				}
				createRow2.createCell(tempCol + 1);
				createRow2.createCell(tempCol + 2);
				CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol, tempCol + 2);
				setRegionStyle(sheet, region1, centerCellStyle);
				sheet.addMergedRegion(region1);
				tempCol = tempCol + 3;
			}
		}
		// 第三行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow3 = sheet.getRow(tempRow);
		for (Map.Entry<Object, Object> entry : timeMap.entrySet()) {
			Object[] objs = (Object[]) entry.getValue();
			List<Object> monthList = (List<Object>) objs[1];
			for (Object month : monthList) {
				XSSFCell cell = createRow3.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				cell.setCellValue("上报金额");
				XSSFCell cell2 = createRow3.createCell(tempCol + 1);
				cell2.setCellStyle(centerCellStyle);
				cell2.setCellValue("已追加金额(已通过)");
				XSSFCell cell3 = createRow3.createCell(tempCol + 2);
				cell3.setCellStyle(centerCellStyle);
				cell3.setCellValue("新追加金额(未通过)");
				tempCol = tempCol + 3;
			}
		}
	}
	/**
	 * 导出合并计划上报表模板
	 *
	 * @param request
	 * @param map
	 */
	public void exporMergeFbsBudget(HttpServletRequest request, String filePath, Map<String, Object> map, Map<String, String> moneyMap, String timeType, Date beginDate, Date endDate) {
		FileOutputStream fout = null;
		try {
			String codedFileName = this.getCodedFileName(request, "合并计划上报表");
			File file = this.getFile(filePath);
			// 声明一个工作薄
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 生成一个表格
			XSSFSheet sheet = workbook.createSheet(codedFileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			XSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook);
			XSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook);
			XSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook);
			// 组装数据
			if (!EmptyUtils.isEmpty(map)) {
				// 组装计划基本信息
				XSSFRow rowInfo = sheet.createRow(0);
				// 单位
				XSSFCell cellTimeType = rowInfo.createCell(0);
				cellTimeType.setCellStyle(rightLockCellStyle);
				cellTimeType.setCellValue("计划周期类型");
				XSSFCell cellTimeType1 = rowInfo.createCell(1);
				cellTimeType1.setCellStyle(leftLockCellStyle);
				cellTimeType1.setCellValue(getTimeTypeName(timeType));
				// 计划开始时间
				XSSFCell cellDate = rowInfo.createCell(2);
				cellDate.setCellStyle(rightLockCellStyle);
				cellDate.setCellValue("时间");
				XSSFCell cellDate1 = rowInfo.createCell(3);
				cellDate1.setCellStyle(leftLockCellStyle);
				cellDate1.setCellValue(DateUtils.formatDate(beginDate) + " ~ " + DateUtils.formatDate(endDate));
				// 币别
				XSSFCell cellCur = rowInfo.createCell(4);
				cellCur.setCellStyle(rightLockCellStyle);
				cellCur.setCellValue("币别");
				XSSFCell cellCur1 = rowInfo.createCell(5);
				cellCur1.setCellStyle(leftLockCellStyle);
				cellCur1.setCellValue(btCurrencyService.getNatualCurrency().getCurName());
				Map<Object, Object> timeMap = (Map<Object, Object>) map.get("timeMap");
				List<FbsBudgetDtlAbsVO> itemList = (List<FbsBudgetDtlAbsVO>) map.get("itemList");
				Map<String, String> corpMap = (Map<String, String>) map.get("corpMap");
				Map<String, FbsBudgetDtlAbsVO> dtlMap = (Map<String, FbsBudgetDtlAbsVO>) map.get("dtlMap");
				Map<String, Object> collectMap = (Map<String, Object>) map.get("collectMap");
				// 组装表格标题头
				String[] head = new String[]{"序号", "科目编号", "科目名称", "合并金额"};
				int startRow = 2;
				int endRow = startRow + 1;
				this.setHeadByMergeFbsBudget(workbook, sheet, head, startRow, endRow, head.length, timeType, timeMap, moneyMap, corpMap);
				// 组装数据
				int index = endRow + 1;
				int no = 1;
				for (FbsBudgetDtlAbsVO vo : itemList) {
					XSSFRow row = sheet.createRow(index++);
					// 序号
					XSSFCell cell = row.createCell(0);
					cell.setCellStyle(centerLockCellStyle);
					cell.setCellValue(no++);
					// 科目编号
					XSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(leftLockCellStyle);
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					XSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(leftLockCellStyle);
					cell2.setCellValue(vo.getItemName());
					// 汇总金额
					for (Map.Entry<String, Object> entry : collectMap.entrySet()) {
						if (vo.getItemCode().equalsIgnoreCase(entry.getKey())) {
							XSSFCell cell3 = row.createCell(3);
							cell3.setCellStyle(rightLockCellStyle);
							cell3.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(entry.getValue()) ? BigDecimal.ZERO : new BigDecimal(String.valueOf(entry.getValue()))));
						}
					}
					int tempIndex = head.length;
					for (Map.Entry<String, String> moneyEntry : moneyMap.entrySet()) {
						for (Map.Entry<String, String> corpEntry : corpMap.entrySet()) {
							for (Map.Entry<String, FbsBudgetDtlAbsVO> dtlEntry : dtlMap.entrySet()) {
								boolean flag = (vo.getItemCode() + BudgetConstant.BUDGET_SPLIT + corpEntry.getKey()).equalsIgnoreCase(dtlEntry.getKey());
								if (flag) {
									// 上报金额
									if ("1".equalsIgnoreCase(moneyEntry.getKey())) {
										XSSFCell cellBmoney = row.createCell(tempIndex++);
										cellBmoney.setCellStyle(rightLockCellStyle);
										cellBmoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(dtlEntry.getValue().getbMoney()) ? BigDecimal.ZERO : dtlEntry.getValue().getbMoney()));
									}
									// 追加金额-已追加
									if ("2".equalsIgnoreCase(moneyEntry.getKey())) {
										XSSFCell cellBsuperMoney = row.createCell(tempIndex++);
										cellBsuperMoney.setCellStyle(rightLockCellStyle);
										cellBsuperMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(dtlEntry.getValue().getBsuperMoney()) ? BigDecimal.ZERO : dtlEntry.getValue().getBsuperMoney()));
									}
									// 追加金额-在途
									if ("3".equalsIgnoreCase(moneyEntry.getKey())) {
										XSSFCell cellBtempMoney = row.createCell(tempIndex++);
										cellBtempMoney.setCellStyle(rightLockCellStyle);
										cellBtempMoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(dtlEntry.getValue().getBtempMoney()) ? BigDecimal.ZERO : dtlEntry.getValue().getBtempMoney()));
									}
								}
							}
						}
					}
				}
			}
			fout = new FileOutputStream(file);
			workbook.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 设置计划上报情况表标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head     标题集合
	 * @param startRow 标题起始行
	 */
	public void setHeadByMergeFbsBudget(XSSFWorkbook workbook, XSSFSheet sheet, String[] head, int startRow, int endRow, int startCol,
										String timeType, Map<Object, Object> timeMap, Map<String, String> moneyMap, Map<String, String> corpMap) {
		XSSFCellStyle centerCellStyle = getCenterCellStyle(workbook);
		// 第一行标题
		XSSFRow row = sheet.createRow(startRow);
		for (int i = 0; i < head.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(centerCellStyle);
		}
		for (int i = startRow + 1; i <= endRow; i++) {
			XSSFRow createRow = sheet.createRow(i);
			for (int j = 0; j < head.length; j++) {
				XSSFCell cell = createRow.createCell(j);
				cell.setCellStyle(centerCellStyle);
			}
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）z
		for (int i = 0; i < head.length; i++) {
			CellRangeAddress region1 = new CellRangeAddress(startRow, endRow, i, i);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
		int tempCol = startCol;
		int tempRow = startRow;
		XSSFRow createRow1 = sheet.getRow(startRow);
		if (moneyMap.containsKey("1")) {
			XSSFCell cell = createRow1.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue("上报金额");
			if (corpMap.size() > 1) {
				for (int i = 1; i < corpMap.size(); i++) {
					createRow1.createCell(tempCol + i);
				}
				tempCol = tempCol + corpMap.size();
				CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol - corpMap.size(), tempCol - 1);
				setRegionStyle(sheet, region1, centerCellStyle);
				sheet.addMergedRegion(region1);
			} else {
				tempCol++;
			}
		}
		if (moneyMap.containsKey("2")) {
			XSSFCell cell = createRow1.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue("已追加金额(已通过)");
			if (corpMap.size() > 1) {
				for (int i = 1; i < corpMap.size(); i++) {
					createRow1.createCell(tempCol + i);
				}
				tempCol = tempCol + corpMap.size();
				CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol - corpMap.size(), tempCol - 1);
				setRegionStyle(sheet, region1, centerCellStyle);
				sheet.addMergedRegion(region1);
			} else {
				tempCol++;
			}
		}
		if (moneyMap.containsKey("3")) {
			XSSFCell cell = createRow1.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			cell.setCellValue("新追加金额(未通过)");
			if (corpMap.size() > 1) {
				for (int i = 1; i < corpMap.size(); i++) {
					createRow1.createCell(tempCol + i);
				}
				tempCol = tempCol + corpMap.size();
				CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol - corpMap.size(), tempCol - 1);
				setRegionStyle(sheet, region1, centerCellStyle);
				sheet.addMergedRegion(region1);
			}
		}
		// 第二行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow2 = sheet.getRow(tempRow);
		for (Map.Entry<String, String> entry : moneyMap.entrySet()) {
			for (Map.Entry<String, String> corpEntry : corpMap.entrySet()) {
				XSSFCell cell = createRow2.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				cell.setCellValue(corpEntry.getValue());
				tempCol++;
			}
		}
	}
	/**
	 * 导出合并计划上报表模板
	 *
	 * @param request
	 * @param map
	 */
	public void exporMergeFbsBudgetResolve(HttpServletRequest request, String filePath, Map<String, Object> map, Map<String, String> moneyMap, String timeType, Date beginDate, Date endDate, BtCurrencyDTO btCurrencyDTO) {
		FileOutputStream fout = null;
		try {
			String codedFileName = this.getCodedFileName(request, "合并计划上报表");
			File file = this.getFile(filePath);
			// 声明一个工作薄
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 生成一个表格
			XSSFSheet sheet = workbook.createSheet(codedFileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			XSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook);
			XSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook);
			XSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook);
			// 组装数据
			if (!EmptyUtils.isEmpty(map)) {
				// 组装计划基本信息
				XSSFRow rowInfo = sheet.createRow(0);
				// 单位
				XSSFCell cellTimeType = rowInfo.createCell(0);
				cellTimeType.setCellStyle(rightLockCellStyle);
				cellTimeType.setCellValue("计划周期类型");
				XSSFCell cellTimeType1 = rowInfo.createCell(1);
				cellTimeType1.setCellStyle(leftLockCellStyle);
				cellTimeType1.setCellValue(getTimeTypeName(timeType));
				// 计划开始时间
				XSSFCell cellDate = rowInfo.createCell(2);
				cellDate.setCellStyle(rightLockCellStyle);
				cellDate.setCellValue("时间");
				XSSFCell cellDate1 = rowInfo.createCell(3);
				cellDate1.setCellStyle(leftLockCellStyle);
				cellDate1.setCellValue(DateUtils.formatDate(beginDate) + " ~ " + DateUtils.formatDate(endDate));
				// 币别
				XSSFCell cellCur = rowInfo.createCell(4);
				cellCur.setCellStyle(rightLockCellStyle);
				cellCur.setCellValue("币别");
				XSSFCell cellCur1 = rowInfo.createCell(5);
				cellCur1.setCellStyle(leftLockCellStyle);
				int maxDateCount = (int) map.get("maxDateCount");
				Map<Object, Object> timeMap = (Map<Object, Object>) map.get("timeMap");
				Map<String, com.bytter.biz.budget.spi.vo.FbsItemVO> itemMap = (Map<String, com.bytter.biz.budget.spi.vo.FbsItemVO>) map.get("itemMap");
				Map<String, String> corpMap = (Map<String, String>) map.get("corpMap");
				Map<String, Object> dtlMap = (Map<String, Object>) map.get("dtlMap");
				Map<String, Object> collectMap = (Map<String, Object>) map.get("collectMap");
				cellCur1.setCellValue(btCurrencyDTO.getCurName());
				// 组装表格标题头
				String[] head = new String[]{"序号", "科目编号", "科目名称", "合并金额"};
				int startRow = 2;
				int endRow = startRow + 2;
				this.setHeadByMergeFbsBudgetResolve(workbook, sheet, head, startRow, endRow, head.length, timeType, timeMap, moneyMap, corpMap, maxDateCount);
				// 组装数据
				int index = endRow + 1;
				int no = 1;
				for (Map.Entry<String, com.bytter.biz.budget.spi.vo.FbsItemVO> entry : itemMap.entrySet()) {
					String itemName = entry.getValue().getItemName();
					String itemCode = entry.getValue().getItemCode();
					XSSFRow row = sheet.createRow(index++);
					// 序号
					XSSFCell cell = row.createCell(0);
					cell.setCellStyle(centerLockCellStyle);
					cell.setCellValue(no++);
					// 科目编号
					XSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(leftLockCellStyle);
					cell1.setCellValue(itemCode);
					// 科目名称
					XSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(leftLockCellStyle);
					cell2.setCellValue(itemName);
					// 汇总金额
					for (Map.Entry<String, Object> collectEntry : collectMap.entrySet()) {
						if (itemCode.equalsIgnoreCase(collectEntry.getKey())) {
							XSSFCell cell3 = row.createCell(3);
							cell3.setCellStyle(rightLockCellStyle);
							cell3.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(collectEntry.getValue()) ? BigDecimal.ZERO : new BigDecimal(String.valueOf(collectEntry.getValue()))));
						}
					}
					int tempIndex = head.length;
					for (Map.Entry<String, Object> dtlEntry : dtlMap.entrySet()) {
						boolean flag = itemCode.equalsIgnoreCase(dtlEntry.getKey());
						if (flag) {
							Map<Integer, BigDecimal> bMoneyMap = (Map<Integer, BigDecimal>) dtlEntry.getValue();
							for (Map.Entry<Integer, BigDecimal> moneyEntry : bMoneyMap.entrySet()) {
								XSSFCell cellBmoney = row.createCell(tempIndex++);
								cellBmoney.setCellStyle(rightLockCellStyle);
								cellBmoney.setCellValue(NumberUtil.formatNumeric(EmptyUtils.isEmpty(moneyEntry.getValue()) ? BigDecimal.ZERO : moneyEntry.getValue()));
							}
						}
					}
				}
			}
			fout = new FileOutputStream(file);
			workbook.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 设置计划上报情况表标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head     标题集合
	 * @param startRow 标题起始行
	 */
	public void setHeadByMergeFbsBudgetResolve(XSSFWorkbook workbook, XSSFSheet sheet, String[] head, int startRow, int endRow, int startCol,
											   String timeType, Map<Object, Object> timeMap, Map<String, String> moneyMap, Map<String, String> corpMap, int maxDateCount) {
		XSSFCellStyle centerCellStyle = getCenterCellStyle(workbook);
		// 第一行标题
		XSSFRow row = sheet.createRow(startRow);
		for (int i = 0; i < head.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(head[i]);
			cell.setCellStyle(centerCellStyle);
		}
		for (int i = startRow + 1; i <= endRow; i++) {
			XSSFRow createRow = sheet.createRow(i);
			for (int j = 0; j < head.length; j++) {
				XSSFCell cell = createRow.createCell(j);
				cell.setCellStyle(centerCellStyle);
			}
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）z
		for (int i = 0; i < head.length; i++) {
			CellRangeAddress region1 = new CellRangeAddress(startRow, endRow, i, i);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
		int tempCol = startCol;
		int tempRow = startRow;
		XSSFRow createRow1 = sheet.getRow(startRow);
		for (int i = 1; i <= maxDateCount; i++) {
			XSSFCell cell = createRow1.createCell(tempCol);
			cell.setCellStyle(centerCellStyle);
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
				cell.setCellValue(String.valueOf(i) + "月");
			} else {
				cell.setCellValue(String.valueOf(i) + "周");
			}
			int col = moneyMap.size() * corpMap.size();
			for (int j = 1; j < col; j++) {
				createRow1.createCell(tempCol + j);
			}
			tempCol = tempCol + col;
			CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol - col, tempCol - 1);
			setRegionStyle(sheet, region1, centerCellStyle);
			sheet.addMergedRegion(region1);
		}
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow2 = sheet.getRow(tempRow);
		for (int k = 1; k <= maxDateCount; k++) {
			if (moneyMap.containsKey("1")) {
				XSSFCell cell = createRow2.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				cell.setCellValue("上报金额");
				if (corpMap.size() > 1) {
					for (int i = 1; i < corpMap.size(); i++) {
						createRow2.createCell(tempCol + i);
					}
					tempCol = tempCol + corpMap.size();
					CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol - corpMap.size(), tempCol - 1);
					setRegionStyle(sheet, region1, centerCellStyle);
					sheet.addMergedRegion(region1);
				} else {
					tempCol++;
				}
			}
			if (moneyMap.containsKey("2")) {
				XSSFCell cell = createRow2.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				cell.setCellValue("已追加金额(已通过)");
				if (corpMap.size() > 1) {
					for (int i = 1; i < corpMap.size(); i++) {
						createRow2.createCell(tempCol + i);
					}
					tempCol = tempCol + corpMap.size();
					CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol - corpMap.size(), tempCol - 1);
					setRegionStyle(sheet, region1, centerCellStyle);
					sheet.addMergedRegion(region1);
				} else {
					tempCol++;
				}
			}
			if (moneyMap.containsKey("3")) {
				XSSFCell cell = createRow2.createCell(tempCol);
				cell.setCellStyle(centerCellStyle);
				cell.setCellValue("新追加金额(未通过)");
				if (corpMap.size() > 1) {
					for (int i = 1; i < corpMap.size(); i++) {
						createRow2.createCell(tempCol + i);
					}
					tempCol = tempCol + corpMap.size();
					CellRangeAddress region1 = new CellRangeAddress(tempRow, tempRow, tempCol - corpMap.size(), tempCol - 1);
					setRegionStyle(sheet, region1, centerCellStyle);
					sheet.addMergedRegion(region1);
				} else {
					tempCol++;
				}
			}
		}
		// 第三行
		tempCol = startCol;
		tempRow = tempRow + 1;
		XSSFRow createRow3 = sheet.getRow(tempRow);
		for (int i = 1; i <= maxDateCount; i++) {
			for (Map.Entry<String, String> entry : moneyMap.entrySet()) {
				for (Map.Entry<String, String> corpEntry : corpMap.entrySet()) {
					XSSFCell cell = createRow3.createCell(tempCol);
					cell.setCellStyle(centerCellStyle);
					cell.setCellValue(corpEntry.getValue());
					tempCol++;
				}
			}
		}
	}
	/**
	 * 导出
	 *
	 * @param request
	 * @param response
	 * @param fileName
	 * @param filePath
	 * @param exportType
	 */
	public void exports(HttpServletRequest request, HttpServletResponse response, String fileName, String filePath, String exportType) {
		File file = new File(filePath);
		if (file.exists()) {
			try {
				if (ServletUtils.isIE(request)) {
					fileName = URLEncoder.encode(fileName, "UTF8");
				} else {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				}
			} catch (UnsupportedEncodingException e) {
				fileName = fileName;
			}
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/force-download;");
			response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int length;
				while ((length = bis.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			} catch (Exception e) {
				throw new BusinessException(e);
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
