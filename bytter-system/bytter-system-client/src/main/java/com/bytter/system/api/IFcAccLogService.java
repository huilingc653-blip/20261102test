package com.bytter.system.api;
import java.util.List;
import com.bytter.system.api.dto.FcAccLogDTO;
import com.bytter.system.api.vo.FcAccLogVO;
/**
 * 内部账号日志
 */
public interface IFcAccLogService  {
	/**
	* 删除对象
	* @param entity
	*/
	
	void deleteEntity(FcAccLogDTO entity);
	/**
	* 根据id删除
	* @param id
	*/
 	
	void delete(String id);
	/**
	* 保存
	* @param entity
	*/
 	void save(FcAccLogDTO entity);
	/**
	* 更新
	* @param entity
	*/
	void update(FcAccLogDTO entity);
	/**
	* 保存or更新
	* @param entity
	*/
 	
	void saveOrUpdate(FcAccLogDTO entity);
	/**
	 * 根据业务表单据Id查询日志信息
	 * @param bizId 业务表单据Id
	 * @return
	 */
	List<FcAccLogDTO> listByBizId(String bizId);
	/**
	 * 查询日志信息
	 * @param bizId 业务表单据Id
	 * @param account 账号
	 * @param bizType 10开户，11改户，12初始化，13销户，14冻结，15解冻
	 * @return
	 */
	List<FcAccLogVO> listByParams(String bizId, String account, String bizType);
	void saveLog(FcAccLogDTO entity, String loginName);
}
