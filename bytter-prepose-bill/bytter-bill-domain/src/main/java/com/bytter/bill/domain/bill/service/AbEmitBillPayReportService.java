package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbEmitBillPayReportDao;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayReportVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @Title: Service
 * @Description: 到期兑付确认统计
 * @author onlineGenerator
 * @date 2018-01-15 16:35:25
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLPAY)
@Service("abEmitBillPayReportService")
@Transactional
public class AbEmitBillPayReportService extends BaseService<AbAccrecordsVO, String>{
	@Autowired
	private AbEmitBillPayReportDao abEmitBillPayReportDao;
	@Autowired
	private AbCorpService abCorpService;
	public BaseDao<AbAccrecordsVO, String> getEntityDao() {
		return abEmitBillPayReportDao;
	}
	/**
	 * 根据选择的统计时间范围生成表格列
	 * @param model
	 * @param statistical 统计方式
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 */
	@Transactional(readOnly = true)
	public void tableInit(Model model,String statistical,String startDate,String endDate){
		//获取生成报表的表头日期列
		if(startDate!=null&&endDate!=null){
			List<String> dateList = getDateList(statistical,startDate,endDate);
			model.addAttribute ("dateList",dateList);
			model.addAttribute ("listSize",dateList.size ());
		}
	}
	/**
	 * 根据时间范围获取表格列
	 * @param statistical
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Transactional(readOnly = true)
	protected List<String> getDateList(String statistical,String startDate,String endDate){
		List<String> list = new ArrayList<> ();//需要统计的月份或日期
		SimpleDateFormat sdf= new SimpleDateFormat(
				AccebillConstant.AB_STATISTICAL_TYPE0.equals (statistical)?"yyyy-MM":"yyyy-MM-dd");
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime (DateUtils.str2Date (startDate,sdf));
		calendar2.setTime (DateUtils.str2Date (endDate,sdf));
		int num = DateUtils.dateDiff (AccebillConstant.AB_STATISTICAL_TYPE0.equals (statistical)?'M':'d', calendar2, calendar1);
		list.add(startDate);
		for(int i=1;i<=num;i++){
			calendar1.add(AccebillConstant.AB_STATISTICAL_TYPE0.equals (statistical)?calendar1.MONTH:calendar1.DATE, 1);
			startDate = DateUtils.date2Str (calendar1.getTime (),sdf);
			list.add (startDate);
		}
		return list;
	}
	/**
	 * 加载兑付确认已完成报表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbEmitBillPayReport(DataTablesInput dataTablesInput,String statistical,String startDate,String endDate,String corpId) {
		/**得到按照统计方式和日期范围需要生成报表的列数*/
		List<String> dateList = new ArrayList<> ();
		if(startDate!=null&&endDate!=null){
			dateList = getDateList(statistical,startDate,endDate);
		}
		/**提取数据库已完成的兑付确认的数据*/
		List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_BILLPAYREPORT_QUERY_CORPOP,dataTablesInput.getCurrentUserId());
		Map<String,Object> params = new HashMap<String,Object>();
		Map<String,Object> tableParams = dataTablesInput.getFormdata ();
		dataTablesInput.getFormdata ().remove ("statistical");
		SimpleDateFormat formatSdf = new SimpleDateFormat(AccebillConstant.AB_STATISTICAL_TYPE0.equals (statistical)?"yyyy-MM":"yyyy-MM-dd");
        if(AccebillConstant.AB_STATISTICAL_TYPE0.equals (statistical)){
			params.put ("startDate",DateUtils.getMonthFirstDate(DateUtils.str2Date(startDate,formatSdf)));
			params.put ("endDate",DateUtils.getNextMonthFirstDate(DateUtils.str2Date(endDate,formatSdf)));
		}else{
			params.put ("startDate",DateUtils.str2Date(startDate,formatSdf));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime (DateUtils.str2Date (endDate,formatSdf));
			calendar.add(calendar.DATE,1);
			params.put ("endDate",calendar.getTime ());
		}
		dataTablesInput.getFormdata ().remove ("ged_businessDate");
		dataTablesInput.getFormdata ().remove ("led_businessDate");
		if(StringUtils.isNotBlank (tableParams.get ("eqs_billType")==null?null:tableParams.get ("eqs_billType").toString ())){
			params.put ("billType",tableParams.get ("eqs_billType"));
			dataTablesInput.getFormdata ().remove ("eqs_billType");
		}
		if(StringUtils.isNotBlank (tableParams.get ("eqs_billFlag")==null?null:tableParams.get ("eqs_billFlag").toString ())){
			params.put ("billFlag",tableParams.get ("eqs_billFlag"));
			dataTablesInput.getFormdata ().remove ("eqs_billFlag");
		}
		if(StringUtils.isNotBlank (corpId)){
			String[] corpArray = corpId.split (",");
			params.put ("corpId",corpArray);
			dataTablesInput.getFormdata ().remove ("ins_corpId");
		}
		if(StringUtils.isNotBlank (tableParams.get ("likes_acceptor")==null?null:tableParams.get ("likes_acceptor").toString ())){
			String acceptor = "%" + tableParams.get ("likes_acceptor") + "%";
			params.put("acceptor", acceptor);
			dataTablesInput.getFormdata ().remove ("likes_acceptor");
		}
		DataTablesOutput outPut = abEmitBillPayReportDao.listAbEmitBillPayReport(dataTablesInput, corpAuthList, statistical,params);
        /**得到按照承兑人需要生成报表的行数*/
		List<AbEmitBillPayReportVO> list = outPut.getData ();
		if(list==null || list.isEmpty ()){
			return outPut;
		}
		List<String> acceptorList = new ArrayList<String> ();
		acceptorList.add (list.get (0).getAcceptor ());
		boolean flag;
		for(AbEmitBillPayReportVO lis:list){
			flag = true;
			for(String name:acceptorList){
				if(name.equals (lis.getAcceptor ())){
					flag = false;break;
				}
			}
			if(flag){
				acceptorList.add (lis.getAcceptor ());
			}
		}
		/**组装表格数据*/
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>> ();//保存表格数据
		Map<String,Object> map;//保存单行数据
		Map<String,Object> lastMap = new HashMap<String,Object> ();//最后一行列合计
		lastMap.put("acceptor","合计");
		//行小计
		BigDecimal totalBillMoney,totalBailMoney,totalSurplusMoney;
		//列合计
		BigDecimal colBillMoney,colBailMoney,colSurplusMoney;
		//总计
		BigDecimal totalColBillMoney = BigDecimal.ZERO;//票面金额总计
		BigDecimal totalColBailMoney = BigDecimal.ZERO;//保证金总计
		BigDecimal totalColSurplusMoney = BigDecimal.ZERO;//敞口金额总计
		for(String acceptor:acceptorList){//行
			map = new HashMap<String,Object> ();
			totalBillMoney = BigDecimal.ZERO;
			totalBailMoney = BigDecimal.ZERO;
			totalSurplusMoney = BigDecimal.ZERO;
			map.put ("acceptor",acceptor);
			int j = 1;//直接用date拼接时，前台解析没有取到值，故加变量j
			for(String date:dateList) {//动态列
				colBillMoney = BigDecimal.ZERO;
				colBailMoney = BigDecimal.ZERO;
				colSurplusMoney = BigDecimal.ZERO;
				//flag = true;
				for(AbEmitBillPayReportVO lis:list){
					if(acceptor.equals (lis.getAcceptor ())&&date.equals (lis.getBusinessDate ())){
						//行和列对应数据时，保存行单元格数据
						map.put ("billMoney"+j,lis.getTotalBillMoney ());
						map.put ("bailMoney"+j,lis.getTotalBailMoney ());
						map.put ("surplusMoney"+j,lis.getSurplusMoney ());
						//行小计累加
						totalBillMoney = totalBillMoney.add (lis.getTotalBillMoney ());
						totalBailMoney = totalBailMoney.add (lis.getTotalBailMoney ());
						totalSurplusMoney = totalSurplusMoney.add (lis.getSurplusMoney ());
						colBillMoney = lis.getTotalBillMoney ();
						colBailMoney = lis.getTotalBailMoney ();
						colSurplusMoney = lis.getSurplusMoney ();
						list.remove (lis);//取完数据后移除，提高计算效率
						//flag = false;
						break;
					}
				}
				/*if(flag){
					//当前承兑人当前日期没有数据时以0补充  -- 前台会自动把空值格式化为0.00
					map.put ("billMoney"+j,BigDecimal.ZERO);
					map.put ("bailMoney"+j,BigDecimal.ZERO);
					map.put ("surplusMoney"+j,BigDecimal.ZERO);
				}*/
				//列合计累加
				lastMap.put("billMoney"+(j),colBillMoney.add(
						new BigDecimal (lastMap.get ("billMoney"+(j))==null?0.00:Double.parseDouble (lastMap.get ("billMoney"+(j)).toString ()))));
				lastMap.put("bailMoney"+(j),colBailMoney.add(
						new BigDecimal (lastMap.get ("bailMoney"+(j))==null?0.00:Double.parseDouble (lastMap.get ("bailMoney"+(j)).toString ()))));
				lastMap.put("surplusMoney"+(j),colSurplusMoney.add(
						new BigDecimal (lastMap.get ("surplusMoney"+(j))==null?0.00:Double.parseDouble (lastMap.get ("surplusMoney"+(j)).toString ()))));
				j++;
			}
			//行小计
			map.put ("totalBillMoney",totalBillMoney);
			map.put ("totalBailMoney",totalBailMoney);
			map.put ("totalSurplusMoney",totalSurplusMoney);
			//总计累加
			totalColBillMoney = totalColBillMoney.add (totalBillMoney);
			totalColBailMoney = totalColBailMoney.add (totalBailMoney);
			totalColSurplusMoney = totalColSurplusMoney.add (totalSurplusMoney);
			mapList.add (map);
		}
		//最后一行的总计
		lastMap.put("totalBillMoney",totalColBillMoney);
		lastMap.put("totalBailMoney",totalColBailMoney);
		lastMap.put("totalSurplusMoney",totalColSurplusMoney);
		mapList.add(lastMap);
		outPut.setData (mapList);
		return outPut;
	}
}
