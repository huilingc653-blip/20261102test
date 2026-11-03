package com.bytter.check.service;
import com.bytter.check.dao.FcBankBillProcDao;
import com.bytter.check.entity.FcBankBillProc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: FC_BANK_BILL_PROC
* @author onlineGenerator
* @date 2017-10-17 17:03:07
* @version 1.0
*
*/
@Service("fcBankBillProcService")
@Transactional
public class FcBankBillProcService extends BaseService<FcBankBillProc,String> {
	@Autowired
	private FcBankBillProcDao fcBankBillProcDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcBankBillProc entity){
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
 	public void save(FcBankBillProc entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcBankBillProc entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcBankBillProc entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcBankBillProc, String> getEntityDao() {
		return fcBankBillProcDao;
	}
}
