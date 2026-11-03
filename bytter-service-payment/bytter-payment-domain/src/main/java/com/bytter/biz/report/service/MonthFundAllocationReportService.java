package com.bytter.biz.report.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.service.IChoProductReqService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IEquityMaintenanceService;
import com.bytter.system.api.IOperationAreaService;
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
 * @Description: 月度资金划拨明细表
 * @date 2024-05-16
 */
@Service("monthFundaAllocationReportService")
@Transactional
public class MonthFundAllocationReportService {
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
	@DubboReference(check = false)
	private IOperationAreaService operationAreaService;
	@Autowired
	private NisBusTypeService nisBusTypeService;

	/**
	 * 月度资金划拨明细表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		// 查询单位
		String queryCorpId = (String)dataTablesInput.getFormdata().get("ins_corpId");
		List<String> queryCorpList = new ArrayList<String>();
		if(!EmptyUtils.isEmpty(queryCorpId)){
			String[] queryCorpIds = queryCorpId.split(",");
			queryCorpList = Arrays.asList(queryCorpIds);
		}

		// 查询日期
		Date queryDateBegin = DateUtils.parse(String.valueOf(dataTablesInput.getFormdata().get("queryDateBegin")), "yyyy-MM-dd");
		Date queryDateEnd = DateUtils.parse(String.valueOf(dataTablesInput.getFormdata().get("queryDateEnd")), "yyyy-MM-dd");
		// 报表显示方式
		String showType = (String) dataTablesInput.getFormdata().get("showType");
		// 股份属性
		String equityMaintenance = (String) dataTablesInput.getFormdata().get("equityMaintenance");
		List<String> equityMaintenanceList = new ArrayList<>();
		if(!EmptyUtils.isEmpty(equityMaintenance)){
			String[] codes = equityMaintenance.split(",");
			equityMaintenanceList = Arrays.asList(codes);
		}

		//增加报表的集团总计
		List<CorpDTO> corpList = new ArrayList<>();
		CorpDTO corpTop = new CorpDTO();
		corpTop.setSupCorpName("总计");
		corpList.add(corpTop);

		//增加报表的股权维护大项合计
		List<EquityMaintenanceDTO> equityMaintenanceDTOS = equityMaintenanceService.queryAll();
		//放入map,后面方便统计
		Map<String, CorpDTO> corpSecondMap = new HashMap<String, CorpDTO>();
		for(EquityMaintenanceDTO equityMaintenanceDTO: equityMaintenanceDTOS){
			CorpDTO corpDTO = new CorpDTO();
			corpDTO.setSupCorpName(equityMaintenanceDTO.getEquityName() + "子公司合计");
			corpSecondMap.put(equityMaintenanceDTO.getEquityCode(), corpDTO);
			corpList.add(corpDTO);
		}

		// 100000单位
		CorpDTO corpDTO = corpService.getCorpByCode("100000");
		List<CorpDTO> corpDTOS = build100000GuQuan(corpDTO);
		build100000Data(corpList,queryCorpList,corpDTOS,corpSecondMap,equityMaintenanceList,queryDateBegin,queryDateEnd,corpTop);


		//增加报表的大区合计    二级单位统计
		List<CorpDTO> levelCorpList = corpService.listLevelCorpAndChildrenCorp(ResourceConstant.MONTH_FUND_ALLOCATION_REPORT_QUERY, user.getId(), 2);
		buildThreeData(corpList,queryCorpList,levelCorpList,equityMaintenanceList,equityMaintenanceDTOS,corpSecondMap,queryDateBegin,queryDateEnd,corpTop);


		//数据计算
		this.calculateDate(corpList);

		// 如果是汇总显示方式，过滤具体单位数据
		if (CommonConstant.NO.equals(showType)){
			corpList = corpList.stream().filter(obj -> ObjectUtil.isEmpty(obj.getName())).collect(Collectors.toList());
		}

		DataTablesOutput dataTablesOutput = new DataTablesOutput();
		dataTablesOutput.setDraw(dataTablesInput.getDraw());
		dataTablesOutput.setData(corpList);
		return dataTablesOutput;
	}

	private void build100000Data(List<CorpDTO> corpList, List<String> queryCorpList, List<CorpDTO> levelCorpList, Map<String, CorpDTO> corpSecondMap,List<String> equityMaintenanceList, Date queryDateBegin, Date queryDateEnd, CorpDTO corpTop) {
		for(CorpDTO levelCorp : levelCorpList){
			corpList.add(levelCorp);

			//三级单位统计
			List<CorpDTO> underCorpList = new ArrayList<>();
			underCorpList.add(levelCorp);

			//根据页面筛选单位进行过滤
			if(!EmptyUtils.isEmpty(queryCorpList)){
				List<String> finalQueryCorpList = queryCorpList;
				underCorpList = underCorpList.stream().filter(underCorp -> finalQueryCorpList.contains(underCorp.getId())).collect(Collectors.toList());
			}

			if (ObjectUtil.isEmpty(underCorpList)) continue;

			if (ObjectUtil.isNotEmpty(equityMaintenanceList) && !equityMaintenanceList.contains(levelCorp.getEquityMaintenanceId())) continue;

			//三级单位及下属单位数据查询统计
			this.queryDate(levelCorp, queryCorpList, queryDateBegin,queryDateEnd,levelCorp.getAccountGroup());

			//股权维护大项合计处理
			CorpDTO corpSecond = corpSecondMap.get(levelCorp.getEquityMaintenanceId());
			if(!EmptyUtils.isEmpty(corpSecond) && ObjectUtil.isNotEmpty(levelCorp.getAccountGroup())){
				this.sumData(corpSecond, levelCorp);
			}
			//集团总计处理
			if (ObjectUtil.isEmpty(levelCorp.getAccountGroup())){
				this.sumData(corpTop, levelCorp);
			}
		}
	}

	/***
	 * 100000单位特殊处理
	 * @param corp
	 * @return TODO
	 */
	private List<CorpDTO> build100000GuQuan(CorpDTO corp ) {
		List<CorpDTO> list = new ArrayList<>();
		CorpDTO corpDTO = new CorpDTO();
		BeanUtils.copyNotNullProperties(corp, corpDTO);
		corpDTO.setSupCorpName("集团合计");
		corpDTO.setName("");
		corpDTO.setOperationAreaName("");
		// 账号组
		corpDTO.setAccountGroup("");
		list.add(corpDTO);

		corpDTO = new CorpDTO();
		BeanUtils.copyNotNullProperties(corp, corpDTO);
		corpDTO.setSupCorpName("集团（总部）");
		corpDTO.setAccountGroup("D1023");
		list.add(corpDTO);

		corpDTO = new CorpDTO();
		BeanUtils.copyNotNullProperties(corp, corpDTO);
		corpDTO.setSupCorpName("集团（广州）");
		corpDTO.setAccountGroup("D1004");
		list.add(corpDTO);

		corpDTO = new CorpDTO();
		BeanUtils.copyNotNullProperties(corp, corpDTO);
		corpDTO.setSupCorpName("集团（花都）");

		corpDTO.setAccountGroup("D1002");
		list.add(corpDTO);

		corpDTO = new CorpDTO();
		BeanUtils.copyNotNullProperties(corp, corpDTO);
		corpDTO.setSupCorpName("集团（番禺）");

		corpDTO.setAccountGroup("D1003");
		list.add(corpDTO);

		return list;
	}

