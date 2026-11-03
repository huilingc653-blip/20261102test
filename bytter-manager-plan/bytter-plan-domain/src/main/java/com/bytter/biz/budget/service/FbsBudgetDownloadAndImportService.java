package com.bytter.biz.budget.service;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsCorpItem;
import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.entity.FbsSalesReturnAnalyse;
import com.bytter.biz.budget.vo.*;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.excel.dto.ExcelImportDTO;
import com.bytter.core.excel.enums.ExcelMessageEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.enums.IResourceTemplateEnum;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.poi.enums.SplitTypeEnum;
import com.bytter.poi.excel.ExcelImportUtil;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.result.ExcelImportResult;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FBS_BUDGET_DTL_ABS
 * @date 2017-07-18 11:25:46
 */
@Service("fbsBudgetDownloadAndImportService")
@Transactional
public class FbsBudgetDownloadAndImportService<excelImportServiceImpl> implements IUserExcelVerifyHandler<FbsBudgetImportVO> {
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
	@Autowired
	private FbsBudgetRollMonthService fbsBudgetRollMonthService;
	@Autowired
	private FbsCorpItemService fbsCorpItemService;
	@Autowired
	private FbsBudgetProcessService fbsBudgetProcessService;
	@Autowired
	private FbsSalesReturnAnalyseService fbsSalesReturnAnalyseService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private FbsBudgetDetailService fbsBudgetDetailService;
	@Autowired
	private FbsBudgetRollMonthWeekService fbsBudgetRollMonthWeekService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired(required=false)
	private FbsBudgetRollQuarterService fbsBudgetRollQuarterService;
    @Autowired(required=false)
    private FbsBudgetRollWeekService fbsBudgetRollWeekService;
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public ExcelVerifyHanlderResult verifyHandler(FbsBudgetImportVO vo, Object... obj) {
		List<String> msgList = new ArrayList<>();
		if (EmptyUtils.isEmpty(vo.getbMoney())) {
			vo.setbMoney(BigDecimal.ZERO);
		}
		if (!EmptyUtils.isBlank(vo.getItemCode())) {
			FbsItemVO item = this.fbsItemService.getFbsItemVO(null, vo.getItemCode());
			if (item != null && CommonConstant.YES.equals(item.getValidSign())) {
				vo.setItem(this.fbsItemService.getFbsItemVO(null, vo.getItemCode()));
			} else {
				msgList.add("budget.message.itemcode.notexist");
			}
		} else {
			msgList.add("nis.assign.message.itemcode.null");
		}
		if (EmptyUtils.isEmptyList(msgList)) {
			return new ExcelVerifyHanlderResult(true, msgList);
		} else {
			return new ExcelVerifyHanlderResult(false, msgList);
		}
	}
	@Override
	public void verifyHandler(List<FbsBudgetImportVO> list, Object... user) {
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
	 * 获取居中样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook, Boolean locked) {
		return initStyle(workbook, HorizontalAlignment.CENTER, locked);
	}
	/**
	 * 获取靠右样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook, Boolean locked) {
		return initStyle(workbook, HorizontalAlignment.RIGHT, locked);
	}
	/**
	 * 获取靠左样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getLeftCellStyle(HSSFWorkbook workbook, Boolean locked) {
		return initStyle(workbook, HorizontalAlignment.LEFT, locked);
	}
	/**
	 * 获取居中锁定背景色样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getCenterLockCellStyle(HSSFWorkbook workbook) {
		// 标题样式
		HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.CENTER, true);
		// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GOLD.index);
		style.setWrapText(true);
		return style;
	}
	/**
	 * 获取居中锁定背景色样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getCenterLockWriteCellStyle(HSSFWorkbook workbook) {
		// 标题样式
		HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.CENTER, true);
		// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setWrapText(true);
		return style;
	}
	/**
	 * 获取靠右锁定背景色样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getRightLockCellStyle(HSSFWorkbook workbook) {
		// 标题样式
		HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.RIGHT, true);
		// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		return style;
	}
	/**
	 * 获取靠右锁定背景色样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getRightLockWriteCellStyle(HSSFWorkbook workbook) {
		// 标题样式
		HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.RIGHT, true);
		// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		return style;
	}
	/**
	 * 获取靠左样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getLeftLockCellStyle(HSSFWorkbook workbook) {
		// 标题样式
		HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.LEFT, true);
		// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GOLD.index);
		style.setWrapText(true);
		return style;
	}
	/**
	 * 获取靠左样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getLeftLockWriteCellStyle(HSSFWorkbook workbook) {
		// 标题样式
		HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.LEFT, true);
		// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setWrapText(true);
		return style;
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
	 * 写入最后行
	 *
	 * @param workbook
	 * @param sheet
	 * @param lastSize
	 * @throws Exception
	 */
	public void setLastCell(HSSFWorkbook workbook, HSSFSheet sheet, int lastSize) throws Exception {
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("注意：１.灰色单元格表示系统科目或明细科目金额不能修改;２.资金计划导入会覆盖浏览器上报的计划金额;");
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
	 * @param timeType
	 * @param target
	 * @return
	 */
	public String getFileName(String timeType, String target) {
		String timeTypeName = getTimeTypeName(timeType);
		if (EmptyUtils.isBlank(timeTypeName)) {
			timeTypeName = "计划";
		}
		String targetName = BudgetConstant.BUDGET_TARGET_SUPER.equalsIgnoreCase(target) ? "追加" : "上报";
		return timeTypeName + targetName;
	}
	/**
	 * 获取文件名
	 *
	 * @param request
	 * @param timeType
	 * @param target
	 * @return
	 */
	public String getCodedFileName(HttpServletRequest request, String timeType, String target) {
		String timeTypeName = dictService.get("fbs.timeType", timeType);
		if (EmptyUtils.isBlank(timeTypeName)) {
			timeTypeName = "计划";
		}
		String targetName = BudgetConstant.BUDGET_TARGET_SUPER.equalsIgnoreCase(target) ? "追加" : "上报";
		String codedFileName = timeTypeName + targetName;
        codedFileName= modifyFileNameOfV1(request,codedFileName);
		return codedFileName;
	}
	/**
	 * 下载预算固定模式模板
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void downloadBudget(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			int moneyColumn = 3;
			// 组装标题头
			String moneyName = BudgetConstant.BUDGET_TARGET_SUPER.equalsIgnoreCase(target) ? "新追加金额" : "上报金额";
			String[] head = new String[]{"序号", "科目编号", "科目名称", moneyName};
			this.setHeadCell(workbook, sheet, head, 2);
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 3;
				HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
				HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
				HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
				HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
                //不可填写科目样式置灰
                HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
                HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
				int rowNum = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					HSSFRow row = sheet.createRow(index);
					// 序号
					HSSFCell cell1 = row.createCell(0);
					if(CommonConstant.YES.equals(vo.getIsWrite())) {
                        cell1.setCellStyle(centerLockCellStyle);
                    }else{
                        cell1.setCellStyle(centerLockWriteCellStyle);
                    }
					cell1.setCellValue(rowNum++);
					// 科目编号
					HSSFCell cell2 = row.createCell(1);
                    if(CommonConstant.YES.equals(vo.getIsWrite())) {
                        cell2.setCellStyle(leftLockCellStyle);
                    }else{
                        cell2.setCellStyle(leftLockWriteCellStyle);
                    }
					cell2.setCellValue(vo.getItemCode());
					// 科目名称
					HSSFCell cell3 = row.createCell(2);
                    if(CommonConstant.YES.equals(vo.getIsWrite())) {
                        cell3.setCellStyle(leftLockCellStyle);
                    }else{
                        cell3.setCellStyle(leftLockWriteCellStyle);
                    }
					cell3.setCellValue(vo.getItemName());
					// 预算金额
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
					HSSFCell cell4 = row.createCell(moneyColumn);
					if (!EmptyUtils.isEmpty(fbsItemVO) && "1".equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell4.setCellStyle(rightUnLockCellStyle);
					} else {
						cell4.setCellStyle(rightLockCellStyle);
					}
					cell4.setCellValue("0.00");
					index++;
				}
				// 设置最后行
				this.setLastCell(workbook, sheet, dtlList.size());
			}
			workbook.write(os);
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
	 * 下载预算分解模板
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void downloadBudgetResolve(HttpServletRequest request, HttpServletResponse response, String id, String timeType, Date beginDate, String target) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			//年计划上报模式:0,年固定;1,年分解
			String yimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
			//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
			String mimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
			boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
			boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
            //不可填写科目样式置灰
            HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
            HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && resolveYear) {
				// 组装标题头
				for (int i = 1; i <= 12; i++) {
					headList.add(i + "月");
				}
				String[] head = headList.toArray(new String[]{});
				// new String[]{"序号", "科目编号", "科目名称", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"}
				this.setHeadCell(workbook, sheet, head, 2);
				List<FbsBudgetResolveYearVO> listResolveYearBudget = this.fbsBudgetResolveYearService.listVOByBudgetId(id);
				int minMonth = DateUtils.getMonth(DateUtils.getDate());
				if (!EmptyUtils.isEmptyList(listResolveYearBudget)) {
					int index = 3;
					int rowNum = 1;
					for (FbsBudgetResolveYearVO budgetResolveYear : listResolveYearBudget) {
                        FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, budgetResolveYear.getItemCode());
						HSSFRow row = sheet.createRow(index);
						// 序号
						HSSFCell cell1 = row.createCell(0);
						if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell1.setCellStyle(centerLockCellStyle);
                        }else{
                            cell1.setCellStyle(centerLockWriteCellStyle);
                        }
						cell1.setCellValue(rowNum++);
						// 科目编号
						HSSFCell cell2 = row.createCell(1);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell2.setCellStyle(leftLockCellStyle);
                        }else{
                            cell2.setCellStyle(leftLockWriteCellStyle);
                        }
						cell2.setCellValue(budgetResolveYear.getItemCode());
						// 科目名称
						HSSFCell cell3 = row.createCell(2);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell3.setCellStyle(leftLockCellStyle);
                        }else{
                            cell3.setCellStyle(leftLockWriteCellStyle);
                        }
						cell3.setCellValue(budgetResolveYear.getItemName());
						for (int i = 1; i <= 12; i++) {
							// 预算金额
							HSSFCell cellMoney = row.createCell(2 + i);
							if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
								if (i < minMonth && BudgetConstant.BUDGET_TARGET_SUPER.equalsIgnoreCase(target)) {
									cellMoney.setCellStyle(rightLockCellStyle);
								} else {
									cellMoney.setCellStyle(rightUnLockCellStyle);
								}
							} else {
								cellMoney.setCellStyle(rightLockCellStyle);
							}
							cellMoney.setCellValue("0.00");
						}
						index++;
					}
					// 设置最后行
					this.setLastCell(workbook, sheet, listResolveYearBudget.size());
				}
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && resolveMonth) {
				// 组装标题头
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(beginDate);
				for (int i = 0; i < listPeriod.size(); i++) {
					headList.add("第" + (i + 1) + "周");
				}
				String[] head = headList.toArray(new String[]{});
				this.setHeadCell(workbook, sheet, head, 2);
				List<FbsBudgetResolveMonthVO> listMonth = this.fbsBudgetResolveMonthService.listVOByBudgetId(id);
				int minWeek = DateUtils.getWeekOfMonth(DateUtils.getDate());
				if (!EmptyUtils.isEmptyList(listMonth)) {
					int index = 3;
					int rowNum = 4;
					for (FbsBudgetResolveMonthVO budgetResolveMonth : listMonth) {
                        FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, budgetResolveMonth.getItemCode());
						HSSFRow row = sheet.createRow(index);
						// 序号
						HSSFCell cell1 = row.createCell(0);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell1.setCellStyle(centerLockCellStyle);
                        }else{
                            cell1.setCellStyle(centerLockWriteCellStyle);
                        }
						cell1.setCellValue(rowNum++);
						// 科目编号
						HSSFCell cell2 = row.createCell(1);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell2.setCellStyle(leftLockCellStyle);
                        }else{
                            cell2.setCellStyle(leftLockWriteCellStyle);
                        }
						cell2.setCellValue(budgetResolveMonth.getItemCode());
						// 科目名称
						HSSFCell cell3 = row.createCell(2);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell3.setCellStyle(leftLockCellStyle);
                        }else{
                            cell3.setCellStyle(leftLockWriteCellStyle);
                        }
						cell3.setCellValue(budgetResolveMonth.getItemName());
						for (int i = 0; i < listPeriod.size(); i++) {
							// 预算金额
							HSSFCell cellMoney = row.createCell(3 + i);
							if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
								if ((i + 1) < minWeek && BudgetConstant.BUDGET_TARGET_SUPER.equalsIgnoreCase(target)) {
									cellMoney.setCellStyle(rightLockCellStyle);
								} else {
									cellMoney.setCellStyle(rightUnLockCellStyle);
								}
							} else {
								cellMoney.setCellStyle(rightLockCellStyle);
							}
							cellMoney.setCellValue("0.00");
						}
						index++;
					}
					// 设置最后行
					this.setLastCell(workbook, sheet, listMonth.size());
				}
			}
			workbook.write(os);
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
	 * 组装计划基础信息
	 *
	 * @param workbook
	 * @param sheet
	 * @param id
	 * @throws Exception
	 */
	public void setBudgetInfo(HSSFWorkbook workbook, HSSFSheet sheet, String id) throws Exception {
		HSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook, true);
		HSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook, true);
		FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
		HSSFRow rowCorp = sheet.createRow(0);
		// 单位
		HSSFCell cell = rowCorp.createCell(0);
		cell.setCellStyle(rightLockCellStyle);
		cell.setCellValue("单位");
		HSSFCell cell1 = rowCorp.createCell(1);
		cell1.setCellStyle(leftLockCellStyle);
		cell1.setCellValue(fbsBudget.getCorpName());
		// 计划周期类型
		HSSFCell cell2 = rowCorp.createCell(2);
		cell2.setCellStyle(rightLockCellStyle);
		cell2.setCellValue("计划周期类型");
		HSSFCell cell3 = rowCorp.createCell(3);
		cell3.setCellStyle(leftLockCellStyle);
		cell3.setCellValue(getTimeTypeName(fbsBudget.getTimeType()));
		HSSFRow rowDate = sheet.createRow(1);
		// 计划开始时间
		HSSFCell cellDate = rowDate.createCell(0);
		cellDate.setCellStyle(rightLockCellStyle);
		cellDate.setCellValue("计划开始时间");
		HSSFCell cellDate1 = rowDate.createCell(1);
		cellDate1.setCellStyle(leftLockCellStyle);
		cellDate1.setCellValue(DateUtils.formatDate(fbsBudget.getBeginDate()));
		// 计划结束时间
		HSSFCell cellDate2 = rowDate.createCell(2);
		cellDate2.setCellStyle(rightLockCellStyle);
		cellDate2.setCellValue("计划结束时间");
		HSSFCell cellDate3 = rowDate.createCell(3);
		cellDate3.setCellStyle(leftLockCellStyle);
		cellDate3.setCellValue(DateUtils.formatDate(fbsBudget.getEndDate()));
		HSSFRow rowCur = sheet.createRow(2);
		// 币别
		HSSFCell cellCur = rowCur.createCell(0);
		cellCur.setCellStyle(rightLockCellStyle);
		cellCur.setCellValue("币别");
		HSSFCell cellCur1 = rowCur.createCell(1);
		cellCur1.setCellStyle(leftLockCellStyle);
		cellCur1.setCellValue(fbsBudget.getCurName());
		HSSFCell cellCur2 = rowCur.createCell(2);
		cellCur2.setCellStyle(rightLockCellStyle);
		cellCur2.setCellValue("");
		HSSFCell cellCur3 = rowCur.createCell(3);
		cellCur3.setCellStyle(leftLockCellStyle);
		cellCur3.setCellValue("");
	}
	/**
	 * 组装滚动计划基础信息
	 *
	 * @param workbook
	 * @param sheet
	 * @param id
	 * @throws Exception
	 */
	public void setBudgetInfoRoll(HSSFWorkbook workbook, HSSFSheet sheet, String id,Date endDate) throws Exception {
		HSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook, true);
		HSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook, true);
		FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
		HSSFRow rowCorp = sheet.createRow(0);
		// 单位
		HSSFCell cell = rowCorp.createCell(0);
		cell.setCellStyle(rightLockCellStyle);
		cell.setCellValue("单位");
		HSSFCell cell1 = rowCorp.createCell(1);
		cell1.setCellStyle(leftLockCellStyle);
		cell1.setCellValue(fbsBudget.getCorpName());
		// 计划周期类型
		HSSFCell cell2 = rowCorp.createCell(2);
		cell2.setCellStyle(rightLockCellStyle);
		cell2.setCellValue("计划周期类型");
		HSSFCell cell3 = rowCorp.createCell(3);
		cell3.setCellStyle(leftLockCellStyle);
		cell3.setCellValue(getTimeTypeName(fbsBudget.getTimeType()));
		HSSFRow rowDate = sheet.createRow(1);
		// 计划开始时间
		HSSFCell cellDate = rowDate.createCell(0);
		cellDate.setCellStyle(rightLockCellStyle);
		cellDate.setCellValue("计划开始时间");
		HSSFCell cellDate1 = rowDate.createCell(1);
		cellDate1.setCellStyle(leftLockCellStyle);
		cellDate1.setCellValue(DateUtils.formatDate(fbsBudget.getBeginDate()));
		// 计划结束时间
		HSSFCell cellDate2 = rowDate.createCell(2);
		cellDate2.setCellStyle(rightLockCellStyle);
		cellDate2.setCellValue("计划结束时间");
		HSSFCell cellDate3 = rowDate.createCell(3);
		cellDate3.setCellStyle(leftLockCellStyle);
		cellDate3.setCellValue(DateUtils.formatDate(endDate));
		HSSFRow rowCur = sheet.createRow(2);
		// 币别
		HSSFCell cellCur = rowCur.createCell(0);
		cellCur.setCellStyle(rightLockCellStyle);
		cellCur.setCellValue("币别");
		HSSFCell cellCur1 = rowCur.createCell(1);
		cellCur1.setCellStyle(leftLockCellStyle);
		cellCur1.setCellValue(fbsBudget.getCurName());
		HSSFCell cellCur2 = rowCur.createCell(2);
		cellCur2.setCellStyle(rightLockCellStyle);
		cellCur2.setCellValue("");
		HSSFCell cellCur3 = rowCur.createCell(3);
		cellCur3.setCellStyle(leftLockCellStyle);
		cellCur3.setCellValue("");
	}
	/**
	 * 导出预算固定模式模板
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void exportBudget(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) {
		String filePath = request.getSession().getServletContext().getRealPath("/") + File.separator + "budget" + File.separator + "download" + File.separator;
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装计划基础信息
			this.setBudgetInfo(workbook, sheet, id);
			// 组装标题头
			String[] head = new String[]{"序号", "科目编号", "科目名称", "上报金额", "已追加金额(已通过)", "上报金额合计", "新追加金额(未通过)"};
			this.setHeadCell(workbook, sheet, head, 4);
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			// 组装数据
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 5;
				int num = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					HSSFRow row = sheet.createRow(index);
					// 序号
					HSSFCell cell = row.createCell(0);
					if(CommonConstant.YES.equals(vo.getIsWrite())) {
						cell.setCellStyle(centerLockCellStyle);
					}else{
						cell.setCellStyle(centerLockWriteCellStyle);
					}
					cell.setCellValue(num);
					// 科目编号
					HSSFCell cell1 = row.createCell(1);
					if(CommonConstant.YES.equals(vo.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					HSSFCell cell2 = row.createCell(2);
					if(CommonConstant.YES.equals(vo.getIsWrite())) {
						cell2.setCellStyle(leftLockCellStyle);
					}else{
						cell2.setCellStyle(leftLockWriteCellStyle);
					}
					cell2.setCellValue(vo.getItemName());
					// 上报金额
					HSSFCell cell3 = row.createCell(3);
					cell3.setCellStyle(rightLockCellStyle);
					BigDecimal bMoney = EmptyUtils.isEmpty(vo.getbMoney()) ? BigDecimal.ZERO : vo.getbMoney();
					cell3.setCellValue(NumberUtil.formatNumeric(bMoney));
					if (CommonConstant.YES.equalsIgnoreCase(vo.getIsWrite())) {
						cell3.setCellStyle(rightUnLockCellStyle);
					} else {
						cell3.setCellStyle(rightLockCellStyle);
					}
					// 已追加金额(已通过)
					HSSFCell cell4 = row.createCell(4);
					cell4.setCellStyle(rightLockCellStyle);
					BigDecimal bsuperMoney = EmptyUtils.isEmpty(vo.getBsuperMoney()) ? BigDecimal.ZERO : vo.getBsuperMoney();
					cell4.setCellValue(NumberUtil.formatNumeric(bsuperMoney));
					if (CommonConstant.YES.equalsIgnoreCase(vo.getIsWrite())) {
						cell4.setCellStyle(rightUnLockCellStyle);
					} else {
						cell4.setCellStyle(rightLockCellStyle);
					}
					// 上报金额合计
					HSSFCell cell5 = row.createCell(5);
					cell5.setCellStyle(rightLockCellStyle);
					BigDecimal bMoneySum = EmptyUtils.isEmpty(vo.getbMoneySum()) ? BigDecimal.ZERO : vo.getbMoneySum();
					cell5.setCellValue(NumberUtil.formatNumeric(bMoneySum));
					if (CommonConstant.YES.equalsIgnoreCase(vo.getIsWrite())) {
						cell5.setCellStyle(rightUnLockCellStyle);
					} else {
						cell5.setCellStyle(rightLockCellStyle);
					}
					// 新追加金额(未通过)
					HSSFCell cell6 = row.createCell(6);
					cell6.setCellStyle(rightLockCellStyle);
					BigDecimal btempMoney = EmptyUtils.isEmpty(vo.getBtempMoney()) ? BigDecimal.ZERO : vo.getBtempMoney();
					cell6.setCellValue(NumberUtil.formatNumeric(btempMoney));
					if (CommonConstant.YES.equalsIgnoreCase(vo.getIsWrite())) {
						cell6.setCellStyle(rightUnLockCellStyle);
					} else {
						cell6.setCellStyle(rightLockCellStyle);
					}
					index++;
					num++;
				}
			}
			workbook.write(os);
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
	 * 导出预算固定模式模板(不包含追加信息)
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void exportBudgetInput(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) {
		String filePath = request.getSession().getServletContext().getRealPath("/") + File.separator + "budget" + File.separator + "download" + File.separator;
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装计划基础信息
			this.setBudgetInfo(workbook, sheet, id);
			// 组装标题头
			String[] head = new String[]{"序号", "科目编号", "科目名称", "上报金额"};
			this.setHeadCell(workbook, sheet, head, 3);
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
            //不可填写科目样式置灰
            HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
            HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			// 组装数据
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 4;
				int num = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					HSSFRow row = sheet.createRow(index);
					// 序号
					HSSFCell cell = row.createCell(0);
                    if(CommonConstant.YES.equals(vo.getIsWrite())) {
                        cell.setCellStyle(centerLockCellStyle);
                    }else{
                        cell.setCellStyle(centerLockWriteCellStyle);
                    }
					cell.setCellValue(num);
					// 科目编号
					HSSFCell cell1 = row.createCell(1);
                    if(CommonConstant.YES.equals(vo.getIsWrite())) {
                        cell1.setCellStyle(leftLockCellStyle);
                    }else{
                        cell1.setCellStyle(leftLockWriteCellStyle);
                    }
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					HSSFCell cell2 = row.createCell(2);
                    if(CommonConstant.YES.equals(vo.getIsWrite())) {
                        cell2.setCellStyle(leftLockCellStyle);
                    }else{
                        cell2.setCellStyle(leftLockWriteCellStyle);
                    }
					cell2.setCellValue(vo.getItemName());
					// 上报金额
					HSSFCell cell3 = row.createCell(3);
					cell3.setCellStyle(rightLockCellStyle);
					BigDecimal bMoney = EmptyUtils.isEmpty(vo.getbMoney()) ? BigDecimal.ZERO : vo.getbMoney();
					cell3.setCellValue(NumberUtil.formatNumeric(bMoney));
					if (CommonConstant.YES.equalsIgnoreCase(vo.getIsWrite())) {
						cell3.setCellStyle(rightUnLockCellStyle);
					} else {
						cell3.setCellStyle(rightLockCellStyle);
					}
					index++;
					num++;
				}
			}
			workbook.write(os);
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
	 * 下载预算分解模板
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void exportBudgetResolve(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			//年计划上报模式:0,年固定;1,年分解
			String yimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
			//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
			String mimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
			boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
			boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装计划基础信息
			this.setBudgetInfo(workbook, sheet, id);
            HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
            HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
            HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
            HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
            //不可填写科目样式置灰
            HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
            HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			headList.add("上报金额");
			int periodStartCol = headList.size();
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && resolveYear) {
				// 组装标题头
				for (int i = 1; i <= 12; i++) {
					headList.add(i + "月");
				}
				String[] head = headList.toArray(new String[]{});
				setHeadbudgetResolve(workbook, sheet, head, 4, 12, periodStartCol);
				List<FbsBudgetResolveYearVO> listResolveYearBudget = this.fbsBudgetResolveYearService.listVOByBudgetId(id);
				if (!EmptyUtils.isEmptyList(listResolveYearBudget)) {
					int index = 6;
					int num = 1;
					for (FbsBudgetResolveYearVO budgetResolveYear : listResolveYearBudget) {
                        FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, budgetResolveYear.getItemCode());
						HSSFRow row = sheet.createRow(index);
						// 序号
						HSSFCell cell1 = row.createCell(0);
						if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell1.setCellStyle(centerLockCellStyle);
                        }else{
                            cell1.setCellStyle(centerLockWriteCellStyle);
                        }
						cell1.setCellValue(num);
						// 科目编号
						HSSFCell cell2 = row.createCell(1);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell2.setCellStyle(leftLockCellStyle);
                        }else{
                            cell2.setCellStyle(leftLockWriteCellStyle);
                        }
						cell2.setCellValue(budgetResolveYear.getItemCode());
						// 科目名称
						HSSFCell cell3 = row.createCell(2);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell3.setCellStyle(leftLockCellStyle);
                        }else{
                            cell3.setCellStyle(leftLockWriteCellStyle);
                        }
						cell3.setCellValue(budgetResolveYear.getItemName());
						// 上报金额
						HSSFCell cell4 = row.createCell(3);
						cell4.setCellStyle(rightLockCellStyle);
						cell4.setCellValue(NumberUtil.formatNumeric(budgetResolveYear.getBMoney()));
                        HSSFCellStyle moneyStyle = null;
                        if (CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            moneyStyle = rightUnLockCellStyle;
                        } else {
                            moneyStyle = rightLockCellStyle;
                        }
						for (int i = 1; i <= 12; i++) {
							int col = periodStartCol + (i - 1) * 3;
							// 上报金额
							HSSFCell cellMoney = row.createCell(col);
							cellMoney.setCellStyle(moneyStyle);
							Method method = budgetResolveYear.getClass().getMethod("getBMoney" + i, new Class[0]);
							BigDecimal bMoney = EmptyUtils.isEmpty(method.invoke(budgetResolveYear)) ? BigDecimal.ZERO : (BigDecimal) method.invoke(budgetResolveYear);
							cellMoney.setCellValue(NumberUtil.formatNumeric(bMoney));
							// 已追加金额
							HSSFCell cellBsuperMoney = row.createCell(col + 1);
							cellBsuperMoney.setCellStyle(moneyStyle);
							Method bsuperMethod = budgetResolveYear.getClass().getMethod("getBsuperMoney" + i, new Class[0]);
							BigDecimal bsuperMoney = EmptyUtils.isEmpty(bsuperMethod.invoke(budgetResolveYear)) ? BigDecimal.ZERO : (BigDecimal) bsuperMethod.invoke(budgetResolveYear);
							cellBsuperMoney.setCellValue(NumberUtil.formatNumeric(bsuperMoney));
							// 新追加金额
							HSSFCell cellBtempMoney = row.createCell(col + 2);
							cellBtempMoney.setCellStyle(moneyStyle);
							Method btempMethod = budgetResolveYear.getClass().getMethod("getBtempMoney" + i, new Class[0]);
							BigDecimal btempMoney = EmptyUtils.isEmpty(btempMethod.invoke(budgetResolveYear)) ? BigDecimal.ZERO : (BigDecimal) btempMethod.invoke(budgetResolveYear);
							cellBtempMoney.setCellValue(NumberUtil.formatNumeric(btempMoney));
						}
						index++;
						num++;
					}
				}
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && resolveMonth) {
				// 组装标题头
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(fbsBudget.getBeginDate());
				for (int i = 0; i < listPeriod.size(); i++) {
					headList.add("第" + (i + 1) + "周");
				}
				String[] head = headList.toArray(new String[]{});
				setHeadbudgetResolve(workbook, sheet, head, 4, listPeriod.size(), periodStartCol);
				//  设置数据
				List<FbsBudgetResolveMonthVO> listMonth = this.fbsBudgetResolveMonthService.listVOByBudgetId(id);
				if (!EmptyUtils.isEmptyList(listMonth)) {
					int index = 6;
					int num = 1;
					for (FbsBudgetResolveMonthVO budgetResolveMonth : listMonth) {
                        FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, budgetResolveMonth.getItemCode());
						HSSFRow row = sheet.createRow(index);
						// 序号
						HSSFCell cell1 = row.createCell(0);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell1.setCellStyle(centerLockCellStyle);
                        }else{
                            cell1.setCellStyle(centerLockWriteCellStyle);
                        }
						cell1.setCellValue(num);
						// 科目编号
						HSSFCell cell2 = row.createCell(1);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell2.setCellStyle(leftLockCellStyle);
                        }else{
                            cell2.setCellStyle(leftLockWriteCellStyle);
                        }
						cell2.setCellValue(budgetResolveMonth.getItemCode());
						// 科目名称
						HSSFCell cell3 = row.createCell(2);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell3.setCellStyle(leftLockCellStyle);
                        }else{
                            cell3.setCellStyle(leftLockWriteCellStyle);
                        }
						cell3.setCellValue(budgetResolveMonth.getItemName());
						// 上报金额
						HSSFCell cell4 = row.createCell(3);
						cell4.setCellStyle(rightLockCellStyle);
						cell4.setCellValue(NumberUtil.formatNumeric(budgetResolveMonth.getBMoney()));
                        HSSFCellStyle moneyStyle = null;
                        if (CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            moneyStyle = rightUnLockCellStyle;
                        } else {
                            moneyStyle = rightLockCellStyle;
                        }
						for (int i = 0; i < listPeriod.size(); i++) {
							int col = periodStartCol + i * 3;
							// 上报金额
							HSSFCell cellMoney = row.createCell(col);
							cellMoney.setCellStyle(moneyStyle);
							Method method = budgetResolveMonth.getClass().getMethod("getBMoney" + (i + 1), new Class[0]);
							BigDecimal bMoney = EmptyUtils.isEmpty(method.invoke(budgetResolveMonth)) ? BigDecimal.ZERO : (BigDecimal) method.invoke(budgetResolveMonth);
							cellMoney.setCellValue(NumberUtil.formatNumeric(bMoney));
							// 已追加金额
							HSSFCell cellBsuperMoney = row.createCell(col + 1);
							cellBsuperMoney.setCellStyle(moneyStyle);
							Method bsuperMethod = budgetResolveMonth.getClass().getMethod("getBsuperMoney" + (i + 1), new Class[0]);
							BigDecimal bsuperMoney = EmptyUtils.isEmpty(bsuperMethod.invoke(budgetResolveMonth)) ? BigDecimal.ZERO : (BigDecimal) bsuperMethod.invoke(budgetResolveMonth);
							cellBsuperMoney.setCellValue(NumberUtil.formatNumeric(bsuperMoney));
							// 新追加金额
							HSSFCell cellBtempMoney = row.createCell(col + 2);
							cellBtempMoney.setCellStyle(moneyStyle);
							Method btempMethod = budgetResolveMonth.getClass().getMethod("getBtempMoney" + (i + 1), new Class[0]);
							BigDecimal btempMoney = EmptyUtils.isEmpty(btempMethod.invoke(budgetResolveMonth)) ? BigDecimal.ZERO : (BigDecimal) btempMethod.invoke(budgetResolveMonth);
							cellBtempMoney.setCellValue(NumberUtil.formatNumeric(btempMoney));
						}
						index++;
						num++;
					}
				}
			}
			workbook.write(os);
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
	 * 下载预算分解模板(不包含追加)
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void exportBudgetResolveInput(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			//年计划上报模式:0,年固定;1,年分解
			String yimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YIMODE).getValue();
			//月计划上报模式:0,月固定;1,月滚动;2,月滚动+双月周明细;3,月分解周
			String mimode = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODE).getValue();
			boolean resolveYear = BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_YIMODE_RESOLVE_YEAR.equalsIgnoreCase(yimode);
			boolean resolveMonth = BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && BudgetConstant.FBS_CONFIG_MIMODE_RESOLVE_MONTH_WEEK.equalsIgnoreCase(mimode);
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装计划基础信息
			this.setBudgetInfo(workbook, sheet, id);
            HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
            HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
            HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
            HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
            //不可填写科目样式置灰
            HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
            HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			headList.add("上报金额");
			int periodStartCol = headList.size();
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType) && resolveYear) {
				// 组装标题头
				for (int i = 1; i <= 12; i++) {
					headList.add(i + "月");
				}
				String[] head = headList.toArray(new String[]{});
				setHeadbudgetResolveInput(workbook, sheet, head, 4, 12, periodStartCol);
				List<FbsBudgetResolveYearVO> listResolveYearBudget = this.fbsBudgetResolveYearService.listVOByBudgetId(id);
				if (!EmptyUtils.isEmptyList(listResolveYearBudget)) {
					int index = 6;
					int num = 1;
					for (FbsBudgetResolveYearVO budgetResolveYear : listResolveYearBudget) {
                        FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, budgetResolveYear.getItemCode());
						HSSFRow row = sheet.createRow(index);
						// 序号
						HSSFCell cell1 = row.createCell(0);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell1.setCellStyle(centerLockCellStyle);
                        }else{
                            cell1.setCellStyle(centerLockWriteCellStyle);
                        }
						cell1.setCellValue(num);
						// 科目编号
						HSSFCell cell2 = row.createCell(1);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell2.setCellStyle(leftLockCellStyle);
                        }else{
                            cell2.setCellStyle(leftLockWriteCellStyle);
                        }
						cell2.setCellValue(budgetResolveYear.getItemCode());
						// 科目名称
						HSSFCell cell3 = row.createCell(2);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell3.setCellStyle(leftLockCellStyle);
                        }else{
                            cell3.setCellStyle(leftLockWriteCellStyle);
                        }
						cell3.setCellValue(budgetResolveYear.getItemName());
						// 上报金额
						HSSFCell cell4 = row.createCell(3);
						cell4.setCellStyle(rightLockCellStyle);
						cell4.setCellValue(NumberUtil.formatNumeric(budgetResolveYear.getBMoney()));
                        HSSFCellStyle moneyStyle = null;
                        if (CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            moneyStyle = rightUnLockCellStyle;
                        } else {
                            moneyStyle = rightLockCellStyle;
                        }
						for (int i = 1; i <= 12; i++) {
							int col = periodStartCol + (i - 1) * 1;
							// 上报金额
							HSSFCell cellMoney = row.createCell(col);
							cellMoney.setCellStyle(moneyStyle);
							Method method = budgetResolveYear.getClass().getMethod("getBMoney" + i, new Class[0]);
							BigDecimal bMoney = EmptyUtils.isEmpty(method.invoke(budgetResolveYear)) ? BigDecimal.ZERO : (BigDecimal) method.invoke(budgetResolveYear);
							cellMoney.setCellValue(NumberUtil.formatNumeric(bMoney));
						}
						index++;
						num++;
					}
				}
			} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType) && resolveMonth) {
				// 组装标题头
				List<FbsBudgetPeriodVO> listPeriod = fbsBudgetPeriodService.getWeekByMonth(fbsBudget.getBeginDate());
				for (int i = 0; i < listPeriod.size(); i++) {
					headList.add("第" + (i + 1) + "周");
				}
				String[] head = headList.toArray(new String[]{});
				setHeadbudgetResolveInput(workbook, sheet, head, 4, listPeriod.size(), periodStartCol);
				//  设置数据
				List<FbsBudgetResolveMonthVO> listMonth = this.fbsBudgetResolveMonthService.listVOByBudgetId(id);
				if (!EmptyUtils.isEmptyList(listMonth)) {
					int index = 6;
					int num = 1;
					for (FbsBudgetResolveMonthVO budgetResolveMonth : listMonth) {
                        FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, budgetResolveMonth.getItemCode());
						HSSFRow row = sheet.createRow(index);
						// 序号
						HSSFCell cell1 = row.createCell(0);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell1.setCellStyle(centerLockCellStyle);
                        }else{
                            cell1.setCellStyle(centerLockWriteCellStyle);
                        }
						cell1.setCellValue(num);
						// 科目编号
						HSSFCell cell2 = row.createCell(1);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell2.setCellStyle(leftLockCellStyle);
                        }else{
                            cell2.setCellStyle(leftLockWriteCellStyle);
                        }
						cell2.setCellValue(budgetResolveMonth.getItemCode());
						// 科目名称
						HSSFCell cell3 = row.createCell(2);
                        if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            cell3.setCellStyle(leftLockCellStyle);
                        }else{
                            cell3.setCellStyle(leftLockWriteCellStyle);
                        }
						cell3.setCellValue(budgetResolveMonth.getItemName());
						// 上报金额
						HSSFCell cell4 = row.createCell(3);
						cell4.setCellStyle(rightLockCellStyle);
						cell4.setCellValue(NumberUtil.formatNumeric(budgetResolveMonth.getBMoney()));
                        HSSFCellStyle moneyStyle = null;
                        if (CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
                            moneyStyle = rightUnLockCellStyle;
                        } else {
                            moneyStyle = rightLockCellStyle;
                        }
						for (int i = 0; i < listPeriod.size(); i++) {
							int col = periodStartCol + i * 1;
							// 上报金额
							HSSFCell cellMoney = row.createCell(col);
							cellMoney.setCellStyle(moneyStyle);
							Method method = budgetResolveMonth.getClass().getMethod("getBMoney" + (i + 1), new Class[0]);
							BigDecimal bMoney = EmptyUtils.isEmpty(method.invoke(budgetResolveMonth)) ? BigDecimal.ZERO : (BigDecimal) method.invoke(budgetResolveMonth);
							cellMoney.setCellValue(NumberUtil.formatNumeric(bMoney));
						}
						index++;
						num++;
					}
				}
			}
			workbook.write(os);
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
	 * 导出预算固定模式模板
	 *
	 * @param request
	 * @param response
	 * @param corpId   单位ID
	 * @param timeType 预算类型
	 */
	public void exportBudgetCur(HttpServletRequest request, HttpServletResponse response, String corpId, String timeType, String beginDate, String endDate, String target) {
		String filePath = request.getSession().getServletContext().getRealPath("/") + File.separator + "budget" + File.separator + "download" + File.separator;
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			FbsBudgetVO fbsBudget = new FbsBudgetVO();
			fbsBudget.setCorpId(corpId);
			fbsBudget.setCorpName(corpService.get(corpId).getName());
			fbsBudget.setTimeType(timeType);
			fbsBudget.setBeginDate(DateUtils.parseDate(beginDate));
			fbsBudget.setEndDate(DateUtils.parseDate(endDate));
			BtCurrencyDTO currencyDTO = null;
			//获取汇总单位的资金计划
			FbsBudget corpBudget = this.fbsBudgetService.getCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
			if (corpBudget != null) {
				currencyDTO = btCurrencyService.getObjectById(corpBudget.getCurId());
				fbsBudget.setCurName(currencyDTO.getCurName());
				BeanUtils.copyNotNullProperties(corpBudget, fbsBudget);
				fbsBudget.setDtlList(this.fbsBudgetDtlAbsService.listByBudgetId(corpBudget.getId(), corpBudget.getTimeType()));
			} else {
				//获取本位币
				currencyDTO = btCurrencyService.getNatualCurrency();
				fbsBudget.setCurId(currencyDTO.getId());
				fbsBudget.setCurName(currencyDTO.getCurName());
				List<FbsBudgetDtlAbsVO> list = new ArrayList<>();
				//获取单位分配的科目
				List<FbsItemVO> itemList = fbsCorpItemService.getAllItemByCorpId(fbsBudget.getCorpId());
				for (FbsItemVO itemVO : itemList) {
					FbsBudgetDtlAbsVO dtlAbsVO = new FbsBudgetDtlAbsVO();
					dtlAbsVO.setItem(itemVO);
					dtlAbsVO.setItemCode(itemVO.getItemCode());
					dtlAbsVO.setItemName(itemVO.getItemName());
					dtlAbsVO.setbMoney(BigDecimal.ZERO);
					dtlAbsVO.setBtempMoney(BigDecimal.ZERO);
					dtlAbsVO.setBsuperMoney(BigDecimal.ZERO);
					list.add(dtlAbsVO);
				}
				fbsBudget.setDtlList(list);
			}
			//获取下级单位
			List<FbsBudgetInputPreVO> childCorps = fbsBudgetService.getChildCorpBudget(fbsBudget.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
			LinkedHashMap<String, List<FbsBudgetDtlAbsVO>> childCorpMap = new LinkedHashMap<String, List<FbsBudgetDtlAbsVO>>();
			//存储下级单位计划上报ID
			List<String> budgetIds = new ArrayList<String>();
			//汇总单位
			HashMap<String, FbsBudgetDtlAbsVO> itemSumMap = new HashMap<String, FbsBudgetDtlAbsVO>();
			for (FbsBudgetInputPreVO input : childCorps) {
				//获取下级单位资金计划
				FbsBudget entity = fbsBudgetService.getCorpBudget(input.getCorpId(), fbsBudget.getTimeType(), fbsBudget.getBeginDate(), fbsBudget.getEndDate());
				List<FbsBudgetDtlAbsVO> listDtl = null;
				if (entity != null && !entity.getStatus().equals(BudgetConstant.STATUE_NULL)) {
					//获取下级单位资金计划明细
					listDtl = fbsBudgetDtlAbsService.listByBudgetId(entity.getId(), entity.getTimeType());
					if (!EmptyUtils.isEmptyList(listDtl)) {
						budgetIds.add(entity.getId());
						//存储下级单位分配的科目
						HashMap<String, FbsBudgetDtlAbsVO> itemMap = new HashMap<String, FbsBudgetDtlAbsVO>();
						for (FbsBudgetDtlAbsVO dtl : listDtl) {
							itemMap.put(dtl.getItemCode(), dtl);
							itemSumMap.put(dtl.getItemCode(), dtl);
						}
						for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
							for (FbsBudgetDtlAbsVO childVo : listDtl) {
								if (childVo.getItemCode().equals(vo.getItemCode())) {
									childVo.setbMoney(childVo.getbMoney().multiply(entity.getExchangeRate()));
									childVo.setBsuperMoney(childVo.getBsuperMoney().multiply(entity.getExchangeRate()));
									childVo.setBtempMoney(childVo.getBtempMoney().multiply(entity.getExchangeRate()));
									//设置汇总金额
									vo.setbMoney(vo.getbMoney().add(childVo.getbMoney()));
									vo.setBsuperMoney(vo.getBsuperMoney().add(childVo.getBsuperMoney()));
									vo.setBtempMoney(vo.getBtempMoney().add(childVo.getBtempMoney()));
								}
							}
							//汇总单位分配的科目，下级单位没有，则初始化该科目的信息
							if (!itemMap.containsKey(vo.getItemCode())) {
								FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
								BeanUtils.copyNotNullProperties(vo, dtl);
								dtl.setbMoney(BigDecimal.ZERO);
								dtl.setBsuperMoney(BigDecimal.ZERO);
								dtl.setBtempMoney(BigDecimal.ZERO);
								listDtl.add(dtl);
							}
						}
					}
				}
				if (EmptyUtils.isEmptyList(listDtl)) {
					listDtl = new ArrayList<FbsBudgetDtlAbsVO>();
					for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
						FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
						BeanUtils.copyNotNullProperties(vo, dtl);
						dtl.setbMoney(BigDecimal.ZERO);
						dtl.setBsuperMoney(BigDecimal.ZERO);
						dtl.setBtempMoney(BigDecimal.ZERO);
						listDtl.add(dtl);
					}
				}
				childCorpMap.put(input.getCorpName(), listDtl);
			}
			//获取汇总金额
			String budgetAuthSql = SqlUtil.getInSql(budgetIds, "b.budget_id", true);
			List<FbsBudgetDtlAbsVO> listSumDtl = fbsBudgetDtlAbsService.collectByBudgetIds(fbsBudget.getTimeType(), budgetAuthSql);
			for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
				for (FbsBudgetDtlAbsVO childVo : listSumDtl) {
					if (childVo.getItemCode().equals(vo.getItemCode())) {
						//设置汇总金额
						vo.setbMoney(vo.getbMoney().add(childVo.getbMoney()));
						vo.setBsuperMoney(vo.getBsuperMoney().add(childVo.getBsuperMoney()));
						vo.setBtempMoney(vo.getBtempMoney().add(childVo.getBtempMoney()));
					}
				}
				if (!itemSumMap.containsKey(vo.getItemCode())) {
					FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
					BeanUtils.copyNotNullProperties(vo, dtl);
					dtl.setbMoney(BigDecimal.ZERO);
					dtl.setBsuperMoney(BigDecimal.ZERO);
					dtl.setBtempMoney(BigDecimal.ZERO);
					listSumDtl.add(dtl);
				}
			}
			if (EmptyUtils.isEmptyList(listSumDtl)) {
				listSumDtl = new ArrayList<FbsBudgetDtlAbsVO>();
				for (FbsBudgetDtlAbsVO vo : fbsBudget.getDtlList()) {
					FbsBudgetDtlAbsVO dtl = new FbsBudgetDtlAbsVO();
					BeanUtils.copyNotNullProperties(vo, dtl);
					dtl.setbMoney(BigDecimal.ZERO);
					dtl.setBsuperMoney(BigDecimal.ZERO);
					dtl.setBtempMoney(BigDecimal.ZERO);
					listSumDtl.add(dtl);
				}
			}
			// 组装计划基础信息
			this.setBudgetInfo(workbook, sheet, fbsBudget);
			// 组装标题头
			String[] head = new String[]{"单位", "币别", "汇率", "状态"};
			this.setHeadCell(workbook, sheet, head, 4);
			HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, false);
			HSSFCellStyle centerLockCellStyle =  getCenterLockCellStyle(workbook);
			HSSFCellStyle leftCellStyle = getLeftCellStyle(workbook, false);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightCellStyle = getRightCellStyle(workbook, false);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			if (!EmptyUtils.isEmptyList(childCorps)) {
				int index = 5;
				for (FbsBudgetInputPreVO vo : childCorps) {
					HSSFRow row = sheet.createRow(index);
					// 单位
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(leftCellStyle);
					cell.setCellValue("("+vo.getCorpCode()+")"+vo.getCorpName());
					// 币别
					HSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(centerCellStyle);
					cell1.setCellValue(vo.getCurName());
					// 汇率
					HSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(rightCellStyle);
					if(!EmptyUtils.isEmpty(vo.getExchangeRate())) {
						cell2.setCellValue(NumberUtil.formatDoubles(vo.getExchangeRate(), 4));
					}
					// 状态
					HSSFCell cell3 = row.createCell(3);
					cell3.setCellStyle(centerCellStyle);
					cell3.setCellValue(dictService.get("fbsBudget.status", vo.getStatus()));
					index++;
				}
			}
			//新增汇总金额
			FbsBudgetInputPreVO sumCorp = new FbsBudgetInputPreVO();
			sumCorp.setCorpName("汇总金额");
			childCorps.add(sumCorp);
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			headList.add("上报金额");
			headList.add("已追加金额(已通过)");
			headList.add("新追加金额(未通过)");
			int periodStartCol = 3;
			String[]  headDtl= headList.toArray(new String[]{});
			setHeadbudgetCur(workbook, sheet, headDtl, 5+childCorps.size(), childCorps.size()*3, childCorps, periodStartCol);
			if (!EmptyUtils.isEmptyList(fbsBudget.getDtlList())) {
				int index = 6 + childCorps.size()+1;
				int num = 1;
				for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
					HSSFRow row = sheet.createRow(index);
					// 序号
					HSSFCell cell1 = row.createCell(0);
					if(CommonConstant.YES.equals(dtl.getIsWrite())){
						cell1.setCellStyle(centerLockCellStyle);
					}else {
						cell1.setCellStyle(centerLockWriteCellStyle);
					}
					cell1.setCellValue(num);
					// 科目编号
					HSSFCell cell2 = row.createCell(1);
					if(CommonConstant.YES.equals(dtl.getIsWrite())) {
						cell2.setCellStyle(leftLockCellStyle);
					}else{
						cell2.setCellStyle(leftLockWriteCellStyle);
					}
					cell2.setCellValue(dtl.getItemCode());
					// 科目名称
					HSSFCell cell3 = row.createCell(2);
					if(CommonConstant.YES.equals(dtl.getIsWrite())) {
						cell3.setCellStyle(leftLockCellStyle);
					}else{
						cell3.setCellStyle(leftLockWriteCellStyle);
					}
					cell3.setCellValue(dtl.getItemName());
					// 预算金额
					HSSFCellStyle moneyStyle = null;
					if (CommonConstant.YES.equalsIgnoreCase(dtl.getIsWrite())) {
						moneyStyle = rightCellStyle;
					} else {
						moneyStyle = rightLockCellStyle;
					}
					//上报金额
					for(int k=1; k<=3;k++){
						for (int i=0; i<childCorps.size();i++) {
							//获取明细
							List<FbsBudgetDtlAbsVO> listDtl = null;
							if(!"汇总金额".equals(childCorps.get(i).getCorpName())) {
								listDtl = childCorpMap.get(childCorps.get(i).getCorpName());
							}else{
								listDtl = listSumDtl;
							}
							int col = periodStartCol + (k-1) * childCorps.size();
							// 上报金额
							HSSFCell cellMoney = row.createCell(col + i);
							cellMoney.setCellStyle(moneyStyle);
							if (!EmptyUtils.isEmptyList(listDtl)) {
								for (FbsBudgetDtlAbsVO vo : listDtl) {
									if (vo.getItemCode().equals(dtl.getItemCode())) {
										if(k==1) {
											cellMoney.setCellValue(NumberUtil.formatNumeric(vo.getbMoney()));
										}else if(k==2){
											cellMoney.setCellValue(NumberUtil.formatNumeric(vo.getBsuperMoney()));
										}else if(k==3){
											cellMoney.setCellValue(NumberUtil.formatNumeric(vo.getBtempMoney()));
										}
										break;
									}
								}
							}
						}
					}
					index++;
					num++;
				}
			}
			workbook.write(os);
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
	 * 组装计划基础信息
	 *
	 * @param workbook
	 * @param sheet
	 * @param fbsBudget
	 * @throws Exception
	 */
	public void setBudgetInfo(HSSFWorkbook workbook, HSSFSheet sheet, FbsBudgetVO fbsBudget) throws Exception {
		HSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook, true);
		HSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook, true);
		HSSFRow rowCorp = sheet.createRow(0);
		// 单位
		HSSFCell cell = rowCorp.createCell(0);
		cell.setCellStyle(rightLockCellStyle);
		cell.setCellValue("单位");
		HSSFCell cell1 = rowCorp.createCell(1);
		cell1.setCellStyle(leftLockCellStyle);
		cell1.setCellValue(fbsBudget.getCorpName());
		// 计划周期类型
		HSSFCell cell2 = rowCorp.createCell(2);
		cell2.setCellStyle(rightLockCellStyle);
		cell2.setCellValue("计划周期类型");
		HSSFCell cell3 = rowCorp.createCell(3);
		cell3.setCellStyle(leftLockCellStyle);
		cell3.setCellValue(getTimeTypeName(fbsBudget.getTimeType()));
		HSSFRow rowDate = sheet.createRow(1);
		// 计划开始时间
		HSSFCell cellDate = rowDate.createCell(0);
		cellDate.setCellStyle(rightLockCellStyle);
		cellDate.setCellValue("计划开始时间");
		HSSFCell cellDate1 = rowDate.createCell(1);
		cellDate1.setCellStyle(leftLockCellStyle);
		cellDate1.setCellValue(DateUtils.formatDate(fbsBudget.getBeginDate()));
		// 计划结束时间
		HSSFCell cellDate2 = rowDate.createCell(2);
		cellDate2.setCellStyle(rightLockCellStyle);
		cellDate2.setCellValue("计划结束时间");
		HSSFCell cellDate3 = rowDate.createCell(3);
		cellDate3.setCellStyle(leftLockCellStyle);
		cellDate3.setCellValue(DateUtils.formatDate(fbsBudget.getEndDate()));
		HSSFRow rowCur = sheet.createRow(2);
		// 币别
		HSSFCell cellCur = rowCur.createCell(0);
		cellCur.setCellStyle(rightLockCellStyle);
		cellCur.setCellValue("币别");
		HSSFCell cellCur1 = rowCur.createCell(1);
		cellCur1.setCellStyle(leftLockCellStyle);
		cellCur1.setCellValue(fbsBudget.getCurName());
		HSSFCell cellCur2 = rowCur.createCell(2);
		cellCur2.setCellStyle(rightLockCellStyle);
		cellCur2.setCellValue("");
		HSSFCell cellCur3 = rowCur.createCell(3);
		cellCur3.setCellStyle(leftLockCellStyle);
		cellCur3.setCellValue("");
	}
	/**
	 * 设置预算分解标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head           标题集合
	 * @param rowTitle       标题行
	 * @param childCorps     单位
	 * @param periodStartCol 周期起始列
	 */
	public void setHeadbudgetCur(HSSFWorkbook workbook, HSSFSheet sheet, String[] head, int rowTitle, int maxPeriod,  List<FbsBudgetInputPreVO> childCorps, int periodStartCol) {
		HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, true);
		// 第一行标题
		HSSFRow row = sheet.createRow(rowTitle);
		for (int i = 0; i < head.length; i++) {
			if (i < periodStartCol) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(head[i]);
				cell.setCellStyle(centerCellStyle);
			}else{
				int col = periodStartCol + (i - periodStartCol) * childCorps.size();
				HSSFCell cell = row.createCell(col);
				cell.setCellValue(head[i]);
				for (int j = 1; j < childCorps.size(); j++) {
					row.createCell(col + j);
				}
			}
		}
		for (Cell cell : row) {
			cell.setCellStyle(centerCellStyle);
		}
		// 第二行标题
		HSSFRow row1 = sheet.createRow(rowTitle + 1);
		row1.createCell(0);
		row1.createCell(1);
		row1.createCell(2);
		for (int i = 0; i < 3; i++) {
			int col = periodStartCol + i * childCorps.size();
			for (int j = 0; j < childCorps.size(); j++) {
				HSSFCell cell = row1.createCell(col+j);
				cell.setCellValue(childCorps.get(j).getCorpName());
			}
		}
		for (Cell cell : row1) {
			cell.setCellStyle(centerCellStyle);
		}
		//CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
		//合并行
		for (int i = 0; i < 3; i++) {
			CellRangeAddress region = new CellRangeAddress(rowTitle, rowTitle + 1, i, i);
			setRegionStyle(sheet, region, centerCellStyle);
			sheet.addMergedRegion(region);
		}
		//合并列
		int firstCol = 3;
		for (int i = 0; i < 3; i++) {
			CellRangeAddress region = new CellRangeAddress(rowTitle, rowTitle, firstCol, firstCol + childCorps.size() -1);
			setRegionStyle(sheet, region, centerCellStyle);
			sheet.addMergedRegion(region);
			firstCol += childCorps.size();
		}
	}
	/**
	 * 设置预算分解标题
	 *
	 * @param workbook
	 * @param sheet
	 * @param head           标题集合
	 * @param rowTitle       标题行
	 * @param maxPeriod      最大周期
	 * @param periodStartCol 周期起始列
	 */
	public void setHeadbudgetResolve(HSSFWorkbook workbook, HSSFSheet sheet, String[] head, int rowTitle, int maxPeriod, int periodStartCol) {
		HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, true);
		// 第一行标题
		HSSFRow row = sheet.createRow(rowTitle);
		for (int i = 0; i < head.length; i++) {
			if (i < periodStartCol) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(head[i]);
			} else {
				int col = periodStartCol + (i - periodStartCol) * 3;
				HSSFCell cell = row.createCell(col);
				cell.setCellValue(head[i]);
				row.createCell(col + 1);
				row.createCell(col + 2);
			}
		}
		for (Cell cell : row) {
			cell.setCellStyle(centerCellStyle);
		}
		// 第二行标题
		HSSFRow row1 = sheet.createRow(rowTitle + 1);
		row1.createCell(0);
		row1.createCell(1);
		row1.createCell(2);
		row1.createCell(3);
		for (int i = 0; i < maxPeriod; i++) {
			int col = periodStartCol + i * 3;
			HSSFCell cell = row1.createCell(col);
			cell.setCellValue("上报金额");
			cell.setCellStyle(getCenterCellStyle(workbook, true));
			HSSFCell cell1 = row1.createCell(col + 1);
			cell1.setCellValue("已追加金额");
			HSSFCell cell2 = row1.createCell(col + 2);
			cell2.setCellValue("新追加金额");
		}
		for (Cell cell : row1) {
			cell.setCellStyle(centerCellStyle);
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
		CellRangeAddress region1 = new CellRangeAddress(4, 5, 0, 0);
		setRegionStyle(sheet, region1, centerCellStyle);
		sheet.addMergedRegion(region1);
		CellRangeAddress region2 = new CellRangeAddress(4, 5, 1, 1);
		setRegionStyle(sheet, region2, centerCellStyle);
		sheet.addMergedRegion(region2);
		CellRangeAddress region3 = new CellRangeAddress(4, 5, 2, 2);
		setRegionStyle(sheet, region3, centerCellStyle);
		sheet.addMergedRegion(region3);
		CellRangeAddress region4 = new CellRangeAddress(4, 5, 3, 3);
		setRegionStyle(sheet, region4, centerCellStyle);
		sheet.addMergedRegion(region4);
		int firstCol = 4;
		for (int i = 0; i < maxPeriod; i++) {
			CellRangeAddress region5 = new CellRangeAddress(4, 4, firstCol, firstCol + 2);
			setRegionStyle(sheet, region5, centerCellStyle);
			sheet.addMergedRegion(region5);
			firstCol += 3;
		}
	}
	/**
	 * 设置预算分解标题（不包含追加）
	 *
	 * @param workbook
	 * @param sheet
	 * @param head           标题集合
	 * @param rowTitle       标题行
	 * @param maxPeriod      最大周期
	 * @param periodStartCol 周期起始列
	 */
	public void setHeadbudgetResolveInput(HSSFWorkbook workbook, HSSFSheet sheet, String[] head, int rowTitle, int maxPeriod, int periodStartCol) {
		HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, true);
		// 第一行标题
		HSSFRow row = sheet.createRow(rowTitle);
		for (int i = 0; i < head.length; i++) {
			if (i < periodStartCol) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(head[i]);
			} else {
				int col = periodStartCol + (i - periodStartCol) * 1;
				HSSFCell cell = row.createCell(col);
				cell.setCellValue(head[i]);
			}
		}
		for (Cell cell : row) {
			cell.setCellStyle(centerCellStyle);
		}
		// 第二行标题
		HSSFRow row1 = sheet.createRow(rowTitle + 1);
		row1.createCell(0);
		row1.createCell(1);
		row1.createCell(2);
		row1.createCell(3);
		for (int i = 0; i < maxPeriod; i++) {
			int col = periodStartCol + i * 1;
			HSSFCell cell = row1.createCell(col);
			cell.setCellValue("上报金额");
			cell.setCellStyle(getCenterCellStyle(workbook, true));
		}
		for (Cell cell : row1) {
			cell.setCellStyle(centerCellStyle);
		}
		//  CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
		CellRangeAddress region1 = new CellRangeAddress(4, 5, 0, 0);
		setRegionStyle(sheet, region1, centerCellStyle);
		sheet.addMergedRegion(region1);
		CellRangeAddress region2 = new CellRangeAddress(4, 5, 1, 1);
		setRegionStyle(sheet, region2, centerCellStyle);
		sheet.addMergedRegion(region2);
		CellRangeAddress region3 = new CellRangeAddress(4, 5, 2, 2);
		setRegionStyle(sheet, region3, centerCellStyle);
		sheet.addMergedRegion(region3);
		CellRangeAddress region4 = new CellRangeAddress(4, 5, 3, 3);
		setRegionStyle(sheet, region4, centerCellStyle);
		sheet.addMergedRegion(region4);
	}
	/**
	 * 设置合并单元格的边框
	 *
	 * @param sheet
	 * @param region
	 * @param cs
	 */
	private void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) {
		for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
			HSSFRow row = sheet.getRow(i);
			if (region.getFirstColumn() != region.getLastColumn()) {
				for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
					HSSFCell cell = row.getCell((short) j);
					cell.setCellStyle(cs);
				}
			}
		}
	}
	/**
	 * 导出预算月滚动模式模板
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void exportBudgetRollMonth(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target, ICurrentUser user) {
		// 组装数据
		Map<String, Object> rollMonthMap = fbsBudgetRollMonthService.getRollMonthBudget(id, user);
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			HSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook, false);
			HSSFCellStyle leftLockCellStyle = getLeftCellStyle(workbook, false);
			HSSFCellStyle rightLockCellStyle = getRightCellStyle(workbook, false);
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			List<String> months = (List<String>) rollMonthMap.get("months");
			Date showDate = fbsBudget.getBeginDate();
			showDate = DateUtils.addMonth(showDate,months.size()-1);
			FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(showDate,fbsBudget.getTimeType());
			// 组装计划基础信息
			this.setBudgetInfoRoll(workbook, sheet, id,fbsBudgetPeriodVO.getEndDate());
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			for (String month : months) {
				headList.add(month + "月");
			}
			String[] head = headList.toArray(new String[]{});
			this.setHeadCell(workbook, sheet, head, 4);
			CellRangeAddress region1 = new CellRangeAddress(4, 5, 0, 0);
			setRegionStyle(sheet, region1, centerLockCellStyle);
			sheet.addMergedRegion(region1);
			CellRangeAddress region2 = new CellRangeAddress(4, 5, 1, 1);
			setRegionStyle(sheet, region2, centerLockCellStyle);
			sheet.addMergedRegion(region2);
			CellRangeAddress region3 = new CellRangeAddress(4, 5, 2, 2);
			setRegionStyle(sheet, region3, centerLockCellStyle);
			sheet.addMergedRegion(region3);
			// 第二行标题
			HSSFRow row1 = sheet.createRow(5);
			row1.createCell(0);
			row1.createCell(1);
			row1.createCell(2);
			for (int i = 0; i < months.size(); i++) {
				HSSFCell cell = row1.createCell(3 + i);
				cell.setCellValue("上报金额");
			}
			for (Cell cell : row1) {
				cell.setCellStyle(centerLockCellStyle);
			}
			// 组装数据
			List<FbsBudgetRollMonthVO> rollMonthList = (List<FbsBudgetRollMonthVO>) rollMonthMap.get("rollMonthList");
			if (!EmptyUtils.isEmpty(rollMonthMap)) {
				int index = 6;
                int num = 1;
				for (FbsBudgetRollMonthVO vo : rollMonthList) {
					HSSFRow row = sheet.createRow(index);
					// 序号
					HSSFCell cell = row.createCell(0);
					cell.setCellStyle(centerLockCellStyle);
					cell.setCellValue(num);
					// 科目编号
					HSSFCell cell1 = row.createCell(1);
					cell1.setCellStyle(leftLockCellStyle);
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					HSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(leftLockCellStyle);
					cell2.setCellValue(vo.getItemName());
					for(int i = 0;i<vo.getbMoney().size();i++){
						HSSFCell cell3 = row.createCell(i+3);
						cell3.setCellStyle(rightLockCellStyle);
						BigDecimal bMoney = EmptyUtils.isEmpty(vo.getbMoney().get(i)) ? BigDecimal.ZERO : vo.getbMoney().get(i);
						cell3.setCellValue(NumberUtil.formatNumeric(bMoney));
					}
					index++;
                    num++;
				}
			}
			workbook.write(os);
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
	 * 下载预算月滚动模板
	 *
	 * @param request
	 * @param response
	 * @param id   ID
	 * @param timeType 预算类型
	 */
	public void downloadBudgetRollMonth(HttpServletRequest request, HttpServletResponse response, String id, String timeType, Date beginDate, String target) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			//月滚动模式
			int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
			//是否可以跨年
			boolean isSpanYear = false;
			//是否有年计划
			String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
			//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
			if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
					|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
				isSpanYear = true;
			}
			int month = DateUtils.getMonth(beginDate);
			//保存月份
			List<String> months = new ArrayList<String>();
			for (int i = 1; i <= mimodeRoll; i++) {
				if(!isSpanYear) {
					if (month <= 12) {
						months.add(month + "");
						month++;
					}
				}else{
					months.add(month + "");
					month++;
					if(month >12){
						month = 1;
					}
				}
			}
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			// 组装标题头
			for (String m : months) {
				headList.add(m + "月");
			}
			String[] head = headList.toArray(new String[]{});
			this.setHeadCell(workbook, sheet, head, 2);
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 3;
				int rowNum = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					HSSFRow row = sheet.createRow(index);
					// 序号
					HSSFCell cell1 = row.createCell(0);
					cell1.setCellStyle(centerLockCellStyle);
					cell1.setCellValue(rowNum++);
					// 科目编号
					HSSFCell cell2 = row.createCell(1);
					cell2.setCellStyle(centerLockCellStyle);
					cell2.setCellValue(vo.getItemCode());
					// 科目名称
					HSSFCell cell3 = row.createCell(2);
					cell3.setCellStyle(centerLockCellStyle);
					cell3.setCellValue(vo.getItemName());
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
					for (int i = 0; i < months.size(); i++) {
						// 预算金额
						HSSFCell cellMoney = row.createCell(3 + i);
						if (!EmptyUtils.isEmpty(fbsItemVO) && "1".equalsIgnoreCase(fbsItemVO.getIsWrite())) {
							cellMoney.setCellStyle(rightUnLockCellStyle);
						} else {
							cellMoney.setCellStyle(rightLockCellStyle);
						}
						cellMoney.setCellValue("0.00");
					}
					index++;
				}
				// 设置最后行
				this.setLastCell(workbook, sheet, dtlList.size());
			}
			workbook.write(os);
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
	 * 下载计划明细录入模式模板
	 *
	 * @param request
	 * @param response
	 * @param target
	 */
	public void downloadFbsBudgetDetail(HttpServletRequest request, HttpServletResponse response, String target, String itemCode, String itemName) {
		String fileName = "计划明细录入";
		String codedFileName = fileName;
        codedFileName= modifyFileNameOfV1(request,codedFileName);
		FbsItem fbsItem = fbsItemService.getFbsItem(itemCode);
		if(!EmptyUtils.isEmpty(fbsItem)){
			itemName = fbsItem.getItemName();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装标题头
			String moneyName = BudgetConstant.BUDGET_TARGET_INPUT.equalsIgnoreCase(target) ? "*上报金额" : "*追加金额";
			String dtlCol = iConfigService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
			dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
			String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目");
			headList.add("科目名称");
			headList.add(moneyName);
			/*headList.add("*币别");*/
			headList.add("*归属日期");
			for (int i = 0; i < dtlCols.length; i++) {
				headList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 0);
			HSSFFont redFont = workbook.createFont();
			redFont.setColor(IndexedColors.RED.getIndex());
			HSSFCell itemCodeCell = sheet.getRow(0).getCell(1);
			HSSFRichTextString richString = new HSSFRichTextString("*科目");
			richString.applyFont(0, 1, redFont);
			itemCodeCell.setCellValue(richString);
			HSSFCell moneyNameCell = sheet.getRow(0).getCell(3);
			richString = new HSSFRichTextString(moneyName);
			richString.applyFont(0, 1, redFont);
			moneyNameCell.setCellValue(richString);
			/*HSSFCell curCell = sheet.getRow(0).getCell(4);
			richString = new HSSFRichTextString("*币别");
			richString.applyFont(0, 1, redFont);
			curCell.setCellValue(richString);*/
			HSSFCell vestingDateCell = sheet.getRow(0).getCell(4);
			richString = new HSSFRichTextString("*归属日期");
			richString.applyFont(0, 1, redFont);
			vestingDateCell.setCellValue(richString);
			HSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook, true);
			HSSFRow row = sheet.createRow(1);
			HSSFCell cell = row.createCell(0);
			cell.setCellStyle(centerLockCellStyle);
			cell.setCellValue(1);
			HSSFCell cell1 = row.createCell(1);
			cell1.setCellStyle(centerLockCellStyle);
			cell1.setCellValue(itemCode);
			HSSFCell cell2 = row.createCell(2);
			cell2.setCellStyle(centerLockCellStyle);
			cell2.setCellValue(itemName);
			HSSFCell cell3 = row.createCell(3);
			cell3.setCellStyle(centerLockCellStyle);
			/*HSSFCell cell4 = row.createCell(4);
			cell4.setCellStyle(centerLockCellStyle);
			cell4.setCellValue("人民币");*/
			for (int i = 0; i <= dtlCols.length; i++) {
				HSSFCell cell5 = row.createCell(i + 4);
				cell5.setCellStyle(centerLockCellStyle);
			}
			workbook.write(os);
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
	 * 下载预算明细导入模板
	 *
	 * @param request
	 * @param response
	 * @param id       预算ID
	 * @param timeType 预算类型
	 */
	public void downloadFbsBudgetDetailByInput(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target) {
		String fileName = getFileName(timeType, target);
        String codedFileName = BudgetConstant.BUDGET_TARGET_INPUT.equalsIgnoreCase(target) ? "资金计划上报明细导入" : "资金计划追加明细导入";
        codedFileName= modifyFileNameOfV1(request,codedFileName);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			int moneyColumn = 3;
			// 组装标题头
			String moneyName = BudgetConstant.BUDGET_TARGET_INPUT.equalsIgnoreCase(target) ? "*上报金额" : "*追加金额";
			String dtlCol = iConfigService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
			dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
			String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("*科目");
			headList.add("科目名称");
			headList.add(moneyName);
			/*headList.add("*币别");*/
			headList.add("*归属日期");
			for (int i = 0; i < dtlCols.length; i++) {
				headList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 0);
			HSSFFont redFont = workbook.createFont();
			redFont.setColor(IndexedColors.RED.getIndex());
			HSSFCell itemCodeCell = sheet.getRow(0).getCell(1);
			HSSFRichTextString richString = new HSSFRichTextString("*科目");
			richString.applyFont(0, 1, redFont);
			itemCodeCell.setCellValue(richString);
			HSSFCell moneyNameCell = sheet.getRow(0).getCell(3);
			richString = new HSSFRichTextString(moneyName);
			richString.applyFont(0, 1, redFont);
			moneyNameCell.setCellValue(richString);
			HSSFCell vestingDateCell = sheet.getRow(0).getCell(4);
			richString = new HSSFRichTextString("*归属日期");
			richString.applyFont(0, 1, redFont);
			vestingDateCell.setCellValue(richString);
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 1;
				HSSFCellStyle centerLockCellStyle = getCenterCellStyle(workbook, false);
				HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
				int rowNum = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					//可以输入明细的科目
					if(CommonConstant.NO.equals(vo.getIsWrite()) || CommonConstant.YES.equals(vo.getIsBeginPeriod())
							|| CommonConstant.YES.equals(vo.getIsEndPeriod())) {
						continue;
					}
					HSSFRow row = sheet.createRow(index);
					// 序号
					HSSFCell cell1 = row.createCell(0);
					cell1.setCellStyle(centerLockCellStyle);
					cell1.setCellValue(rowNum++);
					// 科目编号
					HSSFCell cell2 = row.createCell(1);
					cell2.setCellStyle(centerLockCellStyle);
					cell2.setCellValue(vo.getItemCode());
					// 科目名称
					HSSFCell cell3 = row.createCell(2);
					cell3.setCellStyle(centerLockCellStyle);
					cell3.setCellValue(vo.getItemName());
					// 预算金额
					HSSFCell cell4 = row.createCell(moneyColumn);
					cell4.setCellStyle(rightUnLockCellStyle);
					for (int i = 0; i <= dtlCols.length; i++) {
						HSSFCell cell5 = row.createCell(i + 4);
						cell5.setCellStyle(centerLockCellStyle);
					}
					index++;
				}
			}
			workbook.write(os);
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


    private String modifyFileNameOfV1(HttpServletRequest request,String codedFileName) {
        try {
            if (request.getRequestURL().toString().contains("v1")) {
                codedFileName = URLEncoder.encode(codedFileName, "UTF8");
            } else {
                if (ServletUtils.isIE(request)) {
                    codedFileName = URLEncoder.encode(codedFileName, "UTF8");
                } else {
                    codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedFileName;
    }
	/**
	 * 导出计划明细录入模式模板
	 *
	 * @param request
	 * @param response
	 * @param target
	 */
	public void exportBudgetDetail(HttpServletRequest request, HttpServletResponse response, String target, String budgetId, String detailId) {
		String fileName = "计划明细录入";
		String codedFileName = fileName;
        codedFileName= modifyFileNameOfV1(request,codedFileName);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装标题头
			List<String> dtlColList = new ArrayList<String>();
			List<String> dtlColValueList = new ArrayList<String>();
			String dtlCol = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOL).getValue();//从参数读到需求显示的列
			dtlCol = dtlCol.replace("[", "").replace("]", "").replaceAll("/\\s+/g", "");
			String[] dtlCols = dtlCol.split(CommonConstant.COMMA_SPLIT);
			for (int i = 0; i < dtlCols.length; i++) {
				if(StringUtils.isNotBlank(dtlCols[i])) {
					dtlColList.add(dictService.get(BudgetConstant.BUDGET_DETAILCOL_DICT, dtlCols[i]));
					dtlColValueList.add(dtlCols[i]);
				}
			}
			List<String> headList = new ArrayList<>();
			headList.add("计划科目");
			headList.add("上报金额");
			/*headList.add("折合人民币");*/
			headList.add("已追加金额");
			//headList.add("折合人民币");
			if ("add".equals(target)) {
				headList.add("追加金额");
				//headList.add("折合人民币");
			}
			//headList.add("币别");
			headList.add("归属日期");
			for (int i = 0; i < dtlCols.length; i++) {
				if(StringUtils.isNotBlank(dtlCols[i])) {
					headList.add(dtlColList.get(i));
				}
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 0);
			List<FbsBudgetDetailVO> list = this.fbsBudgetDetailService.getDetailList(budgetId, detailId);
			HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, false);
			HSSFCellStyle rightCellStyle = getRightCellStyle(workbook, false);
			HSSFCellStyle leftCellStyle = getLeftCellStyle(workbook, false);
			int rowIndex = 1;
			for (FbsBudgetDetailVO vo : list) {
				HSSFRow row = sheet.createRow(rowIndex++);
				int col = 0;
				HSSFCell cell = row.createCell(col++);
				cell.setCellStyle(leftCellStyle);
				cell.setCellValue("(" + vo.getItemCode() + ") " + vo.getItemName());
				cell = row.createCell(col++);
				cell.setCellStyle(rightCellStyle);
				cell.setCellValue(NumberUtil.formatNumeric(vo.getBMoney()));
				cell = row.createCell(col++);
				cell.setCellStyle(rightCellStyle);
				cell.setCellValue(NumberUtil.formatNumeric(vo.getNatualMoney()));
				cell = row.createCell(col++);
				cell.setCellStyle(rightCellStyle);
				cell.setCellValue(NumberUtil.formatNumeric(vo.getBsuperMoney()));
				cell = row.createCell(col++);
				cell.setCellStyle(rightCellStyle);
				cell.setCellValue(NumberUtil.formatNumeric(vo.getBsuperNatualMoney()));
				if ("add".equals(target)) {
					cell = row.createCell(col++);
					cell.setCellStyle(rightCellStyle);
					cell.setCellValue(NumberUtil.formatNumeric(vo.getBtempMoney()));
					cell = row.createCell(col++);
					cell.setCellStyle(rightCellStyle);
					cell.setCellValue(NumberUtil.formatNumeric(vo.getBtempNatualMoney()));
				}
				cell = row.createCell(col++);
				cell.setCellStyle(centerCellStyle);
				cell.setCellValue(vo.getCurName());
				cell = row.createCell(col++);
				cell.setCellStyle(centerCellStyle);
				cell.setCellValue(DateUtils.format(vo.getVestingDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
				for (String dtl : dtlColValueList) {
					// 结算类型
					if ("settlType".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(centerCellStyle);
						cell.setCellValue(EmptyUtils.isBlank(vo.getSettlType()) ? "" : dictService.get("settlType", vo.getSettlType()));
					}
					// 单位/部门
					if ("corpName".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(leftCellStyle);
						cell.setCellValue(vo.getCorpName());
					}
					// 用途
					if ("purpose".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(leftCellStyle);
						cell.setCellValue(vo.getPurpose());
					}
					// 期望支付日期
					if ("wishPayday".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(centerCellStyle);
						cell.setCellValue(vo.getPurpose());
						cell.setCellValue(EmptyUtils.isEmpty(vo.getWishPayday()) ? "" : DateUtils.formatDate(vo.getWishPayday()));
					}
					// 付款账户
					if ("payerAcc".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(leftCellStyle);
						cell.setCellValue(vo.getPurpose());
						cell.setCellValue(vo.getPayerAcc());
					}
					// 收款账户名
					if ("payeeAccName".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(leftCellStyle);
						cell.setCellValue(vo.getPurpose());
						cell.setCellValue(vo.getPayeeAccName());
					}
					// 收款账户
					if ("payeeAcc".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(leftCellStyle);
						cell.setCellValue(vo.getPurpose());
						cell.setCellValue(vo.getPayeeAcc());
					}
					// 收款开户行
					if ("payeeBank".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(leftCellStyle);
						cell.setCellValue(vo.getPurpose());
						cell.setCellValue(vo.getPayeeBank());
					}
					// 合同号
					if ("contract".equals(dtl)) {
						cell = row.createCell(col++);
						cell.setCellStyle(leftCellStyle);
						cell.setCellValue(vo.getPurpose());
						cell.setCellValue(vo.getContract());
					}
				}
			}
			workbook.write(os);
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
	 * 下载计划实际差异分析表
	 *
	 * @param request
	 * @param response
	 * @param id
	 */
	public void exportBudgetFact(HttpServletRequest request, HttpServletResponse response, String id) {
		String fileName = "计划实际差异分析";
		String codedFileName = fileName;
        codedFileName= modifyFileNameOfV1(request,codedFileName);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("科目");
			headList.add("科目名称");
			headList.add("收付属性");
			headList.add("计划金额");
			headList.add("实际金额");
			headList.add("差异金额");
			headList.add("差异比率（%）");
			headList.add("状态");
			headList.add("差异原因");
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 4);
			//组装数据
			FbsBudgetVO fbsBudget = fbsBudgetService.getBudget(id);
			BigDecimal incomeTotal = BigDecimal.ZERO;//计划收入总额
			BigDecimal payTotal = BigDecimal.ZERO;//计划支出总额
			BigDecimal factIncomeTotal = BigDecimal.ZERO;//实际收入总额
			BigDecimal factPayTotal = BigDecimal.ZERO;//实际支出总额
			String lowerLmit = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_LOWERLMIT).getValue();//计划差异原因分析金额下限
			BigDecimal lowerLmitVal = BigDecimal.ZERO;
			if (StringUtils.isNotEmpty(lowerLmit)) {
				lowerLmitVal = new BigDecimal(lowerLmit);
			}
			int index = 5;
			HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, false);
			HSSFCellStyle leftCellStyle = getLeftCellStyle(workbook, false);
			HSSFCellStyle rightCellStyle = getRightCellStyle(workbook, false);
			for (FbsBudgetDtlAbsVO dtl : fbsBudget.getDtlList()) {
				dtl.setbMoney(dtl.getbMoney().add(dtl.getBsuperMoney()));//实际金额 = 上报金额 + 已追加金额
				if (dtl.getAnalyseStatus() == null) {
					dtl.setFactMoney(fbsBudgetProcessService.collectAlreadyProcessMoney(fbsBudget.getBeginDate(), fbsBudget.getEndDate(), dtl.getItemCode(), fbsBudget.getCorpId()));
					dtl.setAnalyseStatus(BudgetConstant.ANALYSE_STATUE_TEMP);
				} else {
					if (dtl.getFactMoney() == null) {
						dtl.setFactMoney(BigDecimal.ZERO);
					}
				}
				dtl.setDiffMoney(dtl.getbMoney().subtract(dtl.getFactMoney()));//差异金额 = 计划金额 - 实际金额
				//差异比率=差异金额/计划金额
				if (dtl.getDiffMoney().compareTo(BigDecimal.ZERO) > 0 && dtl.getbMoney().compareTo(BigDecimal.ZERO) > 0) {
					dtl.setDiffRatio(dtl.getDiffMoney().divide(dtl.getbMoney(), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
				} else {
					dtl.setDiffRatio(BigDecimal.ZERO);
				}
				if (dtl.getDiffMoney().abs().compareTo(lowerLmitVal) >= 0) {
					dtl.setShowDiffReason(true);
				} else {
					dtl.setShowDiffReason(false);
				}
				if (dtl.getItem().getSign() == 1) {
					if (BudgetConstant.ITEM_CODE_200.equals(dtl.getItemCode())) {
						incomeTotal = incomeTotal.add(dtl.getbMoney());
						factIncomeTotal = factIncomeTotal.add(dtl.getFactMoney());
					}
				} else {
					if (BudgetConstant.ITEM_CODE_400.equals(dtl.getItemCode())) {
						payTotal = payTotal.add(dtl.getbMoney());
						factPayTotal = factPayTotal.add(dtl.getFactMoney());
					}
				}
				HSSFRow row = sheet.createRow(index);
				// 科目
				HSSFCell cell1 = row.createCell(0);
				cell1.setCellStyle(leftCellStyle);
				cell1.setCellValue(dtl.getItemCode());
				// 科目名称
				HSSFCell cell2 = row.createCell(1);
				cell2.setCellStyle(leftCellStyle);
				cell2.setCellValue(dtl.getItemName());
				// 收付属性
				HSSFCell cell3 = row.createCell(2);
				cell3.setCellStyle(centerCellStyle);
				cell3.setCellValue(dtl.getItem().getSign() > 0 ? "收入" : "支出");
				// 计划金额
				HSSFCell cell4 = row.createCell(3);
				cell4.setCellStyle(rightCellStyle);
				cell4.setCellValue(NumberUtil.formatNumeric(dtl.getbMoney()));
				//实际金额
				HSSFCell cell5 = row.createCell(4);
				cell5.setCellStyle(rightCellStyle);
				cell5.setCellValue(NumberUtil.formatNumeric(dtl.getFactMoney()));
				//差异金额
				HSSFCell cell6 = row.createCell(5);
				cell6.setCellStyle(rightCellStyle);
				cell6.setCellValue(NumberUtil.formatNumeric(dtl.getDiffMoney()));
				///差异比率（%）
				HSSFCell cell7 = row.createCell(6);
				cell7.setCellStyle(rightCellStyle);
				cell7.setCellValue(NumberUtil.formatNumeric(dtl.getDiffRatio()));
				//状态
				HSSFCell cell8 = row.createCell(7);
				cell8.setCellStyle(leftCellStyle);
				if (!dtl.getShowDiffReason()) {
					cell8.setCellValue("");
				} else{
					cell8.setCellValue(dictService.get("fbsBudget.analyseStatus", dtl.getAnalyseStatus()));
				}
				//差异原因
				HSSFCell cell9 = row.createCell(8);
				cell9.setCellStyle(leftCellStyle);
				cell9.setCellValue(dtl.getDiffReason());
				index++;
			}
			HSSFRow rowCorp = sheet.createRow(0);
			// 单位
			HSSFCell cell = rowCorp.createCell(0);
			cell.setCellStyle(rightCellStyle);
			cell.setCellValue("单位");
			HSSFCell cell1 = rowCorp.createCell(1);
			cell1.setCellStyle(leftCellStyle);
			cell1.setCellValue(fbsBudget.getCorpName());
			// 日期
			HSSFCell cell2 = rowCorp.createCell(2);
			cell2.setCellStyle(rightCellStyle);
			cell2.setCellValue("日期");
			HSSFCell cell3 = rowCorp.createCell(3);
			cell3.setCellStyle(leftCellStyle);
			cell3.setCellValue(DateUtils.formatDate(fbsBudget.getBeginDate()) + "至" + DateUtils.formatDate(fbsBudget.getEndDate()));
			HSSFRow rowPlan = sheet.createRow(1);
			// 计划收入总额
			HSSFCell cell4 = rowPlan.createCell(0);
			cell4.setCellStyle(rightCellStyle);
			cell4.setCellValue("计划收入总额");
			HSSFCell cell5 = rowPlan.createCell(1);
			cell5.setCellStyle(rightCellStyle);
			cell5.setCellValue(NumberUtil.formatNumeric(incomeTotal));
			// 计划支出总额
			HSSFCell cell6 = rowPlan.createCell(2);
			cell6.setCellStyle(rightCellStyle);
			cell6.setCellValue("计划支出总额");
			HSSFCell cell7 = rowPlan.createCell(3);
			cell7.setCellStyle(rightCellStyle);
			cell7.setCellValue(NumberUtil.formatNumeric(payTotal));
			HSSFRow rowFact = sheet.createRow(2);
			// 实际收入总额
			HSSFCell cell8 = rowFact.createCell(0);
			cell8.setCellStyle(rightCellStyle);
			cell8.setCellValue("实际收入总额");
			HSSFCell cell9 = rowFact.createCell(1);
			cell9.setCellStyle(rightCellStyle);
			cell9.setCellValue(NumberUtil.formatNumeric(factIncomeTotal));
			// 实际支出总额
			HSSFCell cell10 = rowFact.createCell(2);
			cell10.setCellStyle(rightCellStyle);
			cell10.setCellValue("实际支出总额");
			HSSFCell cell11 = rowFact.createCell(3);
			cell11.setCellStyle(rightCellStyle);
			cell11.setCellValue(NumberUtil.formatNumeric(factPayTotal));
			workbook.write(os);
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
	 * 下载销售回款差异分析表
	 *
	 * @param request
	 * @param response
	 * @param corpId
	 */
	public void exportBudgetSales(HttpServletRequest request, HttpServletResponse response, String corpId, String queryDate) {
		String fileName = "销售回款差异分析";
		String codedFileName = fileName;
        codedFileName= modifyFileNameOfV1(request,codedFileName);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("单位名称");
			headList.add("日期");
			headList.add("客户名称");
			headList.add("回款方式");
			headList.add("计划回款金额");
			headList.add("实际回款金额");
			headList.add("计划与实际差异金额");
			headList.add("差异总额");
			headList.add("差异比率（%）");
			headList.add("状态");
			headList.add("差异原因");
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 0);
			//组装数据
			BigDecimal planTotal = BigDecimal.ZERO;//计划总额
			BigDecimal factTotal = BigDecimal.ZERO;//实际总额
			BigDecimal diffTotal = BigDecimal.ZERO;//差异总额
			String ratioLimit = iConfigService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_RATIOLIMIT).getValue();//销售回款原因分析比率限制
			BigDecimal ratioLimitVal = BigDecimal.ZERO;
			if (StringUtils.isNotEmpty(ratioLimit)) {
				ratioLimitVal = new BigDecimal(ratioLimit).multiply(new BigDecimal(100));
			}
			List<FbsSalesReturnAnalyseVO> dltList = new ArrayList<>();
			Date beginDate = DateUtils.parseDate(queryDate + "-01");
			List<FbsSalesReturnAnalyse> list = fbsSalesReturnAnalyseService.listByCorpId(corpId, beginDate, null);
			int index = 1;
			HSSFCellStyle centerCellStyle = getCenterCellStyle(workbook, false);
			HSSFCellStyle leftCellStyle = getLeftCellStyle(workbook, false);
			HSSFCellStyle rightCellStyle = getRightCellStyle(workbook, false);
			for (FbsSalesReturnAnalyse sales : list) {
				FbsSalesReturnAnalyseVO vo = new FbsSalesReturnAnalyseVO();
				BeanUtils.copyNotNullProperties(sales, vo);
				vo.setDiffCashMoney(vo.getPlanCashMoney().subtract(vo.getFactCashMoney()));
				vo.setDiffBillMoney(vo.getPlanBillMoney().subtract(vo.getFactBillMoney()));
				vo.setDiffMoney(vo.getDiffCashMoney().add(vo.getDiffBillMoney()));
				vo.setDiffRatio(vo.getDiffMoney().divide((vo.getPlanCashMoney().add(vo.getPlanBillMoney())), 6, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
				String corpName = corpService.getCorpName(vo.getCorpId());
				vo.setCorpName(corpName);
				if (vo.getDiffRatio().abs().compareTo(ratioLimitVal) >= 0) {
					vo.setShowDiffReason(true);
				} else {
					vo.setShowDiffReason(false);
				}
				dltList.add(vo);
				planTotal = planTotal.add(vo.getPlanCashMoney()).add(vo.getPlanBillMoney());
				factTotal = factTotal.add(vo.getFactCashMoney()).add(vo.getFactBillMoney());
				diffTotal = diffTotal.add(vo.getDiffMoney());
				for (int i = 0; i < 2; i++) {
					HSSFRow row = sheet.createRow(index);
					// 单位名称
					HSSFCell cell1 = row.createCell(0);
					cell1.setCellStyle(leftCellStyle);
					cell1.setCellValue(corpService.get(corpId).getName());
					// 日期
					HSSFCell cell2 = row.createCell(1);
					cell2.setCellStyle(centerCellStyle);
					cell2.setCellValue(queryDate);
					// 客户名称
					HSSFCell cell3 = row.createCell(2);
					cell3.setCellStyle(centerCellStyle);
					cell3.setCellValue(vo.getOppAccName());
					// 回款方式
					HSSFCell cell4 = row.createCell(3);
					cell4.setCellStyle(centerCellStyle);
					if (i == 0) {
						cell4.setCellValue("现款");
					} else {
						cell4.setCellValue("票据");
					}
					// 计划回款金额
					HSSFCell cell5 = row.createCell(4);
					cell5.setCellStyle(rightCellStyle);
					if (i == 0) {
						cell5.setCellValue(NumberUtil.formatNumeric(vo.getPlanCashMoney()));
					} else {
						cell5.setCellValue(NumberUtil.formatNumeric(vo.getPlanBillMoney()));
					}
					// 实际回款金额
					HSSFCell cell6 = row.createCell(5);
					cell6.setCellStyle(rightCellStyle);
					if (i == 0) {
						cell6.setCellValue(NumberUtil.formatNumeric(vo.getFactCashMoney()));
					} else {
						cell6.setCellValue(NumberUtil.formatNumeric(vo.getFactBillMoney()));
					}
					// 计划与实际差异金额
					HSSFCell cell7 = row.createCell(6);
					cell7.setCellStyle(rightCellStyle);
					if (i == 0) {
						cell7.setCellValue(NumberUtil.formatNumeric(vo.getDiffCashMoney()));
					} else {
						cell7.setCellValue(NumberUtil.formatNumeric(vo.getDiffBillMoney()));
					}
					// 差异总额
					HSSFCell cell8 = row.createCell(7);
					cell8.setCellStyle(rightCellStyle);
					cell8.setCellValue(NumberUtil.formatNumeric(vo.getDiffMoney()));
					// 差异比率（%）
					HSSFCell cell9 = row.createCell(8);
					cell9.setCellStyle(rightCellStyle);
					cell9.setCellValue(NumberUtil.formatNumeric(vo.getDiffRatio()));
					//状态
					HSSFCell cell10 = row.createCell(9);
					cell10.setCellStyle(centerCellStyle);
					if (!vo.getShowDiffReason()) {
						cell10.setCellValue("");
					} else{
						cell10.setCellValue(dictService.get("fbsBudget.analyseStatus", vo.getAnalyseStatus()));
					}
					// 差异原因
					HSSFCell cell11 = row.createCell(10);
					cell11.setCellStyle(leftCellStyle);
					cell11.setCellValue(vo.getDiffReason());
					index++;
				}
				//合并
				CellRangeAddress region1 = new CellRangeAddress(index - 2, index - 1, 0, 0);
				setRegionStyle(sheet, region1, centerCellStyle);
				sheet.addMergedRegion(region1);
				CellRangeAddress region2 = new CellRangeAddress(index - 2, index - 1, 1, 1);
				setRegionStyle(sheet, region2, centerCellStyle);
				sheet.addMergedRegion(region2);
				CellRangeAddress region3 = new CellRangeAddress(index - 2, index - 1, 2, 2);
				setRegionStyle(sheet, region3, centerCellStyle);
				sheet.addMergedRegion(region3);
				CellRangeAddress region4 = new CellRangeAddress(index - 2, index - 1, 7, 7);
				setRegionStyle(sheet, region4, centerCellStyle);
				sheet.addMergedRegion(region4);
				CellRangeAddress region5 = new CellRangeAddress(index - 2, index - 1, 8, 8);
				setRegionStyle(sheet, region5, centerCellStyle);
				sheet.addMergedRegion(region5);
				CellRangeAddress region6 = new CellRangeAddress(index - 2, index - 1, 9, 9);
				setRegionStyle(sheet, region6, centerCellStyle);
				sheet.addMergedRegion(region6);
				CellRangeAddress region7 = new CellRangeAddress(index - 2, index - 1, 10, 10);
				setRegionStyle(sheet, region7, centerCellStyle);
				sheet.addMergedRegion(region7);
			}
			HSSFRow rowTotal = sheet.createRow(index);
			// 合计
			HSSFCell cell = rowTotal.createCell(0);
			cell.setCellStyle(rightCellStyle);
			cell.setCellValue("合计");
			rowTotal.createCell(1);
			rowTotal.createCell(2);
			rowTotal.createCell(3);
			// 计划回款金额
			HSSFCell cell2 = rowTotal.createCell(4);
			cell2.setCellStyle(rightCellStyle);
			cell2.setCellValue(NumberUtil.formatNumeric(planTotal));
			// 实际回款金额
			HSSFCell cell3 = rowTotal.createCell(5);
			cell3.setCellStyle(rightCellStyle);
			cell3.setCellValue(NumberUtil.formatNumeric(factTotal));
			// 差异金额
			HSSFCell cell4 = rowTotal.createCell(6);
			cell4.setCellStyle(rightCellStyle);
			cell4.setCellValue(NumberUtil.formatNumeric(diffTotal));
			HSSFCell cell5 = rowTotal.createCell(7);
			cell5.setCellStyle(rightCellStyle);
			cell5.setCellValue(NumberUtil.formatNumeric(diffTotal));
			rowTotal.createCell(8);
			rowTotal.createCell(9);
			rowTotal.createCell(10);
			//合并
			CellRangeAddress regionTotal = new CellRangeAddress(index, index, 0, 3);
			setRegionStyle(sheet, regionTotal, rightCellStyle);
			sheet.addMergedRegion(regionTotal);
			CellRangeAddress regionNull = new CellRangeAddress(index, index, 8, 10);
			setRegionStyle(sheet, regionNull, rightCellStyle);
			sheet.addMergedRegion(regionNull);
			workbook.write(os);
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
	 * 下载月滚动+双月周计划上报模板
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @param target
	 * @param user
	 */
	public void downloadBudgetRollMonthWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target, ICurrentUser user) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			//月滚动模式
			int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
			//是否有年计划
			String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
			//mRollSubperiod-月滚动模式 滚动分解子周期
			int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
			//是否可以跨年
			boolean isSpanYear = false;
			//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
			if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
					|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
				isSpanYear = true;
			}
			int month = DateUtils.getMonth(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			List<String> months = new ArrayList<>();
			List<String> monthsHid = new ArrayList<>();
			for (int i = 1; i <= mimodeRoll; i++) {
				if(!isSpanYear) {
					if (month <= 12) {
						months.add(year+"年"+month + "月");
						monthsHid.add(month+"");
						month++;
					}
				}else{
					if(month>12) {
						month = month - 12;
						year++;
					}
					months.add(year+"年"+month + "月");
					monthsHid.add(month+"");
					month++;
				}
			}
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			Date beginDate = fbsBudget.getBeginDate();
			for (int i = 0;i<monthsHid.size();i++){
				headList.add(months.get(i));
			}
			for (int i = 0;i<monthsHid.size();i++){
				if(mRollSubperiod >i) {
					List<FbsBudgetPeriodVO> nextperiodVOList = fbsBudgetPeriodService.getWeekByMonth(beginDate);
					for (int j = 0; j < nextperiodVOList.size(); j++) {
						headList.add(months.get(i)+"第" + (j + 1) + "周");
					}
					beginDate = DateUtils.addMonth(beginDate,1);
				}
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 2);
			// 组装数据
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 3;
				int count = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
					int col = 0;
					HSSFRow row = sheet.createRow(index++);
					// 序号
					HSSFCell cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(centerLockCellStyle);
					}else{
						cell1.setCellStyle(centerLockWriteCellStyle);
					}
					cell1.setCellValue(count);
					// 科目编号
					cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemName());
					// 预算金额
					HSSFCellStyle moneyStyle = null;
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						moneyStyle = rightUnLockCellStyle;
					} else {
						moneyStyle = rightLockCellStyle;
					}
					beginDate = fbsBudget.getBeginDate();
					for (int i = 0; i < months.size(); i++) {
						cell1 = row.createCell(col++);
						if(i <mRollSubperiod){
							cell1.setCellStyle(rightLockCellStyle);
						}else{
							cell1.setCellStyle(moneyStyle);
						}
						cell1.setCellValue("0.00");
					}
					for(int i= 0;i<months.size();i++){
						if(mRollSubperiod >i){
							List<FbsBudgetPeriodVO> nextperiodVOList = fbsBudgetPeriodService.getWeekByMonth(beginDate);
							for (int j = 0; j < nextperiodVOList.size(); j++) {
								cell1 = row.createCell(col++);
								cell1.setCellStyle(moneyStyle);
								cell1.setCellValue("0.00");
							}
							beginDate = DateUtils.addMonth(beginDate,1);
						}
					}
					count++;
				}
				// 设置最后行
				this.setLastCell(workbook, sheet, dtlList.size());
			}
			workbook.write(os);
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
	 * 导出月滚动+双月周计划上报
	 *
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @param target
	 * @param user
	 */
	public void exportBudgetRollMonthWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target, ICurrentUser user) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			//月滚动模式
			int mimodeRoll = Integer.parseInt(configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_MIMODEROLL).getValue());
			//是否有年计划
			String ycrl = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_YCRL).getValue();
			//mRollSubperiod-月滚动模式 滚动分解子周期
			int mRollSubperiod = Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_MROLLSUBPERIOD));
			//是否可以跨年
			boolean isSpanYear = false;
			//是否可以跨年：未启用年计划 或者 启用了年计划但不控制子周期时支持跨年
			if (BudgetConstant.CONFIG_CRL_0.equalsIgnoreCase(ycrl)
					|| BudgetConstant.CONFIG_CRL_2.equals(ycrl)) {
				isSpanYear = true;
			}
			int month = DateUtils.getMonth(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			List<String> months = new ArrayList<>();
			List<String> monthsHid = new ArrayList<>();
			Date showDate = fbsBudget.getBeginDate();
			for (int i = 1; i <= mimodeRoll; i++) {
				if(!isSpanYear) {
					if (month <= 12) {
						months.add(year+"年"+month + "月");
						monthsHid.add(month+"");
						month++;
					}
				}else{
					if(month>12) {
						month = month - 12;
						year++;
					}
					months.add(year+"年"+month + "月");
					monthsHid.add(month+"");
					month++;
				}
				showDate = DateUtils.addMonth(showDate,1);
			}
			showDate = DateUtils.addMonth(showDate,-1);
			FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(showDate,fbsBudget.getTimeType());
			// 组装计划基础信息
			this.setBudgetInfoRoll(workbook, sheet, id,fbsBudgetPeriodVO.getEndDate());
			// 样式
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			List<FbsBudgetPeriodVO> periodVOList = fbsBudgetPeriodService.getWeekByMonth(fbsBudget.getBeginDate());
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			// 第二行标题
			List<String> headList1 = new ArrayList<>();
			headList1.add("");
			headList1.add("");
			headList1.add("");
			if(mRollSubperiod > 0 ) {
				for (int i = 0; i < periodVOList.size(); i++) {
					headList.add("第" + (i + 1) + "周");
					headList1.add("上报金额");
				}
			}
			Date beginDate = fbsBudget.getBeginDate();
			for (int i = 0;i<months.size();i++){
				headList.add(months.get(i));
				headList1.add("上报金额");
				beginDate = DateUtils.addMonth(beginDate,1);
				if(mRollSubperiod-1 >i){
					List<FbsBudgetPeriodVO> nextperiodVOList = fbsBudgetPeriodService.getWeekByMonth(beginDate);
					for (int j = 0; j < nextperiodVOList.size(); j++) {
						headList.add("第" + (j + 1) + "周");
						headList1.add("上报金额");
					}
				}
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 3);
			this.setHeadCell(workbook, sheet, headList1.toArray(new String[]{}), 4);
			CellRangeAddress region1 = new CellRangeAddress(3, 4, 0, 0);
			setRegionStyle(sheet, region1, centerLockCellStyle);
			sheet.addMergedRegion(region1);
			CellRangeAddress region2 = new CellRangeAddress(3, 4, 1, 1);
			setRegionStyle(sheet, region2, centerLockCellStyle);
			sheet.addMergedRegion(region2);
			CellRangeAddress region3 = new CellRangeAddress(3, 4, 2, 2);
			setRegionStyle(sheet, region3, centerLockCellStyle);
			sheet.addMergedRegion(region3);
			// 组装数据
			List<FbsBudgetRollMonthWeekVO> dtlList = fbsBudgetRollMonthWeekService.listRollMonthBudget(id, user);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 5;
				int count = 1;
				for (FbsBudgetRollMonthWeekVO vo : dtlList) {
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
					int col = 0;
					HSSFRow row = sheet.createRow(index++);
					// 序号
					HSSFCell cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(centerLockCellStyle);
					}else{
						cell1.setCellStyle(centerLockWriteCellStyle);
					}
					cell1.setCellValue(count);
					// 科目编号
					cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemName());
					// 预算金额
					HSSFCellStyle moneyStyle = null;
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						moneyStyle = rightUnLockCellStyle;
					} else {
						moneyStyle = rightLockCellStyle;
					}
					List<List<FbsBudgetDtlAbsVO>> childBudgetList = vo.getChildWeekBudget();
					for (int i = 0; i < months.size(); i++) {
						if (!EmptyUtils.isEmpty(childBudgetList)){
							if(childBudgetList.size() >i) {
								List<FbsBudgetDtlAbsVO> list = childBudgetList.get(i);
								for (int j = 0; j < list.size(); j++) {
									cell1 = row.createCell(col++);
									cell1.setCellStyle(moneyStyle);
									cell1.setCellValue(NumberUtil.formatNumeric(list.get(j).getbMoney()));
								}
							}
						}
						cell1 = row.createCell(col++);
						cell1.setCellStyle(moneyStyle);
						cell1.setCellValue(NumberUtil.formatNumeric(vo.getbMoney().get(i)));
					}
					count++;
				}
			}
			workbook.write(os);
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
	 * 下载季滚动计划上报模板
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @param target
	 * @param user
	 */
	public void downloadBudgetRollQuarter(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target, ICurrentUser user) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			//季计划滚动;
			int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
			//季计划滚动，分解月
			int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
			int quarter = DateUtils.getQuarterOfYear(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存季度
			List<String> quarters = new ArrayList<>();
			for (int i = 1; i <= qimodeRoll; i++) {
				String quarterShow = "";
				quarterShow +=year+"年"+quarter+"季度";
				quarters.add(quarterShow);
				quarter++;
				if(quarter>4){
					year++;
					quarter = 1;
				}
			}
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			Date beginDate = fbsBudget.getBeginDate();
			for (int i = 0;i<quarters.size();i++){
				headList.add(quarters.get(i));
				beginDate = DateUtils.addMonth(beginDate,3);
			}
			for(int k=0;qRollSubperiod>k;k++){
				for (int j = 0; j < 3; j++) {
					headList.add(quarters.get(k)+"第" + (j + 1) + "月");
				}
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 2);
			// 组装数据
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 3;
				int count = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
					int col = 0;
					HSSFRow row = sheet.createRow(index++);
					// 序号
					HSSFCell cell1 = row.createCell(col++);
					if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(centerLockCellStyle);
					}else{
						cell1.setCellStyle(centerLockWriteCellStyle);
					}
					cell1.setCellValue(count);
					// 科目编号
					cell1 = row.createCell(col++);
					if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					cell1 = row.createCell(col++);
					if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemName());
					// 预算金额
					HSSFCellStyle moneyStyle = null;
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						moneyStyle = rightUnLockCellStyle;
					} else {
						moneyStyle = rightLockCellStyle;
					}
					beginDate = fbsBudget.getBeginDate();
					for (int i = 0; i < quarters.size(); i++) {
						cell1 = row.createCell(col++);
						if(i <qRollSubperiod){
							cell1.setCellStyle(rightLockCellStyle);
						}else{
							cell1.setCellStyle(moneyStyle);
						}
						cell1.setCellValue("0.00");
						beginDate = DateUtils.addMonth(beginDate,3);
					}
					for(int k=0;qRollSubperiod>k;k++){
						for (int j = 0; j < 3; j++) {
							cell1 = row.createCell(col++);
							cell1.setCellStyle(moneyStyle);
							cell1.setCellValue("0.00");
						}
					}
					count++;
				}
				// 设置最后行
				this.setLastCell(workbook, sheet, dtlList.size());
			}
			workbook.write(os);
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
	 * 下载周滚动计划上报模板
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @param target
	 * @param user
	 */
	public void downloadBudgetRollWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target, ICurrentUser user) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(fileName);
			// 锁定表格
			sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			//周计划滚动;
			int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
			//周计划滚动，分解天
			int wRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
			Date weekBeginDate = fbsBudget.getBeginDate();
			List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
			List<String> weeksHid = new ArrayList<>();
            int thismonth = DateUtils.getMonth(weekBeginDate);
			for (int i = 1; i <= wimodeRoll; i++) {
				FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
				int week = DateUtils.getWeekOfMonth(weekBeginDate);
				int month = DateUtils.getMonth(weekBeginDate);
                if(month > thismonth){
                    week = week -1;
                }
				//保存周
				String weekShow = "";
				weekShow +=month+"月第"+week+"周";
				fbsBudgetPaternityVO.setPaternal(weekShow);
				weeks.add(fbsBudgetPaternityVO);
				weeksHid.add(week+"");
				weekBeginDate = DateUtils.addWeek(weekBeginDate,1);
			}
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			for (int i = 0;i<weeks.size();i++){
				headList.add(weeks.get(i).getPaternal());
			}
			weekBeginDate = fbsBudget.getBeginDate();
			for(int k=0;wRollSubperiod>k;k++){
				FbsBudgetPeriodVO fbsBudgetPeriodVO  = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(weekBeginDate,fbsBudget.getTimeType());
				//一周有多少天
				int daycount = DateUtils.dayDiffs(fbsBudgetPeriodVO.getEndDate(),fbsBudgetPeriodVO.getBeginDate())+1;
				for (int j = 0; j < daycount; j++) {
					headList.add(weeks.get(k).getPaternal()+"第" + (j+1) + "天");
				}
				weekBeginDate = DateUtils.addWeek(weekBeginDate,1);
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 2);
			// 组装数据
			List<FbsBudgetDtlAbsVO> dtlList = fbsBudgetDtlAbsService.listByBudgetId(id, timeType);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 3;
				int count = 1;
				for (FbsBudgetDtlAbsVO vo : dtlList) {
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
					int col = 0;
					HSSFRow row = sheet.createRow(index++);
					// 序号
					HSSFCell cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(centerLockCellStyle);
					}else{
						cell1.setCellStyle(centerLockWriteCellStyle);
					}
					cell1.setCellValue(count);
					// 科目编号
					cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemName());
					// 预算金额
					HSSFCellStyle moneyStyle = null;
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						moneyStyle = rightUnLockCellStyle;
					} else {
						moneyStyle = rightLockCellStyle;
					}
					for (int i = 0; i < weeks.size(); i++) {
						cell1 = row.createCell(col++);
						if(i <wRollSubperiod){
							cell1.setCellStyle(rightLockCellStyle);
						}else{
							cell1.setCellStyle(moneyStyle);
						}
						cell1.setCellValue("0.00");
					}
					weekBeginDate = fbsBudget.getBeginDate();
					for(int k=0;wRollSubperiod>k;k++){
						FbsBudgetPeriodVO fbsBudgetPeriodVO  = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(weekBeginDate,fbsBudget.getTimeType());
						//一周有多少天
						int daycount = DateUtils.dayDiffs(fbsBudgetPeriodVO.getEndDate(),fbsBudgetPeriodVO.getBeginDate())+1;
						for (int j = 0; j < daycount; j++) {
							cell1 = row.createCell(col++);
							cell1.setCellStyle(moneyStyle);
							cell1.setCellValue("0.00");
						}
						weekBeginDate = DateUtils.addWeek(weekBeginDate,1);
					}
					count++;
				}
				// 设置最后行
				this.setLastCell(workbook, sheet, dtlList.size());
			}
			workbook.write(os);
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
	 * 导入滚动模板
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ExcelImportDTO uploadRoll(HttpServletRequest request, HttpServletResponse response,ICurrentUser user) throws Exception {
		ExcelImportDTO result = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		Map<String, String[]> mapReq = new HashMap<String, String[]>();
		mapReq.putAll(request.getParameterMap());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					String code = request.getParameter("code");
					String titleRows = request.getParameter("titleRows");
					String headRows = request.getParameter("headRows");
					String endRows = request.getParameter("endRows");
					mapReq.remove("page");
					mapReq.remove("code");
					mapReq.remove("titleRows");
					mapReq.remove("headRows");
					mapReq.remove("endRows");
					ExcelImportDTO excelImportDTO = dealExcel(mapReq, user, file, titleRows, headRows, endRows, code);
					if (EmptyUtils.isEmpty(excelImportDTO)) {
						excelImportDTO = new ExcelImportDTO();
					}
					result = excelImportDTO;
				}
			}
		}
		return result;
	}
	/**
	 * 导出季滚动上报
	 * @param request
	 * @param response
	 * @param id
	 * @param timeType
	 * @param target
	 * @param user
	 */
	public void exportBudgetRollQuarter(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target, ICurrentUser user) {
		String fileName = getFileName(timeType, target);
		String codedFileName = getCodedFileName(request, timeType, target);
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
			//sheet.protectSheet("edit");
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(20);
			// 样式
			HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
			HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
			HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
			FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
			//季计划滚动;
			int qimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QIMODEROLL));
			//季计划滚动，分解月
			int qRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_QROLLSUBPERIOD));
			int quarter = DateUtils.getQuarterOfYear(fbsBudget.getBeginDate());
			int year = DateUtils.getYear(fbsBudget.getBeginDate());
			//保存季度
			List<String> quarters = new ArrayList<>();
			Date showDate = fbsBudget.getBeginDate();
			for (int i = 1; i <= qimodeRoll; i++) {
				String quarterShow = "";
				quarterShow +=year+"年"+quarter+"季度";
				quarters.add(quarterShow);
				quarter++;
				if(quarter>4){
					year++;
					quarter = 1;
				}
				showDate = DateUtils.addMonth(showDate,3);
			}
			showDate = DateUtils.addMonth(showDate,-3);
			FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(showDate,fbsBudget.getTimeType());
			// 组装计划基础信息
