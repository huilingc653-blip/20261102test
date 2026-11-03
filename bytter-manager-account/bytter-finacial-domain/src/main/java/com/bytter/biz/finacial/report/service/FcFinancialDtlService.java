package com.bytter.biz.finacial.report.service;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.vo.FcAccbookDtlVO;
import com.bytter.biz.finacial.accbook.vo.VFcAccbookAndBlnedsVO;
import com.bytter.biz.finacial.report.dao.FcFinancialDtlDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@Service("fcFinancialDtlService")
@Transactional
public class FcFinancialDtlService extends BaseService<VFcAccbookAndBlnedsVO,String> {
	
	@Autowired
	private FcFinancialDtlDao fcFinancialDtlDao;
	@Autowired
	private FcAccbookDao fcAccbookDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService iDictService;
	public BaseDao<VFcAccbookAndBlnedsVO, String> getEntityDao() {
		return fcFinancialDtlDao;
	}
	
	/**
	 * 功能介绍 : 流水账明细。<br>
	 * <b>日期：</b>2018年1月8日<br>
	 * @version 1.0
	 * @author yangl
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryFcAccbookAndBlnedsData(DataTablesInput input, String resource, ICurrentUser user) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		DataTablesOutput output = fcFinancialDtlDao.listOutputData(input, corpAuth);
		return setOutputPageData(output);
	}
	
	/**
	 * 功能介绍 : 设置凭证分录信息。<br>
	 * <b>日期：</b>2018年1月8日<br>
	 * @version 1.0
	 * @author yangl 
	 */
	public DataTablesOutput setOutputPageData(DataTablesOutput output) {
		List<VFcAccbookAndBlnedsVO> data = output.getData();
		if (null == data) {
			return output;
		}
		for(VFcAccbookAndBlnedsVO vo:data){
			//设置明细集合
			List<FcAccbookDtlVO> dtlDTOs = fcAccbookDao.getPageDtlsByBookId(vo.getId(),false);
			vo.setAccbookDtl(dtlDTOs);
			vo.setDtlSize(dtlDTOs.size());
		}
		return output;
	}
	/**
	 * 导出
	 * @param input
	 * @param resource
	 * @param userId
	 * @return
	 */
	public HSSFWorkbook createExcelContent(DataTablesInput input, String resource, String userId){
		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
		List<VFcAccbookAndBlnedsVO> list = fcFinancialDtlDao.listExcel(input,corpAuth);//查询结果
		List res = setBooksData(list);
		// 创建工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		String content = "流水账查询表";
		// 创建sheet
		HSSFSheet sheet = workbook.createSheet(content);
		// 创建行
		for (int i = 0; i < 2; i++) {
			sheet.createRow(i);
		}
		// 创建Excel表头
		this.createExcelHeader(sheet);
		// 创建Excel表中的内容
		this.createExcelBody(workbook,sheet,res);
		return workbook;
	}
	/**
	 * 创建表头
	 */
	public void createExcelHeader(HSSFSheet sheet){
		// 第一行
		HSSFRow row1 = sheet.getRow(0);
		// 第二行
		HSSFRow row2 = sheet.getRow(1);
		row1.createCell(0).setCellValue("基本信息");
		row1.createCell(5).setCellValue("会计分录信息");
		row1.createCell(13).setCellValue("其他信息");
		row2.createCell(0).setCellValue("记账日期");
		row2.createCell(1).setCellValue("起息日期");
		row2.createCell(2).setCellValue("业务种类");
		row2.createCell(3).setCellValue("流水号");
		row2.createCell(4).setCellValue("凭证号");
		row2.createCell(5).setCellValue("凭证种类");
		row2.createCell(6).setCellValue("账号");
		row2.createCell(7).setCellValue("账户名称");
		row2.createCell(8).setCellValue("摘要");
		row2.createCell(9).setCellValue("原始凭证号");
		row2.createCell(10).setCellValue("借方发生额");
		row2.createCell(11).setCellValue("贷方发生额");
		row2.createCell(12).setCellValue("余额");
		row2.createCell(13).setCellValue("记账员代码");
		row2.createCell(14).setCellValue("复核员代码");
		row2.createCell(15).setCellValue("加急标志");
		row2.createCell(16).setCellValue("凭证状态");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 12));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 16));
	}
	/**
	 * 创建Excel内容样式
	 * @param workbook
	 * @return
	 */
	private HSSFCellStyle createExcelBodyStyle(HSSFWorkbook workbook) {
		HSSFCellStyle cs = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体"); //字体
		font.setFontHeightInPoints((short) 12);
		cs.setFont(font);      //列的字体
		cs.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
		cs.setAlignment(HorizontalAlignment.CENTER);        //水平居中对齐
		return cs;
	}
	/**
	 * 创建Excel表内容
	 */
	private void createExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<VFcAccbookAndBlnedsVO> list) {
		// Excel表格内容样式
		HSSFCellStyle cs = this.createExcelBodyStyle(workbook);
		// 指向所在行
		int index = 2;
		int dIndex = 0;
		for (VFcAccbookAndBlnedsVO book : list){
			// 创建行
			//HSSFRow row = sheet.getRow(index);
			HSSFRow row = sheet.createRow(index);
			row.createCell(0).setCellValue(DateUtils.formatDate(book.getTallyDate()));
			row.createCell(1).setCellValue(DateUtils.formatDate(book.getInterestStartDate()));
			row.createCell(2).setCellValue(book.getRaceName());
			row.createCell(3).setCellValue(book.getOrderNo());
			row.createCell(4).setCellValue(book.getVoucherNo());
			row.createCell(5).setCellValue(book.getVoucherName());
			row.createCell(13).setCellValue(book.getOperatorCode());
			row.createCell(14).setCellValue(book.getCheckerCode());
			String emergencySign = iDictService.get("Accbook.EmergencySign",book.getEmergencySign());
			row.createCell(15).setCellValue(emergencySign);
			String voucherState = iDictService.get("Finacial.VoucherState",book.getVoucherState());
			row.createCell(16).setCellValue(voucherState);
			int endRow = book.getAccbookDtl().size()-1;
			endRow = index+endRow;
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 13, 13));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 14, 14));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 15, 15));
			sheet.addMergedRegion(new CellRangeAddress(index, endRow , 16, 16));
			for (FcAccbookDtlVO dtl : book.getAccbookDtl()) {
				if(0 != dIndex) {
					HSSFRow rowDtl = sheet.createRow(index);
					rowDtl.createCell(6).setCellValue(dtl.getAccount());
					rowDtl.createCell(7).setCellValue(dtl.getAccName());
					rowDtl.createCell(8).setCellValue(dtl.getSummaryContent());
					rowDtl.createCell(9).setCellValue(dtl.getOrigVoucherNo());
					rowDtl.createCell(10).setCellValue(String.format("%.2f", dtl.getDebitMoney()));
					rowDtl.createCell(11).setCellValue(String.format("%.2f", dtl.getCreditMoney()));
					rowDtl.createCell(12).setCellValue(String.format("%.2f", dtl.getBalance()));
				}else {
					row.createCell(6).setCellValue(dtl.getAccount());
					row.createCell(7).setCellValue(dtl.getAccName());
					row.createCell(8).setCellValue(dtl.getSummaryContent());
					row.createCell(9).setCellValue(dtl.getOrigVoucherNo());
					row.createCell(10).setCellValue(String.format("%.2f", dtl.getDebitMoney()));
					row.createCell(11).setCellValue(String.format("%.2f", dtl.getCreditMoney()));
					row.createCell(12).setCellValue(String.format("%.2f", dtl.getBalance()));
					dIndex = 1;
				}
				index++;
			}
			dIndex = 0;
		}
	}
	/**
	 * 判断是否为ie浏览器
	 * @param request
	 * @return
	 */
	public boolean isIE(HttpServletRequest request) {
		return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
	}
	/**
	 * 功能介绍 : 设置凭证分录信息。<br>
	 * <b>日期：</b>2018年1月8日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public List<VFcAccbookAndBlnedsVO> setBooksData(List<VFcAccbookAndBlnedsVO> books) {
		if (null == books) {
			return new ArrayList<>();
		}
		for(VFcAccbookAndBlnedsVO vo:books){
			//设置明细集合
			List<FcAccbookDtlVO> dtlDTOs = fcAccbookDao.getPageDtlsByBookId(vo.getId(),false);
			vo.setAccbookDtl(dtlDTOs);
			vo.setDtlSize(dtlDTOs.size());
		}
		return books;
	}
}
