package com.bytter.biz.finacial.outaccbook.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.outaccbook.dao.FcOutAccbookDtlHisDao;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtlHis;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FC_OUT_ACCBOOK_DTL_HIS
* @author onlineGenerator
* @date 2017-07-07 16:10:22
* @version 1.0
*
*/
@Service("fcOutAccbookDtlHisService")
@Transactional
public class FcOutAccbookDtlHisService extends BaseService<FcOutAccbookDtlHis,String> {
	@Autowired
	private FcOutAccbookDtlHisDao fcOutAccbookDtlHisDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcOutAccbookDtlHis entity){
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
 	public void save(FcOutAccbookDtlHis entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcOutAccbookDtlHis entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcOutAccbookDtlHis entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcOutAccbookDtlHis, String> getEntityDao() {
		return fcOutAccbookDtlHisDao;
	}
}
