package com.bytter.biz.bdlinks.service;

import com.bytter.biz.bdlinks.dao.BisExcMidAccDao;
import com.bytter.biz.bdlinks.entity.BisExcMidAcc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.biz.payment.api.constant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: BIS_EXC
* @author onlineGenerator
* @date 2017-06-28 16:31:56
* @version 1.0
*
*/
@Service("bisExcMidAccService")
@Transactional
@WorkFlow(NisWorkFlowConstant.NIS_BDLINK_PAY)
public class BisExcMidAccService extends BaseService<BisExcMidAcc,String> {
	@Autowired
	private BisExcMidAccDao bisExcMidAccDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(BisExcMidAcc entity){
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
 	public void save(BisExcMidAcc entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BisExcMidAcc entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(BisExcMidAcc entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<BisExcMidAcc, String> getEntityDao() {
		return bisExcMidAccDao;
	}
}
