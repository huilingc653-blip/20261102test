package com.bytter.system.api;


import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.dto.BtBankAccMessageDTO;

import java.util.List;

public interface IBtBankAccMessageService {
	
	
	public void save(BtBankAccMessageDTO entity);
	/**
	* 更新
	* @param entity
	*/
	public void update(BtBankAccMessageDTO entity);
	/**
	* 保存or更新
	* @param entity
	*/
	public void saveOrUpdate(BtBankAccMessageDTO entity);

	/**
	 * 删除对象
	 * @param dto
	 */
	public void deleteEntity(BtBankAccMessageDTO dto);
 	
	public void batchSaveEntity(List<BtBankAccMessageDTO> entityList);


	/**
	 * 写入日志信息表
	 * @param msg
	 * @return
	 */
	public boolean saveMessage(BtBankAccMessageDTO msg);
	
	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @return
	 */
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user);
	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @param userCode 操作人代码
	 * @param userName 操作人名称
	 * @return
	 */
	public boolean saveMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName);


	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @return
	 */
	public BtBankAccMessageDTO buildMessage(String billNo, Integer messageType, String message, String busType, ICurrentUser user);


	/**
	 * 写入日志信息表
	 * @param billNo 外部引用ID
	 * @param messageType 日志类型
	 * @param message 日志信息
	 * @param busType 业务类型
	 * @param userCode 操作人代码
	 * @param userName 操作人名称
	 * @return
	 */
	public BtBankAccMessageDTO buildMessage(String billNo, Integer messageType, String message, String busType, String userCode, String userName);

	/**
	 * 日志列表
	 * @param billNo 外部引用ID
	 * @param busType 业务类型
	 * @return
	 */
	public List<BtBankAccMessageDTO> listMessage(String billNo, String busType);

}
