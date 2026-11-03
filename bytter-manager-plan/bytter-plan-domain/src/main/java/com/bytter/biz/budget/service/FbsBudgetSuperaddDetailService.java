package com.bytter.biz.budget.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.budget.dao.FbsBudgetSuperaddDetailDao;
import com.bytter.biz.budget.entity.FbsBudgetSuperaddDetail;
import com.bytter.biz.budget.vo.FbsBudgetSuperaddDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: FBS_BUDGET_SUPERADD_DETAIL
* @author onlineGenerator
* @date 2017-07-28 11:36:59
* @version 1.0
*
*/
@Service("fbsBudgetSuperaddDetailService")
@Transactional
public class FbsBudgetSuperaddDetailService extends BaseService<FbsBudgetSuperaddDetail,String> {
	@Autowired
	private FbsBudgetSuperaddDetailDao fbsBudgetSuperaddDetailDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FbsBudgetSuperaddDetail entity){
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
 	public void save(FbsBudgetSuperaddDetail entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FbsBudgetSuperaddDetail entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FbsBudgetSuperaddDetail entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FbsBudgetSuperaddDetail, String> getEntityDao() {
		return fbsBudgetSuperaddDetailDao;
	}
	
	/**
	 * 根据追加Id获取明细列表
	 * @param superaddId
	 * @return
	 */
	public List<FbsBudgetSuperaddDetail> listBySuperaddId(String superaddId) {
		return fbsBudgetSuperaddDetailDao.listBySuperaddId(superaddId);
	}
	
	/**
	 * 根据追加Id获取明细列表
	 * @param superaddId
	 * @return
	 */
	public List<FbsBudgetSuperaddDetailVO> listVOBySuperaddId(String superaddId) {
		return fbsBudgetSuperaddDetailDao.listVOBySuperaddId(superaddId);
	}
	
	/**
	 * 根据计划追加Id删除明细
	 * @param superaddId
	 */
	public void deleteBySuperaddId(String superaddId) {
		fbsBudgetSuperaddDetailDao.deleteBySuperaddId(superaddId);
	}
	
	/**
	 * 根据计划追加Id删除明细
	 * @param entity
	 * 
	 */
	public void updateSuperMoneyBySuperaddId(FbsBudgetSuperaddDetailVO entity) {
		fbsBudgetSuperaddDetailDao.updateSuperMoneyBySuperaddId(entity);
	}
}
