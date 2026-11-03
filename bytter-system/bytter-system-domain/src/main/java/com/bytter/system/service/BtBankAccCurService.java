package com.bytter.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.system.dao.BtBankAccCurDao;
import com.bytter.system.entity.BtBankAccCur;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: BT_BANK_ACC_CUR
* @author onlineGenerator
* @date 2017-06-02 18:03:22
* @version 1.0
*
*/
@Service("btBankAccCurService")
@Transactional
public class BtBankAccCurService extends BaseService<BtBankAccCur,String> {
	@Autowired
	private BtBankAccCurDao btBankAccCurDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(BtBankAccCur entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(BtBankAccCur entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BtBankAccCur entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(BtBankAccCur entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<BtBankAccCur, String> getEntityDao() {
		return btBankAccCurDao;
	}
}