	/***
	 * TODO
	 * @param corpList 返回数据
	 * @param queryCorpList 查询条件单位ID
	 * @param levelCorpList 二级单位List
	 * @param equityMaintenanceList 查询条件股份属性
	 * @param equityMaintenanceDTOS 所有股权属性列表
	 * @param corpSecondMap 股权合计Map
	 * @param queryDateBegin 查询开始日期
	 * @param queryDateEnd 查询结束日期
	 * @param corpTop 顶级总计单位
	 * @return TODO
	 */
	private void buildThreeData(List<CorpDTO> corpList,List<String> queryCorpList,List<CorpDTO> levelCorpList, List<String> equityMaintenanceList,
								List<EquityMaintenanceDTO> equityMaintenanceDTOS,Map<String, CorpDTO> corpSecondMap,Date queryDateBegin,Date queryDateEnd,
								CorpDTO corpTop) {
		for(CorpDTO levelCorp : levelCorpList){
			levelCorp.setSupCorpName(levelCorp.getName()+"合计");
			levelCorp.setName("");
			corpList.add(levelCorp);

			//三级单位统计
			List<CorpDTO> underCorpList = corpService.listCorpByParentId(levelCorp.getId(),equityMaintenanceList);
			if(Objects.isNull(underCorpList)){
				underCorpList = new ArrayList<>();
			}

			// 去除underCorpList中code为100000的单位
			underCorpList = underCorpList.stream().filter(underCorp -> !"100000".equals(underCorp.getCode())).collect(Collectors.toList());

			//根据页面筛选单位进行过滤
			if(!EmptyUtils.isEmpty(queryCorpList)){
				List<String> finalQueryCorpList = queryCorpList;
				underCorpList = underCorpList.stream().filter(underCorp -> finalQueryCorpList.contains(underCorp.getId())).collect(Collectors.toList());
			}

			//根据股权维护分组和数据查询包装
			Map<String,List<CorpDTO>> map = new HashMap<String,List<CorpDTO>>();
			for(CorpDTO underCorp : underCorpList){
				underCorp.setSupCorpName(underCorp.getProvinceName());

				//三级单位及下属单位数据查询统计
				this.queryDate(underCorp, queryCorpList, queryDateBegin,queryDateEnd,"");

				//分组
				if(map.containsKey(underCorp.getEquityMaintenanceId())){
					map.get(underCorp.getEquityMaintenanceId()).add(underCorp);
				}else{
					List<CorpDTO> newCorpDTO = new ArrayList<CorpDTO>();
					newCorpDTO.add(underCorp);
					map.put(underCorp.getEquityMaintenanceId(),newCorpDTO);
				}
			}

			//增加报表的股权维护分类小计
			for(EquityMaintenanceDTO equityMaintenanceDTO:equityMaintenanceDTOS){
				CorpDTO equityMaintenanceCorpDTO = new CorpDTO();
				equityMaintenanceCorpDTO.setSupCorpName(equityMaintenanceDTO.getEquityName()+"小计");
				corpList.add(equityMaintenanceCorpDTO);

				//增加报表的明细项
				List<CorpDTO> corpDTOS = map.get(equityMaintenanceDTO.getEquityCode());
				if(!EmptyUtils.isEmpty(corpDTOS)){
					for(CorpDTO corpusDTO : corpDTOS){
						this.sumData(equityMaintenanceCorpDTO, corpusDTO);
						corpList.add(corpusDTO);
					}
				}

				//二级单位合计处理
				this.sumData(levelCorp, equityMaintenanceCorpDTO);
				//股权维护大项合计处理
				CorpDTO corpSecond = corpSecondMap.get(equityMaintenanceDTO.getEquityCode());
				if(!EmptyUtils.isEmpty(corpSecond)){
					this.sumData(corpSecond, equityMaintenanceCorpDTO);
				}
			}
			//集团总计处理
			this.sumData(corpTop, levelCorp);
		}
	}

