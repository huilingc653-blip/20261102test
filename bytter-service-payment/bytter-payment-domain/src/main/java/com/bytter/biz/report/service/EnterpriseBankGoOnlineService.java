package com.bytter.biz.report.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.report.dao.EnterpriseBankGoOnlineDao;
import com.bytter.biz.report.vo.EnterpriseBankGoOnlineVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICellManageService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IOperationAreaService;
import com.bytter.system.api.dto.CellManageDto;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.OperationAreaDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhoup
 * @version 1.0
 * @Title: Service
 * @Description: 银企上线占比表Service
 * @date 2017-10-09 16:31:56
 */
@Service("enterpriseBankGoOnlineService")
@Transactional
public class EnterpriseBankGoOnlineService extends BaseService<EnterpriseBankGoOnlineVo, String> {
	@Autowired
	private EnterpriseBankGoOnlineDao enterpriseBankGoOnlineDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IOperationAreaService operationAreaService;
	@DubboReference(check = false)
	private ICellManageService cellManageService;
	@Override
	public BaseDao<EnterpriseBankGoOnlineVo, String> getEntityDao() {
		return enterpriseBankGoOnlineDao;
	}

	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		CellManageDto cellManageDto = cellManageService.getCellManageByCodeOrName1("6000", "大区");
		//根据资源，用户id 获取单位id
		List<String> userCorpIds = corpService.getUserCorpIds("report.enterpriseBankGoOnline.query", user.getId());
		DataTablesOutput dataTablesOutput = enterpriseBankGoOnlineDao.getDataTablesUnion(userCorpIds,dataTablesInput,cellManageDto.getId());
		//处理数据(维度:根据营运区分组)
		List<EnterpriseBankGoOnlineVo> list = dealData(dataTablesInput,dataTablesOutput.getData(),cellManageDto.getId());
		dataTablesOutput.setData(list);
		return dataTablesOutput;
	}

	/**
	 * 分组数据
	 * @param list
	 * @return
	 */
	private List<EnterpriseBankGoOnlineVo> dealData(DataTablesInput dataTablesInput,List<EnterpriseBankGoOnlineVo> list,String unitCmId){
		List<EnterpriseBankGoOnlineVo> result0 = new ArrayList<>();
		List<EnterpriseBankGoOnlineVo> result1 = new ArrayList<>();
		List<EnterpriseBankGoOnlineVo> result = new ArrayList<>();
		//查询总合计(多个账号合作同一家银行，不可重复统计)
		int totalCooperationBank = enterpriseBankGoOnlineDao.getCooperationBankTotal(dataTablesInput,unitCmId);
		HashMap<String, Integer> regionCooperationBankMap = new HashMap<>();
		//查询所有营运区
		List<OperationAreaDTO> operationAreaList = operationAreaService.getOperationArea();
		Map<String, OperationAreaDTO> operationAreaMap = new HashMap<>();
		for (OperationAreaDTO operationAreaDTO : operationAreaList) {
			operationAreaMap.put(operationAreaDTO.getOperationAreaCode(),operationAreaDTO);
		}
		//查看所有大区
		List<CorpDTO> region = corpService.getRegion(unitCmId);
		Map<String, CorpDTO> regionMap = new LinkedHashMap<>();
		for (CorpDTO corpDTO : region) {
			//查询大区合计合作银行数量(多个账号合作同一家银行，不可重复统计)
			int regionCooperationBank = enterpriseBankGoOnlineDao.getCooperationBankGroupByRegion(dataTablesInput,unitCmId,corpDTO.getId());
			regionCooperationBankMap.put(corpDTO.getId(),regionCooperationBank);
			regionMap.put(corpDTO.getId(),corpDTO);
		}
		//拆分营运区ID
		for (EnterpriseBankGoOnlineVo enterpriseBankGoOnlineVo : list) {
			String operationAreaId= enterpriseBankGoOnlineVo.getOperateAreaId();
			if (EmptyUtils.isNotEmpty(operationAreaId)){
				List<String> operationAreaIdList = Arrays.asList(operationAreaId.split(","));
				//如果存在多个营运区,进行拆分，其它统计数据保留
				if (operationAreaIdList.size()>=2){
					for (String operationAreaId1 : operationAreaIdList) {
						if (operationAreaMap.containsKey(operationAreaId1)){
							EnterpriseBankGoOnlineVo enterpriseBankGoOnlineVo1 = new EnterpriseBankGoOnlineVo();
							BeanUtils.copyNotNullProperties(enterpriseBankGoOnlineVo,enterpriseBankGoOnlineVo1);
							enterpriseBankGoOnlineVo1.setOperateAreaId(operationAreaId1);
							enterpriseBankGoOnlineVo1.setOperateAreaName(operationAreaMap.get(operationAreaId1).getOperationAreaName());
							result0.add(enterpriseBankGoOnlineVo1);
						}
					}
				}else{
					result0.add(enterpriseBankGoOnlineVo);
				}
			}
		}
		//根据大区，营运区多字段分组
//		Map<String, Map<String, List<EnterpriseBankGoOnlineVo>>> collect = result0.stream().collect(Collectors.groupingBy(EnterpriseBankGoOnlineVo::getRegionId, Collectors.groupingBy(EnterpriseBankGoOnlineVo::getOperateAreaId)));
        Map<String, Map<String, List<EnterpriseBankGoOnlineVo>>> collect = result0.stream()
                .collect(Collectors.groupingBy(
                        EnterpriseBankGoOnlineVo::getRegionId,
                        LinkedHashMap::new,
                        Collectors.groupingBy(EnterpriseBankGoOnlineVo::getOperateAreaId,
                                LinkedHashMap::new,
                                Collectors.toList())
                ));
        Map<String, List<EnterpriseBankGoOnlineVo>> groupByregionMap = new LinkedHashMap<>();
		//求和
		//大区
		collect.forEach((key,value)->{
			//大区对应的所有明细数据
			ArrayList<EnterpriseBankGoOnlineVo> enterpriseBankGoOnlineVos = new ArrayList<>();
			//营运区
			value.forEach((key1,value1)->{
				EnterpriseBankGoOnlineVo enterpriseBankGoOnlineVo1 = sumEnterpriseBankGoOnlineVo(value1);
				enterpriseBankGoOnlineVo1.setOperateAreaId(key1);
				enterpriseBankGoOnlineVo1.setRegionId(key);
				enterpriseBankGoOnlineVos.add(enterpriseBankGoOnlineVo1);
			});
			groupByregionMap.put(key,enterpriseBankGoOnlineVos);
			//大区统计
			EnterpriseBankGoOnlineVo enterpriseBankGoOnlineVo = sumEnterpriseBankGoOnlineVo(enterpriseBankGoOnlineVos);
			enterpriseBankGoOnlineVo.setRegionId(key);
			enterpriseBankGoOnlineVo.setLevel(2);
			//合作银行
			if(regionCooperationBankMap.containsKey(enterpriseBankGoOnlineVo.getRegionId())){
				enterpriseBankGoOnlineVo.setCooperationBankNum(regionCooperationBankMap.get(enterpriseBankGoOnlineVo.getRegionId()));
			}
			result1.add(enterpriseBankGoOnlineVo);
		});
		//合计
		EnterpriseBankGoOnlineVo amount = sumEnterpriseBankGoOnlineVo(result1);
		amount.setRegionName("合计");
		amount.setLevel(1);
		//合作银行
		amount.setCooperationBankNum(totalCooperationBank);
		result.add(amount);
		for (EnterpriseBankGoOnlineVo enterpriseBankGoOnlineVo : result1) {
			if (!EmptyUtils.isEmpty(regionMap.get(enterpriseBankGoOnlineVo.getRegionId()))){
				enterpriseBankGoOnlineVo.setRegionName(regionMap.get(enterpriseBankGoOnlineVo.getRegionId()).getName());
			}
			if (enterpriseBankGoOnlineVo.getAccSumNum()!=0&&enterpriseBankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()!=0){
				//计算可上银企上线占比(可上银企账户数量/总账户数)
				enterpriseBankGoOnlineVo.setIsNotOnlineProportion(BigDecimal.valueOf(enterpriseBankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()*100).divide(BigDecimal.valueOf(enterpriseBankGoOnlineVo.getAccSumNum()),2, RoundingMode.HALF_UP));
			}else{
				enterpriseBankGoOnlineVo.setIsNotOnlineProportion(BigDecimal.ZERO);
			}
			if (enterpriseBankGoOnlineVo.getIsOnlineEnterpriseBankNum()!=0&&enterpriseBankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()!=0){
				//计算银企上线进度(已上银企账户数量/可上银企账户数量)
				enterpriseBankGoOnlineVo.setIsOnlineProportion(BigDecimal.valueOf(enterpriseBankGoOnlineVo.getIsOnlineEnterpriseBankNum()*100).divide(BigDecimal.valueOf(enterpriseBankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()),2, RoundingMode.HALF_UP));
			}else{
				enterpriseBankGoOnlineVo.setIsOnlineProportion(BigDecimal.ZERO);
			}
			//合作银行
//			if(regionCooperationBankMap.containsKey(enterpriseBankGoOnlineVo.getRegionId())){
//				enterpriseBankGoOnlineVo.setCooperationBankNum(regionCooperationBankMap.get(enterpriseBankGoOnlineVo.getRegionId()));
//			}
			result.add(enterpriseBankGoOnlineVo);
			if (groupByregionMap.containsKey(enterpriseBankGoOnlineVo.getRegionId())) {
				for (EnterpriseBankGoOnlineVo bankGoOnlineVo : groupByregionMap.get(enterpriseBankGoOnlineVo.getRegionId())) {
					bankGoOnlineVo.setLevel(3);
					if (!EmptyUtils.isEmpty(regionMap.get(enterpriseBankGoOnlineVo.getRegionId()))){
						bankGoOnlineVo.setRegionName(regionMap.get(enterpriseBankGoOnlineVo.getRegionId()).getName());
					}
					if (!EmptyUtils.isEmpty(operationAreaMap.get(bankGoOnlineVo.getOperateAreaId()))){
						bankGoOnlineVo.setOperateAreaName(operationAreaMap.get(bankGoOnlineVo.getOperateAreaId()).getOperationAreaName());
					}
					if (bankGoOnlineVo.getAccSumNum()!=0&&bankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()!=0){
						//计算可上银企上线占比(可上银企账户数量/总账户数)
						bankGoOnlineVo.setIsNotOnlineProportion(BigDecimal.valueOf(bankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()*100).divide(BigDecimal.valueOf(bankGoOnlineVo.getAccSumNum()),2, RoundingMode.HALF_UP));
					}else{
						bankGoOnlineVo.setIsNotOnlineProportion(BigDecimal.ZERO);
					}
					if (bankGoOnlineVo.getIsOnlineEnterpriseBankNum()!=0&&bankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()!=0){
						//计算银企上线进度(已上银企账户数量/可上银企账户数量)
						bankGoOnlineVo.setIsOnlineProportion(BigDecimal.valueOf(bankGoOnlineVo.getIsOnlineEnterpriseBankNum()*100).divide(BigDecimal.valueOf(bankGoOnlineVo.getIsNotOnlineEnterpriseBankNum()),2, RoundingMode.HALF_UP));
					}else{
						bankGoOnlineVo.setIsOnlineProportion(BigDecimal.ZERO);
					}
					result.add(bankGoOnlineVo);
				}
			}
		}
		return result;
	}
	/**
	 * 对List<EnterpriseBankGoOnlineVo>集合里的数据求和并返回一个对象
	 */
	private EnterpriseBankGoOnlineVo sumEnterpriseBankGoOnlineVo(List<EnterpriseBankGoOnlineVo> list){
		Integer cooperatonBankNum  = 0;
		Integer accSumNum  = 0;
		Integer isOnlineEnterpriseBankNum  = 0;
		Integer isNotOnlineEnterpriseBankNum  = 0;
		if(!CollectionUtils.isEmpty(list)){
			for (EnterpriseBankGoOnlineVo bankGoOnlineVo : list) {
				cooperatonBankNum = cooperatonBankNum.intValue()+bankGoOnlineVo.getCooperationBankNum().intValue();
				accSumNum = accSumNum.intValue()+bankGoOnlineVo.getAccSumNum().intValue();
				isOnlineEnterpriseBankNum = isOnlineEnterpriseBankNum.intValue()+bankGoOnlineVo.getIsOnlineEnterpriseBankNum().intValue();
				isNotOnlineEnterpriseBankNum = isNotOnlineEnterpriseBankNum.intValue()+bankGoOnlineVo.getIsNotOnlineEnterpriseBankNum().intValue();
			}
		}
		EnterpriseBankGoOnlineVo enterpriseBankGoOnlineVo = new EnterpriseBankGoOnlineVo();
		enterpriseBankGoOnlineVo.setCooperationBankNum(cooperatonBankNum);
		enterpriseBankGoOnlineVo.setAccSumNum(accSumNum);
		enterpriseBankGoOnlineVo.setIsOnlineEnterpriseBankNum(isOnlineEnterpriseBankNum);
		enterpriseBankGoOnlineVo.setIsNotOnlineEnterpriseBankNum(isNotOnlineEnterpriseBankNum);
		return enterpriseBankGoOnlineVo;
	}
}
