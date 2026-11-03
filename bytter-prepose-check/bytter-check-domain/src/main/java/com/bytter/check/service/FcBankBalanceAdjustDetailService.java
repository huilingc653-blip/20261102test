package com.bytter.check.service;
import com.bytter.check.dao.FcBankBalanceAdjustDetailDao;
import com.bytter.check.entity.FcBankBalanceAdjustDetail;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: 对账余额调节明细表
* @author onlineGenerator
* @date 2017-10-17 09:55:30
* @version 1.0
*
*/
@Service("fcBankBalanceAdjustDetailService")
@Transactional
public class FcBankBalanceAdjustDetailService extends BaseService<FcBankBalanceAdjustDetail,String> {
	@Autowired
	private FcBankBalanceAdjustDetailDao fcBankBalanceAdjustDetailDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcBankBalanceAdjustDetail entity){
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
 	public void save(FcBankBalanceAdjustDetail entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcBankBalanceAdjustDetail entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcBankBalanceAdjustDetail entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcBankBalanceAdjustDetail, String> getEntityDao() {
		return fcBankBalanceAdjustDetailDao;
	}
}
