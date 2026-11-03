package com.bytter.check.service;

import com.bytter.check.dao.FcCompareCheckTypeDao;
import com.bytter.check.entity.FcCompareCheckType;
import com.bytter.check.vo.FcCompareCheckTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Title: Service
* @Description: FC_COMPARE_CHECK_TYPE
* @author onlineGenerator
* @date 2018-01-03 14:14:11
* @version 1.0
*
*/
@Service("fcCompareCheckTypeService")
@Transactional
public class FcCompareCheckTypeService extends BaseService<FcCompareCheckType,String> {
	@Autowired
	private FcCompareCheckTypeDao fcCompareCheckTypeDao;
	@Autowired
	private FcCheckTypeDtlService fcCheckTypeDtlService;
	@DubboReference(check = false)
	private IDictService iDictService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcCompareCheckType entity){
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
 	public void save(FcCompareCheckType entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCompareCheckType entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcCompareCheckType entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcCompareCheckType, String> getEntityDao() {
		return fcCompareCheckTypeDao;
	}
	
	/**
	 * <b>功能介绍：</b>核对标准列表数据<br>
	 * <b>日期：</b>2018年1月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DataTablesOutput getDataTablesList(DataTablesInput dataTablesInput){
		if(EmptyUtils.isEmptyList(dataTablesInput.getOrder())){
			dataTablesInput.addOrder("sort", true);
		}
		dataTablesInput.addFilter(MatchTypeEnum.EQ, "status", 0);//搜索状态正常的数据
    	DataTablesOutput outData = this.getDataTables(dataTablesInput);
    	List<FcCompareCheckType> checkTypeList = outData.getData();
    	List<FcCompareCheckTypeVO> checkTypeListVO = BeanUtils.copyListProperties(checkTypeList, FcCompareCheckTypeVO.class);
    	for (FcCompareCheckTypeVO checkTypeVO : checkTypeListVO) {
			checkTypeVO.setCheckTypesName(getcheckTypesBycheckTypeId(checkTypeVO.getId()).get("checkTypesName"));
		}
    	outData.setData(checkTypeListVO);
    	return outData;
	}
	
	/**
	 * <b>功能介绍：</b>保存核对标准<br>
	 * <b>日期：</b>2018年1月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param entity
	 * @param userId
	 */
	public void saveCompareCheckType(FcCompareCheckType entity, String userId){
		String checkTyps = entity.getCheckTypes();
		entity.setStatus(CommonConstant.STATUE_TEMP);
		entity.setCreateBy(userId);
		entity.setCreateTime(new Date());
		super.saveEntity(entity);
		fcCheckTypeDtlService.batchSaveCheckTypeDtl(entity.getId(), checkTyps);
	}
	
	/**
	 * <b>功能介绍：</b>更新核对标准<br>
	 * <b>日期：</b>2018年1月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param entity
	 */
	public void updateCompareCheckType(FcCompareCheckType entity,String userId){
		FcCompareCheckType t = this.get(entity.getId());
		BeanUtils.copyNotNullProperties(entity, t);
		t.setUpdateBy(userId);
		t.setUpdateTime(new Date());
		this.saveOrUpdate(t);
		fcCompareCheckTypeDao.deleteCheckTypeDtl(t.getId());
		fcCheckTypeDtlService.batchSaveCheckTypeDtl(entity.getId(), entity.getCheckTypes());
	}
	
	/**
	 * <b>功能介绍：</b>根据核对标准的Id合并明细code或者name<br>
	 * <b>日期：</b>2018年1月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param checkTypeId
	 * @return
	 */
	public Map<String,String> getcheckTypesBycheckTypeId(String checkTypeId){
		Map<String,String> result = new HashMap<String,String>();
		List<String> checkTypeList = fcCompareCheckTypeDao.getcheckTypesBycheckTypeId(checkTypeId);	
		String checkTypes = "";
		String checkTypesName = "";
		for (String checkType : checkTypeList) {
			checkTypes += checkType + ",";
			checkTypesName += iDictService.get("check.checkType", checkType) + "，";
		}
		result.put("checkTypes", checkTypes.substring(0, checkTypes.length()-1));
		result.put("checkTypesName", checkTypesName.substring(0, checkTypesName.length()-1));
		return result;
	}
	
	/**
	 * <b>功能介绍：</b>查询所有有效核对标准<br>
	 * <b>日期：</b>2018年1月4日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	public List<FcCompareCheckTypeVO> getCheckTypesJson(){
		List<FcCompareCheckTypeVO> checkTypesJson = fcCompareCheckTypeDao.getCheckTypeList();
		for (FcCompareCheckTypeVO checkType : checkTypesJson) {
			checkType.setCheckTypes(getcheckTypesBycheckTypeId(checkType.getId()).get("checkTypes"));
		}
		return checkTypesJson;
	}
	
}