	/**
	 *	三级单位及下属单位数据查询统计
	 * @param corpDTO  需要进行包装数据的对象
	 * @param queryCorpList  页面进行筛选的单位Id集合
	 * @param queryDateBegin  查询日期开始
	 * @param queryDateEnd  查询日期结束
	 */
	public void queryDate(CorpDTO corpDTO, List<String> queryCorpList, Date queryDateBegin,Date queryDateEnd,String accountGroup){
		//获取三级单位及下属单位
		List<CorpDTO> lastCorpList = corpService.listUnderCorpByParentCorpIdSkipLevel(corpDTO.getId());
		//根据页面筛选单位进行过滤
		List<String> lastCorpIdList = lastCorpList.stream().map(CorpDTO::getId).collect(Collectors.toList());
		if(!EmptyUtils.isEmpty(queryCorpList)){
			lastCorpIdList = lastCorpIdList.stream().filter(queryCorpList::contains).collect(Collectors.toList());
		}
		lastCorpIdList.add(corpDTO.getId());

		List<String> busTypes = new ArrayList<String>();
		NisBusTypeDTO busTypeDTO5 = nisBusTypeService.getByBusType(NisBusTypeConstant.SYS_BUS_TYPE_BS005);
		if(!EmptyUtils.isEmpty(busTypeDTO5)){
			busTypes.add(busTypeDTO5.getId());
		}
		NisBusTypeDTO busTypeDTO6 = nisBusTypeService.getByBusType(NisBusTypeConstant.SYS_BUS_TYPE_BS006);
		if(!EmptyUtils.isEmpty(busTypeDTO6)){
			busTypes.add(busTypeDTO6.getId());
		}
		NisBusTypeDTO busTypeDTO7 = nisBusTypeService.getByBusType(NisBusTypeConstant.SYS_BUS_TYPE_BS007);
		if(!EmptyUtils.isEmpty(busTypeDTO7)){
			busTypes.add(busTypeDTO7.getId());
		}
		NisBusTypeDTO busTypeDTO8 = nisBusTypeService.getByBusType(NisBusTypeConstant.SYS_BUS_TYPE_BS008);
		if(!EmptyUtils.isEmpty(busTypeDTO8)){
			busTypes.add(busTypeDTO8.getId());
		}
		NisBusTypeDTO busTypeDTO9 = nisBusTypeService.getByBusType(NisBusTypeConstant.SYS_BUS_TYPE_BS009);
		if(!EmptyUtils.isEmpty(busTypeDTO9)){
			busTypes.add(busTypeDTO9.getId());
		}
		NisBusTypeDTO busTypeDTO10 = nisBusTypeService.getByBusType(NisBusTypeConstant.SYS_BUS_TYPE_BS010);
		if(!EmptyUtils.isEmpty(busTypeDTO10)){
			busTypes.add(busTypeDTO10.getId());
		}

		//进行数据查询
		List<BisAccDtlDTO> bisAccDtlDTOList = bisAccDtlService.listBisAccDtlByCorpIdsQueryDateBusTypes(lastCorpIdList, busTypes,queryDateBegin,queryDateEnd,accountGroup);
		//数据封装
		this.packData(corpDTO, bisAccDtlDTOList);
	}

