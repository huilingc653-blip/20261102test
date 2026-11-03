package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbBillTransferDao;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: 票据上划下拨
* @author onlineGenerator
* @date 2017-06-21 15:40:01
* @version 1.0
*
*/
@Service("abBillTransferService")
@Transactional
public class AbBillTransferService extends BaseService<AbBillTransfer,String> {
	@Autowired
	private AbBillTransferDao abBillTransferDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(AbBillTransfer entity){
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
 	public void save(AbBillTransfer entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(AbBillTransfer entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(AbBillTransfer entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<AbBillTransfer, String> getEntityDao() {
		return abBillTransferDao;
	}
}
