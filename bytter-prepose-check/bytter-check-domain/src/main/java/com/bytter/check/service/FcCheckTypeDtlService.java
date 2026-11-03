package com.bytter.check.service;

import com.bytter.check.dao.FcCheckTypeDtlDao;
import com.bytter.check.entity.FcCheckTypeDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @Title: Service
* @Description: FC_CHECK_TYPE_DTL
* @author onlineGenerator
* @date 2018-01-03 14:15:14
* @version 1.0
*
*/
@Service("fcCheckTypeDtlService")
@Transactional
public class FcCheckTypeDtlService extends BaseService<FcCheckTypeDtl,String> {
	@Autowired
	private FcCheckTypeDtlDao fcCheckTypeDtlDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcCheckTypeDtl entity){
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
 	public void save(FcCheckTypeDtl entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCheckTypeDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcCheckTypeDtl entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcCheckTypeDtl, String> getEntityDao() {
		return fcCheckTypeDtlDao;
	}
	
	/**
	 * <b>功能介绍：</b>批量增加核对明细<br>
	 * <b>日期：</b>2018年1月3日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param checkTypeId
	 * @param checkTypes
	 */
	public void batchSaveCheckTypeDtl(String checkTypeId,String checkTypes){
		String[] checkType = checkTypes.replace("all,", "").split(",");
		List<FcCheckTypeDtl> dtls = new ArrayList<FcCheckTypeDtl>();
		for (String str : checkType) {
			FcCheckTypeDtl dtl = new FcCheckTypeDtl();
			dtl.setCheckTypeId(checkTypeId);
			dtl.setCheckType(str);
			dtls.add(dtl);
		}
		this.batchSave(dtls);
	}
	
}