	/**
	 * 数据封装
	 * @param corpDTO  封装对象
	 * @param bisAccDtlDTOList  数据列表
	 */
	public void packData(CorpDTO corpDTO, List<BisAccDtlDTO> bisAccDtlDTOList){
		//当期数据封装  资金上划(资金系统上划+银行上划+自主上划)  资金下拨（资金系统下拨+银行下拨+自主下拨）  账户收入  账户支出
		for(BisAccDtlDTO bisAccDtlDTO : bisAccDtlDTOList){
			String busTypeId = bisAccDtlDTO.getBusType();
			String busType = "";
			NisBusTypeDTO busTypeDTO = nisBusTypeService.getById(busTypeId);
			if(!EmptyUtils.isEmpty(busTypeDTO)){
				busType = busTypeDTO.getBusType();
			}
			BigDecimal amt = bisAccDtlDTO.getAmt();
			if(EmptyUtils.isEmpty(amt)){
				amt = BigDecimal.ZERO;
			}

			if(NisBusTypeConstant.SYS_BUS_TYPE_BS005.equals(busType) || NisBusTypeConstant.SYS_BUS_TYPE_BS006.equals(busType) || NisBusTypeConstant.SYS_BUS_TYPE_BS007.equals(busType)){
				//资金上划统计
				BigDecimal sumUpward = corpDTO.getSumUpward();
				if(EmptyUtils.isEmpty(sumUpward)){
					sumUpward = BigDecimal.ZERO;
				}
				corpDTO.setSumUpward(sumUpward.add(amt));

				if(NisBusTypeConstant.SYS_BUS_TYPE_BS005.equals(busType)){
					//资金系统上划
					BigDecimal sumUpwardSystem = corpDTO.getSumUpwardSystem();
					if(EmptyUtils.isEmpty(sumUpwardSystem)){
						sumUpwardSystem = BigDecimal.ZERO;
					}
					corpDTO.setSumUpwardSystem(sumUpwardSystem.add(amt));
				}else if(NisBusTypeConstant.SYS_BUS_TYPE_BS006.equals(busType)){
					//银行上划
					BigDecimal sumUpwardBank = corpDTO.getSumUpwardBank();
					if(EmptyUtils.isEmpty(sumUpwardBank)){
						sumUpwardBank = BigDecimal.ZERO;
					}
					corpDTO.setSumUpwardBank(sumUpwardBank.add(amt));
				}else if(NisBusTypeConstant.SYS_BUS_TYPE_BS007.equals(busType)){
					//自主上划
					BigDecimal sumUpwardActive = corpDTO.getSumUpwardActive();
					if(EmptyUtils.isEmpty(sumUpwardActive)){
						sumUpwardActive = BigDecimal.ZERO;
					}
					corpDTO.setSumUpwardActive(sumUpwardActive.add(amt));
				}
			}else if(NisBusTypeConstant.SYS_BUS_TYPE_BS008.equals(busType) || NisBusTypeConstant.SYS_BUS_TYPE_BS009.equals(busType) || NisBusTypeConstant.SYS_BUS_TYPE_BS010.equals(busType)){
				//资金下拨统计(集合统计)
				BigDecimal sumAllocation = corpDTO.getSumAllocation();
				if(EmptyUtils.isEmpty(sumAllocation)){
					sumAllocation = BigDecimal.ZERO;
				}
				corpDTO.setSumAllocation(sumAllocation.add(amt));

				if(NisBusTypeConstant.SYS_BUS_TYPE_BS008.equals(busType)){
					//资金系统上划
					BigDecimal sumAllocationSystem = corpDTO.getSumAllocationSystem();
					if(EmptyUtils.isEmpty(sumAllocationSystem)){
						sumAllocationSystem = BigDecimal.ZERO;
					}
					corpDTO.setSumAllocationSystem(sumAllocationSystem.add(amt));
				}else if(NisBusTypeConstant.SYS_BUS_TYPE_BS009.equals(busType)){
					//银行上划
					BigDecimal sumAllocationBank = corpDTO.getSumAllocationBank();
					if(EmptyUtils.isEmpty(sumAllocationBank)){
						sumAllocationBank = BigDecimal.ZERO;
					}
					corpDTO.setSumAllocationBank(sumAllocationBank.add(amt));
				}else if(NisBusTypeConstant.SYS_BUS_TYPE_BS010.equals(busType)){
					//自主上划
					BigDecimal sumAllocationActive = corpDTO.getSumAllocationActive();
					if(EmptyUtils.isEmpty(sumAllocationActive)){
						sumAllocationActive = BigDecimal.ZERO;
					}
					corpDTO.setSumAllocationActive(sumAllocationActive.add(amt));
				}
			}

			String cdSign = bisAccDtlDTO.getCdSign();
			if(NisBusTypeConstant.CD_SIGN_PAYER.equals(cdSign)){
				//账户支出
				BigDecimal accountPayOut = corpDTO.getAccountPayOut();
				if(EmptyUtils.isEmpty(accountPayOut)){
					accountPayOut = BigDecimal.ZERO;
				}
				corpDTO.setAccountPayOut(accountPayOut.add(amt));
			}else{
				//账户收入
				BigDecimal accountIncome = corpDTO.getAccountIncome();
				if(EmptyUtils.isEmpty(accountIncome)){
					accountIncome = BigDecimal.ZERO;
				}
				corpDTO.setAccountIncome(accountIncome.add(amt));
			}
		}
	}

