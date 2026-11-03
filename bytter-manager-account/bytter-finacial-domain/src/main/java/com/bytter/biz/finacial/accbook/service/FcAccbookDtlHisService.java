package com.bytter.biz.finacial.accbook.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDtlHisDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtlHis;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_ACCBOOK_DTL_HIS
* @author onlineGenerator
* @date 2017-06-15 18:17:12
* @version 1.0
*
*/
@Service("fcAccbookDtlHisService")
@Transactional
public class FcAccbookDtlHisService extends BaseService<FcAccbookDtlHis,String> {
	@Autowired
	private FcAccbookDtlHisDao fcAccbookDtlHisDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccbookDtlHis entity){
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
 	public void save(FcAccbookDtlHis entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookDtlHis entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccbookDtlHis entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcAccbookDtlHis, String> getEntityDao() {
		return fcAccbookDtlHisDao;
	}
}
