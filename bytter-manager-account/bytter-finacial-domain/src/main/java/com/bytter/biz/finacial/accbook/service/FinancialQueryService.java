package com.bytter.biz.finacial.accbook.service;
import com.bytter.biz.finacial.accbook.dao.FcAccbookBlnedDao;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.dao.FinancialQueryDao;
import com.bytter.biz.finacial.accbook.vo.FcAccbookDtlVO;
import com.bytter.biz.finacial.accbook.vo.VFcAccbookAndBlnedsVO;
import com.bytter.biz.finacial.closedeal.service.FcDayCloseService;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.IUserService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("financialQueryService")
@Transactional
public class FinancialQueryService extends BaseService<VFcAccbookAndBlnedsVO,String> {

	@Autowired
	private FinancialQueryDao financialQueryDao;
	@Autowired
	private FcAccbookDao fcAccbookDao;
	@Autowired
	private FcAccbookBlnedDao fcAccbookBlnedDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcCorpPoolService corpPoolService;
	@DubboReference(check = false)
	private IDictService iDictService;
	@Autowired(required = false)
	private FcDayCloseService dayCloseService;

	@DubboReference(check = false)
	private IFcVoucherTypeService iFcVoucherTypeService;

	@DubboReference(check = false)
	private IUserService iUserService;

	public BaseDao<VFcAccbookAndBlnedsVO, String> getEntityDao() {
		return financialQueryDao;
	}

	/**
	 * 功能介绍 : 查询记账凭证列表。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author yangl
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryFcAccbookAndBlnedsData(DataTablesInput input, String resource, String userId) {
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput output = financialQueryDao.listOutputData(input, corpAuthSql);
		return setOutputPageDataNew(output);
	}
	/**
	 * 待设置现金流项目列表
	 * @param input
	 * @param resource
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput listUpdateCashItemDataTable(DataTablesInput input, String resource, String userId) {
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput output = financialQueryDao.listUpdateCashItemDataTable(input, corpAuthSql);
		return setOutputPageData(output);
	}
	/**
	 * 功能介绍 : 设置凭证分录信息。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public DataTablesOutput setOutputPageData(DataTablesOutput output) {
		List<VFcAccbookAndBlnedsVO> data = output.getData();
		if (null == data || EmptyUtils.isEmptyList(data)) {
			return output;
		}
		List<String> bookIds = new ArrayList<>();
        for(VFcAccbookAndBlnedsVO vo : data) {
            bookIds.add(vo.getId());
        }
        List<FcAccbookDtlVO> bookDtlDTOs = fcAccbookDao.getPageDtlsByBookIds(bookIds,false);
        List<FcAccbookDtlVO> bookBlnDtlDTOs = fcAccbookBlnedDao.getPageDtlsByBookIds(bookIds,false);
        Map<String, List<FcAccbookDtlVO>> dtlMap = new HashMap<>();
        groupByBookId(dtlMap, bookDtlDTOs);
        groupByBookId(dtlMap, bookBlnDtlDTOs);

		List<String> voucherIds = data.stream().map(VFcAccbookAndBlnedsVO::getVoucherType).collect(Collectors.toList());
		ArrayList<String> userIds = Lists.newArrayList();
		data.forEach(a -> {
			userIds.add(a.getOperatorCode());
			userIds.add(a.getCheckerCode());
		});
		List<FcVoucherTypeDTO> fcVoucherTypeDTOS = iFcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
		Map<String, String> voucherMap = fcVoucherTypeDTOS.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		Map<String, String> userMap = iUserService.getUserNameByIds(userIds);

		for(VFcAccbookAndBlnedsVO vo : data) {
		    String bookId = vo.getId();
			//设置明细集合
			List<FcAccbookDtlVO> dtlDTOs = dtlMap.get(bookId);
			if (EmptyUtils.isEmptyList(dtlDTOs)) {
                dtlDTOs = fcAccbookDao.getPageDtlsByBookId(vo.getId(),false);
            }
			if(EmptyUtils.isEmptyList(dtlDTOs)){//已日结账务
				dtlDTOs=fcAccbookBlnedDao.getPageDtlsByBookId(vo.getId(),false);
			}

			//voucherName voucherType=fc_voucher_type表Id
			//operatorCode
			//checkCode
			if (ObjectUtils.isNotEmpty(voucherMap)){
				vo.setVoucherName(voucherMap.get(vo.getVoucherType()));
			}
			if (ObjectUtils.isNotEmpty(userMap)){
				vo.setOperatorCode(userMap.get(vo.getOperatorCode()));
				vo.setCheckerCode(userMap.get(vo.getCheckerCode()));
			}

			vo.setAccbookDtl(dtlDTOs);
			vo.setDtlSize(dtlDTOs.size());
		}
		return output;
	}

	/**
	 * 参考上面方法 setOutputPageData
	 * 专门为 记账凭证查询 构建填充数据方法。此处的 记账员和复核员 需要的是 code，而不是填充 name
	 * @param output
	 * @return
	 */
	public DataTablesOutput setOutputPageDataNew(DataTablesOutput output) {
		List<VFcAccbookAndBlnedsVO> data = output.getData();
		if (null == data || EmptyUtils.isEmptyList(data)) {
			return output;
		}
		List<String> bookIds = new ArrayList<>();
		for(VFcAccbookAndBlnedsVO vo : data) {
			bookIds.add(vo.getId());
		}
		List<FcAccbookDtlVO> bookDtlDTOs = fcAccbookDao.getPageDtlsByBookIds(bookIds,false);
		List<FcAccbookDtlVO> bookBlnDtlDTOs = fcAccbookBlnedDao.getPageDtlsByBookIds(bookIds,false);
		Map<String, List<FcAccbookDtlVO>> dtlMap = new HashMap<>();
		groupByBookId(dtlMap, bookDtlDTOs);
		groupByBookId(dtlMap, bookBlnDtlDTOs);
		List<String> voucherIds = data.stream().map(VFcAccbookAndBlnedsVO::getVoucherType).collect(Collectors.toList());

		ArrayList<String> userIds = Lists.newArrayList();
		data.forEach(a -> {
			userIds.add(a.getOperatorCode());
			userIds.add(a.getCheckerCode());
		});
		List<FcVoucherTypeDTO> fcVoucherTypeDTOS = iFcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
		Map<String, String> voucherMap = fcVoucherTypeDTOS.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		//获取 账号
		Map<String, String> userMap = iUserService.getLoginNameByIds(userIds);

		for(VFcAccbookAndBlnedsVO vo : data) {
			String bookId = vo.getId();
			//设置明细集合
			List<FcAccbookDtlVO> dtlDTOs = dtlMap.get(bookId);
			if (EmptyUtils.isEmptyList(dtlDTOs)) {
				dtlDTOs = fcAccbookDao.getPageDtlsByBookId(vo.getId(),false);
			}
			if(EmptyUtils.isEmptyList(dtlDTOs)){//已日结账务
				dtlDTOs=fcAccbookBlnedDao.getPageDtlsByBookId(vo.getId(),false);
			}
			if (ObjectUtils.isNotEmpty(voucherMap)){
				vo.setVoucherName(voucherMap.get(vo.getVoucherType()));
			}

			if (ObjectUtils.isNotEmpty(userMap)){
				vo.setOperatorCode(userMap.get(vo.getOperatorCode()));
				vo.setCheckerCode(userMap.get(vo.getCheckerCode()));
			}

			vo.setAccbookDtl(dtlDTOs);
			vo.setDtlSize(dtlDTOs.size());
		}
		return output;
	}