//			this.setBudgetInfoRoll(workbook, sheet, id,fbsBudgetPeriodVO.getEndDate());
			this.setBudgetInfo(workbook, sheet, id);
			// 组装标题头
			List<String> headList = new ArrayList<>();
			headList.add("序号");
			headList.add("科目编号");
			headList.add("科目名称");
			// 第二行标题
			List<String> headList1 = new ArrayList<>();
			headList1.add("");
			headList1.add("");
			headList1.add("");
			if(qRollSubperiod >0) {
				for (int i = 0; i < 3; i++) {
					headList.add("第" + (i + 1) + "月");
					headList1.add("上报金额");
				}
			}
			Date beginDate = fbsBudget.getBeginDate();
			for (int i = 0;i<quarters.size();i++){
				headList.add(quarters.get(i));
				headList1.add("上报金额");
				beginDate = DateUtils.addMonth(beginDate,3);
				if(qRollSubperiod-1 >i){
					for (int j = 0; j < 3; j++) {
						headList.add("第" + (j + 1) + "月");
						headList1.add("上报金额");
					}
				}
			}
			this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 3);
			this.setHeadCell(workbook, sheet, headList1.toArray(new String[]{}), 4);
			CellRangeAddress region1 = new CellRangeAddress(3, 4, 0, 0);
			setRegionStyle(sheet, region1, centerLockCellStyle);
			sheet.addMergedRegion(region1);
			CellRangeAddress region2 = new CellRangeAddress(3, 4, 1, 1);
			setRegionStyle(sheet, region2, centerLockCellStyle);
			sheet.addMergedRegion(region2);
			CellRangeAddress region3 = new CellRangeAddress(3, 4, 2, 2);
			setRegionStyle(sheet, region3, centerLockCellStyle);
			sheet.addMergedRegion(region3);
			// 组装数据
			List<FbsBudgetRollMonthWeekVO> dtlList = fbsBudgetRollQuarterService.listRollQuarterBudget(id, user);
			if (!EmptyUtils.isEmptyList(dtlList)) {
				int index = 5;
				int count = 1;
				for (FbsBudgetRollMonthWeekVO vo : dtlList) {
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
					int col = 0;
					HSSFRow row = sheet.createRow(index++);
					// 序号
					HSSFCell cell1 = row.createCell(col++);
					if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(centerLockCellStyle);
					}else{
						cell1.setCellStyle(centerLockWriteCellStyle);
					}
					cell1.setCellValue(count);
					// 科目编号
					cell1 = row.createCell(col++);
					if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemCode());
					// 科目名称
					cell1 = row.createCell(col++);
					if(CommonConstant.YES.equals(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
					cell1.setCellValue(vo.getItemName());
					// 预算金额
					HSSFCellStyle moneyStyle = null;
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						moneyStyle = rightUnLockCellStyle;
					} else {
						moneyStyle = rightLockCellStyle;
					}
					List<List<FbsBudgetDtlAbsVO>> childBudgetList = vo.getChildWeekBudget();
					for (int i = 0; i < quarters.size(); i++) {
						if (!EmptyUtils.isEmpty(childBudgetList)){
							if(childBudgetList.size() >i) {
								List<FbsBudgetDtlAbsVO> list = childBudgetList.get(i);
								for (int j = 0; j < list.size(); j++) {
									cell1 = row.createCell(col++);
									cell1.setCellStyle(moneyStyle);
									cell1.setCellValue(NumberUtil.formatNumeric(list.get(j).getbMoney()));
								}
							}
						}
						cell1 = row.createCell(col++);
						cell1.setCellStyle(moneyStyle);
						cell1.setCellValue(NumberUtil.formatNumeric(vo.getbMoney().get(i)));
					}
					count++;
				}
			}
			workbook.write(os);
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
     * 导出周滚动上报
     * @param request
     * @param response
     * @param id
     * @param timeType
     * @param target
     * @param user
     */
    public void exportBudgetRollWeek(HttpServletRequest request, HttpServletResponse response, String id, String timeType, String target, ICurrentUser user) {
        String fileName = getFileName(timeType, target);
        String codedFileName = getCodedFileName(request, timeType, target);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        if (request.getRequestURL().toString().contains("v1")) {
            response.setHeader("content-disposition", "attachment;filename*=UTF-8''" + codedFileName+ ".xls");
        } else {
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        }
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(fileName);
            // 锁定表格
            //sheet.protectSheet("edit");
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 样式
            HSSFCellStyle centerLockCellStyle = getCenterLockCellStyle(workbook);
			HSSFCellStyle leftLockCellStyle = getLeftLockCellStyle(workbook);
            HSSFCellStyle rightLockCellStyle = getRightLockCellStyle(workbook);
            HSSFCellStyle rightUnLockCellStyle = getRightCellStyle(workbook, false);
			//不可填写科目样式置灰
			HSSFCellStyle centerLockWriteCellStyle = getCenterLockWriteCellStyle(workbook);
			HSSFCellStyle leftLockWriteCellStyle = getLeftLockWriteCellStyle(workbook);
            FbsBudgetVO fbsBudget = this.fbsBudgetService.getBudget(id);
            //周计划滚动;
            int wimodeRoll =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WIMODEROLL));
            //周计划滚动，分解天
            int wRollSubperiod =  Integer.parseInt(fbsBudgetService.getInputMode(BudgetConstant.FBS_CONFIG_WROLLSUBPERIOD));
            Date weekBeginDate = fbsBudget.getBeginDate();
            List<FbsBudgetPaternityVO> weeks = new ArrayList<>();
            List<String> weeksHid = new ArrayList<>();
            int thismonth = DateUtils.getMonth(weekBeginDate);
            Date showDate = fbsBudget.getBeginDate();
            for (int i = 1; i <= wimodeRoll; i++) {
                FbsBudgetPaternityVO fbsBudgetPaternityVO = new FbsBudgetPaternityVO();
                int week = DateUtils.getWeekOfMonth(weekBeginDate);
                int month = DateUtils.getMonth(weekBeginDate);
                showDate = DateUtils.addWeek(showDate,1);
                if(month > thismonth){
                    week = week -1;
					showDate = DateUtils.addDay(showDate,-1);
                }
                //保存周
                String weekShow = "";
                weekShow +=month+"月第"+week+"周";
                fbsBudgetPaternityVO.setPaternal(weekShow);
                weeks.add(fbsBudgetPaternityVO);
                weeksHid.add(week+"");
                weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
            }
            showDate = DateUtils.addWeek(showDate,-1);
            FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(showDate,fbsBudget.getTimeType());
            // 组装计划基础信息
