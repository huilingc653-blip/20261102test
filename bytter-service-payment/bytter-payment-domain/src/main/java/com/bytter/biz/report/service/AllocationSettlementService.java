package com.bytter.biz.report.service;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.report.dao.AllocationSettlementDao;
import com.bytter.biz.report.vo.AllocationSettlementDtlVO;
import com.bytter.biz.report.vo.AllocationSettlementVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoup
 * @Description 结算量统计表
 * @date 2018.4.18
 */
@Service("allocationSettlementService")
@Transactional
public class AllocationSettlementService extends BaseService<BisAccDtlDTO, String>{
	
	@Autowired
	private AllocationSettlementDao allocationSettlementDao ;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@DubboReference(check = false)
	private IDictService dictService;
	
	@Override
	public BaseDao<BisAccDtlDTO, String> getEntityDao() {
		return allocationSettlementDao;
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getData(AllocationSettlementVO allocationSettlementVO,ICurrentUser user){
		DataTablesOutput datas = null;
		String type=allocationSettlementVO.getType();
		StringBuilder sql = new StringBuilder();
		if("1".equals(type)){
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY, user.getId()), "hl.hcorpId", true);
			sql.append(corpAuthSql);
			datas = allocationSettlementDao.getDataByCorp(allocationSettlementVO,sql.toString());
		}else if("2".equals(type)){
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY, user.getId()), "acc.corp_id", true);
			datas = allocationSettlementDao.getDataByBankAcc(allocationSettlementVO,corpAuthSql);
		}else if("3".equals(type)){
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY, user.getId()), "acc.corp_id", true);
			datas = allocationSettlementDao.getDataByBankName(allocationSettlementVO,corpAuthSql);
		}else{
			String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY, user.getId()), "acc.corp_id", true);
			sql.append(corpAuthSql);
			datas = allocationSettlementDao.getDataByBankType(allocationSettlementVO,sql.toString());
		}
		//获取汇率
		if (datas != null && datas.getData() != null) {
			List<AllocationSettlementVO> list = datas.getData();
			HashMap<String, BigDecimal> curExrateMap = new HashMap<>();
			Map<String, DictDTO> dictDTOMap = dictService.listMap("btBankAcc.accType");
			List<String> curIds = list.stream().map(item -> item.getCurId()).collect(Collectors.toList());
			Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(curIds);
			for(AllocationSettlementVO vo:list){
				BigDecimal curExrate = BigDecimal.ONE;
				if(CommonConstant.YES.equals(allocationSettlementVO.getChangeCur())) {
					if (curExrateMap.containsKey(vo.getCurId())) {
						curExrate = curExrateMap.get(vo.getCurId());
					} else {
						curExrate = fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), allocationSettlementVO.getReducedCurId(), DateUtils.setSpecifiedDateOfTime(allocationSettlementVO.getRateDate()), allocationSettlementVO.getRateType());
						curExrateMap.put(vo.getCurId(), curExrate);
					}
				}
				vo.setAccTypeName(dictDTOMap.getOrDefault(vo.getAccType(), DictDTO.EMPTY).getName());
				vo.setCurName(currencyNameMap.get(vo.getCurId()));
				vo.setCurExrate(curExrate);
			}
		}
		return datas;
	}
	/**
	 * 根据条件查询收支明细详情
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	public DataTablesOutput getDataDtlByType(DataTablesInput dataTablesInput, ICurrentUser user){
		String resource = ResourceConstant.NETBANK_REPORT_ALLOCATIONSETTLEMENT_QUERY;
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "acc.corp_Id", true);
		DataTablesOutput dataTablesOutput = allocationSettlementDao.getDataDtlByType(dataTablesInput, corpAuthSql);
		List<AllocationSettlementDtlVO> list = dataTablesOutput.getData();
		if(!CollectionUtils.isEmpty(list)){
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
			for(AllocationSettlementDtlVO vo : list){
				if(null != corpMap){
					if(corpMap.containsKey(vo.getCorpId())){
						CorpDTO corpDTO = corpMap.get(vo.getCorpId());
						if(null != corpDTO){
							vo.setCorpName(corpDTO.getName());
						}
					}
				}
				if(null != currencyMap){
					if(currencyMap.containsKey(vo.getCurId())){
						BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
						if(null != btCurrencyDTO){
							vo.setCurName(btCurrencyDTO.getCurName());
						}
					}
				}
			}
		}
		return dataTablesOutput;
	}
	/**
	 *币种转换校验
	 *@Param origCurId 原币种
	 *@Param convertCurId   转换币种
	 *@Param rateType
	 *@Param rateDate
	 *@return
	 */
	public void checkConvertCurId(String origCurId, String convertCurId, String rateType, String rateDate) {
		//如果是查询所以币种
		List<BtCurrencyDTO> curList = null;
		if (StringUtils.isNotEmpty(origCurId)) {
			curList = new ArrayList<>();
			String[] curIds = origCurId.split(CommonConstant.COMMA_SPLIT);
			for(String curId : curIds){
				BtCurrencyDTO cur = new BtCurrencyDTO();
				cur.setId(curId);
				curList.add(cur);
			}
		}else {
			curList = currencyService.getBtCurrency();
		}
		for (BtCurrencyDTO dto : curList) {
			BigDecimal curExrate = BigDecimal.ZERO;
			if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(dto.getId())) {
				curExrate = fcExcrateAdjustService.getExchangeRateBy(dto.getId(), convertCurId, DateUtils.setSpecifiedDateOfTime(DateUtils.parse(rateDate, "yyyy-MM")), rateType);//距查询日期最近的币别汇率
			}
			if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
				throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
			}
		}
	}
}
