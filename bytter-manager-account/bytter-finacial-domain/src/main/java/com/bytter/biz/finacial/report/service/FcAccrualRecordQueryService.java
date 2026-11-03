package com.bytter.biz.finacial.report.service;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.report.dao.FcAccrualRecordQueryDao;
import com.bytter.biz.finacial.report.vo.FcAccrualRecordListVO;
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
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service("fcAccrualRecordQueryService")
@Transactional
public class FcAccrualRecordQueryService extends BaseService<FcAccrualRecordListVO,String>{
	
	@Autowired
	private FcAccrualRecordQueryDao fcAccrualRecordQueryDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@Override
	public BaseDao<FcAccrualRecordListVO, String> getEntityDao() {
		return fcAccrualRecordQueryDao;
	}
	
	/**
	 * 手续费结转记录
	 * @param input
	 * @param resource
	 * @param user
	 * @return
	 */
	public DataTablesOutput queryFcAccrualRecordData(DataTablesInput input,String resource, ICurrentUser user){
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		//查询币别信息
		DataTablesInput headInput=new DataTablesInput();
		BeanUtils.copyNotNullProperties(input,headInput);
		headInput.setStart(input.getStart()-1);
		DataTablesOutput headData=fcAccrualRecordQueryDao.queryFcAccrualRecordHead(headInput, corpAuth);
		List<FcAccrualRecordListVO> headVo=headData.getData();
		//币别信息为空则直接返回，不为空则查询手续费
		if(EmptyUtils.isEmptyList(headVo)){
			return headData;
		}
		//根据分页获取币别
		String curCode = Convert.toStr(input.getFormdata().get("EQs_curId"));
		if (ObjectUtil.isNotEmpty(curCode)){
			//将币别添加到查询条件中
			input.addFilter(MatchTypeEnum.EQ, "curCode", curCode);
		}
		//查询手续费结转明细数
		Map<String,Object> dtlNum=fcAccrualRecordQueryDao.queryFcAccrualRecordDtlNum(input,corpAuth);
		DataTablesInput dtlInput=new DataTablesInput();
		BeanUtils.copyNotNullProperties(input,dtlInput);
		dtlInput.setLength(Integer.valueOf(String.valueOf(dtlNum.get("TATOLNUM"))));
		dtlInput.setStart(0);
		//获取手续费结转明细
		DataTablesOutput recordDtlOut=fcAccrualRecordQueryDao.queryFcAccrualRecordData(dtlInput,corpAuth);
		List<FcAccrualRecordListVO> list=recordDtlOut.getData();
		if (ObjectUtil.isNotEmpty(list)){
			list.get(0).setAccrualNum(headData.getRecordsTotal());
		}
		return recordDtlOut;
		
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
		List<FcAccrualRecordListVO> headVo = fcAccrualRecordQueryDao.excelFcAccrualRecordHead(input, corpAuth);//表头信息
		String curCode = "";
		String curName = "";
		if(!EmptyUtils.isEmptyList(headVo)) {
			curCode = headVo.get(0).getCurId();
		}
		//将币别添加到查询条件中
		input.addFilter(MatchTypeEnum.EQ, "curId", curCode);
		List<FcAccrualRecordListVO> res = setVoInfo(input,corpAuth);//结果
		if(!EmptyUtils.isEmptyList(res)){
			curName = res.get(0).getCurName();
		}else {
			res = new ArrayList<>();
		}
		// 创建工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		String content = "手续费结转记录查询表";
		// 创建sheet
		HSSFSheet sheet = workbook.createSheet(content);
		// 创建Excel表中的内容
		String sDate = (String)input.getFormdata().get("GEd_tallyDate");
		String eDate = (String)input.getFormdata().get("LEd_tallyDate");
		this.createExcelHeader(workbook,sheet,sDate,eDate,curName);
		this.createExcelBody(workbook,sheet,res);
		return workbook;
	}
	public List<FcAccrualRecordListVO> setVoInfo(DataTablesInput input,List<String> corpAuth){
		//查询账号记账明细
		List<FcAccrualRecordListVO> accDtls=fcAccrualRecordQueryDao.excelFcAccrualRecordData(input,corpAuth);
		return accDtls;
	}
	/**表头*/
	public void createExcelHeader(HSSFWorkbook workbook, HSSFSheet sheet,String sDate,String eDate,String curName){
		// 第一行
		sheet.createRow(0);
		HSSFRow row1 = sheet.getRow(0);
		if(StringUtils.isEmpty(sDate)){
			sDate = "";
		}
		if(StringUtils.isEmpty(eDate)){
			eDate = "";
		}
		row1.createCell(0).setCellValue("记账日期区间:" + sDate+"-"+ eDate);
		row1.createCell(4).setCellValue("币别:" + curName);
		//第二行
		sheet.createRow(1);
		HSSFRow row2 = sheet.getRow(1);
		HSSFCell r = row2.createCell(0);
		r.setCellValue("流水号");
		HSSFCell r1 = row2.createCell(1);
		r1.setCellValue("凭证开始日期");
		HSSFCell r2 = row2.createCell(2);
		r2.setCellValue("凭证结束日期");
		HSSFCell r3 = row2.createCell(3);
		r3.setCellValue("计提账号");
		HSSFCell r4 = row2.createCell(4);
		r4.setCellValue("经办人代码");
		HSSFCell r5 = row2.createCell(5);
		r5.setCellValue("计提时间");
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
	}
	/**
	 * 创建Excel表内容
	 */
	private void createExcelBody(HSSFWorkbook workbook, HSSFSheet sheet, List<FcAccrualRecordListVO> list) {
		int index = 2;// 指向所在行
		for (FcAccrualRecordListVO res : list){
			sheet.createRow(index);
			HSSFRow row5 = sheet.getRow(index);
			HSSFCell r = row5.createCell(0);
			r.setCellValue(res.getTargetOrderNo());
			HSSFCell r1 = row5.createCell(1);
			r1.setCellValue(DateUtils.formatDate(res.getStartDate()));
			HSSFCell r2 = row5.createCell(2);
			r2.setCellValue(DateUtils.formatDate(res.getEndDate()));
			HSSFCell r3 = row5.createCell(3);
			r3.setCellValue(res.getAccount());
			HSSFCell r4 = row5.createCell(4);
			r4.setCellValue(res.getOperatorCode());
			HSSFCell r5 = row5.createCell(5);
			r5.setCellValue(DateUtils.formatDate(res.getAccrueTime()));
			index++;
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
}
