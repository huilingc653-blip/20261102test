package com.bytter.biz.report.service;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.InnerAccChangingDTO;
import com.bytter.biz.report.dao.InnerAccChangingDao;
import com.bytter.biz.report.vo.InnerAccChangingVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
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
import java.util.List;
/**
 * @author zhangxb
 * @Description 内部账户改户
 */
@Service("innerAccChangingService")
@Transactional
public class InnerAccChangingService  extends BaseService<InnerAccChangingDTO, String>{
	
	@Autowired
	private InnerAccChangingDao innerAccChangingDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@Override
	public BaseDao<InnerAccChangingDTO, String> getEntityDao() {
		// TODO Auto-generated method stub
		return innerAccChangingDao;
	}
	
	/**内部账户变化列表**/
	public List<InnerAccChangingVO> getList(String startTime,String endTime,String corpId,String netId, ICurrentUser user){
		List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_CHANGING_QUERY, user.getId());
		List<InnerAccChangingVO> list=innerAccChangingDao.getList(startTime,endTime,corpId,netId,corpAuth);
		for(InnerAccChangingVO vo:list){
			String corpName=corpService.getCorpName(vo.getCorpId());
			String netName=corpService.getCorpName(vo.getNetId());
			vo.setCorpName(corpName);
			vo.setNetName(netName);
		}
		return list;
	}
	
	/**明细表**/
	public List<InnerAccChangingVO> getAccByCorpId(String corpId,String startTime,String endTime,String type){
		List<InnerAccChangingVO> list=innerAccChangingDao.getAccByCorpId(corpId,startTime,endTime,type);
		for(InnerAccChangingVO vo:list){
			String corpName=corpService.getCorpName(vo.getCorpId());
			String netName=corpService.getCorpName(vo.getNetId());
			vo.setCorpName(corpName);
			vo.setNetName(netName);
			BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(vo.getCurId());
			vo.setCurName(btCurrencyDTO.getCurName());
		}
		return list;
	}
	
	/**更改**/
	public List<InnerAccChangingVO> getHistoryByAccount(String account,String startTime,String endTime){
		return innerAccChangingDao.getHistoryByAccount(account,startTime,endTime);
	}
	
	/**冻结/解冻**/
	public List<InnerAccChangingVO> getFreezeByAccount(String account,String startTime,String endTime,String freezeSign){
		return innerAccChangingDao.getFreezeByAccount(account,startTime,endTime,freezeSign);
	}
	/**
	 * 导出
	 */
	public HSSFWorkbook createExcelContent(List<InnerAccChangingVO> res){
		// 创建工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		String content = "内部账户增减变动情况表";
		// 创建sheet
		HSSFSheet sheet = workbook.createSheet(content);
		// 创建Excel表中的内容
		this.createExcelHeader(sheet);
		this.createExcelBody(sheet,res);
		return workbook;
	}
	/**表头*/
	public void createExcelHeader(HSSFSheet sheet){
		// 第一行
		sheet.createRow(0);
		HSSFRow row1 = sheet.getRow(0);
		row1.createCell(0).setCellValue("网点");
		row1.createCell(1).setCellValue("单位");
		row1.createCell(2).setCellValue("内部账户增减变动");
		//第二行
		sheet.createRow(1);
		HSSFRow row2 = sheet.getRow(1);
		HSSFCell r = row2.createCell(2);
		r.setCellValue("开户");
		HSSFCell r1 = row2.createCell(3);
		r1.setCellValue("变更");
		HSSFCell r2 = row2.createCell(4);
		r2.setCellValue("注销");
		HSSFCell r3 = row2.createCell(5);
		r3.setCellValue("冻结");
		HSSFCell r4 = row2.createCell(6);
		r4.setCellValue("解冻");
		sheet.addMergedRegion(new CellRangeAddress(0,1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0,1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0,0, 2, 6));
	}
	/**
	 * 创建Excel表内容
	 */
	private void createExcelBody(HSSFSheet sheet, List<InnerAccChangingVO> list) {
		int index = 2;// 指向所在行
		for (InnerAccChangingVO res : list){
			sheet.createRow(index);
			HSSFRow row5 = sheet.getRow(index);
			HSSFCell r = row5.createCell(0);
			r.setCellValue(res.getNetName());
			HSSFCell r1 = row5.createCell(1);
			r1.setCellValue(res.getCorpName());
			HSSFCell r2 = row5.createCell(2);
			r2.setCellValue(res.getAcc());
			HSSFCell r3 = row5.createCell(3);
			r3.setCellValue(res.getHistory());
			HSSFCell r4 = row5.createCell(4);
			r4.setCellValue(res.getWriteoff());
			HSSFCell r5 = row5.createCell(5);
			r5.setCellValue(res.getFreeze());
			HSSFCell r6 = row5.createCell(6);
			r6.setCellValue(res.getThaw());
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