	/**
	 * 数据总计
	 * @param corpDTO
	 * @param corpDTONext
	 */
	public void sumData(CorpDTO corpDTO, CorpDTO corpDTONext){
		//当期数据封装  资金上划(资金系统上划+银行上划+自主上划)  资金下拨（资金系统下拨+银行下拨+自主下拨）  账户收入  账户支出
		//资金上划统计（合计统计）
		BigDecimal sumUpward = corpDTO.getSumUpward();
		if(EmptyUtils.isEmpty(sumUpward)){
			sumUpward = BigDecimal.ZERO;
		}
		BigDecimal sumUpward1 = corpDTONext.getSumUpward();
		if(EmptyUtils.isEmpty(sumUpward1)){
			sumUpward1 = BigDecimal.ZERO;
			corpDTONext.setSumUpward(sumUpward1);
		}
		corpDTO.setSumUpward(sumUpward.add(sumUpward1));
		//资金系统上划统计
		BigDecimal sumUpwardSystem = corpDTO.getSumUpwardSystem();
		if(EmptyUtils.isEmpty(sumUpwardSystem)){
			sumUpwardSystem = BigDecimal.ZERO;
		}
		BigDecimal sumUpwardSystem1 = corpDTONext.getSumUpwardSystem();
		if(EmptyUtils.isEmpty(sumUpwardSystem1)){
			sumUpwardSystem1 = BigDecimal.ZERO;
			corpDTONext.setSumUpwardSystem(sumUpwardSystem1);
		}
		corpDTO.setSumUpwardSystem(sumUpwardSystem.add(sumUpwardSystem1));
		//银行上划统计
		BigDecimal sumUpwardBank = corpDTO.getSumUpwardBank();
		if(EmptyUtils.isEmpty(sumUpwardBank)){
			sumUpwardBank = BigDecimal.ZERO;
		}
		BigDecimal sumUpwardBank1 = corpDTONext.getSumUpwardBank();
		if(EmptyUtils.isEmpty(sumUpwardBank1)){
			sumUpwardBank1 = BigDecimal.ZERO;
			corpDTONext.setSumUpwardBank(sumUpwardBank1);
		}
		corpDTO.setSumUpwardBank(sumUpwardBank.add(sumUpwardBank1));
		//自主上划统计
		BigDecimal sumUpwardActive = corpDTO.getSumUpwardActive();
		if(EmptyUtils.isEmpty(sumUpwardActive)){
			sumUpwardActive = BigDecimal.ZERO;
		}
		BigDecimal sumUpwardActive1 = corpDTONext.getSumUpwardActive();
		if(EmptyUtils.isEmpty(sumUpwardActive1)){
			sumUpwardActive1 = BigDecimal.ZERO;
			corpDTONext.setSumUpwardActive(sumUpwardActive1);
		}
		corpDTO.setSumUpwardActive(sumUpwardActive.add(sumUpwardActive1));

		//资金下拨统计(合计统计)
		BigDecimal sumAllocation = corpDTO.getSumAllocation();
		if(EmptyUtils.isEmpty(sumAllocation)){
			sumAllocation = BigDecimal.ZERO;
		}
		BigDecimal sumAllocation1 = corpDTONext.getSumAllocation();
		if(EmptyUtils.isEmpty(sumAllocation1)){
			sumAllocation1 = BigDecimal.ZERO;
			corpDTONext.setSumAllocation(sumAllocation1);
		}
		corpDTO.setSumAllocation(sumAllocation.add(sumAllocation1));
		//资金系统下拨统计
		BigDecimal sumAllocationSystem = corpDTO.getSumAllocationSystem();
		if(EmptyUtils.isEmpty(sumAllocationSystem)){
			sumAllocationSystem = BigDecimal.ZERO;
		}
		BigDecimal sumAllocationSystem1 = corpDTONext.getSumAllocationSystem();
		if(EmptyUtils.isEmpty(sumAllocationSystem1)){
			sumAllocationSystem1 = BigDecimal.ZERO;
			corpDTONext.setSumAllocationSystem(sumAllocationSystem1);
		}
		corpDTO.setSumAllocationSystem(sumAllocationSystem.add(sumAllocationSystem1));
		//银行下拨统计
		BigDecimal sumAllocationBank = corpDTO.getSumAllocationBank();
		if(EmptyUtils.isEmpty(sumAllocationBank)){
			sumAllocationBank = BigDecimal.ZERO;
		}
		BigDecimal sumAllocationBank1 = corpDTONext.getSumAllocationBank();
		if(EmptyUtils.isEmpty(sumAllocationBank1)){
			sumAllocationBank1 = BigDecimal.ZERO;
			corpDTONext.setSumAllocationBank(sumAllocationBank1);
		}
		corpDTO.setSumAllocationBank(sumAllocationBank.add(sumAllocationBank1));
		//自主下拨统计
		BigDecimal sumAllocationActive = corpDTO.getSumAllocationActive();
		if(EmptyUtils.isEmpty(sumAllocationActive)){
			sumAllocationActive = BigDecimal.ZERO;
		}
		BigDecimal sumAllocationActive1 = corpDTONext.getSumAllocationActive();
		if(EmptyUtils.isEmpty(sumAllocationActive1)){
			sumAllocationActive1 = BigDecimal.ZERO;
			corpDTONext.setSumAllocationActive(sumAllocationActive1);
		}
		corpDTO.setSumAllocationActive(sumAllocationActive.add(sumAllocationActive1));

		//账户支出
		BigDecimal accountPayOut = corpDTO.getAccountPayOut();
		if(EmptyUtils.isEmpty(accountPayOut)){
			accountPayOut = BigDecimal.ZERO;
		}
		BigDecimal accountPayOut1 = corpDTONext.getAccountPayOut();
		if(EmptyUtils.isEmpty(accountPayOut1)){
			accountPayOut1 = BigDecimal.ZERO;
			corpDTONext.setAccountPayOut(accountPayOut1);
		}
		corpDTO.setAccountPayOut(accountPayOut.add(accountPayOut1));
		//账户收入
		BigDecimal accountIncome = corpDTO.getAccountIncome();
		if(EmptyUtils.isEmpty(accountIncome)){
			accountIncome = BigDecimal.ZERO;
		}
		BigDecimal accountIncome1 = corpDTONext.getAccountIncome();
		if(EmptyUtils.isEmpty(accountIncome1)){
			accountIncome1 = BigDecimal.ZERO;
			corpDTONext.setAccountIncome(accountIncome1);
		}
		corpDTO.setAccountIncome(accountIncome.add(accountIncome1));
	}

