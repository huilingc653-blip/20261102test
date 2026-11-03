package com.bytter.biz.report.service;

import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.report.dao.NisAllocationDailyReportDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.service.IChoProductReqService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IEquityMaintenanceService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.EquityMaintenanceDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 集团月度资金划拨分析表
 * @date 2024-05-16
 */
@Service("groupMonthFundaAllocationReportService")
@Transactional
public class GroupMonthFundAllocationReportService extends BaseService<BisExcDTO, String> {
	@Autowired
	private NisAllocationDailyReportDao nisAllocationDailyReportDao;
	@Autowired
	private NisAllocationReqService nisAllocationReqService;
	@Autowired
	private NisBusTypeService nisBusTypeService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IEquityMaintenanceService equityMaintenanceService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IChoProductReqService choProductReqService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@Override
	public BaseDao<BisExcDTO, String> getEntityDao() {
		return nisAllocationDailyReportDao;
	}
	/**
	 * 资金日报表列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		//查询日期
		Date queryDate = DateUtils.parse(String.valueOf(dataTablesInput.getFormdata().get("queryDate")), "yyyy-MM");
		dataTablesInput.getFormdata().remove("queryDate");
		String corpId = (String)dataTablesInput.getFormdata().get("ins_corpId");

		//获取有效的股权信息
		List<EquityMaintenanceDTO> equityMaintenanceDTOS = equityMaintenanceService.queryAll();
		if(EmptyUtils.isNotEmpty(equityMaintenanceDTOS)){
			List<String> ids;
			if(StringUtils.isNotBlank(corpId)){
				ids = Arrays.asList(corpId.split(","));
			}else{
				//根据资源，用户id 获取单位id
				ids = corpService.getUserCorpIds(ResourceConstant.CHO_CHOPRODUCT_NISALLOCATIONDAILYREPORT_QUERY, user.getId());
			}
			List<CorpDTO> corpList = null;
			if(EmptyUtils.isNotEmpty(ids)){
				corpList = corpService.listCorpDTOByCorpIds2(ids);
				if(EmptyUtils.isNotEmpty(corpList) && null != queryDate){
					//按单位封装列表数据
					this.packData(corpList, queryDate);
					this.groupStatData(equityMaintenanceDTOS,corpList);
				}
			}
		}

		DataTablesOutput dataTablesOutput = new DataTablesOutput();
		dataTablesOutput.setDraw(dataTablesInput.getDraw());
		dataTablesOutput.setData(equityMaintenanceDTOS);
		return dataTablesOutput;
	}

	/**
	 * 封装数据
	 * @param corpList
	 * @param queryDate
	 */
	public void packData(List<CorpDTO> corpList, Date queryDate){
		//获取查询日期前一月日期(环比日期)
		Date beforeMonthDate = DateUtils.addMonth(queryDate,-1);
		//获取查询日期前一年日期（同比日期）
		Date beforeYearDate = DateUtils.addYear(queryDate,-1);
		//业务类型id
		List<String> busTypes = new ArrayList<>();
		//业务类型键值对
		Map<String,String> nisBusTypeMap = new HashMap<>();
		//获取有效的业务类型
		List<NisBusTypeVO> nisBusTypeList = nisBusTypeService.getNisBusType(NisBusTypeConstant.VALID_TRUE);
		for(NisBusTypeVO nisBusTypeVO : nisBusTypeList){
			if(NisBusTypeConstant.SYS_BUS_TYPE_BS005.equals(nisBusTypeVO.getBusType()) ||
					NisBusTypeConstant.SYS_BUS_TYPE_BS006.equals(nisBusTypeVO.getBusType()) ||
					NisBusTypeConstant.SYS_BUS_TYPE_BS007.equals(nisBusTypeVO.getBusType()) ||
					NisBusTypeConstant.SYS_BUS_TYPE_BS008.equals(nisBusTypeVO.getBusType()) ||
					NisBusTypeConstant.SYS_BUS_TYPE_BS009.equals(nisBusTypeVO.getBusType()) ||
					NisBusTypeConstant.SYS_BUS_TYPE_BS010.equals(nisBusTypeVO.getBusType())){
				busTypes.add(nisBusTypeVO.getId());
				nisBusTypeMap.put(nisBusTypeVO.getBusType(),nisBusTypeVO.getId());
			}
		}
		//获取单位id集合
		List<String> corpIds = corpList.stream().map(CorpDTO::getId).collect(Collectors.toList());
		//根据查询日期、调拨类型获取调拨单据
		List<BisAccDtlDTO> bisAccDtlDTOS = bisAccDtlService.listBisAccDtlByCorpIdsQueryDateBusTypes(corpIds,queryDate,busTypes);
		//获取资金调拨同比数据
		List<BisAccDtlDTO> tongBiList= bisAccDtlService.listBisAccDtlByCorpIdsQueryDateBusTypes(corpIds,beforeYearDate,busTypes);
		//获取资金调拨环比数据
		List<BisAccDtlDTO> huanBiList= bisAccDtlService.listBisAccDtlByCorpIdsQueryDateBusTypes(corpIds,beforeMonthDate,busTypes);
		//根据单位id、调拨类型过滤同比单据
		for(CorpDTO corpDTO : corpList){
			//根据单位id、调拨类型过滤单据集合
			List<BisAccDtlDTO> upwardList= bisAccDtlDTOS.stream().filter(s -> corpDTO.getId().equals(s.getCorpId())
					&& (nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS005).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS006).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS007).equals(s.getBusType()))).collect(Collectors.toList());
			//根据单位id、调拨类型过滤单据
			List<BisAccDtlDTO> allocationList= bisAccDtlDTOS.stream().filter(s -> corpDTO.getId().equals(s.getCorpId())
					&& (nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS008).equals(s.getBusType())
							|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS009).equals(s.getBusType())
							|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS010).equals(s.getBusType()))).collect(Collectors.toList());

			//统计查询日期上划金额和
			BigDecimal sumUpward = upwardList.stream().map(BisAccDtlDTO::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			//设置资金上划金额
			corpDTO.setSumUpward(sumUpward);
			//统计查询日期下拨金额和
			BigDecimal sumAllocation = allocationList.stream().map(BisAccDtlDTO::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			//设置资金下拨金额
			corpDTO.setSumAllocation(sumAllocation);
			//留存金额 = 上划金额 - 下拨金额
			BigDecimal retainedAmount = sumUpward.subtract(sumAllocation);
			//设置留存金额
			corpDTO.setRetainedAmount(retainedAmount);

			//根据单位id、调拨类型过滤同比单据
			List<BisAccDtlDTO> tongBiUpwardList= tongBiList.stream().filter(s -> corpDTO.getId().equals(s.getCorpId())
					&& (nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS005).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS006).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS007).equals(s.getBusType()))).collect(Collectors.toList());
			//根据单位id、调拨类型过滤单据集合
			List<BisAccDtlDTO> tongBiAllocationList= tongBiList.stream().filter(s -> corpDTO.getId().equals(s.getCorpId())
					&& (nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS008).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS009).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS010).equals(s.getBusType()))).collect(Collectors.toList());

			//统计同比日期上划金额和
			BigDecimal tongBiSumUpward = tongBiUpwardList.stream().map(BisAccDtlDTO::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			corpDTO.setTongBiSumUpward(tongBiSumUpward);

			//统计同比日期下拨金额和
			BigDecimal tongBiSumAllocation = tongBiAllocationList.stream().map(BisAccDtlDTO::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			corpDTO.setTongBiSumAllocation(tongBiSumAllocation);

			//同比留存金额 = 同比上划金额 - 同比下拨金额
			BigDecimal tongBiRetainedAmount = tongBiSumUpward.subtract(tongBiSumAllocation);
			corpDTO.setTongBiRetainedAmount(tongBiRetainedAmount);

			List<BisAccDtlDTO> huanBiUpwardList= huanBiList.stream().filter(s -> corpDTO.getId().equals(s.getCorpId())
					&& (nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS005).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS006).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS007).equals(s.getBusType()))).collect(Collectors.toList());
			//根据单位id、调拨类型过滤单据集合
			List<BisAccDtlDTO> huanBiAllocationList= huanBiList.stream().filter(s -> corpDTO.getId().equals(s.getCorpId())
					&& (nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS008).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS009).equals(s.getBusType())
					|| nisBusTypeMap.get(NisBusTypeConstant.SYS_BUS_TYPE_BS010).equals(s.getBusType()))).collect(Collectors.toList());

			//统计环比日期上划金额和
			BigDecimal huanBiSumUpward = huanBiUpwardList.stream().map(BisAccDtlDTO::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			corpDTO.setHuanBiSumUpward(huanBiSumUpward);

			//统计环比日期下拨金额和
			BigDecimal huanBiSumAllocation = huanBiAllocationList.stream().map(BisAccDtlDTO::getAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
			corpDTO.setHuanBiSumAllocation(huanBiSumAllocation);

			//环比留存金额 = 环比上划金额 - 环比下拨金额
			BigDecimal huanBiRetainedAmount = huanBiSumUpward.subtract(huanBiSumAllocation);
			corpDTO.setHuanBiRetainedAmount(huanBiRetainedAmount);

		}
	}

	/**
	 * 百分比计算
	 * @param money1
	 * @param money2
	 * @return
	 */
	public BigDecimal calculatePercentage(BigDecimal money1,BigDecimal money2){
		if (money1.intValue() == 0 && money2.intValue() != 0) {
			return new BigDecimal(-100.00);
		}
		if (money2.intValue() == 0 && money1.intValue() != 0) {
			return new BigDecimal(+100.00);
		}
		if(money2.compareTo(BigDecimal.ZERO) == 0){
			return BigDecimal.ZERO;
		}
		BigDecimal percentage = BigDecimal.ZERO;

		percentage = money1.divide(money2, 4, BigDecimal.ROUND_HALF_UP);
		percentage = percentage.multiply(new BigDecimal(100));
		return percentage;
	}

	/**
	 * 分组统计数据
	 * @param equityMaintenanceList
	 * @param corpList
	 */
	public void groupStatData(List<EquityMaintenanceDTO> equityMaintenanceList, List<CorpDTO> corpList){
		BigDecimal statSumUpward = BigDecimal.ZERO;
		BigDecimal statHuanBiUpward = BigDecimal.ZERO;
		BigDecimal statTongBiUpward = BigDecimal.ZERO;
		BigDecimal statSumAllocation = BigDecimal.ZERO;
		BigDecimal statHuanBiAllocation = BigDecimal.ZERO;
		BigDecimal statTongBiAllocation = BigDecimal.ZERO;
		BigDecimal statRetainedAmount = BigDecimal.ZERO;
		BigDecimal statHuanBiRetained = BigDecimal.ZERO;
		BigDecimal statTongBiRetained = BigDecimal.ZERO;
		BigDecimal statRetentionRate = BigDecimal.ZERO;
		for(EquityMaintenanceDTO equityMaintenanceDTO : equityMaintenanceList){
			List<CorpDTO> list = corpList.stream().filter(s -> equityMaintenanceDTO.getEquityCode().equals(s.getEquityMaintenanceId()))
					.collect(Collectors.toList());
			//资金上划金额汇总
			BigDecimal sumUpward = list.stream().map(CorpDTO::getSumUpward).reduce(BigDecimal.ZERO, BigDecimal::add);
			equityMaintenanceDTO.setSumUpward(sumUpward);
			statSumUpward = statSumUpward.add(sumUpward);
			//资金上划环比
			BigDecimal huanBiSumUpward = list.stream().map(CorpDTO::getHuanBiSumUpward).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal huanBiUpward = this.calculatePercentage(sumUpward,huanBiSumUpward);
			equityMaintenanceDTO.setHuanBiUpward(huanBiUpward);
			statHuanBiUpward = statHuanBiUpward.add(huanBiUpward);
			//资金上划同比
			BigDecimal tongBiSumUpward = list.stream().map(CorpDTO::getTongBiSumUpward).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal tongBiUpward = this.calculatePercentage(sumUpward,tongBiSumUpward);
			equityMaintenanceDTO.setTongBiUpward(tongBiUpward);
			statTongBiUpward = statTongBiUpward.add(tongBiUpward);
			//资金下拨金额汇总
			BigDecimal sumAllocation = list.stream().map(CorpDTO::getSumAllocation).reduce(BigDecimal.ZERO, BigDecimal::add);
			equityMaintenanceDTO.setSumAllocation(sumAllocation);
			statSumAllocation = statSumAllocation.add(sumAllocation);
			//资金下拨环比
			BigDecimal huanBiSumAllocation = list.stream().map(CorpDTO::getHuanBiSumAllocation).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal huanBiAllocation = this.calculatePercentage(sumAllocation,huanBiSumAllocation);
			equityMaintenanceDTO.setHuanBiAllocation(huanBiAllocation);
			statHuanBiAllocation = statHuanBiAllocation.add(huanBiAllocation);
			//资金下拨同比
			BigDecimal tongBiSumAllocation = list.stream().map(CorpDTO::getTongBiSumAllocation).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal tongBiAllocation = this.calculatePercentage(sumAllocation,tongBiSumAllocation);
			equityMaintenanceDTO.setTongBiAllocation(tongBiAllocation);
			statTongBiAllocation = statTongBiAllocation.add(tongBiAllocation);
			//留存金额
			BigDecimal sumRetainedAmount = list.stream().map(CorpDTO::getRetainedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			equityMaintenanceDTO.setRetainedAmount(sumRetainedAmount);
			statRetainedAmount = statRetainedAmount.add(sumRetainedAmount);
			//留存环比
			BigDecimal huanBiSumRetainedAmount = list.stream().map(CorpDTO::getHuanBiRetainedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal huanBiRetained = this.calculatePercentage(sumRetainedAmount,huanBiSumRetainedAmount);
			equityMaintenanceDTO.setHuanBiRetained(huanBiRetained);
			statHuanBiRetained = statHuanBiRetained.add(huanBiRetained);
			//留存同比
			BigDecimal tongBiSumRetainedAmount = list.stream().map(CorpDTO::getTongBiRetainedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal tongBiRetained = this.calculatePercentage(sumRetainedAmount,tongBiSumRetainedAmount);
			equityMaintenanceDTO.setTongBiRetained(tongBiRetained);
			statTongBiRetained = statTongBiRetained.add(tongBiRetained);
			//留存率
			BigDecimal retentionRate = this.calculatePercentage(sumRetainedAmount, sumUpward);
			equityMaintenanceDTO.setRetentionRate(retentionRate);
			statRetentionRate = statRetentionRate.add(retentionRate);
		}

		EquityMaintenanceDTO total = new EquityMaintenanceDTO();
		total.setEquityName("合计");
		total.setSumUpward(statSumUpward);
		//资金上划环比
		total.setHuanBiUpward(statHuanBiUpward);
		//资金上划同比
		total.setTongBiUpward(statTongBiUpward);
		//资金下拨金额汇总
		total.setSumAllocation(statSumAllocation);
		//资金下拨环比
		total.setHuanBiAllocation(statHuanBiAllocation);
		//资金下拨同比
		total.setTongBiAllocation(statTongBiAllocation);
		//留存金额
		total.setRetainedAmount(statRetainedAmount);
		//留存环比
		total.setHuanBiRetained(statHuanBiRetained);
		//留存同比
		total.setTongBiRetained(statTongBiRetained);
		//留存率
		total.setRetentionRate(statRetentionRate);
		equityMaintenanceList.add(total);
	}
}
