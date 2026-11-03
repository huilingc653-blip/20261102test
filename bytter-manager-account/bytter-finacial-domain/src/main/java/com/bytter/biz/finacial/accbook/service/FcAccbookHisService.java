package com.bytter.biz.finacial.accbook.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.dao.FcAccbookHisDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookHis;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_ACCBOOK_HIS
* @author onlineGenerator
* @date 2017-06-15 18:18:02
* @version 1.0
*
*/
@Service("fcAccbookHisService")
@Transactional
public class FcAccbookHisService extends BaseService<FcAccbookHis,String> {
	@Autowired
	private FcAccbookHisDao fcAccbookHisDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccbookHis entity){
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
 	public void save(FcAccbookHis entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookHis entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccbookHis entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcAccbookHis, String> getEntityDao() {
		return fcAccbookHisDao;
	}
}
