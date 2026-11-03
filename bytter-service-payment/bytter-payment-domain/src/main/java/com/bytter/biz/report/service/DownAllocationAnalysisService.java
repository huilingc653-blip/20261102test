package com.bytter.biz.report.service;
import com.bytter.biz.allocation.dao.NisAllocationReqDao;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.report.dao.DownAllocationAnalysisDao;
import com.bytter.biz.report.vo.DownAllocationAnalysisListVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IMultiLangService;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author zuoy
 * @Description 资金调拨明细表
 * @date 2017.10.10
 */
@Service("downAllocationAnalysisService")
@Transactional
public class DownAllocationAnalysisService extends BaseService<DownAllocationAnalysisListVO, String> {
	@Autowired
	private DownAllocationAnalysisDao downAllocationAnalysisDao;
	@Autowired
	private NisAllocationReqDao nisAllocationReqDao;

	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@Override
	public BaseDao<DownAllocationAnalysisListVO, String> getEntityDao() {
		return downAllocationAnalysisDao;
	}
	@Transactional(readOnly = true)
	public DataTablesOutput queryAccbookDtls(DataTablesInput input, String resource, ICurrentUser user) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		DataTablesInput headInput=new DataTablesInput();
		BeanUtils.copyNotNullProperties(input,headInput);
		headInput.setStart(input.getStart()-1);
		//根据查出单位确定页数
		DataTablesOutput headData = downAllocationAnalysisDao.groupSize(headInput,corpAuth);
		//查询下拨明细总额
		BigDecimal big = downAllocationAnalysisDao.getTatolMoney(headInput,corpAuth);
		List<DownAllocationAnalysisListVO> headVo=headData.getData();
		if(EmptyUtils.isEmptyList(headVo)){
			return headData;
		}
		String corpId=headVo.get(0).getCorpId();
		input.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
		DataTablesInput dtlInput=new DataTablesInput();
		BeanUtils.copyNotNullProperties(input,dtlInput);
		dtlInput.setStart(0);
		//总账户余额查询
		List<Map<String,Object>> bals = downAllocationAnalysisDao.getTotalBisAccBal(corpAuth);
		//根据单位查询下拨单据列表
		DataTablesOutput accDtlOut= new DataTablesOutput();
		List<DownAllocationAnalysisListVO> corpVo= downAllocationAnalysisDao.queryDownAllocationAnalysisDtl(dtlInput);
		for(DownAllocationAnalysisListVO vo:corpVo){
			if(null==vo.getStatus()){
				vo.setStatus(0);
				if(StringUtils.isNotBlank(vo.getCbsBillId())) {
					NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqDao.getByUnion(vo.getCbsBillId());
					if(null != nisAllocationReqListDTO) {
						vo.setStatus(nisAllocationReqListDTO.getStatus());//状态为空查询下拔单状态
					}
				}
			}
		}
		corpVo.get(0).setCount((int)headData.getRecordsTotal());
		if(!EmptyUtils.isEmptyList(bals)){
			corpVo.get(0).setBankList(bals);
		}
		corpVo.get(0).setRowSize(bals.size()%2 == 0 ? bals.size()/2 : (bals.size()/2) + 1);
		corpVo.get(0).setTotal(big);
		accDtlOut.setData(corpVo);
		return accDtlOut;
	}
	public List<Map<String,Object>> getBals(DataTablesInput input, String resource, String userId){
		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
		List<Map<String,Object>> bals = downAllocationAnalysisDao.getTotalBisAccBal(corpAuth);
		return bals;
	}
	/**
	 * 根据单号查找下拨详情
	 * @param billNo
	 * @return
	 */
	public DownAllocationAnalysisListVO getByUnion(String billNo,String payerBank){
		return downAllocationAnalysisDao.getByUnion(billNo,payerBank);
	}
	/**
	 * <b>功能介绍：</b>获取国际化<br>
	 * <b>日期：</b>2018年2月27日<br>
	 * <b>作者：</b>wangzx<br>
	 * @version 1.0
	 * @param str
	 * @return
	 */
	public String getLangStr(String str){
		return multiLangServiceI.getLang(str);
	}
	/**
	 * 导出
	 * @param input
	 * @param resource
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> excel(DataTablesInput input, String resource, String userId){
		List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
		List<Map<String,Object>> bals = downAllocationAnalysisDao.getTotalBisAccBal(corpAuth);
		//查出有几个单位
		DataTablesOutput headData = downAllocationAnalysisDao.groupSize(input,corpAuth);
		List<DownAllocationAnalysisListVO> headVo=headData.getData();
		if(EmptyUtils.isEmptyList(headVo)){
			return null;
		}
		String corpId = "";
		List<DownAllocationAnalysisListVO> list = null;
		DownAllocationAnalysisListVO vo = null;
		Map<String,List<DownAllocationAnalysisListVO>> mapList = new HashMap<>();
		for(DownAllocationAnalysisListVO dao : headVo) {
			corpId = dao.getCorpId();
			input.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
			list = downAllocationAnalysisDao.getExcel(input, corpAuth);
			mapList.put("corpId",list);
			if (!EmptyUtils.isEmptyList(list)) {
				for (DownAllocationAnalysisListVO listVo : list) {
					vo = new DownAllocationAnalysisListVO();
					vo.setBillNo(listVo.getBillNo());
					vo.setVoucherName(listVo.getVoucherName());
					vo.setCorpName(listVo.getCorpName());
					vo.setPayerAcc(listVo.getPayerAcc());
					vo.setPayerAccName(listVo.getPayerAccName());
					vo.setPayerBank(listVo.getPayerBank());
					vo.setPayeeAcc(listVo.getPayeeAcc());
					vo.setPayeeAccName(listVo.getPayeeAccName());
					vo.setPayeeBank(listVo.getPayeeBank());
					vo.setCurName(listVo.getCurName());
					vo.setPayMoney(listVo.getPayMoney());
					vo.setPurpose(listVo.getPurpose());
					vo.setStatusStr(dictService.get("nisBillhead.status", listVo.getStatus()));
					map.add(BeanUtils.beanToMap(vo));
				}
			}
		}
		return map;
	}
	/**
	 * <br>
	 * <b>功能：</b>列头的样式<br>
	 * <b>作者：</b>wangzx<br>
	 * <b>日期：</b> 2018-2-27 <br>
	 * @param wb
	 * @return
	 */
	public HSSFCellStyle initColumnHeadStyle(HSSFWorkbook wb) {
		HSSFCellStyle columnHeadStyle = wb.createCellStyle();
		HSSFFont columnHeadFont = wb.createFont();
		columnHeadFont.setFontName("宋体");
		columnHeadFont.setFontHeightInPoints((short) 10);
		columnHeadFont.setBold(true);
		columnHeadStyle.setFont(columnHeadFont);
		columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
		columnHeadStyle.setLocked(true);
		columnHeadStyle.setWrapText(true);
		columnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
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
	 * <br>
	 * <b>功能：</b>单元格的默认样式<br>
	 * <b>作者：</b>wangzx<br>
	 * <b>日期：</b> 2018-2-28 <br>
	 * @param wb
	 * @return
	 */
	public HSSFCellStyle initColumnCenterstyle(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		HSSFCellStyle centerstyle = wb.createCellStyle();
		centerstyle.setFont(font);
		centerstyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		centerstyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
		centerstyle.setWrapText(true);
		centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderLeft(BorderStyle.THIN);
		centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderRight(BorderStyle.THIN);
		centerstyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
		centerstyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
		return centerstyle;
	}
	/**
	 * 生成excel数据
	 * @param input
	 * @param resource
	 * @param userId
	 * @param impType
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook doExportInterestBillXls(DataTablesInput input, String resource, String userId, String impType
												) throws Exception{
		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("下拨金额实时分析表");
		HSSFCellStyle cellStyle = this.initColumnHeadStyle(workbook);
		int index = 0;
		//创建 表头
		HSSFRow rowHead = sheet.createRow(index);
		rowHead.setHeightInPoints(20);
		HSSFCell cellHead = rowHead.createCell(0);
		//标题
		cellHead.setCellValue("下拨金额实时分析表");
		cellHead.setCellStyle(cellStyle);
		for (int i = 1; i < 13; i++) {
			HSSFCell cell = rowHead.createCell(i);
			cell.setCellValue(new HSSFRichTextString());
			cell.setCellStyle(cellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 0, (short) 12));
		index ++;
		HSSFCellStyle dataCellStyle = this.initColumnCenterstyle(workbook);
		List<Map<String,Object>> bals = downAllocationAnalysisDao.getTotalBisAccBal(corpAuth);
		Map<String,Object> balMap = new TreeMap<String, Object>();
		if(!EmptyUtils.isEmptyList(bals)){
			for(Map<String, Object> bal : bals){
				balMap.put(bal.get("TYPENAME").toString(), bal.get("AVAILBAL"));
			}
		}
		index = this.writeTotalAndBisAccBal(sheet, dataCellStyle, index, balMap, "当前总账户余额：");
		Map<String,Object> totalMap = new HashMap<String, Object>();
		Map<String, List<DownAllocationAnalysisListVO>> detailsMap = this.getDownAllocationAnalysisDatas(input,resource,userId, totalMap);
		index = this.writeAllocationDetailsData(workbook, sheet, cellStyle, index, detailsMap);
		this.writeTotalAndBisAccBal(sheet, dataCellStyle, index, totalMap, "下拨金额合计：");
		return workbook;
	}
	/**
	 * <br>
	 * <b>功能：</b>写总账户余额<br>
	 * <b>作者：</b>wangzx<br>
	 * <b>日期：</b> 2018-2-28 <br>
	 * @param sheet
	 * @param cellStyle
	 * @param index
	 * @param bals
	 */
	private int writeTotalAndBisAccBal(HSSFSheet sheet,HSSFCellStyle cellStyle,int index,
									   Map<String, Object> bals,String title){
		Iterator<Map.Entry<String,Object>> iter = bals.entrySet().iterator();
		int count = 1;
		HSSFRow rowBal = null;
		while(iter.hasNext()){
			rowBal = sheet.createRow(index);
			rowBal.setHeightInPoints(20);
			Map.Entry<String,Object> et0 = iter.next();
			if(count == 1){
				HSSFCell cell0 = rowBal.createCell(0);
				cell0.setCellValue(title);
				cell0.setCellStyle(cellStyle);
				HSSFCell cell1 = rowBal.createCell(1);
				cell1.setCellStyle(cellStyle);
			}else{
				HSSFCell cell0 = rowBal.createCell(0);
				cell0.setCellStyle(cellStyle);
				HSSFCell cell1 = rowBal.createCell(1);
				cell1.setCellStyle(cellStyle);
			}
			sheet.addMergedRegion(new CellRangeAddress(index, index, (short)0, 1));
			if(!"下拨金额合计：".equals(title)) {
				HSSFCell cell2 = rowBal.createCell(2);
				cell2.setCellValue(et0.getKey() + "：");
				cell2.setCellStyle(cellStyle);
				HSSFCell cell3 = rowBal.createCell(3);
				cell3.setCellStyle(cellStyle);
				cell3.setCellValue(NumberUtil.formatNumeric(Double.valueOf(et0.getValue().toString())));
			}else {
				HSSFCell cell2 = rowBal.createCell(2);
				cell2.setCellValue(NumberUtil.formatNumeric(Double.valueOf(et0.getValue().toString())));
				cell2.setCellStyle(cellStyle);
				HSSFCell cell3 = rowBal.createCell(3);
				cell3.setCellStyle(cellStyle);
				cell3.setCellValue(new HSSFRichTextString());
			}
			HSSFCell cell4 = rowBal.createCell(4);
			cell4.setCellValue(new HSSFRichTextString());
			cell4.setCellStyle(cellStyle);
			HSSFCell cell5 = rowBal.createCell(5);
			cell5.setCellValue(new HSSFRichTextString());
			cell5.setCellStyle(cellStyle);
			HSSFCell cell8 = rowBal.createCell(8);
			cell8.setCellValue(new HSSFRichTextString());
			cell8.setCellStyle(cellStyle);
			HSSFCell cell9 = rowBal.createCell(9);
			cell9.setCellValue(new HSSFRichTextString());
			cell9.setCellStyle(cellStyle);
			HSSFCell cell10 = rowBal.createCell(10);
			cell10.setCellValue(new HSSFRichTextString());
			cell10.setCellStyle(cellStyle);
			HSSFCell cell11 = rowBal.createCell(11);
			cell11.setCellValue(new HSSFRichTextString());
			cell11.setCellStyle(cellStyle);
			HSSFCell cell12 = rowBal.createCell(12);
			cell12.setCellValue(new HSSFRichTextString());
			cell12.setCellStyle(cellStyle);
			if(iter.hasNext()){
				Map.Entry<String,Object> et1 = iter.next();
				HSSFCell cell6 = rowBal.createCell(6);
				cell6.setCellValue(et1.getKey()+": ");
				cell6.setCellStyle(cellStyle);
				HSSFCell cell7 = rowBal.createCell(7);
				cell7.setCellValue(NumberUtil.formatNumeric(Double.valueOf(et1.getValue().toString())));
				cell7.setCellStyle(cellStyle);
			}else{
				HSSFCell cell6 = rowBal.createCell(6);
				cell6.setCellValue(new HSSFRichTextString());
				cell6.setCellStyle(cellStyle);
				HSSFCell cell7 = rowBal.createCell(7);
				cell7.setCellValue(new HSSFRichTextString());
				cell7.setCellStyle(cellStyle);
			}
			index++;
			count += 2;
		}
		if("当前总账户余额：".equals(title)){
			HSSFRow rowBlank = sheet.createRow(index);
			rowBlank.setHeightInPoints(20);
			for(int i = 0; i< 13; i ++){
				HSSFCell cell = rowBlank.createCell(i);
				cell.setCellValue(new HSSFRichTextString());
				cell.setCellStyle(cellStyle);
			}
			sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 0, (short) 12));
		}
		return index + 1;
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>下拨数据查询<br>
	 * <b>作者：</b>wangzx<br>
	 * <b>日期：</b> 2018-2-28 <br>
	 * @param totalMap
	 * @return
	 */
	public Map<String, List<DownAllocationAnalysisListVO>> getDownAllocationAnalysisDatas(DataTablesInput input, String resource, String userId, Map<String,Object> totalMap){
		Map<String, List<DownAllocationAnalysisListVO>> result = new TreeMap<String, List<DownAllocationAnalysisListVO>>();
		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
		List<DownAllocationAnalysisListVO> datas = downAllocationAnalysisDao.getExcel(input, corpAuth);
		String tempKey = null;
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal money = BigDecimal.ZERO;
		int index = 0;
		for(DownAllocationAnalysisListVO data : datas){
			String groupKey = data.getGroupKey();
			if(StringUtils.isNotBlank(tempKey) && !tempKey.equals(groupKey)){
				//小计
				int count = result.get(tempKey).size();
				DownAllocationAnalysisListVO bean = new DownAllocationAnalysisListVO();
				bean.setBillNo("合计：");
				bean.setVoucherName("共"+count+"笔");
				bean.setCurName("小计：");
				bean.setPayMoney(subtotal);
				result.get(tempKey).add(bean);
				subtotal = BigDecimal.ZERO;
			}
			BigDecimal payMoney = data.getPayMoney();
			subtotal = subtotal.add(payMoney);
			//合计
			money = money.add(payMoney);
			totalMap.put("totalMoney",money);
			if(result.containsKey(groupKey)){
				result.get(groupKey).add(data);
			}else{
				List<DownAllocationAnalysisListVO> list = new ArrayList<DownAllocationAnalysisListVO>();
				list.add(data);
				result.put(groupKey, list);
			}
			tempKey = groupKey;
			if(index == datas.size() - 1){
				//小计
				int count = result.get(tempKey).size();
				DownAllocationAnalysisListVO bean = new DownAllocationAnalysisListVO();
				bean.setBillNo("合计：");
				bean.setVoucherName("共"+count+"笔");
				bean.setCurName("小计：");
				bean.setPayMoney(subtotal);
				result.get(tempKey).add(bean);
				subtotal = BigDecimal.ZERO;
			}
			index ++;
		}
		return result;
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>数据<br>
	 * <b>作者：</b>wangzx<br>
	 * <b>日期：</b> 2013-12-9 <br>
	 * @param workbook
	 * @param sheet
	 * @param cellStyle
	 * @param index
	 * @param detailsMap
	 */
	private int writeAllocationDetailsData(HSSFWorkbook workbook,HSSFSheet sheet,HSSFCellStyle cellStyle,int index,
										   Map<String, List<DownAllocationAnalysisListVO>> detailsMap){
		Iterator<Map.Entry<String, List<DownAllocationAnalysisListVO>>> iter = detailsMap.entrySet().iterator();
		HSSFCellStyle dataCellStyle = this.initColumnCenterstyle(workbook);
		while(iter.hasNext()){
			Map.Entry<String, List<DownAllocationAnalysisListVO>> et0 = iter.next();
			String groupKey = et0.getKey().substring(0, et0.getKey().indexOf(";"));
			index = this.writeAllocationDetailsHead(sheet, dataCellStyle, index++, groupKey);
			List<DownAllocationAnalysisListVO> beans = et0.getValue();
			for(DownAllocationAnalysisListVO bean : beans){
				HSSFRow row = sheet.createRow(index++);
				row.setHeightInPoints(20);
				for(int i = 0; i < 13; i ++){
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(dataCellStyle);
					switch(i){
						case 0:
							cell.setCellValue(bean.getBillNo());
							break;
						case 1:
							cell.setCellValue(bean.getVoucherName());
							break;
						case 2:
							sheet.setColumnWidth(i, 256 * 35);
							cell.setCellValue(bean.getCorpName());
							break;
						case 3:
							sheet.setColumnWidth(i, 256 * 35);
							cell.setCellValue(bean.getPayerAcc());
							break;
						case 4:
							sheet.setColumnWidth(i, 256 * 35);
							cell.setCellValue(bean.getPayeeAccName());
							break;
						case 5:
							sheet.setColumnWidth(i, 256 * 40);
							cell.setCellValue(bean.getPayerBank());
							break;
						case 6:
							sheet.setColumnWidth(i, 256 * 35);
							cell.setCellValue(bean.getPayeeAcc());
							break;
						case 7:
							sheet.setColumnWidth(i, 256 * 35);
							cell.setCellValue(bean.getPayeeAccName());
							break;
						case 8:
							sheet.setColumnWidth(i, 256 * 35);
							cell.setCellValue(bean.getPayeeBank());
							break;
						case 9:
							cell.setCellValue(bean.getCurName());
							break;
						case 10:
							cell.setCellValue(NumberUtil.formatNumeric(bean.getPayMoney().doubleValue()));
							break;
						case 11:
							sheet.setColumnWidth(i, 256 * 35);
							cell.setCellValue(bean.getPurpose());
							break;
						case 12:
							cell.setCellValue(dictService.get("nisBillhead.status", bean.getStatus()));
							break;
						default:
							break;
					}
				}
			}
			HSSFRow rowBlank = sheet.createRow(index);
			rowBlank.setHeightInPoints(20);
			for(int i = 0; i< 13; i ++){
				HSSFCell cell = rowBlank.createCell(i);
				cell.setCellValue(new HSSFRichTextString());
				cell.setCellStyle(cellStyle);
			}
			sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 0, (short) 12));
			index += 1;
		}
		return index;
	}
	/**
	 * <br>
	 * <b>功能：</b>表头<br>
	 * <b>作者：</b>wangzx<br>
	 * <b>日期：</b> 2018-2-28 <br>
	 * @param sheet
	 * @param cellStyle
	 * @param index
	 * @param groupKey
	 */
	private int writeAllocationDetailsHead(HSSFSheet sheet,HSSFCellStyle cellStyle,int index,String groupKey){
		HSSFRow rowGroup = sheet.createRow(index);
		rowGroup.setHeightInPoints(20);
		HSSFCell cell0 = rowGroup.createCell(0);
		cell0.setCellValue("下拨单位: ");
		cell0.setCellStyle(cellStyle);
		HSSFCell cell1 = rowGroup.createCell(1);
		cell1.setCellValue(groupKey);
		HSSFCellStyle cellStyle0 = cellStyle;
		cellStyle0.setAlignment(HorizontalAlignment.LEFT);
		cell1.setCellStyle(cellStyle0);
		for (int i = 2; i < 13; i++) {
			HSSFCell cell = rowGroup.createCell(i);
			cell.setCellValue(new HSSFRichTextString());
			cell.setCellStyle(cellStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress(index, index, (short) 1, (short) 12));
		index += 1;
		/*String[] heads = new String[]{"指令编号", "结算类型" , "下拨单位", "付款账号", "付款人", "付款开户行",
				"收款账号", "收款人", "收款开户行", "币别", "金额", "用途", "状态"};*/
		String[] heads = new String[]{getLangStr("table.bisGatherCmd.voucherNo"),
				getLangStr("fcVoucherType.settlType") ,
				getLangStr("downAllocationAnalysis.corpName"),
				getLangStr("table.nisAllocationReq.payerAcc"),
				getLangStr("table.nisAllocationReq.payerInfo"),
				getLangStr("table.nisAllocationReport.payerBank"),
				getLangStr("table.nisAllocationReq.payeeAcc"),
				getLangStr("table.nisAllocationReq.payeeInfo"),
				getLangStr("table.nisAllocationReport.payeeBank"),
				getLangStr("table.nisAllocationReport.curName"),
				getLangStr("table.nisBillhead.payMoney"),
				getLangStr("table.nisBillhead.purpose"),
				getLangStr("table.nisBillhead.status")};
		HSSFRow rowHead = sheet.createRow(index++);
		rowHead.setHeightInPoints(20);
		for (int i = 0; i < heads.length; i++) {
			HSSFCell cell = rowHead.createCell(i);
			cell.setCellValue(heads[i]);
			cell.setCellStyle(cellStyle);
			sheet.setColumnWidth(i, 256 * 18);
		}
		return index;
	}
}
