package com.bytter.check.service;
import com.bytter.check.dao.FcBankCheckLogDao;
import com.bytter.check.entity.FcBankCheckLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
* @Title: Service
* @Description: FC_BANK_CHECK_LOG
* @author onlineGenerator
* @date 2017-09-29 14:33:58
* @version 1.0
*
*/
@Service("fcBankCheckLogService")
@Transactional
public class FcBankCheckLogService extends BaseService<FcBankCheckLog,String> {
	@Autowired
	private FcBankCheckLogDao fcBankCheckLogDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcBankCheckLog entity){
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
 	public void save(FcBankCheckLog entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcBankCheckLog entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcBankCheckLog entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcBankCheckLog, String> getEntityDao() {
		return fcBankCheckLogDao;
	}
	
	/**
	 * <b>功能介绍：</b>反结转更新对账日志<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adId
	 */
	public void updateRedoCheckLog(String[] adId){
		fcBankCheckLogDao.updateRedoCheckLog(adId);
	}
	
	/**
	 * <b>功能介绍：</b>根据billId获取日志<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param billId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FcBankCheckLog> getCheckLogByBillId(String billId){
		return fcBankCheckLogDao.getCheckLogByBillId(billId);
	}
}
