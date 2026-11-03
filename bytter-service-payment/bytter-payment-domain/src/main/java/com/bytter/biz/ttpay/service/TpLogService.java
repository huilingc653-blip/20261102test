package com.bytter.biz.ttpay.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.ttpay.dao.TpLogDao;
import com.bytter.biz.ttpay.entity.TpLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
/**
 * @Title: Service
 * @Description: 第三方支付日志Service
 * @author zhengg
 * @date 2019-09-24 15:43:54
 * @version 1.0
 *
 */
@Service("tpLogService")
@Transactional
public class TpLogService extends BaseService<TpLog,String> {
	@Autowired
	private TpLogDao tpLogDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(TpLog entity){
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
 	public void save(TpLog entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(TpLog entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(TpLog entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<TpLog, String> getEntityDao() {
		return tpLogDao;
	}
	/**
	 * 写入日志信息表
	 * @param info
	 * @return
	 */
	public boolean saveTpLog(TpLog info){
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
	public boolean saveTpLog(String quoteId,String logType,String msg,ICurrentUser userVo){
		return saveTpLog(quoteId, logType, msg, userVo.getLoginName(), userVo.getUsername());
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
	public boolean saveTpLog(String quoteId,String logType,String msg,String userCode,String userName){
		TpLog info=new TpLog();
		info.setQuoteId(quoteId);
		info.setLogType(logType);
		info.setMsg(msg);
		info.setCreateBy(userCode);
		info.setCreateName(userName);
		return saveTpLog(info);
	}
	/**
	 * 写入日志信息表
	 * @param quoteIds
	 * @param logType
	 * @param msgs
	 * @return
	 */
	public boolean batchSaveTpLog(String[] quoteIds,String logType,String[] msgs,ICurrentUser userVo){
		return batchSaveTpLog(quoteIds, logType, msgs, userVo.getLoginName(), userVo.getUsername());
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
	public boolean batchSaveTpLog(String[] quoteIds,String logType,String[] msgs,String userCode,String userName){
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
				boolean reBoo=saveTpLog(quoteId, logType, msg, userCode, userName);
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
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveTpLog(String[] quoteIds,String logType,String msg,ICurrentUser userVo){
		return batchSaveTpLog(quoteIds, logType, msg, userVo.getLoginName(), userVo.getUsername());
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
	public boolean batchSaveTpLog(String[] quoteIds,String logType,String msg,String userCode,String userName){
		String[] msgs={msg};
		return batchSaveTpLog(quoteIds, logType, msgs, userCode, userName);
	}
	/**
	 * 查询满足quoteIds的日志记录数据
	 * @param quoteIds
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TpLog> listTpLog(String[] quoteIds){
		if(quoteIds!=null&&quoteIds.length>0){
			List<TpLog> logList = tpLogDao.listTpLog(quoteIds);
			if(logList!=null&&!logList.isEmpty()){
				// this.//inject(logList);
			}
			return logList;
		}
		return new ArrayList<TpLog>();
	}
	/**
	 * 查询quoteId的日志记录数据
	 * @param quoteId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TpLog> listTpLog(String quoteId){
		if(!StringUtils.isBlank(quoteId)){
			String[] quoteIds={quoteId};
			return this.listTpLog(quoteIds);
		}
		return new ArrayList<TpLog>();
	}
}
