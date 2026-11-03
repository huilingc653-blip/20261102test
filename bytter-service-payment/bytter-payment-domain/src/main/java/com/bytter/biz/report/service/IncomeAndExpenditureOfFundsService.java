package com.bytter.biz.report.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.report.dao.IncomeAndExpenditureOfFundsDao;
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
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICellManageService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IEquityMaintenanceService;
import com.bytter.system.api.dto.CellManageDto;
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
 * @author zhoup
 * @version 1.0
 * @Title: Service
 * @Description: 资金收支表Service
 * @date 2017-10-09 16:31:56
 */
@Service("incomeAndExpenditureOfFundsService")
@Transactional
public class IncomeAndExpenditureOfFundsService extends BaseService<IncomeAndExpenditureOfFundsVo, String> {
	@Autowired
	private IncomeAndExpenditureOfFundsDao incomeAndExpenditureOfFundsDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private ICellManageService cellManageService;
	@DubboReference(check = false)
	private IEquityMaintenanceService equityMaintenanceService;
	@Autowired
	private NisAllocationDailyReportDao nisAllocationDailyReportDao;
	@Override
	public BaseDao<IncomeAndExpenditureOfFundsVo, String> getEntityDao() {
		return incomeAndExpenditureOfFundsDao;
	}

	public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
		DataTablesOutput output = new DataTablesOutput();
		CellManageDto region = cellManageService.getCellManageByCodeOrName1("6000", "大区");
		//根据资源，用户id 获取单位id
		List<String> userCorpIds = corpService.getUserCorpIds("report.incomeAndExp.query", user.getId());

		// 参数
		String corpIds = (String) input.getFormdata().get("ins_corpId");
		Date startDate = DateUtil.parseDate((String) input.getFormdata().get("ged_startDate"));
		Date endDate = DateUtil.parseDate((String) input.getFormdata().get("led_endDate"));
		String equityMaintenance = (String) input.getFormdata().get("equityMaintenance");
		String showType = (String) input.getFormdata().get("showType");

		List<IncomeAndExpenditureOfFundsVo> list = incomeAndExpenditureOfFundsDao.getDataTablesUnion(userCorpIds,corpIds,startDate,endDate, region.getId(),equityMaintenance,null);
		// 把100单位放到总部
		CorpDTO corp100000 = corpService.getCorpByCode("100000");
		for(IncomeAndExpenditureOfFundsVo vo : list){
			if ("100".equals(vo.getCorpCode())){
				String temp = corp100000.getId()+",";
				vo.setParentIds(corp100000.getParentIds());
				vo.setRegionId(corp100000.getId());
				vo.setRegionName(corp100000.getName());
			}
		}
		//处理数据(维度:根据营运区分组)
		list = dealData(list,region.getId(),showType,startDate,endDate);
		output.setData(list);
		return output;
	}

	/**
	 * 分组数据
	 * @param sourceList
	 * @return
	 */
	private List<IncomeAndExpenditureOfFundsVo> dealData(List<IncomeAndExpenditureOfFundsVo> sourceList,String regionId,String showType,Date startDate,Date endDate){
		ArrayList<IncomeAndExpenditureOfFundsVo> filterList = new ArrayList<>();
		CorpDTO corp1080 = corpService.getCorpByCode("1080");
		CorpDTO corp100000 = corpService.getCorpByCode("100000");
		//去除不是大区下的单位
		for (IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo : sourceList) {
			//获取parentIds来判断上级网点是否存在大区
			String parentIds = incomeAndExpenditureOfFundsVo.getParentIds();
			List<String> parentPath = Arrays.asList(parentIds.split(","));
			CorpDTO regionDTO = this.adjustIsRegion(parentPath,regionId);
			if (regionDTO != null){
				incomeAndExpenditureOfFundsVo.setRegionId(regionDTO.getId());
				incomeAndExpenditureOfFundsVo.setRegionName(regionDTO.getName());
				filterList.add(incomeAndExpenditureOfFundsVo);
			}
		}
		//去除股权id为空字符串的值
		List<IncomeAndExpenditureOfFundsVo> list = filterList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getRegionId())).collect(Collectors.toList());
		list = list.stream().filter(item -> ObjectUtil.isNotEmpty(item.getEquityMaintenanceId())).collect(Collectors.toList());
		if (ObjectUtil.isEmpty(list)) {
			return new ArrayList<>();
		}

		//准备数据
		Map<String,EquityMaintenanceDTO> equityMaintenanceDTOMap = new HashMap<>();
		List<EquityMaintenanceDTO> equityMaintenanceDTOS = equityMaintenanceService.queryAll();
		for (EquityMaintenanceDTO equityMaintenanceDTO : equityMaintenanceDTOS) {
			equityMaintenanceDTOMap.put(equityMaintenanceDTO.getEquityCode(),equityMaintenanceDTO);
		}
		//查看所有大区
		List<CorpDTO> region = corpService.getRegion(regionId);
		Map<String, CorpDTO> regionMap = new LinkedHashMap<>();
		for (CorpDTO corpDTO : region) {
			regionMap.put(corpDTO.getId(),corpDTO);
		}

		//计算期末余额(期初余额+收入-支出)
		for (IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo : list) {
			incomeAndExpenditureOfFundsVo.setEndBal(incomeAndExpenditureOfFundsVo.getBeginBal().add(incomeAndExpenditureOfFundsVo.getIncome())
																					.subtract(incomeAndExpenditureOfFundsVo.getExpenditure()));
		}
		//最终返回的数据
		List<IncomeAndExpenditureOfFundsVo> result = new ArrayList<>();
		//集团股权分组统计
		List<IncomeAndExpenditureOfFundsVo> result0 = new ArrayList<>();
		//区域统计
		List<IncomeAndExpenditureOfFundsVo> result1 = new ArrayList<>();

		HashMap<String, List<IncomeAndExpenditureOfFundsVo>> equityMaintenanceMap = new LinkedHashMap<>();
		//区域下股权下的明细数据
		HashMap<String, List<IncomeAndExpenditureOfFundsVo>> regionEquityMaintenanceMap = new LinkedHashMap<>();
		//对数据分组
		//根据区域，股权多字段分组
		Map<String, Map<String, List<IncomeAndExpenditureOfFundsVo>>> collect = list.stream().collect(Collectors.groupingBy(IncomeAndExpenditureOfFundsVo::getRegionId,LinkedHashMap::new, Collectors.groupingBy(IncomeAndExpenditureOfFundsVo::getEquityMaintenanceId,LinkedHashMap::new,Collectors.toList())));
