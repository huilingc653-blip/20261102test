package com.bytter.biz.fundwarn.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.fundwarn.dao.BtWarningLogDao;
import com.bytter.biz.fundwarn.entity.BtWarningLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
/**
 * @Title: Service
 * @Description: 预警日志业务处理类
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 */
@Service("btWarningLogService")
@Transactional
public class BtWarningLogService extends BaseService<BtWarningLog,String> {
	
	@Autowired
	private BtWarningLogDao btWarningLogDao;
	
    @Override
    public BaseDao<BtWarningLog, String> getEntityDao() {
        return btWarningLogDao;
    }
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(BtWarningLog entity){
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
 	public void save(BtWarningLog entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(BtWarningLog entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(BtWarningLog entity){
 		super.saveOrUpdate(entity);
 	}
	/**
	 * 写入日志信息表
	 * @param info
	 * @return
	 */
	public boolean saveBtWarningLog(BtWarningLog info){
		if(info==null||StringUtils.isAllBlank(info.getQuoteId(),info.getMsg())){
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
	public boolean saveBtWarningLog(String quoteId,String logType,String msg,ICurrentUser userVo){
		if(userVo!=null){
			return saveBtWarningLog(quoteId, logType, msg, userVo.getLoginName(), userVo.getUsername());
		}
		return saveBtWarningLog(quoteId, logType, msg,null,null);
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
	public boolean saveBtWarningLog(String quoteId,String logType,String msg,String userCode,String userName){
		BtWarningLog info=new BtWarningLog();
		info.setQuoteId(quoteId);
		info.setLogType(logType);
		info.setMsg(msg);
		info.setCreateBy(userCode);
		info.setCreateName(userName);
		return saveBtWarningLog(info);
	}
	
	/**
	 * 写入日志信息表
	 * @param quoteIds
	 * @param logType
	 * @param msgs
	 * @return
	 */
	public boolean batchSaveBtWarningLog(String[] quoteIds,String logType,String[] msgs,ICurrentUser userVo){
		return batchSaveBtWarningLog(quoteIds, logType, msgs, userVo.getLoginName(), userVo.getUsername());
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
	public boolean batchSaveBtWarningLog(String[] quoteIds,String logType,String[] msgs,String userCode,String userName){
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
				boolean reBoo=saveBtWarningLog(quoteId, logType, msg, userCode, userName);
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
	public boolean batchSaveBtWarningLog(String[] quoteIds,String logType,String msg,ICurrentUser userVo){
		return batchSaveBtWarningLog(quoteIds, logType, msg, userVo.getLoginName(), userVo.getUsername());
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
	public boolean batchSaveBtWarningLog(String[] quoteIds,String logType,String msg,String userCode,String userName){
		String[] msgs={msg}; 
		return batchSaveBtWarningLog(quoteIds, logType, msgs, userCode, userName);
	}
	
	/**
	 * 查询满足quoteIds的日志记录数据
	 * @param quoteIds
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BtWarningLog> listBtWarningLog(String[] quoteIds){
		if(quoteIds!=null&&quoteIds.length>0){
			List<BtWarningLog> logList = btWarningLogDao.listBtWarningLog(quoteIds);
			if(logList!=null&&!logList.isEmpty()){
				// this.inject(logList);
			}
			return logList;
		}
		return new ArrayList<BtWarningLog>();
	}
	/**
	 * 查询quoteId的日志记录数据
	 * @param quoteId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<BtWarningLog> listBtWarningLog(String quoteId){
		if(!StringUtils.isBlank(quoteId)){
			String[] quoteIds={quoteId};
			return this.listBtWarningLog(quoteIds);
		}
		return new ArrayList<BtWarningLog>();
	}
}
