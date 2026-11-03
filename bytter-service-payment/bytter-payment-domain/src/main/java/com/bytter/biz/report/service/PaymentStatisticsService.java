package com.bytter.biz.report.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.netbank.dao.NisBillDao;
import com.bytter.biz.netbank.dao.NisBillheadDao;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.report.dao.NisAllocationDailyReportDao;
import com.bytter.biz.report.vo.AccountManagementStatisticsVO;
import com.bytter.biz.report.vo.PaymentStatisticsMonthVO;
import com.bytter.biz.report.vo.PaymentStatisticsVO;
import com.bytter.cloud.common.core.util.DateUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.service.IChoProductReqService;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiangw
 * @version 1.0
 * @Title: Service
 * @Description: 支付统计表
 * @date 2024-08-14
 */
@Service("paymentStatisticsService")
@Transactional
public class PaymentStatisticsService extends BaseService<NisBillhead, String> {
	@Autowired
	private NisBillheadDao nisBillheadDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private NisBillheadService nisBillheadService;
	@Override
	public BaseDao<NisBillhead, String> getEntityDao() {
		return nisBillheadDao;
	}
	/**
	 * 月度资金划拨汇总表查询
	 *
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<PaymentStatisticsVO> getList(PaymentStatisticsVO vo, ICurrentUser user) {
		String queryCorpId = vo.getCorpId();
		List<String> queryCorpList = new ArrayList<String>();
		if(!EmptyUtils.isEmpty(queryCorpId)){
			String[] queryCorpIds = queryCorpId.split(",");
			queryCorpList = Arrays.asList(queryCorpIds);
		}

		//增加报表的集团总计
		List<PaymentStatisticsVO> paymentStatisticsVOS = new ArrayList<PaymentStatisticsVO>();
		PaymentStatisticsVO top = new PaymentStatisticsVO();
		top.setAreaName("集团合计");
		top.setIsSum(1);
		paymentStatisticsVOS.add(top);

		//集团合计
		List<BigDecimal> topCounts = new ArrayList<BigDecimal>();
		List<PaymentStatisticsMonthVO> paymentStatisticsMonthVOS = getMonthList(vo.getBeginDate(), vo.getEndDate());
		for(PaymentStatisticsMonthVO paymentStatisticsMonthVO : paymentStatisticsMonthVOS){
			topCounts.add(BigDecimal.ZERO);
			topCounts.add(BigDecimal.ZERO);
		}

		//总部合计-特殊处理-单位名称为大参林医药集团股份有限公司
		CorpDTO corpDTO = corpService.getCorpIdByName("大参林医药集团股份有限公司");
		boolean result1 = false;
		if(!EmptyUtils.isEmpty(queryCorpList)){
			if(queryCorpList.contains(corpDTO.getId())){
				result1 = true;
			}
		}else{
			result1 = true;
		}
		if(result1){

		}

		//增加报表的大区合计    二级单位统计
		List<CorpDTO> levelCorpList = corpService.listLevelCorpAndChildrenCorp(ResourceConstant.MONTH_FUND_ALLOCATION_COLLECT_REPORT_QUERY, user.getId(), 2);
		for(CorpDTO levelCorp : levelCorpList){
			PaymentStatisticsVO second = new PaymentStatisticsVO();
			second.setAreaName(levelCorp.getName()+"合计");
			second.setIsSum(1);
			paymentStatisticsVOS.add(second);

			//二级大区合计
			List<BigDecimal> secondCounts = new ArrayList<BigDecimal>();
			for(PaymentStatisticsMonthVO paymentStatisticsMonthVO1 : paymentStatisticsMonthVOS){
				secondCounts.add(BigDecimal.ZERO);
				secondCounts.add(BigDecimal.ZERO);
			}

			//获取二级单位及下属单位
			List<CorpDTO> lastCorpList = corpService.listUnderCorpByParentCorpIdSkipLevel(levelCorp.getId());
			for(CorpDTO lastCorp: lastCorpList){
				//根据页面筛选单位进行过滤
				boolean result = false;
				if(!EmptyUtils.isEmpty(queryCorpList)){
					if(queryCorpList.contains(lastCorp.getId())){
						result = true;
					}
				}else{
					result = true;
				}
				if(result){
					PaymentStatisticsVO last = new PaymentStatisticsVO();
					last.setAreaName(levelCorp.getName());
					last.setCorpName(lastCorp.getName());
					last.setCorpId(lastCorp.getId());
					//剔除二级单位
					if(!lastCorp.getId().equals(levelCorp.getId())) {
						paymentStatisticsVOS.add(last);
					}

					//查询数据  现款	0 票据 1
					List<BigDecimal> counts1 = new ArrayList<BigDecimal>();
					List<BigDecimal> counts2 = new ArrayList<BigDecimal>();
					BigDecimal sum1 = BigDecimal.ZERO;
					BigDecimal sum2 = BigDecimal.ZERO;
					for(PaymentStatisticsMonthVO paymentStatisticsMonthVO2: paymentStatisticsMonthVOS){
						if(paymentStatisticsMonthVO2.getIsSum()==0){
							String beginMonth = paymentStatisticsMonthVO2.getMonth();
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
							try {
								calendar.setTime(simpleDateFormat.parse(beginMonth));
								calendar.add(Calendar.MONTH, 1);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							String endMonth = simpleDateFormat.format(calendar.getTime());
							BigDecimal count1 = nisBillheadDao.getCountNisBillHead(lastCorp.getId(), "0", beginMonth, endMonth);
							counts1.add(count1);
							sum1 = sum1.add(count1);
							BigDecimal count2 = nisBillheadDao.getCountNisBillHead(lastCorp.getId(), "1", beginMonth, endMonth);
							counts2.add(count2);
							sum2 = sum2.add(count2);
						}else{
							counts1.add(sum1);
							sum1 = BigDecimal.ZERO;
							counts2.add(sum2);
							sum2 = BigDecimal.ZERO;
						}
					}
					List<BigDecimal> counts = new ArrayList<BigDecimal>(counts1);
					counts.addAll(counts2);
					last.setDataList(counts);

					//汇总
					for(int i = 0; i < counts.size(); i++){
						BigDecimal secondCount = secondCounts.get(i);
						secondCount = secondCount.add(counts.get(i));
						secondCounts.set(i, secondCount);
						BigDecimal topCount = topCounts.get(i);
						topCount = topCount.add(counts.get(i));
						topCounts.set(i, topCount);
					}
				}
			}
			second.setDataList(secondCounts);
		}
		top.setDataList(topCounts);
		// 省区赋值
		for (PaymentStatisticsVO  paymentStatisticsVO : paymentStatisticsVOS){
			if (ObjectUtil.isNotEmpty(paymentStatisticsVO.getCorpId())){
				CorpDTO tempCorp = corpService.get(paymentStatisticsVO.getCorpId());
				paymentStatisticsVO.setProvinceName(tempCorp.getProvinceName());
			}
		}
		return paymentStatisticsVOS;
	}

	public List<String> getTitleList(String beginDate, String endDate) {
		List<Integer> months = getMonths(beginDate, endDate);
		List<String> titleList1 = new ArrayList<String>();
		List<String> titleList2 = new ArrayList<String>();
		for(int i=0; i<months.size(); i++){
			Integer month = months.get(i);
			if (month > 12) {
				month = month % 12;
				if (month == 0) {
					month = 12;
				}
			}
			titleList1.add(month.toString()+"月支付现汇总笔数");
			titleList2.add(month.toString()+"月银承出票总笔数");
			//取余，判断
			int a = month % 3;
			if(a == 0){
				int b = month / 3;
				if(b == 1){
					titleList1.add("第一季度现汇小计");
					titleList2.add("第一季度票据小计");
				}else if(b == 2){
					titleList1.add("第二季度现汇小计");
					titleList2.add("第二季度票据小计");
				}else if(b == 3){
					titleList1.add("第三季度现汇小计");
					titleList2.add("第三季度票据小计");
				}else if(b == 4){
					titleList1.add("第四季度现汇小计");
					titleList2.add("第四季度票据小计");
				}
			}else{
				if(i == (months.size()-1)){
					int b = month / 3;
					if(b == 0){
						titleList1.add("第一季度现汇小计");
						titleList2.add("第一季度票据小计");
					}else if(b == 1){
						titleList1.add("第二季度现汇小计");
						titleList2.add("第二季度票据小计");
					}else if(b == 2){
						titleList1.add("第三季度现汇小计");
						titleList2.add("第三季度票据小计");
					}else if(b == 3){
						titleList1.add("第四季度现汇小计");
						titleList2.add("第四季度票据小计");
					}
				}
			}
		}
		List<String> titleList0 = new ArrayList<String>();
		titleList0.add("大区");
		titleList0.add("省区");
		titleList0.add("营运区");
		List<String> titleList = new ArrayList<String>(titleList0);
		titleList.addAll(titleList1);
		titleList.addAll(titleList2);
		return titleList;
	}

	public List<PaymentStatisticsMonthVO> getMonthList(String beginDate, String endDate) {
		List<Integer> months = getMonths(beginDate, endDate);
		List<String> monthsS = getMonthsS(beginDate, endDate);
		List<PaymentStatisticsMonthVO> monthList = new ArrayList<PaymentStatisticsMonthVO>();
		for(int i=0; i<months.size(); i++){
			Integer month = months.get(i);
			String monthS = monthsS.get(i);
			PaymentStatisticsMonthVO paymentStatisticsMonthVO = new PaymentStatisticsMonthVO();
			paymentStatisticsMonthVO.setMonth(monthS);
			paymentStatisticsMonthVO.setIsSum(0);
			monthList.add(paymentStatisticsMonthVO);
			//取余，判断
			int a = month % 3;
			if(a == 0){
				PaymentStatisticsMonthVO paymentStatisticsMonthVO1 = new PaymentStatisticsMonthVO();
				paymentStatisticsMonthVO1.setMonth("合计");
				paymentStatisticsMonthVO1.setIsSum(1);
				monthList.add(paymentStatisticsMonthVO1);
			}else{
				if(i == (months.size()-1)){
					PaymentStatisticsMonthVO paymentStatisticsMonthVO1 = new PaymentStatisticsMonthVO();
					paymentStatisticsMonthVO1.setMonth("合计");
					paymentStatisticsMonthVO1.setIsSum(1);
					monthList.add(paymentStatisticsMonthVO1);
				}
			}
		}
		return monthList;
	}

	/**
	 * 获取月份之间集合，存在跨年的问题
	 *
	 * @param beginDate 查询开始时间
	 * @param endDate 	查询结束时间
	 * @return 月份集合
	 */
	public List<Integer> getMonths(String beginDate, String endDate){
		int num = 3; // 默认查询时间跨度三个月
		Date bDate = null;
		if (!EmptyUtils.isBlank(beginDate) && !EmptyUtils.isBlank(endDate)) {
			bDate = DateUtil.parseDate(beginDate, "yyyy-MM");
			Date eDate = DateUtil.parseDate(endDate, "yyyy-MM");
			// 查询时间开始结束日期都不为空
			num = DateUtil.getMonthDiff(bDate, eDate) + 1;
		} else if (EmptyUtils.isBlank(beginDate) && EmptyUtils.isBlank(endDate)) {
			// 查询时间开始结束日期都为空
			bDate = DateUtil.addMonth(DateUtil.nowDate(), 1 - num);
		} else if (!EmptyUtils.isBlank(beginDate) && EmptyUtils.isBlank(endDate)) {
			// 查询时间开始日期不为空，结束日期为空
			bDate = DateUtil.parseDate(beginDate, "yyyy-MM");
		} else if (EmptyUtils.isBlank(beginDate) && !EmptyUtils.isBlank(endDate)) {
			// 查询时间开始日期为空，结束日期不为空
			bDate = DateUtil.addMonth(DateUtil.parseDate(endDate, "yyyy-MM"), 1 - num);
		}

		int bm = DateUtil.getMonth(bDate);
		List<Integer> months = Lists.newArrayList();
		for(int i = 0; i < num; i++){
			months.add(bm);
			bm = bm + 1;
		}
		return months;
	}

