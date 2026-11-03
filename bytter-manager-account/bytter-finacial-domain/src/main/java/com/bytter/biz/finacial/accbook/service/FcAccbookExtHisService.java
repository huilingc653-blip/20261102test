package com.bytter.biz.finacial.accbook.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.dao.FcAccbookExtHisDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExtHis;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_ACCBOOK_EXT_HIS
* @author onlineGenerator
* @date 2017-06-15 18:17:53
* @version 1.0
*
*/
@Service("fcAccbookExtHisService")
@Transactional
public class FcAccbookExtHisService extends BaseService<FcAccbookExtHis,String> {
	@Autowired
	private FcAccbookExtHisDao fcAccbookExtHisDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccbookExtHis entity){
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
 	public void save(FcAccbookExtHis entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookExtHis entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccbookExtHis entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcAccbookExtHis, String> getEntityDao() {
		return fcAccbookExtHisDao;
	}
}
