package com.bytter.biz.finacial.paramconfig.service;
import com.bytter.biz.finacial.paramconfig.entity.ChargeTypeBankTypeRel;
import com.bytter.biz.finacial.paramconfig.dao.ChargeTypeBankTypeRelDao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
* @Title: Service
* @Description: 费用类型与银行类型关联表
* @author onlineGenerator
* @date 2017-05-23 14:29:59
* @version 1.0
*
*/
@Service("chargeTypeBankTypeRelService")
@Transactional
public class ChargeTypeBankTypeRelService extends BaseService<ChargeTypeBankTypeRel,String> {
	@Autowired
	private ChargeTypeBankTypeRelDao chargeTypeBankTypeRelDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(ChargeTypeBankTypeRel entity){
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
 	public void save(ChargeTypeBankTypeRel entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(ChargeTypeBankTypeRel entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(ChargeTypeBankTypeRel entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<ChargeTypeBankTypeRel, String> getEntityDao() {
		return chargeTypeBankTypeRelDao;
	}
	/**
	 * <b>功能介绍:批量新增银行类别</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public void batchBankTypeRel(List<ChargeTypeBankTypeRel> chargeTypeBankTypeRel,String chargeTypeId){
		if(StringUtils.isNotBlank(chargeTypeId)&&!"undefined".equals(chargeTypeId)){
			for(ChargeTypeBankTypeRel ctbtr: chargeTypeBankTypeRel){
				ctbtr.setChargeTypeId(chargeTypeId);
			}
		}
		batchBankTypeRel(chargeTypeBankTypeRel);
	}
	/**
	 * <b>功能介绍:批量新增银行类别</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public void batchBankTypeRel(List<ChargeTypeBankTypeRel> chargeTypeBankTypeRel){
		for (ChargeTypeBankTypeRel ctbtr : chargeTypeBankTypeRel) {
			if(ctbtr.getBankType()!=""){
				chargeTypeBankTypeRelDao.save(ctbtr);
			}
		}
	}
	
	/**
	 * 功能介绍 : 获取费用类型关联银行类型集合。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<ChargeTypeBankTypeRel> getChargeTypeRelsByTypeId(String typeId) {
		return chargeTypeBankTypeRelDao.getChargeTypeRelsByTypeId(typeId);
	}
	
}
