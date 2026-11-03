package com.bytter.biz.fundwarn.service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.biz.fundwarn.vo.BtWarnDataDtlInfoVO;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.fundwarn.dao.BtWarnDataDtlDao;
import com.bytter.biz.fundwarn.entity.BtWarnData;
import com.bytter.biz.fundwarn.entity.BtWarnDataDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
/**
 * @Title: Service
 * @Description: 预警策略数据信息 业务处理类
 * @author zhenghj
 * @date 2018-10-12
 */
@Service("btWarnDataDtlService")
@Transactional
public class BtWarnDataDtlService extends BaseService<BtWarnDataDtl,String> {
	
	@Autowired
	private BtWarnDataDtlDao btWarnDataDtlDao;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	
    @Override
    public BaseDao<BtWarnDataDtl, String> getEntityDao() {
        return btWarnDataDtlDao;
    }
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BtWarnDataDtl entity){
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
	* 根据id删除
	* @param id
	*/
 	public void deleteByDataId(String dataId){
 		Map<String,Object> params=new HashMap<>();
 		params.put("dataId", dataId);
 		deleteBtWarnDataDtlByParams(params);
 	}
 	
 	/**
 	 * 根据条件删除预警明细信息
 	 * @param params
 	 */
 	public void deleteBtWarnDataDtlByParams(Map<String,Object> params){
 		StringBuilder hqlW=new StringBuilder();
    	String wFields="id,dataId";
    	hqlW.append(SqlUtil.getWhereByParamsAndField(wFields, params));
 		btWarnDataDtlDao.deleteBtWarnDataDtlByParams(params, hqlW.toString());
 	}
 	
	/**
	* 保存
	* @param entity
	*/
 	public void save(BtWarnDataDtl entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BtWarnDataDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BtWarnDataDtl entity){
 		super.saveOrUpdate(entity);
 	}
    /**
     * 加载预警明细数据信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
	public DataTablesOutput listWarnDataDtl(DataTablesInput dataTablesInput) {
		DataTablesOutput dataTablesOutput = btWarnDataDtlDao.getDataTablesUnion(dataTablesInput);
		//预警币别名称  设值
		List<BtWarnDataDtlInfoVO> btWarnDataDtlInfoVOList = dataTablesOutput.getData();
		for(BtWarnDataDtlInfoVO btWarnDataDtlInfoVO:btWarnDataDtlInfoVOList){
			BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(btWarnDataDtlInfoVO.getCurId());
			if(!EmptyUtils.isEmpty(btCurrencyDTO)){
				btWarnDataDtlInfoVO.setCurName(btCurrencyDTO.getCurName());
			}
		}
    	return dataTablesOutput;
    }
    
    /**
     * 批量写入预警查询数据明细信息
     */
    public void batchStrategyDataDtl(List<BtWarnDataDtl> warnDataDtllist,BtWarnData warnData){
    	if(ListUtils.isNullOrEmpty(warnDataDtllist)){
    		return;
    	}
    	//先删除就数据
    	deleteByDataId(warnData.getId());
    	//再写入全新数据
    	for(BtWarnDataDtl info:warnDataDtllist){
    		info.setDataId(warnData.getId());
    		info.setCreateDate(new Date());
    	}
    	btWarnDataDtlDao.batchSave(warnDataDtllist);
    }
}
