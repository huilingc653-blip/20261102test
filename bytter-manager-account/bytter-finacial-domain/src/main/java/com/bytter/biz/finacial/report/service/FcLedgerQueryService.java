package com.bytter.biz.finacial.report.service;
import com.bytter.biz.finacial.report.dao.FcLedgerQueryDao;
import com.bytter.biz.finacial.report.vo.FcLedgerListVO;
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
import com.bytter.system.api.IDictService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("fcLedgerQueryService")
@Transactional
public class FcLedgerQueryService extends BaseService<FcLedgerListVO,String>{
	
	@Autowired
	private FcLedgerQueryDao fcLedgerQueryDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IDictService iDictService;
	/*@Autowired
	private FcCompareCenterDao fcCompareAccDao;*/
	@DubboReference
	private IFcCompareCenterService iFcCompareCenterService;
	@Override
	public BaseDao<FcLedgerListVO, String> getEntityDao() {
		return fcLedgerQueryDao;
	}
	
	/**
	 * 分户账查询
	 * @param input
	 * @param resource
	 * @param user
	 * @return
	 */
	@Transactional(readOnly=true)
	public DataTablesOutput queryLedgerDtlList(DataTablesInput input, String resource, ICurrentUser user) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		//查询账户信息
		DataTablesInput headInput=new DataTablesInput();
		BeanUtils.copyNotNullProperties(input,headInput);
		headInput.setStart(input.getStart()-1);
		DataTablesOutput headData=fcLedgerQueryDao.getFcLedgerHeads(headInput, corpAuth);
		List<FcLedgerListVO> headVo=headData.getData();
		//账户数据为空则直接返回，不为空则查询明细
		if(EmptyUtils.isEmptyList(headVo)){
			return headData;
		}
		//根据分页获取账户
		String account=headVo.get(0).getAccount();
		Date tallyDate;
		if(null==input.getFormdata().get("GEd_tallyDate")){
			//查询账户信息
			FcAccDTO accDTO=fcAccService.getAccDTOByAccount(account);
			//账户启用日期
			tallyDate=accDTO.getAccStartDate();
		}else{
			tallyDate=DateUtils.parse(String.valueOf(input.getFormdata().get("GEd_tallyDate")),DateUtils.PATTERN_CLASSICAL_SIMPLE);
		}
		//账户期初余额
		BigDecimal balance = BigDecimal.ZERO;
		//查询账户余额
		List<BigDecimal> list=iFcCompareCenterService.queryAccBalanceByDate(account, tallyDate);
		if (!EmptyUtils.isEmptyList(list)) {
			balance=list.get(0);
		}
		//将帐号添加到查询条件中
		input.addFilter(MatchTypeEnum.EQ, "account", account);
		//查询明细数量
		Map<String,Object> dtlNum=fcLedgerQueryDao.queryFcLedgerDtlNum(input,corpAuth);
		DataTablesInput dtlInput=new DataTablesInput();
		BeanUtils.copyNotNullProperties(input,dtlInput);
		dtlInput.setLength(Integer.valueOf(String.valueOf(dtlNum.get("TATOLNUM"))));
		dtlInput.setStart(0);
        dtlInput.addFilter(MatchTypeEnum.EQ, "account", account);
		//查询分户账明细
		DataTablesOutput accDtlOut=fcLedgerQueryDao.getFcLedgerDtl(dtlInput, corpAuth);
		List<FcLedgerListVO> accVo=accDtlOut.getData();
		if(accVo!=null&&accVo.size()>0){
			accVo.get(0).setBeginningBalance(balance);
			accVo.get(0).setAccNum(headData.getRecordsTotal());
		}
		return accDtlOut;
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
		List<FcLedgerListVO> headVo = fcLedgerQueryDao.excelFcLedgerHeads(input, corpAuth);//表头信息
		List<FcLedgerListVO> res = setVoInfo(input, headVo);//结果
		// 创建工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		String content = "流水账查询表";
		// 创建sheet
		HSSFSheet sheet = workbook.createSheet(content);
		// 创建Excel表中的内容
		String sDate = (String)input.getFormdata().get("gedTallyDate");
		String eDate = (String)input.getFormdata().get("ledTallyDate");
		if(EmptyUtils.isEmpty(sDate)) {
            sDate = (String)input.getFormdata().get("GEd_tallyDate");
        }
        if(EmptyUtils.isEmpty(eDate)) {
            eDate = (String)input.getFormdata().get("LEd_tallyDate");
        }
		this.createExcelBody(workbook,sheet,res,sDate,eDate);
		return workbook;
	}
	/**
	 * 创建Excel内容样式
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
	private void createExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<FcLedgerListVO> list,String sDate,String eDate) {
		// Excel表格内容样式
		HSSFCellStyle cs = this.createExcelBodyStyle(workbook);
		String account = "";
		int index = 0;// 指向所在行
		for (FcLedgerListVO res : list){
			if(StringUtils.isEmpty(account)){
				account = res.getAccount();
			}
			/*********************创建表头**********************/
			if(index == 0 || !account.equals(res.getAccount())) {
				account = res.getAccount();
				if(index != 0){
					sheet.createRow(index);//多个表添加空行,分隔
					index = index+1;
				}
				// 第一行
				sheet.createRow(index);
				HSSFRow row1 = sheet.getRow(index);
				if(StringUtils.isEmpty(sDate))sDate = "";
				if(StringUtils.isEmpty(eDate))eDate = "";
				row1.createCell(0).setCellValue("记账日期区间:" + sDate+"-"+ eDate);
				row1.createCell(3).setCellValue("单位名称:" + res.getCorpName());
				row1.createCell(6).setCellValue("币别:" + res.getCurName());
				sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
				sheet.addMergedRegion(new CellRangeAddress(index, index, 3, 5));
				sheet.addMergedRegion(new CellRangeAddress(index, index, 6, 7));
				index = index+1;
				// 第二行
				sheet.createRow(index);
				HSSFRow row2 = sheet.getRow(index);
				row2.createCell(0).setCellValue("账号:" + res.getAccount());
				row2.createCell(3).setCellValue("账户名称:" + res.getAccName());
				row2.createCell(6).setCellValue("期初余额:" + String.format("%.2f",res.getBeginningBalance()));
				sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 2));
				sheet.addMergedRegion(new CellRangeAddress(index, index, 3, 5));
				sheet.addMergedRegion(new CellRangeAddress(index, index, 6, 7));
				index = index+1;
				//第三行
				sheet.createRow(index);
				HSSFRow row3 = sheet.getRow(index);
				HSSFCell r = row3.createCell(0);
				r.setCellValue("记账日期");
				r.setCellStyle(cs);
				HSSFCell r1 = row3.createCell(1);
				r1.setCellValue("业务种类");
				r1.setCellStyle(cs);
				HSSFCell r2 = row3.createCell(2);
				r2.setCellValue("凭证种类");
				r2.setCellStyle(cs);
				HSSFCell r3 = row3.createCell(3);
				r3.setCellValue("凭证号");
				r3.setCellStyle(cs);
				HSSFCell r4 = row3.createCell(4);
				r4.setCellValue("摘要");
				r4.setCellStyle(cs);
				HSSFCell r5 = row3.createCell(5);
				r5.setCellValue("借方发生额");
				r5.setCellStyle(cs);
				HSSFCell r6 = row3.createCell(6);
				r6.setCellValue("贷方发生额");
				r6.setCellStyle(cs);
				HSSFCell r7 = row3.createCell(7);
				r7.setCellValue("余额");
				r7.setCellStyle(cs);
				HSSFCell r8 = row3.createCell(8);
				r8.setCellValue("借贷方向");
				r8.setCellStyle(cs);
				index = index+1;
				/*********************表头结束**********************/
			}
			sheet.createRow(index);
			HSSFRow row5 = sheet.getRow(index);
			HSSFCell r = row5.createCell(0);
			r.setCellValue(DateUtils.formatDate(res.getTallyDate()));
			r.setCellStyle(cs);
			HSSFCell r1 = row5.createCell(1);
			r1.setCellValue(res.getBusRaceName());
			r1.setCellStyle(cs);
			HSSFCell r2 = row5.createCell(2);
			r2.setCellValue(res.getVoucherTypeName());
			r2.setCellStyle(cs);
			HSSFCell r3 = row5.createCell(3);
			r3.setCellValue(res.getVoucherNo());
			r3.setCellStyle(cs);
			HSSFCell r4 = row5.createCell(4);
			r4.setCellValue(res.getSummaryContent());
			r4.setCellStyle(cs);
			HSSFCell r5 = row5.createCell(5);
			r5.setCellValue(String.format("%.2f",res.getDebitMoney()));
			r5.setCellStyle(cs);
			HSSFCell r6 = row5.createCell(6);
			r6.setCellValue(String.format("%.2f",res.getCreditMoney()));
			r6.setCellStyle(cs);
			HSSFCell r7 = row5.createCell(7);
			r7.setCellValue(String.format("%.2f",res.getBalance()));
			r7.setCellStyle(cs);
			HSSFCell r8 = row5.createCell(8);
			String balanceWay = iDictService.get("Admin.BalanceWay",res.getBalanceWay());
			r8.setCellValue(balanceWay);
			r8.setCellStyle(cs);
			index++;
		}
	}
	public List<FcLedgerListVO> setVoInfo(DataTablesInput input,List<FcLedgerListVO> headList){
		List<String> accounts = new ArrayList<>();
		for (FcLedgerListVO h : headList) {
			accounts.add(h.getAccount());
		}
		//查询账号记账明细
		List<FcLedgerListVO> accDtls=fcLedgerQueryDao.excelFcLedgerDtl(input,accounts);
		String account = "";
		for (FcLedgerListVO d : accDtls) {
			account = d.getAccount();
			Date tallyDate;
			if (null == input.getFormdata().get("GEd_tallyDate")) {
				//查询账户信息
				FcAccDTO accDTO = fcAccService.getAccDTOByAccount(d.getAccount());
				//账户启用日期
				tallyDate = accDTO.getAccStartDate();
			} else {
				tallyDate = DateUtils.parse(String.valueOf(input.getFormdata().get("GEd_tallyDate")), DateUtils.PATTERN_CLASSICAL_SIMPLE);
			}
			//账户期初余额
			BigDecimal balance = BigDecimal.ZERO;
			//查询账户余额
			List<BigDecimal> list=iFcCompareCenterService.queryAccBalanceByDate(d.getAccount(), tallyDate);
			if (!EmptyUtils.isEmptyList(list)) {
				balance=list.get(0);
			}
			d.setBeginningBalance(balance);
		}
		return accDtls;
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
