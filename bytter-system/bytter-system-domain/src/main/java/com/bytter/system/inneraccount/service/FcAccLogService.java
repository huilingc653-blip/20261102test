package com.bytter.system.inneraccount.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.vo.FcAccLogVO;
import com.bytter.system.inneraccount.dao.FcAccLogDao;
import com.bytter.system.inneraccount.entity.FcAccLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;
/**
 * 内部账号日志
 */
@Service("fcAccLogService")
@Transactional
public class FcAccLogService extends BaseService<FcAccLog,String> {
	@Autowired
	private FcAccLogDao fcAccLogDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcAccLog entity){
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
 	public void save(FcAccLog entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccLog entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FcAccLog entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FcAccLog, String> getEntityDao() {
		return fcAccLogDao;
	}
	/**
	 * 根据业务表单据Id查询日志信息
	 * @param bizId 业务表单据Id
	 * @return
	 */
	public List<FcAccLog> listByBizId(String bizId) {
		return fcAccLogDao.listByBizId(bizId);
	}
	/**
	 * 查询日志信息
	 * @param bizId 业务表单据Id
	 * @param account 账号
	 * @param bizType 10开户，11改户，12初始化，13销户，14冻结，15解冻
	 * @return
	 */
	public List<FcAccLogVO> listByParams(String bizId, String account, String bizType) {
		return fcAccLogDao.listByParams(bizId,account,bizType);
	}
	public void saveLog(FcAccLog entity,String loginName){
		if(entity != null){
			entity.setOpCode(loginName);
			entity.setOpTime(new Timestamp(System.currentTimeMillis()));
			this.save(entity);
		}
	}
}