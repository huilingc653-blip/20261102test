package com.bytter.subject.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.subject.dao.FcSubjectInitDao;
import com.bytter.subject.entity.FcSubjectInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: 格式定义表
* @author onlineGenerator
* @date 2017-08-30 17:58:29
* @version 1.0
* TODO
*/
@Service("fcSubjectInitService")
@Transactional
public class FcSubjectInitService extends BaseService<FcSubjectInit,String> {
	@Autowired
	private FcSubjectInitDao fcSubjectInitDao;
	
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcSubjectInit entity){
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
 	public void save(FcSubjectInit entity){
 		super.saveEntity(entity);
 	}
 	
	/**
	* 更新
	* @param entity
	*/
	public void update(FcSubjectInit entity){
		super.updateEntity(entity);
	}
	
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcSubjectInit entity){
 		super.saveOrUpdate(entity);
 	}
 	
	
 	
	public BaseDao<FcSubjectInit, String> getEntityDao() {
		return fcSubjectInitDao;
	}
}
