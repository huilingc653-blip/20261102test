package com.bytter.biz.finacial.outaccbook.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.outaccbook.dao.FcPreOutAccbookDao;
import com.bytter.biz.finacial.outaccbook.entity.FcPreOutAccbook;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_PRE_OUT_ACCBOOK
* @author onlineGenerator
* @date 2017-07-07 16:05:10
* @version 1.0
*
*/
@Service("fcPreOutAccbookService")
@Transactional
public class FcPreOutAccbookService extends BaseService<FcPreOutAccbook,String> {
	@Autowired
	private FcPreOutAccbookDao fcPreOutAccbookDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcPreOutAccbook entity){
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
 	public void save(FcPreOutAccbook entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcPreOutAccbook entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcPreOutAccbook entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcPreOutAccbook, String> getEntityDao() {
		return fcPreOutAccbookDao;
	}
}
