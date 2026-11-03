package com.bytter.bill.domain.bill.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbLogDao;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import java.util.*;
/**
 * @Title: Service
 * @Description: 票据日志
 * @author onlineGenerator
 * @date 2017-06-21 15:43:05
 * @version 1.0
 *
 */
@Service("abLogService")
@Transactional
public class AbLogService extends BaseService<AbLog, String> {
	@Autowired
	private AbLogDao abLogDao;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	/**
	 * 删除对象
	 * 
	 * @param entity
	 */
	public void deleteEntity(AbLog entity) {
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
	public void save(AbLog entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void update(AbLog entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * 
	 * @param entity
	 */
	public void saveOrUpdate(AbLog entity) {
		super.saveOrUpdate(entity);
	}
	public BaseDao<AbLog, String> getEntityDao() {
		return abLogDao;
	}
	/**
	 * 写入日志信息表
	 * 
	 * @param info
	 * @return
	 */
	public boolean saveAbLog(AbLog info) {
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
	 * @param quotezId
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteId
	 *            外部引用ID 一般为票据流水表ID
	 * @param briefId
	 *            业务类型
	 * @param logType
	 *            日志类型
	 * @param msg
	 *            日志信息
	 * @return
	 */
	public boolean saveAbLog(String quotezId, String quoteId, String briefId, String logType, String msg,
			ICurrentUser userVo) {
		if (userVo != null) {
			return saveAbLog(quotezId, quoteId, briefId, logType, msg, userVo.getLoginName(), userVo.getUsername());
		} else {
			return saveAbLog(quotezId, quoteId, briefId, logType, msg, null, null);
		}
	}
	/**
	 * 写入日志信息表
	 * 
	 * @param quotezId
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteId
	 *            外部引用ID 一般为票据流水表ID
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
	public boolean saveAbLog(String quotezId, String quoteId, String briefId, String logType, String msg,
			String userCode, String userName) {
		AbLog info = new AbLog();
		info.setQuotezId(quotezId);
		info.setQuoteId(quoteId);
		info.setBriefId(briefId);
		info.setLogType(logType);
		info.setMsg(msg);
		info.setCreateBy(userCode);
		info.setCreateName(userName);
		return saveAbLog(info);
	}
	/**
	 * 批量写入日志信息表
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @param logType
	 * @param msgs
	 * @return
	 */
	public boolean batchSaveAbLog(String[] quotezIds, String[] quoteIds, String briefId, String logType, String[] msgs,
			ICurrentUser userVo) {
		if (userVo != null) {
			return batchSaveAbLog(quotezIds, quoteIds, briefId, logType, msgs, userVo.getLoginName(),
					userVo.getUsername());
		} else {
			return batchSaveAbLog(quotezIds, quoteIds, briefId, logType, msgs, null, null);
		}
	}
	/**
	 * 写入日志信息表
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @param briefId
	 *            业务类型代码
	 * @param logType
	 * @param msgs
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveAbLog(String[] quotezIds, String[] quoteIds, String briefId, String logType, String[] msgs,
			String userCode, String userName) {
		if (quoteIds != null && msgs != null && quoteIds.length > 0 && msgs.length > 0) {
			String quotezId = null;
			String quoteId = null;
			String msg = null;
			for (int i = 0; i < quoteIds.length; i++) {
				quoteId = quoteIds[i];
				if(quotezIds!=null && i<quotezIds.length){
					quotezId = quotezIds[i];
				}
				if (msgs.length < quoteIds.length) {
					msg = msgs[0];
				} else {
					msg = msgs[i];
				}
				boolean reBoo = saveAbLog(quotezId, quoteId, briefId, logType, msg, userCode, userName);
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
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @param logType
	 * @param msg
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveAbLog(String[] quotezIds, String[] quoteIds, String briefId, String logType, String msg,
			ICurrentUser userVo) {
		if (userVo != null) {
			return batchSaveAbLog(quotezIds, quoteIds, briefId, logType, msg, userVo.getLoginName(),
					userVo.getUsername());
		} else {
			return batchSaveAbLog(quotezIds, quoteIds, briefId, logType, msg, null, null);
		}
	}
	/**
	 * 写入日志信息表
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @param logType
	 * @param msg
	 * @param userCode
	 * @param userName
	 * @return
	 */
	public boolean batchSaveAbLog(String[] quotezIds, String[] quoteIds, String briefId, String logType, String msg,
			String userCode, String userName) {
		String[] msgs = { msg };
		return batchSaveAbLog(quotezIds, quoteIds, briefId, logType, msgs, userCode, userName);
	}
	/**
	 * 查询满足quoteIds的日志记录数据
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbLog> listAbLog(String[] quotezIds, String[] quoteIds) {
		return listAbLog(quotezIds, quoteIds, null);
	}
	/**
	 * 查询满足ids的日志记录数据
	 * 
	 * @param ids
	 *            外部引用ID或者外部主引用ID 只要一个满足就能查询处理
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbLog> listAbLog(String[] ids) {
		return listAbLog(null, null, ids);
	}
	/**
	 * 查询满足quoteIds的日志记录数据
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @param ids
	 *            外部引用ID或者外部主引用ID 只要一个满足就能查询处理
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbLog> listAbLog(String[] quotezIds, String[] quoteIds, String[] ids) {
		if (StringUtils.isNotEmpty(quotezIds) || StringUtils.isNotEmpty(quoteIds) || StringUtils.isNotEmpty(ids)) {
			List<AbLog> logList = abLogDao.listAbLog(quotezIds, quoteIds, ids);
			if (logList != null && !logList.isEmpty()) {
				List<AbLog> logListTask = new ArrayList<AbLog>();
				Map<String, String> aidMap = new HashMap<>();
				for(AbLog log:logList){
					if (aidMap.containsKey(log.getQuotezId())) {
						continue;
					} else {
						aidMap.put(log.getQuotezId(), log.getQuotezId());
					}
					//根据从表id查询所有流水，根据流水获取所有审批数据
					List<AbAccrecordsVO> accList = abAccrecordsService.listAbAccrecordsIdByVvId(log.getQuotezId());
					if(!CollectionUtils.isEmpty(accList)){
						for(AbAccrecordsVO acc: accList){
							List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(acc.getId());
							if(!EmptyUtils.isEmptyList(taskResponseDTOS)){
								for(int i = 0; i < taskResponseDTOS.size(); i++) {
									TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
									AbLog abLog = new AbLog();
									String messageType = AccebillConstant.ABLOG_LOGTYPE_APP;
									String messageTypeName = "审批通过";
									if(!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
											&& !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())){
										continue;
									}
									//审批打回
									if("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())){
										messageType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
										messageTypeName = "审批打回";
									}else if("2".equals(responseDTO.getTaskPassFlag())){
										//审批撤回票据暂未实现
										//messageType = NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE;
									}
									abLog.setBriefId(acc.getBriefId());
									abLog.setBriefIdName(acc.getBriefIdName());
									abLog.setQuoteId(acc.getId());
									abLog.setQuotezId(acc.getVvid());
									abLog.setLogType(messageType);
									abLog.setMsg(messageTypeName);
									abLog.setLogTypeName(messageTypeName);
									abLog.setCreateDate(responseDTO.getEndTime());
									UserDTO user = userService.getByUserId(responseDTO.getUserId());
									if (user != null) {
										abLog.setCreateBy(user.getLoginName());
										abLog.setCreateName(responseDTO.getUserName());
									}
									logListTask.add(abLog);
								}
							}
						}
					}
				}
				logList.addAll(logListTask);
				logList.sort(Comparator.comparing(AbLog::getCreateDate));
				// todo:冗余数据
				if (ObjectUtil.isNotEmpty(logList)){
					Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BRIEF_STATUS", "AB_LOG_LOGTYPE");
					for (AbLog log : logList){
						injectValues(log,listDictMap);
					}
				}
			}
			return logList;
		}
		return new ArrayList<AbLog>();
	}
	// 冗余操作日志数据
	private void injectValues(AbLog log, Map<String, Map<String, DictDTO>> listDictMap) {
		Optional.ofNullable(log.getBriefId()).ifPresent(briefId -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
					.ifPresent(dict -> log.setBriefIdName(dict.getName()));
		});
		Optional.ofNullable(log.getLogType()).ifPresent(logType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_LOG_LOGTYPE");
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(logType)))
					.ifPresent(dict -> log.setLogTypeName(dict.getName()));
		});
	}
	/**
	 * 查询quoteId的日志记录数据
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<AbLog> listAbLog(String quotezId, String quoteId) {
		String[] quotezIds = null;
		String[] quoteIds = null;
		if (StringUtils.isNotEmpty(quotezId)) {
			quotezIds = new String[] { quotezId };
		}
		if (StringUtils.isNotEmpty(quoteId)) {
			quoteIds = new String[] { quoteId };
		}
		return this.listAbLog(quotezIds, quoteIds);
	}
	/**
	 * 查询quoteId的日志记录数据
	 * 
	 * @param quotezIds
	 *            外部主引用ID 一般为票据从表ID
	 * @param quoteIds
	 *            外部引用ID 一般为票据流水表ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public void initLogViewInfo(String ids, Model model) {
		if (StringUtils.isNotEmpty(ids)) {
			String[] idsArr = StringUtils.split(ids, ",");
			List<AbLog> logList = listAbLog(idsArr);
			model.addAttribute("logList", logList);
		}
	}

    @Transactional(readOnly = true)
    public void initLogViewInfo(String ids, Map<String,Object> model) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idsArr = StringUtils.split(ids, ",");
            List<AbLog> logList = listAbLog(idsArr);
            model.put("logList", logList);
        }
    }
}
