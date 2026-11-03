package com.bytter.biz.inneraccount.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.biz.inneraccount.dao.BisGatherPolicyDao;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
/**
* @Title: Service
* @Description: 调拨策略表
* @author onlineGenerator
* @date 2017-06-16 14:38:52
* @version 1.0
*
*/
@Service("bisGatherPolicyService")
@Transactional
public class BisGatherPolicyService extends BaseService<BisGatherPolicy,String> {
	@Autowired
	private BisGatherPolicyDao bisGatherPolicyDao;
	@Autowired
	private BisAccGatherPolicyService bisAccGatherPolicyService;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(BisGatherPolicy entity){
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
 	public void save(BisGatherPolicy entity, ICurrentUser user){
 		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BisGatherPolicy entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BisGatherPolicy entity, ICurrentUser user){
 		entity.setUpdateBy(user.getLoginName());
 		entity.setUpdateDate(DateUtils.getDate());
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<BisGatherPolicy, String> getEntityDao() {
		return bisGatherPolicyDao;
	}
	
	/**
	 * 根据策略代码获取策略信息
	 * @param gatherPolicyCode
	 * @return
	 */
	public BisGatherPolicy findByCode(String gatherPolicyCode){
		return bisGatherPolicyDao.getUniqueBy("gatherPolicyCode", gatherPolicyCode);
	}
	
	/**
	 * 根据调拨类型获取策略信息
	 * @param gatherType
	 * @return
	 */
	public List<BisGatherPolicy> listByGatherType(String gatherType){
		return bisGatherPolicyDao.listBy("gatherType", gatherType);
	}
	/**
	 * 批量更新有效标志
	 * @param ids
	 * @param validSign
	 */
	public void batchUpdateValidSign(String ids, String validSign){
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			BisGatherPolicy entity = this.get(id);
			entity.setValidSign(validSign);
			this.saveOrUpdate(entity);
		}
	}
	public void updateBisGatherPolicy(BisGatherPolicy bisGatherPolicy) {
		BisGatherPolicy t = this.get(bisGatherPolicy.getId(), true);
		//调整过策略生效日期，则更新账号策略的next_time为空
		boolean updateNextTime = false;
		if (!bisGatherPolicy.getDefDate().equals(t.getDefDate())) {
			updateNextTime = true;
		}
		BeanUtils.copyNotNullProperties(bisGatherPolicy, t);
		String launchTime = DateUtils.formatDate(t.getDefDate(),"HH:mm:ss").replace(":", "");
		t.setLaunchTime(launchTime);
		String endTime = DateUtils.formatDate(t.getEndDate(),"HH:mm:ss").replace(":", "");
		t.setEndTime(endTime);
		this.saveOrUpdate(t);
		if (updateNextTime) {
			bisAccGatherPolicyService.updateNextTimeByPolicyId(bisGatherPolicy.getId());
		}
	}
}
