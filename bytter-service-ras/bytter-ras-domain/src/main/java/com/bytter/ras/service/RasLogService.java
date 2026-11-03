package com.bytter.ras.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.bytter.ras.dao.RasLogDao;
import com.bytter.ras.entity.RasLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;



/**
 * @Title: Service
 * @Description: 营收稽核日志
 * @author onlineGenerator
 * @date 2019-04-29 15:43:05
 * @version 1.0
 *
 */
@Service("rasLogService")
@Transactional
public class RasLogService extends BaseService<RasLog, String> {

	@Autowired
	private RasLogDao rasLogDao;

	/**
	 * 删除对象
	 * 
	 * @param entity
	 */
	public void deleteEntity(RasLog entity) {
		super.deleteEntity(entity);
	}

	/**
	 * 根据id删除
	 * 
	 * @param id
	 */
	public void delete(String id) {
		super.delete(id);
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 */
	public void save(RasLog entity) {
		super.saveEntity(entity);
	}

	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(RasLog entity) {
		super.updateEntity(entity);
	}

	/**
	 * 保存or更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(RasLog entity) {
		super.saveOrUpdate(entity);
	}

	public BaseDao<RasLog, String> getEntityDao() {
		return rasLogDao;
	}

	/**
	 * 写入日志信息表
	 * 
	 * @param info
	 * @return
	 */
	public boolean saveRasLog(RasLog info) {
		if (info == null || StringUtils.isAllBlank(info.getQuoteId(), info.getMsg())) {
			return false;
		}
		info.setCreateDate(new Date());
		this.save(info);
		return true;
	}



	/**
	 * 写入日志信息表
	 * 
	 * @param quoteId
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @param briefId
	 *            业务类型
	 * @param logType
	 *            日志类型
	 * @param msg
	 *            日志信息
	 * @return
	 */
	public boolean saveRasLog(String quoteId, String logType, String msg,
			ICurrentUser userVo) {
		if (userVo != null) {
			return saveRasLog(quoteId, logType, msg, userVo.getLoginName(), userVo.getUsername());
		} else {
			return saveRasLog(quoteId, logType, msg, null, null);
		}
	}

	/**
	 * 写入日志信息表
	 * 
	 * @param quoteId
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @param logType
	 *            日志类型
	 * @param msg
	 *            日志信息
	 * @param userCode
	 *            操作人代码
	 * @param userName
	 *            操作人名称
	 * @return
	 */
	public boolean saveRasLog(String quoteId, String logType, String msg,
			String userCode, String userName) {
		RasLog info = new RasLog();
		info.setQuoteId(quoteId);
		info.setLogType(logType);
		info.setMsg(msg);
		info.setCreateBy(userCode);
		info.setCreateName(userName);
		return saveRasLog(info);
	}

	/**
	 * 批量写入日志信息表
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @param logType
	 * @param msgs
	 * @return
	 */
	public boolean batchSaveRasLog(String[] quoteIds, String logType, String[] msgs,
			ICurrentUser userVo) {
		if (userVo != null) {
			return batchSaveRasLog(quoteIds, logType, msgs, userVo.getLoginName(),
					userVo.getUsername());
		} else {
			return batchSaveRasLog(quoteIds, logType, msgs, null, null);
		}
	}

	/**
	 * 写入日志信息表
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @param briefId
	 *            业务类型代码
	 * @param logType
	 * @param msgs
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveRasLog(String[] quoteIds, String logType, String[] msgs,
			String userCode, String userName) {
		if (quoteIds != null && msgs != null && quoteIds.length > 0 && msgs.length > 0) {
			String quoteId = null;
			String msg = null;
			for (int i = 0; i < quoteIds.length; i++) {
				quoteId = quoteIds[i];
				if (msgs.length < quoteIds.length) {
					msg = msgs[0];
				} else {
					msg = msgs[i];
				}
				boolean reBoo = saveRasLog(quoteId, logType, msg, userCode, userName);
				if (!reBoo) {
					return reBoo;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 写入日志信息表
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @param logType
	 * @param msg
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveRasLog(String[] quoteIds, String logType, String msg,
			ICurrentUser userVo) {
		if (userVo != null) {
			return batchSaveRasLog(quoteIds, logType, msg, userVo.getLoginName(),
					userVo.getUsername());
		} else {
			return batchSaveRasLog(quoteIds, logType, msg, null, null);
		}
	}

	/**
	 * 写入日志信息表
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @param logType
	 * @param msg
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveRasLog(String[] quoteIds, String logType, String msg,
			String userCode, String userName) {
		String[] msgs = { msg };
		return batchSaveRasLog(quoteIds, logType, msgs, userCode, userName);
	}

	/**
	 * 查询满足quoteIds的日志记录数据
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为营收稽核从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<RasLog> listRasLogByQuoteId(String[] quoteIds) {
		return listRasLog(quoteIds, null);
	}

	/**
	 * 查询满足ids的日志记录数据
	 * 
	 * @param ids
	 *            外部引用ID或者外部主引用ID 只要一个满足就能查询处理
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<RasLog> listRasLogByIds(String[] ids) {
		return listRasLog(null, ids);
	}

	/**
	 * 查询满足quoteIds的日志记录数据
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @param ids
	 *            外部引用ID或者外部主引用ID 只要一个满足就能查询处理
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<RasLog> listRasLog(String[] quoteIds, String[] ids) {
		if (StringUtils.isNotEmpty(quoteIds) || StringUtils.isNotEmpty(ids)) {
			List<RasLog> logList = rasLogDao.listRasLog(quoteIds, ids);
			if (logList != null && !logList.isEmpty()) {
				//this.inject(logList);
			}
			return logList;
		}
		return new ArrayList<RasLog>();
	}

	/**
	 * 查询quoteId的日志记录数据
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<RasLog> listRasLog(String quoteId) {
		String[] quoteIds = null;
		if (StringUtils.isNotEmpty(quoteId)) {
			quoteIds = new String[] { quoteId };
		}
		return this.listRasLogByQuoteId(quoteIds);
	}

	/**
	 * 查询quoteId的日志记录数据
	 * 
	 * @param quoteIds
	 *            外部引用ID 一般为营收稽核流水表ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public void initLogViewInfo(String ids, Model model) {
		if (StringUtils.isNotEmpty(ids)) {
			String[] idsArr = StringUtils.split(ids, ",");
			List<RasLog> logList = listRasLogByQuoteId(idsArr);
			model.addAttribute("logList", logList);
		}
	}

}