package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbDiscountDao;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: 票据贴现
* @author onlineGenerator
* @date 2017-06-21 15:43:17
* @version 1.0
*
*/
@Service("abDiscountService")
@Transactional
public class AbDiscountService extends BaseService<AbDiscount,String> {
	@Autowired
	private AbDiscountDao abDiscountDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(AbDiscount entity){
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
 	public void save(AbDiscount entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(AbDiscount entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(AbDiscount entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<AbDiscount, String> getEntityDao() {
		return abDiscountDao;
	}
	
	/**
	 * 根据票据流水ID获取票据贴现信息
	 * @param recordId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbDiscount> listAbDiscountinfoByRecordId(String recordId){
		return abDiscountDao.listAbDiscountinfoByRecordId(recordId);
	}
	
	/**
	 * 根据recordId获取唯一票据贴现信息
	 * @param recordId
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbDiscount getAbDiscountByRecordId(String recordId){
		return abDiscountDao.getAbDiscountByRecordId(recordId); 
	}
	
	/**
	 * 贴现收回审批打回时根据票据流水id删除贴现收回利息
	 * @param recordId
	 * @return
	 */
	public int deleteByRecordId(String recordId){
 		Map<String, Object> params=new HashMap<>();
		params.put("recordId", recordId);
 		return abDiscountDao.deleteByRecordId(params);
 	}
	/**
	 * 根据传递流水ID获取贴现明细id
	 * @param recordId  流水ID
	 * @return 贴现明细id
	 */
	public String getDiscountIdByRecordId(String recordId){
		return abDiscountDao.getDiscountIdByRecordId(recordId);
	}
	/**
	 *打回时候把当前贴现明细表的流水id修改为之前的流水id
	 * @param id 明细id
	 * @param recordId 流水id
	 * @return 返回修改成功数量
	 */
	public int updateDiscountRecordIdById(String id,String recordId){
		if (StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(recordId)) {
			return abDiscountDao.updateDiscountRecordIdById(id, recordId);
		} else {
			return 0;
		}
	}

	public AbDiscount getById(String id){
		return abDiscountDao.get(id);
	}
}
