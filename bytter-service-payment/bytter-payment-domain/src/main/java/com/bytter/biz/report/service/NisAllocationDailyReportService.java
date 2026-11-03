package com.bytter.biz.report.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.sql.visitor.functions.If;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.report.dao.NisAllocationDailyReportDao;
import com.bytter.biz.report.vo.FundDailyReportVO;
import com.bytter.biz.report.vo.IncomeAndExpenditureOfFundsVo;
import com.bytter.biz.report.vo.IncomeExpenditureReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.ChoProductReqDTO;
import com.bytter.increment.api.service.IChoProductReqService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICellManageService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CellManageDto;
import com.bytter.system.api.dto.CorpDTO;
import io.reactivex.processors.FlowableProcessor;
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
 * @Description: 资金日报表
 * @date 2024-05-14
 */
@Service("nisAllocationDailyReportService")
@Transactional
public class NisAllocationDailyReportService extends BaseService<BisExcDTO, String> {
	@Autowired
	private NisAllocationDailyReportDao nisAllocationDailyReportDao;
	@Autowired
	private NisBusTypeService nisBusTypeService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IChoProductReqService choProductReqService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@DubboReference(check = false)
	private ICellManageService cellManageService;

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
		Date queryDate = DateUtils.parse(String.valueOf(dataTablesInput.getFormdata().get("queryDate")), DateUtils.PATTERN_CLASSICAL_SIMPLE);
		dataTablesInput.getFormdata().remove("queryDate");
		//根据资源，用户id 获取单位id
		String corpId = (String)dataTablesInput.getFormdata().get("ins_corpId");
		List<String> ids = new ArrayList<>();
		if(StringUtils.isNotBlank(corpId)){
			ids = new ArrayList<>(Arrays.asList(corpId.split(",")));
			// 增加判断，如果选中的是3级单位，把他的下级也增加进来
			List<CorpDTO> list = corpService.listCorpDTOByCorpIds2(ids);
			for (CorpDTO corpDTO : list) {
				if (ObjectUtil.isNotEmpty(corpDTO.getParentIds()) && corpDTO.getParentIds().split(",").length == 3){
					// 查出它的子级单位
					List<CorpDTO> corpDTOList = corpService.getCorpByParentId(corpDTO.getId());
					if (ObjectUtil.isNotEmpty(corpDTOList)){
						for (CorpDTO temp : corpDTOList) {
							ids.add(temp.getId());
						}
					}
				}
			}

		}else{
			//根据资源，用户id 获取单位id
			ids = corpService.getUserCorpIds(ResourceConstant.CHO_CHOPRODUCT_NISALLOCATIONDAILYREPORT_QUERY, user.getId());
		}
		String equityMaintenance = (String) dataTablesInput.getFormdata().get("equityMaintenance");
		String showType = (String) dataTablesInput.getFormdata().get("showType");

		List<CorpDTO> corpList = null;
		if(EmptyUtils.isNotEmpty(ids)){
			corpList = nisAllocationDailyReportDao.queryData(ids,queryDate,equityMaintenance,null);
			corpList = handleGroupData(corpList,showType,queryDate);
			if (ObjectUtil.isNotEmpty(corpList) ){
				// 过滤parentIds通过逗号分割超过4层的数据
				corpList = corpList.stream().filter(corp -> {
					String parentIdsStr = corp.getParentIds();
					if (ObjectUtil.isEmpty(parentIdsStr)) { // parentIds为空直接保留
						return true;
					}
					String[] parentIds = parentIdsStr.split(",");
					return parentIds.length <= 3; // 允许最多3层
				}).collect(Collectors.toList());
			}
		}

