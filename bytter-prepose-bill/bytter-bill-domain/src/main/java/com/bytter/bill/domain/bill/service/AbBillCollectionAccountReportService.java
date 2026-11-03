package com.bytter.bill.domain.bill.service;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillCollectionAccountReportDao;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountReportVo;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @Title: Service
 * @Description: 到期托收到账统计
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 *
 */
@Service("abBillCollectionAccountReportService")
@Transactional
public class AbBillCollectionAccountReportService extends BaseService<AbBillCollectionAccountVo, String> {
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbBillCollectionAccountReportDao abBillCollectionAccountReportDao;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	/**
	 * 托收到账统计列表查询
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,String countType) {
		Map<String, Object> formData = input.getFormdata();
		List<Map<String, Object>> list = new ArrayList<>();// 将accountReportList的数据进行改造后存放到list中
		String startDate = (String) formData.get("startDate");// 开始日期
		String endDate = (String) formData.get("endDate");// 结束日期
		Map<String, Object> map = null;// 存放每个承兑人的托收到账记录,例(acceptor="tom","2018-01"=1000.00
										// ...,sumMoney=3000.00)
		BigDecimal sumMoney = new BigDecimal(0);// 合计金额
		List<String> corpAuthList = abCorpService.getUserCorpIds(
				AccebillConstant.AB_BILL_RESOURCE_COLLECTIONACCOUNTREPORT_CORPOP, input.getCurrentUserId());
		input.getFormdata().put("isCollectionApplyConfirm",abVoucherinfoViceService.isCollectionApplyConfirm());
		List<AbBillCollectionAccountReportVo> accountReportList = abBillCollectionAccountReportDao
				.getDataTablesUnion(input, corpAuthList,countType);
		List<String> dateList = getDateList(countType, startDate, endDate);// 获取日期集合
		String acceptor=null;
		for (AbBillCollectionAccountReportVo vo : accountReportList) {
			if(StringUtils.isEmpty(acceptor)||!acceptor.equals(vo.getAcceptor())){
				if(map==null){
					map = new HashMap<>();
					sumMoney = vo.getMoney();
				}else{
					map.put("sumMoney", sumMoney);// 合计金额
					map = new HashMap<>();
					sumMoney = vo.getMoney();
				}
				map.put("acceptor", vo.getAcceptor());// 承兑人
				for (String date : dateList) {
					map.put(date, BigDecimal.valueOf(0));
				}
				list.add(map);
			}else{
				sumMoney=sumMoney.add(vo.getMoney());
			}
			map.put(vo.getHandleDate(), vo.getMoney());
		}
		if(map!=null){
			map.put("sumMoney", sumMoney);// 合计金额
		}
//		int i=0;
//		for (AbBillCollectionAccountReportVo vo : accountReportList) {
//			
//			if (map == null) {// 第一笔数据
//				map = new HashMap<>();
//				map.put("acceptor", vo.getAcceptor());// 承兑人
//				map.put(vo.getHandleDate(), vo.getMoney());// 业务办理日期,票据金额
//				map.put("sumMoney", vo.getMoney());// 合计金额
//				sumMoney = vo.getMoney();
//				i++;
//				continue;
//			}
//			if (!EmptyUtils.isEmpty(map) && !EmptyUtils.isEmpty(vo.getAcceptor()) && vo.getAcceptor().equals(map.get("acceptor"))) {// 承兑人与上一笔数据相同
//				map.put(vo.getHandleDate(), vo.getMoney());
//				sumMoney = sumMoney.add(vo.getMoney());
//				map.put("sumMoney", sumMoney);
//			} else {
//				for (String date : dateList) {// 遍历map，将之前未设置金额的日期赋值为0
//					if (!map.containsKey(date)) {
//						map.put(date, BigDecimal.valueOf(0));
//					}
//				}
//				list.add(map);// 将之前的map加进list中
//				
//				sumMoney = new BigDecimal(0);// 承兑人与上笔不同,合计金额清零。进行之后的承兑人合计金额统计
//				map = new HashMap<>();
//				map.put("acceptor", vo.getAcceptor());// 承兑人
//				map.put(vo.getHandleDate(), vo.getMoney());// 业务办理日期,票据金额
//				map.put("sumMoney", vo.getMoney());// 合计金额
//				sumMoney = vo.getMoney();
//			}
//			i++;
//			if(i==accountReportList.size()){
//				for (String date : dateList) {// 遍历map，将之前未设置金额的日期赋值为0
//					if (!map.containsKey(date)) {
//						map.put(date, BigDecimal.valueOf(0));
//					}
//				}
//				list.add(map);
//			}
//
//		}
		return new DataTablesOutput(0, list, 0, 0);
	}
	/**
	 * 获取托收到账统计的一些页面信息
	 * 
	 * @param model
	 */
	public void getSumCollectionAccountReportList(Model model, AbBillCollectionAccountReportVo accountReportVo) {
		// 日期集合
		List<String> dateList = new ArrayList<String>();
		try {
			dateList = getDateList(accountReportVo.getCountType(), accountReportVo.getStartDate(),
					accountReportVo.getEndDate());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("dateList", dateList);// 日期集合
		model.addAttribute("querySign", "1");// 查询参数
		model.addAttribute("countType", accountReportVo.getCountType());// 统计方式
		model.addAttribute("startDate", accountReportVo.getStartDate());// 开始日期
		model.addAttribute("endDate", accountReportVo.getEndDate());// 结束日期
		model.addAttribute("corpId", accountReportVo.getCorpId());// 单位id
		model.addAttribute("billType", accountReportVo.getBillType());// 票据类型
		model.addAttribute("billFlag", accountReportVo.getBillFlag());// 流通介质
		if(StringUtils.isNotEmpty(accountReportVo.getAcceptor())){
			try {
				model.addAttribute("acceptor", URLDecoder.decode(accountReportVo.getAcceptor(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
	}
	/**
	 * 根据日期区间获取日期列表集合
	 * 
	 * @param countType
	 *            0:按月统计,1:按天统计
	 * @param startDateStr
	 *            开始日期
	 * @param endDateStr
	 *            结束日期
	 * @return
	 */
	public static List<String> getDateList(String countType, String startDateStr, String endDateStr) {
		SimpleDateFormat sdFormat;
		char flag;
		if (AccebillConstant.AB_STATISTICAL_TYPE0.equals(countType)) {// 按月
			sdFormat = new SimpleDateFormat("yyyy-MM");
			flag = 'M';
		} else {// 按日
			sdFormat = new SimpleDateFormat("yyyy-MM-dd");
			flag = 'd';
		}
		// 当前日期日历对象
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		Date startDate = DateUtils.str2Date(startDateStr, sdFormat);
		Date endDate = DateUtils.str2Date(endDateStr, sdFormat);
		// 开始日期的日历对象
		calendar1.setTime(startDate);
		// 结束日期的日历对象
		calendar2.setTime(endDate);
		List<String> dateList = new ArrayList<String>();// 日期集合
		if (startDateStr != null && endDateStr != null) {
			int days = DateUtils.dateDiff(flag, calendar2, calendar1);
			dateList.add(sdFormat.format(calendar1.getTime()));
			int type = AccebillConstant.AB_STATISTICAL_TYPE0.equals(countType) ? Calendar.MONTH : Calendar.DAY_OF_MONTH;
			for (int i = 0; i < days; i++) {
				calendar1.add(type, 1);
				dateList.add(sdFormat.format(calendar1.getTime()));
			}
		}
		return dateList;
	}
	@Override
	public BaseDao<AbBillCollectionAccountVo, String> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}
}