//		Map<String, List<IncomeAndExpenditureOfFundsVo>> groupByEquityMaintenance = list.stream().collect(Collectors.groupingBy(IncomeAndExpenditureOfFundsVo::getEquityMaintenanceId));
		Map<String, List<IncomeAndExpenditureOfFundsVo>> groupByEquityMaintenance = list.stream().collect(Collectors.groupingBy(IncomeAndExpenditureOfFundsVo::getEquityMaintenanceId,LinkedHashMap::new,Collectors.toList()));
		//集团股权分组统计求和
		groupByEquityMaintenance.forEach((key,value)->{
			IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo = sumIncomeAndExpenditureOfFundsVo(value);
			incomeAndExpenditureOfFundsVo.setEquityMaintenanceId(key);
			result0.add(incomeAndExpenditureOfFundsVo);
		});

        //根据单位排序字段排序区域
        region.forEach(info->{
            String key = info.getId();
            if(StrUtil.isNotEmpty(key) && collect.containsKey(key)){
                //股权统计
                List<IncomeAndExpenditureOfFundsVo> incomeAndExpenditureOfFundsVos = new ArrayList<>();
                //股权求和
                collect.get(key).forEach((key1,value1)->{
                    IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo = sumIncomeAndExpenditureOfFundsVo(value1);
                    incomeAndExpenditureOfFundsVo.setEquityMaintenanceId(key1);
                    incomeAndExpenditureOfFundsVo.setRegionId(key);
                    incomeAndExpenditureOfFundsVos.add(incomeAndExpenditureOfFundsVo);
                    //用区域Id和股权ID做拼接来作为key值
                    regionEquityMaintenanceMap.put(key+key1,value1);
                });
                equityMaintenanceMap.put(key,incomeAndExpenditureOfFundsVos);
                //区域统计
                IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo1 = sumIncomeAndExpenditureOfFundsVo(incomeAndExpenditureOfFundsVos);
                incomeAndExpenditureOfFundsVo1.setRegionId(key);
                result1.add(incomeAndExpenditureOfFundsVo1);
            }
        });
		//集团总计
		IncomeAndExpenditureOfFundsVo groupTotal =sumIncomeAndExpenditureOfFundsVo(list);
		groupTotal.setLevel(1);
		groupTotal.setRegionName("集团总计");
		result.add(groupTotal);
		//集团股权统计
		for (IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo : result0) {
			incomeAndExpenditureOfFundsVo.setLevel(2);
			if (equityMaintenanceDTOMap.containsKey(incomeAndExpenditureOfFundsVo.getEquityMaintenanceId())){
				incomeAndExpenditureOfFundsVo.setRegionName(equityMaintenanceDTOMap.get(incomeAndExpenditureOfFundsVo.getEquityMaintenanceId()).getEquityName()+"子公司合计");
			}
			result.add(incomeAndExpenditureOfFundsVo);
		}
		for (IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo : result1) {
			incomeAndExpenditureOfFundsVo.setLevel(3);
			if (regionMap.containsKey(incomeAndExpenditureOfFundsVo.getRegionId())){
				incomeAndExpenditureOfFundsVo.setRegionName(regionMap.get(incomeAndExpenditureOfFundsVo.getRegionId()).getName()+"合计");
			}
			//区域统计
			result.add(incomeAndExpenditureOfFundsVo);
			if (equityMaintenanceMap.containsKey(incomeAndExpenditureOfFundsVo.getRegionId())){
				List<IncomeAndExpenditureOfFundsVo> incomeAndExpenditureOfFundsVos = equityMaintenanceMap.get(incomeAndExpenditureOfFundsVo.getRegionId());
				for (IncomeAndExpenditureOfFundsVo andExpenditureOfFundsVo : incomeAndExpenditureOfFundsVos) {
					andExpenditureOfFundsVo.setLevel(4);
					if (equityMaintenanceDTOMap.containsKey(andExpenditureOfFundsVo.getEquityMaintenanceId())){
						andExpenditureOfFundsVo.setRegionName("其中: "+equityMaintenanceDTOMap.get(andExpenditureOfFundsVo.getEquityMaintenanceId()).getEquityName()+"小计");
					}
					//区域其中股权统计
					result.add(andExpenditureOfFundsVo);
					if (regionEquityMaintenanceMap.containsKey(andExpenditureOfFundsVo.getRegionId()+andExpenditureOfFundsVo.getEquityMaintenanceId())){
						List<IncomeAndExpenditureOfFundsVo> incomeAndExpenditureOfFundsVos1 = regionEquityMaintenanceMap.get(andExpenditureOfFundsVo.getRegionId() + andExpenditureOfFundsVo.getEquityMaintenanceId());
						for (IncomeAndExpenditureOfFundsVo expenditureOfFundsVo : incomeAndExpenditureOfFundsVos1) {
							expenditureOfFundsVo.setLevel(5);
							////区域其中股权统计下的明细数据
							result.add(expenditureOfFundsVo);
						}
					}
				}
			}
		}
		List<IncomeAndExpenditureOfFundsVo> resultList = new ArrayList<>();
		// 总部ids
		List<String> ids = new ArrayList<>();
		for (IncomeAndExpenditureOfFundsVo vo : result){
			if ("100".equals(vo.getCorpCode()) || "100000".equals(vo.getCorpCode())){
				ids.add(vo.getCorpId());
			}
		}
		Boolean flag = false;
		for (IncomeAndExpenditureOfFundsVo vo : result) {
			if (EmptyUtils.isEmpty(vo.getCorpName())){
				vo.setCorpName("");
			}
			if (EmptyUtils.isEmpty(vo.getRegionName())){
				vo.setRegionName("");
			}
			if (EmptyUtils.isEmpty(vo.getRemark())){
				vo.setRemark("");
			}
			if (EmptyUtils.isNotEmpty(vo.getCorpName())){
				vo.setRegionName("");
			}
			if (!"100".equals(vo.getCorpCode()) && !"100000".equals(vo.getCorpCode())){
				if (ObjectUtil.isNotEmpty(vo.getCorpId()) && ObjectUtil.isNotEmpty(vo.getProvinceId())){
					CorpDTO corpDTO = corpService.get(vo.getCorpId());
					vo.setProvinceName(corpDTO.getProvinceName());
				}
				resultList.add(vo);
			}
			if (!flag && ("100000".equals(vo.getCorpCode()) || "100".equals(vo.getCorpCode()))){
				flag = true;
				addHeadOfficeData(corp1080,corp100000,vo,ids,startDate,endDate,regionId,resultList);
			}
		}

		for (IncomeAndExpenditureOfFundsVo vo : resultList) {
			// 把单位省份置换到大区字段
			if (ObjectUtil.isNotEmpty(vo.getCorpName())){
				vo.setRegionName(vo.getProvinceName());
			}
		}

		//  根据显示方式过滤数据
		if (CommonConstant.NO.equals(showType)){
			resultList = resultList.stream().filter(obj -> ObjectUtil.isEmpty(obj.getCorpName())).collect(Collectors.toList());
		}
		return resultList;
	}

	private void addHeadOfficeData(CorpDTO corp1080, CorpDTO corp100000, IncomeAndExpenditureOfFundsVo vo, List<String> ids, Date startDate, Date endDate, String regionId, List<IncomeAndExpenditureOfFundsVo> resultList) {
		List<IncomeAndExpenditureOfFundsVo> tempList = new ArrayList<>();
		// 总部的分成四行显示，分别是
		//n 总部：单位编码为100000，且银行账号组=“总部”的数据； D1023
		List<IncomeAndExpenditureOfFundsVo> D1023List = incomeAndExpenditureOfFundsDao.getDataTablesUnion(ids,"",startDate,endDate, regionId,"",Arrays.asList("D1023"));
		if (ObjectUtil.isNotEmpty(D1023List)){
			IncomeAndExpenditureOfFundsVo fundsVo = new IncomeAndExpenditureOfFundsVo();
			assignCorpData(fundsVo,D1023List);
			fundsVo.setCorpId(corp100000.getId());
			fundsVo.setCorpCode(corp100000.getCode()+"_1");
			fundsVo.setCorpName(corp100000.getName()+"_总部");
			fundsVo.setEndBal(fundsVo.getBeginBal().add(fundsVo.getIncome()).subtract(fundsVo.getExpenditure()));
			tempList.add(fundsVo);
		}
		//n 广州：单位编码为100000，且银行账号组=“广州”的数据；D1004、D1005、D1159、D1160
		String[] groups = new String[]{"D1004","D1005","D1159","D1160"};
		List<IncomeAndExpenditureOfFundsVo> D1004List = incomeAndExpenditureOfFundsDao.getDataTablesUnion(ids,"",startDate,endDate, regionId,"",Arrays.asList(groups));
		if (ObjectUtil.isNotEmpty(D1004List)){
			IncomeAndExpenditureOfFundsVo fundsVo = new IncomeAndExpenditureOfFundsVo();
			assignCorpData(fundsVo,D1004List);
			fundsVo.setCorpId(corp100000.getId());
			fundsVo.setCorpCode(corp100000.getCode()+"_2");
			fundsVo.setCorpName(corp100000.getName()+"_广州");
			fundsVo.setEndBal(fundsVo.getBeginBal().add(fundsVo.getIncome()).subtract(fundsVo.getExpenditure()));
			tempList.add(fundsVo);
		}
		//n 花都：单位编码为100000，且银行账号组=“花都”的数据；D1002
		List<IncomeAndExpenditureOfFundsVo> D1002List = incomeAndExpenditureOfFundsDao.getDataTablesUnion(ids,"",startDate,endDate, regionId,"",Arrays.asList("D1002"));
		if (ObjectUtil.isNotEmpty(D1002List)){
			IncomeAndExpenditureOfFundsVo fundsVo = new IncomeAndExpenditureOfFundsVo();
			assignCorpData(fundsVo,D1002List);
			fundsVo.setCorpId(corp100000.getId());
			fundsVo.setCorpCode(corp100000.getCode()+"_3");
			fundsVo.setCorpName(corp100000.getName()+"_花都");
			fundsVo.setEndBal(fundsVo.getBeginBal().add(fundsVo.getIncome()).subtract(fundsVo.getExpenditure()));
			tempList.add(fundsVo);
		}
		//n 番禺：单位编码为100000，且银行账号组=“番禺”的数据。D1003
		List<IncomeAndExpenditureOfFundsVo> D1003List = incomeAndExpenditureOfFundsDao.getDataTablesUnion(ids,"",startDate,endDate, regionId,"",Arrays.asList("D1003"));
		if (ObjectUtil.isNotEmpty(D1003List)){
			IncomeAndExpenditureOfFundsVo fundsVo = new IncomeAndExpenditureOfFundsVo();
			assignCorpData(fundsVo,D1003List);
			fundsVo.setCorpId(corp100000.getId());
			fundsVo.setCorpCode(corp100000.getCode()+"_4");
			fundsVo.setCorpName(corp100000.getName()+"_番禺");
			fundsVo.setEndBal(fundsVo.getBeginBal().add(fundsVo.getIncome()).subtract(fundsVo.getExpenditure()));
			tempList.add(fundsVo);
		}

		// 汇总
		IncomeAndExpenditureOfFundsVo fundsVo = new IncomeAndExpenditureOfFundsVo();
		fundsVo.setCorpId(corp100000.getId());
		fundsVo.setCorpCode(corp100000.getCode()+"_0");
		fundsVo.setCorpName(corp100000.getName());
		BigDecimal maxRate = BigDecimal.ZERO;
		for (IncomeAndExpenditureOfFundsVo ofFundsVo : tempList){
			if (ofFundsVo.getMaxRate().compareTo(maxRate) > 0 ){
				maxRate = ofFundsVo.getMaxRate();
			}
			fundsVo.setBeginBal(fundsVo.getBeginBal().add(ofFundsVo.getBeginBal()));
			fundsVo.setIncome(fundsVo.getIncome().add(ofFundsVo.getIncome()));
			fundsVo.setExpenditure(fundsVo.getExpenditure().add(ofFundsVo.getExpenditure()));
			fundsVo.setEndBal(fundsVo.getEndBal().add(ofFundsVo.getEndBal()));
			fundsVo.setRealEndBal(fundsVo.getRealEndBal().add(ofFundsVo.getRealEndBal()));
			fundsVo.setManageMoney(fundsVo.getManageMoney().add(ofFundsVo.getManageMoney()));
			ofFundsVo.setProvinceName(corp100000.getProvinceName());
		}
		fundsVo.setMaxRate(maxRate);
		fundsVo.setEndBal(fundsVo.getBeginBal().add(fundsVo.getIncome()).subtract(fundsVo.getExpenditure()));
		fundsVo.setProvinceName(corp100000.getProvinceName());
		resultList.add(fundsVo);
		resultList.addAll(tempList);

	}

	private void assignCorpData(IncomeAndExpenditureOfFundsVo fundsVo, List<IncomeAndExpenditureOfFundsVo> list) {
		for (IncomeAndExpenditureOfFundsVo temp : list){
			fundsVo.setMaxRate(temp.getMaxRate());
			fundsVo.setBeginBal(fundsVo.getBeginBal().add(temp.getBeginBal()));
			fundsVo.setIncome(fundsVo.getIncome().add(temp.getIncome()));
			fundsVo.setExpenditure(fundsVo.getExpenditure().add(temp.getExpenditure()));
			fundsVo.setEndBal(fundsVo.getEndBal().add(temp.getEndBal()));
			fundsVo.setRealEndBal(fundsVo.getRealEndBal().add(temp.getRealEndBal()));
			fundsVo.setManageMoney(fundsVo.getManageMoney().add(temp.getManageMoney()));
		}
	}

	/**
	 * 判断上级网点是否有大区
	 * @param parentPath
	 * @return
	 */
	private CorpDTO adjustIsRegion(List<String> parentPath,String regionId) {
		CorpDTO corpDTO = incomeAndExpenditureOfFundsDao.selectRegionByCorpIds(parentPath,regionId);
		if (EmptyUtils.isEmpty(corpDTO)){
			return null;
		}
		//判断自己本身是不是大区
		if (CollectionUtils.isNotEmpty(parentPath)&&parentPath.size()==1){
			if (!EmptyUtils.isEmpty(corpService.getRegionByCorpId(corpDTO.getId(),regionId))){
				return null;
			}
		}
		return corpDTO;
	}

	/**
	 * 对List求和并且返回一个对象
	 * @param list
	 * @return
	 */
	private IncomeAndExpenditureOfFundsVo sumIncomeAndExpenditureOfFundsVo(List<IncomeAndExpenditureOfFundsVo> list){
		BigDecimal sumBeginBal = BigDecimal.ZERO;
		BigDecimal sumIncome = BigDecimal.ZERO;
		BigDecimal sumExpenditure = BigDecimal.ZERO;
		BigDecimal sumEndBal = BigDecimal.ZERO;
		BigDecimal sumRealEndBal = BigDecimal.ZERO;
		BigDecimal sumManageMoney = BigDecimal.ZERO;
		for (IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo : list) {
			sumBeginBal = sumBeginBal.add(incomeAndExpenditureOfFundsVo.getBeginBal());
			sumIncome = sumIncome.add(incomeAndExpenditureOfFundsVo.getIncome());
			sumExpenditure = sumExpenditure.add(incomeAndExpenditureOfFundsVo.getExpenditure());
			sumEndBal = sumEndBal.add(incomeAndExpenditureOfFundsVo.getEndBal());
			sumRealEndBal = sumRealEndBal.add(incomeAndExpenditureOfFundsVo.getRealEndBal());
			sumManageMoney = sumManageMoney.add(incomeAndExpenditureOfFundsVo.getManageMoney());
		}
		IncomeAndExpenditureOfFundsVo incomeAndExpenditureOfFundsVo = new IncomeAndExpenditureOfFundsVo();
		incomeAndExpenditureOfFundsVo.setBeginBal(sumBeginBal);
		incomeAndExpenditureOfFundsVo.setIncome(sumIncome);
		incomeAndExpenditureOfFundsVo.setExpenditure(sumExpenditure);
		incomeAndExpenditureOfFundsVo.setEndBal(sumEndBal);
		incomeAndExpenditureOfFundsVo.setRealEndBal(sumRealEndBal);
		incomeAndExpenditureOfFundsVo.setManageMoney(sumManageMoney);
		return incomeAndExpenditureOfFundsVo;
	}

	public DataTablesOutput getMultiLeveDatatable(DataTablesInput dataTablesInput, ICurrentUser currentUser) {
		// 参数
		Date startDate = DateUtils.parseDate(String.valueOf(dataTablesInput.getFormdata().get("startDate")));
		Date endDate = DateUtils.parseDate(String.valueOf(dataTablesInput.getFormdata().get("endDate")));
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
			corpIds = corpService.getUserCorpIds(ResourceConstant.REPORT_INCOMEANDEXP_MULTILEVE_QUERY, currentUser.getId());
		}

		// 返回结果集
		List<IncomeExpenditureReportVO> resultList = new ArrayList<>();
		b:
		if (ObjectUtil.isNotEmpty(corpIds)){
			// 100000 单位
			CorpDTO corp100000 = corpService.getCorpByCode("100000");

			List<IncomeExpenditureReportVO> dataList = incomeAndExpenditureOfFundsDao.queryMultiLeveData(corpIds,startDate,endDate,null,equityMaintenance);
			if (ObjectUtil.isEmpty(dataList)) break b;

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
						IncomeExpenditureReportVO vo = new IncomeExpenditureReportVO();
						vo.setCorpId(corpDTO.getId());
						vo.setCorpCode(corpDTO.getCode());
						vo.setCorpName(corpDTO.getName());
						vo.setParentIds(corpDTO.getParentIds());
						vo.setReportDisplay(corpDTO.getReportDisplay());
						vo.setSort(corpDTO.getSort());
						vo.setCellCode("6000");
						vo.setCellName(corpDTO.getName());

						vo.setBeginBal(BigDecimal.ZERO);
						vo.setIncome(BigDecimal.ZERO);
						vo.setExpenditure(BigDecimal.ZERO);
						vo.setEndBal(BigDecimal.ZERO);
						vo.setRealEndBal(BigDecimal.ZERO);
						vo.setDiffMoney(BigDecimal.ZERO);
						vo.setChoMoney(BigDecimal.ZERO);

						dataList.add(vo);
					}
				}

			}

			// 把100单位的上级更改为跟100000的上级一致，两个要统计在一起
			for (IncomeExpenditureReportVO temp : dataList){
				if ("100".equals(temp.getCorpCode())){
					temp.setParentIds(corp100000.getParentIds());
				}
			}

			// 获取显示单位的数据
			List<IncomeExpenditureReportVO> showList = dataList.stream().filter(data -> CommonConstant.YES.equals(data.getReportDisplay())).collect(Collectors.toList());
			// 获取隐藏单位的数据
			List<IncomeExpenditureReportVO> hideList = dataList.stream().filter(data -> CommonConstant.NO.equals(data.getReportDisplay())).collect(Collectors.toList());

			// 把隐藏单位去掉，并且把隐藏单位的金额赋值给他的第一个上级
			for (IncomeExpenditureReportVO vo : hideList){
				List<CorpDTO> parents = corpService.listParentCorp(Arrays.asList(vo.getCorpId()),false);
				for (CorpDTO temp : parents){
					if (CommonConstant.YES.equals(temp.getReportDisplay())){
						// 把vo的数据赋值到 temp
						for (IncomeExpenditureReportVO parent : showList){
							if (temp.getId().equals(parent.getCorpId())){
								parent.setBeginBal(parent.getBeginBal().add(vo.getBeginBal()));
								parent.setIncome(parent.getIncome().add(vo.getIncome()));
								parent.setExpenditure(parent.getExpenditure().add(vo.getExpenditure()));
								parent.setEndBal(parent.getEndBal().add(vo.getEndBal()));
								parent.setRealEndBal(parent.getRealEndBal().add(vo.getRealEndBal()));
								parent.setDiffMoney(parent.getDiffMoney().add(vo.getDiffMoney()));
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
			resultList.add(initIncomeExpenditureReportData("集团总计",null,dataList,false));
			resultList.add(initIncomeExpenditureReportData("全资子公司合计","0001",dataList,true));
			resultList.add(initIncomeExpenditureReportData("控股子公司合计","0002",dataList,true));

			// 获取dataList中所有大区数据，并且以sort排序
			List<IncomeExpenditureReportVO> cellList = dataList.stream().filter(data -> "6000".equals(data.getCellCode())).sorted(Comparator.comparing(IncomeExpenditureReportVO::getSort)).collect(Collectors.toList());
			// 大区数据，把corpName赋值给ProvinceName
			for (IncomeExpenditureReportVO data : cellList){
				// 添加大区
				resultList.add(data);
				data.setProvinceName(data.getCorpName());

				data.setCorpName(null);
				data.setMaxRate(null);

				// 统计大区数据
				List<IncomeExpenditureReportVO> corpList = dataList.stream().filter(temp -> temp.getParentIds().indexOf(data.getCorpId()) != -1).sorted(Comparator.comparing(IncomeExpenditureReportVO::getProvinceName).thenComparing(IncomeExpenditureReportVO::getCorpCode)).collect(Collectors.toList());
				if (ObjectUtil.isNotEmpty(corpList)){
					// 排除大区本身
					corpList.removeIf(temp -> temp.getCorpId().equals(data.getCorpId()));
					// 大区统计
					data.setBeginBal(corpList.stream().map(IncomeExpenditureReportVO::getBeginBal).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setIncome(corpList.stream().map(IncomeExpenditureReportVO::getIncome).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setExpenditure(corpList.stream().map(IncomeExpenditureReportVO::getExpenditure).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setEndBal(corpList.stream().map(IncomeExpenditureReportVO::getEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setRealEndBal(corpList.stream().map(IncomeExpenditureReportVO::getRealEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setDiffMoney(corpList.stream().map(IncomeExpenditureReportVO::getDiffMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
					data.setChoMoney(corpList.stream().map(IncomeExpenditureReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));

					// 增加：其中：全资小计，
					List<IncomeExpenditureReportVO> list0001 = corpList.stream().filter(temp -> "0001".equals(temp.getEquityCode())).collect(Collectors.toList());
					if (ObjectUtil.isNotEmpty(list0001)){
						resultList.add(initIncomeExpenditureReportData("其中：全资小计","0001",list0001,false));
						specialHandleCorpData(resultList,list0001,startDate,endDate);
					}


					// 其中：控股小计
					List<IncomeExpenditureReportVO> list0002 = corpList.stream().filter(temp -> "0002".equals(temp.getEquityCode())).collect(Collectors.toList());
					if (ObjectUtil.isNotEmpty(list0002)){
						resultList.add(initIncomeExpenditureReportData("其中：控股小计","0002",list0002,false));
						specialHandleCorpData(resultList,list0002,startDate,endDate);
					}
				}
				// 计算完成以后删除corpId，前端根据这个字段是否显示超链接
				data.setCorpId(null);
			}

			// 赋值查询日期
			for (IncomeExpenditureReportVO data : resultList){
				data.setStartDate(DateUtils.formatDate(startDate));
				data.setEndDate(DateUtils.formatDate(endDate));
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

	private void specialHandleCorpData(List<IncomeExpenditureReportVO> resultList, List<IncomeExpenditureReportVO> list, Date startDate, Date endDate) {
		// 过滤出单位为100的数据
		List<IncomeExpenditureReportVO> list100 = list.stream().filter(temp -> "100".equals(temp.getCorpCode())).collect(Collectors.toList());
		list.removeIf(temp -> "100".equals(temp.getCorpCode()));
		for (IncomeExpenditureReportVO temp : list){
			resultList.add( temp);

			// 把100单位数据合并到100000 单位
			if ("100000".equals(temp.getCorpCode())){
				List<String> corpIds = new ArrayList<>();
				corpIds.add(temp.getCorpId());
				if (ObjectUtil.isNotEmpty(list100)){
					corpIds.add(list100.get(0).getCorpId());
					// 计算利率
					List<IncomeExpenditureReportVO> rateList = new ArrayList<>();
					rateList.addAll(list100);
					rateList.add( temp);
					temp.setMaxRate(rateList.stream().map(IncomeExpenditureReportVO::getMaxRate).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
					// 合并余额
					temp.setBeginBal(temp.getBeginBal().add(list100.stream().map(IncomeExpenditureReportVO::getBeginBal).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setIncome(temp.getIncome().add(list100.stream().map(IncomeExpenditureReportVO::getIncome).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setExpenditure(temp.getExpenditure().add(list100.stream().map(IncomeExpenditureReportVO::getExpenditure).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setEndBal(temp.getEndBal().add(list100.stream().map(IncomeExpenditureReportVO::getEndBal).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setRealEndBal(temp.getRealEndBal().add(list100.stream().map(IncomeExpenditureReportVO::getRealEndBal).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setDiffMoney(temp.getDiffMoney().add(list100.stream().map(IncomeExpenditureReportVO::getDiffMoney).reduce(BigDecimal.ZERO,BigDecimal::add)));
					temp.setChoMoney(temp.getChoMoney().add(list100.stream().map(IncomeExpenditureReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add)));
				}

				// 总部的分成四行显示，分别是
				//n 总部：单位编码为100000/100，且银行账号组=“总部”的数据；D1023
				List<IncomeExpenditureReportVO> D1023List = incomeAndExpenditureOfFundsDao.queryMultiLeveData(corpIds,startDate,endDate,Arrays.asList("D1023"),null);
				if (ObjectUtil.isNotEmpty(D1023List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(总部)",temp.getProvinceName(),D1023List));
				}
				//n 广州：单位编码为100000/100，且银行账号组=“广州”的数据；D1004、D1005、D1159、D1160
				String[] groups = new String[]{"D1004","D1005","D1159","D1160"};
				List<IncomeExpenditureReportVO> D1004List = incomeAndExpenditureOfFundsDao.queryMultiLeveData(corpIds,startDate,endDate,Arrays.asList(groups),null);
				if (ObjectUtil.isNotEmpty(D1004List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(广州)",temp.getProvinceName(),D1004List));
				}
				//n 花都：单位编码为100000/100，且银行账号组=“花都”的数据；D1002
				List<IncomeExpenditureReportVO> D1002List = incomeAndExpenditureOfFundsDao.queryMultiLeveData(corpIds,startDate,endDate,Arrays.asList("D1002"),null);
				if (ObjectUtil.isNotEmpty(D1002List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(花都)",temp.getProvinceName(),D1002List));
				}
				//n 番禺：单位编码为100000，且银行账号组=“番禺”的数据。D1003
				List<IncomeExpenditureReportVO> D1003List = incomeAndExpenditureOfFundsDao.queryMultiLeveData(corpIds,startDate,endDate,Arrays.asList("D1003"),null);
				if (ObjectUtil.isNotEmpty(D1003List)){
					resultList.add(initCorp100000Data("大参林医药集团股份有限公司(番禺)",temp.getProvinceName(),D1003List));
				}

			}

		}
	}

	private IncomeExpenditureReportVO initCorp100000Data(String corpName,String provinceName,List<IncomeExpenditureReportVO> list) {
		IncomeExpenditureReportVO vo = new IncomeExpenditureReportVO();
		vo.setProvinceName(provinceName);
		vo.setMaxRate(list.stream().map(IncomeExpenditureReportVO::getMaxRate).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
		vo.setCorpName(corpName);
		vo.setBeginBal(list.stream().map(IncomeExpenditureReportVO::getBeginBal).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setIncome(list.stream().map(IncomeExpenditureReportVO::getIncome).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setExpenditure(list.stream().map(IncomeExpenditureReportVO::getExpenditure).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setEndBal(list.stream().map(IncomeExpenditureReportVO::getEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setRealEndBal(list.stream().map(IncomeExpenditureReportVO::getRealEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setDiffMoney(list.stream().map(IncomeExpenditureReportVO::getDiffMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
		vo.setChoMoney(list.stream().map(IncomeExpenditureReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
		return vo;
	}

	private IncomeExpenditureReportVO initIncomeExpenditureReportData(String provinceName, String equityCode, List<IncomeExpenditureReportVO> dataList, boolean isFilter1080) {
		IncomeExpenditureReportVO vo = new IncomeExpenditureReportVO();
		vo.setProvinceName(provinceName);
		vo.setEquityCode(equityCode);
		if (ObjectUtil.isEmpty(equityCode)){
			vo.setBeginBal(dataList.stream().map(IncomeExpenditureReportVO::getBeginBal).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setIncome(dataList.stream().map(IncomeExpenditureReportVO::getIncome).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setExpenditure(dataList.stream().map(IncomeExpenditureReportVO::getExpenditure).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setEndBal(dataList.stream().map(IncomeExpenditureReportVO::getEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setRealEndBal(dataList.stream().map(IncomeExpenditureReportVO::getRealEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setDiffMoney(dataList.stream().map(IncomeExpenditureReportVO::getDiffMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
			vo.setChoMoney(dataList.stream().map(IncomeExpenditureReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
		}else{
			if (isFilter1080){
				// 获取单位编码=1080的数据
				List<IncomeExpenditureReportVO> D1080List = dataList.stream().filter(data -> "1080".equals(data.getCorpCode())).collect(Collectors.toList());
				String filterId = D1080List.stream().map(IncomeExpenditureReportVO::getCorpId).findFirst().orElse("");
				vo.setBeginBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(IncomeExpenditureReportVO::getBeginBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setIncome(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(IncomeExpenditureReportVO::getIncome).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setExpenditure(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(IncomeExpenditureReportVO::getExpenditure).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setEndBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(IncomeExpenditureReportVO::getEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setRealEndBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(IncomeExpenditureReportVO::getRealEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setDiffMoney(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(IncomeExpenditureReportVO::getDiffMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setChoMoney(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) && data.getParentIds().indexOf(filterId) == -1).map(IncomeExpenditureReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
			}else{
				vo.setBeginBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(IncomeExpenditureReportVO::getBeginBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setIncome(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(IncomeExpenditureReportVO::getIncome).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setExpenditure(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(IncomeExpenditureReportVO::getExpenditure).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setEndBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(IncomeExpenditureReportVO::getEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setRealEndBal(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(IncomeExpenditureReportVO::getRealEndBal).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setDiffMoney(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(IncomeExpenditureReportVO::getDiffMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
				vo.setChoMoney(dataList.stream().filter(data -> equityCode.equals(data.getEquityCode()) ).map(IncomeExpenditureReportVO::getChoMoney).reduce(BigDecimal.ZERO,BigDecimal::add));
			}
		}
		return vo;
	}

	private void handleProvinceName(List<IncomeExpenditureReportVO> dataList) {
		// 获取所有省区id
		List<String> provinceIds = dataList.stream().filter(item -> StringUtils.isNotBlank(item.getProvinceId())).map(IncomeExpenditureReportVO::getProvinceId).collect(Collectors.toList());
		Map<String,String> provinceMap = new HashMap<>();
		if (ObjectUtil.isNotEmpty(provinceIds)){
			List<IncomeExpenditureReportVO> provinceList = incomeAndExpenditureOfFundsDao.queryProvince(provinceIds);
			if (ObjectUtil.isNotEmpty(provinceList)){
				provinceMap = provinceList.stream().collect(Collectors.toMap(IncomeExpenditureReportVO::getProvinceId, IncomeExpenditureReportVO::getProvinceName));
			}
		}
		// 给dataList中的省区名称赋值
		for (IncomeExpenditureReportVO temp : dataList){
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