		DataTablesOutput dataTablesOutput = new DataTablesOutput();
		dataTablesOutput.setDraw(dataTablesInput.getDraw());
		dataTablesOutput.setData(corpList);
		return dataTablesOutput;
	}

	private List<CorpDTO> handleGroupData(List<CorpDTO> corpList,String showType,Date queryDate) {
		List<CorpDTO> resultList = new ArrayList<>();
		if (ObjectUtil.isNotEmpty(corpList)){
			// 整理数据
			CorpDTO corp100 = corpService.getCorpByCode("100");
			CorpDTO corp1080 = corpService.getCorpByCode("1080");
			CorpDTO corp100000 = corpService.getCorpByCode("100000");
			// 把100单位的父ID设置为总部的
			List<CorpDTO> tempList = new ArrayList<>();
			// 根据股权属性增加小计
			Map<String, String> stockRightMap = new LinkedHashMap<>();
			// 单位的所有父节点
			Map<String,String> parentsMap = new LinkedHashMap<>();
			// 总部ID
			List<String> ids = new ArrayList<>();
			for (CorpDTO dto : corpList){
				if ("100".equals(dto.getCorpCode())){
					dto.setParentId(corp100000.getId());
					dto.setParentIds(corp1080.getId()+","+corp100000.getId());
					dto.setSupCorpName(corp100000.getName());
					ids.add(dto.getId());
				}
				if ("100000".equals(dto.getCorpCode())){
					ids.add(dto.getId());
				}
				// 排除大区
				if (!"6000".equals(dto.getUnitCm())){
					// 初始化0
					if (ObjectUtil.isEmpty(dto.getBeforeDayBalance())) dto.setBeforeDayBalance(BigDecimal.ZERO);
					if (ObjectUtil.isEmpty(dto.getCurrentBalance())) dto.setCurrentBalance(BigDecimal.ZERO);
					if (ObjectUtil.isEmpty(dto.getAvailBal())) dto.setAvailBal(BigDecimal.ZERO);
					if (ObjectUtil.isEmpty(dto.getFrzBal())) dto.setFrzBal(BigDecimal.ZERO);
					if (ObjectUtil.isEmpty(dto.getFinaMoney())) dto.setFinaMoney(BigDecimal.ZERO);

					tempList.add(dto);
					if (ObjectUtil.isNotEmpty(dto.getEquityMaintenanceId())){
						stockRightMap.put(dto.getEquityMaintenanceId(),dto.getEquityMaintenanceName());
					}
					parentsMap.put(dto.getId(),dto.getParentIds());
				}
			}
			// 重新赋值
			corpList = tempList;

			// 总数
			CorpDTO dto = new CorpDTO();
			dto.setSupCorpName("集团总计");
			dto.setBeforeDayBalance(corpList.stream().map(CorpDTO::getBeforeDayBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
			dto.setCurrentBalance(corpList.stream().map(CorpDTO::getCurrentBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
			dto.setAvailBal(corpList.stream().map(CorpDTO::getAvailBal).reduce(BigDecimal.ZERO, BigDecimal::add));
			dto.setFrzBal(corpList.stream().map(CorpDTO::getFrzBal).reduce(BigDecimal.ZERO, BigDecimal::add));
			dto.setFinaMoney(corpList.stream().map(CorpDTO::getFinaMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
			resultList.add(dto);

			// 股权合计
			for (Map.Entry<String,String> entry : stockRightMap.entrySet()){
				dto = new CorpDTO();// 股权合计
				dto.setSupCorpName(entry.getValue()+"子公司合计");
				BigDecimal beforeDayBalance = corpList.stream()
						.filter(obj -> entry.getKey().equals(obj.getEquityMaintenanceId()))
						.map(CorpDTO::getBeforeDayBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
				dto.setBeforeDayBalance(beforeDayBalance);
				BigDecimal currentBalance = corpList.stream()
						.filter(obj -> entry.getKey().equals(obj.getEquityMaintenanceId()))
						.map(CorpDTO::getCurrentBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
				dto.setCurrentBalance(currentBalance);
				BigDecimal availBal = corpList.stream()
						.filter(obj -> entry.getKey().equals(obj.getEquityMaintenanceId()))
						.map(CorpDTO::getAvailBal).reduce(BigDecimal.ZERO, BigDecimal::add);
				dto.setAvailBal(availBal);
				BigDecimal frzBal = corpList.stream()
						.filter(obj -> entry.getKey().equals(obj.getEquityMaintenanceId()))
						.map(CorpDTO::getFrzBal).reduce(BigDecimal.ZERO, BigDecimal::add);
				dto.setFrzBal(frzBal);
				BigDecimal finaMoney = corpList.stream()
						.filter(obj -> entry.getKey().equals(obj.getEquityMaintenanceId()))
						.map(CorpDTO::getFinaMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
				dto.setFinaMoney(finaMoney);
				resultList.add(dto);
			}

			// 查询所属网点的所有大区
			Map<String, String> supMap = nisAllocationDailyReportDao.querySupCorp();
			// 华南大区（单位编码为：1190）
			CorpDTO corp1190 = corpService.getCorpByCode("1190");
			if (ObjectUtil.isNotEmpty(supMap)){
				// 大区分组
				Map<String, List<CorpDTO>> supMapCollect = new LinkedHashMap<>();
				for (Map.Entry<String,String> sup : supMap.entrySet()){
					List<CorpDTO> list = new ArrayList<>();
					for (Map.Entry<String,String> entry : parentsMap.entrySet()){
						String id = entry.getKey();
						List<String> parentIds = Arrays.asList(entry.getValue().split(","));
						if (parentIds.contains(sup.getKey())){
							// 取出当前对象
							Optional<CorpDTO> optional = corpList.stream()
									.filter(obj -> id.equals(obj.getId()))
									.findFirst();
							list.add(optional.get());
						}
					}
					if (ObjectUtil.isNotEmpty(list)){
						supMapCollect.put(sup.getKey(),list);
					}
				}
				// 大区数据
				for (Map.Entry<String,List<CorpDTO>> entry : supMapCollect.entrySet()){
					List<CorpDTO> values = entry.getValue();
					values.forEach(item -> item.setSupCorpName(""));

					dto = new CorpDTO();// 大区数据
					dto.setSupCorpName(supMap.get(entry.getKey()));
					// 求和
					dto.setBeforeDayBalance(values.stream().map(CorpDTO::getBeforeDayBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
					dto.setCurrentBalance(values.stream().map(CorpDTO::getCurrentBalance).reduce(BigDecimal.ZERO, BigDecimal::add));
					dto.setAvailBal(values.stream().map(CorpDTO::getAvailBal).reduce(BigDecimal.ZERO, BigDecimal::add));
					dto.setFrzBal(values.stream().map(CorpDTO::getFrzBal).reduce(BigDecimal.ZERO, BigDecimal::add));
					dto.setFinaMoney(values.stream().map(CorpDTO::getFinaMoney).reduce(BigDecimal.ZERO, BigDecimal::add));

					resultList.add(dto);

					// 汇总父级不是大区的单位数据
					for (Map.Entry<String,String> entryP : parentsMap.entrySet()){
						String id = entryP.getKey();
						String[] split = entryP.getValue().split(",");
						List<String> parentIds = new ArrayList<>();
						for (String s : split){
							if (!id.equals(s)){
								parentIds.add(s);
							}
						}

						// 取出当前对象
						Optional<CorpDTO> optional = values.stream()
								.filter(obj -> id.equals(obj.getId()))
								.findFirst();
						if (optional.isPresent()){
							CorpDTO _currentDto = optional.get();
							for(String parentId : parentIds){
								for (CorpDTO parentDto : values){
									if (parentId.equals(parentDto.getId())){
										parentDto.setBeforeDayBalance(parentDto.getBeforeDayBalance().add(_currentDto.getBeforeDayBalance()));
										parentDto.setCurrentBalance(parentDto.getCurrentBalance().add(_currentDto.getCurrentBalance()));
										parentDto.setAvailBal(parentDto.getAvailBal().add(_currentDto.getAvailBal()));
										parentDto.setFrzBal(parentDto.getFrzBal().add(_currentDto.getFrzBal()));
										parentDto.setFinaMoney(parentDto.getFinaMoney().add(_currentDto.getFinaMoney()));
									}
								}
							}
						}
					}

					// 股权分组
					for (Map.Entry<String,String> stockEntry : stockRightMap.entrySet()){
						dto = new CorpDTO();// 股权小计
						dto.setSupCorpName("其中："+stockEntry.getValue()+"小计");
						// 如果不是华南大区，并且股权是院店的，则跳过
						if ((ObjectUtil.isEmpty(corp1190) || !entry.getKey().equals(corp1190.getId())) && "0003".equals(stockEntry.getKey())){
							continue;
						}

						BigDecimal beforeDayBalance = values.stream()
								.filter(obj -> stockEntry.getKey().equals(obj.getEquityMaintenanceId()))
								.map(CorpDTO::getBeforeDayBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
						dto.setBeforeDayBalance(beforeDayBalance);
						BigDecimal currentBalance = values.stream()
								.filter(obj -> stockEntry.getKey().equals(obj.getEquityMaintenanceId()))
								.map(CorpDTO::getCurrentBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
						dto.setCurrentBalance(currentBalance);
						BigDecimal availBal = values.stream()
								.filter(obj -> stockEntry.getKey().equals(obj.getEquityMaintenanceId()))
								.map(CorpDTO::getAvailBal).reduce(BigDecimal.ZERO, BigDecimal::add);
						dto.setAvailBal(availBal);
						BigDecimal frzBal = values.stream()
								.filter(obj -> stockEntry.getKey().equals(obj.getEquityMaintenanceId()))
								.map(CorpDTO::getFrzBal).reduce(BigDecimal.ZERO, BigDecimal::add);
						dto.setFrzBal(frzBal);
						BigDecimal finaMoney = values.stream()
								.filter(obj -> stockEntry.getKey().equals(obj.getEquityMaintenanceId()))
								.map(CorpDTO::getFinaMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
						dto.setFinaMoney(finaMoney);
						resultList.add(dto);

						// 具体单位
						List<CorpDTO> concreteList = values.stream()
								.filter(vo -> stockEntry.getKey().equals(vo.getEquityMaintenanceId()))
								.collect(Collectors.toList());

						if (ObjectUtil.isNotEmpty(concreteList)){
							initCorpData(concreteList);
							Boolean has100000 = false;
							for (CorpDTO v : concreteList){
								if (!"100".equals(v.getCorpCode()) && !"100000".equals(v.getCorpCode())){
									if (ObjectUtil.isNotEmpty(v.getId()) && ObjectUtil.isNotEmpty(v.getProvinceId())){
										CorpDTO corpDTO = corpService.get(v.getId());
										v.setProvinceName(corpDTO.getProvinceName());
									}
									resultList.add(v);
								}
								if (!has100000 && ("100".equals(v.getCorpCode()) || "100000".equals(v.getCorpCode()))){
									addHeadOfficeData(corp1080,corp100000,entry,ids,queryDate,resultList);
									has100000 = true;
								}
							}
						}
					}

				}

			}

			for (CorpDTO vo : resultList) {
				// 把单位省份置换到大区字段
				if (ObjectUtil.isNotEmpty(vo.getName())){
					vo.setSupCorpName(vo.getProvinceName());
				}
				// 把查询日期传递到前端
				vo.setQueryDate(DateUtil.format(queryDate, DatePattern.NORM_DATE_PATTERN));
			}

			//  根据显示方式过滤数据
			if (CommonConstant.NO.equals(showType)){
				resultList = resultList.stream().filter(obj -> ObjectUtil.isEmpty(obj.getName())).collect(Collectors.toList());
			}

		}

		return resultList;
	}

	private void addHeadOfficeData(CorpDTO corp1080,CorpDTO corp100000, Map.Entry<String, List<CorpDTO>> entry, List<String> ids, Date queryDate, List<CorpDTO> resultList) {
		if (corp1080.getId().equals(entry.getKey())){
			List<CorpDTO> tempList = new ArrayList<>();

			// 总部的分成四行显示，分别是
			//n 总部：单位编码为100000，且银行账号组=“总部”的数据；D1023
			List<CorpDTO> D1023List = nisAllocationDailyReportDao.queryData(ids,queryDate,null,Arrays.asList("D1023"));
			if (ObjectUtil.isNotEmpty(D1023List)){
				CorpDTO corpDTO = new CorpDTO();
				initCorpData(D1023List);
				assignCorpData(corpDTO,D1023List);
				corpDTO.setName(corp100000.getName()+"_总部");
				tempList.add(corpDTO);
			}
			//n 广州：单位编码为100000，且银行账号组=“广州”的数据；D1004、D1005、D1159、D1160
			String[] groups = new String[]{"D1004","D1005","D1159","D1160"};
			List<CorpDTO> D1004List = nisAllocationDailyReportDao.queryData(ids,queryDate,null,Arrays.asList(groups));
			if (ObjectUtil.isNotEmpty(D1004List)){
				CorpDTO corpDTO = new CorpDTO();
				initCorpData(D1004List);
				assignCorpData(corpDTO,D1004List);
				corpDTO.setName(corp100000.getName()+"_广州");
				tempList.add(corpDTO);
			}
			//n 花都：单位编码为100000，且银行账号组=“花都”的数据；D1002
			List<CorpDTO> D1002List = nisAllocationDailyReportDao.queryData(ids,queryDate,null,Arrays.asList("D1002"));
			if (ObjectUtil.isNotEmpty(D1002List)){
				CorpDTO corpDTO = new CorpDTO();
				initCorpData(D1002List);
				assignCorpData(corpDTO,D1002List);
				corpDTO.setName(corp100000.getName()+"_花都");
				tempList.add(corpDTO);
			}
			//n 番禺：单位编码为100000，且银行账号组=“番禺”的数据。D1003
			List<CorpDTO> D1003List = nisAllocationDailyReportDao.queryData(ids,queryDate,null,Arrays.asList("D1003"));
			if (ObjectUtil.isNotEmpty(D1003List)){
				CorpDTO corpDTO = new CorpDTO();
				initCorpData(D1003List);
				assignCorpData(corpDTO,D1003List);
				corpDTO.setName(corp100000.getName()+"_番禺");
				tempList.add(corpDTO);
			}

			// 汇总
			CorpDTO corpDTO = new CorpDTO();
			initCorpData(Arrays.asList(corpDTO));
			corpDTO.setName(corp100000.getName());
			corpDTO.setProvinceName(corp100000.getProvinceName());
			for (CorpDTO temp : tempList){
				if (ObjectUtil.isNotEmpty(temp.getMaxRate()) && temp.getMaxRate().compareTo(BigDecimal.ZERO) > 0){
					corpDTO.setMaxRate(temp.getMaxRate());
				}
				if (ObjectUtil.isNotEmpty(temp.getMinRate()) && temp.getMinRate().compareTo(BigDecimal.ZERO) > 0){
					corpDTO.setMinRate(temp.getMinRate());
				}
				if (ObjectUtil.isNotEmpty(temp.getAvgRate()) && temp.getAvgRate().compareTo(BigDecimal.ZERO) > 0){
					corpDTO.setAvgRate(temp.getAvgRate());
				}
				corpDTO.setBeforeDayBalance(corpDTO.getBeforeDayBalance().add(temp.getBeforeDayBalance()));
				corpDTO.setCurrentBalance(corpDTO.getCurrentBalance().add(temp.getCurrentBalance()));
				corpDTO.setAvailBal(corpDTO.getAvailBal().add(temp.getAvailBal()));
				corpDTO.setFrzBal(corpDTO.getFrzBal().add(temp.getFrzBal()));
				corpDTO.setFinaMoney(corpDTO.getFinaMoney().add(temp.getFinaMoney()));
				temp.setProvinceName(corp100000.getProvinceName());
			}
			resultList.add(corpDTO);
			resultList.addAll(tempList);
		}
	}

	private void assignCorpData(CorpDTO corpDTO, List<CorpDTO> list) {
		// 初始化0
		if (ObjectUtil.isEmpty(corpDTO.getBeforeDayBalance())) corpDTO.setBeforeDayBalance(BigDecimal.ZERO);
		if (ObjectUtil.isEmpty(corpDTO.getCurrentBalance())) corpDTO.setCurrentBalance(BigDecimal.ZERO);
		if (ObjectUtil.isEmpty(corpDTO.getAvailBal())) corpDTO.setAvailBal(BigDecimal.ZERO);
		if (ObjectUtil.isEmpty(corpDTO.getFrzBal())) corpDTO.setFrzBal(BigDecimal.ZERO);
		if (ObjectUtil.isEmpty(corpDTO.getFinaMoney())) corpDTO.setFinaMoney(BigDecimal.ZERO);

		for (CorpDTO temp : list){
			corpDTO.setMaxRate(temp.getMaxRate());
			corpDTO.setMinRate(temp.getMinRate());
			corpDTO.setAvgRate(temp.getAvgRate());
			corpDTO.setBeforeDayBalance(corpDTO.getBeforeDayBalance().add(temp.getBeforeDayBalance()));
			corpDTO.setCurrentBalance(corpDTO.getCurrentBalance().add(temp.getCurrentBalance()));
			corpDTO.setAvailBal(corpDTO.getAvailBal().add(temp.getAvailBal()));
			corpDTO.setFrzBal(corpDTO.getFrzBal().add(temp.getFrzBal()));
			corpDTO.setFinaMoney(corpDTO.getFinaMoney().add(temp.getFinaMoney()));
		}

	}

	private void initCorpData(List<CorpDTO> list) {
		for (CorpDTO temp : list){
			if (ObjectUtil.isEmpty(temp.getMaxRate())){
				temp.setMaxRate(BigDecimal.ZERO);
			}
			if (ObjectUtil.isEmpty(temp.getMinRate())){
				temp.setMinRate(BigDecimal.ZERO);
			}
			if (ObjectUtil.isEmpty(temp.getAvgRate())){
				temp.setAvgRate(BigDecimal.ZERO);
			}
			// 初始化0
			if (ObjectUtil.isEmpty(temp.getBeforeDayBalance())) temp.setBeforeDayBalance(BigDecimal.ZERO);
			if (ObjectUtil.isEmpty(temp.getCurrentBalance())) temp.setCurrentBalance(BigDecimal.ZERO);
			if (ObjectUtil.isEmpty(temp.getAvailBal())) temp.setAvailBal(BigDecimal.ZERO);
			if (ObjectUtil.isEmpty(temp.getFrzBal())) temp.setFrzBal(BigDecimal.ZERO);
			if (ObjectUtil.isEmpty(temp.getFinaMoney())) temp.setFinaMoney(BigDecimal.ZERO);

		}
	}

	/**
	 * 多级资金日报表
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	public DataTablesOutput getMultiLeveDatatable(DataTablesInput dataTablesInput, ICurrentUser user) {
		//查询日期
		Date queryDate = DateUtils.parse(String.valueOf(dataTablesInput.getFormdata().get("queryDate")), DateUtils.PATTERN_CLASSICAL_SIMPLE);
		dataTablesInput.getFormdata().remove("queryDate");
		// 显示方式
		String showType = (String)dataTablesInput.getFormdata().get("showType");
		// 股权属性
		String equityMaintenance = (String)dataTablesInput.getFormdata().get("equityMaintenance");
		// 查询所有大区
		Map<String, String> supMap = nisAllocationDailyReportDao.querySupCorp();
		//根据资源，用户id 获取单位id
		String corpId = (String)dataTablesInput.getFormdata().get("ins_corpId");
		List<String> corpIds = new ArrayList<>();
		if(StringUtils.isNotBlank(corpId)){
			List<CorpDTO> corpDTOS = corpService.listCorpDTOByCorpIds2(Arrays.asList(corpId.split(",")));
			// 显示的IDS
			List<String> showIds = new ArrayList<>();
			for (CorpDTO corpDTO : corpDTOS){
				corpIds.add(corpDTO.getId());
				// 把他的大区加进去
				for (String key : supMap.keySet()){
					if (corpDTO.getParentIds().indexOf(key) != -1){
						corpIds.add(key);
						break;
					}
				}

				if (CommonConstant.YES.equals(corpDTO.getReportDisplay())){
					showIds.add(corpDTO.getId());
				}else{
					// 如果查询单位是隐藏，则把第一个显示的上级增加进去
					List<CorpDTO> parents = corpService.listParentCorp(Arrays.asList(corpDTO.getId()),false);
					for (CorpDTO temp : parents){
						// 把显示单位添加进去
						if (CommonConstant.YES.equals(temp.getReportDisplay())){
							corpIds.add(temp.getId());
							break;
						}
					}
				}

			}

			// 如果查询单位是显示，则把所有隐藏下级都增加进去
			List<CorpDTO> subs = corpService.listSubCorp(showIds,false);
			for (CorpDTO temp : subs){
				// 把隐藏单位添加进去
				if (CommonConstant.NO.equals(temp.getReportDisplay())){
					corpIds.add(temp.getId());
				}
			}
		}else{
			//根据资源，用户id 获取单位id
			corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_NISALLOCATIONDAILY_MULTILEVE_QUERY, user.getId());
		}

		// 返回结果集
		List<FundDailyReportVO> resultList = new ArrayList<>();
		b:
		if (ObjectUtil.isNotEmpty(corpIds)){
			// 100000 单位
			CorpDTO corp100000 = corpService.getCorpByCode("100000");

			List<FundDailyReportVO> dataList = nisAllocationDailyReportDao.queryMultiLeveData(corpIds,queryDate,null,equityMaintenance);
			if (ObjectUtil.isEmpty(dataList)) break b;

			// 排除大区如果没有数据，则跳过
			List<FundDailyReportVO> datas = dataList.stream().filter(data -> !"6000".equals(data.getCellCode())).collect(Collectors.toList());
			if (ObjectUtil.isEmpty(datas)) break b;

			// 如果查询条件存在股权属性。则需要把大区对象放入结果数据中
			if (ObjectUtil.isNotEmpty(equityMaintenance)){
				List<String> temps = dataList.stream().filter(data -> "6000".equals(data.getCellCode())).map(data -> data.getCorpId()).collect(Collectors.toList());

				// 把supMap 中key在temps部分去除
				Set<String> strings = supMap.keySet();
				strings.removeAll( temps);

				// 剩下的则没在temps中的需要重新添加到dataList中
				if (ObjectUtil.isNotEmpty(strings)){
					List<CorpDTO> corpDTOS = corpService.listCorpDTOByCorpIds2(new ArrayList<>(strings));

					for (CorpDTO corpDTO : corpDTOS){
						FundDailyReportVO vo = new FundDailyReportVO();
						vo.setCorpId(corpDTO.getId());
						vo.setCorpCode(corpDTO.getCode());
						vo.setCorpName(corpDTO.getName());
						vo.setParentIds(corpDTO.getParentIds());
						vo.setReportDisplay(corpDTO.getReportDisplay());
						vo.setSort(corpDTO.getSort());
						vo.setCellCode("6000");
						vo.setCellName(corpDTO.getName());

						vo.setBeforeBal(BigDecimal.ZERO);
						vo.setCurrentBal(BigDecimal.ZERO);
						vo.setAvailBal(BigDecimal.ZERO);
						vo.setFrzBal(BigDecimal.ZERO);
						vo.setChoMoney(BigDecimal.ZERO);

						dataList.add(vo);
					}
				}

			}

			// 把100单位的上级更改为跟100000的上级一致，两个要统计在一起
			for (FundDailyReportVO temp : dataList){
				if ("100".equals(temp.getCorpCode())){
					temp.setParentIds(corp100000.getParentIds());
				}
			}

			// 获取显示单位的数据
			List<FundDailyReportVO> showList = dataList.stream().filter(data -> CommonConstant.YES.equals(data.getReportDisplay())).collect(Collectors.toList());
			// 获取隐藏单位的数据
			List<FundDailyReportVO> hideList = dataList.stream().filter(data -> CommonConstant.NO.equals(data.getReportDisplay())).collect(Collectors.toList());

			// 把隐藏单位去掉，并且把隐藏单位的金额赋值给他的第一个上级
			for (FundDailyReportVO vo : hideList){
				List<CorpDTO> parents = corpService.listParentCorp(Arrays.asList(vo.getCorpId()),false);
				for (CorpDTO temp : parents){
					if (CommonConstant.YES.equals(temp.getReportDisplay())){
						// 把vo的数据赋值到 temp
						for (FundDailyReportVO parent : showList){
							if (temp.getId().equals(parent.getCorpId())){
								parent.setBeforeBal(parent.getBeforeBal().add(vo.getBeforeBal()));
								parent.setCurrentBal(parent.getCurrentBal().add(vo.getCurrentBal()));
								parent.setAvailBal(parent.getAvailBal().add(vo.getAvailBal()));
								parent.setFrzBal(parent.getFrzBal().add(vo.getFrzBal()));
								parent.setChoMoney(parent.getChoMoney().add(vo.getChoMoney()));
								break ;
							}
						}
						break ;
					}
				}
			}

			// 只返回显示单位的数据
			dataList = showList;
			// 赋值省区名称
			handleProvinceName(dataList);

			// 初始化集团总计，全资子公司合计，控股子公司合计；数据
			resultList.add(initFundDailyReportData("集团总计",null,dataList,false));
			resultList.add(initFundDailyReportData("全资子公司合计","0001",dataList,true));
			resultList.add(initFundDailyReportData("控股子公司合计","0002",dataList,true));

			// 获取dataList中所有大区数据，并且以sort排序
			List<FundDailyReportVO> cellList = dataList.stream().filter(data -> "6000".equals(data.getCellCode())).sorted(Comparator.comparing(FundDailyReportVO::getSort)).collect(Collectors.toList());
			// 大区数据，把corpName赋值给ProvinceName
			for (FundDailyReportVO data : cellList){
				// 添加大区
				resultList.add(data);
				data.setProvinceName(data.getCorpName());

				data.setCorpName(null);
				data.setMaxRate(null);
				data.setMinRate(null);
				data.setAvgRate(null);

				// 统计大区数据
				List<FundDailyReportVO> corpList = dataList.stream().filter(temp -> temp.getParentIds().indexOf(data.getCorpId()) != -1).sorted(Comparator.comparing(FundDailyReportVO::getProvinceName).thenComparing(FundDailyReportVO::getCorpCode)).collect(Collectors.toList());
				if (ObjectUtil.isNotEmpty(corpList)){
					// 排除大区本身
					corpList.removeIf(temp -> temp.getCorpId().equals(data.getCorpId()));
					// 大区统计
					data.setBeforeBal(corpList.stream().map(FundDailyReportVO::getBeforeBal).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setCurrentBal(corpList.stream().map(FundDailyReportVO::getCurrentBal).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setAvailBal(corpList.stream().map(FundDailyReportVO::getAvailBal).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setFrzBal(corpList.stream().map(FundDailyReportVO::getFrzBal).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setChoMoney(corpList.stream().map(FundDailyReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));

					// 增加：其中：全资小计，
					List<FundDailyReportVO> list0001 = corpList.stream().filter(temp -> "0001".equals(temp.getEquityCode())).collect(Collectors.toList());
					if (ObjectUtil.isNotEmpty(list0001)){
						resultList.add(initFundDailyReportData("其中：全资小计","0001",list0001,false));
						specialHandleCorpData(resultList,list0001,queryDate);
					}


					// 其中：控股小计
					List<FundDailyReportVO> list0002 = corpList.stream().filter(temp -> "0002".equals(temp.getEquityCode())).collect(Collectors.toList());
					if (ObjectUtil.isNotEmpty(list0002)){
						resultList.add(initFundDailyReportData("其中：控股小计","0002",list0002,false));
						specialHandleCorpData(resultList,list0002,queryDate);
					}
				}
				// 计算完成以后删除corpId，前端根据这个字段是否显示超链接
				data.setCorpId(null);
			}

			// 赋值queryDate
			for (FundDailyReportVO temp : resultList){
				temp.setQueryDate(queryDate);
			}

			if (CommonConstant.NO.equals(showType)){
				// 过滤单位名称有的数据
				resultList = resultList.stream().filter(data -> ObjectUtil.isEmpty(data.getCorpName())).collect(Collectors.toList());
			}
		}
		DataTablesOutput dataTablesOutput = new DataTablesOutput();
		dataTablesOutput.setDraw(dataTablesInput.getDraw());
		dataTablesOutput.setData(resultList);
		return dataTablesOutput;
	}

	/**
	 * 特殊处理具体单位数据
	 * @param resultList
	 * @param list
	 * @return TODO
	 */
	private void specialHandleCorpData(List<FundDailyReportVO> resultList,List<FundDailyReportVO> list,Date queryDate) {
		// 过滤出单位为100的数据
		List<FundDailyReportVO> list100 = list.stream().filter(temp -> "100".equals(temp.getCorpCode())).collect(Collectors.toList());
		list.removeIf(temp -> "100".equals(temp.getCorpCode()));
		for (FundDailyReportVO temp : list){
			resultList.add( temp);

			// 把100单位数据合并到100000 单位
			if ("100000".equals(temp.getCorpCode())){
				List<String> corpIds = new ArrayList<>();
				corpIds.add(temp.getCorpId());
				if (ObjectUtil.isNotEmpty(list100)){
					corpIds.add(list100.get(0).getCorpId());
					// 计算利率
					List<FundDailyReportVO> rateList = new ArrayList<>();
					rateList.addAll(list100);
					rateList.add( temp);
					temp.setMaxRate(rateList.stream().map(FundDailyReportVO::getMaxRate).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
					temp.setMinRate(rateList.stream().map(FundDailyReportVO::getMinRate).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
					temp.setAvgRate(rateList.stream().map(FundDailyReportVO::getAvgRate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(rateList.size()), 6, BigDecimal.ROUND_HALF_UP));
					// 合并余额
					temp.setBeforeBal(temp.getBeforeBal().add(list100.stream().map(FundDailyReportVO::getBeforeBal).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setCurrentBal(temp.getCurrentBal().add(list100.stream().map(FundDailyReportVO::getCurrentBal).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setAvailBal(temp.getAvailBal().add(list100.stream().map(FundDailyReportVO::getAvailBal).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setFrzBal(temp.getFrzBal().add(list100.stream().map(FundDailyReportVO::getFrzBal).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setChoMoney(temp.getChoMoney().add(list100.stream().map(FundDailyReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add)));
				}

				// 总部的分成四行显示，分别是
				//n 总部：单位编码为100000/100，且银行账号组=“总部”的数据；D1023
				List<FundDailyReportVO> D1023List = nisAllocationDailyReportDao.queryMultiLeveData(corpIds,queryDate,Arrays.asList("D1023"),null);
				if (ObjectUtil.isNotEmpty(D1023List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(总部)", temp.getProvinceName(), D1023List));
				}
				//n 广州：单位编码为100000/100，且银行账号组=“广州”的数据；D1004、D1005、D1159、D1160
				String[] groups = new String[]{"D1004","D1005","D1159","D1160"};
				List<FundDailyReportVO> D1004List = nisAllocationDailyReportDao.queryMultiLeveData(corpIds,queryDate,Arrays.asList(groups),null);
				if (ObjectUtil.isNotEmpty(D1004List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(广州)", temp.getProvinceName(), D1004List));
				}
				//n 花都：单位编码为100000/100，且银行账号组=“花都”的数据；D1002
				List<FundDailyReportVO> D1002List = nisAllocationDailyReportDao.queryMultiLeveData(corpIds,queryDate,Arrays.asList("D1002"),null);
				if (ObjectUtil.isNotEmpty(D1002List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(花都)", temp.getProvinceName(), D1002List));
				}
				//n 番禺：单位编码为100000，且银行账号组=“番禺”的数据。D1003
				List<FundDailyReportVO> D1003List = nisAllocationDailyReportDao.queryMultiLeveData(corpIds,queryDate,Arrays.asList("D1003"),null);
				if (ObjectUtil.isNotEmpty(D1003List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(番禺)", temp.getProvinceName(), D1003List));
				}

			}

		}
	}

	private FundDailyReportVO initCorp100000Data(String corpName, String provinceName, List<FundDailyReportVO> list) {
		FundDailyReportVO vo = new FundDailyReportVO();
		vo.setProvinceName(provinceName);
		vo.setMaxRate(list.stream().map(FundDailyReportVO::getMaxRate).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
		vo.setMinRate(list.stream().map(FundDailyReportVO::getMinRate).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
		vo.setAvgRate(list.stream().map(FundDailyReportVO::getAvgRate).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(list.size()), 6, BigDecimal.ROUND_HALF_UP));
		vo.setCorpName(corpName);
		vo.setBeforeBal(list.stream().map(FundDailyReportVO::getBeforeBal).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setCurrentBal(list.stream().map(FundDailyReportVO::getCurrentBal).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setAvailBal(list.stream().map(FundDailyReportVO::getAvailBal).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setFrzBal(list.stream().map(FundDailyReportVO::getFrzBal).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setChoMoney(list.stream().map(FundDailyReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
		return vo;
	}

	/**
	 * 初始化对象
	 * @param provinceName
	 * @param equityCode
	 * @return TODO
	 */
	private FundDailyReportVO initFundDailyReportData(String provinceName, String equityCode,List<FundDailyReportVO> dataList,boolean isFilter1080) {
		FundDailyReportVO vo = new FundDailyReportVO();
		vo.setProvinceName(provinceName);
		vo.setEquityCode(equityCode);
		if (ObjectUtil.isEmpty(equityCode)){
			vo.setBeforeBal(dataList.stream().map(FundDailyReportVO::getBeforeBal).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setCurrentBal(dataList.stream().map(FundDailyReportVO::getCurrentBal).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setAvailBal(dataList.stream().map(FundDailyReportVO::getAvailBal).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setFrzBal(dataList.stream().map(FundDailyReportVO::getFrzBal).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setChoMoney(dataList.stream().map(FundDailyReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
		}else{
			if (isFilter1080){
				// 获取单位编码=1080的数据
				List<FundDailyReportVO> D1080List = dataList.stream().filter(data -> "1080".equals(data.getCorpCode())).collect(Collectors.toList());
				String filterId = D1080List.stream().map(FundDailyReportVO::getCorpId).findFirst().orElse("");
				vo.setBeforeBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(FundDailyReportVO::getBeforeBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setCurrentBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(FundDailyReportVO::getCurrentBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setAvailBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(FundDailyReportVO::getAvailBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setFrzBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(FundDailyReportVO::getFrzBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setChoMoney(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(FundDailyReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
			}else{
				vo.setBeforeBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(FundDailyReportVO::getBeforeBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setCurrentBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(FundDailyReportVO::getCurrentBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setAvailBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(FundDailyReportVO::getAvailBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setFrzBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(FundDailyReportVO::getFrzBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setChoMoney(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(FundDailyReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
			}

		}

		return vo;
	}

	/**
	 * 给dataList中省区名赋值
	 * @param dataList
	 * @return TODO
	 */
	private void handleProvinceName(List<FundDailyReportVO> dataList) {
		// 获取所有省区id
		List<String> provinceIds = dataList.stream().filter(item -> StringUtils.isNotBlank(item.getProvinceId())).map(FundDailyReportVO::getProvinceId).collect(Collectors.toList());
		Map<String,String> provinceMap = new HashMap<>();
		if (ObjectUtil.isNotEmpty(provinceIds)){
			List<FundDailyReportVO> provinceList = nisAllocationDailyReportDao.queryProvince(provinceIds);
			if (ObjectUtil.isNotEmpty(provinceList)){
				provinceMap = provinceList.stream().collect(Collectors.toMap(FundDailyReportVO::getProvinceId, FundDailyReportVO::getProvinceName));
			}
		}
		// 给dataList中的省区名称赋值
		for (FundDailyReportVO temp : dataList){
			String provinceName = "";
			String provinceId = temp.getProvinceId();
			if (ObjectUtil.isNotEmpty(provinceId)){
				String[] ids = provinceId.split(",");
				for (String id : ids){
					if (provinceMap.containsKey(id)){
						if (ObjectUtil.isEmpty(provinceName)){
							provinceName = provinceMap.get(id);
						}else{
							provinceName += "、" + provinceMap.get(id);
						}
					}
				}
			}
			temp.setProvinceName(provinceName);
		}
	}
}