	/**
	* <br>
	* <b>功能：</b> 按凭证ID分组 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2021/11/12 0012 <br>
	*/
	public void groupByBookId(Map<String, List<FcAccbookDtlVO>> dtlMap, List<FcAccbookDtlVO> dtlDTOs) {
        for(FcAccbookDtlVO vo : dtlDTOs) {
            String bookId = vo.getBookId();
            List<FcAccbookDtlVO> dtlVOS = dtlMap.get(bookId);
            if (EmptyUtils.isEmptyList(dtlVOS)) {
                dtlVOS = new ArrayList<>();
                dtlMap.put(bookId, dtlVOS);
            }
            dtlVOS.add(vo);
        }
    }
	/**
	 * 功能介绍 : 查询资金池记账凭证列表。<br>
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryPoolFcAccbookAndBlnedsData(DataTablesInput input, String resource, ICurrentUser user) {
		String corpAuthSql = SqlUtil.getInSql(corpPoolService.getUserPoolIds(resource,user), "main_info.net_id", true);
		DataTablesOutput output = financialQueryDao.listPoolOutputData(input, corpAuthSql);
		return setOutputPageData(output);
	}
	/**
	 * 导出
	 * @param input
	 * @param resource
	 * @param userId
	 * @return
	 */
	public HSSFWorkbook createExcelContent(DataTablesInput input, String resource, String userId){
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		List<VFcAccbookAndBlnedsVO> list = financialQueryDao.listExcel(input,corpAuthSql);//查询结果
		List res = setBookDtlData(list);
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
		row1.createCell(6).setCellValue("会计分录信息");
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
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 12));
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
			String voucherState = iDictService.get("Finacial.VoucherStatus",book.getVoucherState());
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
	 * 功能介绍 : 设置凭证分录信息-导出<br>
	 */
	public List<VFcAccbookAndBlnedsVO> setBookDtlData(List<VFcAccbookAndBlnedsVO> res) {
		if(EmptyUtils.isEmptyList(res)){
			return new ArrayList<>();
		}

		List<String> voucherIds = res.stream().map(VFcAccbookAndBlnedsVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> fcVoucherTypeDTOS = iFcVoucherTypeService.getFcVoucherTypeByIds(voucherIds);
		Map<String, String> voucherMap = fcVoucherTypeDTOS.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));

		ArrayList<String> userIds = Lists.newArrayList();
		res.forEach(a -> {
			userIds.add(a.getOperatorCode());
			userIds.add(a.getCheckerCode());
		});
		//获取 账号
		Map<String, String> userMap = iUserService.getLoginNameByIds(userIds);
		for(VFcAccbookAndBlnedsVO vo:res){
			//设置明细集合
			List<FcAccbookDtlVO> dtlDTOs = fcAccbookDao.getPageDtlsByBookId(vo.getId(),false);
			if(EmptyUtils.isEmptyList(dtlDTOs)){//已日结账务
				dtlDTOs=fcAccbookBlnedDao.getPageDtlsByBookId(vo.getId(),false);
			}
			vo.setAccbookDtl(dtlDTOs);
			vo.setDtlSize(dtlDTOs.size());

			if (ObjectUtils.isNotEmpty(voucherMap)){
				vo.setVoucherName(voucherMap.get(vo.getVoucherType()));
			}

			if (ObjectUtils.isNotEmpty(userMap)){
				vo.setOperatorCode(userMap.get(vo.getOperatorCode()));
				vo.setCheckerCode(userMap.get(vo.getCheckerCode()));
			}
		}
		return res;
	}
	/**
	 * 判断是否为ie浏览器
	 * @param request
	 * @return
	 */
	public boolean isIE(HttpServletRequest request) {
		return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
	}
}
