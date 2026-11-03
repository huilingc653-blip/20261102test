package com.bytter.biz.fundwarn.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.biz.fundwarn.dao.BtWarnDataDao;
import com.bytter.biz.fundwarn.entity.BtWarnData;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.vo.BtWarnDataInfoVO;
import com.bytter.biz.fundwarn.vo.HomeWarnDataSearchVO;
import com.bytter.biz.fundwarn.vo.HomeWarnDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @Title: Service
 * @Description: 预警策略数据信息 业务处理类
 * @author zhenghj
 * @date 2018-10-12
 */
@Service("btWarnDataService")
@Transactional
public class BtWarnDataService extends BaseService<BtWarnData,String> {
	@Autowired
	private BtWarnDataDao btWarnDataDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	IBankTypeService bankTypeService;
	
    @Override
    public BaseDao<BtWarnData, String> getEntityDao() {
        return btWarnDataDao;
    }
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BtWarnData entity){
		super.deleteEntity(entity);
	}
	
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
 	
	/**
	* 保存
	* @param entity
	*/
 	public void save(BtWarnData entity){
 		if(entity.getCreateDate()==null){
 			entity.setCreateDate(new Date());
 		}
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BtWarnData entity){
		entity.setUpdateDate(new Date());
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BtWarnData entity){
 		if(StringUtils.isEmpty(entity.getId())
 				&&entity.getCreateDate()==null){
 			entity.setCreateDate(new Date());
 		}
 		if(StringUtils.isNotEmpty(entity.getId())){
 			entity.setUpdateDate(new Date());
 		}
        btWarnDataDao.flush();
        btWarnDataDao.clear();
 		super.saveOrUpdate(entity);
 	}
    /**
     * 加载预警主数据信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput listWarnData(DataTablesInput dataTablesInput) {
		List<String> corpAuthList = corpService.getUserCorpIds(FundWarnConstant.RESOURCE_WARNSDATA_NETOP,dataTablesInput.getCurrentUserId());
		DataTablesOutput dataTablesOutput = btWarnDataDao.getDataTablesUnion(dataTablesInput, corpAuthList);
		//预警类别名称  预警币别名称  预警维度名称  预警级别名称  设值
		List<BtWarnDataInfoVO> btWarnDataInfoVOList = dataTablesOutput.getData();
		Map<String, BankTypeDTO> mapType = bankTypeService.getBankTypeMap();
		for(BtWarnDataInfoVO btWarnDataInfoVO:btWarnDataInfoVOList){
			BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(btWarnDataInfoVO.getCurId());
			if(!EmptyUtils.isEmpty(btCurrencyDTO)){
				btWarnDataInfoVO.setCurName(btCurrencyDTO.getCurName());
			}
			BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccInfoByBankAcc(btWarnDataInfoVO.getBankAcc());
			if(!ObjectUtils.isEmpty(btBankAccDTO)) {
				btWarnDataInfoVO.setBankTypeName(!ObjectUtils.isEmpty(mapType.get(btBankAccDTO.getBankTypeId())) ? mapType.get(btBankAccDTO.getBankTypeId()).getTypeName() : "");
			}
			String corpName=corpService.getCorpName(btWarnDataInfoVO.getCorpId());
			btWarnDataInfoVO.setCorpName(corpName);
			btWarnDataInfoVO.setTypeName(FundWarnConstant.TYPECODE_NAME.get(btWarnDataInfoVO.getTypeCode()));
			btWarnDataInfoVO.setDimensionName(FundWarnConstant.DIMENSION_NAME.get(btWarnDataInfoVO.getDimensionCode()));
			btWarnDataInfoVO.setWarnLevelName(FundWarnConstant.WARNLEVEL_NAME.get(btWarnDataInfoVO.getWarnLevel()));
		}
		return dataTablesOutput;
    }
    
    /**
     * 根据params获取查询数据
     * @param params 入参
     * @return
     */
    @Transactional(readOnly = true)
    public BtWarnData getBtWarnDataByParams(Map<String,Object> params){
    	StringBuilder hqlW=new StringBuilder();
    	String wFields="id,wsId,corpId,code,name,curId,typeCode,dimensionCode,no,bankAcc,fcAcc,monthNum,sizeNum,warnDate";
    	hqlW.append(SqlUtil.getWhereByParamsAndField(wFields, params));
    	List<BtWarnData> list=btWarnDataDao.getBtWarnDataByParams(params,hqlW.toString());
    	if(ListUtils.isNullOrEmpty(list)){
    		return null;
    	}
    	return list.get(0);
    }
	/**
	 * 结算收支分析图
	 * @param searchVO
	 * @param user
	 * @return
	 */
	public Map<String, Object> createWarnDataBar(HomeWarnDataSearchVO searchVO, ICurrentUser user) {
		List<String> corpIds = null;
		if(StringUtils.isNotBlank(searchVO.getCorpId())){
			corpIds = new ArrayList<>();
			for(String corpId:searchVO.getCorpId().split(CommonConstant.COMMA_SPLIT)){
				corpIds.add(corpId);
			}
		} else {
			//获取资源权限范围内的单位信息
			corpIds = corpService.getUserCorpIds(FundWarnConstant.RESOURCE_WARNSDATA_QUERY, user.getId());
		}
		String corpAuthSql = " and " + SqlUtil.getInSql(corpIds, "a.corp_Id", true);
		//x轴
		List<String> xData = new ArrayList<>();
		Date beginDate = searchVO.getBeginDate() == null ? DateUtils.addDay(DateUtils.getDate(), -7):searchVO.getBeginDate();
		Date endDate = searchVO.getEndDate() == null ? DateUtils.getDate():searchVO.getEndDate();
		searchVO.setBeginDate(beginDate);
		searchVO.setEndDate(endDate);
		List<HomeWarnDataVO> listData = this.btWarnDataDao.getWarnDataTimes(searchVO, corpAuthSql);
		Map<String, Integer> dataMap = new HashMap<>();
		for(HomeWarnDataVO vo:listData){
			String key = vo.getWarnLevel()+DateUtils.format(vo.getWarnDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
			dataMap.put(key, vo.getWarnTimes());
		}
		List<Integer> warnLevel0 = new ArrayList<>();//低
		List<Integer> warnLevel1 = new ArrayList<>();//中
		List<Integer> warnLevel2 = new ArrayList<>();//高
		while (beginDate.compareTo(endDate) <= 0){
			String beginDateStr = DateUtils.formatDate(beginDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
			xData.add(beginDateStr);
			//低
			String key0 = "0"+beginDateStr;
			if(dataMap.containsKey(key0)){
				warnLevel0.add(dataMap.get(key0));
			} else {
				warnLevel0.add(0);
			}
			//中
			String key1 = "1"+beginDateStr;
			if(dataMap.containsKey(key1)){
				warnLevel1.add(dataMap.get(key1));
			} else {
				warnLevel1.add(0);
			}
			//高
			String key2 = "2"+beginDateStr;
			if(dataMap.containsKey(key2)){
				warnLevel2.add(dataMap.get(key2));
			} else {
				warnLevel2.add(0);
			}
			beginDate = DateUtils.addDay(beginDate, 1);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("xData", JsonUtils.toJSONString(xData));
		map.put("warnLevel0", JsonUtils.toJSONString(warnLevel0));
		map.put("warnLevel1", JsonUtils.toJSONString(warnLevel1));
		map.put("warnLevel2", JsonUtils.toJSONString(warnLevel2));
		return map;
	}
	/**
	 * 获取预警统计数量
	 *
	 * @param userId
	 * @return
	 */
	public Integer getWarnDataCountByUserId(String userId) {
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(FundWarnConstant.RESOURCE_WARNSDATA_NETOP,userId), "a.corp_Id", true);
		return btWarnDataDao.getWarnDataCountByUserId(corpAuthSql);
	}
	
	public void saveOrUpdateEntity(BtWarnData btWarnData) {
		if(!ObjectUtils.isEmpty(btWarnData) && !ObjectUtils.isEmpty(btWarnData.getId())) {
			BtWarnData data = btWarnDataDao.get(btWarnData.getId(), true);
			data.setCorpId(btWarnData.getCorpId());
			data.setBankAcc(btWarnData.getBankAcc());
			data.setTriggerNum(btWarnData.getTriggerNum());
			data.setWarnDate(btWarnData.getWarnDate());
			data.setUpdateDate(new Date());
			btWarnDataDao.update(data);
		}else {
			btWarnDataDao.save(btWarnData);
		}
	}
}