	/**
	 * 数据计算
	 * @param corpList
	 */
	public void calculateDate(List<CorpDTO> corpList){
		//计算资金留存   +   是否超额下拨
		for(CorpDTO corpDTO : corpList){
			BigDecimal sumUpward =corpDTO.getSumUpward();
			if(EmptyUtils.isEmpty(sumUpward)){
				sumUpward = BigDecimal.ZERO;
				corpDTO.setSumUpward(sumUpward);
			}
			BigDecimal sumAllocation = corpDTO.getSumAllocation();
			if(EmptyUtils.isEmpty(sumAllocation)){
				sumAllocation = BigDecimal.ZERO;
				corpDTO.setSumAllocation(sumAllocation);
			}
			corpDTO.setRetainedAmount(corpDTO.getSumUpward().subtract(corpDTO.getSumAllocation()));
			if(corpDTO.getRetainedAmount().compareTo(BigDecimal.ZERO) < 0){
				corpDTO.setIsExcess("是");
			}else{
				corpDTO.setIsExcess("否");
			}
		}
	}

	/**
	 * 计算百分比
	 * @param money1
	 * @param money2
	 * @return
	 */
	public BigDecimal calculatePercentage(BigDecimal money1,BigDecimal money2){
		if(money2.compareTo(BigDecimal.ZERO) == 0){
			return BigDecimal.ZERO;
		}
		return money1.divide(money2, 2, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
	}
}
