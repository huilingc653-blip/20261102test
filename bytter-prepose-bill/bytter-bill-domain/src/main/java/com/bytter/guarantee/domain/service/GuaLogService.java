package com.bytter.guarantee.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.MapUtils;
import com.bytter.guarantee.domain.dao.GuaLogDao;
import com.bytter.guarantee.domain.entity.GuaLog;
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
 * @author fusb
 * @version 1.0
 * @Title GuaLogService
 * @Description 担保日志Service
 * @date 2018年12月29日
 */
@Service("guaLogService")
@Transactional
public class GuaLogService extends BaseService<GuaLog, String> {
    @Autowired
    GuaLogDao guaLogDao;
    @DubboReference(check = false)
    private IDictService iDictService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IUserService userService;
    /**
     * 新增担保日志
     *
     * @param guaId
     *            担保id
     * @param money
     *            业务金额
     * @param messageType
     *            日志类型
     * @param message
     *            日志信息
     * @param user
     *            业务操作员
     */
    public void saveGuaLog(String guaId, BigDecimal money, String messageType, String message, ICurrentUser user) {
        GuaLog guaLog = new GuaLog();
        guaLog.setGuaId(guaId);
        guaLog.setCreateBy(user.getLoginName());
        guaLog.setCreateByName(user.getUsername());
        guaLog.setMsgType(messageType);
        guaLog.setMessage(message);
        guaLog.setMoney(money);
        guaLog.setCreateDate(DateUtils.getDate());
        this.saveOrUpdate(guaLog);
    }
    /**
     * 加载日志列表信息
     *
     * @param guaId
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listGuaLog(String guaId, DataTablesInput input) {
        DataTablesOutput dataTablesOutput = guaLogDao.listGuaLog(guaId, input);
        List<GuaLog> logList = dataTablesOutput.getData();
        if (EmptyUtils.isEmptyList(logList)) {
            return dataTablesOutput;
        }
        Map<String, DictDTO> dictMap = new HashMap<String, DictDTO>();
        String sql = "select d.ID,d.TYPE_CODE as code,d.TYPE_NAME as name from sys_dict_group g,sys_dict d where g.id=d.type_group_id and g.type_group_code='cms.messageType' ";
        List<Map<String, Object>> list = iDictService.getExtendTableData(sql);
        for (Map<String, Object> map : list) {
            DictDTO dto = new DictDTO();
            // 注意：Oracle 返回的 Map key 会自动转为大写（如 CODE/NAME），MySQL 保持原样。
            // 使用大小写不敏感的取法 + 非空判断，避免不同数据库下 key 大小写不一致导致的 NPE。
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
        for (GuaLog guaLog : logList) {
            DictDTO dictDTO = dictMap.get(guaLog.getMessage());
            if (null != dictDTO) {
                guaLog.setMessage(dictDTO.getCode());
            }
        }
        List<TaskResponseDTO> taskResponseDTOS = workFlowService.listAllCommentsByBusinessId(guaId);
        if (!EmptyUtils.isEmptyList(taskResponseDTOS)) {
            for (int i = 0; i < taskResponseDTOS.size(); i++) {
                TaskResponseDTO responseDTO = taskResponseDTOS.get(i);
                GuaLog nisMessage = new GuaLog();
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
        List<GuaLog> allList = new ArrayList<>();
        if (!EmptyUtils.isEmpty(logList)) {
            allList.add(logList.get(0));
            out: for (int i = 0; i < logList.size(); i++) {
                GuaLog p = logList.get(i);
                for (GuaLog a : allList) {
                    if (p.getCreateBy().equals(a.getCreateBy()) && p.getMsgType().equals(a.getMsgType())) {
                        continue out;
                    }
                }
                allList.add(p);
            }
        }
        Collections.sort(allList, new Comparator<GuaLog>() {
            @Override
            public int compare(GuaLog msg1, GuaLog msg2) {
                return msg2.getCreateDate().compareTo(msg1.getCreateDate());
            }
        });
        dataTablesOutput.setData(allList);
        dataTablesOutput.setRecordsTotal(allList.size());
        dataTablesOutput.setRecordsFiltered(allList.size());
        return dataTablesOutput;
    }
    /**
     * 根据保证金id,从日志表FC_BOND_LOG中得到相应的操作信息
     *
     * @param guaId
     * @return
     */
    @Transactional(readOnly = true)
    public List<GuaLog> listOpMessage(String guaId) {
        return guaLogDao.getListOpMesageByBondId(guaId);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(GuaLog entity) {
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
    public void updateEntity(GuaLog entity) {
        super.updateEntity(entity);
    }
    /**
     * 新增对象
     *
     * @param entity
     */
    public void save(GuaLog entity) {
        super.saveEntity(entity);
    }
    /**
     * 保存or新增
     *
     * @param entity
     */
    public void saveOrUpdate(GuaLog entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<GuaLog, String> getEntityDao() {
        return guaLogDao;
    }
}
