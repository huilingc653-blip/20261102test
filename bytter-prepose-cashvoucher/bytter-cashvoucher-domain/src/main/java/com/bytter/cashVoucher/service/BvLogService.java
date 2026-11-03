package com.bytter.cashVoucher.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.cashVoucher.dao.BvLogDao;
import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
* @Title: Service
* @Description: 空白凭证日志
* @author onlineGenerator
* @date 2017-05-31 15:43:54
* @version 1.0
*
*/
@Service("bvLogService")
@Transactional
public class BvLogService extends BaseService<BvLog,String> {
	
	@Autowired
	private BvLogDao bvLogDao;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BvLog entity){
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
 	public void save(BvLog entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BvLog entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BvLog entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<BvLog, String> getEntityDao() {
		return bvLogDao;
	}
	
	/**
	 * 写入日志信息表
	 * @param info
	 * @return
	 */
	public boolean saveBvLog(BvLog info){
		if(info==null|| StringUtils.isAllBlank(info.getQuoteId(),info.getMsg())){
			return false;
		}
		info.setCreateDate(new Date());
		this.save(info);
		return true;
	}
	
	/**
	 * 写入日志信息表
	 * @param quoteId 外部引用ID
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @return
	 */
	public boolean saveBvLog(String quoteId,String logType,String msg,ICurrentUser userVo){
		return saveBvLog(quoteId, logType, msg, userVo.getLoginName(), userVo.getUsername());
	}
	                                                                                       
	/**
	 * 写入日志信息表
	 * @param quoteId 外部引用ID
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userCode 操作人代码
	 * @param userName 操作人名称
	 * @return
	 */
	public boolean saveBvLog(String quoteId,String logType,String msg,String userCode,String userName){
		BvLog info=new BvLog();
		info.setQuoteId(quoteId);
		info.setLogType(logType);
		info.setMsg(msg);
		info.setCreateBy(userCode);
		info.setCreateName(userName);
		return saveBvLog(info);
	}
	
	/**
	 * 写入日志信息表
	 * @param quoteIds
	 * @param logType
	 * @param msgs
	 * @return
	 */
	public boolean batchSaveBvLog(String[] quoteIds,String logType,String[] msgs,ICurrentUser userVo){
		return batchSaveBvLog(quoteIds, logType, msgs, userVo.getLoginName(), userVo.getUsername());
	}
	
	/**
	 * 写入日志信息表
	 * @param quoteIds
	 * @param logType
	 * @param msgs
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveBvLog(String[] quoteIds,String logType,String[] msgs,String userCode,String userName){
		if(quoteIds!=null&&msgs!=null
				&&quoteIds.length>0&&msgs.length>0){
			String quoteId=null;
			String msg=null;
			for(int i=0;i<quoteIds.length;i++){
				quoteId=quoteIds[i];
				if(msgs.length<quoteIds.length){
					msg=msgs[0];
				}else{
					msg=msgs[i];
				}
				boolean reBoo=saveBvLog(quoteId, logType, msg, userCode, userName);
				if(!reBoo){
					return reBoo;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 写入日志信息表
	 * @param quoteIds
	 * @param logType
	 * @param msg
	 * @return
	 */
	public boolean batchSaveBvLog(String[] quoteIds,String logType,String msg,ICurrentUser userVo){
		return batchSaveBvLog(quoteIds, logType, msg, userVo.getLoginName(), userVo.getUsername());
	}
	
	/**
	 * 写入日志信息表
	 * @param quoteIds
	 * @param logType
	 * @param msg
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveBvLog(String[] quoteIds,String logType,String msg,String userCode,String userName){
		String[] msgs={msg}; 
		return batchSaveBvLog(quoteIds, logType, msgs, userCode, userName);
	}
	
	/**
	 * 查询满足quoteIds的日志记录数据
	 * @param quoteIds
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvLog> listBvLog(String[] quoteIds){
		if(quoteIds!=null&&quoteIds.length>0){
			List<BvLog> logList = bvLogDao.listBvLog(quoteIds);
			if(logList!=null&&!logList.isEmpty()){
			}
			return logList;
		}
		return new ArrayList<BvLog>();
	}
	/**
	 * 查询quoteId的日志记录数据
	 * @param quoteId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BvLog> listBvLog(String quoteId){
		if(!StringUtils.isBlank(quoteId)){
			String[] quoteIds={quoteId};
			// BVLOG_LOGTYPE 从字典表获取映射信息
			List<BvLog> logList = this.listBvLog(quoteIds);
			if (ObjectUtil.isNotEmpty(logList)){
				Map<String, DictDTO> listMap = dictService.listMap("BVLOG_LOGTYPE");
				if (ObjectUtil.isNotEmpty(listMap)){
					logList.forEach(log->{
						if (ObjectUtil.isNotEmpty(log.getLogType())){
							Optional.ofNullable(listMap.get(log.getLogType())).ifPresent(dict->log.setLogTypeName(dict.getName()));
						}
					});
				}
			}
			return logList;
		}
		return new ArrayList<BvLog>();
	}
	
	
}