	/**
	 * 获取月份之间集合，存在跨年的问题
	 *
	 * @param beginDate 查询开始时间
	 * @param endDate 	查询结束时间
	 * @return 月份集合
	 */
	public List<String> getMonthsS(String beginDate, String endDate){
		int num = 3; // 默认查询时间跨度三个月
		Date bDate = null;
		if (!EmptyUtils.isBlank(beginDate) && !EmptyUtils.isBlank(endDate)) {
			bDate = DateUtil.parseDate(beginDate, "yyyy-MM");
			Date eDate = DateUtil.parseDate(endDate, "yyyy-MM");
			// 查询时间开始结束日期都不为空
			num = DateUtil.getMonthDiff(bDate, eDate) + 1;
		} else if (EmptyUtils.isBlank(beginDate) && EmptyUtils.isBlank(endDate)) {
			// 查询时间开始结束日期都为空
			bDate = DateUtil.addMonth(DateUtil.nowDate(), 1 - num);
		} else if (!EmptyUtils.isBlank(beginDate) && EmptyUtils.isBlank(endDate)) {
			// 查询时间开始日期不为空，结束日期为空
			bDate = DateUtil.parseDate(beginDate, "yyyy-MM");
		} else if (EmptyUtils.isBlank(beginDate) && !EmptyUtils.isBlank(endDate)) {
			// 查询时间开始日期为空，结束日期不为空
			bDate = DateUtil.addMonth(DateUtil.parseDate(endDate, "yyyy-MM"), 1 - num);
		}

		List<String> months = Lists.newArrayList();
		for(int i = 0; i < num; i++){
			Date d = DateUtil.addMonth(bDate, i);
			months.add(DateUtil.formatDate(d, "yyyy-MM"));
		}
		return months;
	}

}