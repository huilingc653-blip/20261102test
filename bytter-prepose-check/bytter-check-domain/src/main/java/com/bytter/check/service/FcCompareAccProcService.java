package com.bytter.check.service;
import com.bytter.check.dao.FcCompareAccProcDao;
import com.bytter.check.entity.FcCompareAccProc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: FC_COMPARE_ACC_PROC
* @author onlineGenerator
* @date 2017-10-17 17:03:32
* @version 1.0
*
*/
@Service("fcCompareAccProcService")
@Transactional
public class FcCompareAccProcService extends BaseService<FcCompareAccProc,String> {
	@Autowired
	private FcCompareAccProcDao fcCompareAccProcDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcCompareAccProc entity){
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
 	public void save(FcCompareAccProc entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCompareAccProc entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcCompareAccProc entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcCompareAccProc, String> getEntityDao() {
		return fcCompareAccProcDao;
	}
}
