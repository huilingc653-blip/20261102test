package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.dao.ConLogDao;
import com.bytter.guarantee.domain.entity.ConLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Service
 * @Description: 合同管理日志
 * @version 1.0
 *
 */
@Service("ConLogService")
@Transactional
public class ConLogService extends BaseService<ConLog, String> {
    @Autowired
    private ConLogDao ConLogDao;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(ConLog entity) {
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
    public void save(ConLog entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(ConLog entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(ConLog entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<ConLog, String> getEntityDao() {
        return ConLogDao;
    }
    /**
     * 写入日志信息表
     * 
     * @param info
     * @return
     */
    public boolean saveConLog(ConLog info) {
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
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteId
     *            外部引用ID 一般为合同管理流水表ID
     * @param busType
     *            业务类型
     * @param logType
     *            日志类型
     * @param msg
     *            日志信息
     * @return
     */
    public boolean saveConLog(String quotezId, String quoteId, String busType, String logType, String msg,
            ICurrentUser userVo) {
        if (userVo != null) {
            return saveConLog(quotezId, quoteId, busType, logType, msg, userVo.getLoginName(), userVo.getUsername());
        } else {
            return saveConLog(quotezId, quoteId, busType, logType, msg, null, null);
        }
    }
    /**
     * 写入日志信息表
     * 
     * @param quotezId
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteId
     *            外部引用ID 一般为合同管理流水表ID
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
    public boolean saveConLog(String quotezId, String quoteId, String busType, String logType, String msg,
            String userCode, String userName) {
        ConLog info = new ConLog();
        info.setQuotezId(quotezId);
        info.setQuoteId(quoteId);
        info.setBusType(busType);
        info.setLogType(logType);
        info.setMsg(msg);
        info.setCreateBy(userCode);
        info.setCreateName(userName);
        return saveConLog(info);
    }
    /**
     * 批量写入日志信息表
     * 
     * @param quotezIds
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @param logType
     * @param msgs
     * @return
     */
    public boolean batchSaveConLog(String[] quotezIds, String[] quoteIds, String busType, String logType, String[] msgs,
            ICurrentUser userVo) {
        if (userVo != null) {
            return batchSaveConLog(quotezIds, quoteIds, busType, logType, msgs, userVo.getLoginName(),
                    userVo.getUsername());
        } else {
            return batchSaveConLog(quotezIds, quoteIds, busType, logType, msgs, null, null);
        }
    }
    /**
     * 写入日志信息表
     * 
     * @param quotezIds
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @param busType
     *            业务类型代码
     * @param logType
     * @param msgs
     * @param userCode
     * @param userName
     * @return
     */
    public boolean batchSaveConLog(String[] quotezIds, String[] quoteIds, String busType, String logType, String[] msgs,
            String userCode, String userName) {
        if (quoteIds != null && msgs != null && quoteIds.length > 0 && msgs.length > 0) {
            String quotezId = null;
            String quoteId = null;
            String msg = null;
            for (int i = 0; i < quoteIds.length; i++) {
                quoteId = quoteIds[i];
                quotezId = quotezIds[i];
                if (msgs.length < quoteIds.length) {
                    msg = msgs[0];
                } else {
                    msg = msgs[i];
                }
                boolean reBoo = saveConLog(quotezId, quoteId, busType, logType, msg, userCode, userName);
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
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @param logType
     * @param msg
     * @param userCode
     * @param userName
     * @return
     */
    public boolean batchSaveConLog(String[] quotezIds, String[] quoteIds, String busType, String logType, String msg,
            ICurrentUser userVo) {
        if (userVo != null) {
            return batchSaveConLog(quotezIds, quoteIds, busType, logType, msg, userVo.getLoginName(),
                    userVo.getUsername());
        } else {
            return batchSaveConLog(quotezIds, quoteIds, busType, logType, msg, null, null);
        }
    }
    /**
     * 写入日志信息表
     * 
     * @param quotezIds
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @param logType
     * @param msg
     * @param userCode
     * @param userName
     * @return
     */
    public boolean batchSaveConLog(String[] quotezIds, String[] quoteIds, String busType, String logType, String msg,
            String userCode, String userName) {
        String[] msgs = { msg };
        return batchSaveConLog(quotezIds, quoteIds, busType, logType, msgs, userCode, userName);
    }
    /**
     * 查询满足quoteIds的日志记录数据
     * 
     * @param quotezIds
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConLog> listConLog(String[] quotezIds, String[] quoteIds) {
        return listConLog(quotezIds, quoteIds, null);
    }
    /**
     * 查询满足ids的日志记录数据
     * 
     * @param ids
     *            外部引用ID或者外部主引用ID 只要一个满足就能查询处理
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConLog> listConLog(String[] ids) {
        return listConLog(null, null, ids);
    }
    /**
     * 查询满足quoteIds的日志记录数据
     * 
     * @param quotezIds
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @param ids
     *            外部引用ID或者外部主引用ID 只要一个满足就能查询处理
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConLog> listConLog(String[] quotezIds, String[] quoteIds, String[] ids) {
        if (StringUtils.isNotEmpty(quotezIds) || StringUtils.isNotEmpty(quoteIds) || StringUtils.isNotEmpty(ids)) {
            List<ConLog> logList = ConLogDao.listConLog(quotezIds, quoteIds, ids);
            if (logList != null && !logList.isEmpty()) {
                // todo 反射注入修改
            }
            return logList;
        }
        return new ArrayList<ConLog>();
    }
    /**
     * 查询quoteId的日志记录数据
     * 
     * @param quotezIds
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConLog> listConLog(String quotezId, String quoteId) {
        String[] quotezIds = null;
        String[] quoteIds = null;
        if (StringUtils.isNotEmpty(quotezId)) {
            quotezIds = new String[] { quotezId };
        }
        if (StringUtils.isNotEmpty(quoteId)) {
            quoteIds = new String[] { quoteId };
        }
        return this.listConLog(quotezIds, quoteIds);
    }
    /**
     * 查询quoteId的日志记录数据
     * 
     * @param quotezIds
     *            外部主引用ID 一般为合同管理从表ID
     * @param quoteIds
     *            外部引用ID 一般为合同管理流水表ID
     * @return
     */
    @Transactional(readOnly = true)
    public void initLogViewInfo(String ids, Model model) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idsArr = StringUtils.split(ids, ",");
            List<ConLog> logList = listConLog(idsArr);
            model.addAttribute("logList", logList);
        }
    }
}