//            this.setBudgetInfoRoll(workbook, sheet, id,fbsBudgetPeriodVO.getEndDate());
            this.setBudgetInfo(workbook, sheet, id);
            // 组装标题头
            List<String> headList = new ArrayList<>();
            headList.add("序号");
            headList.add("科目编号");
            headList.add("科目名称");
            // 第二行标题
            List<String> headList1 = new ArrayList<>();
            headList1.add("");
            headList1.add("");
            headList1.add("");
			weekBeginDate = fbsBudget.getBeginDate();
            for (int i = 0;i<weeks.size();i++){
				FbsBudgetPeriodVO fbsBudgetPeriodVO1 = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(weekBeginDate, fbsBudget.getTimeType());
				//一周有多少天
				int weekcount = DateUtils.dayDiffs(fbsBudgetPeriodVO1.getEndDate(), fbsBudgetPeriodVO1.getBeginDate()) + 1;
                if(wRollSubperiod >i){
                    for (int j = 0; j < weekcount; j++) {
                        headList.add("第" + (j + 1) + "天");
                        headList1.add("上报金额");
                    }
                }
				headList.add(weeks.get(i).getPaternal());
				headList1.add("上报金额");
				weekBeginDate = DateUtils.addWeek(weekBeginDate, 1);
            }
            this.setHeadCell(workbook, sheet, headList.toArray(new String[]{}), 3);
            this.setHeadCell(workbook, sheet, headList1.toArray(new String[]{}), 4);
            CellRangeAddress region1 = new CellRangeAddress(3, 4, 0, 0);
            setRegionStyle(sheet, region1, centerLockCellStyle);
            sheet.addMergedRegion(region1);
            CellRangeAddress region2 = new CellRangeAddress(3, 4, 1, 1);
            setRegionStyle(sheet, region2, centerLockCellStyle);
            sheet.addMergedRegion(region2);
            CellRangeAddress region3 = new CellRangeAddress(3, 4, 2, 2);
            setRegionStyle(sheet, region3, centerLockCellStyle);
            sheet.addMergedRegion(region3);
			// 组装数据
			List<FbsBudgetRollMonthWeekVO> dtlList = fbsBudgetRollWeekService.listRollQuarterBudget(id, user);
            if (!EmptyUtils.isEmptyList(dtlList)) {
                int index = 5;
                int count = 1;
                for (FbsBudgetRollMonthWeekVO vo : dtlList) {
					FbsItemVO fbsItemVO = fbsItemService.getFbsItemVO(null, vo.getItemCode());
                    int col = 0;
                    HSSFRow row = sheet.createRow(index++);
                    // 序号
                    HSSFCell cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(centerLockCellStyle);
					}else{
						cell1.setCellStyle(centerLockWriteCellStyle);
					}
                    cell1.setCellValue(count);
                    // 科目编号
                    cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
                    cell1.setCellValue(vo.getItemCode());
                    // 科目名称
                    cell1 = row.createCell(col++);
					if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
						cell1.setCellStyle(leftLockCellStyle);
					}else{
						cell1.setCellStyle(leftLockWriteCellStyle);
					}
                    cell1.setCellValue(vo.getItemName());
                    // 预算金额
                    HSSFCellStyle moneyStyle = null;
                    if (CommonConstant.YES.equalsIgnoreCase(fbsItemVO.getIsWrite())) {
                        moneyStyle = rightUnLockCellStyle;
                    } else {
                        moneyStyle = rightLockCellStyle;
                    }
                    List<List<FbsBudgetDtlAbsVO>> childBudgetList = vo.getChildWeekBudget();
                    for (int i = 0; i < weeks.size(); i++) {
                        if (!EmptyUtils.isEmpty(childBudgetList)){
                            if(childBudgetList.size() >i) {
                                List<FbsBudgetDtlAbsVO> list = childBudgetList.get(i);
                                for (int j = 0; j < list.size(); j++) {
                                    cell1 = row.createCell(col++);
                                    cell1.setCellStyle(moneyStyle);
                                    cell1.setCellValue(NumberUtil.formatNumeric(list.get(j).getbMoney()));
                                }
                            }
                        }
                        cell1 = row.createCell(col++);
                        cell1.setCellStyle(moneyStyle);
                        cell1.setCellValue(NumberUtil.formatNumeric(vo.getbMoney().get(i)));
                    }
                    count++;
                }
            }
            workbook.write(os);
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
	 * @param mapReq
	 * @param user
	 * @param file
	 * @param titleRows
	 * @param headRows
	 * @param endRows
	 * @param code
	 * @return
	 */
	private ExcelImportDTO dealExcel(Map<String, String[]> mapReq, ICurrentUser user, MultipartFile file, String titleRows, String headRows, String endRows, String code) {
		Map<String, Object> alignment = new HashMap<String, Object>();
		Map<String, Object> split = new HashMap<String, Object>();
		Object serviceObject = null;
		ImportParams params = new ImportParams();
		params.setTitleRows(Integer.parseInt(titleRows));
		params.setHeadRows(Integer.parseInt(headRows));
		params.setEndRows(Integer.parseInt(endRows));
		params.setNeedVerfiy(true);//需要检验
		params.setReadTitleKey(true);//需要返回对应关系
		params.setCurrentUser(user);
		params.setParams(mapReq);
		try {
			IResourceTemplateEnum resourceTemplateEnum = ResourceTemplateEnum.getResourceTemplate(code);
			Class aClass = null;
			try {
				try {
					aClass = Class.forName(resourceTemplateEnum.getReflex());
					//获取验证类
					String beanName = (String) Reflections.invokeGetter(aClass.newInstance(), "beanName");
					if (beanName != null) {
						serviceObject = ApplicationContextUtil.getContext().getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
						if (serviceObject instanceof IExcelVerifyHandler) {
							params.setVerifyHanlder((IExcelVerifyHandler) serviceObject);
						}
						if (serviceObject instanceof IUserExcelVerifyHandler) {
							//IUserExcelVerifyHandler s = (IUserExcelVerifyHandler)serviceObject;
							params.setUserVerifyHandler((IUserExcelVerifyHandler) serviceObject);
						}
					}
					dealAlignSplit(aClass, alignment, split);
				} catch (Exception e) {
					logger.error("导入excel模板解析异常：{}", e);
					throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_PARSING);
				}
			} catch (Exception e) {
				logger.error("导入excel模板实例化类异常：{}", e);
				//aClass = Map.class;
				throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_INSTANCE);
			}
			ExcelImportResult result = ExcelImportUtil.importExcelVerify(file.getInputStream(), aClass, params);
			ExcelImportDTO dto = excelVerify(serviceObject, result.getList(), params, alignment, split, null, "1");
			List<List<String>> dataList = this.read(result.getWorkbook());
			List<Map<String,String>> list = new ArrayList<>();
			List<String> keyList = dataList.get(1);
			Map<String,String> keyMap = dto.getMap();
			for(int i = 3,count =1;i<keyList.size();i++,count++){
				keyMap.put(keyList.get(i),"bMoney"+count);
			}
			for(int i=2;i<dataList.size();i++){
				List<String> childList = dataList.get(i);
				Map<String,String> map = new HashMap<>();
				for(int j = 1;j<keyList.size();j++) {
					String data = childList.get(j);
					if(NumberUtils.isNumber(data) && j>1){
						data = NumberUtil.formatNumeric(NumberUtil.formatNumeric(data));
					}
					map.put(keyMap.get(keyList.get(j)), data);
				}
				list.add(map);
			}
			List<Object> objectList = new ArrayList<>();
			for(int i=0;i<list.size();i++){
				objectList.add(list.get(i));
			}
			dto.setList(objectList);
			return dto;
		} catch (Exception e) {
			logger.error("上传异常：{}", e);
			throw new BusinessException(ExcelMessageEnum.EXCEL_MESSAGE_ERROR_TEMPLATE_UPLOAD);
		}
	}
	/**
	 * 封装 处理对齐和货币格式化方法
	 *
	 * @param aClss
	 * @param alignment
	 * @param split
	 */
	private void dealAlignSplit(Class<?> aClss, Map<String, Object> alignment, Map<String, Object> split) {
		try {
			List<Field> annoFiles = Reflections.fields(aClss, Excel.class);
			if (!EmptyUtils.isEmptyList(annoFiles)) {
				for (Field field : annoFiles) {
					Excel excel = field.getAnnotation(Excel.class);
					HorizontalAlignment horizontalAlignment = excel.align();
					if (!HorizontalAlignment.GENERAL.equals(horizontalAlignment)) {
						alignment.put(field.getName(), horizontalAlignment);
					}
					if (!SplitTypeEnum.GENERAL.equals(excel.splitType())) {
						split.put(field.getName(), excel.splitType().toString().toLowerCase());
					}
				}
			}
		} catch (Exception e) {
			logger.error("excel模板解析属性格式化异常：{}", e);
		}
	}
	/**
	 * 校验方法
	 *
	 * @param serviceObject
	 * @param list
	 * @param params
	 * @param alignment
	 * @param split
	 * @param header
	 * @param type          类型 1-简单模板  2-复杂模板 3-自定义模板
	 * @return
	 * @throws Exception
	 */
	private ExcelImportDTO excelVerify(Object serviceObject, List list, ImportParams params
			, Map<String, Object> alignment, Map<String, Object> split, LinkedHashMap<String, LinkedHashMap<String, String>> header, String type) throws Exception {
		if (EmptyUtils.isEmptyList(list)) {
			return new ExcelImportDTO();
		}
		//获取上报单位
		HashMap<String, Object> paramMap = (HashMap<String, Object>)params.getParams();
		String[] corpId = (String[])paramMap.get("corpId");
		List<FbsCorpItem> corpItems = fbsCorpItemService.getItemCode(corpId[0]);
		HashMap<String, FbsCorpItem> itemMap = new HashMap<>();
		if (!EmptyUtils.isEmptyList(corpItems)) {
			for (FbsCorpItem item:corpItems) {
				itemMap.put(item.getItemCode(), item);
			}
		}
		List successList = new ArrayList();
		List errorList = new ArrayList();
		for (FbsBudgetImportVO o : (List<FbsBudgetImportVO>)list) {
			if (!EmptyUtils.isBlank(o.getItemCode())) {
				if (itemMap.containsKey(o.getItemCode())) {
					successList.add(o);
				} else {
					FbsItemVO item = this.fbsItemService.getFbsItemVO(null, o.getItemCode());
					if (item != null && CommonConstant.YES.equals(item.getValidSign())) {
						o.setErrorMsg("budget.message.itemcode.notassign");
					} else {
						o.setErrorMsg("budget.message.itemcode.notexist");
					}
					errorList.add(o);
				}
			} else {
				o.setErrorMsg("nis.assign.message.itemcode.null");
				errorList.add(o);
			}
		}
		return new ExcelImportDTO(list,successList, errorList, params.getTitleKey(), alignment, split);
	}
	public List<List<String>> read(Workbook wb) {
		List<List<String>> dataList = new ArrayList<List<String>>();
		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(0);
		/** 得到Excel的总行数 */
		int rowCount = sheet.getPhysicalNumberOfRows();
		/** 循环Excel的行 */
		for (int i = 0; i <= rowCount; i++) {
			Row row = sheet.getRow(i);
			if (null == row) {
				continue;
			}
			List<String> rowList = new ArrayList<String>();
			/** 循环Excel的列 */
			for (int c = 0; c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				String cellValue = "";
				if (cell != null) {
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							cellValue = cell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (org.apache.poi.ss.usermodel.DateUtil
									.isCellDateFormatted(cell)) {
								SimpleDateFormat fmt = new SimpleDateFormat(
										"yyyy-MM-dd");
								cellValue = fmt.format(cell.getDateCellValue());
							} else {
								Double d = cell.getNumericCellValue();
								cellValue = d.toString();
								// 解决1234.0 去掉后面的.0
								if (null != cellValue
										&& !"".equals(cellValue.trim())) {
									String[] item = cellValue.split("[.]");
									if (1 < item.length && "0".equals(item[1])) {
										cellValue = item[0];
									}
								}
							}
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							cellValue = String.valueOf(cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_BLANK:
							cellValue = cell.getStringCellValue();
							break;
					case Cell.CELL_TYPE_ERROR:
							cellValue = "";
						break;
						case Cell.CELL_TYPE_FORMULA:
							cellValue = cell.getStringCellValue();
						break;
						default:
							cellValue = cell.getStringCellValue();
					}
				}
				// System.out.print(cellValue + "\t");
				rowList.add(cellValue);
			}
			dataList.add(rowList);
		}
		return dataList;
	}
}
