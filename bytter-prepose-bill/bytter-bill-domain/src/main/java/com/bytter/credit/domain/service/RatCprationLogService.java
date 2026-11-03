package com.bytter.credit.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.MapUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.dao.RatCprationLogDao;
import com.bytter.credit.domain.entity.RatProcessMsg;
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

import java.math.BigDecimal;
import java.util.*;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCprationLogService
 * @Description:授信日志Service
 * @date 2018年11月24日 上午9:14:43
 */
@Service("RatCprationLogService")
@Transactional
public class RatCprationLogService extends BaseService<RatProcessMsg, String> {
    @Autowired
    private RatCprationLogDao ratLogDao;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private IWorkFlowService workFlowService;
    /**
     * 新增授信操作日志
     * 
     * @param rationId
     *            授信业务id
     * @param money
     *            金额
     * @param messageType
     *            日志类型
     * @param message
     *            日志内容
     * @return boolean
     */
    public boolean saveRatProcessMessge(String rationId, BigDecimal money, Integer messageType, String message,
            ICurrentUser user) {
        String msgType = messageType.toString();
        RatProcessMsg ratLog = new RatProcessMsg();
        ratLog.setRationId(rationId);
        ratLog.setMsgType(msgType);
        ratLog.setMessage(message);
        ratLog.setMoney(money);
        ratLog.setCreateBy(user.getLoginName());
        ratLog.setCreateByName(user.getUsername());
        ratLog.setCreateDate(DateUtils.getDate());
        return saveRatProcessMsg(ratLog);
    }
    /**
     * 加载日志列表信息
     * 
     * @param rationId
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listRationMsg(String rationId, DataTablesInput input) {
        DataTablesOutput dataTablesOutput = ratLogDao.listMsgData(rationId, input);
        List<RatProcessMsg> logList = dataTablesOutput.getData();
        Map<String, DictDTO> dictMap = new HashMap<String, DictDTO>();
        String sql = "select d.id,d.type_code as code,d.type_name as name from sys_dict_group g,sys_dict d where g.id=d.type_group_id and g.type_group_code='cms.messageType' ";
        List<Map<String, Object>> list = dictService.getExtendTableData(sql);
        for (Map<String, Object> map : list) {
            DictDTO dto = new DictDTO();
            // Oracle 返回大写 key，MySQL 保持原样；用大小写不敏感取值避免 NPE
            Object idVal = MapUtils.getMapValueIgnoreCase(map, "ID");
            Object codeVal = MapUtils.getMapValueIgnoreCase(map, "code");
            Object nameVal = MapUtils.getMapValueIgnoreCase(map, "name");
            if (idVal == null || codeVal == null || nameVal == null) {
                continue;
            }
            dto.setId(idVal.toString());
            dto.setCode(codeVal.toString());
            dto.setName(nameVal.toString());
            dictMap.put(nameVal.toString(), dto);
        }
        for (RatProcessMsg log : logList) {
            DictDTO dictDTO = dictMap.get(log.getMessage());
            if (null != dictDTO) {
                log.setMessage(dictDTO.getCode());
            }
        }
        List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(rationId);
        if (!EmptyUtils.isEmptyList(taskResponseDTOS)) {
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
                RatProcessMsg nisMessage = new RatProcessMsg();
                Integer messageType = 5;
                String message = "24";
                if (!"1".equals(responseDTO.getTaskPassFlag()) && !"2".equals(responseDTO.getTaskPassFlag())
                        && !"3".equals(responseDTO.getTaskPassFlag()) && !"4".equals(responseDTO.getTaskPassFlag())) {
                    continue;
                }
                if ("3".equals(responseDTO.getTaskPassFlag()) || "4".equals(responseDTO.getTaskPassFlag())) {
                    messageType = 4;
                    message = "25";
                } else if ("2".equals(responseDTO.getTaskPassFlag())) {
                    messageType = 26;
                }
                nisMessage.setMsgType(messageType.toString());
                nisMessage.setMessage(message);
                nisMessage.setCreateDate(responseDTO.getEndTime());
                UserDTO user = userService.getByUserId(responseDTO.getUserId());
                if (user != null) {
                    nisMessage.setCreateBy(user.getLoginName());
                    nisMessage.setCreateByName(responseDTO.getUserName());
                }
                logList.add(nisMessage);
            }
        }
        List<RatProcessMsg> allList = new ArrayList<>();
        if (!EmptyUtils.isEmpty(logList)) {
            allList.add(logList.get(0));
            out: for (int i = 0; i < logList.size(); i++) {
                RatProcessMsg p = logList.get(i);
                for (RatProcessMsg a : allList) {
                    if (StringUtils.isNotEmpty(p.getCreateBy()) && p.getCreateBy().equals(a.getCreateBy())
                            && p.getMsgType().equals(a.getMsgType())) {
                        continue out;
                    }
                }
                allList.add(p);
            }
        }
        Collections.sort(allList, new Comparator<RatProcessMsg>() {
            @Override
            public int compare(RatProcessMsg msg1, RatProcessMsg msg2) {
                return msg2.getCreateDate().compareTo(msg1.getCreateDate());
            }
        });
        dataTablesOutput.setData(allList);
        dataTablesOutput.setRecordsTotal(allList.size());
        dataTablesOutput.setRecordsFiltered(allList.size());
        return dataTablesOutput;
    }
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatProcessMsg entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除对象
     * 
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 更新新对象
     * 
     * @param entity
     */
    public void updateEntity(RatProcessMsg entity) {
        super.updateEntity(entity);
    }
    /**
     * 新增对象
     * 
     * @param entity
     */
    public void save(RatProcessMsg entity) {
        super.saveEntity(entity);
    }
    /**
     * 保存or新增
     * 
     * @param entity
     */
    public void saveOrUpdate(RatProcessMsg entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 新增授信操作日志
     * 
     * @param entity
     * @return boolean
     */
    public boolean saveRatProcessMsg(RatProcessMsg entity) {
        if (entity == null || StringUtils.isAllBlank(entity.getRationId(), entity.getMessage())) {
            return false;
        }
        this.save(entity);
        return true;
    }
    @Override
    public BaseDao<RatProcessMsg, String> getEntityDao() {
        return ratLogDao;
    }
}
