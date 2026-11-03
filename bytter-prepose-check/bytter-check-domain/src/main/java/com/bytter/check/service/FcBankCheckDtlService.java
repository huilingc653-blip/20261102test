package com.bytter.check.service;
import com.bytter.check.dao.FcBankCheckDtlDao;
import com.bytter.check.entity.FcBankCheckDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
* @Title: Service
* @Description: FC_BANK_CHECK_DTL
* @author onlineGenerator
* @date 2017-09-29 14:32:51
* @version 1.0
*
*/
@Service("fcBankCheckDtlService")
@Transactional
public class FcBankCheckDtlService extends BaseService<FcBankCheckDtl,String> {
	@Autowired
	private FcBankCheckDtlDao fcBankCheckDtlDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcBankCheckDtl entity){
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
 	public void save(FcBankCheckDtl entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcBankCheckDtl entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcBankCheckDtl entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcBankCheckDtl, String> getEntityDao() {
		return fcBankCheckDtlDao;
	}
	
	/**
	 * <b>功能介绍：</b>反结转更新银行对账明细<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adId
	 */
	public void updateBankRedoAdjustOver(String[] adId){
		fcBankCheckDtlDao.updateBankRedoAdjustOver(adId);
	}
	
	/**
	 * <b>功能介绍：</b>反结转更新中心对账明细<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adId
	 */
	public void updateAccRedoAdjustOver(String[] adId){
		fcBankCheckDtlDao.updateAccRedoAdjustOver(adId);
	}
}
