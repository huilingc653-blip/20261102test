package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.dao.AbBillTransferDownDao;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownCorpDtlVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownManageVo;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownNetCollectVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @Title: Service
 * @Description: 票据划拨公共业务处理类
 * @author onlineGenerator
 * @date 2017-10-31
 * @version 1.0
 *
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_RECALL)
@Service("abBillTransferDownService")
@Transactional
public class AbBillTransferDownService extends BaseService<AbBillTransferDownManageVo, String> {
	@Autowired
	private AbBillTransferDownDao abBillTransferDownDao;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	public BaseDao<AbBillTransferDownManageVo, String> getEntityDao() {
		return abBillTransferDownDao;
	}
	/**
	 * 加载票据下拨业务信息数据
	 * @param dataTablesInput 查询入参
	 * @return 返回列表数据信息
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listAbBillQueryDtlInfo(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_TRANSFERDOWN_QUERY_CORPOP,dataTablesInput.getCurrentUserId());
		String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
		//设置是否过滤票据账号
		accountGrantService.setElectricBillAccount(dataTablesInput,AccebillConstant.AB_BILL_TRANSFERDOWN_QUERY_CORPOP,corpIds);
		DataTablesOutput<AbBillTransferDownManageVo> dataTablesOutput = abBillTransferDownDao.listAbBillQueryDtl(dataTablesInput, corpAuthList);
		Set<String> corpIdSet = new HashSet<>();
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TRANSFERDOWNAPPLY_STATUS", "AB_BILL_BUSTYPE_NAME");
			for (AbBillTransferDownManageVo bill : listData){
				injectValues(bill, listDictMap);
				if(StrUtil.isNotBlank(bill.getApplyCorpId())){
					corpIdSet.add(bill.getApplyCorpId());
				}
				if(StrUtil.isNotBlank(bill.getCorpId())){
					corpIdSet.add(bill.getCorpId());
				}
				if(StrUtil.isNotBlank(bill.getBusCorpId())){
					corpIdSet.add(bill.getBusCorpId());
				}
			}
		});
		if(corpIdSet.size() <= 0){
			return dataTablesOutput;
		}
		Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
		if(CollectionUtil.isEmpty(stringCorpDTOMap)){
			return dataTablesOutput;
		}
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			for (AbBillTransferDownManageVo bill : listData){
				CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
				CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
				CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
				bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
				bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
				bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
				bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
				bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
			}
		});
		return dataTablesOutput;
	}
	private void injectValues(AbBillTransferDownManageVo bill,Map<String, Map<String, DictDTO>> listDictMap){
		// 业务处理状态
		Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TRANSFERDOWNAPPLY_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
					.ifPresent(dict -> bill.setStatusName(dict.getName()));
		});
		// 业务类型
		Optional.ofNullable(bill.getBusType()).ifPresent(busType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSTYPE_NAME");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(busType + "")))
					.ifPresent(dict -> bill.setBusTypeName(dict.getName()));
		});
	}
	/**
	 * 加载网点票据划拨汇总表信息数据
	 * @param dataTablesInput 查询入参
	 * @return 返回列表数据信息
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listNetCollectInfo(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ABBILLTRANSFERDOWN_NETCOLLECT_QUERY_CORPOP,dataTablesInput.getCurrentUserId());
		DataTablesOutput<AbBillTransferDownNetCollectVO> dataTablesOutput = abBillTransferDownDao.listNetCollectInfo(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			listData = injectCorpAndNetInfo(listData);
			dataTablesOutput.setData(listData);
		});
		return dataTablesOutput;
	}
	private List<AbBillTransferDownNetCollectVO> injectCorpAndNetInfo(List<AbBillTransferDownNetCollectVO> listData) {
		// 根据corpId获取单位和网点信息
		List<String> corpIdList = listData.stream().map(AbBillTransferDownNetCollectVO::getCorpId).collect(Collectors.toList());
		List<Map<String, Object>> corpInfoList = abCorpService.getCorpAndNetInfoByCorpId(corpIdList);
		// 合并结果集
		listData = listData.stream()
				.map(source -> corpInfoList.stream()
						.filter(target -> ObjectUtil.equal(Convert.toStr(target.get("CORPID")), source.getCorpId()))
						.findFirst()
						.map(target -> {
							source.setCorpCode(Convert.toStr(target.get("CORPCODE")));
							source.setCorpName(Convert.toStr(target.get("CORPNAME")));
							source.setNetCode(Convert.toStr(target.get("NETCODE")));
							source.setNetName(Convert.toStr(target.get("NETNAME")));
							return source;
						}).orElse(source))
				.collect(Collectors.toList());
		// 添加冗余数据
		Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BRIEF_STATUS");
		for (AbBillTransferDownNetCollectVO bill : listData) {
			// 业务类型
			Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
				Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSTYPE_NAME");
				Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
						.ifPresent(dict -> bill.setBriefName(dict.getName()));
			});
		}
		return listData;
	}
	/**
	 * 加载票据调拨明细表信息数据
	 * @param dataTablesInput 查询入参
	 * @return 返回列表数据信息
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listCorpDtlInfo(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ABBILLTRANSFERDOWN_CORPDTL_QUERY_CORPOP,dataTablesInput.getCurrentUserId());
		DataTablesOutput<AbBillTransferDownCorpDtlVO> dataTablesOutput = abBillTransferDownDao.listCorpDtlInfo(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
			// 根据corpId获取单位和网点信息
			List<String> corpIdList = listData.stream().map(AbBillTransferDownCorpDtlVO::getCorpId).collect(Collectors.toList());
			List<Map<String, Object>> corpInfoList = abCorpService.getCorpAndNetInfoByCorpId(corpIdList);
			// 合并结果集
			listData = listData.stream()
					.map(source -> corpInfoList.stream()
							.filter(target -> ObjectUtil.equal(Convert.toStr(target.get("CORPID")), source.getCorpId()))
							.findFirst()
							.map(target -> {
								source.setCorpCode(Convert.toStr(target.get("CORPCODE")));
								source.setCorpName(Convert.toStr(target.get("CORPNAME")));
								source.setNetCode(Convert.toStr(target.get("NETCODE")));
								source.setNetName(Convert.toStr(target.get("NETNAME")));
								return source;
							}).orElse(source))
					.collect(Collectors.toList());
			// 添加冗余数据
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BRIEF_STATUS");
			for (AbBillTransferDownCorpDtlVO bill : listData) {
				// 业务类型
				Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
					Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BUSTYPE_NAME");
					Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
							.ifPresent(dict -> bill.setBriefName(dict.getName()));
				});
			}
			dataTablesOutput.setData(listData);
		});
		return dataTablesOutput;
	}
	/**
	 * 创建柱状图报表
	 * @param vo
	 * @return
	 */
	public Object createNetCollectInfoSumBar(AbBillTransferDownNetCollectVO vo, ICurrentUser user) {
		List dataList = null;
		if (!EmptyUtils.isEmpty(vo)) {
			List<String> corpAuthList=abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ABBILLTRANSFERDOWN_NETCOLLECT_QUERY_CORPOP,user.getId());
			dataList = this.abBillTransferDownDao.listNetCollectInfo(vo,corpAuthList);
			// 组合单位和网点信息
			if (ObjectUtil.isNotEmpty(dataList)){
				dataList = injectCorpAndNetInfo(dataList);
			}
		}
		return this.getBarData(dataList);
	}
	/**
	 * 组装报表数据
	 * @param list
	 * @return
	 */
	private Object getBarData(List<AbBillTransferDownNetCollectVO> list) {
		Map<String, Object> map = new HashMap<>();
		Set<String> legend = new HashSet();
		Set<String> corpSet = new HashSet();
		//类型
		legend.add("上划金额");
		legend.add("归集金额");
		legend.add("下拨金额");
		legend.add("存放中心余额");
		for (AbBillTransferDownNetCollectVO vo : list) {
			corpSet.add(vo.getCorpName());
			//legend.add(vo.getCurName());
		}
		map.put("legend", legend);
		//map.put("xAxisData2", corpSet);
		List<String> yAxisList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			AbBillTransferDownNetCollectVO netVo = list.get(i);
			yAxisList.add(netVo.getCorpName());
		}
		map.put("yAxisData1", yAxisList);
		List<Series> series = new ArrayList<>();
		Map<String, String> openParams = new HashMap<>();
		for (String moneyTypeName : legend) {
			List<Object> datas = new ArrayList<>();
			for (int j=0;j<yAxisList.size();j++) {
				boolean flag = true;
				String curId = "";
				String corpId = "";
				for (AbBillTransferDownNetCollectVO vo : list) {
					if (yAxisList.get(j).equalsIgnoreCase(vo.getCorpName())) {
						flag = false;
						corpId = vo.getCorpId();
						if(moneyTypeName.equals("存放中心余额"))datas.add(vo.getNetBalMoney());
						if(moneyTypeName.equals("下拨金额"))datas.add(vo.getXbMoney());
						if(moneyTypeName.equals("归集金额"))datas.add(vo.getGjMoney());
						if(moneyTypeName.equals("上划金额"))datas.add(vo.getShMoney());
						break;
					}
				}
				if (flag) {
					datas.add(BigDecimal.ZERO);
					datas.add(BigDecimal.ZERO);
					datas.add(BigDecimal.ZERO);
					datas.add(BigDecimal.ZERO);
				} /*else {
					openParams.put(corpName  + moneyTypeName, corpId + "!-!5,8!-!" + curId);
					openParams.put(corpName  + moneyTypeName, corpId + "!-!2,7!-!" + curId);
					openParams.put(corpName  + moneyTypeName, corpId + "!-!3!-!" + curId);
					openParams.put(corpName  + moneyTypeName, corpId + "!-!6!-!" + curId);
				}*/
			}
			// 设置数据
			Bar bar = new Bar();
			bar.setName(moneyTypeName);
			bar.setType(SeriesType.bar);
			//bar.setStack("stack");
			bar.setData(datas);
			bar.setSymbolSize(12);
			series.add(bar);
		}
		map.put("openParams", openParams);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			map.put("series", mapper.writeValueAsString(series));
			return map;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "[]";
		}
	}
}
