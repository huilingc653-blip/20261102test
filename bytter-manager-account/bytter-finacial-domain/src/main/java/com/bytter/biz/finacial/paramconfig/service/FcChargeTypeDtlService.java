package com.bytter.biz.finacial.paramconfig.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.paramconfig.dao.FcChargeTypeDtlDao;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeTypeDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
/**
* @Title: Service
* @Description: 费用类型明细
* @author onlineGenerator
* @date 2017-06-15 11:24:27
* @version 1.0
*
*/
@Service("fcChargeTypeDtlService")
@Transactional
public class FcChargeTypeDtlService extends BaseService<FcChargeTypeDtl,String> {
	@Autowired
	private FcChargeTypeDtlDao fcChargeTypeDtlDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcChargeTypeDtl entity){
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
 	public void save(FcChargeTypeDtl entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcChargeTypeDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcChargeTypeDtl entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcChargeTypeDtl, String> getEntityDao() {
		return fcChargeTypeDtlDao;
	}
	
	/**
	 * <b>功能介绍:批量新增费用明细</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public void batchAddFcChargeTypeDtl(List<FcChargeTypeDtl> fcChargeTypeDtls,String chargeTypeId){
		if(StringUtils.isNotBlank(chargeTypeId)&&!"undefined".equals(chargeTypeId)){
			for (FcChargeTypeDtl fcChargeTypeDtl : fcChargeTypeDtls) {
				fcChargeTypeDtl.setChargeTypeId(chargeTypeId);
			}
		}
		batchAddFcChargeTypeDtl(fcChargeTypeDtls);
	}
	/**
	 * <b>功能介绍:批量新增费用明细</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public void batchAddFcChargeTypeDtl(List<FcChargeTypeDtl> fcChargeTypeDtls){
		for (FcChargeTypeDtl fcChargeTypeDtl : fcChargeTypeDtls) {
			fcChargeTypeDtlDao.save(fcChargeTypeDtl);
		}
	}
	
	/**
	 * 功能介绍 : 获取费用类型关联费用计算明细集合。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcChargeTypeDtl> getChargeTypeDtlByTypeId(String typeId) {
		return fcChargeTypeDtlDao.getChargeTypeDtlByTypeId(typeId);
	}
	
}
